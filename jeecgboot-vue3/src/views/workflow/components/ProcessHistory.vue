<template>
  <div class="process-history">
    <a-row :gutter="16">
      <a-col :span="16">
        <a-card title="流程时间线" size="small">
          <ProcessTimeline :process-instance-id="processInstanceId" :compact="false" @select="handleSelect" />
        </a-card>
      </a-col>
      <a-col :span="8">
        <a-space direction="vertical" style="width: 100%">
          <a-card v-if="showComments" title="审批意见" size="small">
            <a-list :data-source="comments" size="small" :pagination="false">
              <template #renderItem="{ item }">
                <a-list-item>
                  <a-space>
                    <a-tag>{{ item.user || '-' }}</a-tag>
                    <span>{{ formatTime(item.time) }}</span>
                  </a-space>
                  <template #description>
                    <div>{{ item.message }}</div>
                  </template>
                </a-list-item>
              </template>
            </a-list>
          </a-card>
          <a-card v-if="showAttachments" title="相关附件" size="small">
            <a-list :data-source="attachments" size="small" :pagination="false">
              <template #renderItem="{ item }">
                <a-list-item>
                  <a-space>
                    <a :href="item.url" target="_blank">{{ item.name }}</a>
                    <a-tag v-if="item.category">{{ item.category }}</a-tag>
                    <span>{{ formatTime(item.time) }}</span>
                  </a-space>
                </a-list-item>
              </template>
            </a-list>
          </a-card>
        </a-space>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
  import { ref, watch } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { formatToDateTime } from '/@/utils/dateUtil';
  import ProcessTimeline from './ProcessTimeline.vue';

  const props = defineProps({
    processInstanceId: { type: String, required: true },
    showComments: { type: Boolean, default: false },
    showAttachments: { type: Boolean, default: false },
  });

  const comments = ref<any[]>([]);
  const attachments = ref<any[]>([]);

  watch(
    () => props.processInstanceId,
    async () => {
      if (!props.processInstanceId) return;
      if (props.showComments || props.showAttachments) {
        await loadDetail();
      }
    },
    { immediate: true }
  );

  async function loadDetail() {
    try {
      // 取最近一次任务的详情作为示例；前端需要全量可在时间线点击触发
      const hist: any = await defHttp.get({ url: '/workflow/render/history', params: { processInstanceId: props.processInstanceId, pageNo: 1, pageSize: 1 } });
      const item = (hist?.items && hist.items[0]) || null;
      if (!item) { comments.value = []; attachments.value = []; return; }
      const detail: any = await defHttp.get({ url: '/workflow/render/history/detail', params: { processInstanceId: props.processInstanceId, nodeId: item.nodeId, taskId: item.id } });
      comments.value = detail?.comments || [];
      attachments.value = detail?.attachments || [];
    } catch {
      comments.value = [];
      attachments.value = [];
    }
  }

  async function handleSelect(payload: { nodeId: string; taskId: string }) {
    try {
      const detail: any = await defHttp.get({ url: '/workflow/render/history/detail', params: { processInstanceId: props.processInstanceId, nodeId: payload.nodeId, taskId: payload.taskId } });
      comments.value = detail?.comments || [];
      attachments.value = detail?.attachments || [];
    } catch {
      comments.value = [];
      attachments.value = [];
    }
  }

  function formatTime(t: any) {
    return formatToDateTime(t);
  }
</script>

<style scoped>
  .process-history {
    padding: 8px 0;
  }
</style>
