package org.jeecg.modules.workflow.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 表单渲染配置 DTO
 * 用于前端根据此配置动态渲染表单和操作按钮
 * 
 * @author jeecg
 * @since 2024-12-25
 */
@Data
public class FormRenderConfig implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // =============== 基础模式信息 ===============
    
    /**
     * UI模式：SPLIT / INTEGRATED / PURE_FORM
     * - SPLIT: 分离模式，表单可独立保存
     * - INTEGRATED: 融合模式，表单随节点变化
     * - PURE_FORM: 纯表单模式（无工作流配置）
     */
    private String mode;
    
    /**
     * 业务状态：DRAFT / PROCESSING / APPROVED / REJECTED 等
     */
    private String businessStatus;
    
    /**
     * 是否允许编辑（综合判断后的结果）
     */
    private boolean allowEdit;
    
    // =============== 工作流状态 ===============
    
    /**
     * 是否有当前待办任务
     */
    private boolean hasCurrentTask;
    
    /**
     * 当前任务ID
     */
    private String taskId;
    
    /**
     * 当前任务名称
     */
    private String taskName;
    
    /**
     * 当前节点ID
     */
    private String nodeId;
    
    /**
     * 流程实例ID
     */
    private String processInstanceId;
    
    /**
     * 流程定义Key
     */
    private String processDefinitionKey;
    
    // =============== 分离模式专属配置 ===============
    
    /**
     * 是否允许仅保存（分离模式）
     */
    private boolean allowSaveOnly;
    
    /**
     * 仅保存按钮文本
     */
    private String saveOnlyButtonText;
    
    /**
     * 是否可以启动工作流
     */
    private boolean canStartWorkflow;
    
    /**
     * 启动工作流按钮文本
     */
    private String workflowButtonText;
    
    // =============== 融合模式专属配置 ===============
    
    /**
     * 可见字段列表（融合模式）
     */
    private List<String> visibleFields;
    
    /**
     * 只读字段列表
     */
    private List<String> readonlyFields;
    
    /**
     * 隐藏字段列表
     */
    private List<String> hiddenFields;
    
    /**
     * 必填字段列表
     */
    private List<String> requiredFields;
    
    /**
     * 布局配置
     */
    private String layout;
    
    /**
     * 审批区位置
     */
    private String approvalPosition;
    
    /**
     * 审批区字段
     */
    private List<String> approvalFields;
    
    /**
     * 子表权限配置
     * key: 子表名, value: 权限模式（EDIT/VIEW/HIDDEN）
     */
    private Map<String, String> subtablePermissions;
    
    // =============== 通用字段权限（兼容现有逻辑） ===============
    
    /**
     * 字段权限配置（兼容现有 FormPermissionConfig）
     */
    private Map<String, Object> fieldPermissions;
    
    // =============== UI 控制 ===============
    
    /**
     * 是否显示工作流面板
     */
    private boolean showWorkflowPanel;
    
    /**
     * 是否显示流程进度
     */
    private boolean showProgress = true;
    
    /**
     * 是否显示版本历史
     */
    private boolean showVersionHistory;
    
    /**
     * 操作按钮列表
     */
    private List<Map<String, Object>> actionButtons;
    
    // =============== 扩展信息 ===============
    
    /**
     * 页面标题
     */
    private String pageTitle;
    
    /**
     * 提示消息
     */
    private String message;
    
    /**
     * 扩展属性
     */
    private Map<String, Object> extras;
    
    // =============== 便捷方法 ===============
    
    /**
     * 是否为分离模式
     */
    public boolean isSplitMode() {
        return "SPLIT".equalsIgnoreCase(mode);
    }
    
    /**
     * 是否为融合模式
     */
    public boolean isIntegratedMode() {
        return "INTEGRATED".equalsIgnoreCase(mode);
    }
    
    /**
     * 是否为纯表单模式
     */
    public boolean isPureFormMode() {
        return "PURE_FORM".equalsIgnoreCase(mode);
    }
    
    /**
     * 是否只读
     */
    public boolean isReadonly() {
        return !allowEdit;
    }
    
    /**
     * 判断指定字段是否只读
     */
    public boolean isFieldReadonly(String fieldName) {
        if (!allowEdit) {
            return true;
        }
        if (readonlyFields != null && readonlyFields.contains(fieldName)) {
            return true;
        }
        return false;
    }
    
    /**
     * 判断指定字段是否隐藏
     */
    public boolean isFieldHidden(String fieldName) {
        if (hiddenFields != null && hiddenFields.contains(fieldName)) {
            return true;
        }
        // 融合模式下，不在可见列表中的字段视为隐藏
        if (isIntegratedMode() && visibleFields != null && !visibleFields.isEmpty()) {
            return !visibleFields.contains(fieldName);
        }
        return false;
    }
    
    /**
     * 判断指定字段是否必填
     */
    public boolean isFieldRequired(String fieldName) {
        return requiredFields != null && requiredFields.contains(fieldName);
    }
}
