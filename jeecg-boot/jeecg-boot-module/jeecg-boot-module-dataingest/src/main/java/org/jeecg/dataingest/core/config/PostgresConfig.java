package org.jeecg.dataingest.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * PostgreSQL配置类
 * @Description: PostgreSQL配置类
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.master")
public class PostgresConfig {
    
    /**数据库URL*/
    private String url;
    
    /**用户名*/
    private String username;
    
    /**密码*/
    private String password;
    
    /**驱动类名*/
    private String driverClassName;
    
    /**连接池配置*/
    private PoolConfig pool;
    
    @Data
    public static class PoolConfig {
        private Integer initialSize = 5;
        private Integer minIdle = 5;
        private Integer maxActive = 20;
        private Long maxWait = 60000L;
        private Long timeBetweenEvictionRunsMillis = 60000L;
        private Long minEvictableIdleTimeMillis = 300000L;
        private String validationQuery = "SELECT 1";
        private Boolean testWhileIdle = true;
        private Boolean testOnBorrow = false;
        private Boolean testOnReturn = false;
        private Boolean poolPreparedStatements = true;
        private Integer maxPoolPreparedStatementPerConnectionSize = 20;
    }
} 