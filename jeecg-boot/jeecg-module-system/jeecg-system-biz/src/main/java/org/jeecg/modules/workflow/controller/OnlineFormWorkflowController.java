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
import org.jeecg.modules.workflow.dto.FormRenderConfig;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowConfig;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowNode;

import org.jeecg.modules.workflow.mapper.OnlCgformWorkflowNodeMapper;
import org.jeecg.modules.workflow.engine.OnlineFormPermissionEngine;
import org.jeecg.modules.workflow.service.OnlineFormWorkflowService;
import org.jeecg.modules.workflow.service.IOnlCgformWorkflowConfigService;
import org.jeecg.modules.workflow.service.WorkflowEventService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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

    @Autowired
    private TaskService taskService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired(required = false)
    private RuntimeService runtimeService;

    @Autowired(required = false)
    private HistoryService historyService;

    @Autowired(required = false)
    private RepositoryService repositoryService;




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
        // 说明：此处不再使用 QueryGenerator 自动反射属性，
        // 避免将实体上的辅助 getter（如 getStatusFieldOrDefault）当作列名，造成非法列错误。
        QueryWrapper<OnlCgformWorkflowConfig> queryWrapper = new QueryWrapper<>();
        try {
            // 白名单方式，仅允许已存在的物理列参与过滤
            Map<String, String[]> pm = req.getParameterMap();
            if (pm != null) {
                String v;
                v = firstParam(pm.get("cgformHeadId"));
                if (v != null && v.length() > 0) queryWrapper.eq("cgform_head_id", v);
                v = firstParam(pm.get("processDefinitionKey"));
                if (v != null && v.length() > 0) queryWrapper.eq("process_definition_key", v);
                v = firstParam(pm.get("workflowEnabled"));
                if (v != null && v.length() > 0) queryWrapper.eq("workflow_enabled", v);
                v = firstParam(pm.get("status"));
                if (v != null && v.length() > 0) queryWrapper.eq("status", v);
                v = firstParam(pm.get("uiMode"));
                if (v != null && v.length() > 0) queryWrapper.eq("ui_mode", v);
            }
        } catch (Exception ignore) {}
        queryWrapper.orderByDesc("update_time").orderByDesc("create_time");

        Page<OnlCgformWorkflowConfig> page = new Page<>(pageNo, pageSize);
        IPage<OnlCgformWorkflowConfig> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    private static String firstParam(String[] arr) {
        return (arr != null && arr.length > 0) ? arr[0] : null;
    }

    /**
     * 添加
     */
    @AutoLog(value = "表单工作流配置-添加")
    @Operation(summary = "表单工作流配置-添加", description = "表单工作流配置-添加")
    @PostMapping(value = "/config/add")
    public Result<?> add(@RequestBody OnlCgformWorkflowConfig onlCgformWorkflowConfig) {
        // Upsert：同一表单只保留一条配置，按 cgform_head_id 唯一
        OnlCgformWorkflowConfig exist = service.getOne(
            new LambdaQueryWrapper<OnlCgformWorkflowConfig>()
                .eq(OnlCgformWorkflowConfig::getCgformHeadId, onlCgformWorkflowConfig.getCgformHeadId())
        );
        if (exist != null) {
            onlCgformWorkflowConfig.setId(exist.getId());
            service.updateById(onlCgformWorkflowConfig);
            return Result.OK("更新成功！");
        } else {
            service.save(onlCgformWorkflowConfig);
            return Result.OK("添加成功！");
        }
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
     * 表单基础信息（UniversalFormPage依赖）
     */
    @AutoLog(value = "表单工作流-基础信息")
    @Operation(summary = "表单工作流-基础信息", description = "返回表单基础信息与流程绑定信息")
    @GetMapping("/form/basic-info")
    //@RequiresPermissions("workflow:form:basic")
    public Result<Map<String, Object>> getFormBasicInfo(@RequestParam String tableName,
                                                        @RequestParam String dataId) {
        try {
            Map<String, Object> info = new java.util.HashMap<>();
            OnlCgformHead head = getCgformHeadByTableName(tableName);
            info.put("formId", head.getId());

            // 使用受信的表名（来自元数据）避免注入
            String safeTable = head.getTableName();
            Map<String, Object> row = null;
            try {
                row = jdbcTemplate.queryForMap("select * from " + safeTable + " where id = ?", dataId);
            } catch (Exception ignore) {}

            // 提取通用字段
            Object createBy = row != null ? row.getOrDefault("create_by", null) : null;
            Object createTime = row != null ? row.getOrDefault("create_time", null) : null;
            Object updateBy = row != null ? row.getOrDefault("update_by", null) : null;
            Object updateTime = row != null ? row.getOrDefault("update_time", null) : null;
            // 优先 bpmn_status；兼容历史 bmp_status
            Object status = null;
            if (row != null) {
                status = row.getOrDefault("bpmn_status", null);
                if (status == null) {
                    status = row.getOrDefault("bmp_status", null);
                }
            }
            Object pi = row != null ? row.getOrDefault("process_instance_id", null) : null;

            info.put("createBy", createBy);
            info.put("createTime", createTime);
            info.put("updateBy", updateBy);
            info.put("updateTime", updateTime);
            info.put("formStatus", status != null ? status : "DRAFT");
            info.put("processInstanceId", pi);

            // 推断流程定义Key（用于 UI 模式与权限配置按流程粒度）
            if (pi != null && repositoryService != null) {
                try {
                    String pdId = null;
                    if (runtimeService != null) {
                        ProcessInstance inst = runtimeService.createProcessInstanceQuery()
                            .processInstanceId(String.valueOf(pi)).singleResult();
                        if (inst != null) pdId = inst.getProcessDefinitionId();
                    }
                    if (pdId == null && historyService != null) {
                        HistoricProcessInstance h = historyService.createHistoricProcessInstanceQuery()
                            .processInstanceId(String.valueOf(pi)).singleResult();
                        if (h != null) pdId = h.getProcessDefinitionId();
                    }
                    if (pdId != null) {
                        ProcessDefinition def = repositoryService.getProcessDefinition(pdId);
                        if (def != null) {
                            info.put("processDefinitionKey", def.getKey());
                        } else {
                            // 回退：从 pdId 提取 key（格式一般为 key:version:id）
                            int idx = pdId.indexOf(":");
                            if (idx > 0) {
                                info.put("processDefinitionKey", pdId.substring(0, idx));
                            }
                        }
                    }
                } catch (Exception ignore) {}
            }

            // 推断一个可读编号（可选）
            if (row != null) {
                Object no = firstNonNull(
                    row.get("form_no"), row.get("bill_no"), row.get("report_no"),
                    row.get("code"), row.get("no"), row.get("order_no")
                );
                if (no != null) {
                    info.put("formNo", no);
                }
            }

            return Result.OK(info);
        } catch (Exception e) {
            log.error("获取表单基础信息失败", e);
            return Result.error("获取表单基础信息失败: " + e.getMessage());
        }
    }

    private Object firstNonNull(Object... values) {
        if (values == null) return null;
        for (Object v : values) {
            if (v != null) return v;
        }
        return null;
    }

    /**
     * 智能提交（通用别名，适配前端UniversalFormPage）
     */
    @AutoLog(value = "表单工作流-智能提交(别名)")
    @Operation(summary = "表单工作流-智能提交(别名)", description = "兼容 /workflow/form/submit 调用")
    @PostMapping("/form/submit")
    @RequiresPermissions("workflow:form:submit")
    public Result<?> aliasSubmit(@RequestBody Map<String, Object> body) {
        String tableName = String.valueOf(body.get("tableName"));
        String dataId = String.valueOf(body.get("dataId"));
        Object fd = body.get("formData");
        return submitForm(tableName, dataId, com.alibaba.fastjson.JSONObject.parseObject(com.alibaba.fastjson.JSON.toJSONString(fd)));
    }

    /**
     * 保存草稿（通用别名）
     */
    @AutoLog(value = "表单工作流-保存草稿(别名)")
    @Operation(summary = "表单工作流-保存草稿(别名)", description = "兼容 /workflow/form/save-draft 调用")
    @PostMapping("/form/save-draft")
    @RequiresPermissions("workflow:form:save")
    public Result<?> aliasSaveDraft(@RequestBody Map<String, Object> body) {
        String tableName = String.valueOf(body.get("tableName"));
        String dataId = String.valueOf(body.get("dataId"));
        Object fd = body.get("formData");
        return saveDraft(tableName, dataId, com.alibaba.fastjson.JSONObject.parseObject(com.alibaba.fastjson.JSON.toJSONString(fd)));
    }

    /**
     * 手动启动（通用别名）
     */
    @AutoLog(value = "表单工作流-手动启动(别名)")
    @Operation(summary = "表单工作流-手动启动(别名)", description = "兼容 /workflow/form/manual-start 调用")
    @PostMapping("/form/manual-start")
    @RequiresPermissions("workflow:form:start")
    public Result<?> aliasManualStart(@RequestBody Map<String, Object> body) {
        String tableName = String.valueOf(body.get("tableName"));
        String dataId = String.valueOf(body.get("dataId"));
        return manualStartWorkflow(tableName, dataId);
    }

    /**
     * 启动表单工作流
     */
    @AutoLog(value = "表单工作流-启动")
    @Operation(summary = "表单工作流-启动", description = "表单工作流-启动")
    @PostMapping(value = "/start")
    @RequiresPermissions("workflow:form:start")
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
    @RequiresPermissions("workflow:form:submit")
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
    @RequiresPermissions("workflow:form:save")
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
    @RequiresPermissions("workflow:form:start")
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
    @RequiresPermissions("workflow:form:start")
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
    @RequiresPermissions("workflow:form:buttons")
    public Result<?> getSmartButtons(@RequestParam String tableName,
                                    @RequestParam(required = false) String dataId,
                                    @RequestParam(required = false) String taskId) {
        try {
            // 🎯 Name模式：可以直接基于tableName计算按钮，无需转换
            List<Map<String, Object>> buttons = new ArrayList<>();

            if (taskId != null && taskId.length() > 0) {
                Map<String, Object> approve = new java.util.HashMap<>();
                approve.put("id", "approve");
                approve.put("code", "approve");
                approve.put("text", "同意");
                approve.put("type", "primary");
                approve.put("icon", "CheckCircleOutlined");
                approve.put("action", "APPROVE");
                approve.put("permission", "workflow:task:complete");
                approve.put("order", 10);
                buttons.add(approve);

                Map<String, Object> reject = new java.util.HashMap<>();
                reject.put("id", "reject");
                reject.put("code", "reject");
                reject.put("text", "驳回");
                reject.put("type", "danger");
                reject.put("icon", "CloseCircleOutlined");
                reject.put("action", "REJECT");
                reject.put("permission", "workflow:task:complete");
                reject.put("order", 20);
                buttons.add(reject);
            } else {
                Map<String, Object> save = new java.util.HashMap<>();
                save.put("id", "save_draft");
                save.put("code", "save_draft");
                save.put("text", "保存草稿");
                save.put("type", "default");
                save.put("icon", "SaveOutlined");
                save.put("action", "SAVE");
                save.put("permission", "workflow:form:save");
                save.put("order", 10);
                buttons.add(save);

                Map<String, Object> submit = new java.util.HashMap<>();
                submit.put("id", "submit_review");
                submit.put("code", "submit_review");
                submit.put("text", "提交审核");
                submit.put("type", "primary");
                submit.put("icon", "SendOutlined");
                submit.put("action", "SUBMIT");
                submit.put("permission", "workflow:form:submit");
                submit.put("order", 20);
                buttons.add(submit);
            }

            // 合并节点自定义按钮（若存在）
            try {
                OnlCgformHead head = getCgformHeadByTableName(tableName);
                String formId = head.getId();
                String nodeId = null;
                String processKey = null;
                if (taskId != null && taskId.length() > 0) {
                    Task t = taskService.createTaskQuery().taskId(taskId).singleResult();
                    if (t != null) {
                        nodeId = t.getTaskDefinitionKey();
                        String pdId = t.getProcessDefinitionId();
                        if (pdId != null && pdId.contains(":")) {
                            processKey = pdId.split(":")[0];
                        }
                    }
                }
                OnlCgformWorkflowNode nodeCfg = null;
                if (nodeId != null) {
                    nodeCfg = onlCgformWorkflowNodeMapper.selectByFormAndNode(formId, nodeId);
                    if (nodeCfg == null && processKey != null) {
                        nodeCfg = onlCgformWorkflowNodeMapper.selectByProcessAndNode(processKey, nodeId);
                    }
                }
                if (nodeCfg != null) {
                    // 隐藏按钮
                    java.util.Set<String> hidden = new java.util.HashSet<>();
                    try {
                        if (nodeCfg.getHiddenButtons() != null) {
                            hidden.addAll(JSON.parseArray(nodeCfg.getHiddenButtons(), String.class));
                        }
                    } catch (Exception ignore) {}

                    // 自定义按钮（与默认集合合并）
                    try {
                        if (nodeCfg.getCustomButtons() != null) {
                            JSONArray arr = JSON.parseArray(nodeCfg.getCustomButtons());
                            for (int i = 0; i < arr.size(); i++) {
                                JSONObject jb = arr.getJSONObject(i);
                                if (jb == null) continue;
                                String code = jb.getString("code");
                                if (code != null && hidden.contains(code)) continue;
                                Map<String, Object> btn = new java.util.HashMap<>();
                                btn.put("id", jb.getString("id"));
                                btn.put("code", code);
                                btn.put("text", jb.getString("text"));
                                btn.put("type", jb.getString("type"));
                                btn.put("icon", jb.getString("icon"));
                                btn.put("action", jb.getString("action"));
                                btn.put("permission", jb.getString("permission"));
                                btn.put("order", jb.getInteger("order"));
                                buttons.add(btn);
                            }
                        }
                    } catch (Exception ignore) {}

                    // 过滤隐藏按钮
                    if (!hidden.isEmpty()) {
                        buttons.removeIf(b -> hidden.contains(String.valueOf(b.get("code"))));
                    }
                }
            } catch (Exception ignore) {}

            // 基于Shiro权限做服务器端显隐过滤（兜底）
            try {
                org.apache.shiro.subject.Subject subject = org.apache.shiro.SecurityUtils.getSubject();
                buttons.removeIf(b -> {
                    Object perm = b.get("permission");
                    return perm != null && perm.toString().length() > 0 && !subject.isPermitted(perm.toString());
                });
            } catch (Exception ignore) {}

            // TODO: 后续可合并 onl_cgform_workflow_node.customButtons (JSON) 与 BPMN 扩展定义
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
    @RequiresPermissions("workflow:form:nodeSubmit")
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
     * 表单导出（别名路径，适配前端调用）
     */
    @AutoLog(value = "表单工作流-导出表单")
    @Operation(summary = "表单工作流-导出表单", description = "导出表单数据+流程时间线+意见（支持 pdf|csv，默认 pdf）")
    @PostMapping(value = "/form/export")
    @RequiresPermissions("workflow:form:export")
    public org.springframework.http.ResponseEntity<byte[]> exportForm(@RequestBody Map<String, Object> body) {
        try {
            // 统一入参：tableName（兼容历史 formId）
            Object tn = body.get("tableName");
            if (tn == null || String.valueOf(tn).trim().isEmpty()) {
                tn = body.get("formId");
            }
            String tableName = tn == null ? null : String.valueOf(tn);
            String dataId = body.get("dataId") == null ? null : String.valueOf(body.get("dataId"));
            String processInstanceId = body.get("processInstanceId") == null ? null : String.valueOf(body.get("processInstanceId"));
            String format = body.get("format") == null ? "pdf" : String.valueOf(body.get("format")).toLowerCase();

            if (tableName == null || tableName.trim().isEmpty() || dataId == null || dataId.trim().isEmpty()) {
                return org.springframework.http.ResponseEntity.badRequest()
                    .body("缺少必要参数：tableName 或 dataId".getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }

            // 1) 表单主数据
            OnlCgformHead head = getCgformHeadByTableName(tableName);
            String safeTable = head.getTableName();
            java.util.Map<String, Object> row = null;
            try { row = jdbcTemplate.queryForMap("select * from " + safeTable + " where id = ?", dataId); } catch (Exception ignore) {}

            StringBuilder csv = new StringBuilder();
            csv.append("类型,字段,值\n");
            if (row != null) {
                for (var e : row.entrySet()) {
                    csv.append("表单").append(',')
                       .append(e.getKey()).append(',')
                       .append(e.getValue() == null ? "" : String.valueOf(e.getValue()).replace("\n"," ").replace(","," "))
                       .append('\n');
                }
            }

            // 2) 流程时间线与意见
            if (processInstanceId != null) {
                try {
                    var tasks = historyService.createHistoricTaskInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .orderByHistoricTaskInstanceEndTime().desc()
                        .list();
                    for (var ht : tasks) {
                        csv.append("历史").append(',')
                           .append(ht.getTaskDefinitionKey()).append(',')
                           .append((ht.getName() == null ? "" : ht.getName())).append(' ')
                           .append((ht.getAssignee() == null ? "" : ht.getAssignee())).append(' ')
                           .append(ht.getEndTime() == null ? "" : new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ht.getEndTime()))
                           .append('\n');
                        try {
                            java.util.List<org.flowable.engine.task.Comment> cmts = taskService.getTaskComments(ht.getId());
                            for (var c : cmts) {
                                csv.append("意见").append(',')
                                   .append(ht.getTaskDefinitionKey()).append(',')
                                   .append(c.getFullMessage() == null ? "" : c.getFullMessage().replace("\n"," ").replace(","," "))
                                   .append('\n');
                            }
                        } catch (Exception ignore) {}
                    }
                } catch (Exception ignore) {}
            }

            if ("csv".equals(format)) {
                byte[] bytes = csv.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
                return org.springframework.http.ResponseEntity.ok()
                    .header("Content-Type", "text/csv;charset=UTF-8")
                    .header("Content-Disposition", "attachment; filename=workflow_" + dataId + ".csv")
                    .body(bytes);
            } else {
                // PDF 生成（简洁文本版，后续可换成模板/HTML渲染）
                org.apache.pdfbox.pdmodel.PDDocument doc = new org.apache.pdfbox.pdmodel.PDDocument();
                org.apache.pdfbox.pdmodel.PDPage page = new org.apache.pdfbox.pdmodel.PDPage(org.apache.pdfbox.pdmodel.common.PDRectangle.A4);
                doc.addPage(page);
                org.apache.pdfbox.pdmodel.PDPageContentStream cs = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page);
                cs.setLeading(14f);
                cs.beginText();
                cs.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 11);
                cs.newLineAtOffset(40, page.getMediaBox().getHeight() - 40);
                String[] lines = csv.toString().split("\n");
                int lineCount = 0;
                for (String line : lines) {
                    // 简化：换页逻辑
                    if (lineCount > 40) {
                        cs.endText();
                        cs.close();
                        page = new org.apache.pdfbox.pdmodel.PDPage(org.apache.pdfbox.pdmodel.common.PDRectangle.A4);
                        doc.addPage(page);
                        cs = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page);
                        cs.setLeading(14f);
                        cs.beginText();
                        cs.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 11);
                        cs.newLineAtOffset(40, page.getMediaBox().getHeight() - 40);
                        lineCount = 0;
                    }
                    cs.showText(line);
                    cs.newLine();
                    lineCount++;
                }
                cs.endText();
                cs.close();
                java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                doc.save(baos);
                doc.close();
                byte[] bytes = baos.toByteArray();
                return org.springframework.http.ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "attachment; filename=workflow_" + dataId + ".pdf")
                    .body(bytes);
            }
        } catch (Exception e) {
            log.error("导出失败", e);
            return org.springframework.http.ResponseEntity.status(500)
                .body("导出失败".getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }
    }

    /**
     * 获取节点权限配置
     */
    @AutoLog(value = "表单工作流-获取节点权限")
    @Operation(summary = "表单工作流-获取节点权限", description = "表单工作流-获取节点权限")
    @GetMapping(value = "/permission")
    @RequiresPermissions("workflow:permission:view")
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
    @RequiresPermissions("workflow:node:list")
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
    @RequiresPermissions("workflow:node:add")
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
    @RequiresPermissions("workflow:node:edit")
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
    @RequiresPermissions("workflow:node:delete")
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

    /**
     * 根据任务解析办理入口（表名+数据ID）
     */
    @AutoLog(value = "表单工作流-任务入口解析")
    @Operation(summary = "表单工作流-任务入口解析", description = "根据任务ID解析表名与数据ID")
    @GetMapping("/task/resolve")
    public Result<Map<String, Object>> resolveTaskEntry(@RequestParam String taskId) {
        try {
            Task t = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (t == null) {
                return Result.error("任务不存在");
            }

            String nodeId = t.getTaskDefinitionKey();
            String pdId = t.getProcessDefinitionId();
            String processKey = (pdId != null && pdId.contains(":")) ? pdId.split(":")[0] : pdId;

            // 1) 从节点配置解析 formKey 作为表名
            OnlCgformWorkflowNode nodeCfg = null;
            if (processKey != null) {
                nodeCfg = onlCgformWorkflowNodeMapper.selectByProcessAndNode(processKey, nodeId);
            }
            if (nodeCfg == null) {
                return Result.error("未找到节点配置: " + nodeId);
            }

            // 兼容实体未公开getter的情况，反射读取 formKey
            String tableName = null;
            try {
                java.lang.reflect.Method m = nodeCfg.getClass().getMethod("getFormKey");
                Object v = m.invoke(nodeCfg);
                tableName = v == null ? null : String.valueOf(v);
            } catch (Exception ignore) {}

            if (org.jeecg.common.util.oConvertUtils.isEmpty(tableName)) {
                return Result.error("节点未配置表单Key");
            }

            // 2) 查询数据ID（按该表的 process_instance_id 字段关联）
            OnlCgformHead head = getCgformHeadByTableName(tableName);
            String safeTable = head.getTableName();
            String dataId = null;
            try {
                dataId = jdbcTemplate.queryForObject("select id from " + safeTable + " where process_instance_id = ? limit 1",
                    new Object[]{ t.getProcessInstanceId() }, String.class);
            } catch (Exception ignore) {}

            Map<String, Object> resp = new java.util.HashMap<>();
            resp.put("tableName", tableName);
            resp.put("formId", head.getId());
            resp.put("dataId", dataId);
            resp.put("processInstanceId", t.getProcessInstanceId());
            resp.put("taskId", taskId);
            resp.put("nodeId", nodeId);
            return Result.OK(resp);
        } catch (Exception e) {
            log.error("解析任务入口失败", e);
            return Result.error("解析失败: " + e.getMessage());
        }
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

    // ================================== 🎯 表单分离/融合模式 API ==================================

    /**
     * 🎯 获取表单渲染配置（分离/融合模式统一入口）
     * 
     * 前端根据此接口返回的配置决定：
     * - 表单是否可编辑
     * - 显示哪些按钮（仅保存/提交审批/审批操作等）
     * - 字段权限（只读/隐藏/必填）
     * - 子表权限
     * 
     * @param formId 表单ID（可通过 tableName 转换）
     * @param tableName 表名（与 formId 二选一）
     * @param dataId 数据ID（新建时为空）
     * @param taskId 任务ID（有待办任务时传入）
     */
    @AutoLog(value = "表单工作流-获取渲染配置")
    @Operation(summary = "表单工作流-获取渲染配置", description = "获取表单的分离/融合模式渲染配置")
    @GetMapping("/renderConfig")
    public Result<FormRenderConfig> getFormRenderConfig(
            @RequestParam(required = false) String formId,
            @RequestParam(required = false) String tableName,
            @RequestParam(required = false) String dataId,
            @RequestParam(required = false) String taskId) {
        try {
            // formId 和 tableName 二选一
            String finalFormId = formId;
            if (!org.jeecg.common.util.oConvertUtils.isNotEmpty(finalFormId) && 
                org.jeecg.common.util.oConvertUtils.isNotEmpty(tableName)) {
                OnlCgformHead head = getCgformHeadByTableName(tableName);
                finalFormId = head.getId();
            }
            
            if (!org.jeecg.common.util.oConvertUtils.isNotEmpty(finalFormId)) {
                return Result.error("请提供 formId 或 tableName");
            }
            
            FormRenderConfig config = onlineFormWorkflowService.getFormRenderConfig(finalFormId, dataId, taskId);
            return Result.OK(config);
            
        } catch (Exception e) {
            log.error("获取表单渲染配置失败: formId={}, tableName={}, dataId={}, taskId={}", 
                     formId, tableName, dataId, taskId, e);
            return Result.error("获取渲染配置失败: " + e.getMessage());
        }
    }

    /**
     * 🎯 分离模式：仅保存表单（不启动工作流）
     * 
     * 此接口允许用户保存表单数据但不触发工作流，适用于：
     * - 草稿态的多次保存
     * - 驳回后的编辑保存
     * - 不需要走审批的独立保存场景
     */
    @AutoLog(value = "表单工作流-仅保存")
    @Operation(summary = "表单工作流-仅保存", description = "分离模式 - 仅保存表单数据，不启动工作流")
    @PostMapping("/saveOnly")
    @RequiresPermissions("workflow:form:save")
    public Result<?> saveFormOnly(@RequestParam String tableName,
                                  @RequestParam(required = false) String dataId,
                                  @RequestBody JSONObject formData) {
        try {
            return onlineFormWorkflowService.saveFormOnly(tableName, dataId, formData);
        } catch (Exception e) {
            log.error("分离模式-仅保存失败: tableName={}, dataId={}", tableName, dataId, e);
            return Result.error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 🎯 分离模式：保存并提交审批
     * 
     * 此接口先保存表单数据，然后启动工作流，适用于：
     * - 草稿态提交审批
     * - 驳回后重新提交
     */
    @AutoLog(value = "表单工作流-保存并提交")
    @Operation(summary = "表单工作流-保存并提交", description = "分离模式 - 保存表单并提交审批")
    @PostMapping("/saveAndSubmit")
    @RequiresPermissions("workflow:form:submit")
    public Result<?> saveAndSubmitWorkflow(@RequestParam String formId,
                                           @RequestParam String tableName,
                                           @RequestParam(required = false) String dataId,
                                           @RequestBody JSONObject formData) {
        try {
            return onlineFormWorkflowService.saveAndSubmitWorkflow(formId, tableName, dataId, formData);
        } catch (Exception e) {
            log.error("分离模式-保存并提交失败: formId={}, tableName={}, dataId={}", formId, tableName, dataId, e);
            return Result.error("提交失败: " + e.getMessage());
        }
    }

    /**
     * 🎯 通过表名获取渲染配置（简化入口）
     * 
     * 前端常用场景：只知道表名和数据ID
     */
    @AutoLog(value = "表单工作流-通过表名获取渲染配置")
    @Operation(summary = "表单工作流-通过表名获取渲染配置", description = "根据表名获取表单渲染配置")
    @GetMapping("/renderConfigByTable")
    public Result<FormRenderConfig> getFormRenderConfigByTable(
            @RequestParam String tableName,
            @RequestParam(required = false) String dataId,
            @RequestParam(required = false) String taskId) {
        try {
            OnlCgformHead head = getCgformHeadByTableName(tableName);
            FormRenderConfig config = onlineFormWorkflowService.getFormRenderConfig(head.getId(), dataId, taskId);
            return Result.OK(config);
        } catch (Exception e) {
            log.error("通过表名获取渲染配置失败: tableName={}, dataId={}, taskId={}", tableName, dataId, taskId, e);
            return Result.error("获取渲染配置失败: " + e.getMessage());
        }
    }
} 