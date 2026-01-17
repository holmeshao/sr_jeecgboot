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

public class a
extends b {
    private static final long m = 3786503639885610767L;
    private String n;
    private String o;
    private String p;

    public String getDictCode() {
        return this.n;
    }

    public void setDictCode(String dictCode) {
        this.n = dictCode;
    }

    public String getDictTable() {
        return this.o;
    }

    public void setDictTable(String dictTable) {
        this.o = dictTable;
    }

    public String getDictText() {
        return this.p;
    }

    public void setDictText(String dictText) {
        this.p = dictText;
    }

    public a() {
    }

    public a(String string, String string2, String string3, String string4, String string5) {
        this.b = "string";
        this.e = "sel_search";
        this.a = string;
        this.f = string2;
        this.n = string4;
        this.o = string3;
        this.p = string5;
    }

    public a(String string, String string2, String string3, String string4, String string5, String string6) {
        this.b = "string";
        this.e = string2;
        this.a = string;
        this.f = string3;
        this.n = string5;
        this.o = string4;
        this.p = string6;
    }

    @Override
    public Map<String, Object> getPropertyJson() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        hashMap.put("key", this.getKey());
        JSONObject jSONObject = this.getCommonJson();
        if (this.n != null) {
            jSONObject.put("dictCode", (Object)this.n);
        }
        if (this.o != null) {
            jSONObject.put("dictTable", (Object)this.o);
        }
        if (this.p != null) {
            jSONObject.put("dictText", (Object)this.p);
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

