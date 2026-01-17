/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 */
package org.jeecg.common.util.a.a;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;
import org.jeecg.common.util.a.b;

public class f
extends b {
    private static final long m = -3200493311633999539L;
    private String n;
    private String o;
    private String p;
    private Boolean q;

    public String getCode() {
        return this.n;
    }

    public void setCode(String code) {
        this.n = code;
    }

    public String getDestFields() {
        return this.o;
    }

    public void setDestFields(String destFields) {
        this.o = destFields;
    }

    public String getOrgFields() {
        return this.p;
    }

    public void setOrgFields(String orgFields) {
        this.p = orgFields;
    }

    public Boolean getPopupMulti() {
        return this.q;
    }

    public void setPopupMulti(Boolean popupMulti) {
        this.q = popupMulti;
    }

    public f() {
    }

    public f(String string, String string2, String string3, String string4, String string5) {
        this.e = "popup";
        this.b = "string";
        this.a = string;
        this.f = string2;
        this.n = string3;
        this.o = string4;
        this.p = string5;
        this.q = true;
    }

    @Override
    public Map<String, Object> getPropertyJson() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        hashMap.put("key", this.getKey());
        JSONObject jSONObject = this.getCommonJson();
        if (this.n != null) {
            jSONObject.put("code", (Object)this.n);
        }
        if (this.o != null) {
            jSONObject.put("destFields", (Object)this.o);
        }
        if (this.p != null) {
            jSONObject.put("orgFields", (Object)this.p);
        }
        jSONObject.put("popupMulti", (Object)this.q);
        if (super.getPattern() != null) {
            jSONObject.put("pattern", (Object)super.getPattern());
        }
        if (super.getErrorInfo() != null) {
            jSONObject.put("errorInfo", (Object)super.getErrorInfo());
        }
        hashMap.put("prop", jSONObject);
        return hashMap;
    }
}

