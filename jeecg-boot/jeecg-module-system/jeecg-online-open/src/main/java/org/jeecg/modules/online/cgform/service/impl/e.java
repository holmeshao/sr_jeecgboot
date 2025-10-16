/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
 *  org.jeecg.common.constant.CommonConstant
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package org.jeecg.modules.online.cgform.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.online.cgform.entity.OnlCgformIndex;
import org.jeecg.modules.online.cgform.mapper.OnlCgformHeadMapper;
import org.jeecg.modules.online.cgform.mapper.OnlCgformIndexMapper;
import org.jeecg.modules.online.cgform.service.IOnlCgformIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="onlCgformIndexServiceImpl")
public class e
extends ServiceImpl<OnlCgformIndexMapper, OnlCgformIndex>
implements IOnlCgformIndexService {
    private static final Logger a = LoggerFactory.getLogger(e.class);
    @Autowired
    private OnlCgformHeadMapper onlCgformHeadMapper;

    @Override
    public void createIndex(String code, String databaseType, String tbname, String synMethod) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformIndex::getCgformHeadId, (Object)code);
        List list = this.list((Wrapper)lambdaQueryWrapper);
        if (list != null && list.size() > 0) {
            for (OnlCgformIndex onlCgformIndex : list) {
                if ((CommonConstant.DEL_FLAG_1.equals(onlCgformIndex.getDelFlag()) || !"N".equals(onlCgformIndex.getIsDbSynch())) && !"force".equalsIgnoreCase(synMethod)) continue;
                String string = "";
                String string2 = onlCgformIndex.getIndexName();
                String string3 = onlCgformIndex.getIndexField();
                String string4 = "normal".equals(onlCgformIndex.getIndexType()) ? " index " : onlCgformIndex.getIndexType() + " index ";
                switch (databaseType) {
                    case "MYSQL": {
                        string = "create " + string4 + string2 + " on " + tbname + "(" + string3 + ")";
                        break;
                    }
                    case "ORACLE": {
                        string = "create " + string4 + string2 + " on " + tbname + "(" + string3 + ")";
                        break;
                    }
                    case "SQLSERVER": {
                        string = "create " + string4 + string2 + " on " + tbname + "(" + string3 + ")";
                        break;
                    }
                    case "POSTGRESQL": {
                        string = "create " + string4 + string2 + " on " + tbname + "(" + string3 + ")";
                        break;
                    }
                    default: {
                        string = "create " + string4 + string2 + " on " + tbname + "(" + string3 + ")";
                    }
                }
                this.onlCgformHeadMapper.executeDDL(string);
                onlCgformIndex.setIsDbSynch("Y");
                this.updateById(onlCgformIndex);
            }
        }
    }

    @Override
    public boolean isExistIndex(String countSql) {
        if (countSql == null) {
            return true;
        }
        Integer n = ((OnlCgformIndexMapper)this.baseMapper).queryIndexCount(countSql);
        return n != null && n > 0;
    }

    @Override
    public List<OnlCgformIndex> getCgformIndexsByCgformId(String cgformId) {
        return ((OnlCgformIndexMapper)this.baseMapper).selectList((Wrapper)new LambdaQueryWrapper().in(OnlCgformIndex::getCgformHeadId, new Object[]{cgformId}));
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getCgformHeadId": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformIndex") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformIndex::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformIndex") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformIndex::getCgformHeadId;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

