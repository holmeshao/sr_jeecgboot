package org.jeecg.modules.workflow.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            @RequestParam(required = false) String category) {
        
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
            
            // 按流程定义版本倒序（Flowable 7.0 API）
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
            @RequestParam(required = false) String category) {
        
        try {
            if (file.isEmpty()) {
                return Result.error("请选择要部署的流程文件");
            }
            
            String filename = file.getOriginalFilename();
            if (!filename.endsWith(".bpmn") && !filename.endsWith(".bpmn20.xml")) {
                return Result.error("只支持.bpmn或.bpmn20.xml格式的流程文件");
            }
            
            InputStream inputStream = file.getInputStream();
            String deploymentName = name != null ? name : filename;
            
            Deployment deployment = repositoryService.createDeployment()
                    .addInputStream(filename, inputStream)
                    .name(deploymentName)
                    .category(category)
                    .deploy();
            
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
    @DeleteMapping("/{deploymentId}")
    public Result<String> deleteDefinition(
            @PathVariable String deploymentId,
            @RequestParam(defaultValue = "false") Boolean cascade) {
        
        try {
            if (cascade) {
                // 级联删除，同时删除流程实例
                repositoryService.deleteDeployment(deploymentId, true);
            } else {
                repositoryService.deleteDeployment(deploymentId);
            }
            
            log.info("流程定义删除成功，部署ID：{}", deploymentId);
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
}