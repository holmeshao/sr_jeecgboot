/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 */
package org.jeecg.common.util.a.a;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jeecg.common.util.a.a;
import org.jeecg.common.util.a.b;

public class c
extends b {
    String m;
    List<a> n;

    public String getDictTable() {
        return this.m;
    }

    public void setDictTable(String dictTable) {
        this.m = dictTable;
    }

    public List<a> getOtherColumns() {
        return this.n;
    }

    public void setOtherColumns(List<a> otherColumns) {
        this.n = otherColumns;
    }

    public c() {
    }

    public c(String string, String string2, String string3) {
        this.b = "string";
        this.e = "link_down";
        this.a = string;
        this.f = string2;
        this.m = string3;
    }

    @Override
    public Map<String, Object> getPropertyJson() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        hashMap.put("key", this.getKey());
        JSONObject jSONObject = this.getCommonJson();
        JSONObject jSONObject2 = JSONObject.parseObject((String)this.m);
        jSONObject.put("config", (Object)jSONObject2);
        jSONObject.put("others", this.n);
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

