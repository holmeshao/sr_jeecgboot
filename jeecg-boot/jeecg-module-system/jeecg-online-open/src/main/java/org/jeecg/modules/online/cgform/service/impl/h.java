/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONException
 *  com.alibaba.fastjson.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.toolkit.Wrappers
 *  org.apache.commons.lang.StringUtils
 *  org.jeecg.chatgpt.dto.chat.MultiChatMessage
 *  org.jeecg.chatgpt.dto.chat.MultiChatMessage$Role
 *  org.jeecg.chatgpt.service.AiChatService
 *  org.jeecg.common.api.vo.Result
 *  org.jeecg.common.exception.JeecgBootBizTipException
 *  org.jeecg.common.util.AssertUtils
 *  org.jeecg.common.util.UUIDGenerator
 *  org.jeecg.common.util.oConvertUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package org.jeecg.modules.online.cgform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import java.lang.invoke.SerializedLambda;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.jeecg.chatgpt.dto.chat.MultiChatMessage;
import org.jeecg.chatgpt.service.AiChatService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootBizTipException;
import org.jeecg.common.util.AssertUtils;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.enums.a;
import org.jeecg.modules.online.cgform.mapper.OnlCgformHeadMapper;
import org.jeecg.modules.online.cgform.service.IOnlCgformAiService;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="onlineCgformAiServiceImpl")
public class h
implements IOnlCgformAiService {
    private static final Logger a = LoggerFactory.getLogger(h.class);
    @Autowired
    AiChatService aiChatService;
    @Autowired
    IOnlCgformHeadService onlCgformHeadService;
    @Autowired
    IOnlCgformFieldService onlCgformFieldService;
    @Autowired
    OnlCgformHeadMapper onlCgformHeadMapper;
    private static final List<String> b = Arrays.asList("String", "Datetime", "BigDecimal", "Date", "Text", "int", "Double");
    private static final List<String> c = Arrays.asList("text", "textarea", "password", "date", "datetime", "time", "file", "image");

    @Override
    public Result<?> genSchema4Modules(String prompt) {
        if (StringUtils.isEmpty((String)prompt)) {
            return Result.error((String)"\u8bf7\u8f93\u5165\u63d0\u793a\u5185\u5bb9");
        }
        String string = "\u4f60\u4f1a\u6839\u636e\u4e1a\u52a1\u9700\u6c42\u8bbe\u8ba1\u4e0e\u9700\u6c42\u76f8\u5173\u7684\u8868\u5355\uff1b\u53ea\u8f93\u51fajson\u6570\u636e\u4e0d\u8981\u6709\u5176\u4ed6\u63cf\u8ff0\u3002\n\u8f93\u51fa\u4e00\u4e2ajson\u5bf9\u8c61\uff0c\u8be5\u5bf9\u8c61\u7531\u8868\u96c6\u5408\uff08tables)\u548c\u5b57\u5178\u6570\u636e\uff08dictData\uff09\u7ec4\u6210\u3002\u8868\u96c6\u5408\u662f\u4e00\u4e2a\u6570\u7ec4\uff0c\u6bcf\u4e2a\u8868\u662f\u4e00\u4e2ajson\u5bf9\u8c61\uff0c\u5c5e\u6027\u5305\u542b\uff1a\u4e2d\u6587\u540d\uff08tableTxt)\u3001\u82f1\u6587\u540d(tableName)\u3001\u5b57\u6bb5\u5217\u8868(fields);\u5b57\u5178\u6570\u636e\u662f\u4e00\u4e2ajson\u5bf9\u8c61\uff0c\u6bd4\u5982\uff1a{\"\u5b57\u5178\u7f16\u7801\":[{\"value\":\"\u5b57\u5178\u503c\",\"text\":\"\u5c55\u793a\u503c\"}]};\u5b57\u6bb5\u5217\u8868\u662f\u4e00\u4e2ajson\u6570\u7ec4\uff0c\u5305\u542b\u5b57\u6bb5\u82f1\u6587\u540d(dbFieldName)\u3001\u5b57\u6bb5\u4e2d\u6587\u540d(dbFieldTxt)\u3001\u5b57\u6bb5\u6570\u636e\u5e93\u7c7b\u578b(dbType)\u3001\u5b57\u6bb5\u6570\u636e\u5e93\u7c7b\u578b\u957f\u5ea6(dbLength),\u5b57\u6bb5\u6570\u636e\u5e93\u5c0f\u6570\u7c7b\u578b\u957f\u5ea6(dbPointLength)\u3001\u5b57\u6bb5\u663e\u793a\u7c7b\u578b(fieldShowType)\u3001\u5b57\u6bb5\u662f\u5426\u5fc5\u586b(fieldMustInput)\u3001\u5b57\u6bb5\u662f\u5426\u67e5\u8be2\u5b57\u6bb5(isQuery)\u3002\u5b57\u6bb5\u82f1\u6587\u540d\u548c\u8868\u82f1\u6587\u540d\u4f7f\u7528\u4e0b\u5212\u7ebf\u547d\u540d\u6cd5(UnderScoreCase);\u4e0d\u8981\u751f\u6210\u5f53\u524d\u8868\u7684\u4e3b\u952e\u5b57\u6bb5\u6216ID\u5b57\u6bb5;\u53ef\u7528\u7684\u5b57\u6bb5\u6570\u636e\u5e93\u7c7b\u578b(dbType)\u5305\u542b\uff1aString\u3001Datetime\u3001BigDecimal\u3001Date\u3001Text\u3001int\u3001Double\u3002\u53ef\u7528\u7684\u5b57\u6bb5\u663e\u793a\u7c7b\u578b(fieldShowType)\u5305\u542b\uff1atext\u3001textarea\u3001password\u3001date\u3001datetime\u3001time\u3001file\u3001image\u3002\u5b57\u6bb5\u6570\u636e\u5e93\u7c7b\u578b(dbType)\u548c\u5b57\u6bb5\u663e\u793a\u7c7b\u578b(fieldShowType)\u53ea\u80fd\u4f7f\u7528\u4e0a\u8ff0\u9009\u9879\u3002\u5bc6\u7801\u7684\u663e\u793a\u7c7b\u578b\u662f:password\u53c2\u8003json\uff1a{\"tables\":[{\"tableName\":\"order\",\"tableTxt\":\"\u8ba2\u5355\u8868\",\"fields\":[{\"dbFieldName\":\"name\",\"dbFieldTxt\":\"\u59d3\u540d\",\"dbType\":\"string\",\"dbLength\":20,\"dbPointLength\":0,\"fieldShowType\":\"input\",\"fieldMustInput\":\"1\",\"isQuery\":0}]}]}\u3002";
        String string2 = this.a(string, "\u4e1a\u52a1\u9700\u6c42\u5982\u4e0b:" + prompt);
        JSONObject jSONObject = null;
        try {
            jSONObject = JSONArray.parseObject((String)string2);
        }
        catch (JSONException jSONException) {
            throw new JeecgBootBizTipException("ai\u5f00\u5c0f\u5dee\u4e86,\u8bf7\u7a0d\u540e\u518d\u8bd5.");
        }
        JSONArray jSONArray = jSONObject.getJSONArray("tables");
        if (null != jSONArray && !jSONArray.isEmpty()) {
            List<Object> list = new ArrayList();
            for (Object e2 : jSONArray) {
                if (e2 == null) continue;
                JSONObject jSONObject2 = (JSONObject)e2;
                list = this.a(jSONObject2);
            }
            list.forEach(this.onlCgformHeadService::addAll);
        }
        return Result.ok((String)"\u751f\u6210\u6210\u529f");
    }

    private List<org.jeecg.modules.online.cgform.model.a> a(JSONObject jSONObject2) {
        ArrayList<org.jeecg.modules.online.cgform.model.a> arrayList = new ArrayList<org.jeecg.modules.online.cgform.model.a>();
        List list = b.stream().map(String::toLowerCase).collect(Collectors.toList());
        List list2 = c.stream().map(String::toLowerCase).collect(Collectors.toList());
        org.jeecg.modules.online.cgform.model.a a2 = new org.jeecg.modules.online.cgform.model.a();
        OnlCgformHead onlCgformHead = this.b(jSONObject2);
        String string = onlCgformHead.getTableName();
        if (null == string || string.isEmpty()) {
            return arrayList;
        }
        onlCgformHead.setTableName(this.a(string, (Integer)null));
        a2.setHead(onlCgformHead);
        a2.setIndexs(Collections.emptyList());
        a2.setDeleteFieldIds(Collections.emptyList());
        a2.setDeleteIndexIds(Collections.emptyList());
        arrayList.add(a2);
        ArrayList<OnlCgformField> arrayList2 = new ArrayList<OnlCgformField>();
        a2.setFields(arrayList2);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        OnlCgformField onlCgformField = this.a("id", "\u4e3b\u952e", "text", "string", true, true, atomicInteger.getAndIncrement());
        onlCgformField.setDbIsKey(1);
        arrayList2.add(onlCgformField);
        arrayList2.add(this.a("create_by", "\u521b\u5efa\u4eba", "text", "string", false, false, atomicInteger.getAndIncrement()));
        arrayList2.add(this.a("create_time", "\u521b\u5efa\u65e5\u671f", "datetime", "Datetime", false, false, atomicInteger.getAndIncrement()));
        arrayList2.add(this.a("update_by", "\u66f4\u65b0\u4eba", "text", "string", false, false, atomicInteger.getAndIncrement()));
        arrayList2.add(this.a("update_time", "\u66f4\u65b0\u65e5\u671f", "datetime", "Datetime", false, false, atomicInteger.getAndIncrement()));
        ArrayList<String> arrayList3 = new ArrayList<String>(Arrays.asList("id", "create_by", "create_time", "update_by", "update_time"));
        JSONArray jSONArray = jSONObject2.getJSONArray("fields");
        jSONArray.stream().filter(Objects::nonNull).map(object -> (JSONObject)object).forEach(jSONObject -> {
            OnlCgformField onlCgformField = (OnlCgformField)jSONObject.toJavaObject(OnlCgformField.class);
            if (null == onlCgformField.getDbFieldName() || onlCgformField.getDbFieldName().isEmpty()) {
                return;
            }
            if (onlCgformField.getDbFieldName().trim().equalsIgnoreCase("id")) {
                return;
            }
            if (arrayList3.contains(onlCgformField.getDbFieldName().toLowerCase().trim())) {
                return;
            }
            onlCgformField.setDbFieldName(h.a(onlCgformField.getDbFieldName()));
            if (null != onlCgformField.getDbType() && !onlCgformField.getDbType().isEmpty() && list.contains(onlCgformField.getDbType().toLowerCase().trim())) {
                onlCgformField.setDbType(b.get(list.indexOf(onlCgformField.getDbType().toLowerCase().trim())));
            } else {
                onlCgformField.setDbType("String");
            }
            if (null != onlCgformField.getFieldShowType() && !onlCgformField.getFieldShowType().isEmpty() && list2.contains(onlCgformField.getFieldShowType().toLowerCase().trim())) {
                onlCgformField.setFieldShowType(c.get(list2.indexOf(onlCgformField.getFieldShowType().toLowerCase().trim())));
            } else {
                onlCgformField.setFieldShowType("text");
            }
            this.a(onlCgformField, atomicInteger.getAndIncrement());
            arrayList2.add(onlCgformField);
            arrayList3.add(onlCgformField.getDbFieldName().toLowerCase());
        });
        return arrayList;
    }

    @Override
    public Result<?> genSingleSchema4Modules(String prompt) {
        Object object;
        String string = "\u4e25\u683c\u6309\u7167\u53c2\u8003json\u6570\u7ec4\u7684\u683c\u5f0f\u8f93\u51fa\uff0c\u4e0d\u8981\u6709\u5176\u4ed6\u4efb\u4f55\u63cf\u8ff0\uff0c\u5e94\u4ee5[\u5f00\u5934\uff0c\u4ee5]\u7ed3\u5c3e\n\u6839\u636e\u6211\u7684\u4e1a\u52a1\u9700\u6c42\u5e2e\u6211\u751f\u6210\u4e00\u4e2a\u4e1a\u52a1\u8868\u5355\uff0c\u8868\u5355\u53c2\u8003JSON\u5982\u4e0b\uff1a[\"student\",\"\u5b66\u751f\u8868\",[[\"name\",\"\u59d3\u540d\",\"String\",50,0,\"text\"]]]\n\u5728\u8be5JSON\u6570\u7ec4\u4e2d\uff1a\n- \u4e0b\u68070\u4e3a\u8868\u5355\u7684\u82f1\u6587code\uff0c\u4f7f\u7528\u4e0b\u5212\u7ebf\u547d\u540d\u6cd5\n- \u4e0b\u68071\u4e3a\u4e2d\u6587\u540d\u79f0\n- \u4e0b\u68072\u4e3a\u5b57\u6bb5\u6570\u7ec4\n    - \u4e0b\u68070\u4e3a\u5b57\u6bb5\u7684\u82f1\u6587code\uff0c\u4f7f\u7528\u4e0b\u5212\u7ebf\u547d\u540d\u6cd5\n    - \u4e0b\u68071\u4e3a\u4e2d\u6587\u540d\u79f0\n    - \u4e0b\u68072\u4e3a\u5b57\u6bb5\u7c7b\u578b\uff0c\u9650\u5b9a\u4e3a\uff1aString\u3001Datetime\u3001BigDecimal\u3001Date\u3001Text\u3001int\u3001Double\n    - \u4e0b\u68073\u4e3a\u5b57\u6bb5\u957f\u5ea6\n    - \u4e0b\u68074\u4e3a\u63a7\u4ef6\u7c7b\u578b\uff0c\u9650\u5b9a\u4e3a\uff1atext\u3001textarea\u3001password\u3001date\u3001datetime\u3001time\u3001file\u3001image\n\u7279\u522b\u6ce8\u610f\uff1a\n- \u7981\u6b62\u751f\u6210\u4e3b\u952e\u6216 ID \u5b57\u6bb5\u3002\n- \u7981\u6b62\u751f\u6210\u4e2d\u6587\u6807\u70b9\u548c\u5f15\u53f7\u3002\n- \u81f3\u5c11\u751f\u6210\u4e00\u4e2a\u5b57\u6bb5\u3002";
        JSONObject jSONObject = new JSONObject();
        try {
            object = this.a(string, "\u4e1a\u52a1\u9700\u6c42\u5982\u4e0b:" + prompt);
            JSONArray jSONArray = JSONArray.parseArray((String)object);
            jSONObject.put("tableName", jSONArray.get(0));
            jSONObject.put("tableTxt", jSONArray.get(1));
            JSONArray jSONArray2 = new JSONArray();
            jSONObject.put("fields", (Object)jSONArray2);
            JSONArray jSONArray3 = jSONArray.getJSONArray(2);
            if (null == jSONArray3 || jSONArray3.isEmpty()) {
                return Result.error((String)"AI\u5f00\u5c0f\u5dee\u4e86,\u8bf7\u7a0d\u540e\u518d\u8bd5");
            }
            for (Object e2 : jSONArray3) {
                JSONArray jSONArray4 = (JSONArray)e2;
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("dbFieldName", jSONArray4.get(0));
                jSONObject2.put("dbFieldTxt", jSONArray4.get(1));
                jSONObject2.put("dbType", jSONArray4.get(2));
                jSONObject2.put("dbLength", jSONArray4.get(3));
                jSONObject2.put("fieldShowType", jSONArray4.get(4));
                jSONArray2.add((Object)jSONObject2);
            }
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)"AI\u5f00\u5c0f\u5dee\u4e86,\u8bf7\u7a0d\u540e\u518d\u8bd5");
        }
        object = this.a(jSONObject);
        object.forEach(this.onlCgformHeadService::addAll);
        return Result.ok((String)"\u751f\u6210\u6210\u529f");
    }

    @Override
    public Result<?> aiGenFields(String code, String prompt) {
        JSONArray jSONArray;
        String string;
        Object object;
        AssertUtils.assertNotEmpty((String)prompt, (Object)"\u4e1a\u52a1\u9700\u6c42\u4e0d\u80fd\u4e3a\u7a7a");
        List<Object> list = new ArrayList();
        if (oConvertUtils.isNotEmpty((Object)code)) {
            try {
                object = this.onlCgformHeadService.getTable(code);
                string = ((OnlCgformHead)object).getTableName();
                AssertUtils.assertNotEmpty((String)string, (Object)"\u8868\u540d\u4e0d\u5b58\u5728");
                jSONArray = this.onlCgformFieldService.queryFormFieldsByTableName(string);
                if (oConvertUtils.isObjectNotEmpty(jSONArray)) {
                    list = jSONArray.stream().map(OnlCgformField::getDbFieldName).collect(Collectors.toList());
                }
            }
            catch (org.jeecg.modules.online.config.exception.a a2) {
                throw new JeecgBootBizTipException("\u83b7\u53d6\u8868\u4fe1\u606f\u5931\u8d25");
            }
        }
        object = "\u4e25\u683c\u6309\u7167\u53c2\u8003json\u6570\u7ec4\u7684\u683c\u5f0f\u8f93\u51fa\uff0c\u4e0d\u8981\u6709\u5176\u4ed6\u4efb\u4f55\u63cf\u8ff0\uff0c\u5e94\u4ee5[\u5f00\u5934\uff0c\u4ee5]\u7ed3\u5c3e\n\u6839\u636e\u6211\u7684\u4e1a\u52a1\u9700\u6c42\u4ee5\u53ca\u73b0\u6709\u7684\u5b57\u6bb5\u5e2e\u6211\u5efa\u8bae\u4e00\u5957\u4e1a\u52a1\u5b57\u6bb5\uff0c\u8868\u5355\u53c2\u8003JSON\u5982\u4e0b\uff1a[[\"name\",\"\u59d3\u540d\",\"String\",50,0,\"text\"]]\n\u5728\u8be5JSON\u6570\u7ec4\u4e2d\uff1a\n- \u4e0b\u68070\u4e3a\u5b57\u6bb5\u7684\u82f1\u6587code\uff0c\u4f7f\u7528\u4e0b\u5212\u7ebf\u547d\u540d\u6cd5\n- \u4e0b\u68071\u4e3a\u4e2d\u6587\u540d\u79f0\n- \u4e0b\u68072\u4e3a\u5b57\u6bb5\u7c7b\u578b\uff0c\u9650\u5b9a\u4e3a\uff1aString\u3001Datetime\u3001BigDecimal\u3001Date\u3001Text\u3001int\u3001Double\n- \u4e0b\u68073\u4e3a\u5b57\u6bb5\u957f\u5ea6\n- \u4e0b\u68074\u4e3a\u63a7\u4ef6\u7c7b\u578b\uff0c\u9650\u5b9a\u4e3a\uff1atext\u3001textarea\u3001password\u3001date\u3001datetime\u3001time\u3001file\u3001image\n\u4ee5\u4e0b\u5b57\u6bb5\u5df2\u7ecf\u5b58\u5728,\u4e0d\u8981\u91cd\u590d\u751f\u6210:\n%s\n\u7279\u522b\u6ce8\u610f\uff1a\n- \u7981\u6b62\u751f\u6210\u4e3b\u952e\u6216 ID \u5b57\u6bb5\u3002\n- \u7981\u6b62\u751f\u6210\u4e2d\u6587\u6807\u70b9\u548c\u5f15\u53f7\u3002\n- \u81f3\u5c11\u751f\u6210\u4e09\u4e2a\u5b57\u6bb5\u3002";
        object = String.format((String)object, JSON.toJSONString(list));
        string = this.a((String)object, "\u4e1a\u52a1\u9700\u6c42\u5982\u4e0b:" + prompt);
        try {
            jSONArray = JSONArray.parseArray((String)string);
        }
        catch (JSONException jSONException) {
            throw new JeecgBootBizTipException("AI\u5f00\u5c0f\u5dee\u4e86,\u8bf7\u7a0d\u540e\u518d\u8bd5");
        }
        if (jSONArray == null || jSONArray.isEmpty()) {
            a.error("[AIGC]\u751f\u6210\u5b57\u6bb5:\u672a\u80fd\u751f\u6210\u6709\u6548\u5b57\u6bb5");
            return Result.error((String)"AI\u5f00\u5c0f\u5dee\u4e86,\u8bf7\u7a0d\u540e\u518d\u8bd5");
        }
        ArrayList<OnlCgformField> arrayList = new ArrayList<OnlCgformField>();
        AtomicInteger atomicInteger = new AtomicInteger(100);
        for (Object e2 : jSONArray) {
            String string2;
            JSONArray jSONArray2 = (JSONArray)e2;
            if (jSONArray2.size() < 6 || list.contains(string2 = h.a(jSONArray2.getString(0)))) continue;
            list.add(string2);
            OnlCgformField onlCgformField = new OnlCgformField();
            onlCgformField.setDbFieldName(string2);
            onlCgformField.setDbFieldTxt(jSONArray2.getString(1));
            onlCgformField.setDbType(jSONArray2.getString(2));
            onlCgformField.setDbLength(jSONArray2.getInteger(3));
            onlCgformField.setDbPointLength(jSONArray2.getInteger(4));
            onlCgformField.setFieldShowType(jSONArray2.getString(5));
            this.a(onlCgformField, atomicInteger.getAndIncrement());
            onlCgformField.setId(null);
            arrayList.add(onlCgformField);
        }
        if (arrayList.isEmpty()) {
            a.error("[AIGC]\u751f\u6210\u5b57\u6bb5:\u672a\u80fd\u751f\u6210\u6709\u6548\u5b57\u6bb5");
            return Result.error((String)"AI\u5f00\u5c0f\u5dee\u4e86,\u8bf7\u7a0d\u540e\u518d\u8bd5");
        }
        return Result.OK((String)"\u751f\u6210\u6210\u529f", arrayList);
    }

    @Override
    public Result<?> aiGenMockData(String code, Integer count) {
        JSONArray jSONArray;
        String string;
        Object object2;
        AssertUtils.assertNotEmpty((String)code, (Object)"\u8868code\u4e0d\u80fd\u4e3a\u7a7a");
        if (count == null || count <= 0) {
            count = 3;
        }
        List<String> list = Arrays.asList("id", "create_by", "create_time", "update_by", "update_time");
        String string2 = "";
        try {
            object2 = this.onlCgformHeadService.getTable(code);
            string = ((OnlCgformHead)object2).getTableName();
            AssertUtils.assertNotEmpty((String)string, (Object)"\u8868\u540d\u4e0d\u5b58\u5728");
            jSONArray = new JSONArray();
            jSONArray.add((Object)string);
            jSONArray.add((Object)((OnlCgformHead)object2).getTableTxt());
            JSONArray jSONArray2 = new JSONArray();
            jSONArray.add((Object)jSONArray2);
            List<OnlCgformField> list2 = this.onlCgformFieldService.queryFormFieldsByTableName(string);
            if (oConvertUtils.isObjectNotEmpty(list2)) {
                for (OnlCgformField onlCgformField : list2) {
                    JSONArray jSONArray3 = new JSONArray();
                    String string3 = onlCgformField.getDbFieldName();
                    if (list.contains(string3)) continue;
                    jSONArray3.add((Object)string3);
                    jSONArray3.add((Object)onlCgformField.getDbFieldTxt());
                    jSONArray3.add((Object)onlCgformField.getDbType());
                    jSONArray3.add((Object)onlCgformField.getDbLength());
                    jSONArray3.add((Object)onlCgformField.getDbPointLength());
                    jSONArray3.add((Object)onlCgformField.getFieldShowType());
                    jSONArray2.add((Object)jSONArray3);
                }
            }
            string2 = jSONArray.toJSONString();
        }
        catch (org.jeecg.modules.online.config.exception.a a2) {
            throw new JeecgBootBizTipException("\u83b7\u53d6\u8868\u4fe1\u606f\u5931\u8d25");
        }
        object2 = "\u4e25\u683c\u6309\u7167\u53c2\u8003json\u6570\u7ec4\u7684\u683c\u5f0f\u8f93\u51fa\uff0c\u4e0d\u8981\u6709\u5176\u4ed6\u4efb\u4f55\u63cf\u8ff0\uff0c\u5e94\u4ee5[\u5f00\u5934\uff0c\u4ee5]\u7ed3\u5c3e \n\u6839\u636e\u6211\u7684\u8868\u8bbe\u8ba1\uff0c\u751f\u6210\u6d4b\u8bd5\u6570\u636e\uff0c\u53c2\u8003json\u5982\u4e0b\uff1a[{\"fieldName\",\"val\"},{\"fieldName\",\"val\"}] \n\u8868\u8bbe\u8ba1\u683c\u5f0f\u8bf4\u660e: \n- \u4e0b\u68070\u4e3a\u8868\u5355\u7684\u82f1\u6587code\uff0c\u4f7f\u7528\u4e0b\u5212\u7ebf\u547d\u540d\u6cd5 \n- \u4e0b\u68071\u4e3a\u4e2d\u6587\u540d\u79f0 \n- \u4e0b\u68072\u4e3a\u5b57\u6bb5\u6570\u7ec4 \n    - \u4e0b\u68070\u4e3a\u5b57\u6bb5\u7684\u82f1\u6587code\uff0c\u4f7f\u7528\u4e0b\u5212\u7ebf\u547d\u540d\u6cd5 \n    - \u4e0b\u68071\u4e3a\u4e2d\u6587\u540d\u79f0 \n    - \u4e0b\u68072\u4e3a\u5b57\u6bb5\u7c7b\u578b\uff0c\u9650\u5b9a\u4e3a\uff1aString\u3001Datetime\u3001BigDecimal\u3001Date\u3001Text\u3001int\u3001Double \n    - \u4e0b\u68073\u4e3a\u5b57\u6bb5\u957f\u5ea6 \n    - \u4e0b\u68074\u4e3a\u63a7\u4ef6\u7c7b\u578b\uff0c\u9650\u5b9a\u4e3a\uff1atext\u3001textarea\u3001password\u3001date\u3001datetime\u3001time\u3001file\u3001image \n\u8981\u6c42\uff1a \n- \u751f\u6210" + count + "\u6761\u4ee5\u4e0a\u6570\u636e\u3002 \n- \u6570\u636e\u7c7b\u578b\u8981\u4e0e\u5b57\u6bb5\u8bbe\u8ba1\u76f8\u5339\u914d\u3002 \n- date\u63a7\u4ef6\u8981\u751f\u6210\u65e5\u671f\u683c\u5f0f(2023-10-01),datetime\u63a7\u4ef6\u751f\u6210\u65e5\u671f\u65f6\u95f4(2023-10-01 10:00:00),time\u63a7\u4ef6\u751f\u6210\u65f6\u95f4(10:00:00)- \u751f\u6210\u7684\u6570\u636e\u8981\u4e0e\u8868\u8bbe\u8ba1\u7684\u4e1a\u52a1\u76f8\u5173\u3002 ";
        string = this.a((String)object2, "\u8868\u8bbe\u8ba1\u5982\u4e0b\uff1a \n" + string2);
        a.info(string);
        try {
            jSONArray = JSONArray.parseArray((String)string);
        }
        catch (JSONException jSONException) {
            a.error("[AIGC]\u751f\u6210\u6d4b\u8bd5\u6570\u636e,\u672a\u80fd\u751f\u6210\u6709\u6548\u6570\u636e");
            throw new JeecgBootBizTipException("AI\u5f00\u5c0f\u5dee\u4e86,\u8bf7\u7a0d\u540e\u518d\u8bd5");
        }
        if (jSONArray == null || jSONArray.isEmpty()) {
            a.error("[AIGC]\u751f\u6210\u6d4b\u8bd5\u6570\u636e,\u672a\u80fd\u751f\u6210\u6709\u6548\u6570\u636e");
            return Result.error((String)"AI\u5f00\u5c0f\u5dee\u4e86,\u8bf7\u7a0d\u540e\u518d\u8bd5");
        }
        jSONArray.stream().filter(Objects::nonNull).map(object -> (JSONObject)object).forEach(jSONObject -> {
            try {
                String string2 = this.onlCgformHeadService.saveManyFormData(code, (JSONObject)jSONObject, null);
            }
            catch (BusinessException | org.jeecg.modules.online.config.exception.a exception) {
                throw new RuntimeException(exception);
            }
        });
        return Result.ok((String)"\u751f\u6210\u6210\u529f");
    }

    private OnlCgformField a(String string, String string2, String string3, String string4, boolean bl, boolean bl2, int n) {
        OnlCgformField onlCgformField = this.a(new OnlCgformField(), n);
        string = h.a(string);
        onlCgformField.setDbFieldName(string);
        onlCgformField.setDbFieldTxt(string2);
        onlCgformField.setFieldMustInput(bl ? "1" : "0");
        onlCgformField.setIsShowForm(0);
        onlCgformField.setIsShowList(0);
        onlCgformField.setIsReadOnly(bl2 ? 1 : 0);
        onlCgformField.setFieldShowType(string3);
        onlCgformField.setIsQuery(0);
        onlCgformField.setDbPointLength(0);
        onlCgformField.setDbType(string4);
        onlCgformField.setDbIsNull(bl ? 0 : 1);
        return onlCgformField;
    }

    private OnlCgformField a(OnlCgformField onlCgformField, int n) {
        onlCgformField.setId(UUIDGenerator.generate());
        h.a(onlCgformField);
        if (null == onlCgformField.getDbPointLength()) {
            onlCgformField.setDbPointLength(0);
        }
        onlCgformField.setFieldLength(120);
        onlCgformField.setQueryConfigFlag("0");
        onlCgformField.setQueryMode("single");
        onlCgformField.setOrderNum(n);
        onlCgformField.setIsReadOnly(0);
        onlCgformField.setIsShowForm(1);
        onlCgformField.setIsShowList(1);
        onlCgformField.setDbIsNull(1);
        onlCgformField.setDbIsKey(0);
        if (null == onlCgformField.getIsQuery()) {
            onlCgformField.setIsQuery(0);
        }
        if (null == onlCgformField.getFieldMustInput()) {
            onlCgformField.setFieldMustInput("0");
        }
        return onlCgformField;
    }

    private static void a(OnlCgformField onlCgformField) {
        if (null == onlCgformField.getDbLength() || 0 == onlCgformField.getDbLength() || onlCgformField.getDbLength() < 0) {
            String string = onlCgformField.getDbType();
            if (string != null) {
                if ("int".equals(string = string.toLowerCase()) || "double".equals(string) || "bigdecimal".equals(string) || "integer".equals(string) || "decimal".equals(string)) {
                    onlCgformField.setDbLength(10);
                } else if ("string".equals(string) || "password".equals(string)) {
                    onlCgformField.setDbLength(32);
                } else if ("datetime".equals(string) || "date".equals(string)) {
                    onlCgformField.setDbLength(0);
                } else if ("text".equals(string) || "blob".equals(string) || "image".equals(string)) {
                    onlCgformField.setDbLength(0);
                } else {
                    onlCgformField.setDbLength(50);
                }
            } else {
                onlCgformField.setDbLength(50);
            }
        }
    }

    public static String a(String string) {
        int n = 3;
        if (null == string) {
            return string;
        }
        if (string.length() < n) {
            return string.toLowerCase();
        }
        StringBuilder stringBuilder = new StringBuilder(string);
        int n2 = 0;
        for (int i2 = 2; i2 < string.length(); ++i2) {
            if (!Character.isUpperCase(string.charAt(i2))) continue;
            stringBuilder.insert(i2 + n2, "_");
            ++n2;
        }
        return stringBuilder.toString().toLowerCase();
    }

    private OnlCgformHead b(JSONObject jSONObject) {
        OnlCgformHead onlCgformHead = new OnlCgformHead();
        onlCgformHead.setTableName(jSONObject.getString("tableName"));
        onlCgformHead.setTableName(h.a(onlCgformHead.getTableName()));
        onlCgformHead.setTableTxt(jSONObject.getString("tableTxt"));
        onlCgformHead.setTableVersion(1);
        onlCgformHead.setTableType(org.jeecg.modules.online.cgform.enums.a.d);
        onlCgformHead.setFormCategory("temp");
        onlCgformHead.setIdType("UUID");
        onlCgformHead.setIsCheckbox("Y");
        onlCgformHead.setThemeTemplate("normal");
        onlCgformHead.setFormTemplate("1");
        onlCgformHead.setScroll(1);
        onlCgformHead.setIsPage("Y");
        onlCgformHead.setIsTree("N");
        onlCgformHead.setExtConfigJson("{\"reportPrintShow\":0,\"reportPrintUrl\":\"\",\"joinQuery\":0,\"modelFullscreen\":0,\"modalMinWidth\":\"\",\"commentStatus\":0,\"tableFixedAction\":1,\"tableFixedActionType\":\"right\"}");
        onlCgformHead.setIsDesForm("N");
        onlCgformHead.setDesFormCode("");
        return onlCgformHead;
    }

    private String a(String string, Integer n) {
        String string2 = string;
        if (null == n || n < 0) {
            n = 0;
        } else {
            Integer n2 = n;
            Integer n3 = n = Integer.valueOf(n + 1);
            string = string + "_" + n;
        }
        if (null == string || string.isEmpty()) {
            return "ai_table_" + System.currentTimeMillis();
        }
        if (this.onlCgformHeadMapper.exists((Wrapper)Wrappers.lambdaQuery(OnlCgformHead.class).eq(OnlCgformHead::getTableName, (Object)string))) {
            return this.a(string2, n);
        }
        return string;
    }

    private String a(String string, String string2) {
        Object object;
        LinkedList<MultiChatMessage> linkedList = new LinkedList<MultiChatMessage>();
        linkedList.add(MultiChatMessage.builder().role(MultiChatMessage.Role.SYSTEM).content(string).build());
        linkedList.add(MultiChatMessage.builder().role(MultiChatMessage.Role.USER).content(string2).build());
        Object object2 = this.aiChatService.multiCompletions(linkedList);
        if (StringUtils.isEmpty((String)object2)) {
            throw new JeecgBootBizTipException("\u5982\u679c\u60a8\u60f3\u4f7f\u7528AI\u52a9\u624b\uff0c\u8bf7\u5148\u8bbe\u7f6e\u76f8\u5e94\u914d\u7f6e!");
        }
        a.debug("ai\u8fd4\u56de\u7ed3\u679c" + (String)object2);
        if (((String)object2).contains("</think>")) {
            object = ((String)object2).split("</think>");
            object2 = object[((String[])object).length - 1];
        }
        object = Pattern.compile("\\{.*}|\\[.*]", 32);
        Matcher matcher = ((Pattern)object).matcher((CharSequence)object2);
        String string3 = "";
        if (matcher.find()) {
            string3 = matcher.group(0);
        }
        return string3;
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getTableName": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformHead::getTableName;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

