/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  org.jeecg.common.util.oConvertUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Component
 */
package org.jeecg.modules.online.cgform.enhance.impl;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.enhance.CgformEnhanceJavaImportInter;
import org.jeecg.modules.online.cgform.enums.EnhanceDataEnum;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(value="cgformEnhanceImportDemo")
public class c
implements CgformEnhanceJavaImportInter {
    private static final Logger a = LoggerFactory.getLogger(c.class);

    @Override
    public EnhanceDataEnum execute(String tableName, JSONObject json) throws BusinessException {
        if (oConvertUtils.isEmpty((Object)json.get((Object)"name"))) {
            json.put("name", (Object)"\u9ed8\u8ba4\u503c");
            return EnhanceDataEnum.INSERT;
        }
        if ("error".equals(json.getString("name"))) {
            json.put("name", (Object)"\u9ed8\u8ba4\u503c");
            throw new BusinessException("\u6d4b\u8bd5\u629b\u51fa\u5f02\u5e38error");
        }
        if ("hello".equals(json.getString("name"))) {
            json.put("id", (Object)"testid123");
            json.put("name", (Object)"JAVA\u5bfc\u5165\u589e\u5f3a \u6d4b\u8bd5\u4fee\u6539");
            return EnhanceDataEnum.UPDATE;
        }
        if ("ok".equals(json.getString("name"))) {
            return EnhanceDataEnum.ABANDON;
        }
        return EnhanceDataEnum.INSERT;
    }
}

