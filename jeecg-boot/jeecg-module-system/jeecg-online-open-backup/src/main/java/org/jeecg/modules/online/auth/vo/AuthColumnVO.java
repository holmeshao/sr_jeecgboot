/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.auth.vo;

import java.io.Serializable;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;

public class AuthColumnVO
implements Serializable {
    private static final long serialVersionUID = 5445993027926933917L;
    private String id;
    private String cgformId;
    private Integer type = 1;
    private String code;
    private String title;
    private Integer status;
    private boolean listShow;
    private boolean formShow;
    private boolean formEditable;
    private Integer isShowForm;
    private Integer isShowList;
    private String tableName;
    private String tableNameTxt;
    private int switchFlag;
    private Integer dbIsPersist;
    private Boolean isMain;
    private String dbType;
    private String fieldShowType;

    public AuthColumnVO() {
    }

    public AuthColumnVO(OnlCgformField field) {
        this.id = field.getId();
        this.cgformId = field.getCgformHeadId();
        this.code = field.getDbFieldName();
        this.title = field.getDbFieldTxt();
        this.type = 1;
        this.isShowForm = field.getIsShowForm();
        this.isShowList = field.getIsShowList();
        this.dbIsPersist = field.getDbIsPersist();
        this.dbType = field.getDbType();
        this.fieldShowType = field.getFieldShowType();
    }

    public String getId() {
        return this.id;
    }

    public String getCgformId() {
        return this.cgformId;
    }

    public Integer getType() {
        return this.type;
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }

    public Integer getStatus() {
        return this.status;
    }

    public boolean isListShow() {
        return this.listShow;
    }

    public boolean isFormShow() {
        return this.formShow;
    }

    public boolean isFormEditable() {
        return this.formEditable;
    }

    public Integer getIsShowForm() {
        return this.isShowForm;
    }

    public Integer getIsShowList() {
        return this.isShowList;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getTableNameTxt() {
        return this.tableNameTxt;
    }

    public int getSwitchFlag() {
        return this.switchFlag;
    }

    public Integer getDbIsPersist() {
        return this.dbIsPersist;
    }

    public Boolean getIsMain() {
        return this.isMain;
    }

    public String getDbType() {
        return this.dbType;
    }

    public String getFieldShowType() {
        return this.fieldShowType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCgformId(String cgformId) {
        this.cgformId = cgformId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setListShow(boolean listShow) {
        this.listShow = listShow;
    }

    public void setFormShow(boolean formShow) {
        this.formShow = formShow;
    }

    public void setFormEditable(boolean formEditable) {
        this.formEditable = formEditable;
    }

    public void setIsShowForm(Integer isShowForm) {
        this.isShowForm = isShowForm;
    }

    public void setIsShowList(Integer isShowList) {
        this.isShowList = isShowList;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setTableNameTxt(String tableNameTxt) {
        this.tableNameTxt = tableNameTxt;
    }

    public void setSwitchFlag(int switchFlag) {
        this.switchFlag = switchFlag;
    }

    public void setDbIsPersist(Integer dbIsPersist) {
        this.dbIsPersist = dbIsPersist;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public void setFieldShowType(String fieldShowType) {
        this.fieldShowType = fieldShowType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AuthColumnVO)) {
            return false;
        }
        AuthColumnVO authColumnVO = (AuthColumnVO)o;
        if (!authColumnVO.canEqual(this)) {
            return false;
        }
        if (this.isListShow() != authColumnVO.isListShow()) {
            return false;
        }
        if (this.isFormShow() != authColumnVO.isFormShow()) {
            return false;
        }
        if (this.isFormEditable() != authColumnVO.isFormEditable()) {
            return false;
        }
        if (this.getSwitchFlag() != authColumnVO.getSwitchFlag()) {
            return false;
        }
        Integer n = this.getType();
        Integer n2 = authColumnVO.getType();
        if (n == null ? n2 != null : !((Object)n).equals(n2)) {
            return false;
        }
        Integer n3 = this.getStatus();
        Integer n4 = authColumnVO.getStatus();
        if (n3 == null ? n4 != null : !((Object)n3).equals(n4)) {
            return false;
        }
        Integer n5 = this.getIsShowForm();
        Integer n6 = authColumnVO.getIsShowForm();
        if (n5 == null ? n6 != null : !((Object)n5).equals(n6)) {
            return false;
        }
        Integer n7 = this.getIsShowList();
        Integer n8 = authColumnVO.getIsShowList();
        if (n7 == null ? n8 != null : !((Object)n7).equals(n8)) {
            return false;
        }
        Integer n9 = this.getDbIsPersist();
        Integer n10 = authColumnVO.getDbIsPersist();
        if (n9 == null ? n10 != null : !((Object)n9).equals(n10)) {
            return false;
        }
        Boolean bl = this.getIsMain();
        Boolean bl2 = authColumnVO.getIsMain();
        if (bl == null ? bl2 != null : !((Object)bl).equals(bl2)) {
            return false;
        }
        String string = this.getId();
        String string2 = authColumnVO.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getCgformId();
        String string4 = authColumnVO.getCgformId();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getCode();
        String string6 = authColumnVO.getCode();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.getTitle();
        String string8 = authColumnVO.getTitle();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getTableName();
        String string10 = authColumnVO.getTableName();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        String string11 = this.getTableNameTxt();
        String string12 = authColumnVO.getTableNameTxt();
        if (string11 == null ? string12 != null : !string11.equals(string12)) {
            return false;
        }
        String string13 = this.getDbType();
        String string14 = authColumnVO.getDbType();
        if (string13 == null ? string14 != null : !string13.equals(string14)) {
            return false;
        }
        String string15 = this.getFieldShowType();
        String string16 = authColumnVO.getFieldShowType();
        return !(string15 == null ? string16 != null : !string15.equals(string16));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AuthColumnVO;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        n2 = n2 * 59 + (this.isListShow() ? 79 : 97);
        n2 = n2 * 59 + (this.isFormShow() ? 79 : 97);
        n2 = n2 * 59 + (this.isFormEditable() ? 79 : 97);
        n2 = n2 * 59 + this.getSwitchFlag();
        Integer n3 = this.getType();
        n2 = n2 * 59 + (n3 == null ? 43 : ((Object)n3).hashCode());
        Integer n4 = this.getStatus();
        n2 = n2 * 59 + (n4 == null ? 43 : ((Object)n4).hashCode());
        Integer n5 = this.getIsShowForm();
        n2 = n2 * 59 + (n5 == null ? 43 : ((Object)n5).hashCode());
        Integer n6 = this.getIsShowList();
        n2 = n2 * 59 + (n6 == null ? 43 : ((Object)n6).hashCode());
        Integer n7 = this.getDbIsPersist();
        n2 = n2 * 59 + (n7 == null ? 43 : ((Object)n7).hashCode());
        Boolean bl = this.getIsMain();
        n2 = n2 * 59 + (bl == null ? 43 : ((Object)bl).hashCode());
        String string = this.getId();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getCgformId();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getCode();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.getTitle();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getTableName();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        String string6 = this.getTableNameTxt();
        n2 = n2 * 59 + (string6 == null ? 43 : string6.hashCode());
        String string7 = this.getDbType();
        n2 = n2 * 59 + (string7 == null ? 43 : string7.hashCode());
        String string8 = this.getFieldShowType();
        n2 = n2 * 59 + (string8 == null ? 43 : string8.hashCode());
        return n2;
    }

    public String toString() {
        return "AuthColumnVO(id=" + this.getId() + ", cgformId=" + this.getCgformId() + ", type=" + this.getType() + ", code=" + this.getCode() + ", title=" + this.getTitle() + ", status=" + this.getStatus() + ", listShow=" + this.isListShow() + ", formShow=" + this.isFormShow() + ", formEditable=" + this.isFormEditable() + ", isShowForm=" + this.getIsShowForm() + ", isShowList=" + this.getIsShowList() + ", tableName=" + this.getTableName() + ", tableNameTxt=" + this.getTableNameTxt() + ", switchFlag=" + this.getSwitchFlag() + ", dbIsPersist=" + this.getDbIsPersist() + ", isMain=" + this.getIsMain() + ", dbType=" + this.getDbType() + ", fieldShowType=" + this.getFieldShowType() + ")";
    }
}

