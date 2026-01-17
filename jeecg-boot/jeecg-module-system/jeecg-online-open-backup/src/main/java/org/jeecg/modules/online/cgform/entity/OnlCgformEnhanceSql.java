/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 */
package org.jeecg.modules.online.cgform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

@TableName(value="onl_cgform_enhance_sql")
public class OnlCgformEnhanceSql
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_UUID)
    private String id;
    private String cgformHeadId;
    private String buttonCode;
    private String cgbSql;
    private String cgbSqlName;
    private String content;

    public String getId() {
        return this.id;
    }

    public String getCgformHeadId() {
        return this.cgformHeadId;
    }

    public String getButtonCode() {
        return this.buttonCode;
    }

    public String getCgbSql() {
        return this.cgbSql;
    }

    public String getCgbSqlName() {
        return this.cgbSqlName;
    }

    public String getContent() {
        return this.content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCgformHeadId(String cgformHeadId) {
        this.cgformHeadId = cgformHeadId;
    }

    public void setButtonCode(String buttonCode) {
        this.buttonCode = buttonCode;
    }

    public void setCgbSql(String cgbSql) {
        this.cgbSql = cgbSql;
    }

    public void setCgbSqlName(String cgbSqlName) {
        this.cgbSqlName = cgbSqlName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnlCgformEnhanceSql)) {
            return false;
        }
        OnlCgformEnhanceSql onlCgformEnhanceSql = (OnlCgformEnhanceSql)o;
        if (!onlCgformEnhanceSql.canEqual(this)) {
            return false;
        }
        String string = this.getId();
        String string2 = onlCgformEnhanceSql.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getCgformHeadId();
        String string4 = onlCgformEnhanceSql.getCgformHeadId();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getButtonCode();
        String string6 = onlCgformEnhanceSql.getButtonCode();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getCgbSql();
        String string8 = onlCgformEnhanceSql.getCgbSql();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getCgbSqlName();
        String string10 = onlCgformEnhanceSql.getCgbSqlName();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        String string11 = this.getContent();
        String string12 = onlCgformEnhanceSql.getContent();
        return !(string11 == null ? string12 != null : !string11.equals(string12));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OnlCgformEnhanceSql;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        String string = this.getId();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getCgformHeadId();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getButtonCode();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getCgbSql();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getCgbSqlName();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        String string6 = this.getContent();
        n2 = n2 * 59 + (string6 == null ? 43 : string6.hashCode());
        return n2;
    }

    public String toString() {
        return "OnlCgformEnhanceSql(id=" + this.getId() + ", cgformHeadId=" + this.getCgformHeadId() + ", buttonCode=" + this.getButtonCode() + ", cgbSql=" + this.getCgbSql() + ", cgbSqlName=" + this.getCgbSqlName() + ", content=" + this.getContent() + ")";
    }
}

