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
import org.jeecg.modules.online.auth.entity.OnlAuthPage;
import org.jeecg.modules.online.auth.vo.AuthPageVO;

public interface OnlAuthPageMapper
extends BaseMapper<OnlAuthPage> {
    public List<AuthPageVO> queryRoleAuthByFormId(@Param(value="roleId") String var1, @Param(value="cgformId") String var2, @Param(value="type") int var3);

    public List<AuthPageVO> queryAuthColumnByFormId(@Param(value="cgformId") String var1);

    public List<AuthPageVO> queryAuthButtonByFormId(@Param(value="cgformId") String var1);

    public List<AuthPageVO> queryRoleDataAuth(@Param(value="roleId") String var1, @Param(value="cgformId") String var2);

    public List<String> queryRoleNoAuthCode(@Param(value="userId") String var1, @Param(value="cgformId") String var2, @Param(value="control") Integer var3, @Param(value="page") Integer var4, @Param(value="type") Integer var5);
}

