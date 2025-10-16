/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 *  com.fasterxml.jackson.annotation.JsonFormat
 *  org.springframework.format.annotation.DateTimeFormat
 */
package org.jeecg.modules.online.cgreport.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

@TableName(value="onl_cgreport_item")
public class OnlCgreportItem
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_ID)
    private String id;
    private String cgrheadId;
    private String fieldName;
    private String fieldTxt;
    private Integer fieldWidth;
    private String fieldType;
    private String searchMode;
    private Integer isOrder;
    private Integer isSearch;
    private String dictCode;
    private String fieldHref;
    private Integer isShow;
    private Integer orderNum;
    private String replaceVal;
    private String isTotal;
    private String createBy;
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String updateBy;
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String groupTitle;

    public String getId() {
        return this.id;
    }

    public String getCgrheadId() {
        return this.cgrheadId;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getFieldTxt() {
        return this.fieldTxt;
    }

    public Integer getFieldWidth() {
        return this.fieldWidth;
    }

    public String getFieldType() {
        return this.fieldType;
    }

    public String getSearchMode() {
        return this.searchMode;
    }

    public Integer getIsOrder() {
        return this.isOrder;
    }

    public Integer getIsSearch() {
        return this.isSearch;
    }

    public String getDictCode() {
        return this.dictCode;
    }

    public String getFieldHref() {
        return this.fieldHref;
    }

    public Integer getIsShow() {
        return this.isShow;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }

    public String getReplaceVal() {
        return this.replaceVal;
    }

    public String getIsTotal() {
        return this.isTotal;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public String getGroupTitle() {
        return this.groupTitle;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCgrheadId(String cgrheadId) {
        this.cgrheadId = cgrheadId;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldTxt(String fieldTxt) {
        this.fieldTxt = fieldTxt;
    }

    public void setFieldWidth(Integer fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public void setSearchMode(String searchMode) {
        this.searchMode = searchMode;
    }

    public void setIsOrder(Integer isOrder) {
        this.isOrder = isOrder;
    }

    public void setIsSearch(Integer isSearch) {
        this.isSearch = isSearch;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public void setFieldHref(String fieldHref) {
        this.fieldHref = fieldHref;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public void setReplaceVal(String replaceVal) {
        this.replaceVal = replaceVal;
    }

    public void setIsTotal(String isTotal) {
        this.isTotal = isTotal;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnlCgreportItem)) {
            return false;
        }
        OnlCgreportItem onlCgreportItem = (OnlCgreportItem)o;
        if (!onlCgreportItem.canEqual(this)) {
            return false;
        }
        Integer n = this.getFieldWidth();
        Integer n2 = onlCgreportItem.getFieldWidth();
        if (n == null ? n2 != null : !((Object)n).equals(n2)) {
            return false;
        }
        Integer n3 = this.getIsOrder();
        Integer n4 = onlCgreportItem.getIsOrder();
        if (n3 == null ? n4 != null : !((Object)n3).equals(n4)) {
            return false;
        }
        Integer n5 = this.getIsSearch();
        Integer n6 = onlCgreportItem.getIsSearch();
        if (n5 == null ? n6 != null : !((Object)n5).equals(n6)) {
            return false;
        }
        Integer n7 = this.getIsShow();
        Integer n8 = onlCgreportItem.getIsShow();
        if (n7 == null ? n8 != null : !((Object)n7).equals(n8)) {
            return false;
        }
        Integer n9 = this.getOrderNum();
        Integer n10 = onlCgreportItem.getOrderNum();
        if (n9 == null ? n10 != null : !((Object)n9).equals(n10)) {
            return false;
        }
        String string = this.getId();
        String string2 = onlCgreportItem.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getCgrheadId();
        String string4 = onlCgreportItem.getCgrheadId();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getFieldName();
        String string6 = onlCgreportItem.getFieldName();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getFieldTxt();
        String string8 = onlCgreportItem.getFieldTxt();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getFieldType();
        String string10 = onlCgreportItem.getFieldType();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        String string11 = this.getSearchMode();
        String string12 = onlCgreportItem.getSearchMode();
        if (string11 == null ? string12 != null : !string11.equals(string12)) {
            return false;
        }
        String string13 = this.getDictCode();
        String string14 = onlCgreportItem.getDictCode();
        if (string13 == null ? string14 != null : !string13.equals(string14)) {
            return false;
        }
        String string15 = this.getFieldHref();
        String string16 = onlCgreportItem.getFieldHref();
        if (string15 == null ? string16 != null : !string15.equals(string16)) {
            return false;
        }
        String string17 = this.getReplaceVal();
        String string18 = onlCgreportItem.getReplaceVal();
        if (string17 == null ? string18 != null : !string17.equals(string18)) {
            return false;
        }
        String string19 = this.getIsTotal();
        String string20 = onlCgreportItem.getIsTotal();
        if (string19 == null ? string20 != null : !string19.equals(string20)) {
            return false;
        }
        String string21 = this.getCreateBy();
        String string22 = onlCgreportItem.getCreateBy();
        if (string21 == null ? string22 != null : !string21.equals(string22)) {
            return false;
        }
        Date date = this.getCreateTime();
        Date date2 = onlCgreportItem.getCreateTime();
        if (date == null ? date2 != null : !((Object)date).equals(date2)) {
            return false;
        }
        String string23 = this.getUpdateBy();
        String string24 = onlCgreportItem.getUpdateBy();
        if (string23 == null ? string24 != null : !string23.equals(string24)) {
            return false;
        }
        Date date3 = this.getUpdateTime();
        Date date4 = onlCgreportItem.getUpdateTime();
        if (date3 == null ? date4 != null : !((Object)date3).equals(date4)) {
            return false;
        }
        String string25 = this.getGroupTitle();
        String string26 = onlCgreportItem.getGroupTitle();
        return !(string25 == null ? string26 != null : !string25.equals(string26));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OnlCgreportItem;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        Integer n3 = this.getFieldWidth();
        n2 = n2 * 59 + (n3 == null ? 43 : ((Object)n3).hashCode());
        Integer n4 = this.getIsOrder();
        n2 = n2 * 59 + (n4 == null ? 43 : ((Object)n4).hashCode());
        Integer n5 = this.getIsSearch();
        n2 = n2 * 59 + (n5 == null ? 43 : ((Object)n5).hashCode());
        Integer n6 = this.getIsShow();
        n2 = n2 * 59 + (n6 == null ? 43 : ((Object)n6).hashCode());
        Integer n7 = this.getOrderNum();
        n2 = n2 * 59 + (n7 == null ? 43 : ((Object)n7).hashCode());
        String string = this.getId();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getCgrheadId();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getFieldName();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getFieldTxt();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getFieldType();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        String string6 = this.getSearchMode();
        n2 = n2 * 59 + (string6 == null ? 43 : string6.hashCode());
        String string7 = this.getDictCode();
        n2 = n2 * 59 + (string7 == null ? 43 : string7.hashCode());
        String string8 = this.getFieldHref();
        n2 = n2 * 59 + (string8 == null ? 43 : string8.hashCode());
        String string9 = this.getReplaceVal();
        n2 = n2 * 59 + (string9 == null ? 43 : string9.hashCode());
        String string10 = this.getIsTotal();
        n2 = n2 * 59 + (string10 == null ? 43 : string10.hashCode());
        String string11 = this.getCreateBy();
        n2 = n2 * 59 + (string11 == null ? 43 : string11.hashCode());
        Date date = this.getCreateTime();
        n2 = n2 * 59 + (date == null ? 43 : ((Object)date).hashCode());
        String string12 = this.getUpdateBy();
        n2 = n2 * 59 + (string12 == null ? 43 : string12.hashCode());
        Date date2 = this.getUpdateTime();
        n2 = n2 * 59 + (date2 == null ? 43 : ((Object)date2).hashCode());
        String string13 = this.getGroupTitle();
        n2 = n2 * 59 + (string13 == null ? 43 : string13.hashCode());
        return n2;
    }

    public String toString() {
        return "OnlCgreportItem(id=" + this.getId() + ", cgrheadId=" + this.getCgrheadId() + ", fieldName=" + this.getFieldName() + ", fieldTxt=" + this.getFieldTxt() + ", fieldWidth=" + this.getFieldWidth() + ", fieldType=" + this.getFieldType() + ", searchMode=" + this.getSearchMode() + ", isOrder=" + this.getIsOrder() + ", isSearch=" + this.getIsSearch() + ", dictCode=" + this.getDictCode() + ", fieldHref=" + this.getFieldHref() + ", isShow=" + this.getIsShow() + ", orderNum=" + this.getOrderNum() + ", replaceVal=" + this.getReplaceVal() + ", isTotal=" + this.getIsTotal() + ", createBy=" + this.getCreateBy() + ", createTime=" + this.getCreateTime() + ", updateBy=" + this.getUpdateBy() + ", updateTime=" + this.getUpdateTime() + ", groupTitle=" + this.getGroupTitle() + ")";
    }
}

