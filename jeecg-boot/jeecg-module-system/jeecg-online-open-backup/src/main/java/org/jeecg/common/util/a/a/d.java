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

public class d
extends b {
    private static final long m = -558615331436437200L;
    private Integer n;
    private Integer o;
    private Integer p;
    private Integer q;
    private Integer r;

    public Integer getMultipleOf() {
        return this.n;
    }

    public void setMultipleOf(Integer multipleOf) {
        this.n = multipleOf;
    }

    public Integer getMaxinum() {
        return this.o;
    }

    public void setMaxinum(Integer maxinum) {
        this.o = maxinum;
    }

    public Integer getExclusiveMaximum() {
        return this.p;
    }

    public void setExclusiveMaximum(Integer exclusiveMaximum) {
        this.p = exclusiveMaximum;
    }

    public Integer getMinimum() {
        return this.q;
    }

    public void setMinimum(Integer minimum) {
        this.q = minimum;
    }

    public Integer getExclusiveMinimum() {
        return this.r;
    }

    public void setExclusiveMinimum(Integer exclusiveMinimum) {
        this.r = exclusiveMinimum;
    }

    public d() {
    }

    public d(String string, String string2, String string3) {
        this.a = string;
        this.b = string3;
        this.f = string2;
        this.e = "number";
    }

    public d(String string, String string2, String string3, List<DictModel> list) {
        this.b = "integer";
        this.a = string;
        this.e = string3;
        this.f = string2;
        this.c = list;
    }

    @Override
    public Map<String, Object> getPropertyJson() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        hashMap.put("key", this.getKey());
        JSONObject jSONObject = this.getCommonJson();
        if (this.n != null) {
            jSONObject.put("multipleOf", (Object)this.n);
        }
        if (this.o != null) {
            jSONObject.put("maxinum", (Object)this.o);
        }
        if (this.p != null) {
            jSONObject.put("exclusiveMaximum", (Object)this.p);
        }
        if (this.q != null) {
            jSONObject.put("minimum", (Object)this.q);
        }
        if (this.r != null) {
            jSONObject.put("exclusiveMinimum", (Object)this.r);
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

