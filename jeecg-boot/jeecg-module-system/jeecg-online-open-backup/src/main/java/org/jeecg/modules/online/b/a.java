/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.hutool.core.lang.Validator
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONObject
 *  com.baomidou.mybatisplus.annotation.DbType
 *  org.apache.commons.lang.StringUtils
 *  org.jeecg.common.system.query.MatchTypeEnum
 *  org.jeecg.common.system.query.QueryGenerator
 *  org.jeecg.common.system.query.QueryRuleEnum
 *  org.jeecg.common.system.util.JeecgDataAutorUtils
 *  org.jeecg.common.system.vo.SysPermissionDataRuleModel
 *  org.jeecg.common.util.DateUtils
 *  org.jeecg.common.util.SpringContextUtils
 *  org.jeecg.common.util.oConvertUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.jeecg.modules.online.b;

import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.DbType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.system.query.MatchTypeEnum;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.system.util.JeecgDataAutorUtils;
import org.jeecg.common.system.vo.SysPermissionDataRuleModel;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.d.c;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.config.c.d;
import org.jeecg.modules.online.config.model.OnlineFieldConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class a {
    private static final Logger d = LoggerFactory.getLogger(a.class);
    public static final String a = "jdbcTemplate";
    public static final String b = "mybatis";
    private String e;
    private String f;
    private String g;
    private boolean h;
    private List<OnlineFieldConfig> i;
    private List<String> j;
    private List<SysPermissionDataRuleModel> k;
    private Map<String, Object> l;
    private StringBuffer m;
    private StringBuffer n;
    private Map<String, Object> o;
    private String p;
    private boolean q;
    private String r;
    private int s;
    private boolean t;
    private String u;
    private Map<String, String> v;
    private Map<String, String> w;
    private String x;
    public String c;

    public a() {
    }

    public a(String string, String string2) {
        DbType dbType;
        this.e = string;
        this.f = string.replace(".", "");
        this.g = string2;
        if (this.g == null && (dbType = org.jeecg.modules.online.config.c.d.c(null)) != null) {
            this.g = dbType.getDb();
        }
        this.h = this.f(string2);
        this.m = new StringBuffer();
        this.o = new HashMap<String, Object>(5);
        this.k = null;
        this.j = null;
        this.r = " AND ";
        this.s = 1;
        this.t = true;
        this.u = "";
        this.v = new HashMap<String, String>(5);
        this.w = new HashMap<String, String>(5);
    }

    public a(String string) {
        this(string, null);
        this.s = 2;
    }

    public a(String string, boolean bl, String string2) {
        this(string, null);
        this.q = bl;
        this.r = " " + string2 + " ";
        this.s = 2;
    }

    public String a(List<OnlineFieldConfig> list, Map<String, Object> map) {
        this.c(list, map);
        this.d();
        return this.m.toString();
    }

    public String a(List<OnlineFieldConfig> list, Map<String, Object> map, List<SysPermissionDataRuleModel> list2) {
        this.setAuthDatalist(list2);
        this.c(list, map);
        this.b(list);
        this.a(map);
        this.e();
        return this.m.toString();
    }

    public String a(List<OnlineFieldConfig> list, Map<String, Object> map, List<SysPermissionDataRuleModel> list2, String string) {
        this.setAuthDatalist(list2);
        this.u = string;
        this.c(list, map);
        this.b(list);
        this.e();
        return this.m.toString();
    }

    public String b(List<OnlineFieldConfig> list, Map<String, Object> map) {
        this.c(list, map);
        return this.m.toString();
    }

    public String a(List<OnlineFieldConfig> list) {
        if (this.q) {
            for (OnlineFieldConfig onlineFieldConfig : list) {
                String string = onlineFieldConfig.getName();
                String string2 = onlineFieldConfig.getVal();
                if (string2 == null) continue;
                QueryRuleEnum queryRuleEnum = QueryRuleEnum.getByValue((String)onlineFieldConfig.getRule());
                if (queryRuleEnum == null) {
                    queryRuleEnum = QueryRuleEnum.EQ;
                }
                this.a(string, onlineFieldConfig.getType(), (Object)string2, queryRuleEnum);
            }
        }
        return this.m.toString();
    }

    public void c(List<OnlineFieldConfig> list, Map<String, Object> map) {
        String string;
        Object object;
        for (OnlineFieldConfig object2 : list) {
            Object object3;
            Object object4;
            object = object2.getName();
            string = object2.getType();
            if (this.j != null && this.j.contains(object)) {
                object2.setIsSearch(1);
                object2.setMode("single");
            }
            if (oConvertUtils.isNotEmpty((Object)object2.getMainField()) && oConvertUtils.isNotEmpty((Object)object2.getMainTable())) {
                object2.setIsSearch(1);
                object2.setMode("single");
            }
            if (1 != object2.getIsSearch()) continue;
            if ("time".equals(object2.getView()) && !"group".equals(object2.getMode())) {
                object2.setMode("single");
            }
            if ("group".equals(object2.getMode())) {
                object4 = (String)object + "_begin";
                object3 = map.get(this.u + (String)object4);
                if (null != object3) {
                    this.b((String)object, " >= ", string);
                    this.b((String)object4, string, object3);
                }
                String string2 = (String)object + "_end";
                Object object5 = map.get(this.u + string2);
                if (null == object5) continue;
                this.b((String)object, " <= ", string);
                this.a(string2, string, object5, "end");
                continue;
            }
            object4 = map.get(this.u + (String)object);
            if (object4 == null) continue;
            object3 = object2.getView();
            if ("list_multi".equals(object3) || "checkbox".equals(object3) || "sel_depart".equalsIgnoreCase((String)object3) || "sel_user".equalsIgnoreCase((String)object3)) {
                this.e((String)object, object4);
                continue;
            }
            if ("popup".equals(object3) || "popup_dict".equals(object3)) {
                this.f((String)object, object4);
                continue;
            }
            if ("umeditor".equalsIgnoreCase((String)object3)) {
                object4 = object4.toString().startsWith("*") ? object4.toString() : "*" + object4.toString();
                object4 = object4.toString().endsWith("*") ? object4.toString() : object4.toString() + "*";
            }
            this.a((String)object, string, object4);
        }
        for (String string3 : map.keySet()) {
            if (!string3.startsWith("popup_param_pre__") || (object = map.get(string3)) == null) continue;
            string = string3.replace("popup_param_pre__", "");
            this.a(string, "", object);
        }
    }

    public void setAuthList(List<SysPermissionDataRuleModel> authDatalist) {
        this.k = authDatalist;
    }

    private void d() {
        List list = JeecgDataAutorUtils.loadDataSearchConditon();
        if (list != null && list.size() > 0) {
            SysPermissionDataRuleModel sysPermissionDataRuleModel;
            for (int i2 = 0; i2 < list.size() && (sysPermissionDataRuleModel = (SysPermissionDataRuleModel)list.get(i2)) != null; ++i2) {
                String string = sysPermissionDataRuleModel.getRuleValue();
                if (oConvertUtils.isEmpty((Object)string)) continue;
                String string2 = sysPermissionDataRuleModel.getRuleConditions();
                if (QueryRuleEnum.SQL_RULES.getValue().equals(string2)) {
                    this.b("", QueryGenerator.getSqlRuleValue((String)string));
                    continue;
                }
                QueryRuleEnum queryRuleEnum = QueryRuleEnum.getByValue((String)sysPermissionDataRuleModel.getRuleConditions());
                String string3 = "Integer";
                if ((string = string.trim()).startsWith("'") && string.endsWith("'")) {
                    string3 = "string";
                    string = string.substring(1, string.length() - 1);
                } else if (string.startsWith("#{") && string.endsWith("}")) {
                    string3 = "string";
                }
                String string4 = this.a(string);
                this.a(sysPermissionDataRuleModel.getRuleColumn(), string3, (Object)string4, queryRuleEnum);
            }
        }
    }

    private String a(String string) {
        Pattern pattern = Pattern.compile("#\\{.*?}");
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            String string2 = matcher.group();
            if (!oConvertUtils.isNotEmpty((Object)string2)) continue;
            String string3 = QueryGenerator.converRuleValue((String)string2);
            string = matcher.replaceFirst(Matcher.quoteReplacement(string3 != null ? string3 : ""));
            matcher = pattern.matcher(string);
        }
        return string;
    }

    private OnlineFieldConfig a(String string, List<OnlineFieldConfig> list) {
        if (list != null && string != null) {
            String string2 = oConvertUtils.camelToUnderline((String)string);
            for (int i2 = 0; i2 < list.size(); ++i2) {
                OnlineFieldConfig onlineFieldConfig = list.get(i2);
                String string3 = onlineFieldConfig.getName();
                if (!string.equals(string3) && !string2.equals(string3)) continue;
                return onlineFieldConfig;
            }
        }
        return null;
    }

    private void b(List<OnlineFieldConfig> list) {
        List list2 = this.k;
        if (list2 == null) {
            list2 = JeecgDataAutorUtils.loadDataSearchConditon();
        }
        if (list2 != null && list2.size() > 0) {
            SysPermissionDataRuleModel sysPermissionDataRuleModel;
            for (int i2 = 0; i2 < list2.size() && (sysPermissionDataRuleModel = (SysPermissionDataRuleModel)list2.get(i2)) != null; ++i2) {
                String string = sysPermissionDataRuleModel.getRuleValue();
                if (oConvertUtils.isEmpty((Object)string)) continue;
                String string2 = sysPermissionDataRuleModel.getRuleConditions();
                if (QueryRuleEnum.SQL_RULES.getValue().equals(string2)) {
                    this.b("", QueryGenerator.getSqlRuleValue((String)string));
                    continue;
                }
                String string3 = sysPermissionDataRuleModel.getRuleColumn();
                OnlineFieldConfig onlineFieldConfig = this.a(string3, list);
                if (onlineFieldConfig == null) continue;
                String string4 = QueryGenerator.converRuleValue((String)string);
                QueryRuleEnum queryRuleEnum = QueryRuleEnum.getByValue((String)sysPermissionDataRuleModel.getRuleConditions());
                String string5 = onlineFieldConfig.getView();
                if (QueryRuleEnum.IN.equals((Object)queryRuleEnum)) {
                    if ("list_multi".equalsIgnoreCase(string5) || "checkbox".equalsIgnoreCase(string5) || "sel_depart".equalsIgnoreCase(string5) || "sel_user".equalsIgnoreCase(string5)) {
                        this.e(string3, string4);
                        continue;
                    }
                    if ("popup".equalsIgnoreCase(string5) || "popup_dict".equalsIgnoreCase(string5)) {
                        this.f(string3, string4);
                        continue;
                    }
                    this.a(onlineFieldConfig.getName(), onlineFieldConfig.getType(), (Object)string4, queryRuleEnum);
                    continue;
                }
                this.a(onlineFieldConfig.getName(), onlineFieldConfig.getType(), (Object)string4, queryRuleEnum);
            }
        }
    }

    private void e() {
        String string = org.jeecg.modules.online.cgform.d.c.f(this.x);
        boolean bl = org.jeecg.modules.online.cgform.d.c.j(string);
        if (bl) {
            String string2 = SpringContextUtils.getHttpServletRequest().getHeader("X-Tenant-Id");
            this.a("tenant_id", "int", (Object)string2, QueryRuleEnum.EQ);
        }
    }

    private void a(String string, String string2, Object object) {
        this.a(string, string2, object, (QueryRuleEnum)null);
    }

    private void a(String string, String string2, Object object, QueryRuleEnum queryRuleEnum) {
        if (object != null) {
            String string3 = object.toString();
            boolean bl = false;
            if (queryRuleEnum == null) {
                bl = true;
                queryRuleEnum = QueryGenerator.convert2Rule((Object)object);
            }
            if (bl) {
                string3 = string3.trim();
            }
            switch (queryRuleEnum) {
                case GT: 
                case LT: {
                    this.b(string, queryRuleEnum.getValue(), string2);
                    if (bl) {
                        string3 = string3.substring(1);
                    }
                    this.b(string, string2, (Object)string3);
                    break;
                }
                case GE: 
                case LE: {
                    this.b(string, queryRuleEnum.getValue(), string2);
                    if (bl) {
                        string3 = string3.substring(2);
                    }
                    this.b(string, string2, (Object)string3);
                    break;
                }
                case EQ: {
                    this.b(string, queryRuleEnum.getValue(), string2);
                    this.b(string, string2, (Object)string3);
                    break;
                }
                case EQ_WITH_ADD: {
                    this.b(string, queryRuleEnum.getValue(), string2);
                    if (bl) {
                        string3 = string3.replaceAll("\\+\\+", ",");
                    }
                    this.b(string, string2, (Object)string3);
                    break;
                }
                case NE: {
                    this.a(string, " <> ", this.r, "(");
                    if (bl) {
                        string3 = string3.substring(1);
                    }
                    this.b(string, string2, (Object)string3);
                    this.a(string, " IS NULL ) ", MatchTypeEnum.OR.getValue(), string2, null);
                    break;
                }
                case IN: {
                    this.b(string, " in ", string2);
                    this.a(string, string2, string3);
                    break;
                }
                case LIKE: 
                case RIGHT_LIKE: 
                case LEFT_LIKE: {
                    this.b(string, " like ", string2);
                    if (bl) {
                        this.a(string, string3);
                        break;
                    }
                    this.a(string, string3, queryRuleEnum);
                    break;
                }
                case EMPTY: 
                case NOT_EMPTY: {
                    this.b(string, string2, queryRuleEnum);
                    break;
                }
                default: {
                    this.b(string, " = ", string2);
                    this.b(string, string2, (Object)string3);
                }
            }
        }
    }

    private void a(String string, String string2, String string3) {
        String[] stringArray = string3.split(",");
        if (stringArray.length == 0) {
            this.b("('')");
        } else {
            String string4 = "foreach_%s_%s";
            String string5 = "";
            for (int i2 = 0; i2 < stringArray.length; ++i2) {
                String string6 = stringArray[i2].trim();
                String string7 = String.format(string4, string, i2);
                if (i2 > 0) {
                    string5 = string5 + ",";
                }
                String string8 = this.g(string7);
                string5 = a.equals(this.p) ? string5 + ":" + string8 : string5 + "#{" + this.c(string8) + "}";
                if ("Long".equals(string2) || "Integer".equals(string2)) {
                    this.a(string7, Integer.parseInt(string6));
                    continue;
                }
                this.a(string7, (Object)string6);
            }
            this.b("(" + string5 + ")");
        }
    }

    private void a(String string, String string2) {
        String string3 = this.d(string, "VARCHAR");
        this.b(string3);
        String string4 = "";
        string4 = string2.startsWith("*") && string2.endsWith("*") || string2.startsWith("%") && string2.endsWith("%") ? "%" + string2.substring(1, string2.length() - 1) + "%" : (string2.startsWith("*") || string2.startsWith("%") ? "%" + string2.substring(1) : (string2.endsWith("*") || string2.endsWith("%") ? string2.substring(0, string2.length() - 1) + "%" : "%" + string2 + "%"));
        this.a(string, (Object)string4);
    }

    private void a(String string, String string2, QueryRuleEnum queryRuleEnum) {
        String string3 = this.d(string, "VARCHAR");
        this.b(string3);
        if (queryRuleEnum == QueryRuleEnum.LEFT_LIKE) {
            this.a(string, (Object)("%" + string2));
        } else if (queryRuleEnum == QueryRuleEnum.RIGHT_LIKE) {
            this.a(string, (Object)(string2 + "%"));
        } else {
            this.a(string, (Object)("%" + string2 + "%"));
        }
    }

    private void b(String string, String string2, QueryRuleEnum queryRuleEnum) {
        boolean bl;
        if (this.t) {
            this.t = false;
        } else {
            this.m.append(" ").append(this.r).append(" ");
        }
        String string3 = this.c(string, string2);
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl2 = bl = "text".equalsIgnoreCase(string2) && (DbType.MYSQL.getDb().equalsIgnoreCase(this.g) || DbType.MARIADB.getDb().equalsIgnoreCase(this.g));
        if (QueryRuleEnum.EMPTY.equals((Object)queryRuleEnum)) {
            stringBuilder.append(string3).append(" IS NULL ");
            if (bl) {
                stringBuilder.append(" OR ").append(string3).append(" = '' ");
            }
        } else if (QueryRuleEnum.NOT_EMPTY.equals((Object)queryRuleEnum)) {
            stringBuilder.append(string3).append(" IS NOT NULL ");
            if (bl) {
                stringBuilder.append(" AND ").append(string3).append(" != '' ");
            }
        }
        this.m.append(" (").append((CharSequence)stringBuilder).append(") ");
    }

    private void b(String string, String string2, Object object) {
        this.a(string, string2, object, (String)null);
    }

    private void a(String string, String string2, Object object, String string3) {
        String string4 = string2.toLowerCase();
        if (this.e(string2)) {
            if (org.jeecg.modules.online.cgform.d.c.g(object.toString())) {
                this.b(object.toString());
            } else {
                this.b("''");
            }
        } else if ("datetime".equals(string4)) {
            String string5 = object.toString().trim();
            if (string5.length() <= 10) {
                string5 = "end".equals(string3) ? string5 + " 23:59:59" : string5 + " 00:00:00";
            }
            Date date = DateUtils.str2Date((String)string5, (SimpleDateFormat)((SimpleDateFormat)DateUtils.datetimeFormat.get()));
            this.b(string, date);
        } else if ("date".equals(string4)) {
            String string6 = object.toString().trim();
            if (string6.length() > 10) {
                string6 = string6.substring(0, 10);
            }
            Date date = DateUtils.str2Date((String)string6, (SimpleDateFormat)((SimpleDateFormat)DateUtils.date_sdf.get()));
            this.c(string, date);
        } else {
            String string7 = object.toString().trim();
            if (string7.startsWith("'") && string7.endsWith("'") && this.s == 1) {
                this.b(string7);
            } else {
                this.d(string, (Object)string7);
            }
        }
    }

    private void b(String string, String string2) {
        this.a(string, string2, this.r, null);
    }

    private void b(String string, String string2, String string3) {
        this.a(string, string2, this.r, string3, null);
    }

    private void a(String string, String string2, String string3, String string4) {
        this.a(string, string2, string3, null, string4);
    }

    private String c(String string, String string2) {
        String string3 = this.e + string;
        if (DbType.DM.getDb().equalsIgnoreCase(this.g) && "text".equalsIgnoreCase(string2)) {
            string3 = "CONVERT(VARCHAR(5000)," + string3 + ")";
        } else if (Validator.hasChinese((CharSequence)string)) {
            if (DbType.MARIADB.getDb().equalsIgnoreCase(this.g) || DbType.MYSQL.getDb().equalsIgnoreCase(this.g)) {
                string3 = this.e + "`" + string + "`";
            } else if (DbType.ORACLE.getDb().equalsIgnoreCase(this.g) || DbType.POSTGRE_SQL.getDb().equalsIgnoreCase(this.g) || DbType.SQL_SERVER.getDb().equalsIgnoreCase(this.g)) {
                string3 = this.e + "\"" + string + "\"";
            }
        }
        return string3;
    }

    private void a(String string, String string2, String string3, String string4, String string5) {
        if (this.t) {
            this.t = false;
        } else {
            this.m.append(" ").append(string3).append(" ");
        }
        if (null != string5 && !string5.isEmpty()) {
            this.m.append(string5);
        }
        if (!string.isEmpty()) {
            String string6 = this.c(string, string4);
            this.m.append(string6).append(string2);
        } else {
            this.m.append(" ").append(string2).append(" ");
        }
    }

    private void b(String string) {
        this.m.append(string);
    }

    private String d(String string, String string2) {
        string = this.g(string);
        if (a.equals(this.p)) {
            return ":" + string;
        }
        String string3 = this.c(string);
        if (string2 == null) {
            return String.format("#{%s}", string3);
        }
        return String.format("#{%s, jdbcType=%s}", string3, string2);
    }

    private String c(String string) {
        return "param." + this.d(string);
    }

    private void a(String string, Object object) {
        string = this.h(string);
        this.o.put(this.d(string), object);
    }

    private String d(String string) {
        if (this.s == 1) {
            return string;
        }
        return this.f + "_" + string;
    }

    private void b(String string, Object object) {
        if (object != null) {
            String string2 = this.d(string, "TIMESTAMP");
            this.b(string2);
            this.a(string, object);
        }
    }

    private void c(String string, Object object) {
        if (object != null) {
            String string2 = this.d(string, "DATE");
            this.b(string2);
            this.a(string, object);
        }
    }

    private void d(String string, Object object) {
        if (object != null) {
            String string2 = this.d(string, null);
            this.b(string2);
            this.a(string, object);
        }
    }

    private boolean e(String string) {
        return "Long".equals(string) || "Integer".equals(string) || "int".equals(string) || "double".equals(string) || "BigDecimal".equals(string) || "number".equals(string);
    }

    private boolean f(String string) {
        return !"ORACLE".equals(string);
    }

    public static String a(String string, long l2) {
        return string.replaceFirst("\\?", String.valueOf(l2));
    }

    public static String a(String string, long l2, long l3) {
        string = string.replaceFirst("\\?", String.valueOf(l2));
        return string.replaceFirst("\\?", String.valueOf(l3));
    }

    private void e(String string, Object object) {
        if (object != null) {
            String[] stringArray = object.toString().split(",");
            String string2 = "";
            String string3 = this.e + string;
            for (int i2 = 0; i2 < stringArray.length; ++i2) {
                String string4 = string3 + " like '%" + stringArray[i2] + ",%' or " + string3 + " like '%," + stringArray[i2] + "%' or " + string3 + " = '" + stringArray[i2] + "'";
                string2 = string2.length() == 0 ? string4 : string2 + " or " + string4;
            }
            if (string2.length() > 0) {
                String string5 = "(" + string2 + ")";
                this.b("", string5);
            }
        }
    }

    private void f(String string, Object object) {
        if (object != null) {
            String string2 = this.e + string;
            String string3 = "";
            String string4 = "popup_%s_%s";
            String[] stringArray = object.toString().split(",");
            for (int i2 = 0; i2 < stringArray.length; ++i2) {
                String string5 = String.format(string4, string, i2);
                String string6 = this.d(string5, "VARCHAR");
                String string7 = "%" + stringArray[i2] + "%";
                this.a(string5, (Object)string7);
                String string8 = string2 + " like " + string6;
                string3 = string3.length() == 0 ? string8 : string3 + " or " + string8;
            }
            if (string3.length() > 0) {
                String string9 = "(" + string3 + ")";
                this.b("", string9);
            }
        }
    }

    private void a(Map<String, Object> map) {
        String[] stringArray;
        Object object;
        Object object2 = map.get("superQueryMatchType");
        MatchTypeEnum matchTypeEnum = MatchTypeEnum.getByValue((Object)object2);
        if (matchTypeEnum == null) {
            matchTypeEnum = MatchTypeEnum.AND;
        }
        if ((object = map.get("superQueryParams")) == null || StringUtils.isBlank((String)object.toString())) {
            return;
        }
        String string = null;
        try {
            string = URLDecoder.decode(object.toString(), "UTF-8");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
            return;
        }
        JSONArray jSONArray = JSONArray.parseArray((String)string);
        IOnlCgformFieldService iOnlCgformFieldService = (IOnlCgformFieldService)SpringContextUtils.getBean(IOnlCgformFieldService.class);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("JEECG_SUPER_QUERY_MAIN_TABLE");
        if (this.c != null && !"".equals(this.c)) {
            for (String object3 : stringArray = this.c.split(",")) {
                arrayList.add(object3);
            }
        }
        stringArray = new HashMap(5);
        String[] stringArray2 = new StringBuffer();
        for (int i2 = 0; i2 < arrayList.size(); ++i2) {
            Object object3;
            Object object4;
            Object object5;
            OnlineFieldConfig onlineFieldConfig;
            Object object6;
            String string2;
            Object object7;
            String string3 = (String)arrayList.get(i2);
            ArrayList<OnlineFieldConfig> arrayList2 = new ArrayList<OnlineFieldConfig>();
            for (int i3 = 0; i3 < jSONArray.size(); ++i3) {
                object7 = jSONArray.getJSONObject(i3);
                string2 = object7.getString("field");
                if (oConvertUtils.isEmpty((Object)string2)) continue;
                object6 = string2.split(",");
                onlineFieldConfig = new OnlineFieldConfig((JSONObject)object7);
                if ("JEECG_SUPER_QUERY_MAIN_TABLE".equals(string3) && ((String[])object6).length == 1) {
                    boolean string5 = org.jeecg.modules.online.cgform.d.c.g(onlineFieldConfig.getVal());
                    if (!string5 && "bigdecimal".equalsIgnoreCase(onlineFieldConfig.getType())) continue;
                    arrayList2.add(onlineFieldConfig);
                    continue;
                }
                if (((String[])object6).length != 2 || !object6[0].equals(string3)) continue;
                arrayList2.add(onlineFieldConfig);
                JSONObject jSONObject = (JSONObject)stringArray.get(string3);
                if (jSONObject != null) continue;
                object5 = iOnlCgformFieldService.queryFormFieldsByTableName(string3);
                jSONObject = new JSONObject();
                object4 = object5.iterator();
                while (object4.hasNext()) {
                    object3 = (OnlCgformField)object4.next();
                    if (!StringUtils.isNotBlank((String)((OnlCgformField)object3).getMainTable())) continue;
                    jSONObject.put("subTableName", (Object)string3);
                    jSONObject.put("subField", (Object)((OnlCgformField)object3).getDbFieldName());
                    jSONObject.put("mainTable", (Object)((OnlCgformField)object3).getMainTable());
                    jSONObject.put("mainField", (Object)((OnlCgformField)object3).getMainField());
                }
                stringArray.put(string3, jSONObject);
            }
            if (arrayList2.size() <= 0) continue;
            String string4 = i2 == 0 ? this.e : this.f + i2 + ".";
            object7 = new a(string4, true, matchTypeEnum.getValue());
            ((a)object7).setDuplicateParamNameRecord(this.getDuplicateParamNameRecord());
            ((a)object7).setDuplicateSqlNameRecord(this.getDuplicateSqlNameRecord());
            string2 = ((a)object7).a(arrayList2);
            object6 = ((a)object7).getSqlParams();
            if (string2 == null || string2.length() <= 0) continue;
            if (i2 == 0) {
                stringArray2.append(" ").append(string2).append(" ");
                this.o.putAll((Map<String, Object>)object6);
                continue;
            }
            onlineFieldConfig = (JSONObject)stringArray.get(string3);
            String string5 = onlineFieldConfig.getString("subTableName");
            object5 = onlineFieldConfig.getString("subField");
            object4 = onlineFieldConfig.getString("mainField");
            object3 = " %s in (select %s from %s %s where ";
            String string6 = String.format((String)object3, object4, object5, string5, this.f + i2);
            this.o.putAll((Map<String, Object>)object6);
            stringArray2.append(matchTypeEnum.getValue()).append(string6).append(string2).append(") ");
        }
        String string7 = stringArray2.toString();
        if (string7.length() > 0) {
            if (string7.startsWith("AND ")) {
                string7 = string7.substring(3);
            } else if (string7.startsWith("OR ")) {
                string7 = string7.substring(2);
            }
            this.b("", "(" + string7 + ")");
        }
    }

    private String g(String string) {
        return this.a(string, this.v);
    }

    private String h(String string) {
        return this.a(string, this.w);
    }

    private String a(String string, Map<String, String> map) {
        String string2 = map.get(string);
        if (string2 == null) {
            string2 = string;
            map.put(string, string + "_1");
        } else {
            String string3 = string2.substring(string2.lastIndexOf("_") + 1);
            String string4 = string + "_" + (Integer.parseInt(string3) + 1);
            map.put(string, string4);
        }
        return string2;
    }

    public String getAlias() {
        return this.e;
    }

    public String getAliasNoPoint() {
        return this.f;
    }

    public String getDataBaseType() {
        return this.g;
    }

    public boolean a() {
        return this.h;
    }

    public List<OnlineFieldConfig> getFieldList() {
        return this.i;
    }

    public List<String> getNeedList() {
        return this.j;
    }

    public List<SysPermissionDataRuleModel> getAuthDatalist() {
        return this.k;
    }

    public Map<String, Object> getReqParams() {
        return this.l;
    }

    public StringBuffer getSql() {
        return this.m;
    }

    public StringBuffer getSuperQuerySql() {
        return this.n;
    }

    public Map<String, Object> getSqlParams() {
        return this.o;
    }

    public String getDaoType() {
        return this.p;
    }

    public boolean b() {
        return this.q;
    }

    public String getMatchType() {
        return this.r;
    }

    public int getUsePage() {
        return this.s;
    }

    public boolean c() {
        return this.t;
    }

    public String getParamPrefix() {
        return this.u;
    }

    public Map<String, String> getDuplicateSqlNameRecord() {
        return this.v;
    }

    public Map<String, String> getDuplicateParamNameRecord() {
        return this.w;
    }

    public String getTableName() {
        return this.x;
    }

    public String getSubTableStr() {
        return this.c;
    }

    public void setAlias(String alias) {
        this.e = alias;
    }

    public void setAliasNoPoint(String aliasNoPoint) {
        this.f = aliasNoPoint;
    }

    public void setDataBaseType(String dataBaseType) {
        this.g = dataBaseType;
    }

    public void setDateStringSearch(boolean dateStringSearch) {
        this.h = dateStringSearch;
    }

    public void setFieldList(List<OnlineFieldConfig> fieldList) {
        this.i = fieldList;
    }

    public void setNeedList(List<String> needList) {
        this.j = needList;
    }

    public void setAuthDatalist(List<SysPermissionDataRuleModel> authDatalist) {
        this.k = authDatalist;
    }

    public void setReqParams(Map<String, Object> reqParams) {
        this.l = reqParams;
    }

    public void setSql(StringBuffer sql) {
        this.m = sql;
    }

    public void setSuperQuerySql(StringBuffer superQuerySql) {
        this.n = superQuerySql;
    }

    public void setSqlParams(Map<String, Object> sqlParams) {
        this.o = sqlParams;
    }

    public void setDaoType(String daoType) {
        this.p = daoType;
    }

    public void setSuperQuery(boolean superQuery) {
        this.q = superQuery;
    }

    public void setMatchType(String matchType) {
        this.r = matchType;
    }

    public void setUsePage(int usePage) {
        this.s = usePage;
    }

    public void setFirst(boolean first) {
        this.t = first;
    }

    public void setParamPrefix(String paramPrefix) {
        this.u = paramPrefix;
    }

    public void setDuplicateSqlNameRecord(Map<String, String> duplicateSqlNameRecord) {
        this.v = duplicateSqlNameRecord;
    }

    public void setDuplicateParamNameRecord(Map<String, String> duplicateParamNameRecord) {
        this.w = duplicateParamNameRecord;
    }

    public void setTableName(String tableName) {
        this.x = tableName;
    }

    public void setSubTableStr(String subTableStr) {
        this.c = subTableStr;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof a)) {
            return false;
        }
        a a2 = (a)o;
        if (!a2.a(this)) {
            return false;
        }
        if (this.a() != a2.a()) {
            return false;
        }
        if (this.b() != a2.b()) {
            return false;
        }
        if (this.getUsePage() != a2.getUsePage()) {
            return false;
        }
        if (this.c() != a2.c()) {
            return false;
        }
        String string = this.getAlias();
        String string2 = a2.getAlias();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.getAliasNoPoint();
        String string4 = a2.getAliasNoPoint();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.getDataBaseType();
        String string6 = a2.getDataBaseType();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        List<OnlineFieldConfig> list = this.getFieldList();
        List<OnlineFieldConfig> list2 = a2.getFieldList();
        if (list == null ? list2 != null : !((Object)list).equals(list2)) {
            return false;
        }
        List<String> list3 = this.getNeedList();
        List<String> list4 = a2.getNeedList();
        if (list3 == null ? list4 != null : !((Object)list3).equals(list4)) {
            return false;
        }
        List<SysPermissionDataRuleModel> list5 = this.getAuthDatalist();
        List<SysPermissionDataRuleModel> list6 = a2.getAuthDatalist();
        if (list5 == null ? list6 != null : !((Object)list5).equals(list6)) {
            return false;
        }
        Map<String, Object> map = this.getReqParams();
        Map<String, Object> map2 = a2.getReqParams();
        if (map == null ? map2 != null : !((Object)map).equals(map2)) {
            return false;
        }
        StringBuffer stringBuffer = this.getSql();
        StringBuffer stringBuffer2 = a2.getSql();
        if (stringBuffer == null ? stringBuffer2 != null : !stringBuffer.equals(stringBuffer2)) {
            return false;
        }
        StringBuffer stringBuffer3 = this.getSuperQuerySql();
        StringBuffer stringBuffer4 = a2.getSuperQuerySql();
        if (stringBuffer3 == null ? stringBuffer4 != null : !stringBuffer3.equals(stringBuffer4)) {
            return false;
        }
        Map<String, Object> map3 = this.getSqlParams();
        Map<String, Object> map4 = a2.getSqlParams();
        if (map3 == null ? map4 != null : !((Object)map3).equals(map4)) {
            return false;
        }
        String string7 = this.getDaoType();
        String string8 = a2.getDaoType();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.getMatchType();
        String string10 = a2.getMatchType();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        String string11 = this.getParamPrefix();
        String string12 = a2.getParamPrefix();
        if (string11 == null ? string12 != null : !string11.equals(string12)) {
            return false;
        }
        Map<String, String> map5 = this.getDuplicateSqlNameRecord();
        Map<String, String> map6 = a2.getDuplicateSqlNameRecord();
        if (map5 == null ? map6 != null : !((Object)map5).equals(map6)) {
            return false;
        }
        Map<String, String> map7 = this.getDuplicateParamNameRecord();
        Map<String, String> map8 = a2.getDuplicateParamNameRecord();
        if (map7 == null ? map8 != null : !((Object)map7).equals(map8)) {
            return false;
        }
        String string13 = this.getTableName();
        String string14 = a2.getTableName();
        if (string13 == null ? string14 != null : !string13.equals(string14)) {
            return false;
        }
        String string15 = this.getSubTableStr();
        String string16 = a2.getSubTableStr();
        return !(string15 == null ? string16 != null : !string15.equals(string16));
    }

    protected boolean a(Object object) {
        return object instanceof a;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        n2 = n2 * 59 + (this.a() ? 79 : 97);
        n2 = n2 * 59 + (this.b() ? 79 : 97);
        n2 = n2 * 59 + this.getUsePage();
        n2 = n2 * 59 + (this.c() ? 79 : 97);
        String string = this.getAlias();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.getAliasNoPoint();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.getDataBaseType();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        List<OnlineFieldConfig> list = this.getFieldList();
        n2 = n2 * 59 + (list == null ? 43 : ((Object)list).hashCode());
        List<String> list2 = this.getNeedList();
        n2 = n2 * 59 + (list2 == null ? 43 : ((Object)list2).hashCode());
        List<SysPermissionDataRuleModel> list3 = this.getAuthDatalist();
        n2 = n2 * 59 + (list3 == null ? 43 : ((Object)list3).hashCode());
        Map<String, Object> map = this.getReqParams();
        n2 = n2 * 59 + (map == null ? 43 : ((Object)map).hashCode());
        StringBuffer stringBuffer = this.getSql();
        n2 = n2 * 59 + (stringBuffer == null ? 43 : stringBuffer.hashCode());
        StringBuffer stringBuffer2 = this.getSuperQuerySql();
        n2 = n2 * 59 + (stringBuffer2 == null ? 43 : stringBuffer2.hashCode());
        Map<String, Object> map2 = this.getSqlParams();
        n2 = n2 * 59 + (map2 == null ? 43 : ((Object)map2).hashCode());
        String string4 = this.getDaoType();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.getMatchType();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        String string6 = this.getParamPrefix();
        n2 = n2 * 59 + (string6 == null ? 43 : string6.hashCode());
        Map<String, String> map3 = this.getDuplicateSqlNameRecord();
        n2 = n2 * 59 + (map3 == null ? 43 : ((Object)map3).hashCode());
        Map<String, String> map4 = this.getDuplicateParamNameRecord();
        n2 = n2 * 59 + (map4 == null ? 43 : ((Object)map4).hashCode());
        String string7 = this.getTableName();
        n2 = n2 * 59 + (string7 == null ? 43 : string7.hashCode());
        String string8 = this.getSubTableStr();
        n2 = n2 * 59 + (string8 == null ? 43 : string8.hashCode());
        return n2;
    }

    public String toString() {
        return "ConditionHandler(alias=" + this.getAlias() + ", aliasNoPoint=" + this.getAliasNoPoint() + ", dataBaseType=" + this.getDataBaseType() + ", dateStringSearch=" + this.a() + ", fieldList=" + this.getFieldList() + ", needList=" + this.getNeedList() + ", authDatalist=" + this.getAuthDatalist() + ", reqParams=" + this.getReqParams() + ", sql=" + this.getSql() + ", superQuerySql=" + this.getSuperQuerySql() + ", sqlParams=" + this.getSqlParams() + ", daoType=" + this.getDaoType() + ", superQuery=" + this.b() + ", matchType=" + this.getMatchType() + ", usePage=" + this.getUsePage() + ", first=" + this.c() + ", paramPrefix=" + this.getParamPrefix() + ", duplicateSqlNameRecord=" + this.getDuplicateSqlNameRecord() + ", duplicateParamNameRecord=" + this.getDuplicateParamNameRecord() + ", tableName=" + this.getTableName() + ", subTableStr=" + this.getSubTableStr() + ")";
    }
}

