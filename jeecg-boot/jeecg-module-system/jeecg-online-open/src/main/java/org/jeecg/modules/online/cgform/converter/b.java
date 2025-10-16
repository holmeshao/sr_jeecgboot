/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jeecg.common.util.oConvertUtils
 */
package org.jeecg.modules.online.cgform.converter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.converter.FieldCommentConverter;
import org.jeecg.modules.online.cgform.converter.a;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;

public class b {
    public static final int a = 2;
    public static final int b = 1;

    public static void a(int n, List<Map<String, Object>> list, List<OnlCgformField> list2) {
        Map<String, FieldCommentConverter> map = org.jeecg.modules.online.cgform.converter.a.a(list2);
        for (Map<String, Object> map2 : list) {
            Iterator<Map.Entry<String, Object>> iterator = map2.entrySet().iterator();
            HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
            while (iterator.hasNext()) {
                String string;
                String string2;
                FieldCommentConverter fieldCommentConverter;
                Map.Entry<String, Object> entry = iterator.next();
                Object object = entry.getValue();
                if (object == null || (fieldCommentConverter = map.get((string2 = (String)entry.getKey()).toLowerCase())) == null) continue;
                String string3 = object.toString();
                String string4 = string = n == 1 ? fieldCommentConverter.converterToTxt(string3) : fieldCommentConverter.converterToVal(string3);
                if (string == null) {
                    string = string3;
                }
                org.jeecg.modules.online.cgform.converter.b.a(fieldCommentConverter, map2, n);
                org.jeecg.modules.online.cgform.converter.b.a(fieldCommentConverter, hashMap, string3);
                map2.put(string2, string);
            }
            for (Object object : hashMap.keySet()) {
                map2.put((String)object, hashMap.get(object));
            }
        }
    }

    private static void a(FieldCommentConverter fieldCommentConverter, Map<String, Object> map, int n) {
        String string;
        Map<String, String> map2 = fieldCommentConverter.getConfig();
        if (map2 != null && oConvertUtils.isNotEmpty((Object)(string = map2.get("linkField")))) {
            for (String string2 : string.split(",")) {
                Object object = map.get(string2);
                if (object == null) continue;
                String string3 = object.toString();
                String string4 = n == 1 ? fieldCommentConverter.converterToTxt(string3) : fieldCommentConverter.converterToVal(string3);
                map.put(string2, string4);
            }
        }
    }

    private static void a(FieldCommentConverter fieldCommentConverter, Map<String, Object> map, String string) {
        String string2;
        Map<String, String> map2 = fieldCommentConverter.getConfig();
        if (map2 != null && oConvertUtils.isNotEmpty((Object)(string2 = map2.get("treeText")))) {
            map.put(string2, string);
        }
    }
}

