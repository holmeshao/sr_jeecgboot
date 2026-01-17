/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.mapper.BaseMapper
 *  org.apache.ibatis.annotations.Param
 */
package org.jeecg.modules.online.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.online.auth.entity.OnlAuthRelation;

public interface OnlAuthRelationMapper
extends BaseMapper<OnlAuthRelation> {
    public List<String> queryDisabledButtonNameById(@Param(value="ids") List<String> var1);
}

