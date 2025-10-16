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
import java.util.Date;
import org.jeecg.modules.online.cgform.b.b;
import org.springframework.format.annotation.DateTimeFormat;

@TableName(value="onl_cgform_field")
public class OnlCgformField
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_UUID)
    private String id;
    private String cgformHeadId;
    private String dbFieldName;
    private String dbFieldTxt;
    private String dbFieldNameOld;
    private Integer dbIsKey;
    private Integer dbIsNull;
    private Integer dbIsPersist = b.b;
    private String dbType;
    private Integer dbLength;
    private Integer dbPointLength;
    private String dbDefaultVal;
    private String dictField;
    private String dictTable;
    private String dictText;
    private String fieldShowType;
    private String fieldHref;
    private Integer fieldLength;
    private String fieldValidType;
    private String fieldMustInput;
    private String fieldExtendJson;
    private String fieldDefaultValue;
    private Integer isQuery;
    private Integer isShowForm;
    private Integer isShowList;
    private Integer isReadOnly;
    private String queryMode;
    private String mainTable;
    private String mainField;
    private Integer orderNum;
    private String updateBy;
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String createBy;
    private String converter;
    private String queryConfigFlag;
    private String queryDefVal;
    private String queryDictText;
    private String queryDictField;
    private String queryDictTable;
    private String queryShowType;
    private String queryValidType;
    private String queryMustInput;
    private String sortFlag;
    private transient String alias;

    public String getId() {
        return this.id;
    }

    public String getCgformHeadId() {
        return this.cgformHeadId;
    }

    public String getDbFieldName() {
        return this.dbFieldName;
    }

    public String getDbFieldTxt() {
        return this.dbFieldTxt;
    }

    public String getDbFieldNameOld() {
        return this.dbFieldNameOld;
    }

    public Integer getDbIsKey() {
        return this.dbIsKey;
    }

    public Integer getDbIsNull() {
        return this.dbIsNull;
    }

    public Integer getDbIsPersist() {
        return this.dbIsPersist;
    }

    public String getDbType() {
        return this.dbType;
    }

    public Integer getDbLength() {
        return this.dbLength;
    }

    public Integer getDbPointLength() {
        return this.dbPointLength;
    }

    public String getDbDefaultVal() {
        return this.dbDefaultVal;
    }

    public String getDictField() {
        return this.dictField;
    }

    public String getDictTable() {
        return this.dictTable;
    }

    public String getDictText() {
        return this.dictText;
    }

    public String getFieldShowType() {
        return this.fieldShowType;
    }

    public String getFieldHref() {
        return this.fieldHref;
    }

    public Integer getFieldLength() {
        return this.fieldLength;
    }

    public String getFieldValidType() {
        return this.fieldValidType;
    }

    public String getFieldMustInput() {
        return this.fieldMustInput;
    }

    public String getFieldExtendJson() {
        return this.fieldExtendJson;
    }

    public String getFieldDefaultValue() {
        return this.fieldDefaultValue;
    }

    public Integer getIsQuery() {
        return this.isQuery;
    }

    public Integer getIsShowForm() {
        return this.isShowForm;
    }

    public Integer getIsShowList() {
        return this.isShowList;
    }

    public Integer getIsReadOnly() {
        return this.isReadOnly;
    }

    public String getQueryMode() {
        return this.queryMode;
    }

    public String getMainTable() {
        return this.mainTable;
    }

    public String getMainField() {
        return this.mainField;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public String getConverter() {
        return this.converter;
    }

    public String getQueryConfigFlag() {
        return this.queryConfigFlag;
    }

    public String getQueryDefVal() {
        return this.queryDefVal;
    }

    public String getQueryDictText() {
        return this.queryDictText;
    }

    public String getQueryDictField() {
        return this.queryDictField;
    }

    public String getQueryDictTable() {
        return this.queryDictTable;
    }

    public String getQueryShowType() {
        return this.queryShowType;
    }

    public String getQueryValidType() {
        return this.queryValidType;
    }

    public String getQueryMustInput() {
        return this.queryMustInput;
    }

    public String getSortFlag() {
        return this.sortFlag;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCgformHeadId(String cgformHeadId) {
        this.cgformHeadId = cgformHeadId;
    }

    public void setDbFieldName(String dbFieldName) {
        this.dbFieldName = dbFieldName;
    }

    public void setDbFieldTxt(String dbFieldTxt) {
        this.dbFieldTxt = dbFieldTxt;
    }

    public void setDbFieldNameOld(String dbFieldNameOld) {
        this.dbFieldNameOld = dbFieldNameOld;
    }

    public void setDbIsKey(Integer dbIsKey) {
        this.dbIsKey = dbIsKey;
    }

    public void setDbIsNull(Integer dbIsNull) {
        this.dbIsNull = dbIsNull;
    }

    public void setDbIsPersist(Integer dbIsPersist) {
        this.dbIsPersist = dbIsPersist;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public void setDbLength(Integer dbLength) {
        this.dbLength = dbLength;
    }

    public void setDbPointLength(Integer dbPointLength) {
        this.dbPointLength = dbPointLength;
    }

    public void setDbDefaultVal(String dbDefaultVal) {
        this.dbDefaultVal = dbDefaultVal;
    }

    public void setDictField(String dictField) {
        this.dictField = dictField;
    }

    public void setDictTable(String dictTable) {
        this.dictTable = dictTable;
    }

    public void setDictText(String dictText) {
        this.dictText = dictText;
    }

    public void setFieldShowType(String fieldShowType) {
        this.fieldShowType = fieldShowType;
    }

    public void setFieldHref(String fieldHref) {
        this.fieldHref = fieldHref;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    public void setFieldValidType(String fieldValidType) {
        this.fieldValidType = fieldValidType;
    }

    public void setFieldMustInput(String fieldMustInput) {
        this.fieldMustInput = fieldMustInput;
    }

    public void setFieldExtendJson(String fieldExtendJson) {
        this.fieldExtendJson = fieldExtendJson;
    }

    public void setFieldDefaultValue(String fieldDefaultValue) {
        this.fieldDefaultValue = fieldDefaultValue;
    }

    public void setIsQuery(Integer isQuery) {
        this.isQuery = isQuery;
    }

    public void setIsShowForm(Integer isShowForm) {
        this.isShowForm = isShowForm;
    }

    public void setIsShowList(Integer isShowList) {
        this.isShowList = isShowList;
    }

    public void setIsReadOnly(Integer isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public void setQueryMode(String queryMode) {
        this.queryMode = queryMode;
    }

    public void setMainTable(String mainTable) {
        this.mainTable = mainTable;
    }

    public void setMainField(String mainField) {
        this.mainField = mainField;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setConverter(String converter) {
        this.converter = converter;
    }

    public void setQueryConfigFlag(String queryConfigFlag) {
        this.queryConfigFlag = queryConfigFlag;
    }

    public void setQueryDefVal(String queryDefVal) {
        this.queryDefVal = queryDefVal;
    }

    public void setQueryDictText(String queryDictText) {
        this.queryDictText = queryDictText;
    }

    public void setQueryDictField(String queryDictField) {
        this.queryDictField = queryDictField;
    }

    public void setQueryDictTable(String queryDictTable) {
        this.queryDictTable = queryDictTable;
    }

    public void setQueryShowType(String queryShowType) {
        this.queryShowType = queryShowType;
    }

    public void setQueryValidType(String queryValidType) {
        this.queryValidType = queryValidType;
    }

    public void setQueryMustInput(String queryMustInput) {
        this.queryMustInput = queryMustInput;
    }

    public void setSortFlag(String sortFlag) {
        this.sortFlag = sortFlag;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnlCgformField)) {
            return false;
        }
        OnlCgformField onlCgformField = (OnlCgformField)o;
        if (!onlCgformField.canEqual(this)) {
            return false;
        }
        Integer n = this.getDbIsKey();
        Integer n2 = onlCgformField.getDbIsKey();
        if (n == null ? n2 != null : !((Object)n).equals(n2)) {
            return false;
        }
        Integer n3 = this.getDbIsNull();
        Integer n4 = onlCgformField.getDbIsNull();
        if (n3 == null ? n4 != null : !((Object)n3).equals(n4)) {
            return false;
        }
        Integer n5 = this.getDbIsPersist();
        Integer n6 = onlCgformField.getDbIsPersist();
        if (n5 == null ? n6 != null : !((Object)n5).equals(n6)) {
            return false;
        }
        Integer n7 = this.getDbLength();
        Integer n8 = onlCgformField.getDbLength();
        if (n7 == null ? n8 != null : !((Object)n7).equals(n8)) {
            return false;
        }
        Integer n9 = this.getDbPointLength();
        Integer n10 = onlCgformField.getDbPointLength();
        if (n9 == null ? n10 != null : !((Object)n9).equals(n10)) {
            return false;
        }
        Integer n11 = this.getFieldLength();
        Integer n12 = onlCgformField.getFieldLength();
        if (n11 == null ? n12 != null : !((Object)n11).equals(n12)) {
            return false;
        }
        Integer n13 = this.getIsQuery();
        Integer n14 = onlCgformField.getIsQuery();
        if (n13 == null ? n14 != null : !((Object)n13).equals(n14)) {
            return false;
        }
        Integer n15 = this.getIsShowForm();
        Integer n16 = onlCgformField.getIsShowForm();
        if (n15 == null ? n16 != null : !((Object)n15).equals(n16)) {
            return false;
        }
        Integer n17 = this.getIsShowList();
        Integer n18 = onlCgformField.getIsShowList();
        if (n17 == null ? n18 != null : !((Object)n17).equals(n18)) {
            return false;
        }
        Integer n19 = this.getIsReadOnly();
        Integer n20 = onlCgformField.getIsReadOnly();
        if (n19 == null ? n20 != null : !((Object)n19).equals(n20)) {
            return false;
        }
        Integer n21 = this.getOrderNum();
        Integer n22 = onlCgformField.getOrderNum();
        if (n21 == null ? n22 != null : !((Object)n21).equals(n22)) {
            return false;
        }
        String string = this.getId();
        String string2 = onlCgformField.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getCgformHeadId();
        String string4 = onlCgformField.getCgformHeadId();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getDbFieldName();
        String string6 = onlCgformField.getDbFieldName();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getDbFieldTxt();
        String string8 = onlCgformField.getDbFieldTxt();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getDbFieldNameOld();
        String string10 = onlCgformField.getDbFieldNameOld();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        String string11 = this.getDbType();
        String string12 = onlCgformField.getDbType();
        if (string11 == null ? string12 != null : !string11.equals(string12)) {
            return false;
        }
        String string13 = this.getDbDefaultVal();
        String string14 = onlCgformField.getDbDefaultVal();
        if (string13 == null ? string14 != null : !string13.equals(string14)) {
            return false;
        }
        String string15 = this.getDictField();
        String string16 = onlCgformField.getDictField();
        if (string15 == null ? string16 != null : !string15.equals(string16)) {
            return false;
        }
        String string17 = this.getDictTable();
        String string18 = onlCgformField.getDictTable();
        if (string17 == null ? string18 != null : !string17.equals(string18)) {
            return false;
        }
        String string19 = this.getDictText();
        String string20 = onlCgformField.getDictText();
        if (string19 == null ? string20 != null : !string19.equals(string20)) {
            return false;
        }
        String string21 = this.getFieldShowType();
        String string22 = onlCgformField.getFieldShowType();
        if (string21 == null ? string22 != null : !string21.equals(string22)) {
            return false;
        }
        String string23 = this.getFieldHref();
        String string24 = onlCgformField.getFieldHref();
        if (string23 == null ? string24 != null : !string23.equals(string24)) {
            return false;
        }
        String string25 = this.getFieldValidType();
        String string26 = onlCgformField.getFieldValidType();
        if (string25 == null ? string26 != null : !string25.equals(string26)) {
            return false;
        }
        String string27 = this.getFieldMustInput();
        String string28 = onlCgformField.getFieldMustInput();
        if (string27 == null ? string28 != null : !string27.equals(string28)) {
            return false;
        }
        String string29 = this.getFieldExtendJson();
        String string30 = onlCgformField.getFieldExtendJson();
        if (string29 == null ? string30 != null : !string29.equals(string30)) {
            return false;
        }
        String string31 = this.getFieldDefaultValue();
        String string32 = onlCgformField.getFieldDefaultValue();
        if (string31 == null ? string32 != null : !string31.equals(string32)) {
            return false;
        }
        String string33 = this.getQueryMode();
        String string34 = onlCgformField.getQueryMode();
        if (string33 == null ? string34 != null : !string33.equals(string34)) {
            return false;
        }
        String string35 = this.getMainTable();
        String string36 = onlCgformField.getMainTable();
        if (string35 == null ? string36 != null : !string35.equals(string36)) {
            return false;
        }
        String string37 = this.getMainField();
        String string38 = onlCgformField.getMainField();
        if (string37 == null ? string38 != null : !string37.equals(string38)) {
            return false;
        }
        String string39 = this.getUpdateBy();
        String string40 = onlCgformField.getUpdateBy();
        if (string39 == null ? string40 != null : !string39.equals(string40)) {
            return false;
        }
        Date date = this.getUpdateTime();
        Date date2 = onlCgformField.getUpdateTime();
        if (date == null ? date2 != null : !((Object)date).equals(date2)) {
            return false;
        }
        Date date3 = this.getCreateTime();
        Date date4 = onlCgformField.getCreateTime();
        if (date3 == null ? date4 != null : !((Object)date3).equals(date4)) {
            return false;
        }
        String string41 = this.getCreateBy();
        String string42 = onlCgformField.getCreateBy();
        if (string41 == null ? string42 != null : !string41.equals(string42)) {
            return false;
        }
        String string43 = this.getConverter();
        String string44 = onlCgformField.getConverter();
        if (string43 == null ? string44 != null : !string43.equals(string44)) {
            return false;
        }
        String string45 = this.getQueryConfigFlag();
        String string46 = onlCgformField.getQueryConfigFlag();
        if (string45 == null ? string46 != null : !string45.equals(string46)) {
            return false;
        }
        String string47 = this.getQueryDefVal();
        String string48 = onlCgformField.getQueryDefVal();
        if (string47 == null ? string48 != null : !string47.equals(string48)) {
            return false;
        }
        String string49 = this.getQueryDictText();
        String string50 = onlCgformField.getQueryDictText();
        if (string49 == null ? string50 != null : !string49.equals(string50)) {
            return false;
        }
        String string51 = this.getQueryDictField();
        String string52 = onlCgformField.getQueryDictField();
        if (string51 == null ? string52 != null : !string51.equals(string52)) {
            return false;
        }
        String string53 = this.getQueryDictTable();
        String string54 = onlCgformField.getQueryDictTable();
        if (string53 == null ? string54 != null : !string53.equals(string54)) {
            return false;
        }
        String string55 = this.getQueryShowType();
        String string56 = onlCgformField.getQueryShowType();
        if (string55 == null ? string56 != null : !string55.equals(string56)) {
            return false;
        }
        String string57 = this.getQueryValidType();
        String string58 = onlCgformField.getQueryValidType();
        if (string57 == null ? string58 != null : !string57.equals(string58)) {
            return false;
        }
        String string59 = this.getQueryMustInput();
        String string60 = onlCgformField.getQueryMustInput();
        if (string59 == null ? string60 != null : !string59.equals(string60)) {
            return false;
        }
        String string61 = this.getSortFlag();
        String string62 = onlCgformField.getSortFlag();
        return !(string61 == null ? string62 != null : !string61.equals(string62));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OnlCgformField;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        Integer n3 = this.getDbIsKey();
        n2 = n2 * 59 + (n3 == null ? 43 : ((Object)n3).hashCode());
        Integer n4 = this.getDbIsNull();
        n2 = n2 * 59 + (n4 == null ? 43 : ((Object)n4).hashCode());
        Integer n5 = this.getDbIsPersist();
        n2 = n2 * 59 + (n5 == null ? 43 : ((Object)n5).hashCode());
        Integer n6 = this.getDbLength();
        n2 = n2 * 59 + (n6 == null ? 43 : ((Object)n6).hashCode());
        Integer n7 = this.getDbPointLength();
        n2 = n2 * 59 + (n7 == null ? 43 : ((Object)n7).hashCode());
        Integer n8 = this.getFieldLength();
        n2 = n2 * 59 + (n8 == null ? 43 : ((Object)n8).hashCode());
        Integer n9 = this.getIsQuery();
        n2 = n2 * 59 + (n9 == null ? 43 : ((Object)n9).hashCode());
        Integer n10 = this.getIsShowForm();
        n2 = n2 * 59 + (n10 == null ? 43 : ((Object)n10).hashCode());
        Integer n11 = this.getIsShowList();
        n2 = n2 * 59 + (n11 == null ? 43 : ((Object)n11).hashCode());
        Integer n12 = this.getIsReadOnly();
        n2 = n2 * 59 + (n12 == null ? 43 : ((Object)n12).hashCode());
        Integer n13 = this.getOrderNum();
        n2 = n2 * 59 + (n13 == null ? 43 : ((Object)n13).hashCode());
        String string = this.getId();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getCgformHeadId();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getDbFieldName();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getDbFieldTxt();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getDbFieldNameOld();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        String string6 = this.getDbType();
        n2 = n2 * 59 + (string6 == null ? 43 : string6.hashCode());
        String string7 = this.getDbDefaultVal();
        n2 = n2 * 59 + (string7 == null ? 43 : string7.hashCode());
        String string8 = this.getDictField();
        n2 = n2 * 59 + (string8 == null ? 43 : string8.hashCode());
        String string9 = this.getDictTable();
        n2 = n2 * 59 + (string9 == null ? 43 : string9.hashCode());
        String string10 = this.getDictText();
        n2 = n2 * 59 + (string10 == null ? 43 : string10.hashCode());
        String string11 = this.getFieldShowType();
        n2 = n2 * 59 + (string11 == null ? 43 : string11.hashCode());
        String string12 = this.getFieldHref();
        n2 = n2 * 59 + (string12 == null ? 43 : string12.hashCode());
        String string13 = this.getFieldValidType();
        n2 = n2 * 59 + (string13 == null ? 43 : string13.hashCode());
        String string14 = this.getFieldMustInput();
        n2 = n2 * 59 + (string14 == null ? 43 : string14.hashCode());
        String string15 = this.getFieldExtendJson();
        n2 = n2 * 59 + (string15 == null ? 43 : string15.hashCode());
        String string16 = this.getFieldDefaultValue();
        n2 = n2 * 59 + (string16 == null ? 43 : string16.hashCode());
        String string17 = this.getQueryMode();
        n2 = n2 * 59 + (string17 == null ? 43 : string17.hashCode());
        String string18 = this.getMainTable();
        n2 = n2 * 59 + (string18 == null ? 43 : string18.hashCode());
        String string19 = this.getMainField();
        n2 = n2 * 59 + (string19 == null ? 43 : string19.hashCode());
        String string20 = this.getUpdateBy();
        n2 = n2 * 59 + (string20 == null ? 43 : string20.hashCode());
        Date date = this.getUpdateTime();
        n2 = n2 * 59 + (date == null ? 43 : ((Object)date).hashCode());
        Date date2 = this.getCreateTime();
        n2 = n2 * 59 + (date2 == null ? 43 : ((Object)date2).hashCode());
        String string21 = this.getCreateBy();
        n2 = n2 * 59 + (string21 == null ? 43 : string21.hashCode());
        String string22 = this.getConverter();
        n2 = n2 * 59 + (string22 == null ? 43 : string22.hashCode());
        String string23 = this.getQueryConfigFlag();
        n2 = n2 * 59 + (string23 == null ? 43 : string23.hashCode());
        String string24 = this.getQueryDefVal();
        n2 = n2 * 59 + (string24 == null ? 43 : string24.hashCode());
        String string25 = this.getQueryDictText();
        n2 = n2 * 59 + (string25 == null ? 43 : string25.hashCode());
        String string26 = this.getQueryDictField();
        n2 = n2 * 59 + (string26 == null ? 43 : string26.hashCode());
        String string27 = this.getQueryDictTable();
        n2 = n2 * 59 + (string27 == null ? 43 : string27.hashCode());
        String string28 = this.getQueryShowType();
        n2 = n2 * 59 + (string28 == null ? 43 : string28.hashCode());
        String string29 = this.getQueryValidType();
        n2 = n2 * 59 + (string29 == null ? 43 : string29.hashCode());
        String string30 = this.getQueryMustInput();
        n2 = n2 * 59 + (string30 == null ? 43 : string30.hashCode());
        String string31 = this.getSortFlag();
        n2 = n2 * 59 + (string31 == null ? 43 : string31.hashCode());
        return n2;
    }

    public String toString() {
        return "OnlCgformField(id=" + this.getId() + ", cgformHeadId=" + this.getCgformHeadId() + ", dbFieldName=" + this.getDbFieldName() + ", dbFieldTxt=" + this.getDbFieldTxt() + ", dbFieldNameOld=" + this.getDbFieldNameOld() + ", dbIsKey=" + this.getDbIsKey() + ", dbIsNull=" + this.getDbIsNull() + ", dbIsPersist=" + this.getDbIsPersist() + ", dbType=" + this.getDbType() + ", dbLength=" + this.getDbLength() + ", dbPointLength=" + this.getDbPointLength() + ", dbDefaultVal=" + this.getDbDefaultVal() + ", dictField=" + this.getDictField() + ", dictTable=" + this.getDictTable() + ", dictText=" + this.getDictText() + ", fieldShowType=" + this.getFieldShowType() + ", fieldHref=" + this.getFieldHref() + ", fieldLength=" + this.getFieldLength() + ", fieldValidType=" + this.getFieldValidType() + ", fieldMustInput=" + this.getFieldMustInput() + ", fieldExtendJson=" + this.getFieldExtendJson() + ", fieldDefaultValue=" + this.getFieldDefaultValue() + ", isQuery=" + this.getIsQuery() + ", isShowForm=" + this.getIsShowForm() + ", isShowList=" + this.getIsShowList() + ", isReadOnly=" + this.getIsReadOnly() + ", queryMode=" + this.getQueryMode() + ", mainTable=" + this.getMainTable() + ", mainField=" + this.getMainField() + ", orderNum=" + this.getOrderNum() + ", updateBy=" + this.getUpdateBy() + ", updateTime=" + this.getUpdateTime() + ", createTime=" + this.getCreateTime() + ", createBy=" + this.getCreateBy() + ", converter=" + this.getConverter() + ", queryConfigFlag=" + this.getQueryConfigFlag() + ", queryDefVal=" + this.getQueryDefVal() + ", queryDictText=" + this.getQueryDictText() + ", queryDictField=" + this.getQueryDictField() + ", queryDictTable=" + this.getQueryDictTable() + ", queryShowType=" + this.getQueryShowType() + ", queryValidType=" + this.getQueryValidType() + ", queryMustInput=" + this.getQueryMustInput() + ", sortFlag=" + this.getSortFlag() + ", alias=" + this.getAlias() + ")";
    }
}

