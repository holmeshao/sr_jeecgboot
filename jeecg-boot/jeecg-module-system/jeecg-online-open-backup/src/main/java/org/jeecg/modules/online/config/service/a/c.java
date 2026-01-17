/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jeecg.common.util.oConvertUtils
 */
package org.jeecg.modules.online.config.service.a;

import java.util.List;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.config.c.a;
import org.jeecg.modules.online.config.service.DbTableHandleI;

public class c
implements DbTableHandleI {
    @Override
    public String getAddColumnSql(a columnMeta) {
        String string = columnMeta.getColumnName();
        String string2 = this.a(columnMeta);
        String string3 = " ADD " + string + " " + string2;
        if (oConvertUtils.isNotEmpty((Object)columnMeta.getFieldDefault())) {
            string3 = string3 + " DEFAULT " + columnMeta.getFieldDefault();
            if (!"Y".equals(columnMeta.getIsNullable())) {
                string3 = string3 + " NOT NULL";
            }
        }
        return string3;
    }

    @Override
    public String getMatchClassTypeByDataType(String dataType, int digits) {
        String string = dataType.toLowerCase();
        String string2 = "";
        string2 = "date".equals(string) || "time".equals(string) ? "date" : ("timestamp".equals(string) ? "datetime" : ("numeric".equals(string) ? "bigdecimal" : ("double".equals(string) ? "double" : ("integer".equals(string) ? "int" : ("clob".equals(string) ? "text" : ("blob".equals(string) ? "blob" : "string"))))));
        return string2;
    }

    @Override
    public String dropTableSQL(String tableName) {
        return " DROP TABLE  " + tableName.toUpperCase() + " ";
    }

    @Override
    public String getDropColumnSql(String fieldName) {
        return " DROP COLUMN " + fieldName.toUpperCase() + "";
    }

    private String a(a a2) {
        String string = a2.getColunmType().toLowerCase();
        String string2 = "";
        switch (string) {
            case "string": {
                string2 = String.format("varchar(%s)", a2.getColumnSize());
                break;
            }
            case "date": {
                string2 = "DATE";
                break;
            }
            case "datetime": {
                string2 = "TIMESTAMP";
                break;
            }
            case "int": {
                string2 = "INTEGER";
                break;
            }
            case "double": {
                string2 = "double";
                break;
            }
            case "bigdecimal": {
                string2 = String.format("NUMERIC(%s, %s)", a2.getColumnSize(), a2.getDecimalDigits());
                break;
            }
            case "text": {
                string2 = "CLOB";
                break;
            }
            case "blob": {
                string2 = "BLOB";
                break;
            }
            default: {
                string2 = String.format("varchar(%s)", a2.getColumnSize());
            }
        }
        return string2;
    }

    @Override
    public String getReNameFieldName(a columnMeta) {
        String string = this.a(columnMeta);
        return " change " + columnMeta.getOldColumnName() + " " + columnMeta.getColumnName() + " " + string;
    }

    @Override
    public String getCommentSql(a columnMeta) {
        return "COMMENT ON COLUMN " + columnMeta.getTableName() + "." + columnMeta.getColumnName() + " IS '" + columnMeta.getComment() + "'";
    }

    @Override
    public String getUpdateColumnSql(a cgformcolumnMeta, a datacolumnMeta) {
        return null;
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

    private boolean a(String string) {
        String[] stringArray = new String[]{"clob", "blob", "text", "date", "double", "int"};
        boolean bl = false;
        for (int i2 = 0; i2 < stringArray.length; ++i2) {
            if (!stringArray[i2].equals(string)) continue;
            bl = true;
            break;
        }
        return bl;
    }

    @Override
    public void handleUpdateMultiSql(a meta, a config, String tableName, List<String> sqlList) {
        String string;
        String string2 = config.getColumnName();
        String string3 = meta.getColunmType();
        String string4 = config.getColunmType();
        boolean bl = false;
        if (!(string3.equals(string4) && meta.getColumnSize() == config.getColumnSize() && meta.getDecimalDigits() == config.getDecimalDigits() || string3.equals(string4) && this.a(string4))) {
            bl = true;
        }
        if ("Y".equals(config.getIsNullable()) && !config.getIsNullable().equals(meta.getIsNullable())) {
            bl = true;
        }
        if ("N".equals(config.getIsNullable()) && !config.getIsNullable().equals(meta.getIsNullable())) {
            bl = true;
        }
        String string5 = meta.getFieldDefault();
        String string6 = config.getFieldDefault();
        if (!(oConvertUtils.isEmpty((Object)string5) && oConvertUtils.isEmpty((Object)string6) || string6.equals(string5))) {
            bl = true;
        }
        if (bl) {
            string = String.format("alter table %s", tableName);
            String string7 = string + this.getDropColumnSql(meta.getColumnName());
            sqlList.add(string7);
            String string8 = string + this.getAddColumnSql(config);
            sqlList.add(string8);
        }
        if (!meta.b(config)) {
            string = String.format("COMMENT ON COLUMN %s.%s IS '%s'", tableName, string2, config.getComment());
            sqlList.add(string);
        }
    }
}

