/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.cgform.model;

public class TreeModel {
    private String label;
    private String store;
    private String id;
    private String pid;

    public String getLabel() {
        return this.label;
    }

    public String getStore() {
        return this.store;
    }

    public String getId() {
        return this.id;
    }

    public String getPid() {
        return this.pid;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TreeModel)) {
            return false;
        }
        TreeModel treeModel = (TreeModel)o;
        if (!treeModel.canEqual(this)) {
            return false;
        }
        String string = this.getLabel();
        String string2 = treeModel.getLabel();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getStore();
        String string4 = treeModel.getStore();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getId();
        String string6 = treeModel.getId();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getPid();
        String string8 = treeModel.getPid();
        return !(string7 == null ? string8 != null : !string7.equals(string8));
    }

    protected boolean canEqual(Object other) {
        return other instanceof TreeModel;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        String string = this.getLabel();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getStore();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getId();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getPid();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        return n2;
    }

    public String toString() {
        return "TreeModel(label=" + this.getLabel() + ", store=" + this.getStore() + ", id=" + this.getId() + ", pid=" + this.getPid() + ")";
    }
}

