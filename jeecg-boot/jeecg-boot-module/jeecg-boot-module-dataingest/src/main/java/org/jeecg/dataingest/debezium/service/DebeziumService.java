package org.jeecg.dataingest.debezium.service;

import io.debezium.config.Configuration;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.dataingest.debezium.config.DebeziumConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    
    private DebeziumEngine<ChangeEvent<String, String>> engine;
    private ExecutorService executor;
    
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
        log.info("启动CDC监听");
        try {
            java.util.Properties props = buildConfiguration().asProperties();
            engine = DebeziumEngine.create(Json.class)
                    .using(props)
                    .notifying(this::handleEvent)
                    .build();
            executor.execute(engine);
            log.info("CDC监听启动成功");
        } catch (Exception e) {
            log.error("启动CDC监听失败", e);
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
            } catch (Exception e) {
                log.error("关闭DebeziumEngine失败", e);
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
            // TODO: 解析event.value()为JSON并写入PostgreSQL
        } catch (Exception e) {
            log.error("处理CDC事件失败", e);
        }
    }
    
    /**
     * 处理创建事件
     */
    private void handleCreateEvent(String record, io.debezium.data.Envelope sourceRecord) {
        log.info("处理创建事件: {}", record);
        // TODO: 实现创建事件处理逻辑
    }
    
    /**
     * 处理更新事件
     */
    private void handleUpdateEvent(String record, io.debezium.data.Envelope sourceRecord) {
        log.info("处理更新事件: {}", record);
        // TODO: 实现更新事件处理逻辑
    }
    
    /**
     * 处理删除事件
     */
    private void handleDeleteEvent(String record, io.debezium.data.Envelope sourceRecord) {
        log.info("处理删除事件: {}", record);
        // TODO: 实现删除事件处理逻辑
    }
} 