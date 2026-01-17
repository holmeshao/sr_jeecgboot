/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.stereotype.Component
 */
package org.jeecg.modules.online.cgform.converter;

import java.util.Map;
import org.jeecg.modules.online.cgform.converter.FieldCommentConverter;
import org.springframework.stereotype.Component;

@Component(value="customDemoConverter")
public class c
implements FieldCommentConverter {
    @Override
    public String converterToVal(String txt) {
        if (txt != null && "\u7ba1\u7406\u54581".equals(txt)) {
            return "admin";
        }
        return txt;
    }

    @Override
    public String converterToTxt(String val) {
        if (val != null) {
            if ("admin".equals(val)) {
                return "\u7ba1\u7406\u54581";
            }
            if ("scott".equals(val)) {
                return "\u7ba1\u7406\u54582";
            }
        }
        return val;
    }

    @Override
    public Map<String, String> getConfig() {
        return null;
    }
}

