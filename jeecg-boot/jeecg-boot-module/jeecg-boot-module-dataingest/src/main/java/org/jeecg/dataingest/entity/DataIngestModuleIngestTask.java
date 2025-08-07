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
 * 🎯 数据接入任务实体类
 * 
 * @Description: 数据接入任务配置管理
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Data
@TableName("data_ingest_module_ingest_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据接入任务")
public class DataIngestModuleIngestTask implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;

    /** 任务名称 */
    @Excel(name = "任务名称", width = 15)
    @Schema(description = "任务名称")
    private String taskName;

    /** 任务类型(CDC/API/SCHEDULE) */
    @Excel(name = "任务类型", width = 15)
    @Schema(description = "任务类型")
    private String taskType;

    /** 数据源ID */
    @Excel(name = "数据源ID", width = 15)
    @Schema(description = "数据源ID")
    private String datasourceId;

    /** 源表名 */
    @Excel(name = "源表名", width = 15)
    @Schema(description = "源表名")
    private String sourceTable;

    /** 目标表名 */
    @Excel(name = "目标表名", width = 15)
    @Schema(description = "目标表名")
    private String targetTable;

    /** 目标表名前缀 */
    @Excel(name = "目标表名前缀", width = 15)
    @Schema(description = "目标表名前缀")
    private String targetTableNamePre;



    /** 同步模式(FULL/INCREMENTAL) */
    @Excel(name = "同步模式", width = 15)
    @Schema(description = "同步模式")
    private String syncMode;

    /** 任务配置JSON */
    @Schema(description = "任务配置JSON")
    private String taskConfig;

    /** 调度表达式 */
    @Excel(name = "调度表达式", width = 15)
    @Schema(description = "调度表达式")
    private String cronExpression;

    /** 任务状态(0-停止,1-运行,2-暂停,3-错误) */
    @Excel(name = "任务状态", width = 15)
    @Schema(description = "任务状态")
    private Integer status;

    /** 最后执行时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后执行时间")
    private Date lastExecuteTime;

    /** 下次执行时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "下次执行时间")
    private Date nextExecuteTime;

    /** 执行结果 */
    @Schema(description = "执行结果")
    private String executeResult;

    /** 错误信息 */
    @Schema(description = "错误信息")
    private String errorMessage;

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