package org.jeecg.modules.workflow.config;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.history.HistoryLevel;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Flowable工作流引擎配置
 *
 * @author jeecg
 * @since 2025-01-24
 */
@Slf4j
@Configuration
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration) {
        // 设置历史记录级别为FULL，记录所有操作历史
        engineConfiguration.setHistoryLevel(HistoryLevel.FULL);
        
        // 设置数据库更新策略：如果表不存在则创建
        engineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        
        // 设置默认字符集 - Flowable 7.0已移除此方法
        // engineConfiguration.setDefaultCharset("UTF-8");
        
        // 启用作业执行器
        engineConfiguration.setAsyncExecutorActivate(true);
        
        // 设置活动字体，支持中文显示
        engineConfiguration.setActivityFontName("宋体");
        engineConfiguration.setLabelFontName("宋体");
        engineConfiguration.setAnnotationFontName("宋体");
        
        // 设置自动部署
        engineConfiguration.setDeploymentMode("single-resource");
        
        log.info("Flowable工作流引擎配置完成");
    }
    
    /**
     * 配置流程引擎
     */
    @Bean
    public ProcessEngine processEngine(DataSource dataSource) {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        
        // 应用配置
        configure(configuration);
        
        ProcessEngine processEngine = configuration.buildProcessEngine();
        log.info("Flowable ProcessEngine 初始化完成");
        
        return processEngine;
    }
}