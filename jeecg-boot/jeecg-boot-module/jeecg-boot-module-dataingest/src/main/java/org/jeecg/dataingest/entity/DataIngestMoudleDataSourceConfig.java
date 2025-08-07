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
 * @Description: 数据接入模块的数据源配置
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
@Schema(description="数据接入模块的数据源配置")
@Data
@TableName("data_ingest_moudle_data_source_config")
public class DataIngestMoudleDataSourceConfig implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private java.lang.String id;
	/**创建人*/
    @Schema(description = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @Schema(description = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @Schema(description = "所属部门")
    private java.lang.String sysOrgCode;
	/**数据源名称*/
	@Excel(name = "数据源名称", width = 15)
    @Schema(description = "数据源名称")
    private java.lang.String sourceName;
	/**连接配置*/
	@Excel(name = "连接配置", width = 15)
    @Schema(description = "连接配置")
    private java.lang.String connectionConfig;
	/**认证配置*/
	@Excel(name = "认证配置", width = 15)
    @Schema(description = "认证配置")
    private java.lang.String authConfig;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @Schema(description = "备注")
    private java.lang.String remark;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @Schema(description = "租户ID")
    private java.lang.String tenantId;
	/**任务ID*/
    @Schema(description = "任务ID")
    private java.lang.String taskId;
	/**默认表名前缀*/
	@Excel(name = "默认表名前缀", width = 15)
    @Schema(description = "默认表名前缀")
    private java.lang.String tableNamePreDefault;
}
