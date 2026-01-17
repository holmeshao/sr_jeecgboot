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
package org.jeecg.modules.online.cgform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

@TableName(value="onl_cgform_head")
public class OnlCgformHead
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_UUID)
    private String id;
    private String tableName;
    private Integer tableType;
    private Integer tableVersion;
    private String tableTxt;
    private String isCheckbox;
    private String isDbSynch;
    private String isPage;
    private String isTree;
    private String idSequence;
    private String idType;
    private String queryMode;
    private Integer relationType;
    private String subTableStr;
    private Integer tabOrderNum;
    private String treeParentIdField;
    private String treeIdField;
    private String treeFieldname;
    private String formCategory;
    private String formTemplate;
    private String themeTemplate;
    private String formTemplateMobile;
    private String extConfigJson;
    private String updateBy;
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String createBy;
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer copyType;
    private Integer copyVersion;
    private String physicId;
    private transient Integer hascopy;
    private Integer scroll;
    private transient String taskId;
    private String isDesForm;
    private String desFormCode;
    private Integer tenantId;
    private String lowAppId;
    private transient String selectFieldString;

    public List<String> getSelectFieldList() {
        if (this.selectFieldString == null || "".equals(this.selectFieldString)) {
            return null;
        }
        return Arrays.asList(this.selectFieldString.split(","));
    }

    public String getId() {
        return this.id;
    }

    public String getTableName() {
        return this.tableName;
    }

    public Integer getTableType() {
        return this.tableType;
    }

    public Integer getTableVersion() {
        return this.tableVersion;
    }

    public String getTableTxt() {
        return this.tableTxt;
    }

    public String getIsCheckbox() {
        return this.isCheckbox;
    }

    public String getIsDbSynch() {
        return this.isDbSynch;
    }

    public String getIsPage() {
        return this.isPage;
    }

    public String getIsTree() {
        return this.isTree;
    }

    public String getIdSequence() {
        return this.idSequence;
    }

    public String getIdType() {
        return this.idType;
    }

    public String getQueryMode() {
        return this.queryMode;
    }

    public Integer getRelationType() {
        return this.relationType;
    }

    public String getSubTableStr() {
        return this.subTableStr;
    }

    public Integer getTabOrderNum() {
        return this.tabOrderNum;
    }

    public String getTreeParentIdField() {
        return this.treeParentIdField;
    }

    public String getTreeIdField() {
        return this.treeIdField;
    }

    public String getTreeFieldname() {
        return this.treeFieldname;
    }

    public String getFormCategory() {
        return this.formCategory;
    }

    public String getFormTemplate() {
        return this.formTemplate;
    }

    public String getThemeTemplate() {
        return this.themeTemplate;
    }

    public String getFormTemplateMobile() {
        return this.formTemplateMobile;
    }

    public String getExtConfigJson() {
        return this.extConfigJson;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public Integer getCopyType() {
        return this.copyType;
    }

    public Integer getCopyVersion() {
        return this.copyVersion;
    }

    public String getPhysicId() {
        return this.physicId;
    }

    public Integer getHascopy() {
        return this.hascopy;
    }

    public Integer getScroll() {
        return this.scroll;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public String getIsDesForm() {
        return this.isDesForm;
    }

    public String getDesFormCode() {
        return this.desFormCode;
    }

    public Integer getTenantId() {
        return this.tenantId;
    }

    public String getLowAppId() {
        return this.lowAppId;
    }

    public String getSelectFieldString() {
        return this.selectFieldString;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setTableType(Integer tableType) {
        this.tableType = tableType;
    }

    public void setTableVersion(Integer tableVersion) {
        this.tableVersion = tableVersion;
    }

    public void setTableTxt(String tableTxt) {
        this.tableTxt = tableTxt;
    }

    public void setIsCheckbox(String isCheckbox) {
        this.isCheckbox = isCheckbox;
    }

    public void setIsDbSynch(String isDbSynch) {
        this.isDbSynch = isDbSynch;
    }

    public void setIsPage(String isPage) {
        this.isPage = isPage;
    }

    public void setIsTree(String isTree) {
        this.isTree = isTree;
    }

    public void setIdSequence(String idSequence) {
        this.idSequence = idSequence;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public void setQueryMode(String queryMode) {
        this.queryMode = queryMode;
    }

    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    public void setSubTableStr(String subTableStr) {
        this.subTableStr = subTableStr;
    }

    public void setTabOrderNum(Integer tabOrderNum) {
        this.tabOrderNum = tabOrderNum;
    }

    public void setTreeParentIdField(String treeParentIdField) {
        this.treeParentIdField = treeParentIdField;
    }

    public void setTreeIdField(String treeIdField) {
        this.treeIdField = treeIdField;
    }

    public void setTreeFieldname(String treeFieldname) {
        this.treeFieldname = treeFieldname;
    }

    public void setFormCategory(String formCategory) {
        this.formCategory = formCategory;
    }

    public void setFormTemplate(String formTemplate) {
        this.formTemplate = formTemplate;
    }

    public void setThemeTemplate(String themeTemplate) {
        this.themeTemplate = themeTemplate;
    }

    public void setFormTemplateMobile(String formTemplateMobile) {
        this.formTemplateMobile = formTemplateMobile;
    }

    public void setExtConfigJson(String extConfigJson) {
        this.extConfigJson = extConfigJson;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setCopyType(Integer copyType) {
        this.copyType = copyType;
    }

    public void setCopyVersion(Integer copyVersion) {
        this.copyVersion = copyVersion;
    }

    public void setPhysicId(String physicId) {
        this.physicId = physicId;
    }

    public void setHascopy(Integer hascopy) {
        this.hascopy = hascopy;
    }

    public void setScroll(Integer scroll) {
        this.scroll = scroll;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setIsDesForm(String isDesForm) {
        this.isDesForm = isDesForm;
    }

    public void setDesFormCode(String desFormCode) {
        this.desFormCode = desFormCode;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public void setLowAppId(String lowAppId) {
        this.lowAppId = lowAppId;
    }

    public void setSelectFieldString(String selectFieldString) {
        this.selectFieldString = selectFieldString;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnlCgformHead)) {
            return false;
        }
        OnlCgformHead onlCgformHead = (OnlCgformHead)o;
        if (!onlCgformHead.canEqual(this)) {
            return false;
        }
        Integer n = this.getTableType();
        Integer n2 = onlCgformHead.getTableType();
        if (n == null ? n2 != null : !((Object)n).equals(n2)) {
            return false;
        }
        Integer n3 = this.getTableVersion();
        Integer n4 = onlCgformHead.getTableVersion();
        if (n3 == null ? n4 != null : !((Object)n3).equals(n4)) {
            return false;
        }
        Integer n5 = this.getRelationType();
        Integer n6 = onlCgformHead.getRelationType();
        if (n5 == null ? n6 != null : !((Object)n5).equals(n6)) {
            return false;
        }
        Integer n7 = this.getTabOrderNum();
        Integer n8 = onlCgformHead.getTabOrderNum();
        if (n7 == null ? n8 != null : !((Object)n7).equals(n8)) {
            return false;
        }
        Integer n9 = this.getCopyType();
        Integer n10 = onlCgformHead.getCopyType();
        if (n9 == null ? n10 != null : !((Object)n9).equals(n10)) {
            return false;
        }
        Integer n11 = this.getCopyVersion();
        Integer n12 = onlCgformHead.getCopyVersion();
        if (n11 == null ? n12 != null : !((Object)n11).equals(n12)) {
            return false;
        }
        Integer n13 = this.getScroll();
        Integer n14 = onlCgformHead.getScroll();
        if (n13 == null ? n14 != null : !((Object)n13).equals(n14)) {
            return false;
        }
        Integer n15 = this.getTenantId();
        Integer n16 = onlCgformHead.getTenantId();
        if (n15 == null ? n16 != null : !((Object)n15).equals(n16)) {
            return false;
        }
        String string = this.getId();
        String string2 = onlCgformHead.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getTableName();
        String string4 = onlCgformHead.getTableName();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getTableTxt();
        String string6 = onlCgformHead.getTableTxt();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getIsCheckbox();
        String string8 = onlCgformHead.getIsCheckbox();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getIsDbSynch();
        String string10 = onlCgformHead.getIsDbSynch();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        String string11 = this.getIsPage();
        String string12 = onlCgformHead.getIsPage();
        if (string11 == null ? string12 != null : !string11.equals(string12)) {
            return false;
        }
        String string13 = this.getIsTree();
        String string14 = onlCgformHead.getIsTree();
        if (string13 == null ? string14 != null : !string13.equals(string14)) {
            return false;
        }
        String string15 = this.getIdSequence();
        String string16 = onlCgformHead.getIdSequence();
        if (string15 == null ? string16 != null : !string15.equals(string16)) {
            return false;
        }
        String string17 = this.getIdType();
        String string18 = onlCgformHead.getIdType();
        if (string17 == null ? string18 != null : !string17.equals(string18)) {
            return false;
        }
        String string19 = this.getQueryMode();
        String string20 = onlCgformHead.getQueryMode();
        if (string19 == null ? string20 != null : !string19.equals(string20)) {
            return false;
        }
        String string21 = this.getSubTableStr();
        String string22 = onlCgformHead.getSubTableStr();
        if (string21 == null ? string22 != null : !string21.equals(string22)) {
            return false;
        }
        String string23 = this.getTreeParentIdField();
        String string24 = onlCgformHead.getTreeParentIdField();
        if (string23 == null ? string24 != null : !string23.equals(string24)) {
            return false;
        }
        String string25 = this.getTreeIdField();
        String string26 = onlCgformHead.getTreeIdField();
        if (string25 == null ? string26 != null : !string25.equals(string26)) {
            return false;
        }
        String string27 = this.getTreeFieldname();
        String string28 = onlCgformHead.getTreeFieldname();
        if (string27 == null ? string28 != null : !string27.equals(string28)) {
            return false;
        }
        String string29 = this.getFormCategory();
        String string30 = onlCgformHead.getFormCategory();
        if (string29 == null ? string30 != null : !string29.equals(string30)) {
            return false;
        }
        String string31 = this.getFormTemplate();
        String string32 = onlCgformHead.getFormTemplate();
        if (string31 == null ? string32 != null : !string31.equals(string32)) {
            return false;
        }
        String string33 = this.getThemeTemplate();
        String string34 = onlCgformHead.getThemeTemplate();
        if (string33 == null ? string34 != null : !string33.equals(string34)) {
            return false;
        }
        String string35 = this.getFormTemplateMobile();
        String string36 = onlCgformHead.getFormTemplateMobile();
        if (string35 == null ? string36 != null : !string35.equals(string36)) {
            return false;
        }
        String string37 = this.getExtConfigJson();
        String string38 = onlCgformHead.getExtConfigJson();
        if (string37 == null ? string38 != null : !string37.equals(string38)) {
            return false;
        }
        String string39 = this.getUpdateBy();
        String string40 = onlCgformHead.getUpdateBy();
        if (string39 == null ? string40 != null : !string39.equals(string40)) {
            return false;
        }
        Date date = this.getUpdateTime();
        Date date2 = onlCgformHead.getUpdateTime();
        if (date == null ? date2 != null : !((Object)date).equals(date2)) {
            return false;
        }
        String string41 = this.getCreateBy();
        String string42 = onlCgformHead.getCreateBy();
        if (string41 == null ? string42 != null : !string41.equals(string42)) {
            return false;
        }
        Date date3 = this.getCreateTime();
        Date date4 = onlCgformHead.getCreateTime();
        if (date3 == null ? date4 != null : !((Object)date3).equals(date4)) {
            return false;
        }
        String string43 = this.getPhysicId();
        String string44 = onlCgformHead.getPhysicId();
        if (string43 == null ? string44 != null : !string43.equals(string44)) {
            return false;
        }
        String string45 = this.getIsDesForm();
        String string46 = onlCgformHead.getIsDesForm();
        if (string45 == null ? string46 != null : !string45.equals(string46)) {
            return false;
        }
        String string47 = this.getDesFormCode();
        String string48 = onlCgformHead.getDesFormCode();
        if (string47 == null ? string48 != null : !string47.equals(string48)) {
            return false;
        }
        String string49 = this.getLowAppId();
        String string50 = onlCgformHead.getLowAppId();
        return !(string49 == null ? string50 != null : !string49.equals(string50));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OnlCgformHead;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        Integer n3 = this.getTableType();
        n2 = n2 * 59 + (n3 == null ? 43 : ((Object)n3).hashCode());
        Integer n4 = this.getTableVersion();
        n2 = n2 * 59 + (n4 == null ? 43 : ((Object)n4).hashCode());
        Integer n5 = this.getRelationType();
        n2 = n2 * 59 + (n5 == null ? 43 : ((Object)n5).hashCode());
        Integer n6 = this.getTabOrderNum();
        n2 = n2 * 59 + (n6 == null ? 43 : ((Object)n6).hashCode());
        Integer n7 = this.getCopyType();
        n2 = n2 * 59 + (n7 == null ? 43 : ((Object)n7).hashCode());
        Integer n8 = this.getCopyVersion();
        n2 = n2 * 59 + (n8 == null ? 43 : ((Object)n8).hashCode());
        Integer n9 = this.getScroll();
        n2 = n2 * 59 + (n9 == null ? 43 : ((Object)n9).hashCode());
        Integer n10 = this.getTenantId();
        n2 = n2 * 59 + (n10 == null ? 43 : ((Object)n10).hashCode());
        String string = this.getId();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getTableName();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getTableTxt();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getIsCheckbox();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getIsDbSynch();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        String string6 = this.getIsPage();
        n2 = n2 * 59 + (string6 == null ? 43 : string6.hashCode());
        String string7 = this.getIsTree();
        n2 = n2 * 59 + (string7 == null ? 43 : string7.hashCode());
        String string8 = this.getIdSequence();
        n2 = n2 * 59 + (string8 == null ? 43 : string8.hashCode());
        String string9 = this.getIdType();
        n2 = n2 * 59 + (string9 == null ? 43 : string9.hashCode());
        String string10 = this.getQueryMode();
        n2 = n2 * 59 + (string10 == null ? 43 : string10.hashCode());
        String string11 = this.getSubTableStr();
        n2 = n2 * 59 + (string11 == null ? 43 : string11.hashCode());
        String string12 = this.getTreeParentIdField();
        n2 = n2 * 59 + (string12 == null ? 43 : string12.hashCode());
        String string13 = this.getTreeIdField();
        n2 = n2 * 59 + (string13 == null ? 43 : string13.hashCode());
        String string14 = this.getTreeFieldname();
        n2 = n2 * 59 + (string14 == null ? 43 : string14.hashCode());
        String string15 = this.getFormCategory();
        n2 = n2 * 59 + (string15 == null ? 43 : string15.hashCode());
        String string16 = this.getFormTemplate();
        n2 = n2 * 59 + (string16 == null ? 43 : string16.hashCode());
        String string17 = this.getThemeTemplate();
        n2 = n2 * 59 + (string17 == null ? 43 : string17.hashCode());
        String string18 = this.getFormTemplateMobile();
        n2 = n2 * 59 + (string18 == null ? 43 : string18.hashCode());
        String string19 = this.getExtConfigJson();
        n2 = n2 * 59 + (string19 == null ? 43 : string19.hashCode());
        String string20 = this.getUpdateBy();
        n2 = n2 * 59 + (string20 == null ? 43 : string20.hashCode());
        Date date = this.getUpdateTime();
        n2 = n2 * 59 + (date == null ? 43 : ((Object)date).hashCode());
        String string21 = this.getCreateBy();
        n2 = n2 * 59 + (string21 == null ? 43 : string21.hashCode());
        Date date2 = this.getCreateTime();
        n2 = n2 * 59 + (date2 == null ? 43 : ((Object)date2).hashCode());
        String string22 = this.getPhysicId();
        n2 = n2 * 59 + (string22 == null ? 43 : string22.hashCode());
        String string23 = this.getIsDesForm();
        n2 = n2 * 59 + (string23 == null ? 43 : string23.hashCode());
        String string24 = this.getDesFormCode();
        n2 = n2 * 59 + (string24 == null ? 43 : string24.hashCode());
        String string25 = this.getLowAppId();
        n2 = n2 * 59 + (string25 == null ? 43 : string25.hashCode());
        return n2;
    }

    public String toString() {
        return "OnlCgformHead(id=" + this.getId() + ", tableName=" + this.getTableName() + ", tableType=" + this.getTableType() + ", tableVersion=" + this.getTableVersion() + ", tableTxt=" + this.getTableTxt() + ", isCheckbox=" + this.getIsCheckbox() + ", isDbSynch=" + this.getIsDbSynch() + ", isPage=" + this.getIsPage() + ", isTree=" + this.getIsTree() + ", idSequence=" + this.getIdSequence() + ", idType=" + this.getIdType() + ", queryMode=" + this.getQueryMode() + ", relationType=" + this.getRelationType() + ", subTableStr=" + this.getSubTableStr() + ", tabOrderNum=" + this.getTabOrderNum() + ", treeParentIdField=" + this.getTreeParentIdField() + ", treeIdField=" + this.getTreeIdField() + ", treeFieldname=" + this.getTreeFieldname() + ", formCategory=" + this.getFormCategory() + ", formTemplate=" + this.getFormTemplate() + ", themeTemplate=" + this.getThemeTemplate() + ", formTemplateMobile=" + this.getFormTemplateMobile() + ", extConfigJson=" + this.getExtConfigJson() + ", updateBy=" + this.getUpdateBy() + ", updateTime=" + this.getUpdateTime() + ", createBy=" + this.getCreateBy() + ", createTime=" + this.getCreateTime() + ", copyType=" + this.getCopyType() + ", copyVersion=" + this.getCopyVersion() + ", physicId=" + this.getPhysicId() + ", hascopy=" + this.getHascopy() + ", scroll=" + this.getScroll() + ", taskId=" + this.getTaskId() + ", isDesForm=" + this.getIsDesForm() + ", desFormCode=" + this.getDesFormCode() + ", tenantId=" + this.getTenantId() + ", lowAppId=" + this.getLowAppId() + ", selectFieldString=" + this.getSelectFieldString() + ")";
    }
}

