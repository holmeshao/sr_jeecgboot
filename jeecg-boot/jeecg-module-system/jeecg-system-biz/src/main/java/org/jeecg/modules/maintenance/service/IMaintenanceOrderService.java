package org.jeecg.modules.maintenance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.maintenance.entity.MaintenanceOrder;

/**
 * 维保工单Service接口
 *
 * @author jeecg
 * @since 2025-01-24
 */
public interface IMaintenanceOrderService extends IService<MaintenanceOrder> {
    
    /**
     * 根据流程实例ID查询工单
     *
     * @param processInstanceId 流程实例ID
     * @return 工单信息
     */
    MaintenanceOrder getByProcessInstanceId(String processInstanceId);
}