/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  org.jeecg.common.util.oConvertUtils
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package org.jeecg.modules.online.cgform.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.mapper.OnlCgformFieldMapper;
import org.jeecg.modules.online.cgform.mapper.OnlCgformHeadMapper;
import org.jeecg.modules.online.cgform.service.IOnlineBaseAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="onlineBaseAPI")
public class g
implements IOnlineBaseAPI {
    @Autowired
    private OnlCgformHeadMapper onlCgformHeadMapper;
    @Autowired
    private OnlCgformFieldMapper onlCgformFieldMapper;

    @Override
    public String getOnlineErpCode(String code, String tableType) {
        LambdaQueryWrapper lambdaQueryWrapper;
        List list;
        String string;
        OnlCgformHead onlCgformHead;
        if ("3".equals(tableType) && (onlCgformHead = (OnlCgformHead)this.onlCgformHeadMapper.selectById((Serializable)((Object)(string = code.substring(1))))) != null && onlCgformHead.getTableType() == 3 && (list = this.onlCgformFieldMapper.selectList((Wrapper)(lambdaQueryWrapper = (LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformField::getCgformHeadId, (Object)string)))) != null && list.size() > 0) {
            Serializable serializable;
            String string2 = null;
            LambdaQueryWrapper lambdaQueryWrapper2 = list.iterator();
            while (lambdaQueryWrapper2.hasNext()) {
                serializable = (OnlCgformField)lambdaQueryWrapper2.next();
                if (!oConvertUtils.isNotEmpty((Object)((OnlCgformField)serializable).getMainTable())) continue;
                string2 = ((OnlCgformField)serializable).getMainTable();
                break;
            }
            if ((serializable = (OnlCgformHead)this.onlCgformHeadMapper.selectOne((Wrapper)(lambdaQueryWrapper2 = (LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, string2)))) != null) {
                code = "/" + ((OnlCgformHead)serializable).getId();
            }
        }
        return code;
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getCgformHeadId": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformField::getCgformHeadId;
            }
            case "getTableName": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformHead::getTableName;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

