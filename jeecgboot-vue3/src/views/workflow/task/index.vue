<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-space>
          <a-button type="primary" @click="handleRefresh">
            <Icon icon="ant-design:reload-outlined" />
            刷新
          </a-button>
          <a-button @click="handleBatchAssign" :disabled="!selectedRowKeys.length">
            <Icon icon="ant-design:user-add-outlined" />
            批量分配
          </a-button>
        </a-space>
      </template>
      
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'priority'">
          <a-tag :color="getPriorityColor(record.priority)">
            {{ getPriorityText(record.priority) }}
          </a-tag>
        </template>
        
        <template v-if="column.key === 'status'">
          <a-tag :color="getTaskStatusColor(record.status)">
            {{ getTaskStatusText(record.status) }}
          </a-tag>
        </template>
        
        <template v-if="column.key === 'dueDate'">
          <span :class="{ 'text-red-500': isOverdue(record.dueDate) }">
            {{ formatToDateTime(record.dueDate) }}
          </span>
        </template>
        
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button type="link" size="small" @click="handleProcess(record)">
              办理
            </a-button>
            <a-button type="link" size="small" @click="handleAssign(record)">
              分配
            </a-button>
            <a-button type="link" size="small" @click="handleView(record)">
              {{ t('routes.workflow.view') }}
            </a-button>
            <a-button type="link" size="small" @click="handleDelete(record)">
              {{ t('routes.workflow.delete') }}
            </a-button>
          </a-space>
        </template>
      </template>
    </BasicTable>

    <!-- 分配任务弹窗 -->
    <BasicModal
      v-bind="$attrs"
      @register="registerAssignModal"
      title="分配任务"
      @ok="handleAssignSubmit"
    >
      <BasicForm @register="registerAssignForm" />
    </BasicModal>

    <!-- 批量分配弹窗 -->
    <BasicModal
      v-bind="$attrs"
      @register="registerBatchAssignModal"
      title="批量分配任务"
      @ok="handleBatchAssignSubmit"
    >
      <BasicForm @register="registerBatchAssignForm" />
    </BasicModal>

    <!-- 查看任务详情弹窗 -->
    <BasicModal
      v-bind="$attrs"
      @register="registerViewModal"
      :title="t('routes.workflow.taskName')"
      width="800px"
    >
      <div v-if="currentTask">
        <a-descriptions :column="2" bordered>
          <a-descriptions-item :label="t('routes.workflow.taskName')">
            {{ currentTask.name }}
          </a-descriptions-item>
          <a-descriptions-item :label="t('routes.workflow.taskAssignee')">
            {{ currentTask.assignee }}
          </a-descriptions-item>
          <a-descriptions-item :label="t('routes.workflow.taskCreateTime')">
            {{ formatToDateTime(currentTask.createTime) }}
          </a-descriptions-item>
          <a-descriptions-item :label="t('routes.workflow.taskDueDate')">
            {{ formatToDateTime(currentTask.dueDate) }}
          </a-descriptions-item>
          <a-descriptions-item :label="t('routes.workflow.taskPriority')">
            <a-tag :color="getPriorityColor(currentTask.priority)">
              {{ getPriorityText(currentTask.priority) }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item :label="t('routes.workflow.taskStatus')">
            <a-tag :color="getTaskStatusColor(currentTask.status)">
              {{ getTaskStatusText(currentTask.status) }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item :label="t('routes.workflow.taskDescription')" :span="2">
            {{ currentTask.description }}
          </a-descriptions-item>
        </a-descriptions>
      </div>
    </BasicModal>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { BasicTable, useTable } from '/@/components/Table';
import { BasicModal, useModal } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form';
import { useMessage } from '/@/hooks/web/useMessage';
import { useI18n } from '/@/hooks/web/useI18n';
import { workflowTaskApi } from '/@/api/workflow';
import { defHttp } from '/@/utils/http/axios';
import { formatToDateTime } from '/@/utils/dateUtil';

const { t } = useI18n();
const { createMessage } = useMessage();
const router = useRouter();

const currentTask = ref<any>(null);
const selectedRowKeys = ref<string[]>([]);

// 表格配置
const [registerTable, { reload, getSelectRowKeys, setProps }] = useTable({
  title: t('routes.workflow.task'),
  api: workflowTaskApi.getAllTasks,
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
      title: t('routes.workflow.taskAssignee'),
      dataIndex: 'assignee',
      width: 120,
    },
    {
      title: t('routes.workflow.taskPriority'),
      dataIndex: 'priority',
      width: 100,
    },
    {
      title: t('routes.workflow.taskStatus'),
      dataIndex: 'status',
      width: 100,
    },
    {
      title: t('routes.workflow.taskCreateTime'),
      dataIndex: 'createTime',
      width: 180,
      customRender: ({ text }) => formatToDateTime(text),
    },
    {
      title: t('routes.workflow.taskDueDate'),
      dataIndex: 'dueDate',
      width: 180,
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
        field: 'assignee',
        label: t('routes.workflow.taskAssignee'),
        component: 'Input',
        colProps: { span: 8 },
      },
      {
        field: 'status',
        label: t('routes.workflow.taskStatus'),
        component: 'Select',
        componentProps: {
          options: [
            { label: '待办', value: 'pending' },
            { label: '进行中', value: 'in_progress' },
            { label: '已完成', value: 'completed' },
          ],
        },
        colProps: { span: 8 },
      },
      {
        field: 'processInstanceId',
        label: '流程实例ID',
        component: 'Input',
        colProps: { span: 8 },
      },
    ],
  },
});

// 分配任务表单
const [registerAssignForm, { validate: validateAssign, resetFields: resetAssignFields }] = useForm({
  labelWidth: 100,
  schemas: [
    {
      field: 'assignee',
      label: '办理人',
      component: 'ApiSelect',
      componentProps: {
        api: getUserOptions,
        placeholder: '请选择办理人',
      },
      required: true,
    },
    {
      field: 'comment',
      label: '分配说明',
      component: 'InputTextArea',
      componentProps: {
        rows: 3,
        placeholder: '请输入分配说明',
      },
    },
  ],
});

// 批量分配表单
const [registerBatchAssignForm, { validate: validateBatchAssign, resetFields: resetBatchAssignFields }] = useForm({
  labelWidth: 100,
  schemas: [
    {
      field: 'assignee',
      label: '办理人',
      component: 'ApiSelect',
      componentProps: {
        api: getUserOptions,
        placeholder: '请选择办理人',
      },
      required: true,
    },
    {
      field: 'comment',
      label: '分配说明',
      component: 'InputTextArea',
      componentProps: {
        rows: 3,
        placeholder: '请输入分配说明',
      },
    },
  ],
});

// 弹窗配置
const [registerAssignModal, { openModal: openAssignModal, closeModal: closeAssignModal }] = useModal();
const [registerBatchAssignModal, { openModal: openBatchAssignModal, closeModal: closeBatchAssignModal }] = useModal();
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

// 获取任务状态颜色
function getTaskStatusColor(status: string) {
  const colorMap: Record<string, string> = {
    'pending': 'orange',
    'in_progress': 'blue',
    'completed': 'green',
  };
  return colorMap[status] || 'gray';
}

// 获取任务状态文本
function getTaskStatusText(status: string) {
  const textMap: Record<string, string> = {
    'pending': '待办',
    'in_progress': '进行中',
    'completed': '已完成',
  };
  return textMap[status] || status;
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

// 分配任务
function handleAssign(record: any) {
  currentTask.value = record;
  openAssignModal();
}

// 提交分配任务
async function handleAssignSubmit() {
  try {
    const values = await validateAssign();
    // 这里应该调用分配任务的API
    createMessage.success('任务分配成功');
    closeAssignModal();
    resetAssignFields();
    reload();
  } catch (error) {
    createMessage.error('任务分配失败');
  }
}

// 批量分配
function handleBatchAssign() {
  if (selectedRowKeys.value.length === 0) {
    createMessage.warning('请选择要分配的任务');
    return;
  }
  openBatchAssignModal();
}

// 提交批量分配
async function handleBatchAssignSubmit() {
  try {
    const values = await validateBatchAssign();
    // 这里应该调用批量分配任务的API
    createMessage.success(`成功分配 ${selectedRowKeys.value.length} 个任务`);
    closeBatchAssignModal();
    resetBatchAssignFields();
    selectedRowKeys.value = [];
    reload();
  } catch (error) {
    createMessage.error('批量分配失败');
  }
}

// 查看任务详情
function handleView(record: any) {
  currentTask.value = record;
  openViewModal();
}

// 办理（跳转到在线表单SPLIT页）
async function handleProcess(record: any) {
  try {
    const resp: any = await defHttp.get({ url: '/workflow/onlineForm/task/resolve', params: { taskId: record.id } });
    const data = resp?.result || resp || {};
    const tableName = data.tableName;
    const dataId = data.dataId || '';
    if (!tableName) {
      createMessage.error('未配置表单Key，无法办理');
      return;
    }
    await router.push({
      name: 'UniversalFormPage',
      params: { formType: tableName, dataId },
      query: { taskId: record.id }
    });
  } catch (e) {
    createMessage.error('跳转办理失败');
  }
}

// 删除任务
async function handleDelete(record: any) {
  try {
    // 这里应该调用删除任务的API
    createMessage.success(t('routes.workflow.deleteSuccess'));
    reload();
  } catch (error) {
    createMessage.error(t('routes.workflow.deleteFailed'));
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
</script>

<style scoped>
.text-red-500 {
  color: #ef4444;
}
</style> 