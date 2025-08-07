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
 * 维保工单信息
 *
 * @author jeecg
 * @since 2025-01-24
 */
@Data
@TableName("maintenance_order")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "维保工单信息")
public class MaintenanceOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    /**
     * 工单编号
     */
    @Excel(name = "工单编号", width = 15)
    @Schema(description = "工单编号")
    private String orderNo;

    /**
     * 项目ID
     */
    @Schema(description = "项目ID")
    private String projectId;

    /**
     * 故障标题
     */
    @Excel(name = "故障标题", width = 15)
    @Schema(description = "故障标题")
    private String title;

    /**
     * 故障描述
     */
    @Excel(name = "故障描述", width = 15)
    @Schema(description = "故障描述")
    private String description;

    /**
     * 故障位置
     */
    @Excel(name = "故障位置", width = 15)
    @Schema(description = "故障位置")
    private String location;

    /**
     * 优先级
     */
    @Excel(name = "优先级", width = 15, dicCode = "priority_level")
    @Dict(dicCode = "priority_level")
    @Schema(description = "优先级")
    private Integer priorityLevel;

    /**
     * 当前状态
     */
    @Excel(name = "当前状态", width = 15, dicCode = "order_status")
    @Dict(dicCode = "order_status")
    @Schema(description = "当前状态")
    private String currentStatus;

    /**
     * 当前处理人ID
     */
    @Schema(description = "当前处理人ID")
    private String currentHandlerId;

    /**
     * 当前处理人类型
     */
    @Schema(description = "当前处理人类型")
    private String currentHandlerType;

    /**
     * 分配的维修人员ID
     */
    @Schema(description = "分配的维修人员ID")
    private String assignedTechnicianId;

    /**
     * 分配的劳务班组ID
     */
    @Schema(description = "分配的劳务班组ID")
    private String assignedLaborTeamId;

    /**
     * 流程实例ID
     */
    @Schema(description = "流程实例ID")
    private String processInstanceId;

    /**
     * 提交时间
     */
    @Excel(name = "提交时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "提交时间")
    private Date submitTime;

    /**
     * 确认时间
     */
    @Excel(name = "确认时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "确认时间")
    private Date confirmTime;

    /**
     * 审批完成时间
     */
    @Excel(name = "审批完成时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "审批完成时间")
    private Date approveTime;

    /**
     * 派单时间
     */
    @Excel(name = "派单时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "派单时间")
    private Date assignTime;

    /**
     * 开始维修时间
     */
    @Excel(name = "开始维修时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始维修时间")
    private Date startRepairTime;

    /**
     * 完成维修时间
     */
    @Excel(name = "完成维修时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "完成维修时间")
    private Date finishRepairTime;

    /**
     * 验收时间
     */
    @Excel(name = "验收时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "验收时间")
    private Date acceptTime;

    /**
     * 预估费用
     */
    @Excel(name = "预估费用", width = 15)
    @Schema(description = "预估费用")
    private BigDecimal estimatedCost;

    /**
     * 实际费用
     */
    @Excel(name = "实际费用", width = 15)
    @Schema(description = "实际费用")
    private BigDecimal actualCost;

    /**
     * 材料费用
     */
    @Excel(name = "材料费用", width = 15)
    @Schema(description = "材料费用")
    private BigDecimal materialCost;

    /**
     * 人工费用
     */
    @Excel(name = "人工费用", width = 15)
    @Schema(description = "人工费用")
    private BigDecimal laborCost;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * 删除标志
     */
    @Schema(description = "删除标志")
    private Integer delFlag;
}