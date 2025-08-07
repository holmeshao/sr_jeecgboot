<template>
  <div>
    <!-- 统计卡片 -->
    <a-row :gutter="16" class="mb-6">
      <a-col :span="6">
        <a-card>
          <Statistic
            title="总流程实例"
            :value="stats.totalInstances"
            :value-style="{ color: '#3f8600' }"
          >
            <template #prefix>
              <Icon icon="ant-design:play-circle-outlined" />
            </template>
          </Statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card>
          <Statistic
            title="运行中实例"
            :value="stats.runningInstances"
            :value-style="{ color: '#1890ff' }"
          >
            <template #prefix>
              <Icon icon="ant-design:sync-outlined" />
            </template>
          </Statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card>
          <Statistic
            title="我的待办"
            :value="stats.myPendingTasks"
            :value-style="{ color: '#faad14' }"
          >
            <template #prefix>
              <Icon icon="ant-design:clock-circle-outlined" />
            </template>
          </Statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card>
          <Statistic
            title="已完成实例"
            :value="stats.completedInstances"
            :value-style="{ color: '#52c41a' }"
          >
            <template #prefix>
              <Icon icon="ant-design:check-circle-outlined" />
            </template>
          </Statistic>
        </a-card>
      </a-col>
    </a-row>

    <!-- 图表区域 -->
    <a-row :gutter="16" class="mb-6">
      <a-col :span="12">
        <a-card title="流程实例趋势" :bordered="false">
          <div ref="instanceChartRef" style="height: 300px;"></div>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="任务分布" :bordered="false">
          <div ref="taskChartRef" style="height: 300px;"></div>
        </a-card>
      </a-col>
    </a-row>

    <!-- 实时监控 -->
    <a-card title="实时监控" :bordered="false">
      <a-tabs v-model:activeKey="activeTab">
        <a-tab-pane key="instances" tab="流程实例">
          <BasicTable @register="registerInstanceTable" />
        </a-tab-pane>
        <a-tab-pane key="tasks" tab="任务监控">
          <BasicTable @register="registerTaskTable" />
        </a-tab-pane>
        <a-tab-pane key="performance" tab="性能监控">
          <a-row :gutter="16">
            <a-col :span="8">
              <a-card title="平均处理时间" size="small">
                <Statistic
                  :value="performance.avgProcessTime"
                  suffix="分钟"
                  :precision="1"
                />
              </a-card>
            </a-col>
            <a-col :span="8">
              <a-card title="平均任务时间" size="small">
                <Statistic
                  :value="performance.avgTaskTime"
                  suffix="分钟"
                  :precision="1"
                />
              </a-card>
            </a-col>
            <a-col :span="8">
              <a-card title="吞吐量" size="small">
                <Statistic
                  :value="performance.throughput"
                  suffix="实例/天"
                  :precision="0"
                />
              </a-card>
            </a-col>
          </a-row>
        </a-tab-pane>
      </a-tabs>
    </a-card>

    <!-- 流程图查看弹窗 -->
    <BasicModal
      v-bind="$attrs"
      @register="registerDiagramModal"
      title="流程图"
      width="90%"
      :footer="null"
    >
      <div v-if="currentInstance" style="height: 600px;">
        <div ref="diagramRef" style="width: 100%; height: 100%;"></div>
      </div>
    </BasicModal>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { Statistic } from 'ant-design-vue';
import { BasicTable, useTable } from '/@/components/Table';
import { BasicModal, useModal } from '/@/components/Modal';
import { useI18n } from '/@/hooks/web/useI18n';
import { workflowStatsApi, workflowInstanceApi } from '/@/api/workflow';
import { formatToDateTime } from '/@/utils/dateUtil';
import * as echarts from 'echarts';

const { t } = useI18n();

const activeTab = ref('instances');
const stats = ref({
  totalInstances: 0,
  runningInstances: 0,
  completedInstances: 0,
  myPendingTasks: 0,
});
const performance = ref({
  avgProcessTime: 0,
  avgTaskTime: 0,
  throughput: 0,
});
const currentInstance = ref<any>(null);

// 图表引用
const instanceChartRef = ref<HTMLElement>();
const taskChartRef = ref<HTMLElement>();
const diagramRef = ref<HTMLElement>();
let instanceChart: echarts.ECharts | null = null;
let taskChart: echarts.ECharts | null = null;

// 流程实例表格
const [registerInstanceTable] = useTable({
  title: '流程实例监控',
  api: workflowInstanceApi.getList,
  columns: [
    {
      title: t('routes.workflow.instanceId'),
      dataIndex: 'id',
      width: 200,
    },
    {
      title: t('routes.workflow.instanceName'),
      dataIndex: 'name',
      width: 150,
    },
    {
      title: t('routes.workflow.instanceStatus'),
      dataIndex: 'status',
      width: 100,
      customRender: ({ text }) => {
        const color = text === 'running' ? 'green' : text === 'completed' ? 'blue' : 'orange';
        return `<a-tag color="${color}">${text === 'running' ? '运行中' : text === 'completed' ? '已完成' : '已终止'}</a-tag>`;
      },
    },
    {
      title: t('routes.workflow.instanceStartTime'),
      dataIndex: 'startTime',
      width: 180,
      customRender: ({ text }) => formatToDateTime(text),
    },
    {
      title: t('routes.workflow.instanceDuration'),
      dataIndex: 'duration',
      width: 120,
      customRender: ({ text }) => formatDuration(text),
    },
    {
      title: t('routes.workflow.instanceStartUser'),
      dataIndex: 'startUser',
      width: 120,
    },
    {
      title: '操作',
      dataIndex: 'action',
      width: 120,
      customRender: ({ record }) => {
        return `<a-button type="link" size="small" onclick="viewDiagram('${record.id}')">查看流程图</a-button>`;
      },
    },
  ],
  useSearchForm: true,
  showTableSetting: true,
  bordered: true,
  pagination: {
    pageSize: 10,
  },
  formConfig: {
    labelWidth: 100,
    schemas: [
      {
        field: 'name',
        label: t('routes.workflow.instanceName'),
        component: 'Input',
        colProps: { span: 8 },
      },
      {
        field: 'status',
        label: t('routes.workflow.instanceStatus'),
        component: 'Select',
        componentProps: {
          options: [
            { label: '运行中', value: 'running' },
            { label: '已完成', value: 'completed' },
            { label: '已终止', value: 'terminated' },
          ],
        },
        colProps: { span: 8 },
      },
    ],
  },
});

// 任务监控表格
const [registerTaskTable] = useTable({
  title: '任务监控',
  api: workflowTaskApi.getAllTasks,
  columns: [
    {
      title: t('routes.workflow.taskName'),
      dataIndex: 'name',
      width: 200,
    },
    {
      title: t('routes.workflow.taskAssignee'),
      dataIndex: 'assignee',
      width: 120,
    },
    {
      title: t('routes.workflow.taskPriority'),
      dataIndex: 'priority',
      width: 100,
      customRender: ({ text }) => {
        const color = text >= 100 ? 'red' : text >= 50 ? 'orange' : 'green';
        const label = text >= 100 ? '高' : text >= 50 ? '中' : '低';
        return `<a-tag color="${color}">${label}</a-tag>`;
      },
    },
    {
      title: t('routes.workflow.taskCreateTime'),
      dataIndex: 'createTime',
      width: 180,
      customRender: ({ text }) => formatToDateTime(text),
    },
    {
      title: t('routes.workflow.taskDueDate'),
      dataIndex: 'dueDate',
      width: 180,
      customRender: ({ text }) => {
        const isOverdue = text && new Date(text) < new Date();
        return `<span style="color: ${isOverdue ? '#ff4d4f' : 'inherit'}">${formatToDateTime(text)}</span>`;
      },
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 100,
      customRender: ({ text }) => {
        const color = text === 'pending' ? 'orange' : text === 'completed' ? 'green' : 'blue';
        const label = text === 'pending' ? '待办' : text === 'completed' ? '已完成' : '进行中';
        return `<a-tag color="${color}">${label}</a-tag>`;
      },
    },
  ],
  useSearchForm: true,
  showTableSetting: true,
  bordered: true,
  pagination: {
    pageSize: 10,
  },
});

// 弹窗配置
const [registerDiagramModal, { openModal: openDiagramModal, closeModal: closeDiagramModal }] = useModal();

// 格式化持续时间
function formatDuration(duration: number) {
  if (!duration) return '-';
  const minutes = Math.floor(duration / 60000);
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);
  
  if (days > 0) return `${days}天${hours % 24}小时`;
  if (hours > 0) return `${hours}小时${minutes % 60}分钟`;
  return `${minutes}分钟`;
}

// 初始化图表
function initCharts() {
  if (instanceChartRef.value) {
    instanceChart = echarts.init(instanceChartRef.value);
  }
  if (taskChartRef.value) {
    taskChart = echarts.init(taskChartRef.value);
  }
  
  updateCharts();
}

// 更新图表数据
function updateCharts() {
  // 流程实例趋势图
  if (instanceChart) {
    const option = {
      title: {
        text: '流程实例趋势',
        left: 'center',
      },
      tooltip: {
        trigger: 'axis',
      },
      legend: {
        data: ['总实例', '运行中', '已完成'],
        bottom: 10,
      },
      xAxis: {
        type: 'category',
        data: ['1月', '2月', '3月', '4月', '5月', '6月'],
      },
      yAxis: {
        type: 'value',
      },
      series: [
        {
          name: '总实例',
          type: 'line',
          data: [120, 132, 101, 134, 90, 230],
        },
        {
          name: '运行中',
          type: 'line',
          data: [20, 32, 21, 34, 10, 30],
        },
        {
          name: '已完成',
          type: 'line',
          data: [100, 100, 80, 100, 80, 200],
        },
      ],
    };
    instanceChart.setOption(option);
  }
  
  // 任务分布饼图
  if (taskChart) {
    const option = {
      title: {
        text: '任务分布',
        left: 'center',
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)',
      },
      legend: {
        orient: 'vertical',
        left: 'left',
      },
      series: [
        {
          name: '任务状态',
          type: 'pie',
          radius: '50%',
          data: [
            { value: 35, name: '待办' },
            { value: 25, name: '进行中' },
            { value: 40, name: '已完成' },
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)',
            },
          },
        },
      ],
    };
    taskChart.setOption(option);
  }
}

// 加载统计数据
async function loadStats() {
  try {
    const statsData = await workflowStatsApi.getStats();
    stats.value = statsData;
    
    const myTaskStats = await workflowStatsApi.getMyTaskStats();
    stats.value.myPendingTasks = myTaskStats.pending;
    
    const instanceStats = await workflowStatsApi.getInstanceStats({});
    performance.value = instanceStats.performance;
  } catch (error) {
    console.error('加载统计数据失败:', error);
  }
}

// 查看流程图
async function viewDiagram(instanceId: string) {
  try {
    const instance = await workflowInstanceApi.getDetail(instanceId);
    currentInstance.value = instance;
    openDiagramModal();
    
    // 这里可以加载流程图
    // 可以使用 BPMN.js 或其他流程图库来渲染
    setTimeout(() => {
      if (diagramRef.value) {
        // 示例：简单的流程图显示
        diagramRef.value.innerHTML = `
          <div style="display: flex; justify-content: center; align-items: center; height: 100%;">
            <div style="text-align: center;">
              <h3>流程图: ${instance.name}</h3>
              <p>实例ID: ${instance.id}</p>
              <p>状态: ${instance.status}</p>
              <p>这里可以集成 BPMN.js 来显示实际的流程图</p>
            </div>
          </div>
        `;
      }
    }, 100);
  } catch (error) {
    console.error('加载流程图失败:', error);
  }
}

// 定时刷新
let refreshTimer: NodeJS.Timeout;
function startAutoRefresh() {
  refreshTimer = setInterval(() => {
    loadStats();
  }, 30000); // 30秒刷新一次
}

function stopAutoRefresh() {
  if (refreshTimer) {
    clearInterval(refreshTimer);
  }
}

// 窗口大小变化时重绘图表
function handleResize() {
  if (instanceChart) {
    instanceChart.resize();
  }
  if (taskChart) {
    taskChart.resize();
  }
}

onMounted(() => {
  loadStats();
  initCharts();
  startAutoRefresh();
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  stopAutoRefresh();
  window.removeEventListener('resize', handleResize);
  if (instanceChart) {
    instanceChart.dispose();
  }
  if (taskChart) {
    taskChart.dispose();
  }
});

// 暴露给模板的方法
defineExpose({
  viewDiagram,
});
</script>

<style scoped>
.mb-6 {
  margin-bottom: 24px;
}
</style> 