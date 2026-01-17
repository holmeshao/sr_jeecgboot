/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.cgform.service;

import java.util.List;
import java.util.Map;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.config.exception.BusinessException;

public interface IOnlCgformSqlService {
    public void saveBatchOnlineTable(OnlCgformHead var1, List<OnlCgformField> var2, List<Map<String, Object>> var3) throws BusinessException;

    public Map<String, String> saveOnlineImportDataWithValidate(OnlCgformHead var1, List<OnlCgformField> var2, List<Map<String, Object>> var3);

    public void saveOrUpdateSubData(String var1, OnlCgformHead var2, List<OnlCgformField> var3) throws BusinessException;
}

