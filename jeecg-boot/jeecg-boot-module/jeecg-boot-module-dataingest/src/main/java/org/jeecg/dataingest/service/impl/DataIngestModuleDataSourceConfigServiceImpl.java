package org.jeecg.dataingest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.dataingest.entity.DataIngestModuleDataSourceConfig;
import org.jeecg.dataingest.mapper.DataIngestModuleDataSourceConfigMapper;
import org.jeecg.dataingest.service.IDataIngestModuleDataSourceConfigService;
import org.springframework.stereotype.Service;

/**
 * 🎯 数据接入模块数据源配置服务实现类（简化版）
 */
@Service
public class DataIngestModuleDataSourceConfigServiceImpl 
    extends ServiceImpl<DataIngestModuleDataSourceConfigMapper, DataIngestModuleDataSourceConfig> 
    implements IDataIngestModuleDataSourceConfigService {

    @Override
    public DataIngestModuleDataSourceConfig getByDatasourceName(String datasourceName) {
        QueryWrapper<DataIngestModuleDataSourceConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("datasource_name", datasourceName);
        queryWrapper.eq("status", 1);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean testConnection(DataIngestModuleDataSourceConfig config) {
        return config != null;
    }

    @Override
    public boolean enableDatasource(String id) {
        DataIngestModuleDataSourceConfig config = this.getById(id);
        if (config == null) return false;
        config.setStatus(1);
        return this.updateById(config);
    }

    @Override
    public boolean disableDatasource(String id) {
        DataIngestModuleDataSourceConfig config = this.getById(id);
        if (config == null) return false;
        config.setStatus(0);
        return this.updateById(config);
    }
}