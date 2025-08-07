package org.jeecg.modules.demo.org.jeecg.dataingest.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @Description: 数据接入模块的任务主信息表
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
@Schema(description="数据接入模块的任务主信息表")
@Data
@TableName("data_ingest_moudle_ingest_task")
public class DataIngestMoudleIngestTask implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private java.lang.String id;
	/**任务名称*/
	@Excel(name = "任务名称", width = 15)
    @Schema(description = "任务名称")
    private java.lang.String taskName;
	/**任务类型*/
	@Excel(name = "任务类型", width = 15, dicCode = "data_ingest_task_type")
    @Dict(dicCode = "data_ingest_task_type")
    @Schema(description = "任务类型")
    private java.lang.String taskType;
	/**任务配置*/
	@Excel(name = "任务配置", width = 15)
    @Schema(description = "任务配置")
    private java.lang.String taskConfig;
	/**目标表名前缀*/
	@Excel(name = "目标表名前缀", width = 15)
    @Schema(description = "目标表名前缀")
    private java.lang.String targetTableNamePre;
	/**调度配置*/
	@Excel(name = "调度配置", width = 15)
    @Schema(description = "调度配置")
    private java.lang.String scheduleConfig;
	/**任务状态*/
	@Excel(name = "任务状态", width = 15, dicCode = "valid_status")
    @Dict(dicCode = "valid_status")
    @Schema(description = "任务状态")
    private java.lang.Integer status;
	/**上次执行时间*/
	@Excel(name = "上次执行时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description = "上次执行时间")
    private java.util.Date lastExecuteTime;
	/**下次执行时间*/
	@Excel(name = "下次执行时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description = "下次执行时间")
    private java.util.Date nextExecuteTime;
	/**执行次数*/
	@Excel(name = "执行次数", width = 15)
    @Schema(description = "执行次数")
    private java.lang.Integer executeCount;
	/**成功次数*/
	@Excel(name = "成功次数", width = 15)
    @Schema(description = "成功次数")
    private java.lang.Integer successCount;
	/**失败次数*/
	@Excel(name = "失败次数", width = 15)
    @Schema(description = "失败次数")
    private java.lang.Integer failCount;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @Schema(description = "备注")
    private java.lang.String remark;
	/**createBy*/
    @Schema(description = "createBy")
    private java.lang.String createBy;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description = "createTime")
    private java.util.Date createTime;
	/**updateBy*/
    @Schema(description = "updateBy")
    private java.lang.String updateBy;
	/**updateTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description = "updateTime")
    private java.util.Date updateTime;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @Schema(description = "租户ID")
    private java.lang.String tenantId;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @Schema(description = "是否删除")
    private java.lang.Integer isDelete;
}
