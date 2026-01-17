/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONException
 *  com.alibaba.fastjson.JSONObject
 */
package org.jeecg.common.util.a.a;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;
import org.jeecg.common.util.a.b;

public class h
extends b {
    private String m;

    public h() {
    }

    public h(String string, String string2, String string3) {
        this.b = "string";
        this.e = "switch";
        this.a = string;
        this.f = string2;
        this.m = string3;
    }

    @Override
    public Map<String, Object> getPropertyJson() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        hashMap.put("key", this.getKey());
        JSONObject jSONObject = this.getCommonJson();
        JSONArray jSONArray = new JSONArray();
        if (this.m != null) {
            block5: {
                try {
                    jSONArray = JSONArray.parseArray((String)this.m);
                }
                catch (JSONException jSONException) {
                    JSONObject jSONObject2 = JSONArray.parseObject((String)this.m);
                    if (!jSONObject2.containsKey((Object)"switchOptions")) break block5;
                    jSONArray = jSONObject2.getJSONArray("switchOptions");
                }
            }
            jSONObject.put("extendOption", (Object)jSONArray);
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

