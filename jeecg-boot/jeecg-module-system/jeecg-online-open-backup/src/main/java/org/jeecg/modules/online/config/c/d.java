/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.druid.filter.config.ConfigTools
 *  com.baomidou.mybatisplus.annotation.DbType
 *  com.baomidou.mybatisplus.extension.toolkit.JdbcUtils
 *  org.jeecg.common.util.CommonUtils
 *  org.jeecg.common.util.SpringContextUtils
 *  org.jeecg.common.util.dynamic.db.DbTypeUtils
 *  org.jeecg.common.util.oConvertUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.jdbc.datasource.DriverManagerDataSource
 */
package org.jeecg.modules.online.config.c;

import com.alibaba.druid.filter.config.ConfigTools;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.dynamic.db.DbTypeUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.config.model.b;
import org.jeecg.modules.online.config.service.DbTableHandleI;
import org.jeecg.modules.online.config.service.a.a;
import org.jeecg.modules.online.config.service.a.c;
import org.jeecg.modules.online.config.service.a.e;
import org.jeecg.modules.online.config.service.a.f;
import org.jeecg.modules.online.config.service.a.g;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class d {
    private static final Logger b = LoggerFactory.getLogger(d.class);
    public static String a = "";

    public static DbTableHandleI getTableHandle() throws SQLException, org.jeecg.modules.online.config.exception.a {
        return d.a(null);
    }

    public static DbTableHandleI a(b b2) throws SQLException, org.jeecg.modules.online.config.exception.a {
        DbTableHandleI dbTableHandleI = null;
        DbType dbType = d.c(b2);
        String string = DbTypeUtils.getDbTypeString((DbType)dbType);
        if (DbType.DM.equals((Object)dbType)) {
            return new org.jeecg.modules.online.config.service.a.b();
        }
        switch (string) {
            case "MYSQL": {
                dbTableHandleI = new org.jeecg.modules.online.config.service.a.d();
                break;
            }
            case "MARIADB": {
                dbTableHandleI = new org.jeecg.modules.online.config.service.a.d();
                break;
            }
            case "ORACLE": {
                dbTableHandleI = new e();
                break;
            }
            case "DM": {
                dbTableHandleI = new org.jeecg.modules.online.config.service.a.b();
                break;
            }
            case "SQLSERVER": {
                dbTableHandleI = new g();
                break;
            }
            case "POSTGRESQL": {
                dbTableHandleI = new f();
                break;
            }
            case "DB2": {
                dbTableHandleI = new a();
                break;
            }
            case "HSQL": {
                dbTableHandleI = new c();
                break;
            }
            default: {
                dbTableHandleI = new org.jeecg.modules.online.config.service.a.d();
            }
        }
        return dbTableHandleI;
    }

    public static Connection getConnection() throws SQLException {
        DataSource dataSource = (DataSource)SpringContextUtils.getApplicationContext().getBean(DataSource.class);
        return dataSource.getConnection();
    }

    public static String getDatabaseType() throws SQLException, org.jeecg.modules.online.config.exception.a {
        if (oConvertUtils.isNotEmpty((Object)a)) {
            return a;
        }
        DataSource dataSource = (DataSource)SpringContextUtils.getApplicationContext().getBean(DataSource.class);
        return d.a(dataSource);
    }

    public static boolean a() {
        try {
            return "ORACLE".equals(d.getDatabaseType());
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
        catch (org.jeecg.modules.online.config.exception.a a2) {
            a2.printStackTrace();
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String a(DataSource dataSource) throws SQLException, org.jeecg.modules.online.config.exception.a {
        if ("".equals(a)) {
            Connection connection = dataSource.getConnection();
            try {
                DatabaseMetaData databaseMetaData = connection.getMetaData();
                String string = databaseMetaData.getDatabaseProductName().toLowerCase();
                if (string.indexOf("mysql") >= 0) {
                    a = "MYSQL";
                } else if (string.indexOf("oracle") >= 0) {
                    a = "ORACLE";
                } else if (string.indexOf("dm") >= 0) {
                    a = "DM";
                } else if (string.indexOf("sqlserver") >= 0 || string.indexOf("sql server") >= 0) {
                    a = "SQLSERVER";
                } else if (string.indexOf("postgresql") >= 0 || string.indexOf("kingbasees") >= 0) {
                    a = "POSTGRESQL";
                } else if (string.indexOf("mariadb") >= 0) {
                    a = "MARIADB";
                } else {
                    b.error("\u6570\u636e\u5e93\u7c7b\u578b:[" + string + "]\u4e0d\u8bc6\u522b!");
                }
            }
            catch (Exception exception) {
                b.error(exception.getMessage(), (Throwable)exception);
            }
            finally {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            }
        }
        return a;
    }

    public static String a(Connection connection) throws SQLException, org.jeecg.modules.online.config.exception.a {
        if ("".equals(a)) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String string = databaseMetaData.getDatabaseProductName().toLowerCase();
            if (string.indexOf("mysql") >= 0) {
                a = "MYSQL";
            } else if (string.indexOf("oracle") >= 0) {
                a = "ORACLE";
            } else if (string.indexOf("sqlserver") >= 0 || string.indexOf("sql server") >= 0) {
                a = "SQLSERVER";
            } else if (string.indexOf("postgresql") >= 0) {
                a = "POSTGRESQL";
            } else if (string.indexOf("mariadb") >= 0) {
                a = "MARIADB";
            } else {
                b.error("\u6570\u636e\u5e93\u7c7b\u578b:[" + string + "]\u4e0d\u8bc6\u522b!");
            }
        }
        return a;
    }

    public static String a(String string, String string2) {
        switch (string2) {
            case "ORACLE": 
            case "DB2": {
                return string.toUpperCase();
            }
            case "POSTGRESQL": {
                return string.toLowerCase();
            }
        }
        return string;
    }

    public static Boolean a(String string) {
        return d.a(string, null);
    }

    public static Boolean a(String string, b b2) {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            Object object;
            String[] stringArray = new String[]{"TABLE"};
            connection = b2 == null ? d.getConnection() : d.b(b2);
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            DbType dbType = d.c(b2);
            String string2 = DbTypeUtils.getDbTypeString((DbType)dbType);
            String string3 = d.a(string, string2);
            String string4 = null;
            if (b2 != null) {
                string4 = b2.getUsername();
            } else {
                object = (b)SpringContextUtils.getBean(b.class);
                string4 = ((b)object).getUsername();
            }
            if (DbTypeUtils.dbTypeIsOracle((DbType)dbType) || DbType.DB2.equals((Object)dbType)) {
                String string5 = string4 = string4 != null ? string4.toUpperCase() : null;
            }
            if ((resultSet = DbTypeUtils.dbTypeIsSqlServer((DbType)dbType) ? databaseMetaData.getTables(connection.getCatalog(), null, string3, stringArray) : (DbTypeUtils.dbTypeIsPostgre((DbType)dbType) ? databaseMetaData.getTables(connection.getCatalog(), d.b(connection), string3, stringArray) : (DbType.HSQL.equals((Object)dbType) ? databaseMetaData.getTables(connection.getCatalog(), "PUBLIC", string3.toUpperCase(), stringArray) : databaseMetaData.getTables(connection.getCatalog(), string4, string3, stringArray)))).next()) {
                object = true;
                return object;
            }
            object = false;
            return object;
        }
        catch (SQLException sQLException) {
            throw new RuntimeException(sQLException.getMessage(), sQLException);
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
            catch (SQLException sQLException) {
                b.error(sQLException.getMessage(), (Throwable)sQLException);
            }
        }
    }

    public static String b(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT current_schema()");
            if (resultSet.next()) {
                String string = resultSet.getString(1);
                return string;
            }
        }
        catch (SQLException sQLException) {
            return "public";
        }
        return "public";
    }

    public static Map<String, Object> a(List<Map<String, Object>> list) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        for (int i2 = 0; i2 < list.size(); ++i2) {
            hashMap.put(list.get(i2).get("column_name").toString(), list.get(i2));
        }
        return hashMap;
    }

    public static String getDialect() throws SQLException, org.jeecg.modules.online.config.exception.a {
        String string = d.getDatabaseType();
        return d.b(string);
    }

    public static String b(String string) throws SQLException, org.jeecg.modules.online.config.exception.a {
        String string2 = "org.hibernate.dialect.MySQL5InnoDBDialect";
        switch (string) {
            case "SQLSERVER": {
                string2 = "org.hibernate.dialect.SQLServerDialect";
                break;
            }
            case "POSTGRESQL": 
            case "KINGBASEES": {
                string2 = "org.hibernate.dialect.PostgreSQLDialect";
                break;
            }
            case "ORACLE": {
                string2 = "org.hibernate.dialect.OracleDialect";
                break;
            }
            case "DM": {
                string2 = "org.hibernate.dialect.DmDialect";
                break;
            }
        }
        return string2;
    }

    public static String c(String string) {
        return string;
    }

    public static Connection b(b b2) throws SQLException {
        org.jeecg.modules.online.config.model.d d2;
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(b2.getDriverClassName());
        driverManagerDataSource.setUrl(b2.getUrl());
        driverManagerDataSource.setUsername(b2.getUsername());
        String string = b2.getPassword();
        if (string != null && (d2 = b2.getDruid()) != null && oConvertUtils.isNotEmpty((Object)d2.getPublicKey())) {
            b.info("dbconfig.getDruid().getPublicKey() = {}", (Object)d2.getPublicKey());
            try {
                String string2 = ConfigTools.decrypt((String)d2.getPublicKey(), (String)string);
                b.debug("\u89e3\u5bc6\u5bc6\u7801 decryptPassword = {}", (Object)string2);
                string = string2;
            }
            catch (Exception exception) {
                b.error(exception.getMessage(), (Throwable)exception);
            }
        }
        driverManagerDataSource.setPassword(string);
        return driverManagerDataSource.getConnection();
    }

    public static DbType c(b b2) {
        if (b2 == null) {
            return CommonUtils.getDatabaseTypeEnum();
        }
        return JdbcUtils.getDbType((String)b2.getUrl());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ResourceBundle d(String string) {
        PropertyResourceBundle propertyResourceBundle = null;
        BufferedInputStream bufferedInputStream = null;
        String string2 = System.getProperty("user.dir") + File.separator + "config" + File.separator + string + ".properties";
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(string2));
            propertyResourceBundle = new PropertyResourceBundle(bufferedInputStream);
            bufferedInputStream.close();
            if (propertyResourceBundle != null) {
                // empty if block
            }
        }
        catch (IOException iOException) {
        }
        finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }
        }
        return propertyResourceBundle;
    }
}

