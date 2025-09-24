package org.jeecg.modules.workflow.config;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.jeecg.modules.workflow.parser.BpmnFieldPermissionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.flowable.engine.impl.bpmn.parser.factory.DefaultListenerFactory;
import org.jeecg.modules.workflow.listener.DefaultCandidateResolverTaskListener;
import org.jeecg.modules.workflow.listener.CandidateResolveEventListener;
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
    @Autowired
    private DefaultCandidateResolverTaskListener defaultCandidateResolverTaskListener;
    @Autowired
    private CandidateResolveEventListener candidateResolveEventListener;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        
        log.info("开始注册工作流事件监听器...");
        
        // 注册流程部署事件监听器（Flowable 6.8.0 兼容）
        try {
            processEngine.getRuntimeService()
                .addEventListener(bpmnFieldPermissionParser, FlowableEngineEventType.ENTITY_CREATED);
            log.info("流程部署事件监听器注册成功");
        } catch (Exception e) {
            log.warn("流程部署事件监听器注册失败: {}", e.getMessage());
        }

        // 将默认候选解析监听器注册到 Spring 容器，供 BPMN 模型以表达式引用
        // 使用方法（设计器里）：
        //  在 UserTask 的 TaskListener：event=create, delegateExpression=${defaultCandidateResolverTaskListener}
        try {
            log.info("默认候选解析监听器可用：{}", defaultCandidateResolverTaskListener != null);
        } catch (Exception ignore) {}

        // 全局事件：在任务创建时解析并展开候选人（不依赖BPMN是否配置监听器）
        try {
            processEngine.getRuntimeService().addEventListener(candidateResolveEventListener, FlowableEngineEventType.TASK_CREATED);
            log.info("已注册全局候选解析事件监听器 (TASK_CREATED)");
        } catch (Exception e) {
            log.warn("注册全局候选解析事件监听器失败: {}", e.getMessage());
        }
        
        log.info("工作流事件监听器注册完成");
        log.info("- BPMN字段权限解析器已注册到流程部署事件");
    }
}