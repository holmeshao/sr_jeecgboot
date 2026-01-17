/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  org.apache.ibatis.annotations.Param
 */
package org.jeecg.modules.online.cgform.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface OnlineMapper {
    public List<Map<String, Object>> selectByCondition(@Param(value="sqlStr") String var1, @Param(value="param") Map<String, Object> var2);

    public IPage<Map<String, Object>> selectPageByCondition(Page<Map<String, Object>> var1, @Param(value="sqlStr") String var2, @Param(value="param") Map<String, Object> var3);
}

