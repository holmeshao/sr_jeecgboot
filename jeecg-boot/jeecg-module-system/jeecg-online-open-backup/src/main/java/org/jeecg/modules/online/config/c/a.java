/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.DbType
 *  org.apache.commons.lang.StringUtils
 *  org.jeecg.common.util.dynamic.db.DbTypeUtils
 */
package org.jeecg.modules.online.config.c;

import com.baomidou.mybatisplus.annotation.DbType;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.dynamic.db.DbTypeUtils;

public class a {
    private String a;
    private String b;
    private String c;
    private int d;
    private String e;
    private String f;
    private String g;
    private int h;
    private String i;
    private String j;
    private String k;
    private String l;

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof a)) {
            return false;
        }
        a a2 = (a)obj;
        if (this.e.contains("date") || this.e.contains("blob") || this.e.contains("text")) {
            return this.c.equals(a2.getColumnName()) && this.i.equals(a2.i) && this.a(this.f, a2.getComment()) && this.a(this.g, a2.getFieldDefault());
        }
        return this.e.equals(a2.getColunmType()) && this.i.equals(a2.i) && this.d == a2.getColumnSize() && this.a(this.f, a2.getComment()) && this.a(this.g, a2.getFieldDefault());
    }

    public boolean a(DbType dbType, a a2) {
        String string = a2.getColunmType();
        if (DbTypeUtils.dbTypeIf((DbType)dbType, (DbType[])new DbType[]{DbType.ORACLE, DbType.ORACLE_12C}) ? "datetime".equalsIgnoreCase(string) && "date".equalsIgnoreCase(this.e) : DbTypeUtils.dbTypeIsSqlServer((DbType)dbType) && "date".equalsIgnoreCase(string) && "datetime".equalsIgnoreCase(this.e)) {
            return true;
        }
        return this.e.equalsIgnoreCase(string);
    }

    public boolean a(Object object, DbType dbType) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof a)) {
            return false;
        }
        a a2 = (a)object;
        if (!this.a(dbType, a2)) {
            return false;
        }
        if (DbTypeUtils.dbTypeIsSqlServer((DbType)dbType)) {
            if (this.e.contains("date") || this.e.contains("blob") || this.e.contains("text")) {
                return this.c.equals(a2.getColumnName()) && this.i.equals(a2.i);
            }
            return this.e.equals(a2.getColunmType()) && this.i.equals(a2.i) && this.d == a2.getColumnSize() && this.h == a2.getDecimalDigits() && this.a(this.g, a2.getFieldDefault());
        }
        if (DbTypeUtils.dbTypeIsPostgre((DbType)dbType)) {
            if (this.e.contains("date") || this.e.contains("blob") || this.e.contains("text")) {
                return this.c.equals(a2.getColumnName()) && this.i.equals(a2.i);
            }
            return this.e.equals(a2.getColunmType()) && this.i.equals(a2.i) && this.d == a2.getColumnSize() && this.h == a2.getDecimalDigits() && this.a(this.g, a2.getFieldDefault());
        }
        if (DbTypeUtils.dbTypeIsOracle((DbType)dbType)) {
            if (this.e.contains("date") || this.e.contains("blob") || this.e.contains("text")) {
                return this.a(dbType, a2) && this.c.equals(a2.getColumnName()) && this.i.equals(a2.i);
            }
            return this.e.equals(a2.getColunmType()) && this.i.equals(a2.i) && this.d == a2.getColumnSize() && this.h == a2.getDecimalDigits() && this.a(this.g, a2.getFieldDefault());
        }
        if (this.e.contains("date") || this.e.contains("blob") || this.e.contains("text")) {
            return this.a(dbType, a2) && this.c.equals(a2.getColumnName()) && this.i.equals(a2.i) && this.a(this.f, a2.getComment()) && this.a(this.g, a2.getFieldDefault());
        }
        return this.e.equals(a2.getColunmType()) && this.i.equals(a2.i) && this.d == a2.getColumnSize() && this.h == a2.getDecimalDigits() && this.a(this.f, a2.getComment()) && this.a(this.g, a2.getFieldDefault());
    }

    public boolean a(a a2) {
        if (a2 == this) {
            return true;
        }
        return this.a(this.f, a2.getComment());
    }

    public boolean b(a a2) {
        if (a2 == this) {
            return true;
        }
        return this.a(this.f, a2.getComment());
    }

    private boolean a(String string, String string2) {
        boolean bl = StringUtils.isNotEmpty((String)string);
        boolean bl2 = StringUtils.isNotEmpty((String)string2);
        if ("".equals(string2)) {
            if (!bl) {
                return true;
            }
            return string.toLowerCase().toString().indexOf("null") >= 0;
        }
        if (bl != bl2) {
            return false;
        }
        if (bl) {
            return string.equals(string2);
        }
        return true;
    }

    public String getColumnName() {
        return this.c;
    }

    public int getColumnSize() {
        return this.d;
    }

    public String getColunmType() {
        return this.e;
    }

    public String getComment() {
        return this.f;
    }

    public int getDecimalDigits() {
        return this.h;
    }

    public String getIsNullable() {
        return this.i;
    }

    public String getOldColumnName() {
        return this.k;
    }

    public int hashCode() {
        return this.d + this.e.hashCode() * this.c.hashCode();
    }

    public void setColumnName(String columnName) {
        this.c = columnName;
    }

    public void setColumnSize(int columnSize) {
        this.d = columnSize;
    }

    public void setColunmType(String colunmType) {
        this.e = colunmType;
    }

    public void setComment(String comment) {
        this.f = comment;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.h = decimalDigits;
    }

    public void setIsNullable(String isNullable) {
        this.i = isNullable;
    }

    public void setOldColumnName(String oldColumnName) {
        this.k = oldColumnName;
    }

    public String toString() {
        return this.c + "," + this.e + "," + this.i + "," + this.d;
    }

    public String getColumnId() {
        return this.b;
    }

    public void setColumnId(String columnId) {
        this.b = columnId;
    }

    public String getTableName() {
        return this.a;
    }

    public void setTableName(String tableName) {
        this.a = tableName;
    }

    public String getFieldDefault() {
        return this.g;
    }

    public void setFieldDefault(String fieldDefault) {
        this.g = fieldDefault;
    }

    public String getPkType() {
        return this.j;
    }

    public void setPkType(String pkType) {
        this.j = pkType;
    }

    public String getRealDbType() {
        return this.l;
    }

    public void setRealDbType(String realDbType) {
        this.l = realDbType;
    }
}

