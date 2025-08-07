package org.jeecg.dataingest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.dataingest.entity.DataIngestModuleDataCdcTable;

import java.util.List;

/**
 * 数据接入模块CDC表配置服务接口
 * @Description: data_ingest_module_data_cdc_table
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
public interface IDataIngestModuleDataCdcTableService extends IService<DataIngestModuleDataCdcTable> {

    /**
     * 根据源表名查询CDC配置
     * @param sourceTableName 源表名
     * @return CDC配置
     */
    DataIngestModuleDataCdcTable getBySourceTableName(String sourceTableName);

    /**
     * 根据任务ID查询相关的CDC表配置
     * @param taskId 任务ID
     * @return CDC配置列表
     */
    List<DataIngestModuleDataCdcTable> getByTaskId(String taskId);

    /**
     * 根据业务域查询CDC配置
     * @param businessDomain 业务域
     * @return CDC配置列表
     */
    List<DataIngestModuleDataCdcTable> getByBusinessDomain(String businessDomain);

    /**
     * 获取启用NiFi通知的CDC配置
     * @return 启用通知的CDC配置列表
     */
    List<DataIngestModuleDataCdcTable> getEnabledNiFiNotifyConfigs();

    /**
     * 根据源表名和任务ID查询CDC配置
     * @param sourceTableName 源表名
     * @param taskId 任务ID
     * @return CDC配置
     */
    DataIngestModuleDataCdcTable getBySourceTableNameAndTaskId(String sourceTableName, String taskId);
} 