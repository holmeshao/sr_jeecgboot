/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jeecg.common.util.oConvertUtils
 */
package org.jeecg.modules.online.cgform.model;

import org.jeecg.common.util.oConvertUtils;

public class h {
    public static final String a = "asc";
    public static final String b = "desc";
    public static final String c = " ORDER BY ";
    public static final String d = "ID";
    private String e;
    private String f;
    private String g;

    public static h a() {
        return h.a("");
    }

    public static h a(String string) {
        h h2 = new h(d);
        h2.setAlias(string);
        return h2;
    }

    public String getRealSql() {
        String string = this.g + oConvertUtils.camelToUnderline((String)this.e);
        string = a.equals(this.f) ? string + " asc" : string + " desc";
        return string;
    }

    public h() {
    }

    public h(String string, String string2) {
        this.e = string;
        this.f = string2;
        this.g = "";
    }

    public h(String string) {
        this.f = b;
        this.e = string;
        this.g = "";
    }

    public String getColumn() {
        return this.e;
    }

    public void setColumn(String column) {
        this.e = column;
    }

    public String getRule() {
        return this.f;
    }

    public void setRule(String rule) {
        this.f = rule;
    }

    public String getAlias() {
        return this.g;
    }

    public void setAlias(String alias) {
        this.g = alias;
    }
}

