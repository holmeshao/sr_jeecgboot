/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.cgform.model;

public class g {
    private String a;

    public g() {
    }

    public g(String string) {
        this.a = string;
    }

    public String getCustomRender() {
        return this.a;
    }

    public void setCustomRender(String customRender) {
        this.a = customRender;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof g)) {
            return false;
        }
        g g2 = (g)o;
        if (!g2.a(this)) {
            return false;
        }
        String string = this.getCustomRender();
        String string2 = g2.getCustomRender();
        return !(string == null ? string2 != null : !string.equals(string2));
    }

    protected boolean a(Object object) {
        return object instanceof g;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        String string = this.getCustomRender();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        return n2;
    }

    public String toString() {
        return "ScopedSlots(customRender=" + this.getCustomRender() + ")";
    }
}

