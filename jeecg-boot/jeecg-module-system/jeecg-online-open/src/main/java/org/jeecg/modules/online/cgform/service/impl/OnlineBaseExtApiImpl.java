/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  org.jeecg.common.online.api.IOnlineBaseExtApi
 *  org.jeecg.common.system.vo.DictModel
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 */
package org.jeecg.modules.online.cgform.service.impl;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jeecg.common.online.api.IOnlineBaseExtApi;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.online.cgform.d.c;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportAPIService;
import org.jeecg.modules.online.config.exception.a;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="onlineBaseExtApiImpl")
public class OnlineBaseExtApiImpl
implements IOnlineBaseExtApi {
    private static final Logger log = LoggerFactory.getLogger(OnlineBaseExtApiImpl.class);
    @Autowired
    private IOnlCgformHeadService onlCgformHeadService;
    @Autowired
    IOnlCgreportAPIService onlCgreportAPIService;

    public String cgformPostCrazyForm(String tableName, JSONObject jsonObject) throws Exception {
        String string = c.a();
        jsonObject.put("id", (Object)string);
        log.info("[OnlineBaseExtApiImpl] addCrazyFormData payload: table={}, keys={}", tableName, jsonObject.keySet());
        this.onlCgformHeadService.addCrazyFormData(tableName, jsonObject);
        return string;
    }

    public String cgformPutCrazyForm(String tableName, JSONObject jsonObject) throws Exception {
        jsonObject.remove((Object)"create_by");
        jsonObject.remove((Object)"create_time");
        jsonObject.remove((Object)"update_by");
        jsonObject.remove((Object)"update_time");
        log.info("[OnlineBaseExtApiImpl] editCrazyFormData payload: table={}, keys={}, sample={}", tableName, jsonObject.keySet(), jsonObject.toJSONString());
        this.onlCgformHeadService.editCrazyFormData(tableName, jsonObject);
        return jsonObject.getString("id");
    }

    public String cgformDeleteDataByCode(String cgformCode, String dataIds) {
        return this.onlCgformHeadService.deleteDataByCode(cgformCode, dataIds);
    }

    public JSONObject cgformQueryAllDataByTableName(String tableName, String dataIds) {
        try {
            return this.onlCgformHeadService.queryAllDataByTableNameForDesform(tableName, dataIds);
        }
        catch (a a2) {
            log.error("\u67e5\u8be2\u4e3b\u5b50\u8868\u6570\u636e\u5931\u8d25\uff1a", (Throwable)a2);
            return null;
        }
    }

    public Map<String, Object> cgreportGetData(String code, String forceKey, String dataList) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        hashMap.put(forceKey, dataList);
        hashMap.put("getAll", true);
        return this.onlCgreportAPIService.getDataByCode(code, hashMap);
    }

    public List<DictModel> cgreportGetDataPackage(String code, String dictText, String dictCode, String dataList) {
        String string = "force_" + dictCode;
        Map<String, Object> map = this.cgreportGetData(code, string, dataList);
        List list = (List)map.get("records");
        ArrayList<DictModel> arrayList = new ArrayList<DictModel>();
        for (Map map2 : list) {
            String string2 = (String)map2.get(dictCode);
            String string3 = (String)map2.get(dictText);
            arrayList.add(new DictModel(string2, string3));
        }
        return arrayList;
    }
}

