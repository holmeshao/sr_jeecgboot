package org.jeecg.dataingest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.dataingest.entity.DataIngestModuleDataSourceConfig;

/**
 * 🎯 数据接入模块数据源配置服务接口
 * 
 * @Description: 数据接入数据源配置管理服务
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
public interface IDataIngestModuleDataSourceConfigService extends IService<DataIngestModuleDataSourceConfig> {

    /**
     * 根据数据源名称获取配置
     * @param datasourceName 数据源名称
     * @return 数据源配置
     */
    DataIngestModuleDataSourceConfig getByDatasourceName(String datasourceName);

    /**
     * 测试数据源连接
     * @param config 数据源配置
     * @return 是否连接成功
     */
    boolean testConnection(DataIngestModuleDataSourceConfig config);

    /**
     * 启用数据源
     * @param id 数据源ID
     * @return 操作结果
     */
    boolean enableDatasource(String id);

    /**
     * 禁用数据源
     * @param id 数据源ID
     * @return 操作结果
     */
    boolean disableDatasource(String id);
}