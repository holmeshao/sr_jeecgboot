package org.jeecg.modules.workflow.enums;

/**
 * 工作流启动模式枚举
 *
 * @author jeecg
 * @since 2024-12-25
 */
public enum WorkflowStartMode {
    
    /**
     * 自动启动：表单提交即启动工作流
     * 适用场景：紧急流程、简单审批流程
     */
    AUTO("AUTO", "自动启动", "表单提交即启动工作流"),
    
    /**
     * 手动启动：用户需要明确点击"启动流程"按钮
     * 适用场景：用户习惯先保存草稿，需要时再启动流程
     */
    MANUAL("MANUAL", "手动启动", "用户需要明确启动流程"),
    
    /**
     * 可选启动：用户可以选择"保存草稿"或"提交流程"
     * 适用场景：灵活性要求高的业务流程
     */
    OPTIONAL("OPTIONAL", "可选启动", "用户可选择保存或启动流程");
    
    private final String code;
    private final String name;
    private final String description;
    
    WorkflowStartMode(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据代码获取枚举
     */
    public static WorkflowStartMode fromCode(String code) {
        for (WorkflowStartMode mode : values()) {
            if (mode.getCode().equals(code)) {
                return mode;
            }
        }
        return MANUAL; // 默认返回手动启动
    }
    
    /**
     * 是否为自动启动模式
     */
    public boolean isAutoStart() {
        return this == AUTO;
    }
    
    /**
     * 是否为手动启动模式
     */
    public boolean isManualStart() {
        return this == MANUAL;
    }
    
    /**
     * 是否为可选启动模式
     */
    public boolean isOptionalStart() {
        return this == OPTIONAL;
    }
    
    /**
     * 是否支持草稿保存
     */
    public boolean supportsDraft() {
        return this == MANUAL || this == OPTIONAL;
    }
    
    /**
     * 是否需要用户选择
     */
    public boolean requiresUserChoice() {
        return this == OPTIONAL;
    }
}