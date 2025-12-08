package org.jeecg.modules.workflow.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 表单模式配置 DTO
 * 用于解析 OnlCgformWorkflowConfig.uiSchemaJson 字段
 * 
 * 支持两种模式：
 * - SPLIT（分离模式）：表单可独立保存，工作流可选启动
 * - INTEGRATED（融合模式）：表单随工作流节点变化，深度集成
 * 
 * @author jeecg
 * @since 2024-12-25
 */
@Data
public class FormModeConfig implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 模式：SPLIT / INTEGRATED
     */
    private String mode;
    
    /**
     * 分离模式配置
     */
    private SplitConfig splitConfig;
    
    /**
     * 融合模式配置
     */
    private IntegratedConfig integratedConfig;
    
    /**
     * 分离模式配置
     */
    @Data
    public static class SplitConfig implements Serializable {
        private static final long serialVersionUID = 1L;
        
        /**
         * 是否允许仅保存（不启动工作流）
         */
        private boolean allowSaveOnly = true;
        
        /**
         * 工作流提交按钮文本
         */
        private String workflowTriggerButton = "提交审批";
        
        /**
         * 仅保存按钮文本
         */
        private String saveOnlyButton = "仅保存";
        
        /**
         * 草稿态是否显示流程面板
         */
        private boolean showWorkflowPanelInDraft = false;
        
        /**
         * 驳回后是否允许编辑
         */
        private boolean allowEditAfterReject = true;
        
        /**
         * 驳回后的状态值（用于判断是否可编辑）
         */
        private String rejectStatus = "REJECTED";
    }
    
    /**
     * 融合模式配置
     */
    @Data
    public static class IntegratedConfig implements Serializable {
        private static final long serialVersionUID = 1L;
        
        /**
         * 节点UI Schema映射
         * key: nodeId, value: NodeUiSchema
         */
        private Map<String, NodeUiSchema> nodes;
        
        /**
         * 子表权限配置
         * key: 子表名, value: { nodeId: SubtablePermission }
         */
        private Map<String, Map<String, SubtablePermission>> subtablePermissions;
        
        /**
         * 默认布局
         */
        private String defaultLayout = "1-column";
        
        /**
         * 审批区默认位置
         */
        private String defaultApprovalPosition = "BOTTOM";
    }
    
    /**
     * 节点UI Schema
     */
    @Data
    public static class NodeUiSchema implements Serializable {
        private static final long serialVersionUID = 1L;
        
        /**
         * 可见字段列表
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
         * 布局：1-column, 2-column, 3-column
         */
        private String layout;
        
        /**
         * 审批区位置：TOP, BOTTOM, LEFT, RIGHT, NONE
         */
        private String approvalPosition;
        
        /**
         * 审批区包含的字段
         */
        private List<String> approvalFields;
        
        /**
         * 自定义组件配置（JSON）
         */
        private Map<String, Object> customComponents;
        
        /**
         * 字段分组配置
         */
        private List<FieldGroup> fieldGroups;
    }
    
    /**
     * 字段分组
     */
    @Data
    public static class FieldGroup implements Serializable {
        private static final long serialVersionUID = 1L;
        
        /**
         * 分组标题
         */
        private String title;
        
        /**
         * 分组包含的字段
         */
        private List<String> fields;
        
        /**
         * 是否可折叠
         */
        private boolean collapsible = false;
        
        /**
         * 默认是否展开
         */
        private boolean defaultExpanded = true;
    }
    
    /**
     * 子表权限
     */
    @Data
    public static class SubtablePermission implements Serializable {
        private static final long serialVersionUID = 1L;
        
        /**
         * 模式：EDIT / VIEW / HIDDEN
         */
        private String mode;
        
        /**
         * 可编辑列（当mode=EDIT时有效）
         */
        private List<String> editableColumns;
        
        /**
         * 是否允许新增行
         */
        private boolean allowAddRow = true;
        
        /**
         * 是否允许删除行
         */
        private boolean allowDeleteRow = true;
    }
    
    // =============== 便捷方法 ===============
    
    /**
     * 获取有效的分离模式配置（带默认值）
     */
    public SplitConfig getSplitConfigOrDefault() {
        return splitConfig != null ? splitConfig : new SplitConfig();
    }
    
    /**
     * 获取有效的融合模式配置（带默认值）
     */
    public IntegratedConfig getIntegratedConfigOrDefault() {
        return integratedConfig != null ? integratedConfig : new IntegratedConfig();
    }
    
    /**
     * 是否为分离模式
     */
    public boolean isSplitMode() {
        return "SPLIT".equalsIgnoreCase(mode) || mode == null;
    }
    
    /**
     * 是否为融合模式
     */
    public boolean isIntegratedMode() {
        return "INTEGRATED".equalsIgnoreCase(mode);
    }
    
    /**
     * 获取指定节点的UI Schema
     */
    public NodeUiSchema getNodeSchema(String nodeId) {
        if (integratedConfig == null || integratedConfig.getNodes() == null) {
            return null;
        }
        return integratedConfig.getNodes().get(nodeId);
    }
    
    /**
     * 获取指定子表在指定节点的权限
     */
    public SubtablePermission getSubtablePermission(String subtableName, String nodeId) {
        if (integratedConfig == null || integratedConfig.getSubtablePermissions() == null) {
            return null;
        }
        Map<String, SubtablePermission> nodePerms = integratedConfig.getSubtablePermissions().get(subtableName);
        if (nodePerms == null) {
            return null;
        }
        return nodePerms.get(nodeId);
    }
}
