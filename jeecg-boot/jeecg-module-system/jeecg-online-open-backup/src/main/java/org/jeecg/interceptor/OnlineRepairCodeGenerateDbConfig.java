/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.druid.filter.config.ConfigTools
 *  org.apache.commons.lang3.StringUtils
 *  org.jeecgframework.codegenerate.database.CodegenDatasourceConfig
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 */
package org.jeecg.interceptor;

import com.alibaba.druid.filter.config.ConfigTools;
import java.util.ResourceBundle;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.online.config.c.d;
import org.jeecgframework.codegenerate.database.CodegenDatasourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(value="onlineRepairCodeGenerateDbConfig")
@ConditionalOnMissingClass(value={"org.jeecg.config.init.CodeGenerateDbConfig"})
public class OnlineRepairCodeGenerateDbConfig {
    private static final Logger log = LoggerFactory.getLogger(OnlineRepairCodeGenerateDbConfig.class);
    @Value(value="${spring.datasource.dynamic.datasource.master.url:}")
    private String url;
    @Value(value="${spring.datasource.dynamic.datasource.master.username:}")
    private String username;
    @Value(value="${spring.datasource.dynamic.datasource.master.password:}")
    private String password;
    @Value(value="${spring.datasource.dynamic.datasource.master.driver-class-name:}")
    private String driverClassName;
    @Value(value="${spring.datasource.dynamic.datasource.master.druid.public-key:}")
    private String publicKey;
    private static ResourceBundle database_bundle;

    @Bean(value={"initOnlineRepairCodeGenerateDbConfig"})
    public OnlineRepairCodeGenerateDbConfig initOnlineRepairCodeGenerateDbConfig() {
        if (database_bundle == null && StringUtils.isNotBlank((CharSequence)this.url)) {
            if (StringUtils.isNotBlank((CharSequence)this.publicKey)) {
                try {
                    this.password = ConfigTools.decrypt((String)this.publicKey, (String)this.password);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    log.error(" \u4ee3\u7801\u751f\u6210\u5668\u6570\u636e\u5e93\u8fde\u63a5\uff0c\u6570\u636e\u5e93\u5bc6\u7801\u89e3\u5bc6\u5931\u8d25\uff01");
                }
            }
            CodegenDatasourceConfig.initDbConfig((String)this.driverClassName, (String)this.url, (String)this.username, (String)this.password);
        }
        return null;
    }

    static {
        try {
            database_bundle = d.d("jeecg/jeecg_database");
            if (database_bundle == null) {
                database_bundle = ResourceBundle.getBundle("jeecg/jeecg_database");
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

