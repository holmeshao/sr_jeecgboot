package org.jeecg.dataingest.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 数据接入任务响应VO
 * @Description: 返回给前端的任务信息
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Data
@Schema(description = "数据接入任务响应")
public class DataIngestTaskResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "任务ID")
    private String id;

    @Schema(description = "任务名称")
    private String taskName;

    @Schema(description = "任务类型")
    private String taskType;

    @Schema(description = "目标表名前缀")
    private String targetTableNamePre;

    @Schema(description = "任务状态")
    private Integer status;

    @Schema(description = "运行状态", example = "RUNNING,STOPPED,ERROR")
    private String runningStatus;

    @Schema(description = "上次执行时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastExecuteTime;

    @Schema(description = "下次执行时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date nextExecuteTime;

    @Schema(description = "执行次数")
    private Integer executeCount;

    @Schema(description = "成功次数")
    private Integer successCount;

    @Schema(description = "失败次数")
    private Integer failCount;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Schema(description = "数据源配置列表")
    private List<DataSourceConfigInfo> dataSourceConfigs;

    @Schema(description = "CDC监听表配置列表")
    private List<CdcTableConfigInfo> cdcTableConfigs;

    @Data
    @Schema(description = "数据源配置信息")
    public static class DataSourceConfigInfo {
        @Schema(description = "数据源名称")
        private String sourceName;

        @Schema(description = "数据源类型")
        private String sourceType;

        @Schema(description = "连接状态")
        private String connectionStatus;
    }

    @Data
    @Schema(description = "CDC监听表配置信息")
    public static class CdcTableConfigInfo {
        @Schema(description = "源表名称")
        private String sourceTableName;

        @Schema(description = "目标表名称")
        private String targetTableName;

        @Schema(description = "监听状态")
        private String monitorStatus;

        @Schema(description = "最后同步时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date lastSyncTime;
    }
} 