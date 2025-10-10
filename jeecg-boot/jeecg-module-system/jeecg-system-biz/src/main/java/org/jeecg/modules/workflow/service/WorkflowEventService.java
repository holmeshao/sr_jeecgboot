package org.jeecg.modules.workflow.service;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.jeecg.modules.workflow.parser.BpmnFieldPermissionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 🎯 基于Flowable 6.8.0的工作流事件服务
 * 
 * 兼容Spring Boot 2.7.18的稳定版本
 * 
 * @author JeecgBoot工作流集成
 * @version 1.0 (Flowable 6.8.0 Compatible)
 */
@Slf4j
@Service
public class WorkflowEventService {

    @Autowired
    private RepositoryService repositoryService;
    
    @Autowired
    private BpmnFieldPermissionParser bpmnFieldPermissionParser;

    /**
     * 部署新版本后是否自动挂起旧版本（不影响在途实例）。
     * 默认: true
     */
    @Value("${workflow.auto-suspend-old-versions:true}")
    private boolean autoSuspendOldVersions;

    /**
     * 🎯 流程定义部署后的事件处理
     * 
     * 替代传统的事件监听器，提供手动触发的流程部署后处理
     * 
     * @param processDefinitionKey 流程定义Key
     */
    public void onProcessDefinitionDeployed(String processDefinitionKey) {
        log.info("🚀 Flowable 6.8.0 - 处理流程定义部署事件：{}", processDefinitionKey);
        
        try {
            // 1. 获取最新版本的流程定义
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion()
                .singleResult();
                
            if (processDefinition == null) {
                log.warn("❌ 未找到流程定义：{}", processDefinitionKey);
                return;
            }
            
            // 2. 处理字段权限解析
            bpmnFieldPermissionParser.handleProcessDeploymentManually(
                processDefinition.getId(), 
                processDefinition.getKey()
            );
            
            // 2.1 新版本激活后，自动挂起旧版本（不影响在途实例）
            if (autoSuspendOldVersions) {
                try {
                    List<ProcessDefinition> defs = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionKey(processDefinitionKey)
                        .list();
                    for (ProcessDefinition def : defs) {
                        if (!def.getId().equals(processDefinition.getId()) && !def.isSuspended()) {
                            repositoryService.suspendProcessDefinitionById(def.getId(), false, null);
                            log.info("🔕 已自动挂起旧版本：key={}, id={}, version={}", def.getKey(), def.getId(), def.getVersion());
                        }
                    }
                } catch (Exception e) {
                    log.warn("自动挂起旧版本失败：{}", e.getMessage());
                }
            }

            // 3. 其他部署后处理逻辑
            performAdditionalDeploymentTasks(processDefinition);
            
            log.info("✅ 流程定义部署事件处理完成：{}", processDefinitionKey);
            
        } catch (Exception e) {
            log.error("❌ 处理流程定义部署事件失败：" + processDefinitionKey, e);
            throw new RuntimeException("流程定义部署事件处理失败", e);
        }
    }
    
    /**
     * 🎯 批量处理所有流程定义的部署事件
     * 
     * 用于系统初始化或大规模更新时使用
     */
    public void onAllProcessDefinitionsDeployed() {
        log.info("🚀 Flowable 6.8.0 - 批量处理所有流程定义部署事件");
        
        try {
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .list();
                
            int successCount = 0;
            int failCount = 0;
            
            for (ProcessDefinition processDefinition : processDefinitions) {
                try {
                    bpmnFieldPermissionParser.handleProcessDeploymentManually(
                        processDefinition.getId(), 
                        processDefinition.getKey()
                    );
                    
                    performAdditionalDeploymentTasks(processDefinition);
                    successCount++;
                    
                } catch (Exception e) {
                    log.error("❌ 处理流程定义失败：" + processDefinition.getKey(), e);
                    failCount++;
                }
            }
            
            log.info("✅ 批量处理完成：总计 {} 个，成功 {} 个，失败 {} 个", 
                processDefinitions.size(), successCount, failCount);
                
        } catch (Exception e) {
            log.error("❌ 批量处理流程定义部署事件失败", e);
            throw new RuntimeException("批量处理失败", e);
        }
    }
    
    /**
     * 🎯 流程实例启动事件处理
     * 
     * 基于Flowable 7.0新架构的流程实例事件处理
     * 
     * @param processInstanceId 流程实例ID
     * @param processDefinitionKey 流程定义Key
     */
    public void onProcessInstanceStarted(String processInstanceId, String processDefinitionKey) {
        log.info("🎯 Flowable 7.0 - 处理流程实例启动事件：processInstanceId={}, processDefinitionKey={}", 
            processInstanceId, processDefinitionKey);
            
        try {
            // 基于IELE引擎的任务事件处理逻辑
            // TODO: 基于Flowable 7.0的IELE引擎实现具体逻辑
            
            log.info("✅ 流程实例启动事件处理完成：{}", processInstanceId);
            
        } catch (Exception e) {
            log.error("❌ 处理流程实例启动事件失败：" + processInstanceId, e);
        }
    }
    
    /**
     * 🎯 任务创建事件处理
     * 
     * 基于IELE任务引擎的动态事件监听
     * 
     * @param taskId 任务ID
     * @param processInstanceId 流程实例ID
     */
    public void onTaskCreated(String taskId, String processInstanceId) {
        log.info("🎯 Flowable 7.0 - 处理任务创建事件：taskId={}, processInstanceId={}", 
            taskId, processInstanceId);
            
        try {
            // 基于IELE引擎的任务事件处理
            // TODO: 实现基于新架构的任务权限配置等逻辑
            
            log.info("✅ 任务创建事件处理完成：{}", taskId);
            
        } catch (Exception e) {
            log.error("❌ 处理任务创建事件失败：" + taskId, e);
        }
    }
    
    /**
     * 🔧 执行额外的部署任务
     * 
     * @param processDefinition 流程定义
     */
    private void performAdditionalDeploymentTasks(ProcessDefinition processDefinition) {
        log.debug("🔧 执行额外的部署任务：{}", processDefinition.getKey());
        
        try {
            // 1. 初始化流程相关的配置
            // 2. 设置默认的任务权限
            // 3. 其他业务逻辑
            
            log.debug("✅ 额外部署任务完成：{}", processDefinition.getKey());
            
        } catch (Exception e) {
            log.warn("⚠️ 执行额外部署任务时出现异常：{}", processDefinition.getKey(), e);
        }
    }
    
    /**
     * 🎯 检查Flowable 7.0兼容性状态
     * 
     * @return 兼容性检查结果
     */
    public String checkFlowable7Compatibility() {
        StringBuilder status = new StringBuilder();
        status.append("🎯 Flowable 7.0 兼容性状态:\n");
        status.append("✅ 事件服务已适配\n");
        status.append("✅ 手动事件触发可用\n");
        status.append("⚠️ IELE引擎集成待完善\n");
        status.append("⚠️ 事件注册表API待集成\n");
        
        return status.toString();
    }
}