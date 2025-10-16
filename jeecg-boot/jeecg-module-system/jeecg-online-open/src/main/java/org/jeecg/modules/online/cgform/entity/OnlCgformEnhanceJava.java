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

@TableName(value="onl_cgform_enhance_java")
public class OnlCgformEnhanceJava
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_UUID)
    private String id;
    private String cgformHeadId;
    private String buttonCode;
    private String cgJavaType;
    private String cgJavaValue;
    private String activeStatus;
    private String event;

    public String getId() {
        return this.id;
    }

    public String getCgformHeadId() {
        return this.cgformHeadId;
    }

    public String getButtonCode() {
        return this.buttonCode;
    }

    public String getCgJavaType() {
        return this.cgJavaType;
    }

    public String getCgJavaValue() {
        return this.cgJavaValue;
    }

    public String getActiveStatus() {
        return this.activeStatus;
    }

    public String getEvent() {
        return this.event;
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

    public void setCgJavaType(String cgJavaType) {
        this.cgJavaType = cgJavaType;
    }

    public void setCgJavaValue(String cgJavaValue) {
        this.cgJavaValue = cgJavaValue;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnlCgformEnhanceJava)) {
            return false;
        }
        OnlCgformEnhanceJava onlCgformEnhanceJava = (OnlCgformEnhanceJava)o;
        if (!onlCgformEnhanceJava.canEqual(this)) {
            return false;
        }
        String string = this.getId();
        String string2 = onlCgformEnhanceJava.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getCgformHeadId();
        String string4 = onlCgformEnhanceJava.getCgformHeadId();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getButtonCode();
        String string6 = onlCgformEnhanceJava.getButtonCode();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getCgJavaType();
        String string8 = onlCgformEnhanceJava.getCgJavaType();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getCgJavaValue();
        String string10 = onlCgformEnhanceJava.getCgJavaValue();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        String string11 = this.getActiveStatus();
        String string12 = onlCgformEnhanceJava.getActiveStatus();
        if (string11 == null ? string12 != null : !string11.equals(string12)) {
            return false;
        }
        String string13 = this.getEvent();
        String string14 = onlCgformEnhanceJava.getEvent();
        return !(string13 == null ? string14 != null : !string13.equals(string14));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OnlCgformEnhanceJava;
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
        String string4 = this.getCgJavaType();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getCgJavaValue();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        String string6 = this.getActiveStatus();
        n2 = n2 * 59 + (string6 == null ? 43 : string6.hashCode());
        String string7 = this.getEvent();
        n2 = n2 * 59 + (string7 == null ? 43 : string7.hashCode());
        return n2;
    }

    public String toString() {
        return "OnlCgformEnhanceJava(id=" + this.getId() + ", cgformHeadId=" + this.getCgformHeadId() + ", buttonCode=" + this.getButtonCode() + ", cgJavaType=" + this.getCgJavaType() + ", cgJavaValue=" + this.getCgJavaValue() + ", activeStatus=" + this.getActiveStatus() + ", event=" + this.getEvent() + ")";
    }
}

