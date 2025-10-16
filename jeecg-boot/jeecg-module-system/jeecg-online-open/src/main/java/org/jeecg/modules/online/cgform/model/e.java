/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.cgform.model;

import java.util.List;
import java.util.Map;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;

public class e {
    private String a;
    private Map<String, Object> b;
    private Map<String, String> c;
    private List<OnlCgformField> d;

    public e() {
    }

    public e(String string, Map<String, Object> map) {
        this.a = string;
        this.b = map;
    }

    public String getSql() {
        return this.a;
    }

    public Map<String, Object> getParams() {
        return this.b;
    }

    public Map<String, String> getTableAliasMap() {
        return this.c;
    }

    public List<OnlCgformField> getFieldList() {
        return this.d;
    }

    public void setSql(String sql) {
        this.a = sql;
    }

    public void setParams(Map<String, Object> params) {
        this.b = params;
    }

    public void setTableAliasMap(Map<String, String> tableAliasMap) {
        this.c = tableAliasMap;
    }

    public void setFieldList(List<OnlCgformField> fieldList) {
        this.d = fieldList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof e)) {
            return false;
        }
        e e2 = (e)o;
        if (!e2.a(this)) {
            return false;
        }
        String string = this.getSql();
        String string2 = e2.getSql();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        Map<String, Object> map = this.getParams();
        Map<String, Object> map2 = e2.getParams();
        if (map == null ? map2 != null : !((Object)map).equals(map2)) {
            return false;
        }
        Map<String, String> map3 = this.getTableAliasMap();
        Map<String, String> map4 = e2.getTableAliasMap();
        if (map3 == null ? map4 != null : !((Object)map3).equals(map4)) {
            return false;
        }
        List<OnlCgformField> list = this.getFieldList();
        List<OnlCgformField> list2 = e2.getFieldList();
        return !(list == null ? list2 != null : !((Object)list).equals(list2));
    }

    protected boolean a(Object object) {
        return object instanceof e;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        String string = this.getSql();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        Map<String, Object> map = this.getParams();
        n2 = n2 * 59 + (map == null ? 43 : ((Object)map).hashCode());
        Map<String, String> map2 = this.getTableAliasMap();
        n2 = n2 * 59 + (map2 == null ? 43 : ((Object)map2).hashCode());
        List<OnlCgformField> list = this.getFieldList();
        n2 = n2 * 59 + (list == null ? 43 : ((Object)list).hashCode());
        return n2;
    }

    public String toString() {
        return "OnlQueryModel(sql=" + this.getSql() + ", params=" + this.getParams() + ", tableAliasMap=" + this.getTableAliasMap() + ", fieldList=" + this.getFieldList() + ")";
    }
}

