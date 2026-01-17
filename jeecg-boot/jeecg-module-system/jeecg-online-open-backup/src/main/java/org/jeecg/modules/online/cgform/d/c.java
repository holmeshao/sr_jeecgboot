/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONException
 *  com.alibaba.fastjson.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.toolkit.IdWorker
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.shiro.SecurityUtils
 *  org.jeecg.common.exception.JeecgBootException
 *  org.jeecg.common.system.api.ISysBaseAPI
 *  org.jeecg.common.system.query.MatchTypeEnum
 *  org.jeecg.common.system.query.QueryGenerator
 *  org.jeecg.common.system.query.QueryRuleEnum
 *  org.jeecg.common.system.util.JwtUtil
 *  org.jeecg.common.system.vo.DictModel
 *  org.jeecg.common.system.vo.LoginUser
 *  org.jeecg.common.util.CommonUtils
 *  org.jeecg.common.util.DateUtils
 *  org.jeecg.common.util.Md5Util
 *  org.jeecg.common.util.SpringContextUtils
 *  org.jeecg.common.util.SqlInjectionUtil
 *  org.jeecg.common.util.UUIDGenerator
 *  org.jeecg.common.util.oConvertUtils
 *  org.jeecg.config.mybatis.MybatisPlusSaasConfig
 *  org.jeecgframework.poi.excel.entity.params.ExcelExportEntity
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.jeecg.modules.online.cgform.d;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.MatchTypeEnum;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.Md5Util;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.common.util.a.a.e;
import org.jeecg.common.util.a.a.f;
import org.jeecg.common.util.a.a.g;
import org.jeecg.common.util.a.a.h;
import org.jeecg.common.util.a.d;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.online.cgform.d.i;
import org.jeecg.modules.online.cgform.d.k;
import org.jeecg.modules.online.cgform.entity.OnlCgformButton;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceJava;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceJs;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.entity.OnlCgformIndex;
import org.jeecg.modules.online.cgform.enums.CgformValidPatternEnum;
import org.jeecg.modules.online.cgform.mapper.OnlCgformHeadMapper;
import org.jeecg.modules.online.cgform.model.TreeSelectColumn;
import org.jeecg.modules.online.config.model.OnlineFieldConfig;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class c {
    private static final Logger aZ = LoggerFactory.getLogger(c.class);
    public static final String a = "SELECT ";
    public static final String b = " FROM ";
    public static final String c = " JOIN ";
    public static final String d = " ON ";
    public static final String e = " AND ";
    public static final String f = " like ";
    public static final String g = " COUNT(*) ";
    public static final String h = " where 1=1  ";
    public static final String i = " where  ";
    public static final String j = " ORDER BY ";
    public static final String k = "asc";
    public static final String l = "desc";
    public static final String m = "=";
    public static final String n = "!=";
    public static final String o = ">=";
    public static final String p = ">";
    public static final String q = "<=";
    public static final String r = "<";
    public static final String s = " or ";
    public static final String t = "jeecg_row_key";
    public static final String u = "Y";
    public static final String v = "$";
    public static final String w = "CREATE_TIME";
    public static final String x = "CREATE_BY";
    public static final String y = "UPDATE_TIME";
    public static final String z = "UPDATE_BY";
    public static final String A = "SYS_ORG_CODE";
    public static final int B = 2;
    public static final String C = "'";
    public static final String D = "N";
    public static final String E = ",";
    public static final String F = "single";
    public static final String G = "id";
    public static final String H = "bpm_status";
    public static final String I = "1";
    public static final String J = "force";
    public static final String K = "normal";
    public static final String L = "switch";
    public static final String M = "popup";
    public static final String N = "popup_dict";
    public static final String O = "sel_search";
    public static final String P = "image";
    public static final String Q = "file";
    public static final String R = "sel_tree";
    public static final String S = "cat_tree";
    public static final String T = "link_down";
    public static final String U = "date";
    public static final String V = "SYS_USER";
    public static final String W = "REALNAME";
    public static final String X = "USERNAME";
    public static final String Y = "SYS_DEPART";
    public static final String Z = "DEPART_NAME";
    public static final String aa = "ID";
    public static final String ab = "SYS_CATEGORY";
    public static final String ac = "NAME";
    public static final String ad = "CODE";
    public static final String ae = "ID";
    public static final String af = "PID";
    public static final String ag = "HAS_CHILD";
    public static final String ah = "sel_search";
    public static final String ai = "link_table";
    public static final String aj = "link_table_field";
    public static final String ak = "sub_table_design_";
    public static final String al = "sub-table-design_";
    public static final String am = "sub-table-one2one_";
    public static final String an = "import";
    public static final String ao = "export";
    public static final String ap = "query";
    public static final String aq = "form";
    public static final String ar = "list";
    public static final String as = "1";
    public static final String at = "start";
    public static final String au = "erp";
    public static final String av = "innerTable";
    public static final String aw = "exportSingleOnly";
    public static final String ax = "isSingleTableImport";
    public static final String ay = "validateStatus";
    public static final String az = "1";
    public static final String aA = "foreignKeys";
    public static final int aB = 1;
    public static final int aC = 2;
    public static final int aD = 0;
    public static final int aE = 1;
    public static final int aF = 1;
    public static final String aG = "1";
    public static final Integer aH = 2;
    public static final String aI = "1";
    public static final String aJ = "id";
    public static final String aK = "center";
    public static final String aL = "modules/bpm/task/form/OnlineFormDetail";
    public static final String aM = "check/onlineForm/detail";
    public static final String aN = "onl_";
    public static final String aO = "jeecg_submit_form_and_flow";
    public static final String aP = "joinQuery";
    public static final String aQ = "properties";
    public static final String aR = "title";
    public static final String aS = "view";
    public static final String aT = "table";
    public static final String aU = "searchFieldList";
    public static final String aV = "switchOptions";
    public static final String aW = "extConfigJson";
    public static final String aX = "0";
    public static final String aY = "1";
    private static final String ba = "beforeAdd,beforeEdit,afterAdd,afterEdit,beforeDelete,afterDelete,mounted,created";
    private static String bb;

    public static boolean a(OnlCgformHead onlCgformHead) {
        if (onlCgformHead != null && aH.equals(onlCgformHead.getTableType())) {
            JSONObject jSONObject;
            String string = onlCgformHead.getThemeTemplate();
            if (au.equals(string) || av.equals(string) || u.equals(onlCgformHead.getIsTree())) {
                return false;
            }
            String string2 = onlCgformHead.getExtConfigJson();
            if (string2 != null && !"".equals(string2) && (jSONObject = JSON.parseObject((String)string2)).containsKey((Object)aP) && 1 == jSONObject.getInteger(aP)) {
                return true;
            }
        }
        return false;
    }

    public static void a(String string, List<OnlCgformField> list, StringBuffer stringBuffer) {
        if (list == null || list.size() == 0) {
            stringBuffer.append("SELECT id");
        } else {
            stringBuffer.append(a);
            int n = list.size();
            boolean bl = false;
            for (int i2 = 0; i2 < n; ++i2) {
                OnlCgformField onlCgformField = list.get(i2);
                onlCgformField.setDbFieldName(SqlInjectionUtil.getSqlInjectField((String)onlCgformField.getDbFieldName()));
                if (!org.jeecg.modules.online.cgform.b.b.b.equals(onlCgformField.getDbIsPersist())) continue;
                if ("id".equals(onlCgformField.getDbFieldName())) {
                    bl = true;
                }
                if (S.equals(onlCgformField.getFieldShowType()) && oConvertUtils.isNotEmpty((Object)onlCgformField.getDictText())) {
                    stringBuffer.append(onlCgformField.getDictText() + E);
                }
                if (i2 == n - 1) {
                    stringBuffer.append(onlCgformField.getDbFieldName() + " ");
                    continue;
                }
                stringBuffer.append(onlCgformField.getDbFieldName() + E);
            }
            String string2 = stringBuffer.substring(stringBuffer.length() - 1);
            if (E.equals(string2)) {
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            }
            if (!bl) {
                stringBuffer.append(",id");
            }
        }
        stringBuffer.append(b + org.jeecg.modules.online.cgform.d.c.f(string));
    }

    public static String a(String string) {
        return " to_date('" + string + "','yyyy-MM-dd HH24:mi:ss')";
    }

    public static String b(String string) {
        return " to_date('" + string + "','yyyy-MM-dd')";
    }

    public static boolean c(String string) {
        if (ar.equals(string)) {
            return true;
        }
        if ("radio".equals(string)) {
            return true;
        }
        if ("checkbox".equals(string)) {
            return true;
        }
        return "list_multi".equals(string);
    }

    public static boolean a(OnlCgformField onlCgformField) {
        String string;
        if (oConvertUtils.isNotEmpty((Object)onlCgformField.getMainField()) && oConvertUtils.isNotEmpty((Object)onlCgformField.getMainTable()) && oConvertUtils.isNotEmpty((Object)(string = onlCgformField.getFieldExtendJson())) && string.indexOf("textField") > 0) {
            onlCgformField.setDictTable(onlCgformField.getMainTable());
            onlCgformField.setDictField(onlCgformField.getMainField());
            onlCgformField.setFieldShowType("sel_search");
            JSONObject jSONObject = JSON.parseObject((String)string);
            onlCgformField.setDictText(jSONObject.getString("textField"));
            return true;
        }
        return false;
    }

    public static void a(StringBuilder stringBuilder, String string, JSONObject jSONObject, MatchTypeEnum matchTypeEnum, JSONObject jSONObject2, boolean bl) {
        if (!bl) {
            stringBuilder.append(" ").append(matchTypeEnum.getValue()).append(" ");
        }
        String string2 = jSONObject.getString("type");
        String string3 = jSONObject.getString("val");
        String string4 = org.jeecg.modules.online.cgform.d.k.a(string2, string3);
        QueryRuleEnum queryRuleEnum = QueryRuleEnum.getByValue((String)jSONObject.getString("rule"));
        if (queryRuleEnum == null) {
            queryRuleEnum = QueryRuleEnum.EQ;
        }
        if (jSONObject2 != null) {
            String string5 = jSONObject2.getString("subTableName");
            String string6 = jSONObject2.getString("subField");
            String string7 = jSONObject2.getString("mainTable");
            String string8 = jSONObject2.getString("mainField");
            stringBuilder.append("(").append(string8).append(" IN (SELECT ").append(string6).append(b).append(string5).append(" WHERE ");
            if (M.equals(string2)) {
                stringBuilder.append(org.jeecg.modules.online.cgform.d.c.b(string, string3));
            } else {
                stringBuilder.append(string);
                org.jeecg.modules.online.cgform.d.k.a(stringBuilder, queryRuleEnum, string3, string4, string2);
            }
            stringBuilder.append("))");
        } else if (M.equals(string2)) {
            stringBuilder.append(org.jeecg.modules.online.cgform.d.c.b(string, string3));
        } else {
            stringBuilder.append(string);
            org.jeecg.modules.online.cgform.d.k.a(stringBuilder, queryRuleEnum, string3, string4, string2);
        }
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
                    string2 = stringArray[i2] + E;
                }
                string2 = string2.substring(0, string2.length() - 1);
            } else {
                string2 = object.toString();
            }
            hashMap.put(string, string2);
        }
        return hashMap;
    }

    public static boolean a(String string, List<OnlCgformField> list) {
        for (OnlCgformField onlCgformField : list) {
            if (!string.equals(onlCgformField.getDbFieldName())) continue;
            return true;
        }
        return false;
    }

    public static JSONObject a(List<OnlCgformField> list, List<String> list2, TreeSelectColumn treeSelectColumn) {
        Object object;
        JSONObject jSONObject = new JSONObject();
        ArrayList<String> arrayList = new ArrayList<String>();
        ArrayList<org.jeecg.common.util.a.b> arrayList2 = new ArrayList<org.jeecg.common.util.a.b>();
        ISysBaseAPI iSysBaseAPI = (ISysBaseAPI)SpringContextUtils.getBean(ISysBaseAPI.class);
        OnlCgformHeadMapper onlCgformHeadMapper = (OnlCgformHeadMapper)SpringContextUtils.getBean(OnlCgformHeadMapper.class);
        ArrayList<String> arrayList3 = new ArrayList<String>();
        for (OnlCgformField onlCgformField : list) {
            Object object2;
            Object object3;
            Object object4;
            Object object5;
            String string = onlCgformField.getDbFieldName();
            if ("id".equals(string) || arrayList3.contains(string)) continue;
            String string2 = onlCgformField.getDbFieldTxt();
            if ("1".equals(onlCgformField.getFieldMustInput())) {
                arrayList.add(string);
            }
            String string3 = onlCgformField.getFieldShowType();
            Object object6 = null;
            if (L.equals(string3)) {
                object6 = new h(string, string2, onlCgformField.getFieldExtendJson());
            } else if (org.jeecg.modules.online.cgform.d.c.c(string3) || aj.equals(string3)) {
                object6 = new org.jeecg.common.util.a.a.a(string, string3, string2, onlCgformField.getDictTable(), onlCgformField.getDictField(), onlCgformField.getDictText());
                if (org.jeecg.modules.online.cgform.d.i.a(onlCgformField.getDbType())) {
                    object6.setType("number");
                }
            } else if ("sel_search".equals(string3) || org.jeecg.modules.online.cgform.d.c.a(onlCgformField)) {
                object6 = new org.jeecg.common.util.a.a.a(string, string2, onlCgformField.getDictTable(), onlCgformField.getDictField(), onlCgformField.getDictText());
            } else if (ai.equals(string3)) {
                object6 = new org.jeecg.common.util.a.a.a(string, string2, onlCgformField.getDictTable(), onlCgformField.getDictField(), onlCgformField.getDictText());
                object6.setView(ai);
            } else if (org.jeecg.modules.online.cgform.d.i.a(onlCgformField.getDbType())) {
                object5 = new org.jeecg.common.util.a.a.d(string, string2, "number");
                if (CgformValidPatternEnum.INTEGER.getType().equals(onlCgformField.getFieldValidType())) {
                    ((org.jeecg.common.util.a.b)object5).setPattern(CgformValidPatternEnum.INTEGER.getPattern());
                }
                object6 = object5;
            } else if (M.equals(string3)) {
                object5 = new f(string, string2, onlCgformField.getDictTable(), onlCgformField.getDictText(), onlCgformField.getDictField());
                object4 = onlCgformField.getDictText();
                if (object4 != null && !"".equals(object4)) {
                    object3 = ((String)object4).split(E);
                    object2 = object3;
                    int n = ((JSONObject)object2).length;
                    for (int i2 = 0; i2 < n; ++i2) {
                        JSONObject jSONObject2 = object2[i2];
                        if (org.jeecg.modules.online.cgform.d.c.a((String)jSONObject2, list)) continue;
                        org.jeecg.common.util.a.a.b b2 = new org.jeecg.common.util.a.a.b((String)jSONObject2, (String)jSONObject2);
                        b2.setOrder(onlCgformField.getOrderNum());
                        arrayList2.add(b2);
                    }
                }
                if ((object3 = onlCgformField.getFieldExtendJson()) != null && !"".equals(object3) && (object2 = JSONObject.parseObject((String)object3)).containsKey((Object)"popupMulti")) {
                    ((f)object5).setPopupMulti(object2.getBoolean("popupMulti"));
                }
                object6 = object5;
            } else if (N.equals(string3)) {
                object5 = new e(string, string2, onlCgformField.getDictTable(), onlCgformField.getDictText(), onlCgformField.getDictField());
                object4 = onlCgformField.getFieldExtendJson();
                if (object4 != null && !"".equals(object4) && (object3 = JSONObject.parseObject((String)object4)).containsKey((Object)"popupMulti")) {
                    ((e)object5).setPopupMulti(object3.getBoolean("popupMulti"));
                }
                object6 = object5;
            } else if (T.equals(string3)) {
                object5 = new org.jeecg.common.util.a.a.c(string, string2, onlCgformField.getDictTable());
                org.jeecg.modules.online.cgform.d.c.a((org.jeecg.common.util.a.a.c)object5, list, arrayList3, arrayList);
                object6 = object5;
            } else if (R.equals(string3)) {
                object5 = onlCgformField.getDictText();
                object4 = ((String)object5).split(E);
                object3 = onlCgformField.getDictTable() + E + object4[2] + E + object4[0];
                object2 = new org.jeecg.common.util.a.a.i(string, string2, (String)object3, object4[1], onlCgformField.getDictField());
                if (((String[])object4).length > 3) {
                    ((org.jeecg.common.util.a.a.i)object2).setHasChildField((String)object4[3]);
                }
                object6 = object2;
            } else if (S.equals(string3)) {
                object5 = onlCgformField.getDictText();
                object4 = onlCgformField.getDictField();
                object3 = aX;
                if (oConvertUtils.isNotEmpty((Object)object4) && !aX.equals(object4)) {
                    object3 = onlCgformHeadMapper.queryCategoryIdByCode((String)object4);
                }
                if (oConvertUtils.isEmpty((Object)object5)) {
                    object6 = new org.jeecg.common.util.a.a.i(string, string2, (String)object3);
                } else {
                    object6 = new org.jeecg.common.util.a.a.i(string, string2, (String)object3, (String)object5);
                    object2 = new org.jeecg.common.util.a.a.b((String)object5, (String)object5);
                    arrayList2.add((org.jeecg.common.util.a.b)object2);
                }
            } else if (treeSelectColumn != null && string.equals(treeSelectColumn.getFieldName())) {
                object5 = treeSelectColumn.getTableName() + E + treeSelectColumn.getTextField() + E + treeSelectColumn.getCodeField();
                object4 = new org.jeecg.common.util.a.a.i(string, string2, (String)object5, treeSelectColumn.getPidField(), treeSelectColumn.getPidValue());
                ((org.jeecg.common.util.a.a.i)object4).setHasChildField(treeSelectColumn.getHsaChildField());
                ((org.jeecg.common.util.a.a.i)object4).setPidComponent(1);
                object6 = object4;
            } else {
                object5 = new g(string, string2, string3, onlCgformField.getDbLength());
                object6 = object5;
            }
            if (oConvertUtils.isNotEmpty((Object)onlCgformField.getFieldValidType())) {
                object5 = CgformValidPatternEnum.getPatternInfoByType(onlCgformField.getFieldValidType());
                object4 = org.jeecg.modules.online.cgform.d.c.a("validateError", onlCgformField.getFieldExtendJson());
                if (object5 != null) {
                    if (CgformValidPatternEnum.NOTNULL == object5) {
                        arrayList.add(string);
                    } else {
                        object6.setPattern(((CgformValidPatternEnum)((Object)object5)).getPattern());
                        if (oConvertUtils.isEmpty((Object)object4)) {
                            object6.setErrorInfo(((CgformValidPatternEnum)((Object)object5)).getMsg());
                        } else {
                            object6.setErrorInfo((String)object4);
                        }
                    }
                } else {
                    object6.setPattern(onlCgformField.getFieldValidType());
                    if (oConvertUtils.isEmpty((Object)object4)) {
                        object6.setErrorInfo("\u8f93\u5165\u7684\u503c\u4e0d\u5408\u6cd5");
                    } else {
                        object6.setErrorInfo((String)object4);
                    }
                }
            }
            if (onlCgformField.getIsReadOnly() == 1 || list2 != null && list2.indexOf(string) >= 0) {
                object6.setDisabled(true);
            }
            object6.setOrder(onlCgformField.getOrderNum());
            object6.setDefVal(onlCgformField.getFieldDefaultValue());
            object6.setFieldExtendJson(onlCgformField.getFieldExtendJson());
            object6.setDbPointLength(onlCgformField.getDbPointLength());
            object6.setMode(onlCgformField.getQueryMode());
            arrayList2.add((org.jeecg.common.util.a.b)object6);
        }
        if (arrayList.size() > 0) {
            object = new org.jeecg.common.util.a.c(arrayList);
            jSONObject = org.jeecg.common.util.a.d.a((org.jeecg.common.util.a.c)object, arrayList2);
        } else {
            object = new org.jeecg.common.util.a.c();
            jSONObject = org.jeecg.common.util.a.d.a((org.jeecg.common.util.a.c)object, arrayList2);
        }
        return jSONObject;
    }

    public static String a(String string, String string2) {
        JSONObject jSONObject;
        String string3 = "";
        if (string2 != null && !"".equals(string2) && (jSONObject = JSONObject.parseObject((String)string2)).containsKey((Object)string)) {
            string3 = jSONObject.getString(string);
        }
        return string3;
    }

    public static JSONObject b(String string, List<OnlCgformField> list) {
        JSONObject jSONObject = new JSONObject();
        ArrayList<String> arrayList = new ArrayList<String>();
        ArrayList<org.jeecg.common.util.a.b> arrayList2 = new ArrayList<org.jeecg.common.util.a.b>();
        ISysBaseAPI iSysBaseAPI = (ISysBaseAPI)SpringContextUtils.getBean(ISysBaseAPI.class);
        for (OnlCgformField onlCgformField : list) {
            String string2 = onlCgformField.getDbFieldName();
            if ("id".equals(string2)) continue;
            String string3 = onlCgformField.getDbFieldTxt();
            if ("1".equals(onlCgformField.getFieldMustInput())) {
                arrayList.add(string2);
            }
            String string4 = onlCgformField.getFieldShowType();
            String string5 = onlCgformField.getDictField();
            org.jeecg.common.util.a.b b2 = null;
            if (org.jeecg.modules.online.cgform.d.i.a(onlCgformField.getDbType())) {
                b2 = new org.jeecg.common.util.a.a.d(string2, string3, "number");
            } else if (org.jeecg.modules.online.cgform.d.c.c(string4)) {
                List list2 = iSysBaseAPI.queryDictItemsByCode(string5);
                b2 = new g(string2, string3, string4, onlCgformField.getDbLength(), list2);
            } else {
                b2 = new g(string2, string3, string4, onlCgformField.getDbLength());
            }
            b2.setOrder(onlCgformField.getOrderNum());
            arrayList2.add(b2);
        }
        jSONObject = org.jeecg.common.util.a.d.a(string, arrayList, arrayList2);
        return jSONObject;
    }

    public static Set<String> a(List<OnlCgformField> list) {
        String string;
        HashSet<String> hashSet = new HashSet<String>();
        for (OnlCgformField onlCgformField : list) {
            if (M.equals(onlCgformField.getFieldShowType()) && (string = onlCgformField.getDictText()) != null && !"".equals(string)) {
                hashSet.addAll(Arrays.stream(string.split(E)).collect(Collectors.toSet()));
            }
            if (!S.equals(onlCgformField.getFieldShowType()) || !oConvertUtils.isNotEmpty((Object)(string = onlCgformField.getDictText()))) continue;
            hashSet.add(string);
        }
        for (OnlCgformField onlCgformField : list) {
            string = onlCgformField.getDbFieldName();
            if (onlCgformField.getIsShowForm() != 1 || !hashSet.contains(string)) continue;
            hashSet.remove(string);
        }
        return hashSet;
    }

    public static Map<String, Object> a(String string, List<OnlCgformField> list, JSONObject jSONObject) {
        Object object;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        String string2 = "";
        try {
            string2 = org.jeecg.modules.online.config.c.d.getDatabaseType();
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
        catch (org.jeecg.modules.online.config.exception.a a2) {
            a2.printStackTrace();
        }
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        boolean bl = false;
        String string3 = null;
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            throw new JeecgBootException("online\u4fdd\u5b58\u8868\u5355\u6570\u636e\u5f02\u5e38:\u7cfb\u7edf\u672a\u627e\u5230\u5f53\u524d\u767b\u9646\u7528\u6237\u4fe1\u606f");
        }
        Set<String> set = org.jeecg.modules.online.cgform.d.c.a(list);
        String string4 = "tenant_id";
        String string5 = org.jeecg.modules.online.cgform.d.c.f(string);
        boolean bl2 = org.jeecg.modules.online.cgform.d.c.j(string5);
        for (OnlCgformField onlCgformField : list) {
            String string6;
            String string7;
            if (!org.jeecg.modules.online.cgform.b.b.b.equals(onlCgformField.getDbIsPersist()) || null == (string7 = onlCgformField.getDbFieldName())) continue;
            if ("id".equals(string7.toLowerCase())) {
                bl = true;
                string3 = jSONObject.getString(string7);
                continue;
            }
            if (bl2 && string4.equalsIgnoreCase(string7)) continue;
            org.jeecg.modules.online.cgform.d.c.a(onlCgformField, loginUser, jSONObject, x, w, A);
            if (H.equals(string7.toLowerCase())) {
                stringBuffer.append(E + string7);
                stringBuffer2.append(",'1'");
                continue;
            }
            if (set.contains(string7)) {
                stringBuffer.append(E + string7);
                string6 = org.jeecg.modules.online.cgform.d.i.a(string2, onlCgformField, jSONObject, hashMap);
                stringBuffer2.append(E + string6);
                continue;
            }
            if (onlCgformField.getIsShowForm() != 1 && oConvertUtils.isEmpty((Object)onlCgformField.getMainField()) && oConvertUtils.isEmpty((Object)onlCgformField.getDbDefaultVal())) continue;
            if (oConvertUtils.isEmpty((Object)jSONObject.get((Object)string7))) {
                if (oConvertUtils.isEmpty((Object)onlCgformField.getDbDefaultVal())) continue;
                jSONObject.put(string7, (Object)onlCgformField.getDbDefaultVal());
            }
            if ("".equals(jSONObject.get((Object)string7)) && (org.jeecg.modules.online.cgform.d.i.a(string6 = onlCgformField.getDbType()) || org.jeecg.modules.online.cgform.d.i.b(string6))) continue;
            stringBuffer.append(E + string7);
            string6 = org.jeecg.modules.online.cgform.d.i.a(string2, onlCgformField, jSONObject, hashMap);
            stringBuffer2.append(E + string6);
        }
        if (bl) {
            if (oConvertUtils.isEmpty(string3)) {
                string3 = org.jeecg.modules.online.cgform.d.c.a();
            }
        } else {
            string3 = org.jeecg.modules.online.cgform.d.c.a();
        }
        if (bl2) {
            stringBuffer.append(E + string4);
            stringBuffer2.append(",#{" + string4 + "}");
            object = SpringContextUtils.getHttpServletRequest().getHeader("X-Tenant-Id");
            hashMap.put(string4, object);
        }
        object = "insert into " + string5 + "(" + "id" + stringBuffer.toString() + ") values(#{id,jdbcType=VARCHAR}" + stringBuffer2.toString() + ")";
        hashMap.put("execute_sql_string", object);
        hashMap.put("id", string3);
        return hashMap;
    }

    public static Map<String, Object> b(String string, List<OnlCgformField> list, JSONObject jSONObject) {
        StringBuffer stringBuffer = new StringBuffer();
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        String string2 = "";
        try {
            string2 = org.jeecg.modules.online.config.c.d.getDatabaseType();
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
        catch (org.jeecg.modules.online.config.exception.a a2) {
            a2.printStackTrace();
        }
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            throw new JeecgBootException("online\u4fee\u6539\u8868\u5355\u6570\u636e\u5f02\u5e38:\u7cfb\u7edf\u672a\u627e\u5230\u5f53\u524d\u767b\u9646\u7528\u6237\u4fe1\u606f");
        }
        Set<String> set = org.jeecg.modules.online.cgform.d.c.a(list);
        for (OnlCgformField object2 : list) {
            String string3;
            String string4;
            if (!org.jeecg.modules.online.cgform.b.b.b.equals(object2.getDbIsPersist()) || null == (string4 = object2.getDbFieldName())) continue;
            org.jeecg.modules.online.cgform.d.c.a(object2, loginUser, jSONObject, z, y);
            if (set.contains(string4) && jSONObject.get((Object)string4) != null && !"".equals(jSONObject.getString(string4))) {
                string3 = org.jeecg.modules.online.cgform.d.i.a(string2, object2, jSONObject, hashMap);
                stringBuffer.append(string4 + m + string3 + E);
                continue;
            }
            if (object2.getIsShowForm() != 1 || "id".equals(string4) || "".equals(jSONObject.get((Object)string4)) && (org.jeecg.modules.online.cgform.d.i.a(string3 = object2.getDbType()) || org.jeecg.modules.online.cgform.d.i.b(string3)) || oConvertUtils.isNotEmpty((Object)object2.getMainTable()) && oConvertUtils.isNotEmpty((Object)object2.getMainField()) && oConvertUtils.isEmpty((Object)jSONObject.get((Object)string4))) continue;
            string3 = org.jeecg.modules.online.cgform.d.i.a(string2, object2, jSONObject, hashMap);
            stringBuffer.append(string4 + m + string3 + E);
        }
        Object object3 = stringBuffer.toString();
        if (((String)object3).endsWith(E)) {
            object3 = ((String)object3).substring(0, ((String)object3).length() - 1);
        }
        String string5 = "update " + org.jeecg.modules.online.cgform.d.c.f(string) + " set " + (String)object3 + i + "id" + m + C + jSONObject.getString("id") + C;
        hashMap.put("execute_sql_string", string5);
        hashMap.put("id", jSONObject.getString("id"));
        return hashMap;
    }

    public static QueryWrapper<?> a(List<OnlCgformField> list, String string) {
        return org.jeecg.modules.online.cgform.d.c.a(list, "id", string);
    }

    public static QueryWrapper<?> a(List<OnlCgformField> list, String string, String string2) {
        string = SqlInjectionUtil.getSqlInjectField((String)string);
        QueryWrapper queryWrapper = new QueryWrapper();
        ArrayList<String> arrayList = new ArrayList<String>();
        boolean bl = false;
        for (OnlCgformField onlCgformField : list) {
            if (!org.jeecg.modules.online.cgform.b.b.b.equals(onlCgformField.getDbIsPersist())) continue;
            String string3 = onlCgformField.getDbFieldName();
            if ("id".equals(string3)) {
                bl = true;
            }
            string3 = SqlInjectionUtil.getSqlInjectField((String)string3);
            arrayList.add(string3);
        }
        if (!bl) {
            arrayList.add("id");
        }
        if (!arrayList.isEmpty()) {
            queryWrapper.select((Object[])arrayList.toArray(new String[0]));
        }
        queryWrapper.eq((Object)string, (Object)string2);
        return queryWrapper;
    }

    public static void a(OnlCgformField onlCgformField, LoginUser loginUser, JSONObject jSONObject, String ... stringArray) {
        String string = onlCgformField.getDbFieldName();
        boolean bl = false;
        for (String string2 : stringArray) {
            if (!string.toUpperCase().equals(string2)) continue;
            if (onlCgformField.getIsShowForm() == 1) {
                if (jSONObject.get((Object)string) == null) {
                    bl = true;
                }
            } else {
                onlCgformField.setIsShowForm(1);
                bl = true;
            }
            if (!bl) break;
            if (string2.equals(x)) {
                jSONObject.put(string, (Object)loginUser.getUsername());
                break;
            }
            if (string2.equals(w)) {
                onlCgformField.setFieldShowType("datetime");
                jSONObject.put(string, (Object)DateUtils.formatDateTime());
                break;
            }
            if (string2.equals(z)) {
                jSONObject.put(string, (Object)loginUser.getUsername());
                break;
            }
            if (string2.equals(y)) {
                onlCgformField.setFieldShowType("datetime");
                jSONObject.put(string, (Object)DateUtils.formatDateTime());
                break;
            }
            if (!string2.equals(A)) break;
            jSONObject.put(string, (Object)loginUser.getOrgCode());
            break;
        }
    }

    public static boolean a(Object object, Object object2) {
        if (oConvertUtils.isEmpty((Object)object) && oConvertUtils.isEmpty((Object)object2)) {
            return true;
        }
        return oConvertUtils.isNotEmpty((Object)object) && object.equals(object2);
    }

    public static boolean a(OnlCgformField onlCgformField, OnlCgformField onlCgformField2) {
        if (!org.jeecg.modules.online.cgform.b.b.b.equals(onlCgformField2.getDbIsPersist()) && !org.jeecg.modules.online.cgform.b.b.b.equals(onlCgformField.getDbIsPersist())) {
            return false;
        }
        return !org.jeecg.modules.online.cgform.d.c.a((Object)onlCgformField.getDbFieldName(), (Object)onlCgformField2.getDbFieldName()) || !org.jeecg.modules.online.cgform.d.c.a((Object)onlCgformField.getDbFieldTxt(), (Object)onlCgformField2.getDbFieldTxt()) || !org.jeecg.modules.online.cgform.d.c.a((Object)onlCgformField.getDbLength(), onlCgformField2.getDbLength()) || !org.jeecg.modules.online.cgform.d.c.a((Object)onlCgformField.getDbPointLength(), onlCgformField2.getDbPointLength()) || !org.jeecg.modules.online.cgform.d.c.a((Object)onlCgformField.getDbType(), (Object)onlCgformField2.getDbType()) || !org.jeecg.modules.online.cgform.d.c.a((Object)onlCgformField.getDbIsNull(), onlCgformField2.getDbIsNull()) || !org.jeecg.modules.online.cgform.d.c.a((Object)onlCgformField.getDbIsPersist(), onlCgformField2.getDbIsPersist()) || !org.jeecg.modules.online.cgform.d.c.a((Object)onlCgformField.getDbIsKey(), onlCgformField2.getDbIsKey()) || !org.jeecg.modules.online.cgform.d.c.a((Object)onlCgformField.getDbDefaultVal(), (Object)onlCgformField2.getDbDefaultVal());
    }

    public static boolean a(OnlCgformIndex onlCgformIndex, OnlCgformIndex onlCgformIndex2) {
        return !org.jeecg.modules.online.cgform.d.c.a((Object)onlCgformIndex.getIndexName(), (Object)onlCgformIndex2.getIndexName()) || !org.jeecg.modules.online.cgform.d.c.a((Object)onlCgformIndex.getIndexField(), (Object)onlCgformIndex2.getIndexField()) || !org.jeecg.modules.online.cgform.d.c.a((Object)onlCgformIndex.getIndexType(), (Object)onlCgformIndex2.getIndexType());
    }

    public static boolean a(OnlCgformHead onlCgformHead, OnlCgformHead onlCgformHead2) {
        return !org.jeecg.modules.online.cgform.d.c.a((Object)onlCgformHead.getTableName(), (Object)onlCgformHead2.getTableName()) || !org.jeecg.modules.online.cgform.d.c.a((Object)onlCgformHead.getTableTxt(), (Object)onlCgformHead2.getTableTxt());
    }

    public static String a(String string, List<OnlCgformField> list, Map<String, Object> map) {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        String string2 = string + "@";
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        for (String object : map.keySet()) {
            if (object.startsWith(string2)) {
                hashMap.put(object.replace(string2, ""), map.get(object));
                continue;
            }
            hashMap.put(object, map.get(object));
        }
        for (OnlCgformField onlCgformField : list) {
            String string3;
            boolean bl;
            String string4 = onlCgformField.getDbFieldName();
            String string5 = onlCgformField.getDbType();
            if (onlCgformField.getIsShowList() == 1) {
                stringBuffer2.append(E + string4);
            }
            if (oConvertUtils.isNotEmpty((Object)onlCgformField.getMainField())) {
                bl = !org.jeecg.modules.online.cgform.d.i.a(string5);
                string3 = QueryGenerator.getSingleQueryConditionSql((String)string4, (String)"", hashMap.get(string4), (boolean)bl);
                if (!"".equals(string3)) {
                    stringBuffer.append(e + string3);
                }
            }
            if (onlCgformField.getIsQuery() != 1) continue;
            if (F.equals(onlCgformField.getQueryMode())) {
                if (hashMap.get(string4) == null) continue;
                bl = !org.jeecg.modules.online.cgform.d.i.a(string5);
                string3 = QueryGenerator.getSingleQueryConditionSql((String)string4, (String)"", hashMap.get(string4), (boolean)bl);
                if ("".equals(string3)) continue;
                stringBuffer.append(e + string3);
                continue;
            }
            Object v = hashMap.get(string4 + "_begin");
            if (v != null) {
                stringBuffer.append(e + string4 + o);
                if (org.jeecg.modules.online.cgform.d.i.a(string5)) {
                    stringBuffer.append(v.toString());
                } else {
                    stringBuffer.append(C + v.toString() + C);
                }
            }
            if ((string3 = hashMap.get(string4 + "_end")) == null) continue;
            stringBuffer.append(e + string4 + q);
            if (org.jeecg.modules.online.cgform.d.i.a(string5)) {
                stringBuffer.append(string3.toString());
                continue;
            }
            stringBuffer.append(C + string3.toString() + C);
        }
        String string6 = org.jeecg.modules.online.cgform.d.c.b(string, list, hashMap);
        return "SELECT id" + stringBuffer2.toString() + b + org.jeecg.modules.online.cgform.d.c.f(string) + h + stringBuffer.toString() + (String)string6;
    }

    public static String b(String string, List<OnlCgformField> list, Map<String, Object> map) {
        String string2;
        boolean bl = true;
        JSONArray jSONArray = org.jeecg.modules.online.cgform.d.c.b(map);
        MatchTypeEnum matchTypeEnum = org.jeecg.modules.online.cgform.d.c.c(map);
        StringBuilder stringBuilder = new StringBuilder();
        if (jSONArray != null) {
            for (int i2 = 0; i2 < jSONArray.size(); ++i2) {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                String string3 = jSONObject.getString("field");
                String[] stringArray = string3.split(E);
                if (stringArray.length == 1) continue;
                String string4 = stringArray[1];
                if (!string.equalsIgnoreCase(stringArray[0]) || !org.jeecg.modules.online.cgform.d.c.c(string4, list)) continue;
                org.jeecg.modules.online.cgform.d.c.a(stringBuilder, string4, jSONObject, matchTypeEnum, null, bl);
                bl = false;
            }
        }
        if ((string2 = stringBuilder.toString()) == null || "".equals(string2)) {
            return "";
        }
        return " AND (" + string2 + ") ";
    }

    public static boolean c(String string, List<OnlCgformField> list) {
        boolean bl = false;
        for (OnlCgformField onlCgformField : list) {
            if (!oConvertUtils.camelToUnderline((String)string).equalsIgnoreCase(onlCgformField.getDbFieldName())) continue;
            bl = true;
            break;
        }
        return bl;
    }

    @Deprecated
    public static List<ExcelExportEntity> b(List<OnlCgformField> list, String string) {
        ArrayList<ExcelExportEntity> arrayList = new ArrayList<ExcelExportEntity>();
        for (int i2 = 0; i2 < list.size(); ++i2) {
            int n;
            if (null != string && string.equals(list.get(i2).getDbFieldName()) || list.get(i2).getIsShowList() != 1) continue;
            ExcelExportEntity excelExportEntity = new ExcelExportEntity(list.get(i2).getDbFieldTxt(), (Object)list.get(i2).getDbFieldName());
            int n2 = list.get(i2).getDbLength() == 0 ? 12 : (n = list.get(i2).getDbLength() > 30 ? 30 : list.get(i2).getDbLength());
            if (U.equals(list.get(i2).getFieldShowType())) {
                excelExportEntity.setFormat("yyyy-MM-dd");
            } else if ("datetime".equals(list.get(i2).getFieldShowType())) {
                excelExportEntity.setFormat("yyyy-MM-dd HH:mm:ss");
            }
            if (n < 10) {
                n = 10;
            }
            excelExportEntity.setWidth((double)n);
            arrayList.add(excelExportEntity);
        }
        return arrayList;
    }

    public static boolean a(OnlCgformEnhanceJava onlCgformEnhanceJava) {
        String string = onlCgformEnhanceJava.getCgJavaType();
        String string2 = onlCgformEnhanceJava.getCgJavaValue();
        if (oConvertUtils.isNotEmpty((Object)string2)) {
            try {
                Object object;
                if ("class".equals(string) && ((object = Class.forName(string2)) == null || ((Class)object).newInstance() == null)) {
                    return false;
                }
                if ("spring".equals(string) && (object = SpringContextUtils.getBean((String)string2)) == null) {
                    return false;
                }
            }
            catch (Exception exception) {
                aZ.error(exception.getMessage(), (Throwable)exception);
                return false;
            }
        }
        return true;
    }

    public static void b(List<String> list) {
        Collections.sort(list, new b());
    }

    public static void c(List<String> list) {
        Collections.sort(list, new a());
    }

    public static String a(String string, JSONObject jSONObject) {
        if (jSONObject == null) {
            return string;
        }
        string = string.replace("#{UUID}", UUIDGenerator.generate());
        Set set = QueryGenerator.getSqlRuleParams((String)string);
        for (String string2 : set) {
            String string3;
            if (jSONObject.get((Object)string2.toUpperCase()) == null && jSONObject.get((Object)string2.toLowerCase()) == null) {
                string3 = JwtUtil.getUserSystemData((String)string2, null);
                if (string3 == null) {
                    string = string.replace("'#{" + string2 + "}'", "NULL");
                    string = string.replace("#{" + string2 + "}", "NULL");
                    continue;
                }
                string = string.replace("#{" + string2 + "}", string3);
                continue;
            }
            string3 = null;
            if (jSONObject.containsKey((Object)string2.toLowerCase())) {
                string3 = jSONObject.getString(string2.toLowerCase());
            } else if (jSONObject.containsKey((Object)string2.toUpperCase())) {
                string3 = jSONObject.getString(string2.toUpperCase());
            }
            string = string.replace("#{" + string2 + "}", string3);
        }
        return string;
    }

    public static String d(String string, List<OnlCgformButton> list) {
        string = org.jeecg.modules.online.cgform.d.c.e(string, list);
        for (String string2 : ba.split(E)) {
            Matcher matcher;
            Pattern pattern;
            if ("beforeAdd,afterAdd,mounted,created".indexOf(string2) >= 0) {
                pattern = Pattern.compile("(" + string2 + "\\s*\\(\\)\\s*\\{)");
                matcher = pattern.matcher(string);
                if (!matcher.find()) continue;
                string = string.replace(matcher.group(0), string2 + "(that){const getAction=this._getAction,postAction=this._postAction,deleteAction=this._deleteAction;");
                continue;
            }
            pattern = Pattern.compile("(" + string2 + "\\s*\\(row\\)\\s*\\{)");
            matcher = pattern.matcher(string);
            if (matcher.find()) {
                string = string.replace(matcher.group(0), string2 + "(that,row){const getAction=this._getAction,postAction=this._postAction,deleteAction=this._deleteAction;");
                continue;
            }
            Pattern pattern2 = Pattern.compile("(" + string2 + "\\s*\\(\\)\\s*\\{)");
            Matcher matcher2 = pattern2.matcher(string);
            if (!matcher2.find()) continue;
            string = string.replace(matcher2.group(0), string2 + "(that){const getAction=this._getAction,postAction=this._postAction,deleteAction=this._deleteAction;");
        }
        return org.jeecg.modules.online.cgform.d.c.d(string);
    }

    public static void a(OnlCgformEnhanceJs onlCgformEnhanceJs, String string, List<OnlCgformField> list) {
        if (onlCgformEnhanceJs == null || oConvertUtils.isEmpty((Object)onlCgformEnhanceJs.getCgJs())) {
            return;
        }
        String string2 = onlCgformEnhanceJs.getCgJs();
        String string3 = "onlChange";
        Pattern pattern = Pattern.compile("(" + string + "_" + string3 + "\\s*\\(\\)\\s*\\{)");
        Matcher matcher = pattern.matcher(string2);
        if (matcher.find()) {
            string2 = string2.replace(matcher.group(0), string + "_" + string3 + "(){const getAction=this._getAction,postAction=this._postAction,deleteAction=this._deleteAction;");
            for (OnlCgformField onlCgformField : list) {
                Pattern pattern2 = Pattern.compile("(" + onlCgformField.getDbFieldName() + "\\s*\\(\\))");
                Matcher matcher2 = pattern2.matcher(string2);
                if (!matcher2.find()) continue;
                string2 = string2.replace(matcher2.group(0), onlCgformField.getDbFieldName() + "(that,event)");
            }
        }
        onlCgformEnhanceJs.setCgJs(string2);
    }

    public static void a(OnlCgformEnhanceJs onlCgformEnhanceJs, String string, List<OnlCgformField> list, boolean bl) {
        if (onlCgformEnhanceJs == null || oConvertUtils.isEmpty((Object)onlCgformEnhanceJs.getCgJs())) {
            return;
        }
        String string2 = onlCgformEnhanceJs.getCgJs();
        String string3 = "onlChange";
        Pattern pattern = Pattern.compile("([^_]" + string3 + "\\s*\\(\\)\\s*\\{)");
        Matcher matcher = pattern.matcher(string2);
        if (matcher.find()) {
            string2 = string2.replace(matcher.group(0), string3 + "(){const getAction=this._getAction,postAction=this._postAction,deleteAction=this._deleteAction;");
            for (OnlCgformField onlCgformField : list) {
                Pattern pattern2 = Pattern.compile("(" + onlCgformField.getDbFieldName() + "\\s*\\(\\))");
                Matcher matcher2 = pattern2.matcher(string2);
                if (!matcher2.find()) continue;
                string2 = string2.replace(matcher2.group(0), onlCgformField.getDbFieldName() + "(that,event)");
            }
        }
        onlCgformEnhanceJs.setCgJs(string2);
        org.jeecg.modules.online.cgform.d.c.a(onlCgformEnhanceJs);
        org.jeecg.modules.online.cgform.d.c.a(onlCgformEnhanceJs, string, list);
    }

    public static void a(OnlCgformEnhanceJs onlCgformEnhanceJs) {
        String string = onlCgformEnhanceJs.getCgJs();
        String string2 = "show";
        Pattern pattern = Pattern.compile("(" + string2 + "\\s*\\(\\)\\s*\\{)");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            string = string.replace(matcher.group(0), string2 + "(that){const getAction=this._getAction,postAction=this._postAction,deleteAction=this._deleteAction;");
        }
        onlCgformEnhanceJs.setCgJs(string);
    }

    public static String d(String string) {
        return "class OnlineEnhanceJs{constructor(getAction,postAction,deleteAction){this._getAction=getAction;this._postAction=postAction;this._deleteAction=deleteAction;}" + string + "}";
    }

    public static String e(String string, List<OnlCgformButton> list) {
        if (list != null) {
            for (OnlCgformButton onlCgformButton : list) {
                Matcher matcher;
                Pattern pattern;
                String string2 = onlCgformButton.getButtonCode();
                if ("link".equals(onlCgformButton.getButtonStyle())) {
                    pattern = Pattern.compile("(" + string2 + "\\s*\\(row\\)\\s*\\{)");
                    matcher = pattern.matcher(string);
                    if (matcher.find()) {
                        string = string.replace(matcher.group(0), string2 + "(that,row){const getAction=this._getAction,postAction=this._postAction,deleteAction=this._deleteAction;");
                        continue;
                    }
                    Pattern pattern2 = Pattern.compile("(" + string2 + "\\s*\\(\\)\\s*\\{)");
                    Matcher matcher2 = pattern2.matcher(string);
                    if (!matcher2.find()) continue;
                    string = string.replace(matcher2.group(0), string2 + "(that){const getAction=this._getAction,postAction=this._postAction,deleteAction=this._deleteAction;");
                    continue;
                }
                if (!"button".equals(onlCgformButton.getButtonStyle()) && !aq.equals(onlCgformButton.getButtonStyle()) || !(matcher = (pattern = Pattern.compile("(" + string2 + "\\s*\\(\\)\\s*\\{)")).matcher(string)).find()) continue;
                string = string.replace(matcher.group(0), string2 + "(that){const getAction=this._getAction,postAction=this._postAction,deleteAction=this._deleteAction;");
            }
        }
        return string;
    }

    public static JSONArray a(List<OnlCgformField> list, List<String> list2) {
        JSONArray jSONArray = new JSONArray();
        ISysBaseAPI iSysBaseAPI = (ISysBaseAPI)SpringContextUtils.getBean(ISysBaseAPI.class);
        for (OnlCgformField onlCgformField : list) {
            String[] stringArray;
            Object object;
            String string = onlCgformField.getDbFieldName();
            if ("id".equals(string)) continue;
            JSONObject jSONObject = new JSONObject();
            if (list2 != null && list2.indexOf(string) >= 0) {
                jSONObject.put("disabled", (Object)true);
            }
            if (onlCgformField.getIsReadOnly() != null && 1 == onlCgformField.getIsReadOnly()) {
                jSONObject.put("disabled", (Object)true);
            }
            jSONObject.put(aR, (Object)onlCgformField.getDbFieldTxt());
            jSONObject.put("key", (Object)string);
            String string2 = org.jeecg.modules.online.cgform.d.c.d(onlCgformField);
            jSONObject.put("type", (Object)string2);
            if (onlCgformField.getFieldLength() == null) {
                onlCgformField.setFieldLength(186);
            }
            if (("sel_depart".equals(string2) || "sel_user".equals(string2)) && onlCgformField.getFieldLength() < 170) {
                jSONObject.put("width", (Object)"170px");
            } else if (U.equals(string2) && onlCgformField.getFieldLength() < 140) {
                jSONObject.put("width", (Object)"140px");
            } else if ("datetime".equals(string2) && onlCgformField.getFieldLength() < 190) {
                jSONObject.put("width", (Object)"190px");
            } else {
                jSONObject.put("width", (Object)(onlCgformField.getFieldLength() + "px"));
            }
            if (Q.equals(string2) || P.equals(string2)) {
                jSONObject.put("responseName", (Object)"message");
                jSONObject.put("token", (Object)true);
            }
            if (L.equals(string2)) {
                block20: {
                    jSONObject.put("type", (Object)"checkbox");
                    object = new JSONArray();
                    object.add((Object)u);
                    object.add((Object)D);
                    if (oConvertUtils.isNotEmpty((Object)onlCgformField.getFieldExtendJson())) {
                        try {
                            object = JSONArray.parseArray((String)onlCgformField.getFieldExtendJson());
                        }
                        catch (Exception exception) {
                            stringArray = JSONArray.parseObject((String)onlCgformField.getFieldExtendJson());
                            if (!stringArray.containsKey((Object)aV)) break block20;
                            object = stringArray.getJSONArray(aV);
                        }
                    }
                }
                jSONObject.put("customValue", object);
            }
            if (M.equals(string2)) {
                jSONObject.put("popupCode", (Object)onlCgformField.getDictTable());
                jSONObject.put("orgFields", (Object)onlCgformField.getDictField());
                jSONObject.put("destFields", (Object)onlCgformField.getDictText());
                object = onlCgformField.getDictText();
                if (object != null && !"".equals(object)) {
                    ArrayList<String> arrayList = new ArrayList<String>();
                    for (String string3 : stringArray = ((String)object).split(E)) {
                        if (org.jeecg.modules.online.cgform.d.c.a(string3, list)) continue;
                        arrayList.add(string3);
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.put(aR, (Object)string3);
                        jSONObject2.put("key", (Object)string3);
                        jSONObject2.put("type", (Object)"hidden");
                        jSONArray.add((Object)jSONObject2);
                    }
                }
            }
            jSONObject.put("defaultValue", (Object)onlCgformField.getDbDefaultVal());
            jSONObject.put("fieldDefaultValue", (Object)onlCgformField.getFieldDefaultValue());
            jSONObject.put("placeholder", (Object)("\u8bf7\u8f93\u5165" + onlCgformField.getDbFieldTxt()));
            jSONObject.put("validateRules", (Object)org.jeecg.modules.online.cgform.d.c.c(onlCgformField));
            if (ar.equals(onlCgformField.getFieldShowType()) || "radio".equals(onlCgformField.getFieldShowType()) || "checkbox_meta".equals(onlCgformField.getFieldShowType()) || "list_multi".equals(onlCgformField.getFieldShowType()) || "sel_search".equals(onlCgformField.getFieldShowType())) {
                jSONObject.put(aS, (Object)onlCgformField.getFieldShowType());
                jSONObject.put("dictTable", (Object)onlCgformField.getDictTable());
                jSONObject.put("dictText", (Object)onlCgformField.getDictText());
                jSONObject.put("dictCode", (Object)onlCgformField.getDictField());
                if ("list_multi".equals(onlCgformField.getFieldShowType())) {
                    jSONObject.put("width", (Object)"230px");
                }
            }
            jSONObject.put("fieldExtendJson", (Object)onlCgformField.getFieldExtendJson());
            jSONArray.add((Object)jSONObject);
        }
        return jSONArray;
    }

    private static JSONArray c(OnlCgformField onlCgformField) {
        JSONObject jSONObject;
        JSONArray jSONArray = new JSONArray();
        if (onlCgformField.getDbIsNull() == 0 || "1".equals(onlCgformField.getFieldMustInput())) {
            jSONObject = new JSONObject();
            jSONObject.put("required", (Object)true);
            jSONObject.put("message", (Object)(onlCgformField.getDbFieldTxt() + "\u4e0d\u80fd\u4e3a\u7a7a!"));
            jSONArray.add((Object)jSONObject);
        }
        if (oConvertUtils.isNotEmpty((Object)onlCgformField.getFieldValidType())) {
            jSONObject = new JSONObject();
            if ("only".equals(onlCgformField.getFieldValidType())) {
                jSONObject.put("unique", (Object)true);
                jSONObject.put("message", (Object)(onlCgformField.getDbFieldTxt() + "\u4e0d\u80fd\u91cd\u590d"));
            } else {
                jSONObject.put("pattern", (Object)onlCgformField.getFieldValidType());
                String string = org.jeecg.modules.online.cgform.d.c.a("validateError", onlCgformField.getFieldExtendJson());
                if (oConvertUtils.isEmpty((Object)string)) {
                    CgformValidPatternEnum cgformValidPatternEnum = CgformValidPatternEnum.getPatternInfoByType(onlCgformField.getFieldValidType());
                    if (null == cgformValidPatternEnum) {
                        jSONObject.put("message", (Object)(onlCgformField.getDbFieldTxt() + "\u683c\u5f0f\u4e0d\u6b63\u786e"));
                    } else {
                        jSONObject.put("message", (Object)cgformValidPatternEnum.getMsg());
                    }
                } else {
                    jSONObject.put("message", (Object)string);
                }
            }
            jSONArray.add((Object)jSONObject);
        }
        return jSONArray;
    }

    public static Map<String, Object> a(Map<String, Object> map) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        if (map == null || map.isEmpty()) {
            return hashMap;
        }
        Set<String> set = map.keySet();
        for (String string : set) {
            Object object;
            Object object2 = map.get(string);
            if (object2 instanceof Clob) {
                object2 = org.jeecg.modules.online.cgform.d.c.a((Clob)object2);
            } else if (object2 instanceof byte[]) {
                object2 = new String((byte[])object2);
            } else if (object2 instanceof Blob) {
                try {
                    if (object2 != null) {
                        object = (Blob)object2;
                        object2 = new String(object.getBytes(1L, (int)object.length()), "UTF-8");
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            object = string.toLowerCase();
            hashMap.put((String)object, object2 == null ? "" : object2);
        }
        return hashMap;
    }

    public static JSONObject a(JSONObject jSONObject) {
        if (org.jeecg.modules.online.config.c.d.a()) {
            JSONObject jSONObject2 = new JSONObject();
            if (jSONObject == null || jSONObject.isEmpty()) {
                return jSONObject2;
            }
            Set set = jSONObject.keySet();
            for (String string : set) {
                String string2 = string.toLowerCase();
                jSONObject2.put(string2, jSONObject.get((Object)string));
            }
            return jSONObject2;
        }
        return jSONObject;
    }

    public static List<Map<String, Object>> a(JSONArray jSONArray) {
        List<Map<String, Object>> list = jSONArray.stream().map(object -> (JSONObject)object).collect(Collectors.toList());
        return org.jeecg.modules.online.cgform.d.c.a(list, null);
    }

    public static List<Map<String, Object>> d(List<Map<String, Object>> list) {
        return org.jeecg.modules.online.cgform.d.c.a(list, null);
    }

    public static List<Map<String, Object>> a(List<Map<String, Object>> list, Collection<String> collection) {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : list) {
            if (map == null) continue;
            HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
            Set<String> set = map.keySet();
            for (String string : set) {
                Object object;
                Object object2 = map.get(string);
                if (object2 instanceof Clob) {
                    object2 = org.jeecg.modules.online.cgform.d.c.a((Clob)object2);
                } else if (object2 instanceof byte[]) {
                    object2 = new String((byte[])object2);
                } else if (object2 instanceof Long) {
                    if (object2 != null) {
                        object2 = String.valueOf(object2);
                    }
                } else if (object2 instanceof Blob) {
                    try {
                        if (object2 != null) {
                            object = (Blob)object2;
                            object2 = new String(object.getBytes(1L, (int)object.length()), "UTF-8");
                        }
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                object = string.toLowerCase();
                hashMap.put((String)object, object2 == null ? "" : object2);
            }
            String string = org.jeecg.modules.online.cgform.d.c.a(hashMap, collection);
            hashMap.put(t, string);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    private static String a(Map<String, Object> map, Collection<String> collection) {
        String string;
        String string2 = string = map.containsKey("id") ? map.get("id").toString() : null;
        if (oConvertUtils.isNotEmpty((Object)string) && collection != null) {
            for (String string3 : collection) {
                String string4 = string3.toLowerCase() + "_id";
                Object object = map.get(string4);
                if (object == null) {
                    string = string + "@";
                    continue;
                }
                string = string + "@" + object.toString();
            }
        }
        return string;
    }

    public static String a(Clob clob) {
        String string = "";
        try {
            Reader reader = clob.getCharacterStream();
            char[] cArray = new char[(int)clob.length()];
            reader.read(cArray);
            string = new String(cArray);
            reader.close();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
        return string;
    }

    public static Map<String, Object> c(String string, List<OnlCgformField> list, JSONObject jSONObject) {
        Object object;
        String string2;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        String string3 = "";
        try {
            string3 = org.jeecg.modules.online.config.c.d.getDatabaseType();
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
        catch (org.jeecg.modules.online.config.exception.a a2) {
            a2.printStackTrace();
        }
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        boolean bl = false;
        String string4 = null;
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            string2 = JwtUtil.getUserNameByToken((HttpServletRequest)SpringContextUtils.getHttpServletRequest());
            if (oConvertUtils.isNotEmpty((Object)string2)) {
                loginUser = new LoginUser();
                loginUser.setUsername(string2);
            } else {
                throw new JeecgBootException("online\u4fdd\u5b58\u8868\u5355\u6570\u636e\u5f02\u5e38:\u7cfb\u7edf\u672a\u627e\u5230\u5f53\u524d\u767b\u9646\u7528\u6237\u4fe1\u606f");
            }
        }
        string2 = "tenant_id";
        String string5 = SqlInjectionUtil.getSqlInjectTableName((String)org.jeecg.modules.online.cgform.d.c.f(string));
        boolean bl2 = org.jeecg.modules.online.cgform.d.c.j(string5);
        for (OnlCgformField onlCgformField : list) {
            String string6;
            String string7 = SqlInjectionUtil.getSqlInjectField((String)onlCgformField.getDbFieldName());
            if (null == string7 || jSONObject.get((Object)string7) == null && !x.equalsIgnoreCase(string7) && !w.equalsIgnoreCase(string7) && !A.equalsIgnoreCase(string7) || bl2 && string2.equalsIgnoreCase(string7)) continue;
            org.jeecg.modules.online.cgform.d.c.a(onlCgformField, loginUser, jSONObject, x, w, A);
            if ("".equals(jSONObject.get((Object)string7)) && (org.jeecg.modules.online.cgform.d.i.a(string6 = onlCgformField.getDbType()) || org.jeecg.modules.online.cgform.d.i.b(string6))) continue;
            if ("id".equals(string7.toLowerCase())) {
                bl = true;
                string4 = jSONObject.getString(string7);
                continue;
            }
            if (aj.equals(onlCgformField.getFieldShowType()) && !org.jeecg.modules.online.cgform.b.b.b.equals(onlCgformField.getDbIsPersist())) continue;
            stringBuffer.append(E + string7);
            string6 = org.jeecg.modules.online.cgform.d.i.a(string3, onlCgformField, jSONObject, hashMap);
            stringBuffer2.append(E + string6);
        }
        if (!bl || oConvertUtils.isEmpty(string4)) {
            string4 = org.jeecg.modules.online.cgform.d.c.a();
        }
        if (bl2) {
            stringBuffer.append(E + string2);
            stringBuffer2.append(",#{" + string2 + "}");
            object = SpringContextUtils.getHttpServletRequest().getHeader("X-Tenant-Id");
            hashMap.put(string2, object);
        }
        object = "insert into " + string5 + "(" + "id" + stringBuffer.toString() + ") values(" + C + string4 + C + stringBuffer2.toString() + ")";
        hashMap.put("execute_sql_string", object);
        aZ.info("--\u8868\u5355\u8bbe\u8ba1\u5668\u8868\u5355\u4fdd\u5b58sql-->" + (String)object);
        hashMap.put("id", string4);
        return hashMap;
    }

    public static Map<String, Object> d(String string, List<OnlCgformField> list, JSONObject jSONObject) {
        Object object2;
        StringBuffer stringBuffer = new StringBuffer();
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        String string2 = "";
        try {
            string2 = org.jeecg.modules.online.config.c.d.getDatabaseType();
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
        catch (org.jeecg.modules.online.config.exception.a a2) {
            a2.printStackTrace();
        }
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            object2 = JwtUtil.getUserNameByToken((HttpServletRequest)SpringContextUtils.getHttpServletRequest());
            if (oConvertUtils.isNotEmpty((Object)object2)) {
                loginUser = new LoginUser();
                loginUser.setUsername((String)object2);
            } else {
                throw new JeecgBootException("online\u4fdd\u5b58\u8868\u5355\u6570\u636e\u5f02\u5e38:\u7cfb\u7edf\u672a\u627e\u5230\u5f53\u524d\u767b\u9646\u7528\u6237\u4fe1\u606f");
            }
        }
        for (OnlCgformField object3 : list) {
            String string3;
            if (!org.jeecg.modules.online.cgform.b.b.b.equals(object3.getDbIsPersist())) continue;
            String string4 = object3.getDbFieldName();
            if (null == string4) {
                aZ.info("--------online\u4fee\u6539\u8868\u5355\u6570\u636e\u9047\u89c1\u7a7a\u540d\u79f0\u7684\u5b57\u6bb5------->>" + object3.getId());
                continue;
            }
            if ("id".equals(string4) || jSONObject.get((Object)string4) == null && !z.equalsIgnoreCase(string4) && !y.equalsIgnoreCase(string4) && !A.equalsIgnoreCase(string4)) continue;
            org.jeecg.modules.online.cgform.d.c.a(object3, loginUser, jSONObject, z, y, A);
            if ("".equals(jSONObject.get((Object)string4)) && (org.jeecg.modules.online.cgform.d.i.a(string3 = object3.getDbType()) || org.jeecg.modules.online.cgform.d.i.b(string3))) continue;
            string3 = org.jeecg.modules.online.cgform.d.i.a(string2, object3, jSONObject, hashMap);
            stringBuffer.append(string4 + m + string3 + E);
        }
        object2 = stringBuffer.toString();
        if (((String)object2).endsWith(E)) {
            object2 = ((String)object2).substring(0, ((String)object2).length() - 1);
        }
        String string5 = "update " + org.jeecg.modules.online.cgform.d.c.f(string) + " set " + (String)object2 + i + "id" + m + C + jSONObject.getString("id") + C;
        aZ.info("--\u8868\u5355\u8bbe\u8ba1\u5668\u8868\u5355\u7f16\u8f91sql-->" + string5);
        hashMap.put("execute_sql_string", string5);
        hashMap.put("id", jSONObject.getString("id"));
        return hashMap;
    }

    public static Map<String, Object> a(String string, String string2, String string3) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        String string4 = "update " + org.jeecg.modules.online.cgform.d.c.f(string) + " set " + string2 + m + C + 0 + C + i + "id" + m + C + string3 + C;
        aZ.info("--\u4fee\u6539\u6811\u8282\u70b9\u72b6\u6001\uff1a\u4e3a\u65e0\u5b50\u8282\u70b9sql-->" + string4);
        hashMap.put("execute_sql_string", string4);
        return hashMap;
    }

    public static String e(String string) {
        if (string == null || "".equals(string) || aX.equals(string)) {
            return "";
        }
        return "CODE like '" + string + "%" + C;
    }

    public static String f(String string) {
        String string2 = Pattern.matches("^[a-zA-z].*\\$\\d+$", string) ? string.substring(0, string.lastIndexOf(v)) : string;
        return SqlInjectionUtil.getSqlInjectTableName((String)string2);
    }

    public static void a(org.jeecg.common.util.a.a.c c2, List<OnlCgformField> list, List<String> list2, List<String> list3) {
        String string = c2.getDictTable();
        JSONObject jSONObject = JSONObject.parseObject((String)string);
        String string2 = jSONObject.getString("linkField");
        ArrayList<org.jeecg.common.util.a.a> arrayList = new ArrayList<org.jeecg.common.util.a.a>();
        if (oConvertUtils.isNotEmpty((Object)string2)) {
            String[] stringArray = string2.split(E);
            block0: for (OnlCgformField onlCgformField : list) {
                String string3 = onlCgformField.getDbFieldName();
                for (String string4 : stringArray) {
                    if (!string4.equals(string3)) continue;
                    list2.add(string3);
                    if ("1".equals(onlCgformField.getFieldMustInput())) {
                        list3.add(string3);
                    }
                    arrayList.add(new org.jeecg.common.util.a.a(onlCgformField.getDbFieldTxt(), string3, onlCgformField.getOrderNum(), onlCgformField.getFieldDefaultValue()));
                    continue block0;
                }
            }
        }
        c2.setOtherColumns(arrayList);
    }

    public static String a(byte[] byArray, String string, String string2, String string3) {
        return CommonUtils.uploadOnlineImage((byte[])byArray, (String)string, (String)string2, (String)string3);
    }

    public static List<String> e(List<OnlCgformField> list) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (OnlCgformField onlCgformField : list) {
            if (!P.equals(onlCgformField.getFieldShowType())) continue;
            arrayList.add(onlCgformField.getDbFieldTxt());
        }
        return arrayList;
    }

    public static List<String> c(List<OnlCgformField> list, String string) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (OnlCgformField onlCgformField : list) {
            if (!P.equals(onlCgformField.getFieldShowType())) continue;
            arrayList.add(string + "_" + onlCgformField.getDbFieldTxt());
        }
        return arrayList;
    }

    public static String a() {
        long l2 = IdWorker.getId();
        return String.valueOf(l2);
    }

    public static String a(Exception exception) {
        String string;
        String string2 = string = exception.getCause() != null ? exception.getCause().getMessage() : exception.getMessage();
        if (string == null) {
            return "\u672a\u77e5\u9519\u8bef";
        }
        if (string.indexOf("ORA-01452") != -1) {
            string = "ORA-01452: \u65e0\u6cd5 CREATE UNIQUE INDEX; \u627e\u5230\u91cd\u590d\u7684\u5173\u952e\u5b57";
        } else if (string.indexOf("duplicate key") != -1) {
            string = "\u65e0\u6cd5 CREATE UNIQUE INDEX; \u627e\u5230\u91cd\u590d\u7684\u5173\u952e\u5b57";
        }
        return string;
    }

    public static List<DictModel> b(OnlCgformField onlCgformField) {
        JSONObject jSONObject;
        JSONArray jSONArray;
        String string;
        String string2;
        ArrayList<DictModel> arrayList;
        block3: {
            arrayList = new ArrayList<DictModel>();
            String string3 = onlCgformField.getFieldExtendJson();
            string2 = "\u662f";
            string = "\u5426";
            jSONArray = JSONArray.parseArray((String)"[\"Y\",\"N\"]");
            if (oConvertUtils.isNotEmpty((Object)string3)) {
                try {
                    jSONArray = JSONArray.parseArray((String)string3);
                }
                catch (JSONException jSONException) {
                    jSONObject = JSONArray.parseObject((String)string3);
                    if (!jSONObject.containsKey((Object)aV)) break block3;
                    jSONArray = jSONObject.getJSONArray(aV);
                }
            }
        }
        DictModel dictModel = new DictModel(jSONArray.getString(0), string2);
        jSONObject = new DictModel(jSONArray.getString(1), string);
        arrayList.add(dictModel);
        arrayList.add((DictModel)jSONObject);
        return arrayList;
    }

    private static String d(OnlCgformField onlCgformField) {
        if ("checkbox".equals(onlCgformField.getFieldShowType())) {
            return "checkbox";
        }
        if ("pca".equals(onlCgformField.getFieldShowType())) {
            return "pca";
        }
        if (ar.equals(onlCgformField.getFieldShowType())) {
            return "select";
        }
        if (L.equals(onlCgformField.getFieldShowType())) {
            return L;
        }
        if ("sel_user".equals(onlCgformField.getFieldShowType())) {
            return "sel_user";
        }
        if ("sel_depart".equals(onlCgformField.getFieldShowType())) {
            return "sel_depart";
        }
        if ("textarea".equals(onlCgformField.getFieldShowType())) {
            return "textarea";
        }
        if (P.equals(onlCgformField.getFieldShowType()) || Q.equals(onlCgformField.getFieldShowType()) || "radio".equals(onlCgformField.getFieldShowType()) || M.equals(onlCgformField.getFieldShowType()) || "list_multi".equals(onlCgformField.getFieldShowType()) || "sel_search".equals(onlCgformField.getFieldShowType())) {
            return onlCgformField.getFieldShowType();
        }
        if ("datetime".equals(onlCgformField.getFieldShowType())) {
            return "datetime";
        }
        if (U.equals(onlCgformField.getFieldShowType())) {
            return U;
        }
        if ("time".equals(onlCgformField.getFieldShowType())) {
            return "time";
        }
        if ("int".equals(onlCgformField.getDbType())) {
            return "inputNumber";
        }
        if ("double".equals(onlCgformField.getDbType()) || "BigDecimal".equals(onlCgformField.getDbType())) {
            return "inputNumber";
        }
        return "input";
    }

    public static String getDatabseType() {
        if (oConvertUtils.isNotEmpty((Object)bb)) {
            return bb;
        }
        try {
            bb = org.jeecg.modules.online.config.c.d.getDatabaseType();
            return bb;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return bb;
        }
    }

    public static List<String> f(List<String> list) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string : list) {
            arrayList.add(string.toLowerCase());
        }
        return arrayList;
    }

    private static String b(String string, String string2) {
        String string3 = "";
        if (string2 == null || "".equals(string2)) {
            return string3;
        }
        String[] stringArray = string2.split(E);
        for (int i2 = 0; i2 < stringArray.length; ++i2) {
            if (i2 > 0) {
                string3 = string3 + e;
            }
            string3 = string3 + string + f;
            if ("SQLSERVER".equals(org.jeecg.modules.online.cgform.d.c.getDatabseType())) {
                string3 = string3 + D;
            }
            string3 = string3 + "'%" + stringArray[i2] + "%" + C;
        }
        aZ.info(" POPUP fieldSql: " + string3);
        return string3;
    }

    public static String a(String string, String string2, StringBuffer stringBuffer) {
        String string3 = "logs" + File.separator + ((SimpleDateFormat)DateUtils.yyyyMMdd.get()).format(new Date()) + File.separator;
        String string4 = string + File.separator + string3;
        File file = new File(string4);
        if (!file.exists()) {
            file.mkdirs();
        }
        String string5 = string2 + Math.round(Math.random() * 10000.0);
        String string6 = string4 + string5 + ".txt";
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(string6));
            bufferedWriter.write(stringBuffer.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        catch (Exception exception) {
            aZ.info("excel\u5bfc\u5165\u751f\u6210\u9519\u8bef\u65e5\u5fd7\u6587\u4ef6\u5f02\u5e38:" + exception.getMessage());
        }
        return "/sys/common/static/" + string3 + string5 + ".txt";
    }

    public static JSONObject b(JSONObject jSONObject) {
        JSONObject jSONObject2;
        JSONObject jSONObject3;
        if (jSONObject.containsKey((Object)aQ)) {
            jSONObject3 = jSONObject.getJSONObject(aQ);
        } else {
            jSONObject2 = jSONObject.getJSONObject("schema");
            jSONObject3 = jSONObject2.getJSONObject(aQ);
        }
        jSONObject2 = (ISysBaseAPI)SpringContextUtils.getBean(ISysBaseAPI.class);
        for (String string : jSONObject3.keySet()) {
            JSONObject jSONObject4;
            String string2;
            String string3;
            JSONObject jSONObject5 = jSONObject3.getJSONObject(string);
            String string4 = jSONObject5.getString(aS);
            if (org.jeecg.modules.online.cgform.d.c.c(string4)) {
                string3 = jSONObject5.getString("dictCode");
                string2 = jSONObject5.getString("dictText");
                String string5 = jSONObject5.getString("dictTable");
                jSONObject4 = new ArrayList();
                if (oConvertUtils.isNotEmpty((Object)string5)) {
                    jSONObject4 = jSONObject2.queryTableDictItemsByCode(string5, string2, string3);
                } else if (oConvertUtils.isNotEmpty((Object)string3)) {
                    jSONObject4 = jSONObject2.queryEnableDictItemsByCode(string3);
                }
                if (jSONObject4 == null || jSONObject4.size() <= 0) continue;
                jSONObject5.put("enum", jSONObject4.stream().filter(Objects::nonNull).collect(Collectors.toList()));
                continue;
            }
            if (!"tab".equals(string4)) continue;
            string3 = jSONObject5.getString("relationType");
            if ("1".equals(string3)) {
                org.jeecg.modules.online.cgform.d.c.b(jSONObject5);
                continue;
            }
            string2 = jSONObject5.getJSONArray("columns");
            for (int i2 = 0; i2 < string2.size(); ++i2) {
                jSONObject4 = string2.getJSONObject(i2);
                if (!org.jeecg.modules.online.cgform.d.c.c(jSONObject4)) continue;
                String string6 = jSONObject4.getString("dictCode");
                String string7 = jSONObject4.getString("dictText");
                String string8 = jSONObject4.getString("dictTable");
                List list = new ArrayList();
                if (oConvertUtils.isNotEmpty((Object)string8)) {
                    list = jSONObject2.queryTableDictItemsByCode(string8, string7, string6);
                } else if (oConvertUtils.isNotEmpty((Object)string6)) {
                    list = jSONObject2.queryEnableDictItemsByCode(string6);
                }
                if (list == null || list.size() <= 0) continue;
                jSONObject4.put("options", list);
            }
        }
        return jSONObject;
    }

    private static boolean c(JSONObject jSONObject) {
        String string;
        Object object = jSONObject.get((Object)aS);
        return object != null && (ar.equals(string = object.toString()) || "radio".equals(string) || "checkbox_meta".equals(string) || "list_multi".equals(string) || "sel_search".equals(string));
    }

    public static JSONArray b(Map<String, Object> map) {
        Object object = map.get("superQueryParams");
        if (object != null) {
            try {
                String string = URLDecoder.decode(object.toString(), "UTF-8");
                JSONArray jSONArray = JSONArray.parseArray((String)string);
                return jSONArray;
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                aZ.error("\u9ad8\u7ea7\u67e5\u8be2json\u53c2\u6570\u8f6c\u6362\u5931\u8d25" + unsupportedEncodingException.getMessage());
            }
        }
        return null;
    }

    public static MatchTypeEnum c(Map<String, Object> map) {
        Object object = map.get("superQueryMatchType");
        MatchTypeEnum matchTypeEnum = MatchTypeEnum.getByValue((Object)object);
        if (matchTypeEnum == null) {
            matchTypeEnum = MatchTypeEnum.AND;
        }
        return matchTypeEnum;
    }

    public static boolean g(String string) {
        for (int i2 = 0; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if (c2 == '.' || c2 == '-' || c2 == '+' || Character.isDigit(c2)) continue;
            return false;
        }
        return true;
    }

    public static List<OnlineFieldConfig> g(List<OnlCgformField> list) {
        ArrayList<OnlineFieldConfig> arrayList = new ArrayList<OnlineFieldConfig>();
        for (OnlCgformField onlCgformField : list) {
            if (!org.jeecg.modules.online.cgform.b.b.b.equals(onlCgformField.getDbIsPersist())) continue;
            arrayList.add(new OnlineFieldConfig(onlCgformField));
        }
        return arrayList;
    }

    public static String a(Map<String, Object> map, String string) {
        Object object = org.jeecg.modules.online.cgform.d.c.b(map, string);
        if (object != null) {
            return object.toString();
        }
        return null;
    }

    public static Object b(Map<String, Object> map, String string) {
        if (map == null || oConvertUtils.isEmpty((Object)string)) {
            return null;
        }
        Object object = map.get(string);
        if (object != null) {
            return object;
        }
        Object object2 = map.get(string.toUpperCase());
        if (object2 != null) {
            return object2;
        }
        Object object3 = map.get(string.toLowerCase());
        if (object3 != null) {
            return object3;
        }
        return null;
    }

    public static List<String> h(String string) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (oConvertUtils.isNotEmpty((Object)string)) {
            String[] stringArray;
            for (String string2 : stringArray = string.split(E)) {
                int n = string2.indexOf("@");
                if (n > 0) {
                    String string3 = string2.substring(0, n);
                    arrayList.add(string3);
                    continue;
                }
                arrayList.add(string2);
            }
        }
        return arrayList;
    }

    public static Map<String, List<String>> f(String string, List<String> list) {
        HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>(5);
        hashMap.put("id", new ArrayList());
        if (list != null) {
            for (int i2 = 0; i2 < list.size(); ++i2) {
                String object = list.get(i2);
                hashMap.put(object + "_id", new ArrayList());
            }
        }
        if (oConvertUtils.isNotEmpty((Object)string)) {
            String[] stringArray;
            for (String string2 : stringArray = string.split(E)) {
                String[] stringArray2 = string2.split("@");
                if (stringArray2.length > 0) {
                    ((List)hashMap.get("id")).add(stringArray2[0]);
                }
                if (stringArray2.length <= 1 || list == null || list.size() <= 0) continue;
                for (int i2 = 1; i2 < stringArray2.length; ++i2) {
                    int n = i2 - 1;
                    String string3 = list.get(n);
                    ((List)hashMap.get(string3 + "_id")).add(stringArray2[i2]);
                }
            }
        }
        return hashMap;
    }

    public static List<ExcelExportEntity> b(List<OnlCgformField> list, String string, String string2) {
        ArrayList<ExcelExportEntity> arrayList = new ArrayList<ExcelExportEntity>();
        for (int i2 = 0; i2 < list.size(); ++i2) {
            if (null != string && string.equals(list.get(i2).getDbFieldName()) || list.get(i2).getIsShowList() != 1) continue;
            String string3 = list.get(i2).getDbFieldName();
            ExcelExportEntity excelExportEntity = new ExcelExportEntity(list.get(i2).getDbFieldTxt(), (Object)string3);
            if (P.equals(list.get(i2).getFieldShowType())) {
                excelExportEntity.setType(1);
                excelExportEntity.setExportImageType(3);
                excelExportEntity.setImageBasePath(string2);
                excelExportEntity.setHeight(10.0);
                excelExportEntity.setWidth(60.0);
            } else {
                int n;
                int n2 = list.get(i2).getDbLength() == 0 ? 12 : (n = list.get(i2).getDbLength() > 30 ? 30 : list.get(i2).getDbLength());
                if (U.equals(list.get(i2).getFieldShowType())) {
                    excelExportEntity.setFormat("yyyy-MM-dd");
                } else if ("datetime".equals(list.get(i2).getFieldShowType())) {
                    excelExportEntity.setFormat("yyyy-MM-dd HH:mm:ss");
                }
                if (n < 10) {
                    n = 10;
                }
                excelExportEntity.setWidth((double)n);
            }
            arrayList.add(excelExportEntity);
        }
        return arrayList;
    }

    public static <T> List<T> a(Object object, Class<T> clazz) {
        ArrayList<T> arrayList = new ArrayList<T>();
        if (object instanceof List) {
            for (Object e2 : (List)object) {
                arrayList.add(clazz.cast(e2));
            }
            return arrayList;
        }
        return null;
    }

    public static boolean i(String string) {
        return w.equalsIgnoreCase(string) || x.equalsIgnoreCase(string) || y.equalsIgnoreCase(string) || z.equalsIgnoreCase(string) || A.equalsIgnoreCase(string) || "id".equalsIgnoreCase(string);
    }

    public static boolean j(String string) {
        boolean bl = false;
        for (String string2 : MybatisPlusSaasConfig.TENANT_TABLE) {
            if (!string2.equalsIgnoreCase(string)) continue;
            bl = true;
            break;
        }
        return bl;
    }

    public static String k(String string) {
        if (string.indexOf("@") > 0) {
            string = string.substring(0, string.indexOf("@"));
        }
        return string;
    }

    public static String l(String string) {
        if (oConvertUtils.isEmpty((Object)string)) {
            return "";
        }
        String[] stringArray = string.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        for (String string2 : stringArray) {
            if (string2.isEmpty()) continue;
            stringBuilder.append(string2.charAt(0));
        }
        Object object = stringBuilder.toString();
        object = ((String)object).length() >= 3 ? ((String)object).substring(0, 3) : String.format("%-4s", object).replace(' ', 'X');
        String string3 = Md5Util.md5Encode((String)string, (String)"UTF-8");
        object = (String)object + "_" + string3.substring(0, 3);
        return ((String)object).toLowerCase();
    }

    public static class a
    implements Comparator<String> {
        public int a(String string, String string2) {
            if (string == null || string2 == null) {
                return -1;
            }
            if (string.length() > string2.length()) {
                return 1;
            }
            if (string.length() < string2.length()) {
                return -1;
            }
            if (string.compareTo(string2) > 0) {
                return 1;
            }
            if (string.compareTo(string2) < 0) {
                return -1;
            }
            if (string.compareTo(string2) == 0) {
                return 0;
            }
            return 0;
        }

        @Override
        public /* synthetic */ int compare(Object object, Object object2) {
            return this.a((String)object, (String)object2);
        }
    }

    public static class b
    implements Comparator<String> {
        public int a(String string, String string2) {
            if (string == null || string2 == null) {
                return -1;
            }
            if (string.compareTo(string2) > 0) {
                return 1;
            }
            if (string.compareTo(string2) < 0) {
                return -1;
            }
            if (string.compareTo(string2) == 0) {
                return 0;
            }
            return 0;
        }

        @Override
        public /* synthetic */ int compare(Object object, Object object2) {
            return this.a((String)object, (String)object2);
        }
    }
}

