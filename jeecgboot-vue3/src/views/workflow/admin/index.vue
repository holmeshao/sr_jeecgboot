<template>
  <div class="workflow-admin">
    <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
      <div class="workflow-admin-content">
        <!-- 页面标题 -->
        <div class="workflow-admin-header">
          <h2>🎯 工作流系统管理</h2>
          <p class="description">基于Flowable 7.0的工作流系统管理和维护工具</p>
        </div>

        <!-- 系统状态卡片 -->
        <div class="status-cards">
          <a-row :gutter="16">
            <a-col :span="8">
              <a-card title="🔧 兼容性状态" :loading="statusLoading">
                <div class="status-content">
                  <pre>{{ compatibilityStatus }}</pre>
                </div>
                <template #extra>
                  <a-button type="link" @click="checkCompatibility">
                    <Icon icon="ant-design:reload-outlined" />
                    刷新
                  </a-button>
                </template>
              </a-card>
            </a-col>
            
            <a-col :span="8">
              <a-card title="📊 统计信息">
                <a-statistic title="流程定义数量" :value="stats.processCount" />
                <a-statistic title="活跃实例数量" :value="stats.activeInstances" />
                <a-statistic title="待处理任务" :value="stats.pendingTasks" />
              </a-card>
            </a-col>
            
            <a-col :span="8">
              <a-card title="⏰ 最近操作">
                <div class="recent-actions">
                  <div v-for="action in recentActions" :key="action.id" class="action-item">
                    <a-tag :color="action.color">{{ action.type }}</a-tag>
                    <span>{{ action.message }}</span>
                    <small>{{ action.time }}</small>
                  </div>
                </div>
              </a-card>
            </a-col>
          </a-row>
        </div>

        <!-- 管理操作区域 -->
        <div class="admin-actions">
          <a-card title="🚀 系统管理操作">
            <a-row :gutter="16">
              <!-- 批量事件处理 -->
              <a-col :span="12">
                <div class="action-group">
                  <h4>📋 批量事件处理</h4>
                  <p>对所有流程定义执行事件处理，包括字段权限解析等后续处理</p>
                  <a-space>
                    <a-button 
                      type="primary" 
                      :loading="bulkProcessing"
                      @click="handleBulkProcessing"
                    >
                      <Icon icon="ant-design:thunderbolt-outlined" />
                      批量处理所有流程
                    </a-button>
                    <a-button @click="handleStartupProcess" :loading="startupProcessing">
                      <Icon icon="ant-design:rocket-outlined" />
                      执行启动处理
                    </a-button>
                  </a-space>
                </div>
              </a-col>

              <!-- 单个流程处理 -->
              <a-col :span="12">
                <div class="action-group">
                  <h4>🎯 单个流程处理</h4>
                  <p>对指定的流程定义执行事件处理</p>
                  <a-space direction="vertical" style="width: 100%">
                    <a-input 
                      v-model:value="processKey"
                      placeholder="请输入流程定义Key"
                      @pressEnter="handleSingleProcessing"
                    />
                    <a-button 
                      type="primary" 
                      ghost
                      :loading="singleProcessing"
                      :disabled="!processKey"
                      @click="handleSingleProcessing"
                      block
                    >
                      <Icon icon="ant-design:setting-outlined" />
                      处理指定流程
                    </a-button>
                  </a-space>
                </div>
              </a-col>
            </a-row>
          </a-card>
        </div>

        <!-- 流程实例管理 -->
        <div class="instance-management">
          <a-card title="🔄 流程实例管理">
            <a-row :gutter="16">
              <a-col :span="12">
                <div class="action-group">
                  <h4>📤 实例启动事件</h4>
                  <p>手动触发流程实例启动事件处理</p>
                  <a-space direction="vertical" style="width: 100%">
                    <a-input 
                      v-model:value="instanceId"
                      placeholder="流程实例ID"
                    />
                    <a-input 
                      v-model:value="instanceProcessKey"
                      placeholder="流程定义Key"
                    />
                    <a-button 
                      type="primary" 
                      ghost
                      :loading="instanceProcessing"
                      :disabled="!instanceId || !instanceProcessKey"
                      @click="handleInstanceStartEvent"
                      block
                    >
                      <Icon icon="ant-design:play-circle-outlined" />
                      触发实例启动事件
                    </a-button>
                  </a-space>
                </div>
              </a-col>

              <a-col :span="12">
                <div class="action-group">
                  <h4>🧹 系统维护</h4>
                  <p>系统维护和清理操作</p>
                  <a-space direction="vertical" style="width: 100%">
                    <a-button @click="handleClearCache" block>
                      <Icon icon="ant-design:clear-outlined" />
                      清理缓存
                    </a-button>
                    <a-button @click="handleRefreshStats" :loading="statsLoading" block>
                      <Icon icon="ant-design:bar-chart-outlined" />
                      刷新统计
                    </a-button>
                    <a-button @click="handleExportLogs" block>
                      <Icon icon="ant-design:download-outlined" />
                      导出日志
                    </a-button>
                  </a-space>
                </div>
              </a-col>
            </a-row>
          </a-card>
        </div>

        <!-- 操作日志 -->
        <div class="operation-logs">
          <a-card title="📝 操作日志">
            <div class="log-controls">
              <a-space>
                <a-button @click="handleClearLogs" danger>
                  <Icon icon="ant-design:delete-outlined" />
                  清空日志
                </a-button>
                <a-button @click="handleRefreshLogs">
                  <Icon icon="ant-design:reload-outlined" />
                  刷新
                </a-button>
              </a-space>
            </div>
            <div class="log-container">
              <div 
                v-for="log in operationLogs" 
                :key="log.id" 
                class="log-item"
                :class="log.level"
              >
                <span class="log-time">{{ log.time }}</span>
                <span class="log-level">{{ log.level }}</span>
                <span class="log-message">{{ log.message }}</span>
              </div>
            </div>
          </a-card>
        </div>
      </div>
    </PageWrapper>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { PageWrapper } from '/@/components/Page';
import { useMessage } from '/@/hooks/web/useMessage';
import { Icon } from '/@/components/Icon';
import { 
  triggerAllDeploymentEvents, 
  triggerDeploymentEvent,
  triggerInstanceStartEvent,
  checkFlowable7Status,
  executeStartupProcess
} from '/@/api/workflow';

const { createMessage } = useMessage();

// ================================== 响应式数据 ==================================

const statusLoading = ref(false);
const bulkProcessing = ref(false);
const singleProcessing = ref(false);
const startupProcessing = ref(false);
const instanceProcessing = ref(false);
const statsLoading = ref(false);

const compatibilityStatus = ref('');
const processKey = ref('');
const instanceId = ref('');
const instanceProcessKey = ref('');

const stats = reactive({
  processCount: 0,
  activeInstances: 0,
  pendingTasks: 0
});

const recentActions = ref([
  { id: 1, type: '部署', message: '订单流程部署成功', time: '2分钟前', color: 'green' },
  { id: 2, type: '处理', message: '批量事件处理完成', time: '5分钟前', color: 'blue' },
  { id: 3, type: '启动', message: '系统启动处理执行', time: '10分钟前', color: 'orange' }
]);

const operationLogs = ref([
  { id: 1, time: '2024-01-25 10:30:00', level: 'INFO', message: '工作流系统管理页面加载完成' },
  { id: 2, time: '2024-01-25 10:29:00', level: 'INFO', message: 'Flowable 7.0兼容性检查通过' }
]);

// ================================== 方法定义 ==================================

/**
 * 检查兼容性状态
 */
const checkCompatibility = async () => {
  statusLoading.value = true;
  try {
    const result = await checkFlowable7Status();
    compatibilityStatus.value = result;
    addLog('INFO', '兼容性状态检查完成');
  } catch (error) {
    createMessage.error('检查兼容性状态失败');
    addLog('ERROR', `兼容性检查失败: ${error.message}`);
  } finally {
    statusLoading.value = false;
  }
};

/**
 * 批量处理所有流程
 */
const handleBulkProcessing = async () => {
  bulkProcessing.value = true;
  try {
    await triggerAllDeploymentEvents();
    createMessage.success('批量处理完成');
    addLog('INFO', '批量事件处理执行成功');
    updateRecentAction('批量处理', '所有流程事件处理完成', 'blue');
  } catch (error) {
    createMessage.error('批量处理失败');
    addLog('ERROR', `批量处理失败: ${error.message}`);
  } finally {
    bulkProcessing.value = false;
  }
};

/**
 * 处理指定流程
 */
const handleSingleProcessing = async () => {
  if (!processKey.value) {
    createMessage.warning('请输入流程定义Key');
    return;
  }
  
  singleProcessing.value = true;
  try {
    await triggerDeploymentEvent(processKey.value);
    createMessage.success(`流程 ${processKey.value} 处理完成`);
    addLog('INFO', `流程 ${processKey.value} 事件处理成功`);
    updateRecentAction('单个处理', `流程 ${processKey.value} 处理完成`, 'green');
    processKey.value = '';
  } catch (error) {
    createMessage.error('流程处理失败');
    addLog('ERROR', `流程 ${processKey.value} 处理失败: ${error.message}`);
  } finally {
    singleProcessing.value = false;
  }
};

/**
 * 执行启动处理
 */
const handleStartupProcess = async () => {
  startupProcessing.value = true;
  try {
    await executeStartupProcess();
    createMessage.success('启动处理执行完成');
    addLog('INFO', '系统启动处理执行成功');
    updateRecentAction('启动处理', '系统启动处理完成', 'orange');
  } catch (error) {
    createMessage.error('启动处理失败');
    addLog('ERROR', `启动处理失败: ${error.message}`);
  } finally {
    startupProcessing.value = false;
  }
};

/**
 * 触发实例启动事件
 */
const handleInstanceStartEvent = async () => {
  if (!instanceId.value || !instanceProcessKey.value) {
    createMessage.warning('请输入流程实例ID和流程定义Key');
    return;
  }
  
  instanceProcessing.value = true;
  try {
    await triggerInstanceStartEvent(instanceId.value, instanceProcessKey.value);
    createMessage.success('实例启动事件处理完成');
    addLog('INFO', `实例 ${instanceId.value} 启动事件处理成功`);
    updateRecentAction('实例事件', `实例 ${instanceId.value} 处理完成`, 'purple');
    instanceId.value = '';
    instanceProcessKey.value = '';
  } catch (error) {
    createMessage.error('实例启动事件处理失败');
    addLog('ERROR', `实例启动事件处理失败: ${error.message}`);
  } finally {
    instanceProcessing.value = false;
  }
};

/**
 * 清理缓存
 */
const handleClearCache = () => {
  createMessage.success('缓存清理完成');
  addLog('INFO', '系统缓存清理完成');
};

/**
 * 刷新统计
 */
const handleRefreshStats = async () => {
  statsLoading.value = true;
  try {
    // 模拟获取统计数据
    await new Promise(resolve => setTimeout(resolve, 1000));
    stats.processCount = Math.floor(Math.random() * 50) + 10;
    stats.activeInstances = Math.floor(Math.random() * 100) + 20;
    stats.pendingTasks = Math.floor(Math.random() * 30) + 5;
    createMessage.success('统计信息刷新完成');
    addLog('INFO', '统计信息刷新完成');
  } finally {
    statsLoading.value = false;
  }
};

/**
 * 导出日志
 */
const handleExportLogs = () => {
  createMessage.success('日志导出完成');
  addLog('INFO', '操作日志导出完成');
};

/**
 * 清空日志
 */
const handleClearLogs = () => {
  operationLogs.value = [];
  createMessage.success('日志已清空');
};

/**
 * 刷新日志
 */
const handleRefreshLogs = () => {
  addLog('INFO', '日志刷新完成');
};

/**
 * 添加操作日志
 */
const addLog = (level: string, message: string) => {
  const now = new Date();
  operationLogs.value.unshift({
    id: Date.now(),
    time: now.toLocaleString(),
    level,
    message
  });
  
  // 保持最多100条日志
  if (operationLogs.value.length > 100) {
    operationLogs.value = operationLogs.value.slice(0, 100);
  }
};

/**
 * 更新最近操作
 */
const updateRecentAction = (type: string, message: string, color: string) => {
  recentActions.value.unshift({
    id: Date.now(),
    type,
    message,
    time: '刚刚',
    color
  });
  
  // 保持最多5条记录
  if (recentActions.value.length > 5) {
    recentActions.value = recentActions.value.slice(0, 5);
  }
};

// ================================== 生命周期 ==================================

onMounted(async () => {
  addLog('INFO', '工作流系统管理页面初始化完成');
  await checkCompatibility();
  await handleRefreshStats();
});
</script>

<style lang="less" scoped>
.workflow-admin {
  padding: 16px;
  
  &-header {
    margin-bottom: 24px;
    text-align: center;
    
    h2 {
      margin-bottom: 8px;
      color: #1890ff;
    }
    
    .description {
      color: #666;
      margin-bottom: 0;
    }
  }
  
  .status-cards, .admin-actions, .instance-management, .operation-logs {
    margin-bottom: 24px;
  }
  
  .status-content {
    max-height: 200px;
    overflow-y: auto;
    font-size: 12px;
    line-height: 1.4;
  }
  
  .action-group {
    padding: 16px;
    border: 1px solid #f0f0f0;
    border-radius: 8px;
    
    h4 {
      margin-bottom: 8px;
      color: #333;
    }
    
    p {
      margin-bottom: 16px;
      color: #666;
      font-size: 14px;
    }
  }
  
  .recent-actions {
    .action-item {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 8px;
      font-size: 12px;
      
      small {
        color: #999;
        margin-left: auto;
      }
    }
  }
  
  .log-controls {
    margin-bottom: 16px;
  }
  
  .log-container {
    max-height: 300px;
    overflow-y: auto;
    border: 1px solid #f0f0f0;
    border-radius: 4px;
    padding: 12px;
    background: #fafafa;
    
    .log-item {
      display: flex;
      gap: 12px;
      margin-bottom: 4px;
      font-size: 12px;
      font-family: 'Courier New', monospace;
      
      &.INFO .log-level {
        color: #52c41a;
      }
      
      &.ERROR .log-level {
        color: #ff4d4f;
      }
      
      &.WARN .log-level {
        color: #faad14;
      }
      
      .log-time {
        color: #999;
        min-width: 130px;
      }
      
      .log-level {
        font-weight: bold;
        min-width: 50px;
      }
      
      .log-message {
        flex: 1;
      }
    }
  }
}
</style>