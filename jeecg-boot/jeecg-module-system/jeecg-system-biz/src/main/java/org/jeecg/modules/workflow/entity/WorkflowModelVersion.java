package org.jeecg.modules.workflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.jeecg.common.system.base.entity.JeecgEntity;

import java.util.Date;

/**
 * 工作流模型版本（存放XML草稿）
 */
@Data
@TableName("wf_model_version")
public class WorkflowModelVersion {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String modelId;

    private Integer version;

    /** BPMN XML 文本 */
    private String xml;

    /** 备注 */
    private String comment;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}


