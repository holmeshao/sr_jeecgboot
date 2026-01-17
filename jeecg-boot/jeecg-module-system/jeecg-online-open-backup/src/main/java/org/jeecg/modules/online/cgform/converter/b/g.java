/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jeecg.common.system.vo.DictModel
 *  org.jeecg.common.util.SpringContextUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.jeecg.modules.online.cgform.converter.b;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.online.cgform.converter.a.b;
import org.jeecg.modules.online.cgform.d.c;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class g
extends b {
    private static final Logger d = LoggerFactory.getLogger(g.class);
    protected IOnlCgformFieldService c;

    public g(OnlCgformField onlCgformField) {
        String string = onlCgformField.getDictTable();
        String string2 = onlCgformField.getDictText();
        String string3 = onlCgformField.getDictField();
        ArrayList<DictModel> arrayList = new ArrayList<DictModel>();
        try {
            String string4 = string2.split(",")[0];
            this.c = (IOnlCgformFieldService)SpringContextUtils.getBean(IOnlCgformFieldService.class);
            List<Map<String, Object>> list = this.c.queryLinkTableDictList(string, string2, string3);
            if (list != null && list.size() > 0) {
                for (Map<String, Object> map : list) {
                    String string5 = org.jeecg.modules.online.cgform.d.c.a(map, string4);
                    String string6 = org.jeecg.modules.online.cgform.d.c.a(map, string3);
                    arrayList.add(new DictModel(string6, string5));
                }
            }
        }
        catch (Exception exception) {
            d.error("\u5173\u8054\u8bb0\u5f55\u7ec4\u4ef6 \u5bfc\u5165\u5bfc\u51fa\u6570\u636e\u7ffb\u8bd1\u5931\u8d25", (Object)exception.getMessage());
        }
        this.b = arrayList;
    }
}

