package org.jeecg.dataingest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.dataingest.entity.DataIngestModuleFieldMapping;

import java.util.List;

/**
 * 数据接入模块字段映射服务接口
 * @Description: data_ingest_module_field_mapping
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
public interface IDataIngestModuleFieldMappingService extends IService<DataIngestModuleFieldMapping> {

    /**
     * 根据任务ID查询字段映射配置
     * @param taskId 任务ID
     * @return 字段映射配置列表
     */
    List<DataIngestModuleFieldMapping> getByTaskId(String taskId);

    /**
     * 根据源字段名查询字段映射配置
     * @param sourceField 源字段名
     * @return 字段映射配置列表
     */
    List<DataIngestModuleFieldMapping> getBySourceField(String sourceField);

    /**
     * 根据目标字段名查询字段映射配置
     * @param targetField 目标字段名
     * @return 字段映射配置列表
     */
    List<DataIngestModuleFieldMapping> getByTargetField(String targetField);

    /**
     * 根据任务ID和源字段名查询字段映射配置
     * @param taskId 任务ID
     * @param sourceField 源字段名
     * @return 字段映射配置
     */
    DataIngestModuleFieldMapping getByTaskIdAndSourceField(String taskId, String sourceField);
} 