/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONException
 *  com.alibaba.fastjson.JSONObject
 *  com.alibaba.fastjson.serializer.SerializerFeature
 *  com.baomidou.dynamic.datasource.creator.DataSourceProperty
 *  com.baomidou.mybatisplus.annotation.DbType
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper
 *  com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
 *  freemarker.template.TemplateException
 *  org.apache.commons.lang.StringUtils
 *  org.hibernate.HibernateException
 *  org.jeecg.common.api.vo.Result
 *  org.jeecg.common.constant.CommonConstant
 *  org.jeecg.common.constant.enums.CgformEnum
 *  org.jeecg.common.exception.JeecgBootBizTipException
 *  org.jeecg.common.exception.JeecgBootException
 *  org.jeecg.common.system.api.ISysBaseAPI
 *  org.jeecg.common.util.CommonUtils
 *  org.jeecg.common.util.MyClassLoader
 *  org.jeecg.common.util.SpringContextUtils
 *  org.jeecg.common.util.SqlInjectionUtil
 *  org.jeecg.common.util.UUIDGenerator
 *  org.jeecg.common.util.oConvertUtils
 *  org.jeecgframework.codegenerate.database.DbReadTableUtil
 *  org.jeecgframework.codegenerate.generate.impl.CodeGenerateOne
 *  org.jeecgframework.codegenerate.generate.impl.CodeGenerateOneToMany
 *  org.jeecgframework.codegenerate.generate.pojo.ColumnVo
 *  org.jeecgframework.codegenerate.generate.pojo.TableVo
 *  org.jeecgframework.codegenerate.generate.pojo.onetomany.MainTableVo
 *  org.jeecgframework.codegenerate.generate.pojo.onetomany.SubTableVo
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.BeanUtils
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.CollectionUtils
 */
package org.jeecg.modules.online.cgform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.SerializedLambda;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.enums.CgformEnum;
import org.jeecg.common.exception.JeecgBootBizTipException;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.MyClassLoader;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.auth.entity.OnlAuthData;
import org.jeecg.modules.online.auth.entity.OnlAuthPage;
import org.jeecg.modules.online.auth.entity.OnlAuthRelation;
import org.jeecg.modules.online.auth.service.IOnlAuthDataService;
import org.jeecg.modules.online.auth.service.IOnlAuthPageService;
import org.jeecg.modules.online.auth.service.IOnlAuthRelationService;
import org.jeecg.modules.online.cgform.enhance.CgformEnhanceJavaImportInter;
import org.jeecg.modules.online.cgform.enhance.CgformEnhanceJavaInter;
import org.jeecg.modules.online.cgform.enhance.CgformEnhanceJavaListInter;
import org.jeecg.modules.online.cgform.entity.OnlCgformButton;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceJava;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceJs;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceSql;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.entity.OnlCgformIndex;
import org.jeecg.modules.online.cgform.enums.EnhanceDataEnum;
import org.jeecg.modules.online.cgform.mapper.OnlCgformButtonMapper;
import org.jeecg.modules.online.cgform.mapper.OnlCgformEnhanceJavaMapper;
import org.jeecg.modules.online.cgform.mapper.OnlCgformEnhanceJsMapper;
import org.jeecg.modules.online.cgform.mapper.OnlCgformEnhanceSqlMapper;
import org.jeecg.modules.online.cgform.mapper.OnlCgformHeadMapper;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.cgform.service.IOnlCgformIndexService;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.jeecg.modules.online.config.model.a;
import org.jeecg.modules.online.config.model.b;
import org.jeecg.modules.online.config.model.c;
import org.jeecg.modules.online.config.service.DbTableHandleI;
import org.jeecgframework.codegenerate.database.DbReadTableUtil;
import org.jeecgframework.codegenerate.generate.impl.CodeGenerateOne;
import org.jeecgframework.codegenerate.generate.impl.CodeGenerateOneToMany;
import org.jeecgframework.codegenerate.generate.pojo.ColumnVo;
import org.jeecgframework.codegenerate.generate.pojo.TableVo;
import org.jeecgframework.codegenerate.generate.pojo.onetomany.MainTableVo;
import org.jeecgframework.codegenerate.generate.pojo.onetomany.SubTableVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service(value="onlCgformHeadServiceImpl")
public class d
extends ServiceImpl<OnlCgformHeadMapper, OnlCgformHead>
implements IOnlCgformHeadService {
    private static final Logger a = LoggerFactory.getLogger(d.class);
    @Autowired
    private IOnlCgformFieldService fieldService;
    @Autowired
    private IOnlCgformIndexService indexService;
    @Autowired
    private OnlCgformEnhanceJsMapper onlCgformEnhanceJsMapper;
    @Autowired
    private OnlCgformButtonMapper onlCgformButtonMapper;
    @Autowired
    private OnlCgformEnhanceJavaMapper onlCgformEnhanceJavaMapper;
    @Autowired
    private OnlCgformEnhanceSqlMapper onlCgformEnhanceSqlMapper;
    @Autowired
    private IOnlCgformFieldService onlCgformFieldService;
    @Autowired
    private b dataBaseConfig;
    @Autowired
    private IOnlAuthPageService onlAuthPageService;
    @Autowired
    private IOnlAuthDataService onlAuthDataService;
    @Autowired
    private IOnlAuthRelationService onlAuthRelationService;
    @Autowired
    private org.jeecg.modules.online.cgform.enhance.impl.http.a cgformEnhanceJavaHttp;
    @Autowired
    private org.jeecg.modules.online.cgform.enhance.impl.http.b cgformEnhanceJavaListHttp;
    @Value(value="${jeecg.online.datasource:}")
    private String onlineDatasource;
    @Lazy
    @Autowired
    private ISysBaseAPI sysBaseApi;

    @Override
    @Transactional(rollbackFor={Exception.class})
    public Result<?> addAll(org.jeecg.modules.online.cgform.model.a model) {
        String string = UUID.randomUUID().toString().replace("-", "");
        OnlCgformHead onlCgformHead = model.getHead();
        List<OnlCgformField> list = model.getFields();
        List<OnlCgformIndex> list2 = model.getIndexs();
        onlCgformHead.setId(string);
        boolean bl = false;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            OnlCgformField serializable = list.get(i2);
            serializable.setId(null);
            serializable.setCgformHeadId(string);
            if (serializable.getOrderNum() == null) {
                serializable.setOrderNum(i2);
            }
            if (oConvertUtils.isNotEmpty((Object)serializable.getMainTable()) && oConvertUtils.isNotEmpty((Object)serializable.getMainField())) {
                bl = true;
            }
            this.a(serializable);
            if (serializable.getDbIsPersist() != null) continue;
            serializable.setDbIsPersist(org.jeecg.modules.online.cgform.b.b.b);
        }
        for (OnlCgformIndex onlCgformIndex : list2) {
            onlCgformIndex.setId(null);
            onlCgformIndex.setCgformHeadId(string);
            onlCgformIndex.setIsDbSynch("N");
            onlCgformIndex.setDelFlag(CommonConstant.DEL_FLAG_0);
        }
        onlCgformHead.setIsDbSynch("N");
        onlCgformHead.setQueryMode("single");
        onlCgformHead.setTableVersion(1);
        onlCgformHead.setCopyType(0);
        if (onlCgformHead.getTableType() == 3 && onlCgformHead.getTabOrderNum() == null) {
            onlCgformHead.setTabOrderNum(1);
        }
        super.save((Object)onlCgformHead);
        this.fieldService.saveBatch(list);
        this.indexService.saveBatch(list2);
        this.a(onlCgformHead, list);
        if (onlCgformHead.getTableType() == 3 && bl) {
            this.onlCgformFieldService.clearCacheOnlineConfig();
        }
        return Result.ok((String)"\u6dfb\u52a0\u6210\u529f");
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public Result<?> editAll(org.jeecg.modules.online.cgform.model.a model) {
        Object object;
        Integer n;
        OnlCgformHead onlCgformHead = model.getHead();
        OnlCgformHead onlCgformHead2 = (OnlCgformHead)super.getById((Serializable)((Object)onlCgformHead.getId()));
        if (onlCgformHead2 == null) {
            return Result.error((String)"\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
        }
        String string = onlCgformHead2.getIsDbSynch();
        if (org.jeecg.modules.online.cgform.d.c.a(onlCgformHead2, onlCgformHead)) {
            string = "N";
        }
        if ((n = onlCgformHead2.getTableVersion()) == null) {
            n = 1;
        }
        n = n + 1;
        onlCgformHead.setTableVersion(n);
        List<OnlCgformField> list = model.getFields();
        List<OnlCgformIndex> list2 = model.getIndexs();
        ArrayList<OnlCgformField> arrayList = new ArrayList<OnlCgformField>();
        ArrayList<OnlCgformField> arrayList2 = new ArrayList<OnlCgformField>();
        for (OnlCgformField object32 : list) {
            String string2 = String.valueOf(object32.getId());
            this.a(object32);
            if (string2.length() == 32) {
                arrayList2.add(object32);
            } else {
                object = "_pk";
                if (!((String)object).equals(string2)) {
                    object32.setId(null);
                    object32.setCgformHeadId(onlCgformHead.getId());
                    arrayList.add(object32);
                }
            }
            if (object32.getDbIsPersist() != null) continue;
            object32.setDbIsPersist(org.jeecg.modules.online.cgform.b.b.b);
        }
        if (arrayList.size() > 0 && this.a(arrayList)) {
            string = "N";
        }
        int n2 = 0;
        for (OnlCgformField onlCgformField : arrayList2) {
            object = (OnlCgformField)this.fieldService.getById((Serializable)((Object)onlCgformField.getId()));
            this.a(((OnlCgformField)object).getMainTable(), onlCgformHead.getTableName());
            boolean bl = org.jeecg.modules.online.cgform.d.c.a((OnlCgformField)object, onlCgformField);
            if (bl) {
                string = "N";
            }
            if ((((OnlCgformField)object).getOrderNum() == null ? 0 : ((OnlCgformField)object).getOrderNum()) > n2) {
                n2 = ((OnlCgformField)object).getOrderNum();
            }
            if ("Y".equals(onlCgformHead2.getIsDbSynch()) && !onlCgformField.getDbFieldName().equals(((OnlCgformField)object).getDbFieldName())) {
                onlCgformField.setDbFieldNameOld(((OnlCgformField)object).getDbFieldName());
            }
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.lambda().eq(OnlCgformField::getId, (Object)onlCgformField.getId());
            if (onlCgformField.getFieldValidType() == null) {
                updateWrapper.lambda().set(OnlCgformField::getFieldValidType, (Object)"");
            }
            this.fieldService.update(onlCgformField, (Wrapper)updateWrapper);
        }
        for (OnlCgformField onlCgformField : arrayList) {
            if (onlCgformField.getOrderNum() == null) {
                onlCgformField.setOrderNum(++n2);
            }
            this.fieldService.save(onlCgformField);
        }
        List<OnlCgformIndex> list3 = this.indexService.getCgformIndexsByCgformId(onlCgformHead.getId());
        ArrayList<OnlCgformIndex> arrayList3 = new ArrayList<OnlCgformIndex>();
        object = new ArrayList<OnlCgformIndex>();
        for (OnlCgformIndex onlCgformIndex : list2) {
            String string3 = String.valueOf(onlCgformIndex.getId());
            if (string3.length() == 32) {
                object.add(onlCgformIndex);
                continue;
            }
            onlCgformIndex.setId(null);
            onlCgformIndex.setIsDbSynch("N");
            onlCgformIndex.setDelFlag(CommonConstant.DEL_FLAG_0);
            onlCgformIndex.setCgformHeadId(onlCgformHead.getId());
            arrayList3.add(onlCgformIndex);
        }
        for (OnlCgformIndex onlCgformIndex : list3) {
            boolean bl = list2.stream().anyMatch(onlCgformIndex2 -> onlCgformIndex.getId().equals(onlCgformIndex2.getId()));
            if (bl) continue;
            onlCgformIndex.setDelFlag(CommonConstant.DEL_FLAG_1);
            object.add(onlCgformIndex);
            string = "N";
        }
        if (arrayList3.size() > 0) {
            string = "N";
            this.indexService.saveBatch(arrayList3);
        }
        Object object2 = object.iterator();
        while (object2.hasNext()) {
            OnlCgformIndex onlCgformIndex = object2.next();
            OnlCgformIndex onlCgformIndex3 = (OnlCgformIndex)this.indexService.getById((Serializable)((Object)onlCgformIndex.getId()));
            boolean bl = org.jeecg.modules.online.cgform.d.c.a(onlCgformIndex3, onlCgformIndex);
            if (bl) {
                string = "N";
                onlCgformIndex.setIsDbSynch("N");
                if (!onlCgformIndex3.getIndexName().trim().equalsIgnoreCase(onlCgformIndex.getIndexName().trim())) {
                    onlCgformIndex.setIndexNameOld(onlCgformIndex3.getIndexName());
                }
            }
            this.indexService.updateById(onlCgformIndex);
        }
        if (model.getDeleteFieldIds().size() > 0) {
            object2 = model.getDeleteFieldIds();
            Iterator iterator = object2.iterator();
            while (iterator.hasNext()) {
                String string4 = (String)iterator.next();
                OnlCgformField onlCgformField = (OnlCgformField)this.fieldService.getById((Serializable)((Object)string4));
                if (onlCgformField == null) continue;
                if (org.jeecg.modules.online.cgform.b.b.b.equals(onlCgformField.getDbIsPersist())) {
                    string = "N";
                }
                this.a(onlCgformField.getMainTable(), onlCgformHead.getTableName());
                this.fieldService.removeById((Serializable)((Object)string4));
            }
        }
        onlCgformHead.setIsDbSynch(string);
        super.updateById((Object)onlCgformHead);
        this.a(onlCgformHead, list);
        this.b(onlCgformHead, list);
        return Result.ok((String)"\u5168\u90e8\u4fee\u6539\u6210\u529f");
    }

    private boolean a(List<OnlCgformField> list) {
        if (list == null || list.size() == 0) {
            return false;
        }
        boolean bl = false;
        for (OnlCgformField onlCgformField : list) {
            if (!org.jeecg.modules.online.cgform.b.b.b.equals(onlCgformField.getDbIsPersist())) continue;
            bl = true;
            break;
        }
        return bl;
    }

    private void a(String string, String string2) {
        LambdaQueryWrapper lambdaQueryWrapper;
        OnlCgformHead onlCgformHead;
        if (oConvertUtils.isNotEmpty((Object)string) && (onlCgformHead = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectOne((Wrapper)(lambdaQueryWrapper = (LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string)))) != null && oConvertUtils.isNotEmpty((Object)onlCgformHead.getSubTableStr())) {
            String string3 = onlCgformHead.getSubTableStr();
            String[] stringArray = string3.split(",");
            ArrayList<String> arrayList = new ArrayList<String>();
            for (String string4 : stringArray) {
                if (string4.equals(string2)) continue;
                arrayList.add(string4);
            }
            onlCgformHead.setSubTableStr(String.join((CharSequence)",", arrayList));
            ((OnlCgformHeadMapper)this.baseMapper).updateById(onlCgformHead);
        }
    }

    @Override
    public void doDbSynch(String code, String synMethod) throws HibernateException, IOException, TemplateException, SQLException, org.jeecg.modules.online.config.exception.a {
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.getById((Serializable)((Object)code));
        if (onlCgformHead == null) {
            throw new org.jeecg.modules.online.config.exception.a("\u5b9e\u4f53\u914d\u7f6e\u4e0d\u5b58\u5728");
        }
        String string = onlCgformHead.getTableName();
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)code);
        lambdaQueryWrapper.eq(OnlCgformField::getDbIsPersist, (Object)org.jeecg.modules.online.cgform.b.b.b);
        lambdaQueryWrapper.orderByAsc(OnlCgformField::getOrderNum);
        List list = this.fieldService.list((Wrapper)lambdaQueryWrapper);
        a a2 = new a();
        a2.setTableName(string);
        a2.setJformPkType(onlCgformHead.getIdType());
        a2.setJformPkSequence(onlCgformHead.getIdSequence());
        a2.setContent(onlCgformHead.getTableTxt());
        a2.setColumns(list);
        b b2 = this.getOnlineDataBaseConfig();
        a2.setDbConfig(b2);
        DbType dbType = org.jeecg.modules.online.config.c.d.c(b2);
        if ("normal".equals(synMethod) && !dbType.equals((Object)DbType.SQLITE)) {
            long l2 = System.currentTimeMillis();
            boolean bl = org.jeecg.modules.online.config.c.d.a(string, b2);
            if (bl) {
                Object object;
                org.jeecg.modules.online.config.c.c c2 = new org.jeecg.modules.online.config.c.c(b2);
                List<String> list2 = c2.b(a2);
                for (String object22 : list2) {
                    if (oConvertUtils.isEmpty((Object)object22) || oConvertUtils.isEmpty((Object)object22.trim())) continue;
                    object = object22.split(";");
                    if (object == null || ((String[])object).length > 1) {
                        // empty if block
                    }
                    for (String string2 : object) {
                        if (oConvertUtils.isEmpty((Object)string2) || oConvertUtils.isEmpty((Object)string2.trim())) continue;
                        ((OnlCgformHeadMapper)this.baseMapper).executeDDL(string2);
                    }
                }
                List list3 = this.indexService.list((Wrapper)new LambdaQueryWrapper().eq(OnlCgformIndex::getCgformHeadId, (Object)code));
                Iterator iterator = list3.iterator();
                while (iterator.hasNext()) {
                    object = (OnlCgformIndex)iterator.next();
                    if (!"N".equals(((OnlCgformIndex)object).getIsDbSynch()) && !CommonConstant.DEL_FLAG_1.equals(((OnlCgformIndex)object).getDelFlag())) continue;
                    String string3 = oConvertUtils.getString((String)((OnlCgformIndex)object).getIndexNameOld(), (String)((OnlCgformIndex)object).getIndexName());
                    String string4 = c2.b(string3, string);
                    if (this.indexService.isExistIndex(string4)) {
                        String string5 = c2.a(string3, string);
                        try {
                            ((OnlCgformHeadMapper)this.baseMapper).executeDDL(string5);
                            if (CommonConstant.DEL_FLAG_1.equals(((OnlCgformIndex)object).getDelFlag())) {
                                this.indexService.removeById((Serializable)((Object)((OnlCgformIndex)object).getId()));
                                continue;
                            }
                            ((OnlCgformIndex)object).setIndexNameOld("");
                            this.indexService.updateById(object);
                        }
                        catch (Exception exception) {
                            a.error("\u5220\u9664\u8868\u3010" + string + "\u3011\u7d22\u5f15(" + ((OnlCgformIndex)object).getIndexName() + ")\u5931\u8d25!", (Throwable)exception);
                        }
                        continue;
                    }
                    if (!CommonConstant.DEL_FLAG_1.equals(((OnlCgformIndex)object).getDelFlag())) continue;
                    this.indexService.removeById((Serializable)((Object)((OnlCgformIndex)object).getId()));
                }
            } else {
                org.jeecg.modules.online.config.c.c.a(a2);
            }
        } else if ("force".equals(synMethod) || dbType.equals((Object)DbType.SQLITE)) {
            DbTableHandleI dbTableHandleI = org.jeecg.modules.online.config.c.d.getTableHandle();
            String string6 = dbTableHandleI.dropTableSQL(string);
            ((OnlCgformHeadMapper)this.baseMapper).executeDDL(string6);
            org.jeecg.modules.online.config.c.c.a(a2);
        }
        this.indexService.createIndex(code, org.jeecg.modules.online.config.c.d.getDatabaseType(), string, synMethod);
        onlCgformHead.setIsDbSynch("Y");
        if (onlCgformHead.getTableVersion() == 1) {
            onlCgformHead.setTableVersion(2);
        }
        this.updateById(onlCgformHead);
    }

    @Override
    public void deleteRecordAndTable(String id) throws org.jeecg.modules.online.config.exception.a, SQLException {
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.getById((Serializable)((Object)id));
        if (onlCgformHead == null) {
            throw new org.jeecg.modules.online.config.exception.a("\u5b9e\u4f53\u914d\u7f6e\u4e0d\u5b58\u5728");
        }
        long l2 = System.currentTimeMillis();
        boolean bl = org.jeecg.modules.online.config.c.d.a(onlCgformHead.getTableName());
        if (bl) {
            String string = org.jeecg.modules.online.config.c.d.getTableHandle().dropTableSQL(onlCgformHead.getTableName());
            ((OnlCgformHeadMapper)this.baseMapper).executeDDL(string);
        }
        this.deleteRecord(id);
    }

    @Override
    public void deleteRecord(String id) throws org.jeecg.modules.online.config.exception.a, SQLException {
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.getById((Serializable)((Object)id));
        if (onlCgformHead == null) {
            throw new org.jeecg.modules.online.config.exception.a("\u5b9e\u4f53\u914d\u7f6e\u4e0d\u5b58\u5728");
        }
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformHead::getPhysicId, (Object)id);
        List list = ((OnlCgformHeadMapper)this.baseMapper).selectList((Wrapper)lambdaQueryWrapper);
        if (list != null && list.size() > 0) {
            for (OnlCgformHead onlCgformHead2 : list) {
                this.a(onlCgformHead2.getId());
            }
        }
        this.a(onlCgformHead);
        this.a(id);
        if (onlCgformHead.getTableType() == 3) {
            this.onlCgformFieldService.clearCacheOnlineConfig();
        }
    }

    private void a(String string) {
        ((OnlCgformHeadMapper)this.baseMapper).deleteById((Serializable)((Object)string));
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)string);
        this.fieldService.remove((Wrapper)lambdaQueryWrapper);
        LambdaQueryWrapper lambdaQueryWrapper2 = new LambdaQueryWrapper();
        lambdaQueryWrapper2.eq(OnlCgformIndex::getCgformHeadId, (Object)string);
        this.indexService.remove((Wrapper)lambdaQueryWrapper2);
        LambdaQueryWrapper lambdaQueryWrapper3 = new LambdaQueryWrapper();
        lambdaQueryWrapper3.eq(OnlAuthRelation::getCgformId, (Object)string);
        this.onlAuthRelationService.remove((Wrapper)lambdaQueryWrapper3);
        LambdaQueryWrapper lambdaQueryWrapper4 = new LambdaQueryWrapper();
        lambdaQueryWrapper4.eq(OnlAuthData::getCgformId, (Object)string);
        this.onlAuthDataService.remove((Wrapper)lambdaQueryWrapper4);
        LambdaQueryWrapper lambdaQueryWrapper5 = new LambdaQueryWrapper();
        lambdaQueryWrapper5.eq(OnlAuthPage::getCgformId, (Object)string);
        this.onlAuthPageService.remove((Wrapper)lambdaQueryWrapper5);
    }

    private void a(OnlCgformHead onlCgformHead) {
        if (onlCgformHead.getTableType() == 3) {
            Object object;
            LambdaQueryWrapper lambdaQueryWrapper = (LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformField::getCgformHeadId, (Object)onlCgformHead.getId());
            List list = this.fieldService.list((Wrapper)lambdaQueryWrapper);
            String string = null;
            Object object2 = list.iterator();
            while (object2.hasNext() && !oConvertUtils.isNotEmpty((Object)(string = ((OnlCgformField)(object = (OnlCgformField)object2.next())).getMainTable()))) {
            }
            if (oConvertUtils.isNotEmpty(string) && (object2 = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string))) != null && oConvertUtils.isNotEmpty((Object)(object = ((OnlCgformHead)object2).getSubTableStr()))) {
                List list2 = Arrays.asList(((String)object).split(",")).stream().collect(Collectors.toList());
                list2.remove(onlCgformHead.getTableName());
                ((OnlCgformHead)object2).setSubTableStr(String.join((CharSequence)",", list2));
                ((OnlCgformHeadMapper)this.baseMapper).updateById(object2);
            }
        }
    }

    @Override
    public List<Map<String, Object>> queryListData(String sql) {
        return ((OnlCgformHeadMapper)this.baseMapper).queryList(sql);
    }

    @Override
    public void saveEnhance(OnlCgformEnhanceJs onlCgformEnhanceJs) {
        this.onlCgformEnhanceJsMapper.insert(onlCgformEnhanceJs);
    }

    @Override
    public OnlCgformEnhanceJs queryEnhance(String code, String type) {
        return (OnlCgformEnhanceJs)this.onlCgformEnhanceJsMapper.selectOne((Wrapper)((LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformEnhanceJs::getCgJsType, (Object)type)).eq(OnlCgformEnhanceJs::getCgformHeadId, (Object)code));
    }

    @Override
    public void editEnhance(OnlCgformEnhanceJs onlCgformEnhanceJs) {
        this.onlCgformEnhanceJsMapper.updateById(onlCgformEnhanceJs);
    }

    @Override
    public OnlCgformEnhanceSql queryEnhanceSql(String formId, String buttonCode) {
        return (OnlCgformEnhanceSql)this.onlCgformEnhanceSqlMapper.selectOne((Wrapper)((LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformEnhanceSql::getCgformHeadId, (Object)formId)).eq(OnlCgformEnhanceSql::getButtonCode, (Object)buttonCode));
    }

    @Override
    public OnlCgformEnhanceJava queryEnhanceJava(OnlCgformEnhanceJava onlCgformEnhanceJava) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getButtonCode, (Object)onlCgformEnhanceJava.getButtonCode());
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getCgformHeadId, (Object)onlCgformEnhanceJava.getCgformHeadId());
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getCgJavaType, (Object)onlCgformEnhanceJava.getCgJavaType());
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getEvent, (Object)onlCgformEnhanceJava.getEvent());
        return (OnlCgformEnhanceJava)this.onlCgformEnhanceJavaMapper.selectOne((Wrapper)lambdaQueryWrapper);
    }

    @Override
    public List<OnlCgformButton> queryButtonList(String code, boolean isListButton) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformButton::getButtonStatus, (Object)"1");
        lambdaQueryWrapper.eq(OnlCgformButton::getCgformHeadId, (Object)code);
        if (isListButton) {
            lambdaQueryWrapper.in(OnlCgformButton::getButtonStyle, new Object[]{"link", "button"});
        } else {
            lambdaQueryWrapper.eq(OnlCgformButton::getButtonStyle, (Object)"form");
        }
        lambdaQueryWrapper.orderByAsc(OnlCgformButton::getOrderNum);
        return this.onlCgformButtonMapper.selectList((Wrapper)lambdaQueryWrapper);
    }

    @Override
    public List<OnlCgformButton> queryButtonList(String code) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformButton::getButtonStatus, (Object)"1");
        lambdaQueryWrapper.eq(OnlCgformButton::getCgformHeadId, (Object)code);
        lambdaQueryWrapper.orderByAsc(OnlCgformButton::getOrderNum);
        return this.onlCgformButtonMapper.selectList((Wrapper)lambdaQueryWrapper);
    }

    @Override
    public List<String> queryOnlinetables() {
        return ((OnlCgformHeadMapper)this.baseMapper).queryOnlinetables();
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void saveDbTable2Online(String tbname) throws Exception {
        OnlCgformHead onlCgformHead = new OnlCgformHead();
        onlCgformHead.setTableType(1);
        onlCgformHead.setIsCheckbox("Y");
        onlCgformHead.setIsDbSynch(this.checkTableExist(tbname) ? "Y" : "N");
        onlCgformHead.setIsTree("N");
        onlCgformHead.setIsPage("Y");
        onlCgformHead.setQueryMode("group");
        onlCgformHead.setTableName(tbname.toLowerCase());
        onlCgformHead.setTableTxt(tbname);
        onlCgformHead.setTableVersion(1);
        onlCgformHead.setFormTemplate("1");
        onlCgformHead.setCopyType(0);
        onlCgformHead.setIsDesForm("N");
        onlCgformHead.setScroll(1);
        onlCgformHead.setThemeTemplate("normal");
        String string = UUIDGenerator.generate();
        onlCgformHead.setId(string);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("tableFixedAction", (Object)1);
        jSONObject.put("tableFixedActionType", (Object)"right");
        onlCgformHead.setExtConfigJson(jSONObject.toJSONString());
        ArrayList<OnlCgformField> arrayList = new ArrayList<OnlCgformField>();
        try {
            List list = DbReadTableUtil.readOriginalTableColumn((String)tbname);
            boolean bl = list.stream().anyMatch(columnVo -> columnVo.getFieldName().equalsIgnoreCase("id"));
            if (!bl) {
                throw new JeecgBootBizTipException("\u7f3a\u5c11ID\u5b57\u6bb5\uff0c\u4e0d\u80fd\u540c\u6b65");
            }
            for (int i2 = 0; i2 < list.size(); ++i2) {
                ColumnVo columnVo2 = (ColumnVo)list.get(i2);
                String string2 = columnVo2.getFieldDbName();
                OnlCgformField onlCgformField = new OnlCgformField();
                onlCgformField.setCgformHeadId(string);
                onlCgformField.setDbFieldNameOld(columnVo2.getFieldDbName().toLowerCase());
                onlCgformField.setDbFieldName(columnVo2.getFieldDbName().toLowerCase());
                if (oConvertUtils.isNotEmpty((Object)columnVo2.getFiledComment())) {
                    onlCgformField.setDbFieldTxt(columnVo2.getFiledComment());
                } else {
                    onlCgformField.setDbFieldTxt(columnVo2.getFieldName());
                }
                onlCgformField.setDbIsKey(0);
                onlCgformField.setIsShowForm(1);
                onlCgformField.setIsQuery(0);
                onlCgformField.setFieldMustInput("0");
                onlCgformField.setIsShowList(1);
                onlCgformField.setOrderNum(i2 + 1);
                onlCgformField.setQueryMode("single");
                onlCgformField.setDbLength(oConvertUtils.getInt((String)columnVo2.getPrecision()));
                onlCgformField.setFieldLength(120);
                onlCgformField.setDbPointLength(oConvertUtils.getInt((String)columnVo2.getScale()));
                onlCgformField.setFieldShowType("text");
                onlCgformField.setDbIsNull("Y".equals(columnVo2.getNullable()) ? 1 : 0);
                onlCgformField.setIsReadOnly(0);
                if ("id".equalsIgnoreCase(string2)) {
                    String[] stringArray = new String[]{"java.lang.Integer", "java.lang.Long"};
                    String string3 = columnVo2.getFieldType();
                    if (Arrays.asList(stringArray).contains(string3)) {
                        onlCgformHead.setIdType("NATIVE");
                    } else {
                        onlCgformHead.setIdType("UUID");
                    }
                    onlCgformField.setOrderNum(-1);
                    onlCgformField.setDbIsKey(1);
                    onlCgformField.setIsShowForm(0);
                    onlCgformField.setIsShowList(0);
                    onlCgformField.setIsReadOnly(1);
                }
                if ("create_by".equalsIgnoreCase(string2) || "create_time".equalsIgnoreCase(string2) || "update_by".equalsIgnoreCase(string2) || "update_time".equalsIgnoreCase(string2) || "sys_org_code".equalsIgnoreCase(string2)) {
                    onlCgformField.setIsShowForm(0);
                    onlCgformField.setIsShowList(0);
                }
                if ("java.lang.Integer".equalsIgnoreCase(columnVo2.getFieldType())) {
                    onlCgformField.setDbType("int");
                } else if ("java.lang.Long".equalsIgnoreCase(columnVo2.getFieldType())) {
                    onlCgformField.setDbType("int");
                } else if ("java.util.Date".equalsIgnoreCase(columnVo2.getFieldType())) {
                    if ("datetime".equals(columnVo2.getFieldDbType())) {
                        onlCgformField.setDbType("Datetime");
                        onlCgformField.setFieldShowType("datetime");
                    } else {
                        onlCgformField.setDbType("Date");
                        onlCgformField.setFieldShowType("date");
                    }
                } else if ("java.lang.Double".equalsIgnoreCase(columnVo2.getFieldType()) || "java.lang.Float".equalsIgnoreCase(columnVo2.getFieldType())) {
                    onlCgformField.setDbType("double");
                } else if ("java.math.BigDecimal".equalsIgnoreCase(columnVo2.getFieldType()) || "BigDecimal".equalsIgnoreCase(columnVo2.getFieldType())) {
                    onlCgformField.setDbType("BigDecimal");
                } else if ("byte[]".equalsIgnoreCase(columnVo2.getFieldType()) || columnVo2.getFieldType().contains("blob")) {
                    onlCgformField.setDbType("Blob");
                    columnVo2.setCharmaxLength(null);
                } else if ("java.lang.Object".equals(columnVo2.getFieldType()) && ("text".equalsIgnoreCase(columnVo2.getFieldDbType()) || "ntext".equalsIgnoreCase(columnVo2.getFieldDbType()))) {
                    onlCgformField.setDbType("Text");
                    onlCgformField.setFieldShowType("textarea");
                } else if ("java.lang.Object".equals(columnVo2.getFieldType()) && "image".equalsIgnoreCase(columnVo2.getFieldDbType())) {
                    onlCgformField.setDbType("Blob");
                } else {
                    onlCgformField.setDbType("string");
                }
                if (oConvertUtils.isEmpty((Object)columnVo2.getPrecision()) && oConvertUtils.isNotEmpty((Object)columnVo2.getCharmaxLength())) {
                    if (Long.valueOf(columnVo2.getCharmaxLength()) >= 3000L) {
                        onlCgformField.setDbType("Text");
                        onlCgformField.setFieldShowType("textarea");
                        try {
                            onlCgformField.setDbLength(Integer.valueOf(columnVo2.getCharmaxLength()));
                        }
                        catch (Exception exception) {
                            a.error(exception.getMessage(), (Throwable)exception);
                        }
                    } else {
                        onlCgformField.setDbLength(Integer.valueOf(columnVo2.getCharmaxLength()));
                    }
                } else {
                    if (oConvertUtils.isNotEmpty((Object)columnVo2.getPrecision())) {
                        onlCgformField.setDbLength(Integer.valueOf(columnVo2.getPrecision()));
                    } else if (onlCgformField.getDbType().equals("int")) {
                        onlCgformField.setDbLength(10);
                    }
                    if (oConvertUtils.isNotEmpty((Object)columnVo2.getScale())) {
                        onlCgformField.setDbPointLength(Integer.valueOf(columnVo2.getScale()));
                    }
                }
                if (oConvertUtils.getInt((String)columnVo2.getPrecision()) == -1 && oConvertUtils.getInt((String)columnVo2.getScale()) == 0) {
                    onlCgformField.setDbType("Text");
                }
                if ("Blob".equals(onlCgformField.getDbType()) || "Text".equals(onlCgformField.getDbType()) || "Date".equals(onlCgformField.getDbType())) {
                    onlCgformField.setDbLength(0);
                    onlCgformField.setDbPointLength(0);
                }
                onlCgformField.setDbIsPersist(org.jeecg.modules.online.cgform.b.b.b);
                arrayList.add(onlCgformField);
            }
        }
        catch (JeecgBootBizTipException jeecgBootBizTipException) {
            throw jeecgBootBizTipException;
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            throw exception;
        }
        if (oConvertUtils.isEmpty((Object)onlCgformHead.getFormCategory())) {
            onlCgformHead.setFormCategory("bdfl_include");
        }
        this.save(onlCgformHead);
        this.fieldService.saveBatch(arrayList);
    }

    @Override
    public boolean checkTableExist(String tbname) {
        tbname = SqlInjectionUtil.getSqlInjectTableName((String)tbname);
        try {
            ((OnlCgformHeadMapper)this.baseMapper).queryCountByTableName(tbname);
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    private boolean b(String string, String string2) {
        String[] stringArray;
        if (oConvertUtils.isEmpty((Object)string2)) {
            return false;
        }
        for (String string3 : stringArray = string2.split(",")) {
            if (!string3.equalsIgnoreCase(string)) continue;
            return true;
        }
        return false;
    }

    private void a(OnlCgformHead onlCgformHead, List<OnlCgformField> list) {
        block14: {
            block15: {
                if (onlCgformHead.getTableType() != 3) break block15;
                onlCgformHead = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectById((Serializable)((Object)onlCgformHead.getId()));
                for (int i2 = 0; i2 < list.size(); ++i2) {
                    OnlCgformHead onlCgformHead2;
                    OnlCgformField onlCgformField = list.get(i2);
                    String string = onlCgformField.getMainTable();
                    if (oConvertUtils.isEmpty((Object)string) || (onlCgformHead2 = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string))) == null) continue;
                    String string2 = onlCgformHead2.getSubTableStr();
                    if (oConvertUtils.isEmpty((Object)string2)) {
                        string2 = onlCgformHead.getTableName();
                    } else if (!this.b(onlCgformHead.getTableName(), string2)) {
                        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(string2.split(",")));
                        for (int i3 = 0; i3 < arrayList.size(); ++i3) {
                            String string3 = (String)arrayList.get(i3);
                            OnlCgformHead onlCgformHead3 = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string3));
                            if (onlCgformHead3 == null || onlCgformHead.getTabOrderNum() >= oConvertUtils.getInt((Object)onlCgformHead3.getTabOrderNum(), (int)0)) continue;
                            arrayList.add(i3, onlCgformHead.getTableName());
                            break;
                        }
                        if (arrayList.indexOf(onlCgformHead.getTableName()) < 0) {
                            arrayList.add(onlCgformHead.getTableName());
                        }
                        string2 = String.join((CharSequence)",", arrayList);
                    }
                    onlCgformHead2.setSubTableStr(string2);
                    ((OnlCgformHeadMapper)this.baseMapper).updateById(onlCgformHead2);
                    break block14;
                }
                break block14;
            }
            List list2 = ((OnlCgformHeadMapper)this.baseMapper).selectList((Wrapper)new LambdaQueryWrapper().like(OnlCgformHead::getSubTableStr, (Object)onlCgformHead.getTableName()));
            if (list2 == null || list2.size() <= 0) break block14;
            for (OnlCgformHead onlCgformHead4 : list2) {
                String string = onlCgformHead4.getSubTableStr();
                if (onlCgformHead4.getSubTableStr().equals(onlCgformHead.getTableName())) {
                    string = "";
                } else if (onlCgformHead4.getSubTableStr().startsWith(onlCgformHead.getTableName() + ",")) {
                    string = string.replace(onlCgformHead.getTableName() + ",", "");
                } else if (onlCgformHead4.getSubTableStr().endsWith("," + onlCgformHead.getTableName())) {
                    string = string.replace("," + onlCgformHead.getTableName(), "");
                } else if (onlCgformHead4.getSubTableStr().indexOf("," + onlCgformHead.getTableName() + ",") != -1) {
                    string = string.replace("," + onlCgformHead.getTableName() + ",", ",");
                }
                onlCgformHead4.setSubTableStr(string);
                ((OnlCgformHeadMapper)this.baseMapper).updateById(onlCgformHead4);
            }
        }
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public String saveManyFormData(String code, JSONObject json, String xAccessToken) throws org.jeecg.modules.online.config.exception.a, BusinessException {
        Integer n;
        OnlCgformHead onlCgformHead = this.getTable(code);
        String string = "add";
        this.executeEnhanceJava(string, "start", onlCgformHead, json);
        String string2 = org.jeecg.modules.online.cgform.d.c.f(onlCgformHead.getTableName());
        if (onlCgformHead.getTableType() == 2) {
            String string3 = onlCgformHead.getSubTableStr();
            if (oConvertUtils.isNotEmpty((Object)string3)) {
                String[] stringArray;
                for (String string4 : stringArray = string3.split(",")) {
                    OnlCgformHead onlCgformHead2;
                    JSONArray jSONArray = json.getJSONArray(string4);
                    if (jSONArray == null || jSONArray.size() == 0 || (onlCgformHead2 = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string4))) == null) continue;
                    List list = this.fieldService.list((Wrapper)new LambdaQueryWrapper().eq(OnlCgformField::getCgformHeadId, (Object)onlCgformHead2.getId()));
                    String string5 = "";
                    String string6 = null;
                    for (OnlCgformField onlCgformField : list) {
                        if (oConvertUtils.isEmpty((Object)onlCgformField.getMainField())) continue;
                        string5 = onlCgformField.getDbFieldName();
                        String string7 = onlCgformField.getMainField();
                        if (json.get((Object)string7.toLowerCase()) != null) {
                            string6 = json.getString(string7.toLowerCase());
                        }
                        if (json.get((Object)string7.toUpperCase()) == null) continue;
                        string6 = json.getString(string7.toUpperCase());
                    }
                    for (int i2 = 0; i2 < jSONArray.size(); ++i2) {
                        OnlCgformField onlCgformField;
                        onlCgformField = jSONArray.getJSONObject(i2);
                        if (string6 != null) {
                            onlCgformField.put(string5, string6);
                        }
                        this.fieldService.saveFormData(list, string4, (JSONObject)onlCgformField);
                    }
                }
            }
        } else if (org.jeecg.modules.online.cgform.enums.a.c.equals(onlCgformHead.getTableType()) && onlCgformHead.getRelationType() == 1 && null != (n = this.fieldService.queryCountBySql(string2, null, null)) && n > 1) {
            throw new JeecgBootException("\u4e00\u5bf9\u4e00\u7684\u8868\u53ea\u80fd\u65b0\u589e\u4e00\u6761\u6570\u636e");
        }
        if ("Y".equals(onlCgformHead.getIsTree())) {
            this.fieldService.saveTreeFormData(onlCgformHead.getId(), string2, json, onlCgformHead.getTreeIdField(), onlCgformHead.getTreeParentIdField());
        } else {
            this.fieldService.saveFormData(onlCgformHead.getId(), string2, json, false);
        }
        this.executeEnhanceSql(string, onlCgformHead.getId(), json);
        this.executeEnhanceJava(string, "end", onlCgformHead, json);
        return onlCgformHead.getTableName();
    }

    @Override
    public Map<String, Object> querySubFormData(String table, String mainId) throws org.jeecg.modules.online.config.exception.a {
        HashMap<String, Object> hashMap = new HashMap(5);
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.getOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)table));
        if (onlCgformHead == null) {
            throw new org.jeecg.modules.online.config.exception.a("\u6570\u636e\u5e93\u5b50\u8868[" + table + "]\u4e0d\u5b58\u5728");
        }
        List<OnlCgformField> list = this.fieldService.queryFormFields(onlCgformHead.getId(), false);
        String string = null;
        Object object = list.iterator();
        while (object.hasNext()) {
            OnlCgformField onlCgformField = object.next();
            if (!oConvertUtils.isNotEmpty((Object)onlCgformField.getMainField())) continue;
            string = onlCgformField.getDbFieldName();
            break;
        }
        if ((object = this.fieldService.querySubFormData(list, table, string, mainId)) != null && object.size() == 0) {
            throw new org.jeecg.modules.online.config.exception.a("\u6570\u636e\u5e93\u5b50\u8868[" + table + "]\u672a\u627e\u5230\u76f8\u5173\u4fe1\u606f, \u4e3b\u8868ID\u4e3a" + mainId);
        }
        if (object.size() > 1) {
            throw new org.jeecg.modules.online.config.exception.a("\u6570\u636e\u5e93\u5b50\u8868[" + table + "]\u5b58\u5728\u591a\u6761\u8bb0\u5f55, \u4e3b\u8868ID\u4e3a" + mainId);
        }
        hashMap = (Map)object.get(0);
        return hashMap;
    }

    @Override
    public List<Map<String, Object>> queryManySubFormData(String table, String mainId) throws org.jeecg.modules.online.config.exception.a {
        OnlCgformField onlCgformField2;
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.getOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)table));
        if (onlCgformHead == null) {
            throw new org.jeecg.modules.online.config.exception.a("\u6570\u636e\u5e93\u5b50\u8868[" + table + "]\u4e0d\u5b58\u5728");
        }
        List<OnlCgformField> list = this.fieldService.queryFormFields(onlCgformHead.getId(), false);
        if (list == null || list.size() == 0) {
            throw new org.jeecg.modules.online.config.exception.a("\u627e\u4e0d\u5230\u5b50\u8868\u5b57\u6bb5\uff0c\u8bf7\u786e\u8ba4\u914d\u7f6e\u662f\u5426\u6b63\u786e!");
        }
        String string = null;
        String string2 = null;
        String string3 = null;
        for (OnlCgformField onlCgformField2 : list) {
            if (!oConvertUtils.isNotEmpty((Object)onlCgformField2.getMainField())) continue;
            string = onlCgformField2.getDbFieldName();
            string2 = onlCgformField2.getMainTable();
            string3 = onlCgformField2.getMainField();
            break;
        }
        ArrayList arrayList = new ArrayList();
        onlCgformField2 = new OnlCgformField();
        onlCgformField2.setDbFieldName(string3);
        arrayList.add(onlCgformField2);
        Map<String, Object> map = this.fieldService.queryFormData(arrayList, string2, mainId);
        String string4 = oConvertUtils.getString((String)oConvertUtils.getString((Object)map.get(string3)), (String)oConvertUtils.getString((Object)map.get(string3.toUpperCase())));
        List<Map<String, Object>> list2 = this.fieldService.querySubFormData(list, table, string, string4);
        if (list2 != null && list2.size() == 0) {
            return Arrays.asList(new Map[0]);
        }
        ArrayList<Map<String, Object>> arrayList2 = new ArrayList<Map<String, Object>>(list2.size());
        for (Map<String, Object> map2 : list2) {
            arrayList2.add(org.jeecg.modules.online.cgform.d.c.a(map2));
        }
        return arrayList2;
    }

    @Override
    public Map<String, Object> queryManyFormData(String code, String id) throws org.jeecg.modules.online.config.exception.a {
        String string;
        OnlCgformHead onlCgformHead = this.getTable(code);
        List<OnlCgformField> list = this.fieldService.queryFormFields(onlCgformHead.getId(), true);
        if (list == null || list.size() == 0) {
            throw new org.jeecg.modules.online.config.exception.a("\u627e\u4e0d\u5230\u5b57\u6bb5\uff0c\u8bf7\u786e\u8ba4\u914d\u7f6e\u662f\u5426\u6b63\u786e!");
        }
        String string2 = org.jeecg.modules.online.cgform.d.c.f(onlCgformHead.getTableName());
        Map<String, Object> map = this.fieldService.queryFormData(list, string2, id);
        if (onlCgformHead.getTableType() == 2 && oConvertUtils.isNotEmpty((Object)(string = onlCgformHead.getSubTableStr()))) {
            String[] stringArray;
            for (String string3 : stringArray = string.split(",")) {
                OnlCgformHead onlCgformHead2 = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string3));
                if (onlCgformHead2 == null) continue;
                List<OnlCgformField> list2 = this.fieldService.queryFormFields(onlCgformHead2.getId(), false);
                String string4 = "";
                String string5 = null;
                for (OnlCgformField onlCgformField : list2) {
                    if (oConvertUtils.isEmpty((Object)onlCgformField.getMainField())) continue;
                    string4 = onlCgformField.getDbFieldName();
                    String string6 = onlCgformField.getMainField();
                    string5 = org.jeecg.modules.online.cgform.d.c.a(map, string6);
                }
                List<Map<String, Object>> list3 = this.fieldService.querySubFormData(list2, string3, string4, string5);
                if (list3 == null || list3.size() == 0) {
                    map.put(string3, new String[0]);
                    continue;
                }
                map.put(string3, org.jeecg.modules.online.cgform.d.c.d(list3));
            }
        }
        return map;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public String editManyFormData(String code, JSONObject json) throws org.jeecg.modules.online.config.exception.a, BusinessException {
        String string;
        OnlCgformHead onlCgformHead = this.getTable(code);
        String string2 = "edit";
        this.executeEnhanceJava(string2, "start", onlCgformHead, json);
        Map<String, Object> map = this.queryManyFormData(code, json.getString("id"));
        if (oConvertUtils.isEmpty(map)) {
            a.error("\u5f85\u7f16\u8f91\u6570\u636e\u4e0d\u5b58\u5728" + json.getString("id"));
            throw new JeecgBootBizTipException("\u5f85\u7f16\u8f91\u6570\u636e\u4e0d\u5b58\u5728");
        }
        String string3 = onlCgformHead.getTableName();
        if ("Y".equals(onlCgformHead.getIsTree())) {
            this.fieldService.editTreeFormData(onlCgformHead.getId(), string3, json, onlCgformHead.getTreeIdField(), onlCgformHead.getTreeParentIdField());
        } else {
            this.fieldService.editFormData(onlCgformHead.getId(), string3, json, false);
        }
        if (onlCgformHead.getTableType() == 2 && !"erp".equals(onlCgformHead.getThemeTemplate()) && oConvertUtils.isNotEmpty((Object)(string = onlCgformHead.getSubTableStr()))) {
            String[] stringArray;
            for (String string4 : stringArray = string.split(",")) {
                String string5;
                OnlCgformHead onlCgformHead2 = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string4));
                if (onlCgformHead2 == null) continue;
                List list = this.fieldService.list((Wrapper)new LambdaQueryWrapper().eq(OnlCgformField::getCgformHeadId, (Object)onlCgformHead2.getId()));
                String string6 = "";
                String string7 = null;
                String string8 = null;
                for (OnlCgformField onlCgformField : list) {
                    if (oConvertUtils.isEmpty((Object)onlCgformField.getMainField())) continue;
                    string6 = onlCgformField.getDbFieldName();
                    string5 = onlCgformField.getMainField();
                    if (json.get((Object)string5.toLowerCase()) != null) {
                        string7 = json.getString(string5.toLowerCase());
                        string8 = oConvertUtils.getString((Object)map.get(string5.toLowerCase()), (String)string7);
                    }
                    if (json.get((Object)string5.toUpperCase()) == null) continue;
                    string7 = json.getString(string5.toUpperCase());
                    string8 = oConvertUtils.getString((Object)map.get(string5.toUpperCase()), (String)string7);
                }
                if (oConvertUtils.isEmpty(string7)) continue;
                this.fieldService.deleteAutoList(string4, string6, string8);
                JSONArray jSONArray = json.getJSONArray(string4);
                if (jSONArray == null || jSONArray.size() == 0) continue;
                for (int i2 = 0; i2 < jSONArray.size(); ++i2) {
                    string5 = jSONArray.getJSONObject(i2);
                    if (string7 != null) {
                        string5.put(string6, (Object)string7);
                    }
                    this.fieldService.saveFormData(list, string4, (JSONObject)string5);
                }
            }
        }
        this.executeEnhanceJava(string2, "end", onlCgformHead, json);
        this.executeEnhanceSql(string2, onlCgformHead.getId(), json);
        return string3;
    }

    private OnlCgformEnhanceJava a(String string, String string2, String string3) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getActiveStatus, (Object)"1");
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getButtonCode, (Object)string);
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getEvent, (Object)string2);
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getCgformHeadId, (Object)string3);
        return (OnlCgformEnhanceJava)this.onlCgformEnhanceJavaMapper.selectOne((Wrapper)lambdaQueryWrapper);
    }

    private Object b(String string, String string2, String string3) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getActiveStatus, (Object)"1");
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getButtonCode, (Object)string);
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getEvent, (Object)string2);
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getCgformHeadId, (Object)string3);
        OnlCgformEnhanceJava onlCgformEnhanceJava = (OnlCgformEnhanceJava)this.onlCgformEnhanceJavaMapper.selectOne((Wrapper)lambdaQueryWrapper);
        Object object = this.a(onlCgformEnhanceJava);
        return object;
    }

    private void a(JSONObject jSONObject, Object object, String string, OnlCgformEnhanceJava onlCgformEnhanceJava) throws BusinessException {
        if (object != null && object instanceof CgformEnhanceJavaInter) {
            CgformEnhanceJavaInter cgformEnhanceJavaInter = (CgformEnhanceJavaInter)object;
            cgformEnhanceJavaInter.execute(string, jSONObject);
        } else if (object != null && object instanceof org.jeecg.modules.online.cgform.enhance.impl.http.a) {
            ((org.jeecg.modules.online.cgform.enhance.impl.http.a)object).execute(string, jSONObject, onlCgformEnhanceJava);
        }
    }

    @Override
    public void executeEnhanceJava(String buttonCode, String eventType, OnlCgformHead head, JSONObject json) throws BusinessException {
        OnlCgformEnhanceJava onlCgformEnhanceJava = this.a(buttonCode, eventType, head.getId());
        Object object = this.a(onlCgformEnhanceJava);
        this.a(json, object, head.getTableName(), onlCgformEnhanceJava);
    }

    @Override
    public void executeEnhanceExport(OnlCgformHead head, List<Map<String, Object>> dataList) throws BusinessException {
        this.executeEnhanceList(head, "export", dataList);
    }

    @Override
    public EnhanceDataEnum executeEnhanceImport(OnlCgformHead head, JSONObject json) throws BusinessException {
        OnlCgformEnhanceJava onlCgformEnhanceJava = this.a("import", "start", head.getId());
        Object object = this.a(onlCgformEnhanceJava);
        if (object != null && object instanceof CgformEnhanceJavaImportInter) {
            CgformEnhanceJavaImportInter cgformEnhanceJavaImportInter = (CgformEnhanceJavaImportInter)object;
            return cgformEnhanceJavaImportInter.execute(head.getTableName(), json);
        }
        return EnhanceDataEnum.INSERT;
    }

    @Override
    public void executeEnhanceList(OnlCgformHead head, String buttonCode, List<Map<String, Object>> dataList) throws BusinessException {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getActiveStatus, (Object)"1");
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getButtonCode, (Object)buttonCode);
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getCgformHeadId, (Object)head.getId());
        List list = this.onlCgformEnhanceJavaMapper.selectList((Wrapper)lambdaQueryWrapper);
        if (list != null && list.size() > 0) {
            Object object = this.a((OnlCgformEnhanceJava)list.get(0));
            if (object != null && object instanceof CgformEnhanceJavaListInter) {
                CgformEnhanceJavaListInter cgformEnhanceJavaListInter = (CgformEnhanceJavaListInter)object;
                cgformEnhanceJavaListInter.execute(head.getTableName(), dataList);
            } else if (object != null && object instanceof org.jeecg.modules.online.cgform.enhance.impl.http.b) {
                ((org.jeecg.modules.online.cgform.enhance.impl.http.b)object).execute(head.getTableName(), dataList, (OnlCgformEnhanceJava)list.get(0));
            }
        }
    }

    private Object a(OnlCgformEnhanceJava onlCgformEnhanceJava) {
        if (onlCgformEnhanceJava != null) {
            String string = onlCgformEnhanceJava.getCgJavaType();
            String string2 = onlCgformEnhanceJava.getCgJavaValue();
            if (oConvertUtils.isNotEmpty((Object)string2)) {
                Object object = null;
                if ("class".equals(string)) {
                    try {
                        object = MyClassLoader.getClassByScn((String)string2).newInstance();
                    }
                    catch (InstantiationException instantiationException) {
                        a.error(instantiationException.getMessage(), (Throwable)instantiationException);
                    }
                    catch (IllegalAccessException illegalAccessException) {
                        a.error(illegalAccessException.getMessage(), (Throwable)illegalAccessException);
                    }
                } else if ("spring".equals(string)) {
                    object = SpringContextUtils.getBean((String)string2);
                } else if ("http".equals(string)) {
                    object = this.b(onlCgformEnhanceJava);
                }
                return object;
            }
        }
        return null;
    }

    private Object b(OnlCgformEnhanceJava onlCgformEnhanceJava) {
        switch (onlCgformEnhanceJava.getButtonCode()) {
            case "add": 
            case "edit": 
            case "delete": 
            case "import": {
                return this.cgformEnhanceJavaHttp;
            }
            case "export": 
            case "query": {
                return this.cgformEnhanceJavaListHttp;
            }
        }
        return this.cgformEnhanceJavaHttp;
    }

    private OnlCgformEnhanceSql c(String string, String string2) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformEnhanceSql::getButtonCode, (Object)string);
        lambdaQueryWrapper.eq(OnlCgformEnhanceSql::getCgformHeadId, (Object)string2);
        OnlCgformEnhanceSql onlCgformEnhanceSql = (OnlCgformEnhanceSql)this.onlCgformEnhanceSqlMapper.selectOne((Wrapper)lambdaQueryWrapper);
        return onlCgformEnhanceSql;
    }

    private void a(JSONObject jSONObject, OnlCgformEnhanceSql onlCgformEnhanceSql) {
        if (onlCgformEnhanceSql != null && oConvertUtils.isNotEmpty((Object)onlCgformEnhanceSql.getCgbSql())) {
            String[] stringArray;
            String string = org.jeecg.modules.online.cgform.d.c.a(onlCgformEnhanceSql.getCgbSql(), jSONObject);
            for (String string2 : stringArray = string.split(";")) {
                if (string2 == null || "".equals(string2.toLowerCase().trim())) continue;
                ((OnlCgformHeadMapper)this.baseMapper).executeDDL(string2);
            }
        }
    }

    @Override
    public void executeEnhanceSql(String buttonCode, String formId, JSONObject json) {
        OnlCgformEnhanceSql onlCgformEnhanceSql = this.c(buttonCode, formId);
        this.a(json, onlCgformEnhanceSql);
    }

    @Override
    public void executeCustomerButton(String buttonCode, String formId, String dataId) throws BusinessException {
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.getById((Serializable)((Object)formId));
        if (onlCgformHead == null) {
            throw new BusinessException("\u672a\u627e\u5230\u8868\u914d\u7f6e\u4fe1\u606f");
        }
        OnlCgformEnhanceJava onlCgformEnhanceJava = this.a(buttonCode, "start", formId);
        OnlCgformEnhanceJava onlCgformEnhanceJava2 = this.a(buttonCode, "end", formId);
        Object object = this.a(onlCgformEnhanceJava);
        Object object2 = this.a(onlCgformEnhanceJava2);
        OnlCgformEnhanceSql onlCgformEnhanceSql = this.c(buttonCode, formId);
        String string = onlCgformHead.getTableName();
        String[] stringArray = dataId.split(",");
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)formId);
        List list = this.onlCgformFieldService.list((Wrapper)lambdaQueryWrapper);
        for (String string2 : stringArray) {
            Map<String, Object> map = this.d(org.jeecg.modules.online.cgform.d.c.f(onlCgformHead.getTableName()), org.jeecg.modules.online.cgform.d.c.k(string2));
            map = this.a(list, map);
            JSONObject jSONObject = JSONObject.parseObject((String)JSON.toJSONString(map));
            this.a(jSONObject, object, string, onlCgformEnhanceJava);
            this.a(jSONObject, onlCgformEnhanceSql);
            this.a(jSONObject, object2, string, onlCgformEnhanceJava2);
        }
    }

    private Map<String, Object> d(String string, String string2) {
        string = SqlInjectionUtil.getSqlInjectTableName((String)string);
        return ((OnlCgformHeadMapper)this.baseMapper).queryOneByTableNameAndId(string, string2);
    }

    private Map<String, Object> a(List<OnlCgformField> list, Map<String, Object> map) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        for (OnlCgformField onlCgformField : list) {
            String string = onlCgformField.getDbType();
            if ("blob".equalsIgnoreCase(string) || "text".equalsIgnoreCase(string)) continue;
            String string2 = onlCgformField.getDbFieldName();
            Object object = org.jeecg.modules.online.cgform.d.c.b(map, string2);
            hashMap.put(string2, object);
        }
        return hashMap;
    }

    @Override
    public List<OnlCgformButton> queryValidButtonList(String headId) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformButton::getCgformHeadId, (Object)headId);
        lambdaQueryWrapper.eq(OnlCgformButton::getButtonStatus, (Object)"1");
        lambdaQueryWrapper.orderByAsc(OnlCgformButton::getOrderNum);
        return this.onlCgformButtonMapper.selectList((Wrapper)lambdaQueryWrapper);
    }

    @Override
    public OnlCgformEnhanceJs queryEnhanceJs(String formId, String cgJsType) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformEnhanceJs::getCgformHeadId, (Object)formId);
        lambdaQueryWrapper.eq(OnlCgformEnhanceJs::getCgJsType, (Object)cgJsType);
        return (OnlCgformEnhanceJs)this.onlCgformEnhanceJsMapper.selectOne((Wrapper)lambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void deleteOneTableInfo(String formId, String dataId) throws BusinessException {
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.getById((Serializable)((Object)formId));
        if (onlCgformHead == null) {
            throw new BusinessException("\u672a\u627e\u5230\u8868\u914d\u7f6e\u4fe1\u606f");
        }
        String string = org.jeecg.modules.online.cgform.d.c.f(onlCgformHead.getTableName());
        Map<String, Object> map = this.d(string, dataId);
        if (map == null) {
            return;
        }
        Map<String, Object> map2 = org.jeecg.modules.online.cgform.d.c.a(map);
        String string2 = "delete";
        JSONObject jSONObject = JSONObject.parseObject((String)JSON.toJSONString(map2));
        this.executeEnhanceJava(string2, "start", onlCgformHead, jSONObject);
        this.updateParentNode(onlCgformHead, dataId);
        if (onlCgformHead.getTableType() == 2) {
            this.fieldService.deleteAutoListMainAndSub(onlCgformHead, dataId);
        } else {
            string = SqlInjectionUtil.getSqlInjectTableName((String)string);
            ((OnlCgformHeadMapper)this.baseMapper).deleteOne(string, dataId);
        }
        this.executeEnhanceSql(string2, formId, jSONObject);
        this.executeEnhanceJava(string2, "end", onlCgformHead, jSONObject);
    }

    @Override
    @Deprecated
    public JSONObject queryFormItem(OnlCgformHead head, String username) {
        String string;
        Object object;
        List<OnlCgformField> list = this.fieldService.queryAvailableFields(head.getId(), head.getTableName(), head.getTaskId(), false);
        ArrayList<String> arrayList = new ArrayList<String>();
        if (oConvertUtils.isEmpty((Object)head.getTaskId())) {
            object = this.onlAuthPageService.queryFormDisabledCode(head.getId());
            if (object != null && object.size() > 0 && object.get(0) != null) {
                arrayList.addAll((Collection<String>)object);
            }
        } else {
            object = this.fieldService.queryDisabledFields(head.getTableName(), head.getTaskId());
            if (object != null && object.size() > 0 && object.get(0) != null) {
                arrayList.addAll((Collection<String>)object);
            }
        }
        object = org.jeecg.modules.online.cgform.d.c.a(list, arrayList, null);
        if (head.getTableType() == 2 && oConvertUtils.isNotEmpty((Object)(string = head.getSubTableStr()))) {
            for (String string2 : string.split(",")) {
                OnlCgformHead onlCgformHead = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string2));
                if (onlCgformHead == null) continue;
                List<OnlCgformField> list2 = this.fieldService.queryAvailableFields(onlCgformHead.getId(), onlCgformHead.getTableName(), head.getTaskId(), false);
                List<Object> list3 = new ArrayList();
                list3 = oConvertUtils.isNotEmpty((Object)head.getTaskId()) ? this.fieldService.queryDisabledFields(onlCgformHead.getTableName(), head.getTaskId()) : this.onlAuthPageService.queryFormDisabledCode(onlCgformHead.getId());
                JSONObject jSONObject = new JSONObject();
                if (1 == onlCgformHead.getRelationType()) {
                    jSONObject = org.jeecg.modules.online.cgform.d.c.a(list2, list3, null);
                } else {
                    jSONObject.put("columns", (Object)org.jeecg.modules.online.cgform.d.c.a(list2, list3));
                }
                jSONObject.put("relationType", (Object)onlCgformHead.getRelationType());
                jSONObject.put("view", (Object)"tab");
                jSONObject.put("order", (Object)onlCgformHead.getTabOrderNum());
                jSONObject.put("formTemplate", (Object)onlCgformHead.getFormTemplate());
                jSONObject.put("describe", (Object)onlCgformHead.getTableTxt());
                jSONObject.put("key", (Object)onlCgformHead.getTableName());
                object.getJSONObject("properties").put(onlCgformHead.getTableName(), (Object)jSONObject);
            }
        }
        return object;
    }

    @Override
    public List<String> generateCode(org.jeecg.modules.online.cgform.model.d model) throws Exception {
        TableVo tableVo = new TableVo();
        tableVo.setEntityName(model.getEntityName());
        tableVo.setEntityPackage(model.getEntityPackage());
        tableVo.setFtlDescription(model.getFtlDescription());
        tableVo.setTableName(model.getTableName());
        tableVo.setSearchFieldNum(Integer.valueOf(-1));
        ArrayList<ColumnVo> arrayList = new ArrayList<ColumnVo>();
        ArrayList<ColumnVo> arrayList2 = new ArrayList<ColumnVo>();
        this.a(model.getCode(), arrayList, arrayList2);
        OnlCgformHead onlCgformHead = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getId, (Object)model.getCode()));
        HashMap<String, String> hashMap = new HashMap<String, String>(5);
        hashMap.put("scroll", onlCgformHead.getScroll() == null ? "0" : onlCgformHead.getScroll().toString());
        String string = onlCgformHead.getFormTemplate();
        if (oConvertUtils.isEmpty((Object)string)) {
            tableVo.setFieldRowNum(Integer.valueOf(1));
        } else {
            tableVo.setFieldRowNum(Integer.valueOf(Integer.parseInt(string)));
        }
        if ("Y".equals(onlCgformHead.getIsTree())) {
            hashMap.put("pidField", onlCgformHead.getTreeParentIdField());
            hashMap.put("hasChildren", onlCgformHead.getTreeIdField());
            hashMap.put("textField", onlCgformHead.getTreeFieldname());
        }
        if (oConvertUtils.isNotEmpty((Object)model.getVueStyle())) {
            hashMap.put("vueStyle", model.getVueStyle());
        }
        tableVo.setExtendParams(hashMap);
        CgformEnum cgformEnum = CgformEnum.getCgformEnumByConfig((String)model.getJspMode());
        ArrayList<String> arrayList3 = new CodeGenerateOne(tableVo, arrayList, arrayList2).generateCodeFile(model.getProjectPath(), cgformEnum.getTemplatePath(), cgformEnum.getStylePath());
        if (arrayList3 == null || arrayList3.size() == 0) {
            arrayList3 = new ArrayList<String>();
            arrayList3.add(" :::::: \u751f\u6210\u5931\u8d25ERROR\u63d0\u793a :::::: ");
            arrayList3.add("1.\u4ee3\u7801\u751f\u6210\u6a21\u677f`code-template-online`\u76ee\u5f55\u662f\u5426\u5b58\u5728");
            arrayList3.add("2.\u4ee3\u7801\u751f\u6210\u6a21\u677f`code-template-online`\u76ee\u5f55\u662f\u5426\u542b\u6709\u4e2d\u6587\u6216\u7a7a\u683c");
        }
        return arrayList3;
    }

    @Override
    public List<String> generateOneToMany(org.jeecg.modules.online.cgform.model.d model) throws Exception {
        Serializable serializable;
        MainTableVo mainTableVo = new MainTableVo();
        mainTableVo.setEntityName(model.getEntityName());
        mainTableVo.setEntityPackage(model.getEntityPackage());
        mainTableVo.setFtlDescription(model.getFtlDescription());
        mainTableVo.setTableName(model.getTableName());
        OnlCgformHead onlCgformHead = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getId, (Object)model.getCode()));
        String string = onlCgformHead.getFormTemplate();
        if (oConvertUtils.isEmpty((Object)string)) {
            mainTableVo.setFieldRowNum(Integer.valueOf(1));
        } else {
            mainTableVo.setFieldRowNum(Integer.valueOf(Integer.parseInt(string)));
        }
        ArrayList<ColumnVo> arrayList = new ArrayList<ColumnVo>();
        ArrayList<ColumnVo> arrayList2 = new ArrayList<ColumnVo>();
        this.a(model.getCode(), arrayList, arrayList2);
        List<org.jeecg.modules.online.cgform.model.d> list = model.getSubList();
        ArrayList<SubTableVo> arrayList3 = new ArrayList<SubTableVo>();
        for (org.jeecg.modules.online.cgform.model.d object2 : list) {
            serializable = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)object2.getTableName()));
            if (serializable == null) continue;
            SubTableVo subTableVo = new SubTableVo();
            subTableVo.setEntityName(object2.getEntityName());
            subTableVo.setEntityPackage(model.getEntityPackage());
            subTableVo.setTableName(object2.getTableName());
            subTableVo.setFtlDescription(object2.getFtlDescription());
            Integer n = ((OnlCgformHead)serializable).getRelationType();
            subTableVo.setForeignRelationType(n == 1 ? "1" : "0");
            ArrayList<ColumnVo> arrayList4 = new ArrayList<ColumnVo>();
            ArrayList<ColumnVo> arrayList5 = new ArrayList<ColumnVo>();
            OnlCgformField onlCgformField = this.a(((OnlCgformHead)serializable).getId(), arrayList4, arrayList5);
            if (onlCgformField == null) continue;
            subTableVo.setOriginalForeignKeys(new String[]{onlCgformField.getDbFieldName()});
            subTableVo.setForeignKeys(new String[]{onlCgformField.getDbFieldName()});
            subTableVo.setForeignMainKeys(new String[]{onlCgformField.getMainField()});
            subTableVo.setColums(arrayList4);
            subTableVo.setOriginalColumns(arrayList5);
            arrayList3.add(subTableVo);
        }
        CgformEnum cgformEnum = CgformEnum.getCgformEnumByConfig((String)model.getJspMode());
        if (oConvertUtils.isNotEmpty((Object)model.getVueStyle())) {
            List<String> list2 = Arrays.asList(cgformEnum.getVueStyle());
            serializable = new HashMap(5);
            if (list2.contains(model.getVueStyle())) {
                serializable.put("vueStyle", model.getVueStyle());
            } else {
                a.warn("\u4f60\u9009\u62e9\u7684\u9875\u9762\u4ee3\u7801\u7c7b\u578b\uff1a\u3010" + model.getVueStyle() + "\u3011\u4e0d\u652f\u6301\uff0c\u91c7\u7528\u9ed8\u8ba4\u7c7b\u578b:" + list2.get(0) + "\u751f\u6210\u4ee3\u7801\uff01");
                serializable.put("vueStyle", list2.get(0));
            }
            mainTableVo.setExtendParams((Map)((Object)serializable));
        }
        if (arrayList3 == null || arrayList3.size() == 0) {
            a.error("\u4f60\u9009\u62e9\u7684\u8868\u7c7b\u578b\u662f\u3010\u4e3b\u8868\u3011\uff0c\u4f46\u662f\u6ca1\u6709\u5173\u8054\u5b50\u8868\uff0c\u5bfc\u81f4\u751f\u6210\u4ee3\u7801\u62a5\u9519\uff01");
            throw new JeecgBootException("\u4f60\u9009\u62e9\u7684\u8868\u7c7b\u578b\u662f\u3010\u4e3b\u8868\u3011\uff0c\u4f46\u662f\u6ca1\u6709\u5173\u8054\u5b50\u8868\uff0c\u751f\u6210\u4ee3\u7801\u5931\u8d25\uff01");
        }
        List list3 = new CodeGenerateOneToMany(mainTableVo, arrayList, arrayList2, arrayList3).generateCodeFile(model.getProjectPath(), cgformEnum.getTemplatePath(), cgformEnum.getStylePath());
        return list3;
    }

    private OnlCgformField a(String string, List<ColumnVo> list, List<ColumnVo> list2) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)string);
        lambdaQueryWrapper.eq(OnlCgformField::getDbIsPersist, (Object)org.jeecg.modules.online.cgform.b.b.b);
        lambdaQueryWrapper.orderByAsc(OnlCgformField::getOrderNum);
        List list3 = this.fieldService.list((Wrapper)lambdaQueryWrapper);
        OnlCgformField onlCgformField = null;
        for (OnlCgformField onlCgformField2 : list3) {
            JSONObject jSONObject;
            Object object;
            Object object2;
            JSONArray jSONArray;
            if (oConvertUtils.isNotEmpty((Object)onlCgformField2.getMainTable())) {
                onlCgformField = onlCgformField2;
            }
            ColumnVo columnVo = new ColumnVo();
            columnVo.setFieldLength(onlCgformField2.getFieldLength());
            columnVo.setFieldHref(onlCgformField2.getFieldHref());
            columnVo.setFieldValidType(onlCgformField2.getFieldValidType());
            columnVo.setFieldDefault(onlCgformField2.getDbDefaultVal());
            columnVo.setFieldShowType(onlCgformField2.getFieldShowType());
            columnVo.setFieldOrderNum(onlCgformField2.getOrderNum());
            columnVo.setIsKey(onlCgformField2.getDbIsKey() == 1 ? "Y" : "N");
            columnVo.setIsShow(onlCgformField2.getIsShowForm() == 1 ? "Y" : "N");
            columnVo.setIsShowList(onlCgformField2.getIsShowList() == 1 ? "Y" : "N");
            columnVo.setIsQuery(onlCgformField2.getIsQuery() == 1 ? "Y" : "N");
            columnVo.setQueryMode(onlCgformField2.getQueryMode());
            columnVo.setDictField(onlCgformField2.getDictField());
            columnVo.setDictTable(onlCgformField2.getDictTable());
            columnVo.setDictText(onlCgformField2.getDictText());
            columnVo.setFieldDbName(onlCgformField2.getDbFieldName());
            columnVo.setFieldName(oConvertUtils.camelName((String)onlCgformField2.getDbFieldName()));
            columnVo.setFiledComment(onlCgformField2.getDbFieldTxt());
            columnVo.setFieldDbType(onlCgformField2.getDbType());
            columnVo.setFieldType(this.b(onlCgformField2.getDbType()));
            columnVo.setClassType(onlCgformField2.getFieldShowType());
            columnVo.setClassType_row(onlCgformField2.getFieldShowType());
            if (onlCgformField2.getDbIsNull() == 0 || "*".equals(onlCgformField2.getFieldValidType()) || "1".equals(onlCgformField2.getFieldMustInput())) {
                columnVo.setNullable("N");
            } else {
                columnVo.setNullable("Y");
            }
            if ("switch".equals(onlCgformField2.getFieldShowType())) {
                if (oConvertUtils.isNotEmpty((Object)onlCgformField2.getFieldExtendJson())) {
                    block21: {
                        jSONArray = JSONArray.parseArray((String)"[\"Y\",\"N\"]");
                        try {
                            jSONArray = JSONArray.parseArray((String)onlCgformField2.getFieldExtendJson());
                        }
                        catch (JSONException jSONException) {
                            object2 = JSONArray.parseObject((String)onlCgformField2.getFieldExtendJson());
                            if (!object2.containsKey((Object)"switchOptions")) break block21;
                            jSONArray = object2.getJSONArray("switchOptions");
                        }
                    }
                    columnVo.setDictField(JSON.toJSONString((Object)jSONArray, (SerializerFeature[])new SerializerFeature[]{SerializerFeature.UseSingleQuotes}));
                } else {
                    columnVo.setDictField("is_open");
                }
            }
            jSONArray = new HashMap(5);
            if (StringUtils.isNotBlank((String)onlCgformField2.getFieldExtendJson())) {
                try {
                    object = JSONObject.parseObject((String)onlCgformField2.getFieldExtendJson());
                    if (object != null) {
                        jSONArray.putAll(object.getInnerMap());
                    }
                }
                catch (JSONException jSONException) {
                    // empty catch block
                }
            }
            columnVo.setExtendParams((Map)jSONArray);
            if (!jSONArray.isEmpty() && jSONArray.containsKey("picker") && oConvertUtils.isNotEmpty((Object)(object = (String)jSONArray.get("picker")))) {
                if (((String)object).trim().equalsIgnoreCase("default")) {
                    jSONArray.remove("picker");
                } else if ("date".equalsIgnoreCase(onlCgformField2.getFieldShowType()) && oConvertUtils.isNotEmpty((Object)onlCgformField2.getFieldDefaultValue())) {
                    object2 = new org.jeecg.modules.online.cgform.converter.b.b(onlCgformField2);
                    onlCgformField2.setFieldDefaultValue(object2.converterToVal(onlCgformField2.getFieldDefaultValue()));
                }
            }
            if ("popup".equals(onlCgformField2.getFieldShowType()) || "popup_dict".equals(onlCgformField2.getFieldShowType())) {
                boolean bl = true;
                object2 = jSONArray.get("popupMulti");
                if (object2 != null) {
                    bl = (Boolean)object2;
                }
                jSONArray.put("popupMulti", bl);
            }
            columnVo.setSort("1".equals(onlCgformField2.getSortFlag()) ? "Y" : "N");
            columnVo.setReadonly(Integer.valueOf(1).equals(onlCgformField2.getIsReadOnly()) ? "Y" : "N");
            if (oConvertUtils.isNotEmpty((Object)onlCgformField2.getFieldDefaultValue()) && !onlCgformField2.getFieldDefaultValue().trim().startsWith("${") && !onlCgformField2.getFieldDefaultValue().trim().startsWith("#{") && !onlCgformField2.getFieldDefaultValue().trim().startsWith("{{")) {
                columnVo.setDefaultVal(onlCgformField2.getFieldDefaultValue());
            }
            if (("file".equals(onlCgformField2.getFieldShowType()) || "image".equals(onlCgformField2.getFieldShowType())) && oConvertUtils.isNotEmpty((Object)onlCgformField2.getFieldExtendJson()) && oConvertUtils.isNotEmpty((Object)(jSONObject = JSONObject.parseObject((String)onlCgformField2.getFieldExtendJson())).getString("uploadnum"))) {
                columnVo.setUploadnum(jSONObject.getString("uploadnum"));
            }
            list2.add(columnVo);
            if (onlCgformField2.getIsShowForm() != 1 && onlCgformField2.getIsShowList() != 1 && onlCgformField2.getIsQuery() != 1) continue;
            list.add(columnVo);
        }
        return onlCgformField;
    }

    private String b(String string) {
        if ((string = string.toLowerCase()).indexOf("int") >= 0) {
            return "java.lang.Integer";
        }
        if (string.indexOf("double") >= 0) {
            return "java.lang.Double";
        }
        if (string.indexOf("decimal") >= 0) {
            return "java.math.BigDecimal";
        }
        if (string.indexOf("date") >= 0) {
            return "java.util.Date";
        }
        return "java.lang.String";
    }

    @Override
    public void addCrazyFormData(String tbname, JSONObject json) throws org.jeecg.modules.online.config.exception.a, UnsupportedEncodingException {
        String string;
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.getOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)tbname));
        if (onlCgformHead == null) {
            throw new org.jeecg.modules.online.config.exception.a("\u6570\u636e\u5e93\u4e3b\u8868[" + tbname + "]\u4e0d\u5b58\u5728");
        }
        if (onlCgformHead.getTableType() == 2 && (string = onlCgformHead.getSubTableStr()) != null) {
            String[] stringArray;
            for (String string2 : stringArray = string.split(",")) {
                OnlCgformHead onlCgformHead2;
                JSONArray jSONArray = this.a(string2, json);
                if (CollectionUtils.isEmpty((Collection)jSONArray) || (onlCgformHead2 = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string2))) == null) continue;
                List list = this.fieldService.list((Wrapper)new LambdaQueryWrapper().eq(OnlCgformField::getCgformHeadId, (Object)onlCgformHead2.getId()));
                String string3 = "";
                String string4 = null;
                for (OnlCgformField onlCgformField : list) {
                    if (oConvertUtils.isEmpty((Object)onlCgformField.getMainField())) continue;
                    string3 = onlCgformField.getDbFieldName();
                    String string5 = onlCgformField.getMainField();
                    string4 = json.getString(string5);
                }
                for (int i2 = 0; i2 < jSONArray.size(); ++i2) {
                    OnlCgformField onlCgformField;
                    onlCgformField = jSONArray.getJSONObject(i2);
                    if (string4 != null) {
                        onlCgformField.put(string3, string4);
                    }
                    this.fieldService.executeInsertSQL(org.jeecg.modules.online.cgform.d.c.c(string2, list, (JSONObject)onlCgformField));
                }
            }
        }
        this.fieldService.saveFormData(onlCgformHead.getId(), tbname, json, true);
    }

    @Override
    public void editCrazyFormData(String tbname, JSONObject json) throws org.jeecg.modules.online.config.exception.a, UnsupportedEncodingException {
        String string;
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.getOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)tbname));
        if (onlCgformHead == null) {
            throw new org.jeecg.modules.online.config.exception.a("\u6570\u636e\u5e93\u4e3b\u8868[" + tbname + "]\u4e0d\u5b58\u5728");
        }
        if (onlCgformHead.getTableType() == 2 && oConvertUtils.isNotEmpty((Object)(string = onlCgformHead.getSubTableStr()))) {
            String[] stringArray;
            for (String string2 : stringArray = string.split(",")) {
                String string3;
                OnlCgformHead onlCgformHead2 = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string2));
                if (onlCgformHead2 == null) continue;
                List list = this.fieldService.list((Wrapper)new LambdaQueryWrapper().eq(OnlCgformField::getCgformHeadId, (Object)onlCgformHead2.getId()));
                String string4 = "";
                String string5 = null;
                for (OnlCgformField onlCgformField : list) {
                    if (oConvertUtils.isEmpty((Object)onlCgformField.getMainField())) continue;
                    string4 = onlCgformField.getDbFieldName();
                    string3 = onlCgformField.getMainField();
                    string5 = json.getString(string3);
                }
                if (oConvertUtils.isEmpty(string5)) continue;
                this.fieldService.deleteAutoList(string2, string4, string5);
                JSONArray jSONArray = this.a(string2, json);
                if (CollectionUtils.isEmpty((Collection)jSONArray)) continue;
                for (int i2 = 0; i2 < jSONArray.size(); ++i2) {
                    string3 = jSONArray.getJSONObject(i2);
                    if (string5 != null) {
                        string3.put(string4, (Object)string5);
                    }
                    this.fieldService.executeInsertSQL(org.jeecg.modules.online.cgform.d.c.c(string2, list, (JSONObject)string3));
                }
            }
        }
        this.fieldService.editFormData(onlCgformHead.getId(), tbname, json, true);
    }

    private JSONArray a(String string, JSONObject jSONObject) {
        try {
            JSONArray jSONArray = jSONObject.getJSONArray("sub_table_design_" + string);
            if (!CollectionUtils.isEmpty((Collection)jSONArray)) {
                return jSONArray;
            }
            jSONArray = jSONObject.getJSONArray("sub-table-design_" + string);
            if (!CollectionUtils.isEmpty((Collection)jSONArray)) {
                return jSONArray;
            }
            jSONArray = jSONObject.getJSONArray("sub-table-one2one_" + string);
            return jSONArray;
        }
        catch (Exception exception) {
            a.error("\u8868\u5355\u8bbe\u8ba1\u5668\u540c\u6b65\u5230Online\uff0c\u8f93\u5165\u7684\u5b50\u8868\u6570\u636e\u4e0d\u5408\u6cd5\uff0c\u5df2\u5ffd\u7565", (Throwable)exception);
            return null;
        }
    }

    @Override
    public Integer getMaxCopyVersion(String physicId) {
        Integer n = ((OnlCgformHeadMapper)this.baseMapper).getMaxCopyVersion(physicId);
        return n == null ? 0 : n;
    }

    @Override
    public void copyOnlineTableConfig(OnlCgformHead physicTable) throws Exception {
        String string = physicTable.getId();
        OnlCgformHead onlCgformHead = new OnlCgformHead();
        String string2 = UUIDGenerator.generate();
        onlCgformHead.setId(string2);
        onlCgformHead.setPhysicId(string);
        onlCgformHead.setCopyType(1);
        onlCgformHead.setCopyVersion(physicTable.getTableVersion());
        onlCgformHead.setTableVersion(1);
        onlCgformHead.setTableName(this.e(string, physicTable.getTableName()));
        onlCgformHead.setTableTxt(physicTable.getTableTxt());
        onlCgformHead.setFormCategory(physicTable.getFormCategory());
        onlCgformHead.setFormTemplate(physicTable.getFormTemplate());
        onlCgformHead.setFormTemplateMobile(physicTable.getFormTemplateMobile());
        onlCgformHead.setIdSequence(physicTable.getIdSequence());
        onlCgformHead.setIdType(physicTable.getIdType());
        onlCgformHead.setIsCheckbox(physicTable.getIsCheckbox());
        onlCgformHead.setIsPage(physicTable.getIsPage());
        onlCgformHead.setIsTree(physicTable.getIsTree());
        onlCgformHead.setQueryMode(physicTable.getQueryMode());
        onlCgformHead.setTableType(1);
        onlCgformHead.setIsDbSynch("N");
        onlCgformHead.setIsDesForm(physicTable.getIsDesForm());
        onlCgformHead.setDesFormCode(physicTable.getDesFormCode());
        onlCgformHead.setTreeParentIdField(physicTable.getTreeParentIdField());
        onlCgformHead.setTreeFieldname(physicTable.getTreeFieldname());
        onlCgformHead.setTreeIdField(physicTable.getTreeIdField());
        onlCgformHead.setRelationType(null);
        onlCgformHead.setTabOrderNum(null);
        onlCgformHead.setSubTableStr(null);
        onlCgformHead.setThemeTemplate(physicTable.getThemeTemplate());
        onlCgformHead.setScroll(physicTable.getScroll());
        onlCgformHead.setExtConfigJson(physicTable.getExtConfigJson());
        List list = this.fieldService.list((Wrapper)new LambdaQueryWrapper().eq(OnlCgformField::getCgformHeadId, (Object)string));
        for (OnlCgformField onlCgformField : list) {
            OnlCgformField onlCgformField2 = new OnlCgformField();
            onlCgformField2.setCgformHeadId(string2);
            this.a(onlCgformField, onlCgformField2);
            this.fieldService.save(onlCgformField2);
        }
        ((OnlCgformHeadMapper)this.baseMapper).insert(onlCgformHead);
    }

    @Override
    public void initCopyState(List<OnlCgformHead> headList) {
        List<String> list = ((OnlCgformHeadMapper)this.baseMapper).queryCopyPhysicId();
        for (OnlCgformHead onlCgformHead : headList) {
            if (list.contains(onlCgformHead.getId())) {
                onlCgformHead.setHascopy(1);
                continue;
            }
            onlCgformHead.setHascopy(0);
        }
    }

    @Override
    public void deleteBatch(String ids, String flag) {
        String[] stringArray = ids.split(",");
        if ("1".equals(flag)) {
            for (String string : stringArray) {
                try {
                    this.deleteRecordAndTable(string);
                }
                catch (org.jeecg.modules.online.config.exception.a a2) {
                    a2.printStackTrace();
                }
                catch (SQLException sQLException) {
                    sQLException.printStackTrace();
                }
            }
        } else {
            this.removeByIds(Arrays.asList(stringArray));
        }
    }

    @Override
    public void updateParentNode(OnlCgformHead head, String dataId) {
        if ("Y".equals(head.getIsTree())) {
            Integer n;
            String string = org.jeecg.modules.online.cgform.d.c.f(head.getTableName());
            String string2 = head.getTreeParentIdField();
            Map<String, Object> map = this.d(string, dataId);
            String string3 = null;
            if (map.get(string2) != null && !"0".equals(map.get(string2))) {
                string3 = map.get(string2).toString();
            } else if (map.get(string2.toUpperCase()) != null && !"0".equals(map.get(string2.toUpperCase()))) {
                string3 = map.get(string2.toUpperCase()).toString();
            }
            if (string3 != null && (n = ((OnlCgformHeadMapper)this.baseMapper).queryChildNode(string, string2, string3)) == 1) {
                String string4 = head.getTreeIdField();
                this.fieldService.updateTreeNodeNoChild(string, string4, string3);
            }
        }
    }

    private void b(OnlCgformHead onlCgformHead, List<OnlCgformField> list) {
        List list2 = this.list((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getPhysicId, (Object)onlCgformHead.getId()));
        if (list2 != null && list2.size() > 0) {
            for (OnlCgformHead onlCgformHead2 : list2) {
                Object object;
                Object object22;
                ArrayList arrayList2;
                List list3 = this.fieldService.list((Wrapper)new LambdaQueryWrapper().eq(OnlCgformField::getCgformHeadId, (Object)onlCgformHead2.getId()));
                if (list3 == null || list3.size() == 0) {
                    for (OnlCgformField onlCgformField : list) {
                        arrayList2 = new OnlCgformField();
                        ((OnlCgformField)((Object)arrayList2)).setCgformHeadId(onlCgformHead2.getId());
                        this.a(onlCgformField, (OnlCgformField)((Object)arrayList2));
                        this.fieldService.save(arrayList2);
                    }
                    continue;
                }
                HashMap hashMap3 = new HashMap(5);
                for (ArrayList arrayList2 : list3) {
                    hashMap3.put(((OnlCgformField)((Object)arrayList2)).getDbFieldName(), 1);
                }
                HashMap<String, Integer> hashMap = new HashMap<String, Integer>(5);
                for (OnlCgformField onlCgformField : list) {
                    hashMap.put(onlCgformField.getDbFieldName(), 1);
                }
                arrayList2 = new ArrayList();
                ArrayList arrayList3 = new ArrayList();
                for (Object object22 : hashMap.keySet()) {
                    if (hashMap3.get(object22) == null) {
                        arrayList3.add(object22);
                        continue;
                    }
                    arrayList2.add(object22);
                }
                ArrayList arrayList4 = new ArrayList();
                object22 = hashMap3.keySet().iterator();
                while (object22.hasNext()) {
                    object = (String)object22.next();
                    if (hashMap.get(object) != null) continue;
                    arrayList4.add(object);
                }
                if (arrayList4.size() > 0) {
                    object22 = list3.iterator();
                    while (object22.hasNext()) {
                        object = (OnlCgformField)object22.next();
                        if (!arrayList4.contains(((OnlCgformField)object).getDbFieldName())) continue;
                        this.fieldService.removeById((Serializable)((Object)((OnlCgformField)object).getId()));
                    }
                }
                if (arrayList3.size() > 0) {
                    object22 = list.iterator();
                    while (object22.hasNext()) {
                        object = (OnlCgformField)object22.next();
                        if (!arrayList3.contains(((OnlCgformField)object).getDbFieldName())) continue;
                        OnlCgformField onlCgformField = new OnlCgformField();
                        onlCgformField.setCgformHeadId(onlCgformHead2.getId());
                        this.a((OnlCgformField)object, onlCgformField);
                        this.fieldService.save(onlCgformField);
                    }
                }
                if (arrayList2.size() <= 0) continue;
                object22 = arrayList2.iterator();
                while (object22.hasNext()) {
                    object = (String)object22.next();
                    this.b((String)object, list, list3);
                }
            }
        }
    }

    private void b(String string, List<OnlCgformField> list, List<OnlCgformField> list2) {
        OnlCgformField object = null;
        for (OnlCgformField object2 : list) {
            if (!string.equals(object2.getDbFieldName())) continue;
            object = object2;
        }
        Object object3 = null;
        for (OnlCgformField onlCgformField : list2) {
            if (!string.equals(onlCgformField.getDbFieldName())) continue;
            object3 = onlCgformField;
        }
        if (object != null && object3 != null) {
            boolean bl;
            boolean bl2 = false;
            if (!object.getDbType().equals(((OnlCgformField)object3).getDbType())) {
                ((OnlCgformField)object3).setDbType(object.getDbType());
                boolean bl3 = true;
            }
            if (object.getDbDefaultVal() != null && !object.getDbDefaultVal().equals(((OnlCgformField)object3).getDbDefaultVal())) {
                ((OnlCgformField)object3).setDbDefaultVal(object.getDbDefaultVal());
                boolean bl4 = true;
            }
            if (!object.getDbLength().equals(((OnlCgformField)object3).getDbLength())) {
                ((OnlCgformField)object3).setDbLength(object.getDbLength());
                boolean bl5 = true;
            }
            if (object.getDbIsNull() != ((OnlCgformField)object3).getDbIsNull()) {
                ((OnlCgformField)object3).setDbIsNull(object.getDbIsNull());
                bl = true;
            }
            if (bl) {
                this.fieldService.updateById(object3);
            }
        }
    }

    private void a(OnlCgformField onlCgformField, OnlCgformField onlCgformField2) {
        onlCgformField2.setDbDefaultVal(onlCgformField.getDbDefaultVal());
        onlCgformField2.setDbFieldName(onlCgformField.getDbFieldName());
        onlCgformField2.setDbFieldNameOld(onlCgformField.getDbFieldNameOld());
        onlCgformField2.setDbFieldTxt(onlCgformField.getDbFieldTxt());
        onlCgformField2.setDbIsKey(onlCgformField.getDbIsKey());
        onlCgformField2.setDbIsNull(onlCgformField.getDbIsNull());
        onlCgformField2.setDbLength(onlCgformField.getDbLength());
        onlCgformField2.setDbPointLength(onlCgformField.getDbPointLength());
        onlCgformField2.setDbType(onlCgformField.getDbType());
        onlCgformField2.setDictField(onlCgformField.getDictField());
        onlCgformField2.setDictTable(onlCgformField.getDictTable());
        onlCgformField2.setDictText(onlCgformField.getDictText());
        onlCgformField2.setFieldExtendJson(onlCgformField.getFieldExtendJson());
        onlCgformField2.setFieldHref(onlCgformField.getFieldHref());
        onlCgformField2.setFieldLength(onlCgformField.getFieldLength());
        onlCgformField2.setFieldMustInput(onlCgformField.getFieldMustInput());
        onlCgformField2.setFieldShowType(onlCgformField.getFieldShowType());
        onlCgformField2.setFieldValidType(onlCgformField.getFieldValidType());
        onlCgformField2.setFieldDefaultValue(onlCgformField.getFieldDefaultValue());
        onlCgformField2.setIsQuery(onlCgformField.getIsQuery());
        onlCgformField2.setIsShowForm(onlCgformField.getIsShowForm());
        onlCgformField2.setIsShowList(onlCgformField.getIsShowList());
        onlCgformField2.setMainField(onlCgformField.getMainField());
        onlCgformField2.setMainTable(onlCgformField.getMainTable());
        onlCgformField2.setOrderNum(onlCgformField.getOrderNum());
        onlCgformField2.setQueryMode(onlCgformField.getQueryMode());
        onlCgformField2.setIsReadOnly(onlCgformField.getIsReadOnly());
        onlCgformField2.setSortFlag(onlCgformField.getSortFlag());
        onlCgformField2.setQueryDefVal(onlCgformField.getQueryDefVal());
        onlCgformField2.setQueryConfigFlag(onlCgformField.getQueryConfigFlag());
        onlCgformField2.setQueryDictField(onlCgformField.getQueryDictField());
        onlCgformField2.setQueryDictTable(onlCgformField.getQueryDictTable());
        onlCgformField2.setQueryDictText(onlCgformField.getQueryDictText());
        onlCgformField2.setQueryMustInput(onlCgformField.getQueryMustInput());
        onlCgformField2.setQueryShowType(onlCgformField.getQueryShowType());
        onlCgformField2.setQueryValidType(onlCgformField.getQueryValidType());
        onlCgformField2.setConverter(onlCgformField.getConverter());
        onlCgformField2.setDbIsPersist(onlCgformField.getDbIsPersist());
    }

    private void a(OnlCgformField onlCgformField) {
        if ("Text".equals(onlCgformField.getDbType()) || "Blob".equals(onlCgformField.getDbType())) {
            onlCgformField.setDbLength(0);
            onlCgformField.setDbPointLength(0);
        }
    }

    private String e(String string, String string2) {
        List<String> list = ((OnlCgformHeadMapper)this.baseMapper).queryAllCopyTableName(string);
        int n = 0;
        if (list != null || list.size() > 0) {
            for (int i2 = 0; i2 < list.size(); ++i2) {
                String string3 = list.get(i2);
                int n2 = Integer.parseInt(string3.split("\\$")[1]);
                if (n2 <= n) continue;
                n = n2;
            }
        }
        return string2 + "$" + ++n;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public String deleteDataByCode(String cgformCode, String dataIds) {
        OnlCgformHead onlCgformHead = (OnlCgformHead)super.getOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)cgformCode));
        if (onlCgformHead == null) {
            throw new JeecgBootException("\u5b9e\u4f53\u4e0d\u5b58\u5728");
        }
        String string = onlCgformHead.getTableName();
        try {
            if (dataIds.indexOf(",") > 0) {
                this.onlCgformFieldService.deleteAutoListById(string, dataIds);
            } else {
                this.deleteOneTableInfo(onlCgformHead.getId(), dataIds);
            }
        }
        catch (Exception exception) {
            a.error("OnlCgformApiController.formEdit()\u53d1\u751f\u5f02\u5e38\uff1a" + exception.getMessage(), (Throwable)exception);
            throw new JeecgBootException("\u5220\u9664\u5931\u8d25\uff1a" + exception.getMessage());
        }
        return string;
    }

    @Override
    public JSONObject queryAllDataByTableNameForDesform(String tableName, String dataIds) throws org.jeecg.modules.online.config.exception.a {
        JSONObject jSONObject = new JSONObject();
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformHead::getTableName, (Object)tableName);
        OnlCgformHead onlCgformHead = (OnlCgformHead)super.getOne((Wrapper)lambdaQueryWrapper);
        if (onlCgformHead == null) {
            throw new JeecgBootException("\u8868\u5355\u6570\u636e\u4e0d\u5b58\u5728\uff01");
        }
        Map<String, Object> map = this.queryManyFormData(onlCgformHead.getId(), dataIds);
        if (map == null) {
            throw new JeecgBootException("\u8868\u5355\u6570\u636e\u67e5\u8be2\u5931\u8d25\uff01");
        }
        JSONObject jSONObject2 = JSON.parseObject((String)JSON.toJSONString(map));
        String string = onlCgformHead.getSubTableStr();
        if (oConvertUtils.isNotEmpty((Object)string)) {
            ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(string.split(",")));
            LambdaQueryWrapper lambdaQueryWrapper2 = new LambdaQueryWrapper();
            lambdaQueryWrapper2.in(OnlCgformHead::getTableName, arrayList);
            List list = super.list((Wrapper)lambdaQueryWrapper2);
            JSONObject jSONObject3 = new JSONObject();
            JSONObject jSONObject4 = new JSONObject();
            for (OnlCgformHead onlCgformHead2 : list) {
                JSONArray jSONArray = jSONObject2.getJSONArray(onlCgformHead2.getTableName());
                if (jSONArray != null && jSONArray.size() > 0) {
                    if (0 == onlCgformHead2.getRelationType()) {
                        jSONObject3.put(onlCgformHead2.getTableName(), (Object)jSONArray);
                    } else {
                        JSONObject jSONObject5 = jSONArray.getJSONObject(0);
                        jSONObject4.put(onlCgformHead2.getTableName(), (Object)jSONObject5);
                    }
                }
                jSONObject2.remove((Object)onlCgformHead2.getTableName());
            }
            jSONObject.put("one2one", (Object)jSONObject4);
            jSONObject.put("one2many", (Object)jSONObject3);
        }
        jSONObject.put("main", (Object)jSONObject2);
        return jSONObject;
    }

    @Override
    public OnlCgformHead copyOnlineTable(String id, String tableName) {
        Serializable serializable;
        Object object2;
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformHead::getTableName, (Object)tableName);
        Long l2 = ((OnlCgformHeadMapper)this.baseMapper).selectCount((Wrapper)lambdaQueryWrapper);
        if (l2 != null && l2 >= 1L) {
            throw new JeecgBootException("\u8868\u540d\u5df2\u7ecf\u5b58\u5728!");
        }
        OnlCgformHead onlCgformHead = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectById((Serializable)((Object)id));
        if (onlCgformHead == null) {
            throw new JeecgBootException("\u8868\u4e0d\u5b58\u5728!");
        }
        OnlCgformHead onlCgformHead2 = new OnlCgformHead();
        BeanUtils.copyProperties((Object)onlCgformHead, (Object)onlCgformHead2);
        String string = org.jeecg.modules.online.cgform.d.c.a();
        onlCgformHead2.setId(string);
        onlCgformHead2.setSubTableStr(null);
        onlCgformHead2.setTableName(tableName);
        onlCgformHead2.setTableVersion(1);
        onlCgformHead2.setIsDbSynch("N");
        onlCgformHead2.setCreateBy(null);
        onlCgformHead2.setCreateTime(null);
        onlCgformHead2.setUpdateBy(null);
        onlCgformHead2.setUpdateTime(null);
        LambdaQueryWrapper lambdaQueryWrapper2 = new LambdaQueryWrapper();
        lambdaQueryWrapper2.eq(OnlCgformField::getCgformHeadId, (Object)id);
        List list = this.fieldService.list((Wrapper)lambdaQueryWrapper2);
        ArrayList<Serializable> arrayList = new ArrayList<Serializable>();
        if (list != null && list.size() > 0) {
            for (Object object2 : list) {
                serializable = new OnlCgformField();
                BeanUtils.copyProperties((Object)object2, (Object)serializable);
                ((OnlCgformField)serializable).setCgformHeadId(string);
                ((OnlCgformField)serializable).setMainField(null);
                ((OnlCgformField)serializable).setMainTable(null);
                ((OnlCgformField)serializable).setId(null);
                ((OnlCgformField)serializable).setCreateBy(null);
                ((OnlCgformField)serializable).setCreateTime(null);
                ((OnlCgformField)serializable).setUpdateBy(null);
                ((OnlCgformField)serializable).setUpdateTime(null);
                arrayList.add(serializable);
            }
        }
        LambdaQueryWrapper lambdaQueryWrapper3 = new LambdaQueryWrapper();
        lambdaQueryWrapper3.eq(OnlCgformIndex::getCgformHeadId, (Object)id);
        object2 = this.indexService.list((Wrapper)lambdaQueryWrapper3);
        serializable = new ArrayList();
        if (object2 != null && object2.size() > 0) {
            Iterator iterator = object2.iterator();
            while (iterator.hasNext()) {
                OnlCgformIndex onlCgformIndex = (OnlCgformIndex)iterator.next();
                OnlCgformIndex onlCgformIndex2 = new OnlCgformIndex();
                BeanUtils.copyProperties((Object)onlCgformIndex, (Object)onlCgformIndex2);
                onlCgformIndex2.setCgformHeadId(string);
                onlCgformIndex2.setId(null);
                onlCgformIndex2.setCreateBy(null);
                onlCgformIndex2.setCreateTime(null);
                onlCgformIndex2.setUpdateBy(null);
                onlCgformIndex2.setUpdateTime(null);
                serializable.add(onlCgformIndex2);
            }
        }
        this.save(onlCgformHead2);
        this.fieldService.saveBatch(arrayList);
        this.indexService.saveBatch((Collection)((Object)serializable));
        return onlCgformHead2;
    }

    @Override
    public OnlCgformHead getTable(String code) throws org.jeecg.modules.online.config.exception.a {
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.getById((Serializable)((Object)code));
        if (onlCgformHead == null) {
            LambdaQueryWrapper lambdaQueryWrapper = (LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)code);
            onlCgformHead = (OnlCgformHead)((OnlCgformHeadMapper)this.baseMapper).selectOne((Wrapper)lambdaQueryWrapper);
        }
        if (onlCgformHead == null) {
            throw new org.jeecg.modules.online.config.exception.a("online\u8868[" + code + "]\u4e0d\u5b58\u5728");
        }
        return onlCgformHead;
    }

    private b getOnlineDataBaseConfig() {
        if (oConvertUtils.isEmpty((Object)this.onlineDatasource)) {
            return this.dataBaseConfig;
        }
        DataSourceProperty dataSourceProperty = CommonUtils.getDataSourceProperty((String)this.onlineDatasource);
        if (dataSourceProperty == null) {
            a.error("jeecg.online.datasource\u914d\u7f6e\u9519\u8bef,\u83b7\u53d6\u4e0d\u5230\u6570\u636e\u6e90\u8fd4\u56demaster");
            return this.dataBaseConfig;
        }
        b b2 = new b();
        b2.setDriverClassName(dataSourceProperty.getDriverClassName());
        b2.setPassword(dataSourceProperty.getPassword());
        b2.setUsername(dataSourceProperty.getUsername());
        b2.setUrl(dataSourceProperty.getUrl());
        b2.setDmDataBaseConfig(new c());
        return b2;
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getEvent": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJava::getEvent;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJava::getEvent;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformEnhanceJava::getEvent;
            }
            case "getOrderNum": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) {
                    return OnlCgformField::getOrderNum;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) {
                    return OnlCgformButton::getOrderNum;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) {
                    return OnlCgformButton::getOrderNum;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) {
                    return OnlCgformButton::getOrderNum;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlCgformField::getOrderNum;
            }
            case "getCgformHeadId": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformIndex") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformIndex::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformIndex") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformIndex::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJs") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJs::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceSql") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceSql::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJava::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformButton::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformButton::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJava::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJava::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJava::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceSql") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceSql::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformButton::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJs") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJs::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformIndex") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformIndex::getCgformHeadId;
            }
            case "getTableName": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformHead::getTableName;
            }
            case "getButtonStyle": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformButton::getButtonStyle;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformButton::getButtonStyle;
            }
            case "getCgJavaType": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformEnhanceJava::getCgJavaType;
            }
            case "getFieldValidType": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformField::getFieldValidType;
            }
            case "getCgformId": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthRelation") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlAuthRelation::getCgformId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthData") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlAuthData::getCgformId;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlAuthPage::getCgformId;
            }
            case "getId": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getId;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformHead::getId;
            }
            case "getDbIsPersist": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) {
                    return OnlCgformField::getDbIsPersist;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlCgformField::getDbIsPersist;
            }
            case "getButtonCode": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceSql") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceSql::getButtonCode;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJava::getButtonCode;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJava::getButtonCode;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJava::getButtonCode;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJava::getButtonCode;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceSql") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformEnhanceSql::getButtonCode;
            }
            case "getCgJsType": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJs") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJs::getCgJsType;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJs") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformEnhanceJs::getCgJsType;
            }
            case "getActiveStatus": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJava::getActiveStatus;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJava::getActiveStatus;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformEnhanceJava::getActiveStatus;
            }
            case "getPhysicId": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getPhysicId;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformHead::getPhysicId;
            }
            case "getSubTableStr": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformHead::getSubTableStr;
            }
            case "getButtonStatus": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformButton::getButtonStatus;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformButton::getButtonStatus;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformButton::getButtonStatus;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

