/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.mapper.BaseMapper
 *  org.apache.ibatis.annotations.Param
 *  org.jeecg.common.system.vo.SysPermissionDataRuleModel
 */
package org.jeecg.modules.online.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.system.vo.SysPermissionDataRuleModel;
import org.jeecg.modules.online.auth.entity.OnlAuthData;

public interface OnlAuthDataMapper
extends BaseMapper<OnlAuthData> {
    public List<SysPermissionDataRuleModel> queryRoleAuthData(@Param(value="userId") String var1, @Param(value="cgformId") String var2);

    public List<SysPermissionDataRuleModel> queryDepartAuthData(@Param(value="userId") String var1, @Param(value="cgformId") String var2);

    public List<SysPermissionDataRuleModel> queryUserAuthData(@Param(value="userId") String var1, @Param(value="cgformId") String var2);
}

