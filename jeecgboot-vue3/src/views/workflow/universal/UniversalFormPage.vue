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
              :auto-load-permissions="false"
              :show-actions="false"
              @data-change="handleFormDataChange"
              @submit="handleFormSubmit"
              @loaded="handleFormLoaded"
            />
            
            <!-- 🎯 智能工作流操作区域 -->
            <div v-if="!loading" class="form-actions-section">
              <a-divider />
              <a-space size="large" wrap>
                <a-input-textarea v-model:value="processComment" :rows="2" placeholder="处理意见（可选）" style="width: 360px" />
                <SmartButtonGroup
                  :formId="formType"
                  :dataId="dataId"
                  :taskId="currentTaskId || undefined"
                  @save="handleSaveDraft"
                  @submit="handleSubmitForm"
                  @approve="handleApprove"
                  @reject="handleReject"
                  @claim="handleClaim"
                  @unclaim="handleUnclaim"
                  @transfer="handleTransfer"
                />
                <a-popconfirm v-if="currentTaskId" title="添加处理意见？" ok-text="确定" cancel-text="取消" @confirm="handleAddComment">
                  <a-button type="dashed">添加意见</a-button>
                </a-popconfirm>
              </a-space>
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
            <a-button block type="dashed" @click="togglePermissionDebug" :disabled="!formType">
              字段权限调试
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
          <div style="margin-top: 12px; text-align: center;">
            <a-button type="link" @click="viewDiagram">查看流程图</a-button>
          </div>
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

    <!-- 流程图弹窗 -->
    <a-modal
      v-model:open="diagramVisible"
      title="流程图"
      width="80%"
      :footer="null"
      destroy-on-close
    >
      <div v-if="processInstanceId" style="text-align:center;">
        <img :src="`/workflow/instance/${processInstanceId}/diagram.png`" alt="流程图" style="max-width:100%;" />
      </div>
    </a-modal>

    <!-- 字段权限调试弹窗（仅开发辅助） -->
    <a-modal
      v-model:open="permDebugVisible"
      title="字段权限调试"
      width="720px"
      :footer="null"
      destroy-on-close
    >
      <a-table :data-source="permDebugRows" :columns="permDebugCols" rowKey="field" size="small" :pagination="false" />
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
  PrinterOutlined
} from '@ant-design/icons-vue';
import WorkflowOnlineForm from '@/components/jeecg/OnlineForm/WorkflowOnlineForm.vue';
// 使用JeecgBoot现有的按钮系统，通过SmartButtonGroup组件处理工作流按钮
import ProcessTimeline from '../components/ProcessTimeline.vue';
import ProcessHistory from '../components/ProcessHistory.vue';
import VersionTimeline from '../components/VersionTimeline.vue';
import VersionCompare from '../components/VersionCompare.vue';
// 使用JeecgBoot现有的Tag组件，无需专门的状态和优先级标签组件
// 已统一依赖 SmartButtonGroup 渲染，不再本地生成
import { useUserStore } from '/@/store/modules/user';
import { formatToDateTime } from '/@/utils/dateUtil';
// 🎯 导入基于JeecgBoot API的方法
import { defHttp } from '/@/utils/http/axios';
import { workflowConfigApi, workflowTaskApi, workflowRenderApi } from '/@/api/workflow';

// API方法定义
const submitForm = (tableName: string, dataId: string, formData: any) => {
  return defHttp.post({
    url: '/workflow/onlineForm/form/submit',
    data: { tableName, dataId, formData }
  });
};

const saveDraft = (tableName: string, dataId: string, formData: any) => {
  return defHttp.post({
    url: '/workflow/onlineForm/form/save-draft', 
    data: { tableName, dataId, formData }
  });
};

const getFormBasicInfo = (tableName: string, dataId: string) => {
  return defHttp.get({
    url: '/workflow/onlineForm/form/basic-info',
    params: { tableName, dataId }
  });
};

const manualStartWorkflow = (tableName: string, dataId: string) => {
  return defHttp.post({
    url: '/workflow/onlineForm/form/manual-start',
    data: { tableName, dataId }
  });
};

const getProcessInfo = (processInstanceId: string) => {
  return defHttp.get({
    url: '/workflow/render/process/info',
    params: { processInstanceId }
  });
};

const completeTask = (taskId: string, data: any) => workflowTaskApi.complete(taskId, data);

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

// 统一 SmartButtonGroup 渲染，不再维护本地按钮状态

// 工作流启动模式相关状态（保留以便未来使用）
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
const diagramVisible = ref(false);
const permDebugVisible = ref(false);
const permDebugRows = ref<any[]>([]);
const permDebugCols = [
  { title: '字段', dataIndex: 'field', key: 'field', width: 160 },
  { title: '标题', dataIndex: 'title', key: 'title', width: 180 },
  { title: '可编辑', dataIndex: 'editable', key: 'editable' },
  { title: '只读', dataIndex: 'readonly', key: 'readonly' },
  { title: '隐藏', dataIndex: 'hidden', key: 'hidden' },
  { title: '必填', dataIndex: 'required', key: 'required' },
];

// === 流程变量：节点白名单配置（从表单/工作流配置解析）
const variableWhitelistByNode = ref<Record<string, string[]>>({});

function parseJsonSafe(input: any): any {
  if (!input) return null;
  if (typeof input === 'object') return input;
  if (typeof input === 'string') {
    try { return JSON.parse(input); } catch { return null; }
  }
  return null;
}

function extractVariableWhitelist(config: any): Record<string, string[]> {
  try {
    const head = config?.result?.head || config?.head || null;
    if (!head) return {};
    // 兼容多处放置：ui_schema_json / extendJson / fieldExtendJson
    const sources: any[] = [head.ui_schema_json, head.extendJson, head.fieldExtendJson]
      .map(parseJsonSafe)
      .filter(Boolean);
    for (const src of sources) {
      const vars = src?.workflow?.variables;
      if (vars && typeof vars === 'object') return vars;
    }
  } catch {}
  return {};
}

// 计算属性
const isReadonlyMode = computed(() => {
  return displayMode.value.mode === 'VIEW' || displayMode.value.mode === 'TRACK';
});

const isCompleted = computed(() => {
  return currentStatus.value === 'COMPLETED' || currentStatus.value === 'FINISHED';
});

// 已改为统一 SmartButtonGroup，无需本地按钮文案计算

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
    
    // 根据配置 ui_mode 选择渲染模式：SPLIT/INTEGRATED
    let uiMode: string | null = null;
    try {
      if (basicInfoData.formId && basicInfoData.processDefinitionKey) {
        const modeRes: any = await workflowConfigApi.getUiMode({ cgformHeadId: basicInfoData.formId, processDefinitionKey: basicInfoData.processDefinitionKey });
        uiMode = modeRes?.uiMode || null;
      }
    } catch {}

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

    // 若 ui_mode=INTEGRATED，跳转融合页
    if (uiMode === 'INTEGRATED') {
      await router.replace({
        path: '/workflow/form/integrated',
        query: {
          formId: basicInfoData.formId,
          tableName: formType.value,
          dataId: dataId.value,
          processDefinitionKey: basicInfoData.processDefinitionKey || '',
          processInstanceId: basicInfoData.processInstanceId || '',
          taskId: currentTaskId.value || '',
          nodeId: currentNodeId.value || ''
        }
      });
      return;
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
  // 解析节点变量白名单
  try {
    variableWhitelistByNode.value = extractVariableWhitelist(config) || {};
  } catch {
    variableWhitelistByNode.value = {};
  }
}

/**
 * 🎯 加载任务信息并生成工作流按钮
 */
async function loadTaskInfo() {
  if (!currentTaskId.value) return;
  
  try {
    // 获取任务详情以判定当前节点ID等信息
    const taskInfo: any = await (workflowTaskApi as any).getDetail(currentTaskId.value);
    if (taskInfo && taskInfo.result) {
      const t = taskInfo.result;
      currentNodeId.value = t.taskDefinitionKey || '';
      processInstanceId.value = t.processInstanceId || processInstanceId.value;
    }
    needComment.value = true;
    // 统一依赖 SmartButtonGroup，无需本地按钮生成
    await loadNodePermissions();
    
  } catch (error) {
    console.error('加载任务信息失败:', error);
  }
}

/**
 * 加载当前节点的字段权限（SPLIT模式使用）
 */
async function loadNodePermissions() {
  try {
    const formId = (basicInfo as any).formId;
    const processDefinitionKey = (basicInfo as any).processDefinitionKey || '';
    if (!formId || !currentNodeId.value || !processDefinitionKey) return;
    const data: any = await workflowRenderApi.getNodeRender({
      formId,
      processDefinitionKey,
      nodeId: currentNodeId.value,
      processInstanceId: processInstanceId.value,
    });
    currentFieldPermissions.value = data?.permissions || {};
  } catch (e) {
    currentFieldPermissions.value = {} as any;
  }
}

// 按钮由 SmartButtonGroup 统一加载

/**
 * 🎯 更新基础操作按钮可见性
 */
function updateBaseActionsVisibility() {}

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
      
      // 更新数据ID（如果是新建）并修正路径拼接
      if (!dataId.value && result.result.dataId) {
        dataId.value = result.result.dataId;
        await router.replace({
          name: 'UniversalFormPage',
          params: { formType: formType.value, dataId: dataId.value },
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
      
      // 更新数据ID（如果是新建）并修正路径拼接
      if (!dataId.value && result.result.dataId) {
        dataId.value = result.result.dataId;
        await router.replace({
          name: 'UniversalFormPage',
          params: { formType: formType.value, dataId: dataId.value },
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

// === 审批动作统一入口：确保内置变量 + 可扩展变量合并 ===
async function handleApprove() {
  if (!currentTaskId.value) return;
  const payload: any = { variables: { approve_result: 'pass' }, comment: processComment.value };
  await completeWithExtVars(payload);
}

async function handleReject() {
  if (!currentTaskId.value) return;
  const payload: any = { variables: { approve_result: 'reject' }, comment: processComment.value || '驳回' };
  await completeWithExtVars(payload);
}

async function completeWithExtVars(base: any) {
  try {
    loading.value = true;
    const nodeKey = currentNodeId.value;
    const white = variableWhitelistByNode.value || {};
    const allowKeys: string[] = Array.isArray(white[nodeKey]) ? white[nodeKey] : [];
    const merged: any = { ...base };
    if (allowKeys.length && typeof (window as any).WF_collectVars === 'function') {
      try {
        const ctx = { nodeKey, processDefinitionKey: (basicInfo as any).processDefinitionKey || '', user: userStore.userInfo, comment: processComment.value };
        const ext = await (window as any).WF_collectVars({ ...(formData as any) }, ctx);
        if (ext && typeof ext === 'object') {
          const picked: Record<string, any> = {};
          allowKeys.forEach(k => { if (k in ext) picked[k] = ext[k]; });
          merged.variables = { ...(merged.variables || {}), ...picked };
        }
      } catch {}
    }
    // 若白名单包含通用意见变量且未由扩展提供，则自动补充
    if (allowKeys.includes('approve_opinion')) {
      merged.variables = { ...(merged.variables || {}), approve_opinion: processComment.value || '' };
    }
    await completeTask(currentTaskId.value, merged);
    message.success('提交成功');
    await initializePage();
  } catch (e) {
    console.error(e);
    message.error('提交失败，请重试');
  } finally {
    loading.value = false;
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
// 工作流动作统一由 SmartButtonGroup 触发并回调

/**
 * 处理表单提交（兼容旧方法）
 */
async function handleFormSubmit(submitData: any) {
  try {
    loading.value = true;
    
    if (hasCurrentTask.value && currentTaskId.value) {
      // 工作流任务提交：合并扩展变量
      const merged = { ...submitData } as any;
      const nodeKey = currentNodeId.value;
      const white = variableWhitelistByNode.value || {};
      const allowKeys: string[] = Array.isArray(white[nodeKey]) ? white[nodeKey] : [];
      if (allowKeys.length && typeof (window as any).WF_collectVars === 'function') {
        try {
          const ctx = { nodeKey, processDefinitionKey: (basicInfo as any).processDefinitionKey || '', user: userStore.userInfo, comment: processComment.value };
          const ext = await (window as any).WF_collectVars({ ...(formData as any) }, ctx);
          if (ext && typeof ext === 'object') {
            const picked: Record<string, any> = {};
            allowKeys.forEach(k => { if (k in ext) picked[k] = ext[k]; });
            merged.variables = { ...(merged.variables || {}), ...picked };
          }
        } catch (e) { /* 忽略扩展变量错误，继续提交流程 */ }
      }
      if (allowKeys.includes('approve_opinion')) {
        merged.variables = { ...(merged.variables || {}), approve_opinion: processComment.value || '' };
      }
      await completeTask(currentTaskId.value, { ...merged, comment: processComment.value });
      
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
    if (!formType.value || !dataId.value) {
      message.warning('表单信息不完整，无法导出');
      return;
    }

    // 基于JeecgBoot导出机制实现工作流表单导出
    const { handleExportXls } = useMethods();
    
    // 构建导出参数
    const exportParams = {
      tableName: formType.value,
      dataId: dataId.value,
      taskId: currentTaskId.value,
      processInstanceId: processInstanceId.value,
      includeHistory: true, // 包含流程历史
      includeComments: true, // 包含处理意见
      formData: formData // 包含表单数据
    };

    // 生成导出文件名
    const fileName = `工作流表单_${dataId.value}_${dayjs().format('YYYY-MM-DD-HH-mm-ss')}`;
    
    // 调用导出API
    await handleExportXls(fileName, '/workflow/onlineForm/form/export', exportParams);
    
    message.success('导出成功');
  } catch (error) {
    console.error('导出失败:', error);
    message.error('导出失败，请重试');
  }
}

function viewDiagram() {
  diagramVisible.value = true;
}

async function togglePermissionDebug() {
  try {
    if (!formType.value) return;
    // 需要 processDefinitionKey 才能精确合成；若无，传空则走默认策略
    const pdKey = (basicInfo as any).processDefinitionKey || '';
    const nodeId = currentNodeId.value || '';
    const data: any = await defHttp.get({
      url: '/workflow/render/node/permissionDebug',
      params: { formId: (basicInfo as any).formId || '', processDefinitionKey: pdKey, nodeId },
    });
    permDebugRows.value = Array.isArray(data) ? data : (data?.result || []);
    permDebugVisible.value = true;
  } catch (e) {
    permDebugRows.value = [];
    permDebugVisible.value = true;
  }
}

async function handleClaim() {
  if (!currentTaskId.value) return;
  try {
    await defHttp.post({ url: '/workflow/task/claim', data: { taskId: currentTaskId.value } });
    message.success('已认领');
    await initializePage();
  } catch (e) {
    message.error('认领失败');
  }
}

async function handleUnclaim() {
  if (!currentTaskId.value) return;
  try {
    await defHttp.post({ url: '/workflow/task/unclaim', data: { taskId: currentTaskId.value } });
    message.success('已释放');
    await initializePage();
  } catch (e) {
    message.error('释放失败');
  }
}

async function handleAddComment() {
  if (!currentTaskId.value) return;
  if (!processComment.value) {
    message.warning('请输入处理意见');
    return;
  }
  try {
    await defHttp.post({ url: `/workflow/task/${currentTaskId.value}/comment`, data: { message: processComment.value } });
    message.success('已添加意见');
  } catch (e) {
    message.error('添加意见失败');
  }
}

async function handleTransfer(payload: any) {
  try {
    if (!currentTaskId.value) return;
    const assignee = payload?.assignee;
    if (!assignee) {
      message.warning('缺少接收人（assignee）参数');
      return;
    }
    await workflowTaskApi.transfer(currentTaskId.value, assignee);
    message.success('已转办');
    await initializePage();
  } catch (e) {
    message.error('转办失败');
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