/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.extension.service.IService
 */
package org.jeecg.modules.online.cgreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportItem;

public interface IOnlCgreportItemService
extends IService<OnlCgreportItem> {
    public List<Map<String, String>> getAutoListQueryInfo(String var1);
}

