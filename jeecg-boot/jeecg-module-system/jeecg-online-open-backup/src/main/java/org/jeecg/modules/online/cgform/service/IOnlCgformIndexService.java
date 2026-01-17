/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.extension.service.IService
 */
package org.jeecg.modules.online.cgform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.jeecg.modules.online.cgform.entity.OnlCgformIndex;

public interface IOnlCgformIndexService
extends IService<OnlCgformIndex> {
    public void createIndex(String var1, String var2, String var3, String var4);

    public boolean isExistIndex(String var1);

    public List<OnlCgformIndex> getCgformIndexsByCgformId(String var1);
}

