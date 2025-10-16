/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.DbType
 *  com.baomidou.mybatisplus.extension.plugins.pagination.DialectFactory
 *  com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect
 *  com.baomidou.mybatisplus.extension.toolkit.JdbcUtils
 *  javax.servlet.http.HttpServletRequest
 *  org.jeecg.common.system.vo.DynamicDataSourceModel
 *  org.jeecg.common.util.ReflectHelper
 *  org.jeecg.common.util.dynamic.db.DataSourceCachePool
 *  org.jeecg.common.util.dynamic.db.DynamicDBUtil
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.jeecg.modules.online.cgreport.c;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.jeecg.common.system.vo.DynamicDataSourceModel;
import org.jeecg.common.util.ReflectHelper;
import org.jeecg.common.util.dynamic.db.DataSourceCachePool;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class a {
    private static final Logger d = LoggerFactory.getLogger(a.class);
    public static final String a = " where ";
    public static final String b = " and ";
    public static final String c = " or ";

    public static String a(String string) {
        string = string.replaceAll("(?i) where ", a);
        string = string.replaceAll("(?i) and ", b);
        string = string.replaceAll("(?i) or ", c);
        String string2 = "(,\\s*|\\s*(\\w|\\.)+\\s*[^, ]+ *\\S*)\\$\\{\\w+\\}\\S*";
        Pattern pattern = Pattern.compile(string2);
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            String string3 = matcher.group();
            d.debug("${}\u5339\u914d\u5e26\u53c2SQL\u7247\u6bb5 ==>" + string3);
            if (string3.indexOf(a) != -1) {
                String string4 = string3.substring(0, string3.indexOf(a));
                string = string.replace(string3, string4 + " where 1=1");
            } else if (string3.indexOf(b) != -1) {
                if ((string3 = string3.substring(string3.indexOf("and"))).indexOf("(") > 0) {
                    string3 = string3.substring(string3.indexOf("(") + 1);
                    string = string.replace(string3, " 1=1 ");
                } else {
                    string = string.replace(string3, "and 1=1");
                }
            } else if (string3.indexOf(c) != -1) {
                if ((string3 = string3.substring(string3.indexOf("or"))).indexOf("(") > 0) {
                    string3 = string3.substring(string3.indexOf("(") + 1);
                    string = string.replace(string3, " 1=1 ");
                } else {
                    string = string.replace(string3, "or 1=1");
                }
            } else {
                string = string3.startsWith(",") ? string.replace(string3, " ,1 ") : string.replace(string3, " 1=1 ");
            }
            d.debug("${}\u66ff\u6362\u540e\u7ed3\u679c ==>" + string);
        }
        string = string.replaceAll("(?i)\\(\\s*1=1\\s*(AND|OR)", "(");
        string = string.replaceAll("(?i)(AND|OR)\\s*1=1", "");
        return string;
    }

    public static Map<String, Object> a(HttpServletRequest httpServletRequest) {
        Map map = httpServletRequest.getParameterMap();
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        Iterator iterator = map.entrySet().iterator();
        String string = "";
        String string2 = "";
        Object object = null;
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            string = (String)entry.getKey();
            object = entry.getValue();
            if ("_t".equals(string) || null == object) {
                string2 = "";
            } else if (object instanceof String[]) {
                String[] stringArray = (String[])object;
                for (int i2 = 0; i2 < stringArray.length; ++i2) {
                    string2 = stringArray[i2] + ",";
                }
                string2 = string2.substring(0, string2.length() - 1);
            } else {
                string2 = object.toString();
            }
            hashMap.put(string, string2);
        }
        return hashMap;
    }

    public static boolean b(String string) {
        String string2 = string.toLowerCase();
        return string2.indexOf("select") == 0;
    }

    public static String c(String string) {
        return String.format("SELECT COUNT(1) \"total\" FROM ( %s ) temp_count", string);
    }

    public static Map<String, Object> a(String string, String string2) {
        Map map = null;
        DynamicDataSourceModel dynamicDataSourceModel = DataSourceCachePool.getCacheDynamicDataSourceModel((String)string);
        DbType dbType = JdbcUtils.getDbType((String)dynamicDataSourceModel.getDbUrl());
        IDialect iDialect = DialectFactory.getDialect((DbType)dbType);
        if (string2.toUpperCase().contains("LIMIT") || string2.toUpperCase().contains("OFFSET")) {
            List list = DynamicDBUtil.findList((String)string, (String)string2, (Object[])new Object[0]);
            if (list != null && list.size() > 0) {
                map = (Map)list.get(0);
            }
        } else {
            Page page = new Page(1L, 1L);
            DialectModel dialectModel = iDialect.buildPaginationSql(string2, page.offset(), page.getSize());
            String string3 = dialectModel.getDialectSql();
            if (string3.contains("?")) {
                long l2 = (Long)ReflectHelper.getFieldVal((String)"firstParam", (Object)dialectModel);
                long l3 = (Long)ReflectHelper.getFieldVal((String)"secondParam", (Object)dialectModel);
                int n = string3.length() - string3.replaceAll("\\?", "").length();
                map = n == 1 ? (Map)DynamicDBUtil.findOne((String)string, (String)string3, (Object[])new Object[]{l2}) : (Map)DynamicDBUtil.findOne((String)string, (String)string3, (Object[])new Object[]{l2, l3});
            } else {
                map = (Map)DynamicDBUtil.findOne((String)string, (String)string3, (Object[])new Object[0]);
            }
        }
        return map;
    }

    public static List<Map<String, Object>> a(String string, String string2, String string3, int n, int n2, Map<String, Object> map) {
        DynamicDataSourceModel dynamicDataSourceModel = DataSourceCachePool.getCacheDynamicDataSourceModel((String)string2);
        String string4 = string3;
        DialectModel dialectModel = null;
        List list = null;
        if (!Boolean.valueOf(string).booleanValue()) {
            DbType dbType = JdbcUtils.getDbType((String)dynamicDataSourceModel.getDbUrl());
            IDialect iDialect = DialectFactory.getDialect((DbType)dbType);
            Page page = new Page((long)n, (long)n2);
            dialectModel = iDialect.buildPaginationSql(string3, page.offset(), page.getSize());
            string4 = dialectModel.getDialectSql();
        }
        if (string4.contains("?")) {
            long l2 = (Long)ReflectHelper.getFieldVal((String)"firstParam", dialectModel);
            long l3 = (Long)ReflectHelper.getFieldVal((String)"secondParam", dialectModel);
            int n3 = string4.length() - string4.replaceAll("\\?", "").length();
            string4 = n3 == 1 ? org.jeecg.modules.online.b.a.a(string4, l2) : org.jeecg.modules.online.b.a.a(string4, l2, l3);
        }
        list = DynamicDBUtil.findListByNamedParam((String)string2, (String)string4, map);
        return list;
    }
}

