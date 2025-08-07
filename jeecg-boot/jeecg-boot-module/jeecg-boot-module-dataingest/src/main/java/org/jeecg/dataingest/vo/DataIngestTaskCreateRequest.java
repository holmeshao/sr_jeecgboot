package org.jeecg.dataingest.vo;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 数据接入任务创建请求VO
 * @Description: 用于接收前端传来的JSON格式任务配置
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Data
@Schema(description = "数据接入任务创建请求")
public class DataIngestTaskCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "任务名称", required = true)
    @NotBlank(message = "任务名称不能为空")
    private String taskName;

    @Schema(description = "任务类型", required = true, example = "SQLSERVER_CDC,MYSQL_CDC,POSTGRESQL_CDC")
    @NotBlank(message = "任务类型不能为空")
    private String taskType;

    @Schema(description = "目标表名前缀", example = "ods_")
    private String targetTableNamePre;

    @Schema(description = "任务状态", example = "1:启用,0:禁用")
    @NotNull(message = "任务状态不能为空")
    private Integer status;

    @Schema(description = "数据源配置列表")
    private List<DataSourceConfigVO> dataIngestMoudleDataSourceConfig;

    @Schema(description = "字段映射配置")
    private JSONArray dataIngestMoudleFieldMapping;

    @Schema(description = "CDC监听表配置列表")
    private List<CdcTableConfigVO> dataIngestMoudleDataCdcTable;

    @Data
    @Schema(description = "数据源配置")
    public static class DataSourceConfigVO {
        @Schema(description = "数据源名称")
        @NotBlank(message = "数据源名称不能为空")
        private String sourceName;

        @Schema(description = "连接配置JSON字符串")
        @NotBlank(message = "连接配置不能为空")
        private String connectionConfig;

        @Schema(description = "认证配置JSON字符串")
        private String authConfig;
    }

    @Data
    @Schema(description = "CDC监听表配置")
    public static class CdcTableConfigVO {
        @Schema(description = "源表名称")
        @NotBlank(message = "源表名称不能为空")
        private String sourceTableName;

        @Schema(description = "目标表名称")
        private String targetTableName;
    }
} 