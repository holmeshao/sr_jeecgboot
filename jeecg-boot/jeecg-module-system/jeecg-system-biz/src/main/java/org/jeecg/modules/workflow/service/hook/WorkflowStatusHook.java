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
     * @param statusField 正在写入的状态字段名（如 bmp_status）
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
}


