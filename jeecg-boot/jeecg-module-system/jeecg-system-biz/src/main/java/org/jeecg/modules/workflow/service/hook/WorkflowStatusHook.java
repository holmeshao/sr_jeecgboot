package org.jeecg.modules.workflow.service.hook;

/**
 * 业务状态回写钩子（可选注入）
 *
 * 在工作流更新业务状态前后回调，便于项目按节点/变量映射业务自定义的 status 字段
 */
public interface WorkflowStatusHook {

    /**
     * 回写前回调
     * @param formId 表单ID
     * @param dataId 数据ID
     * @param statusField 正在写入的状态字段名（如 bpmn_status）
     * @param oldValue 旧值
     * @param newSemanticStatus 新的语义状态（DRAFT/IN_PROCESS/COMPLETED/REJECTED…）
     */
    default void beforeUpdateStatus(String formId, String dataId, String statusField, Object oldValue, Object newSemanticStatus) {}

    /**
     * 回写后回调
     * @param formId 表单ID
     * @param dataId 数据ID
     * @param statusField 写入字段名
     * @param oldValue 旧值
     * @param newStoredValue 实际存入库的值（如 0/1/2/3）
     */
    default void afterUpdateStatus(String formId, String dataId, String statusField, Object oldValue, Object newStoredValue) {}

    /**
     * 业务状态钩子：当流程关键动作发生时（提交、同意、驳回、完成等）回调，便于同步业务表自定义 status 字段。
     * 实现方可根据动作/节点自由映射业务状态。
     *
     * @param formId   表单ID
     * @param dataId   数据ID
     * @param action   动作：SUBMIT/APPROVE/REJECT/COMPLETE/START_NODE_AUTO_COMPLETE 等
     * @param nodeName 当前节点名称（可能为空）
     */
    default void onWorkflowAction(String formId, String dataId, String action, String nodeName) {}
}


