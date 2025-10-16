/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.boot.context.properties.ConfigurationProperties
 *  org.springframework.stereotype.Component
 */
package org.jeecg.modules.online.config.model;

import org.jeecg.modules.online.config.model.d;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component(value="dmDataBaseConfig")
@ConfigurationProperties(prefix="spring.datasource.druid")
public class c {
    private String a;
    private String b;
    private String c;
    private String d;
    private d e;

    public d getDruid() {
        return this.e;
    }

    public void setDruid(d druid) {
        this.e = druid;
    }

    public String getUrl() {
        return this.a;
    }

    public void setUrl(String url) {
        this.a = url;
    }

    public String getUsername() {
        return this.b;
    }

    public void setUsername(String username) {
        this.b = username;
    }

    public String getPassword() {
        return this.c;
    }

    public void setPassword(String password) {
        this.c = password;
    }

    public String getDriverClassName() {
        return this.d;
    }

    public void setDriverClassName(String driverClassName) {
        this.d = driverClassName;
    }
}

