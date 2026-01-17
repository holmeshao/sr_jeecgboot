/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  org.jeecg.common.system.api.ISysBaseAPI
 *  org.jeecg.common.util.SpringContextUtils
 *  org.jeecg.common.util.oConvertUtils
 */
package org.jeecg.modules.online.cgform.converter.b;

import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.converter.a.b;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;

public class c
extends b {
    public c(OnlCgformField onlCgformField) {
        Object object;
        ISysBaseAPI iSysBaseAPI = (ISysBaseAPI)SpringContextUtils.getBean(ISysBaseAPI.class);
        String string = "SYS_DEPART";
        String string2 = "DEPART_NAME";
        String string3 = "ID";
        String string4 = onlCgformField.getFieldExtendJson();
        if (oConvertUtils.isNotEmpty((Object)string4)) {
            String string5;
            object = JSON.parseObject((String)string4);
            if (object.containsKey((Object)"store")) {
                string5 = object.getString("store");
                string3 = oConvertUtils.camelToUnderline((String)string5);
            }
            if (object.containsKey((Object)"text")) {
                string5 = object.getString("text");
                string2 = oConvertUtils.camelToUnderline((String)string5);
            }
        }
        object = iSysBaseAPI.queryTableDictItemsByCode(string, string2, string3);
        this.b = object;
        this.a = onlCgformField.getDbFieldName();
    }

    @Override
    public String converterToVal(String txt) {
        if (oConvertUtils.isEmpty((Object)txt)) {
            return null;
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string : txt.split(",")) {
            String string2 = super.converterToVal(string);
            if (string2 == null) continue;
            arrayList.add(string2);
        }
        return String.join((CharSequence)",", arrayList);
    }

    @Override
    public String converterToTxt(String val) {
        if (oConvertUtils.isEmpty((Object)val)) {
            return null;
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string : val.split(",")) {
            String string2 = super.converterToTxt(string);
            if (string2 == null) continue;
            arrayList.add(string2);
        }
        return String.join((CharSequence)",", arrayList);
    }
}

