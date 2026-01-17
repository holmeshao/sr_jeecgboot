/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.config.service;

import java.util.List;
import org.jeecg.modules.online.config.exception.a;

public interface DbTableHandleI {
    public String getAddColumnSql(org.jeecg.modules.online.config.c.a var1);

    public String getReNameFieldName(org.jeecg.modules.online.config.c.a var1);

    public String getUpdateColumnSql(org.jeecg.modules.online.config.c.a var1, org.jeecg.modules.online.config.c.a var2) throws a;

    public String getMatchClassTypeByDataType(String var1, int var2);

    public String dropTableSQL(String var1);

    public String getDropColumnSql(String var1);

    public String getCommentSql(org.jeecg.modules.online.config.c.a var1);

    public String getSpecialHandle(org.jeecg.modules.online.config.c.a var1, org.jeecg.modules.online.config.c.a var2);

    public String dropIndexs(String var1, String var2);

    public String countIndex(String var1, String var2);

    default public void handleUpdateMultiSql(org.jeecg.modules.online.config.c.a meta, org.jeecg.modules.online.config.c.a config, String tableName, List<String> sqlList) {
    }
}

