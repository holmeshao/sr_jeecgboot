package org.jeecg.modules.maintenance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 维保项目信息
 *
 * @author jeecg
 * @since 2025-01-24
 */
@Data
@TableName("maintenance_project")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "维保项目信息")
public class MaintenanceProject implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    /**
     * 项目编码
     */
    @Excel(name = "项目编码", width = 15)
    @Schema(description = "项目编码")
    private String projectCode;

    /**
     * 项目名称
     */
    @Excel(name = "项目名称", width = 15)
    @Schema(description = "项目名称")
    private String projectName;

    /**
     * 客户ID
     */
    @Schema(description = "客户ID")
    private String customerId;

    /**
     * 项目经理ID
     */
    @Schema(description = "项目经理ID")
    private String projectManagerId;

    /**
     * 质保开始日期
     */
    @Excel(name = "质保开始日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "质保开始日期")
    private Date warrantyStartDate;

    /**
     * 质保结束日期
     */
    @Excel(name = "质保结束日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "质保结束日期")
    private Date warrantyEndDate;

    /**
     * 质保期（月）
     */
    @Excel(name = "质保期（月）", width = 15)
    @Schema(description = "质保期（月）")
    private Integer warrantyMonths;

    /**
     * 项目地址
     */
    @Excel(name = "项目地址", width = 15)
    @Schema(description = "项目地址")
    private String projectAddress;

    /**
     * 合同金额
     */
    @Excel(name = "合同金额", width = 15)
    @Schema(description = "合同金额")
    private BigDecimal contractAmount;

    /**
     * 项目状态
     */
    @Excel(name = "项目状态", width = 15, dicCode = "project_status")
    @Dict(dicCode = "project_status")
    @Schema(description = "项目状态")
    private Integer status;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private Date createTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private String updateBy;

    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private Date updateTime;

    /**
     * 删除标志
     */
    @Schema(description = "删除标志")
    private Integer delFlag;
}