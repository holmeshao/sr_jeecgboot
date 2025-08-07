package org.jeecg.modules.workflow.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工作流任务管理Controller
 *
 * @author jeecg
 * @since 2025-01-24
 */
@Tag(name = "工作流任务管理")
@RestController
@RequestMapping("/workflow/task")
@Slf4j
public class WorkflowTaskController {

    @Autowired
    private TaskService taskService;
    
    @Autowired
    private HistoryService historyService;

    /**
     * 获取我的待办任务
     */
    @AutoLog(value = "获取我的待办任务")
    @Operation(summary = "获取我的待办任务", description = "获取我的待办任务")
    @GetMapping("/my")
    public Result<Map<String, Object>> getMyTasks(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String processDefinitionKey,
            @RequestParam(required = false) String taskName,
            HttpServletRequest request) {
        
        try {
            String username = JwtUtil.getUserNameByToken(request);
            if (oConvertUtils.isEmpty(username)) {
                return Result.error("用户未登录");
            }
            
            TaskQuery query = taskService.createTaskQuery()
                    .taskAssignee(username)
                    .active();
            
            // 添加查询条件
            if (oConvertUtils.isNotEmpty(processDefinitionKey)) {
                query.processDefinitionKey(processDefinitionKey);
            }
            if (oConvertUtils.isNotEmpty(taskName)) {
                query.taskNameLike("%" + taskName + "%");
            }
            
            // 按创建时间倒序
            query.orderByTaskCreateTime().desc();
            
            // 分页查询
            long total = query.count();
            List<Task> tasks = query.listPage((pageNo - 1) * pageSize, pageSize);
            
            // 转换为前端需要的格式
            List<Map<String, Object>> records = tasks.stream()
                    .map(this::convertTask)
                    .collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", records);
            result.put("total", total);
            result.put("current", pageNo);
            result.put("size", pageSize);
            
            return Result.OK(result);
        } catch (Exception e) {
            log.error("获取我的待办任务失败", e);
            return Result.error("获取我的待办任务失败：" + e.getMessage());
        }
    }

    /**
     * 获取任务列表（管理员使用）
     */
    @AutoLog(value = "获取任务列表")
    @Operation(summary = "获取任务列表", description = "获取任务列表")
    @GetMapping("/list")
    public Result<Map<String, Object>> getTaskList(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String processDefinitionKey,
            @RequestParam(required = false) String assignee,
            @RequestParam(required = false) String taskName) {
        
        try {
            TaskQuery query = taskService.createTaskQuery().active();
            
            // 添加查询条件
            if (oConvertUtils.isNotEmpty(processDefinitionKey)) {
                query.processDefinitionKey(processDefinitionKey);
            }
            if (oConvertUtils.isNotEmpty(assignee)) {
                query.taskAssignee(assignee);
            }
            if (oConvertUtils.isNotEmpty(taskName)) {
                query.taskNameLike("%" + taskName + "%");
            }
            
            // 按创建时间倒序
            query.orderByTaskCreateTime().desc();
            
            // 分页查询
            long total = query.count();
            List<Task> tasks = query.listPage((pageNo - 1) * pageSize, pageSize);
            
            // 转换为前端需要的格式
            List<Map<String, Object>> records = tasks.stream()
                    .map(this::convertTask)
                    .collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", records);
            result.put("total", total);
            result.put("current", pageNo);
            result.put("size", pageSize);
            
            return Result.OK(result);
        } catch (Exception e) {
            log.error("获取任务列表失败", e);
            return Result.error("获取任务列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取任务详情
     */
    @AutoLog(value = "获取任务详情")
    @Operation(summary = "获取任务详情", description = "获取任务详情")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getTaskDetail(@PathVariable String id) {
        try {
            Task task = taskService.createTaskQuery().taskId(id).singleResult();
            if (task == null) {
                return Result.error("任务不存在");
            }
            
            Map<String, Object> result = convertTask(task);
            
            // 获取任务变量
            Map<String, Object> variables = taskService.getVariables(id);
            result.put("variables", variables);
            
            return Result.OK(result);
        } catch (Exception e) {
            log.error("获取任务详情失败", e);
            return Result.error("获取任务详情失败：" + e.getMessage());
        }
    }

    /**
     * 完成任务
     */
    @AutoLog(value = "完成任务")
    @Operation(summary = "完成任务", description = "完成任务")
    @PutMapping("/{id}/complete")
    public Result<String> completeTask(
            @PathVariable String id,
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        
        try {
            String username = JwtUtil.getUserNameByToken(request);
            if (oConvertUtils.isEmpty(username)) {
                return Result.error("用户未登录");
            }
            
            Task task = taskService.createTaskQuery().taskId(id).singleResult();
            if (task == null) {
                return Result.error("任务不存在");
            }
            
            // 检查任务是否分配给当前用户
            if (!username.equals(task.getAssignee())) {
                return Result.error("无权完成此任务");
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> variables = (Map<String, Object>) params.get("variables");
            String comment = (String) params.get("comment");
            
            // 添加审批意见
            if (oConvertUtils.isNotEmpty(comment)) {
                taskService.addComment(id, task.getProcessInstanceId(), comment);
            }
            
            // 完成任务
            taskService.complete(id, variables);
            
            log.info("任务完成成功，任务ID：{}, 操作人：{}", id, username);
            return Result.OK("任务完成成功");
            
        } catch (Exception e) {
            log.error("完成任务失败", e);
            return Result.error("完成任务失败：" + e.getMessage());
        }
    }

    /**
     * 委托任务
     */
    @AutoLog(value = "委托任务")
    @Operation(summary = "委托任务", description = "委托任务")
    @PutMapping("/{id}/delegate")
    public Result<String> delegateTask(
            @PathVariable String id,
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        
        try {
            String username = JwtUtil.getUserNameByToken(request);
            String delegateUser = (String) params.get("delegateUser");
            String reason = (String) params.get("reason");
            
            if (oConvertUtils.isEmpty(delegateUser)) {
                return Result.error("委托用户不能为空");
            }
            
            Task task = taskService.createTaskQuery().taskId(id).singleResult();
            if (task == null) {
                return Result.error("任务不存在");
            }
            
            // 检查任务是否分配给当前用户
            if (!username.equals(task.getAssignee())) {
                return Result.error("无权委托此任务");
            }
            
            // 委托任务
            taskService.delegateTask(id, delegateUser);
            
            // 添加委托说明
            if (oConvertUtils.isNotEmpty(reason)) {
                taskService.addComment(id, task.getProcessInstanceId(), 
                    "任务委托给 " + delegateUser + "，原因：" + reason);
            }
            
            log.info("任务委托成功，任务ID：{}, 委托人：{}, 被委托人：{}", id, username, delegateUser);
            return Result.OK("任务委托成功");
            
        } catch (Exception e) {
            log.error("委托任务失败", e);
            return Result.error("委托任务失败：" + e.getMessage());
        }
    }

    /**
     * 转办任务
     */
    @AutoLog(value = "转办任务")
    @Operation(summary = "转办任务", description = "转办任务")
    @PutMapping("/{id}/transfer")
    public Result<String> transferTask(
            @PathVariable String id,
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        
        try {
            String username = JwtUtil.getUserNameByToken(request);
            String transferUser = (String) params.get("transferUser");
            String reason = (String) params.get("reason");
            
            if (oConvertUtils.isEmpty(transferUser)) {
                return Result.error("转办用户不能为空");
            }
            
            Task task = taskService.createTaskQuery().taskId(id).singleResult();
            if (task == null) {
                return Result.error("任务不存在");
            }
            
            // 检查任务是否分配给当前用户
            if (!username.equals(task.getAssignee())) {
                return Result.error("无权转办此任务");
            }
            
            // 转办任务
            taskService.setAssignee(id, transferUser);
            
            // 添加转办说明
            if (oConvertUtils.isNotEmpty(reason)) {
                taskService.addComment(id, task.getProcessInstanceId(), 
                    "任务转办给 " + transferUser + "，原因：" + reason);
            }
            
            log.info("任务转办成功，任务ID：{}, 转办人：{}, 接收人：{}", id, username, transferUser);
            return Result.OK("任务转办成功");
            
        } catch (Exception e) {
            log.error("转办任务失败", e);
            return Result.error("转办任务失败：" + e.getMessage());
        }
    }

    /**
     * 获取任务表单数据
     */
    @AutoLog(value = "获取任务表单数据")
    @Operation(summary = "获取任务表单数据", description = "获取任务表单数据")
    @GetMapping("/{id}/form")
    public Result<Map<String, Object>> getTaskForm(@PathVariable String id) {
        try {
            Task task = taskService.createTaskQuery().taskId(id).singleResult();
            if (task == null) {
                return Result.error("任务不存在");
            }
            
            // 获取任务变量
            Map<String, Object> variables = taskService.getVariables(id);
            
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", id);
            result.put("taskName", task.getName());
            result.put("processInstanceId", task.getProcessInstanceId());
            result.put("variables", variables);
            result.put("formData", new HashMap<>());  // 这里可以根据需要加载具体的表单定义
            
            return Result.OK(result);
        } catch (Exception e) {
            log.error("获取任务表单数据失败", e);
            return Result.error("获取任务表单数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取历史任务列表
     */
    @AutoLog(value = "获取历史任务列表")
    @Operation(summary = "获取历史任务列表", description = "获取历史任务列表")
    @GetMapping("/history")
    public Result<Map<String, Object>> getHistoryTasks(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String processInstanceId,
            @RequestParam(required = false) String assignee) {
        
        try {
            HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();
            
            // 添加查询条件
            if (oConvertUtils.isNotEmpty(processInstanceId)) {
                query.processInstanceId(processInstanceId);
            }
            if (oConvertUtils.isNotEmpty(assignee)) {
                query.taskAssignee(assignee);
            }
            
            // 按开始时间倒序
            query.orderByHistoricTaskInstanceStartTime().desc();
            
            // 分页查询
            long total = query.count();
            List<HistoricTaskInstance> tasks = query.listPage((pageNo - 1) * pageSize, pageSize);
            
            // 转换为前端需要的格式
            List<Map<String, Object>> records = tasks.stream()
                    .map(this::convertHistoricTask)
                    .collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", records);
            result.put("total", total);
            result.put("current", pageNo);
            result.put("size", pageSize);
            
            return Result.OK(result);
        } catch (Exception e) {
            log.error("获取历史任务列表失败", e);
            return Result.error("获取历史任务列表失败：" + e.getMessage());
        }
    }

    /**
     * 转换Task为前端需要的格式
     */
    private Map<String, Object> convertTask(Task task) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", task.getId());
        result.put("name", task.getName());
        result.put("description", task.getDescription());
        result.put("assignee", task.getAssignee());
        result.put("owner", task.getOwner());
        result.put("createTime", task.getCreateTime());
        result.put("dueDate", task.getDueDate());
        result.put("priority", task.getPriority());
        result.put("processInstanceId", task.getProcessInstanceId());
        result.put("processDefinitionId", task.getProcessDefinitionId());
        result.put("taskDefinitionKey", task.getTaskDefinitionKey());
        result.put("suspended", task.isSuspended());
        result.put("tenantId", task.getTenantId());
        
        return result;
    }

    /**
     * 转换HistoricTaskInstance为前端需要的格式
     */
    private Map<String, Object> convertHistoricTask(HistoricTaskInstance task) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", task.getId());
        result.put("name", task.getName());
        result.put("description", task.getDescription());
        result.put("assignee", task.getAssignee());
        result.put("owner", task.getOwner());
        result.put("startTime", task.getStartTime());
        result.put("endTime", task.getEndTime());
        result.put("duration", task.getDurationInMillis());
        result.put("deleteReason", task.getDeleteReason());
        result.put("priority", task.getPriority());
        result.put("processInstanceId", task.getProcessInstanceId());
        result.put("processDefinitionId", task.getProcessDefinitionId());
        result.put("taskDefinitionKey", task.getTaskDefinitionKey());
        result.put("tenantId", task.getTenantId());
        
        return result;
    }
}