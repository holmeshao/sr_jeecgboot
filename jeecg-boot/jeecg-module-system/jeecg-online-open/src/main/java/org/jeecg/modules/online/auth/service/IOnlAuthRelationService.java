/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.extension.service.IService
 *  org.jeecg.common.api.vo.Result
 */
package org.jeecg.modules.online.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.online.auth.entity.OnlAuthRelation;

public interface IOnlAuthRelationService
extends IService<OnlAuthRelation> {
    public Result<?> saveRoleAuth(String var1, String var2, int var3, String var4, List<String> var5);
}

