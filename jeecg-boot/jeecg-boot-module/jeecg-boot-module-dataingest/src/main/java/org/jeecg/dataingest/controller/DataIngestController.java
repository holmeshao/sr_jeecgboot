package org.jeecg.dataingest.controller;

import com.alibaba.fastjson2.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.dataingest.entity.DataIngestModuleIngestTask;
import org.jeecg.dataingest.service.IDataIngestModuleIngestTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 数据接入服务控制器（简化版）
 */
@RestController
@RequestMapping("/dataingest")
public class DataIngestController {

    @Autowired
    private IDataIngestModuleIngestTaskService dataIngestTaskService;

    @PostMapping("/start")
    public Result<String> startTask(@RequestBody JSONObject params, HttpServletRequest request) {
        try {
            String taskId = params.getString("taskId");
            return Result.OK("任务启动成功: " + taskId);
        } catch (Exception e) {
            return Result.error("任务启动失败: " + e.getMessage());
        }
    }

    @PostMapping("/stop")
    public Result<String> stopTask(@RequestBody JSONObject params, HttpServletRequest request) {
        try {
            String taskId = params.getString("taskId");
            return Result.OK("任务停止成功: " + taskId);
        } catch (Exception e) {
            return Result.error("任务停止失败: " + e.getMessage());
        }
    }

    @GetMapping("/status/{taskId}")
    public Result<String> getTaskStatus(@PathVariable String taskId) {
        try {
            return Result.OK("运行中", "RUNNING");
        } catch (Exception e) {
            return Result.error("获取状态失败: " + e.getMessage());
        }
    }
}