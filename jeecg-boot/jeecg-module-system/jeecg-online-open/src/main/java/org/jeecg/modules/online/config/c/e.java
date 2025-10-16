/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.config.c;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class e {
    protected static Map<String, String> a = new HashMap<String, String>(5);

    private static String a(String string, int n) {
        String string2 = string;
        Iterator<String> iterator = a.keySet().iterator();
        while (iterator.hasNext()) {
            String string3 = String.valueOf(iterator.next());
            String string4 = String.valueOf(a.get(string3));
            if (n == 1) {
                string2 = string.replaceAll(string3, string4);
                continue;
            }
            if (n != 2) continue;
            string2 = string.replaceAll(string4, string3);
        }
        return string2;
    }

    static {
        a.put("class", "clazz");
    }
}

