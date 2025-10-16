/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.extension.service.IService
 *  org.jeecg.common.api.vo.Result
 */
package org.jeecg.modules.online.cgform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.online.cgform.entity.OnlCgformButton;

public interface IOnlCgformButtonService
extends IService<OnlCgformButton> {
    public void saveButton(OnlCgformButton var1);

    public Result<OnlCgformButton> editButton(OnlCgformButton var1);

    public List<OnlCgformButton> queryBuiltInButtonList(String var1);
}

