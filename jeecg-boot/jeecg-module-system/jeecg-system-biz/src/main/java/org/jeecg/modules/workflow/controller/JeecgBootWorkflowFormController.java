package org.jeecg.modules.workflow.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.workflow.service.OnlineFormWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 🎯 基于JeecgBoot的工作流表单控制器
 * 深度利用JeecgBoot现有能力，扩展工作流功能
 * 
 * @author jeecg
 * @since 2024-12-25
 */
@Tag(name = "JeecgBoot工作流表单")
@RestController
@RequestMapping("/workflow/form")
@Slf4j
public class JeecgBootWorkflowFormController {

    @Autowired
    private OnlineFormWorkflowService onlineFormWorkflowService;

    /**
     * 🎯 智能提交表单（基于JeecgBoot API + 工作流扩展）
     */
    @AutoLog(value = "智能提交表单")
    @Operation(summary = "智能提交表单", description = "基于JeecgBoot在线表单API，扩展工作流能力")
    @PostMapping("/submit")
    public Result<Map<String, Object>> submitForm(@RequestBody SubmitFormRequest request) {
        
        log.info("接收到表单提交请求: tableName={}, dataId={}", request.getTableName(), request.getDataId());
        
        try {
            JSONObject formData = new JSONObject(request.getFormData());
            return onlineFormWorkflowService.submitForm(request.getTableName(), request.getDataId(), formData);
            
        } catch (Exception e) {
            log.error("表单提交失败", e);
            return Result.error("表单提交失败: " + e.getMessage());
        }
    }

    /**
     * 🎯 保存表单草稿（基于JeecgBoot API）
     */
    @AutoLog(value = "保存表单草稿")
    @Operation(summary = "保存表单草稿", description = "基于JeecgBoot在线表单API保存草稿")
    @PostMapping("/save-draft")
    public Result<Map<String, Object>> saveDraft(@RequestBody SubmitFormRequest request) {
        
        log.info("接收到草稿保存请求: tableName={}, dataId={}", request.getTableName(), request.getDataId());
        
        try {
            JSONObject formData = new JSONObject(request.getFormData());
            return onlineFormWorkflowService.saveDraftForm(request.getTableName(), request.getDataId(), formData);
            
        } catch (Exception e) {
            log.error("草稿保存失败", e);
            return Result.error("草稿保存失败: " + e.getMessage());
        }
    }

    /**
     * 🎯 获取表单配置（增强JeecgBoot API，支持工作流权限）
     */
    @AutoLog(value = "获取表单配置")
    @Operation(summary = "获取表单配置", description = "获取JeecgBoot在线表单配置，应用工作流权限")
    @GetMapping("/config")
    public Result<JSONObject> getFormConfig(@RequestParam String tableName,
                                          @RequestParam(required = false) String taskId,
                                          @RequestParam(required = false) String nodeId,
                                          @RequestParam(required = false) String processDefinitionKey) {
        
        log.info("获取表单配置: tableName={}, taskId={}, nodeId={}", tableName, taskId, nodeId);
        
        try {
            // 1. 获取基础表单配置
            Result<JSONObject> configResult = onlineFormWorkflowService.getFormConfig(tableName, taskId);
            if (!configResult.isSuccess()) {
                return configResult;
            }
            
            JSONObject formConfig = configResult.getResult();
            
            // 2. 如果有节点ID，应用工作流权限
            if (nodeId != null && processDefinitionKey != null) {
                formConfig = onlineFormWorkflowService.applyWorkflowPermissions(formConfig, nodeId, processDefinitionKey);
            }
            
            return Result.OK(formConfig);
            
        } catch (Exception e) {
            log.error("获取表单配置失败", e);
            return Result.error("获取表单配置失败: " + e.getMessage());
        }
    }

    /**
     * 🎯 获取表单数据（调用JeecgBoot API）
     */
    @AutoLog(value = "获取表单数据")
    @Operation(summary = "获取表单数据", description = "获取JeecgBoot在线表单数据")
    @GetMapping("/data")
    public Result<JSONObject> getFormData(@RequestParam String formId, @RequestParam String dataId) {
        
        log.info("获取表单数据: formId={}, dataId={}", formId, dataId);
        
        try {
            return onlineFormWorkflowService.getFormData(formId, dataId);
            
        } catch (Exception e) {
            log.error("获取表单数据失败", e);
            return Result.error("获取表单数据失败: " + e.getMessage());
        }
    }

    /**
     * 🎯 获取表单基本信息（基于JeecgBoot API完整实现）
     */
    @AutoLog(value = "获取表单基本信息")
    @Operation(summary = "获取表单基本信息", description = "获取表单的基本信息和状态")
    @GetMapping("/basic-info")
    public Result<Map<String, Object>> getFormBasicInfo(@RequestParam String tableName, 
                                                       @RequestParam(required = false) String dataId) {
        
        log.info("获取表单基本信息: tableName={}, dataId={}", tableName, dataId);
        
        try {
            // 1. 先获取表单配置以确定formId
            Result<JSONObject> configResult = onlineFormWorkflowService.getFormConfig(tableName, null);
            if (!configResult.isSuccess()) {
                return Result.error("获取表单配置失败: " + configResult.getMessage());
            }
            
            JSONObject config = configResult.getResult();
            String formId = config.getJSONObject("head").getString("id");
            
            // 2. 获取表单基本信息
            Map<String, Object> basicInfo = new HashMap<>();
            basicInfo.put("formId", formId);
            basicInfo.put("tableName", tableName);
            basicInfo.put("dataId", dataId);
            basicInfo.put("formTitle", config.getJSONObject("head").getString("tableTxt"));
            
            // 3. 如果有dataId，获取详细数据和状态
            if (StringUtils.hasText(dataId)) {
                Result<JSONObject> dataResult = onlineFormWorkflowService.getFormData(formId, dataId);
                if (dataResult.isSuccess()) {
                    JSONObject formData = dataResult.getResult();
                    
                    // 设置基本字段
                    basicInfo.put("createBy", formData.getString("create_by"));
                    basicInfo.put("createTime", formData.getString("create_time"));
                    basicInfo.put("updateBy", formData.getString("update_by"));
                    basicInfo.put("updateTime", formData.getString("update_time"));
                    
                    // 解析状态信息
                    String status = formData.getString("status");
                    if (StringUtils.isEmpty(status)) {
                        status = formData.getString("workflow_status");
                    }
                    if (StringUtils.isEmpty(status)) {
                        status = "DRAFT";
                    }
                    basicInfo.put("formStatus", status);
                    
                    // 解析流程信息
                    String processInstanceId = formData.getString("process_instance_id");
                    if (StringUtils.hasText(processInstanceId)) {
                        basicInfo.put("processInstanceId", processInstanceId);
                        basicInfo.put("hasWorkflow", true);
                        basicInfo.put("workflowStatus", "ACTIVE");
                    } else {
                        basicInfo.put("hasWorkflow", false);
                        basicInfo.put("workflowStatus", "NONE");
                    }
                    
                } else {
                    return Result.error("获取表单数据失败: " + dataResult.getMessage());
                }
            } else {
                // 新建模式的默认信息
                basicInfo.put("formStatus", "DRAFT");
                basicInfo.put("hasWorkflow", false);
                basicInfo.put("workflowStatus", "NONE");
                basicInfo.put("createBy", getCurrentUser());
                basicInfo.put("createTime", formatCurrentTime());
            }
            
            return Result.OK(basicInfo);
            
        } catch (Exception e) {
            log.error("获取表单基本信息失败", e);
            return Result.error("获取表单基本信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取当前用户
     */
    private String getCurrentUser() {
        try {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            return loginUser != null ? loginUser.getUsername() : "system";
        } catch (Exception e) {
            return "system";
        }
    }
    
    /**
     * 格式化当前时间
     */
    private String formatCurrentTime() {
        return java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
    }

    /**
     * 🎯 手动启动工作流（基于JeecgBoot表名）
     */
    @AutoLog(value = "手动启动工作流")
    @Operation(summary = "手动启动工作流", description = "手动启动表单的工作流程")
    @PostMapping("/manual-start")
    public Result<Map<String, Object>> manualStartWorkflow(@RequestBody ManualStartRequest request) {
        
        log.info("手动启动工作流: tableName={}, dataId={}", request.getTableName(), request.getDataId());
        
        try {
            // 1. 先获取表单配置以确定formId
            Result<JSONObject> configResult = onlineFormWorkflowService.getFormConfig(request.getTableName(), null);
            if (!configResult.isSuccess()) {
                return Result.error("获取表单配置失败: " + configResult.getMessage());
            }
            
            JSONObject config = configResult.getResult();
            String formId = config.getJSONObject("head").getString("id");
            
            // 2. 调用现有的手动启动工作流方法
            String processInstanceId = onlineFormWorkflowService.manualStartWorkflow(formId, request.getDataId());
            
            if (StringUtils.hasText(processInstanceId)) {
                // 3. 构建返回结果
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("processInstanceId", processInstanceId);
                result.put("tableName", request.getTableName());
                result.put("dataId", request.getDataId());
                result.put("message", "工作流启动成功");
                result.put("startTime", formatCurrentTime());
                result.put("startBy", getCurrentUser());
                
                return Result.OK(result);
            } else {
                return Result.error("工作流启动失败，未返回流程实例ID");
            }
            
        } catch (Exception e) {
            log.error("手动启动工作流失败", e);
            return Result.error("手动启动工作流失败: " + e.getMessage());
        }
    }

    // 请求DTO类
    public static class SubmitFormRequest {
        private String tableName;
        private String dataId;
        private Map<String, Object> formData;

        public String getTableName() { return tableName; }
        public void setTableName(String tableName) { this.tableName = tableName; }

        public String getDataId() { return dataId; }
        public void setDataId(String dataId) { this.dataId = dataId; }

        public Map<String, Object> getFormData() { return formData; }
        public void setFormData(Map<String, Object> formData) { this.formData = formData; }
    }

    public static class ManualStartRequest {
        private String tableName;
        private String dataId;

        public String getTableName() { return tableName; }
        public void setTableName(String tableName) { this.tableName = tableName; }

        public String getDataId() { return dataId; }
        public void setDataId(String dataId) { this.dataId = dataId; }
    }
}