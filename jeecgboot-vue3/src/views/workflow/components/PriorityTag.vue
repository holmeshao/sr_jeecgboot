<template>
  <a-tag :color="tagColor">
    <template #icon>
      <component :is="tagIcon" />
    </template>
    {{ priorityText }}
  </a-tag>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { 
  ArrowDownOutlined,
  MinusOutlined,
  ArrowUpOutlined,
  ExclamationOutlined
} from '@ant-design/icons-vue';

// 定义组件属性
interface Props {
  level: number | string;
}

const props = defineProps<Props>();

// 优先级配置映射
const priorityConfig = {
  1: {
    text: '低',
    color: 'default',
    icon: ArrowDownOutlined
  },
  2: {
    text: '中',
    color: 'warning',
    icon: MinusOutlined
  },
  3: {
    text: '高',
    color: 'error',
    icon: ArrowUpOutlined
  },
  4: {
    text: '紧急',
    color: 'red',
    icon: ExclamationOutlined
  }
};

// 计算属性
const priorityText = computed(() => {
  const level = Number(props.level);
  return priorityConfig[level]?.text || `优先级${props.level}`;
});

const tagColor = computed(() => {
  const level = Number(props.level);
  return priorityConfig[level]?.color || 'default';
});

const tagIcon = computed(() => {
  const level = Number(props.level);
  return priorityConfig[level]?.icon || MinusOutlined;
});
</script>