/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
 *  org.jeecg.common.util.oConvertUtils
 *  org.springframework.cache.annotation.Cacheable
 *  org.springframework.stereotype.Service
 */
package org.jeecg.modules.online.cgreport.service.a;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.lang.invoke.SerializedLambda;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgreport.c.a;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportItem;
import org.jeecg.modules.online.cgreport.mapper.OnlCgreportItemMapper;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportItemService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service(value="onlCgreportItemServiceImpl")
public class d
extends ServiceImpl<OnlCgreportItemMapper, OnlCgreportItem>
implements IOnlCgreportItemService {
    @Override
    @Cacheable(value={"sys:cache:online:rp"}, key="'search-v2-'+#cgrheadId")
    public List<Map<String, String>> getAutoListQueryInfo(String cgrheadId) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgreportItem::getCgrheadId, (Object)cgrheadId);
        lambdaQueryWrapper.eq(OnlCgreportItem::getIsSearch, (Object)1);
        List list = this.list((Wrapper)lambdaQueryWrapper);
        ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
        int n = 0;
        for (OnlCgreportItem onlCgreportItem : list) {
            HashMap<String, String> hashMap = new HashMap<String, String>(5);
            hashMap.put("label", onlCgreportItem.getFieldTxt());
            String string = onlCgreportItem.getDictCode();
            if (oConvertUtils.isNotEmpty((Object)string)) {
                if (a.b(string)) {
                    hashMap.put("view", "search");
                    hashMap.put("fieldId", onlCgreportItem.getId());
                } else {
                    hashMap.put("view", "list");
                }
            } else {
                hashMap.put("view", onlCgreportItem.getFieldType().toLowerCase());
            }
            hashMap.put("mode", oConvertUtils.isEmpty((Object)onlCgreportItem.getSearchMode()) ? "single" : onlCgreportItem.getSearchMode());
            hashMap.put("field", onlCgreportItem.getFieldName());
            if (++n > 2) {
                hashMap.put("hidden", "1");
            }
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getIsSearch": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportItem") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlCgreportItem::getIsSearch;
            }
            case "getCgrheadId": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgreport/entity/OnlCgreportItem") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgreportItem::getCgrheadId;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

