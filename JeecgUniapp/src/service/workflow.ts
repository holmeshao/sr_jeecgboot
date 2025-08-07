import { http } from '@/utils/http';

/**
 * 🎯 移动端工作流API
 * 支持Flowable 7.0兼容性
 */

// ================================== 基础工作流API ==================================

/**
 * 启动表单工作流
 */
export const startWorkflowMobile = (tableName: string, dataId?: string) => {
  return http.post('/workflow/mobile/form/start-workflow', {
    tableName,
    dataId
  });
};

/**
 * 提交节点表单
 */
export const submitFormMobile = (taskId: string, nodeCode: string, formData: any) => {
  return http.post('/workflow/mobile/form/submit', {
    taskId,
    nodeCode,
    formData
  });
};

/**
 * 保存表单草稿
 */
export const saveDraftMobile = (tableName: string, dataId: string | undefined, formData: any) => {
  return http.post('/workflow/mobile/form/save-draft', {
    tableName,
    dataId,
    formData
  });
};

/**
 * 执行工作流动作
 */
export const executeWorkflowActionMobile = (
  actionCode: string,
  taskId: string,
  comment: string,
  formData: any
) => {
  return http.post('/workflow/mobile/action/execute', {
    actionCode,
    taskId,
    comment,
    formData
  });
};

/**
 * 获取节点权限配置
 */
export const getNodePermissionMobile = (taskId: string, formId: string) => {
  return http.get('/workflow/mobile/form/node-permission', {
    taskId,
    formId
  });
};

// ================================== Flowable 7.0兼容性API ==================================

/**
 * 🎯 手动触发流程定义部署事件处理
 * @param processDefinitionKey 流程定义Key
 */
export const triggerDeploymentEventMobile = (processDefinitionKey: string) => {
  return http.post('/workflow/triggerDeploymentEvent', {
    processDefinitionKey
  });
};

/**
 * 🎯 批量触发所有流程定义的部署事件
 */
export const triggerAllDeploymentEventsMobile = () => {
  return http.post('/workflow/triggerAllDeploymentEvents');
};

/**
 * 🎯 检查Flowable 7.0兼容性状态
 */
export const checkFlowable7StatusMobile = () => {
  return http.get('/workflow/flowable7Status');
};

/**
 * 🎯 手动触发流程实例启动事件
 * @param processInstanceId 流程实例ID
 * @param processDefinitionKey 流程定义Key
 */
export const triggerInstanceStartEventMobile = (
  processInstanceId: string, 
  processDefinitionKey: string
) => {
  return http.post('/workflow/triggerInstanceStartEvent', {
    processInstanceId,
    processDefinitionKey
  });
};

// ================================== 移动端特有API ==================================

/**
 * 获取用户待办任务列表
 */
export const getUserTodoTasksMobile = (params: {
  pageNo?: number;
  pageSize?: number;
  assignee?: string;
}) => {
  return http.get('/workflow/mobile/task/todo', params);
};

/**
 * 获取用户已办任务列表
 */
export const getUserDoneTasksMobile = (params: {
  pageNo?: number;
  pageSize?: number;
  assignee?: string;
}) => {
  return http.get('/workflow/mobile/task/done', params);
};

/**
 * 获取流程实例详情
 */
export const getProcessInstanceDetailMobile = (processInstanceId: string) => {
  return http.get(`/workflow/mobile/instance/${processInstanceId}`);
};

/**
 * 获取任务详情
 */
export const getTaskDetailMobile = (taskId: string) => {
  return http.get(`/workflow/mobile/task/${taskId}`);
};

/**
 * 获取流程定义列表
 */
export const getProcessDefinitionsMobile = (params?: {
  category?: string;
  key?: string;
  name?: string;
}) => {
  return http.get('/workflow/mobile/definition/list', params);
};