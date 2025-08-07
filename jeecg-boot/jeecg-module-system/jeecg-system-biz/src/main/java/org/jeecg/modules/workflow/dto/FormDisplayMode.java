package org.jeecg.modules.workflow.dto;

import lombok.Data;
import org.jeecg.modules.workflow.model.FormPermissionConfig;
import java.util.List;

/**
 * 表单显示模式
 * 
 * @author jeecg
 * @since 2024-12-25
 */
@Data
public class FormDisplayMode {
    
    /**
     * 显示模式枚举
     */
    public enum Mode {
        OPERATE,    // 操作模式（有当前任务）
        TRACK,      // 跟踪模式（流程进行中但无任务）
        VIEW        // 查看模式（流程结束或未开始）
    }
    
    /**
     * 显示模式
     */
    private Mode mode;
    
    /**
     * 是否有当前任务
     */
    private boolean hasCurrentTask;
    
    /**
     * 当前任务ID
     */
    private String currentTaskId;
    
    /**
     * 当前任务名称
     */
    private String currentTaskName;
    
    /**
     * 流程实例ID
     */
    private String processInstanceId;
    
    /**
     * 字段权限配置
     */
    private FormPermissionConfig fieldPermissions;
    
    /**
     * 可用操作按钮
     */
    private List<NodeButton> availableActions;
    
    /**
     * 是否显示流程进度
     */
    private boolean showProgress = true;
    
    /**
     * 是否显示版本历史
     */
    private boolean showVersionHistory = false;
    
    /**
     * 是否启用快捷键
     */
    private boolean enableShortcuts = true;
    
    /**
     * 页面标题
     */
    private String pageTitle;
    
    /**
     * 提示信息
     */
    private String message;
    
    /**
     * 扩展属性
     */
    private java.util.Map<String, Object> extras;
    
    /**
     * 获取模式描述
     */
    public String getModeDescription() {
        switch (mode) {
            case OPERATE:
                return "操作模式";
            case TRACK:
                return "跟踪模式";
            case VIEW:
                return "查看模式";
            default:
                return "未知模式";
        }
    }
    
    /**
     * 是否可编辑
     */
    public boolean isEditable() {
        return mode == Mode.OPERATE && hasCurrentTask;
    }
    
    /**
     * 是否只读
     */
    public boolean isReadonly() {
        return mode == Mode.VIEW || mode == Mode.TRACK;
    }
}