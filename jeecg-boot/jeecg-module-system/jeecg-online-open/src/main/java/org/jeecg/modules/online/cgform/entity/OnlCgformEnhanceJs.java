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

@TableName(value="onl_cgform_enhance_js")
public class OnlCgformEnhanceJs
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_UUID)
    private String id;
    private String cgformHeadId;
    private String cgJsType;
    private String cgJs;
    private String content;

    public String getId() {
        return this.id;
    }

    public String getCgformHeadId() {
        return this.cgformHeadId;
    }

    public String getCgJsType() {
        return this.cgJsType;
    }

    public String getCgJs() {
        return this.cgJs;
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

    public void setCgJsType(String cgJsType) {
        this.cgJsType = cgJsType;
    }

    public void setCgJs(String cgJs) {
        this.cgJs = cgJs;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnlCgformEnhanceJs)) {
            return false;
        }
        OnlCgformEnhanceJs onlCgformEnhanceJs = (OnlCgformEnhanceJs)o;
        if (!onlCgformEnhanceJs.canEqual(this)) {
            return false;
        }
        String string = this.getId();
        String string2 = onlCgformEnhanceJs.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getCgformHeadId();
        String string4 = onlCgformEnhanceJs.getCgformHeadId();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getCgJsType();
        String string6 = onlCgformEnhanceJs.getCgJsType();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getCgJs();
        String string8 = onlCgformEnhanceJs.getCgJs();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getContent();
        String string10 = onlCgformEnhanceJs.getContent();
        return !(string9 == null ? string10 != null : !string9.equals(string10));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OnlCgformEnhanceJs;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        String string = this.getId();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getCgformHeadId();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getCgJsType();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getCgJs();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getContent();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        return n2;
    }

    public String toString() {
        return "OnlCgformEnhanceJs(id=" + this.getId() + ", cgformHeadId=" + this.getCgformHeadId() + ", cgJsType=" + this.getCgJsType() + ", cgJs=" + this.getCgJs() + ", content=" + this.getContent() + ")";
    }
}

