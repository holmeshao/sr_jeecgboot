/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper
 *  com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper
 *  com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
 *  org.apache.shiro.SecurityUtils
 *  org.jeecg.common.system.vo.LoginUser
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package org.jeecg.modules.online.auth.service.a;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.ArrayList;
import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.online.auth.entity.OnlAuthPage;
import org.jeecg.modules.online.auth.entity.OnlAuthRelation;
import org.jeecg.modules.online.auth.mapper.OnlAuthPageMapper;
import org.jeecg.modules.online.auth.mapper.OnlAuthRelationMapper;
import org.jeecg.modules.online.auth.service.IOnlAuthPageService;
import org.jeecg.modules.online.auth.vo.AuthColumnVO;
import org.jeecg.modules.online.auth.vo.AuthPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value="onlAuthPageServiceImpl")
public class b
extends ServiceImpl<OnlAuthPageMapper, OnlAuthPage>
implements IOnlAuthPageService {
    @Autowired
    private OnlAuthRelationMapper onlAuthRelationMapper;

    @Override
    public void disableAuthColumn(AuthColumnVO authColumnVO) {
        LambdaUpdateWrapper lambdaUpdateWrapper = (LambdaUpdateWrapper)((LambdaUpdateWrapper)((LambdaUpdateWrapper)((LambdaUpdateWrapper)new UpdateWrapper().lambda().eq(OnlAuthPage::getCgformId, (Object)authColumnVO.getCgformId())).eq(OnlAuthPage::getCode, (Object)authColumnVO.getCode())).eq(OnlAuthPage::getType, (Object)1)).set(OnlAuthPage::getStatus, (Object)0);
        this.update((Wrapper)lambdaUpdateWrapper);
    }

    @Override
    @Transactional
    public void enableAuthColumn(AuthColumnVO authColumnVO) {
        String string = authColumnVO.getCgformId();
        String string2 = authColumnVO.getCode();
        LambdaQueryWrapper lambdaQueryWrapper = (LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlAuthPage::getCgformId, (Object)string)).eq(OnlAuthPage::getCode, (Object)string2)).eq(OnlAuthPage::getType, (Object)1);
        ArrayList<OnlAuthPage> arrayList = this.list((Wrapper)lambdaQueryWrapper);
        if (arrayList != null && arrayList.size() > 0) {
            LambdaUpdateWrapper lambdaUpdateWrapper = (LambdaUpdateWrapper)((LambdaUpdateWrapper)((LambdaUpdateWrapper)((LambdaUpdateWrapper)new UpdateWrapper().lambda().eq(OnlAuthPage::getCgformId, (Object)string)).eq(OnlAuthPage::getCode, (Object)string2)).eq(OnlAuthPage::getType, (Object)1)).set(OnlAuthPage::getStatus, (Object)1);
            this.update((Wrapper)lambdaUpdateWrapper);
        } else {
            arrayList = new ArrayList<OnlAuthPage>();
            arrayList.add(new OnlAuthPage(string, string2, 3, 5));
            arrayList.add(new OnlAuthPage(string, string2, 5, 5));
            arrayList.add(new OnlAuthPage(string, string2, 5, 3));
            this.saveBatch(arrayList);
        }
    }

    @Override
    @Transactional
    public void updateAuthColumnBatch(List<AuthColumnVO> authColumnVOList) {
        for (AuthColumnVO authColumnVO : authColumnVOList) {
            if (authColumnVO.getStatus() == 1) {
                this.enableAuthColumn(authColumnVO);
                continue;
            }
            this.disableAuthColumn(authColumnVO);
        }
    }

    @Override
    @Transactional
    public void switchAuthColumn(AuthColumnVO authColumnVO) {
        String string = authColumnVO.getCgformId();
        String string2 = authColumnVO.getCode();
        int n = authColumnVO.getSwitchFlag();
        if (n == 1) {
            this.switchListShow(string, string2, authColumnVO.isListShow());
        } else if (n == 2) {
            this.switchFormShow(string, string2, authColumnVO.isFormShow());
        } else if (n == 3) {
            this.switchFormEditable(string, string2, authColumnVO.isFormEditable());
        } else if (n == 4) {
            this.switchFormEditable(string, string2, authColumnVO.isFormEditable());
            this.switchFormShow(string, string2, authColumnVO.isFormShow());
        }
    }

    @Override
    @Transactional
    public void switchAuthColumnBatch(List<AuthColumnVO> authColumnVOList) {
        for (AuthColumnVO authColumnVO : authColumnVOList) {
            this.switchAuthColumn(authColumnVO);
        }
    }

    @Override
    @Transactional
    public void switchFormShow(String cgformId, String code, boolean flag) {
        this.a(cgformId, code, 5, 5, flag);
    }

    @Override
    @Transactional
    public void switchFormEditable(String cgformId, String code, boolean flag) {
        this.a(cgformId, code, 3, 5, flag);
    }

    @Override
    @Transactional
    public void switchListShow(String cgformId, String code, boolean flag) {
        this.a(cgformId, code, 5, 3, flag);
    }

    @Override
    public List<AuthPageVO> queryRoleAuthByFormId(String roleId, String cgformId, int type) {
        return ((OnlAuthPageMapper)this.baseMapper).queryRoleAuthByFormId(roleId, cgformId, type);
    }

    @Override
    public List<AuthPageVO> queryRoleDataAuth(String roleId, String cgformId) {
        return ((OnlAuthPageMapper)this.baseMapper).queryRoleDataAuth(roleId, cgformId);
    }

    @Override
    public List<AuthPageVO> queryAuthByFormId(String cgformId, int type) {
        if (type == 1) {
            return ((OnlAuthPageMapper)this.baseMapper).queryAuthColumnByFormId(cgformId);
        }
        return ((OnlAuthPageMapper)this.baseMapper).queryAuthButtonByFormId(cgformId);
    }

    @Override
    public List<String> queryRoleNoAuthCode(String cgformId, Integer control, Integer page) {
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String string = loginUser.getId();
        return ((OnlAuthPageMapper)this.baseMapper).queryRoleNoAuthCode(string, cgformId, control, page, null);
    }

    @Override
    public List<String> queryFormDisabledCode(String cgformId) {
        return this.queryRoleNoAuthCode(cgformId, 3, 5);
    }

    @Override
    public List<String> queryHideCode(String userId, String cgformId, boolean isList) {
        return ((OnlAuthPageMapper)this.baseMapper).queryRoleNoAuthCode(userId, cgformId, 5, isList ? 3 : 5, null);
    }

    @Override
    public List<String> queryListHideColumn(String userId, String cgformId) {
        return ((OnlAuthPageMapper)this.baseMapper).queryRoleNoAuthCode(userId, cgformId, 5, 3, 1);
    }

    @Override
    public List<String> queryFormHideColumn(String userId, String cgformId) {
        return ((OnlAuthPageMapper)this.baseMapper).queryRoleNoAuthCode(userId, cgformId, 5, 5, 1);
    }

    @Override
    public List<String> queryFormHideButton(String userId, String cgformId) {
        return ((OnlAuthPageMapper)this.baseMapper).queryRoleNoAuthCode(userId, cgformId, 5, 5, 2);
    }

    @Override
    public List<String> queryHideCode(String cgformId, boolean isList) {
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String string = loginUser.getId();
        return ((OnlAuthPageMapper)this.baseMapper).queryRoleNoAuthCode(string, cgformId, 5, isList ? 3 : 5, null);
    }

    @Override
    public List<String> queryListHideButton(String userId, String cgformId) {
        if (userId == null) {
            LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
            userId = loginUser.getId();
        }
        return ((OnlAuthPageMapper)this.baseMapper).queryRoleNoAuthCode(userId, cgformId, 5, 3, 2);
    }

    private void a(String string, String string2, int n, int n2, boolean bl) {
        LambdaQueryWrapper lambdaQueryWrapper = (LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)new LambdaQueryWrapper().eq(OnlAuthPage::getCgformId, (Object)string)).eq(OnlAuthPage::getCode, (Object)string2)).eq(OnlAuthPage::getControl, (Object)n)).eq(OnlAuthPage::getPage, (Object)n2)).eq(OnlAuthPage::getType, (Object)1);
        OnlAuthPage onlAuthPage = (OnlAuthPage)((OnlAuthPageMapper)this.baseMapper).selectOne((Wrapper)lambdaQueryWrapper);
        if (bl) {
            if (onlAuthPage == null) {
                OnlAuthPage onlAuthPage2 = new OnlAuthPage();
                onlAuthPage2.setCgformId(string);
                onlAuthPage2.setCode(string2);
                onlAuthPage2.setControl(n);
                onlAuthPage2.setPage(n2);
                onlAuthPage2.setType(1);
                onlAuthPage2.setStatus(1);
                ((OnlAuthPageMapper)this.baseMapper).insert(onlAuthPage2);
            } else if (onlAuthPage.getStatus() == 0) {
                onlAuthPage.setStatus(1);
                ((OnlAuthPageMapper)this.baseMapper).updateById(onlAuthPage);
            }
        } else if (!bl && onlAuthPage != null) {
            String string3 = onlAuthPage.getId();
            ((OnlAuthPageMapper)this.baseMapper).deleteById((Serializable)((Object)string3));
            this.onlAuthRelationMapper.delete((Wrapper)new LambdaQueryWrapper().eq(OnlAuthRelation::getAuthId, (Object)string3));
        }
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getControl": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlAuthPage::getControl;
            }
            case "getType": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) {
                    return OnlAuthPage::getType;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) {
                    return OnlAuthPage::getType;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) {
                    return OnlAuthPage::getType;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlAuthPage::getType;
            }
            case "getPage": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlAuthPage::getPage;
            }
            case "getCgformId": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlAuthPage::getCgformId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlAuthPage::getCgformId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlAuthPage::getCgformId;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlAuthPage::getCgformId;
            }
            case "getStatus": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) {
                    return OnlAuthPage::getStatus;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/Integer;")) break;
                return OnlAuthPage::getStatus;
            }
            case "getCode": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlAuthPage::getCode;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlAuthPage::getCode;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlAuthPage::getCode;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthPage") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlAuthPage::getCode;
            }
            case "getAuthId": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/auth/entity/OnlAuthRelation") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlAuthRelation::getAuthId;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

