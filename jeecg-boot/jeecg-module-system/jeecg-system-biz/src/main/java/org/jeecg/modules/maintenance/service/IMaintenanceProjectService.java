package org.jeecg.modules.maintenance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.maintenance.entity.MaintenanceProject;

/**
 * 维保项目Service接口
 *
 * @author jeecg
 * @since 2025-01-24
 */
public interface IMaintenanceProjectService extends IService<MaintenanceProject> {
    
    /**
     * 检查项目是否在质保期内
     *
     * @param projectId 项目ID
     * @return 是否在质保期内
     */
    boolean isUnderWarranty(String projectId);
}