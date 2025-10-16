/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper
 *  com.baomidou.mybatisplus.core.mapper.BaseMapper
 *  org.apache.ibatis.annotations.Delete
 *  org.apache.ibatis.annotations.Param
 *  org.apache.ibatis.annotations.Select
 *  org.apache.ibatis.annotations.Update
 */
package org.jeecg.modules.online.cgform.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;

public interface OnlCgformFieldMapper
extends BaseMapper<OnlCgformField> {
    public static final String SELECT_SQL = "SELECT ${ew.sqlSelect} FROM ${tableName} ${ew.customSqlSegment}";

    @Select(value={"SELECT ${ew.sqlSelect} FROM ${tableName} ${ew.customSqlSegment}"})
    public JSONObject doSelect(@Param(value="tableName") String var1, @Param(value="ew") QueryWrapper<?> var2);

    @Select(value={"SELECT ${ew.sqlSelect} FROM ${tableName} ${ew.customSqlSegment}"})
    public List<JSONObject> doSelectList(@Param(value="tableName") String var1, @Param(value="ew") QueryWrapper<?> var2);

    @Update(value={"UPDATE ${tableName} SET ${ew.sqlSet} ${ew.customSqlSegment}"})
    public int doUpdate(@Param(value="tableName") String var1, @Param(value="ew") UpdateWrapper<?> var2);

    @Delete(value={"DELETE FROM ${tableName} ${ew.customSqlSegment}"})
    public int doDelete(@Param(value="tableName") String var1, @Param(value="ew") QueryWrapper<?> var2);

    public void executeInsertSQL(Map<String, Object> var1);

    public void executeUpdatetSQL(Map<String, Object> var1);

    public List<String> selectOnlineHideColumns(@Param(value="user_id") String var1, @Param(value="online_tbname") String var2);

    public List<String> selectOnlineDisabledColumns(@Param(value="user_id") String var1, @Param(value="online_tbname") String var2);

    public List<String> selectFlowAuthColumns(@Param(value="table_name") String var1, @Param(value="task_id") String var2, @Param(value="rule_type") String var3);
}

