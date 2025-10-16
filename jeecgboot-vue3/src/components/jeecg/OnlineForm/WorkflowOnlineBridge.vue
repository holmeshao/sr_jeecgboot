<template>
  <div class="workflow-online-bridge">
    <!-- 优先选择运行时：
         - 有 taskId 或有 dataId（办理/编辑）时优先 ProcessOnlineForm（自带流程按钮）
         - 纯新增（无 taskId 且无 dataId）用 OnlineForm（按 headId 渲染） -->
    <component
      v-if="runtimeComp && !runtimeIsLocal && table && loadedPath.includes('ProcessOnlineForm-')"
      :is="runtimeComp"
      :tableName="table"
      :dataId="dataId"
      :taskId="taskId"
      ref="runtimeRef"
      @rendered="onLoaded"
      @success="onSubmitted"
    />
    <component
      v-else-if="runtimeComp && !runtimeIsLocal && table"
      :is="runtimeComp"
      :id="resolvedFormId || undefined"
      ref="runtimeRef"
      @rendered="onLoaded"
      @success="onSubmitted"
    />
    <!-- 回退：使用本地轻渲染，按本地 props 传 table/dataId -->
    <LocalForm
      v-else-if="runtimeIsLocal && table"
      :table="table"
      :dataId="dataId"
      ref="runtimeRef"
      @loaded="onLoaded"
    />
  </div>
</template>

<script lang="ts" setup>
import { ref, watch, onMounted, defineExpose, markRaw, nextTick } from 'vue';
import { defHttp } from '/@/utils/http/axios';

interface Props {
  table: string;      // online 表名（tableName）
  dataId?: string;    // 数据ID
  taskId?: string;    // 任务ID
  permissions?: any;  // 工作流字段权限（供后续适配器使用）
}

const props = defineProps<Props>();
function onLoaded() {
  // v1 权限适配：若运行时实例暴露 setFieldState 之类 API，则按工作流权限动态设置
  try {
    const inst: any = runtimeRef.value;
    const perms = props.permissions || {};
    const editable: string[] = perms.editableFields || perms.editable || [];
    const readonly: string[] = perms.readonlyFields || perms.readonly || [];
    const hidden: string[] = perms.hiddenFields || perms.hidden || [];
    const required: string[] = perms.requiredFields || perms.required || [];

    const apply = (type: 'disable'|'hide'|'required'|'enable', keys: string[]) => {
      if (!Array.isArray(keys) || keys.length === 0) return;
      if (inst?.setFieldState) {
        keys.forEach((k: string) => inst.setFieldState(k, type));
      } else if (inst?.formRef?.setFields) {
        // 降级：仅对必填/只读做基础处理
        if (type === 'required') {
          inst.formRef.setFields([{ name: k, rules: [{ required: true, message: '必填' }] }]);
        }
      }
    };

    // 只读优先于可编辑
    apply('hide', hidden);
    apply('required', required);
    apply('disable', readonly);
    // 可编辑最后（解除只读）
    if (Array.isArray(editable) && editable.length && inst?.setFieldState) {
      editable.forEach((k: string) => inst.setFieldState(k, 'enable'));
    }
  } catch {}
}

const runtimeComp = ref<any>(null);
const runtimeIsLocal = ref<boolean>(false);
const runtimeRef = ref<any>(null);
const resolvedFormId = ref<string>('');
const loadedPath = ref<string>('');
const resolvedFormConfig = ref<any>(null);
const lastSubmittedData = ref<any>(null);
const pendingResolvers = ref<Array<(v:any)=>void>>([]);

import LocalForm from '/@/components/jeecg/OnlineForm/WorkflowOnlineForm.vue';

async function loadRuntime() {
  // 尝试加载 vendor 里的 Online 运行时组件（不同版本文件名不同），使用 vite-ignore 避免预解析报错
  const candidates = (props.taskId || props.dataId)
    ? ['/vendor/online/ProcessOnlineForm-98ede18d.mjs', '/vendor/online/OnlineForm-58282699.mjs']
    : ['/vendor/online/OnlineForm-58282699.mjs', '/vendor/online/ProcessOnlineForm-98ede18d.mjs'];
  for (const p of candidates) {
    try {
      const mod: any = await import(/* @vite-ignore */ p);
      const comp = mod?.default || mod?.ProcessOnlineForm || mod?.OnlineForm;
      if (comp) {
        runtimeComp.value = markRaw(comp);
        console.debug('[WorkflowOnlineBridge] runtime loaded =', p);
        loadedPath.value = p;
        if (!props.table) {
          console.warn('[WorkflowOnlineBridge] 缺少 table 参数，运行时将无法加载。请检查路由或父组件传参。');
        } else {
          console.log('[WorkflowOnlineBridge] 传参映射 -> tableName:', props.table, ' dataId:', props.dataId);
          // 若后续退化为 OnlineForm，提前解析出表单ID
          try {
            const url = `/online/cgform/api/getFormItemBytbname/${props.table}`;
            const resp: any = await defHttp.get({ url });
            const real = resp?.result || resp;
            const headId = real?.head?.id || '';
            resolvedFormId.value = headId;
            resolvedFormConfig.value = real;
            console.log('[WorkflowOnlineBridge] 解析到 formId =', headId);
          } catch (e) {
            resolvedFormId.value = '';
            resolvedFormConfig.value = null;
          }
        }
        return;
      }
    } catch { /* ignore and try next */ }
  }
  // 最后兜底：使用本地轻渲染（功能完整但样式较简）
  runtimeComp.value = markRaw(LocalForm);
  runtimeIsLocal.value = true;
}

onMounted(loadRuntime);

// 当加载的是 OnlineForm 时，需要手动注入配置并打开表单
watch([runtimeComp, () => resolvedFormId.value, () => loadedPath.value], async () => {
  if (!runtimeComp.value || !props.table) return;
  if (!loadedPath.value.includes('OnlineForm-')) return; // 仅当加载 OnlineForm 才需要手动初始化
  if (!resolvedFormId.value || !resolvedFormConfig.value) return;
  await nextTick();
  const inst: any = runtimeRef.value;
  if (inst && typeof inst.createRootProperties === 'function') {
    try {
      await inst.createRootProperties(resolvedFormConfig.value);
      // 新增或编辑：无 dataId 则走新增，避免触发“待编辑数据不存在”
      const isUpdate = !!props.dataId;
      if (typeof inst.show === 'function') {
        if (isUpdate) inst.show(true, { id: props.dataId });
        else inst.show(false, undefined);
      }
      console.log('[WorkflowOnlineBridge] OnlineForm 初始化完成，isUpdate =', isUpdate);
    } catch (e) {
      console.warn('[WorkflowOnlineBridge] 初始化 OnlineForm 失败:', e);
    }
  }
});

// 当加载的是 ProcessOnlineForm 且为新增（无 dataId）时，强制切换到底层 OnlineForm 的新增模式
watch([runtimeComp, () => loadedPath.value], async () => {
  if (!runtimeComp.value) return;
  if (!loadedPath.value.includes('ProcessOnlineForm-')) return;
  if (props.dataId) return; // 编辑态无需干预
  await nextTick();
  const inst: any = runtimeRef.value;
  try {
    if (inst?.onlineFormCompRef?.show) {
      inst.onlineFormCompRef.show(false, undefined);
      console.log('[WorkflowOnlineBridge] ProcessOnlineForm -> 强制切换为新增模式');
    }
  } catch {}
});

watch(() => props.table, () => {
  // online 运行时会自行响应 code/id 变化，这里不做额外处理
  // 但我们在 table 变化时也尝试解析一次 formId，确保 OnlineForm 兜底可用
  if (props.table) {
    defHttp.get({ url: `/online/cgform/api/getFormItemBytbname/${props.table}` }).then((resp: any) => {
      const real = resp?.result || resp;
      resolvedFormId.value = real?.head?.id || '';
      resolvedFormConfig.value = real || null;
    }).catch(() => {
      resolvedFormId.value = '';
      resolvedFormConfig.value = null;
    });
  }
});

// 对外暴露 API，融合页可调用
async function getData() {
  const inst: any = runtimeRef.value;
  // 优先运行时提供的统一方法
  if (inst?.getValue) {
    try { return await inst.getValue(); } catch {}
  }
  if (inst?.getData) {
    try { return await inst.getData(); } catch {}
  }
  if (inst?.getFieldsValue) {
    try {
      const v = await inst.getFieldsValue();
      if (v && Object.keys(v).length) return v;
    } catch {}
  }
  // 尝试内部 onlineFormCompRef 的取值
  const inner: any = inst?.onlineFormCompRef;
  if (inner?.getValue) {
    try { return await inner.getValue(); } catch {}
  }
  if (inner?.getData) {
    try { return await inner.getData(); } catch {}
  }
  if (inner?.getFieldsValue) {
    try {
      const v = await inner.getFieldsValue();
      if (v && Object.keys(v).length) return v;
    } catch {}
  }
  if (inner?.formRef?.getFieldsValue) {
    try {
      const v = inner.formRef.getFieldsValue(true as any);
      if (v && Object.keys(v).length) return v;
    } catch {}
  }
  // 其次尝试底层表单实例（BasicForm）的取值
  if (inst?.formRef?.getFieldsValue) {
    try {
      // true: 获取包含未渲染的初始值（不同版本实现不同，做向后兼容）
      const v = inst.formRef.getFieldsValue(true as any);
      if (v && Object.keys(v).length) return v;
    } catch {}
  }
  // 兜底：直接读取运行时保留的数据模型
  try {
    const raw = inner?.formData || inner?.dbData || inst?.formData || inst?.dbData || {};
    // 深拷贝，避免外部修改影响内部状态
    return JSON.parse(JSON.stringify(raw));
  } catch {
    return {};
  }
}

async function validate() {
  const inst: any = runtimeRef.value;
  if (inst?.validate) return await inst.validate();
  if (inst?.formRef?.validate) return await inst.formRef.validate();
  if (inst?.onlineFormCompRef?.validate) return await inst.onlineFormCompRef.validate();
  if (inst?.onlineFormCompRef?.formRef?.validate) return await inst.onlineFormCompRef.formRef.validate();
  // 兼容部分版本：校验方法名不同
  if (inst?.formRef?.validateFields) return await inst.formRef.validateFields();
  if (inst?.onlineFormCompRef?.formRef?.validateFields) return await inst.onlineFormCompRef.formRef.validateFields();
  return true;
}

async function save() {
  const inst: any = runtimeRef.value;
  if (inst?.save) return await inst.save();
  if (inst?.handleSave) return await inst.handleSave();
  return null;
}

async function submit() {
  const inst: any = runtimeRef.value;
  if (inst?.submit) return await inst.submit();
  if (inst?.handleSubmit) return await inst.handleSubmit();
  return null;
}

// expose moved to the end with getCurrentId

// 追加：获取当前记录ID（适配 OnlineForm 暴露的 dbData/formData）
function getCurrentId(): string | undefined {
  try {
    const inst: any = runtimeRef.value;
    const inner: any = inst?.onlineFormCompRef;
    return inner?.dbData?.id || inner?.formData?.id || inst?.dbData?.id || inst?.formData?.id || undefined;
  } catch { return undefined; }
}
function onSubmitted(payload: any) {
  try {
    lastSubmittedData.value = payload;
    // 唤醒一次性等待者
    if (pendingResolvers.value.length) {
      const resolvers = [...pendingResolvers.value];
      pendingResolvers.value.length = 0;
      resolvers.forEach((fn) => {
        try { fn(payload); } catch {}
      });
    }
  } catch {}
}

async function submitWithResult(timeoutMs: number = 8000): Promise<any> {
  return new Promise(async (resolve, reject) => {
    let done = false;
    const timer = setTimeout(() => {
      if (!done) {
        // 超时兜底：返回已知的值（可能为空），避免悬挂
        done = true;
        resolve(lastSubmittedData.value || {});
      }
    }, timeoutMs);
    pendingResolvers.value.push((v: any) => {
      if (done) return; done = true; clearTimeout(timer); resolve(v);
    });
    try {
      await submit();
    } catch (e) {
      if (done) return; done = true; clearTimeout(timer); reject(e);
    }
  });
}

defineExpose({ getData, validate, save, submit, submitWithResult, getCurrentId });
</script>

<style scoped>
.workflow-online-bridge { width: 100%; }
</style>


