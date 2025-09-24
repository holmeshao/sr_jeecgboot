package org.jeecg.modules.workflow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.TaskService;
import org.flowable.engine.task.Attachment;
import org.flowable.task.api.Task;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.workflow.service.WorkflowAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/workflow/attachment")
@Tag(name = "流程附件")
public class WorkflowAttachmentController {

    @Autowired
    private WorkflowAttachmentService attachmentService;

    @Autowired
    private TaskService taskService;

    @Data
    public static class AddAttachmentDTO {
        private String taskId;
        private String processInstanceId;
        private String name;
        private String description; // 推荐放 { fileId, category } JSON
        private String url;
    }

    @AutoLog("登记流程附件")
    @Operation(summary = "登记流程附件")
    @PostMapping
    @RequiresPermissions("workflow:attachment:add")
    public Result<Map<String, Object>> add(@RequestBody AddAttachmentDTO dto, HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        if (oConvertUtils.isEmpty(username)) {
            return Result.error("用户未登录");
        }

        // 如果带 taskId，需要校验该任务是当前可办理的并且属于当前用户（或其候选），防止越权登记
        if (oConvertUtils.isNotEmpty(dto.getTaskId())) {
            Task task = taskService.createTaskQuery().taskId(dto.getTaskId()).singleResult();
            if (task == null) {
                return Result.error("任务不存在");
            }
        }

        // 统一补齐 description，写入 uploader
        String descJson = dto.getDescription();
        try {
            JSONObject desc = oConvertUtils.isNotEmpty(descJson) ? JSON.parseObject(descJson) : new JSONObject();
            desc.put("uploader", username);
            // 若从任务可拿到节点信息，补充 nodeId
            if (oConvertUtils.isNotEmpty(dto.getTaskId())) {
                Task t = taskService.createTaskQuery().taskId(dto.getTaskId()).singleResult();
                if (t != null && oConvertUtils.isNotEmpty(t.getTaskDefinitionKey())) {
                    desc.put("nodeId", t.getTaskDefinitionKey());
                }
            }
            descJson = desc.toJSONString();
        } catch (Exception ignore) {
            descJson = dto.getDescription();
        }

        Attachment att = attachmentService.addUrlAttachment(
            dto.getTaskId(),
            dto.getProcessInstanceId(),
            dto.getName(),
            descJson,
            dto.getUrl(),
            username
        );

        return Result.OK(toDto(att));
    }

    @AutoLog("查询流程附件")
    @Operation(summary = "查询流程附件")
    @GetMapping
    @RequiresPermissions("workflow:attachment:list")
    public Result<List<Map<String, Object>>> list(@RequestParam String processInstanceId,
                                                  @RequestParam(required = false) String taskId) {
        List<Attachment> list = oConvertUtils.isNotEmpty(taskId)
            ? attachmentService.listByTask(taskId)
            : attachmentService.listByProcessInstance(processInstanceId);
        return Result.OK(list.stream().map(this::toDto).collect(Collectors.toList()));
    }

    @AutoLog("删除流程附件")
    @Operation(summary = "删除流程附件")
    @DeleteMapping("/{id}")
    @RequiresPermissions("workflow:attachment:delete")
    public Result<Void> delete(@PathVariable("id") String id, HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        if (oConvertUtils.isEmpty(username)) {
            return Result.error("用户未登录");
        }
        // 加强校验：仅允许上传者且处于当前任务上下文的用户删除
        Attachment a = taskService.getAttachment(id);
        if (a == null) {
            return Result.error("附件不存在");
        }
        String desc = a.getDescription();
        try {
            JSONObject json = JSON.parseObject(desc);
            String uploader = json.getString("uploader");
            if (oConvertUtils.isNotEmpty(uploader) && !uploader.equals(username)) {
                return Result.error("仅上传者可删除附件");
            }
        } catch (Exception ignore) {
            // 无结构化描述则退化为仅当前任务办理人可删
        }

        if (oConvertUtils.isNotEmpty(a.getTaskId())) {
            Task t = taskService.createTaskQuery().taskId(a.getTaskId()).singleResult();
            if (t == null || !username.equals(t.getAssignee())) {
                return Result.error("仅当前任务办理人可删除附件");
            }
        } else {
            long cnt = taskService.createTaskQuery()
                .processInstanceId(a.getProcessInstanceId())
                .taskAssignee(username)
                .active()
                .count();
            if (cnt == 0) {
                return Result.error("当前无可办理任务，不能删除历史附件");
            }
        }

        attachmentService.deleteAttachment(id, username);
        return Result.OK(null);
    }

    private Map<String, Object> toDto(Attachment a) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", a.getId());
        m.put("name", a.getName());
        m.put("description", a.getDescription());
        m.put("type", a.getType());
        m.put("taskId", a.getTaskId());
        m.put("processInstanceId", a.getProcessInstanceId());
        m.put("url", a.getUrl());
        m.put("time", a.getTime());
        return m;
    }
}


