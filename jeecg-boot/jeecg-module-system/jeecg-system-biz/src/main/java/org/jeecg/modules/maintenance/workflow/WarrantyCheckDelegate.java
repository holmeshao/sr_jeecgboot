package org.jeecg.modules.maintenance.workflow;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.jeecg.modules.maintenance.entity.MaintenanceProject;
import org.jeecg.modules.maintenance.service.IMaintenanceProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 质保期检查委托类
 *
 * @author jeecg
 * @since 2025-01-24
 */
@Slf4j
@Component
public class WarrantyCheckDelegate implements JavaDelegate {

    @Autowired
    private IMaintenanceProjectService projectService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            // 获取流程变量
            String projectId = (String) execution.getVariable("projectId");
            
            if (projectId == null) {
                log.warn("项目ID为空，无法进行质保期检查");
                execution.setVariable("underWarranty", false);
                return;
            }
            
            // 查询项目信息
            MaintenanceProject project = projectService.getById(projectId);
            if (project == null) {
                log.warn("项目不存在，项目ID：{}", projectId);
                execution.setVariable("underWarranty", false);
                return;
            }
            
            // 检查质保期
            Date now = new Date();
            boolean underWarranty = false;
            
            if (project.getWarrantyStartDate() != null && project.getWarrantyEndDate() != null) {
                underWarranty = now.after(project.getWarrantyStartDate()) && 
                               now.before(project.getWarrantyEndDate());
            }
            
            // 设置流程变量
            execution.setVariable("underWarranty", underWarranty);
            execution.setVariable("warrantyStartDate", project.getWarrantyStartDate());
            execution.setVariable("warrantyEndDate", project.getWarrantyEndDate());
            
            log.info("质保期检查完成，项目ID：{}，是否在质保期内：{}", projectId, underWarranty);
            
        } catch (Exception e) {
            log.error("质保期检查失败", e);
            // 发生异常时，默认设置为非质保期
            execution.setVariable("underWarranty", false);
        }
    }
}