/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 */
package org.jeecg.modules.online.cgform.converter.b;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;
import org.jeecg.modules.online.cgform.a.a;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;

public class f
extends org.jeecg.modules.online.cgform.converter.a.a {
    private String f;

    public String getLinkField() {
        return this.f;
    }

    public void setLinkField(String linkField) {
        this.f = linkField;
    }

    public f(OnlCgformField onlCgformField) {
        String string = onlCgformField.getDictTable();
        a a2 = (a)JSONObject.parseObject((String)string, a.class);
        this.setTable(a2.getTable());
        this.setCode(a2.getKey());
        this.setText(a2.getTxt());
        this.f = a2.getLinkField();
    }

    @Override
    public Map<String, String> getConfig() {
        HashMap<String, String> hashMap = new HashMap<String, String>(5);
        hashMap.put("linkField", this.f);
        return hashMap;
    }
}

