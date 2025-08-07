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
 * 🎯 数据接入日志实体类
 * 
 * @Description: 数据接入操作日志记录
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Data
@TableName("data_ingest_module_ingest_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据接入日志")
public class DataIngestMoudleIngestLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;

    /** 任务ID */
    @Excel(name = "任务ID", width = 15)
    @Schema(description = "任务ID")
    private String taskId;

    /** 操作动作 */
    @Excel(name = "操作动作", width = 15)
    @Schema(description = "操作动作")
    private String action;

    /** 操作状态 */
    @Excel(name = "操作状态", width = 15)
    @Schema(description = "操作状态")
    private Integer status;

    /** 操作人员 */
    @Excel(name = "操作人员", width = 15)
    @Schema(description = "操作人员")
    private String operator;

    /** 操作时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "操作时间")
    private Date operateTime;

    /** 请求参数 */
    @Schema(description = "请求参数JSON")
    private String param;

    /** 执行结果 */
    @Schema(description = "执行结果")
    private String result;

    /** 错误信息 */
    @Schema(description = "错误信息")
    private String errorMessage;

    /** 执行耗时(毫秒) */
    @Excel(name = "执行耗时", width = 15)
    @Schema(description = "执行耗时(毫秒)")
    private Long duration;

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