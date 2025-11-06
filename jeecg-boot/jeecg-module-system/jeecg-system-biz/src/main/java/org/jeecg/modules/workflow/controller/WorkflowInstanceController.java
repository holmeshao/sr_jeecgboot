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
import org.flowable.engine.TaskService;
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
import java.util.ArrayList;
import java.util.Base64;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.bpmn.model.FlowElementsContainer;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.GraphicInfo;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;

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

    @Autowired
    private TaskService taskService;

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
            byte[] png = generateDiagramBytes(id);
            if (png == null || png.length == 0) return ResponseEntity.status(500).build();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(png);
        } catch (Exception e) {
            log.error("生成流程图失败, instanceId={}", id, e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 同一功能返回 Base64（XHR 可见，便于排查/前端控制缓存）
     */
    @AutoLog(value = "获取流程图Base64")
    @Operation(summary = "获取流程图Base64", description = "返回 dataUrl 形式便于前端直接展示")
    @GetMapping(value = "/{id}/diagram")
    public Result<Map<String, Object>> getInstanceDiagramBase64(@PathVariable String id) {
        try {
            byte[] png = generateDiagramBytes(id);
            String dataUrl = png == null || png.length == 0 ? "" : "data:image/png;base64," + Base64.getEncoder().encodeToString(png);
            Map<String, Object> m = new HashMap<>();
            m.put("dataUrl", dataUrl);
            log.info("[diagram-base64] instanceId={}, bytes={}KB", id, png == null ? 0 : (png.length / 1024));
            return Result.OK(m);
        } catch (Exception e) {
            log.error("生成流程图Base64失败, instanceId={}", id, e);
            return Result.error("生成流程图失败: " + e.getMessage());
        }
    }

    /**
     * 返回该实例对应的 BPMN XML（用于前端 bpmn-js 渲染）
     */
    @AutoLog(value = "获取流程BPMN XML")
    @Operation(summary = "获取流程BPMN XML", description = "按实例ID解析流程定义并返回BPMN XML")
    @GetMapping(value = "/{id}/bpmn.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<byte[]> getInstanceBpmnXml(@PathVariable String id) {
        try {
            String processDefinitionId = null;
            ProcessInstance running = runtimeService.createProcessInstanceQuery()
                .processInstanceId(id).singleResult();
            if (running != null) {
                processDefinitionId = running.getProcessDefinitionId();
            } else {
                HistoricProcessInstance hp = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(id).singleResult();
                if (hp != null) processDefinitionId = hp.getProcessDefinitionId();
            }
            if (processDefinitionId == null) return ResponseEntity.notFound().build();
            ProcessDefinition def = repositoryService.getProcessDefinition(processDefinitionId);
            if (def == null) return ResponseEntity.notFound().build();
            String resourceName = def.getResourceName();
            InputStream in = repositoryService.getResourceAsStream(def.getDeploymentId(), resourceName);
            try (InputStream input = in; ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buf = new byte[4096];
                int len; while ((len = input.read(buf)) != -1) baos.write(buf, 0, len);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(baos.toByteArray());
            }
        } catch (Exception e) {
            log.error("获取BPMN XML失败, instanceId={}", id, e);
            return ResponseEntity.status(500).build();
        }
    }

    /** 统一生成PNG字节 */
    private byte[] generateDiagramBytes(String instanceId) throws Exception {
        // 解析流程定义 & 活动节点
        String processDefinitionId = null;
        List<String> activeIds = new java.util.ArrayList<>();

        ProcessInstance running = runtimeService.createProcessInstanceQuery()
            .processInstanceId(instanceId).singleResult();
        if (running != null) {
            processDefinitionId = running.getProcessDefinitionId();
            try { activeIds = runtimeService.getActiveActivityIds(instanceId); } catch (Exception ignore) {}
        } else {
            HistoricProcessInstance hp = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId).singleResult();
            if (hp != null) processDefinitionId = hp.getProcessDefinitionId();
        }
        if (processDefinitionId == null) return new byte[0];

        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        if (model == null) return new byte[0];
        try {
            java.util.Collection<org.flowable.bpmn.model.Process> ps = model.getProcesses();
            log.info("[diagram] pdId={}, processes={}, activeIds={}", processDefinitionId, ps == null ? 0 : ps.size(), activeIds);
        } catch (Exception ignore) {}

        InputStream in;
        if (processEngineConfiguration != null && processEngineConfiguration.getProcessDiagramGenerator() != null) {
            String font = "宋体";
            in = processEngineConfiguration.getProcessDiagramGenerator()
                .generateDiagram(model, "png", activeIds, java.util.Collections.emptyList(), font, font, font, null, 1.0, false);
        } else {
            ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
            String font = "宋体";
            in = generator.generateDiagram(model, "png", activeIds, java.util.Collections.emptyList(), font, font, font, null, 1.0, false);
        }
        try (InputStream input = in; ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[4096];
            int len;
            while ((len = input.read(buf)) != -1) baos.write(buf, 0, len);
            return baos.toByteArray();
        }
    }

    /**
     * 流程图元信息：返回每个节点的坐标、尺寸以及该节点关联的历史任务列表
     * 便于前端在PNG之上叠加悬浮提示（执行人/起止/耗时等）。
     */
    @AutoLog(value = "获取流程图元信息")
    @Operation(summary = "获取流程图元信息", description = "返回节点bounds与任务执行元信息，供前端叠加气泡")
    @GetMapping("/{id}/diagram/meta")
    public Result<Map<String, Object>> getInstanceDiagramMeta(@PathVariable String id) {
        try {
            // 解析流程定义ID
            String processDefinitionId = null;
            HistoricProcessInstance hp = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(id)
                .singleResult();
            if (hp != null) {
                processDefinitionId = hp.getProcessDefinitionId();
            } else {
                ProcessInstance running = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(id).singleResult();
                if (running != null) processDefinitionId = running.getProcessDefinitionId();
            }
            if (processDefinitionId == null) return Result.error("流程实例不存在");

            BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
            if (model == null) return Result.error("流程模型不存在");

            Map<String, Object> resp = new HashMap<>();
            List<Map<String, Object>> nodes = new ArrayList<>();

            // 收集所有节点的图形信息
            for (org.flowable.bpmn.model.Process p : model.getProcesses()) {
                for (FlowElement fe : p.getFlowElements()) {
                    if (fe instanceof FlowNode) {
                        String idKey = fe.getId();
                        GraphicInfo gi = model.getGraphicInfo(idKey);
                        if (gi == null) continue;
                        Map<String, Object> n = new HashMap<>();
                        n.put("id", idKey);
                        n.put("name", fe.getName());
                        n.put("type", fe.getClass().getSimpleName());
                        n.put("x", gi.getX());
                        n.put("y", gi.getY());
                        n.put("width", gi.getWidth());
                        n.put("height", gi.getHeight());
                        nodes.add(n);
                    }
                }
            }

            // 历史任务信息映射到节点
            List<Map<String, Object>> tasks = new ArrayList<>();
            try {
                List<HistoricTaskInstance> hts = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(id)
                    .orderByHistoricTaskInstanceStartTime().asc()
                    .list();
                for (HistoricTaskInstance t : hts) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", t.getId());
                    m.put("nodeId", t.getTaskDefinitionKey());
                    m.put("name", t.getName());
                    m.put("assignee", t.getAssignee());
                    m.put("startTime", t.getStartTime() == null ? null : t.getStartTime().getTime());
                    m.put("endTime", t.getEndTime() == null ? null : t.getEndTime().getTime());
                    Long dur = (t.getDurationInMillis() == null ? null : t.getDurationInMillis());
                    m.put("duration", dur);
                    // 审核意见（历史评论）
                    try {
                        List<org.flowable.engine.task.Comment> cmts = taskService.getTaskComments(t.getId());
                        List<Map<String, Object>> commentList = new ArrayList<>();
                        for (org.flowable.engine.task.Comment c : cmts) {
                            Map<String, Object> cm = new HashMap<>();
                            cm.put("user", c.getUserId());
                            cm.put("message", c.getFullMessage());
                            cm.put("time", c.getTime() == null ? null : c.getTime().getTime());
                            commentList.add(cm);
                        }
                        m.put("comments", commentList);
                    } catch (Exception ignore) {}
                    tasks.add(m);
                }
            } catch (Exception ignore) {}

            // 活动节点ID数组
            List<String> activeIds = new ArrayList<>();
            try { activeIds = runtimeService.getActiveActivityIds(id); } catch (Exception ignore) {}

            // 已完成节点ID集合（用于前端绿色标记）
            List<String> completedIds = new ArrayList<>();
            try {
                List<HistoricActivityInstance> hais = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(id)
                    .finished()
                    .orderByHistoricActivityInstanceEndTime().asc()
                    .list();
                for (HistoricActivityInstance hai : hais) {
                    completedIds.add(hai.getActivityId());
                }
            } catch (Exception ignore) {}

            // 已执行连线：直接从历史活动中取 activityType=sequenceFlow
            List<Map<String, Object>> executedFlows = new ArrayList<>();
            try {
                List<HistoricActivityInstance> seqs = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(id)
                    .activityType("sequenceFlow")
                    .orderByHistoricActivityInstanceStartTime().asc()
                    .list();
                for (HistoricActivityInstance s : seqs) {
                    if (s.getActivityId() != null) {
                        Map<String, Object> m = new HashMap<>();
                        m.put("id", s.getActivityId());
                        String nm = s.getActivityName() == null ? "" : s.getActivityName();
                        // 经验规则：名字含“驳回/退回/拒绝/不通过/否决”等视作驳回
                        String lower = nm.toLowerCase();
                        boolean reject = nm.contains("驳回") || nm.contains("退回") || nm.contains("拒绝") || nm.contains("不通过")
                                || lower.contains("reject") || lower.contains("return") || lower.contains("deny");
                        m.put("status", reject ? "reject" : "approve");
                        executedFlows.add(m);
                    }
                }
            } catch (Exception ignore) {}

            resp.put("nodes", nodes);
            resp.put("tasks", tasks);
            resp.put("activeIds", activeIds);
            resp.put("completedIds", completedIds);
            resp.put("executedFlows", executedFlows);
            return Result.OK(resp);
        } catch (Exception e) {
            log.error("获取流程图元信息失败", e);
            return Result.error("获取流程图元信息失败: " + e.getMessage());
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