package org.jeecg.dataingest.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据接入任务表
 * @Description: 数据接入任务表
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Data
@TableName("ingest_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="数据接入任务表")
public class IngestTask extends JeecgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;
    
    /**任务名称*/
    @Schema(description = "任务名称")
    private String taskName;
    
    /**任务类型*/
    @Schema(description = "任务类型")
    private String taskType;
    
    /**数据源ID*/
    @Schema(description = "数据源ID")
    private String dataSourceId;
    
    /**任务配置*/
    @Schema(description = "任务配置")
    private String taskConfig;
    
    /**目标表名*/
    @Schema(description = "目标表名")
    private String targetTable;
    
    /**调度配置*/
    @Schema(description = "调度配置")
    private String scheduleConfig;
    
    /**任务状态*/
    @Schema(description = "任务状态")
    private Integer status;
    
    /**上次执行时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "上次执行时间")
    private Date lastExecuteTime;
    
    /**下次执行时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "下次执行时间")
    private Date nextExecuteTime;
    
    /**执行次数*/
    @Schema(description = "执行次数")
    private Long executeCount;
    
    /**成功次数*/
    @Schema(description = "成功次数")
    private Long successCount;
    
    /**失败次数*/
    @Schema(description = "失败次数")
    private Long failCount;
    
    /**备注*/
    @Schema(description = "备注")
    private String remark;
} 