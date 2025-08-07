package org.jeecg.dataingest.kingdee.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jeecg.dataingest.kingdee.config.KingdeeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 金蝶服务类
 * @Description: 金蝶服务类
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Slf4j
@Service
public class KingdeeService {
    
    @Autowired
    private KingdeeConfig kingdeeConfig;
    
    private OkHttpClient httpClient;
    private String cachedToken;
    private long tokenExpireTime;
    
    public KingdeeService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }
    
    /**
     * 获取访问令牌
     */
    public String getToken() {
        if (isTokenValid()) {
            return cachedToken;
        }
        
        try {
            String token = requestToken();
            if (token != null) {
                cachedToken = token;
                tokenExpireTime = System.currentTimeMillis() + kingdeeConfig.getCommon().getTokenCacheTime();
                return token;
            }
        } catch (Exception e) {
            log.error("获取金蝶访问令牌失败", e);
        }
        
        return null;
    }
    
    /**
     * 执行金蝶查询
     */
    public JSONObject executeQuery(String formId, String fieldKeys, String filterString) {
        try {
            String token = getToken();
            if (token == null) {
                log.error("无法获取有效的访问令牌");
                return null;
            }
            
            String url = buildQueryUrl(formId, fieldKeys, filterString);
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    return JSON.parseObject(responseBody);
                } else {
                    log.error("金蝶查询失败: {}", response.code());
                    return null;
                }
            }
        } catch (Exception e) {
            log.error("执行金蝶查询失败", e);
            return null;
        }
    }
    
    /**
     * 检查令牌是否有效
     */
    private boolean isTokenValid() {
        return cachedToken != null && System.currentTimeMillis() < tokenExpireTime;
    }
    
    /**
     * 请求访问令牌
     */
    private String requestToken() throws IOException {
        // 根据配置选择金蝶Cloud或K3
        if (kingdeeConfig.getCloud() != null) {
            return requestCloudToken();
        } else if (kingdeeConfig.getK3() != null) {
            return requestK3Token();
        } else {
            log.error("未配置金蝶连接信息");
            return null;
        }
    }
    
    /**
     * 请求金蝶Cloud令牌
     */
    private String requestCloudToken() throws IOException {
        KingdeeConfig.CloudConfig config = kingdeeConfig.getCloud();
        
        JSONObject requestBody = new JSONObject();
        requestBody.put("appid", config.getAppId());
        requestBody.put("appsecret", config.getAppSecret());
        requestBody.put("username", config.getUsername());
        requestBody.put("password", config.getPassword());
        requestBody.put("lcid", config.getLcid());
        requestBody.put("dbid", config.getDbId());
        
        String url = config.getBaseUrl() + "/Kingdee.BOS.WebApi.ServicesStub.AuthService.ValidateUser.common.kdsvc";
        
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), 
                requestBody.toJSONString()
        );
        
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject result = JSON.parseObject(responseBody);
                
                if (result.getBoolean("LoginResultType")) {
                    return result.getString("Context");
                } else {
                    log.error("金蝶Cloud登录失败: {}", result.getString("Message"));
                    return null;
                }
            }
        }
        
        return null;
    }
    
    /**
     * 请求金蝶K3令牌
     */
    private String requestK3Token() throws IOException {
        KingdeeConfig.K3Config config = kingdeeConfig.getK3();
        
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", config.getUsername());
        requestBody.put("password", config.getPassword());
        requestBody.put("lcid", config.getLcid());
        requestBody.put("dbid", config.getDbId());
        
        String url = config.getBaseUrl() + "/Kingdee.BOS.WebApi.ServicesStub.AuthService.ValidateUser.common.kdsvc";
        
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), 
                requestBody.toJSONString()
        );
        
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject result = JSON.parseObject(responseBody);
                
                if (result.getBoolean("LoginResultType")) {
                    return result.getString("Context");
                } else {
                    log.error("金蝶K3登录失败: {}", result.getString("Message"));
                    return null;
                }
            }
        }
        
        return null;
    }
    
    /**
     * 构建查询URL
     */
    private String buildQueryUrl(String formId, String fieldKeys, String filterString) {
        // 根据配置选择基础URL
        String baseUrl = kingdeeConfig.getCloud() != null ? 
                kingdeeConfig.getCloud().getBaseUrl() : 
                kingdeeConfig.getK3().getBaseUrl();
        
        return baseUrl + "/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc";
    }
} 