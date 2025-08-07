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
 * 数据源配置表
 * @Description: 数据源配置表
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Data
@TableName("data_source_config")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="数据源配置表")
public class DataSourceConfig extends JeecgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;
    
    /**数据源名称*/
    @Schema(description = "数据源名称")
    private String sourceName;
    
    /**数据源类型*/
    @Schema(description = "数据源类型")
    private String sourceType;
    
    /**连接配置*/
    @Schema(description = "连接配置")
    private String connectionConfig;
    
    /**认证配置*/
    @Schema(description = "认证配置")
    private String authConfig;
    
    /**状态*/
    @Schema(description = "状态")
    private Integer status;
    
    /**备注*/
    @Schema(description = "备注")
    private String remark;
} 