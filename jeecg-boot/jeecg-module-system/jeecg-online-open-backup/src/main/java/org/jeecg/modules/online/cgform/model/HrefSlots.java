/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.cgform.model;

public class HrefSlots {
    private String slotName;
    private String href;

    public HrefSlots() {
    }

    public HrefSlots(String slotName, String href) {
        this.slotName = slotName;
        this.href = href;
    }

    public String getSlotName() {
        return this.slotName;
    }

    public String getHref() {
        return this.href;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HrefSlots)) {
            return false;
        }
        HrefSlots hrefSlots = (HrefSlots)o;
        if (!hrefSlots.canEqual(this)) {
            return false;
        }
        String string = this.getSlotName();
        String string2 = hrefSlots.getSlotName();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getHref();
        String string4 = hrefSlots.getHref();
        return !(string3 == null ? string4 != null : !string3.equals(string4));
    }

    protected boolean canEqual(Object other) {
        return other instanceof HrefSlots;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        String string = this.getSlotName();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getHref();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        return n2;
    }

    public String toString() {
        return "HrefSlots(slotName=" + this.getSlotName() + ", href=" + this.getHref() + ")";
    }
}

