<template>
  <div class="process-history">
    <a-row :gutter="16">
      <a-col :span="16">
        <a-card title="流程时间线" size="small">
          <ProcessTimeline :process-instance-id="processInstanceId" :compact="false" />
        </a-card>
      </a-col>
      <a-col :span="8">
        <a-space direction="vertical" style="width: 100%">
          <a-card v-if="showComments" title="审批意见" size="small">
            <a-empty description="暂未实现，后续接入后端意见列表接口" />
          </a-card>
          <a-card v-if="showAttachments" title="相关附件" size="small">
            <a-empty description="暂未实现，后续接入附件列表接口" />
          </a-card>
        </a-space>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
  import ProcessTimeline from './ProcessTimeline.vue';

  defineProps({
    processInstanceId: { type: String, required: true },
    showComments: { type: Boolean, default: false },
    showAttachments: { type: Boolean, default: false },
  });
</script>

<style scoped>
  .process-history {
    padding: 8px 0;
  }
</style>
