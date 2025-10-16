/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  org.jeecg.common.system.vo.DictModel
 */
package org.jeecg.modules.online.cgform.service;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.online.cgform.entity.OnlCgformButton;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceJs;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.model.b;

public interface IOnlineService {
    public b queryOnlineConfig(OnlCgformHead var1, String var2);

    public JSONObject queryOnlineFormObj(OnlCgformHead var1, OnlCgformEnhanceJs var2);

    public JSONObject queryOnlineFormObj(OnlCgformHead var1, String var2);

    public List<OnlCgformButton> queryFormValidButton(String var1);

    public JSONObject queryOnlineFormItem(OnlCgformHead var1, String var2);

    public JSONObject queryFlowOnlineFormItem(OnlCgformHead var1, String var2, String var3);

    public String queryEnahcneJsString(String var1, String var2);

    public JSONObject getOnlineVue3QueryInfo(String var1);

    public List<DictModel> getOnlineTableDictData(String var1, String var2, String var3);
}

