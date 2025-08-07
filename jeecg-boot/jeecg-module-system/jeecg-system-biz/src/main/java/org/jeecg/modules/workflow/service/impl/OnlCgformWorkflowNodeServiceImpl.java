package org.jeecg.modules.workflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowNode;
import org.jeecg.modules.workflow.mapper.OnlCgformWorkflowNodeMapper;
import org.jeecg.modules.workflow.model.FormPermissionConfig;
import org.jeecg.modules.workflow.service.IOnlCgformWorkflowNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 🎯 工作流节点权限配置服务实现
 * 基于JeecgBoot在线表单，提供字段级权限管理
 * 
 * @author JeecgBoot工作流集成
 * @version 1.0
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class OnlCgformWorkflowNodeServiceImpl 
    extends ServiceImpl<OnlCgformWorkflowNodeMapper, OnlCgformWorkflowNode> 
    implements IOnlCgformWorkflowNodeService {

    @Autowired
    private OnlCgformWorkflowNodeMapper nodeMapper;

    @Autowired
    private IOnlCgformFieldService onlCgformFieldService;

    @Autowired
    private IOnlCgformHeadService onlCgformHeadService;

    // =============== 基础查询方法 ===============

    @Override
    public OnlCgformWorkflowNode getByFormAndNode(String formId, String nodeId) {
        if (oConvertUtils.isEmpty(formId) || oConvertUtils.isEmpty(nodeId)) {
            return null;
        }
        return nodeMapper.selectByFormAndNode(formId, nodeId);
    }

    @Override
    public OnlCgformWorkflowNode getByProcessAndNode(String processKey, String nodeId) {
        if (oConvertUtils.isEmpty(processKey) || oConvertUtils.isEmpty(nodeId)) {
            return null;
        }
        return nodeMapper.selectByProcessAndNode(processKey, nodeId);
    }

    @Override
    public List<OnlCgformWorkflowNode> getByProcessKey(String processKey) {
        if (oConvertUtils.isEmpty(processKey)) {
            return Collections.emptyList();
        }
        return nodeMapper.selectByProcessKey(processKey);
    }

    @Override
    public List<OnlCgformWorkflowNode> getByFormId(String formId) {
        if (oConvertUtils.isEmpty(formId)) {
            return Collections.emptyList();
        }
        return nodeMapper.selectByFormId(formId);
    }

    // =============== 权限配置保存方法 ===============

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateNodePermission(String formId, String processKey, String nodeId, 
                                          String nodeName, FormPermissionConfig permissionConfig) {
        
        log.debug("保存节点权限配置：formId={}, nodeId={}", formId, nodeId);

        try {
            // 查找现有配置
            OnlCgformWorkflowNode existing = getByFormAndNode(formId, nodeId);
            
            if (existing != null) {
                // 更新现有配置
                updateNodeConfig(existing, permissionConfig);
                updateById(existing);
                log.debug("更新节点权限配置成功：{}", nodeId);
            } else {
                // 创建新配置
                OnlCgformWorkflowNode newConfig = createNodeConfig(formId, processKey, 
                                                                  nodeId, nodeName, permissionConfig);
                save(newConfig);
                log.debug("创建节点权限配置成功：{}", nodeId);
            }

        } catch (Exception e) {
            log.error("保存节点权限配置失败：formId={}, nodeId={}", formId, nodeId, e);
            throw new RuntimeException("保存节点权限配置失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveNodePermissions(String formId, String processKey, 
                                        Map<String, FormPermissionConfig> nodePermissions) {
        
        log.debug("批量保存节点权限配置：formId={}, 节点数量={}", formId, nodePermissions.size());

        try {
            for (Map.Entry<String, FormPermissionConfig> entry : nodePermissions.entrySet()) {
                String nodeId = entry.getKey();
                FormPermissionConfig config = entry.getValue();
                
                // 节点名称可以从配置中获取，或者使用nodeId作为默认值
                String nodeName = nodeId; // 可以根据需要从其他地方获取
                
                saveOrUpdateNodePermission(formId, processKey, nodeId, nodeName, config);
            }

            log.debug("批量保存节点权限配置完成：{} 个节点", nodePermissions.size());

        } catch (Exception e) {
            log.error("批量保存节点权限配置失败：formId={}", formId, e);
            throw new RuntimeException("批量保存节点权限配置失败: " + e.getMessage(), e);
        }
    }

    // =============== 删除方法 ===============

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByProcessKey(String processKey) {
        if (oConvertUtils.isEmpty(processKey)) {
            return;
        }

        LambdaQueryWrapper<OnlCgformWorkflowNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OnlCgformWorkflowNode::getProcessDefinitionKey, processKey);
        
        int count = remove(wrapper) ? 1 : 0;
        log.debug("删除流程 {} 的权限配置：{} 条记录", processKey, count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByFormId(String formId) {
        if (oConvertUtils.isEmpty(formId)) {
            return;
        }

        LambdaQueryWrapper<OnlCgformWorkflowNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OnlCgformWorkflowNode::getCgformHeadId, formId);
        
        int count = remove(wrapper) ? 1 : 0;
        log.debug("删除表单 {} 的权限配置：{} 条记录", formId, count);
    }

    // =============== 检查和统计方法 ===============

    @Override
    public boolean hasNodePermission(String formId, String nodeId) {
        if (oConvertUtils.isEmpty(formId) || oConvertUtils.isEmpty(nodeId)) {
            return false;
        }
        return nodeMapper.checkNodeConfigured(formId, nodeId) > 0;
    }

    @Override
    public int getConfiguredNodeCount(String processKey) {
        if (oConvertUtils.isEmpty(processKey)) {
            return 0;
        }
        return nodeMapper.countConfiguredNodes(processKey);
    }

    // =============== 表单字段相关方法 ===============

    @Override
    public List<Map<String, Object>> getFormFieldsForPermissionConfig(String formId) {
        log.debug("获取表单字段列表用于权限配置：formId={}", formId);

        try {
            // 1. 根据formId获取表单头信息
            OnlCgformHead cgformHead = onlCgformHeadService.getById(formId);
            if (cgformHead == null) {
                log.warn("表单头信息不存在：formId={}", formId);
                return Collections.emptyList();
            }

            // 2. 根据表单头ID获取字段信息（使用正确的MyBatis-Plus API）
            List<OnlCgformField> fields = onlCgformFieldService.list(
                new LambdaQueryWrapper<OnlCgformField>()
                    .eq(OnlCgformField::getCgformHeadId, cgformHead.getId())
                    .orderByAsc(OnlCgformField::getOrderNum)
            );
            
            if (oConvertUtils.isEmpty(fields)) {
                log.warn("表单 {} 未找到字段信息", formId);
                return Collections.emptyList();
            }

            return fields.stream().map(field -> {
                Map<String, Object> fieldInfo = new HashMap<>();
                fieldInfo.put("fieldName", field.getDbFieldName());
                fieldInfo.put("fieldLabel", field.getDbFieldTxt());
                fieldInfo.put("fieldType", field.getDbType());
                fieldInfo.put("isRequired", field.getDbIsNull() == 0);
                fieldInfo.put("defaultValue", field.getDbDefaultVal());
                fieldInfo.put("comment", field.getDbFieldTxt());
                
                // 添加字段分类信息
                fieldInfo.put("category", categorizeField(field));
                
                return fieldInfo;
            }).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("获取表单字段失败：formId={}", formId, e);
            return Collections.emptyList();
        }
    }

    /**
     * 🎯 字段分类
     * 帮助用户理解字段用途
     */
    private String categorizeField(OnlCgformField field) {
        String fieldName = field.getDbFieldName().toLowerCase();
        String fieldComment = field.getDbFieldTxt();

        // 系统字段
        if (fieldName.matches("(id|create_by|create_time|update_by|update_time|del_flag|version|process_instance_id|bmp_status|tenant_id|dept_id)")) {
            return "system";
        }

        // 通用流程字段
        if (fieldName.matches("(audit|approve|process|review|handle|comment|opinion|remark|note)_.*") ||
            fieldName.matches(".*(opinion|comment|remark|note|memo|desc|description|reason|result)$")) {
            return "workflow";
        }

        // 文件字段
        if (fieldName.matches(".*(file|attachment|upload|photo|image|picture|doc|pdf).*")) {
            return "file";
        }

        // 根据注释判断
        if (oConvertUtils.isNotEmpty(fieldComment)) {
            if (fieldComment.matches(".*(审批|意见|备注|说明|处理|审核|批注|评论|原因|结果|建议|反馈|记录|操作|流程).*")) {
                return "workflow";
            }
        }

        return "business";
    }

    // =============== 版本管理方法 ===============

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyPermissionsToNewVersion(String sourceProcessKey, String targetProcessKey) {
        log.debug("复制权限配置到新版本：{} -> {}", sourceProcessKey, targetProcessKey);

        try {
            List<OnlCgformWorkflowNode> sourceConfigs = getByProcessKey(sourceProcessKey);
            if (oConvertUtils.isEmpty(sourceConfigs)) {
                log.debug("源流程 {} 无权限配置，跳过复制", sourceProcessKey);
                return;
            }

            for (OnlCgformWorkflowNode sourceConfig : sourceConfigs) {
                OnlCgformWorkflowNode newConfig = copyNodeConfig(sourceConfig, targetProcessKey);
                save(newConfig);
            }

            log.debug("复制权限配置完成：{} 个节点配置", sourceConfigs.size());

        } catch (Exception e) {
            log.error("复制权限配置失败：{} -> {}", sourceProcessKey, targetProcessKey, e);
            throw new RuntimeException("复制权限配置失败: " + e.getMessage(), e);
        }
    }

    // =============== 辅助方法 ===============

    /**
     * 🎯 更新节点配置
     */
    private void updateNodeConfig(OnlCgformWorkflowNode existing, FormPermissionConfig permissionConfig) {
        existing.setEditableFields(JSON.toJSONString(permissionConfig.getEditableFields()));
        existing.setReadonlyFields(JSON.toJSONString(permissionConfig.getReadonlyFields()));
        existing.setHiddenFields(JSON.toJSONString(permissionConfig.getHiddenFields()));
        existing.setRequiredFields(JSON.toJSONString(permissionConfig.getRequiredFields()));
        existing.setFormMode(permissionConfig.getFormMode());
        existing.setConditionalPermissions(permissionConfig.getConditionalPermissions());
        
        // 更新按钮配置
        if (oConvertUtils.isNotEmpty(permissionConfig.getCustomButtons())) {
            existing.setCustomButtons(JSON.toJSONString(permissionConfig.getCustomButtons()));
        }
        if (oConvertUtils.isNotEmpty(permissionConfig.getHiddenButtons())) {
            existing.setHiddenButtons(JSON.toJSONString(permissionConfig.getHiddenButtons()));
        }
    }

    /**
     * 🎯 创建节点配置
     */
    private OnlCgformWorkflowNode createNodeConfig(String formId, String processKey, 
                                                  String nodeId, String nodeName, 
                                                  FormPermissionConfig permissionConfig) {
        OnlCgformWorkflowNode config = new OnlCgformWorkflowNode();
        config.setCgformHeadId(formId);
        config.setProcessDefinitionKey(processKey);
        config.setNodeId(nodeId);
        config.setNodeName(nodeName);
        config.setEditableFields(JSON.toJSONString(permissionConfig.getEditableFields()));
        config.setReadonlyFields(JSON.toJSONString(permissionConfig.getReadonlyFields()));
        config.setHiddenFields(JSON.toJSONString(permissionConfig.getHiddenFields()));
        config.setRequiredFields(JSON.toJSONString(permissionConfig.getRequiredFields()));
        config.setFormMode(permissionConfig.getFormMode());
        config.setConditionalPermissions(permissionConfig.getConditionalPermissions());
        config.setStatus(1);
        config.setSortOrder(100);
        
        // 设置按钮配置
        if (oConvertUtils.isNotEmpty(permissionConfig.getCustomButtons())) {
            config.setCustomButtons(JSON.toJSONString(permissionConfig.getCustomButtons()));
        }
        if (oConvertUtils.isNotEmpty(permissionConfig.getHiddenButtons())) {
            config.setHiddenButtons(JSON.toJSONString(permissionConfig.getHiddenButtons()));
        }
        
        return config;
    }

    /**
     * 🎯 复制节点配置
     */
    private OnlCgformWorkflowNode copyNodeConfig(OnlCgformWorkflowNode source, String targetProcessKey) {
        OnlCgformWorkflowNode copy = new OnlCgformWorkflowNode();
        copy.setCgformHeadId(source.getCgformHeadId());
        copy.setProcessDefinitionKey(targetProcessKey); // 使用新的流程Key
        copy.setNodeId(source.getNodeId());
        copy.setNodeName(source.getNodeName());
        copy.setEditableFields(source.getEditableFields());
        copy.setReadonlyFields(source.getReadonlyFields());
        copy.setHiddenFields(source.getHiddenFields());
        copy.setRequiredFields(source.getRequiredFields());
        copy.setConditionalPermissions(source.getConditionalPermissions());
        copy.setFormMode(source.getFormMode());
        copy.setCustomButtons(source.getCustomButtons());
        copy.setHiddenButtons(source.getHiddenButtons());
        copy.setSortOrder(source.getSortOrder());
        copy.setStatus(1);
        copy.setRemark("从版本 " + source.getProcessDefinitionKey() + " 复制");
        return copy;
    }
}