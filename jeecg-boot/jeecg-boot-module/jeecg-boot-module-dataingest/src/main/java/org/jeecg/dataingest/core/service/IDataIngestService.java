package org.jeecg.dataingest.core.service;

import org.jeecg.dataingest.entity.DataIngestMoudleIngestTask;

/**
 * 数据接入服务接口
 * @Description: 数据接入服务接口
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
public interface IDataIngestService {
    
    /**
     * 执行数据接入任务
     * @param task 任务信息
     * @return 执行结果
     */
    boolean executeTask(DataIngestMoudleIngestTask task);
    
    /**
     * 执行数据接入任务（带日志记录）
     * @param task 任务信息
     * @param taskId 数据库中的任务ID
     * @return 执行结果
     */
    boolean executeTaskWithLog(DataIngestMoudleIngestTask task, String taskId);
    
    /**
     * 停止任务执行
     * @param taskId 任务ID
     * @return 停止结果
     */
    boolean stopTask(String taskId);
    
    /**
     * 获取任务执行状态
     * @param taskId 任务ID
     * @return 任务状态
     */
    Integer getTaskStatus(String taskId);
}