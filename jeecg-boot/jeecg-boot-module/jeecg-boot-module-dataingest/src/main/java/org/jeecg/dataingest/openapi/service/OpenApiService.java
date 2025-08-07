package org.jeecg.dataingest.openapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 通用API服务类
 * @Description: 通用API服务类
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Slf4j
@Service
public class OpenApiService {
    
    private final OkHttpClient httpClient;
    
    public OpenApiService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }
    
    /**
     * 执行API请求
     */
    public JSONObject executeApiRequest(ApiRequest request) {
        try {
            Request.Builder requestBuilder = new Request.Builder()
                    .url(request.getUrl());
            
            // 添加请求头
            if (request.getHeaders() != null) {
                for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
                    requestBuilder.addHeader(header.getKey(), header.getValue());
                }
            }
            
            // 设置请求方法和请求体
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                requestBuilder.get();
            } else if ("POST".equalsIgnoreCase(request.getMethod())) {
                RequestBody body = buildRequestBody(request);
                requestBuilder.post(body);
            } else if ("PUT".equalsIgnoreCase(request.getMethod())) {
                RequestBody body = buildRequestBody(request);
                requestBuilder.put(body);
            } else if ("DELETE".equalsIgnoreCase(request.getMethod())) {
                requestBuilder.delete();
            }
            
            Request httpRequest = requestBuilder.build();
            
            try (Response response = httpClient.newCall(httpRequest).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    return JSON.parseObject(responseBody);
                } else {
                    log.error("API请求失败: {} - {}", response.code(), response.message());
                    return null;
                }
            }
        } catch (Exception e) {
            log.error("执行API请求失败", e);
            return null;
        }
    }
    
    /**
     * 构建请求体
     */
    private RequestBody buildRequestBody(ApiRequest request) {
        if (request.getBody() != null) {
            String contentType = request.getContentType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = "application/json";
            }
            
            String bodyString;
            if (request.getBody() instanceof String) {
                bodyString = (String) request.getBody();
            } else {
                bodyString = JSON.toJSONString(request.getBody());
            }
            
            return RequestBody.create(MediaType.parse(contentType), bodyString);
        }
        
        return RequestBody.create(MediaType.parse("application/json"), "");
    }
    
    /**
     * API请求配置类
     */
    public static class ApiRequest {
        private String url;
        private String method = "GET";
        private Map<String, String> headers;
        private Object body;
        private String contentType;
        
        // Getters and Setters
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        
        public Map<String, String> getHeaders() { return headers; }
        public void setHeaders(Map<String, String> headers) { this.headers = headers; }
        
        public Object getBody() { return body; }
        public void setBody(Object body) { this.body = body; }
        
        public String getContentType() { return contentType; }
        public void setContentType(String contentType) { this.contentType = contentType; }
    }
} 