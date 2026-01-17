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
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.support.SFunction
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
 *  org.apache.shiro.SecurityUtils
 *  org.jeecg.common.api.dto.DataLogDTO
 *  org.jeecg.common.config.mqtoken.UserTokenContext
 *  org.jeecg.common.exception.JeecgBootException
 *  org.jeecg.common.system.api.ISysBaseAPI
 *  org.jeecg.common.system.util.JeecgDataAutorUtils
 *  org.jeecg.common.system.vo.DictModel
 *  org.jeecg.common.system.vo.LoginUser
 *  org.jeecg.common.system.vo.SysPermissionDataRuleModel
 *  org.jeecg.common.system.vo.SysUserCacheInfo
 *  org.jeecg.common.util.SqlInjectionUtil
 *  org.jeecg.common.util.TokenUtils
 *  org.jeecg.common.util.oConvertUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.cache.annotation.CacheEvict
 *  org.springframework.cache.annotation.Cacheable
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.CollectionUtils
 */
package org.jeecg.modules.online.cgform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.lang.invoke.SerializedLambda;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.DataLogDTO;
import org.jeecg.common.config.mqtoken.UserTokenContext;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JeecgDataAutorUtils;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysPermissionDataRuleModel;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.auth.service.IOnlAuthDataService;
import org.jeecg.modules.online.auth.service.IOnlAuthPageService;
import org.jeecg.modules.online.b.a;
import org.jeecg.modules.online.cgform.b.b;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.mapper.OnlCgformFieldMapper;
import org.jeecg.modules.online.cgform.mapper.OnlCgformHeadMapper;
import org.jeecg.modules.online.cgform.mapper.OnlineMapper;
import org.jeecg.modules.online.cgform.model.TreeModel;
import org.jeecg.modules.online.cgform.model.e;
import org.jeecg.modules.online.cgform.model.h;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportAPIService;
import org.jeecg.modules.online.config.c.d;
import org.jeecg.modules.online.config.model.OnlineFieldConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service(value="onlCgformFieldServiceImpl")
public class c
extends ServiceImpl<OnlCgformFieldMapper, OnlCgformField>
implements IOnlCgformFieldService {
    private static final Logger b = LoggerFactory.getLogger(c.class);
    @Autowired
    IOnlCgreportAPIService onlCgreportAPIService;
    @Autowired
    private OnlCgformFieldMapper onlCgformFieldMapper;
    @Autowired
    private OnlCgformHeadMapper cgformHeadMapper;
    @Autowired
    private IOnlAuthDataService onlAuthDataService;
    @Autowired
    private IOnlAuthPageService onlAuthPageService;
    @Lazy
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private OnlineMapper onlineMapper;
    private static final String c = "0";
    public static ExecutorService a = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

    @Override
    public Map<String, Object> queryAutolistPage(OnlCgformHead head, Map<String, Object> params, List<String> needList) {
        e e2 = this.getQueryInfo(head, params, needList);
        String string = e2.getSql();
        Map<String, Object> map = e2.getParams();
        List<OnlCgformField> list = e2.getFieldList();
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        Integer n = params.get("pageSize") == null ? 10 : Integer.parseInt(params.get("pageSize").toString());
        if (n == -521) {
            List<Map<String, Object>> list2 = this.onlineMapper.selectByCondition(string, map);
            if (list2 == null || list2.size() == 0) {
                hashMap.put("total", 0);
                hashMap.put("fieldList", list);
            } else {
                hashMap.put("total", list2.size());
                hashMap.put("fieldList", list);
                hashMap.put("records", org.jeecg.modules.online.cgform.d.c.d(list2));
            }
        } else {
            Integer n2 = params.get("pageNo") == null ? 1 : Integer.parseInt(params.get("pageNo").toString());
            Page page = new Page((long)n2.intValue(), (long)n.intValue());
            IPage<Map<String, Object>> iPage = this.onlineMapper.selectPageByCondition((Page<Map<String, Object>>)page, string, map);
            hashMap.put("total", iPage.getTotal());
            List<Map<String, Object>> list3 = org.jeecg.modules.online.cgform.d.c.d(iPage.getRecords());
            this.handleLinkTableDictData(head.getId(), list3);
            this.a(head, list3);
            hashMap.put("records", list3);
        }
        return hashMap;
    }

    @Override
    public Map<String, Object> queryAutoTreeNoPage(String tbname, String headId, Map<String, Object> params, List<String> needList, String pidField) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)headId);
        lambdaQueryWrapper.orderByAsc(OnlCgformField::getOrderNum);
        List list = this.list((Wrapper)lambdaQueryWrapper);
        List<OnlCgformField> list2 = this.queryAvailableFields(headId, tbname, true, list, needList);
        StringBuffer stringBuffer = new StringBuffer();
        org.jeecg.modules.online.cgform.d.c.a(tbname, list2, stringBuffer);
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String string = loginUser.getId();
        List<SysPermissionDataRuleModel> list3 = this.onlAuthDataService.queryUserOnlineAuthData(string, headId);
        if (list3 != null && list3.size() > 0) {
            JeecgDataAutorUtils.installUserInfo((SysUserCacheInfo)this.sysBaseAPI.getCacheUser(loginUser.getUsername()));
        }
        a a2 = new a("t.");
        a2.setTableName(tbname);
        a2.setNeedList(needList);
        a2.setSubTableStr("");
        List<OnlineFieldConfig> list4 = org.jeecg.modules.online.cgform.d.c.g(list);
        String string2 = a2.a(list4, params, list3);
        Map<String, Object> map = a2.getSqlParams();
        if (string2.trim().length() > 0) {
            stringBuffer.append(" t ").append(" where  ").append(string2);
        }
        String string3 = this.a(list, params);
        stringBuffer.append(string3);
        Integer n = params.get("pageSize") == null ? 10 : Integer.parseInt(params.get("pageSize").toString());
        if (n == -521) {
            List<Map<String, Object>> list5 = this.onlineMapper.selectByCondition(stringBuffer.toString(), map);
            if ("true".equals(params.get("hasQuery"))) {
                HashMap<String, Map<String, Object>> hashMap2 = new HashMap<String, Map<String, Object>>();
                ArrayList<String> arrayList = new ArrayList<String>();
                for (Map<String, Object> map2 : list5) {
                    Object object;
                    String string4 = org.jeecg.modules.online.cgform.d.c.a(map2, pidField);
                    if (string4 != null && !c.equals(string4)) {
                        Object object2;
                        if (arrayList.contains(string4)) continue;
                        object = this.a(string4, tbname, headId, needList, pidField);
                        arrayList.add(string4);
                        if (!oConvertUtils.isNotEmpty(object) || !oConvertUtils.isNotEmpty((Object)(object2 = object.containsKey("id") ? object.get("id") : object.get("ID")))) continue;
                        hashMap2.put(object2.toString(), (Map<String, Object>)object);
                        continue;
                    }
                    object = map2.containsKey("id") ? map2.get("id") : map2.get("ID");
                    if (!oConvertUtils.isNotEmpty((Object)object)) continue;
                    hashMap2.put(object.toString(), map2);
                }
                list5 = new ArrayList(hashMap2.values());
            }
            if (list5 == null || list5.size() == 0) {
                hashMap.put("total", 0);
                hashMap.put("fieldList", list2);
            } else {
                hashMap.put("total", list5.size());
                hashMap.put("fieldList", list2);
                hashMap.put("records", org.jeecg.modules.online.cgform.d.c.d(list5));
            }
        } else {
            Integer n2 = params.get("pageNo") == null ? 1 : Integer.parseInt(params.get("pageNo").toString());
            Page page = new Page((long)n2.intValue(), (long)n.intValue());
            IPage<Map<String, Object>> iPage = this.onlineMapper.selectPageByCondition((Page<Map<String, Object>>)page, stringBuffer.toString(), map);
            hashMap.put("total", iPage.getTotal());
            hashMap.put("records", org.jeecg.modules.online.cgform.d.c.d(iPage.getRecords()));
        }
        return hashMap;
    }

    private Map<String, Object> a(String string, String string2, String string3, List<String> list, String string4) {
        List<Map<String, Object>> list2;
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        hashMap.put("id", string);
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)string3);
        lambdaQueryWrapper.orderByAsc(OnlCgformField::getOrderNum);
        List list3 = this.list((Wrapper)lambdaQueryWrapper);
        List<OnlCgformField> list4 = this.queryAvailableFields(string3, string2, true, list3, list);
        StringBuffer stringBuffer = new StringBuffer();
        org.jeecg.modules.online.cgform.d.c.a(string2, list4, stringBuffer);
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String string5 = loginUser.getId();
        List<SysPermissionDataRuleModel> list5 = this.onlAuthDataService.queryUserOnlineAuthData(string5, string3);
        if (list5 != null && list5.size() > 0) {
            JeecgDataAutorUtils.installUserInfo((SysUserCacheInfo)this.sysBaseAPI.getCacheUser(loginUser.getUsername()));
        }
        a a2 = new a("t.");
        a2.setTableName(string2);
        a2.setNeedList(list);
        a2.setSubTableStr("");
        List<OnlineFieldConfig> list6 = org.jeecg.modules.online.cgform.d.c.g(list3);
        String string6 = a2.a(list6, hashMap, list5);
        Map<String, Object> map = a2.getSqlParams();
        stringBuffer.append(" t ").append(" where  ").append(" id = '").append(string).append("' ");
        if (string6.trim().length() > 0) {
            stringBuffer.append(" and ").append(string6);
        }
        if ((list2 = this.onlineMapper.selectByCondition(stringBuffer.toString(), map)) != null && list2.size() > 0) {
            Map<String, Object> map2 = list2.get(0);
            if (map2 != null && map2.get(string4) != null && !c.equals(map2.get(string4))) {
                return this.a(map2.get(string4).toString(), string2, string3, list, string4);
            }
            return map2;
        }
        return null;
    }

    @Override
    public void saveFormData(String code, String tbname, JSONObject json, boolean isCrazy) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)code);
        List list = this.list((Wrapper)lambdaQueryWrapper);
        if (isCrazy) {
            Map<String, Object> map = org.jeecg.modules.online.cgform.d.c.c(tbname, list, json);
            this.addOnlineInsertDataLog(tbname, map.get("id").toString());
            ((OnlCgformFieldMapper)this.baseMapper).executeInsertSQL(map);
        } else {
            Map<String, Object> map = org.jeecg.modules.online.cgform.d.c.a(tbname, (List<OnlCgformField>)list, json);
            this.addOnlineInsertDataLog(tbname, map.get("id").toString());
            ((OnlCgformFieldMapper)this.baseMapper).executeInsertSQL(map);
        }
    }

    @Override
    public void saveTreeFormData(String code, String tbname, JSONObject json, String hasChildField, String pidField) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)code);
        List list = this.list((Wrapper)lambdaQueryWrapper);
        for (OnlCgformField onlCgformField : list) {
            if (hasChildField.equals(onlCgformField.getDbFieldName()) && onlCgformField.getIsShowForm() != 1) {
                onlCgformField.setIsShowForm(1);
                json.put(hasChildField, (Object)c);
                continue;
            }
            if (!pidField.equals(onlCgformField.getDbFieldName()) || !oConvertUtils.isEmpty((Object)json.get((Object)pidField))) continue;
            onlCgformField.setIsShowForm(1);
            json.put(pidField, (Object)c);
        }
        Map<String, Object> map = org.jeecg.modules.online.cgform.d.c.a(tbname, (List<OnlCgformField>)list, json);
        this.addOnlineInsertDataLog(tbname, map.get("id").toString());
        ((OnlCgformFieldMapper)this.baseMapper).executeInsertSQL(map);
        if (!c.equals(json.getString(pidField))) {
            OnlCgformField onlCgformField;
            onlCgformField = new UpdateWrapper();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(hasChildField, (Object)"1");
            onlCgformField.eq("id", json.getString(pidField));
            this.a(tbname, jSONObject, (UpdateWrapper<?>)onlCgformField);
        }
    }

    @Override
    public void saveFormData(List<OnlCgformField> fieldList, String tbname, JSONObject json) {
        Map<String, Object> map = org.jeecg.modules.online.cgform.d.c.a(tbname, fieldList, json);
        this.onlCgformFieldMapper.executeInsertSQL(map);
    }

    @Override
    public void editFormData(String code, String tbname, JSONObject json, boolean isCrazy) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)code);
        List list = this.list((Wrapper)lambdaQueryWrapper);
        if (isCrazy) {
            Map<String, Object> map = org.jeecg.modules.online.cgform.d.c.d(tbname, list, json);
            this.addOnlineUpdateDataLog(tbname, map.get("id").toString(), list, json);
            this.onlCgformFieldMapper.executeUpdatetSQL(map);
        } else {
            Map<String, Object> map = org.jeecg.modules.online.cgform.d.c.b(tbname, (List<OnlCgformField>)list, json);
            this.addOnlineUpdateDataLog(tbname, map.get("id").toString(), list, json);
            this.onlCgformFieldMapper.executeUpdatetSQL(map);
        }
    }

    @Override
    public void editTreeFormData(String code, String tbname, JSONObject json, String hasChildField, String pidField) {
        String string = org.jeecg.modules.online.cgform.d.c.f(tbname);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq((Object)"id", (Object)json.getString("id"));
        Map map = (Map)this.a(string, null, queryWrapper, JSONObject.class);
        Map<String, Object> map2 = org.jeecg.modules.online.cgform.d.c.a(map);
        String string2 = map2.get(pidField).toString();
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)code);
        List list = this.list((Wrapper)lambdaQueryWrapper);
        for (OnlCgformField onlCgformField : list) {
            if (!pidField.equals(onlCgformField.getDbFieldName()) || !oConvertUtils.isEmpty((Object)json.get((Object)pidField))) continue;
            onlCgformField.setIsShowForm(1);
            json.put(pidField, (Object)c);
        }
        Map<String, Object> map3 = org.jeecg.modules.online.cgform.d.c.b(tbname, (List<OnlCgformField>)list, json);
        this.addOnlineUpdateDataLog(tbname, map3.get("id").toString(), list, json);
        ((OnlCgformFieldMapper)this.baseMapper).executeUpdatetSQL(map3);
        if (!string2.equals(json.getString(pidField))) {
            Integer n;
            OnlCgformField onlCgformField;
            onlCgformField = new JSONObject();
            if (!(c.equals(string2) || (n = this.queryCountBySql(string, pidField, string2)) != null && n != 0)) {
                onlCgformField.put(hasChildField, c);
                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.eq((Object)"id", (Object)string2);
                this.a(string, (JSONObject)onlCgformField, updateWrapper);
            }
            if (!c.equals(json.getString(pidField))) {
                onlCgformField.put(hasChildField, "1");
                n = new UpdateWrapper();
                n.eq("id", json.getString(pidField));
                this.a(string, (JSONObject)onlCgformField, (UpdateWrapper<?>)n);
            }
        }
    }

    @Override
    public Map<String, Object> queryFormData(String code, String tbname, String id) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)code);
        lambdaQueryWrapper.eq(OnlCgformField::getIsShowForm, (Object)1);
        List list = this.list((Wrapper)lambdaQueryWrapper);
        QueryWrapper<?> queryWrapper = org.jeecg.modules.online.cgform.d.c.a((List<OnlCgformField>)list, id);
        return (Map)this.a(tbname, queryWrapper, JSONObject.class);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @Transactional(rollbackFor={Exception.class})
    public void deleteAutoListMainAndSub(OnlCgformHead head, String ids) {
        if (head.getTableType() == 2) {
            String string = head.getId();
            String string2 = head.getTableName();
            String string3 = "tableName";
            String string4 = "linkField";
            String string5 = "linkValueStr";
            String string6 = "mainField";
            ArrayList arrayList = new ArrayList();
            if (oConvertUtils.isNotEmpty((Object)head.getSubTableStr())) {
                HashMap<String, Object> hashMap;
                Object object;
                for (Object object2 : head.getSubTableStr().split(",")) {
                    LambdaQueryWrapper lambdaQueryWrapper;
                    List object3;
                    OnlCgformHead onlCgformHead = (OnlCgformHead)this.cgformHeadMapper.selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, object2));
                    if (onlCgformHead == null || (object3 = this.list((Wrapper)(lambdaQueryWrapper = (LambdaQueryWrapper)((LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformField::getCgformHeadId, (Object)onlCgformHead.getId())).eq(OnlCgformField::getMainTable, (Object)head.getTableName())))) == null || object3.size() == 0) continue;
                    object = (OnlCgformField)object3.get(0);
                    hashMap = new HashMap<String, Object>(5);
                    hashMap.put(string4, ((OnlCgformField)object).getDbFieldName());
                    hashMap.put(string6, ((OnlCgformField)object).getMainField());
                    hashMap.put(string3, object2);
                    hashMap.put(string5, "");
                    arrayList.add(hashMap);
                }
                LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
                lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)string);
                List list = this.list((Wrapper)lambdaQueryWrapper);
                String[] stringArray = ids.split(",");
                for (String string7 : stringArray) {
                    String parsedId = string7;
                    if (string7.indexOf("@") > 0) {
                        String string8 = string7.substring(0, string7.indexOf("@"));
                        parsedId = string8;
                    }
                    object = org.jeecg.modules.online.cgform.d.c.a((List<OnlCgformField>)list, (String)parsedId);
                    hashMap = (HashMap<String, Object>)this.a(string2, (QueryWrapper<?>)object, (Class)JSONObject.class);
                    ArrayList arrayList2 = new ArrayList();
                    for (Map map : arrayList) {
                        Object v = hashMap.get(((String)map.get(string6)).toLowerCase());
                        if (v == null) {
                            v = hashMap.get(((String)map.get(string6)).toUpperCase());
                        }
                        if (v == null) continue;
                        String string9 = (String)map.get(string5) + String.valueOf(v) + ",";
                        map.put(string5, string9);
                    }
                }
                for (Map map : arrayList) {
                    this.deleteAutoList((String)map.get(string3), (String)map.get(string4), (String)map.get(string5));
                }
            }
            this.deleteAutoListById(head.getTableName(), ids);
        }
    }

    @Override
    public void deleteAutoListById(String tbname, String ids) {
        this.deleteAutoList(tbname, "id", ids);
    }

    @Override
    public void deleteAutoList(String tbname, String linkField, String linkValue) {
        if (linkValue != null && !"".equals(linkValue)) {
            QueryWrapper queryWrapper = linkValue.split(",");
            ArrayList<String> arrayList = new ArrayList<String>();
            for (String string : queryWrapper) {
                if (string == null || "".equals(string)) continue;
                if (string.indexOf("@") > 0) {
                    string = string.substring(0, string.indexOf("@"));
                }
                arrayList.add(string);
            }
            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.in((Object)SqlInjectionUtil.getSqlInjectField((String)linkField), arrayList.toArray());
            this.a(org.jeecg.modules.online.cgform.d.c.f(tbname), queryWrapper2);
        }
    }

    @Override
    public List<Map<String, String>> getAutoListQueryInfo(String code) {
        String string;
        int n = 0;
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.cgformHeadMapper.selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getId, (Object)code));
        ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
        boolean bl = org.jeecg.modules.online.cgform.d.c.a(onlCgformHead);
        n = this.a(onlCgformHead, arrayList, n, bl);
        Integer n2 = onlCgformHead.getTableType();
        if (bl && n2 != null && 2 == n2 && (string = onlCgformHead.getSubTableStr()) != null && !"".equals(string)) {
            String[] stringArray;
            for (String string2 : stringArray = string.split(",")) {
                OnlCgformHead onlCgformHead2 = (OnlCgformHead)this.cgformHeadMapper.selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string2));
                if (onlCgformHead2 == null) continue;
                n = this.a(onlCgformHead2, arrayList, n, true);
            }
        }
        return arrayList;
    }

    @Override
    public List<OnlCgformField> queryFormFields(String code, boolean isform) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)code);
        if (isform) {
            lambdaQueryWrapper.eq(OnlCgformField::getIsShowForm, (Object)1);
        }
        return this.list((Wrapper)lambdaQueryWrapper);
    }

    @Override
    public List<OnlCgformField> queryFormFieldsByTableName(String tableName) {
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.cgformHeadMapper.selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)tableName));
        if (onlCgformHead != null) {
            LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)onlCgformHead.getId());
            return this.list((Wrapper)lambdaQueryWrapper);
        }
        return null;
    }

    @Override
    public OnlCgformField queryFormFieldByTableNameAndField(String tableName, String fieldName) {
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.cgformHeadMapper.selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)tableName));
        if (onlCgformHead != null) {
            LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)onlCgformHead.getId());
            lambdaQueryWrapper.eq(OnlCgformField::getDbFieldName, (Object)fieldName);
            if (this.list((Wrapper)lambdaQueryWrapper) != null && this.list((Wrapper)lambdaQueryWrapper).size() > 0) {
                return (OnlCgformField)this.list((Wrapper)lambdaQueryWrapper).get(0);
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> queryFormData(List<OnlCgformField> fieldList, String tbname, String id) {
        QueryWrapper<?> queryWrapper = org.jeecg.modules.online.cgform.d.c.a(fieldList, id);
        return (Map)this.a(tbname, queryWrapper, JSONObject.class);
    }

    @Override
    public Map<String, Object> generateMockData(String tableName) {
        List<OnlCgformField> list = this.queryFormFieldsByTableName(tableName);
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        if (list == null || list.size() == 0) {
            return hashMap;
        }
        for (OnlCgformField onlCgformField : list) {
            String string = onlCgformField.getDbFieldName();
            hashMap.put(string, "");
        }
        return hashMap;
    }

    @Override
    public List<Map<String, Object>> querySubFormData(List<OnlCgformField> fieldList, String tbname, String linkField, String value) {
        QueryWrapper<?> queryWrapper = org.jeecg.modules.online.cgform.d.c.a(fieldList, linkField, value);
        Collection collection = this.a(tbname, queryWrapper, Collection.class);
        return collection.stream().map(object -> (JSONObject)object).collect(Collectors.toList());
    }

    @Override
    public List<String> selectOnlineHideColumns(String tbname) {
        String string = "online:" + tbname + ":%";
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String string2 = loginUser.getId();
        List<String> list = ((OnlCgformFieldMapper)this.baseMapper).selectOnlineHideColumns(string2, string);
        return this.a(list);
    }

    @Override
    public List<OnlCgformField> queryAvailableFields(String cgFormId, String tbname, String taskId, boolean isList) {
        List<String> list;
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)cgFormId);
        if (isList) {
            lambdaQueryWrapper.eq(OnlCgformField::getIsShowList, (Object)1);
        } else {
            lambdaQueryWrapper.eq(OnlCgformField::getIsShowForm, (Object)1);
        }
        lambdaQueryWrapper.orderByAsc(OnlCgformField::getOrderNum);
        List list2 = this.list((Wrapper)lambdaQueryWrapper);
        String string = "online:" + tbname + "%";
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String string2 = loginUser.getId();
        ArrayList<String> arrayList = new ArrayList<String>();
        if (oConvertUtils.isEmpty((Object)taskId)) {
            list = this.onlAuthPageService.queryHideCode(string2, cgFormId, isList);
            if (list != null && list.size() != 0 && list.get(0) != null) {
                arrayList.addAll(list);
            }
        } else if (oConvertUtils.isNotEmpty((Object)taskId) && (list = ((OnlCgformFieldMapper)this.baseMapper).selectFlowAuthColumns(tbname, taskId, "1")) != null && list.size() > 0 && list.get(0) != null) {
            arrayList.addAll(list);
        }
        if (arrayList.size() == 0) {
            return list2;
        }
        list = new ArrayList<String>();
        for (int i2 = 0; i2 < list2.size(); ++i2) {
            OnlCgformField onlCgformField = (OnlCgformField)list2.get(i2);
            if (!this.b(onlCgformField.getDbFieldName(), arrayList)) continue;
            list.add((String)((Object)onlCgformField));
        }
        return list;
    }

    @Override
    public List<String> queryDisabledFields(String tbname) {
        String string = "online:" + tbname + "%";
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String string2 = loginUser.getId();
        List<String> list = ((OnlCgformFieldMapper)this.baseMapper).selectOnlineDisabledColumns(string2, string);
        return this.a(list);
    }

    @Override
    public List<String> queryDisabledFields(String tbname, String taskId) {
        if (oConvertUtils.isEmpty((Object)taskId)) {
            return null;
        }
        List<String> list = ((OnlCgformFieldMapper)this.baseMapper).selectFlowAuthColumns(tbname, taskId, "2");
        return this.a(list);
    }

    private List<String> a(List<String> list) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (list == null || list.size() == 0 || list.get(0) == null) {
            return arrayList;
        }
        for (String string : list) {
            String string2;
            if (oConvertUtils.isEmpty((Object)string) || oConvertUtils.isEmpty((Object)(string2 = string.substring(string.lastIndexOf(":") + 1)))) continue;
            arrayList.add(string2);
        }
        return arrayList;
    }

    @Override
    public List<OnlCgformField> queryAvailableFields(String tbname, boolean isList, List<OnlCgformField> List2, List<String> needList) {
        String string = "online:" + tbname + "%";
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String string2 = loginUser.getId();
        List<String> list = ((OnlCgformFieldMapper)this.baseMapper).selectOnlineHideColumns(string2, string);
        return this.a(list, isList, List2, needList);
    }

    @Override
    public List<OnlCgformField> queryAvailableFields(String cgformId, String tbname, boolean isList, List<OnlCgformField> List2, List<String> needList) {
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String string = loginUser.getId();
        List<String> list = this.onlAuthPageService.queryListHideColumn(string, cgformId);
        return this.a(list, isList, List2, needList);
    }

    private List<OnlCgformField> a(List<String> list, boolean bl, List<OnlCgformField> list2, List<String> list3) {
        ArrayList<OnlCgformField> arrayList = new ArrayList<OnlCgformField>();
        boolean bl2 = true;
        if (list == null || list.size() == 0 || list.get(0) == null) {
            bl2 = false;
        }
        for (OnlCgformField onlCgformField : list2) {
            String string = onlCgformField.getDbFieldName();
            if (list3 != null && list3.contains(string)) {
                onlCgformField.setIsQuery(1);
                arrayList.add(onlCgformField);
                continue;
            }
            if (bl) {
                if (onlCgformField.getIsShowList() != 1) {
                    if (!oConvertUtils.isNotEmpty((Object)onlCgformField.getMainTable()) || !oConvertUtils.isNotEmpty((Object)onlCgformField.getMainField())) continue;
                    arrayList.add(onlCgformField);
                    continue;
                }
            } else if (onlCgformField.getIsShowForm() != 1) continue;
            if (bl2) {
                if (!this.b(string, list)) continue;
                arrayList.add(onlCgformField);
                continue;
            }
            arrayList.add(onlCgformField);
        }
        return arrayList;
    }

    private boolean b(String string, List<String> list) {
        boolean bl = true;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            String string2;
            String string3 = list.get(i2);
            if (oConvertUtils.isEmpty((Object)string3) || oConvertUtils.isEmpty((Object)(string2 = string3.substring(string3.lastIndexOf(":") + 1))) || !string2.equals(string)) continue;
            bl = false;
        }
        return bl;
    }

    public boolean a(String string, List<OnlCgformField> list) {
        boolean bl = false;
        for (OnlCgformField onlCgformField : list) {
            if (!oConvertUtils.camelToUnderline((String)string).equals(onlCgformField.getDbFieldName())) continue;
            bl = true;
            break;
        }
        return bl;
    }

    @Override
    public void executeInsertSQL(Map<String, Object> params) {
        ((OnlCgformFieldMapper)this.baseMapper).executeInsertSQL(params);
    }

    @Override
    public void executeUpdatetSQL(Map<String, Object> params) {
        ((OnlCgformFieldMapper)this.baseMapper).executeUpdatetSQL(params);
    }

    @Override
    public List<TreeModel> queryDataListByLinkDown(org.jeecg.modules.online.cgform.a.a linkDown) {
        linkDown.setTable(SqlInjectionUtil.getSqlInjectTableName((String)linkDown.getTable()));
        linkDown.setKey(SqlInjectionUtil.getSqlInjectField((String)linkDown.getKey()));
        linkDown.setTxt(SqlInjectionUtil.getSqlInjectField((String)linkDown.getTxt()));
        linkDown.setIdField(SqlInjectionUtil.getSqlInjectField((String)linkDown.getIdField()));
        linkDown.setPidField(SqlInjectionUtil.getSqlInjectField((String)linkDown.getPidField()));
        if (oConvertUtils.isNotEmpty((Object)linkDown.getLinkField())) {
            linkDown.setLinkField(SqlInjectionUtil.getSqlInjectField((String[])new String[0]));
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select((Object[])new String[]{linkDown.getKey() + " as store", linkDown.getTxt() + " as label", linkDown.getIdField() + " as id", linkDown.getPidField() + " as pid"});
        if (oConvertUtils.isNotEmpty((Object)linkDown.getPidValue())) {
            queryWrapper.eq((Object)linkDown.getPidField(), (Object)linkDown.getPidValue());
        } else if (oConvertUtils.isNotEmpty((Object)linkDown.getCondition())) {
            SqlInjectionUtil.specialFilterContentForDictSql((String)linkDown.getCondition());
            queryWrapper.apply(linkDown.getCondition(), new Object[0]);
        }
        Class<List> clazz = List.class;
        List list = this.a(linkDown.getTable(), queryWrapper, clazz);
        return list.stream().map(jSONObject -> (TreeModel)jSONObject.toJavaObject(TreeModel.class)).collect(Collectors.toList());
    }

    @Override
    public void updateTreeNodeNoChild(String tableName, String filed, String id) {
        Map<String, Object> map = org.jeecg.modules.online.cgform.d.c.a(tableName, filed, id);
        ((OnlCgformFieldMapper)this.baseMapper).executeUpdatetSQL(map);
    }

    @Override
    public String queryTreeChildIds(OnlCgformHead head, String ids) {
        String string = head.getTreeParentIdField();
        String string2 = head.getTableName();
        String[] stringArray = ids.split(",");
        StringBuffer stringBuffer = new StringBuffer();
        for (String string3 : stringArray) {
            if (string3 == null || stringBuffer.toString().contains(string3)) continue;
            if (stringBuffer.toString().length() > 0) {
                stringBuffer.append(",");
            }
            stringBuffer.append(string3);
            this.a(string3, string, string2, stringBuffer);
        }
        return stringBuffer.toString();
    }

    @Override
    public String queryTreePids(OnlCgformHead head, String ids) {
        CharSequence[] charSequenceArray;
        String string = head.getTreeParentIdField();
        String string2 = head.getTableName();
        StringBuffer stringBuffer = new StringBuffer();
        for (String string3 : charSequenceArray = ids.split(",")) {
            if (string3 == null) continue;
            String string4 = org.jeecg.modules.online.cgform.d.c.f(string2);
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq((Object)"id", (Object)string3);
            Map map = (Map)this.a(string4, null, queryWrapper, JSONObject.class);
            Map<String, Object> map2 = org.jeecg.modules.online.cgform.d.c.a(map);
            String string5 = map2.get(string).toString();
            String string6 = "'" + String.join((CharSequence)"','", charSequenceArray) + "'";
            List<Map<String, Object>> list = this.queryListBySql(string4, null, string, string5, string6);
            if (list != null && list.size() != 0 || Arrays.asList(charSequenceArray).contains(string5) || stringBuffer.toString().contains(string5)) continue;
            stringBuffer.append(string5).append(",");
        }
        return stringBuffer.toString();
    }

    @Override
    public String queryForeignKey(String cgFormId, String mainTable) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)cgFormId);
        lambdaQueryWrapper.eq(OnlCgformField::getMainTable, (Object)mainTable);
        List list = this.list((Wrapper)lambdaQueryWrapper);
        if (list != null && list.size() > 0) {
            return ((OnlCgformField)list.get(0)).getMainField();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> queryListBySql(String tableName, String fields, String pidField, String metaPid, String inIds) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (oConvertUtils.isNotEmpty((Object)pidField)) {
            pidField = SqlInjectionUtil.getSqlInjectField((String)pidField);
            queryWrapper.eq((Object)pidField, (Object)metaPid);
        }
        if (oConvertUtils.isNotEmpty((Object)inIds)) {
            queryWrapper.in((Object)"id", Arrays.asList(inIds.split(",")));
        }
        Collection collection = this.a(tableName, fields, queryWrapper, Collection.class);
        return collection.stream().map(object -> (JSONObject)object).collect(Collectors.toList());
    }

    private StringBuffer a(String string, String string2, String string3, StringBuffer stringBuffer) {
        List<Map<String, Object>> list = this.queryListBySql(string3, null, string2, string, null);
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map : list) {
                Map<String, Object> map2 = org.jeecg.modules.online.cgform.d.c.a(map);
                if (!stringBuffer.toString().contains(map2.get("id").toString())) {
                    stringBuffer.append(",").append(map2.get("id"));
                }
                this.a(map2.get("id").toString(), string2, string3, stringBuffer);
            }
        }
        return stringBuffer;
    }

    private String a(List<OnlCgformField> list, Map<String, Object> map) {
        Object object;
        Object object22;
        ArrayList<Object> arrayList;
        Object object3 = map.get("column");
        ArrayList<Object> arrayList2 = new ArrayList<Object>();
        if (object3 != null && !"id".equals(object3.toString())) {
            arrayList = object3.toString();
            Object iterator = map.get("order");
            object22 = "desc";
            if (iterator != null) {
                object22 = iterator.toString();
            }
            object = new h((String)((Object)arrayList), (String)object22);
            arrayList2.add(object);
        } else {
            for (OnlCgformField onlCgformField : list) {
                JSONObject jSONObject;
                String string;
                object22 = onlCgformField.getFieldExtendJson();
                object = new h(onlCgformField.getDbFieldName());
                if (!oConvertUtils.isNotEmpty((Object)object22) || !((String)object22).trim().startsWith("{") || !((String)object22).trim().endsWith("}") || (string = (jSONObject = JSON.parseObject((String)object22)).getString("orderRule")) == null || "".equals(string)) continue;
                ((h)object).setRule(string);
                arrayList2.add(object);
            }
            if (arrayList2.size() == 0) {
                arrayList = h.a();
                arrayList2.add(arrayList);
            }
        }
        arrayList = new ArrayList<Object>();
        for (Object object22 : arrayList2) {
            if (!this.a(((h)object22).getColumn(), list)) continue;
            object = ((h)object22).getRealSql();
            arrayList.add(object);
        }
        return " ORDER BY " + String.join((CharSequence)",", arrayList);
    }

    private int a(OnlCgformHead onlCgformHead, List<Map<String, String>> list, int n, boolean bl) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)onlCgformHead.getId());
        lambdaQueryWrapper.eq(OnlCgformField::getIsQuery, (Object)1);
        lambdaQueryWrapper.eq(OnlCgformField::getDbIsPersist, (Object)org.jeecg.modules.online.cgform.b.b.b);
        lambdaQueryWrapper.orderByAsc(OnlCgformField::getOrderNum);
        List list2 = this.list((Wrapper)lambdaQueryWrapper);
        for (OnlCgformField onlCgformField : list2) {
            String string;
            String[] stringArray;
            String string2;
            String string3;
            HashMap<String, String> hashMap = new HashMap<String, String>(5);
            hashMap.put("label", onlCgformField.getDbFieldTxt());
            if (bl) {
                hashMap.put("field", onlCgformHead.getTableName() + "@" + onlCgformField.getDbFieldName());
            } else {
                hashMap.put("field", onlCgformField.getDbFieldName());
            }
            hashMap.put("dbField", onlCgformField.getDbFieldName());
            hashMap.put("mode", onlCgformField.getQueryMode());
            if (oConvertUtils.isNotEmpty((Object)onlCgformField.getFieldExtendJson())) {
                hashMap.put("fieldExtendJson", onlCgformField.getFieldExtendJson());
            }
            if ("1".equals(string3 = onlCgformField.getQueryConfigFlag())) {
                string2 = onlCgformField.getQueryShowType();
                hashMap.put("config", "1");
                hashMap.put("view", string2);
                hashMap.put("defValue", onlCgformField.getQueryDefVal());
                if ("cat_tree".equals(string2)) {
                    hashMap.put("pcode", onlCgformField.getQueryDictField());
                } else if ("sel_tree".equals(string2)) {
                    stringArray = onlCgformField.getQueryDictText().split(",");
                    string = onlCgformField.getQueryDictTable() + "," + stringArray[2] + "," + stringArray[0];
                    hashMap.put("dict", string);
                    hashMap.put("pidField", stringArray[1]);
                    hashMap.put("hasChildField", stringArray[3]);
                    hashMap.put("pidValue", onlCgformField.getQueryDictField());
                } else {
                    hashMap.put("dictTable", onlCgformField.getQueryDictTable());
                    hashMap.put("dictCode", onlCgformField.getQueryDictField());
                    hashMap.put("dictText", onlCgformField.getQueryDictText());
                }
            } else {
                string2 = onlCgformField.getFieldShowType();
                hashMap.put("view", string2);
                hashMap.put("mode", onlCgformField.getQueryMode());
                if ("cat_tree".equals(string2)) {
                    hashMap.put("pcode", onlCgformField.getDictField());
                } else if ("sel_tree".equals(string2)) {
                    stringArray = onlCgformField.getDictText().split(",");
                    string = onlCgformField.getDictTable() + "," + stringArray[2] + "," + stringArray[0];
                    hashMap.put("dict", string);
                    hashMap.put("pidField", stringArray[1]);
                    hashMap.put("hasChildField", stringArray[3]);
                    hashMap.put("pidValue", onlCgformField.getDictField());
                } else {
                    hashMap.put("dictTable", onlCgformField.getDictTable());
                    hashMap.put("dictCode", onlCgformField.getDictField());
                    hashMap.put("dictText", onlCgformField.getDictText());
                }
            }
            if (++n > 2) {
                hashMap.put("hidden", "1");
            }
            list.add(hashMap);
        }
        return n;
    }

    @Override
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public void clearCacheOnlineConfig() {
    }

    @Override
    public e getQueryInfo(OnlCgformHead head, Map<String, Object> params, List<String> needList) {
        String string = SqlInjectionUtil.getSqlInjectTableName((String)head.getTableName());
        String string2 = head.getId();
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)string2);
        lambdaQueryWrapper.eq(OnlCgformField::getDbIsPersist, (Object)org.jeecg.modules.online.cgform.b.b.b);
        lambdaQueryWrapper.orderByAsc(OnlCgformField::getOrderNum);
        List list = this.list((Wrapper)lambdaQueryWrapper);
        List<Object> list2 = new ArrayList();
        List<String> list3 = head.getSelectFieldList();
        list2 = list3 != null && list3.size() > 0 ? this.a(string2, list, list3, needList) : this.queryAvailableFields(string2, string, true, list, needList);
        StringBuffer stringBuffer = new StringBuffer();
        org.jeecg.modules.online.cgform.d.c.a(string, list2, stringBuffer);
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String string3 = loginUser.getId();
        List<SysPermissionDataRuleModel> list4 = this.onlAuthDataService.queryUserOnlineAuthData(string3, string2);
        if (list4 != null && list4.size() > 0) {
            JeecgDataAutorUtils.installUserInfo((SysUserCacheInfo)this.sysBaseAPI.getCacheUser(loginUser.getUsername()));
        }
        DbType dbType = d.c(null);
        a a2 = new a("t.", dbType.getDb());
        a2.setTableName(string);
        a2.setNeedList(needList);
        a2.setSubTableStr(head.getSubTableStr());
        List<OnlineFieldConfig> list5 = org.jeecg.modules.online.cgform.d.c.g(list);
        String string4 = a2.a(list5, params, list4);
        Map<String, Object> map = a2.getSqlParams();
        if (string4.trim().length() > 0) {
            stringBuffer.append(" t ").append(" where  ").append(string4);
        }
        String string5 = this.a(list, params);
        stringBuffer.append(string5);
        e e2 = new e(stringBuffer.toString(), map);
        e2.setFieldList(list2);
        return e2;
    }

    @Override
    public void addOnlineInsertDataLog(String tableName, String dataId) {
        a.execute(() -> {
            String string3 = " \u521b\u5efa\u4e86\u8bb0\u5f55";
            DataLogDTO dataLogDTO = new DataLogDTO(tableName, dataId, string3, "comment");
            this.sysBaseAPI.saveDataLog(dataLogDTO);
        });
    }

    @Override
    public void addOnlineUpdateDataLog(String tableName, String dataId, List<OnlCgformField> fieldList, JSONObject json) {
        Map<String, Object> map;
        String string = org.jeecg.modules.online.cgform.d.c.f(tableName);
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.select((Object[])new SFunction[]{OnlCgformHead::getTableType, OnlCgformHead::getTableTxt, OnlCgformHead::getSubTableStr});
        lambdaQueryWrapper.eq(OnlCgformHead::getTableName, (Object)string);
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.cgformHeadMapper.selectOne((Wrapper)lambdaQueryWrapper);
        if (onlCgformHead == null) {
            return;
        }
        HashSet hashSet = new HashSet();
        if (oConvertUtils.isNotEmpty((Object)onlCgformHead.getSubTableStr())) {
            hashSet.addAll(Arrays.stream(onlCgformHead.getSubTableStr().split(",")).collect(Collectors.toSet()));
        }
        if ((map = this.a(string, dataId)) != null) {
            StringBuilder stringBuilder = new StringBuilder();
            if (hashSet.size() > 0) {
                for (String string2 : hashSet) {
                    JSONArray jSONArray;
                    String string3 = org.jeecg.modules.online.cgform.d.c.f(string2);
                    String string4 = this.a(string3, jSONArray = json.getJSONArray(string3), string, dataId);
                    if (!oConvertUtils.isNotEmpty((Object)string4)) continue;
                    stringBuilder.append(string4).append("\uff1b");
                }
            }
            String string5 = TokenUtils.getTokenByRequest();
            a.execute(() -> {
                UserTokenContext.setToken((String)string5);
                DataLogDTO dataLogDTO = new DataLogDTO(string, dataId, "comment");
                StringBuilder stringBuilder2 = new StringBuilder(this.a(fieldList, json, map));
                if (stringBuilder.length() > 0) {
                    stringBuilder2.append("\uff1b").append((CharSequence)stringBuilder);
                }
                String string5 = stringBuilder2.toString();
                if (oConvertUtils.isNotEmpty((Object)(string5 = Arrays.stream(string5.split("\uff1b")).filter(string -> oConvertUtils.isNotEmpty((Object)string.trim())).collect(Collectors.joining("\uff1b"))))) {
                    dataLogDTO.setContent(string5);
                    this.sysBaseAPI.saveDataLog(dataLogDTO);
                }
            });
        }
    }

    private String a(List<OnlCgformField> list, JSONObject jSONObject, Map<String, Object> map) {
        StringBuffer stringBuffer = new StringBuffer();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (OnlCgformField onlCgformField : list) {
            Object object;
            Object object2;
            String string;
            String string2 = onlCgformField.getDbFieldName();
            if (null == string2 || "id".equalsIgnoreCase(string2) || "SYS_ORG_CODE".equalsIgnoreCase(string2) || "CREATE_BY".equalsIgnoreCase(string2) || "UPDATE_BY".equalsIgnoreCase(string2) || "CREATE_TIME".equalsIgnoreCase(string2) || "UPDATE_TIME".equalsIgnoreCase(string2) || "blob".equalsIgnoreCase(string = onlCgformField.getDbType()) || "text".equalsIgnoreCase(string) || oConvertUtils.isEmpty((Object)jSONObject.get((Object)string2)) && oConvertUtils.isEmpty((Object)map.get(string2))) continue;
            String string3 = "\u7a7a";
            Object object3 = "";
            object3 = jSONObject.get((Object)string2) == null ? "\u7a7a" : (((String)(object2 = jSONObject.get((Object)string2).toString())).length() == 0 ? "\u7a7a" : object2);
            if ("Datetime".equalsIgnoreCase(string)) {
                if (map.get(string2) != null) {
                    object2 = null;
                    object = map.get(string2);
                    if (object instanceof Timestamp) {
                        Timestamp timestamp = (Timestamp)object;
                        object2 = LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
                    } else {
                        object2 = (LocalDateTime)map.get(string2);
                    }
                    string3 = dateTimeFormatter.format((TemporalAccessor)object2);
                }
            } else if ("BigDecimal".equalsIgnoreCase(string) || "double".equalsIgnoreCase(string)) {
                object2 = onlCgformField.getDbPointLength();
                if (map.get(string2) != null) {
                    string3 = map.get(string2).toString();
                }
                if (jSONObject.get((Object)string2) == null || jSONObject.get((Object)string2).toString().length() == 0) {
                    object3 = "\u7a7a";
                } else {
                    object = jSONObject.get((Object)string2).toString();
                    if (((String)object).indexOf(".") < 0) {
                        object = (String)object + ".";
                    }
                    if (((String)object).length() - 1 - ((String)object).indexOf(".") < (Integer)object2) {
                        int n = 0;
                        while ((n = ((String)(object = (String)object + c)).length() - 1 - ((String)object).indexOf(".")) < (Integer)object2) {
                        }
                    }
                    object3 = object;
                }
            } else if (map.get(string2) != null && (string3 = map.get(string2).toString()).length() == 0) {
                string3 = "\u7a7a";
            }
            if (string3.equals(object3) || "double".equalsIgnoreCase(string) && map.get(string2) != null && jSONObject.get((Object)string2) != null && ((BigDecimal)(object2 = new BigDecimal(map.get(string2).toString()))).compareTo((BigDecimal)(object = new BigDecimal(jSONObject.get((Object)string2).toString()))) == 0) continue;
            if (oConvertUtils.isNotEmpty((Object)onlCgformField.getDictField()) && !"popup".equalsIgnoreCase(onlCgformField.getFieldShowType()) && ((String[])(object2 = this.a(onlCgformField, string3, (String)object3))).length == 2) {
                string3 = object2[0];
                object3 = object2[1];
            }
            stringBuffer.append("  \u5c06\u540d\u79f0\u4e3a\u3010" + onlCgformField.getDbFieldTxt() + "\u3011\u7684\u5b57\u6bb5\u5185\u5bb9 " + string3 + " \u4fee\u6539\u4e3a " + (String)object3 + "\uff1b  ");
        }
        return stringBuffer.toString();
    }

    private String[] a(OnlCgformField onlCgformField, String string, String string2) {
        Object object;
        List list2 = null;
        if ("popup_dict".equalsIgnoreCase(onlCgformField.getFieldShowType())) {
            object = this.a(onlCgformField.getDictTable(), onlCgformField.getDictField(), onlCgformField.getDictText(), string + "," + string2);
            System.out.println(object);
            if (null != object && !object.isEmpty()) {
                list2 = object.values().stream().filter(Objects::nonNull).map(list -> new DictModel(((Map)list.get(0)).get(onlCgformField.getDictField()) + "", ((Map)list.get(0)).get(onlCgformField.getDictText()) + "")).collect(Collectors.toList());
            }
        } else {
            object = "";
            object = oConvertUtils.isNotEmpty((Object)onlCgformField.getDictTable()) ? onlCgformField.getDictTable() + "," + onlCgformField.getDictText() + "," + onlCgformField.getDictField() : onlCgformField.getDictField();
            list2 = this.sysBaseAPI.getDictItems((String)object);
        }
        if (oConvertUtils.listIsNotEmpty((Collection)list2)) {
            object = new ArrayList();
            if (string.contains(",")) {
                object = Arrays.asList(string.split(","));
            } else {
                object.add(string);
            }
            ArrayList<String> arrayList = new ArrayList<String>();
            if (string2.contains(",")) {
                arrayList = Arrays.asList(string2.split(","));
            } else {
                arrayList.add(string);
            }
            for (DictModel dictModel : list2) {
                org.jeecg.modules.online.cgform.service.impl.c.a(dictModel, object);
                org.jeecg.modules.online.cgform.service.impl.c.a(dictModel, arrayList);
            }
            if (oConvertUtils.listIsNotEmpty(object)) {
                string = String.join((CharSequence)",", object);
            }
            if (oConvertUtils.listIsNotEmpty(arrayList)) {
                string2 = String.join((CharSequence)",", arrayList);
            }
        }
        return new String[]{string, string2};
    }

    private static void a(DictModel dictModel, List<String> list) {
        String string = dictModel.getValue();
        if (oConvertUtils.listIsNotEmpty(list) && list.contains(string)) {
            list.replaceAll(string2 -> {
                if (string2.equalsIgnoreCase(string)) {
                    return dictModel.getText();
                }
                return string2;
            });
        }
    }

    private String a(String string, JSONArray jSONArray, String string2, String string3) {
        Object object2;
        if (jSONArray == null) {
            return "";
        }
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.select((Object[])new SFunction[]{OnlCgformHead::getId, OnlCgformHead::getTableTxt, OnlCgformHead::getRelationType});
        lambdaQueryWrapper.eq(OnlCgformHead::getTableName, (Object)string);
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.cgformHeadMapper.selectOne((Wrapper)lambdaQueryWrapper);
        if (onlCgformHead == null) {
            return "";
        }
        LambdaQueryWrapper lambdaQueryWrapper2 = new LambdaQueryWrapper();
        lambdaQueryWrapper2.eq(OnlCgformField::getCgformHeadId, (Object)onlCgformHead.getId());
        List<OnlCgformField> list = this.list((Wrapper)lambdaQueryWrapper2);
        if (list == null || list.size() == 0) {
            return "";
        }
        OnlCgformField onlCgformField3 = list.stream().filter(onlCgformField -> string2.equals(onlCgformField.getMainTable())).findFirst().orElse(null);
        if (onlCgformField3 == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq((Object)onlCgformField3.getDbFieldName(), (Object)string3);
        List list2 = this.a(string, null, queryWrapper, List.class);
        if (list2 == null || list2.isEmpty()) {
            if (jSONArray.isEmpty()) {
                return "";
            }
            stringBuilder.append("\u65b0\u589e\u4e86").append(jSONArray.size()).append("\u6761\u6570\u636e\uff0c");
        } else if (jSONArray.isEmpty()) {
            stringBuilder.append("\u5220\u9664\u4e86").append(list2.size()).append("\u6761\u6570\u636e\uff0c");
        } else {
            if (org.jeecg.modules.online.cgform.enums.a.g.equals(onlCgformHead.getRelationType())) {
                JSONObject jSONObject = jSONArray.getJSONObject(0);
                JSONObject jSONObject2 = (JSONObject)list2.get(0);
                String string4 = this.a(list = list.stream().filter(onlCgformField2 -> onlCgformField2 != onlCgformField3).collect(Collectors.toList()), jSONObject, jSONObject2.getInnerMap());
                if (oConvertUtils.isEmpty((Object)string4)) {
                    return "";
                }
                stringBuilder.append("\u5b50\u8868[").append(onlCgformHead.getTableTxt()).append("]\uff1a");
                return stringBuilder.append(string4).toString();
            }
            object2 = new AtomicInteger();
            int n = 0;
            int n2 = 0;
            Set set = list2.stream().map(object -> ((JSONObject)object).getString("id")).collect(Collectors.toSet());
            HashSet<String> hashSet = new HashSet<String>();
            for (int i2 = 0; i2 < jSONArray.size(); ++i2) {
                String string5 = jSONArray.getJSONObject(i2).getString("id");
                if (oConvertUtils.isEmpty((Object)string5)) {
                    ((AtomicInteger)object2).getAndIncrement();
                    continue;
                }
                hashSet.add(string5);
            }
            HashSet<Object> hashSet2 = new HashSet<Object>();
            hashSet2.addAll(set);
            hashSet2.addAll(hashSet);
            hashSet2.removeIf(oConvertUtils::isEmpty);
            block1: for (String string6 : hashSet2) {
                if (set.contains(string6) && !hashSet.contains(string6)) {
                    ++n2;
                    continue;
                }
                JSONObject jSONObject = null;
                for (int i3 = 0; i3 < jSONArray.size(); ++i3) {
                    JSONObject jSONObject3 = jSONArray.getJSONObject(i3);
                    if (!string6.equals(jSONObject3.getString("id"))) continue;
                    jSONObject = jSONObject3;
                    break;
                }
                JSONObject jSONObject4 = list2.stream().filter(object -> string6.equals(((JSONObject)object).getString("id"))).findFirst().orElse(null);
                if (jSONObject == null || jSONObject4 == null) continue;
                for (OnlCgformField onlCgformField4 : list) {
                    String string7;
                    if (onlCgformField4 == onlCgformField3 || "id".equalsIgnoreCase(string7 = onlCgformField4.getDbFieldName()) || "SYS_ORG_CODE".equalsIgnoreCase(string7) || "CREATE_BY".equalsIgnoreCase(string7) || "UPDATE_BY".equalsIgnoreCase(string7) || "CREATE_TIME".equalsIgnoreCase(string7) || "UPDATE_TIME".equalsIgnoreCase(string7)) continue;
                    String string8 = jSONObject4.getString(string7);
                    String string9 = jSONObject.getString(string7);
                    if (!oConvertUtils.isNotEmpty((Object)string8) || !oConvertUtils.isNotEmpty((Object)string9) || string8.equals(string9)) continue;
                    ++n;
                    continue block1;
                }
            }
            if (((AtomicInteger)object2).get() > 0) {
                stringBuilder.append("\u65b0\u589e\u4e86").append(((AtomicInteger)object2).get()).append("\u6761\u6570\u636e\uff0c");
            }
            if (n > 0) {
                stringBuilder.append("\u4fee\u6539\u4e86").append(n).append("\u6761\u6570\u636e\uff0c");
            }
            if (n2 > 0) {
                stringBuilder.append("\u5220\u9664\u4e86").append(n2).append("\u6761\u6570\u636e\uff0c");
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.insert(0, "]\uff1a").insert(0, onlCgformHead.getTableTxt()).insert(0, "\u5b50\u8868[");
        }
        if (((String)(object2 = stringBuilder.toString())).endsWith("\uff0c")) {
            object2 = ((String)object2).substring(0, ((String)object2).length() - 1);
        }
        return object2;
    }

    private Map<String, Object> a(String string, String string2) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq((Object)"id", (Object)string2);
        return (Map)this.a(string, null, queryWrapper, JSONObject.class);
    }

    @Override
    @Cacheable(value={"sys:cache:online:linkTable"}, key="#table+#textString+#code")
    public List<Map<String, Object>> queryLinkTableDictList(String table, String textString, String code) {
        String string = textString + "," + code;
        return this.queryListBySql(table, string, null, null, null);
    }

    @Override
    public void handleLinkTableDictData(String headId, List<Map<String, Object>> dataList) {
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        Object[] objectArray = new String[]{"link_table_field", "link_table", "popup_dict"};
        LambdaQueryWrapper lambdaQueryWrapper = (LambdaQueryWrapper)((LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformField::getCgformHeadId, (Object)headId)).in(OnlCgformField::getFieldShowType, objectArray);
        List list = this.onlCgformFieldMapper.selectList((Wrapper)lambdaQueryWrapper);
        if (list != null && list.size() > 0) {
            String string;
            Object object;
            String string2;
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            for (OnlCgformField onlCgformField : list) {
                if (!"link_table_field".equals(onlCgformField.getFieldShowType())) continue;
                string2 = onlCgformField.getDictTable();
                object = (List)hashMap.get(string2);
                if (object == null) {
                    object = new ArrayList();
                }
                string = onlCgformField.getDbFieldName() + "," + onlCgformField.getDictText();
                object.add(string);
                hashMap.put(string2, object);
            }
            for (OnlCgformField onlCgformField : list) {
                Map<Object, List<Object>> map2;
                Object object2;
                if ("link_table".equals(onlCgformField.getFieldShowType())) {
                    string2 = onlCgformField.getDictTable();
                    object = onlCgformField.getDictText();
                    string = onlCgformField.getDictField();
                    object2 = this.a(string2, (String)object, string);
                    map2 = this.b((List<OnlCgformField>)object2);
                    LambdaQueryWrapper lambdaQueryWrapper2 = new LambdaQueryWrapper();
                    lambdaQueryWrapper2.eq(OnlCgformHead::getTableName, (Object)string2);
                    OnlCgformHead onlCgformHead = (OnlCgformHead)this.cgformHeadMapper.selectOne((Wrapper)lambdaQueryWrapper2);
                    if (onlCgformHead == null) {
                        throw new JeecgBootException("\u672a\u627e\u5230\u5173\u8054\u8868\u3010" + string2 + "\u3011\u7684\u914d\u7f6e\u4fe1\u606f");
                    }
                    List<Map<String, Object>> list2 = this.queryLinkTableDictList(string2, (String)object, string);
                    String string3 = onlCgformField.getDbFieldName().toLowerCase();
                    String string4 = ((String)object).split(",")[0];
                    String string5 = string;
                    for (Map<String, Object> map3 : dataList) {
                        Object object3 = map3.get(string3);
                        if (object3 == null || "".equals(object3.toString())) continue;
                        String string6 = this.a(onlCgformHead, map2, list2, string4, string5, object3);
                        map3.put(string3 + "_dictText", string6);
                        List list3 = (List)hashMap.get(string3);
                        if (list3 == null || list3.size() <= 0) continue;
                        for (String string7 : list3) {
                            String[] stringArray = string7.split(",");
                            String string8 = stringArray[0];
                            String string9 = stringArray[1];
                            String string10 = this.a(onlCgformHead, map2, list2, string9, string5, object3);
                            map3.put(string8.toLowerCase(), string10);
                        }
                    }
                    continue;
                }
                if (!"popup_dict".equalsIgnoreCase(onlCgformField.getFieldShowType())) continue;
                string2 = onlCgformField.getDictTable();
                object = onlCgformField.getDictText();
                string = onlCgformField.getDictField();
                object2 = dataList.stream().map(map -> map.get(onlCgformField.getDbFieldName().toLowerCase())).filter(Objects::nonNull).map(Object::toString).collect(Collectors.joining(","));
                if (((String)object2).isEmpty()) continue;
                object2 = Arrays.stream(((String)object2).split(",")).distinct().collect(Collectors.joining(","));
                map2 = this.a(string2, string, (String)object, (String)object2);
                dataList.forEach(arg_0 -> c.a(onlCgformField, map2, (String)object, arg_0));
            }
        }
    }

    private Map<Object, List<Map<String, Object>>> a(String string, String string2, String string3, String string4) {
        Map<Object, List<Map<String, Object>>> map2;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("force_" + string2, string4);
        b.info("popup\u5b57\u5178\u5b57\u6bb5\u7ffb\u8bd1-\u67e5\u8be2online\u62a5\u8868\u53c2\u6570:" + JSONObject.toJSONString(hashMap));
        Map<String, Object> map3 = null;
        try {
            map3 = this.onlCgreportAPIService.getDataByCode(string, hashMap);
        }
        catch (Exception exception) {
            b.error("\u67e5\u8be2\u5b57\u5178\u6570\u636e\u5931\u8d25:{}", (Object)exception.getMessage(), (Object)exception);
        }
        if (null != map3 && !map3.isEmpty()) {
            Long l2 = (Long)map3.get("total");
            if (l2 > 0L) {
                List list = (List)map3.get("records");
                System.out.println(list);
                map2 = list.stream().filter(Objects::nonNull).map(map -> {
                    HashMap hashMap = new HashMap(2);
                    hashMap.put(string2, map.get(string2));
                    hashMap.put(string3, map.get(string3));
                    return hashMap;
                }).collect(Collectors.groupingBy(map -> map.get(string2)));
            } else {
                map2 = new HashMap();
            }
        } else {
            map2 = new HashMap<Object, List<Map<String, Object>>>();
        }
        return map2;
    }

    public void a(OnlCgformHead onlCgformHead, List<Map<String, Object>> list) {
        Object object;
        String string2;
        Map<String, Object> map22;
        boolean bl = "Y".equals(onlCgformHead.getIsTree());
        if (!bl) {
            return;
        }
        String string3 = onlCgformHead.getTreeParentIdField();
        ArrayList<String> arrayList = new ArrayList<String>();
        HashMap<String, List> hashMap = new HashMap<String, List>();
        for (Map<String, Object> map22 : list) {
            string2 = org.jeecg.modules.online.cgform.d.c.a(map22, string3);
            if (!oConvertUtils.isEmpty((Object)string2) && !c.equals(string2)) {
                arrayList.add(string2);
                hashMap.computeIfAbsent(string2, string -> new ArrayList()).add(map22);
                continue;
            }
            object = " ";
            map22.put(string3 + "_dictText", object);
        }
        if (arrayList.isEmpty()) {
            return;
        }
        String string4 = onlCgformHead.getTreeFieldname();
        map22 = new HashMap<String, Object>();
        map22.put("linkTableSelectFields", string3 + "," + (String)string4);
        map22.put("pageNo", "1");
        map22.put("pageSize", arrayList.size());
        map22.put("superQueryMatchType", "and");
        string2 = "%5B%7B%22field%22:%22id%22,%22rule%22:%22in%22,%22val%22:%22";
        string2 = string2 + String.join((CharSequence)",", arrayList);
        string2 = string2 + "%22%7D%5D";
        map22.put("superQueryParams", string2);
        object = this.queryAutolistPage(onlCgformHead, map22, new ArrayList<String>());
        if (object == null) {
            return;
        }
        JSONObject jSONObject = new JSONObject((Map)object);
        JSONArray jSONArray = jSONObject.getJSONArray("records");
        for (int i2 = 0; i2 < jSONArray.size(); ++i2) {
            String string5;
            JSONObject jSONObject2 = jSONArray.getJSONObject(i2);
            String string6 = org.jeecg.modules.online.cgform.d.c.a((Map<String, Object>)jSONObject2, "id");
            List list2 = (List)hashMap.get(string6);
            if (CollectionUtils.isEmpty((Collection)list2) || !oConvertUtils.isNotEmpty((Object)(string5 = org.jeecg.modules.online.cgform.d.c.a((Map<String, Object>)jSONObject2, string4)))) continue;
            list2.forEach(map -> map.put(string3 + "_dictText", string5));
        }
    }

    private List<OnlCgformField> a(String string, String string2, String string3) {
        LambdaQueryWrapper lambdaQueryWrapper = (LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string);
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.cgformHeadMapper.selectOne((Wrapper)lambdaQueryWrapper);
        if (onlCgformHead == null) {
            throw new JeecgBootException("\u5b9e\u4f53\u672a\u627e\u5230");
        }
        if (oConvertUtils.isEmpty((Object)string2) || oConvertUtils.isEmpty((Object)string3)) {
            throw new JeecgBootException("\u5173\u8054\u8bb0\u5f55\u5b57\u5178\u53c2\u6570\u4e0d\u6b63\u786e");
        }
        LambdaQueryWrapper lambdaQueryWrapper2 = string2.split(",");
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(string3);
        for (String string4 : lambdaQueryWrapper2) {
            arrayList.add(string4);
        }
        LambdaQueryWrapper lambdaQueryWrapper3 = (LambdaQueryWrapper)((LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformField::getCgformHeadId, (Object)onlCgformHead.getId())).in(OnlCgformField::getDbFieldName, arrayList);
        List list = this.onlCgformFieldMapper.selectList((Wrapper)lambdaQueryWrapper3);
        return list;
    }

    private Map<String, List<DictModel>> b(List<OnlCgformField> list) {
        HashMap<String, List<DictModel>> hashMap = new HashMap<String, List<DictModel>>();
        for (OnlCgformField onlCgformField : list) {
            List list2;
            String string = onlCgformField.getDictTable();
            String string2 = onlCgformField.getDictText();
            String string3 = onlCgformField.getDictField();
            if (!org.jeecg.modules.online.cgform.d.c.c(onlCgformField.getFieldShowType())) continue;
            if (oConvertUtils.isNotEmpty((Object)string) && oConvertUtils.isNotEmpty((Object)string2) && oConvertUtils.isNotEmpty((Object)string3)) {
                list2 = this.sysBaseAPI.queryTableDictItemsByCode(string, string2, string3);
                hashMap.put(onlCgformField.getDbFieldName(), list2);
                continue;
            }
            if (!oConvertUtils.isNotEmpty((Object)string3)) continue;
            list2 = this.sysBaseAPI.queryDictItemsByCode(string3);
            hashMap.put(onlCgformField.getDbFieldName(), list2);
        }
        return hashMap;
    }

    private List<OnlCgformField> a(String string, List<OnlCgformField> list, List<String> list2, List<String> list3) {
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String string2 = loginUser.getId();
        ArrayList<OnlCgformField> arrayList = new ArrayList<OnlCgformField>();
        for (OnlCgformField onlCgformField : list) {
            String string3 = onlCgformField.getDbFieldName();
            if (list2.indexOf(string3) < 0) continue;
            arrayList.add(onlCgformField);
        }
        List<String> list4 = this.onlAuthPageService.queryListHideColumn(string2, string);
        return this.a(list4, true, arrayList, list3);
    }

    private String a(OnlCgformHead onlCgformHead, Map<String, List<DictModel>> map, List<Map<String, Object>> list, String string, String string2, Object object) {
        String[] stringArray;
        Object object2;
        String string3 = object.toString();
        boolean bl = "Y".equals(onlCgformHead.getIsTree());
        if (bl && ((String)(object2 = onlCgformHead.getTreeParentIdField())).equals(string)) {
            String string4 = onlCgformHead.getTreeFieldname();
            return this.a(list, string, string4, string3);
        }
        object2 = new ArrayList();
        for (String string5 : stringArray = string3.split(",")) {
            String string6 = "";
            for (Map<String, Object> map2 : list) {
                List<DictModel> list2;
                String string7 = org.jeecg.modules.online.cgform.d.c.a(map2, string2);
                if (!string5.equals(string7) || (string6 = org.jeecg.modules.online.cgform.d.c.a(map2, string)) == null || (list2 = map.get(string)) == null || list2.size() <= 0) continue;
                for (DictModel dictModel : list2) {
                    if (!dictModel.getValue().equals(string6)) continue;
                    string6 = dictModel.getText();
                }
            }
            if (!oConvertUtils.isNotEmpty((Object)string6)) continue;
            object2.add(string6);
        }
        if (object2.size() > 0) {
            return String.join((CharSequence)",", (Iterable<? extends CharSequence>)object2);
        }
        return "";
    }

    private String a(List<Map<String, Object>> list, String string, String string2, String string3) {
        Map<String, Object> map = this.a(list, string3);
        if (map == null) {
            return string3;
        }
        String string4 = org.jeecg.modules.online.cgform.d.c.a(map, string);
        if (oConvertUtils.isEmpty((Object)string4) || c.equals(string4)) {
            return "";
        }
        Map<String, Object> map2 = this.a(list, string4);
        if (map2 == null) {
            return string3;
        }
        String string5 = org.jeecg.modules.online.cgform.d.c.a(map2, string2);
        if (string5 != null) {
            return string5;
        }
        return string3;
    }

    private Map<String, Object> a(List<Map<String, Object>> list, String string) {
        for (Map<String, Object> map : list) {
            String string2 = org.jeecg.modules.online.cgform.d.c.a(map, "id");
            if (string2 == null || !string2.equals(string)) continue;
            return map;
        }
        return null;
    }

    public <T> T a(String string, QueryWrapper<?> queryWrapper, Class<T> clazz) {
        string = SqlInjectionUtil.getSqlInjectTableName((String)string);
        if (clazz == JSONObject.class) {
            return (T)((OnlCgformFieldMapper)this.baseMapper).doSelect(string, queryWrapper);
        }
        return (T)((OnlCgformFieldMapper)this.baseMapper).doSelectList(string, queryWrapper);
    }

    public <T> T a(String string, String string2, QueryWrapper<?> queryWrapper, Class<T> clazz) {
        string2 = oConvertUtils.isNotEmpty((Object)string2) ? SqlInjectionUtil.getSqlInjectField((String)string2) : "*";
        queryWrapper.select((Object[])new String[]{string2});
        return this.a(string, queryWrapper, clazz);
    }

    private int a(String string, JSONObject jSONObject, UpdateWrapper<?> updateWrapper) {
        string = SqlInjectionUtil.getSqlInjectTableName((String)string);
        for (String string2 : jSONObject.keySet()) {
            string2 = SqlInjectionUtil.getSqlInjectField((String)string2);
            updateWrapper.set((Object)string2, jSONObject.get((Object)string2));
        }
        return ((OnlCgformFieldMapper)this.baseMapper).doUpdate(string, updateWrapper);
    }

    @Override
    public Integer queryCountBySql(String tableName, String pidField, String metaPid) {
        JSONObject jSONObject;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select((Object[])new String[]{"count(1) as COUNT"});
        if (oConvertUtils.isNotEmpty((Object)pidField)) {
            pidField = SqlInjectionUtil.getSqlInjectField((String)pidField);
            queryWrapper.eq((Object)pidField, (Object)metaPid);
        }
        if ((jSONObject = this.a(tableName, queryWrapper, JSONObject.class)) == null) {
            return 0;
        }
        if (jSONObject.containsKey((Object)"COUNT")) {
            return jSONObject.getInteger("COUNT");
        }
        return jSONObject.getInteger("count");
    }

    private Integer a(String string, QueryWrapper<?> queryWrapper) {
        string = SqlInjectionUtil.getSqlInjectTableName((String)string);
        return ((OnlCgformFieldMapper)this.baseMapper).doDelete(string, queryWrapper);
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getTableTxt": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableTxt;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformHead::getTableTxt;
            }
            case "getOrderNum": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) {
                    return OnlCgformField::getOrderNum;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) {
                    return OnlCgformField::getOrderNum;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) {
                    return OnlCgformField::getOrderNum;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) {
                    return OnlCgformField::getOrderNum;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlCgformField::getOrderNum;
            }
            case "getRelationType": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlCgformHead::getRelationType;
            }
            case "getCgformHeadId": {
                return OnlCgformField::getCgformHeadId;
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                return OnlCgformField::getCgformHeadId;
            }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
            }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
            }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
            }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
            }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
            }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
            }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind()