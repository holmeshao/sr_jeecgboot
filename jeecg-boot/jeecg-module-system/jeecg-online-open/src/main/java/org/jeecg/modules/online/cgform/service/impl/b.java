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
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceJava;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceSql;
import org.jeecg.modules.online.cgform.mapper.OnlCgformEnhanceJavaMapper;
import org.jeecg.modules.online.cgform.mapper.OnlCgformEnhanceSqlMapper;
import org.jeecg.modules.online.cgform.service.IOnlCgformEnhanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="onlCgformEnhanceService")
public class b
implements IOnlCgformEnhanceService {
    @Autowired
    private OnlCgformEnhanceJavaMapper onlCgformEnhanceJavaMapper;
    @Autowired
    private OnlCgformEnhanceSqlMapper onlCgformEnhanceSqlMapper;

    @Override
    public List<OnlCgformEnhanceJava> queryEnhanceJavaList(String cgformId) {
        LambdaQueryWrapper lambdaQueryWrapper = (LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformEnhanceJava::getCgformHeadId, (Object)cgformId);
        List list = this.onlCgformEnhanceJavaMapper.selectList((Wrapper)lambdaQueryWrapper);
        return list;
    }

    @Override
    public void saveEnhanceJava(OnlCgformEnhanceJava onlCgformEnhanceJava) {
        this.onlCgformEnhanceJavaMapper.insert(onlCgformEnhanceJava);
    }

    @Override
    public void updateEnhanceJava(OnlCgformEnhanceJava onlCgformEnhanceJava) {
        this.onlCgformEnhanceJavaMapper.updateById(onlCgformEnhanceJava);
    }

    @Override
    public void deleteEnhanceJava(String id) {
        this.onlCgformEnhanceJavaMapper.deleteById((Serializable)((Object)id));
    }

    @Override
    public void deleteBatchEnhanceJava(List<String> idList) {
        this.onlCgformEnhanceJavaMapper.deleteBatchIds(idList);
    }

    @Override
    public boolean checkOnlyEnhance(OnlCgformEnhanceJava onlCgformEnhanceJava) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getButtonCode, (Object)onlCgformEnhanceJava.getButtonCode());
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getCgformHeadId, (Object)onlCgformEnhanceJava.getCgformHeadId());
        lambdaQueryWrapper.eq(OnlCgformEnhanceJava::getEvent, (Object)onlCgformEnhanceJava.getEvent());
        Long l2 = this.onlCgformEnhanceJavaMapper.selectCount((Wrapper)lambdaQueryWrapper);
        if (l2 != null) {
            if (l2 == 1L && oConvertUtils.isEmpty((Object)onlCgformEnhanceJava.getId())) {
                return false;
            }
            if (l2 == 2L) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkOnlyEnhance(OnlCgformEnhanceSql onlCgformEnhanceSql) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformEnhanceSql::getButtonCode, (Object)onlCgformEnhanceSql.getButtonCode());
        lambdaQueryWrapper.eq(OnlCgformEnhanceSql::getCgformHeadId, (Object)onlCgformEnhanceSql.getCgformHeadId());
        Long l2 = this.onlCgformEnhanceSqlMapper.selectCount((Wrapper)lambdaQueryWrapper);
        if (l2 != null) {
            if (l2 == 1L && oConvertUtils.isEmpty((Object)onlCgformEnhanceSql.getId())) {
                return false;
            }
            if (l2 > 1L) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<OnlCgformEnhanceSql> queryEnhanceSqlList(String cgformId) {
        LambdaQueryWrapper lambdaQueryWrapper = (LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformEnhanceSql::getCgformHeadId, (Object)cgformId);
        lambdaQueryWrapper.orderByDesc(OnlCgformEnhanceSql::getId);
        List list = this.onlCgformEnhanceSqlMapper.selectList((Wrapper)lambdaQueryWrapper);
        return list;
    }

    @Override
    public void saveEnhanceSql(OnlCgformEnhanceSql onlCgformEnhanceSql) {
        this.onlCgformEnhanceSqlMapper.insert(onlCgformEnhanceSql);
    }

    @Override
    public void updateEnhanceSql(OnlCgformEnhanceSql onlCgformEnhanceSql) {
        this.onlCgformEnhanceSqlMapper.updateById(onlCgformEnhanceSql);
    }

    @Override
    public void deleteEnhanceSql(String id) {
        this.onlCgformEnhanceSqlMapper.deleteById((Serializable)((Object)id));
    }

    @Override
    public void deleteBatchEnhanceSql(List<String> idList) {
        this.onlCgformEnhanceSqlMapper.deleteBatchIds(idList);
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getEvent": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformEnhanceJava::getEvent;
            }
            case "getCgformHeadId": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJava::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJava::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceSql") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceSql::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceSql") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformEnhanceSql::getCgformHeadId;
            }
            case "getId": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceSql") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformEnhanceSql::getId;
            }
            case "getButtonCode": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceJava") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformEnhanceJava::getButtonCode;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformEnhanceSql") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformEnhanceSql::getButtonCode;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

