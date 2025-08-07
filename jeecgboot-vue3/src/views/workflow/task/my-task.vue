<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-space>
          <a-button type="primary" @click="handleRefresh">
            <Icon icon="ant-design:reload-outlined" />
            刷新
          </a-button>
          <a-button @click="handleBatchComplete" :disabled="!selectedRowKeys.length">
            <Icon icon="ant-design:check-outlined" />
            批量完成
          </a-button>
        </a-space>
      </template>
      
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'priority'">
          <a-tag :color="getPriorityColor(record.priority)">
            {{ getPriorityText(record.priority) }}
          </a-tag>
        </template>
        
        <template v-if="column.key === 'dueDate'">
          <span :class="{ 'text-red-500': isOverdue(record.dueDate) }">
            {{ formatToDateTime(record.dueDate) }}
          </span>
        </template>
        
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button type="link" size="small" @click="handleComplete(record)">
              {{ t('routes.workflow.complete') }}
            </a-button>
            <a-button type="link" size="small" @click="handleDelegate(record)">
              {{ t('routes.workflow.delegate') }}
            </a-button>
            <a-button type="link" size="small" @click="handleView(record)">
              {{ t('routes.workflow.view') }}
            </a-button>
          </a-space>
        </template>
      </template>
    </BasicTable>

    <!-- 完成任务弹窗 -->
    <BasicModal
      v-bind="$attrs"
      @register="registerCompleteModal"
      :title="t('routes.workflow.complete')"
      @ok="handleCompleteSubmit"
    >
      <BasicForm @register="registerCompleteForm" />
    </BasicModal>

    <!-- 委托任务弹窗 -->
    <BasicModal
      v-bind="$attrs"
      @register="registerDelegateModal"
      :title="t('routes.workflow.delegate')"
      @ok="handleDelegateSubmit"
    >
      <BasicForm @register="registerDelegateForm" />
    </BasicModal>

    <!-- 查看任务详情弹窗 -->
    <BasicModal
      v-bind="$attrs"
      @register="registerViewModal"
      :title="t('routes.workflow.taskName')"
      width="800px"
    >
      <div v-if="currentTask">
        <a-tabs>
          <a-tab-pane key="basic" tab="基本信息">
            <a-descriptions :column="2" bordered>
              <a-descriptions-item :label="t('routes.workflow.taskName')">
                {{ currentTask.name }}
              </a-descriptions-item>
              <a-descriptions-item :label="t('routes.workflow.taskAssignee')">
                {{ currentTask.assignee }}
              </a-descriptions-item>
              <a-descriptions-item :label="t('routes.workflow.taskCreateTime')">
                {{ formatDate(currentTask.createTime) }}
              </a-descriptions-item>
              <a-descriptions-item :label="t('routes.workflow.taskDueDate')">
                {{ formatDate(currentTask.dueDate) }}
              </a-descriptions-item>
              <a-descriptions-item :label="t('routes.workflow.taskPriority')">
                <a-tag :color="getPriorityColor(currentTask.priority)">
                  {{ getPriorityText(currentTask.priority) }}
                </a-tag>
              </a-descriptions-item>
              <a-descriptions-item :label="t('routes.workflow.taskDescription')" :span="2">
                {{ currentTask.description }}
              </a-descriptions-item>
            </a-descriptions>
          </a-tab-pane>
          
          <a-tab-pane key="form" tab="表单数据">
            <div v-if="taskFormData">
              <a-form layout="vertical">
                <a-form-item 
                  v-for="field in taskFormData.fields" 
                  :key="field.name"
                  :label="field.label"
                >
                  <component 
                    :is="getFormComponent(field.type)"
                    v-model:value="field.value"
                    :disabled="true"
                    v-bind="field.props || {}"
                  />
                </a-form-item>
              </a-form>
            </div>
            <a-empty v-else description="暂无表单数据" />
          </a-tab-pane>
          
          <a-tab-pane key="history" tab="处理历史">
            <a-timeline>
              <a-timeline-item 
                v-for="item in taskHistory" 
                :key="item.id"
                :color="getHistoryColor(item.type)"
              >
                <template #dot>
                  <Icon :icon="getHistoryIcon(item.type)" />
                </template>
                <div>
                  <div class="font-medium">{{ item.action }}</div>
                  <div class="text-gray-500 text-sm">
                    {{ item.user }} - {{ formatDate(item.time) }}
                  </div>
                  <div v-if="item.comment" class="mt-2 p-2 bg-gray-50 rounded">
                    {{ item.comment }}
                  </div>
                </div>
              </a-timeline-item>
            </a-timeline>
          </a-tab-pane>
        </a-tabs>
      </div>
    </BasicModal>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { BasicTable, useTable } from '/@/components/Table';
import { BasicModal, useModal } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form';
import { useMessage } from '/@/hooks/web/useMessage';
import { useI18n } from '/@/hooks/web/useI18n';
import { workflowTaskApi, workflowHistoryApi } from '/@/api/workflow';
import { formatToDateTime } from '/@/utils/dateUtil';

const { t } = useI18n();
const { createMessage } = useMessage();

const currentTask = ref<any>(null);
const taskFormData = ref<any>(null);
const taskHistory = ref<any[]>([]);
const selectedRowKeys = ref<string[]>([]);

// 表格配置
const [registerTable, { reload, getSelectRowKeys }] = useTable({
  title: t('routes.workflow.myTask'),
  api: workflowTaskApi.getMyTasks,
  rowKey: 'id',
  rowSelection: {
    type: 'checkbox',
    onChange: (keys) => {
      selectedRowKeys.value = keys;
    },
  },
  columns: [
    {
      title: t('routes.workflow.taskName'),
      dataIndex: 'name',
      width: 200,
    },
    {
      title: '流程名称',
      dataIndex: 'processDefinitionName',
      width: 150,
    },
    {
      title: t('routes.workflow.taskPriority'),
      dataIndex: 'priority',
      width: 100,
    },
    {
      title: t('routes.workflow.taskCreateTime'),
      dataIndex: 'createTime',
      width: 180,
      customRender: ({ text }) => formatDate(text),
    },
    {
      title: t('routes.workflow.taskDueDate'),
      dataIndex: 'dueDate',
      width: 180,
    },
    {
      title: '发起人',
      dataIndex: 'processInstanceStartUser',
      width: 120,
    },
    {
      title: '操作',
      dataIndex: 'action',
      width: 200,
      fixed: 'right',
    },
  ],
  useSearchForm: true,
  showTableSetting: true,
  bordered: true,
  formConfig: {
    labelWidth: 100,
    schemas: [
      {
        field: 'name',
        label: t('routes.workflow.taskName'),
        component: 'Input',
        colProps: { span: 8 },
      },
      {
        field: 'processDefinitionName',
        label: '流程名称',
        component: 'Input',
        colProps: { span: 8 },
      },
      {
        field: 'priority',
        label: t('routes.workflow.taskPriority'),
        component: 'Select',
        componentProps: {
          options: [
            { label: '低', value: 0 },
            { label: '中', value: 50 },
            { label: '高', value: 100 },
          ],
        },
        colProps: { span: 8 },
      },
    ],
  },
});

// 完成任务表单
const [registerCompleteForm, { validate: validateComplete, resetFields: resetCompleteFields }] = useForm({
  labelWidth: 100,
  schemas: [
    {
      field: 'comment',
      label: t('routes.workflow.formComments'),
      component: 'InputTextArea',
      componentProps: {
        rows: 4,
        placeholder: '请输入审批意见',
      },
    },
    {
      field: 'variables',
      label: t('routes.workflow.formVariables'),
      component: 'InputTextArea',
      componentProps: {
        rows: 3,
        placeholder: '请输入流程变量（JSON格式）',
      },
    },
  ],
});

// 委托任务表单
const [registerDelegateForm, { validate: validateDelegate, resetFields: resetDelegateFields }] = useForm({
  labelWidth: 100,
  schemas: [
    {
      field: 'assignee',
      label: '委托人',
      component: 'ApiSelect',
      componentProps: {
        api: getUserOptions,
        placeholder: '请选择委托人',
      },
      required: true,
    },
    {
      field: 'comment',
      label: '委托原因',
      component: 'InputTextArea',
      componentProps: {
        rows: 3,
        placeholder: '请输入委托原因',
      },
    },
  ],
});

// 弹窗配置
const [registerCompleteModal, { openModal: openCompleteModal, closeModal: closeCompleteModal }] = useModal();
const [registerDelegateModal, { openModal: openDelegateModal, closeModal: closeDelegateModal }] = useModal();
const [registerViewModal, { openModal: openViewModal, closeModal: closeViewModal }] = useModal();

// 获取优先级颜色
function getPriorityColor(priority: number) {
  if (priority >= 100) return 'red';
  if (priority >= 50) return 'orange';
  return 'green';
}

// 获取优先级文本
function getPriorityText(priority: number) {
  if (priority >= 100) return '高';
  if (priority >= 50) return '中';
  return '低';
}

// 判断是否逾期
function isOverdue(dueDate: string) {
  if (!dueDate) return false;
  return new Date(dueDate) < new Date();
}

// 刷新
function handleRefresh() {
  reload();
}

// 完成任务
function handleComplete(record: any) {
  currentTask.value = record;
  openCompleteModal();
}

// 提交完成任务
async function handleCompleteSubmit() {
  try {
    const values = await validateComplete();
    await workflowTaskApi.complete(currentTask.value.id, values);
    createMessage.success(t('routes.workflow.completeSuccess'));
    closeCompleteModal();
    resetCompleteFields();
    reload();
  } catch (error) {
    createMessage.error(t('routes.workflow.completeFailed'));
  }
}

// 委托任务
function handleDelegate(record: any) {
  currentTask.value = record;
  openDelegateModal();
}

// 提交委托任务
async function handleDelegateSubmit() {
  try {
    const values = await validateDelegate();
    await workflowTaskApi.delegate(currentTask.value.id, values.assignee);
    createMessage.success('委托成功');
    closeDelegateModal();
    resetDelegateFields();
    reload();
  } catch (error) {
    createMessage.error('委托失败');
  }
}

// 查看任务详情
async function handleView(record: any) {
  currentTask.value = record;
  
  try {
    // 获取表单数据
    const formData = await workflowTaskApi.getFormData(record.id);
    taskFormData.value = formData;
    
    // 获取任务历史
    const history = await workflowHistoryApi.getTaskHistory(record.processInstanceId);
    taskHistory.value = history;
    
    openViewModal();
  } catch (error) {
    createMessage.error('获取任务详情失败');
  }
}

// 批量完成
async function handleBatchComplete() {
  if (selectedRowKeys.value.length === 0) {
    createMessage.warning('请选择要完成的任务');
    return;
  }
  
  try {
    // 这里可以实现批量完成逻辑
    createMessage.success(`成功完成 ${selectedRowKeys.value.length} 个任务`);
    selectedRowKeys.value = [];
    reload();
  } catch (error) {
    createMessage.error('批量完成失败');
  }
}

// 获取用户选项
async function getUserOptions() {
  // 这里应该调用实际的用户列表API
  return [
    { label: '张三', value: 'zhangsan' },
    { label: '李四', value: 'lisi' },
    { label: '王五', value: 'wangwu' },
  ];
}

// 获取表单组件
function getFormComponent(type: string) {
  const componentMap: Record<string, string> = {
    'input': 'a-input',
    'textarea': 'a-textarea',
    'select': 'a-select',
    'date': 'a-date-picker',
    'number': 'a-input-number',
  };
  return componentMap[type] || 'a-input';
}

// 获取历史记录颜色
function getHistoryColor(type: string) {
  const colorMap: Record<string, string> = {
    'start': 'green',
    'complete': 'blue',
    'delegate': 'orange',
    'transfer': 'purple',
  };
  return colorMap[type] || 'gray';
}

// 获取历史记录图标
function getHistoryIcon(type: string) {
  const iconMap: Record<string, string> = {
    'start': 'ant-design:play-circle-outlined',
    'complete': 'ant-design:check-circle-outlined',
    'delegate': 'ant-design:user-switch-outlined',
    'transfer': 'ant-design:swap-outlined',
  };
  return iconMap[type] || 'ant-design:clock-circle-outlined';
}

onMounted(() => {
  reload();
});
</script>

<style scoped>
.text-red-500 {
  color: #ef4444;
}
</style> 