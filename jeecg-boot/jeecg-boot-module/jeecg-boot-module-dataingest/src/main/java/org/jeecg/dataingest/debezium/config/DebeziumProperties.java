package org.jeecg.dataingest.debezium.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Debezium 配置属性类
 * 支持标准的 debezium 前缀配置
 * @Description: Debezium 配置属性类
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "debezium")
public class DebeziumProperties {
    
    /**
     * 是否启用 Debezium 功能（总开关）
     */
    private boolean enabled = true;
    
    /**
     * 集群配置
     */
    private ClusterConfig cluster = new ClusterConfig();
    
    /**
     * 存储配置
     */
    private StorageConfig storage = new StorageConfig();
    
    /**
     * 处理配置
     */
    private ProcessingConfig processing = new ProcessingConfig();
    
    @Data
    public static class ClusterConfig {
        /**
         * 是否启用集群模式
         */
        private boolean enabled = true;
        
        /**
         * 节点ID（默认会自动生成）
         */
        private String nodeId;
        
        /**
         * 心跳间隔（秒）
         */
        private int heartbeatInterval = 30;
        
        /**
         * 任务分配间隔（秒）
         */
        private int assignmentInterval = 60;
        
        /**
         * 节点超时时间（秒）
         */
        private int nodeTimeout = 180;
    }
    
    @Data
    public static class StorageConfig {
        /**
         * Offset存储路径
         */
        private String offsetPath = "/tmp/debezium/offsets";
        
        /**
         * History存储路径
         */
        private String historyPath = "/tmp/debezium/history";
    }
    
    @Data
    public static class ProcessingConfig {
        /**
         * 批量处理大小
         */
        private int batchSize = 1000;
        
        /**
         * 处理间隔（毫秒）
         */
        private int processInterval = 5000;
        
        /**
         * 最大重试次数
         */
        private int maxRetry = 3;
    }
}
