/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  org.jeecg.common.util.oConvertUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Component
 */
package org.jeecg.modules.online.cgform.enhance.impl.http;

import com.alibaba.fastjson.JSONObject;
import java.util.Map;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.enhance.impl.http.base.CgformEnhanceHttpInter;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceJava;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(value="cgformEnhanceJavaHttpImpl")
public class a
implements CgformEnhanceHttpInter {
    private static final Logger a = LoggerFactory.getLogger(a.class);

    @Override
    public void execute(String tableName, JSONObject record, OnlCgformEnhanceJava enhance) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("tableName", (Object)tableName);
        jSONObject.put("record", (Object)record);
        Object object = this.sendPost(jSONObject, enhance);
        Integer n = null;
        if (object != null && (n = oConvertUtils.getInt((Object)object)) == null && object instanceof JSONObject) {
            JSONObject jSONObject2 = (JSONObject)object;
            n = oConvertUtils.getInt((Object)jSONObject2.get((Object)"code"));
            JSONObject jSONObject3 = jSONObject2.getJSONObject("record");
            if (jSONObject3 != null) {
                record.putAll((Map)jSONObject3);
            }
        }
    }
}

