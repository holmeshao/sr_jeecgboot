/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.mapper.BaseMapper
 *  org.apache.ibatis.annotations.Param
 */
package org.jeecg.modules.online.cgform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.online.cgform.entity.OnlCgformIndex;

public interface OnlCgformIndexMapper
extends BaseMapper<OnlCgformIndex> {
    public int queryIndexCount(@Param(value="sqlStr") String var1);
}

