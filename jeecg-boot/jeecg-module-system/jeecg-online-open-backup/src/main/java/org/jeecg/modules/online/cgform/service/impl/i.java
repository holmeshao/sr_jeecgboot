/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONObject
 *  com.baomidou.mybatisplus.annotation.DbType
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  org.apache.poi.ss.usermodel.Workbook
 *  org.apache.poi.xssf.usermodel.XSSFWorkbook
 *  org.apache.shiro.SecurityUtils
 *  org.jeecg.common.system.api.ISysBaseAPI
 *  org.jeecg.common.system.query.MatchTypeEnum
 *  org.jeecg.common.system.util.JeecgDataAutorUtils
 *  org.jeecg.common.system.vo.LoginUser
 *  org.jeecg.common.system.vo.SysPermissionDataRuleModel
 *  org.jeecg.common.system.vo.SysUserCacheInfo
 *  org.jeecg.common.util.dynamic.db.DbTypeUtils
 *  org.jeecg.common.util.oConvertUtils
 *  org.jeecgframework.poi.excel.entity.ExportParams
 *  org.jeecgframework.poi.excel.entity.enmus.ExcelType
 *  org.jeecgframework.poi.excel.entity.params.ExcelExportEntity
 *  org.jeecgframework.poi.excel.export.ExcelExportServer
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.stereotype.Service
 */
package org.jeecg.modules.online.cgform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.lang.invoke.SerializedLambda;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.MatchTypeEnum;
import org.jeecg.common.system.util.JeecgDataAutorUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysPermissionDataRuleModel;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.common.util.dynamic.db.DbTypeUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.auth.service.IOnlAuthDataService;
import org.jeecg.modules.online.b.a;
import org.jeecg.modules.online.cgform.b.b;
import org.jeecg.modules.online.cgform.d.c;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.mapper.OnlineMapper;
import org.jeecg.modules.online.cgform.model.e;
import org.jeecg.modules.online.cgform.model.f;
import org.jeecg.modules.online.cgform.model.h;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.cgform.service.IOnlineJoinQueryService;
import org.jeecg.modules.online.config.c.d;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.jeecg.modules.online.config.model.OnlineFieldConfig;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.jeecgframework.poi.excel.export.ExcelExportServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service(value="onlineJoinQueryService")
public class i
implements IOnlineJoinQueryService {
    private static final Logger a = LoggerFactory.getLogger(i.class);
    @Autowired
    IOnlCgformFieldService onlCgformFieldService;
    @Autowired
    IOnlCgformHeadService onlCgformHeadService;
    @Autowired
    private IOnlAuthDataService onlAuthDataService;
    @Lazy
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private OnlineMapper onlineMapper;
    @Value(value="${jeecg.path.upload}")
    private String upLoadPath;

    @Override
    public Map<String, Object> pageList(OnlCgformHead head, Map<String, Object> params, boolean ignoreSelectSubField) {
        e e2 = this.getQueryInfo(head, params, ignoreSelectSubField);
        String string = e2.getSql();
        Map<String, Object> map = e2.getParams();
        Map<String, String> map2 = e2.getTableAliasMap();
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        Integer n = params.get("pageSize") == null ? 10 : Integer.parseInt(params.get("pageSize").toString());
        if (n == -521) {
            List<Map<String, Object>> list = this.onlineMapper.selectByCondition(string, map);
            if (list == null || list.size() == 0) {
                hashMap.put("total", 0);
            } else {
                hashMap.put("total", list.size());
                if (ignoreSelectSubField) {
                    list = this.b(list);
                }
                hashMap.put("records", c.a(list, map2.values()));
            }
            if (ignoreSelectSubField) {
                hashMap.put("fieldList", e2.getFieldList());
            }
        } else {
            Integer n2 = params.get("pageNo") == null ? 1 : Integer.parseInt(params.get("pageNo").toString());
            Page page = new Page((long)n2.intValue(), (long)n.intValue());
            page.setOptimizeCountSql(false);
            IPage<Map<String, Object>> iPage = this.onlineMapper.selectPageByCondition((Page<Map<String, Object>>)page, string, map);
            hashMap.put("total", iPage.getTotal());
            List<Map<String, Object>> list = iPage.getRecords();
            if (ignoreSelectSubField) {
                list = this.b(list);
            }
            hashMap.put("records", c.a(list, map2.values()));
        }
        return hashMap;
    }

    private String a(f f2, String string, String string2, String string3) {
        String string4 = f2.getAlias();
        String string5 = f2.getTableName();
        String string6 = c.f(string5);
        String string7 = string4 + ".";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" AND EXISTS (");
        stringBuffer.append("SELECT ");
        stringBuffer.append(string7 + "id");
        stringBuffer.append(" FROM ");
        stringBuffer.append(string6);
        stringBuffer.append(" " + string4);
        stringBuffer.append(" where  ");
        stringBuffer.append(string7);
        stringBuffer.append(f2.getJoinField());
        stringBuffer.append("=");
        stringBuffer.append(string);
        stringBuffer.append(f2.getMainField());
        if (string2 != null && string2.length() > 0) {
            stringBuffer.append(string2);
        }
        if (string3 != null && string3.length() > 0) {
            stringBuffer.append(" AND (").append(string3).append(") ");
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    @Override
    public Map<String, Object> pageList(OnlCgformHead head, Map<String, Object> params) {
        return this.pageList(head, params, false);
    }

    private String a(List<String> list, Map<String, Integer> map, Map<String, String> map2) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string : list) {
            String[] stringArray = string.split("\\.");
            String string2 = stringArray[0];
            if ("a".equals(string2)) {
                arrayList.add(string);
                continue;
            }
            String string3 = stringArray[1];
            int n = map.get(string3);
            if (n > 1) {
                String string4 = map2.get(string2);
                arrayList.add(string + " " + c.l(string4) + "_" + string3);
                continue;
            }
            arrayList.add(string);
        }
        return String.join((CharSequence)",", arrayList);
    }

    private void a(String string, boolean bl, List<OnlCgformField> list, List<String> list2, Map<String, Integer> map) {
        if (list == null || list.size() == 0) {
            if (bl) {
                list2.add(string + "id");
            }
        } else {
            int n = list.size();
            for (int i2 = 0; i2 < n; ++i2) {
                OnlCgformField onlCgformField = list.get(i2);
                String string2 = onlCgformField.getDbFieldName();
                if ("id".equals(string2) || 1 != onlCgformField.getIsShowList()) continue;
                if ("cat_tree".equals(onlCgformField.getFieldShowType()) && oConvertUtils.isNotEmpty((Object)onlCgformField.getDictText())) {
                    list2.add(string + onlCgformField.getDictText());
                }
                list2.add(string + string2);
                Integer n2 = map.get(string2);
                if (n2 == null) {
                    map.put(string2, 1);
                    continue;
                }
                map.put(string2, 1 + n2);
            }
            list2.add(string + "id");
            map.put("id", 2);
        }
    }

    private f a(OnlCgformHead onlCgformHead, int n, boolean bl) {
        String string = onlCgformHead.getId();
        String string2 = onlCgformHead.getTableName();
        f f2 = new f(string2, string, bl);
        List<OnlCgformField> list = this.a(string);
        List<OnlCgformField> list2 = this.onlCgformFieldService.queryAvailableFields(string, string2, true, list, null);
        f2.setAllFieldList(list);
        f2.setSelectFieldList(list2);
        f2.setAliasByIntValue(n);
        if (!bl) {
            for (OnlCgformField onlCgformField : list) {
                if (!oConvertUtils.isNotEmpty((Object)onlCgformField.getMainField()) || !oConvertUtils.isNotEmpty((Object)onlCgformField.getMainTable())) continue;
                f2.setMainField(onlCgformField.getMainField());
                f2.setJoinField(onlCgformField.getDbFieldName());
                break;
            }
        }
        return f2;
    }

    private List<f> a(OnlCgformHead onlCgformHead, String string) {
        String string2;
        int n = 97;
        ArrayList<f> arrayList = new ArrayList<f>();
        f f2 = this.a(onlCgformHead, n++, true);
        List<SysPermissionDataRuleModel> list = this.onlAuthDataService.queryUserOnlineAuthData(string, onlCgformHead.getId());
        f2.setAuthList(list);
        arrayList.add(f2);
        Integer n2 = onlCgformHead.getTableType();
        if (n2 != null && n2 == 2 && (string2 = onlCgformHead.getSubTableStr()) != null && !"".equals(string2)) {
            String[] stringArray;
            for (String string3 : stringArray = string2.split(",")) {
                OnlCgformHead onlCgformHead2 = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string3));
                if (onlCgformHead2 == null) continue;
                f f3 = this.a(onlCgformHead2, n++, false);
                List<SysPermissionDataRuleModel> list2 = this.onlAuthDataService.queryUserOnlineAuthData(string, onlCgformHead2.getId());
                f3.setAuthList(list2);
                arrayList.add(f3);
            }
        }
        return arrayList;
    }

    private Map<String, List<OnlCgformField>> a(OnlCgformHead onlCgformHead, Map<String, String> map) {
        String string;
        HashMap<String, List<OnlCgformField>> hashMap = new HashMap<String, List<OnlCgformField>>(5);
        map.put(onlCgformHead.getTableName(), onlCgformHead.getId());
        List<OnlCgformField> list = this.a(onlCgformHead.getId());
        hashMap.put(onlCgformHead.getTableName(), list);
        Integer n = onlCgformHead.getTableType();
        if (n != null && n == 2 && (string = onlCgformHead.getSubTableStr()) != null && !"".equals(string)) {
            String[] stringArray;
            for (String string2 : stringArray = string.split(",")) {
                OnlCgformHead onlCgformHead2 = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string2));
                if (onlCgformHead2 == null) continue;
                map.put(onlCgformHead2.getTableName(), onlCgformHead2.getId());
                List<OnlCgformField> list2 = this.a(onlCgformHead2.getId());
                hashMap.put(onlCgformHead2.getTableName(), list2);
            }
        }
        return hashMap;
    }

    private List<OnlCgformField> a(String string) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)string);
        lambdaQueryWrapper.eq(OnlCgformField::getDbIsPersist, (Object)b.b);
        lambdaQueryWrapper.orderByAsc(OnlCgformField::getOrderNum);
        return this.onlCgformFieldService.list((Wrapper)lambdaQueryWrapper);
    }

    private boolean a(Map<String, Object> map, boolean bl, String string, String string2, List<OnlCgformField> list, List<h> list2) {
        boolean bl2;
        block5: {
            block3: {
                String string3;
                String string4;
                String string5;
                block4: {
                    bl2 = bl;
                    Object object = map.get("column");
                    if (object == null || "id".equals(object.toString())) break block3;
                    string5 = object.toString();
                    Object object2 = map.get("order");
                    string4 = "desc";
                    if (object2 != null) {
                        string4 = object2.toString();
                    }
                    if (!bl) break block4;
                    if (c.c(string5, list)) {
                        h h2 = new h(string5, string4);
                        h2.setAlias(string2);
                        list2.add(h2);
                    }
                    break block5;
                }
                if (!string5.startsWith(string) || !c.c(string3 = string5.replaceFirst(string + "_", ""), list)) break block5;
                h h3 = new h(string3, string4);
                h3.setAlias(string2);
                list2.add(h3);
                bl2 = true;
                break block5;
            }
            for (OnlCgformField onlCgformField : list) {
                JSONObject jSONObject;
                String string6;
                if (!"1".equals(onlCgformField.getSortFlag())) continue;
                String string7 = onlCgformField.getFieldExtendJson();
                h h4 = new h(onlCgformField.getDbFieldName());
                h4.setAlias(string2);
                if (string7 == null || "".equals(string7) || (string6 = (jSONObject = JSON.parseObject((String)string7)).getString("orderRule")) == null || "".equals(string6)) continue;
                h4.setRule(string6);
                list2.add(h4);
                bl2 = true;
            }
        }
        return bl2;
    }

    private String a(List<h> list) {
        Object object;
        if (list.size() == 0) {
            object = h.a("a.");
            list.add((h)object);
        }
        object = new ArrayList();
        for (h h2 : list) {
            String string = h2.getRealSql();
            object.add(string);
        }
        return " ORDER BY " + String.join((CharSequence)",", (Iterable<? extends CharSequence>)object);
    }

    private String a(StringBuilder stringBuilder) {
        String string = stringBuilder.toString();
        if (string == null || "".equals(string)) {
            return "";
        }
        return " AND (" + string + ") ";
    }

    private boolean a(StringBuilder stringBuilder, JSONArray jSONArray, MatchTypeEnum matchTypeEnum, String string, String string2, List<OnlCgformField> list, boolean bl, boolean bl2) {
        boolean bl3 = bl2;
        if (jSONArray != null) {
            for (int i2 = 0; i2 < jSONArray.size(); ++i2) {
                String string3;
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                String string4 = jSONObject.getString("field");
                String[] stringArray = string4.split(",");
                if (stringArray.length == 1) {
                    if (!bl || !c.c(string4, list)) continue;
                    string3 = string + string4;
                    c.a(stringBuilder, string3, jSONObject, matchTypeEnum, null, bl3);
                    bl3 = false;
                    continue;
                }
                string3 = stringArray[1];
                if (!string2.equalsIgnoreCase(stringArray[0]) || !c.c(string3, list)) continue;
                String string5 = string + string3;
                c.a(stringBuilder, string5, jSONObject, matchTypeEnum, null, bl3);
                bl3 = false;
            }
        }
        return bl3;
    }

    private List<Map<String, Object>> b(List<Map<String, Object>> list) {
        HashMap<String, Map<String, Object>> hashMap = new HashMap<String, Map<String, Object>>(5);
        for (Map<String, Object> map : list) {
            String string = "";
            if (map.containsKey("id")) {
                string = map.get("id").toString();
            } else if (map.containsKey("ID")) {
                string = map.get("ID").toString();
            }
            hashMap.putIfAbsent(string, map);
        }
        return new ArrayList<Map<String, Object>>(hashMap.values());
    }

    private boolean a(f f2, JSONArray jSONArray) {
        if (f2.a()) {
            return true;
        }
        String string = f2.getTableName();
        if (jSONArray != null && jSONArray.size() > 0) {
            for (int i2 = 0; i2 < jSONArray.size(); ++i2) {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                String string2 = jSONObject.getString("field");
                String[] stringArray = string2.split(",");
                if (stringArray.length != 2 || stringArray[0] == null || !stringArray[0].equals(string)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean a(f f2) {
        if (f2.a()) {
            return true;
        }
        List<OnlCgformField> list = f2.getSelectFieldList();
        if (list != null && list.size() > 0) {
            for (OnlCgformField onlCgformField : list) {
                String string = onlCgformField.getMainTable();
                if (string != null && !"".equals(string)) continue;
                return true;
            }
        }
        return false;
    }

    private a a(f f2, JSONArray jSONArray, String string, a a2) {
        String string2 = f2.getTableName();
        boolean bl = f2.a();
        List<OnlCgformField> list = f2.getAllFieldList();
        ArrayList<OnlineFieldConfig> arrayList = new ArrayList<OnlineFieldConfig>();
        if (jSONArray != null) {
            Object object;
            for (int i2 = 0; i2 < jSONArray.size(); ++i2) {
                Object object2;
                object = jSONArray.getJSONObject(i2);
                String string3 = object.getString("field");
                String[] stringArray = string3.split(",");
                if (stringArray.length == 1) {
                    if (!bl || !c.c(string3, list)) continue;
                    object2 = new OnlineFieldConfig((JSONObject)object);
                    arrayList.add((OnlineFieldConfig)object2);
                    continue;
                }
                object2 = stringArray[1];
                if (!string2.equalsIgnoreCase(stringArray[0]) || !c.c((String)object2, list)) continue;
                OnlineFieldConfig onlineFieldConfig = new OnlineFieldConfig((JSONObject)object);
                arrayList.add(onlineFieldConfig);
            }
            if (arrayList.size() > 0) {
                String string4 = f2.getAlias() + ".";
                object = new a(string4, true, string);
                ((a)object).setDuplicateSqlNameRecord(a2.getDuplicateSqlNameRecord());
                ((a)object).setDuplicateParamNameRecord(a2.getDuplicateParamNameRecord());
                ((a)object).a(arrayList);
                return object;
            }
        }
        return null;
    }

    @Override
    public e getQueryInfo(OnlCgformHead head, Map<String, Object> params, boolean ignoreSelectSubField) {
        return this.getQueryInfo(head, params, ignoreSelectSubField, false);
    }

    @Override
    public e getQueryInfo(OnlCgformHead head, Map<String, Object> params, boolean ignoreSelectSubField, boolean isNewExport) {
        Object object;
        Object object2;
        List<OnlCgformField> list;
        Object object3;
        String string;
        Object object4;
        DbType dbType = d.c(null);
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        List<f> list2 = this.a(head, loginUser.getId());
        JSONArray jSONArray = c.b(params);
        MatchTypeEnum matchTypeEnum = c.c(params);
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl = true;
        Object object6 = "";
        boolean bl2 = false;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        ArrayList<String> arrayList = new ArrayList<String>();
        ArrayList<h> arrayList2 = new ArrayList<h>();
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>(5);
        HashMap<String, String> hashMap2 = new HashMap<String, String>(5);
        List<OnlCgformField> list3 = new ArrayList<OnlCgformField>();
        HashMap<String, Object> hashMap3 = new HashMap<String, Object>(5);
        for (f object52 : list2) {
            String string2;
            boolean bl3;
            object4 = object52.getSelectFieldList();
            string = object52.getAlias();
            object3 = string + ".";
            String string3 = " " + string + " ";
            String string4 = object52.getTableName();
            list = object52.getAllFieldList();
            List<SysPermissionDataRuleModel> list4 = object52.getAuthList();
            if (!bl2 && list4 != null && list4.size() > 0) {
                JeecgDataAutorUtils.installUserInfo((SysUserCacheInfo)this.sysBaseAPI.getCacheUser(loginUser.getUsername()));
                bl2 = true;
            }
            a a2 = new a((String)object3, dbType.getDb());
            a2.setTableName(string4);
            a2.setNeedList(null);
            a2.setFirst(false);
            object2 = c.g(list);
            object = a2.a((List<OnlineFieldConfig>)object2, params, list4, string4 + "@");
            Map<String, Object> map = a2.getSqlParams();
            hashMap3.putAll(map);
            boolean bl4 = this.a(params, object52.a(), string4, (String)object3, list, arrayList2);
            boolean bl5 = this.a(object52);
            boolean bl6 = this.a(object52, jSONArray);
            boolean bl7 = ((String)object).length() > 0;
            boolean bl8 = bl5 || bl6 || bl7 || bl4;
            if (!bl8) continue;
            boolean bl9 = bl3 = !bl5 && (bl6 || bl7);
            if (bl4) {
                bl3 = false;
            }
            if (ignoreSelectSubField && object52.a() || !ignoreSelectSubField && bl5) {
                this.a((String)object3, object52.a(), (List<OnlCgformField>)object4, arrayList, hashMap);
            }
            String string5 = "";
            a a3 = this.a(object52, jSONArray, matchTypeEnum.getValue(), a2);
            if (a3 != null && (string5 = a3.getSql().toString()).length() > 0) {
                hashMap3.putAll(a3.getSqlParams());
            }
            if (object52.a()) {
                stringBuffer.append(" FROM " + c.f(string4) + (String)string3);
                object6 = object3;
            } else {
                hashMap2.put(string, string4);
                if (bl3) {
                    string2 = this.a(object52, (String)object6, (String)object, string5);
                    stringBuffer2.append(string2);
                } else {
                    stringBuffer.append(" LEFT JOIN ");
                    stringBuffer.append(c.f(string4));
                    stringBuffer.append(string3);
                    stringBuffer.append(" ON ");
                    stringBuffer.append((String)object3);
                    stringBuffer.append(object52.getJoinField());
                    stringBuffer.append("=");
                    stringBuffer.append((String)object6);
                    stringBuffer.append(object52.getMainField());
                }
            }
            if (!bl3) {
                stringBuffer2.append((String)object);
                if (string5.length() > 0) {
                    if (bl) {
                        stringBuilder.append(string5);
                        bl = false;
                    } else {
                        stringBuilder.append(" ").append(matchTypeEnum.getValue()).append(" ").append(string5);
                    }
                }
            }
            string2 = string4 + ".";
            while (stringBuffer2.toString().toUpperCase().contains(string2.toUpperCase())) {
                int n = stringBuffer2.toString().toUpperCase().indexOf(string2.toUpperCase());
                stringBuffer2.replace(n, n + string4.length(), string);
            }
        }
        String string5 = this.a(arrayList, hashMap, hashMap2);
        String string6 = this.a(stringBuilder);
        object4 = this.a(arrayList2);
        string = "SELECT " + (String)string5 + stringBuffer.toString() + " where 1=1  " + stringBuffer2.toString() + string6;
        if (!DbTypeUtils.dbTypeIsSqlServer((DbType)dbType)) {
            string = string + (String)object4;
        }
        a.info("---Online\u8054\u5408\u67e5\u8be2sql :>> " + string);
        a.info("---Online\u8054\u5408\u67e5\u8be2sqlParams :>> " + hashMap3);
        object3 = new e(string, hashMap3);
        ((e)object3).setTableAliasMap(hashMap2);
        for (f f2 : list2) {
            list = f2.getSelectFieldList();
            if (isNewExport) {
                for (OnlCgformField onlCgformField : list) {
                    object2 = onlCgformField.getDbFieldName();
                    object = (Integer)hashMap.get(object2);
                    if (object != null && (Integer)object > 1 && !f2.a()) {
                        onlCgformField.setDbFieldName(f2.getTableName() + "_" + (String)object2);
                    }
                    list3.add(onlCgformField);
                }
                continue;
            }
            if (!ignoreSelectSubField || !f2.a()) continue;
            list3 = list;
        }
        ((e)object3).setFieldList(list3);
        return object3;
    }

    @Override
    public XSSFWorkbook handleOnlineExport(OnlCgformHead head, Map<String, Object> params) {
        XSSFWorkbook xSSFWorkbook = new XSSFWorkbook();
        boolean bl = c.a(head);
        e e2 = null;
        e2 = bl ? this.getQueryInfo(head, params, false, true) : this.onlCgformFieldService.getQueryInfo(head, params, null);
        boolean bl2 = true;
        Integer n = 50000;
        IPage<Map<String, Object>> iPage = Integer.valueOf(1);
        String string = e2.getSql();
        Map<String, Object> map = e2.getParams();
        List<OnlCgformField> list = e2.getFieldList();
        List<ExcelExportEntity> list2 = c.b(list, "id", this.upLoadPath);
        boolean bl3 = false;
        while (bl2) {
            String[] stringArray;
            Object object;
            String string2;
            Page page = new Page((long)iPage.intValue(), (long)n.intValue());
            page.setOptimizeCountSql(false);
            page.setSearchCount(false);
            IPage<Map<String, Object>> iPage2 = iPage;
            iPage = iPage.intValue() + 1;
            ArrayList arrayList = iPage;
            params.put("pageNo", iPage2);
            a.info("---Online\u8868\u5355\u5bfc\u51fa-\u67e5\u8be2sql: >>  " + string);
            a.info("---Online\u8868\u5355\u5bfc\u51fa-\u67e5\u8be2sqlParam: >>  " + map.toString());
            iPage2 = this.onlineMapper.selectPageByCondition((Page<Map<String, Object>>)page, string, map);
            arrayList = c.d(iPage2.getRecords());
            if (arrayList == null || arrayList.size() == 0) {
                bl2 = false;
                continue;
            }
            List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
            String string3 = string2 = params.get("selections") == null ? null : params.get("selections").toString();
            if (oConvertUtils.isNotEmpty((Object)string2)) {
                bl2 = false;
                if (bl) {
                    object = e2.getTableAliasMap();
                    stringArray = new ArrayList(object.values());
                    String[] stringArray2 = c.f(string2, stringArray);
                    list3 = arrayList.stream().filter(map2 -> this.a((Map<String, Object>)map2, (Map<String, List<String>>)stringArray2)).collect(Collectors.toList());
                } else {
                    object = c.h(string2);
                    list3 = arrayList.stream().filter(arg_0 -> i.a((List)object, arg_0)).collect(Collectors.toList());
                }
            } else {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                list3.addAll(arrayList);
            }
            org.jeecg.modules.online.cgform.converter.b.a(1, list3, list);
            try {
                this.onlCgformHeadService.executeEnhanceExport(head, list3);
            }
            catch (BusinessException businessException) {
                a.error("\u5bfc\u51fajava\u589e\u5f3a\u5904\u7406\u51fa\u9519", (Object)businessException.getMessage());
            }
            if (head.getTableType() == 2 && !bl && oConvertUtils.isEmpty((Object)params.get("exportSingleOnly")) && oConvertUtils.isNotEmpty((Object)(object = head.getSubTableStr()))) {
                for (String string4 : stringArray = ((String)object).split(",")) {
                    this.addAllSubTableDate(string4, params, list3, list2, bl3);
                }
                bl3 = true;
            }
            object = new ExcelExportServer();
            stringArray = new ExportParams();
            stringArray.setType(ExcelType.XSSF);
            object.createSheetForMap((Workbook)xSSFWorkbook, (ExportParams)stringArray, list2, list3);
        }
        return xSSFWorkbook;
    }

    @Override
    public void addAllSubTableDate(String subTable, Map<String, Object> params, List<Map<String, Object>> result, List<ExcelExportEntity> entityList, boolean subEntityExist) {
        if (oConvertUtils.isEmpty((Object)subTable)) {
            return;
        }
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)subTable));
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)onlCgformHead.getId());
        lambdaQueryWrapper.orderByAsc(OnlCgformField::getOrderNum);
        List list = this.onlCgformFieldService.list((Wrapper)lambdaQueryWrapper);
        String string = "";
        String string2 = "";
        for (Object object : list) {
            if (!oConvertUtils.isNotEmpty((Object)((OnlCgformField)object).getMainField())) continue;
            string = ((OnlCgformField)object).getMainField();
            string2 = ((OnlCgformField)object).getDbFieldName();
            break;
        }
        if (!subEntityExist) {
            ExcelExportEntity excelExportEntity = new ExcelExportEntity(onlCgformHead.getTableTxt(), (Object)subTable);
            excelExportEntity.setList(c.b(list, "id", this.upLoadPath));
            entityList.add(excelExportEntity);
        }
        for (int i2 = 0; i2 < result.size(); ++i2) {
            Object object;
            params.put(string2, result.get(i2).get(string));
            object = c.a(onlCgformHead.getTableName(), (List<OnlCgformField>)list, params);
            a.info("-----------\u52a8\u6001\u5217\u8868\u67e5\u8be2\u5b50\u8868sql\u300b\u300b" + (String)object);
            List<Map<String, Object>> list2 = this.onlCgformHeadService.queryListData((String)object);
            org.jeecg.modules.online.cgform.converter.b.a(1, list2, list);
            result.get(i2).put(subTable, c.d(list2));
        }
    }

    private boolean a(Map<String, Object> map, Map<String, List<String>> map2) {
        boolean bl = true;
        for (String string : map2.keySet()) {
            List<String> list = map2.get(string);
            bl = bl && list.contains(map.get(string));
        }
        return bl;
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getOrderNum": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) {
                    return OnlCgformField::getOrderNum;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlCgformField::getOrderNum;
            }
            case "getCgformHeadId": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformField::getCgformHeadId;
            }
            case "getTableName": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformHead::getTableName;
            }
            case "getDbIsPersist": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlCgformField::getDbIsPersist;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }

    private static /* synthetic */ boolean a(List list, Map map) {
        return list.contains(map.get("id"));
    }
}

