/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  org.apache.commons.lang.StringUtils
 *  org.apache.shiro.SecurityUtils
 *  org.jeecg.common.system.api.ISysBaseAPI
 *  org.jeecg.common.system.vo.DictModel
 *  org.jeecg.common.system.vo.LoginUser
 *  org.jeecg.common.util.oConvertUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.cache.annotation.Cacheable
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.stereotype.Service
 */
package org.jeecg.modules.online.cgform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.auth.service.IOnlAuthPageService;
import org.jeecg.modules.online.cgform.a.a;
import org.jeecg.modules.online.cgform.b.b;
import org.jeecg.modules.online.cgform.d.c;
import org.jeecg.modules.online.cgform.d.d;
import org.jeecg.modules.online.cgform.entity.OnlCgformButton;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceJs;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.model.HrefSlots;
import org.jeecg.modules.online.cgform.model.OnlColumn;
import org.jeecg.modules.online.cgform.model.TreeSelectColumn;
import org.jeecg.modules.online.cgform.model.g;
import org.jeecg.modules.online.cgform.service.IOnlCgformButtonService;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.cgform.service.IOnlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service(value="onlineService")
public class j
implements IOnlineService {
    private static final Logger a = LoggerFactory.getLogger(j.class);
    @Autowired
    private IOnlCgformFieldService onlCgformFieldService;
    @Autowired
    private IOnlCgformHeadService onlCgformHeadService;
    @Autowired
    private IOnlCgformButtonService onlCgformButtonService;
    @Lazy
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private IOnlAuthPageService onlAuthPageService;

    @Override
    public org.jeecg.modules.online.cgform.model.b queryOnlineConfig(OnlCgformHead head, String username) {
        Object object;
        Object object2;
        Object object4;
        Object object5;
        String string = head.getId();
        boolean bl = c.a(head);
        List<OnlCgformField> list = this.b(string);
        List<String> list2 = this.onlAuthPageService.queryHideCode(string, true);
        ArrayList<OnlColumn> arrayList = new ArrayList<OnlColumn>();
        HashMap<String, List<DictModel>> hashMap = new HashMap<String, List<DictModel>>(5);
        ArrayList<HrefSlots> arrayList2 = new ArrayList<HrefSlots>();
        ArrayList<org.jeecg.modules.online.cgform.model.c> arrayList3 = new ArrayList<org.jeecg.modules.online.cgform.model.c>();
        ArrayList<String> arrayList4 = new ArrayList<String>();
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>(5);
        List<String> list3 = head.getSelectFieldList();
        List<OnlColumn> list4 = list.iterator();
        while (list4.hasNext()) {
            object5 = list4.next();
            object4 = ((OnlCgformField)object5).getDbFieldName();
            String object32 = ((OnlCgformField)object5).getMainTable();
            object2 = ((OnlCgformField)object5).getMainField();
            if (oConvertUtils.isNotEmpty((Object)object2) && oConvertUtils.isNotEmpty((Object)object32)) {
                object = new org.jeecg.modules.online.cgform.model.c((String)object4, (String)object2);
                arrayList3.add((org.jeecg.modules.online.cgform.model.c)object);
            }
            if (((OnlCgformField)object5).getIsShowList() == null || 1 != ((OnlCgformField)object5).getIsShowList() || "id".equals(object4) || list2.contains(object4) || arrayList4.contains(object4) || list3 != null && list3.size() > 0 && list3.indexOf(object4) < 0) continue;
            object = this.a((OnlCgformField)object5, hashMap, arrayList2);
            hashMap2.put(((OnlCgformField)object5).getDbFieldName(), 1);
            arrayList.add((OnlColumn)object);
            String string2 = ((OnlColumn)object).getLinkField();
            if (string2 == null || "".equals(string2)) continue;
            this.a(list, (List<String>)arrayList4, (List<OnlColumn>)arrayList, (String)object4, string2);
        }
        this.a(arrayList, arrayList4);
        if (bl && (list4 = this.a(head, hashMap, arrayList2, hashMap2)).size() > 0) {
            object5 = new ArrayList();
            for (String string3 : hashMap2.keySet()) {
                if ((Integer)hashMap2.get(string3) <= 1) continue;
                object5.add(string3);
            }
            for (OnlColumn onlColumn : list4) {
                object2 = onlColumn.getDataIndex();
                if (object5.contains(object2)) {
                    onlColumn.setDataIndex(c.l(onlColumn.getTableName()) + "_" + (String)object2);
                }
                arrayList.add(onlColumn);
            }
        }
        list4 = new org.jeecg.modules.online.cgform.model.b();
        ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setCode(string);
        ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setTableType(head.getTableType());
        ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setFormTemplate(head.getFormTemplate());
        ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setDescription(head.getTableTxt());
        ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setCurrentTableName(head.getTableName());
        ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setPaginationFlag(head.getIsPage());
        ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setCheckboxFlag(head.getIsCheckbox());
        ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setScrollFlag(head.getScroll());
        ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setRelationType(head.getRelationType());
        ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setColumns(arrayList);
        ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setDictOptions(hashMap);
        ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setFieldHrefSlots(arrayList2);
        ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setForeignKeys(arrayList3);
        ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setHideColumns(list2);
        object5 = this.onlCgformHeadService.queryButtonList(string, true);
        object4 = new ArrayList();
        Iterator iterator = object5.iterator();
        while (iterator.hasNext()) {
            object2 = (OnlCgformButton)iterator.next();
            if (list2.contains(((OnlCgformButton)object2).getButtonCode())) continue;
            object4.add(object2);
        }
        List<OnlCgformButton> list5 = this.onlCgformButtonService.queryBuiltInButtonList(string);
        object4.addAll(list5);
        ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setCgButtonList((List<OnlCgformButton>)object4);
        object2 = this.onlCgformHeadService.queryEnhanceJs(string, "list");
        if (object2 != null && oConvertUtils.isNotEmpty((Object)((OnlCgformEnhanceJs)object2).getCgJs())) {
            object = d.b(((OnlCgformEnhanceJs)object2).getCgJs(), (List<OnlCgformButton>)object5);
            ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setEnhanceJs((String)object);
        }
        if ("Y".equals(head.getIsTree())) {
            ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setPidField(head.getTreeParentIdField());
            ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setHasChildrenField(head.getTreeIdField());
            ((org.jeecg.modules.online.cgform.model.b)((Object)list4)).setTextField(head.getTreeFieldname());
        }
        return list4;
    }

    private void a(List<OnlColumn> list, List<String> list2) {
        Iterator<OnlColumn> iterator = list.iterator();
        while (iterator.hasNext()) {
            OnlColumn onlColumn = iterator.next();
            String string = onlColumn.getDataIndex();
            if (list2 == null || list2.indexOf(string) < 0 || !oConvertUtils.isEmpty((Object)onlColumn.getCustomRender())) continue;
            iterator.remove();
        }
    }

    private String[] a(String string) {
        String[] stringArray = new String[]{"", ""};
        if (string != null && !"".equals(string)) {
            JSONObject jSONObject = JSON.parseObject((String)string);
            if (jSONObject.containsKey((Object)"store")) {
                stringArray[0] = oConvertUtils.camelToUnderline((String)jSONObject.getString("store"));
            }
            if (jSONObject.containsKey((Object)"text")) {
                stringArray[1] = oConvertUtils.camelToUnderline((String)jSONObject.getString("text"));
            }
        }
        return stringArray;
    }

    private void a(List<OnlCgformField> list, List<String> list2, List<OnlColumn> list3, String string, String string2) {
        if (oConvertUtils.isNotEmpty((Object)string2)) {
            String[] stringArray;
            block0: for (String string3 : stringArray = string2.split(",")) {
                for (OnlCgformField onlCgformField : list) {
                    String string4 = onlCgformField.getDbFieldName();
                    if (1 != onlCgformField.getIsShowList() || !string3.equals(string4)) continue;
                    list2.add(string3);
                    OnlColumn onlColumn = new OnlColumn(onlCgformField.getDbFieldTxt(), string4);
                    onlColumn.setCustomRender(string);
                    list3.add(onlColumn);
                    continue block0;
                }
            }
        }
    }

    @Override
    public JSONObject queryOnlineFormObj(OnlCgformHead head, OnlCgformEnhanceJs onlCgformEnhanceJs) {
        List<String> list;
        JSONObject jSONObject = new JSONObject();
        String string = head.getId();
        String string2 = head.getTaskId();
        List<OnlCgformField> list2 = this.onlCgformFieldService.queryAvailableFields(string, head.getTableName(), string2, false);
        ArrayList<String> arrayList = new ArrayList<String>();
        if (oConvertUtils.isEmpty((Object)string2)) {
            list = this.onlAuthPageService.queryFormDisabledCode(head.getId());
            if (list != null && list.size() > 0 && list.get(0) != null) {
                arrayList.addAll(list);
            }
        } else {
            list = this.onlCgformFieldService.queryDisabledFields(head.getTableName(), string2);
            if (list != null && list.size() > 0 && list.get(0) != null) {
                arrayList.addAll(list);
            }
        }
        d.a(onlCgformEnhanceJs, head.getTableName(), list2);
        list = null;
        if ("Y".equals(head.getIsTree())) {
            list = new TreeSelectColumn();
            ((TreeSelectColumn)((Object)list)).setCodeField("id");
            ((TreeSelectColumn)((Object)list)).setFieldName(head.getTreeParentIdField());
            ((TreeSelectColumn)((Object)list)).setPidField(head.getTreeParentIdField());
            ((TreeSelectColumn)((Object)list)).setPidValue("0");
            ((TreeSelectColumn)((Object)list)).setHsaChildField(head.getTreeIdField());
            ((TreeSelectColumn)((Object)list)).setTableName(c.f(head.getTableName()));
            ((TreeSelectColumn)((Object)list)).setTextField(head.getTreeFieldname());
        }
        JSONObject jSONObject2 = c.a(list2, arrayList, list);
        jSONObject2.put("table", (Object)head.getTableName());
        jSONObject2.put("describe", (Object)head.getTableTxt());
        jSONObject.put("schema", (Object)jSONObject2);
        jSONObject.put("head", (Object)head);
        List<OnlCgformButton> list3 = this.queryFormValidButton(string);
        if (list3 != null && list3.size() > 0) {
            jSONObject.put("cgButtonList", list3);
        }
        if (onlCgformEnhanceJs != null && oConvertUtils.isNotEmpty((Object)onlCgformEnhanceJs.getCgJs())) {
            String string3 = d.c(onlCgformEnhanceJs.getCgJs(), list3);
            onlCgformEnhanceJs.setCgJs(string3);
            jSONObject.put("enhanceJs", (Object)d.a(onlCgformEnhanceJs.getCgJs()));
        }
        return jSONObject;
    }

    @Override
    @Cacheable(value={"sys:cache:online:form"}, key="'erp'+ #head.id+'-'+#username")
    public JSONObject queryOnlineFormObj(OnlCgformHead head, String username) {
        OnlCgformEnhanceJs onlCgformEnhanceJs = this.onlCgformHeadService.queryEnhanceJs(head.getId(), "form");
        return this.queryOnlineFormObj(head, onlCgformEnhanceJs);
    }

    @Override
    public List<OnlCgformButton> queryFormValidButton(String headId) {
        List<OnlCgformButton> list = this.onlCgformHeadService.queryButtonList(headId, false);
        List list2 = null;
        if (list != null && list.size() > 0) {
            LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
            String string = loginUser.getId();
            List<String> list3 = this.onlAuthPageService.queryFormHideButton(string, headId);
            list2 = list.stream().filter(onlCgformButton -> list3 == null || list3.indexOf(onlCgformButton.getButtonCode()) < 0).collect(Collectors.toList());
        }
        return list2;
    }

    @Override
    @Cacheable(value={"sys:cache:online:form"}, key="#head.id+'-'+#username")
    public JSONObject queryOnlineFormItem(OnlCgformHead head, String username) {
        head.setTaskId(null);
        return this.a(head);
    }

    @Override
    public JSONObject queryFlowOnlineFormItem(OnlCgformHead head, String username, String taskId) {
        head.setTaskId(taskId);
        return this.a(head);
    }

    @Override
    @Cacheable(value={"sys:cache:online:form"}, key="'enhancejs' + #code + '-' + #type")
    public String queryEnahcneJsString(String code, String type) {
        String string = "";
        OnlCgformEnhanceJs onlCgformEnhanceJs = this.onlCgformHeadService.queryEnhanceJs(code, type);
        if (onlCgformEnhanceJs != null && oConvertUtils.isNotEmpty((Object)onlCgformEnhanceJs.getCgJs())) {
            string = d.b(onlCgformEnhanceJs.getCgJs(), null);
        }
        return string;
    }

    @Override
    public JSONObject getOnlineVue3QueryInfo(String headId) {
        String string;
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getById((Serializable)((Object)headId));
        if (onlCgformHead == null) {
            return null;
        }
        boolean bl = c.a(onlCgformHead);
        ArrayList<String> arrayList = new ArrayList<String>();
        JSONObject jSONObject = this.a(headId, arrayList, true, null);
        JSONObject jSONObject2 = jSONObject.getJSONObject("properties");
        jSONObject.put("title", (Object)onlCgformHead.getTableTxt());
        jSONObject.put("table", (Object)onlCgformHead.getTableName());
        jSONObject.put("joinQuery", (Object)bl);
        jSONObject.put("searchFieldList", arrayList);
        if (oConvertUtils.isNotEmpty((Object)onlCgformHead.getExtConfigJson())) {
            jSONObject.put("extConfigJson", (Object)onlCgformHead.getExtConfigJson());
        }
        if (c.aH.equals(onlCgformHead.getTableType()) && (string = onlCgformHead.getSubTableStr()) != null && !"".equals(string)) {
            String[] stringArray;
            for (String string2 : stringArray = string.split(",")) {
                OnlCgformHead onlCgformHead2 = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string2));
                if (onlCgformHead2 == null) continue;
                JSONObject jSONObject3 = this.a(onlCgformHead2.getId(), arrayList, false, string2);
                jSONObject3.put("title", (Object)onlCgformHead2.getTableTxt());
                jSONObject3.put("view", (Object)"table");
                jSONObject2.put(string2, (Object)jSONObject3);
            }
        }
        return jSONObject;
    }

    @Override
    public List<DictModel> getOnlineTableDictData(String table, String text, String code) {
        Object object;
        List list;
        Object object2;
        Object object3;
        List list2 = null;
        try {
            object3 = this.onlCgformHeadService.getTable(table);
            object2 = (LambdaQueryWrapper)((LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformField::getCgformHeadId, (Object)((OnlCgformHead)object3).getId())).eq(OnlCgformField::getDbFieldName, (Object)text);
            list = this.onlCgformFieldService.list((Wrapper)object2);
            if (list != null && list.size() > 0) {
                object = (OnlCgformField)list.get(0);
                String string = ((OnlCgformField)object).getDictTable();
                String string2 = ((OnlCgformField)object).getDictField();
                String string3 = ((OnlCgformField)object).getDictText();
                if (oConvertUtils.isNotEmpty((Object)string) && oConvertUtils.isNotEmpty((Object)string2) && oConvertUtils.isNotEmpty((Object)string3)) {
                    list2 = this.sysBaseAPI.queryTableDictItemsByCode(string, string3, string2);
                } else if (oConvertUtils.isNotEmpty((Object)string2)) {
                    list2 = this.sysBaseAPI.queryDictItemsByCode(string2);
                }
            }
        }
        catch (Exception exception) {
            a.error("\u4ed6\u8868\u5b57\u6bb5\u83b7\u53d6\u5b57\u5178\u6570\u636e\u5931\u8d25", (Object)exception.getMessage());
        }
        object3 = this.sysBaseAPI.queryTableDictItemsByCode(table, text, code);
        if (list2 != null && list2.size() > 0) {
            object2 = object3.iterator();
            block2: while (object2.hasNext()) {
                list = (DictModel)object2.next();
                object = list.getText();
                for (String string2 : list2) {
                    if (!string2.getValue().equals(object)) continue;
                    list.setText(string2.getText());
                    continue block2;
                }
            }
        }
        return object3;
    }

    private JSONObject a(String string, List<String> list, boolean bl, String string2) {
        LambdaQueryWrapper lambdaQueryWrapper2 = new LambdaQueryWrapper();
        lambdaQueryWrapper2.eq(OnlCgformField::getCgformHeadId, (Object)string);
        lambdaQueryWrapper2.and(lambdaQueryWrapper -> {
            LambdaQueryWrapper cfr_ignored_0 = (LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)lambdaQueryWrapper.eq(OnlCgformField::getIsShowList, (Object)1)).or()).eq(OnlCgformField::getIsQuery, (Object)1);
        });
        lambdaQueryWrapper2.eq(OnlCgformField::getDbIsPersist, (Object)b.b);
        lambdaQueryWrapper2.orderByAsc(OnlCgformField::getOrderNum);
        List list2 = this.onlCgformFieldService.list((Wrapper)lambdaQueryWrapper2);
        for (OnlCgformField onlCgformField : list2) {
            onlCgformField.setFieldDefaultValue(null);
            if ("1".equals(onlCgformField.getQueryConfigFlag())) {
                onlCgformField.setFieldDefaultValue(onlCgformField.getQueryDefVal());
                onlCgformField.setDictField(onlCgformField.getQueryDictField());
                onlCgformField.setDictTable(onlCgformField.getQueryDictTable());
                onlCgformField.setDictText(onlCgformField.getQueryDictText());
                onlCgformField.setFieldShowType(onlCgformField.getQueryShowType());
            }
            if (1 != onlCgformField.getIsQuery()) continue;
            if (bl) {
                list.add(onlCgformField.getDbFieldName());
                continue;
            }
            list.add(string2 + "@" + onlCgformField.getDbFieldName());
        }
        JSONObject jSONObject = c.a((List<OnlCgformField>)list2, null, null);
        c.b(jSONObject);
        return jSONObject;
    }

    private List<OnlCgformField> b(String string) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)string);
        lambdaQueryWrapper.orderByAsc(OnlCgformField::getOrderNum);
        return this.onlCgformFieldService.list((Wrapper)lambdaQueryWrapper);
    }

    private JSONObject a(OnlCgformHead onlCgformHead) {
        OnlCgformEnhanceJs onlCgformEnhanceJs = this.onlCgformHeadService.queryEnhanceJs(onlCgformHead.getId(), "form");
        JSONObject jSONObject = this.queryOnlineFormObj(onlCgformHead, onlCgformEnhanceJs);
        jSONObject.put("formTemplate", (Object)onlCgformHead.getFormTemplate());
        List<String> list = this.onlAuthPageService.queryHideCode(onlCgformHead.getId(), true);
        if (list != null && list.indexOf("update") >= 0) {
            jSONObject.put("form_disable_update", (Object)true);
        }
        if (onlCgformHead.getTableType() == 2) {
            JSONObject jSONObject2 = jSONObject.getJSONObject("schema");
            String string = onlCgformHead.getSubTableStr();
            if (oConvertUtils.isNotEmpty((Object)string)) {
                Object object;
                ArrayList<OnlCgformHead> arrayList = new ArrayList<OnlCgformHead>();
                for (String string2 : string.split(",")) {
                    object = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string2));
                    if (object == null) continue;
                    arrayList.add((OnlCgformHead)object);
                }
                if (arrayList.size() > 0) {
                    Collections.sort(arrayList, new Comparator<OnlCgformHead>(){

                        public int a(OnlCgformHead onlCgformHead, OnlCgformHead onlCgformHead2) {
                            Integer n;
                            Integer n2 = onlCgformHead.getTabOrderNum();
                            if (n2 == null) {
                                n2 = 0;
                            }
                            if ((n = onlCgformHead2.getTabOrderNum()) == null) {
                                n = 0;
                            }
                            return n2.compareTo(n);
                        }

                        @Override
                        public /* synthetic */ int compare(Object object, Object object2) {
                            return this.a((OnlCgformHead)object, (OnlCgformHead)object2);
                        }
                    });
                    for (OnlCgformHead onlCgformHead2 : arrayList) {
                        Object object2;
                        String string2;
                        List<OnlCgformField> list2 = this.onlCgformFieldService.queryAvailableFields(onlCgformHead2.getId(), onlCgformHead2.getTableName(), onlCgformHead.getTaskId(), false);
                        d.b(onlCgformEnhanceJs, onlCgformHead2.getTableName(), list2);
                        string2 = new JSONObject();
                        object = new ArrayList();
                        object = oConvertUtils.isNotEmpty((Object)onlCgformHead.getTaskId()) ? this.onlCgformFieldService.queryDisabledFields(onlCgformHead2.getTableName(), onlCgformHead.getTaskId()) : this.onlAuthPageService.queryFormDisabledCode(onlCgformHead2.getId());
                        if (1 == onlCgformHead2.getRelationType()) {
                            string2 = c.a(list2, object, null);
                        } else {
                            string2.put("columns", (Object)c.a(list2, object));
                            object2 = this.onlAuthPageService.queryListHideButton(null, onlCgformHead2.getId());
                            string2.put("hideButtons", object2);
                        }
                        object2 = this.onlCgformFieldService.queryForeignKey(onlCgformHead2.getId(), onlCgformHead.getTableName());
                        string2.put("foreignKey", object2);
                        string2.put("id", (Object)onlCgformHead2.getId());
                        string2.put("describe", (Object)onlCgformHead2.getTableTxt());
                        string2.put("key", (Object)onlCgformHead2.getTableName());
                        string2.put("view", (Object)"tab");
                        string2.put("order", (Object)onlCgformHead2.getTabOrderNum());
                        string2.put("relationType", (Object)onlCgformHead2.getRelationType());
                        string2.put("formTemplate", (Object)onlCgformHead2.getFormTemplate());
                        jSONObject2.getJSONObject("properties").put(onlCgformHead2.getTableName(), (Object)string2);
                    }
                }
            }
            if (onlCgformEnhanceJs != null && oConvertUtils.isNotEmpty((Object)onlCgformEnhanceJs.getCgJs())) {
                jSONObject.put("enhanceJs", (Object)d.a(onlCgformEnhanceJs.getCgJs()));
            }
        }
        return jSONObject;
    }

    private OnlColumn a(OnlCgformField onlCgformField, Map<String, List<DictModel>> map, List<HrefSlots> list) {
        List list2;
        Object object;
        Object object2;
        Object object3;
        String string = onlCgformField.getDbFieldName();
        OnlColumn onlColumn = new OnlColumn(onlCgformField.getDbFieldTxt(), string);
        onlColumn.setDbType(onlCgformField.getDbType());
        String string2 = onlCgformField.getDictField();
        String string3 = onlCgformField.getFieldShowType();
        if (string3 == null) {
            return onlColumn;
        }
        if (oConvertUtils.isNotEmpty((Object)string2) && !"popup".equals(string3) && !"popup_dict".equals(string3) && !"link_table".equals(string3)) {
            object3 = new ArrayList<DictModel>();
            if (oConvertUtils.isNotEmpty((Object)onlCgformField.getDictTable())) {
                object3 = this.sysBaseAPI.queryTableDictItemsByCode(onlCgformField.getDictTable(), onlCgformField.getDictText(), string2);
            } else if (oConvertUtils.isNotEmpty((Object)onlCgformField.getDictField())) {
                object3 = this.sysBaseAPI.queryDictItemsByCode(string2);
            }
            map.put(string, (List<DictModel>)object3);
            onlColumn.setCustomRender(string);
        }
        if ("switch".equals(string3)) {
            object3 = c.b(onlCgformField);
            map.put(string, (List<DictModel>)object3);
            onlColumn.setCustomRender(string);
        }
        if ("popup_dict".equals(string3)) {
            onlColumn.setFieldType(string3);
        }
        if ("link_table_field".equals(string3)) {
            onlColumn.setFieldType(string3);
        }
        if ("link_table".equals(string3)) {
            onlColumn.setFieldType(string3);
            onlColumn.setHrefSlotName(onlCgformField.getDictTable());
        }
        if ("link_down".equals(string3)) {
            object3 = onlCgformField.getDictTable();
            object2 = (a)JSONObject.parseObject((String)object3, a.class);
            try {
                object = this.sysBaseAPI.queryTableDictItemsByCode(((a)object2).getTable(), ((a)object2).getTxt(), ((a)object2).getKey());
                map.put(string, (List<DictModel>)object);
                onlColumn.setCustomRender(string);
                onlColumn.setLinkField(((a)object2).getLinkField());
            }
            catch (Exception exception) {
                a.warn("\u8054\u52a8\u7ec4\u4ef6\u914d\u7f6e\u9519\u8bef!", (Object)exception.getMessage());
            }
        }
        if ("sel_tree".equals(string3)) {
            object3 = onlCgformField.getDictText().split(",");
            object2 = this.sysBaseAPI.queryTableDictItemsByCode(onlCgformField.getDictTable(), object3[2], object3[0]);
            map.put(string, (List<DictModel>)object2);
            onlColumn.setCustomRender(string);
        }
        if ("cat_tree".equals(string3)) {
            object3 = onlCgformField.getDictText();
            if (oConvertUtils.isEmpty((Object)object3)) {
                object2 = c.e(onlCgformField.getDictField());
                object = this.sysBaseAPI.queryFilterTableDictInfo("SYS_CATEGORY", "NAME", "ID", (String)object2);
                map.put(string, (List<DictModel>)object);
                onlColumn.setCustomRender(string);
            } else {
                onlColumn.setCustomRender("_replace_text_" + (String)object3);
            }
        }
        if ("sel_depart".equals(string3)) {
            object3 = this.a(onlCgformField.getFieldExtendJson());
            object2 = object3[0].length() > 0 ? object3[0] : "ID";
            object = object3[1].length() > 0 ? object3[1] : "DEPART_NAME";
            list2 = this.sysBaseAPI.queryTableDictItemsByCode("SYS_DEPART", (String)object, (String)object2);
            map.put(string, list2);
            onlColumn.setCustomRender(string);
        }
        if ("sel_user".equals(onlCgformField.getFieldShowType())) {
            object3 = this.a(onlCgformField.getFieldExtendJson());
            object2 = object3[0].length() > 0 ? object3[0] : "USERNAME";
            object = object3[1].length() > 0 ? object3[1] : "REALNAME";
            list2 = this.sysBaseAPI.queryTableDictItemsByCode("SYS_USER", (String)object, (String)object2);
            map.put(string, list2);
            onlColumn.setCustomRender(string);
        }
        if (string3.indexOf("file") >= 0) {
            onlColumn.setScopedSlots(new g("fileSlot"));
        } else if (string3.indexOf("image") >= 0) {
            onlColumn.setScopedSlots(new g("imgSlot"));
        } else if (string3.indexOf("editor") >= 0) {
            onlColumn.setScopedSlots(new g("htmlSlot"));
        } else if (string3.equals("date")) {
            onlColumn.setScopedSlots(new g("dateSlot"));
        } else if (string3.equals("pca")) {
            onlColumn.setScopedSlots(new g("pcaSlot"));
        }
        if (StringUtils.isNotBlank((String)onlCgformField.getFieldHref())) {
            object3 = "fieldHref_" + string;
            onlColumn.setHrefSlotName((String)object3);
            list.add(new HrefSlots((String)object3, onlCgformField.getFieldHref()));
        }
        if ("1".equals(onlCgformField.getSortFlag())) {
            onlColumn.setSorter(true);
        }
        if (oConvertUtils.isNotEmpty((Object)(object3 = onlCgformField.getFieldExtendJson()))) {
            onlColumn.setFieldExtendJson((String)object3);
            if (((String)object3).indexOf("showLength") > 0 && (object2 = JSON.parseObject((String)object3)) != null && object2.get((Object)"showLength") != null) {
                onlColumn.setShowLength(oConvertUtils.getInt((Object)object2.get((Object)"showLength")));
            }
        }
        return onlColumn;
    }

    private List<OnlColumn> a(OnlCgformHead onlCgformHead, Map<String, List<DictModel>> map, List<HrefSlots> list, Map<String, Integer> map2) {
        String string;
        int n = onlCgformHead.getTableType();
        ArrayList<OnlColumn> arrayList = new ArrayList<OnlColumn>();
        if (n == 2 && (string = onlCgformHead.getSubTableStr()) != null && !"".equals(string)) {
            String[] stringArray;
            for (String string2 : stringArray = string.split(",")) {
                LambdaQueryWrapper lambdaQueryWrapper = (LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string2);
                OnlCgformHead onlCgformHead2 = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)lambdaQueryWrapper);
                if (onlCgformHead2 == null) continue;
                List<String> list2 = this.onlAuthPageService.queryHideCode(onlCgformHead2.getId(), true);
                List<OnlCgformField> list3 = this.b(onlCgformHead2.getId());
                for (OnlCgformField onlCgformField : list3) {
                    String string3;
                    if (1 != onlCgformField.getIsShowList() && 1 != onlCgformField.getIsQuery() || list2.contains(string3 = onlCgformField.getDbFieldName()) || "id".equals(string3)) continue;
                    Integer n2 = map2.get(string3);
                    if (n2 == null) {
                        map2.put(string3, 1);
                    } else {
                        map2.put(string3, n2 + 1);
                    }
                    OnlColumn onlColumn = this.a(onlCgformField, map, list);
                    if (1 != onlCgformField.getIsShowList()) continue;
                    onlColumn.setTableName(onlCgformHead2.getTableName());
                    arrayList.add(onlColumn);
                }
            }
        }
        return arrayList;
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getDbFieldName": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformField::getDbFieldName;
            }
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
            case "getIsShowList": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlCgformField::getIsShowList;
            }
            case "getDbIsPersist": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlCgformField::getDbIsPersist;
            }
            case "getIsQuery": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlCgformField::getIsQuery;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

