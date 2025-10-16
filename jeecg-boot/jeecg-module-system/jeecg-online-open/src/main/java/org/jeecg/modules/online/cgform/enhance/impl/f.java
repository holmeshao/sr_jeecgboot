/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Component
 */
package org.jeecg.modules.online.cgform.enhance.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jeecg.modules.online.cgform.enhance.CgformEnhanceJavaListInter;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(value="cgformEnhanceQueryDemo")
public class f
implements CgformEnhanceJavaListInter {
    private static final Logger a = LoggerFactory.getLogger(f.class);

    @Override
    public void execute(String tableName, List<Map<String, Object>> data) throws BusinessException {
        List<a> list = this.a();
        if (data == null) {
            return;
        }
        for (Map<String, Object> map : data) {
            Object object = map.get("province");
            if (object == null) continue;
            String string = list.stream().filter(a2 -> object.toString().equals(a2.a())).map(a::b).findAny().orElse("");
            map.put("province", string);
        }
    }

    private List<a> a() {
        ArrayList<a> arrayList = new ArrayList<a>();
        arrayList.add(new a("bj", "\u5317\u4eac"));
        arrayList.add(new a("sd", "\u5c71\u4e1c"));
        arrayList.add(new a("ah", "\u5b89\u5fbd"));
        return arrayList;
    }

    class a {
        String a;
        String b;

        public a(String string, String string2) {
            this.a = string;
            this.b = string2;
        }

        public String a() {
            return this.a;
        }

        public String b() {
            return this.b;
        }
    }
}

