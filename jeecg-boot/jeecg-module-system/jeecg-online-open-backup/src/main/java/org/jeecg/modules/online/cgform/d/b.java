/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jeecg.common.system.api.ISysBaseAPI
 *  org.jeecg.common.util.SpringContextUtils
 *  org.jeecgframework.poi.handler.impl.ExcelDataHandlerDefaultImpl
 *  org.jeecgframework.poi.util.PoiPublicUtil
 */
package org.jeecg.modules.online.cgform.d;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.online.cgform.d.c;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecgframework.poi.handler.impl.ExcelDataHandlerDefaultImpl;
import org.jeecgframework.poi.util.PoiPublicUtil;

public class b
extends ExcelDataHandlerDefaultImpl {
    Map<String, OnlCgformField> a;
    ISysBaseAPI b;
    String c;
    String d;
    String e;

    public b(List<OnlCgformField> list, String string, String string2) {
        this.a = this.a(list);
        this.c = string;
        this.d = "online";
        this.e = string2;
        this.b = (ISysBaseAPI)SpringContextUtils.getBean(ISysBaseAPI.class);
    }

    private Map<String, OnlCgformField> a(List<OnlCgformField> list) {
        HashMap<String, OnlCgformField> hashMap = new HashMap<String, OnlCgformField>();
        for (OnlCgformField onlCgformField : list) {
            hashMap.put(onlCgformField.getDbFieldTxt(), onlCgformField);
        }
        return hashMap;
    }

    public void setMapValue(Map<String, Object> map, String originKey, Object value) {
        String string = this.a(originKey);
        if (value instanceof Double) {
            map.put(string, PoiPublicUtil.doubleToString((Double)((Double)value)));
        } else if (value instanceof byte[]) {
            byte[] byArray = (byte[])value;
            String string2 = org.jeecg.modules.online.cgform.d.c.a(byArray, this.c, this.d, this.e);
            if (string2 != null) {
                map.put(string, string2);
            }
        } else {
            map.put(string, value == null ? "" : value.toString());
        }
    }

    private String a(String string) {
        if (this.a.containsKey(string)) {
            return "$mainTable$" + this.a.get(string).getDbFieldName();
        }
        return "$subTable$" + string;
    }
}

