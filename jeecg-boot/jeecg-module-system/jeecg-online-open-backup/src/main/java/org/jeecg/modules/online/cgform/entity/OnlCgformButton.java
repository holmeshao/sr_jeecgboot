/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 */
package org.jeecg.modules.online.cgform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

@TableName(value="onl_cgform_button")
public class OnlCgformButton
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_UUID)
    private String id;
    private String cgformHeadId;
    private String buttonCode;
    private String buttonName;
    private String buttonStyle;
    private String optType;
    private String exp;
    private String buttonStatus;
    private Integer orderNum;
    private String buttonIcon;
    private String optPosition;

    public String getId() {
        return this.id;
    }

    public String getCgformHeadId() {
        return this.cgformHeadId;
    }

    public String getButtonCode() {
        return this.buttonCode;
    }

    public String getButtonName() {
        return this.buttonName;
    }

    public String getButtonStyle() {
        return this.buttonStyle;
    }

    public String getOptType() {
        return this.optType;
    }

    public String getExp() {
        return this.exp;
    }

    public String getButtonStatus() {
        return this.buttonStatus;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }

    public String getButtonIcon() {
        return this.buttonIcon;
    }

    public String getOptPosition() {
        return this.optPosition;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCgformHeadId(String cgformHeadId) {
        this.cgformHeadId = cgformHeadId;
    }

    public void setButtonCode(String buttonCode) {
        this.buttonCode = buttonCode;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public void setButtonStyle(String buttonStyle) {
        this.buttonStyle = buttonStyle;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public void setButtonStatus(String buttonStatus) {
        this.buttonStatus = buttonStatus;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public void setButtonIcon(String buttonIcon) {
        this.buttonIcon = buttonIcon;
    }

    public void setOptPosition(String optPosition) {
        this.optPosition = optPosition;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnlCgformButton)) {
            return false;
        }
        OnlCgformButton onlCgformButton = (OnlCgformButton)o;
        if (!onlCgformButton.canEqual(this)) {
            return false;
        }
        Integer n = this.getOrderNum();
        Integer n2 = onlCgformButton.getOrderNum();
        if (n == null ? n2 != null : !((Object)n).equals(n2)) {
            return false;
        }
        String string = this.getId();
        String string2 = onlCgformButton.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getCgformHeadId();
        String string4 = onlCgformButton.getCgformHeadId();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getButtonCode();
        String string6 = onlCgformButton.getButtonCode();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getButtonName();
        String string8 = onlCgformButton.getButtonName();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getButtonStyle();
        String string10 = onlCgformButton.getButtonStyle();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        String string11 = this.getOptType();
        String string12 = onlCgformButton.getOptType();
        if (string11 == null ? string12 != null : !string11.equals(string12)) {
            return false;
        }
        String string13 = this.getExp();
        String string14 = onlCgformButton.getExp();
        if (string13 == null ? string14 != null : !string13.equals(string14)) {
            return false;
        }
        String string15 = this.getButtonStatus();
        String string16 = onlCgformButton.getButtonStatus();
        if (string15 == null ? string16 != null : !string15.equals(string16)) {
            return false;
        }
        String string17 = this.getButtonIcon();
        String string18 = onlCgformButton.getButtonIcon();
        if (string17 == null ? string18 != null : !string17.equals(string18)) {
            return false;
        }
        String string19 = this.getOptPosition();
        String string20 = onlCgformButton.getOptPosition();
        return !(string19 == null ? string20 != null : !string19.equals(string20));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OnlCgformButton;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        Integer n3 = this.getOrderNum();
        n2 = n2 * 59 + (n3 == null ? 43 : ((Object)n3).hashCode());
        String string = this.getId();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getCgformHeadId();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getButtonCode();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getButtonName();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getButtonStyle();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        String string6 = this.getOptType();
        n2 = n2 * 59 + (string6 == null ? 43 : string6.hashCode());
        String string7 = this.getExp();
        n2 = n2 * 59 + (string7 == null ? 43 : string7.hashCode());
        String string8 = this.getButtonStatus();
        n2 = n2 * 59 + (string8 == null ? 43 : string8.hashCode());
        String string9 = this.getButtonIcon();
        n2 = n2 * 59 + (string9 == null ? 43 : string9.hashCode());
        String string10 = this.getOptPosition();
        n2 = n2 * 59 + (string10 == null ? 43 : string10.hashCode());
        return n2;
    }

    public String toString() {
        return "OnlCgformButton(id=" + this.getId() + ", cgformHeadId=" + this.getCgformHeadId() + ", buttonCode=" + this.getButtonCode() + ", buttonName=" + this.getButtonName() + ", buttonStyle=" + this.getButtonStyle() + ", optType=" + this.getOptType() + ", exp=" + this.getExp() + ", buttonStatus=" + this.getButtonStatus() + ", orderNum=" + this.getOrderNum() + ", buttonIcon=" + this.getButtonIcon() + ", optPosition=" + this.getOptPosition() + ")";
    }
}

