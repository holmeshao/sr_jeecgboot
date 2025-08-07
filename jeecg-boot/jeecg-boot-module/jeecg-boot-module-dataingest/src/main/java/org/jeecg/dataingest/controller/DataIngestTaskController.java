package org.jeecg.dataingest.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
// 移除IngestTask导入，直接使用DataIngestMoudleIngestTask
import org.jeecg.dataingest.core.service.IDataIngestService;
import org.jeecg.dataingest.debezium.service.DebeziumService;
import org.jeecg.dataingest.entity.DataIngestMoudleIngestTask;
import org.jeecg.dataingest.service.IDataIngestMoudleIngestTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据接入任务管理控制器
 * @Description: 数据接入任务管理控制器
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("/dataingest/task")
public class DataIngestTaskController {
    
    @Autowired
    private IDataIngestService dataIngestService;
    
    @Autowired
    private DebeziumService debeziumService;
    
    @Autowired
    private IDataIngestMoudleIngestTaskService taskService;
    
    /**
     * 启动CDC监听任务
     */
    @PostMapping("/cdc/start")
    public Result<String> startCDC(@RequestParam(required = false) String taskId) {
        try {
            if (taskId == null || taskId.isEmpty()) {
                taskId = "SYSTEM_CDC_TASK_" + System.currentTimeMillis();
            }
            
            log.info("启动CDC监听任务: {}", taskId);
            debeziumService.startCDC(taskId);
            
            return Result.OK("CDC监听任务启动成功，任务ID: " + taskId);
        } catch (Exception e) {
            log.error("启动CDC监听任务失败", e);
            return Result.error("启动CDC监听任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 停止CDC监听任务
     */
    @PostMapping("/cdc/stop")
    public Result<String> stopCDC() {
        try {
            log.info("停止CDC监听任务");
            debeziumService.stopCDC();
            return Result.OK("CDC监听任务已停止");
        } catch (Exception e) {
            log.error("停止CDC监听任务失败", e);
            return Result.error("停止CDC监听任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 执行指定的数据接入任务
     */
    @PostMapping("/execute/{taskId}")
    public Result<String> executeTask(@PathVariable String taskId) {
        try {
            log.info("执行数据接入任务: {}", taskId);
            
            // 从数据库获取任务配置
            DataIngestMoudleIngestTask dbTask = taskService.getById(taskId);
            if (dbTask == null) {
                return Result.error("任务不存在: " + taskId);
            }
            
            // 直接使用数据库任务对象执行
            boolean result = dataIngestService.executeTaskWithLog(dbTask, taskId);
            
            if (result) {
                return Result.OK("任务执行成功: " + dbTask.getTaskName());
            } else {
                return Result.error("任务执行失败: " + dbTask.getTaskName());
            }
            
        } catch (Exception e) {
            log.error("执行数据接入任务失败: " + taskId, e);
            return Result.error("执行任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 停止指定的数据接入任务
     */
    @PostMapping("/stop/{taskId}")
    public Result<String> stopTask(@PathVariable String taskId) {
        try {
            log.info("停止数据接入任务: {}", taskId);
            boolean result = dataIngestService.stopTask(taskId);
            
            if (result) {
                return Result.OK("任务已停止: " + taskId);
            } else {
                return Result.error("停止任务失败: " + taskId);
            }
            
        } catch (Exception e) {
            log.error("停止数据接入任务失败: " + taskId, e);
            return Result.error("停止任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取任务执行状态
     */
    @GetMapping("/status/{taskId}")
    public Result<Map<String, Object>> getTaskStatus(@PathVariable String taskId) {
        try {
            Integer status = dataIngestService.getTaskStatus(taskId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", taskId);
            result.put("status", status);
            result.put("statusText", getStatusText(status));
            result.put("checkTime", new Date());
            
            return Result.OK(result);
            
        } catch (Exception e) {
            log.error("获取任务状态失败: " + taskId, e);
            return Result.error("获取任务状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取系统健康状态
     */
    @GetMapping("/health")
    public Result<Map<String, Object>> getHealthStatus() {
        try {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "UP");
            health.put("timestamp", new Date());
            health.put("version", "1.0.0");
            health.put("description", "DataIngest模块运行正常");
            
            return Result.OK(health);
            
        } catch (Exception e) {
            log.error("获取健康状态失败", e);
            return Result.error("获取健康状态失败: " + e.getMessage());
        }
    }
    
    // 已移除convertToIngestTask方法，直接使用DataIngestMoudleIngestTask
    
    /**
     * 获取状态文本描述
     */
    private String getStatusText(Integer status) {
        if (status == null) {
            return "未知";
        }
        
        switch (status) {
            case 0:
                return "待执行";
            case 1:
                return "执行中";
            case 2:
                return "执行成功";
            case 3:
                return "执行失败";
            case 4:
                return "已停止";
            default:
                return "未知状态";
        }
    }
}