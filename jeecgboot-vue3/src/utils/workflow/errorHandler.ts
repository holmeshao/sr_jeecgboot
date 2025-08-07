import { message, notification, Modal } from 'ant-design-vue';
import { h } from 'vue';

/**
 * 🎯 工作流错误处理工具
 * 基于JeecgBoot的消息提示模式，提供用户友好的错误处理
 */

// 错误代码映射
const ERROR_CODE_MAP: Record<string, string> = {
  'FORM_NOT_FOUND': '表单配置不存在，请检查表单设置',
  'DATA_NOT_FOUND': '数据不存在，可能已被删除',
  'PROCESS_NOT_FOUND': '工作流实例不存在',
  'TASK_NOT_FOUND': '任务不存在或已完成',
  'PERMISSION_CONFIG_NOT_FOUND': '权限配置不存在，请先配置节点权限',
  'INVALID_FORM_DATA': '表单数据格式错误',
  'WORKFLOW_NOT_ENABLED': '此表单未启用工作流功能',
  'CANNOT_START_WORKFLOW': '无法启动工作流',
  'INVALID_PERMISSION_CONFIG': '权限配置存在错误',
};

// 错误级别
export enum ErrorLevel {
  INFO = 'info',
  SUCCESS = 'success',
  WARNING = 'warning',
  ERROR = 'error',
}

// 错误处理选项
export interface ErrorHandlerOptions {
  showMessage?: boolean;        // 是否显示消息提示
  showNotification?: boolean;   // 是否显示通知
  showModal?: boolean;          // 是否显示模态框
  level?: ErrorLevel;           // 错误级别
  duration?: number;            // 显示时长（秒）
  title?: string;               // 标题
  description?: string;         // 详细描述
  onClose?: () => void;         // 关闭回调
}

/**
 * 🎯 统一错误处理函数
 */
export function handleWorkflowError(
  error: any, 
  options: ErrorHandlerOptions = {}
): void {
  const {
    showMessage = true,
    showNotification = false,
    showModal = false,
    level = ErrorLevel.ERROR,
    duration = 3,
    title,
    description,
    onClose
  } = options;

  // 解析错误信息
  const errorInfo = parseError(error);
  
  console.error('Workflow Error:', errorInfo);

  // 显示消息提示
  if (showMessage) {
    const messageText = title || errorInfo.message;
    
    switch (level) {
      case ErrorLevel.SUCCESS:
        message.success(messageText, duration);
        break;
      case ErrorLevel.WARNING:
        message.warning(messageText, duration);
        break;
      case ErrorLevel.INFO:
        message.info(messageText, duration);
        break;
      case ErrorLevel.ERROR:
      default:
        message.error(messageText, duration);
        break;
    }
  }

  // 显示通知
  if (showNotification) {
    notification[level]({
      message: title || '操作提示',
      description: description || errorInfo.message,
      duration: duration,
      onClose: onClose,
    });
  }

  // 显示模态框
  if (showModal) {
    const modalTitle = title || getModalTitle(level);
    const modalContent = description || errorInfo.message;
    
    if (level === ErrorLevel.ERROR) {
      Modal.error({
        title: modalTitle,
        content: modalContent,
        onOk: onClose,
      });
    } else if (level === ErrorLevel.WARNING) {
      Modal.warning({
        title: modalTitle,
        content: modalContent,
        onOk: onClose,
      });
    } else {
      Modal.info({
        title: modalTitle,
        content: modalContent,
        onOk: onClose,
      });
    }
  }
}

/**
 * 🎯 解析错误对象
 */
function parseError(error: any): { code: string; message: string; details?: any } {
  // 处理axios错误
  if (error.response) {
    const response = error.response;
    const data = response.data;
    
    if (data && data.code && data.message) {
      return {
        code: data.code,
        message: ERROR_CODE_MAP[data.code] || data.message,
        details: data
      };
    }
    
    // HTTP状态码错误
    switch (response.status) {
      case 400:
        return { code: 'BAD_REQUEST', message: '请求参数错误' };
      case 401:
        return { code: 'UNAUTHORIZED', message: '未授权，请重新登录' };
      case 403:
        return { code: 'FORBIDDEN', message: '权限不足，请联系管理员' };
      case 404:
        return { code: 'NOT_FOUND', message: '请求的资源不存在' };
      case 500:
        return { code: 'INTERNAL_ERROR', message: '服务器内部错误' };
      default:
        return { code: 'UNKNOWN_HTTP_ERROR', message: `HTTP错误：${response.status}` };
    }
  }
  
  // 处理网络错误
  if (error.code === 'NETWORK_ERROR' || error.message === 'Network Error') {
    return { code: 'NETWORK_ERROR', message: '网络连接失败，请检查网络设置' };
  }
  
  // 处理超时错误
  if (error.code === 'ECONNABORTED' || error.message.includes('timeout')) {
    return { code: 'TIMEOUT', message: '请求超时，请稍后重试' };
  }
  
  // 处理业务错误对象
  if (error.code && error.message) {
    return {
      code: error.code,
      message: ERROR_CODE_MAP[error.code] || error.message
    };
  }
  
  // 处理字符串错误
  if (typeof error === 'string') {
    return { code: 'UNKNOWN_ERROR', message: error };
  }
  
  // 处理Error对象
  if (error instanceof Error) {
    return { code: 'JAVASCRIPT_ERROR', message: error.message };
  }
  
  // 兜底处理
  return { code: 'UNKNOWN_ERROR', message: '未知错误，请联系管理员' };
}

/**
 * 🎯 获取模态框标题
 */
function getModalTitle(level: ErrorLevel): string {
  switch (level) {
    case ErrorLevel.SUCCESS:
      return '操作成功';
    case ErrorLevel.WARNING:
      return '操作警告';
    case ErrorLevel.INFO:
      return '操作提示';
    case ErrorLevel.ERROR:
    default:
      return '操作失败';
  }
}

/**
 * 🎯 快捷方法 - 显示成功消息
 */
export function showSuccess(message: string, options?: Partial<ErrorHandlerOptions>): void {
  handleWorkflowError(
    { code: 'SUCCESS', message },
    { ...options, level: ErrorLevel.SUCCESS }
  );
}

/**
 * 🎯 快捷方法 - 显示警告消息
 */
export function showWarning(message: string, options?: Partial<ErrorHandlerOptions>): void {
  handleWorkflowError(
    { code: 'WARNING', message },
    { ...options, level: ErrorLevel.WARNING }
  );
}

/**
 * 🎯 快捷方法 - 显示错误消息
 */
export function showError(message: string, options?: Partial<ErrorHandlerOptions>): void {
  handleWorkflowError(
    { code: 'ERROR', message },
    { ...options, level: ErrorLevel.ERROR }
  );
}

/**
 * 🎯 快捷方法 - 显示信息消息
 */
export function showInfo(message: string, options?: Partial<ErrorHandlerOptions>): void {
  handleWorkflowError(
    { code: 'INFO', message },
    { ...options, level: ErrorLevel.INFO }
  );
}

/**
 * 🎯 确认对话框
 */
export function showConfirm(
  title: string,
  content: string,
  onOk?: () => void,
  onCancel?: () => void
): void {
  Modal.confirm({
    title,
    content,
    okText: '确定',
    cancelText: '取消',
    onOk,
    onCancel,
  });
}

/**
 * 🎯 异步操作包装器
 * 自动处理异步操作的错误
 */
export async function withErrorHandler<T>(
  asyncFn: () => Promise<T>,
  options?: ErrorHandlerOptions
): Promise<T | null> {
  try {
    const result = await asyncFn();
    return result;
  } catch (error) {
    handleWorkflowError(error, options);
    return null;
  }
}

/**
 * 🎯 验证数据完整性
 */
export function validateRequiredFields(
  data: Record<string, any>,
  requiredFields: string[]
): { valid: boolean; message?: string } {
  const missingFields: string[] = [];
  
  for (const field of requiredFields) {
    const value = data[field];
    if (value === null || value === undefined || value === '') {
      missingFields.push(field);
    }
  }
  
  if (missingFields.length > 0) {
    return {
      valid: false,
      message: `以下必填字段未填写：${missingFields.join('、')}`
    };
  }
  
  return { valid: true };
}

/**
 * 🎯 网络状态检查
 */
export function checkNetworkStatus(): boolean {
  return navigator.onLine;
}

/**
 * 🎯 重试机制包装器
 */
export async function withRetry<T>(
  asyncFn: () => Promise<T>,
  maxRetries: number = 3,
  delayMs: number = 1000
): Promise<T> {
  let lastError: any;
  
  for (let i = 0; i <= maxRetries; i++) {
    try {
      return await asyncFn();
    } catch (error) {
      lastError = error;
      
      if (i === maxRetries) {
        throw error;
      }
      
      // 延迟后重试
      await new Promise(resolve => setTimeout(resolve, delayMs * (i + 1)));
    }
  }
  
  throw lastError;
}