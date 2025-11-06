<template>
  <div class="bpmn-viewer-wrapper">
    <div class="bpmn-toolbar">
      <button class="btn" @click="onFit">适配</button>
      <button class="btn" @click="onZoomIn">＋</button>
      <button class="btn" @click="onZoomOut">－</button>
      <button class="btn" @click="onReset">重置</button>
    </div>
    <div ref="containerRef" class="bpmn-container"></div>
  </div>
  
</template>

<script lang="ts" setup>
import { onMounted, onBeforeUnmount, ref, watch, nextTick } from 'vue';
import { defHttp } from '/@/utils/http/axios';

interface MetaData {
  activeIds?: string[];
  completedIds?: string[];
  executedFlows?: Array<string | { id: string; status?: string }>;
  nodes?: any[];
  tasks?: any[];
}

const props = defineProps<{ instanceId: string | undefined; meta?: MetaData }>();

const containerRef = ref<HTMLDivElement | null>(null);
let viewer: any = null;
let resizeObs: ResizeObserver | null = null;

const emit = defineEmits<{ (e: 'titleChange', title: string): void }>();

async function loadBpmnJs(): Promise<any> {
  // 尝试本地依赖（若用户安装了 bpmn-js）
  try {
    // @ts-ignore
    const mod = await import(/* @vite-ignore */ 'bpmn-js/dist/bpmn-navigated-viewer.production.min.js');
    return mod.default || mod; // NavigatedViewer
  } catch (_) {
    // 退化到CDN脚本（运行时加载，不影响首屏）
    if ((window as any).BpmnJSNavigatedViewer) return (window as any).BpmnJSNavigatedViewer;
    await new Promise<void>((resolve, reject) => {
      const s = document.createElement('script');
      s.src = 'https://unpkg.com/bpmn-js@11.5.0/dist/bpmn-navigated-viewer.production.min.js';
      s.onload = () => {
        (window as any).BpmnJSNavigatedViewer = (window as any).NavigatedViewer;
        resolve();
      };
      s.onerror = () => reject(new Error('load bpmn-js failed'));
      document.head.appendChild(s);
    });
    return (window as any).BpmnJSNavigatedViewer;
  }
}

async function fetchXml(): Promise<string> {
  if (!props.instanceId) return '';
  // 显式传第二参，避免 transformRequestHook 读取 undefined 触发 Reflect.has 报错
  const resp: any = await defHttp.get(
    { url: `/workflow/instance/${props.instanceId}/bpmn.xml`, headers: { Accept: 'application/xml' } },
    { isTransformResponse: false }
  );
  // 可能直接是字符串，或 axios 的 data 字段
  if (typeof resp === 'string') return resp as string;
  if (resp && typeof resp.data === 'string') return resp.data;
  return (resp?.result || resp || '') as string;
}

function fit() {
  if (!viewer) return;
  const canvas = viewer.get('canvas');
  try { canvas.zoom('fit-viewport', 'auto'); } catch {}
}

function onFit() { try { viewer?.get('canvas')?.zoom('fit-viewport'); } catch {} }
function onReset() { try { viewer?.get('canvas')?.zoom('fit-viewport'); } catch {} }
function onZoomIn() {
  try {
    const canvas = viewer.get('canvas');
    const cur = canvas.zoom();
    const center = { x: (containerRef.value?.clientWidth || 0) / 2, y: (containerRef.value?.clientHeight || 0) / 2 };
    canvas.zoom(Math.min(4, (cur || 1) + 0.2), center);
  } catch {}
}
function onZoomOut() {
  try {
    const canvas = viewer.get('canvas');
    const cur = canvas.zoom();
    const center = { x: (containerRef.value?.clientWidth || 0) / 2, y: (containerRef.value?.clientHeight || 0) / 2 };
    canvas.zoom(Math.max(0.2, (cur || 1) - 0.2), center);
  } catch {}
}

function addMarkersAndOverlays() {
  if (!viewer || !props.meta) return;
  const { activeIds = [], completedIds = [], executedFlows = [], tasks = [] } = props.meta;
  const canvas = viewer.get('canvas');
  const overlays = viewer.get('overlays');
  const elementRegistry = viewer.get('elementRegistry');

  // 清理旧 overlays/markers
  try { overlays.clear(); } catch {}
  try { canvas._eventBus && canvas._eventBus.fire('elements.changed', { elements: [] }); } catch {}

  // 建立 DI-id -> element-id 的映射，兼容后端返回的是 BPMNDiagram 的 edge/shape id
  const diToEl = new Map<string, string>();
  (elementRegistry.getAll() || []).forEach((el: any) => {
    const diId = el?.di?.id;
    if (diId) diToEl.set(String(diId), String(el.id));
  });

  const normalizeId = (id: string) => diToEl.get(id) || id;

  const done = new Set((completedIds || []).map((id) => normalizeId(id)));
  const active = new Set((activeIds || []).map((id) => normalizeId(id)));

  // 节点标记
  [...done].forEach((id) => canvas.addMarker(id, 'highlight-completed'));
  [...active].forEach((id) => canvas.addMarker(id, 'highlight-current'));

  // 执行过的连线（若后端提供对象，status=reject/approve）
  const flows: Array<{ id: string; status?: string }> = executedFlows.map((f: any) =>
    typeof f === 'string' ? { id: normalizeId(f) } : { id: normalizeId(f.id), status: f.status },
  );
  flows.forEach((f) => {
    const cls = f.status === 'reject' ? 'highlight-flow-reject' : 'highlight-flow-approve';
    canvas.addMarker(f.id, cls);
  });

  // 构造节点->任务列表映射
  const nodeId2Tasks = new Map<string, any[]>();
  tasks.forEach((t: any) => {
    const k = t.nodeId || t.taskDefinitionKey;
    if (!k) return;
    const arr = nodeId2Tasks.get(k) || [];
    arr.push(t);
    nodeId2Tasks.set(k, arr);
  });

  // 覆盖层（title原生提示，兼容端上WebView；更高级可以集成 Tooltip 库）
  const buildTitle = (id: string, name?: string) => {
    const arr = nodeId2Tasks.get(id) || [];
    const lines: string[] = [];
    lines.push(`节点：${name || id}`);
    arr.forEach((t: any) => {
      const durMin = t.duration ? Math.round(t.duration / 60000) : 0;
      lines.push(`处理人：${t.assignee || '-'} 开始：${fmt(t.startTime)} 结束：${fmt(t.endTime)} 耗时：${durMin}分`);
      if (Array.isArray(t.comments)) {
        t.comments.forEach((c: any) => lines.push(`意见-${c.user || '-'}：${c.message || ''}`));
      }
    });
    return lines.join('\n');
  };

  const buildTooltipDiv = (id: string, name?: string) => {
    const container = document.createElement('div');
    container.className = 'bpmn-node-tooltip';
    container.style.display = 'none';
    const arr = (nodeId2Tasks.get(id) || []).slice();
    arr.sort((a: any, b: any) => (b?.startTime || 0) - (a?.startTime || 0));
    const t = arr[0] || {};
    const durMin = t?.duration ? Math.round(t.duration / 60000) : 0;
    const comments = Array.isArray(t?.comments) ? t.comments : [];
    const commentHtml = comments.map((c: any) => `<div class=\"cmt\">- ${c.user || '-'}：${c.message || ''}</div>`).join('');
    container.innerHTML = `
      <div class=\"row\"><span class=\"lbl\">节点：</span><span class=\"val\">${name || id}</span></div>
      <div class=\"row\"><span class=\"lbl\">处理人：</span><span class=\"val\">${t?.assignee || '-'}</span></div>
      <div class=\"row\"><span class=\"lbl\">开始：</span><span class=\"val\">${fmt(t?.startTime)}</span></div>
      <div class=\"row\"><span class=\"lbl\">结束：</span><span class=\"val\">${fmt(t?.endTime)}</span></div>
      <div class=\"row\"><span class=\"lbl\">耗时：</span><span class=\"val\">${durMin} 分钟</span></div>
      ${commentHtml ? `<div class=\"row\"><span class=\"lbl\">意见：</span></div>${commentHtml}` : ''}
    `;
    return container;
  };

  // 遍历渲染器里的图形元素，确保与XML一致
  const elements = elementRegistry.getAll() || [];
  elements.forEach((el: any) => {
    const bo = el && el.businessObject;
    if (!bo) return;
    const type = bo.$type as string;
    // 只对常见可办理节点添加悬浮（开始/结束/网关忽略）
    if (type && type.startsWith('bpmn:') && /Task|StartEvent|EndEvent|SubProcess|CallActivity/i.test(type)) {
      const id = bo.id as string;
      const name = bo.name as string;
      const div = document.createElement('div');
      div.className = 'overlay-hitarea';
      // 允许捕获鼠标事件（仅用自定义浮层，取消原生 title，避免出现两种提示样式）
      div.style.pointerEvents = 'auto';
      // 构造 tooltip 并默认隐藏
      const tip = buildTooltipDiv(id, name);
      // 悬浮显示/隐藏
      div.addEventListener('mouseenter', () => { tip.style.display = 'block'; });
      div.addEventListener('mouseleave', () => { tip.style.display = 'none'; });
      // 关键：显式设置覆盖层尺寸为图形尺寸，否则 100% 无法生效
      const w = (el as any).width || 0;
      const h = (el as any).height || 0;
      if (w && h) {
        div.style.width = `${w}px`;
        div.style.height = `${h}px`;
      }
      try {
        overlays.add(id, { position: { top: 0, left: 0 }, html: div });
        // tooltip 叠加在节点上方
        overlays.add(id, { position: { top: -((el as any).height || 0) - 10, left: 0 }, html: tip });
      } catch {}
    }
  });
}

function fmt(ts?: number) {
  if (!ts) return '-';
  const d = new Date(ts);
  const p = (n: number) => String(n).padStart(2, '0');
  return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())} ${p(d.getHours())}:${p(d.getMinutes())}`;
}

async function render() {
  if (!containerRef.value || !props.instanceId) return;
  const NavigatedViewer = await loadBpmnJs();
  if (!viewer) {
    viewer = new NavigatedViewer({ container: containerRef.value });
  }
  const xml = await fetchXml();
  await viewer.importXML(xml);
  fit();
  addMarkersAndOverlays();
  try {
    const root = viewer.get('canvas')?.getRootElement?.();
    const title = root?.businessObject?.name || root?.id || '';
    emit('titleChange', String(title || ''));
  } catch {}
}

onMounted(async () => {
  await render();
  // 自适应
  if (containerRef.value && 'ResizeObserver' in window) {
    resizeObs = new ResizeObserver(() => fit());
    resizeObs.observe(containerRef.value);
  }
});

onBeforeUnmount(() => {
  if (resizeObs && containerRef.value) resizeObs.unobserve(containerRef.value);
  resizeObs = null;
  try { viewer && viewer.destroy && viewer.destroy(); } catch {}
  viewer = null;
});

watch(() => props.instanceId, async () => {
  await nextTick();
  await render();
});

watch(() => props.meta, async () => {
  await nextTick();
  addMarkersAndOverlays();
});
</script>

<style scoped>
.bpmn-viewer-wrapper { width: 100%; height: 100%; }
.bpmn-container { width: 100%; height: 72vh; min-height: 72vh; background: #fff; position: relative; }
.bpmn-toolbar { display: flex; gap: 8px; padding: 4px 0 8px; }
.bpmn-toolbar .btn { padding: 2px 8px; border: 1px solid #d9d9d9; background: #fafafa; cursor: pointer; }
/* 隐藏 bpmn 内置角标与面包屑 */
:deep(.bjs-powered-by) { display: none !important; }
:deep(.bjs-breadcrumbs) { display: none !important; }
/* 节点高亮 */
:deep(.highlight-current .djs-visual > :not(text)) { stroke: #ff4d4f !important; stroke-width: 3 !important; }
:deep(.highlight-completed .djs-visual > :not(text)) { stroke: #52c41a !important; stroke-width: 3 !important; }
/* 连线高亮 */
:deep(.highlight-flow-approve .djs-visual > path) { stroke: #52c41a !important; stroke-width: 3 !important; }
:deep(.highlight-flow-reject .djs-visual > path) { stroke: #ff4d4f !important; stroke-width: 3 !important; }
/* 透明可点的覆盖层，用于title提示 */
.overlay-hitarea { width: 100%; height: 100%; background: rgba(0,0,0,0); }
/* 自定义 tooltip */
.bpmn-node-tooltip {
  background: rgba(0,0,0,0.75);
  color: #fff;
  padding: 8px 10px;
  border-radius: 4px;
  font-size: 12px;
  /* 加最大宽度，超出自动换行 */
  max-width: 420px;
  white-space: normal;
  word-break: break-all;
}
.bpmn-node-tooltip .row { margin-bottom: 4px; line-height: 1.6; white-space: normal; }
.bpmn-node-tooltip .cmt { font-size: 12px; padding-left: 8px; }
.bpmn-node-tooltip .lbl { display: inline-block; color: #c2c2c2; margin-right: 6px; }
.bpmn-node-tooltip .val { display: inline-block; }
</style>


