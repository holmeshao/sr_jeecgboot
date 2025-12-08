package org.jeecg.modules.workflow.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 工作流任务上下文
 * 用于前端判断应该显示哪些按钮
 */
@Data
public class WorkflowTaskContext implements Serializable {
    private static final long serialVersionUID = 1L;

    // ============== 基础信息 ==============
    
    /**
     * 是否有关联的任务
     */
    private boolean hasTask;
    
    /**
     * 任务ID
     */
    private String taskId;
    
    /**
     * 任务名称（节点名称）
     */
    private String taskName;
    
    /**
     * 任务定义Key
     */
    private String taskDefinitionKey;
    
    /**
     * 流程实例ID
     */
    private String processInstanceId;
    
    /**
     * 流程定义Key
     */
    private String processDefinitionKey;
    
    /**
     * 业务Key（通常是表单数据ID）
     */
    private String businessKey;
    
    // ============== 任务状态 ==============
    
    /**
     * 任务状态：UNASSIGNED(待认领), ASSIGNED(已分配), COMPLETED(已完成)
     */
    private String taskStatus;
    
    /**
     * 流程状态：NOT_STARTED(未启动), RUNNING(运行中), COMPLETED(已完成), SUSPENDED(已挂起), TERMINATED(已终止)
     */
    private String processStatus;
    
    // ============== 人员信息 ==============
    
    /**
     * 当前任务处理人
     */
    private String assignee;
    
    /**
     * 候选用户列表
     */
    private List<String> candidateUsers;
    
    /**
     * 候选组列表
     */
    private List<String> candidateGroups;
    
    /**
     * 流程发起人
     */
    private String starter;
    
    // ============== 权限判断（核心） ==============
    
    /**
     * 当前用户是否是任务处理人
     */
    private boolean assigneeFlag;
    
    /**
     * 当前用户是否是候选人（可认领）
     */
    private boolean candidateFlag;
    
    /**
     * 当前用户是否是流程发起人
     */
    private boolean starterFlag;
    
    // ============== 可执行操作 ==============
    
    /**
     * 是否可以认领任务
     */
    private boolean canClaim;
    
    /**
     * 是否可以释放任务（取消认领）
     */
    private boolean canUnclaim;
    
    /**
     * 是否可以审批（通过/驳回）
     */
    private boolean canApprove;
    
    /**
     * 是否可以撤回
     */
    private boolean canWithdraw;
    
    /**
     * 是否可以提交（发起流程）
     */
    private boolean canSubmit;
    
    /**
     * 是否可以保存草稿
     */
    private boolean canSave;
    
    /**
     * 是否只读模式（查看详情）
     */
    private boolean readOnly;
    
    // ============== 可用按钮列表 ==============
    
    /**
     * 可用的操作按钮列表
     */
    private List<WorkflowButton> availableButtons;
    
    /**
     * 工作流按钮
     */
    @Data
    public static class WorkflowButton implements Serializable {
        private static final long serialVersionUID = 1L;
        
        /**
         * 按钮编码
         */
        private String code;
        
        /**
         * 按钮名称
         */
        private String name;
        
        /**
         * 按钮类型：primary, default, danger, warning
         */
        private String type;
        
        /**
         * 按钮图标
         */
        private String icon;
        
        /**
         * 排序
         */
        private int order;
        
        public WorkflowButton() {}
        
        public WorkflowButton(String code, String name, String type, int order) {
            this.code = code;
            this.name = name;
            this.type = type;
            this.order = order;
        }
        
        public WorkflowButton(String code, String name, String type, String icon, int order) {
            this.code = code;
            this.name = name;
            this.type = type;
            this.icon = icon;
            this.order = order;
        }
    }
    
    // ============== 静态工厂方法 ==============
    
    /**
     * 创建新建表单的上下文（无流程）
     */
    public static WorkflowTaskContext forNewForm() {
        WorkflowTaskContext context = new WorkflowTaskContext();
        context.setHasTask(false);
        context.setProcessStatus("NOT_STARTED");
        context.setCanSubmit(true);
        context.setCanSave(true);
        context.setReadOnly(false);
        return context;
    }
    
    /**
     * 创建查看模式的上下文
     */
    public static WorkflowTaskContext forViewOnly(String processInstanceId) {
        WorkflowTaskContext context = new WorkflowTaskContext();
        context.setHasTask(false);
        context.setProcessInstanceId(processInstanceId);
        context.setReadOnly(true);
        return context;
    }

    // ============== 显式布尔字段 Setter（避免 Lombok 命名歧义） ==============

    public void setAssigneeFlag(boolean assigneeFlag) {
        this.assigneeFlag = assigneeFlag;
    }

    public void setStarterFlag(boolean starterFlag) {
        this.starterFlag = starterFlag;
    }
}
