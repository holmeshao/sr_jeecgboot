/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 */
package org.jeecg.modules.online.cgform.enhance;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.online.cgform.enums.EnhanceDataEnum;
import org.jeecg.modules.online.config.exception.BusinessException;

public interface CgformEnhanceJavaImportInter {
    public EnhanceDataEnum execute(String var1, JSONObject var2) throws BusinessException;
}

