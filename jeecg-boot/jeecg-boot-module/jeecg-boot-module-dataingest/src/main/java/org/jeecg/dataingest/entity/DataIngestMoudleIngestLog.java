package org.jeecg.dataingest.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
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
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: data_ingest_moudle_ingest_log
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
@Data
@TableName("data_ingest_moudle_ingest_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="data_ingest_moudle_ingest_log")
public class DataIngestMoudleIngestLog implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;
	/**任务ID*/
	@Excel(name = "任务ID", width = 15)
    @Schema(description = "任务ID")
    private String taskId;
	/**执行批次*/
	@Excel(name = "执行批次", width = 15)
    @Schema(description = "执行批次")
    private String batchId;
	/**执行状态*/
	@Excel(name = "执行状态", width = 15)
    @Schema(description = "执行状态")
    private Integer status;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description = "开始时间")
    private Date startTime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description = "结束时间")
    private Date endTime;
	/**处理记录数*/
	@Excel(name = "处理记录数", width = 15)
    @Schema(description = "处理记录数")
    private Integer recordCount;
	/**成功记录数*/
	@Excel(name = "成功记录数", width = 15)
    @Schema(description = "成功记录数")
    private Integer successCount;
	/**失败记录数*/
	@Excel(name = "失败记录数", width = 15)
    @Schema(description = "失败记录数")
    private Integer failCount;
	/**错误信息*/
	@Excel(name = "错误信息", width = 15)
    @Schema(description = "错误信息")
    private String errorMessage;
	/**执行日志*/
	@Excel(name = "执行日志", width = 15)
    @Schema(description = "执行日志")
    private String executeLog;
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
}
