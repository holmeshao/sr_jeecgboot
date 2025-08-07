package org.jeecg.modules.workflow.exception;

/**
 * 🎯 工作流状态异常
 * 用于处理工作流状态相关的异常情况
 * 
 * @author jeecg
 * @since 2024-12-25
 */
public class WorkflowStateException extends RuntimeException {
    
    public WorkflowStateException(String message) {
        super(message);
    }
    
    public WorkflowStateException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // 静态工厂方法，提供常见的状态异常
    
    public static WorkflowStateException processAlreadyStarted(String dataId) {
        return new WorkflowStateException(String.format("数据 %s 的工作流已启动，无法重复启动", dataId));
    }
    
    public static WorkflowStateException processNotStarted(String dataId) {
        return new WorkflowStateException(String.format("数据 %s 的工作流尚未启动", dataId));
    }
    
    public static WorkflowStateException processCompleted(String processInstanceId) {
        return new WorkflowStateException(String.format("流程实例 %s 已完成，无法继续操作", processInstanceId));
    }
    
    public static WorkflowStateException processSuspended(String processInstanceId) {
        return new WorkflowStateException(String.format("流程实例 %s 已挂起，请先激活后再操作", processInstanceId));
    }
    
    public static WorkflowStateException taskCompleted(String taskId) {
        return new WorkflowStateException(String.format("任务 %s 已完成，无法再次操作", taskId));
    }
    
    public static WorkflowStateException taskSuspended(String taskId) {
        return new WorkflowStateException(String.format("任务 %s 已挂起，无法操作", taskId));
    }
    
    public static WorkflowStateException invalidStatus(String currentStatus, String expectedStatus) {
        return new WorkflowStateException(String.format("当前状态 %s 无效，期望状态为 %s", currentStatus, expectedStatus));
    }
    
    public static WorkflowStateException cannotTransition(String fromStatus, String toStatus) {
        return new WorkflowStateException(String.format("无法从状态 %s 转换到状态 %s", fromStatus, toStatus));
    }
    
    public static WorkflowStateException formStatusMismatch(String formStatus, String expectedStatus) {
        return new WorkflowStateException(String.format("表单状态 %s 与期望状态 %s 不匹配", formStatus, expectedStatus));
    }
    
    public static WorkflowStateException dataStatusInvalid(String dataId, String status) {
        return new WorkflowStateException(String.format("数据 %s 的状态 %s 无效", dataId, status));
    }
    
    public static WorkflowStateException concurrentModification(String dataId) {
        return new WorkflowStateException(String.format("数据 %s 已被其他用户修改，请刷新后重试", dataId));
    }
}