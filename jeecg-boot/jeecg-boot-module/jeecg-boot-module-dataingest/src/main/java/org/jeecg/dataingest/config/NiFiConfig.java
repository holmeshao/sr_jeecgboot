package org.jeecg.dataingest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * NiFi配置类
 * @Description: 配置NiFi相关的Bean和参数
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Configuration
public class NiFiConfig {

    @Value("${nifi.api.timeout:5000}")
    private int nifiApiTimeout;

    @Value("${nifi.api.connection-timeout:3000}")
    private int connectionTimeout;

    @Value("${nifi.api.read-timeout:10000}")
    private int readTimeout;

    /**
     * 创建专用于NiFi API调用的RestTemplate
     */
    @Bean("nifiRestTemplate")
    public RestTemplate nifiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        // 设置超时配置
        ClientHttpRequestFactory factory = clientHttpRequestFactory();
        restTemplate.setRequestFactory(factory);
        
        return restTemplate;
    }

    /**
     * 配置HTTP请求工厂
     */
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        
        // 连接超时时间（毫秒）
        factory.setConnectTimeout(connectionTimeout);
        
        // 读取超时时间（毫秒）
        factory.setReadTimeout(readTimeout);
        
        return factory;
    }
}