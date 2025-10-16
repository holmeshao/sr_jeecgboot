/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  org.jeecg.common.system.vo.DictModel
 */
package org.jeecg.common.util.a.a;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.a.b;

public class g
extends b {
    private static final long m = -3200493311633999539L;
    private Integer n;
    private Integer o;

    public Integer getMaxLength() {
        return this.n;
    }

    public void setMaxLength(Integer maxLength) {
        this.n = maxLength;
    }

    public Integer getMinLength() {
        return this.o;
    }

    public void setMinLength(Integer minLength) {
        this.o = minLength;
    }

    public g() {
    }

    public g(String string, String string2, String string3, Integer n) {
        this.n = n;
        this.a = string;
        this.e = string3;
        this.f = string2;
        this.b = "string";
    }

    public g(String string, String string2, String string3, Integer n, List<DictModel> list) {
        this.n = n;
        this.a = string;
        this.e = string3;
        this.f = string2;
        this.b = "string";
        this.c = list;
    }

    @Override
    public Map<String, Object> getPropertyJson() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        hashMap.put("key", this.getKey());
        JSONObject jSONObject = this.getCommonJson();
        if (this.n != null) {
            jSONObject.put("maxLength", (Object)this.n);
        }
        if (this.o != null) {
            jSONObject.put("minLength", (Object)this.o);
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

