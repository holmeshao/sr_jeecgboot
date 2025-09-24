package org.jeecg.modules.workflow.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

/**
 * 工作流程实例管理Controller
 *
 * @author jeecg
 * @since 2025-01-24
 */
@Tag(name = "工作流程实例管理")
@RestController
@RequestMapping("/workflow/instance")
@Slf4j
public class WorkflowInstanceController {

    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private RepositoryService repositoryService;
    
    @Autowired
    private HistoryService historyService;

    @Autowired(required = false)
    private ProcessEngineConfigurationImpl processEngineConfiguration;

    /**
     * 获取流程实例列表
     */
    @AutoLog(value = "获取流程实例列表")
    @Operation(summary = "获取流程实例列表", description = "获取流程实例列表")
    @GetMapping("/list")
    public Result<Map<String, Object>> getInstanceList(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String processDefinitionKey,
            @RequestParam(required = false) String businessKey,
            @RequestParam(required = false) String status) {
        
        try {
            // 查询运行中的实例
            ProcessInstanceQuery runningQuery = runtimeService.createProcessInstanceQuery();
            
            // 查询历史实例（包括已完成的）
            HistoricProcessInstanceQuery historyQuery = historyService.createHistoricProcessInstanceQuery();
            
            // 添加查询条件
            if (oConvertUtils.isNotEmpty(processDefinitionKey)) {
                runningQuery.processDefinitionKey(processDefinitionKey);
                historyQuery.processDefinitionKey(processDefinitionKey);
            }
            if (oConvertUtils.isNotEmpty(businessKey)) {
                runningQuery.processInstanceBusinessKey(businessKey);
                historyQuery.processInstanceBusinessKey(businessKey);
            }
            
            List<Map<String, Object>> records;
            long total;
            
            if ("running".equals(status)) {
                // 只查询运行中的实例
                total = runningQuery.count();
                List<ProcessInstance> instances = runningQuery
                        .orderByStartTime().desc()
                        .listPage((pageNo - 1) * pageSize, pageSize);
                
                records = instances.stream().map(this::convertProcessInstance).collect(Collectors.toList());
                
            } else if ("finished".equals(status)) {
                // 只查询已完成的实例
                historyQuery.finished();
                total = historyQuery.count();
                List<HistoricProcessInstance> instances = historyQuery
                        .orderByProcessInstanceStartTime().desc()
                        .listPage((pageNo - 1) * pageSize, pageSize);
                
                records = instances.stream().map(this::convertHistoricProcessInstance).collect(Collectors.toList());
                
            } else {
                // 查询所有实例（历史表包含运行中和已完成的）
                total = historyQuery.count();
                List<HistoricProcessInstance> instances = historyQuery
                        .orderByProcessInstanceStartTime().desc()
                        .listPage((pageNo - 1) * pageSize, pageSize);
                
                records = instances.stream().map(this::convertHistoricProcessInstance).collect(Collectors.toList());
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", records);
            result.put("total", total);
            result.put("current", pageNo);
            result.put("size", pageSize);
            
            return Result.OK(result);
        } catch (Exception e) {
            log.error("获取流程实例列表失败", e);
            return Result.error("获取流程实例列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取流程实例详情
     */
    @AutoLog(value = "获取流程实例详情")
    @Operation(summary = "获取流程实例详情", description = "获取流程实例详情")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getInstanceDetail(@PathVariable String id) {
        try {
            // 先查询运行中的实例
            ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(id).singleResult();
            
            if (instance != null) {
                return Result.OK(convertProcessInstance(instance));
            }
            
            // 如果运行中没有，查询历史实例
            HistoricProcessInstance historicInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(id).singleResult();
            
            if (historicInstance != null) {
                return Result.OK(convertHistoricProcessInstance(historicInstance));
            }
            
            return Result.error("流程实例不存在");
        } catch (Exception e) {
            log.error("获取流程实例详情失败", e);
            return Result.error("获取流程实例详情失败：" + e.getMessage());
        }
    }

    /**
     * 启动流程实例
     */
    @AutoLog(value = "启动流程实例")
    @Operation(summary = "启动流程实例", description = "启动流程实例")
    @PostMapping("/start")
    public Result<String> startInstance(@RequestBody Map<String, Object> params) {
        try {
            String processDefinitionKey = (String) params.get("processDefinitionKey");
            String businessKey = (String) params.get("businessKey");
            @SuppressWarnings("unchecked")
            Map<String, Object> variables = (Map<String, Object>) params.get("variables");
            
            if (oConvertUtils.isEmpty(processDefinitionKey)) {
                return Result.error("流程定义Key不能为空");
            }
            
            ProcessInstance instance = runtimeService.startProcessInstanceByKey(
                    processDefinitionKey, businessKey, variables);
            
            log.info("流程实例启动成功，实例ID：{}", instance.getId());
            return Result.OK("流程实例启动成功", instance.getId());
            
        } catch (Exception e) {
            log.error("启动流程实例失败", e);
            return Result.error("启动流程实例失败：" + e.getMessage());
        }
    }

    /**
     * 终止流程实例
     */
    @AutoLog(value = "终止流程实例")
    @Operation(summary = "终止流程实例", description = "终止流程实例")
    @PutMapping("/{id}/terminate")
    public Result<String> terminateInstance(@PathVariable String id, @RequestBody Map<String, Object> params) {
        try {
            String reason = (String) params.get("reason");
            
            ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(id).singleResult();
            
            if (instance == null) {
                return Result.error("流程实例不存在或已结束");
            }
            
            runtimeService.deleteProcessInstance(id, reason != null ? reason : "流程终止");
            
            log.info("流程实例终止成功，实例ID：{}", id);
            return Result.OK("流程实例终止成功");
            
        } catch (Exception e) {
            log.error("终止流程实例失败", e);
            return Result.error("终止流程实例失败：" + e.getMessage());
        }
    }

    /**
     * 获取流程实例历史
     */
    @AutoLog(value = "获取流程实例历史")
    @Operation(summary = "获取流程实例历史", description = "获取流程实例历史")
    @GetMapping("/{id}/history")
    public Result<Map<String, Object>> getInstanceHistory(@PathVariable String id) {
        try {
            HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(id).singleResult();
            
            if (instance == null) {
                return Result.error("流程实例不存在");
            }
            
            Map<String, Object> result = convertHistoricProcessInstance(instance);
            
            // 添加额外的历史信息，比如变量历史、任务历史等
            // 这里可以根据需要扩展
            
            return Result.OK(result);
        } catch (Exception e) {
            log.error("获取流程实例历史失败", e);
            return Result.error("获取流程实例历史失败：" + e.getMessage());
        }
    }

    /**
     * 获取流程图PNG（尽量高亮当前活动节点；已完成流程仅显示图）
     */
    @AutoLog(value = "获取流程图PNG")
    @Operation(summary = "获取流程图PNG", description = "按流程实例生成流程图，包含当前活动节点高亮")
    @GetMapping(value = "/{id}/diagram.png", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getInstanceDiagramPng(@PathVariable String id) {
        try {
            // 解析流程定义
            String processDefinitionId = null;
            List<String> activeIds = java.util.Collections.emptyList();

            ProcessInstance running = runtimeService.createProcessInstanceQuery()
                .processInstanceId(id)
                .singleResult();
            if (running != null) {
                processDefinitionId = running.getProcessDefinitionId();
                try {
                    activeIds = runtimeService.getActiveActivityIds(id);
                } catch (Exception ignore) {}
            } else {
                HistoricProcessInstance hp = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(id)
                    .singleResult();
                if (hp != null) {
                    processDefinitionId = hp.getProcessDefinitionId();
                }
            }

            if (processDefinitionId == null) {
                return ResponseEntity.notFound().build();
            }

            BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
            if (model == null) {
                return ResponseEntity.notFound().build();
            }

            // 使用流程图生成器
            InputStream in;
            if (processEngineConfiguration != null && processEngineConfiguration.getProcessDiagramGenerator() != null) {
                // 字体尽量使用常见中文字体名，避免方框；无法加载时由引擎兜底
                String font = "宋体";
                in = processEngineConfiguration.getProcessDiagramGenerator()
                    .generateDiagram(model, "png", activeIds, java.util.Collections.emptyList(), font, font, font, null, 1.0, false);
            } else {
                // 退化：尝试读取部署时的图资源
                ProcessDefinition def = repositoryService.getProcessDefinition(processDefinitionId);
                String diagramRes = def != null ? def.getDiagramResourceName() : null;
                if (diagramRes == null) {
                    return ResponseEntity.notFound().build();
                }
                in = repositoryService.getResourceAsStream(def.getDeploymentId(), diagramRes);
            }

            try (InputStream input = in; ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buf = new byte[4096];
                int len;
                while ((len = input.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(baos.toByteArray());
            }
        } catch (Exception e) {
            log.error("生成流程图失败", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 转换ProcessInstance为前端需要的格式
     */
    private Map<String, Object> convertProcessInstance(ProcessInstance instance) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", instance.getId());
        result.put("name", instance.getName());
        result.put("processDefinitionId", instance.getProcessDefinitionId());
        result.put("processDefinitionKey", instance.getProcessDefinitionKey());
        result.put("processDefinitionName", instance.getProcessDefinitionName());
        result.put("processDefinitionVersion", instance.getProcessDefinitionVersion());
        result.put("businessKey", instance.getBusinessKey());
        result.put("startTime", instance.getStartTime());
        result.put("startUserId", instance.getStartUserId());
        result.put("suspended", instance.isSuspended());
        result.put("tenantId", instance.getTenantId());
        result.put("status", "running");
        result.put("endTime", null);
        result.put("duration", null);
        
        return result;
    }

    /**
     * 转换HistoricProcessInstance为前端需要的格式
     */
    private Map<String, Object> convertHistoricProcessInstance(HistoricProcessInstance instance) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", instance.getId());
        result.put("name", instance.getName());
        result.put("processDefinitionId", instance.getProcessDefinitionId());
        result.put("processDefinitionKey", instance.getProcessDefinitionKey());
        result.put("processDefinitionName", instance.getProcessDefinitionName());
        result.put("processDefinitionVersion", instance.getProcessDefinitionVersion());
        result.put("businessKey", instance.getBusinessKey());
        result.put("startTime", instance.getStartTime());
        result.put("endTime", instance.getEndTime());
        result.put("duration", instance.getDurationInMillis());
        result.put("startUserId", instance.getStartUserId());
        result.put("deleteReason", instance.getDeleteReason());
        result.put("tenantId", instance.getTenantId());
        result.put("status", instance.getEndTime() != null ? "finished" : "running");
        
        return result;
    }
}