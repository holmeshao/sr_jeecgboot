/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jeecg.common.exception.JeecgBootException
 *  org.jeecg.common.util.oConvertUtils
 */
package org.jeecg.modules.online.b;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportParam;

public class b<T> {
    private final String a = "url";
    private final Map<String, Object> b;
    private final List<T> c;
    private final Map<String, Object> d = new HashMap<String, Object>();
    private final Map<String, Object> e = new HashMap<String, Object>();

    public b(Map<String, Object> map, List<T> list) {
        this.b = map;
        this.c = list;
    }

    public String a(String string) {
        return this.a(string, "mybatis");
    }

    public String b(String string) {
        return this.a(string, "jdbcTemplate");
    }

    public String c(String string) {
        return this.a(string, this.a);
    }

    private String a(String string, String string2) {
        if (this.c == null || this.c.isEmpty()) {
            return string;
        }
        for (T t : this.c) {
            String string3 = this.a(t);
            String string4 = this.b(t);
            Object object = this.b.get("self_" + string3);
            Object object2 = this.b.get(string3);
            String string5 = "";
            if (oConvertUtils.isNotEmpty((Object)object)) {
                string5 = object.toString();
            } else if (oConvertUtils.isNotEmpty((Object)object2)) {
                string5 = object2.toString();
            } else if (oConvertUtils.isNotEmpty((Object)string4)) {
                string5 = string4;
            }
            String string6 = "${" + string3 + "}";
            int n = string.indexOf(string6);
            if (n > 0) {
                if (string5.startsWith("'") && string5.endsWith("'")) {
                    string5 = string5.substring(1, string5.length() - 1);
                }
                if (this.a.equals(string2)) {
                    string = string.replace(string6, string5);
                    continue;
                }
                String string7 = "_sql_param_" + string3;
                String string8 = "jdbcTemplate".equals(string2) ? ":" + string7 : "#{param." + string7 + "}";
                String string9 = "'([^']*)\\$\\{" + string3 + "}([^']*)'";
                Pattern pattern = Pattern.compile(string9);
                Matcher matcher = pattern.matcher(string);
                if (matcher.find()) {
                    string5 = matcher.group(1) + string5 + matcher.group(2);
                    string = string.replace(matcher.group(0), string8);
                } else {
                    String string10 = "'?\\$\\{" + string3 + "}'?";
                    string = string.replaceAll(string10, string8);
                }
                this.d.put(string7, string5);
                continue;
            }
            if (!oConvertUtils.isNotEmpty((Object)string5)) continue;
            if (t instanceof OnlCgreportParam) {
                this.d.put(string3, object);
                this.b.put("popup_param_pre__" + string3, string5);
                continue;
            }
            if (!(t instanceof OnlCgreportParam)) continue;
            if (this.a.equals(string2)) {
                string = string.replace(string6, "");
                continue;
            }
            this.e.put(string3, string5);
        }
        return string;
    }

    private String a(T t) {
        if (t instanceof OnlCgreportParam) {
            return ((OnlCgreportParam)t).getParamName();
        }
        if (t instanceof OnlCgreportParam) {
            return ((OnlCgreportParam)t).getParamName();
        }
        throw new JeecgBootException("\u4e0d\u652f\u6301\u7684\u7c7b\u578b\uff1a" + t.getClass().getName());
    }

    private String b(T t) {
        if (t instanceof OnlCgreportParam) {
            return ((OnlCgreportParam)t).getParamValue();
        }
        if (t instanceof OnlCgreportParam) {
            return ((OnlCgreportParam)t).getParamValue();
        }
        throw new JeecgBootException("\u4e0d\u652f\u6301\u7684\u7c7b\u578b\uff1a" + t.getClass().getName());
    }

    public Map<String, Object> getSelfSqlParams() {
        return this.d;
    }

    public Map<String, Object> getOtherParams() {
        return this.e;
    }
}

