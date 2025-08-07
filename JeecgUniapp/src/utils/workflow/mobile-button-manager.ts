import { http } from '@/utils/http';
import type { MobileWorkflowButton } from '@/components/workflow/workflow-mobile-form.vue';

/**
 * 🎯 移动端工作流按钮管理器
 * 基于JeecgUniapp的交互模式和API体系
 */

// 移动端按钮模板
export interface MobileButtonTemplate {
  code: string;
  label: string;
  type?: 'primary' | 'success' | 'warning' | 'danger' | 'default';
  requireComment?: boolean;
  confirmMessage?: string;
  conditions?: {
    status?: string[];
    nodeTypes?: string[];
    userRoles?: string[];
  };
}

// 预定义的移动端按钮模板（针对移动端场景优化）
const MOBILE_BUTTON_TEMPLATES: MobileButtonTemplate[] = [
  // 核心审批操作
  {
    code: 'approve',
    label: '同意',
    type: 'primary',
    requireComment: true,
    conditions: {
      nodeTypes: ['userTask', 'approvalTask'],
      status: ['PENDING', 'IN_PROCESS']
    }
  },
  {
    code: 'reject',
    label: '驳回',
    type: 'danger',
    requireComment: true,
    confirmMessage: '确定要驳回吗？',
    conditions: {
      nodeTypes: ['userTask', 'approvalTask'],
      status: ['PENDING', 'IN_PROCESS']
    }
  },
  // 流程控制操作
  {
    code: 'return',
    label: '退回',
    type: 'warning',
    requireComment: true,
    confirmMessage: '确定要退回到上一步吗？',
    conditions: {
      nodeTypes: ['userTask'],
      status: ['PENDING', 'IN_PROCESS']
    }
  },
  {
    code: 'claim',
    label: '认领',
    type: 'success',
    conditions: {
      nodeTypes: ['userTask'],
      status: ['PENDING']
    }
  },
  {
    code: 'unclaim',
    label: '释放',
    type: 'default',
    conditions: {
      nodeTypes: ['userTask'],
      status: ['CLAIMED']
    }
  },
  // 简化的管理操作（移动端常用）
  {
    code: 'transfer',
    label: '转办',
    type: 'default',
    conditions: {
      nodeTypes: ['userTask'],
      status: ['PENDING', 'IN_PROCESS']
    }
  },
  {
    code: 'suspend',
    label: '挂起',
    type: 'warning',
    confirmMessage: '确定要挂起流程吗？',
    conditions: {
      status: ['IN_PROCESS']
    }
  },
  {
    code: 'resume',
    label: '恢复',
    type: 'success',
    conditions: {
      status: ['SUSPENDED']
    }
  }
];

/**
 * 🎯 移动端工作流上下文
 */
export interface MobileWorkflowContext {
  taskId?: string;
  processInstanceId?: string;
  status: string;
  nodeType?: string;
  formType?: string;
  userRoles?: string[];
  isOwner?: boolean;
}

/**
 * 🎯 移动端工作流按钮管理器
 */
export class MobileWorkflowButtonManager {
  private buttonTemplates: MobileButtonTemplate[];
  private context: MobileWorkflowContext;

  constructor(context: MobileWorkflowContext, customTemplates?: MobileButtonTemplate[]) {
    this.context = context;
    this.buttonTemplates = [...MOBILE_BUTTON_TEMPLATES, ...(customTemplates || [])];
  }

  /**
   * 🎯 生成移动端适配的按钮列表
   */
  generateButtons(): MobileWorkflowButton[] {
    const buttons: MobileWorkflowButton[] = [];

    for (const template of this.buttonTemplates) {
      if (this.shouldShowButton(template)) {
        const button = this.createMobileButton(template);
        buttons.push(button);
      }
    }

    // 移动端按钮排序：重要操作优先
    return buttons.sort((a, b) => this.getMobilePriority(a.code) - this.getMobilePriority(b.code));
  }

  /**
   * 🎯 检查按钮是否应该在移动端显示
   */
  private shouldShowButton(template: MobileButtonTemplate): boolean {
    const conditions = template.conditions;
    if (!conditions) return true;

    // 检查状态条件
    if (conditions.status && !conditions.status.includes(this.context.status)) {
      return false;
    }

    // 检查节点类型条件
    if (conditions.nodeTypes && this.context.nodeType && 
        !conditions.nodeTypes.includes(this.context.nodeType)) {
      return false;
    }

    // 检查用户角色条件  
    if (conditions.userRoles && this.context.userRoles && 
        !conditions.userRoles.some(role => this.context.userRoles!.includes(role))) {
      return false;
    }

    return true;
  }

  /**
   * 🎯 创建移动端按钮实例
   */
  private createMobileButton(template: MobileButtonTemplate): MobileWorkflowButton {
    return {
      code: template.code,
      label: template.label,
      type: template.type,
      enabled: true,
      visible: true,
      loading: false,
      requireComment: template.requireComment,
      confirmMessage: template.confirmMessage
    };
  }

  /**
   * 🎯 移动端按钮优先级（常用操作排前面）
   */
  private getMobilePriority(buttonCode: string): number {
    const mobilePriorityMap: Record<string, number> = {
      'approve': 1,    // 同意最重要
      'reject': 2,     // 驳回第二重要
      'claim': 3,      // 认领任务
      'return': 4,     // 退回
      'transfer': 5,   // 转办
      'unclaim': 6,    // 释放
      'suspend': 7,    // 挂起
      'resume': 8      // 恢复
    };
    return mobilePriorityMap[buttonCode] || 99;
  }
}

/**
 * 🎯 移动端快捷方法：根据任务信息生成按钮
 */
export async function generateMobileWorkflowButtons(
  taskId?: string,
  processInstanceId?: string,
  formType?: string
): Promise<MobileWorkflowButton[]> {
  
  // 构建移动端上下文
  let context: MobileWorkflowContext = {
    status: 'DRAFT'
  };

  if (taskId) {
    try {
      // 获取移动端任务信息
      const taskInfo = await getMobileTaskInfo(taskId);
      
      context = {
        taskId: taskId,
        processInstanceId: taskInfo.processInstanceId,
        status: 'PENDING',
        nodeType: taskInfo.nodeType || 'userTask',
        formType: formType,
        isOwner: taskInfo.isOwner || false
      };
    } catch (error) {
      console.warn('获取移动端任务信息失败，使用默认上下文', error);
    }
  } else if (processInstanceId) {
    context.processInstanceId = processInstanceId;
    context.status = 'IN_PROCESS';
    context.formType = formType;
  }

  const manager = new MobileWorkflowButtonManager(context);
  return manager.generateButtons();
}

/**
 * 🎯 获取移动端任务信息
 */
export async function getMobileTaskInfo(taskId: string): Promise<any> {
  try {
    const response = await http.get(`/workflow/mobile/task/${taskId}/info`);
    return response;
  } catch (error) {
    console.error('获取移动端任务信息失败:', error);
    return {
      processInstanceId: '',
      nodeType: 'userTask',
      isOwner: false
    };
  }
}

/**
 * 🎯 移动端工作流状态映射
 */
export const MOBILE_STATUS_MAP = {
  'DRAFT': '草稿',
  'PENDING': '待处理',
  'IN_PROCESS': '进行中',
  'COMPLETED': '已完成',
  'SUSPENDED': '已挂起',
  'TERMINATED': '已终止',
  'CLAIMED': '已认领'
};

/**
 * 🎯 获取移动端状态显示文本
 */
export function getMobileStatusText(status: string): string {
  return MOBILE_STATUS_MAP[status] || status;
}

/**
 * 🎯 移动端按钮动作处理器
 */
export class MobileActionHandler {
  
  /**
   * 处理同意操作
   */
  static async handleApprove(taskId: string, comment: string, formData: any): Promise<any> {
    return await http.post('/workflow/mobile/task/approve', {
      taskId,
      comment,
      formData
    });
  }

  /**
   * 处理驳回操作
   */
  static async handleReject(taskId: string, comment: string, formData: any): Promise<any> {
    return await http.post('/workflow/mobile/task/reject', {
      taskId,
      comment,
      formData
    });
  }

  /**
   * 处理退回操作
   */
  static async handleReturn(taskId: string, comment: string): Promise<any> {
    return await http.post('/workflow/mobile/task/return', {
      taskId,
      comment
    });
  }

  /**
   * 处理认领操作
   */
  static async handleClaim(taskId: string): Promise<any> {
    return await http.post('/workflow/mobile/task/claim', {
      taskId
    });
  }

  /**
   * 处理释放操作
   */
  static async handleUnclaim(taskId: string): Promise<any> {
    return await http.post('/workflow/mobile/task/unclaim', {
      taskId
    });
  }

  /**
   * 处理转办操作（移动端简化版）
   */
  static async handleTransfer(taskId: string, targetUser: string): Promise<any> {
    return await http.post('/workflow/mobile/task/transfer', {
      taskId,
      targetUser
    });
  }

  /**
   * 处理挂起操作
   */
  static async handleSuspend(processInstanceId: string): Promise<any> {
    return await http.post('/workflow/mobile/process/suspend', {
      processInstanceId
    });
  }

  /**
   * 处理恢复操作
   */
  static async handleResume(processInstanceId: string): Promise<any> {
    return await http.post('/workflow/mobile/process/resume', {
      processInstanceId
    });
  }
}

/**
 * 🎯 移动端工作流按钮管理器工厂函数
 */
export function createMobileButtonManager(
  context: MobileWorkflowContext,
  customTemplates?: MobileButtonTemplate[]
): MobileWorkflowButtonManager {
  return new MobileWorkflowButtonManager(context, customTemplates);
}

/**
 * 🎯 移动端按钮点击处理的统一入口
 */
export async function handleMobileButtonClick(
  buttonCode: string,
  taskId?: string,
  processInstanceId?: string,
  comment?: string,
  formData?: any
): Promise<any> {
  
  switch (buttonCode) {
    case 'approve':
      return await MobileActionHandler.handleApprove(taskId!, comment!, formData);
    case 'reject':
      return await MobileActionHandler.handleReject(taskId!, comment!, formData);
    case 'return':
      return await MobileActionHandler.handleReturn(taskId!, comment!);
    case 'claim':
      return await MobileActionHandler.handleClaim(taskId!);
    case 'unclaim':
      return await MobileActionHandler.handleUnclaim(taskId!);
    case 'transfer':
      // 移动端转办需要额外的用户选择界面，这里先抛出提示
      throw new Error('转办功能需要选择目标用户');
    case 'suspend':
      return await MobileActionHandler.handleSuspend(processInstanceId!);
    case 'resume':
      return await MobileActionHandler.handleResume(processInstanceId!);
    default:
      throw new Error(`未知的按钮操作: ${buttonCode}`);
  }
}