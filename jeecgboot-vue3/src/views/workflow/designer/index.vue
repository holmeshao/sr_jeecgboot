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
          请求发布
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
        <!-- 高级：同步配置与BPMN（默认隐藏） -->
        <a-dropdown>
          <a-button>
            高级操作
          </a-button>
          <template #overlay>
            <a-menu>
              <a-menu-item @click="syncFromConfig" :disabled="!hasProcess">同步到BPMN</a-menu-item>
              <a-menu-item @click="syncToConfig" :disabled="!hasProcess">从BPMN读取</a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
        <a-button @click="togglePropertiesPanel">
          <Icon icon="ant-design:setting-outlined" />
          {{ showProperties ? '隐藏' : '显示' }}属性面板
        </a-button>
        <a-button @click="zoomReset">
          <Icon icon="ant-design:compress-outlined" />
          重置视图
        </a-button>
        <!-- 条件入口改由右侧属性面板承载，此处移除按钮 -->
        <a-divider type="vertical" />
        <a-button @click="openUserTaskProps">
          <Icon icon="ant-design:user-outlined" />
          用户任务属性
        </a-button>
        <a-button @click="setDefaultFlow">
          <Icon icon="ant-design:check-outlined" />
          设为默认流
        </a-button>
        <a-button @click="clearDefaultFlow">
          <Icon icon="ant-design:close-outlined" />
          清除默认流
        </a-button>
        <a-divider type="vertical" />
        <!-- 监听器相关操作聚合到下拉，避免与右侧面板重复 -->
        <a-dropdown>
          <a-button>
            监听器
          </a-button>
          <template #overlay>
            <a-menu>
              <a-menu-item @click="openListenerManager">任务监听器管理</a-menu-item>
              <a-menu-item @click="openExecListenerManager">执行监听器管理</a-menu-item>
              <a-menu-item @click="openExecBulkManager">全局执行监听器</a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
        <a-divider type="vertical" />
        <a-button @click="openMultiInstance">
          <Icon icon="ant-design:branches-outlined" />
          多实例/会签
        </a-button>
        <a-button @click="openServiceScript">
          <Icon icon="ant-design:code-outlined" />
          服务/脚本任务
        </a-button>
        <a-button @click="openElementTemplate">
          <Icon icon="ant-design:appstore-outlined" />
          元素模板
        </a-button>
        <a-divider type="vertical" />
        <!-- 执行监听器相关已合并到上方下拉 -->
        <a-button @click="openTimerConfig">
          <Icon icon="ant-design:clock-circle-outlined" />
          定时事件
        </a-button>
        <a-button @click="openEventDefConfig">
          <Icon icon="ant-design:notification-outlined" />
          事件定义
        </a-button>
      </div>
    </div>

    <!-- 设计器主体 -->
    <div class="designer-container">
      <!-- BPMN画布 -->
      <div ref="bpmnContainer" class="bpmn-canvas" :class="{ 'with-properties': showProperties }"></div>

      <!-- 本地二开的属性面板（替换外部包） -->
      <div v-show="showProperties" class="properties-panel">
        <div class="bio-properties-panel">
          <div class="bio-properties-panel-header">属性</div>

          <div v-if="!!activeElementType" class="bio-properties-panel-group">
            <div class="bio-properties-panel-group-header" @click="toggleGroup('general')">
              <span>GENERAL</span>
              <span class="group-toggle" :class="{ open: groupOpen.general }"></span>
            </div>
            <div v-show="groupOpen.general">
            <div class="bio-properties-panel-entry">
              <div class="bio-properties-panel-label">Id</div>
              <a-input v-model:value="generalId" @change="onGeneralIdChange" />
            </div>
            <div class="bio-properties-panel-entry">
              <div class="bio-properties-panel-label">Name</div>
              <a-input v-model:value="generalName" @change="onGeneralNameChange" />
            </div>
            </div>
          </div>

          <div v-if="!!activeElementType" class="bio-properties-panel-group">
            <div class="bio-properties-panel-group-header" @click="toggleGroup('documentation')">
              <span>DOCUMENTATION</span>
              <span class="group-toggle" :class="{ open: groupOpen.documentation }"></span>
            </div>
            <div v-show="groupOpen.documentation">
            <div class="bio-properties-panel-entry">
              <div class="bio-properties-panel-label">Text</div>
              <a-textarea :rows="3" v-model:value="docText" @change="onDocumentationChange" placeholder="文档/说明" />
            </div>
            </div>
          </div>

          <div v-if="activeElementType === 'bpmn:SequenceFlow'" class="bio-properties-panel-group">
            <div class="bio-properties-panel-group-header" @click="toggleGroup('condition')">
              <span>CONDITION</span>
              <span class="group-toggle" :class="{ open: groupOpen.condition }"></span>
            </div>
            <div v-show="groupOpen.condition">
            <div class="bio-properties-panel-entry">
              <div class="bio-properties-panel-label">表达式</div>
              <a-textarea :rows="3" v-model:value="condExpression" @change="onCondChange" placeholder="如：${amount > 1000}" />
            </div>
            </div>
          </div>

          <div v-if="activeElementType === 'bpmn:UserTask'" class="bio-properties-panel-group">
            <div class="bio-properties-panel-group-header" @click="toggleGroup('assignment')">
              <span>ASSIGNMENT</span>
              <span class="group-toggle" :class="{ open: groupOpen.assignment }"></span>
            </div>
            <div v-show="groupOpen.assignment">
            <div class="bio-properties-panel-entry">
              <div class="bio-properties-panel-label">Assignee</div>
              <div style="display:flex; gap:8px; align-items:center;">
                <a-input :value="assigneeDisplay || assignee" readonly />
                <a-button size="small" @click="openPickAssignee">选择用户</a-button>
              </div>
            </div>
            <div class="bio-properties-panel-entry">
              <div class="bio-properties-panel-label">Candidate Users</div>
              <div style="display:flex; gap:8px; align-items:center;">
                <a-input :value="candidateUsersDisplay || candidateUsers" readonly />
                <a-button size="small" @click="openPickUsers">选择用户</a-button>
              </div>
            </div>
            <div class="bio-properties-panel-entry">
              <div class="bio-properties-panel-label">Candidate Groups</div>
              <div style="display:flex; gap:8px; align-items:center;">
                <a-input :value="candidateGroupsDisplay || candidateGroups" readonly />
                <a-button size="small" @click="openPickRolesForGroups">选择角色</a-button>
                <a-button size="small" @click="openPickDeptsForGroups">选择部门</a-button>
              </div>
            </div>
            <div class="bio-properties-panel-entry">
              <div class="bio-properties-panel-label">Due Date</div>
              <a-input v-model:value="dueDate" @change="onUserTaskChange('camunda:dueDate', dueDate)" />
            </div>
            <div class="bio-properties-panel-entry">
              <div class="bio-properties-panel-label">Priority</div>
              <a-input-number style="width: 100%" v-model:value="priorityNum" @change="onPriorityChange" />
            </div>
            </div>
          </div>

          <div v-if="activeElementType === 'bpmn:UserTask'" class="bio-properties-panel-group">
            <div class="bio-properties-panel-group-header" @click="toggleGroup('listeners')">
              <span>LISTENERS</span>
              <span class="group-toggle" :class="{ open: groupOpen.listeners }"></span>
            </div>
            <div v-show="groupOpen.listeners">
            <div class="bio-properties-panel-entry" style="display:flex; gap:8px; align-items:center; padding-top:8px;">
              <a-select style="width:110px" v-model:value="newListenerEvent" :options="[
                {label:'create',value:'create'},{label:'assignment',value:'assignment'},
                {label:'complete',value:'complete'},{label:'delete',value:'delete'}
              ]" />
              <a-select style="width:160px" v-model:value="newListenerType" :options="[
                {label:'class',value:'class'},{label:'expression',value:'expression'},{label:'delegateExpression',value:'delegateExpression'}
              ]" />
              <a-input style="flex:1" v-model:value="newListenerValue" placeholder="org.example.MyTaskListener 或 ${expr}" />
              <a-button type="primary" @click="addTaskListenerInline">新增</a-button>
            </div>
            <div class="bio-properties-panel-entry" v-for="it in listenerList" :key="it.__k" style="display:flex; gap:8px; align-items:center;">
              <span style="width:90px;color:#666">{{it.event}}</span>
              <span style="width:150px;color:#666">{{it.__type}}</span>
              <span style="flex:1;color:#333;word-break:break-all;">{{it.__value}}</span>
              <a-button type="link" danger @click="removeListener(it)">删除</a-button>
            </div>
            </div>
          </div>

          <div v-if="activeElementType === 'bpmn:UserTask'" class="bio-properties-panel-group">
            <div class="bio-properties-panel-group-header" @click="toggleGroup('multi')">
              <span>MULTI-INSTANCE</span>
              <span class="group-toggle" :class="{ open: groupOpen.multi }"></span>
            </div>
            <div v-show="groupOpen.multi">
            <div class="bio-properties-panel-entry">
              <div class="bio-properties-panel-label">启用多实例</div>
              <a-switch v-model:checked="isMultiLocal" @change="applyMultiLocal" />
            </div>
            <div class="bio-properties-panel-entry">
              <div class="bio-properties-panel-label">集合变量</div>
              <a-input v-model:value="collectionLocal" @change="applyMultiLocal" />
            </div>
            <div class="bio-properties-panel-entry">
              <div class="bio-properties-panel-label">元素变量</div>
              <a-input v-model:value="elementVariableLocal" @change="applyMultiLocal" />
            </div>
            <div class="bio-properties-panel-entry">
              <div class="bio-properties-panel-label">顺序执行</div>
              <a-switch v-model:checked="isSequentialLocal" @change="applyMultiLocal" />
            </div>
            <div class="bio-properties-panel-entry">
              <div class="bio-properties-panel-label">完成条件</div>
              <a-input v-model:value="completionConditionLocal" @change="applyMultiLocal" placeholder="${nrOfCompletedInstances/nrOfInstances >= 0.6}" />
            </div>
            </div>
          </div>

          <div v-if="!activeElementType" style="padding: 12px 16px; color:#999;">请选择画布上的元素</div>
        </div>
      </div>
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
    <!-- 条件表达式改由右侧属性面板承载，移除弹窗入口 -->

    <!-- 用户任务属性弹窗 -->
    <BasicModal v-bind="$attrs" @register="registerUserTaskModal" title="用户任务属性" @ok="handleUserTaskSubmit">
      <BasicForm @register="registerUserTaskForm" />
    </BasicModal>

    <!-- 任务监听器弹窗 -->
    <BasicModal v-bind="$attrs" @register="registerListenerModal" title="添加任务监听器" @ok="handleListenerSubmit">
      <BasicForm @register="registerListenerForm" />
    </BasicModal>

    <!-- 系统选择器：用户/角色/部门，用于 ASSIGNMENT 快捷选择 -->
    <a-form-item style="display:none">
      <UserSelectModal @register="registerPickUserModal" @getSelectResult="onPickedUsers" :rowKey="'id'" :labelKey="'realname'" />
      <RoleSelectModal @register="registerPickRoleModal" @getSelectResult="onPickedRoles" :rowKey="'roleCode'" :labelKey="'roleName'" />
      <DeptSelectModal @register="registerPickDeptModal" @getSelectResult="onPickedDepts" :multiple="true" />
    </a-form-item>
    <!-- 监听器管理弹窗（列表+删除） -->
    <BasicModal v-bind="$attrs" @register="registerListenerListModal" title="任务监听器管理" :footer="null" width="800px">
      <div style="margin-bottom: 12px; display:flex; gap:8px; align-items:center;">
        <a-button type="primary" @click="openTaskListener">新增监听器</a-button>
      </div>
      <a-table :data-source="listenerList" :columns="listenerColumns" row-key="__k" :pagination="false">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-button type="link" danger @click="removeListener(record)">删除</a-button>
          </template>
        </template>
      </a-table>
    </BasicModal>

    <!-- 多实例/会签 弹窗 -->
    <BasicModal v-bind="$attrs" @register="registerMultiModal" title="多实例/会签" @ok="handleMultiSubmit">
      <BasicForm @register="registerMultiForm" />
    </BasicModal>

    <!-- 服务/脚本任务 弹窗 -->
    <BasicModal v-bind="$attrs" @register="registerSvcScriptModal" title="服务/脚本任务" @ok="handleSvcScriptSubmit">
      <BasicForm @register="registerSvcScriptForm" />
    </BasicModal>

    <!-- 元素模板 弹窗 -->
    <BasicModal v-bind="$attrs" @register="registerTplModal" title="元素模板" @ok="handleTplSubmit">
      <BasicForm @register="registerTplForm" />
    </BasicModal>

    <!-- 执行监听器 弹窗（新增） -->
    <BasicModal v-bind="$attrs" @register="registerExecModal" title="添加执行监听器" @ok="handleExecListenerSubmit">
      <BasicForm @register="registerExecForm" />
    </BasicModal>

    <!-- 执行监听器管理 弹窗（列表+删除） -->
    <BasicModal v-bind="$attrs" @register="registerExecListModal" title="执行监听器管理" :footer="null" width="800px">
      <div style="margin-bottom: 12px; display:flex; gap:8px; align-items:center;">
        <a-button type="primary" @click="openExecListener">新增监听器</a-button>
      </div>
      <a-table :data-source="execListenerList" :columns="execListenerColumns" row-key="__k" :pagination="false">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-button type="link" danger @click="removeExecListener(record)">删除</a-button>
          </template>
        </template>
      </a-table>
    </BasicModal>

    <!-- 定时事件 弹窗 -->
    <BasicModal v-bind="$attrs" @register="registerTimerModal" title="定时事件配置" @ok="handleTimerSubmit">
      <BasicForm @register="registerTimerForm" />
    </BasicModal>

    <!-- 全局执行监听器管理（所有元素） -->
    <BasicModal v-bind="$attrs" @register="registerExecBulkModal" title="全局执行监听器管理" :footer="null" width="1000px">
      <a-table :data-source="execBulkList" :columns="execBulkColumns" row-key="__rk" :pagination="false">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-button type="link" danger @click="removeExecListenerAt(record)">删除</a-button>
          </template>
        </template>
      </a-table>
    </BasicModal>

    <!-- 事件定义配置 -->
    <BasicModal v-bind="$attrs" @register="registerEventDefModal" title="事件定义" @ok="handleEventDefSubmit">
      <BasicForm @register="registerEventDefForm" />
    </BasicModal>
  </div>
</template>

<script lang="ts" setup>
  import { ref, reactive, onMounted, onBeforeUnmount, nextTick, markRaw, toRaw } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
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
  // 启用 Camunda Provider，让右侧属性面板具备 Assignment/Listeners/Condition 等分组
  // 兼容 Camunda7（即 Flowable 的 camunda 扩展同源）
  // @ts-ignore
  import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda.json';
  import { is } from 'bpmn-js/lib/util/ModelUtil';
  import { TextFieldEntry, TextAreaEntry, isTextFieldEntryEdited } from '@bpmn-io/properties-panel';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicModal, useModal } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  // 系统选择器（用户/角色/部门）
  import UserSelectModal from '/@/components/Form/src/jeecg/components/modal/UserSelectModal.vue';
  import RoleSelectModal from '/@/components/Form/src/jeecg/components/modal/RoleSelectModal.vue';
  import DeptSelectModal from '/@/components/Form/src/jeecg/components/modal/DeptSelectModal.vue';

  // 动态导入bpmn-js相关模块
  // 上述静态导入已生效，无需在运行时动态赋值

  const { createMessage } = useMessage();
  const route = useRoute();
  const router = useRouter();

  // 组件状态
  const bpmnContainer = ref<HTMLElement>();
  // 外部属性面板容器不再使用
  // const propertiesContainer = ref<HTMLElement>();
  const fileInput = ref<HTMLInputElement>();
  const showProperties = ref(true);
  const hasProcess = ref(false);
  const saving = ref(false);
  const deploying = ref(false);
  const savingDraftToServer = ref(false);

  let modeler: any = null;
  // let propertiesPanel: any = null;
  const activeElementType = ref<string>('');
  const activeElementRef = ref<any>(null);
  const generalId = ref<string>('');
  const generalName = ref<string>('');
  const docText = ref<string>('');
  const condExpression = ref<string>('');
  const assignee = ref<string>('');
  const assigneeDisplay = ref<string>('');
  const candidateUsers = ref<string>('');
  const candidateUsersDisplay = ref<string>('');
  const candidateGroups = ref<string>('');
  const candidateGroupsDisplay = ref<string>('');
  const dueDate = ref<string>('');
  const priorityNum = ref<number | undefined>(undefined);
  // 选择器上下文
  const pickTarget = ref<'assignee'|'candidateUsers'|'candidateGroupsRole'|'candidateGroupsDept'|''>('');
  // 监听器（本地面板内联新增）
  const newListenerEvent = ref<string>('create');
  const newListenerType = ref<'class'|'expression'|'delegateExpression'>('class' as any);
  const newListenerValue = ref<string>('');
  // 多实例（本地面板）
  const isMultiLocal = ref<boolean>(false);
  const collectionLocal = ref<string>('');
  const elementVariableLocal = ref<string>('');
  const isSequentialLocal = ref<boolean>(false);
  const completionConditionLocal = ref<string>('');
  // 面板分组展开/收起
  const groupOpen = reactive({
    general: true,
    documentation: true,
    condition: true,
    assignment: true,
    listeners: true,
    multi: false,
  });
  function toggleGroup(key: keyof typeof groupOpen) {
    groupOpen[key] = !groupOpen[key];
    // 展开监听器时刷新列表，避免用户以为“展开为空”
    if (key === 'listeners' && groupOpen.listeners) {
      const el = activeElementRef.value;
      if (el && (el.businessObject?.$type === 'bpmn:UserTask')) {
        try { refreshListenerListFromTask(el); } catch (e) {}
      }
    }
  }
  const currentDefinitionId = ref<string>('');

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
  // 用户任务属性表单
  const [registerUserTaskForm, { validate: validateUserTask, setFieldsValue: setUserTaskFields, resetFields: resetUserTaskFields }] = useForm({
    labelWidth: 100,
    schemas: [
      { field: 'assignee', label: '指派人', component: 'Input', componentProps: { placeholder: '单个用户ID，支持${expr}' } },
      { field: 'candidateUsers', label: '候选用户', component: 'Input', componentProps: { placeholder: '逗号分隔的用户ID列表' } },
      { field: 'candidateGroups', label: '候选角色/组', component: 'Input', componentProps: { placeholder: '逗号分隔的组编码' } },
      { field: 'dueDate', label: '到期时间', component: 'Input', componentProps: { placeholder: 'ISO日期或表达式，如 ${now()+P2D}' } },
      { field: 'priority', label: '优先级', component: 'InputNumber', componentProps: { style: 'width: 100%' } },
    ],
  });
  // 任务监听器表单
  const [registerListenerForm, { validate: validateListener, setFieldsValue: setListenerFields, resetFields: resetListenerFields }] = useForm({
    labelWidth: 100,
    schemas: [
      { field: 'event', label: '事件', component: 'Select', required: true, componentProps: { options: [
        { label: 'create', value: 'create' },
        { label: 'assignment', value: 'assignment' },
        { label: 'complete', value: 'complete' },
        { label: 'delete', value: 'delete' },
      ], allowClear: false } },
      { field: 'class', label: 'Java类', component: 'Input', componentProps: { placeholder: 'org.example.MyTaskListener' } },
      { field: 'expression', label: '表达式', component: 'Input', componentProps: { placeholder: '${myBean.handle(execution)}' } },
      { field: 'delegateExpression', label: '委派表达式', component: 'Input', componentProps: { placeholder: '${taskListener}' } },
    ],
  });
  const listenerList = ref<any[]>([]);
  const listenerColumns = [
    { title: '事件', dataIndex: 'event', width: 120 },
    { title: '类型', dataIndex: '__type', width: 140 },
    { title: '值', dataIndex: '__value' },
    { title: '操作', key: 'action', width: 120 },
  ];
  function openListenerManager() {
    const task = getSelectedUserTask();
    if (!task) {
      createMessage.warning('请选中一个用户任务');
      return;
    }
    refreshListenerListFromTask(task);
    openListenerListModal(true);
  }
  function refreshListenerListFromTask(task: any) {
    const bo = task.businessObject || {};
    const ext = bo.extensionElements;
    const items = (ext?.values || []).filter((v: any) => v.$type === 'camunda:TaskListener');
    listenerList.value = items.map((it: any, idx: number) => ({
      __k: `${idx}`,
      event: it.event || '',
      __type: it.class ? 'class' : it.delegateExpression ? 'delegateExpression' : it.expression ? 'expression' : '',
      __value: it.class || it.delegateExpression || it.expression || '',
      __idx: idx,
    }));
  }
  function removeListener(record: any) {
    const task = getSelectedUserTask();
    if (!task) return;
    const bo = task.businessObject;
    const moddle = modeler.get('moddle');
    const modeling = modeler.get('modeling');
    const ext = bo.extensionElements || moddle.create('bpmn:ExtensionElements');
    const kept = (ext.values || []).filter((v: any) => !(v.$type === 'camunda:TaskListener'));
    const listeners = (ext.values || []).filter((v: any) => v.$type === 'camunda:TaskListener');
    const newListeners = listeners.filter((_: any, i: number) => i !== record.__idx);
    ext.values = [...kept, ...newListeners];
    modeling.updateProperties(task, { extensionElements: ext });
    refreshListenerListFromTask(task);
    createMessage.success('已删除监听器');
  }

  // 条件表达式改由右侧属性面板承载，移除独立表单

  // 弹窗配置
  const [registerSaveModal, { openModal: openSaveModal, closeModal: closeSaveModal }] = useModal();
  const [registerDeployModal, { openModal: openDeployModal, closeModal: closeDeployModal }] = useModal();
  const [registerVersionModal, { openModal: openVersionModal, closeModal: closeVersionModal }] = useModal();
  const [registerDraftModal, { openModal: openDraftModal, closeModal: closeDraftModal }] = useModal();
  const [registerUserTaskModal, { openModal: openUserTaskModal, closeModal: closeUserTaskModal }] = useModal();
  const [registerListenerModal, { openModal: openListenerModal, closeModal: closeListenerModal }] = useModal();
  const [registerListenerListModal, { openModal: openListenerListModal, closeModal: closeListenerListModal }] = useModal();
  const [registerMultiModal, { openModal: openMultiModal, closeModal: closeMultiModal }] = useModal();
  const [registerSvcScriptModal, { openModal: openSvcScriptModal, closeModal: closeSvcScriptModal }] = useModal();
  const [registerTplModal, { openModal: openTplModal, closeModal: closeTplModal }] = useModal();
  const [registerExecModal, { openModal: openExecModal, closeModal: closeExecModal }] = useModal();
  const [registerExecListModal, { openModal: openExecListModal, closeModal: closeExecListModal }] = useModal();
  const [registerTimerModal, { openModal: openTimerModal, closeModal: closeTimerModal }] = useModal();
  const [registerExecBulkModal, { openModal: openExecBulkModal, closeModal: closeExecBulkModal }] = useModal();
  const [registerEventDefModal, { openModal: openEventDefModal, closeModal: closeEventDefModal }] = useModal();
  // 系统选择器
  const [registerPickUserModal, { openModal: openPickUserModal }] = useModal();
  const [registerPickRoleModal, { openModal: openPickRoleModal }] = useModal();
  const [registerPickDeptModal, { openModal: openPickDeptModal }] = useModal();

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
      // 自定义属性提供器：为 SequenceFlow 添加条件输入、为 UserTask 添加指派/候选/优先级等
      function CustomPropertiesProvider(
        propertiesPanel: any,
        translate: (s: string) => string,
        bpmnFactory: any,
        elementRegistry: any,
        commandStack: any,
      ) {
        const provider = {
          getGroups(element: any) {
            console.debug('[jeecg] propertiesProvider getGroups for', element?.id, element?.businessObject?.$type);
            return function (groups: any[]) {
            // 条件 - 适用于 SequenceFlow
            if (is(element, 'bpmn:SequenceFlow')) {
              const entries: any[] = [];
              entries.push({
                id: 'sequenceFlow-condition',
                element,
                label: translate('Condition'),
                component: TextAreaEntry,
                getValue: () => {
                  const ce = element.businessObject.conditionExpression;
                  return ce ? (ce.body || '') : '';
                },
                setValue: (value: string) => {
                  const bo = element.businessObject;
                  let ce = bo.conditionExpression;
                  if (value && value.trim().length) {
                    if (!ce) ce = bpmnFactory.create('bpmn:FormalExpression');
                    ce.body = value;
                  } else {
                    ce = null;
                  }
                  commandStack.execute('element.updateModdleProperties', {
                    element,
                    moddleElement: bo,
                    properties: { conditionExpression: ce },
                  });
                },
                isEdited: isTextFieldEntryEdited,
              });

              groups.push({ id: 'jeecg-condition', label: translate('Condition'), entries });
            }

            // 用户任务 - 指派/候选/到期时间/优先级（camunda 扩展属性）
            if (is(element, 'bpmn:UserTask')) {
              const bo = element.businessObject;
              const ent = (id: string, camundaKey: string, label: string) => ({
                id,
                element,
                label: translate(label),
                component: TextFieldEntry,
                getValue: () => bo.get(camundaKey) || '',
                setValue: (val: string) =>
                  commandStack.execute('element.updateModdleProperties', {
                    element,
                    moddleElement: bo,
                    properties: { [camundaKey]: val || undefined },
                  }),
                isEdited: isTextFieldEntryEdited,
              });

              const entries = [
                ent('assignee', 'camunda:assignee', 'Assignee'),
                ent('candidateUsers', 'camunda:candidateUsers', 'Candidate Users'),
                ent('candidateGroups', 'camunda:candidateGroups', 'Candidate Groups'),
                ent('dueDate', 'camunda:dueDate', 'Due Date'),
                ent('priority', 'camunda:priority', 'Priority'),
              ];

              groups.push({ id: 'jeecg-userTask', label: translate('Assignment'), entries });
            }

            return groups;
          };
          },
        };
        // 注册
        try {
          // 暴露到 window 便于排查
          (window as any)._jeecg_pp = propertiesPanel;
          propertiesPanel && propertiesPanel.registerProvider && propertiesPanel.registerProvider(1000, provider);
        } catch (e) {
          console.warn('[jeecg] registerProvider failed', e);
        }
      }
      (CustomPropertiesProvider as any).$inject = ['propertiesPanel', 'translate', 'bpmnFactory', 'elementRegistry', 'commandStack'];

      // 确保在引导时实例化，从而执行 registerProvider（不要依赖 typed aggregator）
      const CustomPropertiesProviderModule = {
        __init__: ['customPropertiesProvider'],
        customPropertiesProvider: ['type', CustomPropertiesProvider],
      } as any;

      // 兜底提供 debounceInput 服务（某些组合版本下不存在或不是函数）
      const DebounceInputFallbackModule = {
        debounceInput: ['value', (() => {
          const create = (fn: any, wait?: number) => {
            if (typeof fn !== 'function') return fn;
            if (!wait || wait <= 0) return fn;
            let timer: any = null;
            return (...args: any[]) => {
              if (timer) clearTimeout(timer);
              timer = setTimeout(() => fn.apply(null, args), wait);
            };
          };
          const combined: any = (fn: any, wait?: number) => create(fn, wait);
          combined.debounce = (fn: any, wait?: number) => create(fn, wait);
          return combined;
        })()],
        debounce: ['value', (() => {
          const create = (fn: any, wait?: number) => {
            if (typeof fn !== 'function') return fn;
            if (!wait || wait <= 0) return fn;
            let timer: any = null;
            return (...args: any[]) => {
              if (timer) clearTimeout(timer);
              timer = setTimeout(() => fn.apply(null, args), wait);
            };
          };
          const combined: any = (fn: any, wait?: number) => create(fn, wait);
          combined.debounce = (fn: any, wait?: number) => create(fn, wait);
          return combined;
        })()],
      } as any;

      modeler = new (BpmnModeler as any)({
        container: bpmnContainer.value,
        keyboard: { bindTo: window },
        // propertiesPanel: { parent: propertiesContainer.value },
        additionalModules: [],
        moddleExtensions: {
          camunda: camundaModdleDescriptor as any,
        },
      });
      // 彻底停用外部属性面板，避免其渲染与我们本地面板冲突

      modeler.on('import.done', () => {
        hasProcess.value = true;
        zoomReset();
        try { updateConditionOverlays(); } catch (e) {}
      });

      modeler.on('commandStack.changed', () => {
        hasProcess.value = true;
        try { updateConditionOverlays(); } catch (e) {}
      });

      // 监听选中切换，驱动本地属性面板
      modeler.on('selection.changed', (e: any) => {
        const el = (e.newSelection || [])[0];
        activeElementRef.value = el ? markRaw(el) : null;
        const type = el?.businessObject?.$type || '';
        activeElementType.value = type;
        if (el) {
          generalId.value = el.id || el.businessObject?.id || '';
          generalName.value = el.businessObject?.name || '';
          docText.value = (el.businessObject?.documentation && el.businessObject.documentation[0]?.text) || '';
        } else {
          generalId.value = '';
          generalName.value = '';
          docText.value = '';
        }
        if (type === 'bpmn:SequenceFlow') {
          condExpression.value = el?.businessObject?.conditionExpression?.body || '';
        } else if (type === 'bpmn:UserTask') {
          const bo = el.businessObject || {};
          assignee.value = bo.get?.('camunda:assignee') || '';
          assigneeDisplay.value = '';
          candidateUsers.value = bo.get?.('camunda:candidateUsers') || '';
          candidateUsersDisplay.value = '';
          candidateGroups.value = bo.get?.('camunda:candidateGroups') || '';
          candidateGroupsDisplay.value = '';
          dueDate.value = bo.get?.('camunda:dueDate') || '';
          const p = bo.get?.('camunda:priority');
          priorityNum.value = p ? Number(p) : undefined;
          try {
            if (groupOpen.listeners) refreshListenerListFromTask(el);
          } catch (err) {}
          // 多实例
          const loop = bo.loopCharacteristics;
          isMultiLocal.value = !!loop;
          collectionLocal.value = loop?.collection || '';
          elementVariableLocal.value = loop?.elementVariable || '';
          isSequentialLocal.value = !!(loop?.isSequential);
          completionConditionLocal.value = loop?.completionCondition?.body || '';
        }
      });

      // 如果从流程定义进入，加载已部署流程XML
      const defId = (route.query?.definitionId as string) || '';
      const modelIdFromRoute = (route.query?.modelId as string) || '';
      if (defId) {
        await loadDefinitionXml(defId);
      } else if (modelIdFromRoute) {
        await loadModelLatestXml(modelIdFromRoute);
      } else {
        // 在 importXML 之前先创建一个空白 diagram，避免 modeler 还未初始化导致 null
        await modeler.importXML(defaultBpmnXml);
        hasProcess.value = true;
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
      currentDefinitionId.value = definitionId;
      createMessage.success({ content: '已加载流程定义，可在线编辑后重新部署生成新版本', duration: 1 });
    } catch (e) {
      console.error('加载流程XML失败:', e);
      createMessage.error('加载流程XML失败，已回退为新建流程');
      await createNew();
    }
  }

  // 同步：配置 -> BPMN
  async function syncFromConfig() {
    try {
      const defId = currentDefinitionId.value || (route.query?.definitionId as string) || '';
      if (!defId) { createMessage.warning('需要从流程定义进入后使用'); return; }
      const xml = await workflowDefinitionApi.syncFromConfig(defId as any);
      const content = typeof xml === 'string' ? xml : (xml.result || xml.xml);
      if (!content) { createMessage.error('未获取到同步后的XML'); return; }
      await modeler.importXML(content);
      createMessage.success({ content: '已根据配置写入BPMN（未部署）', duration: 1 });
    } catch (e) {
      createMessage.error('同步到BPMN失败');
    }
  }

  // 同步：BPMN -> 配置
  async function syncToConfig() {
    try {
      const defId = currentDefinitionId.value || (route.query?.definitionId as string) || '';
      if (!defId) { createMessage.warning('需要从流程定义进入后使用'); return; }
      await workflowDefinitionApi.syncToConfig(defId as any);
      createMessage.success('已从BPMN回写配置（仅更新已存在的节点条目）');
    } catch (e) {
      createMessage.error('从BPMN读取失败');
    }
  }

  // 创建新流程
  async function createNew() {
    try {
      await modeler.importXML(defaultBpmnXml);
      hasProcess.value = true;
      createMessage.success({ content: '已创建新的流程模板', duration: 1 });
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

  // 部署改为：请求发布 → 跳到流程定义页，并携带当前模型ID（若有）
  function deployProcess() {
    try {
      const q: any = {};
      if (currentModelId.value) q.modelId = currentModelId.value;
      router.push({ path: '/workflow/definition', query: q });
    } catch (e) {
      // 兜底：如果路由跳转失败，保持旧行为（本地打开部署），避免打断使用
      try { openDeployModal(); } catch (_) {}
    }
  }

  // 处理部署提交
  async function handleDeploySubmit() {
    try {
      deploying.value = true;
      const values = await validateDeploy();
      const result = await modeler.saveXML({ format: true });

      // 优先走 JSON 直传 XML，避免 multipart 依赖与网关改写问题
      await workflowDefinitionApi.deployByXml({
        name: values.name,
        category: values.category,
        description: values.description,
        xml: result.xml,
      });

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

  // 条件设置改由右侧属性面板承载，删除本地快捷入口逻辑

  // ============ 用户任务属性增强 ============
  function getSelectedSequenceFlow(): any | null {
    try {
      const selection = modeler?.get('selection');
      const elems = selection?.get?.() || [];
      if (!elems.length) return null;
      const flow = elems.find((e: any) => (e?.type || e?.businessObject?.$type) === 'bpmn:SequenceFlow');
      return flow || null;
    } catch (e) {
      return null;
    }
  }
  function getSelectedUserTask(): any | null {
    try {
      const selection = modeler?.get('selection');
      const elems = selection?.get?.() || [];
      if (!elems.length) return null;
      const task = elems.find((e: any) => (e?.type || e?.businessObject?.$type) === 'bpmn:UserTask');
      return task || null;
    } catch (e) {
      return null;
    }
  }

  function openUserTaskProps() {
    const task = getSelectedUserTask();
    if (!task) {
      createMessage.warning('请选中一个用户任务');
      return;
    }
    const bo = task.businessObject || {};
    setUserTaskFields({
      assignee: bo.assignee || '',
      candidateUsers: bo.candidateUsers || '',
      candidateGroups: bo.candidateGroups || '',
      dueDate: bo.dueDate || '',
      priority: bo.priority ? Number(bo.priority) : undefined,
      formKey: bo.formKey || '',
    } as any);
    openUserTaskModal(true);
  }

  async function handleUserTaskSubmit() {
    try {
      const values = await validateUserTask();
      const task = getSelectedUserTask();
      if (!task) return;
      const modeling = modeler.get('modeling');
      const props: any = {};
      ['assignee', 'candidateUsers', 'candidateGroups', 'dueDate', 'formKey'].forEach((k) => {
        if (values[k] !== undefined) props[k] = values[k] || undefined;
      });
      if (values.priority !== undefined) props.priority = String(values.priority);
      modeling.updateProperties(task, props);
      createMessage.success('用户任务属性已更新');
      closeUserTaskModal();
      resetUserTaskFields();
    } catch (e) {}
  }

  // ============ 默认流设置 ============
  function setDefaultFlow() {
    // 选中连线，将其设为其源网关/任务的默认流（若为网关效果最佳）
    const flow = getSelectedSequenceFlow();
    if (!flow) {
      createMessage.warning('请选中一条连线');
      return;
    }
    const source = flow.source;
    if (!source) {
      createMessage.warning('未找到连线源节点');
      return;
    }
    const modeling = modeler.get('modeling');
    modeling.updateProperties(source, { default: flow });
    createMessage.success('已设为默认流');
  }

  function clearDefaultFlow() {
    const flow = getSelectedSequenceFlow();
    if (!flow) {
      createMessage.warning('请选中一条连线');
      return;
    }
    const source = flow.source;
    if (!source) {
      createMessage.warning('未找到连线源节点');
      return;
    }
    const modeling = modeler.get('modeling');
    modeling.updateProperties(source, { default: null });
    createMessage.success('已清除默认流');
  }

  // ===== 本地属性面板：事件处理 =====
  function onCondChange() {
    const el = activeElementRef.value; if (!el) return;
    const bo = el.businessObject;
    const moddle = modeler.get('moddle');
    const commandStack = modeler.get('commandStack');
    let ce = bo.conditionExpression;
    const value = condExpression.value || '';
    if (value && value.trim().length) {
      if (!ce) ce = moddle.create('bpmn:FormalExpression');
      ce.body = value;
    } else {
      ce = null;
    }
    commandStack.execute('element.updateModdleProperties', {
      element: el,
      moddleElement: bo,
      properties: { conditionExpression: ce },
    });
  }

  function onUserTaskChange(key: string, val: string) {
    const el = toRaw(activeElementRef.value); if (!el) return;
    const bo = el.businessObject;
    const commandStack = modeler.get('commandStack');
    commandStack.execute('element.updateModdleProperties', {
      element: el,
      moddleElement: bo,
      properties: { [key]: val || undefined },
    });
  }
  function onPriorityChange() {
    onUserTaskChange('camunda:priority',
      typeof priorityNum.value === 'number' ? String(priorityNum.value) : '');
  }

  // ===== 本地属性面板：任务监听器（内联） =====
  function openPickAssignee() {
    pickTarget.value = 'assignee';
    openPickUserModal(true, { isUpdate: false, rowKey: 'id', multiple: false } as any);
  }
  function openPickUsers() {
    pickTarget.value = 'candidateUsers';
    openPickUserModal(true, { isUpdate: false, rowKey: 'id', multiple: true } as any);
  }
  function openPickRolesForGroups() {
    pickTarget.value = 'candidateGroupsRole';
    // 使用 roleCode 作为选中值
    openPickRoleModal(true, { isUpdate: false, rowKey: 'roleCode' } as any);
  }
  function openPickDeptsForGroups() {
    pickTarget.value = 'candidateGroupsDept';
    openPickDeptModal(true, { isUpdate: false, multiple: true } as any);
  }
  function onPickedUsers(options: any, values: any) {
    const arr = Array.isArray(values) ? values : (values ? [values] : []);
    if (pickTarget.value === 'assignee') {
      const first = arr[0] || '';
      assignee.value = first;
      try {
        const name = Array.isArray(options) && options.length ? (options[0].label || '') : '';
        assigneeDisplay.value = name || first;
      } catch (e) { assigneeDisplay.value = first; }
      onUserTaskChange('camunda:assignee', first);
    } else if (pickTarget.value === 'candidateUsers') {
      const joined = arr.join(',');
      candidateUsers.value = joined;
      try {
        const names = Array.isArray(options) ? options.map((o: any)=> (o.label || o.value)).join(',') : joined;
        candidateUsersDisplay.value = names;
      } catch (e) { candidateUsersDisplay.value = joined; }
      onUserTaskChange('camunda:candidateUsers', joined);
    }
    pickTarget.value = '';
  }
  function onPickedRoles(options: any, values: any) {
    const arr = Array.isArray(values) ? values : (values ? [values] : []);
    // 以 role:<roleCode> 前缀编码，留待后端监听器解析为用户
    const tokens = arr.map((code: string) => `role:${code}`);
    const current = (candidateGroups.value || '').split(',').filter(Boolean);
    const merged = Array.from(new Set([...current, ...tokens])).join(',');
    candidateGroups.value = merged;
    try {
      const names = Array.isArray(options) ? options.map((o:any)=> (o.label || o.value)).join(',') : '';
      candidateGroupsDisplay.value = names ? [candidateGroupsDisplay.value, names].filter(Boolean).join(',') : candidateGroupsDisplay.value;
    } catch (e) {}
    onUserTaskChange('camunda:candidateGroups', merged);
    pickTarget.value = '';
  }
  function onPickedDepts(options: any, values: any) {
    const arr = Array.isArray(values) ? values : (values ? [values] : []);
    const tokens = arr.map((id: string) => `dept:${id}`);
    const current = (candidateGroups.value || '').split(',').filter(Boolean);
    const merged = Array.from(new Set([...current, ...tokens])).join(',');
    candidateGroups.value = merged;
    try {
      const names = Array.isArray(options) ? options.map((o:any)=> (o.label || o.value)).join(',') : '';
      candidateGroupsDisplay.value = names ? [candidateGroupsDisplay.value, names].filter(Boolean).join(',') : candidateGroupsDisplay.value;
    } catch (e) {}
    onUserTaskChange('camunda:candidateGroups', merged);
    pickTarget.value = '';
  }
  function addTaskListenerInline() {
    const el = activeElementRef.value; if (!el) { createMessage.warning('请先选中用户任务'); return; }
    const bo = el.businessObject || {};
    const moddle = modeler.get('moddle');
    const modeling = modeler.get('modeling');
    const event = newListenerEvent.value || 'create';
    const type = newListenerType.value;
    const val = (newListenerValue.value || '').trim();
    if (!val) { createMessage.warning('请输入监听器值'); return; }
    const ext = bo.extensionElements || moddle.create('bpmn:ExtensionElements');
    let listeners = (ext.values || []).filter((v: any) => v.$type === 'camunda:TaskListener');
    const payload: any = { event };
    if (type === 'class') payload.class = val;
    if (type === 'expression') payload.expression = val;
    if (type === 'delegateExpression') payload.delegateExpression = val;
    const tl = moddle.create('camunda:TaskListener', payload);
    listeners = listeners.concat([tl]);
    ext.values = [ ...(ext.values || []).filter((v: any) => v.$type !== 'camunda:TaskListener'), ...listeners ];
    modeling.updateProperties(el, { extensionElements: ext });
    try { refreshListenerListFromTask(el); } catch (e) {}
    newListenerValue.value = '';
    createMessage.success('已新增任务监听器');
  }

  // ===== 本地属性面板：多实例 =====
  function applyMultiLocal() {
    const el = activeElementRef.value; if (!el) return;
    const moddle = modeler.get('moddle');
    const modeling = modeler.get('modeling');
    if (!isMultiLocal.value) {
      modeling.updateProperties(el, { loopCharacteristics: null });
      return;
    }
    const props: any = {
      isSequential: !!isSequentialLocal.value,
      collection: collectionLocal.value || undefined,
      elementVariable: elementVariableLocal.value || undefined,
    };
    if (completionConditionLocal.value && completionConditionLocal.value.trim().length) {
      props.completionCondition = moddle.create('bpmn:FormalExpression', { body: completionConditionLocal.value });
    }
    const loop = moddle.create('bpmn:MultiInstanceLoopCharacteristics', props);
    modeling.updateProperties(el, { loopCharacteristics: loop });
  }

  // ===== 本地属性面板：General / Documentation =====
  function onGeneralIdChange() {
    const el = activeElementRef.value; if (!el) return;
    const newId = generalId.value?.trim(); if (!newId) return;
    const commandStack = modeler.get('commandStack');
    commandStack.execute('element.updateId', { element: el, newId });
  }
  function onGeneralNameChange() {
    const el = activeElementRef.value; if (!el) return;
    const modeling = modeler.get('modeling');
    try {
      // 使用 updateLabel 确保图形上即时创建/更新可见标签（含 SequenceFlow 外置标签）
      modeling.updateLabel(el, generalName.value || '');
    } catch (e) {
      // 兜底：回退到直接写 businessObject.name
      modeling.updateProperties(el, { name: generalName.value || undefined });
    }
  }
  function onDocumentationChange() {
    const el = activeElementRef.value; if (!el) return;
    const bo = el.businessObject;
    const moddle = modeler.get('moddle');
    const modeling = modeler.get('modeling');
    const text = docText.value || '';
    const docs = text ? [moddle.create('bpmn:Documentation', { text })] : [];
    modeling.updateProperties(el, { documentation: docs });
  }

  // ============ 任务监听器（Camunda/Flowable 兼容方式） ============
  function openTaskListener() {
    const task = getSelectedUserTask();
    if (!task) {
      createMessage.warning('请选中一个用户任务');
      return;
    }
    // 简版：打开新增监听器表单（不读取现有列表）
    setListenerFields({ event: 'create', class: '', expression: '', delegateExpression: '' } as any);
    openListenerModal(true);
  }

  async function handleListenerSubmit() {
    try {
      const values = await validateListener();
      const task = getSelectedUserTask();
      if (!task) return;
      const moddle = modeler.get('moddle');
      const modeling = modeler.get('modeling');
      const bo = task.businessObject;
      const ext = bo.extensionElements || moddle.create('bpmn:ExtensionElements');
      let listeners = (ext.values || []).filter((v: any) => v.$type === 'camunda:TaskListener');
      const tl = moddle.create('camunda:TaskListener', {
        event: values.event,
        class: values.class || undefined,
        expression: values.expression || undefined,
        delegateExpression: values.delegateExpression || undefined,
      });
      listeners = listeners.concat([tl]);
      ext.values = [ ... (ext.values || []).filter((v: any) => v.$type !== 'camunda:TaskListener'), ...listeners ];
      modeling.updateProperties(task, { extensionElements: ext });
      createMessage.success('监听器已添加');
      closeListenerModal();
      resetListenerFields();
    } catch (e) {}
  }

  // ============ 多实例/会签 ============
  const [registerMultiForm, { validate: validateMulti, setFieldsValue: setMultiFields, resetFields: resetMultiFields }] = useForm({
    labelWidth: 110,
    schemas: [
      { field: 'isMulti', label: '启用多实例', component: 'Switch' },
      { field: 'collection', label: '集合变量', component: 'Input', componentProps: { placeholder: '如：assigneeList 或 ${expr}' } },
      { field: 'elementVariable', label: '元素变量', component: 'Input', componentProps: { placeholder: '如：assignee' } },
      { field: 'isSequential', label: '顺序执行', component: 'Switch', componentProps: {} },
      { field: 'completionCondition', label: '完成条件', component: 'Input', componentProps: { placeholder: '如：${nrOfCompletedInstances/nrOfInstances >= 0.6}' } },
    ],
  });
  function openMultiInstance() {
    const task = getSelectedUserTask();
    if (!task) { createMessage.warning('请选中一个用户任务'); return; }
    const bo = task.businessObject;
    const loop = bo.loopCharacteristics;
    setMultiFields({
      isMulti: !!loop,
      collection: loop?.collection || '',
      elementVariable: loop?.elementVariable || '',
      isSequential: loop?.isSequential || false,
      completionCondition: loop?.completionCondition?.body || '',
    } as any);
    openMultiModal(true);
  }
  async function handleMultiSubmit() {
    try {
      const values = await validateMulti();
      const task = getSelectedUserTask(); if (!task) return;
      const moddle = modeler.get('moddle');
      const modeling = modeler.get('modeling');
      if (!values.isMulti) {
        modeling.updateProperties(task, { loopCharacteristics: null });
        createMessage.success('已关闭多实例');
        closeMultiModal(); resetMultiFields(); return;
      }
      const props: any = {
        isSequential: !!values.isSequential,
        collection: values.collection || undefined,
        elementVariable: values.elementVariable || undefined,
      };
      if (values.completionCondition) {
        props.completionCondition = moddle.create('bpmn:FormalExpression', { body: values.completionCondition });
      }
      const loop = moddle.create('bpmn:MultiInstanceLoopCharacteristics', props);
      modeling.updateProperties(task, { loopCharacteristics: loop });
      createMessage.success('多实例设置已更新');
      closeMultiModal(); resetMultiFields();
    } catch (e) {}
  }

  // ============ 服务/脚本任务 ============
  const [registerSvcScriptForm, { validate: validateSvcScript, setFieldsValue: setSvcScriptFields, resetFields: resetSvcScriptFields }] = useForm({
    labelWidth: 110,
    schemas: [
      { field: 'type', label: '类型', component: 'RadioButtonGroup', required: true, componentProps: { options: [
        { label: 'ServiceTask', value: 'service' },
        { label: 'ScriptTask', value: 'script' },
      ], buttonStyle: 'solid' } },
      { field: 'class', label: 'Java类', component: 'Input' },
      { field: 'expression', label: '表达式', component: 'Input' },
      { field: 'delegateExpression', label: '委派表达式', component: 'Input' },
      { field: 'scriptFormat', label: '脚本语言', component: 'Input', componentProps: { placeholder: 'groovy/javascript' } },
      { field: 'script', label: '脚本内容', component: 'InputTextArea', componentProps: { rows: 4 } },
      { field: 'resultVariable', label: '结果变量', component: 'Input' },
    ],
  });
  function openServiceScript() {
    const selection = modeler?.get('selection');
    const elem = (selection?.get?.() || [])[0];
    if (!elem || !['bpmn:ServiceTask','bpmn:ScriptTask'].includes(elem?.businessObject?.$type)) {
      createMessage.warning('请选中 ServiceTask 或 ScriptTask'); return;
    }
    const bo = elem.businessObject;
    setSvcScriptFields({
      type: bo.$type === 'bpmn:ServiceTask' ? 'service' : 'script',
      class: bo.class || '',
      expression: bo.expression || '',
      delegateExpression: bo.delegateExpression || '',
      scriptFormat: bo.scriptFormat || '',
      script: bo.script || bo.scriptText || '',
      resultVariable: bo.resultVariable || '',
    } as any);
    openSvcScriptModal(true);
  }
  async function handleSvcScriptSubmit() {
    try {
      const values = await validateSvcScript();
      const selection = modeler?.get('selection');
      const elem = (selection?.get?.() || [])[0]; if (!elem) return;
      const modeling = modeler.get('modeling');
      const props: any = {};
      if (values.type === 'service') {
        ['class','expression','delegateExpression'].forEach((k) => props[k] = values[k] || undefined);
      } else {
        props.scriptFormat = values.scriptFormat || undefined;
        props.script = values.script || undefined;
        props.resultVariable = values.resultVariable || undefined;
      }
      modeling.updateProperties(elem, props);
      createMessage.success('任务属性已更新');
      closeSvcScriptModal(); resetSvcScriptFields();
    } catch (e) {}
  }

  // ============ 元素模板 ============
  const [registerTplForm, { validate: validateTpl, resetFields: resetTplFields }] = useForm({
    labelWidth: 110,
    schemas: [
      { field: 'tpl', label: '选择模板', component: 'Select', required: true, componentProps: { options: [
        { label: '发起即办理（Start → 当前任务）', value: 'start_to_current' },
        { label: '发起确认 → 办理（Start → 发起确认 → 当前）', value: 'start_confirm_to_current' },
        { label: '审批任务（用户任务+表单Key+默认权限）', value: 'approve' },
        { label: '审批任务（含默认按钮集）', value: 'approve_buttons' },
        { label: '会签任务（并行多实例）', value: 'countersign_parallel' },
        { label: '会签任务（串行多实例）', value: 'countersign_sequential' },
        { label: '服务任务（Java类）', value: 'service_class' },
        { label: '脚本服务（ScriptTask）', value: 'script_service' },
      ], allowClear: false } },
    ],
  });
  function openElementTemplate() { openTplModal(true); }
  async function handleTplSubmit() {
    try {
      const { tpl } = await validateTpl();
      const selection = modeler?.get('selection');
      const elem = (selection?.get?.() || [])[0]; if (!elem) { createMessage.warning('请先选中元素'); return; }
      const moddle = modeler.get('moddle');
      const modeling = modeler.get('modeling');
      const elementRegistry = modeler.get('elementRegistry');
      const elementFactory = modeler.get('elementFactory');
      const canvas = modeler.get('canvas');
      if (tpl === 'approve') {
        if ((elem.businessObject?.$type) !== 'bpmn:UserTask') { createMessage.warning('审批模板需选中用户任务'); return; }
        modeling.updateProperties(elem, { formKey: 'online-form' });
      } else if (tpl === 'approve_buttons') {
        if ((elem.businessObject?.$type) !== 'bpmn:UserTask') { createMessage.warning('审批模板需选中用户任务'); return; }
        // 设置表单Key
        modeling.updateProperties(elem, { formKey: 'online-form' });
        // 写入默认按钮集到 camunda:Properties
        const bo = elem.businessObject;
        const ext = bo.extensionElements || moddle.create('bpmn:ExtensionElements');
        let propsEl: any = (ext.values || []).find((v: any) => v.$type === 'camunda:Properties');
        if (!propsEl) propsEl = moddle.create('camunda:Properties', { values: [] });
        // 移除旧的 jeecg.buttons
        propsEl.values = (propsEl.values || []).filter((p: any) => !(p.name === 'jeecg.buttons'));
        const buttons = [
          { code: 'agree', text: '同意', action: 'approve' },
          { code: 'reject', text: '驳回', action: 'reject' },
          { code: 'return', text: '退回', action: 'return' },
        ];
        const prop = moddle.create('camunda:Property', { name: 'jeecg.buttons', value: JSON.stringify(buttons) });
        propsEl.values = [...(propsEl.values || []), prop];
        const others = (ext.values || []).filter((v: any) => v.$type !== 'camunda:Properties');
        ext.values = [...others, propsEl];
        modeling.updateProperties(elem, { extensionElements: ext });
      } else if (tpl === 'countersign_parallel') {
        const loop = moddle.create('bpmn:MultiInstanceLoopCharacteristics', { isSequential: false, collection: 'assigneeList', elementVariable: 'assignee' });
        modeling.updateProperties(elem, { loopCharacteristics: loop });
      } else if (tpl === 'countersign_sequential') {
        const loop = moddle.create('bpmn:MultiInstanceLoopCharacteristics', { isSequential: true, collection: 'assigneeList', elementVariable: 'assignee' });
        modeling.updateProperties(elem, { loopCharacteristics: loop });
      } else if (tpl === 'service_class') {
        modeling.updateProperties(elem, { class: 'org.example.DemoServiceTask' });
      } else if (tpl === 'script_service') {
        if ((elem.businessObject?.$type) !== 'bpmn:ScriptTask') { createMessage.warning('脚本服务模板需选中 ScriptTask'); return; }
        modeling.updateProperties(elem, { scriptFormat: 'groovy', script: "// TODO: 编写脚本\nreturn true;", resultVariable: 'result' });
      } else if (tpl === 'start_to_current') {
        const root = canvas.getRootElement();
        let start = (elementRegistry.getAll() || []).find((e: any) => e?.businessObject?.$type === 'bpmn:StartEvent');
        if (!start) {
          const target: any = elem;
          const x = (target.x || 0) - 160;
          const y = (target.y || 0);
          const startShape = elementFactory.createShape({ type: 'bpmn:StartEvent' });
          start = modeling.createShape(startShape, { x, y }, root);
        }
        const hasConn = (start.outgoing || []).some((c: any) => c.target === elem);
        if (!hasConn) modeling.connect(start, elem);
        createMessage.success('已设置：发起即办理');
      } else if (tpl === 'start_confirm_to_current') {
        const root = canvas.getRootElement();
        let start = (elementRegistry.getAll() || []).find((e: any) => e?.businessObject?.$type === 'bpmn:StartEvent');
        if (!start) {
          const target: any = elem;
          const x = (target.x || 0) - 220;
          const y = (target.y || 0);
          const startShape = elementFactory.createShape({ type: 'bpmn:StartEvent' });
          start = modeling.createShape(startShape, { x, y }, root);
        }
        const direct = (start.outgoing || []).find((c: any) => c.target === elem);
        if (direct) modeling.removeElements([direct]);
        const target: any = elem;
        const midx = (start.x + target.x) / 2;
        const midy = target.y;
        const utShape = elementFactory.createShape({ type: 'bpmn:UserTask' });
        const confirmTask = modeling.createShape(utShape, { x: midx, y: midy }, root);
        modeling.updateProperties(confirmTask, { name: '物业发起确认', 'camunda:assignee': '${initiator}' } as any);
        modeling.connect(start, confirmTask);
        modeling.connect(confirmTask, elem);
        createMessage.success('已设置：发起确认 → 办理');
      }
      createMessage.success('模板已应用');
      closeTplModal(); resetTplFields();
    } catch (e) {}
  }

  // ============ 执行监听器 ============
  const [registerExecForm, { validate: validateExec, setFieldsValue: setExecFields, resetFields: resetExecFields }] = useForm({
    labelWidth: 100,
    schemas: [
      { field: 'event', label: '事件', component: 'Select', required: true, componentProps: { options: [
        { label: 'start', value: 'start' },
        { label: 'end', value: 'end' },
      ], allowClear: false } },
      { field: 'class', label: 'Java类', component: 'Input' },
      { field: 'expression', label: '表达式', component: 'Input' },
      { field: 'delegateExpression', label: '委派表达式', component: 'Input' },
    ],
  });
  const execListenerList = ref<any[]>([]);
  const execListenerColumns = [
    { title: '事件', dataIndex: 'event', width: 120 },
    { title: '类型', dataIndex: '__type', width: 140 },
    { title: '值', dataIndex: '__value' },
    { title: '操作', key: 'action', width: 120 },
  ];
  function getSelectedFlowElement(): any | null {
    const selection = modeler?.get('selection');
    const elem = (selection?.get?.() || [])[0];
    return elem || null;
  }
  function openExecListener() {
    const elem = getSelectedFlowElement();
    if (!elem) { createMessage.warning('请先选中一个元素'); return; }
    setExecFields({ event: 'start', class: '', expression: '', delegateExpression: '' } as any);
    openExecModal(true);
  }
  function openExecListenerManager() {
    const elem = getSelectedFlowElement(); if (!elem) { createMessage.warning('请先选中一个元素'); return; }
    refreshExecListenerListFromElem(elem);
    openExecListModal(true);
  }
  function refreshExecListenerListFromElem(elem: any) {
    const bo = elem.businessObject || {};
    const ext = bo.extensionElements;
    const items = (ext?.values || []).filter((v: any) => v.$type === 'camunda:ExecutionListener');
    execListenerList.value = items.map((it: any, idx: number) => ({
      __k: `${idx}`,
      event: it.event || '',
      __type: it.class ? 'class' : it.delegateExpression ? 'delegateExpression' : it.expression ? 'expression' : '',
      __value: it.class || it.delegateExpression || it.expression || '',
      __idx: idx,
    }));
  }
  async function handleExecListenerSubmit() {
    try {
      const values = await validateExec();
      const elem = getSelectedFlowElement(); if (!elem) return;
      const moddle = modeler.get('moddle');
      const modeling = modeler.get('modeling');
      const bo = elem.businessObject;
      const ext = bo.extensionElements || moddle.create('bpmn:ExtensionElements');
      let ls = (ext.values || []).filter((v: any) => v.$type === 'camunda:ExecutionListener');
      const el = moddle.create('camunda:ExecutionListener', {
        event: values.event,
        class: values.class || undefined,
        expression: values.expression || undefined,
        delegateExpression: values.delegateExpression || undefined,
      });
      ls = ls.concat([el]);
      ext.values = [ ... (ext.values || []).filter((v: any) => v.$type !== 'camunda:ExecutionListener'), ...ls ];
      modeling.updateProperties(elem, { extensionElements: ext });
      createMessage.success('执行监听器已添加');
      try { refreshExecListenerListFromElem(elem); } catch (e) {}
      closeExecModal(); resetExecFields();
    } catch (e) {}
  }
  function removeExecListener(record: any) {
    const elem = getSelectedFlowElement(); if (!elem) return;
    const bo = elem.businessObject;
    const moddle = modeler.get('moddle');
    const modeling = modeler.get('modeling');
    const ext = bo.extensionElements || moddle.create('bpmn:ExtensionElements');
    const kept = (ext.values || []).filter((v: any) => !(v.$type === 'camunda:ExecutionListener'));
    const items = (ext.values || []).filter((v: any) => v.$type === 'camunda:ExecutionListener');
    const newItems = items.filter((_: any, i: number) => i !== record.__idx);
    ext.values = [...kept, ...newItems];
    modeling.updateProperties(elem, { extensionElements: ext });
    refreshExecListenerListFromElem(elem);
    createMessage.success('已删除执行监听器');
  }

  // ============ 定时事件（中间/边界） ============
  const [registerTimerForm, { validate: validateTimer, setFieldsValue: setTimerFields, resetFields: resetTimerFields }] = useForm({
    labelWidth: 110,
    schemas: [
      { field: 'type', label: '定时类型', component: 'Select', required: true, componentProps: { options: [
        { label: 'timeDate', value: 'timeDate' },
        { label: 'timeDuration', value: 'timeDuration' },
        { label: 'timeCycle', value: 'timeCycle' },
      ], allowClear: false } },
      { field: 'value', label: '值', component: 'Input', required: true, componentProps: { placeholder: 'ISO日期 / PT2H / R3/PT10M' } },
      { field: 'cancelActivity', label: 'cancelActivity(边界事件)', component: 'Switch' },
    ],
  });
  // Spring cron 到 timeCycle 的映射提示：直接使用 cron 表达式会存入 timeCycle
  function openExecBulkManager() {
    const elementRegistry = modeler?.get('elementRegistry');
    const all = elementRegistry?.getAll?.() || [];
    execBulkList.value = [];
    all.forEach((el: any) => {
      const bo = el.businessObject || {};
      const ext = bo.extensionElements;
      const items = (ext?.values || []).filter((v: any) => v.$type === 'camunda:ExecutionListener');
      items.forEach((it: any, idx: number) => {
        execBulkList.value.push({
          __rk: `${el.id}_${idx}`,
          elementId: el.id,
          elementType: bo.$type,
          event: it.event || '',
          __type: it.class ? 'class' : it.delegateExpression ? 'delegateExpression' : it.expression ? 'expression' : '',
          __value: it.class || it.delegateExpression || it.expression || '',
          __idx: idx,
        });
      });
    });
    openExecBulkModal(true);
  }
  const execBulkList = ref<any[]>([]);
  const execBulkColumns = [
    { title: '元素ID', dataIndex: 'elementId', width: 200 },
    { title: '类型', dataIndex: 'elementType', width: 160 },
    { title: '事件', dataIndex: 'event', width: 100 },
    { title: '监听器类型', dataIndex: '__type', width: 140 },
    { title: '值', dataIndex: '__value' },
    { title: '操作', key: 'action', width: 120 },
  ];
  function removeExecListenerAt(record: any) {
    const elementRegistry = modeler.get('elementRegistry');
    const el = elementRegistry.get(record.elementId);
    if (!el) return;
    const bo = el.businessObject;
    const moddle = modeler.get('moddle');
    const modeling = modeler.get('modeling');
    const ext = bo.extensionElements || moddle.create('bpmn:ExtensionElements');
    const kept = (ext.values || []).filter((v: any) => !(v.$type === 'camunda:ExecutionListener'));
    const items = (ext.values || []).filter((v: any) => v.$type === 'camunda:ExecutionListener');
    const newItems = items.filter((_: any, i: number) => i !== record.__idx);
    ext.values = [...kept, ...newItems];
    modeling.updateProperties(el, { extensionElements: ext });
    execBulkList.value = execBulkList.value.filter((r) => r !== record);
    createMessage.success('已删除执行监听器');
  }

  // 事件定义（消息/信号/错误）
  const [registerEventDefForm, { validate: validateEventDef, setFieldsValue: setEventDefFields, resetFields: resetEventDefFields }] = useForm({
    labelWidth: 110,
    schemas: [
      { field: 'type', label: '事件类型', component: 'Select', required: true, componentProps: { options: [
        { label: '消息', value: 'message' },
        { label: '信号', value: 'signal' },
        { label: '错误', value: 'error' },
      ], allowClear: false } },
      { field: 'ref', label: '标识', component: 'Input', required: true, componentProps: { placeholder: 'messageRef/signalRef/errorRef' } },
      { field: 'errorCode', label: '错误码', component: 'Input', componentProps: { placeholder: '仅错误事件需要' } },
    ],
  });
  function openEventDefConfig() {
    const selection = modeler?.get('selection');
    const elem = (selection?.get?.() || [])[0];
    if (!elem) { createMessage.warning('请选择事件元素'); return; }
    setEventDefFields({ type: 'message', ref: '', errorCode: '' } as any);
    openEventDefModal(true);
  }
  async function handleEventDefSubmit() {
    try {
      const values = await validateEventDef();
      const selection = modeler?.get('selection');
      const elem = (selection?.get?.() || [])[0]; if (!elem) return;
      const moddle = modeler.get('moddle');
      const modeling = modeler.get('modeling');
      let def: any = null;
      if (values.type === 'message') {
        def = moddle.create('bpmn:MessageEventDefinition', { messageRef: values.ref });
      } else if (values.type === 'signal') {
        def = moddle.create('bpmn:SignalEventDefinition', { signalRef: values.ref });
      } else if (values.type === 'error') {
        def = moddle.create('bpmn:ErrorEventDefinition', { errorRef: values.ref, errorCode: values.errorCode || undefined });
      }
      modeling.updateProperties(elem, { eventDefinitions: def ? [def] : [] });
      createMessage.success('事件定义已设置');
      closeEventDefModal(); resetEventDefFields();
    } catch (e) {}
  }
  function openTimerConfig() {
    const selection = modeler?.get('selection');
    const elem = (selection?.get?.() || [])[0];
    if (!elem || !['bpmn:IntermediateCatchEvent','bpmn:BoundaryEvent'].includes(elem?.businessObject?.$type)) {
      createMessage.warning('请选中中间事件或边界事件'); return;
    }
    const bo = elem.businessObject;
    const def = (bo.eventDefinitions || [])[0];
    let type = '', value = '', cancelActivity = bo.cancelActivity || false;
    if (def) {
      const body = def.timeDate?.body || def.timeDuration?.body || def.timeCycle?.body || '';
      if (def.timeDate) type = 'timeDate';
      else if (def.timeDuration) type = 'timeDuration';
      else if (def.timeCycle) type = 'timeCycle';
      value = body;
    }
    setTimerFields({ type, value, cancelActivity } as any);
    openTimerModal(true);
  }
  async function handleTimerSubmit() {
    try {
      const values = await validateTimer();
      const selection = modeler?.get('selection');
      const elem = (selection?.get?.() || [])[0]; if (!elem) return;
      const moddle = modeler.get('moddle');
      const modeling = modeler.get('modeling');
      const bo = elem.businessObject;
      const timer = moddle.create('bpmn:TimerEventDefinition', {});
      timer[values.type] = moddle.create('bpmn:FormalExpression', { body: values.value });
      modeling.updateProperties(elem, {
        eventDefinitions: [timer],
        cancelActivity: (bo.$type === 'bpmn:BoundaryEvent') ? !!values.cancelActivity : undefined,
      });
      createMessage.success('定时事件已设置');
      closeTimerModal(); resetTimerFields();
    } catch (e) {}
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

  // （已移除：页面级消息/alert 拦截逻辑，改为在接口处关闭提示）

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
        createMessage.success({ content: '已载入模型最新版本', duration: 1 });
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
      flex-wrap: wrap;

      .toolbar-left,
      .toolbar-right {
        display: flex;
        align-items: center;
        gap: 8px;
        flex-wrap: wrap;
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
              display: flex;
              align-items: center;
              justify-content: space-between;
              cursor: pointer;
              user-select: none;
              .group-toggle {
                display: inline-block;
                width: 0;
                height: 0;
                border-left: 5px solid transparent;
                border-right: 5px solid transparent;
                border-top: 6px solid #999;
                transition: transform 0.15s ease;
                &.open {
                  transform: rotate(180deg);
                }
              }
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
