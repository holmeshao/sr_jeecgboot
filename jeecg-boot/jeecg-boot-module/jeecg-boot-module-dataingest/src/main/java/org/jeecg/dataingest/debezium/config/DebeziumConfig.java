package org.jeecg.dataingest.debezium.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Debezium配置类
 * @Description: Debezium配置类
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "org-jeecg-dataingest-debezium")
public class DebeziumConfig {
    
    /**SQLServer连接配置*/
    private SqlServerConfig sqlserver;
    
    /**PostgreSQL目标配置*/
    private PostgresConfig postgres;
    
    /**通用配置*/
    private CommonConfig common;
    
    @Data
    public static class SqlServerConfig {
        private String hostname;
        private Integer port;
        private String database;
        private String username;
        private String password;
        private String serverName;
        private String tableIncludeList;
        private String tableExcludeList;
        private String columnIncludeList;
        private String columnExcludeList;
    }
    
    @Data
    public static class PostgresConfig {
        private String url;
        private String username;
        private String password;
        private String schema;
        private String tablePrefix = "ods_";
    }
    
    @Data
    public static class CommonConfig {
        private String snapshotMode = "initial";
        private String offsetStorage = "org.apache.kafka.connect.storage.FileOffsetBackingStore";
        private String offsetStorageFileName = "offset.dat";
        private Long offsetFlushIntervalMs = 60000L;
        private String name = "sqlserver-connector";
        private String databaseHistory = "io.debezium.relational.history.FileDatabaseHistory";
        private String databaseHistoryFile = "dbhistory.dat";
    }
} 