package org.jeecg.dataingest.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson配置类
 * 配置分布式锁功能
 * 
 * @author jeecg-boot
 */
@Configuration
public class RedissonConfig {

    @Value("${jeecg.redisson.address}")
    private String address;

    @Value("${jeecg.redisson.password:}")
    private String password;

    @Value("${jeecg.redisson.type:STANDALONE}")
    private String type;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        
        if ("STANDALONE".equals(type)) {
            // 单机模式
            config.useSingleServer()
                    .setAddress("redis://" + address)
                    .setPassword(password.isEmpty() ? null : password)
                    .setDatabase(0)
                    .setConnectionPoolSize(64)
                    .setConnectionMinimumIdleSize(10);
        } else if ("CLUSTER".equals(type)) {
            // 集群模式
            config.useClusterServers()
                    .addNodeAddress("redis://" + address)
                    .setPassword(password.isEmpty() ? null : password)
                    .setScanInterval(2000);
        }
        
        return Redisson.create(config);
    }
} 