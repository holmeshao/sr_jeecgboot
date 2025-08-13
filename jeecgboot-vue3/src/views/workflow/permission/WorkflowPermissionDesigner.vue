<template>
  <a-modal v-model:open="visible" title="字段权限配置器" width="1000px" :footer="null" destroyOnClose>
    <div class="designer-config-form">
      <a-form layout="inline">
        <a-form-item label="表单ID">
          <a-input v-model:value="localFormId" placeholder="请输入表单ID" style="width: 260px" />
        </a-form-item>
        <a-form-item label="流程定义Key">
          <a-input v-model:value="localProcessDefinitionKey" placeholder="请输入流程定义Key" style="width: 260px" />
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button type="primary" @click="applyAndShow">加载配置</a-button>
            <a-button @click="close">关闭</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </div>

    <FieldPermissionDesigner
      v-if="ready"
      :formId="localFormId"
      :processDefinitionKey="localProcessDefinitionKey"
      :formName="localFormName"
      :processName="localProcessName"
      @success="handleSuccess"
    />
    <a-empty v-else description="请输入表单ID和流程定义Key后点击加载配置" />
  </a-modal>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import FieldPermissionDesigner from '../components/FieldPermissionDesigner.vue';

  const visible = ref(false);
  const ready = ref(false);

  const localFormId = ref('');
  const localProcessDefinitionKey = ref('');
  const localFormName = ref('');
  const localProcessName = ref('');

  const emit = defineEmits<{
    (e: 'success'): void;
  }>();

  function open(options?: { formId?: string; processDefinitionKey?: string; formName?: string; processName?: string }) {
    if (options) {
      if (options.formId) localFormId.value = options.formId;
      if (options.processDefinitionKey) localProcessDefinitionKey.value = options.processDefinitionKey;
      if (options.formName) localFormName.value = options.formName;
      if (options.processName) localProcessName.value = options.processName;
    }
    ready.value = Boolean(localFormId.value && localProcessDefinitionKey.value);
    visible.value = true;
  }

  function applyAndShow() {
    ready.value = Boolean(localFormId.value && localProcessDefinitionKey.value);
  }

  function close() {
    visible.value = false;
  }

  function handleSuccess() {
    emit('success');
  }

  defineExpose({ open, close });
</script>

<style scoped>
  .designer-config-form {
    margin-bottom: 16px;
    padding: 12px;
    background: #fafafa;
    border-radius: 6px;
  }
</style>
