/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONArray
 */
package org.jeecg.modules.online.cgreport.model;

import com.alibaba.fastjson.JSONArray;
import java.util.Map;

public class ParamItemVo {
    private String fieldTxt;
    private String fieldName;
    private String fieldType;
    private String searchMode;
    private Object value;

    public void putMap(Map<String, Object> map) {
        if (this.value == null) {
            return;
        }
        map.put(this.fieldName, this.value);
        boolean bl = "group".equals(this.searchMode);
        if (bl) {
            JSONArray jSONArray = (JSONArray)this.value;
            map.put(this.fieldName + "_begin", jSONArray.get(0));
            map.put(this.fieldName + "_end", jSONArray.get(1));
        }
    }

    public String getFieldTxt() {
        return this.fieldTxt;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getFieldType() {
        return this.fieldType;
    }

    public String getSearchMode() {
        return this.searchMode;
    }

    public Object getValue() {
        return this.value;
    }

    public void setFieldTxt(String fieldTxt) {
        this.fieldTxt = fieldTxt;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public void setSearchMode(String searchMode) {
        this.searchMode = searchMode;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ParamItemVo)) {
            return false;
        }
        ParamItemVo paramItemVo = (ParamItemVo)o;
        if (!paramItemVo.canEqual(this)) {
            return false;
        }
        String string = this.getFieldTxt();
        String string2 = paramItemVo.getFieldTxt();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getFieldName();
        String string4 = paramItemVo.getFieldName();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getFieldType();
        String string6 = paramItemVo.getFieldType();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getSearchMode();
        String string8 = paramItemVo.getSearchMode();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        Object object = this.getValue();
        Object object2 = paramItemVo.getValue();
        return !(object == null ? object2 != null : !object.equals(object2));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ParamItemVo;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        String string = this.getFieldTxt();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getFieldName();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getFieldType();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getSearchMode();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        Object object = this.getValue();
        n2 = n2 * 59 + (object == null ? 43 : object.hashCode());
        return n2;
    }

    public String toString() {
        return "ParamItemVo(fieldTxt=" + this.getFieldTxt() + ", fieldName=" + this.getFieldName() + ", fieldType=" + this.getFieldType() + ", searchMode=" + this.getSearchMode() + ", value=" + this.getValue() + ")";
    }
}

