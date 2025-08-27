import { defHttp } from '/@/utils/http/axios';

/**
 * 工作流API接口
 */

enum Api {
  // 流程定义相关
  DefinitionList = '/workflow/definition/list',
  DefinitionDetail = '/workflow/definition',
  DefinitionDeploy = '/workflow/definition/deploy',
  DefinitionXml = '/workflow/definition',
  DefinitionToggle = '/workflow/definition',
  
  // 流程实例相关
  InstanceList = '/workflow/instance/list',
  InstanceDetail = '/workflow/instance',
  InstanceStart = '/workflow/instance/start',
  InstanceTerminate = '/workflow/instance',
  InstanceHistory = '/workflow/instance',
  InstanceDiagram = '/workflow/instance',
  
  // 任务相关
  TaskMy = '/workflow/task/my',
  TaskList = '/workflow/task/list',
  TaskDetail = '/workflow/task',
  TaskComplete = '/workflow/task',
  TaskDelegate = '/workflow/task',
  TaskTransfer = '/workflow/task',
  TaskForm = '/workflow/task',
  TaskHistory = '/workflow/task/history',
  
  // 维保工作流相关
  MaintenanceWorkflowStart = '/maintenance/workflow/start',
  MaintenanceCurrentTask = '/maintenance/workflow/current-task',
  MaintenanceTechnicianReview = '/maintenance/workflow/technician-review',
  MaintenanceApproval = '/maintenance/workflow/approval',
  MaintenanceAssignLabor = '/maintenance/workflow/assign-labor',
  MaintenanceLaborExecution = '/maintenance/workflow/labor-execution',
  MaintenanceQualityInspection = '/maintenance/workflow/quality-inspection',
  MaintenanceCustomerEvaluation = '/maintenance/workflow/customer-evaluation',
  
  // 历史记录相关
  HistoryProcess = '/workflow/history/process',
  HistoryTask = '/workflow/history/task',
  HistoryVariable = '/workflow/history/variable',
  
  // 用户相关
  UserList = '/workflow/user/list',
  RoleList = '/workflow/role/list',
  DeptList = '/workflow/dept/list',
  
  // 统计相关
  Stats = '/workflow/stats',
  StatsMyTasks = '/workflow/stats/my-tasks',
  StatsInstances = '/workflow/stats/instances',
}

// 流程定义相关
export const workflowDefinitionApi = {
  // 获取流程定义列表
  getList: (params: any) => defHttp.get({ url: Api.DefinitionList, params }),
  
  // 获取流程定义详情
  getDetail: (id: string) => defHttp.get({ url: `${Api.DefinitionDetail}/${id}` }),
  
  // 部署流程定义
  deploy: (data: any) => defHttp.post({ url: Api.DefinitionDeploy, data }),
  
  // 删除流程定义
  delete: (id: string) => defHttp.delete({ url: `${Api.DefinitionDetail}/${id}` }),
  
  // 获取流程定义XML
  getXml: (id: string) => defHttp.get({ url: `${Api.DefinitionXml}/${id}/xml` }),
  
  // 挂起/激活流程定义
  toggleState: (id: string, action: 'suspend' | 'activate') => 
    defHttp.put({ url: `${Api.DefinitionToggle}/${id}/${action}` }),
};

// ===== 模型仓库 API（新增） =====
export const workflowModelApi = {
  // 保存或更新模型基本信息
  saveModel: (data: any) => defHttp.post({ url: '/workflow/model', data }),
  // 新增模型版本（保存草稿XML）
  createVersion: (modelId: string, data: { xml: string; comment?: string }) =>
    defHttp.post({ url: `/workflow/model/${modelId}/versions`, data }),
  // 获取最新XML
  getLatestXml: (modelId: string) => defHttp.get({ url: `/workflow/model/${modelId}/xml` }),
  // 获取版本列表
  listVersions: (modelId: string) => defHttp.get({ url: `/workflow/model/${modelId}/versions` }),
  // 按Key获取模型
  getByKey: (modelKey: string) => defHttp.get({ url: `/workflow/model/byKey`, params: { modelKey } }),
  // 模型列表（关键字）
  list: (keyword?: string) => defHttp.get({ url: `/workflow/model/list`, params: { keyword } }),
  // 从模型部署（后端可选实现：直接传versionId；当前前端已通过上传XML复用deploy接口）
  deployVersion: (modelId: string, version: number) =>
    defHttp.post({ url: `/workflow/model/${modelId}/deploy`, params: { version } }),
};

// 流程实例相关
export const workflowInstanceApi = {
  // 获取流程实例列表
  getList: (params: any) => defHttp.get({ url: Api.InstanceList, params }),
  
  // 获取流程实例详情
  getDetail: (id: string) => defHttp.get({ url: `${Api.InstanceDetail}/${id}` }),
  
  // 启动流程实例
  start: (data: any) => defHttp.post({ url: Api.InstanceStart, data }),
  
  // 终止流程实例
  terminate: (id: string, reason?: string) => 
    defHttp.put({ url: `${Api.InstanceTerminate}/${id}/terminate`, data: { reason } }),
  
  // 获取流程实例历史
  getHistory: (id: string) => defHttp.get({ url: `${Api.InstanceHistory}/${id}/history` }),
  
  // 获取流程图
  getDiagram: (id: string) => defHttp.get({ url: `${Api.InstanceDiagram}/${id}/diagram` }),
};

// 任务相关
export const workflowTaskApi = {
  // 获取我的任务列表
  getMyTasks: (params: any) => defHttp.get({ url: Api.TaskMy, params }),
  
  // 获取所有任务列表
  getAllTasks: (params: any) => defHttp.get({ url: Api.TaskList, params }),
  
  // 获取任务详情
  getDetail: (id: string) => defHttp.get({ url: `${Api.TaskDetail}/${id}` }),
  
  // 完成任务
  complete: (id: string, data: any) => defHttp.put({ url: `${Api.TaskComplete}/${id}/complete`, data }),
  
  // 委托任务
  delegate: (id: string, assignee: string) => 
    defHttp.put({ url: `${Api.TaskDelegate}/${id}/delegate`, data: { assignee } }),
  
  // 转办任务
  transfer: (id: string, assignee: string) => 
    defHttp.put({ url: `${Api.TaskTransfer}/${id}/transfer`, data: { assignee } }),
  
  // 获取任务表单数据
  getFormData: (id: string) => defHttp.get({ url: `${Api.TaskForm}/${id}/form` }),
  
  // 保存任务表单数据
  saveFormData: (id: string, data: any) => defHttp.put({ url: `${Api.TaskForm}/${id}/form`, data }),
};

// 历史记录相关
export const workflowHistoryApi = {
  // 获取流程历史
  getProcessHistory: (processInstanceId: string) => 
    defHttp.get({ url: `${Api.HistoryProcess}/${processInstanceId}` }),
  
  // 获取任务历史
  getTaskHistory: (processInstanceId: string) => 
    defHttp.get({ url: `${Api.HistoryTask}/${processInstanceId}` }),
  
  // 获取变量历史
  getVariableHistory: (processInstanceId: string) => 
    defHttp.get({ url: `${Api.HistoryVariable}/${processInstanceId}` }),
};

// 用户相关
export const workflowUserApi = {
  // 获取用户列表（用于任务分配）
  getUserList: (params: any) => defHttp.get({ url: Api.UserList, params }),
  
  // 获取角色列表
  getRoleList: (params: any) => defHttp.get({ url: Api.RoleList, params }),
  
  // 获取部门列表
  getDeptList: (params: any) => defHttp.get({ url: Api.DeptList, params }),
};

// 统计相关
export const workflowStatsApi = {
  // 获取工作流统计信息
  getStats: () => defHttp.get({ url: Api.Stats }),
  
  // 获取我的待办统计
  getMyTaskStats: () => defHttp.get({ url: Api.StatsMyTasks }),
  
  // 获取流程实例统计
  getInstanceStats: (params: any) => defHttp.get({ url: Api.StatsInstances, params }),
};

// 维保工作流相关
export const maintenanceWorkflowApi = {
  // 启动维保工单流程
  startProcess: (orderId: string) => 
    defHttp.post({ url: `${Api.MaintenanceWorkflowStart}/${orderId}` }),
  
  // 获取工单当前任务
  getCurrentTask: (orderId: string) => 
    defHttp.get({ url: `${Api.MaintenanceCurrentTask}/${orderId}` }),
  
  // 维修人员审核
  technicianReview: (taskId: string, data: any) => 
    defHttp.post({ url: `${Api.MaintenanceTechnicianReview}/${taskId}`, data }),
  
  // 审批流程
  approval: (taskId: string, data: any) => 
    defHttp.post({ url: `${Api.MaintenanceApproval}/${taskId}`, data }),
  
  // 派单给劳务班组
  assignToLaborTeam: (taskId: string, data: any) => 
    defHttp.post({ url: `${Api.MaintenanceAssignLabor}/${taskId}`, data }),
  
  // 劳务执行维修
  laborExecution: (taskId: string, data: any) => 
    defHttp.post({ url: `${Api.MaintenanceLaborExecution}/${taskId}`, data }),
  
  // 质量验收
  qualityInspection: (taskId: string, data: any) => 
    defHttp.post({ url: `${Api.MaintenanceQualityInspection}/${taskId}`, data }),
  
  // 客户满意度评价
  customerEvaluation: (taskId: string, data: any) => 
    defHttp.post({ url: `${Api.MaintenanceCustomerEvaluation}/${taskId}`, data }),
}; 