package org.jeecg.modules.workflow.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 🎯 表单权限配置模型
 * 定义字段级别的权限控制规则
 * 
 * @author JeecgBoot工作流集成
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class FormPermissionConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 可编辑字段列表
     * 这些字段在当前节点可以被用户修改
     */
    private List<String> editableFields = new ArrayList<>();

    /**
     * 只读字段列表  
     * 这些字段在当前节点只能查看，不能修改
     */
    private List<String> readonlyFields = new ArrayList<>();

    /**
     * 隐藏字段列表
     * 这些字段在当前节点不显示给用户
     */
    private List<String> hiddenFields = new ArrayList<>();

    /**
     * 必填字段列表
     * 这些字段在当前节点必须填写
     */
    private List<String> requiredFields = new ArrayList<>();

    /**
     * 表单模式
     * VIEW - 只读模式
     * EDIT - 编辑模式  
     * OPERATE - 操作模式（部分可编辑）
     */
    private String formMode = "VIEW";

    /**
     * 条件权限配置
     * 基于特定条件的动态权限控制
     */
    private String conditionalPermissions;

    /**
     * 自定义按钮配置
     * 节点特定的按钮配置
     */
    private List<CustomButton> customButtons = new ArrayList<>();

    /**
     * 隐藏的标准按钮
     * 需要隐藏的系统默认按钮
     */
    private List<String> hiddenButtons = new ArrayList<>();

    // =============== 便捷方法 ===============

    /**
     * 检查字段是否可编辑
     */
    public boolean isFieldEditable(String fieldName) {
        return editableFields.contains(fieldName);
    }

    /**
     * 检查字段是否只读
     */
    public boolean isFieldReadonly(String fieldName) {
        return readonlyFields.contains(fieldName);
    }

    /**
     * 检查字段是否隐藏
     */
    public boolean isFieldHidden(String fieldName) {
        return hiddenFields.contains(fieldName);
    }

    /**
     * 检查字段是否必填
     */
    public boolean isFieldRequired(String fieldName) {
        return requiredFields.contains(fieldName);
    }

    /**
     * 添加可编辑字段
     */
    public FormPermissionConfig addEditableField(String fieldName) {
        if (!editableFields.contains(fieldName)) {
            editableFields.add(fieldName);
        }
        return this;
    }

    /**
     * 添加只读字段
     */
    public FormPermissionConfig addReadonlyField(String fieldName) {
        if (!readonlyFields.contains(fieldName)) {
            readonlyFields.add(fieldName);
        }
        return this;
    }

    /**
     * 添加隐藏字段
     */
    public FormPermissionConfig addHiddenField(String fieldName) {
        if (!hiddenFields.contains(fieldName)) {
            hiddenFields.add(fieldName);
        }
        return this;
    }

    /**
     * 添加必填字段
     */
    public FormPermissionConfig addRequiredField(String fieldName) {
        if (!requiredFields.contains(fieldName)) {
            requiredFields.add(fieldName);
        }
        return this;
    }

    /**
     * 移除字段的所有权限设置
     */
    public FormPermissionConfig removeFieldPermissions(String fieldName) {
        editableFields.remove(fieldName);
        readonlyFields.remove(fieldName);
        hiddenFields.remove(fieldName);
        requiredFields.remove(fieldName);
        return this;
    }

    /**
     * 检查是否为编辑模式
     */
    public boolean isEditMode() {
        return "EDIT".equals(formMode);
    }

    /**
     * 检查是否为查看模式
     */
    public boolean isViewMode() {
        return "VIEW".equals(formMode);
    }

    /**
     * 检查是否为操作模式
     */
    public boolean isOperateMode() {
        return "OPERATE".equals(formMode);
    }

    // =============== 内部类：自定义按钮 ===============

    /**
     * 自定义按钮配置
     */
    @Data
    @Accessors(chain = true)
    public static class CustomButton implements Serializable {
        
        private static final long serialVersionUID = 1L;

        /**
         * 按钮编码
         */
        private String code;

        /**
         * 按钮标签
         */
        private String label;

        /**
         * 按钮类型
         */
        private String type = "default";

        /**
         * 按钮图标
         */
        private String icon;

        /**
         * 是否需要确认
         */
        private Boolean needConfirm = false;

        /**
         * 确认提示信息
         */
        private String confirmMessage;

        /**
         * 是否需要处理意见
         */
        private Boolean needComment = false;

        /**
         * 权限控制
         */
        private String auth;

        /**
         * 排序
         */
        private Integer sortOrder = 100;
    }
}