<template>
  <div class="workflow-form-page">
    <!-- 顶部状态栏 -->
    <div class="status-header">
      <a-steps :current="currentStepIndex" size="small" style="flex: 1">
        <a-step v-for="step in processSteps" :key="step.id" :title="step.name" />
      </a-steps>
      <div class="current-info">
        <a-tag :color="getStatusColor(currentStatus)">{{ currentStatusText }}</a-tag>
        <span>当前处理人：{{ currentAssignee || '系统' }}</span>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <a-row :gutter="24">
      <!-- 左侧：表单内容（占主要空间） -->
      <a-col :span="18">
        <a-card title="工单详情" :bordered="false">
          <!-- 动态在线表单 - 根据角色和节点智能展示 -->
          <div class="online-form-container">
            <a-spin :spinning="loading">
              <!-- TODO: 集成JeecgBoot在线表单组件 -->
              <div class="form-placeholder">
                <a-form
                  :model="formData"
                  :disabled="displayMode.readonly"
                  layout="vertical"
                >
                  <a-row :gutter="16">
                    <a-col :span="12">
                      <a-form-item label="标题" required>
                        <a-input
                          v-model:value="formData.title"
                          placeholder="请输入标题"
                          :readonly="isFieldReadonly('title')"
                        />
                      </a-form-item>
                    </a-col>
                    <a-col :span="12">
                      <a-form-item label="优先级">
                        <a-select
                          v-model:value="formData.priority"
                          :disabled="isFieldReadonly('priority')"
                        >
                          <a-select-option value="1">低</a-select-option>
                          <a-select-option value="2">中</a-select-option>
                          <a-select-option value="3">高</a-select-option>
                        </a-select>
                      </a-form-item>
                    </a-col>
                  </a-row>
                  
                  <a-form-item label="描述">
                    <a-textarea
                      v-model:value="formData.description"
                      :rows="4"
                      placeholder="请输入描述"
                      :readonly="isFieldReadonly('description')"
                    />
                  </a-form-item>
                  
                  <!-- 流程字段 -->
                  <div v-if="showProcessFields" class="process-fields">
                    <a-divider>处理信息</a-divider>
                    <a-form-item label="处理意见">
                      <a-textarea
                        v-model:value="formData.processComment"
                        :rows="3"
                        placeholder="请填写处理意见..."
                        :readonly="isFieldReadonly('processComment')"
                      />
                    </a-form-item>
                  </div>
                </a-form>
              </div>
            </a-spin>
          </div>
          
          <!-- 当前节点操作区域 -->
          <template v-if="displayMode.hasCurrentTask">
            <a-divider />
            <div class="form-actions">
              <!-- 动态按钮组 -->
              <a-space size="large">
                <a-button
                  v-for="btn in currentNodeButtons"
                  :key="btn.id"
                  :type="btn.type"
                  :loading="btn.loading"
                  :disabled="btn.disabled"
                  @click="handleNodeAction(btn)"
                >
                  <template #icon v-if="btn.icon">
                    <component :is="btn.icon" />
                  </template>
                  {{ btn.text }}
                </a-button>
              </a-space>
            </div>
          </template>
        </a-card>
      </a-col>
      
      <!-- 右侧：流程信息侧栏 -->
      <a-col :span="6">
        <!-- 快速操作 -->
        <a-card title="快速操作" size="small" style="margin-bottom: 16px">
          <a-space direction="vertical" style="width: 100%">
            <a-button block @click="showProcessHistory">
              <template #icon><HistoryOutlined /></template>
              查看流程历史
            </a-button>
            <a-button block @click="showVersionHistory" v-if="versionControlEnabled">
              <template #icon><BranchesOutlined /></template>
              查看版本历史
            </a-button>
            <a-button block @click="exportForm">
              <template #icon><ExportOutlined /></template>
              导出表单
            </a-button>
          </a-space>
        </a-card>
        
        <!-- 流程进度 -->
        <a-card title="流程进度" size="small" style="margin-bottom: 16px">
          <div class="process-timeline">
            <a-timeline>
              <a-timeline-item 
                v-for="(item, index) in processHistory" 
                :key="index"
                :color="item.color"
              >
                <template #dot>
                  <component :is="item.icon" style="font-size: 16px" />
                </template>
                <div>
                  <div class="timeline-title">{{ item.name }}</div>
                  <div class="timeline-time">{{ item.time }}</div>
                  <div class="timeline-user">{{ item.user }}</div>
                </div>
              </a-timeline-item>
            </a-timeline>
          </div>
        </a-card>
        
        <!-- 工单信息 -->
        <a-card title="工单信息" size="small">
          <a-descriptions size="small" :column="1">
            <a-descriptions-item label="工单编号">{{ formBasicInfo.reportNo }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ formBasicInfo.createTime }}</a-descriptions-item>
            <a-descriptions-item label="创建人">{{ formBasicInfo.createBy }}</a-descriptions-item>
            <a-descriptions-item label="当前状态">
              <a-tag :color="getStatusColor(formBasicInfo.status)">
                {{ formBasicInfo.statusText }}
              </a-tag>
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>
    </a-row>
    
    <!-- 流程历史弹窗 -->
    <a-modal
      v-model:visible="historyVisible"
      title="流程历史"
      width="800px"
      :footer="null"
    >
      <WorkflowHistory :processInstanceId="processInstanceId" />
    </a-modal>
    
    <!-- 版本历史弹窗 -->
    <a-modal
      v-model:visible="versionVisible"
      title="版本历史"
      width="1000px"
      :footer="null"
    >
      <FormVersionHistory :processInstanceId="processInstanceId" />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { useMethods } from '/@/hooks/system/useMethods';
import dayjs from 'dayjs';
import { useRoute, useRouter } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import {
  HistoryOutlined,
  BranchesOutlined,
  ExportOutlined,
  CheckCircleOutlined,
  ClockCircleOutlined,
  ExclamationCircleOutlined
} from '@ant-design/icons-vue';
import WorkflowHistory from './WorkflowHistory.vue';
import FormVersionHistory from './FormVersionHistory.vue';
import { getDisplayMode, getNodePermission, submitForm, saveDraft } from '@/api/workflow';

// Props
interface Props {
  formId: string;
  dataId?: string;
  taskId?: string;
}

const props = withDefaults(defineProps<Props>(), {
  dataId: '',
  taskId: ''
});

// 响应式数据
const loading = ref(false);
const historyVisible = ref(false);
const versionVisible = ref(false);

// 表单数据
const formData = reactive({
  title: '',
  description: '',
  priority: '2',
  processComment: ''
});

// 显示模式
const displayMode = ref({
  mode: 'VIEW',
  hasCurrentTask: false,
  readonly: true,
  fieldPermissions: {}
});

// 表单基础信息
const formBasicInfo = ref({
  reportNo: '',
  createTime: '',
  createBy: '',
  status: '',
  statusText: ''
});

// 流程信息
const processInstanceId = ref('');
const currentNodeButtons = ref([]);
const processSteps = ref([]);
const currentStepIndex = ref(0);
const processHistory = ref([]);

// 计算属性
const currentStatus = computed(() => formBasicInfo.value.status);
const currentStatusText = computed(() => formBasicInfo.value.statusText);
const currentAssignee = computed(() => '当前用户');
const versionControlEnabled = computed(() => true);
const showProcessFields = computed(() => displayMode.value.hasCurrentTask);

// 方法
const getStatusColor = (status: string) => {
  const colorMap: Record<string, string> = {
    'DRAFT': 'default',
    'PROCESSING': 'processing',
    'COMPLETED': 'success',
    'REJECTED': 'error'
  };
  return colorMap[status] || 'default';
};

const isFieldReadonly = (fieldName: string) => {
  const permissions = displayMode.value.fieldPermissions;
  return displayMode.value.readonly || 
         (permissions.readonlyFields && permissions.readonlyFields.includes(fieldName));
};

const handleNodeAction = async (button: any) => {
  try {
    button.loading = true;
    
    if (button.confirmMessage) {
      const confirmed = await new Promise((resolve) => {
        Modal.confirm({
          title: '确认操作',
          content: button.confirmMessage,
          onOk: () => resolve(true),
          onCancel: () => resolve(false)
        });
      });
      
      if (!confirmed) {
        return;
      }
    }
    
    switch (button.action) {
      case 'submit':
        await submitNodeForm();
        break;
      case 'save':
        await saveFormDraft();
        break;
      case 'approve':
        await approveTask();
        break;
      case 'reject':
        await rejectTask();
        break;
      default:
        message.warning('未知操作类型');
    }
    
    if (button.successMessage) {
      message.success(button.successMessage);
    }
    
  } catch (error) {
    console.error('操作失败:', error);
    message.error('操作失败: ' + error.message);
  } finally {
    button.loading = false;
  }
};

const submitNodeForm = async () => {
  const response = await submitForm({
    taskId: props.taskId,
    nodeCode: 'current_node',
    formData: formData
  });
  
  if (response.success) {
    // 刷新页面状态
    await loadDisplayMode();
  }
};

const saveFormDraft = async () => {
  const response = await saveDraft({
    formId: props.formId,
    dataId: props.dataId,
    formData: formData
  });
  
  if (response.success) {
    message.success('草稿保存成功');
  }
};

const approveTask = async () => {
  // TODO: 实现审批逻辑
  await submitNodeForm();
};

const rejectTask = async () => {
  // TODO: 实现拒绝逻辑
  await submitNodeForm();
};

const showProcessHistory = () => {
  historyVisible.value = true;
};

const showVersionHistory = () => {
  versionVisible.value = true;
};

const exportForm = async () => {
  try {
    if (!props.formId || !props.dataId) {
      message.warning('表单信息不完整，无法导出');
      return;
    }

    // 基于JeecgBoot导出机制实现工作流表单导出
    const { handleExportXls } = useMethods();
    
    // 构建导出参数
    const exportParams = {
      formId: props.formId,
      dataId: props.dataId,
      taskId: props.taskId,
      processInstanceId: processInstanceId.value,
      includeHistory: true, // 包含流程历史
      includeComments: true // 包含处理意见
    };

    // 生成导出文件名
    const fileName = `工作流表单_${formBasicInfo.value?.reportNo || props.dataId}_${dayjs().format('YYYY-MM-DD-HH-mm-ss')}`;
    
    // 调用导出API
    await handleExportXls(fileName, '/workflow/form/export', exportParams);
    
    message.success('导出成功');
  } catch (error) {
    console.error('导出失败:', error);
    message.error('导出失败，请重试');
  }
};

const loadDisplayMode = async () => {
  try {
    loading.value = true;
    const response = await getDisplayMode({
      formId: props.formId,
      dataId: props.dataId
    });
    
    if (response.success) {
      displayMode.value = response.result;
      processInstanceId.value = response.result.processInstanceId;
      currentNodeButtons.value = response.result.availableActions || [];
      
      // 加载其他信息
      await loadFormBasicInfo();
      await loadProcessHistory();
    }
  } catch (error) {
    console.error('加载显示模式失败:', error);
    message.error('加载页面失败');
  } finally {
    loading.value = false;
  }
};

const loadFormBasicInfo = async () => {
  // TODO: 从API加载表单基础信息
  formBasicInfo.value = {
    reportNo: 'WO20241225001',
    createTime: '2024-12-25 10:00:00',
    createBy: '张三',
    status: 'PROCESSING',
    statusText: '处理中'
  };
};

const loadProcessHistory = async () => {
  // TODO: 从API加载流程历史
  processHistory.value = [
    {
      name: '提交申请',
      time: '2024-12-25 10:00:00',
      user: '张三',
      icon: CheckCircleOutlined,
      color: 'green'
    },
    {
      name: '部门审核',
      time: '2024-12-25 14:00:00',
      user: '李四',
      icon: ClockCircleOutlined,
      color: 'blue'
    }
  ];
  
  processSteps.value = [
    { id: '1', name: '提交申请' },
    { id: '2', name: '部门审核' },
    { id: '3', name: '处理完成' }
  ];
  
  currentStepIndex.value = 1;
};

// 生命周期
onMounted(async () => {
  await loadDisplayMode();
});

// 监听路由参数变化
watch(() => [props.formId, props.dataId, props.taskId], async () => {
  await loadDisplayMode();
});
</script>

<style scoped>
.workflow-form-page {
  padding: 16px;
}

.status-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 16px;
}

.current-info {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.online-form-container {
  min-height: 400px;
}

.form-placeholder {
  /* 临时样式，等待集成真实的在线表单组件 */
}

.process-fields {
  margin-top: 24px;
}

.form-actions {
  text-align: center;
  padding: 16px 0;
}

.process-timeline {
  max-height: 300px;
  overflow-y: auto;
}

.timeline-title {
  font-weight: 500;
  margin-bottom: 4px;
}

.timeline-time {
  font-size: 12px;
  color: #999;
  margin-bottom: 2px;
}

.timeline-user {
  font-size: 12px;
  color: #666;
}

@media (max-width: 768px) {
  .workflow-form-page {
    padding: 8px;
  }
  
  .workflow-form-page .ant-row {
    flex-direction: column;
  }
  
  .workflow-form-page .ant-col {
    width: 100% !important;
    margin-bottom: 16px;
  }
  
  .status-header {
    flex-direction: column;
    gap: 12px;
    padding: 12px 8px;
  }
  
  .status-header .ant-steps {
    order: 2;
  }
  
  .status-header .current-info {
    order: 1;
    justify-content: center;
    flex-wrap: wrap;
    gap: 8px;
  }
  
  /* 优化步骤条在移动端的显示 */
  .status-header .ant-steps .ant-steps-item {
    font-size: 12px;
  }
  
  .status-header .ant-steps .ant-steps-item-title {
    line-height: 1.2;
  }
  
  /* 优化表单卡片 */
  .ant-card {
    margin-bottom: 12px;
  }
  
  .ant-card-head {
    padding: 12px 16px;
  }
  
  .ant-card-body {
    padding: 16px 12px;
  }
  
  /* 优化时间线显示 */
  .process-timeline {
    font-size: 14px;
  }
}
</style>