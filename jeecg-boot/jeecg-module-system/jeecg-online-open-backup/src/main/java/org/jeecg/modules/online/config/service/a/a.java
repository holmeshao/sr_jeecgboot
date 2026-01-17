/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jeecg.common.util.oConvertUtils
 */
package org.jeecg.modules.online.config.service.a;

import java.util.List;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.config.service.DbTableHandleI;

public class a
implements DbTableHandleI {
    @Override
    public String getAddColumnSql(org.jeecg.modules.online.config.c.a columnMeta) {
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
        switch (string) {
            case "varchar": {
                string2 = "string";
                break;
            }
            case "date": 
            case "time": {
                string2 = "date";
                break;
            }
            case "timestamp": {
                string2 = "datetime";
                break;
            }
            case "integer": {
                string2 = "int";
                break;
            }
            case "double": {
                string2 = "double";
                break;
            }
            case "decimal": {
                string2 = "bigdecimal";
                break;
            }
            case "long varchar": {
                string2 = "text";
                break;
            }
            case "blob": {
                string2 = "blob";
                break;
            }
            default: {
                string2 = "string";
            }
        }
        return string2;
    }

    @Override
    public String dropTableSQL(String tableName) {
        return " DROP TABLE  " + tableName.toLowerCase() + " ";
    }

    @Override
    public String getDropColumnSql(String fieldName) {
        return " DROP COLUMN " + fieldName.toUpperCase() + "";
    }

    private String a(org.jeecg.modules.online.config.c.a a2) {
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
                string2 = String.format("DECIMAL(%s, %s)", a2.getColumnSize(), a2.getDecimalDigits());
                break;
            }
            case "text": {
                string2 = "LONG VARCHAR";
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
    public String getReNameFieldName(org.jeecg.modules.online.config.c.a columnMeta) {
        return "RENAME COLUMN  " + columnMeta.getOldColumnName() + " TO " + columnMeta.getColumnName() + "";
    }

    @Override
    public String getCommentSql(org.jeecg.modules.online.config.c.a columnMeta) {
        return "COMMENT ON COLUMN " + columnMeta.getTableName() + "." + columnMeta.getColumnName() + " IS '" + columnMeta.getComment() + "'";
    }

    @Override
    public String getUpdateColumnSql(org.jeecg.modules.online.config.c.a cgformcolumnMeta, org.jeecg.modules.online.config.c.a datacolumnMeta) {
        return null;
    }

    @Override
    public String getSpecialHandle(org.jeecg.modules.online.config.c.a cgformcolumnMeta, org.jeecg.modules.online.config.c.a datacolumnMeta) {
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
        String[] stringArray = new String[]{"blob", "text", "double", "int", "date"};
        boolean bl = false;
        for (int i2 = 0; i2 < stringArray.length; ++i2) {
            if (!stringArray[i2].equals(string)) continue;
            bl = true;
            break;
        }
        return bl;
    }

    @Override
    public void handleUpdateMultiSql(org.jeecg.modules.online.config.c.a meta, org.jeecg.modules.online.config.c.a config, String tableName, List<String> sqlList) {
        String string;
        String string2;
        String string3;
        String string4 = config.getColumnName();
        String string5 = meta.getColunmType();
        if (!(string5.equals(string3 = config.getColunmType()) && meta.getColumnSize() == config.getColumnSize() && meta.getDecimalDigits() == config.getDecimalDigits() || string5.equals(string3) && this.a(string3))) {
            string2 = this.a(config);
            sqlList.add("alter table " + tableName + " alter column " + string4 + " set data type " + string2);
        }
        if ("Y".equals(config.getIsNullable()) && !config.getIsNullable().equals(meta.getIsNullable())) {
            string2 = String.format("alter table %s alter column %s drop not null", tableName, string4);
            sqlList.add(string2);
        }
        if ("N".equals(config.getIsNullable()) && !config.getIsNullable().equals(meta.getIsNullable())) {
            string2 = String.format("alter table %s alter column %s set not null", tableName, string4);
            sqlList.add(string2);
        }
        string2 = meta.getFieldDefault();
        String string6 = config.getFieldDefault();
        if (!(oConvertUtils.isEmpty((Object)string2) && oConvertUtils.isEmpty((Object)string6) || string6.equals(string2))) {
            string = oConvertUtils.isEmpty((Object)string6) ? "NULL" : string6;
            String string7 = String.format("alter table %s alter column %s set default %s", tableName, string4, string);
            sqlList.add(string7);
        }
        if (!meta.b(config)) {
            string = String.format("COMMENT ON COLUMN %s.%s IS '%s'", tableName, string4, config.getComment());
            sqlList.add(string);
        }
    }
}

