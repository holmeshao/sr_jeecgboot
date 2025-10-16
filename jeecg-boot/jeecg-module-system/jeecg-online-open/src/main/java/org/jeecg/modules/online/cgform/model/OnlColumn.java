/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.cgform.model;

import org.jeecg.modules.online.cgform.model.g;

public class OnlColumn {
    private String title;
    private String dataIndex;
    private String align;
    private String fieldExtendJson;
    private String customRender;
    private g scopedSlots;
    private String hrefSlotName;
    private int showLength;
    private boolean sorter = false;
    private String linkField;
    private String tableName;
    private String dbType;
    private String fieldType;

    public OnlColumn() {
    }

    public OnlColumn(String title, String dataIndex) {
        this.align = "center";
        this.title = title;
        this.dataIndex = dataIndex;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDataIndex() {
        return this.dataIndex;
    }

    public String getAlign() {
        return this.align;
    }

    public String getFieldExtendJson() {
        return this.fieldExtendJson;
    }

    public String getCustomRender() {
        return this.customRender;
    }

    public g getScopedSlots() {
        return this.scopedSlots;
    }

    public String getHrefSlotName() {
        return this.hrefSlotName;
    }

    public int getShowLength() {
        return this.showLength;
    }

    public boolean isSorter() {
        return this.sorter;
    }

    public String getLinkField() {
        return this.linkField;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getDbType() {
        return this.dbType;
    }

    public String getFieldType() {
        return this.fieldType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public void setFieldExtendJson(String fieldExtendJson) {
        this.fieldExtendJson = fieldExtendJson;
    }

    public void setCustomRender(String customRender) {
        this.customRender = customRender;
    }

    public void setScopedSlots(g scopedSlots) {
        this.scopedSlots = scopedSlots;
    }

    public void setHrefSlotName(String hrefSlotName) {
        this.hrefSlotName = hrefSlotName;
    }

    public void setShowLength(int showLength) {
        this.showLength = showLength;
    }

    public void setSorter(boolean sorter) {
        this.sorter = sorter;
    }

    public void setLinkField(String linkField) {
        this.linkField = linkField;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnlColumn)) {
            return false;
        }
        OnlColumn onlColumn = (OnlColumn)o;
        if (!onlColumn.canEqual(this)) {
            return false;
        }
        if (this.getShowLength() != onlColumn.getShowLength()) {
            return false;
        }
        if (this.isSorter() != onlColumn.isSorter()) {
            return false;
        }
        String string = this.getTitle();
        String string2 = onlColumn.getTitle();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getDataIndex();
        String string4 = onlColumn.getDataIndex();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getAlign();
        String string6 = onlColumn.getAlign();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getFieldExtendJson();
        String string8 = onlColumn.getFieldExtendJson();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getCustomRender();
        String string10 = onlColumn.getCustomRender();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        g g2 = this.getScopedSlots();
        g g3 = onlColumn.getScopedSlots();
        if (g2 == null ? g3 != null : !((Object)g2).equals(g3)) {
            return false;
        }
        String string11 = this.getHrefSlotName();
        String string12 = onlColumn.getHrefSlotName();
        if (string11 == null ? string12 != null : !string11.equals(string12)) {
            return false;
        }
        String string13 = this.getLinkField();
        String string14 = onlColumn.getLinkField();
        if (string13 == null ? string14 != null : !string13.equals(string14)) {
            return false;
        }
        String string15 = this.getTableName();
        String string16 = onlColumn.getTableName();
        if (string15 == null ? string16 != null : !string15.equals(string16)) {
            return false;
        }
        String string17 = this.getDbType();
        String string18 = onlColumn.getDbType();
        if (string17 == null ? string18 != null : !string17.equals(string18)) {
            return false;
        }
        String string19 = this.getFieldType();
        String string20 = onlColumn.getFieldType();
        return !(string19 == null ? string20 != null : !string19.equals(string20));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OnlColumn;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        n2 = n2 * 59 + this.getShowLength();
        n2 = n2 * 59 + (this.isSorter() ? 79 : 97);
        String string = this.getTitle();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getDataIndex();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getAlign();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getFieldExtendJson();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getCustomRender();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        g g2 = this.getScopedSlots();
        n2 = n2 * 59 + (g2 == null ? 43 : ((Object)g2).hashCode());
        String string6 = this.getHrefSlotName();
        n2 = n2 * 59 + (string6 == null ? 43 : string6.hashCode());
        String string7 = this.getLinkField();
        n2 = n2 * 59 + (string7 == null ? 43 : string7.hashCode());
        String string8 = this.getTableName();
        n2 = n2 * 59 + (string8 == null ? 43 : string8.hashCode());
        String string9 = this.getDbType();
        n2 = n2 * 59 + (string9 == null ? 43 : string9.hashCode());
        String string10 = this.getFieldType();
        n2 = n2 * 59 + (string10 == null ? 43 : string10.hashCode());
        return n2;
    }

    public String toString() {
        return "OnlColumn(title=" + this.getTitle() + ", dataIndex=" + this.getDataIndex() + ", align=" + this.getAlign() + ", fieldExtendJson=" + this.getFieldExtendJson() + ", customRender=" + this.getCustomRender() + ", scopedSlots=" + this.getScopedSlots() + ", hrefSlotName=" + this.getHrefSlotName() + ", showLength=" + this.getShowLength() + ", sorter=" + this.isSorter() + ", linkField=" + this.getLinkField() + ", tableName=" + this.getTableName() + ", dbType=" + this.getDbType() + ", fieldType=" + this.getFieldType() + ")";
    }
}

