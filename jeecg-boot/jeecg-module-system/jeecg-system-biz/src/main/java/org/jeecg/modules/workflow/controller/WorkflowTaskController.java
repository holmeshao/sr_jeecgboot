package org.jeecg.modules.workflow.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.api.IdentityLinkType;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.workflow.dto.WorkflowTaskContext;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
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

    @Autowired(required = false)
    private RuntimeService runtimeService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 获取我的待办任务
     */
    @AutoLog(value = "获取我的待办任务")
    @Operation(summary = "获取我的待办任务", description = "获取我的待办任务")
    @GetMapping("/my")
    @RequiresPermissions("workflow:task:my")
    public Result<Map<String, Object>> getMyTasks(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String processDefinitionKey,
            @RequestParam(required = false) String taskName,
            @RequestParam(required = false) String processInstanceId,
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
            if (oConvertUtils.isNotEmpty(processInstanceId)) {
                query.processInstanceId(processInstanceId);
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
     * 认领任务（别名）
     */
    @AutoLog(value = "认领任务")
    @Operation(summary = "认领任务", description = "认领任务")
    @PostMapping("/claim")
    @RequiresPermissions("workflow:task:claim")
    public Result<String> claimTask(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        try {
            String username = JwtUtil.getUserNameByToken(request);
            if (oConvertUtils.isEmpty(username)) {
                return Result.error("用户未登录");
            }
            String taskId = (String) params.get("taskId");
            if (oConvertUtils.isEmpty(taskId)) {
                return Result.error("taskId不能为空");
            }
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                return Result.error("任务不存在");
            }
            if (oConvertUtils.isEmpty(task.getAssignee())) {
                taskService.claim(taskId, username);
                return Result.OK("任务认领成功");
            } else if (username.equals(task.getAssignee())) {
                return Result.OK("已认领");
            } else {
                return Result.error("任务已被其他人认领");
            }
        } catch (Exception e) {
            log.error("认领任务失败", e);
            return Result.error("认领任务失败：" + e.getMessage());
        }
    }

    /**
     * 释放任务（别名）
     */
    @AutoLog(value = "释放任务")
    @Operation(summary = "释放任务", description = "释放任务")
    @PostMapping("/unclaim")
    @RequiresPermissions("workflow:task:unclaim")
    public Result<String> unclaimTask(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        try {
            String username = JwtUtil.getUserNameByToken(request);
            if (oConvertUtils.isEmpty(username)) {
                return Result.error("用户未登录");
            }
            String taskId = (String) params.get("taskId");
            if (oConvertUtils.isEmpty(taskId)) {
                return Result.error("taskId不能为空");
            }
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                return Result.error("任务不存在");
            }
            if (!username.equals(task.getAssignee())) {
                return Result.error("仅任务认领人可释放");
            }
            taskService.unclaim(taskId);
            return Result.OK("任务释放成功");
        } catch (Exception e) {
            log.error("释放任务失败", e);
            return Result.error("释放任务失败：" + e.getMessage());
        }
    }

    /**
     * 获取任务列表（管理员使用）
     */
    @AutoLog(value = "获取任务列表")
    @Operation(summary = "获取任务列表", description = "获取任务列表")
    @GetMapping("/list")
    @RequiresPermissions("workflow:task:list")
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
    @RequiresPermissions("workflow:task:view")
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
    @RequiresPermissions("workflow:task:complete")
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
            Object snapshotObj = params.get("snapshot");
            
            // 添加审批意见
            if (oConvertUtils.isNotEmpty(comment)) {
                taskService.addComment(id, task.getProcessInstanceId(), comment);
            }

            // 记录表单快照为流程变量（用于版本对比/历史）
            try {
                if (runtimeService != null && snapshotObj != null) {
                    String nodeId = task.getTaskDefinitionKey();
                    String varName = "form_snapshot_" + nodeId + "_" + id;
                    String latestPtr = "form_snapshot_latest_" + nodeId;
                    String jsonVal = com.alibaba.fastjson.JSON.toJSONString(snapshotObj);
                    runtimeService.setVariable(task.getExecutionId(), varName, jsonVal);
                    // 最新指针：记录最近一次任务ID
                    runtimeService.setVariable(task.getExecutionId(), latestPtr, id);
                }
            } catch (Exception ignore) {
                // 忽略快照失败，不影响办理
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
     * 添加任务评论
     */
    @AutoLog(value = "添加任务评论")
    @Operation(summary = "添加任务评论", description = "为指定任务添加处理意见")
    @PostMapping("/{id}/comment")
    public Result<String> addComment(
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

            String message = params == null ? null : (String) params.get("message");
            if (oConvertUtils.isEmpty(message)) {
                return Result.error("评论内容不能为空");
            }

            taskService.addComment(id, task.getProcessInstanceId(), message);
            return Result.OK("评论已添加");
        } catch (Exception e) {
            log.error("添加任务评论失败", e);
            return Result.error("添加任务评论失败：" + e.getMessage());
        }
    }

    /**
     * 兼容别名：POST /workflow/task/complete
     */
    @AutoLog(value = "完成任务(别名)")
    @Operation(summary = "完成任务(别名)", description = "兼容 /workflow/task/complete 调用")
    @PostMapping("/complete")
    @RequiresPermissions("workflow:task:complete")
    public Result<String> completeTaskAlias(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        String taskId = (String) body.get("taskId");
        if (oConvertUtils.isEmpty(taskId)) {
            return Result.error("taskId不能为空");
        }
        return completeTask(taskId, body, request);
    }

    /**
     * 委托任务
     */
    @AutoLog(value = "委托任务")
    @Operation(summary = "委托任务", description = "委托任务")
    @PutMapping("/{id}/delegate")
    @RequiresPermissions("workflow:task:delegate")
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
    @RequiresPermissions("workflow:task:transfer")
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
    @RequiresPermissions("workflow:task:view")
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
    @RequiresPermissions("workflow:task:history")
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

    // ============== 工作流按钮上下文 API ==============

    /**
     * 获取工作流任务上下文
     * 用于前端判断应该显示哪些按钮
     */
    @AutoLog(value = "获取工作流任务上下文")
    @Operation(summary = "获取工作流任务上下文", description = "获取工作流任务上下文，用于前端判断应该显示哪些按钮")
    @GetMapping("/button-context")
    public Result<WorkflowTaskContext> getTaskContext(
            @RequestParam(required = false) String taskId,
            @RequestParam(required = false) String processInstanceId,
            @RequestParam(required = false) String businessKey,
            @RequestParam(required = false, defaultValue = "false") Boolean viewOnly,
            HttpServletRequest request) {
        
        try {
            String username = JwtUtil.getUserNameByToken(request);
            if (oConvertUtils.isEmpty(username)) {
                return Result.error("用户未登录");
            }
            
            // 通过用户名获取当前登录人的系统用户ID，优先使用 ID 与 Flowable 中的 userId 对齐
            String userId = null;
            if (sysUserService != null) {
                SysUser sysUser = sysUserService.getUserByName(username);
                if (sysUser != null) {
                    userId = sysUser.getId();
                }
            }
            
            WorkflowTaskContext context = new WorkflowTaskContext();
            
            // 场景1：有 taskId，获取具体任务的上下文
            if (oConvertUtils.isNotEmpty(taskId)) {
                return Result.OK(buildContextByTaskId(taskId, username, userId, viewOnly));
            }
            
            // 场景2：有 processInstanceId，获取流程实例的当前任务上下文
            if (oConvertUtils.isNotEmpty(processInstanceId)) {
                return Result.OK(buildContextByProcessInstanceId(processInstanceId, username, userId, viewOnly));
            }
            
            // 场景3：只有 businessKey，查找关联的流程实例
            if (oConvertUtils.isNotEmpty(businessKey)) {
                return Result.OK(buildContextByBusinessKey(businessKey, username, userId, viewOnly));
            }
            
            // 场景4：都没有，返回新建表单的上下文
            context = WorkflowTaskContext.forNewForm();
            context.setAvailableButtons(buildButtons(context));
            return Result.OK(context);
            
        } catch (Exception e) {
            log.error("获取工作流任务上下文失败", e);
            return Result.error("获取工作流任务上下文失败：" + e.getMessage());
        }
    }

    /**
     * 根据 taskId 构建上下文
     */
    private WorkflowTaskContext buildContextByTaskId(String taskId, String username, String userId, Boolean viewOnly) {
        WorkflowTaskContext context = new WorkflowTaskContext();
        
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            // 任务不存在，可能已完成，尝试从历史中查询
            HistoricTaskInstance historicTask = historyService.createHistoricTaskInstanceQuery()
                    .taskId(taskId).singleResult();
            if (historicTask != null) {
                context.setHasTask(false);
                context.setTaskId(taskId);
                context.setTaskName(historicTask.getName());
                context.setProcessInstanceId(historicTask.getProcessInstanceId());
                context.setProcessStatus("COMPLETED");
                context.setReadOnly(true);
                context.setAvailableButtons(buildButtons(context));
                return context;
            }
            // 任务完全不存在
            context.setHasTask(false);
            context.setReadOnly(true);
            context.setAvailableButtons(buildButtons(context));
            return context;
        }
        
        // 任务存在，填充上下文
        context.setHasTask(true);
        context.setTaskId(task.getId());
        context.setTaskName(task.getName());
        context.setTaskDefinitionKey(task.getTaskDefinitionKey());
        context.setProcessInstanceId(task.getProcessInstanceId());
        context.setAssignee(task.getAssignee());
        
        // 获取流程实例信息
        if (runtimeService != null && oConvertUtils.isNotEmpty(task.getProcessInstanceId())) {
            try {
                ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(task.getProcessInstanceId())
                        .singleResult();
                if (pi != null) {
                    context.setProcessDefinitionKey(pi.getProcessDefinitionKey());
                    context.setBusinessKey(pi.getBusinessKey());
                    // String 类型：发起人（可能是用户名或用户ID）
                    String starter = pi.getStartUserId();
                    context.setStarter(starter);
                    context.setProcessStatus(pi.isSuspended() ? "SUSPENDED" : "RUNNING");
                    // 布尔标记：当前用户是否为发起人（兼容用户名与用户ID）
                    boolean starterFlag = false;
                    if (starter != null) {
                        starterFlag = starter.equals(username)
                                || (userId != null && starter.equals(userId));
                    }
                    context.setStarterFlag(starterFlag);
                }
            } catch (Exception e) {
                log.debug("获取流程实例信息失败: {}", e.getMessage());
            }
        }
        
        // 判断当前用户的权限（兼容用户名与用户ID）
        String assignee = task.getAssignee();
        boolean assigneeFlag = false;
        if (assignee != null) {
            assigneeFlag = assignee.equals(username)
                    || (userId != null && assignee.equals(userId));
        }
        // 布尔标记：当前用户是否为任务处理人
        context.setAssigneeFlag(assigneeFlag);
        
        // 获取候选人信息
        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(taskId);
        List<String> candidateUsers = identityLinks.stream()
                .filter(link -> IdentityLinkType.CANDIDATE.equals(link.getType()) && link.getUserId() != null)
                .map(IdentityLink::getUserId)
                .collect(java.util.stream.Collectors.toList());
        List<String> candidateGroups = identityLinks.stream()
                .filter(link -> IdentityLinkType.CANDIDATE.equals(link.getType()) && link.getGroupId() != null)
                .map(IdentityLink::getGroupId)
                .collect(java.util.stream.Collectors.toList());
        
        context.setCandidateUsers(candidateUsers);
        context.setCandidateGroups(candidateGroups);
        
        // 判断是否是候选人（兼容用户名与用户ID）
        boolean candidateFlag = false;
        if (userId != null && candidateUsers.contains(userId)) {
            candidateFlag = true;
        } else if (candidateUsers.contains(username)) {
            candidateFlag = true;
        }
        // TODO: 也可以检查用户是否属于候选组
        context.setCandidateFlag(candidateFlag);
        
        // 判断任务状态和可执行操作
        if (oConvertUtils.isEmpty(task.getAssignee())) {
            // 未分配（待认领）
            context.setTaskStatus("UNASSIGNED");
            context.setCanClaim(candidateFlag);
            context.setCanUnclaim(false);
            context.setCanApprove(false);
            context.setCanSave(false);
        } else if (assigneeFlag) {
            // 已分配给当前用户
            context.setTaskStatus("ASSIGNED");
            context.setCanClaim(false);
            // 如果是候选任务被认领的，可以释放；如果是直接分配的，不能释放
            context.setCanUnclaim(!candidateUsers.isEmpty() || !candidateGroups.isEmpty());
            context.setCanApprove(true);
            context.setCanSave(true);
        } else {
            // 已分配给其他人
            context.setTaskStatus("ASSIGNED");
            context.setCanClaim(false);
            context.setCanUnclaim(false);
            context.setCanApprove(false);
            context.setCanSave(false);
        }
        
        // 只读模式覆盖
        if (Boolean.TRUE.equals(viewOnly)) {
            context.setReadOnly(true);
            context.setCanClaim(false);
            context.setCanUnclaim(false);
            context.setCanApprove(false);
            context.setCanSave(false);
            context.setCanSubmit(false);
        } else {
            context.setReadOnly(!assigneeFlag && !context.isCanClaim());
        }
        
        // 构建可用按钮
        context.setAvailableButtons(buildButtons(context));
        
        return context;
    }

    /**
     * 根据 processInstanceId 构建上下文
     */
    private WorkflowTaskContext buildContextByProcessInstanceId(String processInstanceId, String username, String userId, Boolean viewOnly) {
        // 查找当前用户在该流程中的待办任务（兼容用户名与用户ID）
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee(username)
                .singleResult();
        if (task == null && userId != null) {
            task = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .taskAssignee(userId)
                    .singleResult();
        }
        
        if (task != null) {
            return buildContextByTaskId(task.getId(), username, userId, viewOnly);
        }
        
        // 查找当前用户可以认领的任务（兼容用户名与用户ID）
        task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskCandidateUser(username)
                .singleResult();
        if (task == null && userId != null) {
            task = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .taskCandidateUser(userId)
                    .singleResult();
        }
        
        if (task != null) {
            return buildContextByTaskId(task.getId(), username, userId, viewOnly);
        }
        
        // 没有待办任务，可能是查看模式或流程已结束
        WorkflowTaskContext context = new WorkflowTaskContext();
        context.setHasTask(false);
        context.setProcessInstanceId(processInstanceId);
        
        // 检查流程是否还在运行
        if (runtimeService != null) {
            ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            if (pi != null) {
                context.setProcessStatus(pi.isSuspended() ? "SUSPENDED" : "RUNNING");
                context.setProcessDefinitionKey(pi.getProcessDefinitionKey());
                context.setBusinessKey(pi.getBusinessKey());
                // String 类型：发起人（可能是用户名或用户ID）
                String starter = pi.getStartUserId();
                context.setStarter(starter);
                // 布尔标记：当前用户是否为发起人（兼容用户名与用户ID）
                boolean starterFlag = false;
                if (starter != null) {
                    starterFlag = starter.equals(username)
                            || (userId != null && starter.equals(userId));
                }
                context.setStarterFlag(starterFlag);
                
                // 如果是发起人，且流程还在第一个节点，可以撤回
                // TODO: 实现撤回逻辑
            } else {
                context.setProcessStatus("COMPLETED");
            }
        }
        
        context.setReadOnly(true);
        context.setAvailableButtons(buildButtons(context));
        
        return context;
    }

    /**
     * 根据 businessKey 构建上下文
     */
    private WorkflowTaskContext buildContextByBusinessKey(String businessKey, String username, String userId, Boolean viewOnly) {
        // 根据 businessKey 查找流程实例
        if (runtimeService != null) {
            ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                    .processInstanceBusinessKey(businessKey)
                    .singleResult();
            if (pi != null) {
                return buildContextByProcessInstanceId(pi.getId(), username, userId, viewOnly);
            }
        }
        
        // 没有找到流程实例，可能是新建或草稿状态
        WorkflowTaskContext context = WorkflowTaskContext.forNewForm();
        context.setBusinessKey(businessKey);
        context.setAvailableButtons(buildButtons(context));
        return context;
    }

    /**
     * 根据上下文构建可用按钮列表
     */
    private List<WorkflowTaskContext.WorkflowButton> buildButtons(WorkflowTaskContext context) {
        List<WorkflowTaskContext.WorkflowButton> buttons = new java.util.ArrayList<>();
        
        // 只读模式只显示关闭按钮
        if (context.isReadOnly()) {
            buttons.add(new WorkflowTaskContext.WorkflowButton("close", "关闭", "default", "ant-design:close-outlined", 99));
            return buttons;
        }
        
        // 新建/草稿模式
        if (!context.isHasTask() && context.isCanSubmit()) {
            buttons.add(new WorkflowTaskContext.WorkflowButton("submit", "提交审核", "primary", "ant-design:send-outlined", 1));
            if (context.isCanSave()) {
                buttons.add(new WorkflowTaskContext.WorkflowButton("save", "保存草稿", "default", "ant-design:save-outlined", 2));
            }
            buttons.add(new WorkflowTaskContext.WorkflowButton("cancel", "取消", "default", "ant-design:close-outlined", 99));
            return buttons;
        }
        
        // 待认领模式
        if (context.isCanClaim()) {
            buttons.add(new WorkflowTaskContext.WorkflowButton("claim", "认领", "primary", "ant-design:user-add-outlined", 1));
            buttons.add(new WorkflowTaskContext.WorkflowButton("cancel", "取消", "default", "ant-design:close-outlined", 99));
            return buttons;
        }
        
        // 审批模式（当前用户是处理人）
        if (context.isCanApprove()) {
            buttons.add(new WorkflowTaskContext.WorkflowButton("approve", "通过", "primary", "ant-design:check-outlined", 1));
            buttons.add(new WorkflowTaskContext.WorkflowButton("reject", "驳回", "danger", "ant-design:close-outlined", 2));
            if (context.isCanUnclaim()) {
                buttons.add(new WorkflowTaskContext.WorkflowButton("unclaim", "释放", "warning", "ant-design:user-delete-outlined", 3));
            }
            if (context.isCanSave()) {
                buttons.add(new WorkflowTaskContext.WorkflowButton("save", "保存", "default", "ant-design:save-outlined", 4));
            }
            buttons.add(new WorkflowTaskContext.WorkflowButton("cancel", "取消", "default", "ant-design:close-outlined", 99));
            return buttons;
        }
        
        // 默认只显示取消
        buttons.add(new WorkflowTaskContext.WorkflowButton("cancel", "取消", "default", "ant-design:close-outlined", 99));
        return buttons;
    }
}