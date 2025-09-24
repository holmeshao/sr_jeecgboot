<template>
  <a-card :title="title" size="small" :bordered="false">
    <div style="margin-bottom: 8px" v-if="editable">
      <a-upload :showUploadList="false" :beforeUpload="handleUploadBefore">
        <a-button type="primary">上传附件</a-button>
      </a-upload>
    </div>
    <a-list :data-source="items" :locale="{ emptyText: '暂无附件' }" size="small">
      <template #renderItem="{ item }">
        <a-list-item>
          <a-space>
            <a :href="item.url" target="_blank">{{ item.name }}</a>
            <a-tag v-if="item.category">{{ item.category }}</a-tag>
            <span style="color:#999">{{ formatTime(item.time) }}</span>
          </a-space>
          <template #actions>
            <a v-if="editable" @click="remove(item)">删除</a>
          </template>
        </a-list-item>
      </template>
    </a-list>
  </a-card>
  
</template>

<script lang="ts" setup>
import { ref, watch, onMounted, computed } from 'vue';
import { message } from 'ant-design-vue';
import { defHttp } from '/@/utils/http/axios';
import { workflowAttachmentApi } from '/@/api/workflow';

interface AttachmentItem {
  id: string;
  name: string;
  url: string;
  time?: string | number;
  category?: string;
  taskId?: string;
}

const props = defineProps<{
  processInstanceId: string;
  currentTaskId?: string;
  editable?: boolean;
  title?: string;
  groupKey?: string;
  latestTaskId?: string;
}>();

const editable = computed(() => !!props.editable);
const title = computed(() => props.title || '相关附件');

const items = ref<AttachmentItem[]>([]);

watch(() => props.processInstanceId, () => load(), { immediate: true });

onMounted(() => {
  if (props.processInstanceId) load();
});

async function load() {
  if (!props.processInstanceId) return;
  const list = await workflowAttachmentApi.list({ processInstanceId: props.processInstanceId });
  items.value = (list || [])
    .filter((a: any) => !props.groupKey || parseCategory(a.description) === props.groupKey)
    .filter((a: any) => !props.latestTaskId || a.taskId === props.latestTaskId)
    .map((a: any) => ({
      id: a.id,
      name: a.name,
      url: a.url,
      time: a.time,
      category: parseCategory(a.description),
      taskId: a.taskId,
    }));
}

function parseCategory(desc?: string) {
  try {
    if (!desc) return undefined;
    const obj = JSON.parse(desc);
    return obj.category;
  } catch {
    return undefined;
  }
}

function formatTime(t: any) {
  if (!t) return '';
  try {
    const d = new Date(t);
    if (isNaN(d.getTime())) return String(t);
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`;
  } catch {
    return String(t);
  }
}

async function handleUploadBefore(file: File) {
  try {
    // 1) 先上传文件到后端通用上传接口，拿到路径
    const res: any = await defHttp.uploadFile({ url: '/sys/common/upload' }, { file, name: 'file' } as any);
    // Jeecg 通常返回 { message: dbpath, success: true }
    const dbpath = res?.message || res?.result || '';
    if (!dbpath) throw new Error('上传失败');
    const url = `/sys/common/static/${dbpath}`;

    // 2) 登记为流程附件
    await workflowAttachmentApi.add({
      taskId: props.currentTaskId,
      processInstanceId: props.processInstanceId,
      name: file.name,
      description: JSON.stringify({ category: props.groupKey || 'default' }),
      url,
    });

    message.success('上传成功');
    await load();
  } catch (e: any) {
    message.error(e?.message || '上传失败');
  }
  // 阻止 a-upload 默认上送
  return false;
}

async function remove(item: AttachmentItem) {
  try {
    await workflowAttachmentApi.remove(item.id);
    message.success('删除成功');
    await load();
  } catch (e: any) {
    message.error(e?.message || '删除失败');
  }
}

</script>

<style scoped>
</style>


