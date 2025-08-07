package org.jeecg.modules.maintenance.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.maintenance.entity.MaintenanceOrder;
import org.jeecg.modules.maintenance.service.IMaintenanceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 维保工作流管理Controller
 *
 * @author jeecg
 * @since 2025-01-24
 */
@Tag(name = "维保工作流管理")
@RestController
@RequestMapping("/maintenance/workflow")
@Slf4j
public class MaintenanceWorkflowController {

    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private HistoryService historyService;
    
    @Autowired
    private IMaintenanceOrderService maintenanceOrderService;

    /**
     * 启动维保工单流程
     */
    @AutoLog(value = "启动维保工单流程")
    @Operation(summary = "启动维保工单流程", description = "启动维保工单流程")
    @PostMapping("/start/{orderId}")
    public Result<String> startMaintenanceProcess(@PathVariable String orderId, HttpServletRequest request) {
        try {
            String username = JwtUtil.getUserNameByToken(request);
            if (oConvertUtils.isEmpty(username)) {
                return Result.error("用户未登录");
            }
            
            // 获取工单信息
            MaintenanceOrder order = maintenanceOrderService.getById(orderId);
            if (order == null) {
                return Result.error("工单不存在");
            }
            
            // 检查工单是否已经启动流程
            if (oConvertUtils.isNotEmpty(order.getProcessInstanceId())) {
                return Result.error("工单流程已经启动");
            }
            
            // 准备流程变量
            Map<String, Object> variables = new HashMap<>();
            variables.put("orderId", orderId);
            variables.put("projectId", order.getProjectId());
            variables.put("customer", order.getCreateBy());
            variables.put("title", order.getTitle());
            variables.put("description", order.getDescription());
            variables.put("priority", order.getPriorityLevel());
            
            // 设置处理人员（需要根据项目配置获取）
            variables.put("technician", getTechnicianByProject(order.getProjectId()));
            variables.put("projectManager", getProjectManagerByProject(order.getProjectId()));
            variables.put("leader", getLeaderByProject(order.getProjectId()));
            variables.put("inspector", getInspectorByProject(order.getProjectId()));
            
            // 启动流程
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                    "maintenanceOrderProcess", orderId, variables);
            
            // 更新工单的流程实例ID
            order.setProcessInstanceId(processInstance.getId());
            order.setCurrentStatus("TECHNICIAN_REVIEWING");
            maintenanceOrderService.updateById(order);
            
            log.info("维保工单流程启动成功，工单ID：{}，流程实例ID：{}", orderId, processInstance.getId());
            return Result.OK("维保工单流程启动成功", processInstance.getId());
            
        } catch (Exception e) {
            log.error("启动维保工单流程失败", e);
            return Result.error("启动维保工单流程失败：" + e.getMessage());
        }
    }

    /**
     * 维修人员审核
     */
    @AutoLog(value = "维修人员审核")
    @Operation(summary = "维修人员审核", description = "维修人员审核")
    @PostMapping("/technician-review/{taskId}")
    public Result<String> technicianReview(@PathVariable String taskId, 
                                         @RequestBody Map<String, Object> params,
                                         HttpServletRequest request) {
        try {
            String username = JwtUtil.getUserNameByToken(request);
            return completeTask(taskId, params, username);
        } catch (Exception e) {
            log.error("维修人员审核失败", e);
            return Result.error("维修人员审核失败：" + e.getMessage());
        }
    }

    /**
     * 审批流程
     */
    @AutoLog(value = "审批流程")
    @Operation(summary = "审批流程", description = "审批流程")
    @PostMapping("/approval/{taskId}")
    public Result<String> approval(@PathVariable String taskId,
                                  @RequestBody Map<String, Object> params,
                                  HttpServletRequest request) {
        try {
            String username = JwtUtil.getUserNameByToken(request);
            return completeTask(taskId, params, username);
        } catch (Exception e) {
            log.error("审批失败", e);
            return Result.error("审批失败：" + e.getMessage());
        }
    }

    /**
     * 派单给劳务班组
     */
    @AutoLog(value = "派单给劳务班组")
    @Operation(summary = "派单给劳务班组", description = "派单给劳务班组")
    @PostMapping("/assign-labor/{taskId}")
    public Result<String> assignToLaborTeam(@PathVariable String taskId,
                                           @RequestBody Map<String, Object> params,
                                           HttpServletRequest request) {
        try {
            String username = JwtUtil.getUserNameByToken(request);
            
            // 设置劳务班组负责人为下一个任务的处理人
            String laborTeamId = (String) params.get("laborTeamId");
            String laborTeamLeader = getLaborTeamLeader(laborTeamId);
            params.put("laborTeamLeader", laborTeamLeader);
            
            return completeTask(taskId, params, username);
        } catch (Exception e) {
            log.error("派单给劳务班组失败", e);
            return Result.error("派单给劳务班组失败：" + e.getMessage());
        }
    }

    /**
     * 劳务执行维修
     */
    @AutoLog(value = "劳务执行维修")
    @Operation(summary = "劳务执行维修", description = "劳务执行维修")
    @PostMapping("/labor-execution/{taskId}")
    public Result<String> laborExecution(@PathVariable String taskId,
                                        @RequestBody Map<String, Object> params,
                                        HttpServletRequest request) {
        try {
            String username = JwtUtil.getUserNameByToken(request);
            return completeTask(taskId, params, username);
        } catch (Exception e) {
            log.error("劳务执行维修失败", e);
            return Result.error("劳务执行维修失败：" + e.getMessage());
        }
    }

    /**
     * 质量验收
     */
    @AutoLog(value = "质量验收")
    @Operation(summary = "质量验收", description = "质量验收")
    @PostMapping("/quality-inspection/{taskId}")
    public Result<String> qualityInspection(@PathVariable String taskId,
                                           @RequestBody Map<String, Object> params,
                                           HttpServletRequest request) {
        try {
            String username = JwtUtil.getUserNameByToken(request);
            return completeTask(taskId, params, username);
        } catch (Exception e) {
            log.error("质量验收失败", e);
            return Result.error("质量验收失败：" + e.getMessage());
        }
    }

    /**
     * 客户满意度评价
     */
    @AutoLog(value = "客户满意度评价")
    @Operation(summary = "客户满意度评价", description = "客户满意度评价")
    @PostMapping("/customer-evaluation/{taskId}")
    public Result<String> customerEvaluation(@PathVariable String taskId,
                                            @RequestBody Map<String, Object> params,
                                            HttpServletRequest request) {
        try {
            String username = JwtUtil.getUserNameByToken(request);
            return completeTask(taskId, params, username);
        } catch (Exception e) {
            log.error("客户满意度评价失败", e);
            return Result.error("客户满意度评价失败：" + e.getMessage());
        }
    }

    /**
     * 获取工单当前任务
     */
    @AutoLog(value = "获取工单当前任务")
    @Operation(summary = "获取工单当前任务", description = "获取工单当前任务")
    @GetMapping("/current-task/{orderId}")
    public Result<Map<String, Object>> getCurrentTask(@PathVariable String orderId) {
        try {
            MaintenanceOrder order = maintenanceOrderService.getById(orderId);
            if (order == null) {
                return Result.error("工单不存在");
            }
            
            if (oConvertUtils.isEmpty(order.getProcessInstanceId())) {
                return Result.error("工单流程未启动");
            }
            
            // 查询当前活动任务
            Task currentTask = taskService.createTaskQuery()
                    .processInstanceId(order.getProcessInstanceId())
                    .active()
                    .singleResult();
            
            if (currentTask == null) {
                return Result.error("当前没有活动任务");
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", currentTask.getId());
            result.put("taskName", currentTask.getName());
            result.put("assignee", currentTask.getAssignee());
            result.put("createTime", currentTask.getCreateTime());
            result.put("taskDefinitionKey", currentTask.getTaskDefinitionKey());
            
            // 获取任务变量
            Map<String, Object> variables = taskService.getVariables(currentTask.getId());
            result.put("variables", variables);
            
            return Result.OK(result);
        } catch (Exception e) {
            log.error("获取工单当前任务失败", e);
            return Result.error("获取工单当前任务失败：" + e.getMessage());
        }
    }

    /**
     * 通用任务完成方法
     */
    private Result<String> completeTask(String taskId, Map<String, Object> params, String username) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return Result.error("任务不存在");
        }
        
        // 检查任务是否分配给当前用户
        if (!username.equals(task.getAssignee())) {
            return Result.error("无权完成此任务");
        }
        
        // 添加操作人信息
        params.put("operator", username);
        params.put("operationTime", new java.util.Date());
        
        String comment = (String) params.get("comment");
        if (oConvertUtils.isNotEmpty(comment)) {
            taskService.addComment(taskId, task.getProcessInstanceId(), comment);
        }
        
        // 完成任务
        taskService.complete(taskId, params);
        
        // 更新工单状态
        updateOrderStatus(task.getProcessInstanceId(), task.getTaskDefinitionKey());
        
        log.info("任务完成成功，任务ID：{}，操作人：{}", taskId, username);
        return Result.OK("任务完成成功");
    }

    /**
     * 根据任务类型更新工单状态
     */
    private void updateOrderStatus(String processInstanceId, String taskDefinitionKey) {
        try {
            MaintenanceOrder order = maintenanceOrderService.getByProcessInstanceId(processInstanceId);
            if (order != null) {
                String newStatus = mapTaskToOrderStatus(taskDefinitionKey);
                if (oConvertUtils.isNotEmpty(newStatus)) {
                    order.setCurrentStatus(newStatus);
                    maintenanceOrderService.updateById(order);
                }
            }
        } catch (Exception e) {
            log.error("更新工单状态失败", e);
        }
    }

    /**
     * 映射任务定义Key到工单状态
     */
    private String mapTaskToOrderStatus(String taskDefinitionKey) {
        switch (taskDefinitionKey) {
            case "technicianReview":
                return "TECHNICIAN_REVIEWING";
            case "warrantyCheck":
                return "WARRANTY_CHECKING";
            case "managerApproval":
            case "leaderApproval":
                return "PENDING_APPROVAL";
            case "assignToLaborTeam":
                return "PENDING_ASSIGNMENT";
            case "laborExecution":
                return "REPAIR_IN_PROGRESS";
            case "qualityInspection":
                return "PENDING_INSPECTION";
            case "customerEvaluation":
                return "PENDING_EVALUATION";
            default:
                return null;
        }
    }

    /**
     * 根据项目获取维修人员（待实现）
     */
    private String getTechnicianByProject(String projectId) {
        // TODO: 根据项目配置获取维修人员
        return "admin"; // 临时返回
    }

    /**
     * 根据项目获取项目经理（待实现）
     */
    private String getProjectManagerByProject(String projectId) {
        // TODO: 根据项目配置获取项目经理
        return "admin"; // 临时返回
    }

    /**
     * 根据项目获取领导（待实现）
     */
    private String getLeaderByProject(String projectId) {
        // TODO: 根据项目配置获取领导
        return "admin"; // 临时返回
    }

    /**
     * 根据项目获取验收员（待实现）
     */
    private String getInspectorByProject(String projectId) {
        // TODO: 根据项目配置获取验收员
        return "admin"; // 临时返回
    }

    /**
     * 根据劳务班组ID获取班组长（待实现）
     */
    private String getLaborTeamLeader(String laborTeamId) {
        // TODO: 根据劳务班组ID获取班组长
        return "admin"; // 临时返回
    }
}