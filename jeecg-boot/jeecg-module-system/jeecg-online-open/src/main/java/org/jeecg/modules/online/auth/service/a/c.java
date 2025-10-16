/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
 *  org.jeecg.common.api.vo.Result
 *  org.springframework.stereotype.Service
 *  org.springframework.util.CollectionUtils
 */
package org.jeecg.modules.online.auth.service.a;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.lang.invoke.SerializedLambda;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.online.auth.entity.OnlAuthRelation;
import org.jeecg.modules.online.auth.mapper.OnlAuthRelationMapper;
import org.jeecg.modules.online.auth.service.IOnlAuthRelationService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service(value="onlAuthRelationServiceImpl")
public class c
extends ServiceImpl<OnlAuthRelationMapper, OnlAuthRelation>
implements IOnlAuthRelationService {
    @Override
    public Result<?> saveRoleAuth(String roleId, String cgformId, int type, String authMode, List<String> authIds) {
        LambdaQueryWrapper lambdaQueryWrapper = (LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlAuthRelation::getCgformId, (Object)cgformId)).eq(OnlAuthRelation::getType, (Object)type)).eq(OnlAuthRelation::getAuthMode, (Object)authMode)).eq(OnlAuthRelation::getRoleId, (Object)roleId);
        ((OnlAuthRelationMapper)this.baseMapper).delete((Wrapper)lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(authIds)) {
            return Result.OK((String)"\u4fdd\u5b58\u6210\u529f");
        }
        ArrayList<OnlAuthRelation> arrayList = new ArrayList<OnlAuthRelation>();
        for (String string : authIds) {
            OnlAuthRelation onlAuthRelation = new OnlAuthRelation();
            onlAuthRelation.setAuthId(string);
            onlAuthRelation.setCgformId(cgformId);
            onlAuthRelation.setRoleId(roleId);
            onlAuthRelation.setType(type);
            onlAuthRelation.setAuthMode(authMode);
            arrayList.add(onlAuthRelation);
        }
        if (super.saveBatch(arrayList)) {
            if (2 == type) {
                String string;
                List<String> list = ((OnlAuthRelationMapper)this.baseMapper).queryDisabledButtonNameById(authIds);
                if (CollectionUtils.isEmpty((Collection)list)) {
                    return Result.OK((String)"\u4fdd\u5b58\u6210\u529f");
                }
                string = new JSONObject();
                string.put("disabledNames", (Object)list);
                return Result.OK((String)"\u4fdd\u5b58\u6210\u529f", (Object)string);
            }
            return Result.OK((String)"\u4fdd\u5b58\u6210\u529f");
        }
        return Result.error((String)"\u4fdd\u5b58\u5931\u8d25");
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getAuthMode": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthRelation") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlAuthRelation::getAuthMode;
            }
            case "getType": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthRelation") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlAuthRelation::getType;
            }
            case "getCgformId": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthRelation") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlAuthRelation::getCgformId;
            }
            case "getRoleId": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthRelation") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlAuthRelation::getRoleId;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

