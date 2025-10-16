/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  org.apache.poi.ss.usermodel.Workbook
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.jeecg.common.api.vo.Result
 *  org.jeecg.common.aspect.annotation.PermissionData
 *  org.jeecg.common.exception.JeecgBootException
 *  org.jeecg.common.system.api.ISysBaseAPI
 *  org.jeecg.common.system.vo.DictModel
 *  org.jeecg.common.system.vo.DynamicDataSourceModel
 *  org.jeecg.common.util.BrowserUtils
 *  org.jeecg.common.util.SqlInjectionUtil
 *  org.jeecg.common.util.oConvertUtils
 *  org.jeecg.common.util.security.JdbcSecurityUtil
 *  org.jeecgframework.poi.excel.ExcelExportUtil
 *  org.jeecgframework.poi.excel.entity.ExportParams
 *  org.jeecgframework.poi.excel.entity.params.ExcelExportEntity
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package org.jeecg.modules.online.cgreport.a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.DynamicDataSourceModel;
import org.jeecg.common.util.BrowserUtils;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.common.util.security.JdbcSecurityUtil;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportHead;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportItem;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportParam;
import org.jeecg.modules.online.cgreport.mapper.OnlCgreportHeadMapper;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportItemService;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportParamService;
import org.jeecg.modules.online.cgreport.service.a.b;
import org.jeecg.modules.online.cgreport.service.a.c;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="onlCgreportAPI")
@RequestMapping(value={"/online/cgreport/api"})
public class CgreportApiController {
    private static final Logger a = LoggerFactory.getLogger(CgreportApiController.class);
    @Autowired
    private b cgreportAPIService;
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
    private org.jeecg.modules.online.config.b.a onlReportQueryBlackListHandler;

    @GetMapping(value={"/getColumnsAndData/{code}"})
    @PermissionData
    public Result<?> a(@PathVariable(value="code") String string, HttpServletRequest httpServletRequest) {
        OnlCgreportHead onlCgreportHead = (OnlCgreportHead)this.onlCgreportHeadService.getById((Serializable)((Object)string));
        if (onlCgreportHead == null) {
            return Result.error((String)"\u5b9e\u4f53\u4e0d\u5b58\u5728");
        }
        Result<?> result = this.b(string, httpServletRequest);
        if (result.getCode().equals(200)) {
            Map map = (Map)result.getResult();
            List list = (List)map.get("records");
            Map<String, Object> map2 = this.onlCgreportHeadService.queryColumnInfo(string, false);
            JSONArray jSONArray = (JSONArray)map2.get("columns");
            HashMap<String, List<DictModel>> hashMap = new HashMap<String, List<DictModel>>(5);
            if (jSONArray != null) {
                for (int i2 = 0; i2 < jSONArray.size(); ++i2) {
                    String string2;
                    String string3;
                    List<DictModel> list2;
                    JSONObject jSONObject = jSONArray.getJSONObject(i2);
                    Object object = jSONObject.get((Object)"dictCode");
                    if (object == null || (list2 = this.onlCgreportHeadService.queryColumnDictList(string3 = object.toString(), list, string2 = jSONArray.getJSONObject(i2).getString("dataIndex"))) == null) continue;
                    hashMap.put(string2, list2);
                    jSONObject.put("customRender", (Object)string2);
                }
            }
            map2.put("cgreportHeadName", onlCgreportHead.getName());
            map2.put("data", result.getResult());
            map2.put("dictOptions", hashMap);
            return Result.ok(map2);
        }
        return result;
    }

    @Deprecated
    @GetMapping(value={"/getColumns/{code}"})
    public Result<?> a(@PathVariable(value="code") String string) {
        OnlCgreportHead onlCgreportHead = (OnlCgreportHead)this.onlCgreportHeadService.getById((Serializable)((Object)string));
        if (onlCgreportHead == null) {
            return Result.error((String)"\u5b9e\u4f53\u4e0d\u5b58\u5728");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq((Object)"cgrhead_id", (Object)string);
        queryWrapper.eq((Object)"is_show", (Object)1);
        queryWrapper.orderByAsc((Object)"order_num");
        List list = this.onlCgreportItemService.list((Wrapper)queryWrapper);
        ArrayList arrayList = new ArrayList();
        HashMap<String, List> hashMap = new HashMap<String, List>(5);
        for (OnlCgreportItem onlCgreportItem : list) {
            HashMap<String, String> hashMap2 = new HashMap<String, String>(5);
            hashMap2.put("title", onlCgreportItem.getFieldTxt());
            hashMap2.put("dataIndex", onlCgreportItem.getFieldName());
            hashMap2.put("align", "center");
            hashMap2.put("sorter", "true");
            arrayList.add(hashMap2);
            String string2 = onlCgreportItem.getDictCode();
            if (!oConvertUtils.isNotEmpty((Object)string2)) continue;
            List list2 = null;
            if (string2.toLowerCase().indexOf("select ") == 0) {
                this.sysBaseAPI.dictTableWhiteListCheckByDict(string2, new String[0]);
                SqlInjectionUtil.specialFilterContentForOnlineReport((String)string2);
                List<Map<String, Object>> list3 = ((OnlCgreportHeadMapper)this.onlCgreportHeadService.getBaseMapper()).executeSqlDict(string2);
                if (list3 != null && list3.size() != 0) {
                    String string3 = JSON.toJSONString(list3);
                    list2 = JSON.parseArray((String)string3, DictModel.class);
                }
            } else {
                list2 = this.sysBaseAPI.queryDictItemsByCode(string2);
            }
            if (list2 == null) continue;
            hashMap.put(onlCgreportItem.getFieldName(), list2);
            hashMap2.put("customRender", onlCgreportItem.getFieldName());
        }
        HashMap hashMap3 = new HashMap(1);
        hashMap3.put("columns", arrayList);
        hashMap3.put("dictOptions", hashMap);
        hashMap3.put("cgreportHeadName", onlCgreportHead.getName());
        return Result.ok((Object)hashMap3);
    }

    @GetMapping(value={"/getData/{code}"})
    @PermissionData
    public Result<?> b(@PathVariable(value="code") String string, HttpServletRequest httpServletRequest) {
        Map<String, Object> map = org.jeecg.modules.online.cgreport.c.a.a(httpServletRequest);
        map.put("getAll", httpServletRequest.getAttribute("getAll"));
        try {
            return Result.OK(this.cgreportAPIService.getDataById(string, map));
        }
        catch (JeecgBootException jeecgBootException) {
            return Result.error((String)jeecgBootException.getMessage());
        }
    }

    @GetMapping(value={"/getDataOrderByValue/{code}"})
    @PermissionData
    public Result<?> c(@PathVariable(value="code") String string, HttpServletRequest httpServletRequest) {
        OnlCgreportHead onlCgreportHead = (OnlCgreportHead)this.onlCgreportHeadService.getById((Serializable)((Object)string));
        if (onlCgreportHead == null) {
            return Result.error((String)"\u5b9e\u4f53\u4e0d\u5b58\u5728");
        }
        String string2 = onlCgreportHead.getCgrSql().trim();
        String string3 = onlCgreportHead.getDbSource();
        try {
            Map<String, Object> map = org.jeecg.modules.online.cgreport.c.a.a(httpServletRequest);
            Object object = map.get("order_field");
            Object object2 = map.get("order_value");
            if (oConvertUtils.isEmpty((Object)object) || oConvertUtils.isEmpty((Object)object2)) {
                return Result.error((String)"order_field \u548c order_value \u53c2\u6570\u4e0d\u80fd\u4e3a\u7a7a\uff01");
            }
            String string4 = "force_" + object;
            map.put(string4, object2);
            map.put("getAll", true);
            Map<String, Object> map2 = this.cgreportAPIService.executeSelectSqlRoute(string3, string2, map, onlCgreportHead.getId());
            JSONArray jSONArray = JSON.parseArray((String)JSON.toJSONString((Object)map2.get("records")));
            map.remove(object.toString());
            map.remove(string4);
            map.remove("order_field");
            map.remove("order_value");
            map.put("getAll", httpServletRequest.getAttribute("getAll"));
            Map<String, Object> map3 = this.cgreportAPIService.executeSelectSqlRoute(string3, string2, map, onlCgreportHead.getId());
            JSONArray jSONArray2 = JSON.parseArray((String)JSON.toJSONString((Object)map3.get("records")));
            this.a(jSONArray, jSONArray2, object.toString());
            map3.put("records", jSONArray2);
            return Result.ok(map3);
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)("SQL\u6267\u884c\u5931\u8d25\uff1a" + exception.getMessage()));
        }
    }

    private void a(JSONArray jSONArray, JSONArray jSONArray2, String string) {
        for (int i2 = 0; i2 < jSONArray.size(); ++i2) {
            int n;
            JSONObject jSONObject = jSONArray.getJSONObject(i2);
            String string2 = jSONObject.getString(string);
            if (string2 == null || (n = (int)jSONArray2.stream().filter(object -> string2.equals(((JSONObject)object).getString(string))).count()) != 0) continue;
            jSONArray2.add(0, (Object)jSONObject);
        }
    }

    @GetMapping(value={"/getQueryInfo/{code}"})
    public Result<?> b(@PathVariable(value="code") String string) {
        try {
            List<Map<String, String>> list = this.onlCgreportItemService.getAutoListQueryInfo(string);
            return Result.ok(list);
        }
        catch (Exception exception) {
            return Result.error((String)"\u67e5\u8be2\u5931\u8d25");
        }
    }

    @GetMapping(value={"/getParamsInfo/{code}"})
    public Result<?> c(@PathVariable(value="code") String string) {
        try {
            LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(OnlCgreportParam::getCgrheadId, (Object)string);
            lambdaQueryWrapper.orderByAsc(OnlCgreportParam::getOrderNum);
            List list = this.onlCgreportParamService.list((Wrapper)lambdaQueryWrapper);
            return Result.ok((Object)list);
        }
        catch (Exception exception) {
            return Result.error((String)"\u67e5\u8be2\u5931\u8d25");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @PermissionData
    @RequestMapping(value={"/exportManySheetXls/{reportId}"})
    public void a(@PathVariable(value="reportId") String string, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (oConvertUtils.isEmpty((Object)string)) {
            throw new JeecgBootException("\u53c2\u6570\u9519\u8bef");
        }
        Map<String, Object> map = org.jeecg.modules.online.cgreport.c.a.a(httpServletRequest);
        Workbook workbook = this.cgreportAPIService.getReportWorkbook(string, map);
        String string2 = "\u62a5\u8868";
        httpServletResponse.setContentType("application/vnd.ms-excel");
        OutputStream outputStream = null;
        try {
            String string3 = BrowserUtils.checkBrowse((HttpServletRequest)httpServletRequest);
            if ("MSIE".equalsIgnoreCase(string3.substring(0, 4))) {
                httpServletResponse.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(string2, "UTF-8") + ".xls");
            } else {
                String string4 = new String(string2.getBytes("UTF-8"), "ISO8859-1");
                httpServletResponse.setHeader("content-disposition", "attachment;filename=" + string4 + ".xls");
            }
            outputStream = httpServletResponse.getOutputStream();
            workbook.write(outputStream);
        }
        catch (Exception exception) {
            a.warn("\u5bfc\u51fa\u5931\u8d25", (Object)exception.getMessage());
        }
        finally {
            try {
                outputStream.flush();
                outputStream.close();
            }
            catch (Exception exception) {}
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * WARNING - void declaration
     */
    @Deprecated
    @PermissionData
    @RequestMapping(value={"/exportXls/{reportId}"})
    public void b(@PathVariable(value="reportId") String string, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String string2 = "\u62a5\u8868";
        String string3 = "\u5bfc\u51fa\u4fe1\u606f";
        if (oConvertUtils.isNotEmpty((Object)string)) {
            Iterator iterator;
            Object object;
            Object object22;
            Iterator iterator2;
            Object object3;
            Object object5;
            Map<String, Object> map = null;
            try {
                map = this.onlCgreportHeadService.queryCgReportConfig(string);
            }
            catch (Exception exception) {
                throw new JeecgBootException("\u52a8\u6001\u62a5\u8868\u914d\u7f6e\u4e0d\u5b58\u5728!");
            }
            List list = (List)map.get("items");
            httpServletRequest.setAttribute("getAll", (Object)true);
            Result<?> result = this.b(string, httpServletRequest);
            List list2 = null;
            if (result.getCode().equals(200)) {
                object5 = (Map)result.getResult();
                list2 = (List)object5.get("records");
            }
            object5 = new ArrayList();
            HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
            HashMap<Object, List> hashMap2 = new HashMap<Object, List>(5);
            ArrayList<Object> arrayList = new ArrayList<Object>();
            for (int i2 = 0; i2 < list.size(); ++i2) {
                Map object42 = (Map)list.get(i2);
                object3 = (String)object42.get("field_type");
                if ("1".equals(oConvertUtils.getString(((Map)list.get(i2)).get("is_show")))) {
                    String string4;
                    ArrayList<Object> arrayList2;
                    iterator2 = ((Map)list.get(i2)).get("field_name").toString();
                    Object object4 = new ExcelExportEntity(((Map)list.get(i2)).get("field_txt").toString(), iterator2, 15);
                    object22 = "_";
                    object = "---";
                    Object v = ((Map)list.get(i2)).get("dict_code");
                    List<DictModel> list3 = this.onlCgreportHeadService.queryColumnDictList(oConvertUtils.getString(v), list2, (String)((Object)iterator2));
                    if (list3 != null && list3.size() > 0) {
                        arrayList2 = new ArrayList();
                        for (DictModel dictModel : list3) {
                            if (dictModel.getValue().contains((CharSequence)object22)) {
                                string4 = dictModel.getValue().replace((CharSequence)object22, (CharSequence)object);
                                arrayList2.add(dictModel.getText() + (String)object22 + string4);
                                continue;
                            }
                            arrayList2.add(dictModel.getText() + (String)object22 + dictModel.getValue());
                        }
                        object4.setReplace(arrayList2.toArray(new String[arrayList2.size()]));
                    }
                    if (oConvertUtils.isNotEmpty(arrayList2 = ((Map)list.get(i2)).get("replace_val"))) {
                        object4.setReplace(((Object)arrayList2).toString().split(","));
                    }
                    if (oConvertUtils.isNotEmpty(((Map)list.get(i2)).get("group_title"))) {
                        String string5 = ((Map)list.get(i2)).get("group_title").toString();
                        ArrayList arrayList3 = new ArrayList();
                        if (hashMap2.containsKey(string5)) {
                            List list4 = (List)hashMap2.get(string5);
                            list4.add(iterator2);
                        } else {
                            string4 = new ExcelExportEntity(string5, (Object)string5, true);
                            arrayList.add(string4);
                            arrayList3.add(iterator2);
                        }
                        hashMap2.put(string5, arrayList3);
                        object4.setColspan(true);
                    }
                    if (oConvertUtils.isNotEmpty((Object)object3) && oConvertUtils.isEmpty(v) && ("Integer".equals(object3) || "Long".equals(object3))) {
                        object4.setType(4);
                    }
                    arrayList.add(object4);
                }
                if (!"1".equals(oConvertUtils.getString(((Map)list.get(i2)).get("is_total")))) continue;
                object5.add(((Map)list.get(i2)).get("field_name").toString());
            }
            for (Map.Entry exception : hashMap2.entrySet()) {
                object3 = (String)exception.getKey();
                iterator2 = (List)exception.getValue();
                for (Object object22 : arrayList) {
                    if (!((String)object3).equals(object22.getName()) || !object22.isColspan()) continue;
                    object22.setSubColumnList(iterator2);
                }
            }
            if (object5 != null && object5.size() > 0) {
                iterator = object5.iterator();
                while (iterator.hasNext()) {
                    String string6 = (String)((Object)iterator.next());
                    object3 = new BigDecimal(0.0);
                    for (Object object4 : list2) {
                        object22 = object4.get(string6).toString();
                        if (!((String)object22).matches("\\d+(.\\d+)?")) continue;
                        object = new BigDecimal((String)object22);
                        object3 = ((BigDecimal)object3).add((BigDecimal)object);
                    }
                    hashMap.put(string6, object3);
                }
                list2.add(hashMap);
            }
            httpServletResponse.setContentType("application/vnd.ms-excel");
            iterator = null;
            try {
                String string7 = BrowserUtils.checkBrowse((HttpServletRequest)httpServletRequest);
                if ("MSIE".equalsIgnoreCase(string7.substring(0, 4))) {
                    httpServletResponse.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(string2, "UTF-8") + ".xls");
                } else {
                    object3 = new String(string2.getBytes("UTF-8"), "ISO8859-1");
                    httpServletResponse.setHeader("content-disposition", "attachment;filename=" + (String)object3 + ".xls");
                }
                object3 = ExcelExportUtil.exportExcel((ExportParams)new ExportParams(null, string3), arrayList, (Collection)list2);
                iterator = httpServletResponse.getOutputStream();
                object3.write(iterator);
            }
            catch (Exception exception) {
            }
            finally {
                try {
                    ((OutputStream)((Object)iterator)).flush();
                    ((OutputStream)((Object)iterator)).close();
                }
                catch (Exception exception) {}
            }
        } else {
            throw new JeecgBootException("\u53c2\u6570\u9519\u8bef");
        }
    }

    @GetMapping(value={"/getRpColumns/{code}"})
    public Result<?> d(@PathVariable(value="code") String string) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgreportHead::getCode, (Object)string);
        OnlCgreportHead onlCgreportHead = (OnlCgreportHead)this.onlCgreportHeadService.getOne((Wrapper)lambdaQueryWrapper);
        if (onlCgreportHead == null) {
            return Result.error((String)"\u5b9e\u4f53\u4e0d\u5b58\u5728");
        }
        Map<String, Object> map = this.onlCgreportHeadService.queryColumnInfo(onlCgreportHead.getId(), true);
        map.put("cgRpConfigId", onlCgreportHead.getId());
        map.put("cgRpConfigName", onlCgreportHead.getName());
        return Result.ok(map);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @RequiresPermissions(value={"online:report:testConnection"})
    @PostMapping(value={"/testConnection"})
    public Result a(@RequestBody DynamicDataSourceModel dynamicDataSourceModel) {
        Connection connection = null;
        try {
            JdbcSecurityUtil.validate((String)dynamicDataSourceModel.getDbUrl());
            Class.forName(dynamicDataSourceModel.getDbDriver());
            connection = DriverManager.getConnection(dynamicDataSourceModel.getDbUrl(), dynamicDataSourceModel.getDbUsername(), dynamicDataSourceModel.getDbPassword());
            if (connection != null) {
                Result result = Result.ok((String)"\u6570\u636e\u5e93\u8fde\u63a5\u6210\u529f");
                return result;
            }
            Result result = Result.ok((String)"\u6570\u636e\u5e93\u8fde\u63a5\u5931\u8d25\uff1a\u9519\u8bef\u672a\u77e5");
            return result;
        }
        catch (ClassNotFoundException classNotFoundException) {
            a.error(classNotFoundException.toString());
            Result result = Result.error((String)"\u6570\u636e\u5e93\u8fde\u63a5\u5931\u8d25\uff1a\u9a71\u52a8\u7c7b\u4e0d\u5b58\u5728");
            return result;
        }
        catch (Exception exception) {
            a.error(exception.toString());
            Result result = Result.error((String)("\u6570\u636e\u5e93\u8fde\u63a5\u5931\u8d25\uff1a" + exception.getMessage()));
            return result;
        }
        finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            }
            catch (SQLException sQLException) {
                a.error(sQLException.toString());
            }
        }
    }

    @GetMapping(value={"/getReportDictList"})
    @RequiresPermissions(value={"online:report:getDictList"})
    public Result<?> a(@RequestParam(value="fieldId") String string, @RequestParam(name="keyword", required=false) String string2, @RequestParam(name="pageNo", defaultValue="1") Integer n, @RequestParam(name="pageSize", defaultValue="10") Integer n2) {
        OnlCgreportItem onlCgreportItem = (OnlCgreportItem)this.onlCgreportItemService.getById((Serializable)((Object)string));
        if (onlCgreportItem == null) {
            throw new JeecgBootException("\u6307\u5b9a\u5b57\u6bb5\u4e0d\u5b58\u5728");
        }
        String string3 = onlCgreportItem.getDictCode();
        List<DictModel> list = this.onlCgreportHeadService.queryDictSelectData(string3, string2, n, n2);
        return Result.ok(list);
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getOrderNum": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportParam") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlCgreportParam::getOrderNum;
            }
            case "getCgrheadId": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportParam") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgreportParam::getCgrheadId;
            }
            case "getCode": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportHead") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgreportHead::getCode;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

