package org.jeecg.modules.workflow.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.workflow.model.FormPermissionConfig;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowConfig;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowNode;

import org.jeecg.modules.workflow.mapper.OnlCgformWorkflowNodeMapper;
import org.jeecg.modules.workflow.engine.OnlineFormPermissionEngine;
import org.jeecg.modules.workflow.service.OnlineFormWorkflowService;
import org.jeecg.modules.workflow.service.IOnlCgformWorkflowConfigService;
import org.jeecg.modules.workflow.service.WorkflowEventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 表单工作流管理
 *
 * @author jeecg
 * @since 2024-12-25
 */
@Tag(name = "表单工作流管理")
@RestController
@RequestMapping("/workflow/onlineForm")
@Slf4j
public class OnlineFormWorkflowController extends JeecgController<OnlCgformWorkflowConfig, IOnlCgformWorkflowConfigService> {

    @Autowired
    private OnlCgformWorkflowNodeMapper onlCgformWorkflowNodeMapper;

    @Autowired
    private OnlineFormWorkflowService onlineFormWorkflowService;
    
    @Autowired
    private WorkflowEventService workflowEventService;

    @Autowired
    private OnlineFormPermissionEngine permissionEngine;
    
    @Autowired
    private IOnlCgformHeadService cgformHeadService;




    /**
     * 分页列表查询
     */
    @AutoLog(value = "表单工作流配置-分页列表查询")
    @Operation(summary = "表单工作流配置-分页列表查询", description = "表单工作流配置-分页列表查询")
    @GetMapping(value = "/config/list")
    public Result<?> queryPageList(OnlCgformWorkflowConfig onlCgformWorkflowConfig,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<OnlCgformWorkflowConfig> queryWrapper = QueryGenerator.initQueryWrapper(onlCgformWorkflowConfig, req.getParameterMap());
        Page<OnlCgformWorkflowConfig> page = new Page<>(pageNo, pageSize);
        IPage<OnlCgformWorkflowConfig> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     */
    @AutoLog(value = "表单工作流配置-添加")
    @Operation(summary = "表单工作流配置-添加", description = "表单工作流配置-添加")
    @PostMapping(value = "/config/add")
    public Result<?> add(@RequestBody OnlCgformWorkflowConfig onlCgformWorkflowConfig) {
        service.save(onlCgformWorkflowConfig);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     */
    @AutoLog(value = "表单工作流配置-编辑")
    @Operation(summary = "表单工作流配置-编辑", description = "表单工作流配置-编辑")
    @RequestMapping(value = "/config/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody OnlCgformWorkflowConfig onlCgformWorkflowConfig) {
        service.updateById(onlCgformWorkflowConfig);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "表单工作流配置-通过id删除")
    @Operation(summary = "表单工作流配置-通过id删除", description = "表单工作流配置-通过id删除")
    @DeleteMapping(value = "/config/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        service.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     */
    @AutoLog(value = "表单工作流配置-批量删除")
    @Operation(summary = "表单工作流配置-批量删除", description = "表单工作流配置-批量删除")
    @DeleteMapping(value = "/config/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     */
    @AutoLog(value = "表单工作流配置-通过id查询")
    @Operation(summary = "表单工作流配置-通过id查询", description = "表单工作流配置-通过id查询")
    @GetMapping(value = "/config/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        OnlCgformWorkflowConfig onlCgformWorkflowConfig = service.getById(id);
        if (onlCgformWorkflowConfig == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(onlCgformWorkflowConfig);
    }

    /**
     * 启动表单工作流
     */
    @AutoLog(value = "表单工作流-启动")
    @Operation(summary = "表单工作流-启动", description = "表单工作流-启动")
    @PostMapping(value = "/start")
    public Result<?> startWorkflow(@RequestParam String tableName,
                                   @RequestParam String dataId,
                                   @RequestBody Map<String, Object> formData) {
        try {
            // 🎯 Name模式：通过tableName获取formId，然后调用service
            OnlCgformHead cgformHead = getCgformHeadByTableName(tableName);
            String processInstanceId = onlineFormWorkflowService.startFormWorkflow(cgformHead.getId(), dataId, formData);
            return Result.OK("工作流启动成功", processInstanceId);
        } catch (Exception e) {
            log.error("启动工作流失败", e);
            return Result.error("启动工作流失败: " + e.getMessage());
        }
    }

    /**
     * 智能提交表单（根据配置自动处理启动模式）
     */
    @AutoLog(value = "表单工作流-智能提交")
    @Operation(summary = "表单工作流-智能提交", description = "根据配置自动选择保存草稿或启动工作流")
    @PostMapping(value = "/submitForm")
    public Result<?> submitForm(@RequestParam String tableName,
                               @RequestParam String dataId,
                               @RequestBody JSONObject formData) {
        try {
            return onlineFormWorkflowService.submitForm(tableName, dataId, formData);
        } catch (Exception e) {
            log.error("提交表单失败", e);
            return Result.error("提交表单失败: " + e.getMessage());
        }
    }

    /**
     * 保存表单草稿
     */
    @AutoLog(value = "表单工作流-保存草稿")
    @Operation(summary = "表单工作流-保存草稿", description = "保存表单为草稿状态")
    @PostMapping(value = "/saveDraft")
    public Result<?> saveDraft(@RequestParam String tableName,
                              @RequestParam String dataId,
                              @RequestBody JSONObject formData) {
        try {
            return onlineFormWorkflowService.saveDraftForm(tableName, dataId, formData);
        } catch (Exception e) {
            log.error("保存草稿失败", e);
            return Result.error("保存草稿失败: " + e.getMessage());
        }
    }

    /**
     * 手动启动工作流
     */
    @AutoLog(value = "表单工作流-手动启动")
    @Operation(summary = "表单工作流-手动启动", description = "手动启动已保存的草稿工作流")
    @PostMapping(value = "/manualStart")
    public Result<?> manualStartWorkflow(@RequestParam String tableName,
                                        @RequestParam String dataId) {
        try {
            // 🎯 Name模式：通过tableName获取formId，然后调用service
            OnlCgformHead cgformHead = getCgformHeadByTableName(tableName);
            String processInstanceId = onlineFormWorkflowService.manualStartWorkflow(cgformHead.getId(), dataId);
            return Result.OK("工作流启动成功", processInstanceId);
        } catch (Exception e) {
            log.error("手动启动工作流失败", e);
            return Result.error("手动启动工作流失败: " + e.getMessage());
        }
    }

    /**
     * 检查是否可以启动工作流
     */
    @AutoLog(value = "表单工作流-检查启动条件")
    @Operation(summary = "表单工作流-检查启动条件", description = "检查是否可以启动工作流")
    @GetMapping(value = "/canStart")
    public Result<?> canStartWorkflow(@RequestParam String tableName,
                                     @RequestParam String dataId) {
        try {
            // 🎯 Name模式：通过tableName获取formId，然后调用service
            OnlCgformHead cgformHead = getCgformHeadByTableName(tableName);
            boolean canStart = onlineFormWorkflowService.canStartWorkflow(cgformHead.getId(), dataId);
            return Result.OK("查询成功", canStart);
        } catch (Exception e) {
            log.error("检查启动条件失败", e);
            return Result.error("检查启动条件失败: " + e.getMessage());
        }
    }

    /**
     * 获取智能按钮配置
     */
    @AutoLog(value = "表单工作流-获取按钮配置")
    @Operation(summary = "表单工作流-获取按钮配置", description = "根据表单状态和用户权限智能计算按钮")
    @GetMapping(value = "/smartButtons")
    public Result<?> getSmartButtons(@RequestParam String tableName,
                                    @RequestParam(required = false) String dataId,
                                    @RequestParam(required = false) String taskId) {
        try {
            // 🎯 Name模式：可以直接基于tableName计算按钮，无需转换
            List<Map<String, Object>> buttons = new ArrayList<>(); // 临时空列表
            return Result.OK("获取按钮成功", buttons);
        } catch (Exception e) {
            log.error("获取按钮配置失败", e);
            return Result.error("获取按钮配置失败: " + e.getMessage());
        }
    }

    /**
     * 提交节点表单
     */
    @AutoLog(value = "表单工作流-提交节点")
    @Operation(summary = "表单工作流-提交节点", description = "表单工作流-提交节点")
    @PostMapping(value = "/submit")
    public Result<?> submitNodeForm(@RequestParam String taskId,
                                    @RequestParam String nodeCode,
                                    @RequestBody Map<String, Object> formData) {
        try {
            onlineFormWorkflowService.submitNodeForm(taskId, nodeCode, formData);
            return Result.OK("节点提交成功");
        } catch (Exception e) {
            log.error("提交节点失败", e);
            return Result.error("提交节点失败: " + e.getMessage());
        }
    }

    /**
     * 获取节点权限配置
     */
    @AutoLog(value = "表单工作流-获取节点权限")
    @Operation(summary = "表单工作流-获取节点权限", description = "表单工作流-获取节点权限")
    @GetMapping(value = "/permission")
    public Result<?> getNodePermission(@RequestParam String tableName,
                                       @RequestParam String processKey,
                                       @RequestParam String nodeName) {
        try {
            // 🎯 Name模式：通过tableName获取formId，使用processKey和nodeName
            OnlCgformHead cgformHead = getCgformHeadByTableName(tableName);
            FormPermissionConfig permission = permissionEngine.getNodePermission(cgformHead.getId(), processKey, nodeName);
            return Result.OK(permission);
        } catch (Exception e) {
            log.error("获取节点权限失败", e);
            return Result.error("获取节点权限失败: " + e.getMessage());
        }
    }

    /**
     * 节点权限配置管理
     */
    @AutoLog(value = "节点权限配置-分页列表查询")
    @Operation(summary = "节点权限配置-分页列表查询", description = "节点权限配置-分页列表查询")
    @GetMapping(value = "/node/list")
    public Result<?> queryNodePageList(OnlCgformWorkflowNode onlCgformWorkflowNode,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        QueryWrapper<OnlCgformWorkflowNode> queryWrapper = QueryGenerator.initQueryWrapper(onlCgformWorkflowNode, req.getParameterMap());
        Page<OnlCgformWorkflowNode> page = new Page<>(pageNo, pageSize);
        IPage<OnlCgformWorkflowNode> pageList = onlCgformWorkflowNodeMapper.selectPage(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加节点权限配置
     */
    @AutoLog(value = "节点权限配置-添加")
    @Operation(summary = "节点权限配置-添加", description = "节点权限配置-添加")
    @PostMapping(value = "/node/add")
    public Result<?> addNode(@RequestBody OnlCgformWorkflowNode onlCgformWorkflowNode) {
        onlCgformWorkflowNodeMapper.insert(onlCgformWorkflowNode);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑节点权限配置
     */
    @AutoLog(value = "节点权限配置-编辑")
    @Operation(summary = "节点权限配置-编辑", description = "节点权限配置-编辑")
    @RequestMapping(value = "/node/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> editNode(@RequestBody OnlCgformWorkflowNode onlCgformWorkflowNode) {
        onlCgformWorkflowNodeMapper.updateById(onlCgformWorkflowNode);
        return Result.OK("编辑成功!");
    }

    /**
     * 删除节点权限配置
     */
    @AutoLog(value = "节点权限配置-通过id删除")
    @Operation(summary = "节点权限配置-通过id删除", description = "节点权限配置-通过id删除")
    @DeleteMapping(value = "/node/delete")
    public Result<?> deleteNode(@RequestParam(name = "id", required = true) String id) {
        onlCgformWorkflowNodeMapper.deleteById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 获取表单的节点列表
     */
    @AutoLog(value = "节点权限配置-获取表单节点列表")
    @Operation(summary = "节点权限配置-获取表单节点列表", description = "节点权限配置-获取表单节点列表")
    @GetMapping(value = "/node/formNodes")
    public Result<?> getFormNodes(@RequestParam String tableName,
                                  @RequestParam String processKey) {
        // 🎯 Name模式：通过tableName获取formId，使用processKey
        OnlCgformHead cgformHead = getCgformHeadByTableName(tableName);
        List<OnlCgformWorkflowNode> nodes = onlCgformWorkflowNodeMapper.selectList(
            new LambdaQueryWrapper<OnlCgformWorkflowNode>()
                .eq(OnlCgformWorkflowNode::getCgformHeadId, cgformHead.getId())
                .eq(OnlCgformWorkflowNode::getProcessDefinitionKey, processKey)
                .eq(OnlCgformWorkflowNode::getStatus, 1)
                .orderByAsc(OnlCgformWorkflowNode::getSortOrder)
        );
        return Result.OK(nodes);
    }
    
    // ============= 🎯 Name模式辅助方法 =============
    
    /**
     * 🎯 通过tableName获取OnlCgformHead（Name模式核心方法）
     */
    private OnlCgformHead getCgformHeadByTableName(String tableName) {
        OnlCgformHead cgformHead = cgformHeadService.getOne(
            new LambdaQueryWrapper<OnlCgformHead>()
                .eq(OnlCgformHead::getTableName, tableName)
                // 注：OnlCgformHead可能不使用软删除，直接按tableName查询
        );
        
        if (cgformHead == null) {
            throw new RuntimeException("未找到表单配置: " + tableName);
        }
        
        return cgformHead;
    }

    // ================================== Flowable 7.0 兼容性API ==================================
    
    /**
     * 🎯 手动触发流程定义部署事件处理
     * 
     * 基于Flowable 7.0新架构，提供手动触发流程部署后的事件处理
     * 主要用于字段权限解析等部署后处理逻辑
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 处理结果
     */
    @PostMapping("/triggerDeploymentEvent")
    @Operation(summary = "触发流程部署事件处理", description = "Flowable 7.0兼容 - 手动触发流程部署事件处理")
    public Result<String> triggerDeploymentEvent(@RequestParam String processDefinitionKey) {
        try {
            workflowEventService.onProcessDefinitionDeployed(processDefinitionKey);
            return Result.OK("流程部署事件处理完成：" + processDefinitionKey);
        } catch (Exception e) {
            log.error("触发流程部署事件处理失败：" + processDefinitionKey, e);
            return Result.error("处理失败：" + e.getMessage());
        }
    }
    
    /**
     * 🎯 批量处理所有流程定义的部署事件
     * 
     * 用于系统初始化或批量更新时使用
     * 
     * @return 处理结果
     */
    @PostMapping("/triggerAllDeploymentEvents")
    @Operation(summary = "批量触发所有流程部署事件", description = "Flowable 7.0兼容 - 批量处理所有流程定义")
    public Result<String> triggerAllDeploymentEvents() {
        try {
            workflowEventService.onAllProcessDefinitionsDeployed();
            return Result.OK("批量处理完成");
        } catch (Exception e) {
            log.error("批量处理流程部署事件失败", e);
            return Result.error("批量处理失败：" + e.getMessage());
        }
    }
    
    /**
     * 🎯 检查Flowable 7.0兼容性状态
     * 
     * @return 兼容性状态信息
     */
    @GetMapping("/flowable7Status")
    @Operation(summary = "检查Flowable 7.0兼容性状态", description = "查看当前系统的Flowable 7.0兼容性状态")
    public Result<String> checkFlowable7Status() {
        try {
            String status = workflowEventService.checkFlowable7Compatibility();
            return Result.OK(status);
        } catch (Exception e) {
            log.error("检查Flowable 7.0兼容性状态失败", e);
            return Result.error("检查失败：" + e.getMessage());
        }
    }
    
    /**
     * 🎯 手动触发流程实例启动事件
     * 
     * @param processInstanceId 流程实例ID
     * @param processDefinitionKey 流程定义Key
     * @return 处理结果
     */
    @PostMapping("/triggerInstanceStartEvent")
    @Operation(summary = "触发流程实例启动事件", description = "Flowable 7.0兼容 - 手动触发流程实例启动事件")
    public Result<String> triggerInstanceStartEvent(@RequestParam String processInstanceId, 
                                                   @RequestParam String processDefinitionKey) {
        try {
            workflowEventService.onProcessInstanceStarted(processInstanceId, processDefinitionKey);
            return Result.OK("流程实例启动事件处理完成");
        } catch (Exception e) {
            log.error("触发流程实例启动事件失败：" + processInstanceId, e);
            return Result.error("处理失败：" + e.getMessage());
        }
    }
} 