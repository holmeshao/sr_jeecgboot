/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  freemarker.core.TemplateClassResolver
 *  freemarker.template.Configuration
 *  freemarker.template.Template
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.jeecg.modules.online.config.c;

import freemarker.core.TemplateClassResolver;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class g {
    private static final Logger a = LoggerFactory.getLogger(g.class);
    private static Configuration b = new Configuration(Configuration.VERSION_2_3_28);

    private static String a(String string, String string2, Map<String, Object> map) {
        try {
            StringWriter stringWriter = new StringWriter();
            Template template = null;
            template = b.getTemplate(string, string2);
            template.process(map, (Writer)stringWriter);
            return stringWriter.toString();
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return exception.toString();
        }
    }

    public static String a(String string, Map<String, Object> map) {
        return g.a(string, "utf-8", map);
    }

    static {
        b.setNumberFormat("0.#####################");
        b.setClassForTemplateLoading(g.class, "/");
        b.setNewBuiltinClassResolver(TemplateClassResolver.SAFER_RESOLVER);
    }
}

