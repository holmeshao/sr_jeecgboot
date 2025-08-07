import { defHttp } from '/@/utils/http/axios';

/**
 * 🎯 工作流按钮管理器
 * 基于JeecgBoot权限系统和工作流状态动态生成按钮
 */

// 工作流按钮接口（基于JeecgBoot ActionItem扩展）
export interface WorkflowButton {
  code: string;                    // 按钮编码
  label: string;                   // 按钮标签
  type?: 'primary' | 'default' | 'dashed' | 'link' | 'text';  // 按钮类型
  icon?: string;                   // 图标
  enabled?: boolean;               // 是否启用
  visible?: boolean;               // 是否可见
  loading?: boolean;               // 加载状态
  auth?: string | string[];        // 权限控制
  className?: string;              // 自定义样式
  requireComment?: boolean;        // 是否需要处理意见
  
  // 确认对话框配置
  popConfirm?: {
    title: string;
    okText?: string;
    cancelText?: string;
  };
  
  // 按钮处理函数
  onClick?: (comment?: string) => Promise<void> | void;
}

// 按钮配置模板
export interface ButtonTemplate {
  code: string;
  label: string;
  type?: 'primary' | 'default' | 'dashed' | 'link' | 'text';
  icon?: string;
  auth?: string | string[];
  requireComment?: boolean;
  popConfirm?: {
    title: string;
    okText?: string;
    cancelText?: string;
  };
  className?: string;
  conditions?: {
    status?: string[];           // 在哪些状态下显示
    nodeTypes?: string[];        // 在哪些节点类型下显示
    userRoles?: string[];        // 哪些角色可以看到
    formTypes?: string[];        // 哪些表单类型可以使用
  };
}

// 预定义的按钮模板（基于常见工作流场景）
const BUTTON_TEMPLATES: ButtonTemplate[] = [
  // 基础操作按钮
  {
    code: 'approve',
    label: '同意',
    type: 'primary',
    icon: 'ant-design:check-circle-outlined',
    requireComment: true,
    conditions: {
      nodeTypes: ['userTask', 'approvalTask'],
      status: ['PENDING', 'IN_PROCESS']
    }
  },
  {
    code: 'reject',
    label: '驳回',
    type: 'default',
    icon: 'ant-design:close-circle-outlined',
    requireComment: true,
    popConfirm: {
      title: '确定要驳回吗？',
      okText: '确定驳回',
      cancelText: '取消'
    },
    className: 'btn-danger',
    conditions: {
      nodeTypes: ['userTask', 'approvalTask'],
      status: ['PENDING', 'IN_PROCESS']
    }
  },
  {
    code: 'return',
    label: '退回',
    type: 'default',
    icon: 'ant-design:rollback-outlined',
    requireComment: true,
    popConfirm: {
      title: '确定要退回到上一步吗？',
      okText: '确定退回',
      cancelText: '取消'
    },
    conditions: {
      nodeTypes: ['userTask'],
      status: ['PENDING', 'IN_PROCESS']
    }
  },
  {
    code: 'transfer',
    label: '转办',
    type: 'default',
    icon: 'ant-design:swap-outlined',
    conditions: {
      nodeTypes: ['userTask'],
      status: ['PENDING', 'IN_PROCESS']
    }
  },
  {
    code: 'delegate',
    label: '委派',
    type: 'default',
    icon: 'ant-design:user-switch-outlined',
    conditions: {
      nodeTypes: ['userTask'],
      status: ['PENDING', 'IN_PROCESS']
    }
  },
  {
    code: 'claim',
    label: '认领',
    type: 'primary',
    icon: 'ant-design:hand-outlined',
    conditions: {
      nodeTypes: ['userTask'],
      status: ['PENDING']
    }
  },
  {
    code: 'unclaim',
    label: '释放',
    type: 'default',
    icon: 'ant-design:unlock-outlined',
    conditions: {
      nodeTypes: ['userTask'],
      status: ['CLAIMED']
    }
  },
  // 流程控制按钮
  {
    code: 'suspend',
    label: '挂起',
    type: 'default',
    icon: 'ant-design:pause-circle-outlined',
    auth: ['admin', 'process:suspend'],
    popConfirm: {
      title: '确定要挂起流程吗？',
      okText: '确定挂起',
      cancelText: '取消'
    },
    conditions: {
      status: ['IN_PROCESS']
    }
  },
  {
    code: 'resume',
    label: '恢复',
    type: 'primary',
    icon: 'ant-design:play-circle-outlined',
    auth: ['admin', 'process:resume'],
    conditions: {
      status: ['SUSPENDED']
    }
  },
  {
    code: 'terminate',
    label: '终止',
    type: 'default',
    icon: 'ant-design:stop-outlined',
    auth: ['admin', 'process:terminate'],
    requireComment: true,
    popConfirm: {
      title: '确定要终止流程吗？此操作不可逆！',
      okText: '确定终止',
      cancelText: '取消'
    },
    className: 'btn-danger',
    conditions: {
      status: ['IN_PROCESS', 'SUSPENDED']
    }
  },
  // 查看操作按钮
  {
    code: 'view_history',
    label: '查看历史',
    type: 'link',
    icon: 'ant-design:history-outlined',
    conditions: {
      status: ['IN_PROCESS', 'COMPLETED', 'TERMINATED']
    }
  },
  {
    code: 'view_diagram',
    label: '流程图',
    type: 'link',
    icon: 'ant-design:node-index-outlined',
    conditions: {
      status: ['IN_PROCESS', 'COMPLETED', 'TERMINATED']
    }
  },
  {
    code: 'print',
    label: '打印',
    type: 'default',
    icon: 'ant-design:printer-outlined',
    conditions: {
      status: ['IN_PROCESS', 'COMPLETED']
    }
  }
];

/**
 * 🎯 工作流按钮管理器类
 */
export class WorkflowButtonManager {
  private buttonTemplates: ButtonTemplate[];
  private context: WorkflowContext;

  constructor(context: WorkflowContext, customTemplates?: ButtonTemplate[]) {
    this.context = context;
    this.buttonTemplates = [...BUTTON_TEMPLATES, ...(customTemplates || [])];
  }

  /**
   * 🎯 根据上下文生成可用的按钮列表
   */
  generateButtons(): WorkflowButton[] {
    const buttons: WorkflowButton[] = [];

    for (const template of this.buttonTemplates) {
      if (this.shouldShowButton(template)) {
        const button = this.createButtonFromTemplate(template);
        buttons.push(button);
      }
    }

    return buttons.sort((a, b) => this.getButtonPriority(a.code) - this.getButtonPriority(b.code));
  }

  /**
   * 🎯 检查按钮是否应该显示
   */
  private shouldShowButton(template: ButtonTemplate): boolean {
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

    // 检查表单类型条件
    if (conditions.formTypes && this.context.formType && 
        !conditions.formTypes.includes(this.context.formType)) {
      return false;
    }

    return true;
  }

  /**
   * 🎯 从模板创建按钮实例
   */
  private createButtonFromTemplate(template: ButtonTemplate): WorkflowButton {
    return {
      code: template.code,
      label: template.label,
      type: template.type,
      icon: template.icon,
      enabled: true,
      visible: true,
      loading: false,
      auth: template.auth,
      className: template.className,
      requireComment: template.requireComment,
      popConfirm: template.popConfirm,
      onClick: this.createButtonHandler(template.code)
    };
  }

  /**
   * 🎯 创建按钮点击处理函数
   */
  private createButtonHandler(buttonCode: string) {
    return async (comment?: string) => {
      const taskId = this.context.taskId;
      const processInstanceId = this.context.processInstanceId;

      // 根据按钮代码执行不同的操作
      switch (buttonCode) {
        case 'approve':
          return await this.handleApprove(taskId, comment);
        case 'reject':
          return await this.handleReject(taskId, comment);
        case 'return':
          return await this.handleReturn(taskId, comment);
        case 'transfer':
          return await this.handleTransfer(taskId);
        case 'delegate':
          return await this.handleDelegate(taskId);
        case 'claim':
          return await this.handleClaim(taskId);
        case 'unclaim':
          return await this.handleUnclaim(taskId);
        case 'suspend':
          return await this.handleSuspend(processInstanceId);
        case 'resume':
          return await this.handleResume(processInstanceId);
        case 'terminate':
          return await this.handleTerminate(processInstanceId, comment);
        case 'view_history':
          return await this.handleViewHistory(processInstanceId);
        case 'view_diagram':
          return await this.handleViewDiagram(processInstanceId);
        case 'print':
          return await this.handlePrint();
        default:
          console.warn(`未知的按钮操作: ${buttonCode}`);
      }
    };
  }

  /**
   * 🎯 获取按钮优先级（用于排序）
   */
  private getButtonPriority(buttonCode: string): number {
    const priorityMap: Record<string, number> = {
      'approve': 1,
      'reject': 2,
      'return': 3,
      'claim': 4,
      'transfer': 5,
      'delegate': 6,
      'unclaim': 7,
      'suspend': 8,
      'resume': 9,
      'terminate': 10,
      'view_history': 20,
      'view_diagram': 21,
      'print': 22
    };
    return priorityMap[buttonCode] || 99;
  }

  // 🎯 具体的按钮处理方法
  private async handleApprove(taskId?: string, comment?: string) {
    if (!taskId) throw new Error('任务ID不能为空');
    return await defHttp.post({
      url: '/workflow/task/complete',
      data: { taskId, action: 'approve', comment }
    });
  }

  private async handleReject(taskId?: string, comment?: string) {
    if (!taskId) throw new Error('任务ID不能为空');
    return await defHttp.post({
      url: '/workflow/task/complete',
      data: { taskId, action: 'reject', comment }
    });
  }

  private async handleReturn(taskId?: string, comment?: string) {
    if (!taskId) throw new Error('任务ID不能为空');
    return await defHttp.post({
      url: '/workflow/task/return',
      data: { taskId, comment }
    });
  }

  private async handleTransfer(taskId?: string) {
    // 这里应该打开转办对话框，选择转办人
    console.log('打开转办对话框', taskId);
  }

  private async handleDelegate(taskId?: string) {
    // 这里应该打开委派对话框，选择委派人
    console.log('打开委派对话框', taskId);
  }

  private async handleClaim(taskId?: string) {
    if (!taskId) throw new Error('任务ID不能为空');
    return await defHttp.post({
      url: '/workflow/task/claim',
      data: { taskId }
    });
  }

  private async handleUnclaim(taskId?: string) {
    if (!taskId) throw new Error('任务ID不能为空');
    return await defHttp.post({
      url: '/workflow/task/unclaim',
      data: { taskId }
    });
  }

  private async handleSuspend(processInstanceId?: string) {
    if (!processInstanceId) throw new Error('流程实例ID不能为空');
    return await defHttp.post({
      url: '/workflow/process/suspend',
      data: { processInstanceId }
    });
  }

  private async handleResume(processInstanceId?: string) {
    if (!processInstanceId) throw new Error('流程实例ID不能为空');
    return await defHttp.post({
      url: '/workflow/process/resume',
      data: { processInstanceId }
    });
  }

  private async handleTerminate(processInstanceId?: string, comment?: string) {
    if (!processInstanceId) throw new Error('流程实例ID不能为空');
    return await defHttp.post({
      url: '/workflow/process/terminate',
      data: { processInstanceId, reason: comment }
    });
  }

  private async handleViewHistory(processInstanceId?: string) {
    // 这里应该打开历史记录对话框
    console.log('查看历史记录', processInstanceId);
  }

  private async handleViewDiagram(processInstanceId?: string) {
    // 这里应该打开流程图对话框
    console.log('查看流程图', processInstanceId);
  }

  private async handlePrint() {
    // 这里应该打印当前页面
    window.print();
  }
}

/**
 * 🎯 工作流上下文接口
 */
export interface WorkflowContext {
  taskId?: string;                 // 当前任务ID
  processInstanceId?: string;      // 流程实例ID
  status: string;                  // 当前状态
  nodeType?: string;               // 节点类型
  formType?: string;               // 表单类型
  userRoles?: string[];            // 当前用户角色
  userId?: string;                 // 当前用户ID
  assignee?: string;               // 任务处理人
  candidateGroups?: string[];      // 候选组
}

/**
 * 🎯 创建工作流按钮管理器的工厂函数
 */
export function createWorkflowButtonManager(
  context: WorkflowContext,
  customTemplates?: ButtonTemplate[]
): WorkflowButtonManager {
  return new WorkflowButtonManager(context, customTemplates);
}

/**
 * 🎯 基于任务信息自动生成按钮的快捷函数
 */
export async function generateWorkflowButtons(
  taskId?: string,
  processInstanceId?: string,
  formType?: string
): Promise<WorkflowButton[]> {
  
  // 获取任务和流程信息
  let context: WorkflowContext = {
    status: 'DRAFT'
  };

  if (taskId) {
    try {
      // 获取任务详情
      const taskInfo = await defHttp.get({
        url: `/workflow/task/${taskId}/info`
      });
      
      context = {
        taskId: taskId,
        processInstanceId: taskInfo.processInstanceId,
        status: 'PENDING',
        nodeType: taskInfo.nodeType || 'userTask',
        formType: formType,
        assignee: taskInfo.assignee,
        candidateGroups: taskInfo.candidateGroups
      };
    } catch (error) {
      console.warn('获取任务信息失败，使用默认上下文', error);
    }
  } else if (processInstanceId) {
    context.processInstanceId = processInstanceId;
    context.status = 'IN_PROCESS';
    context.formType = formType;
  }

  const manager = createWorkflowButtonManager(context);
  return manager.generateButtons();
}