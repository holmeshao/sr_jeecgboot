/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.cgform.service;

import java.util.List;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceJava;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceSql;

public interface IOnlCgformEnhanceService {
    public List<OnlCgformEnhanceJava> queryEnhanceJavaList(String var1);

    public void saveEnhanceJava(OnlCgformEnhanceJava var1);

    public void updateEnhanceJava(OnlCgformEnhanceJava var1);

    public void deleteEnhanceJava(String var1);

    public void deleteBatchEnhanceJava(List<String> var1);

    public boolean checkOnlyEnhance(OnlCgformEnhanceJava var1);

    public boolean checkOnlyEnhance(OnlCgformEnhanceSql var1);

    public List<OnlCgformEnhanceSql> queryEnhanceSqlList(String var1);

    public void saveEnhanceSql(OnlCgformEnhanceSql var1);

    public void updateEnhanceSql(OnlCgformEnhanceSql var1);

    public void deleteEnhanceSql(String var1);

    public void deleteBatchEnhanceSql(List<String> var1);
}

