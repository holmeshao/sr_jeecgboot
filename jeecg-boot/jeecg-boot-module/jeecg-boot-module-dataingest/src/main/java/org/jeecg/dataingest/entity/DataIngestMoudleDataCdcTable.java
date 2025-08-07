package org.jeecg.modules.demo.org.jeecg.dataingest.entity;

import java.io.Serializable;
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
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 数据接入模块的cdc对接表
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
@Schema(description="数据接入模块的cdc对接表")
@Data
@TableName("data_ingest_moudle_data_cdc_table")
public class DataIngestMoudleDataCdcTable implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private java.lang.String id;
	/**创建人*/
    @Schema(description = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @Schema(description = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @Schema(description = "所属部门")
    private java.lang.String sysOrgCode;
	/**目标表名*/
	@Excel(name = "目标表名", width = 15)
    @Schema(description = "目标表名")
    private java.lang.String targetTableName;
	/**源表名*/
	@Excel(name = "源表名", width = 15)
    @Schema(description = "源表名")
    private java.lang.String sourceTableName;
	/**任务ID*/
    @Schema(description = "任务ID")
    private java.lang.String taskId;
	/**NiFi DWD处理器ID*/
	@Excel(name = "NiFi DWD处理器ID", width = 15)
    @Schema(description = "NiFi DWD处理器ID")
    private java.lang.String nifiDwdProcessorId;
	/**NiFi DWS处理器ID*/
	@Excel(name = "NiFi DWS处理器ID", width = 15)
    @Schema(description = "NiFi DWS处理器ID")
    private java.lang.String nifiDwsProcessorId;
	/**业务域标识*/
	@Excel(name = "业务域标识", width = 15)
    @Schema(description = "业务域标识")
    private java.lang.String businessDomain;
	/**是否启用NiFi通知0=禁用，1=启用。控制是否向NiFi发送CDC变更通知*/
	@Excel(name = "是否启用NiFi通知0=禁用，1=启用。控制是否向NiFi发送CDC变更通知", width = 15)
    @Schema(description = "是否启用NiFi通知0=禁用，1=启用。控制是否向NiFi发送CDC变更通知")
    private java.lang.Integer enableNifiNotify;
	/**NiFi通知模式1=立即通知，2=批量通知，3=定时通知*/
	@Excel(name = "NiFi通知模式1=立即通知，2=批量通知，3=定时通知", width = 15)
    @Schema(description = "NiFi通知模式1=立即通知，2=批量通知，3=定时通知")
    private java.lang.Integer nifiNotifyMode;
	/**延迟触发通知的秒数，0表示立即触发*/
	@Excel(name = "延迟触发通知的秒数，0表示立即触发", width = 15)
    @Schema(description = "延迟触发通知的秒数，0表示立即触发")
    private java.lang.Integer notifyDelaySeconds;
}
