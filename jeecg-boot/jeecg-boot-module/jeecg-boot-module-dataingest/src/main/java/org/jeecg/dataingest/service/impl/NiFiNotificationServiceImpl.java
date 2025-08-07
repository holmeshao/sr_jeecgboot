package org.jeecg.dataingest.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.dataingest.entity.DataIngestMoudleDataCdcTable;
import org.jeecg.dataingest.service.INiFiNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * NiFi通知服务实现类
 * @Description: 负责向NiFi发送CDC数据变更通知
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Slf4j
@Service
public class NiFiNotificationServiceImpl implements INiFiNotificationService {

    @Autowired
    @Qualifier("nifiRestTemplate")
    private RestTemplate restTemplate;

    @Value("${nifi.api.base-url:http://localhost:8080/nifi-api}")
    private String nifiApiBaseUrl;

    @Value("${nifi.api.timeout:5000}")
    private int nifiApiTimeout;

    @Value("${nifi.notification.enabled:true}")
    private boolean notificationEnabled;

    @Value("${nifi.notification.async:true}")
    private boolean asyncNotification;

    private static final String PROCESSORS_ENDPOINT = "/processors";
    private static final String TRIGGER_ENDPOINT = "/run";

    @Override
    public boolean triggerNiFiProcessor(String processorId, JSONObject notificationData) {
        if (!notificationEnabled) {
            log.debug("NiFi通知已禁用，跳过处理器触发: {}", processorId);
            return true;
        }

        if (processorId == null || processorId.trim().isEmpty()) {
            log.warn("处理器ID为空，跳过NiFi通知");
            return false;
        }

        try {
            // 构建请求URL
            String url = nifiApiBaseUrl + PROCESSORS_ENDPOINT + "/" + processorId + TRIGGER_ENDPOINT;
            
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("processorId", processorId);
            requestBody.put("data", notificationData);
            requestBody.put("timestamp", System.currentTimeMillis());
            
            HttpEntity<String> request = new HttpEntity<>(requestBody.toJSONString(), headers);
            
            if (asyncNotification) {
                // 异步通知
                CompletableFuture.runAsync(() -> {
                    try {
                        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
                        if (response.getStatusCode().is2xxSuccessful()) {
                            log.debug("异步触发NiFi处理器成功: {}", processorId);
                        } else {
                            log.warn("异步触发NiFi处理器失败: {}, 状态码: {}", processorId, response.getStatusCode());
                        }
                    } catch (Exception e) {
                        log.error("异步触发NiFi处理器异常: {}", processorId, e);
                    }
                });
                return true;
            } else {
                // 同步通知
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
                
                if (response.getStatusCode().is2xxSuccessful()) {
                    log.debug("同步触发NiFi处理器成功: {}", processorId);
                    return true;
                } else {
                    log.warn("同步触发NiFi处理器失败: {}, 状态码: {}, 响应: {}", 
                             processorId, response.getStatusCode(), response.getBody());
                    return false;
                }
            }
            
        } catch (Exception e) {
            log.error("触发NiFi处理器异常: {}", processorId, e);
            return false;
        }
    }

    @Override
    public boolean triggerDwdProcessor(DataIngestMoudleDataCdcTable cdcConfig, JSONObject changeData) {
        if (cdcConfig.getNifiDwdProcessorId() == null || cdcConfig.getNifiDwdProcessorId().trim().isEmpty()) {
            log.debug("DWD处理器ID为空，跳过DWD层通知: {}", cdcConfig.getSourceTableName());
            return false;
        }

        // 构建DWD层通知数据
        JSONObject dwdData = new JSONObject();
        dwdData.put("layer", "DWD");
        dwdData.put("sourceTable", cdcConfig.getSourceTableName());
        dwdData.put("targetTable", cdcConfig.getTargetTableName());
        dwdData.put("businessDomain", cdcConfig.getBusinessDomain());
        dwdData.put("changeData", changeData);
        dwdData.put("processTime", System.currentTimeMillis());

        // 根据通知模式决定是否延迟
        if (cdcConfig.getNotifyDelaySeconds() != null && cdcConfig.getNotifyDelaySeconds() > 0) {
            CompletableFuture.runAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(cdcConfig.getNotifyDelaySeconds());
                    triggerNiFiProcessor(cdcConfig.getNifiDwdProcessorId(), dwdData);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("延迟触发DWD处理器被中断: {}", cdcConfig.getNifiDwdProcessorId(), e);
                }
            });
            return true;
        } else {
            return triggerNiFiProcessor(cdcConfig.getNifiDwdProcessorId(), dwdData);
        }
    }

    @Override
    public boolean triggerDwsProcessor(DataIngestMoudleDataCdcTable cdcConfig, JSONObject changeData) {
        if (cdcConfig.getNifiDwsProcessorId() == null || cdcConfig.getNifiDwsProcessorId().trim().isEmpty()) {
            log.debug("DWS处理器ID为空，跳过DWS层通知: {}", cdcConfig.getSourceTableName());
            return false;
        }

        // 构建DWS层通知数据
        JSONObject dwsData = new JSONObject();
        dwsData.put("layer", "DWS");
        dwsData.put("sourceTable", cdcConfig.getSourceTableName());
        dwsData.put("targetTable", cdcConfig.getTargetTableName());
        dwsData.put("businessDomain", cdcConfig.getBusinessDomain());
        dwsData.put("aggregationType", "summary"); // 汇总类型，可根据业务需要扩展
        dwsData.put("changeData", changeData);
        dwsData.put("processTime", System.currentTimeMillis());

        return triggerNiFiProcessor(cdcConfig.getNifiDwsProcessorId(), dwsData);
    }

    @Override
    public boolean triggerProcessorBatch(String processorId, JSONObject batchData) {
        if (processorId == null || processorId.trim().isEmpty()) {
            log.warn("批量触发处理器ID为空");
            return false;
        }

        JSONObject notificationData = new JSONObject();
        notificationData.put("type", "batch");
        notificationData.put("batchSize", batchData.getIntValue("batchSize"));
        notificationData.put("batchData", batchData);
        notificationData.put("processTime", System.currentTimeMillis());

        return triggerNiFiProcessor(processorId, notificationData);
    }

    @Override
    public String checkProcessorStatus(String processorId) {
        if (processorId == null || processorId.trim().isEmpty()) {
            return "INVALID_ID";
        }

        try {
            String url = nifiApiBaseUrl + PROCESSORS_ENDPOINT + "/" + processorId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                JSONObject responseData = JSON.parseObject(response.getBody());
                if (responseData != null && responseData.containsKey("status")) {
                    return responseData.getJSONObject("status").getString("runStatus");
                }
                return "UNKNOWN";
            } else {
                log.warn("检查NiFi处理器状态失败: {}, 状态码: {}", processorId, response.getStatusCode());
                return "ERROR";
            }
            
        } catch (Exception e) {
            log.error("检查NiFi处理器状态异常: {}", processorId, e);
            return "EXCEPTION";
        }
    }

    @Override
    public boolean triggerByBusinessDomain(String businessDomain, JSONObject changeData) {
        if (businessDomain == null || businessDomain.trim().isEmpty()) {
            log.warn("业务域为空，无法触发处理器");
            return false;
        }

        // 构建业务域级别的通知数据
        JSONObject domainData = new JSONObject();
        domainData.put("businessDomain", businessDomain);
        domainData.put("changeData", changeData);
        domainData.put("processTime", System.currentTimeMillis());

        // 这里可以根据业务域映射到具体的处理器ID
        // 目前先记录日志，具体映射逻辑可以后续扩展
        log.info("业务域触发通知: {}, 数据: {}", businessDomain, domainData);
        
        // TODO: 实现业务域到处理器的映射逻辑
        // String processorId = getProcessorIdByBusinessDomain(businessDomain);
        // return triggerNiFiProcessor(processorId, domainData);
        
        return true;
    }

    /**
     * 根据通知模式决定触发策略
     */
    public boolean triggerByNotifyMode(DataIngestMoudleDataCdcTable cdcConfig, JSONObject changeData) {
        if (cdcConfig.getEnableNifiNotify() == null || cdcConfig.getEnableNifiNotify() != 1) {
            log.debug("NiFi通知未启用: {}", cdcConfig.getSourceTableName());
            return false;
        }

        Integer notifyMode = cdcConfig.getNifiNotifyMode();
        if (notifyMode == null) {
            notifyMode = 1; // 默认立即通知
        }

        switch (notifyMode) {
            case 1: // 立即通知
                boolean dwdResult = triggerDwdProcessor(cdcConfig, changeData);
                boolean dwsResult = triggerDwsProcessor(cdcConfig, changeData);
                return dwdResult || dwsResult; // 只要有一个成功就返回true
            case 2: // 批量通知
                log.debug("批量通知模式，数据已缓存: {}", cdcConfig.getSourceTableName());
                // TODO: 实现批量通知逻辑
                return true;
            case 3: // 定时通知
                log.debug("定时通知模式，数据已缓存: {}", cdcConfig.getSourceTableName());
                // TODO: 实现定时通知逻辑
                return true;
            default:
                log.warn("未知的通知模式: {}, 使用立即通知", notifyMode);
                boolean defaultDwdResult = triggerDwdProcessor(cdcConfig, changeData);
                boolean defaultDwsResult = triggerDwsProcessor(cdcConfig, changeData);
                return defaultDwdResult || defaultDwsResult;
        }
    }
} 