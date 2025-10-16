/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  org.jeecg.common.exception.JeecgBootException
 */
package org.jeecg.modules.online.cgform.d;

import com.alibaba.fastjson.JSONObject;
import java.math.BigDecimal;
import java.util.Map;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;

public class i {
    public static final String a = "int";
    public static final String b = "Integer";
    public static final String c = "string";
    public static final String d = "double";
    public static final String e = "BigDecimal";
    public static final String f = "Blob";
    public static final String g = "Date";
    public static final String h = "datetime";
    public static final String i = "Timestamp";
    public static final String j = "Long";

    public static boolean a(String string) {
        return a.equals(string) || d.equals(string) || e.equals(string) || b.equals(string) || j.equals(string);
    }

    public static boolean b(String string) {
        return g.equalsIgnoreCase(string) || h.equalsIgnoreCase(string) || i.equalsIgnoreCase(string);
    }

    public static String a(String string, OnlCgformField onlCgformField, JSONObject jSONObject, Map<String, Object> map) {
        String string2 = onlCgformField.getDbType();
        String string3 = onlCgformField.getDbFieldName();
        String string4 = onlCgformField.getFieldShowType();
        if (jSONObject.get((Object)string3) == null) {
            return "null";
        }
        org.jeecg.modules.online.cgform.d.i.a(onlCgformField, jSONObject);
        if (a.equals(string2)) {
            String string5 = jSONObject.getString(string3);
            double d2 = Double.parseDouble(string5);
            map.put(string3, Math.floor(d2));
            return "#{" + string3 + ",jdbcType=INTEGER}";
        }
        if (d.equals(string2)) {
            map.put(string3, jSONObject.getDoubleValue(string3));
            return "#{" + string3 + ",jdbcType=DOUBLE}";
        }
        if (e.equals(string2)) {
            map.put(string3, new BigDecimal(jSONObject.getString(string3)));
            return "#{" + string3 + ",jdbcType=DECIMAL}";
        }
        if (f.equals(string2)) {
            map.put(string3, jSONObject.getString(string3) != null ? jSONObject.getString(string3).getBytes() : null);
            return "#{" + string3 + ",jdbcType=BLOB}";
        }
        if (g.equals(string2) || h.equalsIgnoreCase(string2)) {
            String string6 = jSONObject.getString(string3);
            if ("ORACLE".equals(string)) {
                if ("date".equals(string4)) {
                    map.put(string3, string6.length() > 10 ? string6.substring(0, 10) : string6);
                    return "to_date(#{" + string3 + "},'yyyy-MM-dd')";
                }
                map.put(string3, string6.length() == 10 ? jSONObject.getString(string3) + " 00:00:00" : string6);
                return "to_date(#{" + string3 + "},'yyyy-MM-dd HH24:mi:ss')";
            }
            if ("POSTGRESQL".equals(string)) {
                if ("date".equals(string4)) {
                    map.put(string3, string6.length() > 10 ? string6.substring(0, 10) : string6);
                    return "CAST(#{" + string3 + "} as DATE)";
                }
                map.put(string3, string6.length() == 10 ? jSONObject.getString(string3) + " 00:00:00" : string6);
                return "CAST(#{" + string3 + "} as TIMESTAMP)";
            }
            map.put(string3, jSONObject.getString(string3));
            return "#{" + string3 + "}";
        }
        map.put(string3, jSONObject.getString(string3));
        return "#{" + string3 + ",jdbcType=VARCHAR}";
    }

    private static void a(OnlCgformField onlCgformField, JSONObject jSONObject) {
        String string = onlCgformField.getDbType().toLowerCase();
        String string2 = onlCgformField.getDbFieldName();
        String string3 = onlCgformField.getDbFieldTxt();
        Integer n = onlCgformField.getDbLength();
        Integer n2 = onlCgformField.getDbPointLength();
        if (jSONObject.get((Object)string2) == null) {
            return;
        }
        boolean bl = false;
        if (string.equalsIgnoreCase(d) || string.equalsIgnoreCase(e)) {
            String[] stringArray = jSONObject.getBigDecimal(string2).toPlainString().split("\\.");
            int n3 = stringArray[0].length();
            int n4 = stringArray.length > 1 ? stringArray[1].length() : 0;
            bl = n3 > n - n2 || n4 > n2;
        } else if (string.equalsIgnoreCase(c)) {
            bl = String.valueOf(jSONObject.get((Object)string2)).length() > n;
        } else if (string.equalsIgnoreCase(b) || string.equalsIgnoreCase(a)) {
            boolean bl2 = bl = String.valueOf(jSONObject.get((Object)string2)).length() > n;
        }
        if (bl) {
            throw new JeecgBootException(String.format("%s\u7684\u957f\u5ea6\u8d85\u51fa\u6700\u5927\u957f\u5ea6\u3002", string3));
        }
    }
}

