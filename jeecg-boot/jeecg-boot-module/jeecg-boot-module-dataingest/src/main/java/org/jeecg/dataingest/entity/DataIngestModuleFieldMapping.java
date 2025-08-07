package org.jeecg.dataingest.entity;

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
 * 🎯 数据接入字段映射实体类
 * 
 * @Description: 数据接入字段映射配置
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Data
@TableName("data_ingest_module_field_mapping")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据接入字段映射")
public class DataIngestModuleFieldMapping implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;

    /** 关联任务ID */
    @Excel(name = "关联任务ID", width = 15)
    @Schema(description = "关联的数据接入任务ID")
    private String taskId;

    /** 源字段名 */
    @Excel(name = "源字段名", width = 15)
    @Schema(description = "源字段名")
    private String sourceField;

    /** 目标字段名 */
    @Excel(name = "目标字段名", width = 15)
    @Schema(description = "目标字段名")
    private String targetField;

    /** 字段类型 */
    @Excel(name = "字段类型", width = 15)
    @Schema(description = "字段类型")
    private String fieldType;

    /** 是否必填 */
    @Excel(name = "是否必填", width = 15)
    @Schema(description = "是否必填(1-是,0-否)")
    private String isRequired;

    /** 默认值 */
    @Excel(name = "默认值", width = 15)
    @Schema(description = "默认值")
    private String defaultValue;

    /** 转换规则 */
    @Schema(description = "字段转换规则JSON")
    private String transformRule;

    /** 排序号 */
    @Excel(name = "排序号", width = 15)
    @Schema(description = "排序号")
    private Integer sortOrder;

    /** 备注 */
    @Excel(name = "备注", width = 15)
    @Schema(description = "备注")
    private String remark;

    /** 创建人 */
    @Schema(description = "创建人")
    private String createBy;

    /** 创建日期 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private Date createTime;

    /** 更新人 */
    @Schema(description = "更新人")
    private String updateBy;

    /** 更新日期 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private Date updateTime;

    /** 所属部门 */
    @Schema(description = "所属部门")
    private String sysOrgCode;
}