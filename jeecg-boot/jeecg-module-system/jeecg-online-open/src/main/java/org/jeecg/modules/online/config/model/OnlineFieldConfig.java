/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  org.jeecg.common.util.oConvertUtils
 */
package org.jeecg.modules.online.config.model;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.d.i;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportItem;
import org.jeecg.modules.online.cgreport.model.ParamItemVo;

public class OnlineFieldConfig {
    private String name;
    private String type;
    private String view;
    private String mode;
    private String val;
    private String rule;
    private Integer isSearch;
    private String mainField;
    private String mainTable;
    private String table;

    public OnlineFieldConfig() {
    }

    public OnlineFieldConfig(JSONObject parameter) {
        String string = parameter.getString("field");
        String[] stringArray = string.split(",");
        if (stringArray.length == 1) {
            this.name = string;
        } else if (stringArray.length == 2) {
            this.name = stringArray[1];
            this.table = stringArray[0];
        }
        String string2 = parameter.getString("type");
        String string3 = parameter.getString("dbType");
        this.type = oConvertUtils.isNotEmpty((Object)string3) && i.a(string3) ? string3 : string2;
        if ("list_multi".equals(string2) || "popup".equals(string2)) {
            this.view = string2;
        }
        this.rule = parameter.getString("rule");
        this.val = parameter.getString("val");
        this.mode = "single";
        this.isSearch = 1;
    }

    public OnlineFieldConfig(OnlCgreportItem item) {
        this.name = item.getFieldName();
        this.type = item.getFieldType();
        this.mode = item.getSearchMode();
        this.isSearch = item.getIsSearch();
    }

    public OnlineFieldConfig(ParamItemVo item) {
        this.name = item.getFieldName();
        this.type = item.getFieldType();
        this.mode = item.getSearchMode();
        this.isSearch = 1;
    }

    public OnlineFieldConfig(OnlCgformField item) {
        this.name = item.getDbFieldName();
        this.type = item.getDbType();
        this.mode = item.getQueryMode();
        this.isSearch = item.getIsQuery();
        this.mainField = item.getMainField();
        this.mainTable = item.getMainTable();
        String string = item.getQueryConfigFlag();
        this.view = "1".equals(string) ? item.getQueryShowType() : item.getFieldShowType();
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getView() {
        return this.view;
    }

    public String getMode() {
        return this.mode;
    }

    public String getVal() {
        return this.val;
    }

    public String getRule() {
        return this.rule;
    }

    public Integer getIsSearch() {
        return this.isSearch;
    }

    public String getMainField() {
        return this.mainField;
    }

    public String getMainTable() {
        return this.mainTable;
    }

    public String getTable() {
        return this.table;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setView(String view) {
        this.view = view;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public void setIsSearch(Integer isSearch) {
        this.isSearch = isSearch;
    }

    public void setMainField(String mainField) {
        this.mainField = mainField;
    }

    public void setMainTable(String mainTable) {
        this.mainTable = mainTable;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnlineFieldConfig)) {
            return false;
        }
        OnlineFieldConfig onlineFieldConfig = (OnlineFieldConfig)o;
        if (!onlineFieldConfig.canEqual(this)) {
            return false;
        }
        Integer n = this.getIsSearch();
        Integer n2 = onlineFieldConfig.getIsSearch();
        if (n == null ? n2 != null : !((Object)n).equals(n2)) {
            return false;
        }
        String string = this.getName();
        String string2 = onlineFieldConfig.getName();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getType();
        String string4 = onlineFieldConfig.getType();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getView();
        String string6 = onlineFieldConfig.getView();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getMode();
        String string8 = onlineFieldConfig.getMode();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getVal();
        String string10 = onlineFieldConfig.getVal();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        String string11 = this.getRule();
        String string12 = onlineFieldConfig.getRule();
        if (string11 == null ? string12 != null : !string11.equals(string12)) {
            return false;
        }
        String string13 = this.getMainField();
        String string14 = onlineFieldConfig.getMainField();
        if (string13 == null ? string14 != null : !string13.equals(string14)) {
            return false;
        }
        String string15 = this.getMainTable();
        String string16 = onlineFieldConfig.getMainTable();
        if (string15 == null ? string16 != null : !string15.equals(string16)) {
            return false;
        }
        String string17 = this.getTable();
        String string18 = onlineFieldConfig.getTable();
        return !(string17 == null ? string18 != null : !string17.equals(string18));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OnlineFieldConfig;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        Integer n3 = this.getIsSearch();
        n2 = n2 * 59 + (n3 == null ? 43 : ((Object)n3).hashCode());
        String string = this.getName();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getType();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getView();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getMode();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getVal();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        String string6 = this.getRule();
        n2 = n2 * 59 + (string6 == null ? 43 : string6.hashCode());
        String string7 = this.getMainField();
        n2 = n2 * 59 + (string7 == null ? 43 : string7.hashCode());
        String string8 = this.getMainTable();
        n2 = n2 * 59 + (string8 == null ? 43 : string8.hashCode());
        String string9 = this.getTable();
        n2 = n2 * 59 + (string9 == null ? 43 : string9.hashCode());
        return n2;
    }

    public String toString() {
        return "OnlineFieldConfig(name=" + this.getName() + ", type=" + this.getType() + ", view=" + this.getView() + ", mode=" + this.getMode() + ", val=" + this.getVal() + ", rule=" + this.getRule() + ", isSearch=" + this.getIsSearch() + ", mainField=" + this.getMainField() + ", mainTable=" + this.getMainTable() + ", table=" + this.getTable() + ")";
    }
}

