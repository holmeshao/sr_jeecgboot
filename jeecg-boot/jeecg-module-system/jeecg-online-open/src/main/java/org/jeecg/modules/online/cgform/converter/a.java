/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jeecg.common.util.MyClassLoader
 *  org.jeecg.common.util.SpringContextUtils
 *  org.jeecg.common.util.oConvertUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.jeecg.modules.online.cgform.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jeecg.common.util.MyClassLoader;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.converter.FieldCommentConverter;
import org.jeecg.modules.online.cgform.converter.b.b;
import org.jeecg.modules.online.cgform.converter.b.c;
import org.jeecg.modules.online.cgform.converter.b.d;
import org.jeecg.modules.online.cgform.converter.b.e;
import org.jeecg.modules.online.cgform.converter.b.f;
import org.jeecg.modules.online.cgform.converter.b.h;
import org.jeecg.modules.online.cgform.converter.b.i;
import org.jeecg.modules.online.cgform.converter.b.j;
import org.jeecg.modules.online.cgform.converter.b.k;
import org.jeecg.modules.online.cgform.converter.b.l;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class a {
    private static final Logger a = LoggerFactory.getLogger(a.class);
    private static final String b = "list";
    private static final String c = "radio";
    private static final String d = "checkbox";
    private static final String e = "list_multi";
    private static final String f = "sel_search";
    private static final String g = "sel_tree";
    private static final String h = "cat_tree";
    private static final String i = "link_down";
    private static final String j = "sel_depart";
    private static final String k = "sel_user";
    private static final String l = "pca";
    private static final String m = "switch";
    private static final String n = "input";
    private static final String o = "date";

    public static FieldCommentConverter a(OnlCgformField onlCgformField) {
        String string = onlCgformField.getFieldShowType();
        FieldCommentConverter fieldCommentConverter = null;
        switch (string) {
            case "list": 
            case "radio": {
                fieldCommentConverter = new d(onlCgformField);
                break;
            }
            case "list_multi": 
            case "checkbox": {
                fieldCommentConverter = new h(onlCgformField);
                break;
            }
            case "sel_search": {
                fieldCommentConverter = new e(onlCgformField);
                break;
            }
            case "sel_tree": {
                fieldCommentConverter = new k(onlCgformField);
                break;
            }
            case "cat_tree": {
                fieldCommentConverter = new org.jeecg.modules.online.cgform.converter.b.a(onlCgformField);
                break;
            }
            case "link_down": {
                fieldCommentConverter = new f(onlCgformField);
                break;
            }
            case "sel_depart": {
                fieldCommentConverter = new c(onlCgformField);
                break;
            }
            case "sel_user": {
                fieldCommentConverter = new l(onlCgformField);
                break;
            }
            case "pca": {
                fieldCommentConverter = new i(onlCgformField);
                break;
            }
            case "switch": {
                fieldCommentConverter = new j(onlCgformField);
                break;
            }
            case "input": {
                String string2 = onlCgformField.getDictField();
                if (string2 == null || "".equals(string2)) {
                    fieldCommentConverter = null;
                    break;
                }
                fieldCommentConverter = new d(onlCgformField);
                break;
            }
            case "date": {
                fieldCommentConverter = new b(onlCgformField);
                break;
            }
            default: {
                fieldCommentConverter = null;
            }
        }
        return fieldCommentConverter;
    }

    public static Map<String, FieldCommentConverter> a(List<OnlCgformField> list) {
        HashMap<String, FieldCommentConverter> hashMap = new HashMap<String, FieldCommentConverter>(5);
        for (OnlCgformField onlCgformField : list) {
            FieldCommentConverter fieldCommentConverter = null;
            fieldCommentConverter = oConvertUtils.isNotEmpty((Object)onlCgformField.getConverter()) ? org.jeecg.modules.online.cgform.converter.a.a(onlCgformField.getConverter().trim()) : org.jeecg.modules.online.cgform.converter.a.a(onlCgformField);
            if (fieldCommentConverter == null) continue;
            hashMap.put(onlCgformField.getDbFieldName().toLowerCase(), fieldCommentConverter);
        }
        return hashMap;
    }

    private static FieldCommentConverter a(String string) {
        Object object = null;
        if (string.indexOf(".") > 0) {
            try {
                object = MyClassLoader.getClassByScn((String)string).newInstance();
            }
            catch (InstantiationException instantiationException) {
                a.error(instantiationException.getMessage(), (Throwable)instantiationException);
            }
            catch (IllegalAccessException illegalAccessException) {
                a.error(illegalAccessException.getMessage(), (Throwable)illegalAccessException);
            }
        } else {
            object = SpringContextUtils.getBean((String)string);
        }
        if (object != null && object instanceof FieldCommentConverter) {
            FieldCommentConverter fieldCommentConverter = (FieldCommentConverter)object;
            return fieldCommentConverter;
        }
        return null;
    }
}

