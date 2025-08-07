package org.jeecg.dataingest.debezium;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.debezium.config.Configuration;
import io.debezium.connector.mysql.MySqlConnector;
import io.debezium.connector.postgresql.PostgresConnector;
import io.debezium.connector.sqlserver.SqlServerConnector;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 支持集群模式的 Debezium 任务管理器
 * @Description: 基于Redis的分布式CDC任务管理器，支持集群部署、负载均衡和故障转移
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V2.0
 */
@Slf4j
@Component
public class DebeziumClusterTaskManager {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${server.port:8080}")
    private String serverPort;

    // 本地运行的任务实例（不能序列化到Redis）
    private final ConcurrentHashMap<String, DebeziumEngine<ChangeEvent<String, String>>> localEngines = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ExecutorService> localExecutors = new ConcurrentHashMap<>();

    // Redis Key 前缀
    private static final String TASK_CONFIG_PREFIX = "debezium:task:config:";
    private static final String TASK_STATUS_PREFIX = "debezium:task:status:";
    private static final String TASK_ASSIGNMENT_PREFIX = "debezium:task:assignment:";
    private static final String NODE_HEARTBEAT_PREFIX = "debezium:node:heartbeat:";
    private static final String CLUSTER_TASKS_SET = "debezium:cluster:tasks";

    // 当前节点信息
    private String nodeId;
    private ScheduledExecutorService scheduledExecutor;

    @PostConstruct
    public void init() {
        try {
            // 生成节点ID：IP:PORT
            String localIp = InetAddress.getLocalHost().getHostAddress();
            this.nodeId = localIp + ":" + serverPort;
            
            log.info("初始化Debezium集群任务管理器，节点ID: {}", nodeId);
            
            // 启动心跳检测
            this.scheduledExecutor = Executors.newScheduledThreadPool(2);
            startHeartbeat();
            startTaskMonitor();
            
        } catch (Exception e) {
            log.error("初始化集群任务管理器失败", e);
            throw new RuntimeException("初始化失败", e);
        }
    }

    /**
     * 启动或重启CDC任务（集群模式）
     */
    public void startOrRestartTask(String taskId, DebeziumTaskConfig config) {
        log.info("集群模式启动/重启CDC任务: {} - {}", taskId, config.getTaskName());
        
        try {
            // 1. 保存任务配置到Redis
            saveTaskConfig(taskId, config);
            
            // 2. 尝试获取任务锁（分布式锁）
            if (tryAcquireTaskLock(taskId)) {
                // 3. 启动本地任务
                startLocalTask(taskId, config);
                
                // 4. 注册任务分配关系
                assignTaskToNode(taskId, nodeId);
                
                log.info("成功获取任务锁并启动任务: {} on node: {}", taskId, nodeId);
            } else {
                log.info("任务 {} 已被其他节点接管，跳过启动", taskId);
            }
            
        } catch (Exception e) {
            log.error("集群模式启动CDC任务失败: {}", taskId, e);
            releaseTaskLock(taskId);
            throw new RuntimeException("启动CDC任务失败: " + e.getMessage(), e);
        }
    }

    /**
     * 停止CDC任务（集群模式）
     */
    public void stopTask(String taskId) {
        log.info("集群模式停止CDC任务: {}", taskId);
        
        try {
            // 1. 停止本地任务
            stopLocalTask(taskId);
            
            // 2. 释放任务锁
            releaseTaskLock(taskId);
            
            // 3. 清理任务分配关系
            unassignTaskFromNode(taskId);
            
            log.info("集群模式停止任务成功: {}", taskId);
            
        } catch (Exception e) {
            log.error("集群模式停止CDC任务失败: {}", taskId, e);
            throw new RuntimeException("停止CDC任务失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取集群中的任务状态
     */
    public JSONObject getClusterTaskStatus(String taskId) {
        JSONObject result = new JSONObject();
        
        // 任务配置
        DebeziumTaskConfig config = getTaskConfig(taskId);
        if (config != null) {
            result.put("config", config);
        }
        
        // 任务状态
        String statusJson = redisTemplate.opsForValue().get(TASK_STATUS_PREFIX + taskId);
        if (statusJson != null) {
            result.put("status", JSON.parseObject(statusJson));
        }
        
        // 任务分配信息
        String assignedNode = getTaskAssignedNode(taskId);
        result.put("assignedNode", assignedNode);
        
        // 本地运行状态
        result.put("runningOnCurrentNode", localEngines.containsKey(taskId));
        
        return result;
    }

    /**
     * 获取集群任务列表
     */
    public JSONArray getClusterTaskList() {
        JSONArray taskList = new JSONArray();
        
        Set<String> taskIds = redisTemplate.opsForSet().members(CLUSTER_TASKS_SET);
        if (taskIds != null) {
            for (String taskId : taskIds) {
                JSONObject taskInfo = getClusterTaskStatus(taskId);
                taskInfo.put("taskId", taskId);
                taskList.add(taskInfo);
            }
        }
        
        return taskList;
    }

    /**
     * 启动本地任务
     */
    private void startLocalTask(String taskId, DebeziumTaskConfig config) {
        // 如果本地已经运行，先停止
        if (localEngines.containsKey(taskId)) {
            stopLocalTask(taskId);
        }

        // 构建Debezium配置
        Configuration debeziumConfig = buildDebeziumConfiguration(taskId, config);
        
        // 创建并启动Debezium引擎
        DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                .using(debeziumConfig.asProperties())
                .notifying(record -> handleChangeEvent(taskId, record, config))
                .build();

        ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "debezium-task-" + taskId + "-" + nodeId);
            t.setDaemon(true);
            return t;
        });

        // 保存到本地内存
        localEngines.put(taskId, engine);
        localExecutors.put(taskId, executor);

        // 启动引擎
        executor.submit(engine);

        // 更新状态到Redis
        updateTaskStatus(taskId, "RUNNING", "任务在节点 " + nodeId + " 上启动成功", nodeId);
        
        log.info("本地CDC任务启动成功: {} on {}", taskId, nodeId);
    }

    /**
     * 停止本地任务
     */
    private void stopLocalTask(String taskId) {
        try {
            DebeziumEngine<ChangeEvent<String, String>> engine = localEngines.remove(taskId);
            ExecutorService executor = localExecutors.remove(taskId);

            if (engine != null) {
                engine.close();
                log.info("本地Debezium引擎已关闭: {} on {}", taskId, nodeId);
            }

            if (executor != null) {
                executor.shutdown();
                log.info("本地执行器已关闭: {} on {}", taskId, nodeId);
            }

            updateTaskStatus(taskId, "STOPPED", "任务在节点 " + nodeId + " 上已停止", nodeId);
            
        } catch (Exception e) {
            log.error("停止本地CDC任务失败: {} on {}", taskId, nodeId, e);
            updateTaskStatus(taskId, "ERROR", "停止失败: " + e.getMessage(), nodeId);
            throw new RuntimeException("停止本地CDC任务失败: " + e.getMessage(), e);
        }
    }

    /**
     * 尝试获取任务锁（分布式锁）
     */
    private boolean tryAcquireTaskLock(String taskId) {
        String lockKey = "debezium:lock:task:" + taskId;
        String lockValue = nodeId + ":" + System.currentTimeMillis();
        
        // 使用Redis SET NX EX 实现分布式锁，锁超时30秒
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, Duration.ofSeconds(30));
        
        if (Boolean.TRUE.equals(acquired)) {
            log.debug("成功获取任务锁: {} by {}", taskId, nodeId);
            return true;
        } else {
            log.debug("获取任务锁失败: {} by {}", taskId, nodeId);
            return false;
        }
    }

    /**
     * 释放任务锁
     */
    private void releaseTaskLock(String taskId) {
        String lockKey = "debezium:lock:task:" + taskId;
        try {
            redisTemplate.delete(lockKey);
            log.debug("释放任务锁: {} by {}", taskId, nodeId);
        } catch (Exception e) {
            log.warn("释放任务锁失败: {} by {}", taskId, nodeId, e);
        }
    }

    /**
     * 保存任务配置到Redis
     */
    private void saveTaskConfig(String taskId, DebeziumTaskConfig config) {
        String configJson = JSON.toJSONString(config);
        redisTemplate.opsForValue().set(TASK_CONFIG_PREFIX + taskId, configJson);
        redisTemplate.opsForSet().add(CLUSTER_TASKS_SET, taskId);
        log.debug("保存任务配置到Redis: {}", taskId);
    }

    /**
     * 从Redis获取任务配置
     */
    private DebeziumTaskConfig getTaskConfig(String taskId) {
        String configJson = redisTemplate.opsForValue().get(TASK_CONFIG_PREFIX + taskId);
        if (configJson != null) {
            return JSON.parseObject(configJson, DebeziumTaskConfig.class);
        }
        return null;
    }

    /**
     * 分配任务到节点
     */
    private void assignTaskToNode(String taskId, String nodeId) {
        redisTemplate.opsForValue().set(TASK_ASSIGNMENT_PREFIX + taskId, nodeId);
        log.debug("分配任务到节点: {} -> {}", taskId, nodeId);
    }

    /**
     * 取消任务分配
     */
    private void unassignTaskFromNode(String taskId) {
        redisTemplate.delete(TASK_ASSIGNMENT_PREFIX + taskId);
        log.debug("取消任务分配: {}", taskId);
    }

    /**
     * 获取任务分配的节点
     */
    private String getTaskAssignedNode(String taskId) {
        return redisTemplate.opsForValue().get(TASK_ASSIGNMENT_PREFIX + taskId);
    }

    /**
     * 启动心跳检测
     */
    private void startHeartbeat() {
        scheduledExecutor.scheduleWithFixedDelay(() -> {
            try {
                String heartbeatKey = NODE_HEARTBEAT_PREFIX + nodeId;
                JSONObject heartbeat = new JSONObject();
                heartbeat.put("nodeId", nodeId);
                heartbeat.put("timestamp", System.currentTimeMillis());
                heartbeat.put("localTaskCount", localEngines.size());
                
                // 心跳过期时间30秒
                redisTemplate.opsForValue().set(heartbeatKey, heartbeat.toJSONString(), Duration.ofSeconds(30));
                
            } catch (Exception e) {
                log.warn("发送心跳失败: {}", nodeId, e);
            }
        }, 0, 10, TimeUnit.SECONDS); // 每10秒发送一次心跳
    }

    /**
     * 启动任务监控（故障转移）
     */
    private void startTaskMonitor() {
        scheduledExecutor.scheduleWithFixedDelay(() -> {
            try {
                checkAndRecoverOrphanedTasks();
            } catch (Exception e) {
                log.warn("任务监控异常", e);
            }
        }, 30, 60, TimeUnit.SECONDS); // 每60秒检查一次
    }

    /**
     * 检查并恢复孤儿任务
     */
    private void checkAndRecoverOrphanedTasks() {
        Set<String> allTasks = redisTemplate.opsForSet().members(CLUSTER_TASKS_SET);
        if (allTasks == null) return;

        for (String taskId : allTasks) {
            try {
                String assignedNode = getTaskAssignedNode(taskId);
                if (assignedNode != null && !isNodeAlive(assignedNode)) {
                    log.warn("检测到孤儿任务，尝试恢复: {} 原节点: {}", taskId, assignedNode);
                    
                    // 尝试接管任务
                    DebeziumTaskConfig config = getTaskConfig(taskId);
                    if (config != null && tryAcquireTaskLock(taskId)) {
                        startLocalTask(taskId, config);
                        assignTaskToNode(taskId, nodeId);
                        log.info("成功接管孤儿任务: {} 从 {} 到 {}", taskId, assignedNode, nodeId);
                    }
                }
            } catch (Exception e) {
                log.warn("恢复孤儿任务失败: {}", taskId, e);
            }
        }
    }

    /**
     * 检查节点是否存活
     */
    private boolean isNodeAlive(String nodeId) {
        String heartbeatKey = NODE_HEARTBEAT_PREFIX + nodeId;
        String heartbeatJson = redisTemplate.opsForValue().get(heartbeatKey);
        
        if (heartbeatJson == null) {
            return false;
        }
        
        try {
            JSONObject heartbeat = JSON.parseObject(heartbeatJson);
            long timestamp = heartbeat.getLongValue("timestamp");
            long currentTime = System.currentTimeMillis();
            
            // 如果心跳超过60秒没更新，认为节点已死亡
            return (currentTime - timestamp) < 60000;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 更新任务状态
     */
    private void updateTaskStatus(String taskId, String status, String message, String nodeId) {
        JSONObject statusInfo = new JSONObject();
        statusInfo.put("taskId", taskId);
        statusInfo.put("status", status);
        statusInfo.put("message", message);
        statusInfo.put("nodeId", nodeId);
        statusInfo.put("updateTime", System.currentTimeMillis());
        
        redisTemplate.opsForValue().set(TASK_STATUS_PREFIX + taskId, statusInfo.toJSONString());
    }

    /**
     * 处理CDC变更事件
     */
    private void handleChangeEvent(String taskId, ChangeEvent<String, String> record, DebeziumTaskConfig config) {
        try {
            if (record.value() != null) {
                JSONObject changeEvent = JSON.parseObject(record.value());
                log.debug("收到CDC事件: taskId={}, node={}, event={}", taskId, nodeId, changeEvent);
                
                // 更新统计信息到Redis
                updateStatistics(taskId, true);
                
                // 这里可以添加数据处理逻辑
                // processChangeEvent(taskId, changeEvent, config);
            }
        } catch (Exception e) {
            log.error("处理CDC事件失败: taskId={}, node={}", taskId, nodeId, e);
            updateStatistics(taskId, false);
        }
    }

    /**
     * 更新统计信息到Redis
     */
    private void updateStatistics(String taskId, boolean success) {
        String key = "debezium:statistics:" + taskId;
        String statisticsJson = redisTemplate.opsForValue().get(key);
        
        JSONObject stats;
        if (statisticsJson != null) {
            stats = JSON.parseObject(statisticsJson);
        } else {
            stats = new JSONObject();
            stats.put("taskId", taskId);
            stats.put("processedCount", 0);
            stats.put("errorCount", 0);
        }
        
        if (success) {
            stats.put("processedCount", stats.getIntValue("processedCount") + 1);
        } else {
            stats.put("errorCount", stats.getIntValue("errorCount") + 1);
        }
        stats.put("lastProcessTime", System.currentTimeMillis());
        stats.put("lastProcessNode", nodeId);
        
        redisTemplate.opsForValue().set(key, stats.toJSONString());
    }

    // 构建Debezium配置的方法保持不变，从原来的代码复制过来
    private Configuration buildDebeziumConfiguration(String taskId, DebeziumTaskConfig config) {
        JSONArray dataSourceConfigs = config.getDataSourceConfigs();
        if (dataSourceConfigs == null || dataSourceConfigs.isEmpty()) {
            throw new IllegalArgumentException("数据源配置不能为空");
        }

        JSONObject dataSourceConfig = dataSourceConfigs.getJSONObject(0);
        JSONObject connectionConfig = JSON.parseObject(dataSourceConfig.getString("connectionConfig"));

        Configuration.Builder builder = Configuration.create();
        
        // 基础配置 - 集群模式下使用节点特定的路径
        builder.with("name", "debezium-connector-" + taskId + "-" + nodeId)
               .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
               .with("offset.storage.file.filename", "/tmp/debezium/" + nodeId + "/offset-" + taskId + ".dat")
               .with("offset.flush.interval.ms", 60000)
               .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
               .with("database.history.file.filename", "/tmp/debezium/" + nodeId + "/dbhistory-" + taskId + ".dat");

        // 根据任务类型配置连接器
        switch (config.getTaskType().toUpperCase()) {
            case "SQLSERVER_CDC":
                configureSqlServerConnector(builder, connectionConfig, config);
                break;
            case "MYSQL_CDC":
                configureMySqlConnector(builder, connectionConfig, config);
                break;
            case "POSTGRESQL_CDC":
                configurePostgresConnector(builder, connectionConfig, config);
                break;
            default:
                throw new IllegalArgumentException("不支持的任务类型: " + config.getTaskType());
        }

        return builder.build();
    }

    // 其他配置方法保持不变...
    private void configureSqlServerConnector(Configuration.Builder builder, JSONObject connectionConfig, DebeziumTaskConfig config) {
        builder.with("connector.class", SqlServerConnector.class.getName())
               .with("database.hostname", getConnectionProperty(connectionConfig, "hostname", "localhost"))
               .with("database.port", getConnectionProperty(connectionConfig, "port", "1433"))
               .with("database.user", getConnectionProperty(connectionConfig, "username", "sa"))
               .with("database.password", getConnectionProperty(connectionConfig, "password", ""))
               .with("database.dbname", getConnectionProperty(connectionConfig, "database", ""))
               .with("database.server.name", "server-" + config.getTaskId() + "-" + nodeId)
               .with("table.include.list", buildTableIncludeList(config))
               .with("snapshot.mode", "initial");
    }

    private void configureMySqlConnector(Configuration.Builder builder, JSONObject connectionConfig, DebeziumTaskConfig config) {
        builder.with("connector.class", MySqlConnector.class.getName())
               .with("database.hostname", getConnectionProperty(connectionConfig, "hostname", "localhost"))
               .with("database.port", getConnectionProperty(connectionConfig, "port", "3306"))
               .with("database.user", getConnectionProperty(connectionConfig, "username", "root"))
               .with("database.password", getConnectionProperty(connectionConfig, "password", ""))
               .with("database.server.id", String.valueOf(Math.abs(nodeId.hashCode()) % 65535 + 1))
               .with("database.server.name", "mysql-server-" + config.getTaskId() + "-" + nodeId)
               .with("table.include.list", buildTableIncludeList(config))
               .with("snapshot.mode", "initial");
    }

    private void configurePostgresConnector(Configuration.Builder builder, JSONObject connectionConfig, DebeziumTaskConfig config) {
        builder.with("connector.class", PostgresConnector.class.getName())
               .with("database.hostname", getConnectionProperty(connectionConfig, "hostname", "localhost"))
               .with("database.port", getConnectionProperty(connectionConfig, "port", "5432"))
               .with("database.user", getConnectionProperty(connectionConfig, "username", "postgres"))
               .with("database.password", getConnectionProperty(connectionConfig, "password", ""))
               .with("database.dbname", getConnectionProperty(connectionConfig, "database", ""))
               .with("database.server.name", "postgres-server-" + config.getTaskId() + "-" + nodeId)
               .with("table.include.list", buildTableIncludeList(config))
               .with("plugin.name", "pgoutput")
               .with("snapshot.mode", "initial");
    }

    private String getConnectionProperty(JSONObject connectionConfig, String key, String defaultValue) {
        return connectionConfig.getString(key) != null ? connectionConfig.getString(key) : defaultValue;
    }

    private String buildTableIncludeList(DebeziumTaskConfig config) {
        JSONArray cdcTables = config.getCdcTables();
        if (cdcTables == null || cdcTables.isEmpty()) {
            return "";
        }

        StringBuilder includeList = new StringBuilder();
        for (int i = 0; i < cdcTables.size(); i++) {
            JSONObject table = cdcTables.getJSONObject(i);
            String sourceTableName = table.getString("sourceTableName");
            if (sourceTableName != null && !sourceTableName.trim().isEmpty()) {
                if (includeList.length() > 0) {
                    includeList.append(",");
                }
                includeList.append(sourceTableName);
            }
        }
        return includeList.toString();
    }

    /**
     * 销毁所有任务
     */
    @PreDestroy
    public void destroy() {
        log.info("销毁集群Debezium任务管理器，停止所有本地任务: {}", nodeId);
        
        // 停止调度器
        if (scheduledExecutor != null) {
            scheduledExecutor.shutdown();
        }
        
        // 停止所有本地任务
        for (String taskId : localEngines.keySet()) {
            try {
                stopLocalTask(taskId);
                releaseTaskLock(taskId);
                unassignTaskFromNode(taskId);
            } catch (Exception e) {
                log.error("停止本地任务失败: {} on {}", taskId, nodeId, e);
            }
        }
        
        // 清理心跳
        try {
            redisTemplate.delete(NODE_HEARTBEAT_PREFIX + nodeId);
        } catch (Exception e) {
            log.warn("清理心跳失败: {}", nodeId, e);
        }
        
        log.info("集群Debezium任务管理器销毁完成: {}", nodeId);
    }

    /**
     * CDC任务配置类
     */
    public static class DebeziumTaskConfig {
        private String taskId;
        private String taskName;
        private String taskType;
        private Integer status;
        private String targetTableNamePre;
        private String taskConfig;
        private JSONArray dataSourceConfigs;
        private JSONArray fieldMappings;
        private JSONArray cdcTables;

        // Getters and Setters
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        
        public String getTaskName() { return taskName; }
        public void setTaskName(String taskName) { this.taskName = taskName; }
        
        public String getTaskType() { return taskType; }
        public void setTaskType(String taskType) { this.taskType = taskType; }
        
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
        
        public String getTargetTableNamePre() { return targetTableNamePre; }
        public void setTargetTableNamePre(String targetTableNamePre) { this.targetTableNamePre = targetTableNamePre; }
        
        public String getTaskConfig() { return taskConfig; }
        public void setTaskConfig(String taskConfig) { this.taskConfig = taskConfig; }
        
        public JSONArray getDataSourceConfigs() { return dataSourceConfigs; }
        public void setDataSourceConfigs(JSONArray dataSourceConfigs) { this.dataSourceConfigs = dataSourceConfigs; }
        
        public JSONArray getFieldMappings() { return fieldMappings; }
        public void setFieldMappings(JSONArray fieldMappings) { this.fieldMappings = fieldMappings; }
        
        public JSONArray getCdcTables() { return cdcTables; }
        public void setCdcTables(JSONArray cdcTables) { this.cdcTables = cdcTables; }
    }
} 