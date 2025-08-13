<template>
  <div class="version-compare">
    <!-- 版本对比头部 -->
    <div class="compare-header">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-card size="small" :title="`版本 v${versions[0]?.versionNumber} (${getVersionLabel(0)})`">
            <a-descriptions size="small" :column="1">
              <a-descriptions-item label="节点">{{ getNodeDisplayName(versions[0]?.nodeCode) }}</a-descriptions-item>
              <a-descriptions-item label="操作人">{{ versions[0]?.operatorName }}</a-descriptions-item>
              <a-descriptions-item label="时间">{{ formatTime(versions[0]?.timestamp) }}</a-descriptions-item>
            </a-descriptions>
          </a-card>
        </a-col>
        <a-col :span="12">
          <a-card size="small" :title="`版本 v${versions[1]?.versionNumber} (${getVersionLabel(1)})`">
            <a-descriptions size="small" :column="1">
              <a-descriptions-item label="节点">{{ getNodeDisplayName(versions[1]?.nodeCode) }}</a-descriptions-item>
              <a-descriptions-item label="操作人">{{ versions[1]?.operatorName }}</a-descriptions-item>
              <a-descriptions-item label="时间">{{ formatTime(versions[1]?.timestamp) }}</a-descriptions-item>
            </a-descriptions>
          </a-card>
        </a-col>
      </a-row>
    </div>

    <!-- 对比选项 -->
    <div class="compare-options">
      <a-row justify="space-between" align="middle">
        <a-col>
          <a-space>
            <a-switch 
              v-model:checked="showOnlyDifferences" 
              checked-children="仅显示差异" 
              un-checked-children="显示全部" 
            />
            <a-select 
              v-model:value="compareMode" 
              style="width: 120px;" 
              size="small"
            >
              <a-select-option value="side-by-side">并排对比</a-select-option>
              <a-select-option value="unified">统一视图</a-select-option>
            </a-select>
          </a-space>
        </a-col>
        <a-col>
          <a-space>
            <a-button @click="exportComparison" size="small">
              <ExportOutlined />
              导出对比
            </a-button>
            <a-button @click="switchVersions" size="small">
              <SwapOutlined />
              交换版本
            </a-button>
          </a-space>
        </a-col>
      </a-row>
    </div>

    <!-- 对比内容 -->
    <div class="compare-content">
      <!-- 并排对比模式 -->
      <div v-if="compareMode === 'side-by-side'" class="side-by-side-compare">
        <a-row :gutter="16">
          <a-col :span="12">
            <div class="version-panel left-panel">
              <h4>{{ getVersionTitle(0) }}</h4>
              <div class="field-list">
                <div 
                  v-for="field in displayFields" 
                  :key="field.name"
                  :class="['field-item', getFieldStatus(field, 0)]"
                >
                  <div class="field-label">{{ field.label }}</div>
                  <div class="field-value">
                    <span :class="{ 'diff-highlight': field.isDifferent }">
                      {{ formatFieldValue(field.leftValue) }}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="version-panel right-panel">
              <h4>{{ getVersionTitle(1) }}</h4>
              <div class="field-list">
                <div 
                  v-for="field in displayFields" 
                  :key="field.name"
                  :class="['field-item', getFieldStatus(field, 1)]"
                >
                  <div class="field-label">{{ field.label }}</div>
                  <div class="field-value">
                    <span :class="{ 'diff-highlight': field.isDifferent }">
                      {{ formatFieldValue(field.rightValue) }}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </a-col>
        </a-row>
      </div>

      <!-- 统一视图模式 -->
      <div v-else class="unified-compare">
        <div class="field-list">
          <div 
            v-for="field in displayFields" 
            :key="field.name"
            :class="['field-item-unified', { 'field-different': field.isDifferent }]"
          >
            <div class="field-header">
              <h5>{{ field.label }}</h5>
              <a-tag v-if="field.isDifferent" color="orange" size="small">已变更</a-tag>
              <a-tag v-else color="green" size="small">无变更</a-tag>
            </div>
            
            <div class="field-content" v-if="field.isDifferent">
              <div class="value-before">
                <span class="value-label">变更前：</span>
                <span class="value-text removed">{{ formatFieldValue(field.leftValue) }}</span>
              </div>
              <div class="value-after">
                <span class="value-label">变更后：</span>
                <span class="value-text added">{{ formatFieldValue(field.rightValue) }}</span>
              </div>
            </div>
            
            <div class="field-content" v-else>
              <div class="value-same">
                <span class="value-text">{{ formatFieldValue(field.leftValue) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 变更统计 -->
    <div class="change-summary">
      <a-card title="变更摘要" size="small">
        <a-row :gutter="16">
          <a-col :span="6">
            <a-statistic title="总字段数" :value="allFields.length" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="变更字段" :value="changedFields.length" suffix="个" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="变更率" :value="changeRate" suffix="%" :precision="1" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="时间间隔" :value="timeDiff" suffix="分钟" />
          </a-col>
        </a-row>
        
        <div class="changed-fields-list" v-if="changedFields.length > 0">
          <h5>变更字段详情:</h5>
          <a-space wrap>
            <a-tag 
              v-for="field in changedFields" 
              :key="field.name"
              color="orange"
            >
              {{ field.label }}
            </a-tag>
          </a-space>
        </div>
      </a-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { ExportOutlined, SwapOutlined } from '@ant-design/icons-vue';
import { useMethods } from '/@/hooks/system/useMethods';
import dayjs from 'dayjs';
import { formatToDateTime } from '@/utils/dateUtil';

// 定义组件属性
interface Props {
  versions: any[];
  formConfig?: any;
}

const props = defineProps<Props>();

// 响应式数据
const showOnlyDifferences = ref(true);
const compareMode = ref('side-by-side');

// 字段显示名称映射
const fieldDisplayNames = reactive({
  'title': '标题',
  'description': '描述',
  'urgency_level': '紧急程度',
  'project_id': '项目',
  'report_no': '工单编号',
  'create_time': '创建时间',
  'update_time': '更新时间',
  'status': '状态',
  'priority': '优先级',
  'assignee': '分配人',
  'comment': '备注'
});

// 计算属性
const allFields = computed(() => {
  if (!props.versions || props.versions.length < 2) return [];
  
  const leftData = props.versions[0]?.formData || {};
  const rightData = props.versions[1]?.formData || {};
  
  // 获取所有字段
  const allKeys = new Set([...Object.keys(leftData), ...Object.keys(rightData)]);
  
  return Array.from(allKeys).map(key => {
    const leftValue = leftData[key];
    const rightValue = rightData[key];
    const isDifferent = !isEqual(leftValue, rightValue);
    
    return {
      name: key,
      label: getFieldDisplayName(key),
      leftValue,
      rightValue,
      isDifferent
    };
  });
});

const displayFields = computed(() => {
  if (showOnlyDifferences.value) {
    return allFields.value.filter(field => field.isDifferent);
  }
  return allFields.value;
});

const changedFields = computed(() => {
  return allFields.value.filter(field => field.isDifferent);
});

const changeRate = computed(() => {
  if (allFields.value.length === 0) return 0;
  return (changedFields.value.length / allFields.value.length) * 100;
});

const timeDiff = computed(() => {
  if (!props.versions || props.versions.length < 2) return 0;
  
  const time1 = props.versions[0]?.timestamp;
  const time2 = props.versions[1]?.timestamp;
  
  if (!time1 || !time2) return 0;
  
  return Math.abs(time1 - time2) / (1000 * 60); // 转换为分钟
});

/**
 * 获取版本标签
 */
function getVersionLabel(index: number): string {
  if (!props.versions || !props.versions[index]) return '';
  
  const version = props.versions[index];
  const otherVersion = props.versions[1 - index];
  
  if (!otherVersion) return '当前版本';
  
  if (version.timestamp > otherVersion.timestamp) {
    return '较新版本';
  } else {
    return '较旧版本';
  }
}

/**
 * 获取版本标题
 */
function getVersionTitle(index: number): string {
  if (!props.versions || !props.versions[index]) return '';
  
  const version = props.versions[index];
  return `v${version.versionNumber} - ${getNodeDisplayName(version.nodeCode)}`;
}

/**
 * 获取节点显示名称
 */
function getNodeDisplayName(nodeCode: string): string {
  const nameMap = {
    'start': '流程开始',
    'submit': '提交申请',
    'approve': '审批节点',
    'reject': '拒绝节点',
    'complete': '流程完成'
  };
  return nameMap[nodeCode] || nodeCode;
}

/**
 * 获取字段显示名称
 */
function getFieldDisplayName(fieldName: string): string {
  return fieldDisplayNames[fieldName] || fieldName;
}

/**
 * 格式化时间
 */
function formatTime(timestamp: number): string {
  return formatToDateTime(new Date(timestamp));
}

/**
 * 格式化字段值
 */
function formatFieldValue(value: any): string {
  if (value === null || value === undefined) {
    return '(空)';
  }
  
  if (typeof value === 'boolean') {
    return value ? '是' : '否';
  }
  
  if (typeof value === 'object') {
    return JSON.stringify(value, null, 2);
  }
  
  if (typeof value === 'string' && value.trim() === '') {
    return '(空字符串)';
  }
  
  return String(value);
}

/**
 * 获取字段状态
 */
function getFieldStatus(field: any, versionIndex: number): string {
  if (!field.isDifferent) return 'same';
  
  const leftValue = field.leftValue;
  const rightValue = field.rightValue;
  
  if (versionIndex === 0) {
    // 左侧版本
    if (leftValue === null || leftValue === undefined) return 'added';
    if (rightValue === null || rightValue === undefined) return 'removed';
    return 'modified';
  } else {
    // 右侧版本
    if (rightValue === null || rightValue === undefined) return 'removed';
    if (leftValue === null || leftValue === undefined) return 'added';
    return 'modified';
  }
}

/**
 * 判断两个值是否相等
 */
function isEqual(a: any, b: any): boolean {
  if (a === b) return true;
  
  if (a === null || a === undefined || b === null || b === undefined) {
    return a === b;
  }
  
  if (typeof a !== typeof b) return false;
  
  if (typeof a === 'object') {
    return JSON.stringify(a) === JSON.stringify(b);
  }
  
  return String(a) === String(b);
}

/**
 * 交换版本
 */
function switchVersions() {
  if (props.versions.length >= 2) {
    const temp = props.versions[0];
    props.versions[0] = props.versions[1];
    props.versions[1] = temp;
  }
}

/**
 * 导出对比结果
 */
async function exportComparison() {
  // TODO: 实现导出功能
  try {
    // 导出版本对比详情
    const { handleExportXls } = useMethods();
    
    const exportParams = {
      formId: props.formId,
      leftVersion: props.leftVersion,
      rightVersion: props.rightVersion,
      differences: differences.value
    };

    const fileName = `版本对比详情_${props.formId}_${dayjs().format('YYYY-MM-DD-HH-mm-ss')}`;
    await handleExportXls(fileName, '/workflow/version/compare/export', exportParams);
    
    message.success('导出成功');
  } catch (error) {
    console.error('导出失败:', error);
    message.error('导出失败，请重试');
  };
}
</script>

<style lang="less" scoped>
.version-compare {
  .compare-header {
    margin-bottom: 16px;
  }

  .compare-options {
    margin-bottom: 24px;
    padding: 12px 16px;
    background: #fafafa;
    border-radius: 6px;
  }

  .compare-content {
    margin-bottom: 24px;

    .side-by-side-compare {
      .version-panel {
        border: 1px solid #d9d9d9;
        border-radius: 6px;
        padding: 16px;
        background: white;

        &.left-panel {
          border-right: 2px solid #1890ff;
        }

        &.right-panel {
          border-left: 2px solid #52c41a;
        }

        h4 {
          margin: 0 0 16px 0;
          padding-bottom: 8px;
          border-bottom: 1px solid #f0f0f0;
          font-weight: 600;
        }

        .field-list {
          .field-item {
            display: flex;
            margin-bottom: 12px;
            padding: 8px;
            border-radius: 4px;
            transition: all 0.2s;

            &.same {
              background: #f6ffed;
              border-left: 3px solid #52c41a;
            }

            &.modified {
              background: #fff7e6;
              border-left: 3px solid #fa8c16;
            }

            &.added {
              background: #f6ffed;
              border-left: 3px solid #52c41a;
            }

            &.removed {
              background: #fff2f0;
              border-left: 3px solid #ff4d4f;
            }

            .field-label {
              width: 80px;
              font-weight: 500;
              color: rgba(0, 0, 0, 0.65);
              flex-shrink: 0;
            }

            .field-value {
              flex: 1;
              word-break: break-all;

              .diff-highlight {
                background: #fffb8f;
                padding: 2px 4px;
                border-radius: 2px;
              }
            }
          }
        }
      }
    }

    .unified-compare {
      .field-list {
        .field-item-unified {
          margin-bottom: 16px;
          border: 1px solid #d9d9d9;
          border-radius: 6px;
          padding: 16px;
          background: white;

          &.field-different {
            border-color: #fa8c16;
            background: #fff7e6;
          }

          .field-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 12px;
            padding-bottom: 8px;
            border-bottom: 1px solid #f0f0f0;

            h5 {
              margin: 0;
              font-weight: 600;
            }
          }

          .field-content {
            .value-before,
            .value-after,
            .value-same {
              margin-bottom: 8px;
              font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;

              .value-label {
                display: inline-block;
                width: 80px;
                font-weight: 500;
                color: rgba(0, 0, 0, 0.65);
              }

              .value-text {
                &.removed {
                  background: #ffebee;
                  color: #d32f2f;
                  text-decoration: line-through;
                  padding: 2px 4px;
                  border-radius: 2px;
                }

                &.added {
                  background: #e8f5e8;
                  color: #2e7d32;
                  padding: 2px 4px;
                  border-radius: 2px;
                }
              }
            }

            .value-same .value-text {
              color: rgba(0, 0, 0, 0.85);
            }
          }
        }
      }
    }
  }

  .change-summary {
    .changed-fields-list {
      margin-top: 16px;

      h5 {
        margin-bottom: 8px;
        font-weight: 500;
      }
    }
  }
}

:deep(.ant-descriptions-item-label) {
  font-weight: 500;
  width: 60px;
}

:deep(.ant-card-head-title) {
  font-size: 14px;
}

:deep(.ant-statistic-title) {
  font-size: 12px;
}

:deep(.ant-statistic-content) {
  font-size: 16px;
}
</style>