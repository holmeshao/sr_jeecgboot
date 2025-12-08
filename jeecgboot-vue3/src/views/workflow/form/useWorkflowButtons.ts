/**
 * 工作流按钮 Hook
 * 用于获取工作流任务上下文，并根据上下文返回可用的按钮列表
 */
import { ref, computed, watch, onMounted, Ref } from 'vue';
import { defHttp } from '/@/utils/http/axios';

/**
 * 工作流按钮定义
 */
export interface WorkflowButton {
  code: string;
  name: string;
  type: 'primary' | 'default' | 'danger' | 'warning' | 'link';
  icon?: string;
  order: number;
  loading?: boolean;
  disabled?: boolean;
}

/**
 * 工作流任务上下文
 */
export interface WorkflowTaskContext {
  // 基础信息
  hasTask: boolean;
  taskId?: string;
  taskName?: string;
  taskDefinitionKey?: string;
  processInstanceId?: string;
  processDefinitionKey?: string;
  businessKey?: string;
  
  // 状态
  taskStatus?: 'UNASSIGNED' | 'ASSIGNED' | 'COMPLETED';
  processStatus?: 'NOT_STARTED' | 'RUNNING' | 'COMPLETED' | 'SUSPENDED' | 'TERMINATED';
  
  // 人员信息
  assignee?: string;
  candidateUsers?: string[];
  candidateGroups?: string[];
  starter?: string;
  
  // 权限判断
  assigneeFlag: boolean;
  candidateFlag: boolean;
  starterFlag: boolean;
  
  // 可执行操作
  canClaim: boolean;
  canUnclaim: boolean;
  canApprove: boolean;
  canWithdraw: boolean;
  canSubmit: boolean;
  canSave: boolean;
  readOnly: boolean;
  
  // 可用按钮列表
  availableButtons: WorkflowButton[];
}

/**
 * 工作流参数
 */
export interface WorkflowParams {
  taskId?: string;
  processInstanceId?: string;
  businessKey?: string;
  dataId?: string;
  action?: 'add' | 'edit' | 'view';
}

/**
 * 按钮点击处理器
 */
export type ButtonClickHandler = (code: string, context: WorkflowTaskContext) => void | Promise<void>;

/**
 * 获取工作流任务上下文 API
 */
async function fetchTaskContext(params: WorkflowParams): Promise<WorkflowTaskContext> {
  const { taskId, processInstanceId, businessKey, dataId, action } = params;
  
  // 如果是查看模式，设置 viewOnly
  const viewOnly = action === 'view';
  
  // 如果没有 taskId 和 processInstanceId，用 dataId 作为 businessKey
  const actualBusinessKey = businessKey || dataId;
  
  const response = await defHttp.get({
    url: '/workflow/task/button-context',
    params: {
      taskId,
      processInstanceId,
      businessKey: actualBusinessKey,
      viewOnly,
    },
  });
  
  return response;
}

/**
 * 工作流按钮 Hook
 * 
 * @param params 工作流参数（响应式）
 * @param onButtonClick 按钮点击回调
 * @returns 
 */
export function useWorkflowButtons(
  params: Ref<WorkflowParams> | WorkflowParams,
  onButtonClick?: ButtonClickHandler
) {
  // 任务上下文
  const context = ref<WorkflowTaskContext | null>(null);
  
  // 加载状态
  const loading = ref(false);
  
  // 错误信息
  const error = ref<string | null>(null);
  
  // 按钮加载状态
  const buttonLoadingMap = ref<Record<string, boolean>>({});
  
  // 获取参数值（支持响应式和普通对象）
  const getParams = (): WorkflowParams => {
    return 'value' in params ? params.value : params;
  };
  
  // 加载上下文
  const loadContext = async () => {
    const currentParams = getParams();
    
    // 如果没有任何参数，创建新建模式的上下文
    if (!currentParams.taskId && !currentParams.processInstanceId && 
        !currentParams.businessKey && !currentParams.dataId) {
      context.value = {
        hasTask: false,
        processStatus: 'NOT_STARTED',
        assigneeFlag: false,
        candidateFlag: false,
        starterFlag: true,
        canClaim: false,
        canUnclaim: false,
        canApprove: false,
        canWithdraw: false,
        canSubmit: true,
        canSave: true,
        readOnly: false,
        availableButtons: [
          { code: 'submit', name: '提交审核', type: 'primary', icon: 'ant-design:send-outlined', order: 1 },
          { code: 'save', name: '保存草稿', type: 'default', icon: 'ant-design:save-outlined', order: 2 },
          { code: 'cancel', name: '取消', type: 'default', icon: 'ant-design:close-outlined', order: 99 },
        ],
      };
      return;
    }
    
    loading.value = true;
    error.value = null;
    
    try {
      context.value = await fetchTaskContext(currentParams);
    } catch (e: any) {
      console.error('加载工作流上下文失败:', e);
      error.value = e.message || '加载失败';
      // 失败时回退到只读模式
      context.value = {
        hasTask: false,
        readOnly: true,
        assigneeFlag: false,
        candidateFlag: false,
        starterFlag: false,
        canClaim: false,
        canUnclaim: false,
        canApprove: false,
        canWithdraw: false,
        canSubmit: false,
        canSave: false,
        availableButtons: [
          { code: 'close', name: '关闭', type: 'default', icon: 'ant-design:close-outlined', order: 99 },
        ],
      };
    } finally {
      loading.value = false;
    }
  };
  
  // 可用按钮列表
  const buttons = computed<WorkflowButton[]>(() => {
    if (!context.value) return [];
    return (context.value.availableButtons || []).map(btn => ({
      ...btn,
      loading: buttonLoadingMap.value[btn.code] || false,
    }));
  });
  
  // 是否只读
  const isReadOnly = computed(() => context.value?.readOnly ?? true);
  
  // 是否可以提交
  const canSubmit = computed(() => context.value?.canSubmit ?? false);
  
  // 是否可以审批
  const canApprove = computed(() => context.value?.canApprove ?? false);
  
  // 是否可以认领
  const canClaim = computed(() => context.value?.canClaim ?? false);
  
  // 当前任务ID
  const taskId = computed(() => context.value?.taskId);
  
  // 流程实例ID
  const processInstanceId = computed(() => context.value?.processInstanceId);
  
  // 处理按钮点击
  const handleButtonClick = async (code: string) => {
    if (!context.value) return;
    
    buttonLoadingMap.value[code] = true;
    
    try {
      if (onButtonClick) {
        await onButtonClick(code, context.value);
      }
    } finally {
      buttonLoadingMap.value[code] = false;
    }
  };
  
  // 刷新上下文
  const refresh = () => {
    return loadContext();
  };
  
  // 监听参数变化
  if ('value' in params) {
    watch(params, () => {
      loadContext();
    }, { deep: true });
  }
  
  // 初始化加载
  onMounted(() => {
    loadContext();
  });
  
  return {
    // 状态
    context,
    loading,
    error,
    buttons,
    
    // 计算属性
    isReadOnly,
    canSubmit,
    canApprove,
    canClaim,
    taskId,
    processInstanceId,
    
    // 方法
    handleButtonClick,
    refresh,
    loadContext,
  };
}

/**
 * 默认按钮操作处理
 * 可以在 IntegratedForm 中使用这个函数来处理标准的工作流操作
 */
export function createDefaultButtonHandlers(options: {
  onSubmit?: (context: WorkflowTaskContext) => Promise<void>;
  onSave?: (context: WorkflowTaskContext) => Promise<void>;
  onApprove?: (context: WorkflowTaskContext) => Promise<void>;
  onReject?: (context: WorkflowTaskContext) => Promise<void>;
  onClaim?: (context: WorkflowTaskContext) => Promise<void>;
  onUnclaim?: (context: WorkflowTaskContext) => Promise<void>;
  onCancel?: () => void;
  onClose?: () => void;
}): ButtonClickHandler {
  return async (code: string, context: WorkflowTaskContext) => {
    switch (code) {
      case 'submit':
        if (options.onSubmit) await options.onSubmit(context);
        break;
      case 'save':
        if (options.onSave) await options.onSave(context);
        break;
      case 'approve':
        if (options.onApprove) await options.onApprove(context);
        break;
      case 'reject':
        if (options.onReject) await options.onReject(context);
        break;
      case 'claim':
        if (options.onClaim) await options.onClaim(context);
        break;
      case 'unclaim':
        if (options.onUnclaim) await options.onUnclaim(context);
        break;
      case 'cancel':
        if (options.onCancel) options.onCancel();
        break;
      case 'close':
        if (options.onClose) options.onClose();
        else if (options.onCancel) options.onCancel();
        break;
      default:
        console.warn(`未处理的按钮操作: ${code}`);
    }
  };
}

export default useWorkflowButtons;
