/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
 *  org.jeecg.common.system.vo.SysPermissionDataRuleModel
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package org.jeecg.modules.online.auth.service.a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.jeecg.common.system.vo.SysPermissionDataRuleModel;
import org.jeecg.modules.online.auth.entity.OnlAuthData;
import org.jeecg.modules.online.auth.entity.OnlAuthRelation;
import org.jeecg.modules.online.auth.mapper.OnlAuthDataMapper;
import org.jeecg.modules.online.auth.mapper.OnlAuthRelationMapper;
import org.jeecg.modules.online.auth.service.IOnlAuthDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="onlAuthDataServiceImpl")
public class a
extends ServiceImpl<OnlAuthDataMapper, OnlAuthData>
implements IOnlAuthDataService {
    @Autowired
    private OnlAuthRelationMapper onlAuthRelationMapper;

    @Override
    public void deleteOne(String id) {
        this.removeById((Serializable)((Object)id));
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        this.onlAuthRelationMapper.delete((Wrapper)lambdaQueryWrapper.eq(OnlAuthRelation::getAuthId, (Object)id));
    }

    @Override
    public List<SysPermissionDataRuleModel> queryUserOnlineAuthData(String userId, String cgformId) {
        String string;
        List<SysPermissionDataRuleModel> list = ((OnlAuthDataMapper)this.baseMapper).queryRoleAuthData(userId, cgformId);
        List<SysPermissionDataRuleModel> list2 = ((OnlAuthDataMapper)this.baseMapper).queryDepartAuthData(userId, cgformId);
        List<SysPermissionDataRuleModel> list3 = ((OnlAuthDataMapper)this.baseMapper).queryUserAuthData(userId, cgformId);
        HashMap<String, SysPermissionDataRuleModel> hashMap = new HashMap<String, SysPermissionDataRuleModel>(5);
        for (SysPermissionDataRuleModel sysPermissionDataRuleModel : list) {
            string = sysPermissionDataRuleModel.getId();
            if (hashMap.get(string) != null) continue;
            hashMap.put(string, sysPermissionDataRuleModel);
        }
        for (SysPermissionDataRuleModel sysPermissionDataRuleModel : list2) {
            string = sysPermissionDataRuleModel.getId();
            if (hashMap.get(string) != null) continue;
            hashMap.put(string, sysPermissionDataRuleModel);
        }
        for (SysPermissionDataRuleModel sysPermissionDataRuleModel : list3) {
            string = sysPermissionDataRuleModel.getId();
            if (hashMap.get(string) != null) continue;
            hashMap.put(string, sysPermissionDataRuleModel);
        }
        Collection collection = hashMap.values();
        if (collection == null || collection.size() == 0) {
            return null;
        }
        return new ArrayList<SysPermissionDataRuleModel>(collection);
    }

    @Override
    public void createAiTestAuthData(JSONObject json) {
        ArrayList<OnlAuthData> arrayList = new ArrayList<OnlAuthData>();
        JSONArray jSONArray = json.getJSONArray("data");
        if (jSONArray != null && jSONArray.size() > 0) {
            for (int i2 = 0; i2 < jSONArray.size(); ++i2) {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                OnlAuthData onlAuthData = (OnlAuthData)JSONObject.toJavaObject((JSON)jSONObject, OnlAuthData.class);
                arrayList.add(onlAuthData);
            }
        }
        this.saveBatch(arrayList);
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getAuthId": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthRelation") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlAuthRelation::getAuthId;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

