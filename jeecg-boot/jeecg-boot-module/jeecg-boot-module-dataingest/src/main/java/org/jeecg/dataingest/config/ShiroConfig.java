package org.jeecg.dataingest.config;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.jeecg.config.shiro.ShiroRealm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 数据摄入微服务Shiro配置
 * @author jeecg-boot
 * @date: 2025-01-01
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroRealm shiroRealm() {
        return new ShiroRealm();
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm
        securityManager.setRealm(shiroRealm());
        
        // 关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        
        // 配置不会被拦截的链接
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        
        // 数据摄入微服务的公开接口
        filterChainDefinitionMap.put("/dataingest/health/**", "anon");
        filterChainDefinitionMap.put("/dataingest/actuator/**", "anon");
        filterChainDefinitionMap.put("/dataingest/swagger-ui/**", "anon");
        filterChainDefinitionMap.put("/dataingest/v3/api-docs/**", "anon");
        
        // 需要权限验证的接口
        filterChainDefinitionMap.put("/dataingest/**", "authc");
        
        // 其他所有路径都需要权限验证
        filterChainDefinitionMap.put("/**", "authc");
        
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
}