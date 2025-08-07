package org.jeecg.modules.workflow.exception;

/**
 * 🎯 工作流业务异常
 * 用于处理工作流业务逻辑中的异常情况
 * 
 * @author jeecg
 * @since 2024-12-25
 */
public class WorkflowBusinessException extends RuntimeException {
    
    private String code;
    
    public WorkflowBusinessException(String message) {
        super(message);
        this.code = "WORKFLOW_BUSINESS_ERROR";
    }
    
    public WorkflowBusinessException(String code, String message) {
        super(message);
        this.code = code;
    }
    
    public WorkflowBusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = "WORKFLOW_BUSINESS_ERROR";
    }
    
    public WorkflowBusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
    // 静态工厂方法，提供常见的业务异常
    
    public static WorkflowBusinessException formNotFound(String formId) {
        return new WorkflowBusinessException("FORM_NOT_FOUND", "表单不存在：" + formId);
    }
    
    public static WorkflowBusinessException dataNotFound(String dataId) {
        return new WorkflowBusinessException("DATA_NOT_FOUND", "数据不存在：" + dataId);
    }
    
    public static WorkflowBusinessException processNotFound(String processInstanceId) {
        return new WorkflowBusinessException("PROCESS_NOT_FOUND", "流程实例不存在：" + processInstanceId);
    }
    
    public static WorkflowBusinessException taskNotFound(String taskId) {
        return new WorkflowBusinessException("TASK_NOT_FOUND", "任务不存在：" + taskId);
    }
    
    public static WorkflowBusinessException permissionConfigNotFound(String nodeId) {
        return new WorkflowBusinessException("PERMISSION_CONFIG_NOT_FOUND", "节点权限配置不存在：" + nodeId);
    }
    
    public static WorkflowBusinessException invalidFormData(String reason) {
        return new WorkflowBusinessException("INVALID_FORM_DATA", "表单数据无效：" + reason);
    }
    
    public static WorkflowBusinessException workflowNotEnabled(String formId) {
        return new WorkflowBusinessException("WORKFLOW_NOT_ENABLED", "表单未启用工作流：" + formId);
    }
    
    public static WorkflowBusinessException cannotStartWorkflow(String reason) {
        return new WorkflowBusinessException("CANNOT_START_WORKFLOW", "无法启动工作流：" + reason);
    }
    
    public static WorkflowBusinessException invalidPermissionConfig(String reason) {
        return new WorkflowBusinessException("INVALID_PERMISSION_CONFIG", "权限配置无效：" + reason);
    }
}