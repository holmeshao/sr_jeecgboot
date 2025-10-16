/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Component
 */
package org.jeecg.modules.online.cgform.enhance.impl;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.online.cgform.enhance.CgformEnhanceJavaInter;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(value="cgformEnhanceBeanDemo")
public class a
implements CgformEnhanceJavaInter {
    private static final Logger a = LoggerFactory.getLogger(a.class);

    @Override
    public void execute(String tableName, JSONObject json) throws BusinessException {
        if (json.containsKey((Object)"phone")) {
            json.put("phone", (Object)"18611100000");
        }
    }
}

