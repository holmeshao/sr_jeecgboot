package org.jeecg.modules.workflow.engine;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowNode;
import org.jeecg.modules.workflow.mapper.OnlCgformWorkflowNodeMapper;
import org.jeecg.modules.workflow.model.FormPermissionConfig;
import org.jeecg.modules.workflow.strategy.DefaultFieldPermissionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 🎯 在线表单权限引擎
 * 核心职责：统一管理表单字段权限，支持显式配置和智能默认
 * 
 * 功能特性：
 * 1. 优先使用显式配置（来自Flowable设计器或手工配置）
 * 2. 智能默认策略兜底（通用字段自动识别）
 * 3. 权限应用到JeecgBoot在线表单
 * 4. 权限验证和校验
 * 
 * @author JeecgBoot工作流集成
 * @version 1.0
 */
@Slf4j
@Component
public class OnlineFormPermissionEngine {

    @Autowired
    private OnlCgformWorkflowNodeMapper nodeConfigMapper;

    @Autowired
    private DefaultFieldPermissionStrategy defaultStrategy;

    @Autowired
    private IOnlCgformHeadService onlCgformHeadService;

    @Autowired
    private IOnlCgformFieldService onlCgformFieldService;

    // =============== 核心权限获取方法 ===============

    /**
     * 🎯 获取节点权限配置（统一入口）
     * 
     * 优先级：显式配置 > 智能默认策略
     * 
     * @param formId 表单ID
     * @param processDefinitionKey 流程定义Key  
     * @param nodeId 节点ID
     * @return 权限配置
     */
    public FormPermissionConfig getNodePermission(String formId, String processDefinitionKey, String nodeId) {
        log.debug("获取节点权限配置：formId={}, processKey={}, nodeId={}", formId, processDefinitionKey, nodeId);

        try {
            // 1. 优先查询显式配置
            FormPermissionConfig explicitConfig = getExplicitPermissionConfig(formId, processDefinitionKey, nodeId);
            if (explicitConfig != null) {
                log.debug("使用显式权限配置：{} 个可编辑字段", explicitConfig.getEditableFields().size());
                return explicitConfig;
            }

            // 2. 使用智能默认策略
            log.debug("未找到显式权限配置，使用智能默认策略");
            FormPermissionConfig defaultConfig = defaultStrategy.generateDefaultPermission(formId, nodeId);
            
            log.debug("智能默认权限生成完成：可编辑{}个，只读{}个，隐藏{}个", 
                     defaultConfig.getEditableFields().size(),
                     defaultConfig.getReadonlyFields().size(),
                     defaultConfig.getHiddenFields().size());

            return defaultConfig;

        } catch (Exception e) {
            log.error("获取节点权限配置失败", e);
            return createSafeDefaultConfig();
        }
    }

    /**
     * 🎯 获取显式权限配置
     * 从数据库中查询手工配置或Flowable设计器中配置的权限
     */
    private FormPermissionConfig getExplicitPermissionConfig(String formId, String processDefinitionKey, String nodeId) {
        
        // 优先按表单ID+节点ID查询
        OnlCgformWorkflowNode nodeConfig = nodeConfigMapper.selectByFormAndNode(formId, nodeId);
        
        // 如果没找到，按流程定义Key+节点ID查询
        if (nodeConfig == null && oConvertUtils.isNotEmpty(processDefinitionKey)) {
            nodeConfig = nodeConfigMapper.selectByProcessAndNode(processDefinitionKey, nodeId);
        }

        if (nodeConfig == null || !nodeConfig.isActive()) {
            return null;
        }

        return parseExplicitConfig(nodeConfig);
    }

    /**
     * 🎯 解析显式配置
     * 将数据库中的JSON配置解析为权限配置对象
     */
    private FormPermissionConfig parseExplicitConfig(OnlCgformWorkflowNode nodeConfig) {
        FormPermissionConfig config = new FormPermissionConfig();

        try {
            // 解析各种权限字段
            if (oConvertUtils.isNotEmpty(nodeConfig.getEditableFields())) {
                List<String> editableFields = JSON.parseArray(nodeConfig.getEditableFields(), String.class);
                config.setEditableFields(editableFields != null ? editableFields : Collections.emptyList());
            }

            if (oConvertUtils.isNotEmpty(nodeConfig.getReadonlyFields())) {
                List<String> readonlyFields = JSON.parseArray(nodeConfig.getReadonlyFields(), String.class);
                config.setReadonlyFields(readonlyFields != null ? readonlyFields : Collections.emptyList());
            }

            if (oConvertUtils.isNotEmpty(nodeConfig.getHiddenFields())) {
                List<String> hiddenFields = JSON.parseArray(nodeConfig.getHiddenFields(), String.class);
                config.setHiddenFields(hiddenFields != null ? hiddenFields : Collections.emptyList());
            }

            if (oConvertUtils.isNotEmpty(nodeConfig.getRequiredFields())) {
                List<String> requiredFields = JSON.parseArray(nodeConfig.getRequiredFields(), String.class);
                config.setRequiredFields(requiredFields != null ? requiredFields : Collections.emptyList());
            }

            // 设置表单模式
            config.setFormMode(nodeConfig.getFormModeOrDefault());

            // 设置条件权限
            config.setConditionalPermissions(nodeConfig.getConditionalPermissions());

            log.debug("解析显式权限配置成功：节点 {} - 可编辑{}个，只读{}个，隐藏{}个", 
                     nodeConfig.getNodeId(),
                     config.getEditableFields().size(),
                     config.getReadonlyFields().size(),
                     config.getHiddenFields().size());

            return config;

        } catch (Exception e) {
            log.error("解析显式权限配置失败：节点配置ID={}", nodeConfig.getId(), e);
            return null;
        }
    }

    // =============== 权限应用方法 ===============

    /**
     * 🎯 应用权限到JeecgBoot在线表单配置
     * 
     * @param formConfig JeecgBoot表单配置
     * @param permission 权限配置
     */
    public void applyPermissionToForm(Object formConfig, FormPermissionConfig permission) {
        if (formConfig == null || permission == null) {
            log.warn("表单配置或权限配置为空，跳过权限应用");
            return;
        }

        try {
            // 这里需要根据JeecgBoot在线表单的具体数据结构来实现
            // 由于需要与现有的在线表单系统集成，具体实现需要查看OnlineForm的数据结构
            
            log.debug("开始应用字段权限到表单配置");
            
            // 如果formConfig是Map类型（常见的JSON配置）
            if (formConfig instanceof Map) {
                Map<String, Object> configMap = (Map<String, Object>) formConfig;
                applyPermissionToConfigMap(configMap, permission);
            }
            
            log.debug("字段权限应用完成");

        } catch (Exception e) {
            log.error("应用字段权限失败", e);
        }
    }

    /**
     * 🎯 应用权限到配置Map
     */
    @SuppressWarnings("unchecked")
    private void applyPermissionToConfigMap(Map<String, Object> configMap, FormPermissionConfig permission) {
        // 获取字段列表
        Object fieldsObj = configMap.get("schema");
        if (fieldsObj == null) {
            fieldsObj = configMap.get("fields");
        }

        if (fieldsObj instanceof List) {
            List<Object> fieldsList = (List<Object>) fieldsObj;
            
            for (Object fieldObj : fieldsList) {
                if (fieldObj instanceof Map) {
                    Map<String, Object> field = (Map<String, Object>) fieldObj;
                    applyPermissionToField(field, permission);
                }
            }
        }
    }

    /**
     * 🎯 应用权限到单个字段
     */
    private void applyPermissionToField(Map<String, Object> field, FormPermissionConfig permission) {
        String fieldName = (String) field.get("key");
        if (oConvertUtils.isEmpty(fieldName)) {
            fieldName = (String) field.get("dbFieldName");
        }
        
        if (oConvertUtils.isEmpty(fieldName)) {
            return;
        }

        // 应用编辑权限
        if (permission.isFieldEditable(fieldName)) {
            field.put("disabled", false);
            field.put("readonly", false);
            log.debug("字段 {} 设置为可编辑", fieldName);
        } else if (permission.isFieldReadonly(fieldName)) {
            field.put("disabled", true);
            field.put("readonly", true);
            log.debug("字段 {} 设置为只读", fieldName);
        }

        // 应用显示权限
        if (permission.isFieldHidden(fieldName)) {
            field.put("hidden", true);
            field.put("visible", false);
            log.debug("字段 {} 设置为隐藏", fieldName);
        } else {
            field.put("hidden", false);
            field.put("visible", true);
        }

        // 应用必填权限
        if (permission.isFieldRequired(fieldName)) {
            field.put("required", true);
            log.debug("字段 {} 设置为必填", fieldName);
        }
    }

    // =============== 权限验证方法 ===============

    /**
     * 🎯 验证节点权限（提交时校验）
     * 
     * @param formId 表单ID
     * @param processDefinitionKey 流程定义Key
     * @param nodeId 节点ID
     * @param formData 表单数据
     */
    public void validateNodePermissions(String formId, String processDefinitionKey, 
                                       String nodeId, Map<String, Object> formData) {
        
        log.debug("开始验证节点权限：formId={}, nodeId={}", formId, nodeId);
        
        FormPermissionConfig permission = getNodePermission(formId, processDefinitionKey, nodeId);
        
        // 检查是否提交了只读字段的修改
        for (String readonlyField : permission.getReadonlyFields()) {
            if (formData.containsKey(readonlyField)) {
                String message = String.format("字段 %s 在当前节点为只读，不允许修改", readonlyField);
                log.warn("权限验证失败：{}", message);
                throw new JeecgBootException(message);
            }
        }

        // 检查必填字段
        for (String requiredField : permission.getRequiredFields()) {
            Object value = formData.get(requiredField);
            if (value == null || oConvertUtils.isEmpty(value.toString())) {
                String message = String.format("字段 %s 为必填项，请填写", requiredField);
                log.warn("权限验证失败：{}", message);
                throw new JeecgBootException(message);
            }
        }

        log.debug("节点权限验证通过：{} 个字段检查完成", formData.size());
    }

    // =============== 配置管理方法 ===============

    /**
     * 🎯 保存节点权限配置
     * 
     * @param formId 表单ID
     * @param processDefinitionKey 流程定义Key
     * @param nodeId 节点ID
     * @param nodeName 节点名称
     * @param permissionConfig 权限配置
     */
    public void saveNodePermissionConfig(String formId, String processDefinitionKey, 
                                        String nodeId, String nodeName, 
                                        FormPermissionConfig permissionConfig) {
        try {
            // 查找现有配置
            OnlCgformWorkflowNode existing = nodeConfigMapper.selectByFormAndNode(formId, nodeId);
            
            if (existing != null) {
                // 更新现有配置
                updateNodeConfig(existing, permissionConfig);
                nodeConfigMapper.updateById(existing);
                log.debug("更新节点权限配置：{}", nodeId);
            } else {
                // 创建新配置
                OnlCgformWorkflowNode newConfig = createNodeConfig(formId, processDefinitionKey, 
                                                                  nodeId, nodeName, permissionConfig);
                nodeConfigMapper.insert(newConfig);
                log.debug("创建节点权限配置：{}", nodeId);
            }

        } catch (Exception e) {
            log.error("保存节点权限配置失败：formId={}, nodeId={}", formId, nodeId, e);
            throw new JeecgBootException("保存节点权限配置失败: " + e.getMessage());
        }
    }

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
    }

    /**
     * 🎯 创建节点配置
     */
    private OnlCgformWorkflowNode createNodeConfig(String formId, String processDefinitionKey, 
                                                  String nodeId, String nodeName, 
                                                  FormPermissionConfig permissionConfig) {
        OnlCgformWorkflowNode config = new OnlCgformWorkflowNode();
        config.setCgformHeadId(formId);
        config.setProcessDefinitionKey(processDefinitionKey);
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
        return config;
    }

    // =============== 辅助方法 ===============

    /**
     * 🎯 创建安全默认配置
     * 当所有策略都失败时使用的保底配置
     */
    private FormPermissionConfig createSafeDefaultConfig() {
        FormPermissionConfig config = new FormPermissionConfig();
        config.setFormMode("VIEW");
        config.setEditableFields(Collections.emptyList());
        config.setReadonlyFields(Collections.emptyList());
        config.setHiddenFields(Collections.emptyList());
        config.setRequiredFields(Collections.emptyList());
        log.warn("使用安全默认权限配置：所有字段只读");
        return config;
    }

    /**
     * 🎯 获取表单所有字段名称
     */
    public List<String> getFormFieldNames(String formId) {
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
            
            return fields.stream()
                    .map(OnlCgformField::getDbFieldName)
                    .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            log.error("获取表单字段失败：formId={}", formId, e);
            return Collections.emptyList();
        }
    }
}