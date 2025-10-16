/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 *  com.fasterxml.jackson.annotation.JsonFormat
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  io.swagger.v3.oas.annotations.media.Schema
 *  org.jeecgframework.poi.excel.annotation.Excel
 *  org.springframework.format.annotation.DateTimeFormat
 */
package org.jeecg.modules.online.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@TableName(value="onl_auth_page")
@Schema(description="onl_auth_page")
public class OnlAuthPage
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_ID)
    @Schema(description=" \u4e3b\u952e")
    private String id;
    @Excel(name="online\u8868id", width=15.0)
    @Schema(description="online\u8868id")
    private String cgformId;
    @Excel(name="\u5b57\u6bb5\u540d/\u6309\u94ae\u7f16\u7801", width=15.0)
    @Schema(description="\u5b57\u6bb5\u540d/\u6309\u94ae\u7f16\u7801")
    private String code;
    @Excel(name="1\u5b57\u6bb5 2\u6309\u94ae", width=15.0)
    @Schema(description="1\u5b57\u6bb5 2\u6309\u94ae")
    private Integer type;
    @Excel(name="3\u53ef\u7f16\u8f91 5\u53ef\u89c1", width=15.0)
    @Schema(description="3\u53ef\u7f16\u8f91 5\u53ef\u89c1")
    private Integer control;
    @Excel(name="3\u5217\u8868 5\u8868\u5355", width=15.0)
    @Schema(description="3\u5217\u8868 5\u8868\u5355")
    private Integer page;
    @Excel(name="1\u6709\u6548 0\u65e0\u6548", width=15.0)
    @Schema(description="1\u6709\u6548 0\u65e0\u6548")
    private Integer status;
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description="\u521b\u5efa\u65f6\u95f4")
    @JsonIgnore
    private Date createTime;
    @Schema(description="\u521b\u5efa\u4eba")
    @JsonIgnore
    private String createBy;
    @Schema(description="\u66f4\u65b0\u4eba")
    @JsonIgnore
    private String updateBy;
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description="\u66f4\u65b0\u65e5\u671f")
    @JsonIgnore
    private Date updateTime;

    public OnlAuthPage() {
    }

    public OnlAuthPage(String cgformId, String code, int page, int control) {
        this.type = 1;
        this.cgformId = cgformId;
        this.code = code;
        this.control = control;
        this.page = page;
        this.status = 1;
    }

    public String getId() {
        return this.id;
    }

    public String getCgformId() {
        return this.cgformId;
    }

    public String getCode() {
        return this.code;
    }

    public Integer getType() {
        return this.type;
    }

    public Integer getControl() {
        return this.control;
    }

    public Integer getPage() {
        return this.page;
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

    public OnlAuthPage setId(String id) {
        this.id = id;
        return this;
    }

    public OnlAuthPage setCgformId(String cgformId) {
        this.cgformId = cgformId;
        return this;
    }

    public OnlAuthPage setCode(String code) {
        this.code = code;
        return this;
    }

    public OnlAuthPage setType(Integer type) {
        this.type = type;
        return this;
    }

    public OnlAuthPage setControl(Integer control) {
        this.control = control;
        return this;
    }

    public OnlAuthPage setPage(Integer page) {
        this.page = page;
        return this;
    }

    public OnlAuthPage setStatus(Integer status) {
        this.status = status;
        return this;
    }

    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
    @JsonIgnore
    public OnlAuthPage setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    @JsonIgnore
    public OnlAuthPage setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    @JsonIgnore
    public OnlAuthPage setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
    @JsonIgnore
    public OnlAuthPage setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String toString() {
        return "OnlAuthPage(id=" + this.getId() + ", cgformId=" + this.getCgformId() + ", code=" + this.getCode() + ", type=" + this.getType() + ", control=" + this.getControl() + ", page=" + this.getPage() + ", status=" + this.getStatus() + ", createTime=" + this.getCreateTime() + ", createBy=" + this.getCreateBy() + ", updateBy=" + this.getUpdateBy() + ", updateTime=" + this.getUpdateTime() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnlAuthPage)) {
            return false;
        }
        OnlAuthPage onlAuthPage = (OnlAuthPage)o;
        if (!onlAuthPage.canEqual(this)) {
            return false;
        }
        Integer n = this.getType();
        Integer n2 = onlAuthPage.getType();
        if (n == null ? n2 != null : !((Object)n).equals(n2)) {
            return false;
        }
        Integer n3 = this.getControl();
        Integer n4 = onlAuthPage.getControl();
        if (n3 == null ? n4 != null : !((Object)n3).equals(n4)) {
            return false;
        }
        Integer n5 = this.getPage();
        Integer n6 = onlAuthPage.getPage();
        if (n5 == null ? n6 != null : !((Object)n5).equals(n6)) {
            return false;
        }
        Integer n7 = this.getStatus();
        Integer n8 = onlAuthPage.getStatus();
        if (n7 == null ? n8 != null : !((Object)n7).equals(n8)) {
            return false;
        }
        String string = this.getId();
        String string2 = onlAuthPage.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getCgformId();
        String string4 = onlAuthPage.getCgformId();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getCode();
        String string6 = onlAuthPage.getCode();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        Date date = this.getCreateTime();
        Date date2 = onlAuthPage.getCreateTime();
        if (date == null ? date2 != null : !((Object)date).equals(date2)) {
            return false;
        }
        String string7 = this.getCreateBy();
        String string8 = onlAuthPage.getCreateBy();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getUpdateBy();
        String string10 = onlAuthPage.getUpdateBy();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        Date date3 = this.getUpdateTime();
        Date date4 = onlAuthPage.getUpdateTime();
        return !(date3 == null ? date4 != null : !((Object)date3).equals(date4));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OnlAuthPage;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        Integer n3 = this.getType();
        n2 = n2 * 59 + (n3 == null ? 43 : ((Object)n3).hashCode());
        Integer n4 = this.getControl();
        n2 = n2 * 59 + (n4 == null ? 43 : ((Object)n4).hashCode());
        Integer n5 = this.getPage();
        n2 = n2 * 59 + (n5 == null ? 43 : ((Object)n5).hashCode());
        Integer n6 = this.getStatus();
        n2 = n2 * 59 + (n6 == null ? 43 : ((Object)n6).hashCode());
        String string = this.getId();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getCgformId();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getCode();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        Date date = this.getCreateTime();
        n2 = n2 * 59 + (date == null ? 43 : ((Object)date).hashCode());
        String string4 = this.getCreateBy();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getUpdateBy();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        Date date2 = this.getUpdateTime();
        n2 = n2 * 59 + (date2 == null ? 43 : ((Object)date2).hashCode());
        return n2;
    }
}

