package org.jeecg.dataingest.vo;

import java.util.List;
import org.jeecg.dataingest.entity.DataIngestMoudleIngestTask;
import org.jeecg.dataingest.entity.DataIngestMoudleDataSourceConfig;
import org.jeecg.dataingest.entity.DataIngestMoudleFieldMapping;
import org.jeecg.dataingest.entity.DataIngestMoudleDataCdcTable;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @Description: 数据接入模块的任务主信息表
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
@Data
@Schema(description="数据接入模块的任务主信息表")
public class DataIngestMoudleIngestTaskPage {

	/**主键*/
	@Schema(description = "主键")
    private String id;
	/**任务名称*/
	@Excel(name = "任务名称", width = 15)
	@Schema(description = "任务名称")
    private String taskName;
	/**任务类型*/
	@Excel(name = "任务类型", width = 15, dicCode = "data_ingest_task_type")
    @Dict(dicCode = "data_ingest_task_type")
	@Schema(description = "任务类型")
    private String taskType;
	/**任务配置*/
	@Excel(name = "任务配置", width = 15)
	@Schema(description = "任务配置")
    private String taskConfig;
	/**目标表名前缀*/
	@Excel(name = "目标表名前缀", width = 15)
	@Schema(description = "目标表名前缀")
    private String targetTableNamePre;
	/**调度配置*/
	@Excel(name = "调度配置", width = 15)
	@Schema(description = "调度配置")
    private String scheduleConfig;
	/**任务状态*/
	@Excel(name = "任务状态", width = 15, dicCode = "valid_status")
    @Dict(dicCode = "valid_status")
	@Schema(description = "任务状态")
    private Integer status;
	/**上次执行时间*/
	@Excel(name = "上次执行时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@Schema(description = "上次执行时间")
    private Date lastExecuteTime;
	/**下次执行时间*/
	@Excel(name = "下次执行时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@Schema(description = "下次执行时间")
    private Date nextExecuteTime;
	/**执行次数*/
	@Excel(name = "执行次数", width = 15)
	@Schema(description = "执行次数")
    private Integer executeCount;
	/**成功次数*/
	@Excel(name = "成功次数", width = 15)
	@Schema(description = "成功次数")
    private Integer successCount;
	/**失败次数*/
	@Excel(name = "失败次数", width = 15)
	@Schema(description = "失败次数")
    private Integer failCount;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@Schema(description = "备注")
    private String remark;
	/**createBy*/
	@Schema(description = "createBy")
    private String createBy;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@Schema(description = "createTime")
    private Date createTime;
	/**updateBy*/
	@Schema(description = "updateBy")
    private String updateBy;
	/**updateTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@Schema(description = "updateTime")
    private Date updateTime;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
	@Schema(description = "租户ID")
    private String tenantId;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
	@Schema(description = "是否删除")
    private Integer isDelete;

	@ExcelCollection(name="数据接入模块的数据源配置")
	@Schema(description = "数据接入模块的数据源配置")
	private List<DataIngestMoudleDataSourceConfig> dataIngestMoudleDataSourceConfigList;
	@ExcelCollection(name="数据接入模块的字段映射表")
	@Schema(description = "数据接入模块的字段映射表")
	private List<DataIngestMoudleFieldMapping> dataIngestMoudleFieldMappingList;
	@ExcelCollection(name="数据接入模块的cdc对接表")
	@Schema(description = "数据接入模块的cdc对接表")
	private List<DataIngestMoudleDataCdcTable> dataIngestMoudleDataCdcTableList;

}
