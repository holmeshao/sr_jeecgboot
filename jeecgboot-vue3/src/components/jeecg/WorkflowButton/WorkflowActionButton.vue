<template>
  <div class="workflow-action-buttons">
    <!-- 基础操作按钮组 -->
    <a-space :size="12" v-if="showBaseActions">
      <!-- 保存草稿按钮 -->
      <a-button 
        v-if="allowSave"
        :loading="saving"
        @click="handleSave"
        :disabled="readonly"
      >
        <template #icon>
          <Icon icon="ant-design:save-outlined" />
        </template>
        保存草稿
      </a-button>
      
      <!-- 提交按钮 -->
      <a-button 
        v-if="allowSubmit"
        type="primary"
        :loading="submitting"
        @click="handleSubmit"
        :disabled="readonly"
      >
        <template #icon>
          <Icon icon="ant-design:send-outlined" />
        </template>
        {{ submitText }}
      </a-button>
      
      <!-- 启动工作流按钮 -->
      <a-button 
        v-if="allowStartWorkflow"
        type="primary"
        ghost
        :loading="startingWorkflow"
        @click="handleStartWorkflow"
        :disabled="readonly"
      >
        <template #icon>
          <Icon icon="ant-design:play-circle-outlined" />
        </template>
        启动工作流
      </a-button>
    </a-space>
    
    <!-- 工作流操作按钮组 -->
    <a-space :size="12" v-if="showWorkflowActions && workflowButtons.length > 0">
      <a-divider v-if="showBaseActions" type="vertical" />
      
      <!-- 动态工作流按钮 -->
      <template v-for="button in visibleWorkflowButtons" :key="button.code">
        <!-- 普通按钮 -->
        <a-button
          v-if="!button.popConfirm"
          :type="getButtonType(button)"
          :loading="button.loading"
          :disabled="!button.enabled || readonly"
          @click="handleWorkflowAction(button)"
          :class="button.className"
        >
          <template #icon v-if="button.icon">
            <Icon :icon="button.icon" />
          </template>
          {{ button.label }}
        </a-button>
        
        <!-- 确认按钮（需要确认的操作） -->
        <a-popconfirm
          v-else
          :title="button.popConfirm.title"
          :ok-text="button.popConfirm.okText || '确定'"
          :cancel-text="button.popConfirm.cancelText || '取消'"
          @confirm="handleWorkflowAction(button)"
        >
          <a-button
            :type="getButtonType(button)"
            :loading="button.loading"
            :disabled="!button.enabled || readonly"
            :class="button.className"
          >
            <template #icon v-if="button.icon">
              <Icon :icon="button.icon" />
            </template>
            {{ button.label }}
          </a-button>
        </a-popconfirm>
      </template>
      
      <!-- 更多操作下拉菜单 -->
      <a-dropdown v-if="hiddenWorkflowButtons.length > 0">
        <template #overlay>
          <a-menu>
            <a-menu-item 
              v-for="button in hiddenWorkflowButtons" 
              :key="button.code"
              :disabled="!button.enabled || readonly"
              @click="handleWorkflowAction(button)"
            >
              <Icon :icon="button.icon" v-if="button.icon" />
              {{ button.label }}
            </a-menu-item>
          </a-menu>
        </template>
        <a-button>
          更多
          <Icon icon="ant-design:down-outlined" />
        </a-button>
      </a-dropdown>
    </a-space>
    
    <!-- 任务处理意见输入框 -->
    <div v-if="needComment && showCommentInput" class="workflow-comment">
      <a-divider />
      <div class="comment-section">
        <h4>处理意见</h4>
        <a-textarea
          v-model:value="comment"
          :rows="3"
          :maxlength="500"
          show-count
          placeholder="请输入处理意见..."
          :disabled="readonly"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { Icon } from '/@/components/Icon';
import { useMessage } from '/@/hooks/web/useMessage';
import { usePermission } from '/@/hooks/web/usePermission';
import { handleWorkflowError, showSuccess, showError } from '/@/utils/workflow/errorHandler';
import type { WorkflowButton } from '/@/utils/workflow/buttonManager';

// 组件名称
defineOptions({ name: 'WorkflowActionButton' });

// Props定义
interface Props {
  // 基础操作控制
  allowSave?: boolean;             // 是否显示保存按钮
  allowSubmit?: boolean;           // 是否显示提交按钮
  allowStartWorkflow?: boolean;    // 是否显示启动工作流按钮
  
  // 状态控制
  saving?: boolean;                // 保存状态
  submitting?: boolean;            // 提交状态
  startingWorkflow?: boolean;      // 启动工作流状态
  readonly?: boolean;              // 只读模式
  
  // 文本配置
  submitText?: string;             // 提交按钮文本
  
  // 工作流按钮配置
  workflowButtons?: WorkflowButton[];  // 工作流操作按钮列表
  needComment?: boolean;           // 是否需要处理意见
  showCommentInput?: boolean;      // 是否显示意见输入框
  
  // 显示控制
  showBaseActions?: boolean;       // 是否显示基础操作
  showWorkflowActions?: boolean;   // 是否显示工作流操作
  maxVisibleButtons?: number;      // 最大可见按钮数量
}

const props = withDefaults(defineProps<Props>(), {
  allowSave: true,
  allowSubmit: true,
  allowStartWorkflow: false,
  saving: false,
  submitting: false,
  startingWorkflow: false,
  readonly: false,
  submitText: '提交',
  workflowButtons: () => [],
  needComment: false,
  showCommentInput: true,
  showBaseActions: true,
  showWorkflowActions: true,
  maxVisibleButtons: 4
});

// 事件定义
const emit = defineEmits<{
  save: [];
  submit: [];
  startWorkflow: [];
  workflowAction: [button: WorkflowButton, comment?: string];
}>();

const { createMessage } = useMessage();
const { hasPermission } = usePermission();

// 响应式数据
const comment = ref('');

// 计算属性
const visibleWorkflowButtons = computed(() => {
  return props.workflowButtons
    .filter(button => 
      button.visible !== false && 
      (!button.auth || hasPermission(button.auth))
    )
    .slice(0, props.maxVisibleButtons);
});

const hiddenWorkflowButtons = computed(() => {
  return props.workflowButtons
    .filter(button => 
      button.visible !== false && 
      (!button.auth || hasPermission(button.auth))
    )
    .slice(props.maxVisibleButtons);
});

// 方法
const getButtonType = (button: WorkflowButton): string => {
  return button.type || 'default';
};

const handleSave = async () => {
  try {
    emit('save');
  } catch (error) {
    handleWorkflowError(error, { title: '保存失败' });
  }
};

const handleSubmit = async () => {
  try {
    emit('submit');
  } catch (error) {
    handleWorkflowError(error, { title: '提交失败' });
  }
};

const handleStartWorkflow = async () => {
  try {
    emit('startWorkflow');
  } catch (error) {
    handleWorkflowError(error, { title: '启动工作流失败' });
  }
};

const handleWorkflowAction = async (button: WorkflowButton) => {
  try {
    // 检查是否需要处理意见
    if (button.requireComment && props.needComment) {
      if (!comment.value || comment.value.trim() === '') {
        showError('请输入处理意见');
        return;
      }
    }
    
    // 设置按钮加载状态
    button.loading = true;
    
    // 执行按钮点击事件
    if (button.onClick) {
      await button.onClick(comment.value);
    }
    
    // 触发父组件事件
    emit('workflowAction', button, comment.value);
    
    showSuccess(`${button.label}成功`);
    
    // 清空意见
    if (button.requireComment) {
      comment.value = '';
    }
    
  } catch (error) {
    handleWorkflowError(error, { title: `${button.label}失败` });
  } finally {
    button.loading = false;
  }
};

// 监听需要意见的按钮，自动显示意见输入框
watch(() => props.workflowButtons, (newButtons) => {
  const hasRequireComment = newButtons.some(btn => btn.requireComment);
  if (hasRequireComment && props.needComment) {
    // 可以在这里处理一些逻辑，比如自动聚焦到意见输入框
  }
}, { deep: true });

// 暴露方法
defineExpose({
  getComment: () => comment.value,
  clearComment: () => comment.value = '',
  setComment: (text: string) => comment.value = text,
});
</script>

<style lang="less" scoped>
.workflow-action-buttons {
  .workflow-comment {
    margin-top: 16px;
    
    .comment-section {
      h4 {
        margin-bottom: 8px;
        font-size: 14px;
        font-weight: 600;
        color: #333;
      }
    }
  }
  
  // 按钮样式优化
  .ant-btn {
    &.ant-btn-primary {
      &.ant-btn-background-ghost {
        border-color: #1890ff;
        color: #1890ff;
        
        &:hover {
          background-color: #1890ff;
          color: white;
        }
      }
    }
  }
  
  // 响应式设计
  @media (max-width: 768px) {
    padding: 12px 8px;
    
    .ant-space {
      flex-wrap: wrap;
      width: 100%;
    }
    
    .ant-btn {
      width: 100%;
      height: 44px; // 增加触摸友好的高度
      font-size: 16px; // 适合移动端的字体大小
      margin-bottom: 8px;
    }
    
    .workflow-comment {
      margin-top: 12px;
      
      .ant-input {
        font-size: 16px; // 防止iOS自动缩放
        min-height: 44px;
      }
      
      .ant-input::placeholder {
        font-size: 14px;
      }
    }
    
    // 优化下拉菜单
    .ant-dropdown-trigger {
      width: 100%;
    }
  }
}
</style>