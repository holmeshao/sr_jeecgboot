<template>
  <div class="process-timeline">
    <a-spin :spinning="loading" size="small">
      <a-timeline v-if="timelineData.length > 0">
        <a-timeline-item 
          v-for="(item, index) in timelineData" 
          :key="index"
          :color="getTimelineColor(item.status)"
        >
          <template #dot>
            <a-avatar 
              :size="compact ? 24 : 32" 
              :style="getAvatarStyle(item.status)"
            >
              <component :is="getStatusIcon(item.status)" />
            </a-avatar>
          </template>
          
          <div class="timeline-content">
            <div class="timeline-header">
              <h4 :class="{ compact: compact }">{{ item.taskName || item.activityName }}</h4>
              <span class="timeline-time">{{ formatTime(item.endTime || item.createTime) }}</span>
            </div>
            
            <div class="timeline-meta" v-if="!compact">
              <a-space>
                <a-tag :color="getStatusTagColor(item.status)">
                  {{ getStatusText(item.status) }}
                </a-tag>
                <span v-if="item.assignee">处理人：{{ item.assigneeName || item.assignee }}</span>
                <span v-if="item.duration">耗时：{{ formatDuration(item.duration) }}</span>
              </a-space>
            </div>
            
            <div class="timeline-assignee" v-if="compact && item.assigneeName">
              <span>{{ item.assigneeName }}</span>
            </div>
            
            <div class="timeline-comment" v-if="item.comment && !compact">
              <a-typography-paragraph 
                :ellipsis="{ rows: 2, expandable: true }"
                style="margin-bottom: 0;"
              >
                {{ item.comment }}
              </a-typography-paragraph>
            </div>
            
            <div class="timeline-attachments" v-if="item.attachments && item.attachments.length > 0 && !compact">
              <a-space>
                <a-tag 
                  v-for="attachment in item.attachments.slice(0, 3)" 
                  :key="attachment.id"
                  color="blue"
                  size="small"
                  style="cursor: pointer;"
                  @click="downloadAttachment(attachment)"
                >
                  <PaperClipOutlined />
                  {{ attachment.name }}
                </a-tag>
                <span v-if="item.attachments.length > 3">
                  等{{ item.attachments.length }}个附件
                </span>
              </a-space>
            </div>
          </div>
        </a-timeline-item>
      </a-timeline>
      
      <a-empty v-else description="暂无流程历史" :image="Empty.PRESENTED_IMAGE_SIMPLE" />
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { Empty, message } from 'ant-design-vue';
import { 
  CheckCircleOutlined,
  ClockCircleOutlined,
  ExclamationCircleOutlined,
  StopOutlined,
  PlayCircleOutlined,
  PaperClipOutlined
} from '@ant-design/icons-vue';
import { ProcessHistoryApi } from '@/api/workflow/form';
import { formatToDateTime } from '@/utils/dateUtil';

// 定义组件属性
interface Props {
  processInstanceId: string;
  compact?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  compact: false
});

// 响应式数据
const loading = ref(false);
const timelineData = ref([]);

// 监听processInstanceId变化
watch(() => props.processInstanceId, () => {
  if (props.processInstanceId) {
    loadTimelineData();
  }
}, { immediate: true });

/**
 * 加载时间线数据
 */
async function loadTimelineData() {
  if (!props.processInstanceId) return;
  
  loading.value = true;
  
  try {
    const [processHistory, taskHistory] = await Promise.all([
      ProcessHistoryApi.getProcessHistory(props.processInstanceId),
      ProcessHistoryApi.getTaskHistory(props.processInstanceId)
    ]);
    
    // 合并并排序历史数据
    const allHistory = [...processHistory, ...taskHistory];
    timelineData.value = allHistory
      .sort((a, b) => new Date(a.createTime).getTime() - new Date(b.createTime).getTime())
      .map(item => ({
        ...item,
        status: getItemStatus(item)
      }));
    
  } catch (error) {
    console.error('加载流程历史失败:', error);
    message.error('加载流程历史失败');
  } finally {
    loading.value = false;
  }
}

/**
 * 获取项目状态
 */
function getItemStatus(item: any): string {
  if (item.endTime) {
    return 'completed';
  } else if (item.suspended) {
    return 'suspended';
  } else if (item.assignee) {
    return 'active';
  } else {
    return 'waiting';
  }
}

/**
 * 获取时间线颜色
 */
function getTimelineColor(status: string): string {
  const colorMap = {
    'completed': 'green',
    'active': 'blue',
    'suspended': 'orange',
    'waiting': 'default',
    'error': 'red'
  };
  return colorMap[status] || 'default';
}

/**
 * 获取头像样式
 */
function getAvatarStyle(status: string) {
  const styleMap = {
    'completed': { backgroundColor: '#52c41a', color: 'white' },
    'active': { backgroundColor: '#1890ff', color: 'white' },
    'suspended': { backgroundColor: '#fa8c16', color: 'white' },
    'waiting': { backgroundColor: '#d9d9d9', color: '#666' },
    'error': { backgroundColor: '#ff4d4f', color: 'white' }
  };
  return styleMap[status] || styleMap['waiting'];
}

/**
 * 获取状态图标
 */
function getStatusIcon(status: string) {
  const iconMap = {
    'completed': CheckCircleOutlined,
    'active': PlayCircleOutlined,
    'suspended': StopOutlined,
    'waiting': ClockCircleOutlined,
    'error': ExclamationCircleOutlined
  };
  return iconMap[status] || ClockCircleOutlined;
}

/**
 * 获取状态标签颜色
 */
function getStatusTagColor(status: string): string {
  const colorMap = {
    'completed': 'success',
    'active': 'processing',
    'suspended': 'warning',
    'waiting': 'default',
    'error': 'error'
  };
  return colorMap[status] || 'default';
}

/**
 * 获取状态文本
 */
function getStatusText(status: string): string {
  const textMap = {
    'completed': '已完成',
    'active': '进行中',
    'suspended': '已暂停',
    'waiting': '等待中',
    'error': '异常'
  };
  return textMap[status] || '未知';
}

/**
 * 格式化时间
 */
function formatTime(time: string): string {
  return formatToDateTime(time);
}

/**
 * 格式化持续时间
 */
function formatDuration(duration: number): string {
  if (!duration) return '';
  
  const hours = Math.floor(duration / 3600000);
  const minutes = Math.floor((duration % 3600000) / 60000);
  
  if (hours > 0) {
    return `${hours}小时${minutes}分钟`;
  } else if (minutes > 0) {
    return `${minutes}分钟`;
  } else {
    return '不到1分钟';
  }
}

/**
 * 下载附件
 */
function downloadAttachment(attachment: any) {
  // TODO: 实现附件下载
  message.info('附件下载功能开发中...');
}
</script>

<style lang="less" scoped>
.process-timeline {
  .timeline-content {
    .timeline-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 8px;

      h4 {
        margin: 0;
        font-size: 14px;
        font-weight: 600;
        color: rgba(0, 0, 0, 0.85);
        
        &.compact {
          font-size: 12px;
        }
      }

      .timeline-time {
        font-size: 12px;
        color: rgba(0, 0, 0, 0.45);
        white-space: nowrap;
        margin-left: 12px;
      }
    }

    .timeline-meta {
      margin-bottom: 8px;
      font-size: 12px;
      color: rgba(0, 0, 0, 0.65);
    }

    .timeline-assignee {
      font-size: 12px;
      color: rgba(0, 0, 0, 0.65);
      margin-bottom: 4px;
    }

    .timeline-comment {
      margin-bottom: 8px;
      font-size: 13px;
      color: rgba(0, 0, 0, 0.65);
      line-height: 1.4;
    }

    .timeline-attachments {
      .ant-tag {
        margin-bottom: 4px;
      }
    }
  }
}

:deep(.ant-timeline-item-content) {
  min-height: auto;
}

:deep(.ant-typography) {
  font-size: 13px;
}
</style>