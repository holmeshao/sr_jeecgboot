package org.jeecg.modules.workflow.exception;

/**
 * 🎯 工作流权限异常
 * 用于处理工作流权限相关的异常情况
 * 
 * @author jeecg
 * @since 2024-12-25
 */
public class WorkflowPermissionException extends RuntimeException {
    
    public WorkflowPermissionException(String message) {
        super(message);
    }
    
    public WorkflowPermissionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // 静态工厂方法，提供常见的权限异常
    
    public static WorkflowPermissionException noPermissionToStart(String formId, String user) {
        return new WorkflowPermissionException(String.format("用户 %s 没有权限启动表单 %s 的工作流", user, formId));
    }
    
    public static WorkflowPermissionException noPermissionToEdit(String taskId, String user) {
        return new WorkflowPermissionException(String.format("用户 %s 没有权限编辑任务 %s", user, taskId));
    }
    
    public static WorkflowPermissionException noPermissionToApprove(String taskId, String user) {
        return new WorkflowPermissionException(String.format("用户 %s 没有权限审批任务 %s", user, taskId));
    }
    
    public static WorkflowPermissionException fieldReadonly(String fieldName, String nodeId) {
        return new WorkflowPermissionException(String.format("字段 %s 在节点 %s 为只读，无法修改", fieldName, nodeId));
    }
    
    public static WorkflowPermissionException fieldHidden(String fieldName, String nodeId) {
        return new WorkflowPermissionException(String.format("字段 %s 在节点 %s 已隐藏，无法操作", fieldName, nodeId));
    }
    
    public static WorkflowPermissionException notAssignee(String taskId, String user) {
        return new WorkflowPermissionException(String.format("用户 %s 不是任务 %s 的处理人", user, taskId));
    }
    
    public static WorkflowPermissionException notInCandidateGroup(String taskId, String user) {
        return new WorkflowPermissionException(String.format("用户 %s 不在任务 %s 的候选组中", user, taskId));
    }
    
    public static WorkflowPermissionException taskAlreadyClaimed(String taskId, String claimedBy) {
        return new WorkflowPermissionException(String.format("任务 %s 已被 %s 认领", taskId, claimedBy));
    }
}