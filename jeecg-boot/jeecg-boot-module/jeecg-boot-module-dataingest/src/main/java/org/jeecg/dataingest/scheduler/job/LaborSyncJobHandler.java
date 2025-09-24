package org.jeecg.dataingest.scheduler.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.dataingest.entity.DataIngestMoudleIngestTask;
import org.jeecg.dataingest.core.service.IDataIngestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 劳务平台人员同步 Job
 */
@Slf4j
@Component
public class LaborSyncJobHandler {

    @Autowired
    private IDataIngestService dataIngestService;

    /**
     * 触发劳务平台人员同步
     * 参数示例：{"engId":"ENG123","code":"ABCDEF"}
     */
    @XxlJob("laborSyncJob")
    public void laborSyncJob() {
        String params = XxlJobHelper.getJobParam();
        log.info("开始执行劳务平台人员同步任务, params={}", params);
        try {
            DataIngestMoudleIngestTask task = new DataIngestMoudleIngestTask();
            task.setTaskName("劳务平台人员同步");
            task.setTaskType("LABOR");
            task.setTaskConfig(params);

            boolean result = dataIngestService.executeTask(task);
            if (result) {
                XxlJobHelper.handleSuccess("劳务平台人员同步执行成功");
            } else {
                XxlJobHelper.handleFail("劳务平台人员同步执行失败");
            }
        } catch (Exception e) {
            log.error("劳务平台人员同步任务执行异常", e);
            XxlJobHelper.handleFail("异常: " + e.getMessage());
        }
    }
}


