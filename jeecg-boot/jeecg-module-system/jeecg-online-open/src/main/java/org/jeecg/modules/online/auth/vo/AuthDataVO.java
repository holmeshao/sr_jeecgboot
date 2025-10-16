/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.auth.vo;

import java.io.Serializable;

public class AuthDataVO
implements Serializable {
    private static final long serialVersionUID = 1057819436991228603L;
    private String id;
    private String title;
    private String relId;
    private Boolean checked;

    public Boolean isChecked() {
        return this.relId != null && this.relId.length() > 0;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getRelId() {
        return this.relId;
    }

    public Boolean getChecked() {
        return this.checked;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AuthDataVO)) {
            return false;
        }
        AuthDataVO authDataVO = (AuthDataVO)o;
        if (!authDataVO.canEqual(this)) {
            return false;
        }
        Boolean bl = this.getChecked();
        Boolean bl2 = authDataVO.getChecked();
        if (bl == null ? bl2 != null : !((Object)bl).equals(bl2)) {
            return false;
        }
        String string = this.getId();
        String string2 = authDataVO.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getTitle();
        String string4 = authDataVO.getTitle();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getRelId();
        String string6 = authDataVO.getRelId();
        return !(string5 == null ? string6 != null : !string5.equals(string6));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AuthDataVO;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        Boolean bl = this.getChecked();
        n2 = n2 * 59 + (bl == null ? 43 : ((Object)bl).hashCode());
        String string = this.getId();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getTitle();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getRelId();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        return n2;
    }

    public String toString() {
        return "AuthDataVO(id=" + this.getId() + ", title=" + this.getTitle() + ", relId=" + this.getRelId() + ", checked=" + this.getChecked() + ")";
    }
}

