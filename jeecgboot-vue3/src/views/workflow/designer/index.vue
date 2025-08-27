<template>
  <div class="workflow-designer">
    <!-- 顶部工具栏 -->
    <div class="designer-toolbar">
      <div class="toolbar-left">
        <a-button type="primary" @click="createNew">
          <Icon icon="ant-design:plus-outlined" />
          新建流程
        </a-button>
        <a-button @click="importBpmn">
          <Icon icon="ant-design:import-outlined" />
          导入BPMN
        </a-button>
        <a-button @click="exportBpmn" :disabled="!hasProcess">
          <Icon icon="ant-design:export-outlined" />
          导出BPMN
        </a-button>
        <a-divider type="vertical" />
        <a-button @click="saveDraft" :disabled="!hasProcess" :loading="saving">
          <Icon icon="ant-design:save-outlined" />
          保存草稿
        </a-button>
        <a-button type="primary" @click="deployProcess" :disabled="!hasProcess" :loading="deploying">
          <Icon icon="ant-design:cloud-upload-outlined" />
          部署流程
        </a-button>
        <a-button @click="openVersionHistory">
          <Icon icon="ant-design:history-outlined" />
          版本历史
        </a-button>
        <a-button @click="openDraftBox">
          <Icon icon="ant-design:folder-open-outlined" />
          草稿箱
        </a-button>
      </div>
      <div class="toolbar-right">
        <a-button @click="togglePropertiesPanel">
          <Icon icon="ant-design:setting-outlined" />
          {{ showProperties ? '隐藏' : '显示' }}属性面板
        </a-button>
        <a-button @click="zoomReset">
          <Icon icon="ant-design:compress-outlined" />
          重置视图
        </a-button>
      </div>
    </div>

    <!-- 设计器主体 -->
    <div class="designer-container">
      <!-- BPMN画布 -->
      <div ref="bpmnContainer" class="bpmn-canvas" :class="{ 'with-properties': showProperties }"></div>

      <!-- 属性面板 -->
      <div v-show="showProperties" ref="propertiesContainer" class="properties-panel"></div>
    </div>

    <!-- 导入文件对话框 -->
    <input ref="fileInput" type="file" accept=".bpmn,.bpmn20.xml" style="display: none" @change="handleFileImport" />

    <!-- 保存流程弹窗 -->
    <BasicModal v-bind="$attrs" @register="registerSaveModal" title="保存流程" @ok="handleSaveSubmit">
      <BasicForm @register="registerSaveForm" />
    </BasicModal>

    <!-- 草稿箱（模型仓库）弹窗 -->
    <BasicModal v-bind="$attrs" @register="registerDraftModal" title="模型草稿" width="900px" @ok="handleDraftConfirm">
      <div style="margin-bottom: 12px; display:flex; gap:8px; align-items:center;">
        <a-input v-model:value="draftKeyword" placeholder="按模型Key/名称搜索" style="width: 260px;" />
        <a-button type="primary" @click="loadDraftList">查询</a-button>
      </div>
      <a-table :data-source="draftList" :columns="draftColumns" row-key="id" :pagination="false" :row-selection="draftRowSelection" />
    </BasicModal>

    <!-- 部署流程弹窗 -->
    <BasicModal v-bind="$attrs" @register="registerDeployModal" title="部署流程" @ok="handleDeploySubmit">
      <BasicForm @register="registerDeployForm" />
    </BasicModal>

    <!-- 版本历史弹窗 -->
    <BasicModal v-bind="$attrs" @register="registerVersionModal" title="版本历史" :footer="null" width="900px">
      <a-table :data-source="versionList" :columns="versionColumns" row-key="id" :pagination="false">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-button type="link" @click="onClickLoadVersion(record)">载入</a-button>
            <a-button type="link" @click="deployVersion(record)">部署</a-button>
          </template>
        </template>
      </a-table>
    </BasicModal>
  </div>
</template>

<script lang="ts" setup>
  import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue';
  import { useRoute } from 'vue-router';
  import { workflowDefinitionApi, workflowModelApi } from '/@/api/workflow';
  // bpmn 样式（画布/字体/属性面板）
  import 'bpmn-js/dist/assets/diagram-js.css';
  import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css';
  import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css';
  import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css';
  import '@bpmn-io/properties-panel/dist/assets/properties-panel.css';
  // bpmn-js 与属性面板（改为静态导入，避免运行时动态导入失败）
  // 兼容 bpmn-js-properties-panel v5+ 的官方写法
  // https://github.com/bpmn-io/bpmn-js-examples/tree/master/properties-panel
  import BpmnModeler from 'bpmn-js/lib/Modeler';
  import { BpmnPropertiesPanelModule, BpmnPropertiesProviderModule } from 'bpmn-js-properties-panel';
  import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda.json';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicModal, useModal } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';

  // 动态导入bpmn-js相关模块
  // 上述静态导入已生效，无需在运行时动态赋值

  const { createMessage } = useMessage();
  const route = useRoute();

  // 组件状态
  const bpmnContainer = ref<HTMLElement>();
  const propertiesContainer = ref<HTMLElement>();
  const fileInput = ref<HTMLInputElement>();
  const showProperties = ref(true);
  const hasProcess = ref(false);
  const saving = ref(false);
  const deploying = ref(false);
  const savingDraftToServer = ref(false);

  let modeler: any = null;
  let propertiesPanel: any = null;

  // 表单配置
  const [registerSaveForm, { validate: validateSave, resetFields: resetSaveFields, setFieldsValue: setSaveFieldsValue, updateSchema: updateSaveSchema }] = useForm({
    labelWidth: 100,
    schemas: [
      {
        field: 'id',
        label: 'ID',
        component: 'Input',
        show: false,
      },
      {
        field: 'name',
        label: '流程名称',
        component: 'Input',
        required: true,
        componentProps: {
          placeholder: '请输入流程名称',
        },
      },
      {
        field: 'key',
        label: '流程标识',
        component: 'Input',
        required: true,
        componentProps: {
          placeholder: '请输入流程唯一标识',
        },
      },
      {
        field: 'category',
        label: '流程分类',
        component: 'Input',
        componentProps: {
          placeholder: '请输入流程分类',
        },
      },
      {
        field: 'description',
        label: '流程描述',
        component: 'InputTextArea',
        componentProps: {
          rows: 3,
          placeholder: '请输入流程描述',
        },
      },
    ],
  });

  const [registerDeployForm, { validate: validateDeploy, resetFields: resetDeployFields }] = useForm({
    labelWidth: 100,
    schemas: [
      {
        field: 'name',
        label: '部署名称',
        component: 'Input',
        required: true,
        componentProps: {
          placeholder: '请输入部署名称',
        },
      },
      {
        field: 'category',
        label: '流程分类',
        component: 'Input',
        componentProps: {
          placeholder: '请输入流程分类',
        },
      },
      {
        field: 'description',
        label: '部署描述',
        component: 'InputTextArea',
        componentProps: {
          rows: 3,
          placeholder: '请输入部署描述',
        },
      },
    ],
  });

  // 弹窗配置
  const [registerSaveModal, { openModal: openSaveModal, closeModal: closeSaveModal }] = useModal();
  const [registerDeployModal, { openModal: openDeployModal, closeModal: closeDeployModal }] = useModal();
  const [registerVersionModal, { openModal: openVersionModal, closeModal: closeVersionModal }] = useModal();
  const [registerDraftModal, { openModal: openDraftModal, closeModal: closeDraftModal }] = useModal();

  // 默认BPMN模板
  const defaultBpmnXml = `<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             xmlns:flowable="http://flowable.org/bpmn"
             xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
             xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC"
             xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI"
             typeLanguage="http://www.w3.org/2001/XMLSchema"
             expressionLanguage="http://www.w3.org/1999/XPath"
             targetNamespace="http://flowable.org/bpmn">
  <process id="process_1" name="新建流程" isExecutable="true">
    <startEvent id="startEvent1" name="开始"/>
    <endEvent id="endEvent1" name="结束"/>
    <sequenceFlow id="flow1" sourceRef="startEvent1" targetRef="endEvent1"/>
  </process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_process_1">
    <bpmndi:BPMNPlane bpmnElement="process_1" id="BPMNPlane_process_1">
      <bpmndi:BPMNShape bpmnElement="startEvent1" id="BPMNShape_startEvent1">
        <omgdc:Bounds height="36.0" width="36.0" x="100.0" y="100.0"/>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="endEvent1" id="BPMNShape_endEvent1">
        <omgdc:Bounds height="36.0" width="36.0" x="300.0" y="100.0"/>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge bpmnElement="flow1" id="BPMNEdge_flow1">
        <omgdi:waypoint x="136.0" y="118.0"/>
        <omgdi:waypoint x="300.0" y="118.0"/>
      </bpmndi:BPMNEdge>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</definitions>`;

  // 初始化设计器（静态导入版本）
  async function initBpmnModeler() {
    try {
      modeler = new (BpmnModeler as any)({
        container: bpmnContainer.value,
        keyboard: { bindTo: window },
        propertiesPanel: { parent: propertiesContainer.value },
        additionalModules: [
          (BpmnPropertiesPanelModule as any),
          (BpmnPropertiesProviderModule as any),
        ],
        moddleExtensions: {
          camunda: camundaModdleDescriptor as any,
        },
      });

      modeler.on('import.done', () => {
        hasProcess.value = true;
        zoomReset();
      });

      modeler.on('commandStack.changed', () => {
        hasProcess.value = true;
      });

      // 如果从流程定义进入，加载已部署流程XML
      const defId = (route.query?.definitionId as string) || '';
      const modelIdFromRoute = (route.query?.modelId as string) || '';
      if (defId) {
        await loadDefinitionXml(defId);
      } else if (modelIdFromRoute) {
        await loadModelLatestXml(modelIdFromRoute);
      } else {
        await createNew();
      }
    } catch (error) {
      console.error('初始化BPMN设计器失败:', error);
      createMessage.error('初始化流程设计器失败，请确认依赖安装并刷新页面');
    }
  }

  // 从已部署流程加载XML
  async function loadDefinitionXml(definitionId: string) {
    try {
      const xml = await workflowDefinitionApi.getXml(definitionId as any);
      if (!xml) {
        createMessage.error('未获取到流程XML');
        await createNew();
        return;
      }
      await modeler.importXML(typeof xml === 'string' ? xml : (xml.xml || xml.result || ''));
      hasProcess.value = true;
      createMessage.success('已加载流程定义，可在线编辑后重新部署生成新版本');
    } catch (e) {
      console.error('加载流程XML失败:', e);
      createMessage.error('加载流程XML失败，已回退为新建流程');
      await createNew();
    }
  }

  // 创建新流程
  async function createNew() {
    try {
      await modeler.importXML(defaultBpmnXml);
      hasProcess.value = true;
      createMessage.success('已创建新的流程模板');
    } catch (error) {
      console.error('创建新流程失败:', error);
      createMessage.error('创建新流程失败');
    }
  }

  // 导入BPMN文件
  function importBpmn() {
    fileInput.value?.click();
  }

  // 处理文件导入
  async function handleFileImport(event: Event) {
    const target = event.target as HTMLInputElement;
    const file = target.files?.[0];

    if (file) {
      try {
        const text = await file.text();
        await modeler.importXML(text);
        hasProcess.value = true;
        createMessage.success('BPMN文件导入成功');
      } catch (error) {
        console.error('导入BPMN文件失败:', error);
        createMessage.error('导入BPMN文件失败，请检查文件格式');
      }
    }

    // 清空文件输入框
    target.value = '';
  }

  // 导出BPMN
  async function exportBpmn() {
    try {
      const result = await modeler.saveXML({ format: true });
      const blob = new Blob([result.xml], { type: 'application/xml' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = 'process.bpmn';
      link.click();
      URL.revokeObjectURL(url);
      createMessage.success('BPMN文件导出成功');
    } catch (error) {
      console.error('导出BPMN失败:', error);
      createMessage.error('导出BPMN失败');
    }
  }

  // 保存草稿
  function saveDraft() {
    // 如果是从草稿进入（已有模型ID），则回填并锁定流程标识
    if (currentModelId.value) {
      try {
        setSaveFieldsValue({
          id: currentModelId.value,
          name: currentModelMeta.value?.name,
          key: currentModelMeta.value?.modelKey,
          category: currentModelMeta.value?.category,
        });
      } catch (e) {}
      try {
        updateSaveSchema([
          { field: 'key', componentProps: { disabled: true } },
        ] as any);
      } catch (e) {}
    } else {
      try {
        updateSaveSchema([
          { field: 'key', componentProps: { disabled: false } },
        ] as any);
      } catch (e) {}
    }
    openSaveModal();
  }

  // 处理保存提交
  async function handleSaveSubmit() {
    try {
      const values = await validateSave();
      const result = await modeler.saveXML({ format: true });

      // 保存到后端模型仓库：先保存/更新模型，再新增版本
      savingDraftToServer.value = true;
      const modelResp = await workflowModelApi.saveModel({
        id: values.id,
        modelKey: values.key,
        name: values.name,
        category: values.category,
      });
      const modelId = modelResp || values.id;
      // 更新本地模型元数据与当前模型ID
      if (modelId) {
        currentModelId.value = modelId as any;
        currentModelMeta.value = {
          id: modelId,
          modelKey: values.key,
          name: values.name,
          category: values.category,
        } as any;
      }
      await workflowModelApi.createVersion(modelId, { xml: result.xml, comment: values.description });

      createMessage.success('流程草稿保存成功（已存服务器版本）');
      closeSaveModal();
      resetSaveFields();
    } catch (error) {
      console.error('保存流程失败:', error);
      createMessage.error('保存流程失败');
    } finally {
      savingDraftToServer.value = false;
    }
  }

  // 部署流程
  function deployProcess() {
    openDeployModal();
  }

  // 处理部署提交
  async function handleDeploySubmit() {
    try {
      deploying.value = true;
      const values = await validateDeploy();
      const result = await modeler.saveXML({ format: true });

      // 创建FormData用于文件上传
      const formData = new FormData();
      const blob = new Blob([result.xml], { type: 'application/xml' });
      formData.append('file', blob, `${values.name || 'process'}.bpmn`);
      formData.append('name', values.name);
      if (values.category) {
        formData.append('category', values.category);
      }

      // 调用部署API
      await workflowDefinitionApi.deploy(formData);

      createMessage.success('流程部署成功');
      closeDeployModal();
      resetDeployFields();
    } catch (error) {
      console.error('部署流程失败:', error);
      createMessage.error('部署流程失败');
    } finally {
      deploying.value = false;
    }
  }

  // 切换属性面板
  function togglePropertiesPanel() {
    showProperties.value = !showProperties.value;
    nextTick(() => {
      modeler?.get('canvas').resized();
    });
  }

  // ============ 版本历史 ============
  const currentModelId = ref<string>('');
  const currentModelMeta = ref<any>(null);
  const versionList = ref<any[]>([]);
  const versionColumns = [
    { title: '版本', dataIndex: 'version', width: 100 },
    { title: '备注', dataIndex: 'comment' },
    { title: '创建时间', dataIndex: 'createTime', width: 200 },
    { title: '操作', key: 'action', width: 200 },
  ];

  async function openVersionHistory() {
    if (!currentModelId.value) {
      createMessage.info('当前流程尚未保存为模型，没有版本历史');
      return;
    }
    try {
      const list = await workflowModelApi.listVersions(currentModelId.value);
      versionList.value = list || [];
      openVersionModal();
    } catch (e) {
      createMessage.error('加载版本历史失败');
    }
  }

  // 表格 bodyCell 渲染函数（避免 JSX 语法）
  function renderVersionBodyCell({ column, record }: any) {
    if (column?.key !== 'action') return null;
    return (
      // @ts-ignore 使用 vue 的 h 渲染函数
      window['Vue'].h(
        'a-space',
        null,
        [
          window['Vue'].h('a-button', { type: 'link', onClick: () => onClickLoadVersion(record) }, '载入'),
          window['Vue'].h('a-button', { type: 'link', onClick: () => deployVersion(record) }, '部署'),
        ],
      )
    );
  }

  async function onClickLoadVersion(record: any) {
    try {
      if (!record?.xml) return;
      await modeler.importXML(record.xml);
      createMessage.success(`已载入版本 ${record.version}`);
    } catch (e) {
      createMessage.error('载入版本失败');
    }
  }

  async function deployVersion(record: any) {
    try {
      if (!record?.xml) return;
      const blob = new Blob([record.xml], { type: 'application/xml' });
      const fd = new FormData();
      fd.append('file', blob, `model-v${record.version}.bpmn`);
      await workflowDefinitionApi.deploy(fd as any);
      createMessage.success('部署成功');
      closeVersionModal();
    } catch (e) {
      createMessage.error('部署失败');
    }
  }

  // 重置视图
  function zoomReset() {
    const canvas = modeler?.get('canvas');
    canvas?.zoom('fit-viewport');
  }

  // 组件挂载
  onMounted(() => {
    nextTick(() => {
      initBpmnModeler();
    });
  });

  // 组件卸载
  onBeforeUnmount(() => {
    modeler?.destroy();
  });

  // ============ 草稿箱逻辑 ============
  const draftKeyword = ref<string>('');
  const draftList = ref<any[]>([]);
  const draftColumns = [
    { title: '模型标识', dataIndex: 'modelKey', width: 180 },
    { title: '名称', dataIndex: 'name', width: 200 },
    { title: '分类', dataIndex: 'category', width: 140 },
    { title: '最新版本', dataIndex: 'latestVersion', width: 100 },
    { title: '更新时间', dataIndex: 'updateTime', width: 200 },
  ];

  const selectedDraftRowKeys = ref<string[]>([]);
  const draftRowSelection = {
    type: 'radio' as const,
    selectedRowKeys: selectedDraftRowKeys,
    onChange: (keys: string[]) => {
      selectedDraftRowKeys.value = keys;
    },
  };

  async function openDraftBox() {
    await loadDraftList();
    openDraftModal();
  }

  async function loadDraftList() {
    try {
      const list = await workflowModelApi.list(draftKeyword.value || '');
      draftList.value = list || [];
    } catch (e) {
      createMessage.error('加载模型列表失败');
    }
  }

  async function loadModelLatestXml(modelId: string) {
    try {
      const xml = await workflowModelApi.getLatestXml(modelId);
      if (xml) {
        await modeler.importXML(typeof xml === 'string' ? xml : (xml.xml || xml.result || ''));
        currentModelId.value = modelId;
        hasProcess.value = true;
        createMessage.success('已载入模型最新版本');
        // 尝试补全元数据（用于保存时回填）
        try {
          if (!currentModelMeta.value || currentModelMeta.value.id !== modelId) {
            const list = await workflowModelApi.list('');
            const meta = Array.isArray(list) ? list.find((i: any) => i.id === modelId) : null;
            if (meta) currentModelMeta.value = meta;
          }
        } catch (e) {}
      } else {
        createMessage.warning('模型没有XML内容，已初始化为空白流程');
        await createNew();
      }
    } catch (e) {
      createMessage.error('加载模型XML失败');
    }
  }

  async function handleDraftConfirm() {
    if (!selectedDraftRowKeys.value.length) {
      createMessage.warning('请先选择一条草稿');
      return;
    }
    const modelId = selectedDraftRowKeys.value[0];
    await loadModelLatestXml(modelId);
    closeDraftModal();
  }
</script>

<style lang="less" scoped>
  .workflow-designer {
    height: 100vh;
    display: flex;
    flex-direction: column;
    background: #f5f5f5;

    .designer-toolbar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 16px;
      background: #fff;
      border-bottom: 1px solid #e8e8e8;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

      .toolbar-left,
      .toolbar-right {
        display: flex;
        align-items: center;
        gap: 8px;
      }
    }

    .designer-container {
      flex: 1;
      display: flex;
      height: calc(100vh - 65px);

      .bpmn-canvas {
        flex: 1;
        background: #fff;
        border-right: 1px solid #e8e8e8;

        &.with-properties {
          width: calc(100% - 300px);
        }

        :deep(.djs-palette) {
          border-radius: 4px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
        }

        :deep(.djs-context-pad) {
          border-radius: 4px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
        }
      }

      .properties-panel {
        width: 300px;
        background: #fff;
        border-left: 1px solid #e8e8e8;
        overflow-y: auto;

        :deep(.bio-properties-panel) {
          height: 100%;

          .bio-properties-panel-header {
            background: #fafafa;
            border-bottom: 1px solid #e8e8e8;
            padding: 12px 16px;
            font-weight: 500;
          }

          .bio-properties-panel-group {
            border-bottom: 1px solid #f0f0f0;

            .bio-properties-panel-group-header {
              background: #fafafa;
              padding: 8px 16px;
              font-weight: 500;
              font-size: 12px;
              text-transform: uppercase;
              color: #666;
            }

            .bio-properties-panel-entry {
              padding: 8px 16px;

              .bio-properties-panel-label {
                font-size: 12px;
                color: #666;
                margin-bottom: 4px;
              }

              .bio-properties-panel-input {
                width: 100%;
                padding: 4px 8px;
                border: 1px solid #d9d9d9;
                border-radius: 2px;
                font-size: 12px;

                &:focus {
                  border-color: #1890ff;
                  outline: none;
                  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
                }
              }
            }
          }
        }
      }
    }
  }

  // BPMN设计器基础样式 - 确保能正常显示
  :deep(.djs-container) {
    font-family: Arial, sans-serif;
  }

  :deep(.djs-palette) {
    border: 1px solid #ccc;
    background: white;
    border-radius: 4px;
  }

  :deep(.djs-palette .entry) {
    cursor: pointer;
    padding: 6px;
  }

  :deep(.djs-palette .entry:hover) {
    background: #f0f0f0;
  }

  :deep(.bjs-powered-by) {
    display: none !important;
  }

  /* bpmn-js-properties-panel 基础样式（避免样式缺失导致的空白） */
  :deep(.bio-properties-panel) {
    font-family: Arial, sans-serif;
    font-size: 12px;
    line-height: 1.4;
  }
</style>
