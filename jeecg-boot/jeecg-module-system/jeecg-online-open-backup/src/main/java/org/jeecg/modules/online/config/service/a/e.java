/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.jeecg.common.util.oConvertUtils
 */
package org.jeecg.modules.online.config.service.a;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.config.c.a;
import org.jeecg.modules.online.config.service.DbTableHandleI;

public class e
implements DbTableHandleI {
    @Override
    public String getAddColumnSql(a columnMeta) {
        return " ADD  " + this.a(columnMeta) + "";
    }

    @Override
    public String getReNameFieldName(a columnMeta) {
        return "RENAME COLUMN  " + columnMeta.getOldColumnName() + " TO " + columnMeta.getColumnName() + "";
    }

    @Override
    public String getUpdateColumnSql(a cgformcolumnMeta, a datacolumnMeta) {
        return " MODIFY   " + this.a(cgformcolumnMeta, datacolumnMeta) + "";
    }

    @Override
    public String getMatchClassTypeByDataType(String dataType, int digits) {
        String string = "";
        if ("varchar2".equalsIgnoreCase(dataType)) {
            string = "string";
        }
        if ("nvarchar2".equalsIgnoreCase(dataType)) {
            string = "string";
        } else if ("double".equalsIgnoreCase(dataType)) {
            string = "double";
        } else if ("number".equalsIgnoreCase(dataType) && digits == 0) {
            string = "int";
        } else if ("number".equalsIgnoreCase(dataType) && digits != 0) {
            string = "double";
        } else if ("int".equalsIgnoreCase(dataType)) {
            string = "int";
        } else if ("Date".equalsIgnoreCase(dataType)) {
            string = "date";
        } else if ("TIMESTAMP".equalsIgnoreCase(dataType)) {
            string = "datetime";
        } else if ("blob".equalsIgnoreCase(dataType)) {
            string = "blob";
        } else if ("clob".equalsIgnoreCase(dataType)) {
            string = "text";
        }
        return string;
    }

    @Override
    public String dropTableSQL(String tableName) {
        return " DROP TABLE  " + tableName.toLowerCase() + " ";
    }

    @Override
    public String getDropColumnSql(String fieldName) {
        return " DROP COLUMN " + fieldName.toUpperCase() + "";
    }

    private String a(a a2) {
        String string = "";
        if ("string".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " varchar2(" + a2.getColumnSize() + ")";
        } else if ("date".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " date";
        } else if ("datetime".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " date";
        } else if ("int".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " NUMBER(" + a2.getColumnSize() + ")";
        } else if ("double".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " NUMBER(" + a2.getColumnSize() + "," + a2.getDecimalDigits() + ")";
        } else if ("bigdecimal".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " NUMBER(" + a2.getColumnSize() + "," + a2.getDecimalDigits() + ")";
        } else if ("text".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " CLOB ";
        } else if ("blob".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " BLOB ";
        }
        string = string + (StringUtils.isNotEmpty((String)a2.getFieldDefault()) ? " DEFAULT " + a2.getFieldDefault() : " ");
        string = string + ("Y".equals(a2.getIsNullable()) ? " NULL" : " NOT NULL");
        return string;
    }

    private String a(a a2, a a3) {
        String string = "";
        String string2 = "";
        String string3 = a3.getRealDbType();
        if (!a3.getIsNullable().equals(a2.getIsNullable())) {
            String string4 = string2 = "Y".equals(a2.getIsNullable()) ? "NULL" : "NOT NULL";
        }
        if ("string".equalsIgnoreCase(a2.getColunmType())) {
            if (oConvertUtils.isEmpty((Object)string3) || string3.toLowerCase().indexOf("varchar") < 0) {
                string3 = "varchar2";
            }
            string = a2.getColumnName() + " " + string3 + "(" + a2.getColumnSize() + ") ";
        } else if ("date".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " date ";
        } else if ("datetime".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " date ";
        } else if ("int".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " NUMBER(" + a2.getColumnSize() + ") ";
        } else if ("double".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " NUMBER(" + a2.getColumnSize() + "," + a2.getDecimalDigits() + ") ";
        } else if ("bigdecimal".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " NUMBER(" + a2.getColumnSize() + "," + a2.getDecimalDigits() + ") ";
        } else if ("blob".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " BLOB ";
        } else if ("text".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " CLOB ";
        }
        string = string + (StringUtils.isNotEmpty((String)a2.getFieldDefault()) ? " DEFAULT " + a2.getFieldDefault() : " ");
        string = string + string2;
        return string;
    }

    @Override
    public String getCommentSql(a columnMeta) {
        return "COMMENT ON COLUMN " + columnMeta.getTableName() + "." + columnMeta.getColumnName() + " IS '" + columnMeta.getComment() + "'";
    }

    @Override
    public String getSpecialHandle(a cgformcolumnMeta, a datacolumnMeta) {
        return null;
    }

    @Override
    public String dropIndexs(String indexName, String tableName) {
        return "DROP INDEX " + indexName;
    }

    @Override
    public String countIndex(String indexName, String tableName) {
        return "select count(*) from user_ind_columns where index_name=upper('" + indexName + "')";
    }
}

