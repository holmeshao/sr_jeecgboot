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
 * 数据接入日志表
 * @Description: 数据接入日志表
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Data
@TableName("ingest_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="数据接入日志表")
public class IngestLog extends JeecgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;
    
    /**任务ID*/
    @Schema(description = "任务ID")
    private String taskId;
    
    /**执行批次*/
    @Schema(description = "执行批次")
    private String batchId;
    
    /**执行状态*/
    @Schema(description = "执行状态")
    private Integer status;
    
    /**开始时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间")
    private Date startTime;
    
    /**结束时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束时间")
    private Date endTime;
    
    /**处理记录数*/
    @Schema(description = "处理记录数")
    private Long recordCount;
    
    /**成功记录数*/
    @Schema(description = "成功记录数")
    private Long successCount;
    
    /**失败记录数*/
    @Schema(description = "失败记录数")
    private Long failCount;
    
    /**错误信息*/
    @Schema(description = "错误信息")
    private String errorMessage;
    
    /**执行日志*/
    @Schema(description = "执行日志")
    private String executeLog;
} 