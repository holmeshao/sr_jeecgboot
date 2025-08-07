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
 * 🎯 数据接入CDC表配置实体类
 * 
 * @Description: 数据接入CDC表配置管理
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Data
@TableName("data_ingest_module_data_cdc_table")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据接入CDC表配置")
public class DataIngestModuleDataCdcTable implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;

    /** 关联任务ID */
    @Excel(name = "关联任务ID", width = 15)
    @Schema(description = "关联的数据接入任务ID")
    private String taskId;

    /** 源表名 */
    @Excel(name = "源表名", width = 15)
    @Schema(description = "源表名")
    private String sourceTableName;

    /** 目标表名 */
    @Excel(name = "目标表名", width = 15)
    @Schema(description = "目标表名")
    private String targetTableName;

    /** 是否启用NiFi通知 */
    @Excel(name = "是否启用NiFi通知", width = 15)
    @Schema(description = "是否启用NiFi通知(1-是,0-否)")
    private String enableNifiNotify;

    /** 同步模式 */
    @Excel(name = "同步模式", width = 15)
    @Schema(description = "同步模式(FULL/INCREMENTAL)")
    private String syncMode;

    /** 过滤条件 */
    @Schema(description = "数据过滤条件SQL")
    private String filterCondition;

    /** 转换规则 */
    @Schema(description = "数据转换规则JSON")
    private String transformRules;

    /** 是否启用 */
    @Excel(name = "是否启用", width = 15)
    @Schema(description = "是否启用(1-是,0-否)")
    private String enabled;

    /** 业务域 */
    @Excel(name = "业务域", width = 15)
    @Schema(description = "业务域")
    private String businessDomain;

    /** NiFi DWD处理器ID */
    @Excel(name = "NiFi DWD处理器ID", width = 15)
    @Schema(description = "NiFi DWD处理器ID")
    private String nifiDwdProcessorId;

    /** NiFi DWS处理器ID */
    @Excel(name = "NiFi DWS处理器ID", width = 15)
    @Schema(description = "NiFi DWS处理器ID")
    private String nifiDwsProcessorId;

    /** 通知延迟秒数 */
    @Excel(name = "通知延迟秒数", width = 15)
    @Schema(description = "通知延迟秒数")
    private Integer notifyDelaySeconds;

    /** NiFi通知模式 */
    @Excel(name = "NiFi通知模式", width = 15)
    @Schema(description = "NiFi通知模式")
    private String nifiNotifyMode;



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