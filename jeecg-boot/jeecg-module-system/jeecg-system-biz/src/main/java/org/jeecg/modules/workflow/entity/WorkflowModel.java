package org.jeecg.modules.workflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.jeecg.common.system.base.entity.JeecgEntity;

import java.util.Date;

/**
 * 工作流模型（模型主表）
 */
@Data
@TableName("wf_model")
public class WorkflowModel {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /** 模型标识（建议与流程定义key对齐） */
    private String modelKey;

    /** 模型名称 */
    private String name;

    /** 分类 */
    private String category;

    /** 最新版本号 */
    private Integer latestVersion;

    /** 状态：DRAFT/PUBLISHED */
    private String status;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}


