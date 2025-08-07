package org.jeecg.dataingest.scheduler.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.dataingest.entity.DataIngestMoudleIngestTask;
import org.jeecg.dataingest.core.service.IDataIngestService;
import org.springframework.beans.factory.annotation.Autowired;
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