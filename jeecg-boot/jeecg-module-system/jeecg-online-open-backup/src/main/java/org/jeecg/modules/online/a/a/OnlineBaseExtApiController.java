/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  org.jeecg.common.system.vo.DictModel
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package org.jeecg.modules.online.a.a;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Map;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.online.cgform.service.impl.OnlineBaseExtApiImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="onlineBaseExtAPIController")
@RequestMapping(value={"/online/api"})
public class OnlineBaseExtApiController {
    @Autowired
    OnlineBaseExtApiImpl onlineBaseExtApi;

    @PostMapping(value={"/cgform/crazyForm/{name}"})
    String a(@PathVariable(value="name") String string, @RequestBody JSONObject jSONObject) throws Exception {
        return this.onlineBaseExtApi.cgformPostCrazyForm(string, jSONObject);
    }

    @PutMapping(value={"/cgform/crazyForm/{name}"})
    String b(@PathVariable(value="name") String string, @RequestBody JSONObject jSONObject) throws Exception {
        return this.onlineBaseExtApi.cgformPutCrazyForm(string, jSONObject);
    }

    @GetMapping(value={"/cgform/queryAllDataByTableName"})
    JSONObject a(@RequestParam(value="tableName") String string, @RequestParam(value="dataIds") String string2) {
        return this.onlineBaseExtApi.cgformQueryAllDataByTableName(string, string2);
    }

    @DeleteMapping(value={"/cgform/cgformDeleteDataByCode"})
    String b(@RequestParam(value="cgformCode") String string, @RequestParam(value="dataIds") String string2) {
        return this.onlineBaseExtApi.cgformDeleteDataByCode(string, string2);
    }

    @GetMapping(value={"/cgreportGetData"})
    Map<String, Object> a(@RequestParam(value="code") String string, @RequestParam(value="forceKey") String string2, @RequestParam(value="dataList") String string3) {
        return this.onlineBaseExtApi.cgreportGetData(string, string2, string3);
    }

    @GetMapping(value={"/cgreportGetDataPackage"})
    List<DictModel> a(@RequestParam(value="code") String string, @RequestParam(value="dictText") String string2, @RequestParam(value="dictCode") String string3, @RequestParam(value="dataList") String string4) {
        return this.onlineBaseExtApi.cgreportGetDataPackage(string, string2, string3, string4);
    }
}

