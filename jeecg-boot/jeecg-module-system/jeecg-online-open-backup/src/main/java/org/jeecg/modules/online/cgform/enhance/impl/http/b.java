/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONObject
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Component
 */
package org.jeecg.modules.online.cgform.enhance.impl.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Map;
import org.jeecg.modules.online.cgform.enhance.impl.http.base.CgformEnhanceHttpInter;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceJava;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(value="cgformEnhanceJavaListHttpImpl")
public class b
implements CgformEnhanceHttpInter {
    private static final Logger a = LoggerFactory.getLogger(b.class);

    @Override
    public void execute(String tableName, List<Map<String, Object>> dataList, OnlCgformEnhanceJava enhance) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("tableName", (Object)tableName);
        jSONObject.put("dataList", dataList);
        Object object = this.sendPost(jSONObject, enhance);
        if (object instanceof JSONArray) {
            JSONArray jSONArray = (JSONArray)object;
            for (int i2 = 0; i2 < dataList.size(); ++i2) {
                Map<String, Object> map = dataList.get(i2);
                JSONObject jSONObject2 = jSONArray.getJSONObject(i2);
                map.putAll((Map<String, Object>)jSONObject2);
            }
        }
    }
}

