package org.jeecg.modules.maintenance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.maintenance.entity.MaintenanceProject;
import org.jeecg.modules.maintenance.mapper.MaintenanceProjectMapper;
import org.jeecg.modules.maintenance.service.IMaintenanceProjectService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 维保项目Service实现
 *
 * @author jeecg
 * @since 2025-01-24
 */
@Service
public class MaintenanceProjectServiceImpl extends ServiceImpl<MaintenanceProjectMapper, MaintenanceProject> 
        implements IMaintenanceProjectService {

    @Override
    public boolean isUnderWarranty(String projectId) {
        MaintenanceProject project = this.getById(projectId);
        if (project == null) {
            return false;
        }
        
        Date now = new Date();
        return project.getWarrantyStartDate() != null 
                && project.getWarrantyEndDate() != null
                && now.after(project.getWarrantyStartDate()) 
                && now.before(project.getWarrantyEndDate());
    }
}