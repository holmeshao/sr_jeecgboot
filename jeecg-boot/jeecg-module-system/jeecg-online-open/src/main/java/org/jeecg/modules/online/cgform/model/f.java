/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jeecg.common.system.vo.SysPermissionDataRuleModel
 */
package org.jeecg.modules.online.cgform.model;

import java.util.List;
import org.jeecg.common.system.vo.SysPermissionDataRuleModel;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;

public class f {
    private String a;
    private String b;
    private List<OnlCgformField> c;
    private List<OnlCgformField> d;
    private List<SysPermissionDataRuleModel> e;
    private String f;
    private String g;
    private String h;
    private boolean i;

    public void setAliasByIntValue(int index) {
        char c2 = (char)index;
        this.h = String.valueOf(c2);
    }

    public String getTableAlias() {
        return this.h + ".";
    }

    public f() {
    }

    public f(String string, String string2, boolean bl) {
        this.a = string;
        this.b = string2;
        this.i = bl;
    }

    public String getTableName() {
        return this.a;
    }

    public String getTableId() {
        return this.b;
    }

    public List<OnlCgformField> getAllFieldList() {
        return this.c;
    }

    public List<OnlCgformField> getSelectFieldList() {
        return this.d;
    }

    public List<SysPermissionDataRuleModel> getAuthList() {
        return this.e;
    }

    public String getMainField() {
        return this.f;
    }

    public String getJoinField() {
        return this.g;
    }

    public String getAlias() {
        return this.h;
    }

    public boolean a() {
        return this.i;
    }

    public void setTableName(String tableName) {
        this.a = tableName;
    }

    public void setTableId(String tableId) {
        this.b = tableId;
    }

    public void setAllFieldList(List<OnlCgformField> allFieldList) {
        this.c = allFieldList;
    }

    public void setSelectFieldList(List<OnlCgformField> selectFieldList) {
        this.d = selectFieldList;
    }

    public void setAuthList(List<SysPermissionDataRuleModel> authList) {
        this.e = authList;
    }

    public void setMainField(String mainField) {
        this.f = mainField;
    }

    public void setJoinField(String joinField) {
        this.g = joinField;
    }

    public void setAlias(String alias) {
        this.h = alias;
    }

    public void setMain(boolean isMain) {
        this.i = isMain;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof f)) {
            return false;
        }
        f f2 = (f)o;
        if (!f2.a(this)) {
            return false;
        }
        if (this.a() != f2.a()) {
            return false;
        }
        String string = this.getTableName();
        String string2 = f2.getTableName();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getTableId();
        String string4 = f2.getTableId();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        List<OnlCgformField> list = this.getAllFieldList();
        List<OnlCgformField> list2 = f2.getAllFieldList();
        if (list == null ? list2 != null : !((Object)list).equals(list2)) {
            return false;
        }
        List<OnlCgformField> list3 = this.getSelectFieldList();
        List<OnlCgformField> list4 = f2.getSelectFieldList();
        if (list3 == null ? list4 != null : !((Object)list3).equals(list4)) {
            return false;
        }
        List<SysPermissionDataRuleModel> list5 = this.getAuthList();
        List<SysPermissionDataRuleModel> list6 = f2.getAuthList();
        if (list5 == null ? list6 != null : !((Object)list5).equals(list6)) {
            return false;
        }
        String string5 = this.getMainField();
        String string6 = f2.getMainField();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getJoinField();
        String string8 = f2.getJoinField();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getAlias();
        String string10 = f2.getAlias();
        return !(string9 == null ? string10 != null : !string9.equals(string10));
    }

    protected boolean a(Object object) {
        return object instanceof f;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        n2 = n2 * 59 + (this.a() ? 79 : 97);
        String string = this.getTableName();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getTableId();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        List<OnlCgformField> list = this.getAllFieldList();
        n2 = n2 * 59 + (list == null ? 43 : ((Object)list).hashCode());
        List<OnlCgformField> list2 = this.getSelectFieldList();
        n2 = n2 * 59 + (list2 == null ? 43 : ((Object)list2).hashCode());
        List<SysPermissionDataRuleModel> list3 = this.getAuthList();
        n2 = n2 * 59 + (list3 == null ? 43 : ((Object)list3).hashCode());
        String string3 = this.getMainField();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getJoinField();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getAlias();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        return n2;
    }

    public String toString() {
        return "OnlTable(tableName=" + this.getTableName() + ", tableId=" + this.getTableId() + ", allFieldList=" + this.getAllFieldList() + ", selectFieldList=" + this.getSelectFieldList() + ", authList=" + this.getAuthList() + ", mainField=" + this.getMainField() + ", joinField=" + this.getJoinField() + ", alias=" + this.getAlias() + ", isMain=" + this.a() + ")";
    }
}

