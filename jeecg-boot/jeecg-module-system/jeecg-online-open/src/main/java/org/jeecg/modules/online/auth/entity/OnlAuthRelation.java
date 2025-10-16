/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 *  io.swagger.v3.oas.annotations.media.Schema
 *  org.jeecgframework.poi.excel.annotation.Excel
 */
package org.jeecg.modules.online.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import org.jeecgframework.poi.excel.annotation.Excel;

@TableName(value="onl_auth_relation")
@Schema(description="onl_auth_relation")
public class OnlAuthRelation
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_ID)
    @Schema(description="id")
    private String id;
    @Excel(name="\u89d2\u8272id", width=15.0)
    @Schema(description="\u89d2\u8272id")
    private String roleId;
    @Excel(name="\u6743\u9650id", width=15.0)
    @Schema(description="\u6743\u9650id")
    private String authId;
    @Excel(name="1\u5b57\u6bb5 2\u6309\u94ae 3\u6570\u636e\u6743\u9650", width=15.0)
    @Schema(description="1\u5b57\u6bb5 2\u6309\u94ae 3\u6570\u636e\u6743\u9650")
    private Integer type;
    private String cgformId;
    private String authMode;

    public String getId() {
        return this.id;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public String getAuthId() {
        return this.authId;
    }

    public Integer getType() {
        return this.type;
    }

    public String getCgformId() {
        return this.cgformId;
    }

    public String getAuthMode() {
        return this.authMode;
    }

    public OnlAuthRelation setId(String id) {
        this.id = id;
        return this;
    }

    public OnlAuthRelation setRoleId(String roleId) {
        this.roleId = roleId;
        return this;
    }

    public OnlAuthRelation setAuthId(String authId) {
        this.authId = authId;
        return this;
    }

    public OnlAuthRelation setType(Integer type) {
        this.type = type;
        return this;
    }

    public OnlAuthRelation setCgformId(String cgformId) {
        this.cgformId = cgformId;
        return this;
    }

    public OnlAuthRelation setAuthMode(String authMode) {
        this.authMode = authMode;
        return this;
    }

    public String toString() {
        return "OnlAuthRelation(id=" + this.getId() + ", roleId=" + this.getRoleId() + ", authId=" + this.getAuthId() + ", type=" + this.getType() + ", cgformId=" + this.getCgformId() + ", authMode=" + this.getAuthMode() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnlAuthRelation)) {
            return false;
        }
        OnlAuthRelation onlAuthRelation = (OnlAuthRelation)o;
        if (!onlAuthRelation.canEqual(this)) {
            return false;
        }
        Integer n = this.getType();
        Integer n2 = onlAuthRelation.getType();
        if (n == null ? n2 != null : !((Object)n).equals(n2)) {
            return false;
        }
        String string = this.getId();
        String string2 = onlAuthRelation.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getRoleId();
        String string4 = onlAuthRelation.getRoleId();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getAuthId();
        String string6 = onlAuthRelation.getAuthId();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getCgformId();
        String string8 = onlAuthRelation.getCgformId();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getAuthMode();
        String string10 = onlAuthRelation.getAuthMode();
        return !(string9 == null ? string10 != null : !string9.equals(string10));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OnlAuthRelation;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        Integer n3 = this.getType();
        n2 = n2 * 59 + (n3 == null ? 43 : ((Object)n3).hashCode());
        String string = this.getId();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getRoleId();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getAuthId();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getCgformId();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getAuthMode();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        return n2;
    }
}

