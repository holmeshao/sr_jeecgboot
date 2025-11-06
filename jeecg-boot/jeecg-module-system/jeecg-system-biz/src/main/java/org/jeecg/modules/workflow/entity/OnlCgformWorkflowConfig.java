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
 * 🎯 表单工作流配置实体
 * 基于JeecgBoot在线表单的工作流集成配置
 * 
 * @author JeecgBoot工作流集成
 * @version 1.0
 */
@Data
@TableName("onl_cgform_workflow_config")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "OnlCgformWorkflowConfig对象-表单工作流配置表")
public class OnlCgformWorkflowConfig implements Serializable {

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

    /**是否启用工作流*/
    @Excel(name = "是否启用工作流", width = 15, dicCode = "yn")
    @Schema(description = "是否启用工作流")
    private Boolean workflowEnabled;

    /**版本控制启用*/
    @Excel(name = "版本控制启用", width = 15, dicCode = "yn")
    @Schema(description = "是否启用版本控制")
    private Boolean versionControlEnabled;

    /**权限控制启用*/
    @Excel(name = "权限控制启用", width = 15, dicCode = "yn")
    @Schema(description = "是否启用权限控制")
    private Boolean permissionControlEnabled;

    /**业务主键字段名*/
    @Excel(name = "业务主键字段名", width = 15)
    @Schema(description = "业务主键字段名")
    private String businessKeyField;

    /**状态字段名*/
    @Excel(name = "状态字段名", width = 15)
    @Schema(description = "状态字段名")
    private String statusField;

    /**流程实例字段名*/
    @Excel(name = "流程实例字段名", width = 15)
    @Schema(description = "流程实例字段名")
    private String processInstanceField;

    /**快照策略*/
    @Excel(name = "快照策略", width = 15, dicCode = "workflow_snapshot_strategy")
    @Schema(description = "快照策略(NODE节点级,TASK任务级)")
    private String snapshotStrategy;

    /**快照节点*/
    @Excel(name = "快照节点", width = 15)
    @Schema(description = "需要快照的节点JSON数组")
    private String snapshotNodes;

    /**工作流启动模式*/
    @Excel(name = "工作流启动模式", width = 15, dicCode = "workflow_start_mode")
    @Schema(description = "工作流启动模式(AUTO自动启动,MANUAL手动启动,OPTIONAL可选启动)")
    private String workflowStartMode;

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

    /**UI模式(SPLIT/INTEGRATED)*/
    @Schema(description = "UI模式(SPLIT/INTEGRATED)")
    private String uiMode;

    /**融合模式节点UI Schema(JSON)*/
    @Schema(description = "融合模式节点UI Schema(JSON)")
    private String uiSchemaJson;

    // =============== 业务方法 ===============

    // 注意：避免JavaBeans歧义getter（会被MyBatis/OGNL当作属性），改为非JavaBeans风格的辅助方法
    public boolean hasWorkflowEnabled() { return Boolean.TRUE.equals(this.workflowEnabled); }
    public boolean hasVersionControlEnabled() { return Boolean.TRUE.equals(this.versionControlEnabled); }
    public boolean hasPermissionControlEnabled() { return Boolean.TRUE.equals(this.permissionControlEnabled); }

    /**
     * 是否自动启动工作流
     */
    public boolean isAutoStart() {
        return "AUTO".equals(this.workflowStartMode);
    }

    /**
     * 是否可选启动工作流
     */
    public boolean isOptionalStart() {
        return "OPTIONAL".equals(this.workflowStartMode);
    }

    /**
     * 获取业务主键字段名（带默认值）
     */
    public String getBusinessKeyFieldOrDefault() {
        return businessKeyField != null ? businessKeyField : "id";
    }

    /**
     * 获取状态字段名（带默认值）
     */
    public String getStatusFieldOrDefault() {
        // 统一默认：bpmn_status；当配置为空字符串时也回退默认
        return (statusField != null && statusField.trim().length() > 0) ? statusField : "bpmn_status";
    }

    /**
     * 获取流程实例字段名（带默认值）
     */
    public String getProcessInstanceFieldOrDefault() {
        return (processInstanceField != null && processInstanceField.trim().length() > 0) ? processInstanceField : "process_instance_id";
    }
}