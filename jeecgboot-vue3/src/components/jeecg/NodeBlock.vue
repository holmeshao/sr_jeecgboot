<template>
  <div class="node-block">
    <template v-for="comp in orderedComponents" :key="comp.key">
      <a-form-item :label="comp.label" v-if="!isHidden(comp)" :required="isRequired(comp)">
        <!-- 文件分组由附件组件承载 -->
        <template v-if="comp.type === 'file'">
          <ProcessAttachments
            :processInstanceId="processInstanceId"
            :currentTaskId="currentTaskId"
            :editable="editable && !isReadonly(comp)"
            :groupKey="comp.key"
            :latestTaskId="latestTaskId"
            :title="comp.label"
          />
        </template>
        <template v-else-if="comp.type === 'select'">
          <a-select v-model:value="model[comp.key]" :disabled="isReadonly(comp)" v-bind="comp.props || {}">
            <a-select-option v-for="opt in (comp.props?.options || [])" :key="opt.value" :value="opt.value">{{ opt.label }}</a-select-option>
          </a-select>
        </template>
        <template v-else-if="comp.type === 'radio'">
          <a-radio-group v-model:value="model[comp.key]" :disabled="isReadonly(comp)" v-bind="comp.props || {}">
            <a-radio v-for="opt in (comp.props?.options || [])" :key="opt.value" :value="opt.value">{{ opt.label }}</a-radio>
          </a-radio-group>
        </template>
        <template v-else-if="comp.type === 'checkbox'">
          <a-checkbox-group v-model:value="model[comp.key]" :disabled="isReadonly(comp)" :options="comp.props?.options || []" />
        </template>
        <template v-else-if="comp.type === 'date'">
          <a-date-picker v-model:value="model[comp.key]" :disabled="isReadonly(comp)" v-bind="comp.props || {}" />
        </template>
        <template v-else-if="comp.type === 'datetime'">
          <a-date-picker v-model:value="model[comp.key]" :show-time="true" :disabled="isReadonly(comp)" v-bind="comp.props || {}" />
        </template>
        <template v-else-if="comp.type === 'number'">
          <a-input-number v-model:value="model[comp.key]" :disabled="isReadonly(comp)" v-bind="comp.props || {}" />
        </template>
        <template v-else-if="comp.type === 'textarea'">
          <a-textarea v-model:value="model[comp.key]" :disabled="isReadonly(comp)" v-bind="comp.props || {}" />
        </template>
        <template v-else>
          <a-input v-model:value="model[comp.key]" :disabled="isReadonly(comp)" v-bind="comp.props || {}" />
        </template>
      </a-form-item>
    </template>
  </div>
</template>

<script lang="ts" setup>
import { computed, toRefs, ref, watch, onMounted } from 'vue';
import { defHttp } from '/@/utils/http/axios';
import ProcessAttachments from './ProcessAttachments.vue';

interface ComponentSchema {
  key: string;
  type: string;
  label?: string;
  required?: boolean;
  readonly?: boolean;
  hidden?: boolean;
  order?: number;
  props?: Record<string, any>;
}

const props = defineProps<{
  components: ComponentSchema[];
  permissions?: { editableFields?: string[]; readonlyFields?: string[]; hiddenFields?: string[]; requiredFields?: string[] };
  model: Record<string, any>;
  editable?: boolean;
  processInstanceId?: string;
  currentTaskId?: string;
  latestTaskId?: string;
}>();

const { components, permissions } = toRefs(props);

const localComponents = ref<ComponentSchema[]>([]);

watch(
  () => components?.value,
  (list) => {
    localComponents.value = (list || []).map((c) => ({ ...c, props: { ...(c.props || {}) } }));
    preloadOptions();
  },
  { immediate: true }
);

const orderedComponents = computed(() =>
  (localComponents.value || []).slice().sort((a, b) => (a.order || 0) - (b.order || 0))
);

function inList(list: string[] | undefined, key: string) {
  return !!list && list.includes(key);
}

function isHidden(comp: ComponentSchema) {
  if (comp.hidden) return true;
  return inList(permissions?.value?.hiddenFields, comp.key);
}

function isReadonly(comp: ComponentSchema) {
  if (comp.readonly) return true;
  if (inList(permissions?.value?.readonlyFields, comp.key)) return true;
  // 非editableFields 默认只读（当 editable=false 时）
  if (props.editable === false && !inList(permissions?.value?.editableFields, comp.key)) return true;
  return false;
}

function isRequired(comp: ComponentSchema) {
  if (comp.required) return true;
  return inList(permissions?.value?.requiredFields, comp.key);
}

function resolveComponent(comp: ComponentSchema) {
  switch (comp.type) {
    case 'input':
      return 'a-input';
    case 'textarea':
      return 'a-textarea';
    case 'number':
      return 'a-input-number';
    case 'select':
      return 'a-select';
    case 'date':
      return 'a-date-picker';
    case 'datetime':
      return 'a-date-picker';
    case 'radio':
      return 'a-radio-group';
    case 'checkbox':
      return 'a-checkbox-group';
    case 'file':
      // 文件由 ProcessAttachments 渲染
      return 'span';
    default:
      return 'a-input';
  }
}

async function preloadOptions() {
  for (const comp of localComponents.value) {
    if (!comp?.props) continue;
    // 若有字典编码且未提供静态options，则尝试从后端加载
    const dictCode = comp.props.dictCode;
    if (dictCode && (!Array.isArray(comp.props.options) || comp.props.options.length === 0)) {
      try {
        // 兼容常见Jeecg字典接口：/sys/dict/getDictItems/{code}
        const data: any = await defHttp.get({ url: `/sys/dict/getDictItems/${dictCode}` });
        if (Array.isArray(data)) {
          comp.props.options = data.map((d: any) => ({ label: d.text || d.title || d.label, value: d.value || d.code || d.key }));
        }
      } catch (e) {
        // 忽略加载失败
      }
    }
  }
}

onMounted(preloadOptions);
</script>

<style scoped>
.node-block {
  width: 100%;
}
</style>


