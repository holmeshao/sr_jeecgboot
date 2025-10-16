/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.baomidou.mybatisplus.extension.service.IService
 *  org.jeecg.common.system.vo.SysPermissionDataRuleModel
 */
package org.jeecg.modules.online.auth.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.jeecg.common.system.vo.SysPermissionDataRuleModel;
import org.jeecg.modules.online.auth.entity.OnlAuthData;

public interface IOnlAuthDataService
extends IService<OnlAuthData> {
    public void deleteOne(String var1);

    public List<SysPermissionDataRuleModel> queryUserOnlineAuthData(String var1, String var2);

    public void createAiTestAuthData(JSONObject var1);
}

