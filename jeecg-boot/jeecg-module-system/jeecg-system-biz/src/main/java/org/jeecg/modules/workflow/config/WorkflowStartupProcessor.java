package org.jeecg.modules.workflow.config;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.workflow.service.WorkflowEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 🎯 工作流启动时处理器
 * 
 * 系统启动时自动执行工作流相关的初始化和数据同步工作
 * 
 * @author JeecgBoot工作流集成
 * @version 1.0 (Flowable 7.0 Compatible)
 */
@Slf4j
@Component
public class WorkflowStartupProcessor implements ApplicationRunner {

    @Autowired
    private WorkflowEventService workflowEventService;
    
    /**
     * 是否启用启动时的批量处理
     * 可以通过配置文件控制：workflow.auto-process-on-startup=true/false
     */
    @Value("${workflow.auto-process-on-startup:true}")
    private boolean autoProcessOnStartup;
    
    /**
     * 启动延迟时间（秒），避免启动时资源竞争
     */
    @Value("${workflow.startup-delay-seconds:10}")
    private int startupDelaySeconds;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        
        if (!autoProcessOnStartup) {
            log.info("🎯 工作流启动处理已禁用 (workflow.auto-process-on-startup=false)");
            return;
        }
        
        log.info("🚀 工作流启动处理器开始执行...");
        
        // 延迟执行，确保系统完全启动
        if (startupDelaySeconds > 0) {
            log.info("⏰ 延迟 {} 秒后开始处理，确保系统完全启动", startupDelaySeconds);
            Thread.sleep(startupDelaySeconds * 1000L);
        }
        
        try {
            // 执行批量事件处理
            log.info("🔧 开始执行工作流启动时批量处理...");
            workflowEventService.onAllProcessDefinitionsDeployed();
            
            // 检查兼容性状态
            String status = workflowEventService.checkFlowable7Compatibility();
            log.info("📊 Flowable 7.0兼容性状态：\n{}", status);
            
            log.info("✅ 工作流启动处理完成");
            
        } catch (Exception e) {
            log.error("❌ 工作流启动处理失败", e);
            // 不抛出异常，避免影响系统启动
        }
    }
    
    /**
     * 🎯 手动执行启动处理（用于调试或重新处理）
     */
    public void manualExecuteStartupProcess() {
        log.info("🔧 手动执行工作流启动处理...");
        
        try {
            workflowEventService.onAllProcessDefinitionsDeployed();
            String status = workflowEventService.checkFlowable7Compatibility();
            log.info("📊 手动处理完成，状态：\n{}", status);
            
        } catch (Exception e) {
            log.error("❌ 手动启动处理失败", e);
            throw new RuntimeException("手动启动处理失败", e);
        }
    }
}