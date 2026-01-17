package org.jeecg.modules.workflow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.task.Attachment;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.workflow.engine.OnlineFormPermissionEngine;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowConfig;
import org.jeecg.modules.workflow.model.FormPermissionConfig;
import org.jeecg.modules.workflow.service.IOnlCgformWorkflowConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 渲染数据接口：返回字段权限 + 节点区块 schema（当前节点）+ 最新 taskId
 */
@Slf4j
@RestController
@RequestMapping("/workflow/render")
@Tag(name = "工作流渲染数据")
public class WorkflowRenderController {

    @Autowired
    private OnlineFormPermissionEngine permissionEngine;

    @Autowired
    private IOnlCgformWorkflowConfigService workflowConfigService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private org.jeecg.modules.online.cgform.service.IOnlCgformFieldService fieldService;

    @Autowired
    private org.jeecg.modules.online.cgform.service.IOnlCgformHeadService headService;

    @Data
    public static class RenderVO {
        private FormPermissionConfig permissions;
        private JSONObject nodeSchema; // 当前节点的 schema（ui_schema_json.nodes[nodeId] 数组）
        private String latestTaskId;
    }

    @Data
    public static class HistoryItemVO {
        private String id;               // 对应 taskId 或 伪ID
        private String nodeId;
        private String nodeName;
        private String operator;
        private Long timestamp;
        private JSONArray changedFields; // 简要变更
        private JSONObject formData;     // 当次字段值（可选）
    }

    /**
     * 流程进度信息（UniversalFormPage依赖简版）
     */
    @AutoLog("获取流程进度信息")
    @Operation(summary = "获取流程进度信息")
    @GetMapping("/process/info")
    @RequiresPermissions("workflow:render:process")
    public Result<JSONObject> getProcessInfo(@RequestParam String processInstanceId) {
        try {
            // 简版：当前活动任务作为当前步骤
            var activeTasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .list();

            JSONObject resp = new JSONObject();
            resp.put("currentStepIndex", 0);
            if (activeTasks != null && !activeTasks.isEmpty()) {
                Task t = activeTasks.get(0);
                resp.put("currentAssignee", t.getAssignee());
                // 简易steps，仅包含当前任务
                JSONArray steps = new JSONArray();
                JSONObject s = new JSONObject();
                s.put("id", t.getTaskDefinitionKey());
                s.put("name", t.getName());
                s.put("current", true);
                steps.add(s);
                resp.put("steps", steps);
            } else {
                // 已完成或无活动任务
                resp.put("steps", new JSONArray());
            }
            return Result.OK(resp);
        } catch (Exception e) {
            log.error("获取流程进度失败", e);
            return Result.error("获取流程进度失败:" + e.getMessage());
        }
    }

    @Data
    public static class HistoryVO {
        private JSONArray items; // HistoryItemVO 列表
    }

    @Data
    public static class AttachmentVO {
        private String id;
        private String name;
        private String url;
        private String category;
        private String taskId;
        private Long time;
    }

    @Data
    public static class CommentVO {
        private String id;
        private String user;
        private Long time;
        private String message;
    }

    @Data
    public static class HistoryDetailVO {
        private String taskId;
        private String nodeId;
        private String nodeName;
        private Long timestamp;
        private String operator;
        private JSONObject snapshot;      // 包含 changedFields / formData 等
        private JSONArray attachments;    // AttachmentVO 列表
        private JSONArray comments;       // CommentVO 列表
    }

    @Data
    public static class CompareVO {
        private JSONObject left;   // 左侧版本快照（含formData）
        private JSONObject right;  // 右侧版本快照（含formData）
        private JSONArray diff;    // 简要diff数组 [{ field, left, right }]
    }

    @AutoLog("获取渲染数据")
    @Operation(summary = "获取渲染数据")
    @GetMapping("/node")
    @RequiresPermissions("workflow:render:node")
    public Result<RenderVO> getNodeRender(
            @RequestParam String formId,
            @RequestParam String processDefinitionKey,
            @RequestParam String nodeId,
            @RequestParam(required = false) String processInstanceId) {
        try {
            // 1) 权限合成
            FormPermissionConfig permission = permissionEngine.getNodePermission(formId, processDefinitionKey, nodeId);

            // 2) 取 ui_schema_json 中当前节点的 schema
            JSONObject nodeSchema = null;
            OnlCgformWorkflowConfig cfg = workflowConfigService.getByFormAndProcessKey(formId, processDefinitionKey);
            if (cfg != null && oConvertUtils.isNotEmpty(cfg.getUiSchemaJson())) {
                try {
                    JSONObject schema = JSON.parseObject(cfg.getUiSchemaJson());
                    // 兼容两种路径：nodes 或 workflow.nodes
                    JSONObject nodes = schema.getJSONObject("nodes");
                    if (nodes == null) {
                        JSONObject wf = schema.getJSONObject("workflow");
                        if (wf != null) {
                            nodes = wf.getJSONObject("nodes");
                        }
                    }
                    if (nodes != null) {
                        JSONArray comps = nodes.getJSONArray(nodeId);
                        if (comps != null) {
                            nodeSchema = new JSONObject(new HashMap<>());
                            nodeSchema.put("components", comps);
                        }
                    }
                } catch (Exception e) {
                    log.debug("解析 ui_schema_json 失败: {}", e.getMessage());
                }
            }

            // 3) 最近 taskId（用于主视图“最新”）：优先取活动任务，否则回退已完成历史最近一次
            String latestTaskId = null;
            if (oConvertUtils.isNotEmpty(processInstanceId)) {
                Task t = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .taskDefinitionKey(nodeId)
                    .active()
                    .orderByTaskCreateTime()
                    .desc()
                    .singleResult();
                if (t != null) {
                    latestTaskId = t.getId();
                } else {
                    // 回退：从历史取该节点最近一次完成的任务
                    org.flowable.task.api.history.HistoricTaskInstance ht = historyService
                        .createHistoricTaskInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .taskDefinitionKey(nodeId)
                        .finished()
                        .orderByHistoricTaskInstanceEndTime().desc()
                        .listPage(0, 1)
                        .stream().findFirst().orElse(null);
                    latestTaskId = ht != null ? ht.getId() : null;
                }
            }

            RenderVO vo = new RenderVO();
            vo.setPermissions(permission);
            vo.setNodeSchema(nodeSchema);
            vo.setLatestTaskId(latestTaskId);
            return Result.OK(vo);
        } catch (Exception e) {
            log.error("获取渲染数据失败", e);
            return Result.error("获取渲染数据失败:" + e.getMessage());
        }
    }

    /**
     * 字段权限调试视图（仅开发环境使用，返回每个字段的最终权限与标题）
     */
    @AutoLog("字段权限调试视图")
    @Operation(summary = "字段权限调试视图")
    @GetMapping("/node/permissionDebug")
    @RequiresPermissions("workflow:permission:view")
    public Result<JSONArray> getPermissionDebug(@RequestParam String formId,
                                                @RequestParam String processDefinitionKey,
                                                @RequestParam String nodeId) {
        try {
            FormPermissionConfig cfg = permissionEngine.getNodePermission(formId, processDefinitionKey, nodeId);
            java.util.List<org.jeecg.modules.online.cgform.entity.OnlCgformField> fields = fieldService.list(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<org.jeecg.modules.online.cgform.entity.OnlCgformField>()
                    .eq(org.jeecg.modules.online.cgform.entity.OnlCgformField::getCgformHeadId, formId)
            );
            JSONArray arr = new JSONArray();
            if (fields != null) {
                java.util.Set<String> editable = new java.util.HashSet<>(cfg.getEditableFields() == null ? java.util.Collections.emptyList() : cfg.getEditableFields());
                java.util.Set<String> readonly = new java.util.HashSet<>(cfg.getReadonlyFields() == null ? java.util.Collections.emptyList() : cfg.getReadonlyFields());
                java.util.Set<String> hidden = new java.util.HashSet<>(cfg.getHiddenFields() == null ? java.util.Collections.emptyList() : cfg.getHiddenFields());
                java.util.Set<String> required = new java.util.HashSet<>(cfg.getRequiredFields() == null ? java.util.Collections.emptyList() : cfg.getRequiredFields());
                for (var f : fields) {
                    JSONObject row = new JSONObject(new HashMap<>());
                    row.put("field", f.getDbFieldName());
                    row.put("title", f.getDbFieldTxt());
                    row.put("editable", editable.contains(f.getDbFieldName()));
                    row.put("readonly", readonly.contains(f.getDbFieldName()));
                    row.put("hidden", hidden.contains(f.getDbFieldName()));
                    row.put("required", required.contains(f.getDbFieldName()));
                    arr.add(row);
                }
            }
            return Result.OK(arr);
        } catch (Exception e) {
            log.error("权限调试视图失败", e);
            return Result.error("权限调试失败:" + e.getMessage());
        }
    }

    @AutoLog("获取流程历史（快照+评论+附件简表）")
    @Operation(summary = "获取流程历史（快照+评论+附件简表）")
    @GetMapping("/history")
    @RequiresPermissions("workflow:render:history")
    public Result<HistoryVO> getHistory(
            @RequestParam String processInstanceId,
            @RequestParam(required = false) String nodeId,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String formId) {
        try {
            // 1) 历史任务列表（按时间倒序）
            var q = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .orderByHistoricTaskInstanceEndTime().desc();
            if (oConvertUtils.isNotEmpty(nodeId)) {
                q.taskDefinitionKey(nodeId);
            }
            var tasks = q.listPage((pageNo - 1) * pageSize, pageSize);

            // 2) 组装VO；快照读取策略：form_snapshot_{nodeId}_{taskId}
            JSONArray arr = new JSONArray();
            for (var ht : tasks) {
                String tNodeId = ht.getTaskDefinitionKey();
                String tId = ht.getId();
                String snapshotVarKey = "form_snapshot_" + tNodeId + "_" + tId;
                Object snapObj = null;
                try {
                    snapObj = historyService.createHistoricVariableInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .variableName(snapshotVarKey)
                        .singleResult();
                } catch (Exception ignore) {}

                JSONObject snapJson = null;
                if (snapObj instanceof org.flowable.variable.api.history.HistoricVariableInstance) {
                    Object val = ((org.flowable.variable.api.history.HistoricVariableInstance) snapObj).getValue();
                    if (val instanceof String) {
                        try { snapJson = JSON.parseObject((String) val); } catch (Exception ignore) {}
                    }
                }

                JSONObject item = new JSONObject(new HashMap<>());
                item.put("id", tId);
                item.put("nodeId", tNodeId);
                item.put("nodeName", ht.getName());
                item.put("operator", ht.getAssignee());
                item.put("timestamp", ht.getEndTime() != null ? ht.getEndTime().getTime() : (ht.getStartTime() != null ? ht.getStartTime().getTime() : null));
                if (ht.getStartTime() != null && ht.getEndTime() != null) {
                    long dur = ht.getEndTime().getTime() - ht.getStartTime().getTime();
                    item.put("durationMs", dur);
                }
                if (snapJson != null) {
                    // 仅摘取变更摘要；formData 体积大可选返回
                    Object changed = snapJson.get("changedFields");
                    if (changed instanceof com.alibaba.fastjson.JSONArray) {
                        com.alibaba.fastjson.JSONArray cf = (com.alibaba.fastjson.JSONArray) changed;
                        // 字段名友好化：映射为字段标题
                        java.util.Map<String, String> nameMap = buildFieldTitleMap(formId);
                        com.alibaba.fastjson.JSONArray pretty = new com.alibaba.fastjson.JSONArray();
                        for (int i = 0; i < cf.size(); i++) {
                            String code = cf.getString(i);
                            String title = nameMap.getOrDefault(code, code);
                            pretty.add(title);
                        }
                        item.put("changedFields", pretty);
                    } else {
                        item.put("changedFields", changed);
                    }
                    // 可按需开启：item.put("formData", snapJson.get("formData"));
                }
                arr.add(item);
            }

            HistoryVO vo = new HistoryVO();
            vo.setItems(arr);
            return Result.OK(vo);
        } catch (Exception e) {
            log.error("获取流程历史失败", e);
            return Result.error("获取流程历史失败:" + e.getMessage());
        }
    }

    @AutoLog("获取某次提交的历史详情")
    @Operation(summary = "获取某次提交的历史详情")
    @GetMapping("/history/detail")
    @RequiresPermissions("workflow:render:history")
    public Result<HistoryDetailVO> getHistoryDetail(
            @RequestParam String processInstanceId,
            @RequestParam String nodeId,
            @RequestParam String taskId) {
        try {
            // 1) 快照
            String snapshotVarKey = "form_snapshot_" + nodeId + "_" + taskId;
            JSONObject snapJson = null;
            try {
                var hvar = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .variableName(snapshotVarKey)
                    .singleResult();
                if (hvar != null && hvar.getValue() instanceof String) {
                    snapJson = JSON.parseObject((String) hvar.getValue());
                }
            } catch (Exception ignore) {}

            // 2) 附件（按任务）
            JSONArray attArr = new JSONArray();
            try {
                List<Attachment> list = taskService.getTaskAttachments(taskId);
                for (Attachment a : list) {
                    AttachmentVO vo = new AttachmentVO();
                    vo.setId(a.getId());
                    vo.setName(a.getName());
                    vo.setUrl(a.getUrl());
                    vo.setTaskId(a.getTaskId());
                    vo.setTime(a.getTime() != null ? a.getTime().getTime() : null);
                    // 解析分组category
                    try {
                        JSONObject desc = JSON.parseObject(a.getDescription());
                        vo.setCategory(desc.getString("category"));
                    } catch (Exception ignore) {}
                    attArr.add(JSON.parseObject(JSON.toJSONString(vo)));
                }
            } catch (Exception ignore) {}

            // 3) 评论（按任务）
            JSONArray cmtArr = new JSONArray();
            try {
                List<Comment> cmts = taskService.getTaskComments(taskId);
                for (Comment c : cmts) {
                    CommentVO vo = new CommentVO();
                    vo.setId(c.getId());
                    vo.setUser(c.getUserId());
                    vo.setTime(c.getTime() != null ? c.getTime().getTime() : null);
                    vo.setMessage(c.getFullMessage());
                    cmtArr.add(JSON.parseObject(JSON.toJSONString(vo)));
                }
            } catch (Exception ignore) {}

            // 4) 基本信息
            var ht = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .taskId(taskId)
                .singleResult();

            HistoryDetailVO vo = new HistoryDetailVO();
            vo.setTaskId(taskId);
            vo.setNodeId(nodeId);
            vo.setNodeName(ht != null ? ht.getName() : null);
            vo.setTimestamp(ht != null && ht.getEndTime() != null ? ht.getEndTime().getTime() : null);
            vo.setOperator(ht != null ? ht.getAssignee() : null);
            vo.setSnapshot(snapJson);
            vo.setAttachments(attArr);
            vo.setComments(cmtArr);
            return Result.OK(vo);
        } catch (Exception e) {
            log.error("获取历史详情失败", e);
            return Result.error("获取历史详情失败:" + e.getMessage());
        }
    }

    @AutoLog("版本对比：任意两次提交")
    @Operation(summary = "版本对比：任意两次提交")
    @GetMapping("/history/compare")
    @RequiresPermissions("workflow:render:history")
    public Result<CompareVO> compareHistory(
            @RequestParam String processInstanceId,
            @RequestParam String nodeId,
            @RequestParam String leftTaskId,
            @RequestParam String rightTaskId,
            @RequestParam(required = false) String formId) {
        try {
            JSONObject left = loadSnapshot(processInstanceId, nodeId, leftTaskId);
            JSONObject right = loadSnapshot(processInstanceId, nodeId, rightTaskId);
            JSONArray diff = buildDiff(left, right, formId);
            CompareVO vo = new CompareVO();
            vo.setLeft(left);
            vo.setRight(right);
            vo.setDiff(diff);
            return Result.OK(vo);
        } catch (Exception e) {
            log.error("版本对比失败", e);
            return Result.error("版本对比失败:" + e.getMessage());
        }
    }

    @AutoLog("版本对比：与当前最新对比")
    @Operation(summary = "版本对比：与当前最新对比")
    @GetMapping("/history/compareLatest")
    @RequiresPermissions("workflow:render:history")
    public Result<CompareVO> compareWithLatest(
            @RequestParam String processInstanceId,
            @RequestParam String nodeId,
            @RequestParam String taskId,
            @RequestParam(required = false) String formId) {
        try {
            // 查找最新指针
            String latestPointerKey = "form_snapshot_latest_" + nodeId;
            var latestVar = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName(latestPointerKey)
                .singleResult();
            String latestTaskId = latestVar != null && latestVar.getValue() != null ? String.valueOf(latestVar.getValue()) : null;
            if (latestTaskId == null) {
                return Result.error("未找到最新版本指针");
            }
            JSONObject left = loadSnapshot(processInstanceId, nodeId, taskId);
            JSONObject right = loadSnapshot(processInstanceId, nodeId, latestTaskId);
            JSONArray diff = buildDiff(left, right, formId);
            CompareVO vo = new CompareVO();
            vo.setLeft(left);
            vo.setRight(right);
            vo.setDiff(diff);
            return Result.OK(vo);
        } catch (Exception e) {
            log.error("与最新版本对比失败", e);
            return Result.error("与最新版本对比失败:" + e.getMessage());
        }
    }

    // ========== 辅助方法 ==========
    private JSONObject loadSnapshot(String processInstanceId, String nodeId, String taskId) {
        String key = "form_snapshot_" + nodeId + "_" + taskId;
        var varIns = historyService.createHistoricVariableInstanceQuery()
            .processInstanceId(processInstanceId)
            .variableName(key)
            .singleResult();
        if (varIns != null && varIns.getValue() instanceof String) {
            try { return JSON.parseObject((String) varIns.getValue()); } catch (Exception ignore) {}
        }
        return new JSONObject();
    }

    private JSONArray buildDiff(JSONObject left, JSONObject right, String formId) {
        JSONArray arr = new JSONArray();
        if (left == null) left = new JSONObject();
        if (right == null) right = new JSONObject();
        JSONObject lf = left.getJSONObject("formData");
        JSONObject rf = right.getJSONObject("formData");
        if (lf == null && rf == null) return arr;
        if (lf == null) lf = new JSONObject();
        if (rf == null) rf = new JSONObject();
        java.util.Set<String> fields = new java.util.HashSet<>();
        fields.addAll(lf.keySet());
        fields.addAll(rf.keySet());
        java.util.Map<String, String> titleMap = buildFieldTitleMap(formId);
        for (String f : fields) {
            Object lv = lf.get(f);
            Object rv = rf.get(f);
            String ls = lv == null ? null : String.valueOf(lv);
            String rs = rv == null ? null : String.valueOf(rv);
            if (!java.util.Objects.equals(ls, rs)) {
                JSONObject row = new JSONObject();
                // 字段标题映射
                String title = titleMap.getOrDefault(f, f);
                row.put("field", title);
                row.put("left", lv);
                row.put("right", rv);
                arr.add(row);
            }
        }
        return arr;
    }

    // 构造字段编码到标题的映射（按当前表主流程）
    private java.util.Map<String, String> buildFieldTitleMap(String formId) {
        java.util.Map<String, String> map = new java.util.HashMap<>();
        try {
            if (org.jeecg.common.util.oConvertUtils.isNotEmpty(formId)) {
                java.util.List<org.jeecg.modules.online.cgform.entity.OnlCgformField> fields = fieldService.list(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<org.jeecg.modules.online.cgform.entity.OnlCgformField>()
                        .eq(org.jeecg.modules.online.cgform.entity.OnlCgformField::getCgformHeadId, formId)
                );
                if (fields != null) {
                    for (var f : fields) {
                        map.put(f.getDbFieldName(), f.getDbFieldTxt());
                    }
                }
            }
        } catch (Exception ignore) {}
        return map;
    }
}


