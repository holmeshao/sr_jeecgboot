package org.jeecg.modules.workflow.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.jeecg.modules.workflow.service.WorkflowEventService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import java.util.List;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Added imports for sync endpoints
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowNode;
import org.jeecg.modules.workflow.mapper.OnlCgformWorkflowNodeMapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.util.HashSet;
import java.util.Set;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

/**
 * 工作流程定义管理Controller
 *
 * @author jeecg
 * @since 2025-01-24
 */
@Tag(name = "工作流程定义管理")
@RestController
@RequestMapping("/workflow/definition")
@Slf4j
public class WorkflowDefinitionController {

    @Autowired
    private RepositoryService repositoryService;
    
    @Autowired
    private WorkflowEventService workflowEventService;

    @Autowired(required = false)
    private RuntimeService runtimeService;

    @Autowired(required = false)
    private OnlCgformWorkflowNodeMapper onlCgformWorkflowNodeMapper;

    /**
     * 获取流程定义列表
     */
    @AutoLog(value = "获取流程定义列表")
    @Operation(summary = "获取流程定义列表", description = "获取流程定义列表")
    @GetMapping("/list")
    public Result<Map<String, Object>> getDefinitionList(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String key,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "false") Boolean includeAllVersions) {
        
        try {
            ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
            
            // 添加查询条件
            if (key != null && !key.trim().isEmpty()) {
                query.processDefinitionKeyLike("%" + key + "%");
            }
            if (name != null && !name.trim().isEmpty()) {
                query.processDefinitionNameLike("%" + name + "%");
            }
            if (category != null && !category.trim().isEmpty()) {
                query.processDefinitionCategory(category);
            }
            
            // 仅展示最新版本（默认）。如需查看所有版本，可传 includeAllVersions=true
            if (!Boolean.TRUE.equals(includeAllVersions)) {
                query.latestVersion();
            }

            // 按 key 升序 + 版本号倒序，方便阅读
            query.orderByProcessDefinitionKey().asc();
            query.orderByProcessDefinitionVersion().desc();
            
            // 分页查询
            long total = query.count();
            List<ProcessDefinition> list = query
                    .listPage((pageNo - 1) * pageSize, pageSize);
            
            // 转换为前端需要的格式
            List<Map<String, Object>> records = list.stream().map(def -> {
                Map<String, Object> record = new HashMap<>();
                record.put("id", def.getId());
                record.put("key", def.getKey());
                record.put("name", def.getName());
                record.put("version", def.getVersion());
                record.put("category", def.getCategory());
                record.put("description", def.getDescription());
                record.put("deploymentId", def.getDeploymentId());
                record.put("resourceName", def.getResourceName());
                record.put("diagramResourceName", def.getDiagramResourceName());
                record.put("suspended", def.isSuspended());
                record.put("tenantId", def.getTenantId());
                
                // 获取部署信息
                Deployment deployment = repositoryService.createDeploymentQuery()
                        .deploymentId(def.getDeploymentId()).singleResult();
                if (deployment != null) {
                    record.put("deploymentTime", deployment.getDeploymentTime());
                    record.put("deploymentName", deployment.getName());
                    try {
                        // 读取我们自定义的部署元数据，提取模型版本
                        java.util.List<String> names = repositoryService.getDeploymentResourceNames(deployment.getId());
                        String metaName = null;
                        if (names != null) {
                            for (String n : names) {
                                if ("jeecg-deploy-meta.json".equals(n) || "deployment-description.txt".equals(n)) {
                                    metaName = n; break;
                                }
                            }
                        }
                        if (metaName != null) {
                            InputStream mis = repositoryService.getResourceAsStream(deployment.getId(), metaName);
                            String meta = new String(mis.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
                            Integer modelVersion = null;
                            String modelKey = null;
                            try {
                                JSONObject obj = JSON.parseObject(meta);
                                if (obj != null) {
                                    modelVersion = obj.getInteger("modelVersion");
                                    record.put("modelId", obj.getString("modelId"));
                                    modelKey = obj.getString("modelKey");
                                }
                            } catch (Exception ignore) {
                                // 兼容老格式：纯数字/或 key=value
                                try { modelVersion = Integer.valueOf(meta.trim()); } catch (Exception ignored) {}
                            }
                            if (modelVersion != null) {
                                record.put("modelVersion", modelVersion);
                            }
                            if (modelKey != null) {
                                record.put("modelKey", modelKey);
                            }
                        }
                    } catch (Exception ignore) {}
                }
                
                return record;
            }).collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", records);
            result.put("total", total);
            result.put("current", pageNo);
            result.put("size", pageSize);
            
            return Result.OK(result);
        } catch (Exception e) {
            log.error("获取流程定义列表失败", e);
            return Result.error("获取流程定义列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取流程定义详情
     */
    @AutoLog(value = "获取流程定义详情")
    @Operation(summary = "获取流程定义详情", description = "获取流程定义详情")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getDefinitionDetail(@PathVariable String id) {
        try {
            ProcessDefinition definition = repositoryService.getProcessDefinition(id);
            if (definition == null) {
                return Result.error("流程定义不存在");
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("id", definition.getId());
            result.put("key", definition.getKey());
            result.put("name", definition.getName());
            result.put("version", definition.getVersion());
            result.put("category", definition.getCategory());
            result.put("description", definition.getDescription());
            result.put("deploymentId", definition.getDeploymentId());
            result.put("resourceName", definition.getResourceName());
            result.put("diagramResourceName", definition.getDiagramResourceName());
            result.put("suspended", definition.isSuspended());
            result.put("tenantId", definition.getTenantId());
            
            return Result.OK(result);
        } catch (Exception e) {
            log.error("获取流程定义详情失败", e);
            return Result.error("获取流程定义详情失败：" + e.getMessage());
        }
    }

    /**
     * 部署流程定义
     */
    @AutoLog(value = "部署流程定义")
    @Operation(summary = "部署流程定义", description = "部署流程定义")
    @PostMapping("/deploy")
    public Result<String> deployDefinition(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer modelVersion,
            @RequestParam(required = false) String modelId,
            @RequestParam(required = false) String modelKey) {
        
        try {
            if (file.isEmpty()) {
                return Result.error("请选择要部署的流程文件");
            }
            
            String filename = file.getOriginalFilename();
            if (!filename.endsWith(".bpmn") && !filename.endsWith(".bpmn20.xml")) {
                return Result.error("只支持.bpmn或.bpmn20.xml格式的流程文件");
            }
            
            // 读取并可选改写XML中的 <process name>
            String xml = new String(file.getBytes(), StandardCharsets.UTF_8);
            if (name != null && name.trim().length() > 0) {
                xml = overrideProcessNameInXml(xml, name.trim());
            }
            InputStream inputStream = new java.io.ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            String deploymentName = name != null && name.trim().length() > 0 ? name.trim() : filename;
            
            org.flowable.engine.repository.DeploymentBuilder builder = repositoryService.createDeployment()
                    .addInputStream(filename, inputStream)
                    .name(deploymentName)
                    .category(category);
            try {
                // 写入一份 JSON 元数据，包含来源模型/模型版本
                JSONObject meta = new JSONObject();
                if (description != null) meta.put("description", description.trim());
                if (modelVersion != null) meta.put("modelVersion", modelVersion);
                if (modelId != null) meta.put("modelId", modelId);
                if (modelKey != null) meta.put("modelKey", modelKey);
                builder.addString("jeecg-deploy-meta.json", meta.toJSONString());
            } catch (Exception ignore) {}
            Deployment deployment = builder.deploy();
            
            log.info("流程定义部署成功，部署ID：{}", deployment.getId());
            
            // 🎯 Flowable 7.0兼容处理：自动触发部署后事件处理
            try {
                // 获取部署的流程定义
                List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deployment.getId())
                    .list();
                
                int successCount = 0;
                int failCount = 0;
                
                for (ProcessDefinition pd : processDefinitions) {
                    try {
                        log.info("自动触发流程定义部署后处理：{}", pd.getKey());
                        workflowEventService.onProcessDefinitionDeployed(pd.getKey());
                        successCount++;
                    } catch (Exception e) {
                        log.error("流程定义部署后处理失败：" + pd.getKey(), e);
                        failCount++;
                    }
                }
                
                String message = String.format(
                    "流程定义部署成功！自动事件处理完成：总计 %d 个流程，成功 %d 个，失败 %d 个。字段权限解析等后续处理已自动完成。", 
                    processDefinitions.size(), successCount, failCount
                );
                
                if (failCount > 0) {
                    message += " 部分流程处理失败，请检查日志或手动调用 POST /workflow/triggerDeploymentEvent 重试。";
                }
                
                return Result.OK(message, deployment.getId());
                
            } catch (Exception e) {
                log.error("流程定义部署后处理异常", e);
                return Result.OK("流程定义部署成功，但后续处理异常，请手动调用 POST /workflow/triggerDeploymentEvent", deployment.getId());
            }
            
        } catch (IOException e) {
            log.error("读取流程文件失败", e);
            return Result.error("读取流程文件失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("部署流程定义失败", e);
            return Result.error("部署流程定义失败：" + e.getMessage());
        }
    }

    /**
     * 删除流程定义
     */
    @AutoLog(value = "删除流程定义")
    @Operation(summary = "删除流程定义", description = "删除流程定义")
    @DeleteMapping("/{id}")
    public Result<String> deleteDefinition(
            @PathVariable String id,
            @RequestParam(defaultValue = "false") Boolean cascade) {
        
        try {
            // 兼容：前端可能传的是 流程定义ID(processDefinitionId)，而非 部署ID(deploymentId)
            String resolvedDeploymentId = id;

            ProcessDefinition definition = null;
            try {
                definition = repositoryService.getProcessDefinition(id);
            } catch (Exception ignore) { /* ignore lookup errors */ }

            if (definition != null) {
                resolvedDeploymentId = definition.getDeploymentId();
            }

            // 校验部署是否存在
            Deployment deployment = repositoryService.createDeploymentQuery()
                    .deploymentId(resolvedDeploymentId)
                    .singleResult();
            if (deployment == null) {
                String msg = "未找到部署：" + resolvedDeploymentId + "。若传入的是流程定义ID，请直接传该定义ID或其对应的部署ID。";
                log.error("删除流程定义失败：{}", msg);
                return Result.error(msg);
            }

            // 非级联删除时，若仍有运行中的流程实例，给出友好提示
            if (Boolean.FALSE.equals(cascade) && runtimeService != null) {
                long running = 0L;
                if (definition != null) {
                    running = runtimeService.createProcessInstanceQuery()
                            .processDefinitionId(definition.getId())
                            .count();
                } else {
                    List<ProcessDefinition> defs = repositoryService.createProcessDefinitionQuery()
                            .deploymentId(resolvedDeploymentId)
                            .list();
                    if (defs != null) {
                        for (ProcessDefinition pd : defs) {
                            running += runtimeService.createProcessInstanceQuery()
                                    .processDefinitionId(pd.getId())
                                    .count();
                        }
                    }
                }
                if (running > 0) {
                    return Result.error("存在运行中的流程实例（" + running + " 个），请先完成/终止实例，或在请求中携带 cascade=true 强制删除（同时删除实例）");
                }
            }

            if (cascade) {
                // 级联删除，同时删除流程实例
                repositoryService.deleteDeployment(resolvedDeploymentId, true);
            } else {
                repositoryService.deleteDeployment(resolvedDeploymentId);
            }

            log.info("流程定义删除成功，部署ID：{} (请求ID：{})", resolvedDeploymentId, id);
            return Result.OK("流程定义删除成功");
            
        } catch (Exception e) {
            log.error("删除流程定义失败", e);
            return Result.error("删除流程定义失败：" + e.getMessage());
        }
    }

    /**
     * 获取流程定义XML
     */
    @AutoLog(value = "获取流程定义XML")
    @Operation(summary = "获取流程定义XML", description = "获取流程定义XML")
    @GetMapping("/{id}/xml")
    public Result<String> getDefinitionXml(@PathVariable String id) {
        try {
            ProcessDefinition definition = repositoryService.getProcessDefinition(id);
            if (definition == null) {
                return Result.error("流程定义不存在");
            }
            
            InputStream inputStream = repositoryService.getResourceAsStream(
                    definition.getDeploymentId(), definition.getResourceName());
            
            // 读取XML内容
            byte[] bytes = inputStream.readAllBytes();
            String xml = new String(bytes, "UTF-8");
            
            return Result.OK(xml);
        } catch (Exception e) {
            log.error("获取流程定义XML失败", e);
            return Result.error("获取流程定义XML失败：" + e.getMessage());
        }
    }

    /**
     * 通过XML字符串部署流程定义（JSON直传，无需multipart/file）
     */
    @AutoLog(value = "部署流程定义(JSON/XML)")
    @Operation(summary = "部署流程定义(JSON/XML)", description = "Body传 { name, category, xml }，xml为BPMN XML字符串")
    @PostMapping("/deployByXml")
    public Result<String> deployByXml(@RequestBody Map<String, Object> body) {
        try {
            if (body == null) {
                return Result.error("请求体不能为空");
            }
            String name = body.getOrDefault("name", "process").toString();
            String category = body.getOrDefault("category", "").toString();
            String description = String.valueOf(body.getOrDefault("description", ""));
            // 来源模型元信息（可选）
            Integer modelVersion = null;
            try {
                Object mv = body.get("modelVersion");
                if (mv != null) modelVersion = Integer.valueOf(String.valueOf(mv));
            } catch (Exception ignore) {}
            String modelId = null;
            try { Object mid = body.get("modelId"); if (mid != null) modelId = String.valueOf(mid); } catch (Exception ignore) {}
            String modelKey = null;
            try { Object mk = body.get("modelKey"); if (mk != null) modelKey = String.valueOf(mk); } catch (Exception ignore) {}
            Object xmlObj = body.get("xml");
            if (xmlObj == null) {
                return Result.error("缺少xml字段");
            }
            String xml = String.valueOf(xmlObj);
            if (xml.trim().isEmpty()) {
                return Result.error("xml内容为空");
            }

            // ========== 幂等校验（代码层）==========
            // 如果携带了 modelId + modelVersion，则仅允许部署一次
            if (modelId != null && modelVersion != null) {
                // 读取所有部署记录，检查 jeecg-deploy-meta.json 中是否已有相同 (modelId, modelVersion)
                List<Deployment> deployments = repositoryService.createDeploymentQuery().list();
                for (Deployment dep : deployments) {
                    try {
                        List<String> res = repositoryService.getDeploymentResourceNames(dep.getId());
                        if (res == null) continue;
                        if (!res.contains("jeecg-deploy-meta.json") && !res.contains("deployment-description.txt")) continue;
                        String metaName = res.contains("jeecg-deploy-meta.json") ? "jeecg-deploy-meta.json" : "deployment-description.txt";
                        InputStream is = repositoryService.getResourceAsStream(dep.getId(), metaName);
                        String meta = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
                        try {
                            JSONObject obj = JSON.parseObject(meta);
                            if (obj != null) {
                                String mid = obj.getString("modelId");
                                Integer mv = obj.getInteger("modelVersion");
                                if (modelId.equals(mid) && modelVersion.equals(mv)) {
                                    return Result.error("该模型版本已部署，禁止重复部署");
                                }
                            }
                        } catch (Exception ignore) {}
                    } catch (Exception ignore) {}
                }
            }

            // 如前端传入了 name，则同步覆盖 XML 的 <process name="...">
            if (name != null && name.trim().length() > 0) {
                xml = overrideProcessNameInXml(xml, name.trim());
            }

            // 统一处理：直接用字符串部署
            String resourceName = (name != null && name.trim().length() > 0 ? name.trim() : "process") + ".bpmn20.xml";
            java.io.InputStream inputStream = new java.io.ByteArrayInputStream(xml.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            org.flowable.engine.repository.DeploymentBuilder builder = repositoryService.createDeployment()
                    .addInputStream(resourceName, inputStream)
                    .name(name)
                    .category(category);
            try {
                JSONObject meta = new JSONObject();
                if (description != null && description.trim().length() > 0) meta.put("description", description);
                if (modelVersion != null) meta.put("modelVersion", modelVersion);
                if (modelId != null) meta.put("modelId", modelId);
                if (modelKey != null) meta.put("modelKey", modelKey);
                builder.addString("jeecg-deploy-meta.json", meta.toJSONString());
            } catch (Exception ignore) {}
            Deployment deployment = builder.deploy();

            // 触发部署后事件（与文件上传部署保持一致）
            try {
                List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deployment.getId())
                    .list();
                for (ProcessDefinition pd : processDefinitions) {
                    try {
                        log.info("自动触发流程定义部署后处理：{}", pd.getKey());
                        workflowEventService.onProcessDefinitionDeployed(pd.getKey());
                    } catch (Exception e) {
                        log.error("流程定义部署后处理失败：" + pd.getKey(), e);
                    }
                }
            } catch (Exception e) {
                log.warn("部署后事件处理异常，不影响部署：{}", e.getMessage());
            }

            log.info("流程定义(JSON)部署成功，部署ID：{}", deployment.getId());
            return Result.OK("流程定义部署成功", deployment.getId());
        } catch (Exception e) {
            log.error("部署流程定义(JSON)失败", e);
            return Result.error("部署流程定义失败：" + e.getMessage());
        }
    }

    /**
     * 挂起/激活流程定义
     */
    @AutoLog(value = "挂起/激活流程定义")
    @Operation(summary = "挂起/激活流程定义", description = "挂起/激活流程定义")
    @PutMapping("/{id}/{action}")
    public Result<String> toggleDefinitionState(
            @PathVariable String id,
            @PathVariable @Parameter(description = "操作类型：suspend挂起，activate激活") String action) {
        
        try {
            ProcessDefinition definition = repositoryService.getProcessDefinition(id);
            if (definition == null) {
                return Result.error("流程定义不存在");
            }
            
            if ("suspend".equals(action)) {
                repositoryService.suspendProcessDefinitionById(id);
                log.info("流程定义挂起成功，ID：{}", id);
                return Result.OK("流程定义挂起成功");
            } else if ("activate".equals(action)) {
                repositoryService.activateProcessDefinitionById(id);
                log.info("流程定义激活成功，ID：{}", id);
                return Result.OK("流程定义激活成功");
            } else {
                return Result.error("不支持的操作类型：" + action);
            }
            
        } catch (Exception e) {
            log.error("切换流程定义状态失败", e);
            return Result.error("切换流程定义状态失败：" + e.getMessage());
        }
    }

    /**
     * 同步：将配置表写入 BPMN 扩展（仅返回XML，不直接部署）
     */
    @AutoLog(value = "流程定义-同步配置到BPMN")
    @Operation(summary = "同步配置到BPMN", description = "根据配置表(onl_cgform_workflow_node)写入用户任务的formKey到userTask属性，返回更新后的XML（不部署）")
    @PostMapping("/{id}/syncFromConfig")
    @RequiresPermissions("workflow:definition:sync")
    public Result<String> syncFromConfig(@PathVariable String id) {
        try {
            ProcessDefinition definition = repositoryService.getProcessDefinition(id);
            if (definition == null) {
                return Result.error("流程定义不存在");
            }
            InputStream is = repositoryService.getResourceAsStream(
                definition.getDeploymentId(), definition.getResourceName());
            String xml = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);

            if (onlCgformWorkflowNodeMapper == null) {
                return Result.error("未启用节点配置组件，无法同步");
            }
            String processKey = definition.getKey();
            List<OnlCgformWorkflowNode> nodes = onlCgformWorkflowNodeMapper.selectList(
                new LambdaQueryWrapper<OnlCgformWorkflowNode>()
                    .eq(OnlCgformWorkflowNode::getProcessDefinitionKey, processKey)
                    .eq(OnlCgformWorkflowNode::getStatus, 1)
            );

            if (nodes != null) {
                for (OnlCgformWorkflowNode n : nodes) {
                    String nodeId = n.getNodeId();
                    String formKey = null;
                    try {
                        java.lang.reflect.Method gm = n.getClass().getMethod("getFormKey");
                        Object v = gm.invoke(n);
                        formKey = v == null ? null : String.valueOf(v);
                    } catch (Exception ignore) {}
                    if (nodeId == null || nodeId.isEmpty() || formKey == null || formKey.isEmpty()) {
                        continue;
                    }

                    // 针对特定 userTask（id=...）写入/更新 formKey 属性（简化版正则处理）
                    String utPattern = "(<userTask[^>]*id=\\\"" + java.util.regex.Pattern.quote(nodeId) + "\\\"[^>]*)(>)";
                    java.util.regex.Pattern p = java.util.regex.Pattern.compile(utPattern);
                    java.util.regex.Matcher m = p.matcher(xml);
                    StringBuffer sb = new StringBuffer();
                    boolean found = false;
                    while (m.find()) {
                        found = true;
                        String startTag = m.group(1);
                        // 先尝试替换已有 formKey
                        String replaced = startTag.replaceAll("formKey=\\\".*?\\\"", "formKey=\\\"" + java.util.regex.Matcher.quoteReplacement(formKey) + "\\\"");
                        if (!replaced.contains("formKey=\"")) {
                            replaced = replaced + " formKey=\\\"" + java.util.regex.Matcher.quoteReplacement(formKey) + "\\\"";
                        }
                        m.appendReplacement(sb, replaced + ">");
                    }
                    if (found) {
                        m.appendTail(sb);
                        xml = sb.toString();
                    }
                }
            }

            return Result.OK(xml);
        } catch (Exception e) {
            log.error("同步配置到BPMN失败", e);
            return Result.error("同步失败：" + e.getMessage());
        }
    }

    /**
     * 同步：从 BPMN 读取 formKey 回写到配置表（仅更新已存在的记录，非权威导入）
     */
    @AutoLog(value = "流程定义-从BPMN同步到配置")
    @Operation(summary = "从BPMN同步到配置", description = "读取BPMN中用户任务的formKey，回写到onl_cgform_workflow_node（仅更新存在记录）")
    @PostMapping("/{id}/syncToConfig")
    @RequiresPermissions("workflow:definition:sync")
    public Result<String> syncToConfig(@PathVariable String id) {
        try {
            ProcessDefinition definition = repositoryService.getProcessDefinition(id);
            if (definition == null) {
                return Result.error("流程定义不存在");
            }
            InputStream is = repositoryService.getResourceAsStream(
                definition.getDeploymentId(), definition.getResourceName());
            String xml = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            String processKey = definition.getKey();

            if (onlCgformWorkflowNodeMapper == null) {
                return Result.error("未启用节点配置组件，无法同步");
            }

            // 简化解析：匹配所有 userTask，提取 id 与 formKey
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("<userTask[^>]*id=\\\"(.*?)\\\"[^>]*>");
            java.util.regex.Matcher m = p.matcher(xml);
            int updated = 0;
            while (m.find()) {
                String startTag = m.group();
                String nodeId = m.group(1);
                java.util.regex.Matcher mk = java.util.regex.Pattern.compile("formKey=\\\"(.*?)\\\"").matcher(startTag);
                if (mk.find()) {
                    String formKey = mk.group(1);
                    try {
                        OnlCgformWorkflowNode existing = onlCgformWorkflowNodeMapper.selectByProcessAndNode(processKey, nodeId);
                        if (existing != null) {
                            java.lang.reflect.Method sm = existing.getClass().getMethod("setFormKey", String.class);
                            sm.invoke(existing, formKey);
                            onlCgformWorkflowNodeMapper.updateById(existing);
                            updated++;
                        }
                    } catch (Exception ignore) {}
                }
            }
            return Result.OK("已更新 " + updated + " 条记录");
        } catch (Exception e) {
            log.error("从BPMN同步到配置失败", e);
            return Result.error("同步失败：" + e.getMessage());
        }
    }

    /**
     * 将 BPMN XML 中第一个 <process> 元素的 name 属性覆盖为给定值。
     * 简单正则处理，尽可能不影响其他内容；若未找到 name 属性则追加。
     */
    private String overrideProcessNameInXml(String xml, String newName) {
        if (xml == null || xml.isEmpty()) {
            return xml;
        }
        try {
            // 匹配第一个 <process ...> 起始标签
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("(<process\\b[^>]*)(>)");
            java.util.regex.Matcher m = p.matcher(xml);
            if (m.find()) {
                String startTag = m.group(1);
                String end = m.group(2);
                // 先替换已有 name="..."
                String replaced = startTag.replaceAll("name=\\\".*?\\\"", "name=\\\"" + java.util.regex.Matcher.quoteReplacement(newName) + "\\\"");
                // 若没有 name 属性，则追加
                if (!replaced.contains("name=\"")) {
                    replaced = replaced + " name=\"" + newName.replace("\"", " ") + "\"";
                }
                StringBuilder sb = new StringBuilder();
                m.appendReplacement(sb, replaced + end);
                m.appendTail(sb);
                return sb.toString();
            }
        } catch (Exception ignore) {
        }
        return xml;
    }
}