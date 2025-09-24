package org.jeecg.modules.workflow.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.task.Attachment;
import org.flowable.task.api.Task;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.workflow.service.WorkflowAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class WorkflowAttachmentServiceImpl implements WorkflowAttachmentService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public Attachment addUrlAttachment(String taskId,
                                       String processInstanceId,
                                       String name,
                                       String descriptionJson,
                                       String url,
                                       String currentUser) {

        if (!StringUtils.hasText(taskId) && !StringUtils.hasText(processInstanceId)) {
            throw new JeecgBootException("taskId 和 processInstanceId 不能同时为空");
        }

        String procInstId = processInstanceId;
        if (!StringUtils.hasText(procInstId)) {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                throw new JeecgBootException("任务不存在: " + taskId);
            }
            procInstId = task.getProcessInstanceId();
        }

        // Flowable 附件登记（外链URL）
        Attachment attachment = taskService.createAttachment(
            "url",
            taskId,
            procInstId,
            name,
            descriptionJson,
            url
        );

        log.info("登记流程附件成功: procInstId={}, taskId={}, name={}, url={}", procInstId, taskId, name, url);
        return attachment;
    }

    @Override
    public List<Attachment> listByProcessInstance(String processInstanceId) {
        return taskService.getProcessInstanceAttachments(processInstanceId);
    }

    @Override
    public List<Attachment> listByTask(String taskId) {
        return taskService.getTaskAttachments(taskId);
    }

    @Override
    public void deleteAttachment(String attachmentId, String currentUser) {
        // Flowable 无内置上传者字段，这里只做最小校验，实际控制放在 Controller 层结合当前任务与操作者判断
        try {
            taskService.deleteAttachment(attachmentId);
            log.info("删除流程附件成功: attachmentId={}, operator={}", attachmentId, currentUser);
        } catch (Exception e) {
            throw new JeecgBootException("删除附件失败:" + e.getMessage());
        }
    }
}


