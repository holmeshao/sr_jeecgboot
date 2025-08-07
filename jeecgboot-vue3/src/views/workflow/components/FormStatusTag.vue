<template>
  <a-tag :color="tagColor" :icon="tagIcon">
    {{ statusText }}
  </a-tag>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { 
  EditOutlined,
  SyncOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined,
  ExclamationCircleOutlined,
  StopOutlined
} from '@ant-design/icons-vue';

// 定义组件属性
interface Props {
  status: string;
}

const props = defineProps<Props>();

// 状态配置映射
const statusConfig = {
  'DRAFT': {
    text: '草稿',
    color: 'default',
    icon: EditOutlined
  },
  'PROCESSING': {
    text: '处理中',
    color: 'processing',
    icon: SyncOutlined
  },
  'APPROVED': {
    text: '已通过',
    color: 'success',
    icon: CheckCircleOutlined
  },
  'REJECTED': {
    text: '已拒绝',
    color: 'error',
    icon: CloseCircleOutlined
  },
  'COMPLETED': {
    text: '已完成',
    color: 'success',
    icon: CheckCircleOutlined
  },
  'CANCELLED': {
    text: '已取消',
    color: 'warning',
    icon: StopOutlined
  },
  'SUSPENDED': {
    text: '已暂停',
    color: 'warning',
    icon: ExclamationCircleOutlined
  }
};

// 计算属性
const statusText = computed(() => {
  return statusConfig[props.status]?.text || props.status || '未知';
});

const tagColor = computed(() => {
  return statusConfig[props.status]?.color || 'default';
});

const tagIcon = computed(() => {
  return statusConfig[props.status]?.icon;
});
</script>