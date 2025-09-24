package org.jeecg.modules.workflow.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.task.api.Task;
import org.flowable.engine.TaskService;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.api.IdentityLinkType;
import org.jeecg.modules.workflow.mapper.WorkflowUserLookupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 全局事件监听器：在 TASK_CREATED 时将 candidateGroups 中的
 *   role:<roleCode> / dept:<deptId>
 * 展开为真实用户候选。
 */
@Slf4j
@Component
public class CandidateResolveEventListener implements FlowableEventListener {

    @Autowired
    private TaskService taskService;

    @Autowired(required = false)
    private WorkflowUserLookupMapper lookupMapper;

    @Override
    public void onEvent(FlowableEvent event) {
        if (event.getType() != FlowableEngineEventType.TASK_CREATED) return;
        if (!(event instanceof FlowableEntityEvent)) return;
        Object entity = ((FlowableEntityEvent) event).getEntity();
        if (!(entity instanceof Task)) return;
        Task task = (Task) entity;

        try {
            String taskId = task.getId();
            List<IdentityLink> links = taskService.getIdentityLinksForTask(taskId);

            Set<String> toRemoveGroups = new HashSet<>();
            Set<String> candidateUsers = new HashSet<>();

            for (IdentityLink link : links) {
                if (!IdentityLinkType.CANDIDATE.equals(link.getType())) continue;
                String gid = link.getGroupId();
                if (StringUtils.isBlank(gid)) continue;
                if (gid.startsWith("role:")) {
                    String code = StringUtils.substringAfter(gid, "role:");
                    if (StringUtils.isNotBlank(code)) {
                        resolveByRoleCode(code, candidateUsers);
                        toRemoveGroups.add(gid);
                    }
                } else if (gid.startsWith("dept:")) {
                    String deptId = StringUtils.substringAfter(gid, "dept:");
                    if (StringUtils.isNotBlank(deptId)) {
                        resolveByDeptId(deptId, candidateUsers);
                        toRemoveGroups.add(gid);
                    }
                }
            }

            for (String gid : toRemoveGroups) {
                try { taskService.deleteCandidateGroup(taskId, gid); } catch (Exception ignore) {}
            }

            for (String uid : candidateUsers) {
                try { taskService.addCandidateUser(taskId, uid); } catch (Exception ignore) {}
            }

            if (!candidateUsers.isEmpty() || !toRemoveGroups.isEmpty()) {
                log.info("[WF] global candidate-resolution: taskId={}, addUsers={}, removeGroups={}",
                        taskId, candidateUsers.size(), toRemoveGroups.size());
            }

        } catch (Exception e) {
            log.warn("[WF] CandidateResolveEventListener error: {}", e.getMessage());
        }
    }

    private void resolveByRoleCode(String roleCode, Set<String> userIds) {
        if (lookupMapper == null) return;
        try {
            List<String> list = lookupMapper.selectUserIdsByRoleCode(roleCode);
            if (list != null) userIds.addAll(list);
        } catch (Exception e) {
            log.warn("resolveByRoleCode failed: {}", roleCode, e);
        }
    }

    private void resolveByDeptId(String deptId, Set<String> userIds) {
        if (lookupMapper == null) return;
        try {
            List<String> list = lookupMapper.selectUserIdsByDeptId(deptId);
            if (list != null) userIds.addAll(list);
        } catch (Exception e) {
            log.warn("resolveByDeptId failed: {}", deptId, e);
        }
    }

    @Override
    public boolean isFailOnException() { return false; }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() { return false; }

    @Override
    public String getOnTransaction() { return null; }
}


