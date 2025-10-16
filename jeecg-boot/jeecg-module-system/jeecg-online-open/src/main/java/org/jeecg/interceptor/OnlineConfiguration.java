/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.web.servlet.HandlerInterceptor
 *  org.springframework.web.servlet.config.annotation.InterceptorRegistry
 *  org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 */
package org.jeecg.interceptor;

import org.jeecg.interceptor.a;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(value="onlineConfiguration")
public class OnlineConfiguration
implements WebMvcConfigurer {
    @Bean
    public a onlineInterceptor() {
        return new a();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        String[] stringArray = new String[]{"/*.html", "/html/**", "/js/**", "/css/**", "/images/**"};
        registry.addInterceptor((HandlerInterceptor)this.onlineInterceptor()).excludePathPatterns(stringArray).addPathPatterns(new String[]{"/online/cgform/api/**"});
    }
}

