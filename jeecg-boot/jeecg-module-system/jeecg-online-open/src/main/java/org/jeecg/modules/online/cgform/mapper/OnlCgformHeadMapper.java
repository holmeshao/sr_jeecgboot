/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.InterceptorIgnore
 *  com.baomidou.mybatisplus.core.mapper.BaseMapper
 *  org.apache.ibatis.annotations.Param
 *  org.apache.ibatis.annotations.Select
 */
package org.jeecg.modules.online.cgform.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;

public interface OnlCgformHeadMapper
extends BaseMapper<OnlCgformHead> {
    @InterceptorIgnore(tenantLine="true")
    public void executeDDL(@Param(value="sqlStr") String var1);

    public List<Map<String, Object>> queryList(@Param(value="sqlStr") String var1);

    public List<String> queryOnlinetables();

    public int queryCountByTableName(@Param(value="tbname") String var1);

    public Map<String, Object> queryOneByTableNameAndId(@Param(value="tbname") String var1, @Param(value="dataId") String var2);

    public void deleteOne(@Param(value="tbname") String var1, @Param(value="dataId") String var2);

    @Select(value={"select max(copy_version) from onl_cgform_head where physic_id = #{physicId}"})
    public Integer getMaxCopyVersion(@Param(value="physicId") String var1);

    @Select(value={"select table_name from onl_cgform_head where physic_id = #{physicId}"})
    public List<String> queryAllCopyTableName(@Param(value="physicId") String var1);

    @Select(value={"select physic_id from onl_cgform_head GROUP BY physic_id"})
    public List<String> queryCopyPhysicId();

    public String queryCategoryIdByCode(@Param(value="code") String var1);

    @Select(value={"select count(*) from ${tableName} where ${pidField} = #{pidValue}"})
    public Integer queryChildNode(@Param(value="tableName") String var1, @Param(value="pidField") String var2, @Param(value="pidValue") String var3);
}

