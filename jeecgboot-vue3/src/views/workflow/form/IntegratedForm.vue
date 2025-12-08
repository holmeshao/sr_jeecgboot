<template>
<div class="integrated-form-page" :class="{ 'readonly-mode': isReadonly }">
    <a-card :bordered="false" style="margin-bottom: 16px">
      <a-steps :current="0" size="small">
        <a-step :title="'当前节点：' + (nodeId || '-')" />
      </a-steps>
    </a-card>

    <a-row :gutter="24">
      <a-col :span="18">
        <a-card title="节点扩展" :loading="loading" :bordered="false" style="margin-bottom: 16px">
          <NodeBlock
            v-if="components.length"
            :components="components"
            :permissions="permissions"
            :model="nodeModel"
            :editable="!!currentTaskId && !isReadonly"
            :processInstanceId="processInstanceId || ''"
            :currentTaskId="currentTaskId || ''"
            :latestTaskId="latestTaskId || ''"
          />
          <a-empty v-else description="当前节点无扩展配置" />
        </a-card>
        <a-card title="业务字段（在线表单）" :bordered="false" :loading="bizLoading">
          <div style="position:relative">
            <WorkflowOnlineBridge
              ref="workflowFormRef"
              v-if="tableName"
              :table="tableName"
              :dataId="dataId"
              :permissions="permissionsForOnline"
            />
            <div v-if="isReadonly" style="position:absolute;inset:0;z-index:2;background:transparent;pointer-events:auto"></div>
          </div>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card title="流程信息" :bordered="false">
          <a-descriptions size="small" :column="1">
            <a-descriptions-item label="表单ID">{{ tableName || formId }}</a-descriptions-item>
            <a-descriptions-item label="流程Key">{{ processDefinitionKey }}</a-descriptions-item>
            <a-descriptions-item label="节点ID">{{ nodeId }}</a-descriptions-item>
            <a-descriptions-item label="实例ID">{{ processInstanceId || '-' }}</a-descriptions-item>
            <a-descriptions-item label="任务ID">{{ currentTaskId || '-' }}</a-descriptions-item>
          </a-descriptions>
          <div v-if="processInstanceId" ref="diagramBoxRef" style="margin-top:12px; position:relative">
            <img :src="diagramDataUrl" ref="diagramImgRef" @load="onDiagramLoad" style="width:100%; display:block" />
            <div v-for="tip in diagramTips" :key="tip.id"
                 :style="{
                   position:'absolute',
                   left: Math.round(tip.x * diagramScale) + 'px',
                   top: Math.round(tip.y * diagramScale) + 'px',
                   width: Math.round(tip.width * diagramScale) + 'px',
                   height: Math.round(tip.height * diagramScale) + 'px',
                   pointerEvents:'auto'
                 }"
            >
              <a-tooltip placement="top">
                <template #title>
                  <div>
                    <div>节点：{{ tip.name }}</div>
                    <div v-for="t in tip.tasks" :key="t.id">
                      处理人：{{ t.assignee || '-' }}
                      <span v-if="t.startTime"> 开始：{{ formatTs(t.startTime) }}</span>
                      <span v-if="t.endTime"> 结束：{{ formatTs(t.endTime) }}</span>
                      <span v-if="t.duration"> 耗时：{{ Math.round(t.duration/60000) }} 分</span>
                      <div v-if="Array.isArray(t.comments) && t.comments.length" style="margin-top:4px">
                        意见：
                        <div v-for="c in t.comments" :key="c.time">- {{ c.user || '-' }}：{{ c.message }}</div>
                      </div>
                    </div>
                  </div>
                </template>
                <div :style="nodeBoxStyle(tip.id)"></div>
              </a-tooltip>
            </div>
            <div style="text-align:right; margin-top:8px">
              <a-button type="link" size="small" @click="diagramPreviewOpen = true">查看流程图</a-button>
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>

    <div class="footer-actions" v-if="showActionBar">
      <a-spin :spinning="workflowLoading" size="small">
        <a-space>
          <!-- 处理意见输入框：仅在有任务且可审批时显示 -->
          <a-input-textarea 
            v-model:value="comment" 
            :rows="2" 
            placeholder="处理意见（可选）" 
            style="width: 360px" 
            v-if="workflowContext?.canApprove || workflowContext?.hasTask" 
          />
          
          <!-- 动态工作流按钮 -->
          <template v-for="btn in workflowButtons" :key="btn.code">
            <a-button
              :type="btn.type === 'danger' ? 'primary' : btn.type"
              :danger="btn.type === 'danger'"
              :loading="btn.loading"
              @click="handleButtonClick(btn.code)"
            >
              <template #icon v-if="btn.icon">
                <component :is="btn.icon" />
              </template>
              {{ btn.name }}
            </a-button>
          </template>
          
          <!-- 历史按钮 -->
          <a-button @click="showHistory=true" v-if="processInstanceId">
            <template #icon><HistoryOutlined /></template>
            历史
          </a-button>
          
          <!-- 添加意见按钮 -->
          <a-popconfirm 
            title="添加处理意见？" 
            ok-text="确定" 
            cancel-text="取消" 
            @confirm="addComment" 
            v-if="workflowContext?.hasTask && comment"
          >
            <a-button type="dashed">添加意见</a-button>
          </a-popconfirm>
        </a-space>
      </a-spin>
    </div>

    <a-drawer v-model:open="showHistory" title="流程历史" placement="right" width="560">
      <a-timeline>
        <a-timeline-item v-for="h in histories" :key="h.id">
          <div class="history-item">
            <div class="history-header">
              <b>{{ h.nodeName || h.nodeId }}</b>
              <span class="history-time">{{ formatTs(h.timestamp) }}</span>
            </div>
            <div class="history-meta">操作人：{{ h.operator || '-' }}</div>
            <div class="history-changes" v-if="Array.isArray(h.changedFields) && h.changedFields.length">
              变更字段：
              <a-space wrap>
                <a-tag v-for="f in h.changedFields.slice(0,6)" :key="f">{{ f }}</a-tag>
              </a-space>
            </div>
            <div class="history-actions">
              <a-space>
                <a-button size="small" type="link" @click="viewDetail(h)">查看详情</a-button>
                <a-button size="small" type="link" @click="compareWithCurrent(h)">与当前对比</a-button>
              </a-space>
            </div>
          </div>
        </a-timeline-item>
      </a-timeline>
    </a-drawer>

    <a-drawer v-model:open="detailOpen" title="历史详情" placement="right" width="720">
      <div v-if="detail">
        <a-descriptions bordered size="small" :column="2" style="margin-bottom:12px">
          <a-descriptions-item label="节点">{{ detail.nodeName || detail.nodeId }}</a-descriptions-item>
          <a-descriptions-item label="时间">{{ formatTs(detail.timestamp) }}</a-descriptions-item>
          <a-descriptions-item label="操作人">{{ detail.operator || '-' }}</a-descriptions-item>
          <a-descriptions-item label="任务ID">{{ detail.taskId }}</a-descriptions-item>
        </a-descriptions>
        <a-card size="small" title="变更字段" v-if="detail.snapshot?.changedFields?.length">
          <a-space wrap>
            <a-tag v-for="f in detail.snapshot.changedFields" :key="f">{{ f }}</a-tag>
          </a-space>
        </a-card>
        <a-card size="small" title="当次字段值" style="margin-top:12px" v-if="detail.snapshot?.formData">
          <pre style="white-space:pre-wrap">{{ JSON.stringify(detail.snapshot.formData, null, 2) }}</pre>
        </a-card>
        <a-card size="small" title="附件" style="margin-top:12px">
          <a-list :data-source="detail.attachments || []" size="small">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-space>
                  <a :href="item.url" target="_blank">{{ item.name }}</a>
                  <a-tag v-if="item.category">{{ item.category }}</a-tag>
                </a-space>
                <template #actions>
                  <span>{{ formatTs(item.time) }}</span>
                </template>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
        <a-card size="small" title="评论" style="margin-top:12px">
          <a-list :data-source="detail.comments || []" size="small">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-space>
                  <a-tag>{{ item.user || '-' }}</a-tag>
                  <span>{{ item.message }}</span>
                </a-space>
                <template #actions>
                  <span>{{ formatTs(item.time) }}</span>
                </template>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
      </div>
    </a-drawer>

    <a-drawer v-model:open="compareOpen" title="版本对比" placement="right" width="880">
      <div v-if="compareData">
        <a-table :pagination="false" size="small" :data-source="compareRows" :columns="compareColumns" rowKey="field" />
      </div>
    </a-drawer>

    <a-modal v-model:open="diagramPreviewOpen" :title="'流程图 — ' + (processTitle || '流程')" :footer="null" width="90%" :bodyStyle="{ padding: '8px 12px' }">
      <ProcessBpmnViewer :instanceId="processInstanceId as any" :meta="bpmnMeta" @titleChange="processTitle = $event" />
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, computed, watch, onBeforeUnmount } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import NodeBlock from '/@/components/jeecg/NodeBlock.vue';
import WorkflowOnlineBridge from '/@/components/jeecg/OnlineForm/WorkflowOnlineBridge.vue';
import SmartButtonGroup from '/@/views/workflow/components/SmartButtonGroup.vue';
import { workflowRenderApi, workflowTaskApi } from '/@/api/workflow';
import { defHttp } from '/@/utils/http/axios';
import ProcessBpmnViewer from '/@/views/workflow/components/ProcessBpmnViewer.vue';
import { useWorkflowButtons, createDefaultButtonHandlers, WorkflowTaskContext } from './useWorkflowButtons';
import { HistoryOutlined } from '@ant-design/icons-vue';

const route = useRoute();
const router = useRouter();

const formId = (route.query.formId as string) || '';
const processDefinitionKey = (route.query.processDefinitionKey as string) || '';
const nodeId = (route.query.nodeId as string) || '';
const processInstanceId = ref<string>((route.query.processInstanceId as string) || '');
const currentTaskId = (route.query.taskId as string) || '';
// 兼容多入口：优先取 query.tableName，其次 formId，再次通用页的 params
const tableName = (route.query.tableName as string) || (route.query.formId as string) || (route.params.formType as string) || '';
const dataId = (route.query.dataId as string) || (route.params.dataId as string) || '';

const loading = ref(false);
const submitting = ref(false);
const permissions = ref<any>({});
const components = ref<any[]>([]);
const latestTaskId = ref<string>('');
const nodeModel = reactive<Record<string, any>>({});
const comment = ref<string>('');
const showHistory = ref<boolean>(false);
const histories = ref<any[]>([]);
const detailOpen = ref(false);
const detail = ref<any>(null);
const compareOpen = ref(false);
const compareData = ref<any>(null);
const compareColumns = [
  { title: '字段', dataIndex: 'field', key: 'field', width: 200 },
  { title: '历史值', dataIndex: 'left', key: 'left' },
  { title: '当前值', dataIndex: 'right', key: 'right' },
];
const compareRows = computed(() => {
  const arr = compareData.value?.diff || [];
  return Array.isArray(arr) ? arr : [];
});

const bizLoading = ref(false);
const workflowFormRef = ref<any>();
// const diagramUrl = computed(() => processInstanceId.value ? `/workflow/instance/${processInstanceId.value}/diagram.png?_=${Date.now()}` : '');
const diagramDataUrl = ref<string>('');
const diagramTips = ref<any[]>([]);
const activeIds = ref<string[]>([]);
const completedIds = ref<string[]>([]);
const executedFlows = ref<any[]>([]);
const diagramScale = ref<number>(1);
const diagramImgRef = ref<HTMLImageElement | null>(null);
const diagramBoxRef = ref<HTMLDivElement | null>(null);
const diagramPreviewOpen = ref<boolean>(false);
const processTitle = ref<string>('');
const diagramScaleModal = ref<number>(1);
const diagramModalImgRef = ref<HTMLImageElement | null>(null);
const diagramModalBoxRef = ref<HTMLDivElement | null>(null);

// ============== 工作流按钮 Hook ==============
const workflowParams = computed(() => ({
  taskId: currentTaskId || undefined,
  processInstanceId: processInstanceId.value || undefined,
  dataId: dataId || undefined,
  action: (route.query.action as 'add' | 'edit' | 'view') || undefined,
}));

// 创建按钮点击处理器
const buttonClickHandler = createDefaultButtonHandlers({
  onSubmit: async (ctx) => {
    await onSubmit();
  },
  onSave: async (ctx) => {
    onSaveDraft();
  },
  onApprove: async (ctx) => {
    await handleApproveWithContext(ctx);
  },
  onReject: async (ctx) => {
    await handleRejectWithContext(ctx);
  },
  onClaim: async (ctx) => {
    await handleClaimWithContext(ctx);
  },
  onUnclaim: async (ctx) => {
    await handleUnclaimWithContext(ctx);
  },
  onCancel: () => {
    goBackToList();
  },
  onClose: () => {
    goBackToList();
  },
});

const {
  context: workflowContext,
  loading: workflowLoading,
  buttons: workflowButtons,
  isReadOnly: workflowReadOnly,
  handleButtonClick,
  refresh: refreshWorkflowContext,
} = useWorkflowButtons(workflowParams, buttonClickHandler);

// 带上下文的审批处理
async function handleApproveWithContext(ctx: WorkflowTaskContext) {
  const taskId = ctx.taskId || currentTaskId;
  if (!taskId) return;
  submitting.value = true;
  try {
    const variables: Record<string, any> = { ...nodeModel, approve_result: 'pass' };
    await workflowTaskApi.complete(taskId, { variables, comment: comment.value });
    message.success('已通过');
    await goBackToList();
  } catch (e: any) {
    message.error(e?.message || '操作失败');
  } finally {
    submitting.value = false;
  }
}

async function handleRejectWithContext(ctx: WorkflowTaskContext) {
  const taskId = ctx.taskId || currentTaskId;
  if (!taskId) return;
  submitting.value = true;
  try {
    const variables: Record<string, any> = { ...nodeModel, approve_result: 'reject' };
    await workflowTaskApi.complete(taskId, { variables, comment: comment.value || '驳回' });
    message.success('已驳回');
    await goBackToList();
  } catch (e: any) {
    message.error(e?.message || '操作失败');
  } finally {
    submitting.value = false;
  }
}

async function handleClaimWithContext(ctx: WorkflowTaskContext) {
  const taskId = ctx.taskId || currentTaskId;
  if (!taskId) return;
  try {
    await defHttp.post({ url: '/workflow/task/claim', data: { taskId } });
    message.success('已认领');
    await refreshWorkflowContext();
  } catch (e: any) {
    message.error(e?.message || '操作失败');
  }
}

async function handleUnclaimWithContext(ctx: WorkflowTaskContext) {
  const taskId = ctx.taskId || currentTaskId;
  if (!taskId) return;
  try {
    await defHttp.post({ url: '/workflow/task/unclaim', data: { taskId } });
    message.success('已释放');
    await refreshWorkflowContext();
  } catch (e: any) {
    message.error(e?.message || '操作失败');
  }
}

// 支持显式模式控制：?action=add|edit|view（兼容 mode/intent 参数）
const actionParam = String((route.query.action || route.query.mode || route.query.intent || '') as string).toLowerCase();
const explicitAction = computed<'add'|'edit'|'view'|''>(() => {
  if (['add', 'create', 'new'].includes(actionParam)) return 'add';
  if (['edit', 'update'].includes(actionParam)) return 'edit';
  if (['view', 'detail'].includes(actionParam)) return 'view';
  return '';
});

const isCreateIntent = computed(() => explicitAction.value === 'add' || (!dataId && !currentTaskId));
const isEditIntent = computed(() => explicitAction.value === 'edit');
const isViewIntent = computed(() => explicitAction.value === 'view' || (!!dataId && !currentTaskId));

const isReadonly = computed(() => {
  // 明确的页面意图优先
  if (isCreateIntent.value) return false; // 新增可编辑
  if (isEditIntent.value) return false;   // 编辑可编辑
  if (isViewIntent.value) return true;    // 查看只读

  // 办理入口（存在 taskId）再按权限放开
  if (currentTaskId) {
    // 以工作流上下文的只读标记为准（由后端根据任务权限判断）
    return workflowReadOnly.value;
  }

  // 兜底：无 taskId 且无明确意图 → 认为是新增
  return false;
});

const permissionsForOnline = computed(() => {
  const p = permissions.value || {};
  // 只读模式下，把所有字段放入只读集合（online 运行时适配器会按此禁用）
  if (isReadonly.value) {
    const allKeys = Array.from(new Set([...(p.readonlyFields || []), ...(p.hiddenFields || []), ...(p.editableFields || [])]));
    return { ...p, readonlyFields: allKeys, editableFields: [] };
  }
  return p;
});

const showActionButtons = computed(() => !!currentTaskId);

const showActionBar = computed(() => showActionButtons.value || true);

async function loadRender() {
  if (!formId || !processDefinitionKey || !nodeId) return;
  loading.value = true;
  try {
    const resp: any = await workflowRenderApi.getNodeRender({ formId, processDefinitionKey, nodeId, processInstanceId });
    const data = resp?.result || resp;
    permissions.value = data?.permissions || {};
    components.value = data?.nodeSchema?.components || [];
    latestTaskId.value = data?.latestTaskId || '';
  } catch (e: any) {
    message.error(e?.message || '加载渲染数据失败');
  } finally {
    loading.value = false;
  }
}

async function handleApprove() {
  if (!currentTaskId) return;
  submitting.value = true;
  try {
    const variables: Record<string, any> = { ...nodeModel, approve_result: 'pass' };
    await workflowTaskApi.complete(currentTaskId, { variables, comment: comment.value });
    message.success('已完成');
  } catch (e: any) {
    message.error(e?.message || '操作失败');
  } finally {
    submitting.value = false;
  }
}

async function handleReject() {
  if (!currentTaskId) return;
  submitting.value = true;
  try {
    const variables: Record<string, any> = { ...nodeModel, approve_result: 'reject' };
    await workflowTaskApi.complete(currentTaskId, { variables, comment: comment.value || '驳回' });
    message.success('已驳回');
  } catch (e: any) {
    message.error(e?.message || '操作失败');
  } finally {
    submitting.value = false;
  }
}

async function handleClaim() {
  if (!currentTaskId) return;
  try {
    await defHttp.post({ url: '/workflow/task/claim', data: { taskId: currentTaskId } });
    message.success('已认领');
  } catch (e: any) {
    message.error(e?.message || '操作失败');
  }
}

async function handleUnclaim() {
  if (!currentTaskId) return;
  try {
    await defHttp.post({ url: '/workflow/task/unclaim', data: { taskId: currentTaskId } });
    message.success('已释放');
  } catch (e: any) {
    message.error(e?.message || '操作失败');
  }
}

async function addComment() {
  if (!currentTaskId || !comment.value) {
    message.warning('请输入处理意见');
    return;
  }
  try {
    await defHttp.post({ url: `/workflow/task/${currentTaskId}/comment`, data: { message: comment.value } });
    message.success('已添加意见');
  } catch (e: any) {
    message.error(e?.message || '添加意见失败');
  }
}

async function onSubmit() {
  // 触发在线表单的提交逻辑，确保业务字段与节点扩展一并保存
  const formRef: any = (workflowFormRef as any)?.value;
  try {
    // 新增态：由运行时检测钩子并把数据交回我们；我们只打一枪到单事务接口
    if (!currentTaskId) {
      if (formRef?.validate) await formRef.validate();
      if (formRef?.submit) await formRef.submit();
      else if (formRef?.handleSubmit) await formRef.handleSubmit();
      return;
    }

    // 办理态：存在当前任务，则在业务提交后完成任务并携带节点扩展变量与表单快照
    if (formRef?.submit) {
      await formRef.submit();
    } else if (formRef?.handleSubmit) {
      await formRef.handleSubmit();
    }
    if (currentTaskId) {
      const variables: Record<string, any> = { ...nodeModel, approve_result: 'pass' };
      const snapshot = {
        formData: formRef && formRef.formData ? JSON.parse(JSON.stringify(formRef.formData)) : {},
        nodeModel: { ...nodeModel }
      };
      await workflowTaskApi.complete(currentTaskId, { variables, comment: comment.value, snapshot });
      message.success('已提交并完成当前节点');
      await goBackToList();
    }
  } catch (e: any) {
    message.error(e?.message || '提交失败');
  }
}

function onSaveDraft() {
  // 草稿保存由业务表单负责，这里仅做提示或透传
  const formRef: any = (workflowFormRef as any)?.value;
  if (formRef?.save) {
    formRef.save().then(async () => {
      message.success('草稿已保存');
      await goBackToList();
    });
  } else {
    message.success('已触发保存草稿');
  }
}

onMounted(loadRender);

watch(showHistory, async (val) => {
  if (val && processInstanceId) {
    try {
      const resp: any = await workflowRenderApi.getHistory({ processInstanceId, pageNo: 1, pageSize: 50, formId });
      const data = resp?.result || resp;
      histories.value = data?.items || [];
    } catch {
      histories.value = [];
    }
  }
});

onMounted(loadBizForm);

async function loadBizForm() {
  // 由 WorkflowOnlineForm 负责加载，这里只保留占位与加载状态
  bizLoading.value = false;
}

// 注册/注销 Online 集成提交钩子
onMounted(() => {
  if (!currentTaskId) {
    (window as any).__ONLINE_INTEGRATED_SUBMIT__ = async (collected: any, ctx: any) => {
      const payload = collected && typeof collected === 'object' ? collected : {};
      const submitUrl = `/workflow/onlineForm/submitForm?tableName=${encodeURIComponent(tableName)}&dataId=${encodeURIComponent(dataId || '')}`;
      const resp: any = await defHttp.post({ url: submitUrl, data: payload });
      const r = resp?.result || resp;
      const action = r?.action || 'form_saved';
      if (action === 'workflow_started') message.success('已提交并发起流程');
      else message.success('提交成功');
      await goBackToList();
      return r;
    };
  }
});

onBeforeUnmount(() => {
  if ((window as any).__ONLINE_INTEGRATED_SUBMIT__) {
    try { delete (window as any).__ONLINE_INTEGRATED_SUBMIT__; } catch {}
  }
});

// 加载流程图元信息，用于悬浮提示与高亮
watch(
  () => processInstanceId.value,
  async (pid) => {
    if (!pid) return;
    try {
      const imgResp: any = await defHttp.get({ url: `/workflow/instance/${pid}/diagram` });
      const imgData = imgResp?.result || imgResp || {};
      diagramDataUrl.value = imgData.dataUrl || '';

      const resp: any = await defHttp.get({ url: `/workflow/instance/${pid}/diagram/meta` });
      const data = resp?.result || resp || {};
      activeIds.value = Array.isArray(data.activeIds) ? data.activeIds : [];
      completedIds.value = Array.isArray(data.completedIds) ? data.completedIds : [];
      executedFlows.value = Array.isArray(data.executedFlows) ? data.executedFlows : [];
      const nodes = Array.isArray(data.nodes) ? data.nodes : [];
      const tasks = Array.isArray(data.tasks) ? data.tasks : [];
      const map: Record<string, any[]> = {};
      tasks.forEach((t: any) => {
        const k = t.nodeId || t.taskDefinitionKey;
        if (!map[k]) map[k] = [];
        map[k].push(t);
      });
      diagramTips.value = nodes.map((n: any) => ({
        id: n.id,
        name: n.name,
        x: n.x,
        y: n.y,
        width: n.width,
        height: n.height,
        tasks: map[n.id] || []
      }));
    } catch {}
  },
  { immediate: true }
);

// 若最初没有传 processInstanceId，则尝试从基础信息接口解析（用于查看详情入口）
onMounted(async () => {
  if (!processInstanceId.value && tableName && dataId) {
    try {
      const resp: any = await defHttp.get({ url: `/workflow/onlineForm/form/basic-info`, params: { tableName, dataId } as any });
      const info = resp?.result || resp || {};
      if (info.processInstanceId) processInstanceId.value = String(info.processInstanceId);
    } catch {}
  }
});

function onDiagramLoad() {
  try {
    const img = diagramImgRef.value as any;
    const box = diagramBoxRef.value as any;
    if (!img || !box) return;
    // 原始尺寸（服务端meta坐标即是原图坐标）
    const naturalW = img.naturalWidth || img.width;
    const displayW = img.clientWidth || img.width;
    diagramScale.value = naturalW > 0 ? displayW / naturalW : 1;
  } catch {}
}

const bpmnMeta = computed(() => ({
  activeIds: activeIds.value,
  completedIds: completedIds.value,
  executedFlows: executedFlows.value,
  nodes: diagramTips.value.map((d) => ({ id: d.id, name: d.name })),
  tasks: diagramTips.value.flatMap((d) => (d.tasks || []).map((t: any) => ({ ...t, nodeId: d.id }))),
}));

function onDiagramModalLoad() {
  try {
    const img = diagramModalImgRef.value as any;
    const naturalW = img.naturalWidth || img.width;
    const displayW = img.clientWidth || img.width;
    diagramScaleModal.value = naturalW > 0 ? displayW / naturalW : 1;
  } catch {}
}

function nodeBoxStyle(id: string) {
  const isActive = activeIds.value.includes(id);
  const isDone = completedIds.value.includes(id);
  let border = '1px dashed #999';
  if (isDone) border = '2px solid #52c41a';
  if (isActive) border = '2px solid #ff4d4f';
  return { width: '100%', height: '100%', border, background: 'transparent' } as any;
}

function formatTs(ts?: number) {
  if (!ts) return '';
  const d = new Date(ts);
  const p = (n: number) => String(n).padStart(2, '0');
  return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())} ${p(d.getHours())}:${p(d.getMinutes())}`;
}

async function viewDetail(h: any) {
  try {
    const resp: any = await workflowRenderApi.getHistoryDetail({ processInstanceId, nodeId: h.nodeId, taskId: h.id });
    detail.value = resp?.result || resp || null;
    detailOpen.value = true;
  } catch (e) {
    detail.value = null;
    detailOpen.value = false;
  }
}

async function compareWithCurrent(h: any) {
  try {
    const resp: any = await workflowRenderApi.compareLatest({ processInstanceId, nodeId: h.nodeId, taskId: h.id, formId });
    compareData.value = resp?.result || resp || null;
    compareOpen.value = true;
  } catch (e) {
    compareData.value = null;
    compareOpen.value = false;
  }
}

// 跳回在线表单列表（以 tableName 解析 formId）
async function goBackToList() {
  try {
    if (!tableName) return;
    const resp: any = await defHttp.get({ url: `/online/cgform/api/getFormItemBytbname/${tableName}` });
    const headId: string = resp?.head?.id || resp?.result?.head?.id || '';
    if (headId) {
      await router.replace(`/online/cgformList/${headId}`);
    }
  } catch (e) {
    // ignore
  }
}
</script>

<style scoped>
.integrated-form-page {
  padding-bottom: 64px;
}
.footer-actions {
  position: sticky;
  bottom: 0;
  background: #fff;
  padding: 12px 16px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: flex-end;
}
.readonly-mode :deep(.ant-btn-primary) { display: none !important; }

/* 隐藏 bpmn.io 角标 */
:deep(.bjs-powered-by) { display: none !important; }
</style>


