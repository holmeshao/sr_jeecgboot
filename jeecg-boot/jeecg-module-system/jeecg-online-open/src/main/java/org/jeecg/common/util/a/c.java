/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.common.util.a;

import java.io.Serializable;
import java.util.List;

public class c
implements Serializable {
    private static final long a = 7682073117441544718L;
    private String b = "http://json-schema.org/draft-07/schema#";
    private String c;
    private String d;
    private String e;
    private List<String> f;

    public List<String> getRequired() {
        return this.f;
    }

    public void setRequired(List<String> required) {
        this.f = required;
    }

    public String get$schema() {
        return this.b;
    }

    public void set$schema(String $schema) {
        this.b = $schema;
    }

    public String getTitle() {
        return this.c;
    }

    public void setTitle(String title) {
        this.c = title;
    }

    public String getDescription() {
        return this.d;
    }

    public void setDescription(String description) {
        this.d = description;
    }

    public String getType() {
        return this.e;
    }

    public void setType(String type) {
        this.e = type;
    }

    public c() {
    }

    public c(List<String> list) {
        this.d = "\u6211\u662f\u4e00\u4e2ajsonschema description";
        this.c = "\u6211\u662f\u4e00\u4e2ajsonschema title";
        this.e = "object";
        this.f = list;
    }
}

