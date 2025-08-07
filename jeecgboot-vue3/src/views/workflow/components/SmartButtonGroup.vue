<template>
  <div class="simple-workflow-buttons">
    <!-- 基于现有JeecgBoot按钮系统的简单扩展 -->
    <a-space :size="size" :wrap="wrap">
      <a-button
        v-for="button in workflowButtons"
        :key="button.code"
        :type="button.type"
        :loading="button.loading"
        @click="handleButtonClick(button)"
      >
        <template #icon v-if="button.icon">
          <component :is="button.icon" />
        </template>
        {{ button.name }}
      </a-button>
    </a-space>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { message } from 'ant-design-vue';
import { 
  SaveOutlined,
  SendOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined
} from '@ant-design/icons-vue';

// 定义组件属性 - 简化
interface Props {
  formId: string;
  dataId?: string;
  taskId?: string;
  size?: 'small' | 'middle' | 'large';
  wrap?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  size: 'middle',
  wrap: true
});

// 定义事件 - 简化
const emit = defineEmits<{
  save: [];
  submit: [];
  approve: [];
  reject: [];
}>();

// 简单的工作流按钮配置（基于现有JeecgBoot按钮系统）
const workflowButtons = ref([
  {
    code: 'save_draft',
    name: '保存草稿',
    type: 'default',
    icon: SaveOutlined,
    loading: false,
    action: 'save'
  },
  {
    code: 'submit_review',
    name: '提交审核',
    type: 'primary',
    icon: SendOutlined,
    loading: false,
    action: 'submit'
  },
  {
    code: 'approve',
    name: '审核通过',
    type: 'primary',
    icon: CheckCircleOutlined,
    loading: false,
    action: 'approve'
  },
  {
    code: 'reject',
    name: '审核拒绝',
    type: 'danger',
    icon: CloseCircleOutlined,
    loading: false,
    action: 'reject'
  }
]);

/**
 * 处理按钮点击 - 简化版
 */
function handleButtonClick(button: any) {
  // 简单的事件分发，基于现有JeecgBoot模式
  button.loading = true;
  
  try {
    emit(button.action as any);
    message.success(`${button.name}操作成功`);
  } catch (error) {
    console.error('按钮操作失败:', error);
    message.error(`${button.name}操作失败`);
  } finally {
    setTimeout(() => {
      button.loading = false;
    }, 1000);
  }
}

/**
 * 处理按钮点击
 */
async function handleButtonClick(button: any) {
  
  // 确认消息
  if (button.confirmMessage) {
    const confirmed = await showConfirm(button.confirmMessage);
    if (!confirmed) return;
  }
  
  // 设置加载状态
  button.loading = true;
  
  try {
    // 发出事件
    emit('buttonClick', button, button.actionType);
    
    // 根据动作类型发出具体事件
    switch (button.actionType) {
      case 'SAVE':
      case 'SAVE_DRAFT':
        emit('save', button.actionParams);
        break;
      case 'SUBMIT':
      case 'SUBMIT_REVIEW':
        emit('submit', button.actionParams);
        break;
      case 'APPROVE':
        emit('approve', button.actionParams);
        break;
      case 'REJECT':
        emit('reject', button.actionParams);
        break;
      case 'TRANSFER':
        emit('transfer', button.actionParams);
        break;
      case 'GO_BACK':
        emit('goBack', button.actionParams);
        break;
      case 'DELETE':
        emit('delete', button.actionParams);
        break;
      case 'CANCEL':
        emit('cancel');
        break;
      default:
        console.warn('未知的按钮动作类型:', button.actionType);
    }
    
    // 成功消息
    if (button.successMessage) {
      message.success(button.successMessage);
    }
    
  } catch (error) {
    console.error('按钮操作失败:', error);
    message.error('操作失败，请重试');
  } finally {
    button.loading = false;
  }
}

/**
 * 显示确认对话框
 */
function showConfirm(content: string): Promise<boolean> {
  return new Promise((resolve) => {
    Modal.confirm({
      title: '确认操作',
      content,
      okText: '确定',
      cancelText: '取消',
      onOk() {
        resolve(true);
      },
      onCancel() {
        resolve(false);
      }
    });
  });
}

/**
 * 获取默认按钮（兜底方案）
 */
function getDefaultButtons() {
  const defaultButtons = [];
  
  // 保存按钮
  if (!props.taskId) {
    defaultButtons.push({
      id: 'save',
      text: '保存',
      type: 'default',
      icon: 'SaveOutlined',
      actionType: 'SAVE',
      sortOrder: 10,
      enabled: true,
      loading: false
    });
  }
  
  // 提交按钮
  defaultButtons.push({
    id: 'submit',
    text: props.taskId ? '提交' : '提交审核',
    type: 'primary',
    icon: 'SendOutlined',
    actionType: props.taskId ? 'SUBMIT' : 'SUBMIT_REVIEW',
    sortOrder: 20,
    enabled: true,
    loading: false
  });
  
  return defaultButtons;
}

/**
 * 刷新按钮配置
 */
function refreshButtons() {
  loadButtons();
}

/**
 * 设置按钮加载状态
 */
function setButtonLoading(buttonId: string, loading: boolean) {
  const button = buttons.value.find(btn => btn.id === buttonId);
  if (button) {
    button.loading = loading;
  }
}

/**
 * 设置按钮启用状态
 */
function setButtonEnabled(buttonId: string, enabled: boolean) {
  const button = buttons.value.find(btn => btn.id === buttonId);
  if (button) {
    button.enabled = enabled;
  }
}

// 暴露方法给父组件
defineExpose({
  refreshButtons,
  setButtonLoading,
  setButtonEnabled
});
</script>

<style lang="less" scoped>
.smart-button-group {
  .ant-btn {
    min-width: 80px;
    
    &:not(:last-child) {
      margin-right: 8px;
    }
    
    // 按钮类型样式
    &.ant-btn-primary {
      box-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
    }
    
    &.ant-btn-success {
      background: #52c41a;
      border-color: #52c41a;
      color: white;
      
      &:hover {
        background: #73d13d;
        border-color: #73d13d;
      }
    }
    
    &.ant-btn-warning {
      background: #faad14;
      border-color: #faad14;
      color: white;
      
      &:hover {
        background: #ffc53d;
        border-color: #ffc53d;
      }
    }
  }
}

// 响应式布局
@media (max-width: 768px) {
  .smart-button-group {
    .ant-btn {
      min-width: 70px;
      font-size: 12px;
    }
  }
}
</style>