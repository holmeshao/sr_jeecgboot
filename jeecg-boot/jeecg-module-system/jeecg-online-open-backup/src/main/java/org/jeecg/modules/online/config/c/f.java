/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.config.c;

import java.util.Comparator;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;

public class f
implements Comparator<OnlCgformField> {
    public int a(OnlCgformField onlCgformField, OnlCgformField onlCgformField2) {
        if (onlCgformField == null || onlCgformField.getOrderNum() == null || onlCgformField2 == null || onlCgformField2.getOrderNum() == null) {
            return -1;
        }
        Integer n = onlCgformField.getOrderNum();
        Integer n2 = onlCgformField2.getOrderNum();
        return n < n2 ? -1 : (n.equals(n2) ? 0 : 1);
    }

    @Override
    public /* synthetic */ int compare(Object object, Object object2) {
        return this.a((OnlCgformField)object, (OnlCgformField)object2);
    }
}

