<template>
  <view class="workflow-mobile-form">
    <!-- 🎯 复用原有online-loader，扩展工作流功能 -->
    <online-loader
      ref="onlineLoaderRef"
      :table="table"
      :dataId="dataId"
      :taskId="taskId"
      :edit="edit"
      :flowEdit="flowEdit"
      :disabled="readonly"
      :onlyBackData="true"
      @success="handleFormSuccess"
    />
    
    <!-- 🎯 移动端工作流操作按钮区域 -->
    <view v-if="showActions" class="workflow-actions">
      <!-- 处理意见输入框 -->
      <view v-if="needComment" class="comment-section">
        <view class="comment-title">处理意见</view>
        <textarea
          v-model="comment"
          placeholder="请输入处理意见..."
          maxlength="500"
          :disabled="readonly"
          class="comment-textarea"
        />
        <view class="comment-count">{{ comment.length }}/500</view>
      </view>
      
      <!-- 按钮组 -->
      <view class="action-buttons" :class="{ 'sticky-buttons': stickyButtons }">
        <!-- 基础操作按钮 -->
        <view v-if="showBaseActions" class="base-buttons">
          <!-- 保存草稿 -->
          <button 
            v-if="allowSave" 
            class="btn btn-default"
            :loading="saving"
            :disabled="readonly"
            @click="handleSave"
          >
            保存草稿
          </button>
          
          <!-- 提交按钮 -->
          <button 
            v-if="allowSubmit"
            class="btn btn-primary"
            :loading="submitting"
            :disabled="readonly"
            @click="handleSubmit"
          >
            {{ submitText }}
          </button>
          
          <!-- 启动工作流 -->
          <button 
            v-if="allowStartWorkflow"
            class="btn btn-success"
            :loading="startingWorkflow"
            :disabled="readonly"
            @click="handleStartWorkflow"
          >
            启动工作流
          </button>
        </view>
        
        <!-- 工作流操作按钮 -->
        <view v-if="showWorkflowActions && workflowButtons.length > 0" class="workflow-buttons">
          <button 
            v-for="button in visibleButtons" 
            :key="button.code"
            :class="getButtonClass(button)"
            :loading="button.loading"
            :disabled="!button.enabled || readonly"
            @click="handleWorkflowAction(button)"
          >
            {{ button.label }}
          </button>
          
          <!-- 更多操作 -->
          <view v-if="hiddenButtons.length > 0" class="more-actions">
            <button class="btn btn-default" @click="showMoreActions = true">
              更多操作 ({{ hiddenButtons.length }})
            </button>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 🎯 更多操作弹出层 -->
    <uni-popup ref="moreActionPopup" type="bottom">
      <view class="more-action-popup">
        <view class="popup-header">
          <view class="popup-title">更多操作</view>
          <view class="popup-close" @click="showMoreActions = false">✕</view>
        </view>
        <view class="popup-content">
          <button 
            v-for="button in hiddenButtons" 
            :key="button.code"
            :class="getButtonClass(button)"
            :loading="button.loading"
            :disabled="!button.enabled || readonly"
            @click="handleWorkflowAction(button)"
          >
            {{ button.label }}
          </button>
        </view>
      </view>
    </uni-popup>
    
    <!-- 🎯 确认对话框 -->
    <uni-popup ref="confirmPopup" type="dialog">
      <uni-popup-dialog
        :title="confirmDialog.title"
        :content="confirmDialog.content"
        :before-close="true"
        @close="handleConfirmClose"
        @confirm="handleConfirmOk"
      />
    </uni-popup>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, watch, nextTick } from 'vue';
import onlineLoader from '../online/online-loader.vue';
import { useToast } from '@/hooks/useToast';
import { http } from '@/utils/http';

// 组件名称
defineOptions({ name: 'WorkflowMobileForm' });

// 工作流按钮接口（移动端适配版）
export interface MobileWorkflowButton {
  code: string;
  label: string;
  type?: 'primary' | 'success' | 'warning' | 'danger' | 'default';
  enabled?: boolean;
  visible?: boolean;
  loading?: boolean;
  requireComment?: boolean;
  confirmMessage?: string;
}

// Props定义
interface Props {
  table: string;                           // 表名
  dataId?: string;                        // 数据ID
  taskId?: string;                        // 任务ID  
  edit?: boolean;                         // 编辑模式
  flowEdit?: boolean;                     // 流程编辑模式
  readonly?: boolean;                     // 只读模式
  
  // 按钮控制
  allowSave?: boolean;                    // 允许保存
  allowSubmit?: boolean;                  // 允许提交
  allowStartWorkflow?: boolean;           // 允许启动工作流
  submitText?: string;                    // 提交按钮文本
  
  // 工作流相关
  workflowButtons?: MobileWorkflowButton[]; // 工作流按钮
  needComment?: boolean;                  // 需要意见
  
  // 显示控制
  showActions?: boolean;                  // 显示操作区域
  showBaseActions?: boolean;              // 显示基础操作
  showWorkflowActions?: boolean;          // 显示工作流操作
  maxVisibleButtons?: number;             // 最大可见按钮数
  stickyButtons?: boolean;                // 是否使用粘性按钮布局
}

const props = withDefaults(defineProps<Props>(), {
  edit: false,
  flowEdit: false,
  readonly: false,
  allowSave: true,
  allowSubmit: true,
  allowStartWorkflow: false,
  submitText: '提交',
  workflowButtons: () => [],
  needComment: false,
  showActions: true,
  showBaseActions: true,
  showWorkflowActions: true,
  maxVisibleButtons: 3,
  stickyButtons: true
});

// 事件定义
const emit = defineEmits<{
  save: [data: any];
  submit: [data: any];
  startWorkflow: [data: any];
  workflowAction: [button: MobileWorkflowButton, comment: string, data: any];
  success: [data: any];
}>();

// 组合式API
const toast = useToast();

// 响应式数据
const onlineLoaderRef = ref();
const moreActionPopup = ref();
const confirmPopup = ref();

const formData = ref({});
const comment = ref('');
const saving = ref(false);
const submitting = ref(false);
const startingWorkflow = ref(false);
const showMoreActions = ref(false);

const confirmDialog = ref({
  title: '',
  content: '',
  onConfirm: null as (() => void) | null
});

// 计算属性
const visibleButtons = computed(() => {
  return props.workflowButtons
    .filter(btn => btn.visible !== false)
    .slice(0, props.maxVisibleButtons);
});

const hiddenButtons = computed(() => {
  return props.workflowButtons
    .filter(btn => btn.visible !== false)
    .slice(props.maxVisibleButtons);
});

// 监听更多操作显示状态
watch(showMoreActions, (show) => {
  if (show) {
    moreActionPopup.value?.open();
  } else {
    moreActionPopup.value?.close();
  }
});

// 方法
const getButtonClass = (button: MobileWorkflowButton): string => {
  const baseClass = 'btn';
  const typeClass = button.type ? `btn-${button.type}` : 'btn-default';
  return `${baseClass} ${typeClass}`;
};

const getFormData = async (): Promise<any> => {
  // 获取表单数据，模拟online-loader的数据获取
  if (onlineLoaderRef.value) {
    return formData.value;
  }
  return {};
};

const handleFormSuccess = (data: any) => {
  console.log('表单数据获取成功:', data);
  formData.value = data;
};

const handleSave = async () => {
  if (saving.value) return;
  
  try {
    saving.value = true;
    const data = await getFormData();
    
    // 调用移动端保存API
    const result = await saveDraftMobile(props.table, props.dataId, data);
    
    toast.success('保存成功');
    emit('save', result);
    
  } catch (error) {
    console.error('保存失败:', error);
    toast.error('保存失败，请重试');
  } finally {
    saving.value = false;
  }
};

const handleSubmit = async () => {
  if (submitting.value) return;
  
  try {
    submitting.value = true;
    const data = await getFormData();
    
    // 调用移动端提交API  
    const result = await submitFormMobile(props.table, props.dataId, data);
    
    toast.success('提交成功');
    emit('submit', result);
    
  } catch (error) {
    console.error('提交失败:', error);
    toast.error('提交失败，请重试');
  } finally {
    submitting.value = false;
  }
};

const handleStartWorkflow = async () => {
  if (startingWorkflow.value) return;
  
  try {
    startingWorkflow.value = true;
    const data = await getFormData();
    
    // 调用移动端启动工作流API
    const result = await startWorkflowMobile(props.table, props.dataId);
    
    toast.success('工作流启动成功');
    emit('startWorkflow', result);
    
  } catch (error) {
    console.error('启动工作流失败:', error);
    toast.error('启动工作流失败，请重试');
  } finally {
    startingWorkflow.value = false;
  }
};

const handleWorkflowAction = async (button: MobileWorkflowButton) => {
  try {
    // 检查是否需要确认
    if (button.confirmMessage) {
      showConfirmDialog(button.confirmMessage, () => {
        executeWorkflowAction(button);
      });
      return;
    }
    
    // 检查是否需要处理意见
    if (button.requireComment && props.needComment) {
      if (!comment.value || comment.value.trim() === '') {
        toast.error('请输入处理意见');
        return;
      }
    }
    
    await executeWorkflowAction(button);
    
  } catch (error) {
    console.error(`工作流动作 ${button.code} 执行失败:`, error);
    toast.error(`${button.label}失败，请重试`);
  }
};

const executeWorkflowAction = async (button: MobileWorkflowButton) => {
  button.loading = true;
  
  try {
    const data = await getFormData();
    
    // 调用移动端工作流动作API
    const result = await executeWorkflowActionMobile(
      button.code,
      props.taskId,
      comment.value,
      data
    );
    
    toast.success(`${button.label}成功`);
    emit('workflowAction', button, comment.value, result);
    
    // 清空意见
    if (button.requireComment) {
      comment.value = '';
    }
    
    // 关闭更多操作弹出层
    showMoreActions.value = false;
    
  } finally {
    button.loading = false;
  }
};

const showConfirmDialog = (content: string, onConfirm: () => void) => {
  confirmDialog.value = {
    title: '确认操作',
    content: content,
    onConfirm: onConfirm
  };
  confirmPopup.value?.open();
};

const handleConfirmClose = () => {
  confirmDialog.value.onConfirm = null;
  confirmPopup.value?.close();
};

const handleConfirmOk = () => {
  if (confirmDialog.value.onConfirm) {
    confirmDialog.value.onConfirm();
  }
  handleConfirmClose();
};

// 🎯 移动端API方法（复用现有的移动端HTTP工具）
const saveDraftMobile = async (tableName: string, dataId: string | undefined, formData: any) => {
  return await http.post('/workflow/mobile/form/save-draft', {
    tableName,
    dataId,
    formData
  });
};

const submitFormMobile = async (tableName: string, dataId: string | undefined, formData: any) => {
  return await http.post('/workflow/mobile/form/submit', {
    tableName,
    dataId,
    formData
  });
};

const startWorkflowMobile = async (tableName: string, dataId: string | undefined) => {
  return await http.post('/workflow/mobile/form/start-workflow', {
    tableName,
    dataId
  });
};

const executeWorkflowActionMobile = async (
  actionCode: string,
  taskId: string | undefined,
  comment: string,
  formData: any
) => {
  return await http.post('/workflow/mobile/task/action', {
    actionCode,
    taskId,
    comment,
    formData
  });
};

// 暴露方法给父组件
defineExpose({
  getFormData,
  setComment: (text: string) => comment.value = text,
  getComment: () => comment.value,
  clearComment: () => comment.value = ''
});
</script>

<style lang="scss" scoped>
.workflow-mobile-form {
  width: 100%;
}

.workflow-actions {
  margin-top: 20px;
  padding: 15px;
  background: #fff;
  border-radius: 8px;
}

.comment-section {
  margin-bottom: 20px;
  
  .comment-title {
    font-size: 16px;
    font-weight: 600;
    color: #333;
    margin-bottom: 10px;
  }
  
  .comment-textarea {
    width: 100%;
    min-height: 80px;
    padding: 10px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    font-size: 14px;
    line-height: 1.5;
    resize: vertical;
    box-sizing: border-box;
    
    &:focus {
      border-color: #409eff;
      outline: none;
    }
    
    &:disabled {
      background-color: #f5f7fa;
      color: #c0c4cc;
    }
  }
  
  .comment-count {
    text-align: right;
    font-size: 12px;
    color: #909399;
    margin-top: 5px;
  }
}

.action-buttons {
  .base-buttons,
  .workflow-buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-bottom: 15px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .btn {
    flex: 1;
    min-width: 80px;
    height: 40px;
    border: none;
    border-radius: 6px;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.3s ease;
    
    &:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }
    
    &.btn-default {
      background: #f5f7fa;
      color: #606266;
      
      &:not(:disabled):active {
        background: #e4e7ed;
      }
    }
    
    &.btn-primary {
      background: #409eff;
      color: white;
      
      &:not(:disabled):active {
        background: #337ecc;
      }
    }
    
    &.btn-success {
      background: #67c23a;
      color: white;
      
      &:not(:disabled):active {
        background: #529b2e;
      }
    }
    
    &.btn-warning {
      background: #e6a23c;
      color: white;
      
      &:not(:disabled):active {
        background: #b8851a;
      }
    }
    
    &.btn-danger {
      background: #f56c6c;
      color: white;
      
      &:not(:disabled):active {
        background: #d03050;
      }
    }
  }
  
  .more-actions {
    width: 100%;
    margin-top: 10px;
  }
}

.more-action-popup {
  background: white;
  border-radius: 12px 12px 0 0;
  max-height: 60vh;
  
  .popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 20px;
    border-bottom: 1px solid #ebeef5;
    
    .popup-title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
    
    .popup-close {
      font-size: 20px;
      color: #909399;
      cursor: pointer;
      
      &:active {
        color: #606266;
      }
    }
  }
  
  .popup-content {
    padding: 20px;
    
    .btn {
      width: 100%;
      margin-bottom: 12px;
      
      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}

// Sticky按钮支持
.action-buttons.sticky-buttons {
  position: sticky;
  bottom: 0;
  background: white;
  padding: 15px;
  border-top: 1px solid #e4e7ed;
  margin: 0 -15px;
  z-index: 100;
  border-radius: 12px 12px 0 0;
  box-shadow: 0 -2px 12px rgba(0, 0, 0, 0.1);
}

// 响应式设计
@media (max-width: 750px) {
  .workflow-mobile-form {
    padding: 0;
  }
  
  .action-buttons {
    padding: 12px;
    
    .base-buttons,
    .workflow-buttons {
      flex-direction: column;
      gap: 12px;
      
      .btn {
        width: 100%;
        min-width: auto;
        height: 48px; // 增加触摸友好的高度
        font-size: 16px; // 适合移动设备的字体
      }
    }
  }
  
  .comment-section {
    padding: 12px;
    
    .comment-textarea {
      font-size: 16px; // 防止iOS缩放
      min-height: 100px;
      line-height: 1.5;
    }
    
    .comment-title {
      font-size: 14px;
      font-weight: 600;
      margin-bottom: 8px;
      color: #303133;
    }
    
    .comment-count {
      font-size: 12px;
      color: #909399;
      text-align: right;
      margin-top: 4px;
    }
  }
}
</style>