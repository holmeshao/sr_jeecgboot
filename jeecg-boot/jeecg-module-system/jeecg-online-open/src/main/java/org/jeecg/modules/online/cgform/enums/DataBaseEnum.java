/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.jeecg.modules.online.cgform.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum DataBaseEnum {
    MYSQL("MYSQL", "1"),
    MYSQL_57("MYSQL5.7+", "4"),
    ORACLE("ORACLE", "2"),
    SQLSERVER("SQLSERVER", "3"),
    MARIADB("MARIADB", "5"),
    POSTGRESQL("POSTGRESQL", "6"),
    DA_MENG("DA_MENG", "7"),
    REN_DA_JIN_CANG("REN_DA_JIN_CANG", "8"),
    SHEN_TONG("SHEN_TONG", "9"),
    SQL_LITE("SQL_LITE", "10");

    private static final Logger log;
    private String name;
    private String value;

    private DataBaseEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static String getDataBaseNameByValue(String value) {
        for (DataBaseEnum dataBaseEnum : DataBaseEnum.values()) {
            if (!dataBaseEnum.value.equals(value)) continue;
            return dataBaseEnum.name;
        }
        log.warn("\u4e0d\u8bc6\u522b\u7684\u6570\u636e\u5e93\u7c7b\u578b:{}\uff0c\u5df2\u81ea\u52a8\u8f6c\u4e3a\u9ed8\u8ba4\u6570\u636e\u5e93\u7c7b\u578b:{}", (Object)value, (Object)DataBaseEnum.MYSQL.name);
        return DataBaseEnum.MYSQL.name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    static {
        log = LoggerFactory.getLogger(DataBaseEnum.class);
    }
}

