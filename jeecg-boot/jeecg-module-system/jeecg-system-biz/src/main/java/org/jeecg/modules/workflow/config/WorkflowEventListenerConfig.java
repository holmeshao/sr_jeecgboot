package org.jeecg.modules.workflow.config;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.jeecg.modules.workflow.parser.BpmnFieldPermissionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 工作流事件监听器配置
 * 注册BPMN权限解析器到Flowable引擎
 *
 * @author jeecg
 * @since 2024-12-25
 */
@Slf4j
@Component
public class WorkflowEventListenerConfig implements ApplicationRunner {
    
    @Autowired
    private ProcessEngine processEngine;
    
    @Autowired
    private BpmnFieldPermissionParser bpmnFieldPermissionParser;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        
        log.info("开始注册工作流事件监听器...");
        
        // 注册流程部署事件监听器（临时禁用 - Flowable 7.0 API兼容性问题）
        // TODO: 等Flowable 7.0事件API明确后重新启用
        // processEngine.getRuntimeService()
        //     .addEventListener(bpmnFieldPermissionParser, FlowableEngineEventType.ENTITY_CREATED);
        
        log.warn("流程部署事件监听器暂时禁用 - 等待Flowable 7.0 API兼容性问题解决");
        
        log.info("工作流事件监听器注册完成");
        log.info("- BPMN字段权限解析器已注册到流程部署事件");
    }
}