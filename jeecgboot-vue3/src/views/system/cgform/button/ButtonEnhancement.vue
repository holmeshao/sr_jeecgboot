<template>
  <div class="button-enhancement">
    <!-- 工作流按钮配置增强组件 -->
    <a-card title="工作流按钮配置" size="small" v-if="isWorkflowButton">
      <a-form layout="vertical">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="工作流动作">
              <a-select v-model:value="workflowAction" placeholder="选择工作流动作">
                <a-select-option value="SAVE_DRAFT">保存草稿</a-select-option>
                <a-select-option value="SUBMIT_REVIEW">提交审核</a-select-option>
                <a-select-option value="APPROVE">审核通过</a-select-option>
                <a-select-option value="REJECT">审核拒绝</a-select-option>
                <a-select-option value="TRANSFER">转办</a-select-option>
                <a-select-option value="GO_BACK">退回</a-select-option>
                <a-select-option value="DELEGATE">委派</a-select-option>
                <a-select-option value="SUSPEND">挂起</a-select-option>
                <a-select-option value="REVOKE">撤回</a-select-option>
                <a-select-option value="START_WORKFLOW">启动流程</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="按钮权限">
              <a-input 
                v-model:value="buttonPermissions" 
                placeholder="如：workflow:approve"
              />
            </a-form-item>
          </a-col>
        </a-row>
        
        <a-form-item label="显示条件">
          <a-textarea 
            v-model:value="showCondition" 
            :rows="2"
            placeholder="如：status == 'DRAFT' && hasPermission('submit')"
          />
          <div class="help-text">
            <a-typography-text type="secondary" style="font-size: 12px;">
              支持变量：status(状态)、hasPermission('xxx')(权限)、currentTask(当前任务)
            </a-typography-text>
          </div>
        </a-form-item>
        
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="确认消息">
              <a-input 
                v-model:value="confirmMessage" 
                placeholder="点击按钮时的确认提示"
              />
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="成功消息">
              <a-input 
                v-model:value="successMessage" 
                placeholder="操作成功后的提示"
              />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-card>

    <!-- 按钮预览 -->
    <a-card title="按钮预览" size="small" style="margin-top: 16px;">
      <a-space>
        <a-button 
          :type="getButtonType(buttonStyle)"
          :disabled="buttonStatus !== '1'"
        >
          <template #icon v-if="buttonIcon">
            <component :is="buttonIcon" />
          </template>
          {{ buttonName || '按钮名称' }}
        </a-button>
        
        <a-tag v-if="workflowAction" color="blue">
          {{ getActionText(workflowAction) }}
        </a-tag>
        
        <a-tag v-if="buttonPermissions" color="orange">
          权限: {{ buttonPermissions }}
        </a-tag>
      </a-space>
      
      <div v-if="showCondition" class="condition-preview">
        <a-typography-text code style="font-size: 11px;">
          显示条件: {{ showCondition }}
        </a-typography-text>
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { 
  CheckCircleOutlined,
  CloseCircleOutlined,
  SaveOutlined,
  SendOutlined 
} from '@ant-design/icons-vue';

// 定义组件属性
interface Props {
  buttonCode?: string;
  buttonName?: string;
  buttonIcon?: string;
  buttonStyle?: string;
  buttonStatus?: string;
  workflowAction?: string;
  showCondition?: string;
  confirmMessage?: string;
  successMessage?: string;
  buttonPermissions?: string;
}

const props = withDefaults(defineProps<Props>(), {
  buttonStatus: '1'
});

const emit = defineEmits(['update:workflowAction', 'update:showCondition', 'update:confirmMessage', 'update:successMessage', 'update:buttonPermissions']);

// 响应式数据
const workflowAction = ref(props.workflowAction);
const showCondition = ref(props.showCondition);
const confirmMessage = ref(props.confirmMessage);
const successMessage = ref(props.successMessage);
const buttonPermissions = ref(props.buttonPermissions);

// 计算属性
const isWorkflowButton = computed(() => {
  const workflowButtons = [
    'save_draft', 'submit_review', 'approve', 'reject', 'transfer', 
    'go_back', 'delegate', 'suspend', 'revoke', 'bpm', 'emergency_approve'
  ];
  return workflowButtons.includes(props.buttonCode || '');
});

// 监听变化并向上传递
watch(workflowAction, (val) => emit('update:workflowAction', val));
watch(showCondition, (val) => emit('update:showCondition', val));
watch(confirmMessage, (val) => emit('update:confirmMessage', val));
watch(successMessage, (val) => emit('update:successMessage', val));
watch(buttonPermissions, (val) => emit('update:buttonPermissions', val));

/**
 * 获取按钮类型
 */
function getButtonType(style: string): string {
  if (style === 'button') return 'primary';
  if (style === 'link') return 'link';
  return 'default';
}

/**
 * 获取动作文本
 */
function getActionText(action: string): string {
  const actionMap = {
    'SAVE_DRAFT': '保存草稿',
    'SUBMIT_REVIEW': '提交审核',
    'APPROVE': '审核通过',
    'REJECT': '审核拒绝',
    'TRANSFER': '转办',
    'GO_BACK': '退回',
    'DELEGATE': '委派',
    'SUSPEND': '挂起',
    'REVOKE': '撤回',
    'START_WORKFLOW': '启动流程'
  };
  return actionMap[action] || action;
}
</script>

<style lang="less" scoped>
.button-enhancement {
  .help-text {
    margin-top: 4px;
  }
  
  .condition-preview {
    margin-top: 12px;
    padding: 8px;
    background: #f5f5f5;
    border-radius: 4px;
  }
}
</style>