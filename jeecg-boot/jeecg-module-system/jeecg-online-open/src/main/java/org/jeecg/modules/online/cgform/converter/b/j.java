/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONObject
 *  org.jeecg.common.system.vo.DictModel
 */
package org.jeecg.modules.online.cgform.converter.b;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.online.cgform.converter.a.b;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;

public class j
extends b {
    public j(OnlCgformField onlCgformField) {
        JSONObject jSONObject;
        JSONArray jSONArray;
        String string = onlCgformField.getFieldExtendJson();
        String string2 = "Y";
        String string3 = "N";
        if (string != null && !"".equals(string)) {
            block4: {
                jSONArray = null;
                try {
                    jSONArray = JSONArray.parseArray((String)string);
                }
                catch (Exception exception) {
                    jSONObject = JSONArray.parseObject((String)string);
                    if (!jSONObject.containsKey((Object)"switchOptions")) break block4;
                    jSONArray = jSONObject.getJSONArray("switchOptions");
                }
            }
            if (jSONArray != null && jSONArray.size() == 2) {
                string2 = jSONArray.get(0).toString();
                string3 = jSONArray.get(1).toString();
            }
        }
        jSONArray = new ArrayList();
        DictModel dictModel = new DictModel(string2, "\u662f");
        jSONObject = new DictModel(string3, "\u5426");
        jSONArray.add(dictModel);
        jSONArray.add(jSONObject);
        this.b = jSONArray;
        this.a = onlCgformField.getDbFieldName();
    }
}

