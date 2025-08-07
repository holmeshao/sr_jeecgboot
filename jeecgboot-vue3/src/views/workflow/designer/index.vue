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
      <div 
        ref="bpmnContainer" 
        class="bpmn-canvas"
        :class="{ 'with-properties': showProperties }"
      ></div>
      
      <!-- 属性面板 -->
      <div 
        v-show="showProperties" 
        ref="propertiesContainer" 
        class="properties-panel"
      ></div>
    </div>

    <!-- 导入文件对话框 -->
    <input 
      ref="fileInput" 
      type="file" 
      accept=".bpmn,.bpmn20.xml" 
      style="display: none" 
      @change="handleFileImport"
    />

    <!-- 保存流程弹窗 -->
    <BasicModal
      v-bind="$attrs"
      @register="registerSaveModal"
      title="保存流程"
      @ok="handleSaveSubmit"
    >
      <BasicForm @register="registerSaveForm" />
    </BasicModal>

    <!-- 部署流程弹窗 -->
    <BasicModal
      v-bind="$attrs"
      @register="registerDeployModal"
      title="部署流程"
      @ok="handleDeploySubmit"
    >
      <BasicForm @register="registerDeployForm" />
    </BasicModal>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue';
import { useMessage } from '/@/hooks/web/useMessage';
import { BasicModal, useModal } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form';
import { workflowDefinitionApi } from '/@/api/workflow';

// 动态导入bpmn-js相关模块
let BpmnModeler: any = null;
let BpmnPropertiesPanel: any = null;
let flowableModdleDescriptor: any = null;

const { createMessage } = useMessage();

// 组件状态
const bpmnContainer = ref<HTMLElement>();
const propertiesContainer = ref<HTMLElement>();
const fileInput = ref<HTMLInputElement>();
const showProperties = ref(true);
const hasProcess = ref(false);
const saving = ref(false);
const deploying = ref(false);

let modeler: any = null;
let propertiesPanel: any = null;

// 表单配置
const [registerSaveForm, { validate: validateSave, resetFields: resetSaveFields }] = useForm({
  labelWidth: 100,
  schemas: [
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

// 初始化设计器
async function initBpmnModeler() {
  try {
    // 动态导入模块
    const bpmnModelerModule = await import('bpmn-js/lib/Modeler');
    const propertiesPanelModule = await import('bpmn-js-properties-panel');
    const flowableDescriptor = await import('bpmn-js-properties-panel/lib/provider/flowable/FlowableDescriptor');

    BpmnModeler = bpmnModelerModule.default;
    BpmnPropertiesPanel = propertiesPanelModule.BpmnPropertiesPanel;
    flowableModdleDescriptor = flowableDescriptor.default;

    // 创建BPMN建模器
    modeler = new BpmnModeler({
      container: bpmnContainer.value,
      keyboard: {
        bindTo: window
      },
      propertiesPanel: {
        parent: propertiesContainer.value
      },
      additionalModules: [
        BpmnPropertiesPanel,
        // 可以添加更多插件
      ],
      moddleExtensions: {
        flowable: flowableModdleDescriptor
      }
    });

    // 监听事件
    modeler.on('import.done', () => {
      hasProcess.value = true;
      zoomReset();
    });

    modeler.on('commandStack.changed', () => {
      hasProcess.value = true;
    });

    // 创建新流程
    await createNew();

  } catch (error) {
    console.error('初始化BPMN设计器失败:', error);
    createMessage.error('初始化流程设计器失败，请确保已安装相关依赖');
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
  openSaveModal();
}

// 处理保存提交
async function handleSaveSubmit() {
  try {
    const values = await validateSave();
    const result = await modeler.saveXML({ format: true });
    
    // 这里可以调用API保存到后端
    console.log('保存流程:', { ...values, xml: result.xml });
    
    createMessage.success('流程草稿保存成功');
    closeSaveModal();
    resetSaveFields();
  } catch (error) {
    console.error('保存流程失败:', error);
    createMessage.error('保存流程失败');
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
</style>