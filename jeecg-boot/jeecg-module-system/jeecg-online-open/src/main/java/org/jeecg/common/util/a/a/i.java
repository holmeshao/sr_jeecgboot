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

public class i
extends b {
    private static final long m = 3786503639885610767L;
    private String n;
    private String o;
    private String p;
    private String q;
    private String r;
    private Integer s = 0;

    public String getDict() {
        return this.n;
    }

    public void setDict(String dict) {
        this.n = dict;
    }

    public String getPidField() {
        return this.o;
    }

    public void setPidField(String pidField) {
        this.o = pidField;
    }

    public String getPidValue() {
        return this.p;
    }

    public void setPidValue(String pidValue) {
        this.p = pidValue;
    }

    public String getHasChildField() {
        return this.q;
    }

    public void setHasChildField(String hasChildField) {
        this.q = hasChildField;
    }

    public i() {
    }

    public String getTextField() {
        return this.r;
    }

    public void setTextField(String textField) {
        this.r = textField;
    }

    public Integer getPidComponent() {
        return this.s;
    }

    public void setPidComponent(Integer pidComponent) {
        this.s = pidComponent;
    }

    public i(String string, String string2, String string3, String string4, String string5) {
        this.b = "string";
        this.e = "sel_tree";
        this.a = string;
        this.f = string2;
        this.n = string3;
        this.o = string4;
        this.p = string5;
    }

    public i(String string, String string2, String string3) {
        this.b = "string";
        this.e = "cat_tree";
        this.a = string;
        this.f = string2;
        this.p = string3;
    }

    public i(String string, String string2, String string3, String string4) {
        this(string, string2, string3);
        this.r = string4;
    }

    @Override
    public Map<String, Object> getPropertyJson() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        hashMap.put("key", this.getKey());
        JSONObject jSONObject = this.getCommonJson();
        if (this.n != null) {
            jSONObject.put("dict", (Object)this.n);
        }
        if (this.o != null) {
            jSONObject.put("pidField", (Object)this.o);
        }
        if (this.p != null) {
            jSONObject.put("pidValue", (Object)this.p);
        }
        if (this.r != null) {
            jSONObject.put("textField", (Object)this.r);
        }
        if (this.q != null) {
            jSONObject.put("hasChildField", (Object)this.q);
        }
        if (this.s != null) {
            jSONObject.put("pidComponent", (Object)this.s);
        }
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

