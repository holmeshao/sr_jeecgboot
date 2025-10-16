/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.FieldStrategy
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableField
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 *  com.fasterxml.jackson.annotation.JsonFormat
 *  org.apache.ibatis.type.JdbcType
 *  org.jeecg.common.aspect.annotation.Dict
 *  org.springframework.format.annotation.DateTimeFormat
 */
package org.jeecg.modules.online.cgreport.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import org.apache.ibatis.type.JdbcType;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

@TableName(value="onl_cgreport_head")
public class OnlCgreportHead
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_ID)
    private String id;
    private String code;
    private String name;
    private String cgrSql;
    private String returnValField;
    private String returnTxtField;
    private String returnType;
    @TableField(updateStrategy=FieldStrategy.ALWAYS, jdbcType=JdbcType.VARCHAR)
    @Dict(dicCode="code", dicText="name", dictTable="sys_data_source")
    private String dbSource;
    private String content;
    private String lowAppId;
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String updateBy;
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String createBy;

    public String getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String getCgrSql() {
        return this.cgrSql;
    }

    public String getReturnValField() {
        return this.returnValField;
    }

    public String getReturnTxtField() {
        return this.returnTxtField;
    }

    public String getReturnType() {
        return this.returnType;
    }

    public String getDbSource() {
        return this.dbSource;
    }

    public String getContent() {
        return this.content;
    }

    public String getLowAppId() {
        return this.lowAppId;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCgrSql(String cgrSql) {
        this.cgrSql = cgrSql;
    }

    public void setReturnValField(String returnValField) {
        this.returnValField = returnValField;
    }

    public void setReturnTxtField(String returnTxtField) {
        this.returnTxtField = returnTxtField;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void setDbSource(String dbSource) {
        this.dbSource = dbSource;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLowAppId(String lowAppId) {
        this.lowAppId = lowAppId;
    }

    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnlCgreportHead)) {
            return false;
        }
        OnlCgreportHead onlCgreportHead = (OnlCgreportHead)o;
        if (!onlCgreportHead.canEqual(this)) {
            return false;
        }
        String string = this.getId();
        String string2 = onlCgreportHead.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getCode();
        String string4 = onlCgreportHead.getCode();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getName();
        String string6 = onlCgreportHead.getName();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getCgrSql();
        String string8 = onlCgreportHead.getCgrSql();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getReturnValField();
        String string10 = onlCgreportHead.getReturnValField();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        String string11 = this.getReturnTxtField();
        String string12 = onlCgreportHead.getReturnTxtField();
        if (string11 == null ? string12 != null : !string11.equals(string12)) {
            return false;
        }
        String string13 = this.getReturnType();
        String string14 = onlCgreportHead.getReturnType();
        if (string13 == null ? string14 != null : !string13.equals(string14)) {
            return false;
        }
        String string15 = this.getDbSource();
        String string16 = onlCgreportHead.getDbSource();
        if (string15 == null ? string16 != null : !string15.equals(string16)) {
            return false;
        }
        String string17 = this.getContent();
        String string18 = onlCgreportHead.getContent();
        if (string17 == null ? string18 != null : !string17.equals(string18)) {
            return false;
        }
        String string19 = this.getLowAppId();
        String string20 = onlCgreportHead.getLowAppId();
        if (string19 == null ? string20 != null : !string19.equals(string20)) {
            return false;
        }
        Date date = this.getUpdateTime();
        Date date2 = onlCgreportHead.getUpdateTime();
        if (date == null ? date2 != null : !((Object)date).equals(date2)) {
            return false;
        }
        String string21 = this.getUpdateBy();
        String string22 = onlCgreportHead.getUpdateBy();
        if (string21 == null ? string22 != null : !string21.equals(string22)) {
            return false;
        }
        Date date3 = this.getCreateTime();
        Date date4 = onlCgreportHead.getCreateTime();
        if (date3 == null ? date4 != null : !((Object)date3).equals(date4)) {
            return false;
        }
        String string23 = this.getCreateBy();
        String string24 = onlCgreportHead.getCreateBy();
        return !(string23 == null ? string24 != null : !string23.equals(string24));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OnlCgreportHead;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        String string = this.getId();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getCode();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getName();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getCgrSql();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getReturnValField();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        String string6 = this.getReturnTxtField();
        n2 = n2 * 59 + (string6 == null ? 43 : string6.hashCode());
        String string7 = this.getReturnType();
        n2 = n2 * 59 + (string7 == null ? 43 : string7.hashCode());
        String string8 = this.getDbSource();
        n2 = n2 * 59 + (string8 == null ? 43 : string8.hashCode());
        String string9 = this.getContent();
        n2 = n2 * 59 + (string9 == null ? 43 : string9.hashCode());
        String string10 = this.getLowAppId();
        n2 = n2 * 59 + (string10 == null ? 43 : string10.hashCode());
        Date date = this.getUpdateTime();
        n2 = n2 * 59 + (date == null ? 43 : ((Object)date).hashCode());
        String string11 = this.getUpdateBy();
        n2 = n2 * 59 + (string11 == null ? 43 : string11.hashCode());
        Date date2 = this.getCreateTime();
        n2 = n2 * 59 + (date2 == null ? 43 : ((Object)date2).hashCode());
        String string12 = this.getCreateBy();
        n2 = n2 * 59 + (string12 == null ? 43 : string12.hashCode());
        return n2;
    }

    public String toString() {
        return "OnlCgreportHead(id=" + this.getId() + ", code=" + this.getCode() + ", name=" + this.getName() + ", cgrSql=" + this.getCgrSql() + ", returnValField=" + this.getReturnValField() + ", returnTxtField=" + this.getReturnTxtField() + ", returnType=" + this.getReturnType() + ", dbSource=" + this.getDbSource() + ", content=" + this.getContent() + ", lowAppId=" + this.getLowAppId() + ", updateTime=" + this.getUpdateTime() + ", updateBy=" + this.getUpdateBy() + ", createTime=" + this.getCreateTime() + ", createBy=" + this.getCreateBy() + ")";
    }
}

