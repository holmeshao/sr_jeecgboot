/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONObject
 *  org.jeecg.common.util.oConvertUtils
 */
package org.jeecg.modules.online.cgform.d;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.enums.CgformValidPatternEnum;

public class j {
    private Map<String, OnlCgformField> d;
    private Map<String, OnlCgformField> e;
    private static final String f = ",";
    private static final String g = "\u7b2c%s\u884c\u6821\u9a8c\u4fe1\u606f:";
    private static final String h = "\u603b\u4e0a\u4f20\u884c\u6570:%s,\u5df2\u5bfc\u5165\u884c\u6570:%s,\u9519\u8bef\u884c\u6570:%s";
    public static final String a = "error";
    public static final String b = "tip";
    public static final String c = "filePath";

    public j() {
    }

    public j(List<OnlCgformField> list) {
        this.d = new HashMap<String, OnlCgformField>(5);
        this.e = new HashMap<String, OnlCgformField>(5);
        for (OnlCgformField onlCgformField : list) {
            String string = onlCgformField.getFieldValidType();
            if (string != null && !"".equals(string) && !CgformValidPatternEnum.ONLY.getType().equals(string)) {
                if (CgformValidPatternEnum.NOTNULL.getType().equals(string)) {
                    this.e.put(onlCgformField.getDbFieldName(), onlCgformField);
                } else {
                    this.d.put(onlCgformField.getDbFieldName(), onlCgformField);
                }
            }
            if (onlCgformField.getDbIsNull() != 0 && !"1".equals(onlCgformField.getFieldMustInput()) || !oConvertUtils.isEmpty((Object)onlCgformField.getDbDefaultVal())) continue;
            this.e.put(onlCgformField.getDbFieldName(), onlCgformField);
        }
    }

    public String a(String string, int n) {
        OnlCgformField onlCgformField;
        String string2;
        StringBuffer stringBuffer = new StringBuffer();
        JSONObject jSONObject = JSON.parseObject((String)string);
        for (String string3 : this.e.keySet()) {
            string2 = jSONObject.getString(string3);
            onlCgformField = this.e.get(string3);
            if (string2 != null && !"".equals(string2)) continue;
            stringBuffer.append(onlCgformField.getDbFieldTxt() + CgformValidPatternEnum.NOTNULL.getMsg() + f);
        }
        for (String string3 : this.d.keySet()) {
            Matcher matcher;
            Object object;
            string2 = jSONObject.getString(string3);
            onlCgformField = this.d.get(string3);
            String string4 = onlCgformField.getFieldValidType();
            if (string2 == null || "".equals(string2)) continue;
            String string5 = null;
            String string6 = null;
            if (CgformValidPatternEnum.INTEGER.getType().equals(string4)) {
                string5 = "^-?[1-9]\\d*$";
                string6 = "\u8bf7\u8f93\u5165\u6574\u6570";
            } else {
                object = CgformValidPatternEnum.getPatternInfoByType(string4);
                if (object == null) {
                    string5 = string4;
                    string6 = "\u6821\u9a8c\u3010" + string5 + "\u3011\u672a\u901a\u8fc7";
                } else {
                    string5 = object.getPattern();
                    string6 = object.getMsg();
                }
            }
            if ((matcher = ((Pattern)(object = Pattern.compile(string5))).matcher(string2)).find()) continue;
            stringBuffer.append(onlCgformField.getDbFieldTxt() + string6 + f);
        }
        if (stringBuffer.length() > 0) {
            return j.b(stringBuffer.toString(), n);
        }
        return null;
    }

    public static String b(String string, int n) {
        return String.format(g, n) + string + "\r\n";
    }

    public static String a(int n, int n2) {
        int n3 = n - n2;
        return String.format(h, n, n3, n2);
    }
}

