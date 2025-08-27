<template>
  <div class="universal-form-page">
    <!-- 顶部状态栏 -->
    <div class="status-header">
      <a-steps :current="currentStepIndex" size="small" style="flex: 1">
        <a-step 
          v-for="step in processSteps" 
          :key="step.id" 
          :title="step.name"
          :status="getStepStatus(step)"
        />
      </a-steps>
      <div class="current-info">
        <a-tag :color="getStatusColor(currentStatus)">{{ currentStatusText }}</a-tag>
        <span v-if="currentAssignee">当前处理人：{{ currentAssignee }}</span>
        <span v-else-if="!isCompleted">等待分配</span>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <a-row :gutter="24">
      <!-- 左侧：表单内容（占主要空间） -->
      <a-col :span="18">
        <a-card title="表单详情" :bordered="false">
          <!-- 加载状态 -->
          <a-spin :spinning="loading" tip="正在加载表单数据...">
            
            <!-- 🎯 工作流在线表单组件（基于JeecgBoot） -->
            <WorkflowOnlineForm
              v-if="formType && !loading"
              :table="formType"
              :data-id="dataId"
              :task-id="currentTaskId"
              :edit="!!dataId"
              :flow-edit="hasCurrentTask"
              :workflow-mode="displayMode.mode"
              :node-id="currentNodeId"
              :process-instance-id="processInstanceId"
              :field-permissions="currentFieldPermissions"
              :show-actions="false"
              @data-change="handleFormDataChange"
              @submit="handleFormSubmit"
              @loaded="handleFormLoaded"
            />
            
            <!-- 🎯 智能工作流操作区域 -->
            <div v-if="!loading" class="form-actions-section">
              <a-divider />
              
              <!-- 使用JeecgBoot现有的按钮系统 -->
              <div class="action-buttons">
                <a-space size="large" wrap>
                  <!-- 保存草稿按钮 -->
                  <a-button 
                    v-if="allowSave"
                    @click="handleSaveDraft"
                    :loading="savingDraft"
                    :disabled="isReadonlyMode"
                    size="large"
                  >
                    <template #icon>
                      <SaveOutlined />
                    </template>
                    保存草稿
                  </a-button>

                  <!-- 提交按钮 -->
                  <a-button
                    v-if="allowSubmit"
                    type="primary"
                    @click="handleSubmitForm"
                    :loading="submitting"
                    :disabled="isReadonlyMode"
                    size="large"
                  >
                    <template #icon>
                      <SendOutlined />
                    </template>
                    {{ getSubmitButtonText() }}
                  </a-button>

                  <!-- 启动工作流按钮 -->
                  <a-button
                    v-if="allowStartWorkflow"
                    type="primary"
                    @click="handleManualStartWorkflow"
                    :loading="startingWorkflow"
                    :disabled="isReadonlyMode"
                    size="large"
                  >
                    <template #icon>
                      <PlayCircleOutlined />
                    </template>
                    启动工作流
                  </a-button>

                  <!-- 工作流操作按钮 -->
                  <template v-if="hasCurrentTask && currentWorkflowButtons.length > 0">
                    <a-button
                      v-for="button in currentWorkflowButtons"
                      :key="button.id"
                      :type="button.type"
                      :loading="button.loading"
                      :disabled="button.disabled || isReadonlyMode"
                      @click="handleWorkflowAction(button.action, button)"
                      size="large"
                    >
                      <template #icon v-if="button.icon">
                        <component :is="button.icon" />
                      </template>
                      {{ button.text }}
                    </a-button>
                  </template>
                </a-space>
              </div>
            </div>
            
            <!-- 只读模式提示 -->
            <div v-else-if="isReadonlyMode && !loading" class="readonly-tip">
              <a-alert
                :message="getReadonlyMessage()"
                type="info"
                show-icon
                :closable="false"
              />
            </div>
            
          </a-spin>
        </a-card>
      </a-col>
      
      <!-- 右侧：信息侧栏 -->
      <a-col :span="6">
        <!-- 快速操作 -->
        <a-card title="快速操作" size="small" style="margin-bottom: 16px">
          <a-space direction="vertical" style="width: 100%">
            <a-button block @click="showProcessHistory" :disabled="!processInstanceId">
              <HistoryOutlined />
              查看流程历史
            </a-button>
            <a-button 
              block 
              @click="showVersionHistory" 
              :disabled="!versionControlEnabled || !processInstanceId"
            >
              <DiffOutlined />
              查看版本历史
            </a-button>
            <a-button block @click="exportForm" :disabled="!formData">
              <ExportOutlined />
              导出表单
            </a-button>
            <a-button 
              block 
              @click="printForm" 
              :disabled="!formData"
            >
              <PrinterOutlined />
              打印表单
            </a-button>
          </a-space>
        </a-card>
        
        <!-- 流程进度 -->
        <a-card 
          title="流程进度" 
          size="small" 
          style="margin-bottom: 16px"
          v-if="processInstanceId"
        >
          <ProcessTimeline 
            :process-instance-id="processInstanceId" 
            :compact="true" 
          />
        </a-card>
        
        <!-- 表单基本信息 -->
        <a-card title="基本信息" size="small">
          <a-descriptions size="small" :column="1">
            <a-descriptions-item label="表单编号">
              {{ basicInfo.formNo || '自动生成' }}
            </a-descriptions-item>
            <a-descriptions-item label="创建时间">
              {{ formatDateTime(basicInfo.createTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="创建人">
              {{ basicInfo.createBy }}
            </a-descriptions-item>
            <a-descriptions-item label="最后更新">
              {{ formatDateTime(basicInfo.updateTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="表单状态">
              <a-tag :color="getStatusColor(basicInfo.formStatus)">{{ getStatusText(basicInfo.formStatus) }}</a-tag>
            </a-descriptions-item>
            <a-descriptions-item v-if="basicInfo.priority" label="优先级">
              <a-tag :color="getPriorityColor(basicInfo.priority)">{{ getPriorityText(basicInfo.priority) }}</a-tag>
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>
    </a-row>

    <!-- 模态框 -->
    <!-- 流程历史模态框 -->
    <a-modal
      v-model:open="historyModalVisible"
      title="流程历史"
      width="80%"
      :footer="null"
      destroy-on-close
    >
      <ProcessHistory 
        v-if="historyModalVisible && processInstanceId"
        :process-instance-id="processInstanceId"
        :show-comments="true"
        :show-attachments="true"
      />
    </a-modal>

    <!-- 版本历史模态框 -->
    <a-modal
      v-model:open="versionModalVisible"
      title="版本历史"
      width="90%"
      :footer="null"
      destroy-on-close
    >
      <VersionTimeline 
        v-if="versionModalVisible && processInstanceId"
        :process-instance-id="processInstanceId"
        @compare="handleVersionCompare"
      />
    </a-modal>

    <!-- 版本对比模态框 -->
    <a-modal
      v-model:open="compareModalVisible"
      title="版本对比"
      width="95%"
      :footer="null"
      destroy-on-close
    >
      <VersionCompare 
        v-if="compareModalVisible && compareVersions.length >= 2"
        :versions="compareVersions"
        :form-config="formConfig"
      />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { useMethods } from '/@/hooks/system/useMethods';
import dayjs from 'dayjs';
import { 
  HistoryOutlined, 
  DiffOutlined, 
  ExportOutlined, 
  PrinterOutlined,
  SaveOutlined,
  SendOutlined,
  PlayCircleOutlined
} from '@ant-design/icons-vue';
import WorkflowOnlineForm from '@/components/jeecg/OnlineForm/WorkflowOnlineForm.vue';
// 使用JeecgBoot现有的按钮系统，通过SmartButtonGroup组件处理工作流按钮
import ProcessTimeline from '../components/ProcessTimeline.vue';
import ProcessHistory from '../components/ProcessHistory.vue';
import VersionTimeline from '../components/VersionTimeline.vue';
import VersionCompare from '../components/VersionCompare.vue';
// 使用JeecgBoot现有的Tag组件，无需专门的状态和优先级标签组件
import { generateWorkflowButtons } from '/@/utils/workflow/buttonManager';
import type { WorkflowButton } from '/@/utils/workflow/buttonManager';
import { useUserStore } from '/@/store/modules/user';
import { formatToDateTime } from '/@/utils/dateUtil';
// 🎯 导入基于JeecgBoot API的方法
import { defHttp } from '/@/utils/http/axios';

// API方法定义
const submitForm = (tableName: string, dataId: string, formData: any) => {
  return defHttp.post({
    url: '/workflow/form/submit',
    data: { tableName, dataId, formData }
  });
};

const saveDraft = (tableName: string, dataId: string, formData: any) => {
  return defHttp.post({
    url: '/workflow/form/save-draft', 
    data: { tableName, dataId, formData }
  });
};

const getFormBasicInfo = (tableName: string, dataId: string) => {
  return defHttp.get({
    url: '/workflow/form/basic-info',
    params: { tableName, dataId }
  });
};

const manualStartWorkflow = (tableName: string, dataId: string) => {
  return defHttp.post({
    url: '/workflow/form/manual-start',
    data: { tableName, dataId }
  });
};

const getProcessInfo = (processInstanceId: string) => {
  return defHttp.get({
    url: '/workflow/process/info',
    params: { processInstanceId }
  });
};

const completeTask = (taskId: string, data: any) => {
  return defHttp.post({
    url: '/workflow/task/complete',
    data: { taskId, ...data }
  });
};

// 定义组件名称
defineOptions({ name: 'UniversalFormPage' });

// 路由和用户信息
const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

// 响应式数据
const loading = ref(false);
const formType = ref(''); // 表名，对应JeecgBoot的table参数
const dataId = ref(''); // 数据ID
const formData = reactive({});
const displayMode = ref({ mode: 'CREATE' });
const currentFieldPermissions = ref({});
const basicInfo = reactive({});
const processInfo = ref(null);
const currentNodeId = ref('');

// 流程相关状态
const processInstanceId = ref('');
const currentTaskId = ref('');
const currentStatus = ref('DRAFT');
const currentStatusText = ref('草稿');
const currentAssignee = ref('');
const processSteps = ref([]);
const currentStepIndex = ref(0);

// 操作相关状态
const hasCurrentTask = ref(false);
const needComment = ref(false);
const processComment = ref('');
const versionControlEnabled = ref(false);

// 🎯 新的工作流按钮系统状态
const currentWorkflowButtons = ref<WorkflowButton[]>([]);
const allowSave = ref(true);
const allowSubmit = ref(true);
const allowStartWorkflow = ref(false);
const showBaseActions = ref(true);

// 工作流启动模式相关状态
const canStartWorkflowFlag = ref(false);

// 加载状态
const submitting = ref(false);
const savingDraft = ref(false);
const startingWorkflow = ref(false);

// 模态框状态
const historyModalVisible = ref(false);
const versionModalVisible = ref(false);
const compareModalVisible = ref(false);
const compareVersions = ref<any[]>([]);

// 计算属性
const isReadonlyMode = computed(() => {
  return displayMode.value.mode === 'VIEW' || displayMode.value.mode === 'TRACK';
});

const isCompleted = computed(() => {
  return currentStatus.value === 'COMPLETED' || currentStatus.value === 'FINISHED';
});

// 🎯 新的计算属性 - 支持智能按钮系统
const getSubmitButtonText = () => {
  if (hasCurrentTask.value) {
    return '完成任务';
  } else if (allowStartWorkflow.value) {
    return '提交并启动工作流';
  } else {
    return '提交';
  }
};

// 初始化页面
onMounted(async () => {
  await initializePage();
});

// 监听路由变化
watch(() => route.params, async () => {
  await initializePage();
}, { deep: true });

/**
 * 🎯 初始化页面数据（基于JeecgBoot表名）
 */
async function initializePage() {
  loading.value = true;
  
  try {
    const { formType: routeFormType, dataId: routeDataId } = route.params;
    const { taskId } = route.query;
    
    if (!routeFormType) {
      message.error('缺少表单类型参数');
      return;
    }
    
    // 设置基础数据
    formType.value = routeFormType as string;
    dataId.value = routeDataId as string || '';
    currentTaskId.value = taskId as string || '';
    
    if (!dataId.value) {
      // 新建模式
      await initCreateMode();
    } else {
      // 查看/编辑模式
      await initViewMode();
    }
    
  } catch (error) {
    console.error('初始化页面失败:', error);
    message.error('页面初始化失败，请重试');
  } finally {
    loading.value = false;
  }
}

/**
 * 🎯 初始化创建模式（基于JeecgBoot表名）
 */
async function initCreateMode() {
  // 设置显示模式为创建模式
  displayMode.value = {
    mode: 'CREATE'
  };
  
  // 设置基本信息
  Object.assign(basicInfo, {
    formStatus: 'DRAFT',
    createTime: new Date(),
    createBy: userStore.userInfo?.realname || '当前用户'
  });
  
  // 新建模式无当前任务
  hasCurrentTask.value = false;
  
  // 清空表单数据
  Object.keys(formData).forEach(key => delete formData[key]);
  
  // 🎯 更新按钮显示状态
  updateBaseActionsVisibility();
}

/**
 * 🎯 初始化查看模式（基于JeecgBoot + 工作流扩展）
 */
async function initViewMode() {
  try {
    // 获取基本信息（这里应该从后端API获取）
    const basicInfoData = await getFormBasicInfo(formType.value, dataId.value);
    Object.assign(basicInfo, basicInfoData);
    
    // 设置显示模式
    if (currentTaskId.value) {
      // 有任务ID，表示是工作流任务模式
      displayMode.value = { mode: 'OPERATE' };
      hasCurrentTask.value = true;
      
      // 获取任务相关信息
      await loadTaskInfo();
    } else if (basicInfoData.processInstanceId) {
      // 有流程实例但无当前任务，表示是跟踪模式
      displayMode.value = { mode: 'TRACK' };
      hasCurrentTask.value = false;
      processInstanceId.value = basicInfoData.processInstanceId;
      await loadProcessInfo();
    } else {
      // 普通查看模式
      displayMode.value = { mode: 'VIEW' };
      hasCurrentTask.value = false;
    }
    
    // 设置状态信息
    currentStatus.value = basicInfoData.formStatus || 'DRAFT';
    currentStatusText.value = getStatusText(currentStatus.value);
    
    // 🎯 更新按钮显示状态
    updateBaseActionsVisibility();
    
  } catch (error) {
    console.error('初始化查看模式失败:', error);
    message.error('加载表单信息失败');
  }
}

/**
 * 加载流程信息
 */
async function loadProcessInfo() {
  try {
    const info = await getProcessInfo(processInstanceId.value);
    processInfo.value = info;
    
    if (info) {
      processSteps.value = info.steps || [];
      currentStepIndex.value = info.currentStepIndex || 0;
      currentAssignee.value = info.currentAssignee || '';
    }
  } catch (error) {
    console.error('加载流程信息失败:', error);
  }
}

/**
 * 🎯 处理表单加载完成
 */
function handleFormLoaded(config: any) {
  console.log('表单加载完成:', config);
  // 可以在这里处理表单加载完成后的逻辑
}

/**
 * 🎯 加载任务信息并生成工作流按钮
 */
async function loadTaskInfo() {
  if (!currentTaskId.value) return;
  
  try {
    // 这里应该调用获取任务信息的API
    // const taskInfo = await getTaskInfo(currentTaskId.value);
    // 暂时模拟
    needComment.value = true; // 大部分任务需要处理意见
    
    // 🎯 生成工作流按钮
    await loadWorkflowButtons();
    
  } catch (error) {
    console.error('加载任务信息失败:', error);
  }
}

/**
 * 🎯 加载工作流按钮
 */
async function loadWorkflowButtons() {
  try {
    const buttons = await generateWorkflowButtons(
      currentTaskId.value,
      processInstanceId.value,
      formType.value
    );
    currentWorkflowButtons.value = buttons;
    
    // 根据工作流状态调整基础按钮显示
    updateBaseActionsVisibility();
    
  } catch (error) {
    console.error('加载工作流按钮失败:', error);
    currentWorkflowButtons.value = [];
  }
}

/**
 * 🎯 更新基础操作按钮可见性
 */
function updateBaseActionsVisibility() {
  const mode = displayMode.value.mode;
  
  // 根据模式设置基础按钮
  switch (mode) {
    case 'CREATE':
      allowSave.value = true;
      allowSubmit.value = true;
      allowStartWorkflow.value = canStartWorkflowFlag.value;
      showBaseActions.value = true;
      break;
    case 'EDIT':
      allowSave.value = true;
      allowSubmit.value = true;
      allowStartWorkflow.value = false;
      showBaseActions.value = true;
      break;
    case 'OPERATE':
      allowSave.value = false;
      allowSubmit.value = false;
      allowStartWorkflow.value = false;
      showBaseActions.value = false; // 工作流操作模式只显示工作流按钮
      break;
    case 'VIEW':
    case 'TRACK':
      allowSave.value = false;
      allowSubmit.value = false;
      allowStartWorkflow.value = false;
      showBaseActions.value = false;
      break;
  }
}

/**
 * 处理表单数据变化
 */
function handleFormDataChange(changedData: any) {
  Object.assign(formData, changedData);
}

/**
 * 🎯 智能提交表单（基于JeecgBoot API）
 */
async function handleSubmitForm() {
  try {
    submitting.value = true;
    
    // 调用新的后端API
    const result = await submitForm(formType.value, dataId.value, formData);
    
    if (result.success) {
      message.success(result.result.message || '提交成功');
      
      // 更新数据ID（如果是新建）
      if (!dataId.value && result.result.dataId) {
        dataId.value = result.result.dataId;
        // 更新路由，避免重复提交
        await router.replace({
          path: route.path.replace('/', `/${dataId.value}`),
          query: route.query
        });
      }
      
      // 根据返回结果处理
      if (result.result.action === 'workflow_started') {
        processInstanceId.value = result.result.processInstanceId;
      }
      
      // 刷新页面数据
      await initializePage();
    } else {
      message.error(result.message || '提交失败');
    }
    
  } catch (error) {
    console.error('提交失败:', error);
    message.error('提交失败，请重试');
  } finally {
    submitting.value = false;
  }
}

/**
 * 🎯 保存草稿（基于JeecgBoot API）
 */
async function handleSaveDraft() {
  try {
    savingDraft.value = true;
    
    // 调用新的后端API
    const result = await saveDraft(formType.value, dataId.value, formData);
    
    if (result.success) {
      message.success(result.result.message || '草稿保存成功');
      
      // 更新数据ID（如果是新建）
      if (!dataId.value && result.result.dataId) {
        dataId.value = result.result.dataId;
        // 更新路由
        await router.replace({
          path: route.path.replace('/', `/${dataId.value}`),
          query: route.query
        });
      }
      
      // 更新基本信息
      Object.assign(basicInfo, {
        formStatus: 'DRAFT',
        updateTime: new Date()
      });
      
      // 检查是否可以启动工作流
      if (result.result.canStartWorkflow) {
        canStartWorkflowFlag.value = true;
      }
    } else {
      message.error(result.message || '保存草稿失败');
    }
    
  } catch (error) {
    console.error('保存草稿失败:', error);
    message.error('保存草稿失败，请重试');
  } finally {
    savingDraft.value = false;
  }
}

/**
 * 手动启动工作流
 */
async function handleManualStartWorkflow() {
  try {
    startingWorkflow.value = true;
    
    const { formType, dataId } = route.params;
    const newProcessInstanceId = await manualStartWorkflow(formType as string, dataId as string);
    
    message.success('工作流启动成功');
    
    // 更新流程实例ID
    processInstanceId.value = newProcessInstanceId;
    
    // 刷新页面数据
    await initializePage();
    
  } catch (error) {
    console.error('启动工作流失败:', error);
    message.error('启动工作流失败，请重试');
  } finally {
    startingWorkflow.value = false;
  }
}

/**
 * 🎯 处理工作流按钮动作
 */
async function handleWorkflowAction(button: WorkflowButton, comment?: string) {
  try {
    console.log(`执行工作流动作: ${button.code}`, { comment });
    
    // 重新加载按钮状态（因为按钮动作可能改变任务状态）
    await loadWorkflowButtons();
    
    // 刷新页面数据
    await initializePage();
    
  } catch (error) {
    console.error(`工作流动作 ${button.code} 执行失败:`, error);
    message.error(`${button.label}失败，请重试`);
  }
}

/**
 * 处理表单提交（兼容旧方法）
 */
async function handleFormSubmit(submitData: any) {
  try {
    loading.value = true;
    
    if (hasCurrentTask.value && currentTaskId.value) {
      // 工作流任务提交
      await completeTask(currentTaskId.value, {
        ...submitData,
        comment: processComment.value
      });
      
      message.success('提交成功');
      
      // 刷新页面数据
      await initializePage();
      
    } else {
      // 使用智能提交
      await handleSubmitForm();
    }
    
  } catch (error) {
    console.error('提交失败:', error);
    message.error('提交失败，请重试');
  } finally {
    loading.value = false;
  }
}



/**
 * 显示流程历史
 */
function showProcessHistory() {
  historyModalVisible.value = true;
}

/**
 * 显示版本历史
 */
function showVersionHistory() {
  versionModalVisible.value = true;
}

/**
 * 处理版本对比
 */
function handleVersionCompare(versions: any[]) {
  compareVersions.value = versions;
  compareModalVisible.value = true;
}

/**
 * 导出表单
 */
async function exportForm() {
  try {
    if (!route.params.formId || !route.params.dataId) {
      message.warning('表单信息不完整，无法导出');
      return;
    }

    // 基于JeecgBoot导出机制实现工作流表单导出
    const { handleExportXls } = useMethods();
    
    // 构建导出参数
    const exportParams = {
      formId: route.params.formId,
      dataId: route.params.dataId,
      taskId: currentTaskId.value,
      processInstanceId: processInstanceId.value,
      includeHistory: true, // 包含流程历史
      includeComments: true, // 包含处理意见
      formData: formData // 包含表单数据
    };

    // 生成导出文件名
    const fileName = `工作流表单_${route.params.dataId}_${dayjs().format('YYYY-MM-DD-HH-mm-ss')}`;
    
    // 调用导出API
    await handleExportXls(fileName, '/workflow/form/export', exportParams);
    
    message.success('导出成功');
  } catch (error) {
    console.error('导出失败:', error);
    message.error('导出失败，请重试');
  }
}

/**
 * 打印表单
 */
function printForm() {
  window.print();
}

/**
 * 获取状态颜色
 */
function getStatusColor(status: string): string {
  const statusColors = {
    'DRAFT': 'default',
    'PROCESSING': 'processing',
    'APPROVED': 'success',
    'REJECTED': 'error',
    'COMPLETED': 'success',
    'CANCELLED': 'warning'
  };
  return statusColors[status] || 'default';
}

/**
 * 获取状态文本
 */
function getStatusText(status: string): string {
  const statusTexts = {
    'DRAFT': '草稿',
    'PROCESSING': '处理中',
    'APPROVED': '已通过',
    'REJECTED': '已拒绝',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消'
  };
  return statusTexts[status] || status;
}

/**
 * 获取步骤状态
 */
function getStepStatus(step: any): string {
  if (step.completed) return 'finish';
  if (step.current) return 'process';
  if (step.error) return 'error';
  return 'wait';
}

/**
 * 获取只读模式提示信息
 */
function getReadonlyMessage(): string {
  if (isCompleted.value) {
    return '此表单已完成，当前为查看模式';
  }
  if (displayMode.value.mode === 'TRACK') {
    return '流程进行中，您暂无处理权限，当前为跟踪模式';
  }
  return '当前为查看模式';
}

/**
 * 格式化日期时间
 */
function formatDateTime(date: any): string {
  return formatToDateTime(date);
}

/**
 * 获取优先级颜色
 */
function getPriorityColor(level: number | string): string {
  const numLevel = Number(level);
  const colorMap: Record<number, string> = {
    1: 'blue',      // 低
    2: 'default',   // 中
    3: 'orange',    // 高
    4: 'red',       // 紧急
    5: 'red'        // 非常紧急
  };
  return colorMap[numLevel] || 'default';
}

/**
 * 获取优先级文本
 */
function getPriorityText(level: number | string): string {
  const numLevel = Number(level);
  const textMap: Record<number, string> = {
    1: '低',
    2: '中',
    3: '高',
    4: '紧急',
    5: '非常紧急'
  };
  return textMap[numLevel] || '中';
}

// ===============================
// 智能按钮事件处理函数
// ===============================




</script>

<style lang="less" scoped>
.universal-form-page {
  padding: 16px;
  background: #f0f2f5;
  min-height: calc(100vh - 64px);

  .status-header {
    display: flex;
    align-items: center;
    gap: 24px;
    padding: 16px 24px;
    background: white;
    border-radius: 8px;
    margin-bottom: 16px;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);

    .current-info {
      display: flex;
      align-items: center;
      gap: 12px;
      white-space: nowrap;
      
      .ant-tag {
        margin: 0;
      }
    }
  }

  .form-actions-section {
    margin-top: 24px;

    .comment-section {
      margin-bottom: 24px;

      h4 {
        margin-bottom: 12px;
        font-weight: 600;
        color: rgba(0, 0, 0, 0.85);
      }
    }

    .action-buttons {
      display: flex;
      justify-content: center;
      
      .ant-btn {
        min-width: 100px;
        height: 40px;
        font-size: 14px;
        
        &.ant-btn-primary {
          box-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
        }
      }
    }
  }

  .readonly-tip {
    margin-top: 16px;
  }

  // 响应式设计
  @media (max-width: 768px) {
    padding: 8px;

    .status-header {
      flex-direction: column;
      gap: 12px;
      padding: 12px 8px;
      background: #f8f9fa;
      border-radius: 8px;
      margin-bottom: 12px;
      
      .current-info {
        order: 1;
        justify-content: center;
        flex-wrap: wrap;
        gap: 8px;
      }
      
      :deep(.ant-steps) {
        order: 2;
      }
    }

    .ant-row {
      flex-direction: column;
    }

    .ant-col {
      width: 100% !important;
      margin-bottom: 16px;
    }
    
    // 优化表单操作区域
    .form-actions-section {
      position: sticky;
      bottom: 0;
      background: white;
      padding: 16px 8px;
      border-top: 1px solid #e8e8e8;
      margin: 0 -8px;
      z-index: 10;
      border-radius: 8px 8px 0 0;
      box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.1);
      
      .action-buttons {
        flex-direction: column;
        gap: 8px;
        
        .ant-btn {
          width: 100%;
          height: 44px;
          font-size: 16px;
        }
      }
    }
    
    // 优化卡片
    .ant-card {
      margin-bottom: 12px;
      
      .ant-card-head {
        padding: 12px 16px;
        
        .ant-card-head-title {
          font-size: 16px;
        }
      }
      
      .ant-card-body {
        padding: 16px 12px;
      }
    }
    
    // 优化在线表单显示
    .online-form-container {
      padding: 8px 0;
    }
    
    // 优化流程信息
    :deep(.ant-descriptions) {
      .ant-descriptions-item {
        padding-bottom: 8px;
      }
      
      .ant-descriptions-item-label {
        font-size: 14px;
        padding-bottom: 4px;
      }
      
      .ant-descriptions-item-content {
        font-size: 14px;
      }
    }
  }
}

// 深度选择器样式
:deep(.ant-card-head-title) {
  font-weight: 600;
}

:deep(.ant-descriptions-item-label) {
  font-weight: 500;
  color: rgba(0, 0, 0, 0.65);
}

:deep(.ant-form-item) {
  margin-bottom: 16px;
}

:deep(.ant-steps-item-title) {
  font-size: 12px;
}
</style>