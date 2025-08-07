package org.jeecg.dataingest.entity;

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
 * @Description: 数据接入模块的字段映射表
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
@Schema(description="数据接入模块的字段映射表")
@Data
@TableName("data_ingest_moudle_field_mapping")
public class DataIngestMoudleFieldMapping implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;
	/**任务ID*/
    @Schema(description = "任务ID")
    private String taskId;
	/**源字段名*/
	@Excel(name = "源字段名", width = 15)
    @Schema(description = "源字段名")
    private String sourceField;
	/**目标字段名*/
	@Excel(name = "目标字段名", width = 15)
    @Schema(description = "目标字段名")
    private String targetField;
	/**字段类型*/
	@Excel(name = "字段类型", width = 15)
    @Schema(description = "字段类型")
    private String fieldType;
	/**是否必填*/
	@Excel(name = "是否必填", width = 15)
    @Schema(description = "是否必填")
    private String isRequired;
	/**默认值*/
	@Excel(name = "默认值", width = 15)
    @Schema(description = "默认值")
    private String defaultValue;
	/**转换规则*/
	@Excel(name = "转换规则", width = 15)
    @Schema(description = "转换规则")
    private String transformRule;
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
