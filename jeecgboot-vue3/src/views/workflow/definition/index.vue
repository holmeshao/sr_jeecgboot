<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-space>
          <a-button type="primary" @click="handleGoToDesigner">
            <Icon icon="ant-design:edit-outlined" />
            {{ t('routes.workflow.designer') }}
          </a-button>
          <a-button @click="handleDeploy" v-if="hasPermission('workflow:definition:deploy')">
            <Icon icon="ant-design:plus-outlined" />
            {{ t('routes.workflow.deploy') }}
          </a-button>
          <a-divider type="vertical" />
          <span>显示全部版本</span>
          <a-switch v-model:checked="showAllVersions" @change="onToggleShowAll" />
        </a-space>
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
                icon: 'ant-design:edit-outlined',
                tooltip: '在线编辑',
                onClick: handleEditInDesigner.bind(null, record),
              },
              {
                icon: 'ant-design:form-outlined',
                tooltip: '编辑标识',
                onClick: openEditKeyModal.bind(null, record),
                ifShow: hasPermission('workflow:definition:rename'),
              },
              {
                icon: 'ant-design:history-outlined',
                tooltip: '版本历史',
                onClick: openModelVersions.bind(null, record),
              },
              {
                icon: 'ant-design:cloud-upload-outlined',
                tooltip: '从模型部署',
                onClick: openDeployFromModel.bind(null, record),
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
              {
                icon: 'ant-design:swap-outlined',
                tooltip: '切换版本',
                onClick: openModelVersions.bind(null, record),
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>

    <!-- 部署流程弹窗 -->
  <BasicModal v-bind="$attrs" @register="registerDeployModal" :title="t('routes.workflow.deploy')" @ok="handleDeploySubmit">
      <!-- 发布来源选择 -->
      <div style="margin-bottom: 12px;">
        <a-radio-group v-model:value="deploySource">
          <a-radio-button value="upload">上传BPMN</a-radio-button>
          <a-radio-button value="modelLatest">模型最新版本</a-radio-button>
          <a-radio-button value="modelVersion">选择模型版本</a-radio-button>
        </a-radio-group>
      </div>

      <!-- 上传来源：沿用原有表单 -->
      <BasicForm v-show="deploySource==='upload'" @register="registerDeployForm" />

      <!-- 模型最新版本来源：选择模型后直接部署 -->
      <div v-if="deploySource==='modelLatest'">
        <a-form layout="vertical">
          <a-form-item label="选择模型" required>
            <a-select
              v-model:value="selectedModelId"
              placeholder="请选择模型（草稿）"
              :options="modelOptions"
              :show-search="true"
              :filter-option="(input,option)=> (option?.label||'').toLowerCase().includes(input.toLowerCase())"
              style="width: 100%"/>
          </a-form-item>
          <a-alert type="info" show-icon :message="`将记录模型版本：${latestModelVersionText}`" />
        </a-form>
      </div>

      <!-- 模型版本来源：先选模型，再打开版本列表弹窗部署 -->
      <div v-if="deploySource==='modelVersion'">
        <a-form layout="vertical">
          <a-form-item label="选择模型" required>
            <a-select
              v-model:value="selectedModelId"
              placeholder="请选择模型（草稿）"
              :options="modelOptions"
              :show-search="true"
              :filter-option="(input,option)=> (option?.label||'').toLowerCase().includes(input.toLowerCase())"
              style="width: 100%"/>
          </a-form-item>
          <a-button type="primary" @click="openModelVersionsFromSelector" :disabled="!selectedModelId">打开版本列表并部署</a-button>
          <a-alert style="margin-top:8px" type="info" show-icon message="在版本列表中点“部署”完成发布。本弹窗的确定按钮在该模式下不执行部署。" />
        </a-form>
      </div>
    </BasicModal>

    <!-- 查看流程详情弹窗 -->
    <BasicModal v-bind="$attrs" @register="registerViewModal" :title="t('routes.workflow.definitionName')" width="800px">
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

    <!-- 编辑标识弹窗 -->
    <BasicModal v-bind="$attrs" @register="registerEditKeyModal" title="编辑流程标识" @ok="handleEditKeySubmit">
      <a-form :model="editKeyForm" layout="vertical">
        <a-form-item label="当前标识">
          <a-input v-model:value="editKeyForm.oldKey" disabled />
        </a-form-item>
        <a-form-item label="新标识" required>
          <a-input v-model:value="editKeyForm.newKey" placeholder="请输入新的processDefinitionKey" />
        </a-form-item>
        <a-alert type="info" show-icon message="将以新标识重新部署（不影响历史实例）。表单绑定请到‘工作流配置’页手动更新 process_key。" />
      </a-form>
    </BasicModal>

    <!-- 模型版本历史弹窗（与“从模型部署”共用） -->
    <BasicModal v-bind="$attrs" @register="registerModelVersionModal" title="模型版本历史" :footer="null" width="900px">
      <a-table :data-source="modelVersionList" :columns="modelVersionColumns" row-key="id" :pagination="false">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-button type="link" @click="deployModelVersion(record)">部署</a-button>
          </template>
        </template>
      </a-table>
    </BasicModal>
  </div>
</template>

<script lang="ts" setup>
  import { ref, reactive, onMounted } from 'vue';
  import { useRouter, useRoute } from 'vue-router';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { BasicModal, useModal } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { workflowDefinitionApi, workflowModelApi } from '/@/api/workflow';
  import { defHttp } from '/@/utils/http/axios';
  import { formatToDateTime } from '/@/utils/dateUtil';

  const { t } = useI18n();
  const { createMessage } = useMessage();
  const { hasPermission } = usePermission();
  const router = useRouter();
  const route = useRoute();
  const deploySource = ref<'upload'|'modelLatest'|'modelVersion'>('upload');
  const selectedModelId = ref<string>('');
  const modelOptions = ref<any[]>([]);
  const latestModelVersionText = ref<string>('—');
  const showAllVersions = ref<boolean>(false);

  const currentRecord = ref<any>(null);
  // 切换版本目标（当前列表选中的流程定义）
  const switchTargetDef = ref<any>(null);
  const editKeyForm = reactive<any>({ oldKey: '', newKey: '' });
  const editingRecord = ref<any>(null);

  // 表格配置
  const [registerTable, { reload, getSelectRowKeys, setTableData }] = useTable({
    title: t('routes.workflow.definition'),
    api: (params:any) => workflowDefinitionApi.getList({ ...params, includeAllVersions: showAllVersions.value }),
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
        title: '模型标识',
        dataIndex: 'modelKey',
        width: 160,
      },
      {
        title: t('routes.workflow.definitionVersion'),
        dataIndex: 'version',
        width: 100,
      },
      {
        title: '模型版本',
        dataIndex: 'modelVersion',
        width: 160,
        customRender: ({ record }) => {
          const mv = record.modelVersion ?? '-';
          return `${mv}`;
        },
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
        width: 220,
        customRender: ({ record }) => null,
      },
      // 操作列改由 actionColumn 统一渲染，避免重复
    ],
    useSearchForm: true,
    showTableSetting: true,
    bordered: true,
    actionColumn: {
      width: 340,
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
  const [registerModelVersionModal, { openModal: openModelVersionModal, closeModal: closeModelVersionModal }] = useModal();
  const [registerEditKeyModal, { openModal: openEditKeyModalInner, closeModal: closeEditKeyModal }] = useModal();

  // 获取状态颜色
  function getStatusColor(suspended: boolean) {
    return suspended ? 'orange' : 'green';
  }

  // 跳转到流程设计器
  function handleGoToDesigner() {
    router.push('/workflow/designer');
  }

  function openEditKeyModal(record: any) {
    editingRecord.value = record;
    editKeyForm.oldKey = record.key;
    editKeyForm.newKey = record.key;
    openEditKeyModalInner();
  }

  async function handleEditKeySubmit() {
    try {
      const rec = editingRecord.value;
      if (!rec || !editKeyForm.newKey || editKeyForm.newKey === editKeyForm.oldKey) {
        closeEditKeyModal();
        return;
      }
      // 改为调用专用后端端点：仅“编辑标识”绕过重复部署校验
      await defHttp.post({ url: `/workflow/definition/${rec.id}/renameAndDeploy`, data: {
        newKey: editKeyForm.newKey,
        newName: rec.name,
        category: rec.category,
        // 透传模型元信息，满足“按模型key/模型版本部署”的审计要求
        modelId: rec.modelId,
        modelKey: rec.modelKey,
        modelVersion: rec.modelVersion,
        deleteOld: false,
      }});
      createMessage.success('已重部署并更新流程标识');
      closeEditKeyModal();
      reload();
    } catch (e) {
      createMessage.error('编辑标识失败');
    }
  }

  // 部署流程
  function handleDeploy() {
    openDeployModal();
  }

  // 提交部署
  async function handleDeploySubmit() {
    try {
      // 根据来源分别处理
      if (deploySource.value === 'upload') {
        const values = await validate();
        await workflowDefinitionApi.deploy(values);
        closeDeployModal();
        resetFields();
      } else if (deploySource.value === 'modelLatest') {
        if (!selectedModelId.value) {
          createMessage.warning('请选择模型');
          return;
        }
        // 拉取版本列表，拿到真正的最新版号
        let latestVersion: number | undefined = undefined;
        try {
          const versions = await workflowModelApi.listVersions(selectedModelId.value);
          latestVersion = Array.isArray(versions) && versions.length
            ? Math.max(...versions.map((v: any) => Number(v.version || 0)))
            : undefined;
        } catch (_) {}

        const xml = await workflowModelApi.getLatestXml(selectedModelId.value);
        const str = typeof xml === 'string' ? xml : (xml?.xml || xml?.result || '');
        if (!str) {
          createMessage.error('未获取到模型最新XML，请先在设计器保存一次');
          return;
        }
        let deployName = 'process_from_model';
        let modelKey: string | undefined = undefined;
        try {
          const list = await workflowModelApi.list('');
          const meta = Array.isArray(list) ? list.find((i: any) => i.id === selectedModelId.value) : null;
          if (meta?.name || meta?.modelKey) deployName = meta.name || meta.modelKey;
          modelKey = meta?.modelKey;
        } catch(e) {}
        await workflowDefinitionApi.deployByXml({ name: deployName, xml: str, modelVersion: latestVersion, modelId: selectedModelId.value, modelKey });
        closeDeployModal();
        resetFields();
      } else if (deploySource.value === 'modelVersion') {
        // 该模式不在“确定”执行，由“版本列表”按钮进入并在列表中部署
        createMessage.info('请在模型版本列表中选择并部署');
        return;
      }
      createMessage.success({ content: t('routes.workflow.deploySuccess'), duration: 1 });
      closeDeployModal();
      resetFields();
      reload();
    } catch (error) {
      createMessage.error({ content: t('routes.workflow.deployFailed'), duration: 1 });
    }
  }

  // ============ 来自设计器的“请求发布”支持（modelId -> 用最新模型XML直接部署） ============
  onMounted(async () => {
    try {
      const mid = (route.query?.modelId as string) || '';
      if (!mid) return;
      // 自动打开部署弹窗，并用模型最新XML走 deployByXml
      openDeployModal();
      deploySource.value = 'modelLatest';
      selectedModelId.value = mid;
      try {
        const xml = await workflowModelApi.getLatestXml(mid);
        if (!xml) {
          createMessage.warning('未找到模型最新XML，已切回手动部署');
          deploySource.value = 'upload';
          return;
        }
        const str = typeof xml === 'string' ? xml : (xml.xml || xml.result || '');
        if (!str) {
          createMessage.warning('模型XML为空，已切回手动部署');
          deploySource.value = 'upload';
          return;
        }
        // 取一个友好名称；若无法获取模型信息则使用默认名
        let deployName = 'process_from_model';
        try {
          const list = await workflowModelApi.list('');
          const meta = Array.isArray(list) ? list.find((i: any) => i.id === mid) : null;
          if (meta?.name || meta?.modelKey) deployName = meta.name || meta.modelKey;
        } catch (e) {}
        await workflowDefinitionApi.deployByXml({ name: deployName, xml: str });
      createMessage.success({ content: '已从模型最新版本部署', duration: 1 });
      closeDeployModal();
        reload();
      } catch (e) {
        createMessage.error('从模型部署失败，请手动上传');
      }
    } catch (e) {}
  });

  // 载入模型下拉数据
  async function loadModelOptions() {
    try {
      const list = await workflowModelApi.list('');
      modelOptions.value = Array.isArray(list) ? list.map((m: any) => ({ label: `${m.name || m.modelKey} (${m.modelKey})`, value: m.id })) : [];
      // 估算“最新版本”提示
      try {
        if (selectedModelId.value) {
          const versions = await workflowModelApi.listVersions(selectedModelId.value);
          const maxV = Array.isArray(versions) && versions.length ? Math.max(...versions.map((v:any)=> Number(v.version||0))) : undefined;
          latestModelVersionText.value = maxV ? `v${maxV}` : '—';
        }
      } catch (e) { latestModelVersionText.value = '—'; }
    } catch (e) {
      modelOptions.value = [];
    }
  }
  loadModelOptions();

  function onToggleShowAll() {
    reload();
  }

  // 从“选择模型版本”模式打开版本弹窗
  async function openModelVersionsFromSelector() {
    if (!selectedModelId.value) return;
    try {
      const list = await workflowModelApi.list('');
      const meta = Array.isArray(list) ? list.find((i: any) => i.id === selectedModelId.value) : null;
      if (!meta) { createMessage.warning('未找到模型'); return; }
      currentModel.value = meta;
      const versions = await workflowModelApi.listVersions(selectedModelId.value);
      modelVersionList.value = Array.isArray(versions) ? versions : [];
      openModelVersionModal();
    } catch (e) {
      createMessage.error('打开模型版本失败');
    }
  }

  // 查看详情
  function handleView(record: any) {
    currentRecord.value = record;
    openViewModal();
  }

  // 从流程定义进入设计器（在线编辑→重新部署）
  function handleEditInDesigner(record: any) {
    router.push({
      path: '/workflow/designer',
      query: { definitionId: record.id },
    });
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

  // ================= 模型版本历史 / 从模型部署 =================
  const currentModel = ref<any>(null);
  const modelVersionList = ref<any[]>([]);
  const modelVersionColumns = [
    { title: '版本', dataIndex: 'version', key: 'version', width: 100 },
    { title: '备注', dataIndex: 'comment', key: 'comment' },
    { title: '创建时间', dataIndex: 'createTime', key: 'createTime', width: 200 },
    { title: '操作', key: 'action', width: 160 },
  ];

  async function openModelVersions(record: any) {
    try {
      // 记录切换的目标定义（用于保持相同processDefinitionKey）
      switchTargetDef.value = record;
      // 1) 优先依据部署元数据中的 modelId
      let model: any = null;
      const modelIdFromMeta = record.modelId as string;
      if (modelIdFromMeta) {
        try {
          const all = await workflowModelApi.list('');
          model = Array.isArray(all) ? all.find((m: any) => m.id === modelIdFromMeta) : null;
        } catch (_) {}
      }
      // 2) 回退按流程标识查找同 key 的模型
      if (!model) {
        try {
          model = await workflowModelApi.getByKey(record.key);
        } catch (_) {}
      }
      if (!model) {
        createMessage.warning('未找到对应的模型，请先在设计器保存为模型');
        return;
      }
      currentModel.value = model;
      let list: any = [];
      try {
        list = await workflowModelApi.listVersions(model.id);
      } catch (_) { list = []; }
      modelVersionList.value = Array.isArray(list) ? list : [];
      openModelVersionModal();
    } catch (_) {
      // 统一兜底，避免重复弹多条
      createMessage.error('加载模型版本失败');
    }
  }

  async function openDeployFromModel(record: any) {
    // 与“版本历史”复用同一弹窗，用户可直接点击“部署”
    await openModelVersions(record);
  }

  async function deployModelVersion(versionRecord: any) {
    try {
      if (!versionRecord?.xml) {
        createMessage.error('所选版本没有XML内容');
        return;
      }
      // 统一改为 JSON 直传 xml，且将 <process id> 强制改为当前定义的 key，避免新建为另一条流程
      const deployName = switchTargetDef.value?.name || currentModel.value?.name || currentModel.value?.modelKey || `process-v${versionRecord.version}`;
      let xmlStr: string = versionRecord.xml as string;
      try {
        const targetKey = (switchTargetDef.value && switchTargetDef.value.key) ? String(switchTargetDef.value.key) : '';
        if (targetKey) {
          // 同时兼容 <process> 或 <bpmn:process>
          const reg = /(<(?:\w+:)?process\b[^>]*\bid=")[^"]+("[^>]*>)/;
          xmlStr = xmlStr.replace(reg, `$1${targetKey}$2`);
        }
      } catch (e) {}
      await workflowDefinitionApi.deployByXml({
        name: deployName,
        xml: xmlStr,
        description: versionRecord.comment || '',
        // 尽量回传模型元信息，便于列表展示“模型标识/模型版本”
        modelVersion: Number(versionRecord.version || 0) || undefined,
        modelId: currentModel.value?.id,
        modelKey: currentModel.value?.modelKey,
      } as any);
      createMessage.success('部署成功');
      closeModelVersionModal();
      reload();
    } catch (e) {
      createMessage.error('部署失败');
    }
  }
</script>
