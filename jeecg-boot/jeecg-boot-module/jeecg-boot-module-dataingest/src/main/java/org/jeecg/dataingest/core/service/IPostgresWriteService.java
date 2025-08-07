package org.jeecg.dataingest.core.service;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * PostgreSQL数据写入服务接口
 * @Description: PostgreSQL数据写入服务接口
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
public interface IPostgresWriteService {
    
    /**
     * 写入创建事件数据
     * @param tableName 目标表名
     * @param data 数据内容
     * @param taskId 任务ID
     * @return 写入结果
     */
    boolean writeCreateData(String tableName, JsonNode data, String taskId);
    
    /**
     * 写入更新事件数据
     * @param tableName 目标表名
     * @param beforeData 更新前数据
     * @param afterData 更新后数据
     * @param taskId 任务ID
     * @return 写入结果
     */
    boolean writeUpdateData(String tableName, JsonNode beforeData, JsonNode afterData, String taskId);
    
    /**
     * 写入删除事件数据
     * @param tableName 目标表名
     * @param data 删除的数据
     * @param taskId 任务ID
     * @return 写入结果
     */
    boolean writeDeleteData(String tableName, JsonNode data, String taskId);
    
    /**
     * 检查目标表是否存在
     * @param tableName 表名
     * @return 是否存在
     */
    boolean tableExists(String tableName);
    
    /**
     * 创建目标表
     * @param tableName 表名
     * @param sourceData 源数据（用于推断字段类型）
     * @return 创建结果
     */
    boolean createTable(String tableName, JsonNode sourceData);
    
    /**
     * 通知NiFi处理数据
     * @param tableName 表名
     * @param operation 操作类型（CREATE、UPDATE、DELETE）
     * @param taskId 任务ID
     * @return 通知结果
     */
    boolean notifyNiFi(String tableName, String operation, String taskId);
}