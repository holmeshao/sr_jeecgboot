/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  org.apache.commons.lang.StringUtils
 *  org.apache.poi.hssf.usermodel.HSSFWorkbook
 *  org.apache.poi.ss.usermodel.Workbook
 *  org.jeecg.common.exception.JeecgBootException
 *  org.jeecg.common.system.api.ISysBaseAPI
 *  org.jeecg.common.system.vo.DictModel
 *  org.jeecg.common.util.oConvertUtils
 *  org.jeecgframework.poi.excel.entity.ExportParams
 *  org.jeecgframework.poi.excel.entity.params.ExcelExportEntity
 *  org.jeecgframework.poi.excel.export.ExcelExportServer
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.stereotype.Service
 */
package org.jeecg.modules.online.cgreport.service.a;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportHead;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportItem;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportAPIService;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportItemService;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportParamService;
import org.jeecg.modules.online.cgreport.service.a.c;
import org.jeecg.modules.online.config.b.a;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.jeecgframework.poi.excel.export.ExcelExportServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service(value="onlCgreportAPIService")
public class b
implements IOnlCgreportAPIService {
    private static final Logger a = LoggerFactory.getLogger(b.class);
    @Autowired
    private c onlCgreportHeadService;
    @Autowired
    private IOnlCgreportItemService onlCgreportItemService;
    @Lazy
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private IOnlCgreportParamService onlCgreportParamService;
    @Autowired
    private a onlReportQueryBlackListHandler;

    @Override
    public Map<String, Object> getDataById(String id, Map<String, Object> params) {
        return this.getData(id, null, params);
    }

    @Override
    public Map<String, Object> getDataByCode(String code, Map<String, Object> params) {
        return this.getData(null, code, params);
    }

    @Override
    public Map<String, Object> getData(String id, String code, Map<String, Object> params) {
        Object object;
        OnlCgreportHead onlCgreportHead = null;
        if (oConvertUtils.isNotEmpty((Object)id)) {
            onlCgreportHead = (OnlCgreportHead)this.onlCgreportHeadService.getById((Serializable)((Object)id));
        } else if (oConvertUtils.isNotEmpty((Object)code)) {
            object = new LambdaQueryWrapper();
            object.eq(OnlCgreportHead::getCode, (Object)code);
            onlCgreportHead = (OnlCgreportHead)this.onlCgreportHeadService.getOne((Wrapper)object);
        }
        if (onlCgreportHead == null) {
            throw new JeecgBootException("\u5b9e\u4f53\u4e0d\u5b58\u5728");
        }
        try {
            object = onlCgreportHead.getCgrSql().trim();
            String string = onlCgreportHead.getDbSource();
            return this.executeSelectSqlRoute(string, (String)object, params, onlCgreportHead.getId());
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            throw new JeecgBootException("SQL\u6267\u884c\u5931\u8d25\uff1a" + exception.getMessage());
        }
    }

    @Override
    public Map<String, Object> executeSelectSqlRoute(String dbKey, String sql, Map<String, Object> params, String headId) throws Exception {
        if (!this.onlReportQueryBlackListHandler.isPass(sql)) {
            throw new JeecgBootException(this.onlReportQueryBlackListHandler.getError());
        }
        if (StringUtils.isNotBlank((String)dbKey)) {
            a.debug("Online\u62a5\u8868: \u8d70\u4e86\u591a\u6570\u636e\u6e90\u903b\u8f91");
            return this.onlCgreportHeadService.executeSelectSqlDynamic(dbKey, sql, params, headId);
        }
        a.debug("Online\u62a5\u8868: \u8d70\u4e86\u7a33\u5b9a\u903b\u8f91");
        return this.onlCgreportHeadService.executeSelectSql(sql, headId, params);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public Workbook getReportWorkbook(String reportId, Map<String, Object> params) {
        boolean bl;
        Object object;
        Object object2;
        Map<String, Object> map;
        Object object3;
        LambdaQueryWrapper lambdaQueryWrapper = (LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgreportItem::getCgrheadId, (Object)reportId);
        lambdaQueryWrapper.orderByAsc(OnlCgreportItem::getOrderNum);
        List list = this.onlCgreportItemService.list((Wrapper)lambdaQueryWrapper);
        ArrayList<ExcelExportEntity> arrayList = new ArrayList<ExcelExportEntity>();
        HashMap<String, List> hashMap = new HashMap<String, List>(5);
        ArrayList<Map<String, Object>> arrayList2 = new ArrayList<Map<String, Object>>();
        HashMap<String, Object> hashMap2 = new HashMap<String, Object>(5);
        for (OnlCgreportItem object6 : list) {
            object3 = object6.getFieldType();
            map = object6.getFieldName();
            if ("1".equals(oConvertUtils.getString((Object)object6.getIsShow()))) {
                object2 = new ExcelExportEntity(object6.getFieldTxt(), (Object)map, 15);
                this.a(object6, (ExcelExportEntity)object2);
                if ("date".equalsIgnoreCase(object6.getFieldType())) {
                    object2.setFormat("yyyy-MM-dd");
                } else if ("datetime".equalsIgnoreCase(object6.getFieldType())) {
                    object2.setFormat("yyyy-MM-dd HH:mm:ss");
                }
                String string = object6.getGroupTitle();
                if (oConvertUtils.isNotEmpty((Object)string)) {
                    ArrayList<Map<String, Object>> arrayList3 = new ArrayList<Map<String, Object>>();
                    if (hashMap.containsKey(string)) {
                        List list2 = (List)hashMap.get(string);
                        list2.add(map);
                    } else {
                        object = new ExcelExportEntity(string, (Object)string, true);
                        arrayList.add((ExcelExportEntity)object);
                        arrayList3.add(map);
                    }
                    hashMap.put(string, arrayList3);
                    object2.setColspan(true);
                }
                if (oConvertUtils.isNotEmpty((Object)object3) && oConvertUtils.isEmpty((Object)object6.getDictCode()) && ("Integer".equals(object3) || "Long".equals(object3))) {
                    object2.setType(4);
                }
                arrayList.add((ExcelExportEntity)object2);
            }
            if (!"1".equals(oConvertUtils.getString((String)object6.getIsTotal()))) continue;
            arrayList2.add(map);
        }
        for (Map.Entry entry : hashMap.entrySet()) {
            object3 = (String)entry.getKey();
            map = (List)entry.getValue();
            for (ExcelExportEntity excelExportEntity : arrayList) {
                if (!((String)object3).equals(excelExportEntity.getName()) || !excelExportEntity.isColspan()) continue;
                excelExportEntity.setSubColumnList((List)((Object)map));
            }
        }
        Iterator<Object> iterator = new HSSFWorkbook();
        boolean bl2 = true;
        object3 = 1;
        params.put("pageSize", 10000);
        while (bl) {
            map = object3;
            object2 = object3 = Integer.valueOf((Integer)object3 + 1);
            params.put("pageNo", map);
            map = this.getDataById(reportId, params);
            object2 = (List)map.get("records");
            if (object2 == null || object2.size() == 0) {
                bl = false;
                continue;
            }
            if (arrayList2 != null && arrayList2.size() > 0) {
                for (String string : arrayList2) {
                    object = new BigDecimal(0.0);
                    Iterator iterator2 = object2.iterator();
                    while (iterator2.hasNext()) {
                        Map map2 = (Map)iterator2.next();
                        String string2 = map2.get(string).toString();
                        if (!string2.matches("-?\\d+(.\\d+)?")) continue;
                        BigDecimal bigDecimal = new BigDecimal(string2);
                        object = ((BigDecimal)object).add(bigDecimal);
                    }
                    hashMap2.put(string, object);
                }
                object2.add(hashMap2);
            }
            ExcelExportServer excelExportServer = new ExcelExportServer();
            ExportParams exportParams = new ExportParams();
            excelExportServer.createSheetForMap(iterator, exportParams, arrayList, (Collection)object2);
        }
        return iterator;
    }

    private void a(OnlCgreportItem onlCgreportItem, ExcelExportEntity excelExportEntity) {
        Object object;
        String string = "_";
        String string2 = "---";
        String string3 = onlCgreportItem.getDictCode();
        List<DictModel> list = this.onlCgreportHeadService.queryColumnDictList(oConvertUtils.getString((String)string3), null, null);
        if (list != null && list.size() > 0) {
            object = new ArrayList();
            for (DictModel dictModel : list) {
                if (dictModel == null || dictModel.getValue() == null) continue;
                if (dictModel.getValue().contains(string)) {
                    String string4 = dictModel.getValue().replace(string, string2);
                    object.add(dictModel.getText() + string + string4);
                    continue;
                }
                object.add(dictModel.getText() + string + dictModel.getValue());
            }
            excelExportEntity.setReplace(object.toArray(new String[object.size()]));
        }
        if (oConvertUtils.isNotEmpty((Object)(object = onlCgreportItem.getReplaceVal()))) {
            excelExportEntity.setReplace(object.toString().split(","));
        }
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getOrderNum": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportItem") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlCgreportItem::getOrderNum;
            }
            case "getCgrheadId": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportItem") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgreportItem::getCgrheadId;
            }
            case "getCode": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportHead") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgreportHead::getCode;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

