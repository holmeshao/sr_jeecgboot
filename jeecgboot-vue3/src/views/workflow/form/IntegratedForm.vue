<template>
  <div class="integrated-form-page">
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
            :editable="!!currentTaskId"
            :processInstanceId="processInstanceId || ''"
            :currentTaskId="currentTaskId || ''"
            :latestTaskId="latestTaskId || ''"
          />
          <a-empty v-else description="当前节点无扩展配置" />
        </a-card>
        <a-card title="业务字段（在线表单）" :bordered="false" :loading="bizLoading">
          <WorkflowOnlineBridge
            ref="workflowFormRef"
            v-if="tableName"
            :table="tableName"
            :dataId="dataId"
            :permissions="permissions"
          />
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
        </a-card>
      </a-col>
    </a-row>

    <div class="footer-actions" v-if="true">
      <a-space>
        <a-input-textarea v-model:value="comment" :rows="2" placeholder="处理意见（可选）" style="width: 360px" />
        <SmartButtonGroup
          :formId="tableName || formId"
          :dataId="dataId"
          :taskId="currentTaskId || undefined"
          @submit="onSubmit"
          @approve="handleApprove"
          @reject="handleReject"
          @claim="handleClaim"
          @unclaim="handleUnclaim"
          @save="onSaveDraft"
        />
        <a-button @click="showHistory=true">历史</a-button>
        <a-popconfirm title="添加处理意见？" ok-text="确定" cancel-text="取消" @confirm="addComment">
          <a-button type="dashed">添加意见</a-button>
        </a-popconfirm>
      </a-space>
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
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import NodeBlock from '/@/components/jeecg/NodeBlock.vue';
import WorkflowOnlineBridge from '/@/components/jeecg/OnlineForm/WorkflowOnlineBridge.vue';
import SmartButtonGroup from '/@/views/workflow/components/SmartButtonGroup.vue';
import { workflowRenderApi, workflowTaskApi } from '/@/api/workflow';
import { defHttp } from '/@/utils/http/axios';

const route = useRoute();
const router = useRouter();

const formId = (route.query.formId as string) || '';
const processDefinitionKey = (route.query.processDefinitionKey as string) || '';
const nodeId = (route.query.nodeId as string) || '';
const processInstanceId = (route.query.processInstanceId as string) || '';
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
    // 新增态：直接走后端 /workflow/onlineForm/submitForm（负责保存+按配置启动）
    if (!currentTaskId) {
      // 统一走后端事务接口：提交即保存并发起流程
      if (formRef?.validate) await formRef.validate();
      // 改为：先让在线运行时真正执行提交，让它在 success 事件里给出最终提交值；
      // 再将该值（或兜底 getData）传给后端的一次性事务接口
      let payload: any = {};
      if (typeof formRef?.submitWithResult === 'function') {
        payload = await formRef.submitWithResult(6000);
      }
      if (!payload || Object.keys(payload).length === 0) {
        if (typeof formRef?.getData === 'function') payload = await formRef.getData();
      }
      if (!payload || Object.keys(payload).length === 0) {
        message.error('未能采集到表单数据，请检查字段是否已填写');
        return;
      }

      const submitUrl = `/workflow/onlineForm/submitForm?tableName=${encodeURIComponent(tableName)}&dataId=${encodeURIComponent(dataId || '')}`;
      const resp: any = await defHttp.post({ url: submitUrl, data: payload });
      const r = resp?.result || resp;
      const action = r?.action || 'form_saved';
      if (action === 'workflow_started') message.success('已提交并发起流程');
      else message.success('提交成功');
      await goBackToList();
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
</style>


