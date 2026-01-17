/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.cgform.converter.b;

import org.jeecg.modules.online.cgform.converter.a.a;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;

public class k
extends a {
    public k(OnlCgformField onlCgformField) {
        String string = onlCgformField.getDictText();
        String[] stringArray = string.split(",");
        this.setTable(onlCgformField.getDictTable());
        this.setCode(stringArray[0]);
        this.setText(stringArray[2]);
    }
}

