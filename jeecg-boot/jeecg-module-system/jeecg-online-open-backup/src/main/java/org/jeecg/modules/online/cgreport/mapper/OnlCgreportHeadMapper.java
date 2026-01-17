/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.mapper.BaseMapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  org.apache.ibatis.annotations.Param
 *  org.apache.ibatis.annotations.Select
 *  org.jeecg.common.system.vo.DictModel
 */
package org.jeecg.modules.online.cgreport.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportHead;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportParam;

public interface OnlCgreportHeadMapper
extends BaseMapper<OnlCgreportHead> {
    @Select(value={"${selectSql}"})
    public List<Map<String, Object>> executeSqlDict(@Param(value="selectSql") String var1);

    public List<DictModel> queryDictListBySql(@Param(value="dictCode") String var1, @Param(value="ew") Wrapper<?> var2);

    public IPage<Map<String, Object>> selectPageBySql(Page<Map<String, Object>> var1, @Param(value="tableSql") String var2, @Param(value="ew") Wrapper<?> var3);

    @Deprecated
    public IPage<LinkedHashMap<String, Object>> executeParseSql(Page<Map<String, Object>> var1, @Param(value="sqlStr") String var2);

    public Map<String, Object> queryCgReportMainConfig(@Param(value="reportId") String var1);

    public List<Map<String, Object>> queryCgReportItems(@Param(value="cgrheadId") String var1);

    public List<OnlCgreportParam> queryCgReportParams(@Param(value="cgrheadId") String var1);

    public List<Map<String, Object>> selectByCondition(@Param(value="sqlStr") String var1, @Param(value="param") Map<String, Object> var2);

    public IPage<Map<String, Object>> selectPageByCondition(Page<Map<String, Object>> var1, @Param(value="sqlStr") String var2, @Param(value="param") Map<String, Object> var3);
}

