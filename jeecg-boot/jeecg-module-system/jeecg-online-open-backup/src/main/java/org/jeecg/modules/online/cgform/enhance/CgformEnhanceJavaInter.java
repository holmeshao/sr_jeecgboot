/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 */
package org.jeecg.modules.online.cgform.enhance;

import com.alibaba.fastjson.JSONObject;
import java.util.Map;
import org.jeecg.modules.online.config.exception.BusinessException;

public interface CgformEnhanceJavaInter {
    public void execute(String var1, JSONObject var2) throws BusinessException;

    @Deprecated
    default public void execute(String tableName, Map<String, Object> map) throws BusinessException {
    }
}

