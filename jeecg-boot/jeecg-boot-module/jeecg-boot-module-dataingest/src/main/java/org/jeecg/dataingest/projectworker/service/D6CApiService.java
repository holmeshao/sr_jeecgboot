package org.jeecg.dataingest.projectworker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.dataingest.openapi.service.OpenApiService;
import org.jeecg.dataingest.projectworker.config.ProjectWorkerIntegrationProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * D6C API 统一调用服务
 * - 使用 projectworker.d6c-api.gateway 作为统一网关
 * - 承接 JSON payload 直接 POST
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class D6CApiService {

    private final OpenApiService openApiService;
    private final ProjectWorkerIntegrationProperties props;

    public void post(String method, List<Map<String, Object>> data, Map<String, String> common) {
        if (data == null || data.isEmpty()) { return; }
        String gateway = nvl(props.getD6cApi().getGateway());
        Map<String, Object> payload = new HashMap<>();
        payload.put("appKey", common.get("appKey"));
        payload.put("timestamp", common.get("timestamp"));
        payload.put("method", method);
        payload.put("data", com.alibaba.fastjson.JSON.toJSONString(data));
        payload.put("sign", common.get("sign"));

        if (!isBlank(gateway)) {
            try {
                OpenApiService.ApiRequest req = new OpenApiService.ApiRequest();
                req.setUrl(gateway);
                req.setMethod("POST");
                req.setContentType("application/json");
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                //headers.put("User-Agent", "JeecgBoot-DataIngest/1.0");
                req.setHeaders(headers);
                req.setBody(payload);

                String resp = openApiService.executeRaw(req);
                log.info("推送D6C完成 method={}, records={}, respLen={}", method, data.size(), resp == null ? 0 : resp.length());
            } catch (Exception e) {
                log.error("推送D6C失败 method={}, error={}", method, e.getMessage(), e);
            }
        } else {
            log.info("[dryrun] 未配置 d6cApi.gateway，跳过实际HTTP调用。method={}, records={}", method, data.size());
        }
    }

    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
    private static String nvl(String s) { return s == null ? "" : s; }
}


