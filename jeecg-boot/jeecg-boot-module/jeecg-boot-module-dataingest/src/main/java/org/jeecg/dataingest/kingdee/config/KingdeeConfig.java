package org.jeecg.dataingest.kingdee.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 金蝶配置类
 * @Description: 金蝶配置类
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "org-jeecg-dataingest-kingdee")
public class KingdeeConfig {
    
    /**金蝶Cloud配置*/
    private CloudConfig cloud;
    
    /**金蝶K3配置*/
    private K3Config k3;
    
    /**通用配置*/
    private CommonConfig common;
    
    @Data
    public static class CloudConfig {
        private String baseUrl;
        private String appId;
        private String appSecret;
        private String username;
        private String password;
        private String lcid;
        private String dbId;
    }
    
    @Data
    public static class K3Config {
        private String baseUrl;
        private String username;
        private String password;
        private String lcid;
        private String dbId;
    }
    
    @Data
    public static class CommonConfig {
        private Integer connectTimeout = 30000;
        private Integer readTimeout = 60000;
        private Integer maxRetries = 3;
        private Long tokenCacheTime = 7200000L; // 2小时
    }
} 