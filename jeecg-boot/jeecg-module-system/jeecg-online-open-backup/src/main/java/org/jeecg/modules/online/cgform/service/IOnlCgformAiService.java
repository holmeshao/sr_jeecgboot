/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jeecg.common.api.vo.Result
 */
package org.jeecg.modules.online.cgform.service;

import org.jeecg.common.api.vo.Result;

public interface IOnlCgformAiService {
    public Result<?> genSchema4Modules(String var1);

    public Result<?> genSingleSchema4Modules(String var1);

    public Result<?> aiGenFields(String var1, String var2);

    public Result<?> aiGenMockData(String var1, Integer var2);
}

