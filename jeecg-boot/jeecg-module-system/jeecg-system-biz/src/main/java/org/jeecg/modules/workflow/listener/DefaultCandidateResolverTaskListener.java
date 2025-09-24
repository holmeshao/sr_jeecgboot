package org.jeecg.modules.workflow.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.jeecg.modules.workflow.mapper.WorkflowUserLookupMapper;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 默认候选人解析监听器
 *
 * 读取 camunda:candidateGroups 中的 token：
 *  - role:<roleCode>
 *  - dept:<deptId>
 * 展开为用户ID集合并添加为候选人。
 * 兼容 camunda:candidateUsers，自动合并去重。
 */
@Slf4j
@Component("defaultCandidateResolverTaskListener")
public class DefaultCandidateResolverTaskListener implements TaskListener {

    @Autowired(required = false)
    private WorkflowUserLookupMapper lookupMapper;

    @Autowired(required = false)
    private RuntimeService runtimeService;

    @Override
    public void notify(DelegateTask delegateTask) {
        try {
            String candidateGroups = getString(delegateTask, "candidateGroups");
            String candidateUsers = getString(delegateTask, "candidateUsers");

            Set<String> userIds = new LinkedHashSet<>();
            if (StringUtils.isNotBlank(candidateUsers)) {
                for (String u : candidateUsers.split(",")) {
                    if (StringUtils.isNotBlank(u)) userIds.add(u.trim());
                }
            }

            if (StringUtils.isNotBlank(candidateGroups)) {
                String[] tokens = candidateGroups.split(",");
                for (String tk : tokens) {
                    String token = StringUtils.trimToEmpty(tk);
                    if (token.startsWith("role:")) {
                        String roleCode = token.substring("role:".length());
                        resolveByRoleCode(roleCode, userIds);
                    } else if (token.startsWith("dept:")) {
                        String deptId = token.substring("dept:".length());
                        resolveByDeptId(deptId, userIds);
                    }
                }
            }

            // 批量添加候选人
            if (!userIds.isEmpty()) {
                for (String uid : userIds) {
                    try { delegateTask.addCandidateUser(uid); } catch (Exception ignore) {}
                }
                log.info("[WF] resolved candidates → taskId={}, count={}", delegateTask.getId(), userIds.size());
            }
        } catch (Exception e) {
            log.warn("[WF] defaultCandidateResolverTaskListener error: {}", e.getMessage());
        }
    }

    private void resolveByRoleCode(String roleCode, Set<String> userIds) {
        if (lookupMapper == null || StringUtils.isBlank(roleCode)) return;
        try {
            List<String> list = lookupMapper.selectUserIdsByRoleCode(roleCode);
            if (list != null) userIds.addAll(list);
        } catch (Exception e) {
            log.warn("resolveByRoleCode failed: {}", roleCode, e);
        }
    }

    private void resolveByDeptId(String deptId, Set<String> userIds) {
        if (lookupMapper == null || StringUtils.isBlank(deptId)) return;
        try {
            List<String> list = lookupMapper.selectUserIdsByDeptId(deptId);
            if (list != null) userIds.addAll(list);
        } catch (Exception e) {
            log.warn("resolveByDeptId failed: {}", deptId, e);
        }
    }

    private String getString(DelegateTask task, String key) {
        Object v = null;
        try { v = task.getVariable(key); } catch (Exception ignored) {}
        if (v == null) {
            try { v = task.getVariableLocal(key); } catch (Exception ignored) {}
        }
        if (v == null && runtimeService != null) {
            try {
                String execId = task.getExecutionId();
                if (execId != null) {
                    v = runtimeService.getVariable(execId, key);
                    if (v == null) v = runtimeService.getVariableLocal(execId, key);
                }
            } catch (Exception ignored) {}
        }
        return v == null ? null : String.valueOf(v);
    }
}


