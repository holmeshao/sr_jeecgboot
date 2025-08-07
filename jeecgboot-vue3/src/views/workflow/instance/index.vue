<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleStart" v-if="hasPermission('workflow:instance:start')">
          <Icon icon="ant-design:play-circle-outlined" />
          {{ t('routes.workflow.start') }}
        </a-button>
      </template>
      
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="getStatusColor(record.status)">
            {{ getStatusText(record.status) }}
          </a-tag>
        </template>
        
        <template v-if="column.key === 'action'">
          <TableAction
            :actions="[
              {
                icon: 'clarity:eye-line',
                tooltip: t('routes.workflow.view'),
                onClick: handleView.bind(null, record),
              },
              {
                icon: 'ant-design:play-circle-outlined',
                tooltip: t('routes.workflow.start'),
                onClick: handleStart.bind(null, record),
                ifShow: record.status === 'suspended' && hasPermission('workflow:instance:start'),
              },
              {
                icon: 'ant-design:stop-outlined',
                tooltip: t('routes.workflow.terminate'),
                onClick: handleTerminate.bind(null, record),
                ifShow: record.status === 'running' && hasPermission('workflow:instance:terminate'),
              },
              {
                icon: 'ant-design:delete-outlined',
                color: 'error',
                tooltip: t('routes.workflow.delete'),
                popConfirm: {
                  title: '确定要删除这个流程实例吗？',
                  confirm: handleDelete.bind(null, record),
                },
                ifShow: hasPermission('workflow:instance:delete'),
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>

    <!-- 启动流程弹窗 -->
    <BasicModal
      v-bind="$attrs"
      @register="registerStartModal"
      :title="t('routes.workflow.start')"
      @ok="handleStartSubmit"
    >
      <BasicForm @register="registerStartForm" />
    </BasicModal>

    <!-- 查看实例详情弹窗 -->
    <BasicModal
      v-bind="$attrs"
      @register="registerViewModal"
      :title="t('routes.workflow.instanceName')"
      width="800px"
    >
      <div v-if="currentInstance">
        <a-tabs>
          <a-tab-pane key="basic" tab="基本信息">
            <a-descriptions :column="2" bordered>
              <a-descriptions-item :label="t('routes.workflow.instanceId')">
                {{ currentInstance.id }}
              </a-descriptions-item>
              <a-descriptions-item :label="t('routes.workflow.instanceName')">
                {{ currentInstance.name }}
              </a-descriptions-item>
              <a-descriptions-item :label="t('routes.workflow.instanceStatus')">
                <a-tag :color="getStatusColor(currentInstance.status)">
                  {{ getStatusText(currentInstance.status) }}
                </a-tag>
              </a-descriptions-item>
              <a-descriptions-item :label="t('routes.workflow.instanceStartTime')">
                {{ formatToDateTime(currentInstance.startTime) }}
              </a-descriptions-item>
              <a-descriptions-item :label="t('routes.workflow.instanceStartUser')">
                {{ currentInstance.startUser }}
              </a-descriptions-item>
              <a-descriptions-item :label="t('routes.workflow.instanceBusinessKey')">
                {{ currentInstance.businessKey }}
              </a-descriptions-item>
            </a-descriptions>
          </a-tab-pane>
          
          <a-tab-pane key="diagram" tab="流程图">
            <div style="height: 400px; border: 1px solid #d9d9d9; display: flex; align-items: center; justify-content: center;">
              <div style="text-align: center;">
                <h3>流程图显示区域</h3>
                <p>这里可以集成 BPMN.js 来显示实际的流程图</p>
                <p>实例ID: {{ currentInstance.id }}</p>
              </div>
            </div>
          </a-tab-pane>
          
          <a-tab-pane key="history" tab="流程历史">
            <a-timeline>
              <a-timeline-item 
                v-for="item in processHistory" 
                :key="item.id"
                :color="getHistoryColor(item.type)"
              >
                <template #dot>
                  <Icon :icon="getHistoryIcon(item.type)" />
                </template>
                <div>
                  <div class="font-medium">{{ item.action }}</div>
                  <div class="text-gray-500 text-sm">
                    {{ item.user }} - {{ formatToDateTime(item.time) }}
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
import { ref } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { BasicModal, useModal } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form';
import { usePermission } from '/@/hooks/web/usePermission';
import { useMessage } from '/@/hooks/web/useMessage';
import { useI18n } from '/@/hooks/web/useI18n';
import { workflowInstanceApi, workflowHistoryApi } from '/@/api/workflow';
import { formatToDateTime } from '/@/utils/dateUtil';

const { t } = useI18n();
const { createMessage } = useMessage();
const { hasPermission } = usePermission();

const currentInstance = ref<any>(null);
const processHistory = ref<any[]>([]);

// 表格配置
const [registerTable, { reload }] = useTable({
  title: t('routes.workflow.instance'),
  api: workflowInstanceApi.getList,
  columns: [
    {
      title: t('routes.workflow.instanceId'),
      dataIndex: 'id',
      width: 200,
    },
    {
      title: t('routes.workflow.instanceName'),
      dataIndex: 'name',
      width: 150,
    },
    {
      title: t('routes.workflow.instanceStatus'),
      dataIndex: 'status',
      width: 100,
    },
    {
      title: t('routes.workflow.instanceStartTime'),
      dataIndex: 'startTime',
      width: 180,
      customRender: ({ text }) => formatToDateTime(text),
    },
    {
      title: t('routes.workflow.instanceEndTime'),
      dataIndex: 'endTime',
      width: 180,
      customRender: ({ text }) => formatToDateTime(text),
    },
    {
      title: t('routes.workflow.instanceStartUser'),
      dataIndex: 'startUser',
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
  actionColumn: {
    width: 200,
    title: '操作',
    dataIndex: 'action',
  },
  formConfig: {
    labelWidth: 100,
    schemas: [
      {
        field: 'name',
        label: t('routes.workflow.instanceName'),
        component: 'Input',
        colProps: { span: 8 },
      },
      {
        field: 'status',
        label: t('routes.workflow.instanceStatus'),
        component: 'Select',
        componentProps: {
          options: [
            { label: '运行中', value: 'running' },
            { label: '已完成', value: 'completed' },
            { label: '已终止', value: 'terminated' },
            { label: '已挂起', value: 'suspended' },
          ],
        },
        colProps: { span: 8 },
      },
    ],
  },
});

// 启动流程表单
const [registerStartForm, { validate, resetFields }] = useForm({
  labelWidth: 100,
  schemas: [
    {
      field: 'processDefinitionKey',
      label: '流程定义',
      component: 'ApiSelect',
      componentProps: {
        api: getProcessDefinitionOptions,
        placeholder: '请选择流程定义',
      },
      required: true,
    },
    {
      field: 'businessKey',
      label: t('routes.workflow.instanceBusinessKey'),
      component: 'Input',
      componentProps: {
        placeholder: '请输入业务标识',
      },
    },
    {
      field: 'variables',
      label: t('routes.workflow.formVariables'),
      component: 'InputTextArea',
      componentProps: {
        rows: 4,
        placeholder: '请输入流程变量（JSON格式）',
      },
    },
  ],
});

// 弹窗配置
const [registerStartModal, { openModal: openStartModal, closeModal: closeStartModal }] = useModal();
const [registerViewModal, { openModal: openViewModal, closeModal: closeViewModal }] = useModal();

// 获取状态颜色
function getStatusColor(status: string) {
  const colorMap: Record<string, string> = {
    'running': 'green',
    'completed': 'blue',
    'terminated': 'red',
    'suspended': 'orange',
  };
  return colorMap[status] || 'gray';
}

// 获取状态文本
function getStatusText(status: string) {
  const textMap: Record<string, string> = {
    'running': '运行中',
    'completed': '已完成',
    'terminated': '已终止',
    'suspended': '已挂起',
  };
  return textMap[status] || status;
}

// 启动流程
function handleStart(record?: any) {
  openStartModal();
}

// 提交启动流程
async function handleStartSubmit() {
  try {
    const values = await validate();
    await workflowInstanceApi.start(values);
    createMessage.success(t('routes.workflow.startSuccess'));
    closeStartModal();
    resetFields();
    reload();
  } catch (error) {
    createMessage.error(t('routes.workflow.startFailed'));
  }
}

// 查看详情
async function handleView(record: any) {
  currentInstance.value = record;
  
  try {
    // 获取流程历史
    const history = await workflowHistoryApi.getProcessHistory(record.id);
    processHistory.value = history;
    
    openViewModal();
  } catch (error) {
    createMessage.error('获取流程详情失败');
  }
}

// 终止流程
async function handleTerminate(record: any) {
  try {
    await workflowInstanceApi.terminate(record.id, '手动终止');
    createMessage.success('流程终止成功');
    reload();
  } catch (error) {
    createMessage.error('流程终止失败');
  }
}

// 删除流程
async function handleDelete(record: any) {
  try {
    await workflowInstanceApi.delete(record.id);
    createMessage.success(t('routes.workflow.deleteSuccess'));
    reload();
  } catch (error) {
    createMessage.error(t('routes.workflow.deleteFailed'));
  }
}

// 获取流程定义选项
async function getProcessDefinitionOptions() {
  // 这里应该调用实际的流程定义列表API
  return [
    { label: '请假申请流程', value: 'leave_process' },
    { label: '报销申请流程', value: 'expense_process' },
    { label: '采购申请流程', value: 'purchase_process' },
  ];
}

// 获取历史记录颜色
function getHistoryColor(type: string) {
  const colorMap: Record<string, string> = {
    'start': 'green',
    'complete': 'blue',
    'terminate': 'red',
    'suspend': 'orange',
  };
  return colorMap[type] || 'gray';
}

// 获取历史记录图标
function getHistoryIcon(type: string) {
  const iconMap: Record<string, string> = {
    'start': 'ant-design:play-circle-outlined',
    'complete': 'ant-design:check-circle-outlined',
    'terminate': 'ant-design:stop-circle-outlined',
    'suspend': 'ant-design:pause-circle-outlined',
  };
  return iconMap[type] || 'ant-design:clock-circle-outlined';
}
</script> 