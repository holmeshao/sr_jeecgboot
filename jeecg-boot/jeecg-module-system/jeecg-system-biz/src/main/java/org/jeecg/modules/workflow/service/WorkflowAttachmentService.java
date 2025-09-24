package org.jeecg.modules.workflow.service;

import org.flowable.engine.task.Attachment;

import java.util.List;

/**
 * 工作流附件服务
 */
public interface WorkflowAttachmentService {

    Attachment addUrlAttachment(String taskId,
                                String processInstanceId,
                                String name,
                                String descriptionJson,
                                String url,
                                String currentUser);

    List<Attachment> listByProcessInstance(String processInstanceId);

    List<Attachment> listByTask(String taskId);

    void deleteAttachment(String attachmentId,
                          String currentUser);
}


