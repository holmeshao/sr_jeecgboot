package org.jeecg.modules.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowNode;
import org.jeecg.modules.workflow.model.FormPermissionConfig;

import java.util.List;
import java.util.Map;

/**
 * 🎯 工作流节点权限配置服务接口
 * 基于JeecgBoot标准Service接口
 * 
 * @author JeecgBoot工作流集成
 * @version 1.0
 */
public interface IOnlCgformWorkflowNodeService extends IService<OnlCgformWorkflowNode> {

    /**
     * 根据表单ID和节点ID查询权限配置
     */
    OnlCgformWorkflowNode getByFormAndNode(String formId, String nodeId);

    /**
     * 根据流程定义Key和节点ID查询权限配置
     */
    OnlCgformWorkflowNode getByProcessAndNode(String processKey, String nodeId);

    /**
     * 查询流程的所有节点权限配置
     */
    List<OnlCgformWorkflowNode> getByProcessKey(String processKey);

    /**
     * 查询表单的所有节点权限配置
     */
    List<OnlCgformWorkflowNode> getByFormId(String formId);

    /**
     * 保存或更新节点权限配置
     */
    void saveOrUpdateNodePermission(String formId, String processKey, String nodeId, 
                                   String nodeName, FormPermissionConfig permissionConfig);

    /**
     * 批量保存节点权限配置
     */
    void batchSaveNodePermissions(String formId, String processKey, 
                                 Map<String, FormPermissionConfig> nodePermissions);

    /**
     * 删除流程的所有节点权限配置
     */
    void deleteByProcessKey(String processKey);

    /**
     * 删除表单的所有节点权限配置
     */
    void deleteByFormId(String formId);

    /**
     * 检查节点是否已配置权限
     */
    boolean hasNodePermission(String formId, String nodeId);

    /**
     * 获取流程中配置了权限的节点数量
     */
    int getConfiguredNodeCount(String processKey);

    /**
     * 获取表单字段列表（用于权限配置界面）
     */
    List<Map<String, Object>> getFormFieldsForPermissionConfig(String formId);

    /**
     * 复制权限配置到新版本流程
     */
    void copyPermissionsToNewVersion(String sourceProcessKey, String targetProcessKey);
}