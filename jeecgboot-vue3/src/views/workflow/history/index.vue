<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-space>
          <a-button type="primary" @click="handleRefresh">
            <Icon icon="ant-design:reload-outlined" />
            刷新
          </a-button>
          <a-button @click="handleExport">
            <Icon icon="ant-design:download-outlined" />
            导出
          </a-button>
        </a-space>
      </template>
      
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'type'">
          <a-tag :color="getTypeColor(record.type)">
            {{ getTypeText(record.type) }}
          </a-tag>
        </template>
        
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button type="link" size="small" @click="handleView(record)">
              {{ t('routes.workflow.view') }}
            </a-button>
          </a-space>
        </template>
      </template>
    </BasicTable>

    <!-- 查看历史详情弹窗 -->
    <BasicModal
      v-bind="$attrs"
      @register="registerViewModal"
      title="历史详情"
      width="800px"
    >
      <div v-if="currentHistory">
        <a-descriptions :column="2" bordered>
          <a-descriptions-item label="历史类型">
            <a-tag :color="getTypeColor(currentHistory.type)">
              {{ getTypeText(currentHistory.type) }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="操作人">
            {{ currentHistory.user }}
          </a-descriptions-item>
          <a-descriptions-item label="操作时间">
            {{ formatToDateTime(currentHistory.time) }}
          </a-descriptions-item>
          <a-descriptions-item label="操作类型">
            {{ currentHistory.action }}
          </a-descriptions-item>
          <a-descriptions-item label="流程实例ID" :span="2">
            {{ currentHistory.processInstanceId }}
          </a-descriptions-item>
          <a-descriptions-item label="任务ID" :span="2">
            {{ currentHistory.taskId || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="备注" :span="2">
            {{ currentHistory.comment || '-' }}
          </a-descriptions-item>
        </a-descriptions>
        
        <a-divider />
        
        <h4>变量变更</h4>
        <a-table 
          :columns="variableColumns" 
          :data-source="currentHistory.variables || []"
          :pagination="false"
          size="small"
        />
      </div>
    </BasicModal>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useMethods } from '/@/hooks/system/useMethods';
import dayjs from 'dayjs';
import { BasicTable, useTable } from '/@/components/Table';
import { BasicModal, useModal } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import { useI18n } from '/@/hooks/web/useI18n';
import { workflowHistoryApi } from '/@/api/workflow';
import { formatToDateTime } from '/@/utils/dateUtil';

const { t } = useI18n();
const { createMessage } = useMessage();

const currentHistory = ref<any>(null);

// 变量变更表格列配置
const variableColumns = [
  {
    title: '变量名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '原值',
    dataIndex: 'oldValue',
    key: 'oldValue',
  },
  {
    title: '新值',
    dataIndex: 'newValue',
    key: 'newValue',
  },
  {
    title: '变更时间',
    dataIndex: 'time',
    key: 'time',
    customRender: ({ text }) => formatToDateTime(text),
  },
];

// 表格配置
const [registerTable, { reload }] = useTable({
  title: t('routes.workflow.history'),
  api: getHistoryList,
  columns: [
    {
      title: '历史类型',
      dataIndex: 'type',
      width: 120,
    },
    {
      title: '操作人',
      dataIndex: 'user',
      width: 120,
    },
    {
      title: '操作类型',
      dataIndex: 'action',
      width: 150,
    },
    {
      title: '操作时间',
      dataIndex: 'time',
      width: 180,
      customRender: ({ text }) => formatToDateTime(text),
    },
    {
      title: '流程实例ID',
      dataIndex: 'processInstanceId',
      width: 200,
    },
    {
      title: '任务ID',
      dataIndex: 'taskId',
      width: 200,
    },
    {
      title: '备注',
      dataIndex: 'comment',
      width: 200,
    },
    {
      title: '操作',
      dataIndex: 'action',
      width: 100,
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
        field: 'type',
        label: '历史类型',
        component: 'Select',
        componentProps: {
          options: [
            { label: '流程历史', value: 'process' },
            { label: '任务历史', value: 'task' },
            { label: '变量历史', value: 'variable' },
          ],
        },
        colProps: { span: 8 },
      },
      {
        field: 'user',
        label: '操作人',
        component: 'Input',
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

// 弹窗配置
const [registerViewModal, { openModal: openViewModal, closeModal: closeViewModal }] = useModal();

// 获取历史列表（模拟数据）
async function getHistoryList(params: any) {
  // 这里应该调用实际的历史记录API
  const mockData = [
    {
      id: '1',
      type: 'process',
      user: '张三',
      action: '启动流程',
      time: '2024-01-15 10:30:00',
      processInstanceId: 'proc_001',
      taskId: null,
      comment: '发起请假申请',
      variables: [
        {
          name: 'leaveDays',
          oldValue: null,
          newValue: '3',
          time: '2024-01-15 10:30:00',
        },
      ],
    },
    {
      id: '2',
      type: 'task',
      user: '李四',
      action: '完成任务',
      time: '2024-01-15 14:20:00',
      processInstanceId: 'proc_001',
      taskId: 'task_001',
      comment: '同意请假申请',
      variables: [
        {
          name: 'approved',
          oldValue: null,
          newValue: 'true',
          time: '2024-01-15 14:20:00',
        },
      ],
    },
  ];
  
  return {
    items: mockData,
    total: mockData.length,
  };
}

// 获取类型颜色
function getTypeColor(type: string) {
  const colorMap: Record<string, string> = {
    'process': 'blue',
    'task': 'green',
    'variable': 'orange',
  };
  return colorMap[type] || 'gray';
}

// 获取类型文本
function getTypeText(type: string) {
  const textMap: Record<string, string> = {
    'process': '流程历史',
    'task': '任务历史',
    'variable': '变量历史',
  };
  return textMap[type] || type;
}

// 刷新
function handleRefresh() {
  reload();
}

// 导出
function handleExport() {
  try {
    // 基于JeecgBoot导出机制实现流程历史导出
    const { handleExportXls } = useMethods();
    
    const exportParams = {
      processInstanceId: record.processInstanceId,
      includeDetails: true
    };

    const fileName = `流程历史_${record.processDefinitionName}_${dayjs().format('YYYY-MM-DD-HH-mm-ss')}`;
    await handleExportXls(fileName, '/workflow/history/export', exportParams);
    
    createMessage.success('导出成功');
  } catch (error) {
    console.error('导出失败:', error);
    createMessage.error('导出失败，请重试');
  }
}

// 查看详情
function handleView(record: any) {
  currentHistory.value = record;
  openViewModal();
}
</script> 