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
import org.jeecg.modules.online.config.exception.a;
import org.jeecg.modules.online.config.service.DbTableHandleI;

public class f
implements DbTableHandleI {
    @Override
    public String getAddColumnSql(org.jeecg.modules.online.config.c.a columnMeta) {
        return " ADD COLUMN " + this.a(columnMeta) + ";";
    }

    @Override
    public String getReNameFieldName(org.jeecg.modules.online.config.c.a columnMeta) {
        return " RENAME  COLUMN  " + columnMeta.getOldColumnName() + " to " + columnMeta.getColumnName() + ";";
    }

    @Override
    public String getUpdateColumnSql(org.jeecg.modules.online.config.c.a cgformcolumnMeta, org.jeecg.modules.online.config.c.a datacolumnMeta) throws a {
        return this.c(cgformcolumnMeta, datacolumnMeta);
    }

    @Override
    public String getSpecialHandle(org.jeecg.modules.online.config.c.a cgformcolumnMeta, org.jeecg.modules.online.config.c.a datacolumnMeta) {
        String string = this.d(cgformcolumnMeta, datacolumnMeta);
        if (oConvertUtils.isNotEmpty((Object)string)) {
            return "  ALTER  COLUMN   " + string + ";";
        }
        return null;
    }

    @Override
    public String getMatchClassTypeByDataType(String dataType, int digits) {
        String string = "";
        if ("varchar".equalsIgnoreCase(dataType)) {
            string = "string";
        } else if ("double".equalsIgnoreCase(dataType)) {
            string = "double";
        } else if (dataType.contains("int")) {
            string = "int";
        } else if ("Date".equalsIgnoreCase(dataType)) {
            string = "date";
        } else if ("timestamp".equalsIgnoreCase(dataType)) {
            string = "datetime";
        } else if ("bytea".equalsIgnoreCase(dataType)) {
            string = "blob";
        } else if ("text".equalsIgnoreCase(dataType)) {
            string = "text";
        } else if ("decimal".equalsIgnoreCase(dataType)) {
            string = "bigdecimal";
        } else if ("numeric".equalsIgnoreCase(dataType)) {
            string = "bigdecimal";
        }
        return string;
    }

    @Override
    public String dropTableSQL(String tableName) {
        return " DROP TABLE IF EXISTS " + tableName + " ;";
    }

    @Override
    public String getDropColumnSql(String fieldName) {
        return " DROP COLUMN " + fieldName + ";";
    }

    private boolean a(String string, String string2) {
        string = string == null ? "" : string.toLowerCase();
        string2 = string2 == null ? "" : string2.toLowerCase();
        String string3 = "int,double,bigdecimal";
        return string3.indexOf(string) >= 0 && string3.indexOf(string2) >= 0;
    }

    private String a(org.jeecg.modules.online.config.c.a a2, org.jeecg.modules.online.config.c.a a3) {
        String string = this.getDropColumnSql(a3.getColumnName());
        String string2 = a2.getTableName();
        String string3 = String.format("alter table %s", string2);
        String string4 = string3 + this.getAddColumnSql(a2);
        return string + string4;
    }

    private String b(org.jeecg.modules.online.config.c.a a2, org.jeecg.modules.online.config.c.a a3) {
        String string = a2.getIsNullable();
        String string2 = a3.getIsNullable();
        string = string == null ? "Y" : string;
        String string3 = string2 = string2 == null ? "Y" : string2;
        if (!string.equals(string2)) {
            String string4 = a2.getTableName();
            String string5 = a2.getColumnName();
            String string6 = "ALTER table %s ALTER COLUMN %s %s not null;";
            if ("Y".equals(string)) {
                return String.format(string6, string4, string5, "drop");
            }
            if ("N".equals(string)) {
                return String.format(string6, string4, string5, "set");
            }
        }
        return "";
    }

    private String c(org.jeecg.modules.online.config.c.a a2, org.jeecg.modules.online.config.c.a a3) throws a {
        String string = "  ALTER  COLUMN   ";
        if ("string".equalsIgnoreCase(a2.getColunmType())) {
            string = string + a2.getColumnName() + "  type character varying(" + a2.getColumnSize() + ") ";
        } else if ("date".equalsIgnoreCase(a2.getColunmType())) {
            string = a3.getColunmType().toLowerCase().indexOf("date") >= 0 ? string + a2.getColumnName() + "  type date " : this.a(a2, a3);
        } else if ("datetime".equalsIgnoreCase(a2.getColunmType())) {
            string = a3.getColunmType().toLowerCase().indexOf("date") >= 0 ? string + a2.getColumnName() + "  type timestamp " : this.a(a2, a3);
        } else if ("int".equalsIgnoreCase(a2.getColunmType())) {
            string = this.a(a2.getColunmType(), a3.getColunmType()) ? string + a2.getColumnName() + " type int4" : this.a(a2, a3);
        } else if ("double".equalsIgnoreCase(a2.getColunmType())) {
            string = this.a(a2.getColunmType(), a3.getColunmType()) ? string + a2.getColumnName() + " type  numeric(" + a2.getColumnSize() + "," + a2.getDecimalDigits() + ") " : this.a(a2, a3);
        } else if ("BigDecimal".equalsIgnoreCase(a2.getColunmType())) {
            string = this.a(a2.getColunmType(), a3.getColunmType()) ? string + a2.getColumnName() + " type  decimal(" + a2.getColumnSize() + "," + a2.getDecimalDigits() + ") " : this.a(a2, a3);
        } else if ("text".equalsIgnoreCase(a2.getColunmType())) {
            string = string + a2.getColumnName() + " type text ";
        } else if ("blob".equalsIgnoreCase(a2.getColunmType())) {
            throw new a("blob\u7c7b\u578b\u4e0d\u53ef\u4fee\u6539");
        }
        if (StringUtils.isNotEmpty((String)a2.getFieldDefault())) {
            string = string + ",  ALTER  COLUMN   " + a2.getColumnName() + " set DEFAULT " + a2.getFieldDefault() + " ";
        } else if (StringUtils.isNotEmpty((String)a3.getFieldDefault())) {
            string = string + ",  ALTER  COLUMN   " + a2.getColumnName() + " DROP DEFAULT ";
        }
        if (!string.endsWith(";")) {
            string = string + ";";
        }
        String string2 = this.b(a2, a3);
        string = string + string2;
        return string;
    }

    private String d(org.jeecg.modules.online.config.c.a a2, org.jeecg.modules.online.config.c.a a3) {
        String string = "";
        if (!a2.a(a3)) {
            if ("string".equalsIgnoreCase(a2.getColunmType())) {
                string = a2.getColumnName();
                string = string + (StringUtils.isNotEmpty((String)a2.getFieldDefault()) ? " SET DEFAULT " + a2.getFieldDefault() : " DROP DEFAULT");
            } else if ("date".equalsIgnoreCase(a2.getColunmType()) || "datetime".equalsIgnoreCase(a2.getColunmType())) {
                string = a2.getColumnName();
                string = string + (StringUtils.isNotEmpty((String)a2.getFieldDefault()) ? " SET DEFAULT " + a2.getFieldDefault() : " DROP DEFAULT");
            } else if ("int".equalsIgnoreCase(a2.getColunmType())) {
                string = a2.getColumnName();
                string = string + (StringUtils.isNotEmpty((String)a2.getFieldDefault()) ? " SET DEFAULT " + a2.getFieldDefault() : " DROP DEFAULT");
            } else if ("double".equalsIgnoreCase(a2.getColunmType())) {
                string = a2.getColumnName();
                string = string + (StringUtils.isNotEmpty((String)a2.getFieldDefault()) ? " SET DEFAULT " + a2.getFieldDefault() : " DROP DEFAULT");
            } else if ("bigdecimal".equalsIgnoreCase(a2.getColunmType())) {
                string = a2.getColumnName();
                string = string + (StringUtils.isNotEmpty((String)a2.getFieldDefault()) ? " SET DEFAULT " + a2.getFieldDefault() : " DROP DEFAULT");
            } else if ("text".equalsIgnoreCase(a2.getColunmType())) {
                string = a2.getColumnName();
                string = string + (StringUtils.isNotEmpty((String)a2.getFieldDefault()) ? " SET DEFAULT " + a2.getFieldDefault() : " DROP DEFAULT");
            }
        }
        return string;
    }

    private String a(org.jeecg.modules.online.config.c.a a2) {
        String string = "";
        if ("string".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " character varying(" + a2.getColumnSize() + ") ";
        } else if ("date".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " date ";
        } else if ("datetime".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " timestamp ";
        } else if ("int".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " int4";
        } else if ("double".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " numeric(" + a2.getColumnSize() + "," + a2.getDecimalDigits() + ") ";
        } else if ("bigdecimal".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " decimal(" + a2.getColumnSize() + "," + a2.getDecimalDigits() + ") ";
        } else if ("blob".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " bytea ";
        } else if ("text".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " text ";
        }
        string = string + (StringUtils.isNotEmpty((String)a2.getFieldDefault()) ? " DEFAULT " + a2.getFieldDefault() : " ");
        if ("N".equals(a2.getIsNullable())) {
            string = string + " NOT NULL ";
        }
        return string;
    }

    private String b(org.jeecg.modules.online.config.c.a a2) {
        String string = "";
        if ("string".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " character varying(" + a2.getColumnSize() + ") ";
        } else if ("date".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " date ";
        } else if ("datetime".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " timestamp ";
        } else if ("int".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " int(" + a2.getColumnSize() + ") ";
        } else if ("double".equalsIgnoreCase(a2.getColunmType())) {
            string = a2.getColumnName() + " numeric(" + a2.getColumnSize() + "," + a2.getDecimalDigits() + ") ";
        }
        return string;
    }

    @Override
    public String getCommentSql(org.jeecg.modules.online.config.c.a columnMeta) {
        return "COMMENT ON COLUMN " + columnMeta.getTableName() + "." + columnMeta.getColumnName() + " IS '" + columnMeta.getComment() + "'";
    }

    @Override
    public String dropIndexs(String indexName, String tableName) {
        return "DROP INDEX " + indexName;
    }

    @Override
    public String countIndex(String indexName, String tableName) {
        return "SELECT count(*) FROM pg_indexes WHERE indexname = '" + indexName + "' and tablename = '" + tableName + "'";
    }
}

