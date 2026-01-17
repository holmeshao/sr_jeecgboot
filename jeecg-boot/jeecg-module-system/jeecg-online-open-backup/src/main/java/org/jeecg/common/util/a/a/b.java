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

public class b
extends org.jeecg.common.util.a.b {
    private static final long m = -8939298551502162479L;

    public b() {
    }

    public b(String string, String string2) {
        this.b = "string";
        this.e = "hidden";
        this.a = string;
        this.f = string2;
    }

    @Override
    public Map<String, Object> getPropertyJson() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        hashMap.put("key", this.getKey());
        JSONObject jSONObject = this.getCommonJson();
        jSONObject.put("hidden", (Object)true);
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

