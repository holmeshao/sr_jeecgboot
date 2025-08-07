package org.jeecg.dataingest.core.service;

import org.jeecg.dataingest.core.entity.IngestTask;

/**
 * 数据接入服务接口
 * @Description: 数据接入服务接口
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
public interface IDataIngestService {
    
    /**
     * 启动数据接入任务
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean startTask(String taskId);
    
    /**
     * 停止数据接入任务
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean stopTask(String taskId);
    
    /**
     * 执行数据接入任务
     * @param task 任务配置
     * @return 执行结果
     */
    boolean executeTask(IngestTask task);
    
    /**
     * 获取任务状态
     * @param taskId 任务ID
     * @return 任务状态
     */
    String getTaskStatus(String taskId);
    
    /**
     * 获取任务执行统计
     * @param taskId 任务ID
     * @return 执行统计信息
     */
    Object getTaskStatistics(String taskId);
} 