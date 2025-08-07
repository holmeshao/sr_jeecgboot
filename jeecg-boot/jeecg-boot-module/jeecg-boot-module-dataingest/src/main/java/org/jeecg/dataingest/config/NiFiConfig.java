package org.jeecg.dataingest.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * NiFi集成配置类
 * @Description: 配置NiFi相关的Bean和连接参数
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Slf4j
@Configuration
public class NiFiConfig {

    @Value("${nifi.api.connect-timeout:3000}")
    private int connectTimeout;

    @Value("${nifi.api.read-timeout:5000}")
    private int readTimeout;

    /**
     * 配置用于NiFi通信的RestTemplate
     */
    @Bean(name = "nifiRestTemplate")
    public RestTemplate nifiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        
        // 可以在这里添加拦截器、错误处理器等
        restTemplate.getInterceptors().add((request, body, execution) -> {
            log.debug("NiFi API请求: {} {}", request.getMethod(), request.getURI());
            return execution.execute(request, body);
        });
        
        log.info("NiFi RestTemplate配置完成: connectTimeout={}ms, readTimeout={}ms", 
                connectTimeout, readTimeout);
        
        return restTemplate;
    }

    /**
     * 配置HTTP请求工厂
     */
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        return factory;
    }
} 