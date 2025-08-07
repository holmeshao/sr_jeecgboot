package org.jeecg.modules.workflow.dto;

import lombok.Data;
import java.util.Map;

/**
 * 节点按钮配置
 * 
 * @author jeecg
 * @since 2024-12-25
 */
@Data
public class NodeButton {
    
    /**
     * 按钮ID
     */
    private String id;
    
    /**
     * 按钮文本
     */
    private String text;
    
    /**
     * 按钮类型 (primary, default, danger, success, warning)
     */
    private String type = "default";
    
    /**
     * 按钮动作 (submit, save, approve, reject, transfer, etc.)
     */
    private String action;
    
    /**
     * 按钮图标
     */
    private String icon;
    
    /**
     * 是否显示
     */
    private boolean visible = true;
    
    /**
     * 是否禁用
     */
    private boolean disabled = false;
    
    /**
     * 是否加载中
     */
    private boolean loading = false;
    
    /**
     * 排序顺序
     */
    private int order = 0;
    
    /**
     * 确认消息
     */
    private String confirmMessage;
    
    /**
     * 成功消息
     */
    private String successMessage;
    
    /**
     * 按钮样式
     */
    private String style;
    
    /**
     * CSS类名
     */
    private String className;
    
    /**
     * 扩展属性
     */
    private Map<String, Object> attributes;
    
    /**
     * 权限编码
     */
    private String permission;
    
    /**
     * 条件表达式（决定是否显示）
     */
    private String condition;
    
    /**
     * 创建默认提交按钮
     */
    public static NodeButton createSubmitButton() {
        NodeButton button = new NodeButton();
        button.setId("submit");
        button.setText("提交");
        button.setType("primary");
        button.setAction("submit");
        button.setIcon("check");
        button.setConfirmMessage("确定要提交表单吗？");
        button.setSuccessMessage("提交成功");
        button.setOrder(1);
        return button;
    }
    
    /**
     * 创建默认保存按钮
     */
    public static NodeButton createSaveButton() {
        NodeButton button = new NodeButton();
        button.setId("save");
        button.setText("保存");
        button.setType("default");
        button.setAction("save");
        button.setIcon("save");
        button.setSuccessMessage("保存成功");
        button.setOrder(2);
        return button;
    }
    
    /**
     * 创建默认同意按钮
     */
    public static NodeButton createApproveButton() {
        NodeButton button = new NodeButton();
        button.setId("approve");
        button.setText("同意");
        button.setType("primary");
        button.setAction("approve");
        button.setIcon("check-circle");
        button.setConfirmMessage("确定要同意此申请吗？");
        button.setSuccessMessage("审批成功");
        button.setOrder(1);
        return button;
    }
    
    /**
     * 创建默认拒绝按钮
     */
    public static NodeButton createRejectButton() {
        NodeButton button = new NodeButton();
        button.setId("reject");
        button.setText("拒绝");
        button.setType("danger");
        button.setAction("reject");
        button.setIcon("close-circle");
        button.setConfirmMessage("确定要拒绝此申请吗？");
        button.setSuccessMessage("已拒绝");
        button.setOrder(2);
        return button;
    }
    
    /**
     * 创建默认转办按钮
     */
    public static NodeButton createTransferButton() {
        NodeButton button = new NodeButton();
        button.setId("transfer");
        button.setText("转办");
        button.setType("default");
        button.setAction("transfer");
        button.setIcon("swap");
        button.setOrder(3);
        return button;
    }
}