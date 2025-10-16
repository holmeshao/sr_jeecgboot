/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.extension.service.IService
 */
package org.jeecg.modules.online.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.jeecg.modules.online.auth.entity.OnlAuthPage;
import org.jeecg.modules.online.auth.vo.AuthColumnVO;
import org.jeecg.modules.online.auth.vo.AuthPageVO;

public interface IOnlAuthPageService
extends IService<OnlAuthPage> {
    public void disableAuthColumn(AuthColumnVO var1);

    public void enableAuthColumn(AuthColumnVO var1);

    public void updateAuthColumnBatch(List<AuthColumnVO> var1);

    public void switchAuthColumn(AuthColumnVO var1);

    public void switchAuthColumnBatch(List<AuthColumnVO> var1);

    public void switchFormShow(String var1, String var2, boolean var3);

    public void switchFormEditable(String var1, String var2, boolean var3);

    public void switchListShow(String var1, String var2, boolean var3);

    public List<AuthPageVO> queryRoleAuthByFormId(String var1, String var2, int var3);

    public List<AuthPageVO> queryRoleDataAuth(String var1, String var2);

    public List<AuthPageVO> queryAuthByFormId(String var1, int var2);

    public List<String> queryRoleNoAuthCode(String var1, Integer var2, Integer var3);

    public List<String> queryFormDisabledCode(String var1);

    public List<String> queryHideCode(String var1, String var2, boolean var3);

    public List<String> queryListHideColumn(String var1, String var2);

    public List<String> queryFormHideColumn(String var1, String var2);

    public List<String> queryFormHideButton(String var1, String var2);

    public List<String> queryHideCode(String var1, boolean var2);

    public List<String> queryListHideButton(String var1, String var2);
}

