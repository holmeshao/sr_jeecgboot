package org.jeecg.modules.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowConfig;

import java.util.List;

/**
 * 🎯 工作流配置服务接口
 * 基于JeecgBoot标准Service接口，遵循现有架构模式
 * 
 * @author JeecgBoot工作流集成
 * @version 1.0
 */
public interface IOnlCgformWorkflowConfigService extends IService<OnlCgformWorkflowConfig> {

    /**
     * 根据表单ID查询工作流配置
     */
    OnlCgformWorkflowConfig getByFormId(String formId);

    /**
     * 根据表名查询工作流配置
     */
    OnlCgformWorkflowConfig getByTableName(String tableName);

    /**
     * 根据流程定义Key查询工作流配置
     */
    List<OnlCgformWorkflowConfig> getByProcessKey(String processKey);

    /**
     * 检查表单是否已配置工作流
     */
    boolean hasWorkflowConfig(String formId);

    /**
     * 启用/禁用工作流配置
     */
    void updateStatus(String id, Integer status);

    /**
     * 删除表单的工作流配置
     */
    void deleteByFormId(String formId);

    /**
     * 获取有效的工作流配置列表
     */
    List<OnlCgformWorkflowConfig> getActiveConfigs();

    /**
     * 根据 表单ID + 流程定义Key 查询工作流配置
     */
    OnlCgformWorkflowConfig getByFormAndProcessKey(String formId, String processKey);
}