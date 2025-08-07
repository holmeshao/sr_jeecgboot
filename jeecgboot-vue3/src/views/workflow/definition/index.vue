<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleGoToDesigner">
          <Icon icon="ant-design:edit-outlined" />
          {{ t('routes.workflow.designer') }}
        </a-button>
        <a-button @click="handleDeploy" v-if="hasPermission('workflow:definition:deploy')">
          <Icon icon="ant-design:plus-outlined" />
          {{ t('routes.workflow.deploy') }}
        </a-button>
      </template>
      
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="getStatusColor(record.suspended)">
            {{ record.suspended ? t('routes.workflow.statusSuspended') : t('routes.workflow.statusActive') }}
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
                icon: 'ant-design:download-outlined',
                tooltip: t('routes.workflow.download'),
                onClick: handleDownload.bind(null, record),
              },
              {
                icon: record.suspended ? 'ant-design:play-circle-outlined' : 'ant-design:pause-circle-outlined',
                tooltip: record.suspended ? t('routes.workflow.activate') : t('routes.workflow.suspend'),
                onClick: handleToggleState.bind(null, record),
                ifShow: hasPermission('workflow:definition:edit'),
              },
              {
                icon: 'ant-design:delete-outlined',
                color: 'error',
                tooltip: t('routes.workflow.delete'),
                popConfirm: {
                  title: '确定要删除这个流程定义吗？',
                  confirm: handleDelete.bind(null, record),
                },
                ifShow: hasPermission('workflow:definition:delete'),
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>

    <!-- 部署流程弹窗 -->
    <BasicModal
      v-bind="$attrs"
      @register="registerDeployModal"
      :title="t('routes.workflow.deploy')"
      @ok="handleDeploySubmit"
    >
      <BasicForm @register="registerDeployForm" />
    </BasicModal>

    <!-- 查看流程详情弹窗 -->
    <BasicModal
      v-bind="$attrs"
      @register="registerViewModal"
      :title="t('routes.workflow.definitionName')"
      width="800px"
    >
      <div v-if="currentRecord">
        <a-descriptions :column="2" bordered>
          <a-descriptions-item :label="t('routes.workflow.definitionName')">
            {{ currentRecord.name }}
          </a-descriptions-item>
          <a-descriptions-item :label="t('routes.workflow.definitionKey')">
            {{ currentRecord.key }}
          </a-descriptions-item>
          <a-descriptions-item :label="t('routes.workflow.definitionVersion')">
            {{ currentRecord.version }}
          </a-descriptions-item>
          <a-descriptions-item :label="t('routes.workflow.definitionCategory')">
            {{ currentRecord.category }}
          </a-descriptions-item>
          <a-descriptions-item :label="t('routes.workflow.definitionDescription')" :span="2">
            {{ currentRecord.description }}
          </a-descriptions-item>
          <a-descriptions-item :label="t('routes.workflow.definitionStatus')">
            <a-tag :color="getStatusColor(currentRecord.suspended)">
              {{ currentRecord.suspended ? t('routes.workflow.statusSuspended') : t('routes.workflow.statusActive') }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item :label="t('routes.workflow.definitionDeployTime')">
            {{ formatToDateTime(currentRecord.deploymentTime) }}
          </a-descriptions-item>
        </a-descriptions>
      </div>
    </BasicModal>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { BasicModal, useModal } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form';
import { usePermission } from '/@/hooks/web/usePermission';
import { useMessage } from '/@/hooks/web/useMessage';
import { useI18n } from '/@/hooks/web/useI18n';
import { workflowDefinitionApi } from '/@/api/workflow';
import { formatToDateTime } from '/@/utils/dateUtil';

const { t } = useI18n();
const { createMessage } = useMessage();
const { hasPermission } = usePermission();
const router = useRouter();

const currentRecord = ref<any>(null);

// 表格配置
const [registerTable, { reload, getSelectRowKeys }] = useTable({
  title: t('routes.workflow.definition'),
  api: workflowDefinitionApi.getList,
  columns: [
    {
      title: t('routes.workflow.definitionName'),
      dataIndex: 'name',
      width: 200,
    },
    {
      title: t('routes.workflow.definitionKey'),
      dataIndex: 'key',
      width: 150,
    },
    {
      title: t('routes.workflow.definitionVersion'),
      dataIndex: 'version',
      width: 100,
    },
    {
      title: t('routes.workflow.definitionCategory'),
      dataIndex: 'category',
      width: 120,
    },
    {
      title: t('routes.workflow.definitionStatus'),
      dataIndex: 'status',
      width: 100,
    },
    {
      title: t('routes.workflow.definitionDeployTime'),
      dataIndex: 'deploymentTime',
      width: 180,
      customRender: ({ text }) => formatToDateTime(text),
    },
    {
      title: t('routes.workflow.definitionDeployUser'),
      dataIndex: 'deployedBy',
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
        label: t('routes.workflow.definitionName'),
        component: 'Input',
        colProps: { span: 8 },
      },
      {
        field: 'key',
        label: t('routes.workflow.definitionKey'),
        component: 'Input',
        colProps: { span: 8 },
      },
      {
        field: 'category',
        label: t('routes.workflow.definitionCategory'),
        component: 'Input',
        colProps: { span: 8 },
      },
    ],
  },
});

// 部署表单配置
const [registerDeployForm, { validate, resetFields }] = useForm({
  labelWidth: 100,
  schemas: [
    {
      field: 'name',
      label: t('routes.workflow.definitionName'),
      component: 'Input',
      required: true,
    },
    {
      field: 'category',
      label: t('routes.workflow.definitionCategory'),
      component: 'Input',
    },
    {
      field: 'description',
      label: t('routes.workflow.definitionDescription'),
      component: 'InputTextArea',
      componentProps: {
        rows: 3,
      },
    },
    {
      field: 'file',
      label: '流程文件',
      component: 'Upload',
      componentProps: {
        api: uploadApi,
        maxSize: 10,
        maxNumber: 1,
        accept: ['.bpmn', '.bpmn20.xml'],
      },
      required: true,
    },
  ],
});

// 弹窗配置
const [registerDeployModal, { openModal: openDeployModal, closeModal: closeDeployModal }] = useModal();
const [registerViewModal, { openModal: openViewModal, closeModal: closeViewModal }] = useModal();

// 获取状态颜色
function getStatusColor(suspended: boolean) {
  return suspended ? 'orange' : 'green';
}

// 跳转到流程设计器
function handleGoToDesigner() {
  router.push('/workflow/designer');
}

// 部署流程
function handleDeploy() {
  openDeployModal();
}

// 提交部署
async function handleDeploySubmit() {
  try {
    const values = await validate();
    await workflowDefinitionApi.deploy(values);
    createMessage.success(t('routes.workflow.deploySuccess'));
    closeDeployModal();
    resetFields();
    reload();
  } catch (error) {
    createMessage.error(t('routes.workflow.deployFailed'));
  }
}

// 查看详情
function handleView(record: any) {
  currentRecord.value = record;
  openViewModal();
}

// 下载流程文件
async function handleDownload(record: any) {
  try {
    const response = await workflowDefinitionApi.getXml(record.id);
    // 处理文件下载
    const blob = new Blob([response], { type: 'application/xml' });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `${record.key}.bpmn20.xml`;
    link.click();
    window.URL.revokeObjectURL(url);
  } catch (error) {
    createMessage.error('下载失败');
  }
}

// 切换状态
async function handleToggleState(record: any) {
  try {
    const action = record.suspended ? 'activate' : 'suspend';
    await workflowDefinitionApi.toggleState(record.id, action);
    createMessage.success(`${action === 'activate' ? '激活' : '挂起'}成功`);
    reload();
  } catch (error) {
    createMessage.error('操作失败');
  }
}

// 删除流程
async function handleDelete(record: any) {
  try {
    await workflowDefinitionApi.delete(record.id);
    createMessage.success(t('routes.workflow.deleteSuccess'));
    reload();
  } catch (error) {
    createMessage.error(t('routes.workflow.deleteFailed'));
  }
}

// 文件上传API（需要根据实际情况配置）
function uploadApi() {
  // 这里应该返回实际的文件上传API
  return Promise.resolve();
}
</script> 