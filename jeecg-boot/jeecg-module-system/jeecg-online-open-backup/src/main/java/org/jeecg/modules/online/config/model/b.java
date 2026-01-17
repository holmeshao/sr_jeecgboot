/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jeecg.common.util.oConvertUtils
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.boot.context.properties.ConfigurationProperties
 *  org.springframework.stereotype.Component
 */
package org.jeecg.modules.online.config.model;

import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.config.model.c;
import org.jeecg.modules.online.config.model.d;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component(value="dataBaseConfig")
@ConfigurationProperties(prefix="spring.datasource.dynamic.datasource.master")
public class b {
    @Autowired
    private c dmDataBaseConfig;
    private String a;
    private String b;
    private String c;
    private String d;
    private d e;

    public d getDruid() {
        if (this.e == null) {
            return this.dmDataBaseConfig.getDruid();
        }
        return this.e;
    }

    public void setDruid(d druid) {
        this.e = druid;
    }

    public String getUrl() {
        return oConvertUtils.getString((String)this.a, (String)this.dmDataBaseConfig.getUrl());
    }

    public void setUrl(String url) {
        this.a = url;
    }

    public String getUsername() {
        return oConvertUtils.getString((String)this.b, (String)this.dmDataBaseConfig.getUsername());
    }

    public void setUsername(String username) {
        this.b = username;
    }

    public String getPassword() {
        return oConvertUtils.getString((String)this.c, (String)this.dmDataBaseConfig.getPassword());
    }

    public void setPassword(String password) {
        this.c = password;
    }

    public String getDriverClassName() {
        return oConvertUtils.getString((String)this.d, (String)this.dmDataBaseConfig.getDriverClassName());
    }

    public void setDriverClassName(String driverClassName) {
        this.d = driverClassName;
    }

    public void setDmDataBaseConfig(c dmDataBaseConfig) {
        this.dmDataBaseConfig = dmDataBaseConfig;
    }
}

