package org.jeecg.modules.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 🎯 工作流节点权限配置实体
 * 支持字段级别的精细化权限控制
 * 
 * @author JeecgBoot工作流集成
 * @version 1.0
 */
@Data
@TableName("onl_cgform_workflow_node")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "节点权限配置表")
public class OnlCgformWorkflowNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;

    /**表单ID*/
    @Excel(name = "表单ID", width = 15)
    @Schema(description = "表单ID")
    private String cgformHeadId;

    /**流程定义Key*/
    @Excel(name = "流程定义Key", width = 15)
    @Schema(description = "流程定义Key")
    private String processDefinitionKey;

    /**节点ID*/
    @Excel(name = "节点ID", width = 15)
    @Schema(description = "节点ID")
    private String nodeId;

    /**节点名称*/
    @Excel(name = "节点名称", width = 15)
    @Schema(description = "节点名称")
    private String nodeName;

    /**可编辑字段*/
    @Excel(name = "可编辑字段", width = 15)
    @Schema(description = "可编辑字段JSON数组")
    private String editableFields;

    /**只读字段*/
    @Excel(name = "只读字段", width = 15)
    @Schema(description = "只读字段JSON数组")
    private String readonlyFields;

    /**隐藏字段*/
    @Excel(name = "隐藏字段", width = 15)
    @Schema(description = "隐藏字段JSON数组")
    private String hiddenFields;

    /**必填字段*/
    @Excel(name = "必填字段", width = 15)
    @Schema(description = "必填字段JSON数组")
    private String requiredFields;

    /**条件权限配置*/
    @Excel(name = "条件权限配置", width = 15)
    @Schema(description = "条件权限配置JSON")
    private String conditionalPermissions;

    /**表单模式*/
    @Excel(name = "表单模式", width = 15, dicCode = "form_mode")
    @Schema(description = "表单模式(VIEW只读,EDIT编辑,OPERATE操作)")
    private String formMode;

    /**自定义按钮*/
    @Excel(name = "自定义按钮", width = 15)
    @Schema(description = "自定义按钮配置JSON")
    private String customButtons;

    /**隐藏按钮*/
    @Excel(name = "隐藏按钮", width = 15)
    @Schema(description = "隐藏按钮JSON数组")
    private String hiddenButtons;

    /**排序*/
    @Excel(name = "排序", width = 15)
    @Schema(description = "排序")
    private Integer sortOrder;

    /**状态*/
    @Excel(name = "状态", width = 15, dicCode = "valid_status")
    @Schema(description = "状态")
    private Integer status;

    /**创建人*/
    @Schema(description = "创建人")
    private String createBy;

    /**创建日期*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private Date createTime;

    /**更新人*/
    @Schema(description = "更新人")
    private String updateBy;

    /**更新日期*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private Date updateTime;

    /**备注*/
    @Excel(name = "备注", width = 15)
    @Schema(description = "备注")
    private String remark;

    // =============== 业务方法 ===============

    /**
     * 是否为发起节点
     */
    public boolean isStartNode() {
        return nodeId != null && nodeId.toLowerCase().contains("start");
    }

    /**
     * 是否为结束节点
     */
    public boolean isEndNode() {
        return nodeId != null && nodeId.toLowerCase().contains("end");
    }

    /**
     * 是否为用户任务节点
     */
    public boolean isUserTask() {
        return nodeId != null && !isStartNode() && !isEndNode();
    }

    /**
     * 获取表单模式（带默认值）
     */
    public String getFormModeOrDefault() {
        if (formMode != null) {
            return formMode;
        }
        // 智能默认：发起节点为编辑模式，其他为操作模式
        return isStartNode() ? "EDIT" : "OPERATE";
    }

    /**
     * 获取排序值（带默认值）
     */
    public Integer getSortOrderOrDefault() {
        return sortOrder != null ? sortOrder : 100;
    }

    /**
     * 是否有效
     */
    public boolean isActive() {
        return Integer.valueOf(1).equals(this.status);
    }
}