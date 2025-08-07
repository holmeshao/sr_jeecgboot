package org.jeecg.modules.workflow.parser;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.ExtensionElement;
// import org.flowable.bpmn.model.ExtensionElements;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.delegate.event.FlowableProcessEngineEvent;
import org.flowable.engine.delegate.event.impl.FlowableProcessEventImpl;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.workflow.engine.OnlineFormPermissionEngine;
import org.jeecg.modules.workflow.model.FormPermissionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * 🎯 BPMN字段权限解析器
 * 
 * 核心功能：
 * 1. 监听Flowable流程部署事件
 * 2. 解析BPMN模型中的字段权限扩展属性
 * 3. 自动同步权限配置到数据库
 * 4. 支持jeecg:fieldPermissions扩展元素
 * 
 * BPMN扩展属性格式：
 * <bpmn:extensionElements>
 *   <jeecg:fieldPermissions>
 *     <jeecg:editableFields>["field1", "field2"]</jeecg:editableFields>
 *     <jeecg:readonlyFields>["field3", "field4"]</jeecg:readonlyFields>
 *     <jeecg:hiddenFields>["field5"]</jeecg:hiddenFields>
 *     <jeecg:requiredFields>["field1"]</jeecg:requiredFields>
 *   </jeecg:fieldPermissions>
 * </bpmn:extensionElements>
 * 
 * @author JeecgBoot工作流集成
 * @version 1.0
 */
@Slf4j
@Service
public class BpmnFieldPermissionParser {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private OnlineFormPermissionEngine permissionEngine;

    // =============== 事件监听 ===============

    /**
     * 🎯 监听流程部署事件
     * 当流程模型部署时自动解析字段权限配置
     */
    /**
     * 🎯 基于Flowable 7.0新架构的事件监听器
     * 
     * Flowable 7.0引入了IELE（内部执行事件监听器引擎）和新的事件注册表API
     * 这里提供了基于新架构的实现思路和临时的手动调用方案
     * 
     * 关键变化：
     * 1. IELE任务引擎：动态监听和处理任务事件
     * 2. 事件注册表API：重新设计的事件处理机制  
     * 3. 删除了表单和内容引擎：专注于CMMN、BPMN和DMN
     */
    
    /**
     * 🔧 手动触发流程部署事件处理（Flowable 7.0兼容方案）
     * 
     * 在Flowable 7.0的事件API明确之前，使用手动触发方式
     * 
     * @param processDefinitionId 流程定义ID
     * @param processDefinitionKey 流程定义Key
     */
    public void handleProcessDeploymentManually(String processDefinitionId, String processDefinitionKey) {
        log.info("手动处理流程部署事件：processDefinitionId={}, processDefinitionKey={}", 
                processDefinitionId, processDefinitionKey);
        
        try {
            parseAndSaveFieldPermissions(processDefinitionId, processDefinitionKey);
            log.info("流程部署事件处理完成：processDefinitionKey={}", processDefinitionKey);
        } catch (Exception e) {
            log.error("处理流程部署事件失败：processDefinitionKey=" + processDefinitionKey, e);
        }
    }
    
    /**
     * 🔧 手动解析流程定义的字段权限配置
     * 
     * 在Flowable 7.0事件API问题解决前，提供手动调用方式
     */
    public void manualParseFieldPermissions(String processDefinitionKey) {
        try {
            // 根据key获取最新版本的流程定义
            org.flowable.engine.repository.ProcessDefinition processDefinition = 
                repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .latestVersion()
                    .singleResult();
                    
            if (processDefinition != null) {
                log.info("手动解析流程字段权限：processDefinitionKey={}", processDefinitionKey);
                parseAndSaveFieldPermissions(processDefinition.getId(), processDefinition.getKey());
            } else {
                log.warn("未找到流程定义：processDefinitionKey={}", processDefinitionKey);
            }
        } catch (Exception e) {
            log.error("手动解析流程字段权限失败：processDefinitionKey={}", processDefinitionKey, e);
        }
    }

    // =============== 核心解析方法 ===============

    /**
     * 🎯 解析并保存字段权限配置
     * 
     * @param processDefinitionId 流程定义ID
     * @param processDefinitionKey 流程定义Key
     */
    public void parseAndSaveFieldPermissions(String processDefinitionId, String processDefinitionKey) {
        log.info("开始解析流程 {} 的字段权限配置", processDefinitionKey);

        try {
            // 1. 获取BPMN模型
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
            if (bpmnModel == null) {
                log.warn("未找到流程模型：{}", processDefinitionId);
                return;
            }

            Process process = bpmnModel.getMainProcess();
            if (process == null) {
                log.warn("未找到主流程：{}", processDefinitionId);
                return;
            }

            // 2. 查找所有用户任务
            Collection<UserTask> userTasks = process.findFlowElementsOfType(UserTask.class);
            log.debug("找到 {} 个用户任务节点", userTasks.size());

            int parsedCount = 0;
            int savedCount = 0;

            // 3. 解析每个用户任务的权限配置
            for (UserTask userTask : userTasks) {
                try {
                    log.debug("解析用户任务：{} ({})", userTask.getName(), userTask.getId());
                    
                    // 解析字段权限扩展属性
                    FormPermissionConfig permissionConfig = parseFieldPermissionFromUserTask(userTask);
                    
                    if (permissionConfig != null) {
                        parsedCount++;
                        
                        // 获取关联的表单ID（从formKey或其他属性）
                        String formId = extractFormId(userTask);
                        
                        if (oConvertUtils.isNotEmpty(formId)) {
                            // 保存到数据库
                            permissionEngine.saveNodePermissionConfig(
                                formId, 
                                processDefinitionKey, 
                                userTask.getId(), 
                                userTask.getName(), 
                                permissionConfig
                            );
                            savedCount++;
                            log.debug("已保存节点 {} 的字段权限配置", userTask.getName());
                        } else {
                            log.warn("节点 {} 未配置表单Key，跳过权限配置保存", userTask.getName());
                        }
                    } else {
                        log.debug("节点 {} 未配置字段权限，将使用智能默认策略", userTask.getName());
                    }

                } catch (Exception e) {
                    log.error("解析用户任务 {} 权限配置失败", userTask.getId(), e);
                }
            }

            log.info("流程 {} 字段权限解析完成：解析{}个，保存{}个配置", 
                    processDefinitionKey, parsedCount, savedCount);

        } catch (Exception e) {
            log.error("解析流程字段权限配置失败：{}", processDefinitionKey, e);
        }
    }

    /**
     * 🎯 从用户任务解析字段权限配置
     * 
     * @param userTask 用户任务
     * @return 权限配置，如果未配置则返回null
     */
    private FormPermissionConfig parseFieldPermissionFromUserTask(UserTask userTask) {
        
        // 获取扩展元素
        var extensionElements = userTask.getExtensionElements();
        if (extensionElements == null) {
            return null;
        }

        // 查找 fieldPermissions 扩展元素
        List<ExtensionElement> fieldPermissionsElements = extensionElements.get("fieldPermissions");
        if (oConvertUtils.isEmpty(fieldPermissionsElements)) {
            // 尝试查找带命名空间的元素
            fieldPermissionsElements = extensionElements.get("jeecg:fieldPermissions");
        }
        
        if (oConvertUtils.isEmpty(fieldPermissionsElements)) {
            return null;
        }

        ExtensionElement fieldPermissionsElement = fieldPermissionsElements.get(0);
        FormPermissionConfig config = new FormPermissionConfig();

        try {
            // 解析各种权限配置
            parseFieldArray(fieldPermissionsElement, "editableFields", config::setEditableFields);
            parseFieldArray(fieldPermissionsElement, "jeecg:editableFields", config::setEditableFields);
            
            parseFieldArray(fieldPermissionsElement, "readonlyFields", config::setReadonlyFields);
            parseFieldArray(fieldPermissionsElement, "jeecg:readonlyFields", config::setReadonlyFields);
            
            parseFieldArray(fieldPermissionsElement, "hiddenFields", config::setHiddenFields);
            parseFieldArray(fieldPermissionsElement, "jeecg:hiddenFields", config::setHiddenFields);
            
            parseFieldArray(fieldPermissionsElement, "requiredFields", config::setRequiredFields);
            parseFieldArray(fieldPermissionsElement, "jeecg:requiredFields", config::setRequiredFields);

            // 解析表单模式
            parseStringValue(fieldPermissionsElement, "formMode", config::setFormMode);
            parseStringValue(fieldPermissionsElement, "jeecg:formMode", config::setFormMode);

            log.debug("解析BPMN权限配置成功：可编辑{}个，只读{}个，隐藏{}个", 
                     config.getEditableFields().size(),
                     config.getReadonlyFields().size(),
                     config.getHiddenFields().size());

            return config;

        } catch (Exception e) {
            log.error("解析BPMN权限配置失败：用户任务{}", userTask.getId(), e);
            return null;
        }
    }

    /**
     * 🎯 解析字段数组
     * 
     * @param parent 父元素
     * @param elementName 元素名称
     * @param setter 设置方法
     */
    private void parseFieldArray(ExtensionElement parent, String elementName, 
                                Consumer<List<String>> setter) {
        List<ExtensionElement> elements = parent.getChildElements().get(elementName);
        if (oConvertUtils.isNotEmpty(elements)) {
            try {
                String jsonText = elements.get(0).getElementText();
                if (oConvertUtils.isNotEmpty(jsonText)) {
                    List<String> fields = JSON.parseArray(jsonText, String.class);
                    if (fields != null) {
                        setter.accept(fields);
                        log.debug("解析字段数组 {}：{} 个字段", elementName, fields.size());
                    }
                }
            } catch (Exception e) {
                log.warn("解析字段数组 {} 失败：{}", elementName, e.getMessage());
            }
        }
    }

    /**
     * 🎯 解析字符串值
     * 
     * @param parent 父元素
     * @param elementName 元素名称
     * @param setter 设置方法
     */
    private void parseStringValue(ExtensionElement parent, String elementName, 
                                 Consumer<String> setter) {
        List<ExtensionElement> elements = parent.getChildElements().get(elementName);
        if (oConvertUtils.isNotEmpty(elements)) {
            try {
                String value = elements.get(0).getElementText();
                if (oConvertUtils.isNotEmpty(value)) {
                    setter.accept(value);
                    log.debug("解析字符串值 {}：{}", elementName, value);
                }
            } catch (Exception e) {
                log.warn("解析字符串值 {} 失败：{}", elementName, e.getMessage());
            }
        }
    }

    /**
     * 🎯 提取表单ID
     * 从用户任务的formKey或其他属性中提取关联的表单ID
     * 
     * @param userTask 用户任务
     * @return 表单ID
     */
    private String extractFormId(UserTask userTask) {
        // 1. 优先从formKey提取
        String formKey = userTask.getFormKey();
        if (oConvertUtils.isNotEmpty(formKey)) {
            // formKey可能是完整的表单路径，需要提取表单ID
            // 例如：/form/maintenance-report 或 maintenance-report
            if (formKey.startsWith("/form/")) {
                return formKey.substring(6); // 去掉"/form/"前缀
            } else if (formKey.contains("/")) {
                String[] parts = formKey.split("/");
                return parts[parts.length - 1]; // 取最后一部分
            } else {
                return formKey;
            }
        }

        // 2. 从扩展属性中提取
        var extensionElements = userTask.getExtensionElements();
        if (extensionElements != null) {
            List<ExtensionElement> formIdElements = extensionElements.get("jeecg:formId");
            if (oConvertUtils.isNotEmpty(formIdElements)) {
                return formIdElements.get(0).getElementText();
            }
        }

        // 3. 从任务名称推断（临时方案）
        String taskName = userTask.getName();
        if (oConvertUtils.isNotEmpty(taskName)) {
            log.debug("尝试从任务名称推断表单ID：{}", taskName);
            // 这里可以根据命名约定来推断表单ID
            // 例如：审核维保工单 -> maintenance-report
        }

        log.warn("无法提取用户任务 {} 的表单ID", userTask.getId());
        return null;
    }

    // =============== 工具方法 ===============

    /**
     * 🎯 手动触发解析
     * 用于重新解析已部署的流程
     * 
     * @param processDefinitionKey 流程定义Key
     */
    public void reParseProcessPermissions(String processDefinitionKey) {
        try {
            // 获取最新版本的流程定义
            org.flowable.engine.repository.ProcessDefinition processDefinition = 
                repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .latestVersion()
                    .singleResult();

            if (processDefinition != null) {
                log.info("手动重新解析流程权限配置：{}", processDefinitionKey);
                parseAndSaveFieldPermissions(processDefinition.getId(), processDefinitionKey);
            } else {
                log.warn("未找到流程定义：{}", processDefinitionKey);
            }

        } catch (Exception e) {
            log.error("手动解析流程权限配置失败：{}", processDefinitionKey, e);
        }
    }

    /**
     * 🎯 检查流程是否配置了字段权限
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 是否配置了权限
     */
    public boolean hasFieldPermissionConfig(String processDefinitionKey) {
        try {
            org.flowable.engine.repository.ProcessDefinition processDefinition = 
                repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .latestVersion()
                    .singleResult();

            if (processDefinition == null) {
                return false;
            }

            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
            Process process = bpmnModel.getMainProcess();
            Collection<UserTask> userTasks = process.findFlowElementsOfType(UserTask.class);

            for (UserTask userTask : userTasks) {
                FormPermissionConfig config = parseFieldPermissionFromUserTask(userTask);
                if (config != null) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            log.error("检查流程字段权限配置失败：{}", processDefinitionKey, e);
            return false;
        }
    }
}