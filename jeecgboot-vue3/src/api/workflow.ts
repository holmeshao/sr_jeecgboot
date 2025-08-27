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
  
  // 工作流配置API (修正路径 - 后端在jeecg-system中)
  WORKFLOW_CONFIG_LIST = '/workflow/onlineForm/config/list',
  WORKFLOW_CONFIG_ADD = '/workflow/onlineForm/config/add',
  WORKFLOW_CONFIG_EDIT = '/workflow/onlineForm/config/edit',
  WORKFLOW_CONFIG_DELETE = '/workflow/onlineForm/config/delete',
  
  // 节点权限配置API (修正路径)
  NODE_CONFIG_LIST = '/workflow/onlineForm/node/list',
  NODE_CONFIG_ADD = '/workflow/onlineForm/node/add',
  NODE_CONFIG_EDIT = '/workflow/onlineForm/node/edit',
  NODE_CONFIG_DELETE = '/workflow/onlineForm/node/delete',
  
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

// ============ 工作流定义管理 API ============

/**
 * 工作流定义管理API
 */
export const workflowDefinitionApi = {
  /**
   * 获取流程定义列表
   */
  getList: (params: any) => {
    return defHttp.get<any>({
      url: '/workflow/definition/list',
      params,
    });
  },

  /**
   * 部署流程定义
   */
  deploy: (data: any) => {
    return defHttp.post<any>({
      url: '/workflow/definition/deploy',
      data,
    });
  },

  /**
   * 获取流程定义XML
   */
  getXml: (id: string) => {
    return defHttp.get<any>({
      url: `/workflow/definition/${id}/xml`,
    });
  },

  /**
   * 切换流程定义状态（激活/挂起）
   */
  toggleState: (id: string, action: string) => {
    return defHttp.put<any>({
      url: `/workflow/definition/${id}/${action}`,
    });
  },

  /**
   * 删除流程定义
   */
  delete: (id: string) => {
    return defHttp.delete<any>({
      url: `/workflow/definition/${id}`,
    });
  },
};

// ============ 模型仓库 API ============

export const workflowModelApi = {
  /** 保存或更新模型基本信息 */
  saveModel: (data: any) => {
    return defHttp.post<any>({
      url: '/workflow/model',
      data,
    });
  },

  /** 新增模型版本（保存草稿XML） */
  createVersion: (modelId: string, data: { xml: string; comment?: string }) => {
    return defHttp.post<any>({
      url: `/workflow/model/${modelId}/versions`,
      data,
    });
  },

  /** 获取模型最新XML */
  getLatestXml: (modelId: string) => {
    return defHttp.get<any>({
      url: `/workflow/model/${modelId}/xml`,
    });
  },

  /** 获取模型版本列表 */
  listVersions: (modelId: string) => {
    return defHttp.get<any>({
      url: `/workflow/model/${modelId}/versions`,
    });
  },

  /** 按Key查询模型 */
  getByKey: (modelKey: string) => {
    return defHttp.get<any>({
      url: `/workflow/model/byKey`,
      params: { modelKey },
    });
  },

  /** 模型列表 */
  list: (keyword?: string) => {
    return defHttp.get<any>({
      url: `/workflow/model/list`,
      params: { keyword },
    });
  },

  /** 从模型版本部署（可选：服务端占位） */
  deployVersion: (modelId: string, version: number) => {
    return defHttp.post<any>({
      url: `/workflow/model/${modelId}/deploy`,
      params: { version },
    });
  },
};

// ============ 工作流任务管理 API ============

/**
 * 工作流任务管理API
 */
export const workflowTaskApi = {
  /**
   * 获取我的任务列表
   */
  getMyTasks: (params: any) => {
    return defHttp.get<any>({
      url: '/workflow/task/my-tasks',
      params,
    });
  },

  /**
   * 获取任务列表
   */
  getList: (params: any) => {
    return defHttp.get<any>({
      url: '/workflow/task/list',
      params,
    });
  },

  /**
   * 完成任务
   */
  complete: (taskId: string, data: any) => {
    return defHttp.post<any>({
      url: `/workflow/task/complete/${taskId}`,
      data,
    });
  },

  /**
   * 委派任务
   */
  delegate: (taskId: string, assignee: string) => {
    return defHttp.post<any>({
      url: `/workflow/task/delegate/${taskId}`,
      params: { assignee },
    });
  },

  /**
   * 转办任务
   */
  transfer: (taskId: string, assignee: string) => {
    return defHttp.post<any>({
      url: `/workflow/task/transfer/${taskId}`,
      params: { assignee },
    });
  },

  /**
   * 退回任务
   */
  reject: (taskId: string, data: any) => {
    return defHttp.post<any>({
      url: `/workflow/task/reject/${taskId}`,
      data,
    });
  },
};

// ============ 工作流实例管理 API ============

/**
 * 工作流实例管理API
 */
export const workflowInstanceApi = {
  /**
   * 获取流程实例列表
   */
  getList: (params: any) => {
    return defHttp.get<any>({
      url: '/workflow/instance/list',
      params,
    });
  },

  /**
   * 删除流程实例
   */
  delete: (instanceId: string, reason?: string) => {
    return defHttp.delete<any>({
      url: `/workflow/instance/${instanceId}`,
      params: { reason },
    });
  },

  /**
   * 挂起流程实例
   */
  suspend: (instanceId: string) => {
    return defHttp.post<any>({
      url: `/workflow/instance/suspend/${instanceId}`,
    });
  },

  /**
   * 激活流程实例
   */
  activate: (instanceId: string) => {
    return defHttp.post<any>({
      url: `/workflow/instance/activate/${instanceId}`,
    });
  },
};

// ============ 工作流历史管理 API ============

/**
 * 工作流历史管理API
 */
export const workflowHistoryApi = {
  /**
   * 获取历史任务列表
   */
  getHistoryTasks: (params: any) => {
    return defHttp.get<any>({
      url: '/workflow/history/tasks',
      params,
    });
  },

  /**
   * 获取历史流程实例列表
   */
  getHistoryInstances: (params: any) => {
    return defHttp.get<any>({
      url: '/workflow/history/instances',
      params,
    });
  },

  /**
   * 获取流程历史详情
   */
  getInstanceHistory: (instanceId: string) => {
    return defHttp.get<any>({
      url: `/workflow/history/instance/${instanceId}`,
    });
  },
};

// ============ 工作流统计管理 API ============

/**
 * 工作流统计管理API
 */
export const workflowStatsApi = {
  /**
   * 获取工作流统计数据
   */
  getStats: (params: any) => {
    return defHttp.get<any>({
      url: '/workflow/stats/overview',
      params,
    });
  },

  /**
   * 获取任务统计
   */
  getTaskStats: (params: any) => {
    return defHttp.get<any>({
      url: '/workflow/stats/tasks',
      params,
    });
  },

  /**
   * 获取流程统计
   */
  getProcessStats: (params: any) => {
    return defHttp.get<any>({
      url: '/workflow/stats/processes',
      params,
    });
  },
};