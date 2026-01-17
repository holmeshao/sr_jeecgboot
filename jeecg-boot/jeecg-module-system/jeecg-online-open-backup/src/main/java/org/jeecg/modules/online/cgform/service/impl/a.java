/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
 *  org.jeecg.common.api.vo.Result
 *  org.jeecg.common.exception.JeecgBootException
 *  org.springframework.stereotype.Service
 */
package org.jeecg.modules.online.cgform.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.online.cgform.entity.OnlCgformButton;
import org.jeecg.modules.online.cgform.mapper.OnlCgformButtonMapper;
import org.jeecg.modules.online.cgform.service.IOnlCgformButtonService;
import org.springframework.stereotype.Service;

@Service(value="onlCgformButtonServiceImpl")
public class a
extends ServiceImpl<OnlCgformButtonMapper, OnlCgformButton>
implements IOnlCgformButtonService {
    @Override
    public void saveButton(OnlCgformButton onlCgformButton) {
        LambdaQueryWrapper lambdaQueryWrapper = (LambdaQueryWrapper)((LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformButton::getButtonCode, (Object)onlCgformButton.getButtonCode())).eq(OnlCgformButton::getCgformHeadId, (Object)onlCgformButton.getCgformHeadId());
        Long l2 = ((OnlCgformButtonMapper)this.baseMapper).selectCount((Wrapper)lambdaQueryWrapper);
        if (l2 != null && l2 != 0L) {
            throw new JeecgBootException("\u6309\u94ae\u7f16\u7801\u4e0d\u80fd\u91cd\u590d");
        }
        this.save(onlCgformButton);
    }

    @Override
    public Result<OnlCgformButton> editButton(OnlCgformButton onlCgformButton) {
        Result result = new Result();
        OnlCgformButton onlCgformButton2 = (OnlCgformButton)this.getById((Serializable)((Object)onlCgformButton.getId()));
        if (onlCgformButton2 == null) {
            result.error500("\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
        } else {
            LambdaQueryWrapper lambdaQueryWrapper = (LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlCgformButton::getButtonCode, (Object)onlCgformButton.getButtonCode())).eq(OnlCgformButton::getCgformHeadId, (Object)onlCgformButton.getCgformHeadId())).ne(OnlCgformButton::getId, (Object)onlCgformButton.getId());
            Long l2 = ((OnlCgformButtonMapper)this.baseMapper).selectCount((Wrapper)lambdaQueryWrapper);
            if (l2 == null || l2 == 0L) {
                boolean bl = this.updateById(onlCgformButton);
                if (bl) {
                    result.success("\u4fee\u6539\u6210\u529f!");
                } else {
                    result.error500("\u4fee\u6539\u5931\u8d25!");
                }
            } else {
                result.error500("\u6309\u94ae\u7f16\u7801\u4e0d\u80fd\u91cd\u590d");
            }
        }
        return result;
    }

    @Override
    public List<OnlCgformButton> queryBuiltInButtonList(String formId) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformButton::getCgformHeadId, (Object)formId);
        lambdaQueryWrapper.in(OnlCgformButton::getButtonCode, org.jeecg.modules.online.cgform.d.a.getButtonCodeSet());
        List list = super.list((Wrapper)lambdaQueryWrapper);
        return org.jeecg.modules.online.cgform.d.a.a(formId, list);
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getCgformHeadId": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformButton::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformButton::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformButton::getCgformHeadId;
            }
            case "getId": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformButton::getId;
            }
            case "getButtonCode": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformButton::getButtonCode;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformButton::getButtonCode;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformButton") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformButton::getButtonCode;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

