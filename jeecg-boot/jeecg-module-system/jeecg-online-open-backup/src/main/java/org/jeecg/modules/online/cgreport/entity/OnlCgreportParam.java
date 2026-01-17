/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 *  com.fasterxml.jackson.annotation.JsonFormat
 *  org.springframework.format.annotation.DateTimeFormat
 */
package org.jeecg.modules.online.cgreport.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

@TableName(value="onl_cgreport_param")
public class OnlCgreportParam
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_ID)
    private String id;
    private String cgrheadId;
    private String paramName;
    private String paramTxt;
    private String paramValue;
    private Integer orderNum;
    private String createBy;
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String updateBy;
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public String getId() {
        return this.id;
    }

    public String getCgrheadId() {
        return this.cgrheadId;
    }

    public String getParamName() {
        return this.paramName;
    }

    public String getParamTxt() {
        return this.paramTxt;
    }

    public String getParamValue() {
        return this.paramValue;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCgrheadId(String cgrheadId) {
        this.cgrheadId = cgrheadId;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public void setParamTxt(String paramTxt) {
        this.paramTxt = paramTxt;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnlCgreportParam)) {
            return false;
        }
        OnlCgreportParam onlCgreportParam = (OnlCgreportParam)o;
        if (!onlCgreportParam.canEqual(this)) {
            return false;
        }
        Integer n = this.getOrderNum();
        Integer n2 = onlCgreportParam.getOrderNum();
        if (n == null ? n2 != null : !((Object)n).equals(n2)) {
            return false;
        }
        String string = this.getId();
        String string2 = onlCgreportParam.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getCgrheadId();
        String string4 = onlCgreportParam.getCgrheadId();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getParamName();
        String string6 = onlCgreportParam.getParamName();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getParamTxt();
        String string8 = onlCgreportParam.getParamTxt();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getParamValue();
        String string10 = onlCgreportParam.getParamValue();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        String string11 = this.getCreateBy();
        String string12 = onlCgreportParam.getCreateBy();
        if (string11 == null ? string12 != null : !string11.equals(string12)) {
            return false;
        }
        Date date = this.getCreateTime();
        Date date2 = onlCgreportParam.getCreateTime();
        if (date == null ? date2 != null : !((Object)date).equals(date2)) {
            return false;
        }
        String string13 = this.getUpdateBy();
        String string14 = onlCgreportParam.getUpdateBy();
        if (string13 == null ? string14 != null : !string13.equals(string14)) {
            return false;
        }
        Date date3 = this.getUpdateTime();
        Date date4 = onlCgreportParam.getUpdateTime();
        return !(date3 == null ? date4 != null : !((Object)date3).equals(date4));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OnlCgreportParam;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        Integer n3 = this.getOrderNum();
        n2 = n2 * 59 + (n3 == null ? 43 : ((Object)n3).hashCode());
        String string = this.getId();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getCgrheadId();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getParamName();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getParamTxt();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getParamValue();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        String string6 = this.getCreateBy();
        n2 = n2 * 59 + (string6 == null ? 43 : string6.hashCode());
        Date date = this.getCreateTime();
        n2 = n2 * 59 + (date == null ? 43 : ((Object)date).hashCode());
        String string7 = this.getUpdateBy();
        n2 = n2 * 59 + (string7 == null ? 43 : string7.hashCode());
        Date date2 = this.getUpdateTime();
        n2 = n2 * 59 + (date2 == null ? 43 : ((Object)date2).hashCode());
        return n2;
    }

    public String toString() {
        return "OnlCgreportParam(id=" + this.getId() + ", cgrheadId=" + this.getCgrheadId() + ", paramName=" + this.getParamName() + ", paramTxt=" + this.getParamTxt() + ", paramValue=" + this.getParamValue() + ", orderNum=" + this.getOrderNum() + ", createBy=" + this.getCreateBy() + ", createTime=" + this.getCreateTime() + ", updateBy=" + this.getUpdateBy() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

