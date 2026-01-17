/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.hutool.core.util.ReUtil
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
 *  net.sf.jsqlparser.JSQLParserException
 *  org.apache.commons.lang.StringUtils
 *  org.jeecg.common.api.vo.Result
 *  org.jeecg.common.exception.JeecgBootException
 *  org.jeecg.common.system.api.ISysBaseAPI
 *  org.jeecg.common.system.query.QueryGenerator
 *  org.jeecg.common.system.vo.DictModel
 *  org.jeecg.common.system.vo.DynamicDataSourceModel
 *  org.jeecg.common.util.SqlInjectionUtil
 *  org.jeecg.common.util.dynamic.db.DataSourceCachePool
 *  org.jeecg.common.util.dynamic.db.DynamicDBUtil
 *  org.jeecg.common.util.oConvertUtils
 *  org.jeecg.config.JeecgBaseConfig
 *  org.jeecgframework.minidao.sqlparser.impl.vo.SelectSqlInfo
 *  org.jeecgframework.minidao.util.MiniDaoUtil
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.cache.annotation.CacheEvict
 *  org.springframework.cache.annotation.Cacheable
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package org.jeecg.modules.online.cgreport.service.a;

import cn.hutool.core.util.ReUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.sf.jsqlparser.JSQLParserException;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.DynamicDataSourceModel;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.dynamic.db.DataSourceCachePool;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.modules.online.b.a;
import org.jeecg.modules.online.b.b;
import org.jeecg.modules.online.cgform.enums.DataBaseEnum;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportHead;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportItem;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportParam;
import org.jeecg.modules.online.cgreport.mapper.OnlCgreportHeadMapper;
import org.jeecg.modules.online.cgreport.model.OnlCgreportModel;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportHeadService;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportItemService;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportParamService;
import org.jeecg.modules.online.config.c.d;
import org.jeecg.modules.online.config.model.OnlineFieldConfig;
import org.jeecgframework.minidao.sqlparser.impl.vo.SelectSqlInfo;
import org.jeecgframework.minidao.util.MiniDaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value="onlCgreportHeadServiceImpl")
public class c
extends ServiceImpl<OnlCgreportHeadMapper, OnlCgreportHead>
implements IOnlCgreportHeadService {
    private static final Logger b = LoggerFactory.getLogger(c.class);
    @Autowired
    private IOnlCgreportParamService onlCgreportParamService;
    @Autowired
    private IOnlCgreportItemService onlCgreportItemService;
    @Autowired
    private OnlCgreportHeadMapper mapper;
    @Lazy
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private JeecgBaseConfig jeecgBaseConfig;

    @Override
    public Map<String, Object> executeSelectSql(String sql, String onlCgreportHeadId, Map<String, Object> params) throws SQLException {
        CharSequence[] charSequenceArray;
        Page page;
        Object object;
        Object object2;
        String string;
        String string2 = null;
        try {
            string2 = d.getDatabaseType();
        }
        catch (org.jeecg.modules.online.config.exception.a a2) {
            a2.printStackTrace();
        }
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgreportParam::getCgrheadId, (Object)onlCgreportHeadId);
        List list = this.onlCgreportParamService.list((Wrapper)lambdaQueryWrapper);
        b b2 = new b(params, list);
        sql = b2.a(sql);
        Map<String, Object> map = b2.getSelfSqlParams();
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        Integer n = oConvertUtils.getInt((Object)params.get("pageSize"), (int)10);
        Integer n2 = oConvertUtils.getInt((Object)params.get("pageNo"), (int)1);
        Page page2 = new Page((long)n2.intValue(), (long)n.intValue());
        LambdaQueryWrapper lambdaQueryWrapper2 = new LambdaQueryWrapper();
        lambdaQueryWrapper2.eq(OnlCgreportItem::getCgrheadId, (Object)onlCgreportHeadId);
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String object32 : params.keySet().toArray(new String[0])) {
            if (!object32.startsWith("force_")) continue;
            string = object32.substring("force_".length());
            arrayList.add(string);
            params.put(string, params.get(object32));
        }
        if (arrayList.size() > 0) {
            lambdaQueryWrapper2.in(OnlCgreportItem::getFieldName, arrayList);
            object2 = this.onlCgreportItemService.list((Wrapper)lambdaQueryWrapper2);
            if (object2.size() < arrayList.size()) {
                int n3 = arrayList.stream().anyMatch("id"::equalsIgnoreCase) ? 1 : 0;
                int n4 = object2.stream().anyMatch(onlCgreportItem -> "id".equalsIgnoreCase(onlCgreportItem.getFieldName())) ? 1 : 0;
                if (n3 != 0 && n4 == 0) {
                    OnlCgreportItem onlCgreportItem2 = new OnlCgreportItem();
                    onlCgreportItem2.setFieldName("id");
                    onlCgreportItem2.setFieldType("String");
                    onlCgreportItem2.setSearchMode("single");
                    onlCgreportItem2.setIsSearch(1);
                    object2.add(onlCgreportItem2);
                }
            } else {
                object2.forEach(onlCgreportItem -> onlCgreportItem.setIsSearch(1));
            }
        } else {
            lambdaQueryWrapper2.eq(OnlCgreportItem::getIsSearch, (Object)1);
            object2 = this.onlCgreportItemService.list((Wrapper)lambdaQueryWrapper2);
        }
        sql = QueryGenerator.convertSystemVariables((String)sql);
        ArrayList<OnlineFieldConfig> arrayList2 = new ArrayList<OnlineFieldConfig>();
        Object object4 = object2.iterator();
        while (object4.hasNext()) {
            OnlCgreportItem onlCgreportItem3 = (OnlCgreportItem)object4.next();
            arrayList2.add(new OnlineFieldConfig(onlCgreportItem3));
        }
        object4 = "jeecg_rp_temp.";
        a a2 = new a((String)object4, string2);
        string = a2.a(arrayList2, params);
        Map<String, Object> map2 = a2.getSqlParams();
        if (ReUtil.contains((String)" order\\s+by ", (CharSequence)sql.toLowerCase()) && "SQLSERVER".equalsIgnoreCase(string2)) {
            throw new JeecgBootException("SqlServer\u4e0d\u652f\u6301SQL\u5185\u6392\u5e8f!");
        }
        String string3 = "select * from (" + sql + ") jeecg_rp_temp ";
        if (string.trim().length() > 0) {
            string3 = string3 + " where " + string;
        }
        if ((object = params.get("column")) != null) {
            page = String.valueOf(params.get("order"));
            charSequenceArray = String.valueOf(object).split(",");
            for (int i2 = 0; i2 < charSequenceArray.length; ++i2) {
                charSequenceArray[i2] = SqlInjectionUtil.getSqlInjectField((String)charSequenceArray[i2]);
            }
            String string4 = String.join((CharSequence)(" " + (String)page + ", jeecg_rp_temp."), charSequenceArray);
            string3 = string3 + " order by jeecg_rp_temp." + string4 + " " + (String)page;
        }
        SqlInjectionUtil.specialFilterContentForOnlineReport((String)string3);
        if (!map.isEmpty()) {
            map2.putAll(map);
        }
        if (Boolean.valueOf(String.valueOf(params.get("getAll"))).booleanValue()) {
            charSequenceArray = this.mapper.selectByCondition(string3, map2);
            page = new Page();
            page.setRecords((List)charSequenceArray);
            page.setTotal((long)charSequenceArray.size());
        } else {
            page = this.mapper.selectPageByCondition((Page<Map<String, Object>>)page2, string3, map2);
        }
        hashMap.put("total", page.getTotal());
        hashMap.put("records", org.jeecg.modules.online.cgform.d.c.d(page.getRecords()));
        return hashMap;
    }

    @Override
    public Map<String, Object> executeSelectSqlDynamic(String dbKey, String sql, Map<String, Object> params, String onlCgreportHeadId) {
        Object object2;
        String string = (String)params.get("order");
        String string2 = (String)params.get("column");
        int n = oConvertUtils.getInt((Object)params.get("pageNo"), (int)1);
        int n2 = oConvertUtils.getInt((Object)params.get("pageSize"), (int)10);
        DynamicDataSourceModel dynamicDataSourceModel = DataSourceCachePool.getCacheDynamicDataSourceModel((String)dbKey);
        if (ReUtil.contains((String)" order\\s+by ", (CharSequence)sql.toLowerCase()) && "3".equalsIgnoreCase(dynamicDataSourceModel.getDbType())) {
            throw new JeecgBootException("SqlServer\u4e0d\u652f\u6301SQL\u5185\u6392\u5e8f!");
        }
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgreportParam::getCgrheadId, (Object)onlCgreportHeadId);
        List list = this.onlCgreportParamService.list((Wrapper)lambdaQueryWrapper);
        b b2 = new b(params, list);
        sql = b2.b(sql);
        Map<String, Object> map = b2.getSelfSqlParams();
        LambdaQueryWrapper lambdaQueryWrapper2 = new LambdaQueryWrapper();
        lambdaQueryWrapper2.eq(OnlCgreportItem::getCgrheadId, (Object)onlCgreportHeadId);
        lambdaQueryWrapper2.eq(OnlCgreportItem::getIsSearch, (Object)1);
        List list2 = this.onlCgreportItemService.list((Wrapper)lambdaQueryWrapper2);
        sql = QueryGenerator.convertSystemVariables((String)sql);
        ArrayList<OnlineFieldConfig> arrayList = new ArrayList<OnlineFieldConfig>();
        for (Object object2 : list2) {
            arrayList.add(new OnlineFieldConfig((OnlCgreportItem)object2));
        }
        String string3 = "jeecg_rp_temp.";
        object2 = DataBaseEnum.getDataBaseNameByValue(dynamicDataSourceModel.getDbType());
        a a2 = new a(string3, (String)object2);
        a2.setDaoType("jdbcTemplate");
        String string4 = a2.a(arrayList, params);
        Map<String, Object> map2 = a2.getSqlParams();
        String string5 = "select * from (" + sql + ") jeecg_rp_temp ";
        if (string4.trim().length() > 0) {
            string5 = string5 + " where " + string4;
        }
        String string6 = org.jeecg.modules.online.cgreport.c.a.c(string5);
        Object object3 = params.get("column");
        if (object3 != null) {
            string5 = string5 + " order by jeecg_rp_temp." + object3.toString() + " " + params.get("order").toString();
        }
        if (!map.isEmpty()) {
            map2.putAll(map);
        }
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        Map map3 = DynamicDBUtil.queryCount((String)dbKey, (String)string6, map2);
        hashMap.put("total", map3.get("total"));
        List<Map<String, Object>> list3 = org.jeecg.modules.online.cgreport.c.a.a(String.valueOf(params.get("getAll")), dbKey, string5, n, n2, map2);
        hashMap.put("records", org.jeecg.modules.online.cgform.d.c.d(list3));
        return hashMap;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    @CacheEvict(value={"sys:cache:online:rp"}, allEntries=true, beforeInvocation=true)
    public Result<?> editAll(OnlCgreportModel values) {
        OnlCgreportHead onlCgreportHead = values.getHead();
        OnlCgreportHead onlCgreportHead2 = (OnlCgreportHead)super.getById((Serializable)((Object)onlCgreportHead.getId()));
        if (onlCgreportHead2 == null) {
            return Result.error((String)"\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
        }
        super.updateById((Object)onlCgreportHead);
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgreportItem::getCgrheadId, (Object)onlCgreportHead.getId());
        this.onlCgreportItemService.remove((Wrapper)lambdaQueryWrapper);
        LambdaQueryWrapper lambdaQueryWrapper2 = new LambdaQueryWrapper();
        lambdaQueryWrapper2.eq(OnlCgreportParam::getCgrheadId, (Object)onlCgreportHead.getId());
        this.onlCgreportParamService.remove((Wrapper)lambdaQueryWrapper2);
        for (OnlCgreportParam serializable : values.getParams()) {
            serializable.setCgrheadId(onlCgreportHead.getId());
        }
        for (OnlCgreportItem onlCgreportItem : values.getItems()) {
            onlCgreportItem.setFieldName(onlCgreportItem.getFieldName().trim().toLowerCase());
            onlCgreportItem.setCgrheadId(onlCgreportHead.getId());
        }
        this.onlCgreportItemService.saveBatch(values.getItems());
        this.onlCgreportParamService.saveBatch(values.getParams());
        return Result.ok((String)"\u5168\u90e8\u4fee\u6539\u6210\u529f");
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public Result<?> delete(String id) {
        boolean bl = super.removeById((Serializable)((Object)id));
        if (bl) {
            LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(OnlCgreportItem::getCgrheadId, (Object)id);
            this.onlCgreportItemService.remove((Wrapper)lambdaQueryWrapper);
            LambdaQueryWrapper lambdaQueryWrapper2 = new LambdaQueryWrapper();
            lambdaQueryWrapper2.eq(OnlCgreportParam::getCgrheadId, (Object)id);
            this.onlCgreportParamService.remove((Wrapper)lambdaQueryWrapper2);
        }
        return Result.ok((String)"\u5220\u9664\u6210\u529f");
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public Result<?> bathDelete(String[] ids) {
        for (String string : ids) {
            boolean bl = super.removeById((Serializable)((Object)string));
            if (!bl) continue;
            LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(OnlCgreportItem::getCgrheadId, (Object)string);
            this.onlCgreportItemService.remove((Wrapper)lambdaQueryWrapper);
            LambdaQueryWrapper lambdaQueryWrapper2 = new LambdaQueryWrapper();
            lambdaQueryWrapper2.eq(OnlCgreportParam::getCgrheadId, (Object)string);
            this.onlCgreportParamService.remove((Wrapper)lambdaQueryWrapper2);
        }
        return Result.ok((String)"\u5220\u9664\u6210\u529f");
    }

    @Override
    public List<String> getSqlFields(String sql, String dbKey) throws SQLException, org.jeecg.modules.online.config.exception.a, JSQLParserException {
        List<String> list = null;
        list = StringUtils.isNotBlank((String)dbKey) ? this.a(sql, dbKey) : this.a(sql, null);
        return list;
    }

    @Override
    public List<String> getSqlParams(String sql) {
        if (oConvertUtils.isEmpty((Object)sql)) {
            return null;
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        String string = "\\$\\{\\w+\\}";
        Pattern pattern = Pattern.compile(string);
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            String string2 = matcher.group();
            arrayList.add(string2.substring(string2.indexOf("{") + 1, string2.indexOf("}")));
        }
        return arrayList;
    }

    private List<String> a(String string, String string2) throws SQLException, org.jeecg.modules.online.config.exception.a, JSQLParserException {
        if (oConvertUtils.isEmpty((Object)string)) {
            return null;
        }
        string = string.replace("[^><]=", " = ");
        if ((string = string.trim()).endsWith(";")) {
            string = string.substring(0, string.length() - 1);
        }
        string = QueryGenerator.convertSystemVariables((String)string);
        string = org.jeecg.modules.online.cgreport.c.a.a(string);
        SelectSqlInfo selectSqlInfo = MiniDaoUtil.parseSelectSqlInfo((String)string);
        assert (selectSqlInfo != null);
        if (this.jeecgBaseConfig.getFirewall() != null && this.jeecgBaseConfig.getFirewall().getDisableSelectAll().booleanValue() && selectSqlInfo.isSelectAll()) {
            throw new JeecgBootException("\u4e0d\u5141\u8bb8\u4f7f\u7528 *");
        }
        Set<String> set = null;
        if (StringUtils.isNotBlank((String)string2)) {
            DynamicDataSourceModel dynamicDataSourceModel = DataSourceCachePool.getCacheDynamicDataSourceModel((String)string2);
            if (ReUtil.contains((String)" order\\s+by ", (CharSequence)string.toLowerCase()) && "3".equalsIgnoreCase(dynamicDataSourceModel.getDbType())) {
                throw new JeecgBootException("SqlServer\u4e0d\u652f\u6301SQL\u5185\u6392\u5e8f!");
            }
            Map map = org.jeecg.modules.online.cgreport.c.a.a(string2, string);
            if (map == null) {
                if (!string.contains("*")) {
                    try {
                        map = MiniDaoUtil.parsSqlField((String)string);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                if (map == null) {
                    throw new JeecgBootException("\u8be5\u62a5\u8868sql\u6ca1\u6709\u6570\u636e");
                }
            }
            set = map.keySet();
        } else {
            String string3 = d.getDatabaseType();
            if (ReUtil.contains((String)" order\\s+by ", (CharSequence)string.toLowerCase()) && "SQLSERVER".equalsIgnoreCase(string3)) {
                throw new JeecgBootException("SqlServer\u4e0d\u652f\u6301SQL\u5185\u6392\u5e8f!");
            }
            IPage<LinkedHashMap<String, Object>> iPage = this.mapper.executeParseSql((Page<Map<String, Object>>)new Page(1L, 1L), string);
            List list = iPage.getRecords();
            if (list.isEmpty()) {
                if (!string.contains("*")) {
                    try {
                        set = MiniDaoUtil.parsSqlField((String)string).keySet();
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                if (set == null) {
                    throw new JeecgBootException("\u8be5\u62a5\u8868sql\u6ca1\u6709\u6570\u636e");
                }
            } else {
                set = ((LinkedHashMap)list.get(0)).keySet();
            }
        }
        if (set != null) {
            set.remove("ROW_ID");
        }
        return new ArrayList<String>(set);
    }

    @Override
    public Map<String, Object> queryCgReportConfig(String reportId) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        Map<String, Object> map = this.mapper.queryCgReportMainConfig(reportId);
        List<Map<String, Object>> list = this.mapper.queryCgReportItems(reportId);
        List<OnlCgreportParam> list2 = this.mapper.queryCgReportParams(reportId);
        if (d.a()) {
            hashMap.put("main", org.jeecg.modules.online.cgform.d.c.a(map));
            hashMap.put("items", org.jeecg.modules.online.cgform.d.c.d(list));
        } else {
            hashMap.put("main", map);
            hashMap.put("items", list);
        }
        hashMap.put("params", list2);
        return hashMap;
    }

    @Override
    @Deprecated
    public List<DictModel> queryDictSelectData(String sql, String keyword, int pageNo, int pageSize) {
        Object object;
        this.sysBaseAPI.dictTableWhiteListCheckBySql(sql);
        List<Object> list = new ArrayList<DictModel>();
        Page page = new Page();
        page.setSearchCount(false);
        page.setCurrent((long)pageNo);
        page.setSize((long)pageSize);
        sql = sql.trim();
        int n = sql.lastIndexOf(";");
        if (n == sql.length() - 1) {
            sql = sql.substring(0, n);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        if (keyword != null && !"".equals(keyword)) {
            object = "%" + keyword + "%";
            ((QueryWrapper)((QueryWrapper)queryWrapper.like((Object)"temp.value", object)).or()).like((Object)"temp.text", object);
        }
        object = ((OnlCgreportHeadMapper)this.baseMapper).selectPageBySql((Page<Map<String, Object>>)page, sql, (Wrapper<?>)queryWrapper);
        List list2 = object.getRecords();
        if ((list2 = list2.stream().filter(map -> map != null).collect(Collectors.toList())) != null && list2.size() != 0) {
            String string = JSON.toJSONString(list2);
            list = JSON.parseArray((String)string, DictModel.class);
        }
        return list;
    }

    @Override
    @Cacheable(value={"sys:cache:online:rp"}, key="'column-v2-'+#code+'-'+#queryDict")
    public Map<String, Object> queryColumnInfo(String code, boolean queryDict) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        QueryWrapper queryWrapper = new QueryWrapper();
        ((QueryWrapper)((QueryWrapper)queryWrapper.eq((Object)"cgrhead_id", (Object)code)).eq((Object)"is_show", (Object)1)).orderByAsc((Object)"order_num");
        List list = this.onlCgreportItemService.list((Wrapper)queryWrapper);
        JSONArray jSONArray = new JSONArray();
        JSONArray jSONArray2 = new JSONArray();
        HashMap<String, JSONObject> hashMap2 = new HashMap<String, JSONObject>(5);
        boolean bl = false;
        for (OnlCgreportItem onlCgreportItem : list) {
            JSONObject jSONObject;
            String string;
            Object object;
            String string2;
            JSONObject jSONObject2 = new JSONObject(4);
            jSONObject2.put("id", (Object)onlCgreportItem.getId());
            jSONObject2.put("title", (Object)onlCgreportItem.getFieldTxt());
            jSONObject2.put("dataIndex", (Object)onlCgreportItem.getFieldName());
            jSONObject2.put("fieldType", (Object)onlCgreportItem.getFieldType());
            jSONObject2.put("align", (Object)"center");
            jSONObject2.put("sorter", (Object)"true");
            jSONObject2.put("isTotal", (Object)onlCgreportItem.getIsTotal());
            jSONObject2.put("groupTitle", (Object)onlCgreportItem.getGroupTitle());
            if (oConvertUtils.isNotEmpty((Object)onlCgreportItem.getGroupTitle())) {
                bl = true;
            }
            if ("Integer".equals(string2 = onlCgreportItem.getFieldType()) || "Date".equals(string2) || "Long".equals(string2)) {
                jSONObject2.put("sorter", (Object)"true");
            }
            if (StringUtils.isNotBlank((String)onlCgreportItem.getFieldHref())) {
                object = "fieldHref_" + onlCgreportItem.getFieldName();
                string = new JSONObject();
                string.put("customRender", object);
                jSONObject2.put("scopedSlots", (Object)string);
                jSONObject = new JSONObject();
                jSONObject.put("slotName", object);
                jSONObject.put("href", (Object)onlCgreportItem.getFieldHref());
                jSONArray.add((Object)jSONObject);
            }
            if (oConvertUtils.isNotEmpty((Object)(object = onlCgreportItem.getFieldWidth()))) {
                jSONObject2.put("fieldWidth", object);
            }
            if ((string = onlCgreportItem.getDictCode()) != null && !"".equals(string)) {
                if (queryDict) {
                    jSONObject = this.queryColumnDict(onlCgreportItem.getDictCode(), null, null);
                    hashMap2.put(onlCgreportItem.getFieldName(), jSONObject);
                    jSONObject2.put("customRender", (Object)onlCgreportItem.getFieldName());
                } else {
                    jSONObject2.put("dictCode", (Object)string);
                }
            }
            jSONArray2.add((Object)jSONObject2);
        }
        if (queryDict) {
            hashMap.put("dictOptions", hashMap2);
        }
        hashMap.put("columns", jSONArray2);
        hashMap.put("fieldHrefSlots", jSONArray);
        hashMap.put("isGroupTitle", bl);
        return hashMap;
    }

    @Override
    public List<DictModel> queryColumnDict(String dictCode, JSONArray records, String fieldName) {
        List list = null;
        if (oConvertUtils.isNotEmpty((Object)dictCode)) {
            if (dictCode.trim().toLowerCase().indexOf("select ") == 0 && (fieldName == null || records.size() > 0)) {
                String string;
                Collection<Object> collection;
                int n = (dictCode = dictCode.trim()).lastIndexOf(";");
                if (n == dictCode.length() - 1) {
                    dictCode = dictCode.substring(0, n);
                }
                this.sysBaseAPI.dictTableWhiteListCheckBySql(dictCode);
                String string2 = "SELECT * FROM (" + dictCode + ") temp ";
                if (records != null) {
                    collection = new HashSet();
                    for (int i2 = 0; i2 < records.size(); ++i2) {
                        JSONObject jSONObject = records.getJSONObject(i2);
                        String string3 = jSONObject.getString(fieldName);
                        if (!StringUtils.isNotBlank((String)string3)) continue;
                        ((HashSet)collection).add(string3);
                    }
                    string = "'" + StringUtils.join(collection, (String)"','") + "'";
                    string2 = string2 + "WHERE temp.value IN (" + string + ")";
                }
                if ((collection = ((OnlCgreportHeadMapper)this.baseMapper).executeSqlDict(string2)) != null && collection.size() != 0) {
                    string = JSON.toJSONString(collection);
                    list = JSON.parseArray((String)string, DictModel.class);
                }
            } else {
                list = this.sysBaseAPI.queryDictItemsByCode(dictCode);
            }
        }
        return list;
    }

    @Override
    public List<DictModel> queryColumnDictList(String dictCode, List<Map<String, Object>> records, String fieldName) {
        List<DictModel> list = null;
        if (oConvertUtils.isNotEmpty((Object)dictCode)) {
            if ((dictCode = dictCode.trim()).toLowerCase().indexOf("select ") == 0 && (fieldName == null || records.size() > 0)) {
                if (dictCode.endsWith(";")) {
                    dictCode = dictCode.substring(0, dictCode.length() - 1);
                }
                this.sysBaseAPI.dictTableWhiteListCheckBySql(dictCode);
                QueryWrapper queryWrapper = new QueryWrapper();
                if (records != null && records.size() < 100) {
                    HashSet<String> hashSet = new HashSet<String>();
                    for (int i2 = 0; i2 < records.size(); ++i2) {
                        String string;
                        Map<String, Object> map = records.get(i2);
                        if (map == null || (string = org.jeecg.modules.online.cgform.d.c.a(map, fieldName)) == null) continue;
                        hashSet.add(string.toString());
                    }
                    if (hashSet.size() > 0) {
                        queryWrapper.in((Object)"temp.value", hashSet);
                    }
                }
                list = ((OnlCgreportHeadMapper)this.getBaseMapper()).queryDictListBySql(dictCode, (Wrapper<?>)queryWrapper);
            } else {
                list = this.sysBaseAPI.queryDictItemsByCode(dictCode);
            }
        }
        return list;
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getIsSearch": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportItem") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) {
                    return OnlCgreportItem::getIsSearch;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportItem") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlCgreportItem::getIsSearch;
            }
            case "getCgrheadId": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportParam") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgreportParam::getCgrheadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportItem") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgreportItem::getCgrheadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportParam") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgreportParam::getCgrheadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportItem") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgreportItem::getCgrheadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportItem") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgreportItem::getCgrheadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportParam") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgreportParam::getCgrheadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportItem") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgreportItem::getCgrheadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportParam") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgreportParam::getCgrheadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportItem") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgreportItem::getCgrheadId;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportParam") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgreportParam::getCgrheadId;
            }
            case "getFieldName": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportItem") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgreportItem::getFieldName;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

