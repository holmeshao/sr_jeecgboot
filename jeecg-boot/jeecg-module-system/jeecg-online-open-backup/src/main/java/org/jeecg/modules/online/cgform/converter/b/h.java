/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jeecg.common.system.api.ISysBaseAPI
 *  org.jeecg.common.util.SpringContextUtils
 *  org.jeecg.common.util.oConvertUtils
 */
package org.jeecg.modules.online.cgform.converter.b;

import java.util.ArrayList;
import java.util.List;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.converter.a.b;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;

public class h
extends b {
    public h(OnlCgformField onlCgformField) {
        ISysBaseAPI iSysBaseAPI = (ISysBaseAPI)SpringContextUtils.getBean(ISysBaseAPI.class);
        String string = onlCgformField.getDictTable();
        String string2 = onlCgformField.getDictText();
        String string3 = onlCgformField.getDictField();
        List list = new ArrayList();
        if (oConvertUtils.isNotEmpty((Object)string)) {
            list = iSysBaseAPI.queryTableDictItemsByCode(string, string2, string3);
        } else if (oConvertUtils.isNotEmpty((Object)string3)) {
            list = iSysBaseAPI.queryDictItemsByCode(string3);
        }
        this.b = list;
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

