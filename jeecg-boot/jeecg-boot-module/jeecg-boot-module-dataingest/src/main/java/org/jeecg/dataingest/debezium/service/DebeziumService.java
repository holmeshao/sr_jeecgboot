package org.jeecg.dataingest.debezium.service;

import io.debezium.config.Configuration;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.dataingest.debezium.config.DebeziumConfig;
import org.jeecg.dataingest.entity.DataIngestMoudleIngestLog;
import org.jeecg.dataingest.service.IDataIngestMoudleIngestLogService;
import org.jeecg.dataingest.core.service.IPostgresWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Date;
import java.util.UUID;

/**
 * Debezium服务类
 * @Description: Debezium服务类
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Slf4j
@Service
public class DebeziumService {
    
    @Autowired
    private DebeziumConfig debeziumConfig;
    
    @Autowired
    private IDataIngestMoudleIngestLogService ingestLogService;
    
    @Autowired
    private IPostgresWriteService postgresWriteService;
    
    private DebeziumEngine<ChangeEvent<String, String>> engine;
    private ExecutorService executor;
    private ObjectMapper objectMapper = new ObjectMapper();
    
    // 当前执行的日志记录
    private DataIngestMoudleIngestLog currentLog;
    
    @PostConstruct
    public void init() {
        log.info("初始化Debezium服务");
        executor = Executors.newSingleThreadExecutor();
    }
    
    @PreDestroy
    public void destroy() {
        log.info("销毁Debezium服务");
        if (engine != null) {
            try {
                engine.close();
            } catch (Exception e) {
                log.error("关闭DebeziumEngine失败", e);
            }
        }
        if (executor != null) {
            executor.shutdown();
        }
    }
    
    /**
     * 启动CDC监听
     */
    public void startCDC() {
        startCDC("SYSTEM_CDC_TASK");
    }
    
    /**
     * 启动CDC监听
     * @param taskId 任务ID
     */
    public void startCDC(String taskId) {
        log.info("启动CDC监听，任务ID: {}", taskId);
        
        // 创建日志记录
        currentLog = createIngestLog(taskId, "CDC监听任务");
        
        try {
            java.util.Properties props = buildConfiguration().asProperties();
            engine = DebeziumEngine.create(Json.class)
                    .using(props)
                    .notifying(this::handleEvent)
                    .build();
            executor.execute(engine);
            
            log.info("CDC监听启动成功");
            updateLogStatus(1, "CDC监听启动成功", null);
        } catch (Exception e) {
            log.error("启动CDC监听失败", e);
            updateLogStatus(2, "CDC监听启动失败", e.getMessage());
        }
    }
    
    /**
     * 停止CDC监听
     */
    public void stopCDC() {
        log.info("停止CDC监听");
        if (engine != null) {
            try {
                engine.close();
                log.info("CDC监听已停止");
                updateLogStatus(3, "CDC监听已停止", null);
            } catch (Exception e) {
                log.error("关闭DebeziumEngine失败", e);
                updateLogStatus(2, "关闭DebeziumEngine失败", e.getMessage());
            }
        }
    }
    
    /**
     * 构建Debezium配置
     */
    private Configuration buildConfiguration() {

        DebeziumConfig.SqlServerConfig sqlserver = debeziumConfig.getSqlserver();
        DebeziumConfig.CommonConfig common = debeziumConfig.getCommon();
        
        return Configuration.create()
                .with("name", common.getName())
                .with("connector.class", "io.debezium.connector.sqlserver.SqlServerConnector")
                .with("database.hostname", sqlserver.getHostname())
                .with("database.port", sqlserver.getPort())
                .with("database.user", sqlserver.getUsername())
                .with("database.password", sqlserver.getPassword())
                .with("database.dbname", sqlserver.getDatabase())
                .with("database.server.name", sqlserver.getServerName())
                .with("table.include.list", sqlserver.getTableIncludeList())
                .with("table.exclude.list", sqlserver.getTableExcludeList())
                .with("column.include.list", sqlserver.getColumnIncludeList())
                .with("column.exclude.list", sqlserver.getColumnExcludeList())
                .with("snapshot.mode", common.getSnapshotMode())
                .with("offset.storage", common.getOffsetStorage())
                .with("offset.storage.file.filename", common.getOffsetStorageFileName())
                .with("offset.flush.interval.ms", common.getOffsetFlushIntervalMs())
                .with("database.history", common.getDatabaseHistory())
                .with("database.history.file.filename", common.getDatabaseHistoryFile())
                .build();
    }
    
    /**
     * 处理CDC事件
     */
    private void handleEvent(ChangeEvent<String, String> event) {
        try {
            log.info("收到CDC事件: key={}, value={}", event.key(), event.value());
            
            if (event.value() != null) {
                // 解析事件数据
                JsonNode eventData = objectMapper.readTree(event.value());
                String operation = eventData.has("op") ? eventData.get("op").asText() : "unknown";
                
                // 更新处理计数
                if (currentLog != null) {
                    int recordCount = currentLog.getRecordCount() != null ? currentLog.getRecordCount() + 1 : 1;
                    currentLog.setRecordCount(recordCount);
                    
                    // 根据操作类型处理
                    switch (operation.toLowerCase()) {
                        case "c": // create
                            handleCreateEvent(event.value(), eventData);
                            incrementSuccessCount();
                            break;
                        case "u": // update  
                            handleUpdateEvent(event.value(), eventData);
                            incrementSuccessCount();
                            break;
                        case "d": // delete
                            handleDeleteEvent(event.value(), eventData);
                            incrementSuccessCount();
                            break;
                        default:
                            log.warn("未知的CDC操作类型: {}", operation);
                            incrementFailCount();
                    }
                    
                    // 每处理100条记录更新一次日志
                    if (recordCount % 100 == 0) {
                        updateLogInDatabase();
                    }
                }
            }
            
        } catch (Exception e) {
            log.error("处理CDC事件失败", e);
            incrementFailCount();
            updateLogStatus(2, "处理CDC事件失败", e.getMessage());
        }
    }
    
    /**
     * 处理创建事件
     */
    private void handleCreateEvent(String record, JsonNode eventData) {
        log.info("处理创建事件: {}", record);
        try {
            // 提取表名和数据
            String tableName = extractTableName(eventData);
            JsonNode afterData = eventData.has("after") ? eventData.get("after") : null;
            
            if (afterData != null && tableName != null) {
                // 调用PostgreSQL写入服务
                String targetTableName = "ods_" + tableName; // 添加ODS前缀
                boolean writeResult = postgresWriteService.writeCreateData(targetTableName, afterData, currentLog.getTaskId());
                
                if (writeResult) {
                    log.info("创建事件处理完成，表: {} -> {}, 数据: {}", tableName, targetTableName, afterData.toString());
                    appendExecuteLog("创建事件处理成功: " + tableName + " -> " + targetTableName);
                } else {
                    log.error("创建事件处理失败，表: {} -> {}", tableName, targetTableName);
                    appendExecuteLog("创建事件处理失败: " + tableName + " -> " + targetTableName);
                    throw new RuntimeException("写入PostgreSQL失败");
                }
            }
        } catch (Exception e) {
            log.error("处理创建事件失败", e);
            appendExecuteLog("创建事件处理失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 处理更新事件
     */
    private void handleUpdateEvent(String record, JsonNode eventData) {
        log.info("处理更新事件: {}", record);
        try {
            // 提取表名和数据
            String tableName = extractTableName(eventData);
            JsonNode beforeData = eventData.has("before") ? eventData.get("before") : null;
            JsonNode afterData = eventData.has("after") ? eventData.get("after") : null;
            
            if (afterData != null && tableName != null) {
                // 调用PostgreSQL写入服务
                String targetTableName = "ods_" + tableName; // 添加ODS前缀
                boolean writeResult = postgresWriteService.writeUpdateData(targetTableName, beforeData, afterData, currentLog.getTaskId());
                
                if (writeResult) {
                    log.info("更新事件处理完成，表: {} -> {}, 更新前: {}, 更新后: {}", 
                            tableName, targetTableName,
                            beforeData != null ? beforeData.toString() : "null", 
                            afterData.toString());
                    appendExecuteLog("更新事件处理成功: " + tableName + " -> " + targetTableName);
                } else {
                    log.error("更新事件处理失败，表: {} -> {}", tableName, targetTableName);
                    appendExecuteLog("更新事件处理失败: " + tableName + " -> " + targetTableName);
                    throw new RuntimeException("更新PostgreSQL失败");
                }
            }
        } catch (Exception e) {
            log.error("处理更新事件失败", e);
            appendExecuteLog("更新事件处理失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 处理删除事件
     */
    private void handleDeleteEvent(String record, JsonNode eventData) {
        log.info("处理删除事件: {}", record);
        try {
            // 提取表名和数据
            String tableName = extractTableName(eventData);
            JsonNode beforeData = eventData.has("before") ? eventData.get("before") : null;
            
            if (beforeData != null && tableName != null) {
                // 调用PostgreSQL写入服务（软删除）
                String targetTableName = "ods_" + tableName; // 添加ODS前缀
                boolean writeResult = postgresWriteService.writeDeleteData(targetTableName, beforeData, currentLog.getTaskId());
                
                if (writeResult) {
                    log.info("删除事件处理完成，表: {} -> {}, 删除数据: {}", tableName, targetTableName, beforeData.toString());
                    appendExecuteLog("删除事件处理成功: " + tableName + " -> " + targetTableName);
                } else {
                    log.error("删除事件处理失败，表: {} -> {}", tableName, targetTableName);
                    appendExecuteLog("删除事件处理失败: " + tableName + " -> " + targetTableName);
                    throw new RuntimeException("软删除PostgreSQL失败");
                }
            }
        } catch (Exception e) {
            log.error("处理删除事件失败", e);
            appendExecuteLog("删除事件处理失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 提取表名
     */
    private String extractTableName(JsonNode eventData) {
        if (eventData.has("source")) {
            JsonNode source = eventData.get("source");
            if (source.has("table")) {
                return source.get("table").asText();
            }
        }
        return null;
    }
    
    /**
     * 创建日志记录
     */
    private DataIngestMoudleIngestLog createIngestLog(String taskId, String taskName) {
        DataIngestMoudleIngestLog log = new DataIngestMoudleIngestLog();
        log.setId(UUID.randomUUID().toString().replace("-", ""));
        log.setTaskId(taskId);
        log.setBatchId(UUID.randomUUID().toString().replace("-", ""));
        log.setStatus(0); // 0-开始执行
        log.setStartTime(new Date());
        log.setRecordCount(0);
        log.setSuccessCount(0);
        log.setFailCount(0);
        log.setExecuteLog("开始执行CDC任务: " + taskName);
        log.setCreateTime(new Date());
        
        try {
            ingestLogService.save(log);
            log.info("创建日志记录成功，ID: {}", log.getId());
        } catch (Exception e) {
            log.error("创建日志记录失败", e);
        }
        
        return log;
    }
    
    /**
     * 更新日志状态
     */
    private void updateLogStatus(Integer status, String message, String errorMessage) {
        if (currentLog != null) {
            currentLog.setStatus(status);
            currentLog.setEndTime(new Date());
            if (message != null) {
                appendExecuteLog(message);
            }
            if (errorMessage != null) {
                currentLog.setErrorMessage(errorMessage);
            }
            updateLogInDatabase();
        }
    }
    
    /**
     * 增加成功计数
     */
    private void incrementSuccessCount() {
        if (currentLog != null) {
            int successCount = currentLog.getSuccessCount() != null ? currentLog.getSuccessCount() + 1 : 1;
            currentLog.setSuccessCount(successCount);
        }
    }
    
    /**
     * 增加失败计数
     */
    private void incrementFailCount() {
        if (currentLog != null) {
            int failCount = currentLog.getFailCount() != null ? currentLog.getFailCount() + 1 : 1;
            currentLog.setFailCount(failCount);
        }
    }
    
    /**
     * 追加执行日志
     */
    private void appendExecuteLog(String message) {
        if (currentLog != null) {
            String currentExecuteLog = currentLog.getExecuteLog();
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String newLog = String.format("[%s] %s", timestamp, message);
            
            if (currentExecuteLog != null && !currentExecuteLog.isEmpty()) {
                currentLog.setExecuteLog(currentExecuteLog + "\n" + newLog);
            } else {
                currentLog.setExecuteLog(newLog);
            }
        }
    }
    
    /**
     * 更新数据库中的日志记录
     */
    private void updateLogInDatabase() {
        if (currentLog != null) {
            try {
                currentLog.setUpdateTime(new Date());
                ingestLogService.updateById(currentLog);
            } catch (Exception e) {
                log.error("更新日志记录失败", e);
            }
        }
    }
} 