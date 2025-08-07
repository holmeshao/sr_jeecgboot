package org.jeecg.modules.maintenance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.maintenance.entity.MaintenanceOrder;
import org.jeecg.modules.maintenance.mapper.MaintenanceOrderMapper;
import org.jeecg.modules.maintenance.service.IMaintenanceOrderService;
import org.springframework.stereotype.Service;

/**
 * 维保工单Service实现
 *
 * @author jeecg
 * @since 2025-01-24
 */
@Service
public class MaintenanceOrderServiceImpl extends ServiceImpl<MaintenanceOrderMapper, MaintenanceOrder> 
        implements IMaintenanceOrderService {

    @Override
    public MaintenanceOrder getByProcessInstanceId(String processInstanceId) {
        QueryWrapper<MaintenanceOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_instance_id", processInstanceId);
        return this.getOne(queryWrapper);
    }
}