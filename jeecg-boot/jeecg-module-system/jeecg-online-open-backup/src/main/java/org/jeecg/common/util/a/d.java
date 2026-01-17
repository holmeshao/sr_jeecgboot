/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.jeecg.common.util.a;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jeecg.common.util.a.b;
import org.jeecg.common.util.a.c;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class d {
    private static final Logger a = LoggerFactory.getLogger(d.class);

    public static JSONObject a(c c2, List<b> list) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("$schema", (Object)c2.get$schema());
        jSONObject.put("type", (Object)c2.getType());
        jSONObject.put("title", (Object)c2.getTitle());
        List<String> list2 = c2.getRequired();
        jSONObject.put("required", list2);
        JSONObject jSONObject2 = new JSONObject();
        for (b b2 : list) {
            Map<String, Object> map = b2.getPropertyJson();
            jSONObject2.put(map.get("key").toString(), map.get("prop"));
        }
        jSONObject.put("properties", (Object)jSONObject2);
        return jSONObject;
    }

    public static JSONObject a(String string, List<String> list, List<b> list2) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("type", (Object)"object");
        jSONObject.put("view", (Object)"tab");
        jSONObject.put("title", (Object)string);
        if (list == null) {
            list = new ArrayList<String>();
        }
        jSONObject.put("required", list);
        JSONObject jSONObject2 = new JSONObject();
        for (b b2 : list2) {
            Map<String, Object> map = b2.getPropertyJson();
            jSONObject2.put(map.get("key").toString(), map.get("prop"));
        }
        jSONObject.put("properties", (Object)jSONObject2);
        return jSONObject;
    }
}

