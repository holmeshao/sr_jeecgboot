package org.jeecg.dataingest.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.dataingest.entity.DataIngestModuleIngestTask;

/**
 * 数据接入模块任务服务接口
 * @Description: 支持JSON配置的数据接入任务服务
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
public interface IDataIngestModuleIngestTaskService extends IService<DataIngestModuleIngestTask> {

    /**
     * 创建或更新任务 - 支持JSON配置
     * @param taskConfig 任务配置JSON
     * @param operator 操作人
     * @return 任务ID
     */
    String saveOrUpdateTask(JSONObject taskConfig, String operator);

    /**
     * 删除任务
     * @param taskConfig 包含任务ID的JSON
     * @param operator 操作人
     */
    void deleteTask(JSONObject taskConfig, String operator);

    /**
     * 启动所有启用的任务
     * @return 启动的任务数量
     */
    int startAllEnabledTasks();

    /**
     * 停止所有任务
     * @return 停止的任务数量
     */
    int stopAllTasks();

    /**
     * 根据任务ID获取完整任务配置（包含关联表数据）
     * @param taskId 任务ID
     * @return 完整任务配置JSON
     */
    JSONObject getFullTaskConfig(String taskId);
} 