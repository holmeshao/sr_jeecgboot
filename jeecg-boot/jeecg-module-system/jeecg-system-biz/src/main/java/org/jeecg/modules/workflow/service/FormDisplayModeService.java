package org.jeecg.modules.workflow.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.jeecg.modules.workflow.dto.FormDisplayMode;
import org.jeecg.modules.workflow.model.FormPermissionConfig;
import org.jeecg.modules.workflow.dto.NodeButton;
import org.jeecg.modules.workflow.engine.OnlineFormPermissionEngine;
import org.jeecg.modules.workflow.strategy.DefaultFieldPermissionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 表单显示模式服务
 * 
 * @author jeecg
 * @since 2024-12-25
 */
@Slf4j
@Service
public class FormDisplayModeService {
    
    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private OnlineFormPermissionEngine permissionEngine;
    
    @Autowired
    private DefaultFieldPermissionStrategy defaultStrategy;
    
    /**
     * 计算表单显示模式
     */
    public FormDisplayMode calculateDisplayMode(String formId, String dataId, String userId) {
        
        FormDisplayMode mode = new FormDisplayMode();
        
        try {
            // 1. 获取基础信息
            ProcessInstance processInfo = getProcessInstance(dataId);
            Task currentTask = getCurrentUserTask(processInfo != null ? processInfo.getId() : null, userId);
            
            if (currentTask != null) {
                // 用户有当前任务 - 操作模式
                mode.setMode(FormDisplayMode.Mode.OPERATE);
                mode.setHasCurrentTask(true);
                mode.setCurrentTaskId(currentTask.getId());
                mode.setCurrentTaskName(currentTask.getName());
                
                // 获取节点权限配置
                FormPermissionConfig permissions = permissionEngine.getNodePermission(
                    formId, processInfo.getProcessDefinitionKey(), currentTask.getTaskDefinitionKey());
                mode.setFieldPermissions(permissions);
                
                // 获取节点按钮配置  
                List<NodeButton> buttons = getNodeButtons(formId, currentTask.getTaskDefinitionKey());
                mode.setAvailableActions(buttons);
                
                log.debug("用户 {} 处于操作模式，任务: {}", userId, currentTask.getName());
                
            } else if (processInfo != null && !processInfo.isEnded()) {
                // 流程进行中但用户无任务 - 跟踪模式
                mode.setMode(FormDisplayMode.Mode.TRACK);
                mode.setFieldPermissions(getTrackPermissions(formId));
                mode.setProcessInstanceId(processInfo.getId());
                
                log.debug("用户 {} 处于跟踪模式，流程进行中", userId);
                
            } else {
                // 流程已结束或未开始 - 查看模式
                mode.setMode(FormDisplayMode.Mode.VIEW);
                mode.setFieldPermissions(getViewPermissions(formId));
                
                if (processInfo != null) {
                    mode.setProcessInstanceId(processInfo.getId());
                }
                
                log.debug("用户 {} 处于查看模式，流程状态: {}", userId, 
                         processInfo != null ? "已结束" : "未开始");
            }
            
        } catch (Exception e) {
            log.error("计算表单显示模式失败: formId={}, dataId={}, userId={}", formId, dataId, userId, e);
            // 默认返回查看模式
            mode.setMode(FormDisplayMode.Mode.VIEW);
            mode.setFieldPermissions(getViewPermissions(formId));
        }
        
        return mode;
    }
    
    /**
     * 根据数据ID获取流程实例
     */
    private ProcessInstance getProcessInstance(String dataId) {
        if (dataId == null) {
            return null;
        }
        
        try {
            return runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(dataId)
                .singleResult();
        } catch (Exception e) {
            log.warn("根据业务主键查询流程实例失败: dataId={}", dataId, e);
            return null;
        }
    }
    
    /**
     * 获取当前用户的任务
     */
    private Task getCurrentUserTask(String processInstanceId, String userId) {
        if (processInstanceId == null || userId == null) {
            return null;
        }
        
        try {
            return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee(userId)
                .singleResult();
        } catch (Exception e) {
            log.warn("查询用户任务失败: processInstanceId={}, userId={}", processInstanceId, userId, e);
            return null;
        }
    }
    
    /**
     * 获取节点按钮配置
     */
    private List<NodeButton> getNodeButtons(String formId, String nodeId) {
        List<NodeButton> buttons = new ArrayList<>();
        
        // 默认按钮
        NodeButton submitBtn = new NodeButton();
        submitBtn.setId("submit");
        submitBtn.setText("提交");
        submitBtn.setType("primary");
        submitBtn.setAction("submit");
        buttons.add(submitBtn);
        
        NodeButton saveBtn = new NodeButton();
        saveBtn.setId("save");
        saveBtn.setText("保存");
        saveBtn.setType("default");
        saveBtn.setAction("save");
        buttons.add(saveBtn);
        
        // TODO: 从配置中获取自定义按钮
        
        return buttons;
    }
    
    /**
     * 获取跟踪权限
     */
    private FormPermissionConfig getTrackPermissions(String formId) {
        FormPermissionConfig config = new FormPermissionConfig();
        config.setFormMode("VIEW");
        // 跟踪模式下所有字段只读
        return config;
    }
    
    /**
     * 获取查看权限
     */
    private FormPermissionConfig getViewPermissions(String formId) {
        FormPermissionConfig config = new FormPermissionConfig();
        config.setFormMode("VIEW");
        // 查看模式下所有字段只读
        return config;
    }
    
    /**
     * 🎯 获取表单基础信息（基于JeecgBoot API）
     */
    public FormBasicInfo getFormBasicInfo(String formId, String dataId) {
        FormBasicInfo info = new FormBasicInfo();
        info.setFormId(formId);
        info.setDataId(dataId);
        
        try {
            // 1. 获取表单配置信息
            String configUrl = "/online/cgform/api/getFormItemBytbname/" + formId;
            ResponseEntity<JSONObject> configResponse = restTemplate.getForEntity(configUrl, JSONObject.class);
            
            if (configResponse.getBody() != null && configResponse.getBody().getBooleanValue("success")) {
                JSONObject configResult = configResponse.getBody().getJSONObject("result");
                JSONObject head = configResult.getJSONObject("head");
                
                // 设置表单基本信息
                info.setFormTitle(head.getString("tableTxt"));
                info.setFormType(head.getString("tableName"));
                info.setFormId(head.getString("id"));
                
                // 2. 如果有dataId，获取数据详情
                if (StringUtils.hasText(dataId)) {
                    String dataUrl = "/online/cgform/api/form/" + head.getString("id") + "/" + dataId;
                    ResponseEntity<JSONObject> dataResponse = restTemplate.getForEntity(dataUrl, JSONObject.class);
                    
                    if (dataResponse.getBody() != null && dataResponse.getBody().getBooleanValue("success")) {
                        JSONObject dataResult = dataResponse.getBody().getJSONObject("result");
                        
                        // 设置创建和更新信息
                        info.setCreateBy(dataResult.getString("create_by"));
                        info.setCreateTime(dataResult.getString("create_time"));
                        info.setUpdateBy(dataResult.getString("update_by"));
                        info.setUpdateTime(dataResult.getString("update_time"));
                        
                        // 设置业务状态
                        String status = dataResult.getString("status");
                        if (StringUtils.isEmpty(status)) {
                            status = dataResult.getString("workflow_status");
                        }
                        if (StringUtils.isEmpty(status)) {
                            status = "DRAFT";
                        }
                        info.setStatus(status);
                        
                        // 设置流程信息
                        String processInstanceId = dataResult.getString("process_instance_id");
                        if (StringUtils.hasText(processInstanceId)) {
                            info.setProcessInstanceId(processInstanceId);
                            info.setHasWorkflow(true);
                        }
                    }
                } else {
                    // 新建模式
                    info.setCreateBy(getCurrentUser());
                    info.setCreateTime(formatCurrentTime());
                    info.setStatus("DRAFT");
                    info.setHasWorkflow(false);
                }
            }
            
        } catch (Exception e) {
            log.error("获取表单基础信息失败: formId={}, dataId={}", formId, dataId, e);
            // 返回默认信息
            info.setFormTitle("在线表单");
            info.setFormType("unknown");
            info.setStatus("DRAFT");
            info.setHasWorkflow(false);
        }
        
        return info;
    }
    
    /**
     * 获取当前用户
     */
    private String getCurrentUser() {
        try {
            return (String) org.apache.shiro.SecurityUtils.getSubject().getPrincipal();
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
     * 表单基础信息内部类
     */
    public static class FormBasicInfo {
        private String formId;
        private String dataId;
        private String formTitle;
        private String formType;
        private String createBy;
        private String createTime;
        private String updateBy;
        private String updateTime;
        private String status;
        private String processInstanceId;
        private boolean hasWorkflow;
        
        // Getter and Setter methods
        public String getFormId() { return formId; }
        public void setFormId(String formId) { this.formId = formId; }
        
        public String getDataId() { return dataId; }
        public void setDataId(String dataId) { this.dataId = dataId; }
        
        public String getFormTitle() { return formTitle; }
        public void setFormTitle(String formTitle) { this.formTitle = formTitle; }
        
        public String getFormType() { return formType; }
        public void setFormType(String formType) { this.formType = formType; }
        
        public String getCreateBy() { return createBy; }
        public void setCreateBy(String createBy) { this.createBy = createBy; }
        
        public String getCreateTime() { return createTime; }
        public void setCreateTime(String createTime) { this.createTime = createTime; }
        
        public String getUpdateBy() { return updateBy; }
        public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
        
        public String getUpdateTime() { return updateTime; }
        public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getProcessInstanceId() { return processInstanceId; }
        public void setProcessInstanceId(String processInstanceId) { this.processInstanceId = processInstanceId; }
        
        public boolean isHasWorkflow() { return hasWorkflow; }
        public void setHasWorkflow(boolean hasWorkflow) { this.hasWorkflow = hasWorkflow; }
    }
}