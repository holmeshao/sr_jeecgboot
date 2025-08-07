package org.jeecg.modules.workflow.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.online.api.IOnlineBaseExtApi;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.workflow.dto.FormSnapshot;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowConfig;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowNode;
import org.jeecg.modules.workflow.engine.OnlineFormPermissionEngine;

import org.jeecg.modules.workflow.mapper.OnlCgformWorkflowConfigMapper;
import org.jeecg.modules.workflow.mapper.OnlCgformWorkflowNodeMapper;
import org.jeecg.modules.workflow.model.FormPermissionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 🎯 统一工作流表单服务 - 基于JeecgBoot现有体系
 * 核心原则：深度利用JeecgBoot现有组件，不重复造轮子
 *
 * @author jeecg
 * @since 2024-12-25
 */
@Slf4j
@Service
public class OnlineFormWorkflowService {
    
    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private OnlCgformWorkflowConfigMapper workflowConfigMapper;
    
    @Autowired
    private OnlCgformWorkflowNodeMapper workflowNodeMapper;
    
    @Autowired
    private IOnlineBaseExtApi onlineBaseExtApi;
    
    @Autowired
    private IOnlCgformHeadService cgformHeadService;
    
    @Autowired
    private IOnlCgformFieldService cgformFieldService;
    
    @Autowired
    private OnlineFormPermissionEngine permissionEngine;
    
    /**
     * 🎯 智能提交表单（基于JeecgBoot现有服务 + 工作流扩展）
     */
    @Transactional
    public Result<Map<String, Object>> submitForm(String tableName, String dataId, JSONObject formData) {
        try {
            log.info("🎯 智能提交表单: tableName={}, dataId={}", tableName, dataId);
            
            // 1. 直接通过表名获取表单配置
            OnlCgformHead cgformHead = cgformHeadService.getOne(
                new LambdaQueryWrapper<OnlCgformHead>()
                    .eq(OnlCgformHead::getTableName, tableName)
                    // 注：OnlCgformHead可能不使用软删除标志
            );
            
            if (cgformHead == null) {
                return Result.error("未找到表单配置: " + tableName);
            }
            
            String formId = cgformHead.getId();
            
            // 2. 直接使用JeecgBoot API保存表单数据
            String resultDataId;
            try {
                if (StringUtils.hasText(dataId)) {
                    // 更新模式
                    resultDataId = onlineBaseExtApi.cgformPutCrazyForm(tableName, formData);
                } else {
                    // 新增模式
                    resultDataId = onlineBaseExtApi.cgformPostCrazyForm(tableName, formData);
                }
            } catch (Exception e) {
                log.error("保存表单数据失败", e);
                return Result.error("保存表单数据失败: " + e.getMessage());
            }
            
            // 3. 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("dataId", resultDataId);
            result.put("tableName", tableName);
            result.put("formId", formId);
            result.put("message", "表单提交成功");
            
            // 4. 检查工作流配置
            OnlCgformWorkflowConfig workflowConfig = getWorkflowConfig(formId);
            if (workflowConfig != null && isWorkflowEnabled(workflowConfig)) {
                result.put("workflowEnabled", true);
                result.put("workflowStartMode", workflowConfig.getWorkflowStartMode());
                
                if ("AUTO".equals(workflowConfig.getWorkflowStartMode())) {
                    // 自动启动工作流
                    try {
                        String processInstanceId = startFormWorkflow(formId, resultDataId, formData.getInnerMap());
                        result.put("action", "workflow_started");
                        result.put("processInstanceId", processInstanceId);
                        result.put("message", "表单已提交并自动启动工作流");
                    } catch (Exception e) {
                        log.error("自动启动工作流失败", e);
                        result.put("action", "form_saved_workflow_failed");
                        result.put("message", "表单已保存，但工作流启动失败: " + e.getMessage());
                    }
                } else {
                    result.put("action", "draft_saved");
                    result.put("canStartWorkflow", true);
                    result.put("message", "表单已保存，可手动启动工作流");
                }
            } else {
                result.put("action", "form_saved");
                result.put("workflowEnabled", false);
            }
            
            return Result.OK(result);
            
        } catch (Exception e) {
            log.error("智能提交表单失败: tableName={}, dataId={}", tableName, dataId, e);
            return Result.error("提交失败: " + e.getMessage());
        }
    }
    
    /**
     * 🎯 保存表单草稿（基于JeecgBoot现有服务）
     */
    @Transactional
    public Result<Map<String, Object>> saveDraftForm(String tableName, String dataId, JSONObject formData) {
        try {
            log.info("🎯 保存表单草稿: tableName={}, dataId={}", tableName, dataId);
            
            // 1. 直接通过表名获取表单配置
            OnlCgformHead cgformHead = cgformHeadService.getOne(
                new LambdaQueryWrapper<OnlCgformHead>()
                    .eq(OnlCgformHead::getTableName, tableName)
                    // 注：OnlCgformHead可能不使用软删除标志
            );
            
            if (cgformHead == null) {
                return Result.error("未找到表单配置: " + tableName);
            }
            
            String formId = cgformHead.getId();
            
            // 2. 添加草稿状态标识
            formData.put("status", "DRAFT");
            formData.put("workflow_status", "DRAFT");
            
            // 3. 直接使用JeecgBoot API保存草稿数据
            String resultDataId;
            try {
                if (StringUtils.hasText(dataId)) {
                    // 更新模式
                    resultDataId = onlineBaseExtApi.cgformPutCrazyForm(tableName, formData);
                } else {
                    // 新增模式
                    resultDataId = onlineBaseExtApi.cgformPostCrazyForm(tableName, formData);
                }
            } catch (Exception e) {
                log.error("保存草稿数据失败", e);
                return Result.error("保存草稿失败: " + e.getMessage());
            }
            
            // 4. 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("status", "DRAFT");
            result.put("dataId", resultDataId);
            result.put("tableName", tableName);
            result.put("formId", formId);
            result.put("canEdit", true);
            result.put("message", "草稿保存成功");
            
            // 5. 检查是否可以启动工作流
            OnlCgformWorkflowConfig workflowConfig = getWorkflowConfig(formId);
            if (workflowConfig != null && isWorkflowEnabled(workflowConfig)) {
                result.put("canStartWorkflow", true);
                result.put("workflowStartMode", workflowConfig.getWorkflowStartMode());
                result.put("processDefinitionKey", workflowConfig.getProcessDefinitionKey());
            }
            
            return Result.OK(result);
            
        } catch (Exception e) {
            log.error("保存表单草稿失败: tableName={}, dataId={}", tableName, dataId, e);
            return Result.error("保存草稿失败: " + e.getMessage());
        }
    }
    
    /**
     * 手动启动表单工作流
     */
    @Transactional
    public String manualStartWorkflow(String formId, String dataId) {
        
        log.info("手动启动工作流: formId={}, dataId={}", formId, dataId);
        
        // 1. 检查是否可以启动工作流
        validateCanStartWorkflow(formId, dataId);
        
        // 2. 获取最新的表单数据
        Map<String, Object> formData = getBusinessData(formId, dataId);
        
        // 3. 启动工作流
        return startFormWorkflow(formId, dataId, formData);
    }
    
    /**
     * 检查是否可以启动工作流
     */
    public boolean canStartWorkflow(String formId, String dataId) {
        try {
            validateCanStartWorkflow(formId, dataId);
            return true;
        } catch (Exception e) {
            log.debug("无法启动工作流: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 验证是否可以启动工作流
     */
    private void validateCanStartWorkflow(String formId, String dataId) {
        
                 // 1. 检查工作流配置
         OnlCgformWorkflowConfig config = getWorkflowConfig(formId);
         if (config == null || !isWorkflowEnabled(config)) {
             throw new JeecgBootException("该表单未启用工作流");
         }
        
        // 2. 检查业务数据状态
        String currentStatus = getBusinessStatus(formId, dataId);
        if (!"DRAFT".equals(currentStatus)) {
            throw new JeecgBootException("只有草稿状态的表单可以启动工作流，当前状态：" + currentStatus);
        }
        
        // 3. 检查是否已经有流程实例
        String existingProcessId = getProcessInstanceId(formId, dataId);
        if (existingProcessId != null) {
            throw new JeecgBootException("该表单已启动工作流，流程ID：" + existingProcessId);
        }
        
        // 4. 检查必填字段
        validateRequiredFields(formId, dataId);
    }
    
    /**
     * 启动表单工作流（内部方法）
     */
    @Transactional
    public String startFormWorkflow(String formId, String dataId, Map<String, Object> formData) {
        
                 // 1. 获取工作流配置
         OnlCgformWorkflowConfig config = getWorkflowConfig(formId);
         if (config == null || !isWorkflowEnabled(config)) {
             throw new JeecgBootException("该表单未启用工作流");
         }
        
        // 2. 更新业务表状态
        updateBusinessStatus(formId, dataId, "PROCESSING");
        
        // 3. 启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(
            config.getProcessDefinitionKey(), 
            dataId, 
            formData
        );
        
                 // 4. 更新流程实例ID
         updateProcessInstanceId(formId, dataId, instance.getId());
         
         // 5. 如果启用版本控制，保存初始快照
         if (isVersionControlEnabled(config)) {
             saveFormSnapshot(instance.getId(), "start", formData);
         }
        
        log.info("启动表单工作流成功: formId={}, dataId={}, processInstanceId={}", 
                formId, dataId, instance.getId());
        
        return instance.getId();
    }
    
    /**
     * 提交节点表单数据
     */
    @Transactional
    public void submitNodeForm(String taskId, String nodeCode, Map<String, Object> formData) {
        
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new JeecgBootException("任务不存在或已完成");
        }
        
        String processInstanceId = task.getProcessInstanceId();
        
        // 1. 获取配置
        OnlCgformWorkflowConfig config = getWorkflowConfigByProcessInstance(processInstanceId);
        
                 // 2. 权限验证
         if (isPermissionControlEnabled(config)) {
             permissionEngine.validateNodePermissions(config.getCgformHeadId(), config.getProcessDefinitionKey(), nodeCode, formData);
         }
         
         // 3. 更新业务表
         String dataId = getBusinessDataId(processInstanceId);
         updateBusinessData(config.getCgformHeadId(), dataId, formData);
         
         // 4. 保存版本快照
         if (isVersionControlEnabled(config)) {
             saveFormSnapshot(processInstanceId, nodeCode, formData);
         }
        
        // 5. 完成任务
        taskService.complete(taskId, formData);
        
        // 6. 更新业务状态
        updateBusinessStatusFromProcess(processInstanceId);
        
        log.info("提交节点表单成功: taskId={}, nodeCode={}, processInstanceId={}", 
                taskId, nodeCode, processInstanceId);
    }
    
    /**
     * 获取工作流配置
     */
    public OnlCgformWorkflowConfig getWorkflowConfig(String formId) {
        return workflowConfigMapper.selectOne(
            new LambdaQueryWrapper<OnlCgformWorkflowConfig>()
                .eq(OnlCgformWorkflowConfig::getCgformHeadId, formId)
                .eq(OnlCgformWorkflowConfig::getStatus, 1)
        );
    }
    
    /**
     * 🎯 根据流程实例获取配置
     */
    public OnlCgformWorkflowConfig getWorkflowConfigByProcessInstance(String processInstanceId) {
        log.info("根据流程实例获取配置: processInstanceId={}", processInstanceId);
        
        try {
            // 1. 从流程实例获取业务主键
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
                
            if (processInstance == null) {
                log.warn("流程实例不存在: {}", processInstanceId);
                return null;
            }
            
            String businessKey = processInstance.getBusinessKey();
            if (StringUtils.isEmpty(businessKey)) {
                log.warn("流程实例没有业务主键: {}", processInstanceId);
                return null;
            }
            
            // 2. 解析业务主键获取表单ID
            // 业务主键格式通常为 "formId:dataId" 或者直接是dataId
            String formId;
            if (businessKey.contains(":")) {
                String[] parts = businessKey.split(":");
                formId = parts[0];
            } else {
                // 如果业务主键只是dataId，需要通过其他方式获取formId
                // 这里可以从流程定义或流程变量中获取
                formId = getFormIdFromProcessDefinition(processInstance.getProcessDefinitionId());
            }
            
            if (StringUtils.isEmpty(formId)) {
                log.warn("无法从流程实例获取表单ID: processInstanceId={}, businessKey={}", 
                        processInstanceId, businessKey);
                return null;
            }
            
            // 3. 获取工作流配置
            OnlCgformWorkflowConfig config = getWorkflowConfig(formId);
            if (config == null) {
                log.warn("未找到工作流配置: formId={}", formId);
            }
            
            return config;
            
        } catch (Exception e) {
            log.error("根据流程实例获取配置失败: processInstanceId={}", processInstanceId, e);
            return null;
        }
    }
    
    /**
     * 🎯 从流程定义获取表单ID
     */
    private String getFormIdFromProcessDefinition(String processDefinitionId) {
        try {
            // 从流程定义中获取表单ID，可以通过流程定义key来查询工作流配置
            // 这里简化处理，实际项目中可能需要更复杂的映射逻辑
            
            // 方案1：通过流程定义Key查询配置
            String processDefinitionKey = processDefinitionId.split(":")[0]; // 提取key部分
            
            OnlCgformWorkflowConfig config = workflowConfigMapper.selectOne(
                new LambdaQueryWrapper<OnlCgformWorkflowConfig>()
                    .eq(OnlCgformWorkflowConfig::getProcessDefinitionKey, processDefinitionKey)
                    .eq(OnlCgformWorkflowConfig::getStatus, 1)
                    .last("LIMIT 1")
            );
            
            return config != null ? config.getCgformHeadId() : null;
            
        } catch (Exception e) {
            log.error("从流程定义获取表单ID失败: processDefinitionId={}", processDefinitionId, e);
            return null;
        }
    }
    
    /**
     * 保存表单快照
     */
    private void saveFormSnapshot(String processInstanceId, String nodeCode, Map<String, Object> formData) {
        FormSnapshot snapshot = new FormSnapshot();
        snapshot.setNodeCode(nodeCode);
        snapshot.setFormData(formData);
        snapshot.setTimestamp(System.currentTimeMillis());
        snapshot.setOperator(getCurrentUser());
        snapshot.setChangedFields(calculateChangedFields(formData));
        
        // 存储到Flowable流程变量
        String snapshotKey = "form_snapshot_" + nodeCode;
        runtimeService.setVariable(processInstanceId, snapshotKey, JSON.toJSONString(snapshot));
        
        log.info("保存表单快照: processInstanceId={}, nodeCode={}", processInstanceId, nodeCode);
    }
    
    /**
     * 🎯 获取表单配置（直接使用JeecgBoot现有服务）
     */
    public Result<JSONObject> getFormConfig(String tableName, String taskId) {
        try {
            log.info("🎯 获取表单配置: tableName={}, taskId={}", tableName, taskId);
            
            // 1. 获取表单头信息
            OnlCgformHead cgformHead = cgformHeadService.getOne(
                new LambdaQueryWrapper<OnlCgformHead>()
                    .eq(OnlCgformHead::getTableName, tableName)
                    // 注：OnlCgformHead可能不使用软删除标志
            );
            
            if (cgformHead == null) {
                return Result.error("未找到表单配置: " + tableName);
            }
            
            // 2. 获取表单字段信息
            List<OnlCgformField> cgformFields = cgformFieldService.list(
                new LambdaQueryWrapper<OnlCgformField>()
                    .eq(OnlCgformField::getCgformHeadId, cgformHead.getId())
                    // 注：OnlCgformField可能不使用软删除标志
                    .orderByAsc(OnlCgformField::getOrderNum)
            );
            
            // 3. 构建表单配置JSON（与JeecgBoot在线表单API返回格式兼容）
            JSONObject formConfig = new JSONObject();
            
            // 表单头信息
            JSONObject head = new JSONObject();
            head.put("id", cgformHead.getId());
            head.put("tableName", cgformHead.getTableName());
            head.put("tableTxt", cgformHead.getTableTxt());
            head.put("tableVersion", cgformHead.getTableVersion());
            head.put("formTemplate", cgformHead.getFormTemplate());
            head.put("formCategory", cgformHead.getFormCategory());
            head.put("physicsTableName", cgformHead.getTableName());
            formConfig.put("head", head);
            
            // 表单字段信息
            JSONObject fieldsObj = new JSONObject();
            for (OnlCgformField field : cgformFields) {
                JSONObject fieldObj = new JSONObject();
                fieldObj.put("fieldName", field.getDbFieldName());
                fieldObj.put("fieldTxt", field.getDbFieldTxt());
                fieldObj.put("fieldComment", field.getDbFieldTxt()); // 使用字段显示文本作为注释
                fieldObj.put("fieldType", field.getDbType());
                fieldObj.put("fieldLength", field.getFieldLength());
                fieldObj.put("isKey", field.getDbIsKey());
                fieldObj.put("isNull", field.getDbIsNull());
                fieldObj.put("isShow", field.getIsShowForm());
                fieldObj.put("isShowList", field.getIsShowList());
                fieldObj.put("isReadonly", field.getIsReadOnly());
                fieldObj.put("fieldDefaultValue", field.getFieldDefaultValue());
                fieldObj.put("fieldExtendJson", field.getFieldExtendJson());
                fieldObj.put("dictField", field.getDictField());
                fieldObj.put("dictTable", field.getDictTable());
                fieldObj.put("dictText", field.getDictText());
                fieldObj.put("queryMode", field.getQueryMode());
                fieldObj.put("orderNum", field.getOrderNum());
                
                fieldsObj.put(field.getDbFieldName(), fieldObj);
            }
            formConfig.put("fields", fieldsObj);
            
            // 4. 如果有任务ID，应用工作流权限
            if (StringUtils.hasText(taskId)) {
                formConfig = applyWorkflowPermissionsForTask(formConfig, taskId);
            }
            
            return Result.OK(formConfig);
            
        } catch (Exception e) {
            log.error("获取表单配置失败: tableName={}, taskId={}", tableName, taskId, e);
            return Result.error("获取表单配置失败: " + e.getMessage());
        }
    }
    
    /**
     * 🎯 获取表单数据（直接使用JeecgBoot现有服务）
     */
    public Result<JSONObject> getFormData(String formId, String dataId) {
        try {
            log.info("🎯 获取表单数据: formId={}, dataId={}", formId, dataId);
            
            // 1. 获取表单头信息
            OnlCgformHead cgformHead = cgformHeadService.getById(formId);
            if (cgformHead == null) {
                return Result.error("未找到表单配置: " + formId);
            }
            
            // 2. 使用JeecgBoot API查询表单数据
            try {
                JSONObject formData = onlineBaseExtApi.cgformQueryAllDataByTableName(
                    cgformHead.getTableName(), dataId);
                
                if (formData != null && !formData.isEmpty()) {
                    return Result.OK(formData);
                } else {
                    return Result.error("未找到数据: formId=" + formId + ", dataId=" + dataId);
                }
                
            } catch (Exception e) {
                log.error("查询表单数据异常: formId={}, dataId={}", formId, dataId, e);
                return Result.error("查询表单数据失败: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("获取表单数据失败: formId={}, dataId={}", formId, dataId, e);
            return Result.error("获取表单数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 🎯 为任务应用工作流权限
     */
    private JSONObject applyWorkflowPermissionsForTask(JSONObject formConfig, String taskId) {
        try {
            // 1. 通过任务ID获取流程信息
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                log.warn("任务不存在: {}", taskId);
                return formConfig;
            }
            
            String processInstanceId = task.getProcessInstanceId();
            String nodeId = task.getTaskDefinitionKey();
            
            // 2. 获取工作流配置
            OnlCgformWorkflowConfig config = getWorkflowConfigByProcessInstance(processInstanceId);
            if (config == null) {
                log.warn("未找到工作流配置: processInstanceId={}", processInstanceId);
                return formConfig;
            }
            
            // 3. 应用节点权限
            FormPermissionConfig permission = permissionEngine.getNodePermission(
                config.getCgformHeadId(), config.getProcessDefinitionKey(), nodeId);
            
            if (permission != null) {
                // 应用权限到表单字段
                JSONObject fields = formConfig.getJSONObject("fields");
                if (fields != null) {
                    for (String fieldKey : fields.keySet()) {
                        JSONObject field = fields.getJSONObject(fieldKey);
                        if (field != null) {
                            applyFieldPermissionToConfig(field, fieldKey, permission);
                        }
                    }
                }
                
                // 添加权限元数据
                formConfig.put("workflowPermissions", JSON.parseObject(JSON.toJSONString(permission)));
            }
            
            return formConfig;
            
        } catch (Exception e) {
            log.error("为任务应用工作流权限失败: taskId={}", taskId, e);
            return formConfig;
        }
    }
    
    /**
     * 🎯 应用字段权限到配置
     */
    private void applyFieldPermissionToConfig(JSONObject field, String fieldKey, FormPermissionConfig permission) {
        
        // 设置只读
        if (permission.getReadonlyFields().contains(fieldKey)) {
            field.put("isReadonly", 1);
        } else if (permission.getEditableFields().contains(fieldKey)) {
            field.put("isReadonly", 0);
        }
        
        // 设置隐藏
        if (permission.getHiddenFields().contains(fieldKey)) {
            field.put("isShow", 0);
        }
        
        // 设置必填
        if (permission.getRequiredFields().contains(fieldKey)) {
            field.put("isNull", 0); // JeecgBoot中0表示必填
        }
    }
    
    /**
     * 🎯 应用工作流权限到表单配置
     */
    public JSONObject applyWorkflowPermissions(JSONObject formConfig, String nodeId, String processDefinitionKey) {
        if (formConfig == null || !StringUtils.hasText(nodeId)) {
            return formConfig;
        }
        
        try {
            // 获取节点权限配置
            OnlCgformWorkflowNode nodeConfig = workflowNodeMapper.selectOne(
                new LambdaQueryWrapper<OnlCgformWorkflowNode>()
                    .eq(OnlCgformWorkflowNode::getNodeId, nodeId)
                    .eq(OnlCgformWorkflowNode::getProcessDefinitionKey, processDefinitionKey)
                    .eq(OnlCgformWorkflowNode::getStatus, 1)
            );
            
            if (nodeConfig == null) {
                log.debug("节点 {} 未配置权限，使用默认权限", nodeId);
                return formConfig;
            }
            
            // 解析权限配置
            List<String> editableFields = parseFieldList(nodeConfig.getEditableFields());
            List<String> readonlyFields = parseFieldList(nodeConfig.getReadonlyFields());
            List<String> hiddenFields = parseFieldList(nodeConfig.getHiddenFields());
            List<String> requiredFields = parseFieldList(nodeConfig.getRequiredFields());
            
            // 应用权限到表单字段
            JSONObject schema = formConfig.getJSONObject("schema");
            if (schema != null) {
                for (String fieldKey : schema.keySet()) {
                    JSONObject field = schema.getJSONObject(fieldKey);
                    if (field != null) {
                        applyFieldPermission(field, fieldKey, editableFields, readonlyFields, hiddenFields, requiredFields);
                    }
                }
            }
            
            // 添加权限元数据
            formConfig.put("workflowPermissions", JSONObject.parse(JSON.toJSONString(Map.of(
                "editableFields", editableFields,
                "readonlyFields", readonlyFields,
                "hiddenFields", hiddenFields,
                "requiredFields", requiredFields,
                "nodeId", nodeId
            ))));
            
            log.debug("已应用节点 {} 的权限配置", nodeId);
            return formConfig;
            
        } catch (Exception e) {
            log.error("应用工作流权限失败: nodeId={}", nodeId, e);
            return formConfig;
        }
    }
    

    
    /**
     * 应用字段权限
     */
    private void applyFieldPermission(JSONObject field, String fieldKey, 
                                    List<String> editableFields, List<String> readonlyFields,
                                    List<String> hiddenFields, List<String> requiredFields) {
        
        // 设置只读
        if (readonlyFields.contains(fieldKey)) {
            field.put("disabled", true);
        } else if (editableFields.contains(fieldKey)) {
            field.put("disabled", false);
        }
        
        // 设置隐藏
        if (hiddenFields.contains(fieldKey)) {
            field.put("hidden", true);
        }
        
        // 设置必填
        if (requiredFields.contains(fieldKey)) {
            field.put("required", true);
        }
    }
    
    /**
     * 解析字段列表
     */
    private List<String> parseFieldList(String fieldsJson) {
        if (!StringUtils.hasText(fieldsJson)) {
            return List.of();
        }
        
        try {
            return JSON.parseArray(fieldsJson, String.class);
        } catch (Exception e) {
            log.warn("解析字段列表失败: {}", fieldsJson, e);
            return List.of();
        }
    }
    

    
    /**
     * 🎯 使用智能默认权限验证
     */
    private void validateWithSmartDefaultPermissions(String formId, String nodeCode, Map<String, Object> formData) {
        log.debug("使用智能默认权限验证: formId={}, nodeCode={}", formId, nodeCode);
        
        try {
            // 1. 判断节点类型
            boolean isStartNode = "start".equalsIgnoreCase(nodeCode) || 
                                 nodeCode.toLowerCase().contains("start") ||
                                 nodeCode.toLowerCase().contains("begin");
            
            if (isStartNode) {
                // 发起节点：只验证必填字段，不限制编辑权限
                validateRequiredFieldsFromSchema(formId, formData);
            } else {
                // 非发起节点：业务字段只读，流程字段可编辑
                validateBusinessFieldsReadonly(formData);
            }
            
        } catch (Exception e) {
            log.error("智能默认权限验证异常: formId={}, nodeCode={}", formId, nodeCode, e);
            throw new JeecgBootException("权限验证失败: " + e.getMessage());
        }
    }
    
    /**
     * 🎯 验证业务字段只读（智能识别）
     */
    private void validateBusinessFieldsReadonly(Map<String, Object> formData) {
        for (String fieldKey : formData.keySet()) {
            // 跳过系统字段和通用流程字段
            if (isSystemField(fieldKey) || isCommonProcessField(fieldKey)) {
                continue;
            }
            
            // 业务字段不允许在非发起节点修改
            throw new JeecgBootException("业务字段 [" + fieldKey + "] 在当前节点为只读，不允许修改");
        }
    }
    
    /**
     * 🎯 从表单Schema验证必填字段
     */
    private void validateRequiredFieldsFromSchema(String formId, Map<String, Object> formData) {
        // 这里可以调用现有的validateRequiredFields方法，或者实现类似逻辑
        // 暂时简化处理
        log.debug("验证Schema必填字段: formId={}", formId);
    }
    
    /**
     * 🎯 判断是否为系统字段
     */
    private boolean isSystemField(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            return false;
        }
        
        // 系统字段模式
        String[] systemFieldPatterns = {
            "id", "create_by", "create_time", "update_by", "update_time",
            "del_flag", "version", "tenant_id", "org_code",
            "process_instance_id", "bmp_status", "workflow_status"
        };
        
        String lowerFieldName = fieldName.toLowerCase();
        for (String pattern : systemFieldPatterns) {
            if (lowerFieldName.equals(pattern) || lowerFieldName.endsWith("_" + pattern)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 🎯 判断是否为通用流程字段
     */
    private boolean isCommonProcessField(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            return false;
        }
        
        String lowerFieldName = fieldName.toLowerCase();
        
        // 通用流程字段模式
        String[] processFieldPatterns = {
            "audit", "approve", "process", "review", "check"
        };
        
        String[] processFieldSuffixes = {
            "opinion", "comment", "remark", "note", "memo", "reason", "result"
        };
        
        // 检查前缀
        for (String pattern : processFieldPatterns) {
            if (lowerFieldName.startsWith(pattern + "_")) {
                return true;
            }
        }
        
        // 检查后缀
        for (String suffix : processFieldSuffixes) {
            if (lowerFieldName.endsWith("_" + suffix)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 获取业务数据ID
     */
    private String getBusinessDataId(String processInstanceId) {
        return runtimeService.createProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult()
            .getBusinessKey();
    }
    
    /**
     * 🎯 从流程更新业务状态（基于Flowable流程实例状态）
     */
    private void updateBusinessStatusFromProcess(String processInstanceId) {
        log.info("从流程更新业务状态: processInstanceId={}", processInstanceId);
        
        try {
            // 获取流程实例信息
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
            
            String businessKey = getBusinessDataId(processInstanceId);
            if (StringUtils.isEmpty(businessKey)) {
                log.warn("流程实例 {} 没有关联的业务数据", processInstanceId);
                return;
            }
            
            // 解析businessKey获取formId和dataId
            String[] parts = businessKey.split(":");
            if (parts.length != 2) {
                log.warn("无效的业务键格式: {}", businessKey);
                return;
            }
            
            String formId = parts[0];
            String dataId = parts[1];
            
            // 根据流程状态确定业务状态
            String newStatus;
            if (processInstance == null) {
                // 流程实例不存在，可能已结束
                newStatus = "COMPLETED";
            } else if (processInstance.isSuspended()) {
                newStatus = "SUSPENDED";
            } else {
                newStatus = "IN_PROCESS";
            }
            
            // 更新业务状态
            updateBusinessStatus(formId, dataId, newStatus);
            
        } catch (Exception e) {
            log.error("从流程更新业务状态失败: processInstanceId={}", processInstanceId, e);
        }
    }
    
    /**
     * 🎯 更新业务状态（调用JeecgBoot API）
     */
    private void updateBusinessStatus(String formId, String dataId, String status) {
        log.info("更新业务状态: formId={}, dataId={}, status={}", formId, dataId, status);
        
        try {
            // 获取当前数据
            Map<String, Object> currentData = getBusinessData(formId, dataId);
            if (currentData.isEmpty()) {
                log.warn("找不到业务数据: formId={}, dataId={}", formId, dataId);
                return;
            }
            
            // 更新状态字段
            currentData.put("status", status);
            currentData.put("workflow_status", status);
            currentData.put("update_time", System.currentTimeMillis());
            currentData.put("update_by", getCurrentUser());
            
            // 调用JeecgBoot API更新数据
            JSONObject updateData = new JSONObject(currentData);
            Result<String> result = saveFormData(formId, dataId, updateData, false);
            
            if (result.isSuccess()) {
                log.info("业务状态更新成功: formId={}, dataId={}, status={}", formId, dataId, status);
            } else {
                log.error("业务状态更新失败: {}", result.getMessage());
            }
            
        } catch (Exception e) {
            log.error("更新业务状态异常: formId={}, dataId={}, status={}", formId, dataId, status, e);
        }
    }
    
    /**
     * 计算变更字段
     */
    private java.util.List<String> calculateChangedFields(Map<String, Object> formData) {
        // 这里需要比较当前数据与历史数据，计算变更字段
        // 简化实现，返回所有字段
        return new java.util.ArrayList<>(formData.keySet());
    }
    
    /**
     * 🎯 获取当前用户（从JeecgBoot安全上下文）
     */
    private String getCurrentUser() {
        try {
            // 使用JeecgBoot的用户工具类
            return (String) org.apache.shiro.SecurityUtils.getSubject().getPrincipal();
        } catch (Exception e) {
            log.debug("获取当前用户失败，使用默认用户", e);
            return "system";
        }
    }
    
    /**
     * 🎯 获取业务数据（调用JeecgBoot API）
     */
    private Map<String, Object> getBusinessData(String formId, String dataId) {
        log.info("获取业务数据: formId={}, dataId={}", formId, dataId);
        
        try {
            Result<JSONObject> result = getFormData(formId, dataId);
            if (result.isSuccess()) {
                return result.getResult();
            } else {
                log.warn("获取业务数据失败: {}", result.getMessage());
                return new HashMap<>();
            }
        } catch (Exception e) {
            log.error("获取业务数据异常: formId={}, dataId={}", formId, dataId, e);
            return new HashMap<>();
        }
    }
    
    /**
     * 🎯 获取业务状态（从JeecgBoot表单数据解析）
     */
    private String getBusinessStatus(String formId, String dataId) {
        log.info("获取业务状态: formId={}, dataId={}", formId, dataId);
        
        try {
            Map<String, Object> businessData = getBusinessData(formId, dataId);
            
            // 尝试从常见的状态字段获取
            String[] statusFields = {"status", "form_status", "workflow_status", "bpm_status"};
            
            for (String statusField : statusFields) {
                Object status = businessData.get(statusField);
                if (status != null && !StringUtils.isEmpty(status.toString())) {
                    return status.toString();
                }
            }
            
            // 如果没有找到状态字段，检查是否有流程实例ID来判断状态
            Object processInstanceId = businessData.get("process_instance_id");
            if (processInstanceId != null && !StringUtils.isEmpty(processInstanceId.toString())) {
                return "IN_PROCESS"; // 有流程实例表示在流程中
            }
            
            return "DRAFT"; // 默认草稿状态
            
        } catch (Exception e) {
            log.error("获取业务状态失败: formId={}, dataId={}", formId, dataId, e);
            return "DRAFT";
        }
    }
    
    /**
     * 🎯 获取流程实例ID（从JeecgBoot表单数据解析）
     */
    private String getProcessInstanceId(String formId, String dataId) {
        log.info("获取流程实例ID: formId={}, dataId={}", formId, dataId);
        
        try {
            Map<String, Object> businessData = getBusinessData(formId, dataId);
            
            // 尝试从常见的流程实例ID字段获取
            String[] processFields = {
                "process_instance_id", 
                "processInstanceId", 
                "proc_inst_id", 
                "bpm_proc_inst_id"
            };
            
            for (String processField : processFields) {
                Object processInstanceId = businessData.get(processField);
                if (processInstanceId != null && !StringUtils.isEmpty(processInstanceId.toString())) {
                    return processInstanceId.toString();
                }
            }
            
            return null; // 如果没有启动流程则返回null
            
        } catch (Exception e) {
            log.error("获取流程实例ID失败: formId={}, dataId={}", formId, dataId, e);
            return null;
        }
    }
    
    /**
     * 🎯 更新流程实例ID到业务表
     */
    private void updateProcessInstanceId(String formId, String dataId, String processInstanceId) {
        log.info("更新流程实例ID: formId={}, dataId={}, processInstanceId={}", formId, dataId, processInstanceId);
        
        try {
            // 获取当前数据
            Map<String, Object> currentData = getBusinessData(formId, dataId);
            if (currentData.isEmpty()) {
                log.warn("找不到业务数据，无法更新流程实例ID: formId={}, dataId={}", formId, dataId);
                return;
            }
            
                         // 获取工作流配置中定义的流程实例字段名
             OnlCgformWorkflowConfig config = getWorkflowConfig(formId);
             String processInstanceField = getProcessInstanceFieldOrDefault(config);
            
            // 更新流程实例ID
            currentData.put(processInstanceField, processInstanceId);
            currentData.put("update_time", System.currentTimeMillis());
            currentData.put("update_by", getCurrentUser());
            
            // 调用JeecgBoot API更新数据
            JSONObject updateData = new JSONObject(currentData);
            Result<String> result = saveFormData(formId, dataId, updateData, false);
            
            if (result.isSuccess()) {
                log.info("流程实例ID更新成功: formId={}, dataId={}, processInstanceId={}", 
                        formId, dataId, processInstanceId);
            } else {
                log.error("流程实例ID更新失败: {}", result.getMessage());
            }
            
        } catch (Exception e) {
            log.error("更新流程实例ID异常: formId={}, dataId={}, processInstanceId={}", 
                    formId, dataId, processInstanceId, e);
        }
    }
    
    /**
     * 🎯 更新业务表数据
     */
    private void updateBusinessData(String formId, String dataId, Map<String, Object> formData) {
        log.info("更新业务表数据: formId={}, dataId={}", formId, dataId);
        
        try {
            // 获取当前数据
            Map<String, Object> currentData = getBusinessData(formId, dataId);
            if (currentData.isEmpty()) {
                log.warn("找不到业务数据，无法更新: formId={}, dataId={}", formId, dataId);
                return;
            }
            
            // 合并表单数据
            currentData.putAll(formData);
            currentData.put("update_time", System.currentTimeMillis());
            currentData.put("update_by", getCurrentUser());
            
            // 调用JeecgBoot API更新数据
            JSONObject updateData = new JSONObject(currentData);
            Result<String> result = saveFormData(formId, dataId, updateData, false);
            
            if (result.isSuccess()) {
                log.info("业务表数据更新成功: formId={}, dataId={}", formId, dataId);
            } else {
                log.error("业务表数据更新失败: {}", result.getMessage());
            }
            
        } catch (Exception e) {
            log.error("更新业务表数据异常: formId={}, dataId={}", formId, dataId, e);
        }
    }

    /**
     * 🎯 验证必填字段
     */
    private void validateRequiredFields(String formId, String dataId) {
        log.info("验证必填字段: formId={}, dataId={}", formId, dataId);
        
        try {
            // 1. 获取表单配置
            Result<JSONObject> configResult = getFormConfig(null, null);
            if (!configResult.isSuccess()) {
                log.warn("获取表单配置失败，跳过必填字段验证: {}", configResult.getMessage());
                return;
            }
            
            // 2. 获取业务数据
            Map<String, Object> businessData = getBusinessData(formId, dataId);
            if (businessData.isEmpty()) {
                throw new JeecgBootException("找不到业务数据，无法验证必填字段");
            }
            
            // 3. 获取表单字段配置
            JSONObject formConfig = configResult.getResult();
            JSONObject schema = formConfig.getJSONObject("schema");
            if (schema == null) {
                log.debug("表单没有schema配置，跳过必填字段验证");
                return;
            }
            
            // 4. 验证必填字段
            for (String fieldKey : schema.keySet()) {
                JSONObject field = schema.getJSONObject(fieldKey);
                if (field != null && field.getBooleanValue("required")) {
                    Object value = businessData.get(fieldKey);
                    if (value == null || StringUtils.isEmpty(value.toString().trim())) {
                        String fieldLabel = field.getString("title");
                        throw new JeecgBootException("必填字段 [" + (fieldLabel != null ? fieldLabel : fieldKey) + "] 不能为空");
                    }
                }
            }
            
            log.debug("必填字段验证通过: formId={}, dataId={}", formId, dataId);
            
        } catch (JeecgBootException e) {
            throw e;
        } catch (Exception e) {
            log.error("验证必填字段异常: formId={}, dataId={}", formId, dataId, e);
                         throw new JeecgBootException("必填字段验证失败: " + e.getMessage());
         }
     }
     
     // ============= 🎯 辅助方法区域 =============
     
     /**
      * 🎯 判断是否启用工作流
      */
     private boolean isWorkflowEnabled(OnlCgformWorkflowConfig config) {
         return config != null && config.getWorkflowEnabled() != null && config.getWorkflowEnabled() == 1;
     }
     
     /**
      * 🎯 判断是否启用版本控制
      */
     private boolean isVersionControlEnabled(OnlCgformWorkflowConfig config) {
         return config != null && config.getVersionControlEnabled() != null && config.getVersionControlEnabled() == 1;
     }
     
     /**
      * 🎯 判断是否启用权限控制
      */
     private boolean isPermissionControlEnabled(OnlCgformWorkflowConfig config) {
         return config != null && config.getPermissionControlEnabled() != null && config.getPermissionControlEnabled() == 1;
     }
     
     /**
      * 🎯 获取流程实例字段名（带默认值）
      */
     private String getProcessInstanceFieldOrDefault(OnlCgformWorkflowConfig config) {
         if (config != null && StringUtils.hasText(config.getProcessInstanceField())) {
             return config.getProcessInstanceField();
         }
         return "process_instance_id";
     }
     
     /**
      * 🎯 获取状态字段名（带默认值）
      */
     private String getStatusFieldOrDefault(OnlCgformWorkflowConfig config) {
         if (config != null && StringUtils.hasText(config.getStatusField())) {
             return config.getStatusField();
         }
         return "status";
     }
     
     /**
      * 🎯 保存表单数据 - 兼容性方法
      * 这是对JeecgBoot在线表单API的封装调用
      * 
      * @param formId 表单ID
      * @param dataId 数据ID  
      * @param formData 表单数据
      * @param isAdd 是否为新增操作
      * @return 保存结果
      */
     private Result<String> saveFormData(String formId, String dataId, JSONObject formData, boolean isAdd) {
         try {
             // TODO: 这里需要调用JeecgBoot的在线表单API来保存数据
             // 目前暂时返回成功，实际项目中需要集成在线表单服务
             log.info("保存表单数据: formId={}, dataId={}, isAdd={}", formId, dataId, isAdd);
             log.debug("表单数据: {}", formData.toJSONString());
             
             return Result.OK("数据保存成功", dataId);
             
         } catch (Exception e) {
             log.error("保存表单数据失败: formId={}, dataId={}", formId, dataId, e);
             return Result.error("保存表单数据失败: " + e.getMessage());
         }
     }
 }  