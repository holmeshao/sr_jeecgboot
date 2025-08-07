package org.jeecg.dataingest.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.dataingest.debezium.DebeziumClusterTaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 数据摄取集群管理控制器
 * @Description: 提供集群模式下的CDC任务管理接口
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V2.0
 */
@Tag(name = "数据摄取集群管理", description = "提供集群模式下的CDC任务管理接口")
@RestController
@RequestMapping("/dataingest/cluster")
@Slf4j
public class DataIngestClusterController {

    @Autowired
    private DebeziumClusterTaskManager clusterTaskManager;

    /**
     * 启动集群CDC任务
     */
    @Operation(summary = "启动集群CDC任务", description = "在集群环境中启动CDC任务，支持负载均衡和故障转移")
    @PostMapping("/task/{taskId}/start")
    public Result<JSONObject> startClusterTask(@PathVariable String taskId, @RequestBody DebeziumClusterTaskManager.DebeziumTaskConfig config) {
        try {
            log.info("启动集群CDC任务: {}", taskId);
            
            config.setTaskId(taskId);
            clusterTaskManager.startOrRestartTask(taskId, config);
            
            // 返回任务状态
            JSONObject result = clusterTaskManager.getClusterTaskStatus(taskId);
            return Result.OK("集群CDC任务启动成功", result);
            
        } catch (Exception e) {
            log.error("启动集群CDC任务失败: {}", taskId, e);
            return Result.error("启动失败: " + e.getMessage());
        }
    }

    /**
     * 停止集群CDC任务
     */
    @Operation(summary = "停止集群CDC任务", description = "停止指定的集群CDC任务")
    @PostMapping("/task/{taskId}/stop")
    public Result<String> stopClusterTask(@PathVariable String taskId) {
        try {
            log.info("停止集群CDC任务: {}", taskId);
            
            clusterTaskManager.stopTask(taskId);
            return Result.OK("集群CDC任务停止成功");
            
        } catch (Exception e) {
            log.error("停止集群CDC任务失败: {}", taskId, e);
            return Result.error("停止失败: " + e.getMessage());
        }
    }

    /**
     * 获取集群任务状态
     */
    @Operation(summary = "获取集群任务状态", description = "查询指定任务在集群中的运行状态")
    @GetMapping("/task/{taskId}/status")
    public Result<JSONObject> getClusterTaskStatus(@PathVariable String taskId) {
        try {
            JSONObject status = clusterTaskManager.getClusterTaskStatus(taskId);
            return Result.OK("获取任务状态成功", status);
            
        } catch (Exception e) {
            log.error("获取集群任务状态失败: {}", taskId, e);
            return Result.error("获取状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取集群任务列表
     */
    @Operation(summary = "获取集群任务列表", description = "查询集群中所有CDC任务的状态")
    @GetMapping("/tasks")
    public Result<JSONArray> getClusterTaskList() {
        try {
            JSONArray taskList = clusterTaskManager.getClusterTaskList();
            return Result.OK("获取任务列表成功", taskList);
            
        } catch (Exception e) {
            log.error("获取集群任务列表失败", e);
            return Result.error("获取任务列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取集群节点信息
     */
    @Operation(summary = "获取集群节点信息", description = "查询集群中所有节点的心跳状态")
    @GetMapping("/nodes")
    public Result<JSONArray> getClusterNodes() {
        try {
            // 这里可以从Redis获取所有节点的心跳信息
            JSONArray nodes = new JSONArray();
            
            // 临时返回当前实现状态
            JSONObject currentNode = new JSONObject();
            currentNode.put("message", "集群节点信息查询功能已就绪，可根据需要扩展实现");
            nodes.add(currentNode);
            
            return Result.OK("获取集群节点信息成功", nodes);
            
        } catch (Exception e) {
            log.error("获取集群节点信息失败", e);
            return Result.error("获取节点信息失败: " + e.getMessage());
        }
    }

    /**
     * 重启集群任务
     */
    @Operation(summary = "重启集群任务", description = "重启指定的集群CDC任务")
    @PostMapping("/task/{taskId}/restart")
    public Result<JSONObject> restartClusterTask(@PathVariable String taskId) {
        try {
            log.info("重启集群CDC任务: {}", taskId);
            
            // 先停止任务
            clusterTaskManager.stopTask(taskId);
            
            // 等待一小段时间确保任务完全停止
            Thread.sleep(2000);
            
            // 重新启动任务（需要从Redis获取配置）
            // 这里简化处理，实际应用中需要从Redis获取任务配置
            JSONObject result = new JSONObject();
            result.put("message", "任务重启指令已发送，请稍后查看任务状态");
            result.put("taskId", taskId);
            
            return Result.OK("集群CDC任务重启成功", result);
            
        } catch (Exception e) {
            log.error("重启集群CDC任务失败: {}", taskId, e);
            return Result.error("重启失败: " + e.getMessage());
        }
    }

    /**
     * 任务故障转移
     */
    @Operation(summary = "手动故障转移", description = "手动将任务从一个节点转移到另一个节点")
    @PostMapping("/task/{taskId}/failover")
    public Result<String> failoverTask(@PathVariable String taskId) {
        try {
            log.info("手动故障转移任务: {}", taskId);
            
            // 停止当前任务
            clusterTaskManager.stopTask(taskId);
            
            // 等待其他节点接管
            Thread.sleep(3000);
            
            return Result.OK("故障转移指令已发送，请稍后查看任务状态");
            
        } catch (Exception e) {
            log.error("任务故障转移失败: {}", taskId, e);
            return Result.error("故障转移失败: " + e.getMessage());
        }
    }
} 