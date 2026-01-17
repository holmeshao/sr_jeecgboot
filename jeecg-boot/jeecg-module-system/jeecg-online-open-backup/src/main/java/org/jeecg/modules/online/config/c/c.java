/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.druid.filter.config.ConfigTools
 *  com.baomidou.mybatisplus.annotation.DbType
 *  freemarker.template.TemplateException
 *  org.apache.commons.lang.StringUtils
 *  org.hibernate.HibernateException
 *  org.hibernate.Session
 *  org.hibernate.boot.Metadata
 *  org.hibernate.boot.MetadataSources
 *  org.hibernate.boot.registry.StandardServiceRegistryBuilder
 *  org.hibernate.service.ServiceRegistry
 *  org.hibernate.tool.hbm2ddl.SchemaExport
 *  org.hibernate.tool.schema.TargetType
 *  org.jeecg.common.util.SqlInjectionUtil
 *  org.jeecg.common.util.dynamic.db.DbTypeUtils
 *  org.jeecg.common.util.oConvertUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.jeecg.modules.online.config.c;

import com.alibaba.druid.filter.config.ConfigTools;
import com.baomidou.mybatisplus.annotation.DbType;
import freemarker.template.TemplateException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.dynamic.db.DbTypeUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.config.c.a;
import org.jeecg.modules.online.config.c.d;
import org.jeecg.modules.online.config.c.g;
import org.jeecg.modules.online.config.model.b;
import org.jeecg.modules.online.config.service.DbTableHandleI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class c {
    private static final Logger a = LoggerFactory.getLogger(c.class);
    private static final String b = "org/jeecg/modules/online/config/engine/tableTemplate.ftl";
    private static DbTableHandleI c;
    private static ServiceRegistry d;

    public c(b b2) throws SQLException, org.jeecg.modules.online.config.exception.a {
        c = org.jeecg.modules.online.config.c.d.a(b2);
    }

    public c() throws SQLException, org.jeecg.modules.online.config.exception.a {
        c = org.jeecg.modules.online.config.c.d.a(null);
    }

    public static void a(org.jeecg.modules.online.config.model.a a2) throws IOException, TemplateException, HibernateException, SQLException, org.jeecg.modules.online.config.exception.a {
        Object exception;
        Object object;
        Object object4;
        DbType dbType = org.jeecg.modules.online.config.c.d.c(a2.getDbConfig());
        if (DbTypeUtils.dbTypeIsOracle((DbType)dbType)) {
            object4 = new ArrayList();
            for (OnlCgformField object32 : a2.getColumns()) {
                if ("int".equals(object32.getDbType())) {
                    object32.setDbType("double");
                    object32.setDbPointLength(0);
                }
                object4.add(object32);
            }
            a2.setColumns((List<OnlCgformField>)object4);
        }
        object4 = g.a(b, org.jeecg.modules.online.config.c.c.a(a2, dbType));
        a.debug("xml\uff1a{}", object4);
        HashMap hashMap = new HashMap(5);
        b b2 = a2.getDbConfig();
        if (d == null) {
            hashMap.put("hibernate.connection.driver_class", b2.getDriverClassName());
            hashMap.put("hibernate.connection.url", b2.getUrl());
            hashMap.put("hibernate.connection.username", b2.getUsername());
            object = b2.getPassword();
            if (object != null) {
                if (b2.getDruid() != null && oConvertUtils.isNotEmpty((Object)b2.getDruid().getPublicKey())) {
                    try {
                        exception = ConfigTools.decrypt((String)b2.getDruid().getPublicKey(), (String)object);
                        hashMap.put("hibernate.connection.password", exception);
                    }
                    catch (Exception exception2) {
                        exception2.printStackTrace();
                    }
                } else {
                    hashMap.put("hibernate.connection.password", object);
                }
            }
            hashMap.put("hibernate.show_sql", true);
            hashMap.put("hibernate.format_sql", true);
            hashMap.put("hibernate.temp.use_jdbc_metadata_defaults", false);
            hashMap.put("hibernate.dialect", DbTypeUtils.getDbDialect((DbType)dbType));
            hashMap.put("hibernate.hbm2ddl.auto", "create");
            hashMap.put("hibernate.connection.autocommit", false);
            hashMap.put("hibernate.current_session_context_class", "thread");
            d = new StandardServiceRegistryBuilder().applySettings((Map)hashMap).build();
        }
        object = new MetadataSources(d);
        exception = new ByteArrayInputStream(((String)object4).getBytes("utf-8"));
        object.addInputStream((InputStream)exception);
        Metadata metadata = object.buildMetadata();
        SchemaExport schemaExport = new SchemaExport();
        schemaExport.create(EnumSet.of(TargetType.DATABASE), metadata);
        ((InputStream)exception).close();
        List list = schemaExport.getExceptions();
        for (Exception exception3 : list) {
            Object object2;
            if ("java.sql.SQLSyntaxErrorException".equals(exception3.getCause().getClass().getName())) {
                object2 = (SQLSyntaxErrorException)exception3.getCause();
                if ("42000".equals(((SQLException)object2).getSQLState())) {
                    if (1064 != ((SQLException)object2).getErrorCode() && 903 != ((SQLException)object2).getErrorCode()) continue;
                    a.error(((Throwable)object2).getMessage());
                    throw new org.jeecg.modules.online.config.exception.a("\u8bf7\u786e\u8ba4\u8868\u540d\u662f\u5426\u4e3a\u5173\u952e\u5b57\u3002");
                }
            } else {
                if ("com.microsoft.sqlserver.jdbc.SQLServerException".equals(exception3.getCause().getClass().getName())) {
                    if (exception3.getCause().toString().indexOf("Incorrect syntax near the keyword") != -1) {
                        exception3.printStackTrace();
                        throw new org.jeecg.modules.online.config.exception.a(exception3.getCause().getMessage());
                    }
                    a.error(exception3.getMessage());
                    continue;
                }
                if ((DbType.DM.equals((Object)dbType) || DbType.DB2.equals((Object)dbType)) && (object2 = exception3.getMessage()) != null && ((String)object2).indexOf("Error executing DDL \"drop table") >= 0) {
                    a.error((String)object2);
                    continue;
                }
            }
            throw new org.jeecg.modules.online.config.exception.a(exception3.getMessage());
        }
    }

    public List<String> b(org.jeecg.modules.online.config.model.a a2) throws org.jeecg.modules.online.config.exception.a, SQLException {
        DbType dbType = org.jeecg.modules.online.config.c.d.c(a2.getDbConfig());
        String string = DbTypeUtils.getDbTypeString((DbType)dbType);
        String string2 = org.jeecg.modules.online.config.c.d.a(a2.getTableName(), string);
        String string3 = "alter table  " + string2 + " ";
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            Map<String, a> map = this.a(null, string2, a2.getDbConfig());
            Map<String, a> map2 = this.c(a2);
            Map<String, String> map3 = this.a(a2.getColumns());
            for (String string4 : map2.keySet()) {
                Object object;
                a a3;
                if ("id".equalsIgnoreCase(string4)) continue;
                if (!map.containsKey(string4)) {
                    a3 = map2.get(string4);
                    object = map3.get(string4);
                    if (map3.containsKey(string4) && map.containsKey(object)) {
                        String string5;
                        a a4 = map.get(object);
                        if (DbType.HSQL.equals((Object)dbType)) {
                            this.a(a4, a3, string2, arrayList);
                        } else {
                            string5 = c.getReNameFieldName(a3);
                            if (DbTypeUtils.dbTypeIsSqlServer((DbType)dbType)) {
                                arrayList.add(string5);
                            } else {
                                arrayList.add(string3 + string5);
                            }
                            map.put(string4, map.remove(object));
                            if (DbType.DB2.equals((Object)dbType)) {
                                this.a(a4, a3, string2, arrayList);
                            } else {
                                if (!a4.equals(a3)) {
                                    String string6;
                                    arrayList.add(string3 + this.a(a3, a4));
                                    if (DbTypeUtils.dbTypeIsPostgre((DbType)dbType) && oConvertUtils.isNotEmpty((Object)(string6 = this.b(a3, a4)))) {
                                        arrayList.add(string3 + string6);
                                    }
                                }
                                if (!DbTypeUtils.dbTypeIsSqlServer((DbType)dbType) && !a4.b(a3)) {
                                    arrayList.add(this.c(a3));
                                }
                            }
                        }
                        string5 = this.c(string4, a3.getColumnId());
                        arrayList.add(string5);
                        continue;
                    }
                    arrayList.add(string3 + this.b(a3));
                    if (DbTypeUtils.dbTypeIsSqlServer((DbType)dbType) || !StringUtils.isNotEmpty((String)a3.getComment())) continue;
                    arrayList.add(this.c(a3));
                    continue;
                }
                a3 = map.get(string4);
                object = map2.get(string4);
                if (DbType.DB2.equals((Object)dbType) || DbType.HSQL.equals((Object)dbType)) {
                    this.a(a3, (a)object, string2, arrayList);
                    continue;
                }
                if (!a3.a(object, dbType)) {
                    arrayList.add(string3 + this.a((a)object, a3));
                }
                if (DbTypeUtils.dbTypeIsSqlServer((DbType)dbType) || DbTypeUtils.dbTypeIsOracle((DbType)dbType) || a3.b((a)object)) continue;
                arrayList.add(this.c((a)object));
            }
            for (String string4 : map.keySet()) {
                if (map2.containsKey(string4.toLowerCase()) || map3.containsValue(string4.toLowerCase())) continue;
                arrayList.add(string3 + this.b(string4));
            }
            if (DbType.DB2.equals((Object)dbType)) {
                arrayList.add("CALL SYSPROC.ADMIN_CMD('reorg table " + string2 + "')");
            }
        }
        catch (SQLException sQLException) {
            throw new RuntimeException();
        }
        return arrayList;
    }

    private static Map<String, Object> a(org.jeecg.modules.online.config.model.a a2, DbType dbType) {
        String string = DbTypeUtils.getDbTypeString((DbType)dbType);
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        for (OnlCgformField onlCgformField : a2.getColumns()) {
            onlCgformField.setDbDefaultVal(org.jeecg.modules.online.config.c.c.c(onlCgformField.getDbDefaultVal()));
        }
        hashMap.put("entity", a2);
        hashMap.put("dataType", string);
        hashMap.put("db", dbType.getDb());
        return hashMap;
    }

    private Map<String, a> a(String string, String string2, b b2) throws SQLException {
        HashMap<String, a> hashMap = new HashMap<String, a>(5);
        Connection connection = null;
        try {
            connection = org.jeecg.modules.online.config.c.d.b(b2);
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
        }
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        String string3 = b2.getUsername();
        DbType dbType = org.jeecg.modules.online.config.c.d.c(b2);
        if (DbTypeUtils.dbTypeIsOracle((DbType)dbType) || DbType.DB2.equals((Object)dbType)) {
            string3 = string3.toUpperCase();
        }
        ResultSet resultSet = null;
        resultSet = DbTypeUtils.dbTypeIsSqlServer((DbType)dbType) ? databaseMetaData.getColumns(connection.getCatalog(), null, string2, "%") : (DbTypeUtils.dbTypeIsPostgre((DbType)dbType) ? databaseMetaData.getColumns(connection.getCatalog(), org.jeecg.modules.online.config.c.d.b(connection), string2, "%") : (DbType.HSQL.equals((Object)dbType) ? databaseMetaData.getColumns(connection.getCatalog(), "PUBLIC", string2.toUpperCase(), "%") : databaseMetaData.getColumns(connection.getCatalog(), string3, string2, "%")));
        while (resultSet.next()) {
            a a2 = new a();
            a2.setTableName(string2);
            String string4 = resultSet.getString("COLUMN_NAME").toLowerCase();
            a2.setColumnName(string4);
            String string5 = resultSet.getString("TYPE_NAME");
            int n = resultSet.getInt("DECIMAL_DIGITS");
            String string6 = c.getMatchClassTypeByDataType(string5, n);
            a2.setColunmType(string6);
            a2.setRealDbType(string5);
            int n2 = resultSet.getInt("COLUMN_SIZE");
            a2.setColumnSize(n2);
            a2.setDecimalDigits(n);
            String string7 = resultSet.getInt("NULLABLE") == 1 ? "Y" : "N";
            a2.setIsNullable(string7);
            String string8 = resultSet.getString("REMARKS");
            a2.setComment(string8);
            String string9 = resultSet.getString("COLUMN_DEF");
            String string10 = org.jeecg.modules.online.config.c.c.c(string9) == null ? "" : org.jeecg.modules.online.config.c.c.c(string9);
            a2.setFieldDefault(string10);
            a.debug("getColumnMetadataFormDataBase --->COLUMN_NAME:" + string4.toUpperCase() + " TYPE_NAME :" + string5 + " DECIMAL_DIGITS:" + n + " COLUMN_SIZE:" + n2);
            hashMap.put(string4, a2);
        }
        return hashMap;
    }

    private Map<String, a> c(org.jeecg.modules.online.config.model.a a2) {
        HashMap<String, a> hashMap = new HashMap<String, a>(5);
        List<OnlCgformField> list = a2.getColumns();
        for (OnlCgformField onlCgformField : list) {
            a a3 = new a();
            a3.setTableName(a2.getTableName().toLowerCase());
            a3.setColumnId(onlCgformField.getId());
            a3.setColumnName(onlCgformField.getDbFieldName().toLowerCase());
            a3.setColumnSize(onlCgformField.getDbLength());
            a3.setColunmType(onlCgformField.getDbType().toLowerCase());
            a3.setIsNullable(onlCgformField.getDbIsNull() == 1 ? "Y" : "N");
            a3.setComment(onlCgformField.getDbFieldTxt());
            a3.setDecimalDigits(onlCgformField.getDbPointLength());
            a3.setFieldDefault(org.jeecg.modules.online.config.c.c.c(onlCgformField.getDbDefaultVal()));
            a3.setPkType(a2.getJformPkType() == null ? "UUID" : a2.getJformPkType());
            a3.setOldColumnName(onlCgformField.getDbFieldNameOld() != null ? onlCgformField.getDbFieldNameOld().toLowerCase() : null);
            a.debug("getColumnMetadataFormCgForm ----> DbFieldName: " + onlCgformField.getDbFieldName().toLowerCase() + " | DbType: " + onlCgformField.getDbType().toLowerCase() + " | DbPointLength:" + onlCgformField.getDbPointLength() + " | DbLength:" + onlCgformField.getDbLength());
            hashMap.put(onlCgformField.getDbFieldName().toLowerCase(), a3);
        }
        return hashMap;
    }

    private Map<String, String> a(List<OnlCgformField> list) {
        HashMap<String, String> hashMap = new HashMap<String, String>(5);
        for (OnlCgformField onlCgformField : list) {
            hashMap.put(onlCgformField.getDbFieldName(), onlCgformField.getDbFieldNameOld());
        }
        return hashMap;
    }

    private String b(String string) {
        return c.getDropColumnSql(string);
    }

    private String a(a a2, a a3) throws org.jeecg.modules.online.config.exception.a {
        return c.getUpdateColumnSql(a2, a3);
    }

    private String b(a a2, a a3) {
        return c.getSpecialHandle(a2, a3);
    }

    private void a(a a2, a a3, String string, List<String> list) {
        c.handleUpdateMultiSql(a2, a3, string, list);
    }

    private String a(a a2) {
        return c.getReNameFieldName(a2);
    }

    private String b(a a2) {
        return c.getAddColumnSql(a2);
    }

    private String c(a a2) {
        return c.getCommentSql(a2);
    }

    private String c(String string, String string2) {
        return "update onl_cgform_field set DB_FIELD_NAME_OLD = '" + string + "' where ID ='" + string2 + "'";
    }

    private int a(String string, String string2, Session session) {
        return session.createSQLQuery("update onl_cgform_field set DB_FIELD_NAME_OLD= '" + string + "' where ID ='" + string2 + "'").executeUpdate();
    }

    private static String c(String string) {
        block3: {
            if (StringUtils.isNotEmpty((String)string)) {
                try {
                    Double.valueOf(string);
                }
                catch (Exception exception) {
                    if (string.startsWith("'") && string.endsWith("'")) break block3;
                    string = "'" + string + "'";
                }
            }
        }
        return string;
    }

    public String a(String string, String string2) {
        string = SqlInjectionUtil.getSqlInjectField((String)string);
        string2 = SqlInjectionUtil.getSqlInjectTableName((String)string2);
        return c.dropIndexs(string, string2);
    }

    public String b(String string, String string2) {
        string = SqlInjectionUtil.getSqlInjectField((String)string);
        string2 = SqlInjectionUtil.getSqlInjectTableName((String)string2);
        return c.countIndex(string, string2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static List<String> a(String string) throws SQLException {
        Connection connection = null;
        ResultSet resultSet = null;
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            connection = org.jeecg.modules.online.config.c.d.getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            resultSet = databaseMetaData.getIndexInfo(null, null, string, false, false);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            while (resultSet.next()) {
                String string2 = resultSet.getString("INDEX_NAME");
                if (oConvertUtils.isEmpty((Object)string2)) {
                    string2 = resultSet.getString("index_name");
                }
                if (!oConvertUtils.isNotEmpty((Object)string2)) continue;
                arrayList.add(string2);
            }
        }
        catch (SQLException sQLException) {
            a.error(sQLException.getMessage(), (Throwable)sQLException);
        }
        finally {
            if (connection != null) {
                connection.close();
            }
        }
        return arrayList;
    }

    static {
        d = null;
    }
}

