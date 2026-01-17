/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.common.util.a;

public class a {
    private String b;
    private String c;
    private Integer d;
    protected String a;

    public a() {
    }

    public a(String string, String string2, Integer n) {
        this.b = string;
        this.c = string2;
        this.d = n;
    }

    public a(String string, String string2, Integer n, String string3) {
        this.b = string;
        this.c = string2;
        this.d = n;
        this.a = string3;
    }

    public String getTitle() {
        return this.b;
    }

    public String getField() {
        return this.c;
    }

    public Integer getOrder() {
        return this.d;
    }

    public String getDefVal() {
        return this.a;
    }

    public void setTitle(String title) {
        this.b = title;
    }

    public void setField(String field) {
        this.c = field;
    }

    public void setOrder(Integer order) {
        this.d = order;
    }

    public void setDefVal(String defVal) {
        this.a = defVal;
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
        Integer n = this.getOrder();
        Integer n2 = a2.getOrder();
        if (n == null ? n2 != null : !((Object)n).equals(n2)) {
            return false;
        }
        String string = this.getTitle();
        String string2 = a2.getTitle();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getField();
        String string4 = a2.getField();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getDefVal();
        String string6 = a2.getDefVal();
        return !(string5 == null ? string6 != null : !string5.equals(string6));
    }

    protected boolean a(Object object) {
        return object instanceof a;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        Integer n3 = this.getOrder();
        n2 = n2 * 59 + (n3 == null ? 43 : ((Object)n3).hashCode());
        String string = this.getTitle();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getField();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getDefVal();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        return n2;
    }

    public String toString() {
        return "BaseColumn(title=" + this.getTitle() + ", field=" + this.getField() + ", order=" + this.getOrder() + ", defVal=" + this.getDefVal() + ")";
    }
}

