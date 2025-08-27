package org.jeecg.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Global MyBatis-Plus MetaObjectHandler for auto-filling audit fields.
 * Fields supported:
 *  - createBy (INSERT)
 *  - create_time (INSERT via property name createTime)
 *  - updateBy (INSERT/UPDATE)
 *  - update_time (INSERT/UPDATE via property name updateTime)
 */
@Component
public class JeecgMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        String username = currentUsername();
        Date now = new Date();

        // Only set if null to avoid overriding explicit values
        strictInsertFill(metaObject, "createBy", String.class, username);
        strictInsertFill(metaObject, "createTime", Date.class, now);

        strictInsertFill(metaObject, "updateBy", String.class, username);
        strictInsertFill(metaObject, "updateTime", Date.class, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String username = currentUsername();
        Date now = new Date();

        strictUpdateFill(metaObject, "updateBy", String.class, username);
        strictUpdateFill(metaObject, "updateTime", Date.class, now);
    }

    private String currentUsername() {
        try {
            Object principal = SecurityUtils.getSubject().getPrincipal();
            if (principal instanceof LoginUser) {
                return ((LoginUser) principal).getUsername();
            }
        } catch (Exception ignored) { }
        return null;
    }
}


