/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.cgform.a;

public class a {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;
    private String h;

    private String getQuerySql() {
        StringBuffer stringBuffer = new StringBuffer();
        String string = " ";
        stringBuffer.append("SELECT ");
        return null;
    }

    public String getTable() {
        return this.a;
    }

    public String getTxt() {
        return this.b;
    }

    public String getKey() {
        return this.c;
    }

    public String getLinkField() {
        return this.d;
    }

    public String getIdField() {
        return this.e;
    }

    public String getPidField() {
        return this.f;
    }

    public String getPidValue() {
        return this.g;
    }

    public String getCondition() {
        return this.h;
    }

    public void setTable(String table) {
        this.a = table;
    }

    public void setTxt(String txt) {
        this.b = txt;
    }

    public void setKey(String key) {
        this.c = key;
    }

    public void setLinkField(String linkField) {
        this.d = linkField;
    }

    public void setIdField(String idField) {
        this.e = idField;
    }

    public void setPidField(String pidField) {
        this.f = pidField;
    }

    public void setPidValue(String pidValue) {
        this.g = pidValue;
    }

    public void setCondition(String condition) {
        this.h = condition;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof a)) {
            return false;
        }
        a a2 = (a)o;
        if (!a2.a(this)) {
            return false;
        }
        String string = this.getTable();
        String string2 = a2.getTable();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getTxt();
        String string4 = a2.getTxt();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getKey();
        String string6 = a2.getKey();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getLinkField();
        String string8 = a2.getLinkField();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getIdField();
        String string10 = a2.getIdField();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        String string11 = this.getPidField();
        String string12 = a2.getPidField();
        if (string11 == null ? string12 != null : !string11.equals(string12)) {
            return false;
        }
        String string13 = this.getPidValue();
        String string14 = a2.getPidValue();
        if (string13 == null ? string14 != null : !string13.equals(string14)) {
            return false;
        }
        String string15 = this.getCondition();
        String string16 = a2.getCondition();
        return !(string15 == null ? string16 != null : !string15.equals(string16));
    }

    protected boolean a(Object object) {
        return object instanceof a;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        String string = this.getTable();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getTxt();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getKey();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getLinkField();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getIdField();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        String string6 = this.getPidField();
        n2 = n2 * 59 + (string6 == null ? 43 : string6.hashCode());
        String string7 = this.getPidValue();
        n2 = n2 * 59 + (string7 == null ? 43 : string7.hashCode());
        String string8 = this.getCondition();
        n2 = n2 * 59 + (string8 == null ? 43 : string8.hashCode());
        return n2;
    }

    public String toString() {
        return "LinkDown(table=" + this.getTable() + ", txt=" + this.getTxt() + ", key=" + this.getKey() + ", linkField=" + this.getLinkField() + ", idField=" + this.getIdField() + ", pidField=" + this.getPidField() + ", pidValue=" + this.getPidValue() + ", condition=" + this.getCondition() + ")";
    }
}

