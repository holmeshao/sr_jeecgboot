package org.jeecg.dataingest.scheduler.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.dataingest.entity.DataIngestMoudleIngestTask;
import org.jeecg.dataingest.core.service.IDataIngestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 数据接入任务处理器
 * @Description: 数据接入任务处理器
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Slf4j
@Component
public class DataIngestJobHandler {
    
    @Autowired
    private IDataIngestService dataIngestService;
    
    /**
     * 金蝶数据接入任务
     */
    @XxlJob("kingdeeDataIngestHandler")
    public void kingdeeDataIngestHandler() {
        log.info("开始执行金蝶数据接入任务");
        try {
            // TODO: 从配置中获取金蝶任务信息
            DataIngestMoudleIngestTask task = new DataIngestMoudleIngestTask();
            task.setTaskName("金蝶数据接入");
            task.setTaskType("API");
            
            boolean result = dataIngestService.executeTask(task);
            if (result) {
                XxlJobHelper.handleSuccess("金蝶数据接入任务执行成功");
            } else {
                XxlJobHelper.handleFail("金蝶数据接入任务执行失败");
            }
        } catch (Exception e) {
            log.error("金蝶数据接入任务执行异常", e);
            XxlJobHelper.handleFail("金蝶数据接入任务执行异常: " + e.getMessage());
        }
    }
    
    /**
     * 通用API数据接入任务
     */
    @XxlJob("openApiDataIngestHandler")
    public void openApiDataIngestHandler() {
        log.info("开始执行通用API数据接入任务");
        try {
            // TODO: 从配置中获取API任务信息
            DataIngestMoudleIngestTask task = new DataIngestMoudleIngestTask();
            task.setTaskName("通用API数据接入");
            task.setTaskType("API");
            
            boolean result = dataIngestService.executeTask(task);
            if (result) {
                XxlJobHelper.handleSuccess("通用API数据接入任务执行成功");
            } else {
                XxlJobHelper.handleFail("通用API数据接入任务执行失败");
            }
        } catch (Exception e) {
            log.error("通用API数据接入任务执行异常", e);
            XxlJobHelper.handleFail("通用API数据接入任务执行异常: " + e.getMessage());
        }
    }

    /**
     * 项目人员同步任务（宜昌实名制 -> D6C）
     * 参数支持：engId, code（可选） 多项目可通过分片或参数列表扩展
     */
    @XxlJob("projectWorkerSyncJob")
    public void projectWorkerSyncJob() {
        String param = XxlJobHelper.getJobParam();
        String engId = null;
        String code = null;
        String appkey = null;
        String appSecret = null;
        String projectName = null;
        if (StringUtils.hasText(param)) {
            String p = param.trim();
            if (p.startsWith("{")) {
                try {
                    com.alibaba.fastjson.JSONObject obj = com.alibaba.fastjson.JSON.parseObject(p);
                    engId = obj.getString("engId");
                    code = obj.getString("code");
                    appkey = obj.getString("appkey");
                    appSecret = obj.getString("appSecret");
                    projectName = obj.getString("projectName");
                } catch (Exception ignore) {}
            } else {
                engId = p;
            }
        }

        log.info("开始执行项目人员同步任务，engId={}, code={}", engId, code);
        try {
            // 获取服务
            org.jeecg.dataingest.projectworker.service.ProjectWorkerSyncService svc =
                org.jeecg.common.util.SpringContextUtils.getBean(org.jeecg.dataingest.projectworker.service.ProjectWorkerSyncService.class);
            org.jeecg.dataingest.projectworker.config.ProjectWorkerProjectRegistry registry =
                org.jeecg.common.util.SpringContextUtils.getBean(org.jeecg.dataingest.projectworker.config.ProjectWorkerProjectRegistry.class);

            // 构造覆盖参数（仅传非空项）
            org.jeecg.dataingest.projectworker.config.ProjectWorkerIntegrationProperties.Project override = null;
            if (org.springframework.util.StringUtils.hasText(engId)
                    || org.springframework.util.StringUtils.hasText(code)
                    || org.springframework.util.StringUtils.hasText(appkey)
                    || org.springframework.util.StringUtils.hasText(appSecret)
                    || org.springframework.util.StringUtils.hasText(projectName)) {
                override = new org.jeecg.dataingest.projectworker.config.ProjectWorkerIntegrationProperties.Project();
                override.setEngId(engId);
                if (org.springframework.util.StringUtils.hasText(code)) override.setCode(code);
                if (org.springframework.util.StringUtils.hasText(appkey)) override.setAppkey(appkey);
                if (org.springframework.util.StringUtils.hasText(appSecret)) override.setAppSecret(appSecret);
                if (org.springframework.util.StringUtils.hasText(projectName)) override.setProjectName(projectName);
            }

            // 1) 优先使用 XXL-Job 传参（带覆盖合并）
            if (StringUtils.hasText(engId)) {
                org.jeecg.dataingest.projectworker.service.ProjectWorkerSyncService.SyncResult r = svc.sync(engId, code, override);
                XxlJobHelper.handleSuccess("项目人员同步执行成功: fetched=" + r.getFetched());
                return;
            }

            // 2) 无参数：遍历注册表（Nacos 或 本地 JSON 已由 Registry 初始化）
            java.util.List<org.jeecg.dataingest.projectworker.config.ProjectWorkerIntegrationProperties.Project> projects = registry.listAllProjects();
            if (projects == null || projects.isEmpty()) {
                XxlJobHelper.handleFail("未找到任何项目配置（Nacos 或本地 JSON）");
                return;
            }

            int totalFetched = 0;
            int ok = 0;
            int failed = 0;
            for (org.jeecg.dataingest.projectworker.config.ProjectWorkerIntegrationProperties.Project p : projects) {
                String e = p.getEngId();
                String c = p.getCode();
                if (!org.springframework.util.StringUtils.hasText(e)) { continue; }
                try {
                    // 合并策略：以 registry 项为基准，被 job-level override 非空字段覆盖
                    org.jeecg.dataingest.projectworker.config.ProjectWorkerIntegrationProperties.Project merged = new org.jeecg.dataingest.projectworker.config.ProjectWorkerIntegrationProperties.Project();
                    merged.setEngId(e);
                    merged.setCode(c);
                    merged.setAppkey(p.getAppkey());
                    merged.setAppSecret(p.getAppSecret());
                    merged.setProjectName(p.getProjectName());
                    if (override != null) {
                        if (org.springframework.util.StringUtils.hasText(override.getCode())) merged.setCode(override.getCode());
                        if (org.springframework.util.StringUtils.hasText(override.getAppkey())) merged.setAppkey(override.getAppkey());
                        if (org.springframework.util.StringUtils.hasText(override.getAppSecret())) merged.setAppSecret(override.getAppSecret());
                        if (org.springframework.util.StringUtils.hasText(override.getProjectName())) merged.setProjectName(override.getProjectName());
                    }
                    org.jeecg.dataingest.projectworker.service.ProjectWorkerSyncService.SyncResult r = svc.sync(e, merged.getCode(), merged);
                    totalFetched += r.getFetched();
                    ok++;
                } catch (Exception ex) {
                    failed++;
                    log.error("项目人员同步失败 engId={}, code={} err={}", e, c, ex.getMessage(), ex);
                }
            }

            if (ok > 0 && failed == 0) {
                XxlJobHelper.handleSuccess("项目人员同步执行成功: projects=" + ok + ", fetched=" + totalFetched);
            } else if (ok > 0) {
                XxlJobHelper.handleSuccess("项目人员同步部分成功: success=" + ok + ", failed=" + failed + ", fetched=" + totalFetched);
            } else {
                XxlJobHelper.handleFail("项目人员同步全部失败: failed=" + failed);
            }
        } catch (Exception e) {
            log.error("项目人员同步任务执行异常", e);
            XxlJobHelper.handleFail("项目人员同步任务执行异常: " + e.getMessage());
        }
    }
    
    /**
     * 文件数据接入任务
     */
    @XxlJob("fileDataIngestHandler")
    public void fileDataIngestHandler() {
        log.info("开始执行文件数据接入任务");
        try {
            // TODO: 从配置中获取文件任务信息
            DataIngestMoudleIngestTask task = new DataIngestMoudleIngestTask();
            task.setTaskName("文件数据接入");
            task.setTaskType("FILE");
            
            boolean result = dataIngestService.executeTask(task);
            if (result) {
                XxlJobHelper.handleSuccess("文件数据接入任务执行成功");
            } else {
                XxlJobHelper.handleFail("文件数据接入任务执行失败");
            }
        } catch (Exception e) {
            log.error("文件数据接入任务执行异常", e);
            XxlJobHelper.handleFail("文件数据接入任务执行异常: " + e.getMessage());
        }
    }
} 