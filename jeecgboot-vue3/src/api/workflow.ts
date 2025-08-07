import { defHttp } from '/@/utils/http/axios';
import { useMessage } from '/@/hooks/web/useMessage';

const { createMessage } = useMessage();

enum Api {
  // 工作流表单API
  START_WORKFLOW = '/workflow/form/start',
  SUBMIT_FORM = '/workflow/form/submit',
  GET_DISPLAY_MODE = '/workflow/form/display-mode',
  GET_NODE_PERMISSION = '/workflow/form/node-permission',
  VALIDATE_PERMISSION = '/workflow/form/validate-permission',
  GET_BASIC_INFO = '/workflow/form/basic-info',
  GET_WORKFLOW_CONFIG = '/workflow/form/config',
  SAVE_DRAFT = '/workflow/form/save-draft',
  GET_VERSION_HISTORY = '/workflow/form/version-history',
  
  // 工作流配置API
  WORKFLOW_CONFIG_LIST = '/workflow/onlCgformWorkflowConfig/list',
  WORKFLOW_CONFIG_ADD = '/workflow/onlCgformWorkflowConfig/add',
  WORKFLOW_CONFIG_EDIT = '/workflow/onlCgformWorkflowConfig/edit',
  WORKFLOW_CONFIG_DELETE = '/workflow/onlCgformWorkflowConfig/delete',
  
  // 节点权限配置API
  NODE_CONFIG_LIST = '/workflow/onlCgformWorkflowNode/list',
  NODE_CONFIG_ADD = '/workflow/onlCgformWorkflowNode/add',
  NODE_CONFIG_EDIT = '/workflow/onlCgformWorkflowNode/edit',
  NODE_CONFIG_DELETE = '/workflow/onlCgformWorkflowNode/delete',
  
  // Flowable 7.0兼容性API
  TRIGGER_DEPLOYMENT_EVENT = '/workflow/triggerDeploymentEvent',
  TRIGGER_ALL_DEPLOYMENT_EVENTS = '/workflow/triggerAllDeploymentEvents',
  CHECK_FLOWABLE7_STATUS = '/workflow/flowable7Status',
  TRIGGER_INSTANCE_START_EVENT = '/workflow/triggerInstanceStartEvent',
}

/**
 * 启动表单工作流
 */
export const startWorkflow = (params: {
  formId: string;
  dataId: string;
  formData: Record<string, any>;
}) => {
  return defHttp.post<string>({
    url: Api.START_WORKFLOW,
    params: {
      formId: params.formId,
      dataId: params.dataId,
    },
    data: params.formData,
  });
};

/**
 * 提交节点表单
 */
export const submitForm = (params: {
  taskId: string;
  nodeCode: string;
  formData: Record<string, any>;
}) => {
  return defHttp.post<string>({
    url: Api.SUBMIT_FORM,
    params: {
      taskId: params.taskId,
      nodeCode: params.nodeCode,
    },
    data: params.formData,
  });
};

/**
 * 获取表单显示模式
 */
export const getDisplayMode = (params: {
  formId: string;
  dataId?: string;
}) => {
  return defHttp.get<any>({
    url: Api.GET_DISPLAY_MODE,
    params,
  });
};

/**
 * 获取节点权限配置
 */
export const getNodePermission = (params: {
  formId: string;
  processDefinitionKey?: string;
  nodeId: string;
}) => {
  return defHttp.get<any>({
    url: Api.GET_NODE_PERMISSION,
    params,
  });
};

/**
 * 验证节点权限
 */
export const validatePermission = (params: {
  formId: string;
  processDefinitionKey?: string;
  nodeId: string;
  formData: Record<string, any>;
}) => {
  return defHttp.post<string>({
    url: Api.VALIDATE_PERMISSION,
    params: {
      formId: params.formId,
      processDefinitionKey: params.processDefinitionKey,
      nodeId: params.nodeId,
    },
    data: params.formData,
  });
};

/**
 * 获取表单基础信息
 */
export const getFormBasicInfo = (params: {
  formId: string;
  dataId?: string;
}) => {
  return defHttp.get<any>({
    url: Api.GET_BASIC_INFO,
    params,
  });
};

/**
 * 获取工作流配置
 */
export const getWorkflowConfig = (formId: string) => {
  return defHttp.get<any>({
    url: `${Api.GET_WORKFLOW_CONFIG}/${formId}`,
  });
};

/**
 * 保存表单草稿
 */
export const saveDraft = (params: {
  formId: string;
  dataId?: string;
  formData: Record<string, any>;
}) => {
  return defHttp.post<string>({
    url: Api.SAVE_DRAFT,
    params: {
      formId: params.formId,
      dataId: params.dataId,
    },
    data: params.formData,
  });
};

/**
 * 获取版本历史
 */
export const getVersionHistory = (processInstanceId: string) => {
  return defHttp.get<any>({
    url: Api.GET_VERSION_HISTORY,
    params: { processInstanceId },
  });
};

// ============ 工作流配置管理 API ============

/**
 * 获取工作流配置列表
 */
export const getWorkflowConfigList = (params: any) => {
  return defHttp.get<any>({
    url: Api.WORKFLOW_CONFIG_LIST,
    params,
  });
};

/**
 * 新增工作流配置
 */
export const addWorkflowConfig = (params: any) => {
  return defHttp.post<any>({
    url: Api.WORKFLOW_CONFIG_ADD,
    data: params,
  });
};

/**
 * 编辑工作流配置
 */
export const editWorkflowConfig = (params: any) => {
  return defHttp.put<any>({
    url: Api.WORKFLOW_CONFIG_EDIT,
    data: params,
  });
};

/**
 * 删除工作流配置
 */
export const deleteWorkflowConfig = (id: string) => {
  return defHttp.delete<any>({
    url: `${Api.WORKFLOW_CONFIG_DELETE}/${id}`,
  });
};

// ============ 节点权限配置管理 API ============

/**
 * 获取节点权限配置列表
 */
export const getNodeConfigList = (params: any) => {
  return defHttp.get<any>({
    url: Api.NODE_CONFIG_LIST,
    params,
  });
};

/**
 * 新增节点权限配置
 */
export const addNodeConfig = (params: any) => {
  return defHttp.post<any>({
    url: Api.NODE_CONFIG_ADD,
    data: params,
  });
};

/**
 * 编辑节点权限配置
 */
export const editNodeConfig = (params: any) => {
  return defHttp.put<any>({
    url: Api.NODE_CONFIG_EDIT,
    data: params,
  });
};

/**
 * 删除节点权限配置
 */
export const deleteNodeConfig = (id: string) => {
  return defHttp.delete<any>({
    url: `${Api.NODE_CONFIG_DELETE}/${id}`,
  });
};

// ============ 工作流表单页面路由生成 ============

/**
 * 生成工作流表单页面URL
 */
export const generateFormUrl = (params: {
  formType: string;
  dataId?: string;
  taskId?: string;
}) => {
  let url = `/workflow/form/${params.formType}`;
  
  if (params.dataId) {
    url += `/${params.dataId}`;
  }
  
  if (params.taskId) {
    url += `?taskId=${params.taskId}`;
  }
  
  return url;
};

/**
 * 工作流表单操作提示
 */
export const showWorkflowMessage = {
  success: (message: string) => createMessage.success(message),
  error: (message: string) => createMessage.error(message),
  warning: (message: string) => createMessage.warning(message),
  info: (message: string) => createMessage.info(message),
  
  // 常用提示
  submitSuccess: () => createMessage.success('提交成功'),
  saveSuccess: () => createMessage.success('保存成功'),
  approveSuccess: () => createMessage.success('审批成功'),
  rejectSuccess: () => createMessage.success('已拒绝'),
  
  submitError: (error?: string) => createMessage.error(`提交失败${error ? ': ' + error : ''}`),
  saveError: (error?: string) => createMessage.error(`保存失败${error ? ': ' + error : ''}`),
  permissionError: () => createMessage.error('权限不足，无法执行此操作'),
  validationError: (message: string) => createMessage.error(`验证失败: ${message}`),
};

// ============ TypeScript 类型定义 ============

export interface FormDisplayMode {
  mode: 'OPERATE' | 'TRACK' | 'VIEW';
  hasCurrentTask: boolean;
  currentTaskId?: string;
  currentTaskName?: string;
  processInstanceId?: string;
  fieldPermissions: FormPermissionConfig;
  availableActions: NodeButton[];
  showProgress: boolean;
  showVersionHistory: boolean;
  enableShortcuts: boolean;
  pageTitle?: string;
  message?: string;
  readonly: boolean;
}

export interface FormPermissionConfig {
  editableFields: string[];
  readonlyFields: string[];
  hiddenFields: string[];
  requiredFields: string[];
  conditionalPermissions?: Record<string, any>;
  formMode: string;
  customButtons?: Record<string, any>[];
  hiddenButtons?: string[];
}

export interface NodeButton {
  id: string;
  text: string;
  type: 'primary' | 'default' | 'danger' | 'success' | 'warning';
  action: string;
  icon?: string;
  visible: boolean;
  disabled: boolean;
  loading: boolean;
  order: number;
  confirmMessage?: string;
  successMessage?: string;
  style?: string;
  className?: string;
  attributes?: Record<string, any>;
  permission?: string;
  condition?: string;
}

export interface WorkflowConfig {
  id: string;
  cgformHeadId: string;
  processDefinitionKey: string;
  workflowEnabled: boolean;
  versionControlEnabled: boolean;
  permissionControlEnabled: boolean;
  businessKeyField?: string;
  statusField: string;
  processInstanceField: string;
  snapshotStrategy: string;
  snapshotNodes?: string;
  status: number;
}

export interface NodeConfig {
  id: string;
  cgformHeadId: string;
  processDefinitionKey: string;
  nodeId: string;
  nodeName: string;
  editableFields?: string;
  readonlyFields?: string;
  hiddenFields?: string;
  requiredFields?: string;
  conditionalPermissions?: string;
  formMode: string;
  customButtons?: string;
  hiddenButtons?: string;
  sortOrder: number;
  status: number;
}

// ================================== Flowable 7.0兼容性API ==================================

/**
 * 🎯 手动触发流程定义部署事件处理
 * @param processDefinitionKey 流程定义Key
 */
export const triggerDeploymentEvent = (processDefinitionKey: string) => {
  return defHttp.post<string>({
    url: Api.TRIGGER_DEPLOYMENT_EVENT,
    params: { processDefinitionKey }
  });
};

/**
 * 🎯 批量触发所有流程定义的部署事件
 */
export const triggerAllDeploymentEvents = () => {
  return defHttp.post<string>({
    url: Api.TRIGGER_ALL_DEPLOYMENT_EVENTS
  });
};

/**
 * 🎯 检查Flowable 7.0兼容性状态
 */
export const checkFlowable7Status = () => {
  return defHttp.get<string>({
    url: Api.CHECK_FLOWABLE7_STATUS
  });
};

/**
 * 🎯 手动触发流程实例启动事件
 * @param processInstanceId 流程实例ID
 * @param processDefinitionKey 流程定义Key
 */
export const triggerInstanceStartEvent = (processInstanceId: string, processDefinitionKey: string) => {
  return defHttp.post<string>({
    url: Api.TRIGGER_INSTANCE_START_EVENT,
    params: { 
      processInstanceId, 
      processDefinitionKey 
    }
  });
};

/**
 * 🎯 手动执行系统启动处理
 */
export const executeStartupProcess = () => {
  return defHttp.post<string>({
    url: '/workflow/executeStartupProcess'
  });
};