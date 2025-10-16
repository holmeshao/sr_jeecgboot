/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 *  com.fasterxml.jackson.annotation.JsonFormat
 *  io.swagger.v3.oas.annotations.media.Schema
 *  org.jeecgframework.poi.excel.annotation.Excel
 *  org.springframework.format.annotation.DateTimeFormat
 */
package org.jeecg.modules.online.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@TableName(value="onl_auth_data")
@Schema(description="onl_auth_data")
public class OnlAuthData
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_ID)
    @Schema(description="\u4e3b\u952e")
    private String id;
    @Excel(name="online\u8868ID", width=15.0)
    @Schema(description="online\u8868ID")
    private String cgformId;
    @Excel(name="\u89c4\u5219\u540d", width=15.0)
    @Schema(description="\u89c4\u5219\u540d")
    private String ruleName;
    @Excel(name="\u89c4\u5219\u5217", width=15.0)
    @Schema(description="\u89c4\u5219\u5217")
    private String ruleColumn;
    @Excel(name="\u89c4\u5219\u6761\u4ef6 \u5927\u4e8e\u5c0f\u4e8elike", width=15.0)
    @Schema(description="\u89c4\u5219\u6761\u4ef6 \u5927\u4e8e\u5c0f\u4e8elike")
    private String ruleOperator;
    @Excel(name="\u89c4\u5219\u503c", width=15.0)
    @Schema(description="\u89c4\u5219\u503c")
    private String ruleValue;
    @Excel(name="1\u6709\u6548 0\u65e0\u6548", width=15.0)
    @Schema(description="1\u6709\u6548 0\u65e0\u6548")
    private Integer status;
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description="\u521b\u5efa\u65f6\u95f4")
    private Date createTime;
    @Schema(description="\u521b\u5efa\u4eba")
    private String createBy;
    @Schema(description="\u66f4\u65b0\u4eba")
    private String updateBy;
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description="\u66f4\u65b0\u65e5\u671f")
    private Date updateTime;

    public String getId() {
        return this.id;
    }

    public String getCgformId() {
        return this.cgformId;
    }

    public String getRuleName() {
        return this.ruleName;
    }

    public String getRuleColumn() {
        return this.ruleColumn;
    }

    public String getRuleOperator() {
        return this.ruleOperator;
    }

    public String getRuleValue() {
        return this.ruleValue;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public OnlAuthData setId(String id) {
        this.id = id;
        return this;
    }

    public OnlAuthData setCgformId(String cgformId) {
        this.cgformId = cgformId;
        return this;
    }

    public OnlAuthData setRuleName(String ruleName) {
        this.ruleName = ruleName;
        return this;
    }

    public OnlAuthData setRuleColumn(String ruleColumn) {
        this.ruleColumn = ruleColumn;
        return this;
    }

    public OnlAuthData setRuleOperator(String ruleOperator) {
        this.ruleOperator = ruleOperator;
        return this;
    }

    public OnlAuthData setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
        return this;
    }

    public OnlAuthData setStatus(Integer status) {
        this.status = status;
        return this;
    }

    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
    public OnlAuthData setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public OnlAuthData setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public OnlAuthData setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
    public OnlAuthData setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String toString() {
        return "OnlAuthData(id=" + this.getId() + ", cgformId=" + this.getCgformId() + ", ruleName=" + this.getRuleName() + ", ruleColumn=" + this.getRuleColumn() + ", ruleOperator=" + this.getRuleOperator() + ", ruleValue=" + this.getRuleValue() + ", status=" + this.getStatus() + ", createTime=" + this.getCreateTime() + ", createBy=" + this.getCreateBy() + ", updateBy=" + this.getUpdateBy() + ", updateTime=" + this.getUpdateTime() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnlAuthData)) {
            return false;
        }
        OnlAuthData onlAuthData = (OnlAuthData)o;
        if (!onlAuthData.canEqual(this)) {
            return false;
        }
        Integer n = this.getStatus();
        Integer n2 = onlAuthData.getStatus();
        if (n == null ? n2 != null : !((Object)n).equals(n2)) {
            return false;
        }
        String string = this.getId();
        String string2 = onlAuthData.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getCgformId();
        String string4 = onlAuthData.getCgformId();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getRuleName();
        String string6 = onlAuthData.getRuleName();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getRuleColumn();
        String string8 = onlAuthData.getRuleColumn();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getRuleOperator();
        String string10 = onlAuthData.getRuleOperator();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        String string11 = this.getRuleValue();
        String string12 = onlAuthData.getRuleValue();
        if (string11 == null ? string12 != null : !string11.equals(string12)) {
            return false;
        }
        Date date = this.getCreateTime();
        Date date2 = onlAuthData.getCreateTime();
        if (date == null ? date2 != null : !((Object)date).equals(date2)) {
            return false;
        }
        String string13 = this.getCreateBy();
        String string14 = onlAuthData.getCreateBy();
        if (string13 == null ? string14 != null : !string13.equals(string14)) {
            return false;
        }
        String string15 = this.getUpdateBy();
        String string16 = onlAuthData.getUpdateBy();
        if (string15 == null ? string16 != null : !string15.equals(string16)) {
            return false;
        }
        Date date3 = this.getUpdateTime();
        Date date4 = onlAuthData.getUpdateTime();
        return !(date3 == null ? date4 != null : !((Object)date3).equals(date4));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OnlAuthData;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        Integer n3 = this.getStatus();
        n2 = n2 * 59 + (n3 == null ? 43 : ((Object)n3).hashCode());
        String string = this.getId();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getCgformId();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getRuleName();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getRuleColumn();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getRuleOperator();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        String string6 = this.getRuleValue();
        n2 = n2 * 59 + (string6 == null ? 43 : string6.hashCode());
        Date date = this.getCreateTime();
        n2 = n2 * 59 + (date == null ? 43 : ((Object)date).hashCode());
        String string7 = this.getCreateBy();
        n2 = n2 * 59 + (string7 == null ? 43 : string7.hashCode());
        String string8 = this.getUpdateBy();
        n2 = n2 * 59 + (string8 == null ? 43 : string8.hashCode());
        Date date2 = this.getUpdateTime();
        n2 = n2 * 59 + (date2 == null ? 43 : ((Object)date2).hashCode());
        return n2;
    }
}

