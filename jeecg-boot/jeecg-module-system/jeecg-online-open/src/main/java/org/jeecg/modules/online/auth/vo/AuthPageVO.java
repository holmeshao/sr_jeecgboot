/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.auth.vo;

import java.io.Serializable;

public class AuthPageVO
implements Serializable {
    private static final long serialVersionUID = 724713901683956568L;
    private String id;
    private String code;
    private String title;
    private Integer page;
    private Integer control;
    private String relId;
    private Boolean checked;

    public Boolean isChecked() {
        return this.relId != null && this.relId.length() > 0;
    }

    public String getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }

    public Integer getPage() {
        return this.page;
    }

    public Integer getControl() {
        return this.control;
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

    public void setCode(String code) {
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setControl(Integer control) {
        this.control = control;
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
        if (!(o instanceof AuthPageVO)) {
            return false;
        }
        AuthPageVO authPageVO = (AuthPageVO)o;
        if (!authPageVO.canEqual(this)) {
            return false;
        }
        Integer n = this.getPage();
        Integer n2 = authPageVO.getPage();
        if (n == null ? n2 != null : !((Object)n).equals(n2)) {
            return false;
        }
        Integer n3 = this.getControl();
        Integer n4 = authPageVO.getControl();
        if (n3 == null ? n4 != null : !((Object)n3).equals(n4)) {
            return false;
        }
        Boolean bl = this.getChecked();
        Boolean bl2 = authPageVO.getChecked();
        if (bl == null ? bl2 != null : !((Object)bl).equals(bl2)) {
            return false;
        }
        String string = this.getId();
        String string2 = authPageVO.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getCode();
        String string4 = authPageVO.getCode();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getTitle();
        String string6 = authPageVO.getTitle();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getRelId();
        String string8 = authPageVO.getRelId();
        return !(string7 == null ? string8 != null : !string7.equals(string8));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AuthPageVO;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        Integer n3 = this.getPage();
        n2 = n2 * 59 + (n3 == null ? 43 : ((Object)n3).hashCode());
        Integer n4 = this.getControl();
        n2 = n2 * 59 + (n4 == null ? 43 : ((Object)n4).hashCode());
        Boolean bl = this.getChecked();
        n2 = n2 * 59 + (bl == null ? 43 : ((Object)bl).hashCode());
        String string = this.getId();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getCode();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getTitle();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getRelId();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        return n2;
    }

    public String toString() {
        return "AuthPageVO(id=" + this.getId() + ", code=" + this.getCode() + ", title=" + this.getTitle() + ", page=" + this.getPage() + ", control=" + this.getControl() + ", relId=" + this.getRelId() + ", checked=" + this.getChecked() + ")";
    }
}

