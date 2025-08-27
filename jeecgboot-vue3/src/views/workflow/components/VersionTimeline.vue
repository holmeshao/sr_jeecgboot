<template>
  <div class="version-history-timeline">
    <a-spin :spinning="loading">
      <!-- 版本统计信息 -->
      <div class="version-stats" v-if="!loading && versionHistory.length > 0">
        <a-row :gutter="16" style="margin-bottom: 24px">
          <a-col :span="6">
            <a-statistic title="总版本数" :value="versionHistory.length" prefix="v" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="最新版本" :value="latestVersion" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="创建时间" :value="createTime" formatter="date" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="最后更新" :value="lastUpdateTime" formatter="date" />
          </a-col>
        </a-row>
      </div>

      <!-- 版本操作工具栏 -->
      <div class="version-toolbar" v-if="!loading && versionHistory.length > 0">
        <a-row justify="space-between" align="middle" style="margin-bottom: 16px">
          <a-col>
            <a-space>
              <a-button @click="selectAllVersions" size="small">全选</a-button>
              <a-button @click="clearSelection" size="small">清除选择</a-button>
              <a-button :disabled="selectedVersions.length !== 2" type="primary" size="small" @click="compareSelectedVersions">
                对比选中版本 ({{ selectedVersions.length }}/2)
              </a-button>
            </a-space>
          </a-col>
          <a-col>
            <a-input-search v-model:value="searchKeyword" placeholder="搜索版本..." style="width: 200px" size="small" @search="filterVersions" />
          </a-col>
        </a-row>
      </div>

      <!-- 版本历史时间线 -->
      <a-timeline v-if="!loading && filteredVersions.length > 0">
        <a-timeline-item v-for="(snapshot, index) in filteredVersions" :key="snapshot.id" :color="getTimelineColor(snapshot.nodeCode)">
          <template #dot>
            <a-avatar
              :size="32"
              :style="getNodeAvatarStyle(snapshot.nodeCode)"
              @click="toggleVersionSelection(snapshot)"
              :class="{ 'selected-avatar': isVersionSelected(snapshot) }"
              style="cursor: pointer"
            >
              <template v-if="isVersionSelected(snapshot)">
                <CheckOutlined />
              </template>
              <template v-else>
                {{ getNodeIcon(snapshot.nodeCode) }}
              </template>
            </a-avatar>
          </template>

          <div class="timeline-content">
            <div class="timeline-header">
              <h4>
                <a-checkbox :checked="isVersionSelected(snapshot)" @change="() => toggleVersionSelection(snapshot)" style="margin-right: 8px" />
                {{ getNodeDisplayName(snapshot.nodeCode) }}
                <a-tag size="small" style="margin-left: 8px">v{{ snapshot.versionNumber }}</a-tag>
              </h4>
              <span class="timeline-time">{{ formatTime(snapshot.timestamp) }}</span>
            </div>

            <div class="timeline-meta">
              <a-space wrap>
                <a-tag color="blue">{{ snapshot.operatorName }}</a-tag>
                <span v-if="snapshot.changedFields?.length" class="changed-fields"> 变更了 {{ snapshot.changedFields.length }} 个字段 </span>
                <a-tooltip title="变更字段详情">
                  <a-space>
                    <a-tag v-for="field in snapshot.changedFields?.slice(0, 3)" :key="field" size="small" color="orange">
                      {{ getFieldDisplayName(field) }}
                    </a-tag>
                    <span v-if="snapshot.changedFields?.length > 3" class="more-fields"> +{{ snapshot.changedFields.length - 3 }}... </span>
                  </a-space>
                </a-tooltip>
              </a-space>
            </div>

            <!-- 版本详情预览 -->
            <div class="version-preview" v-if="snapshot.showPreview">
              <a-descriptions size="small" :column="2" bordered style="margin: 12px 0">
                <a-descriptions-item v-for="(value, key) in getImportantFields(snapshot.formData)" :key="key" :label="getFieldDisplayName(key)">
                  <span :class="{ 'field-changed': snapshot.changedFields?.includes(key) }">
                    {{ formatFieldValue(value) }}
                  </span>
                </a-descriptions-item>
              </a-descriptions>
            </div>

            <div class="timeline-actions">
              <a-space>
                <a-button size="small" type="link" @click="togglePreview(snapshot)">
                  {{ snapshot.showPreview ? '收起详情' : '查看详情' }}
                </a-button>
                <a-button size="small" type="link" @click="compareWithCurrent(snapshot)"> 与当前对比 </a-button>
                <a-dropdown>
                  <a-button size="small" type="link"> 更多 <DownOutlined /> </a-button>
                  <template #overlay>
                    <a-menu>
                      <a-menu-item @click="compareWithPrevious(snapshot, index)"> 与上版本对比 </a-menu-item>
                      <a-menu-item @click="exportSnapshot(snapshot)"> 导出此版本 </a-menu-item>
                      <a-menu-item @click="rollbackToSnapshot(snapshot)" :disabled="!canRollback(snapshot)"> 回滚到此版本 </a-menu-item>
                      <a-menu-divider />
                      <a-menu-item @click="downloadVersionReport(snapshot)"> 下载版本报告 </a-menu-item>
                    </a-menu>
                  </template>
                </a-dropdown>
              </a-space>
            </div>
          </div>
        </a-timeline-item>
      </a-timeline>

      <!-- 空状态 -->
      <a-empty v-else-if="!loading" description="暂无版本历史" :image="Empty.PRESENTED_IMAGE_SIMPLE" />
    </a-spin>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, computed, onMounted, watch } from 'vue';
  import { message, Modal, Empty } from 'ant-design-vue';
  import { useMethods } from '/@/hooks/system/useMethods';
  import dayjs from 'dayjs';
  import { CheckOutlined, DownOutlined, ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { getVersionHistory } from '/@/api/workflow';
  import { formatToDateTime } from '@/utils/dateUtil';

  // 定义组件属性
  interface Props {
    processInstanceId: string;
  }

  const props = defineProps<Props>();

  // 定义事件
  const emit = defineEmits<{
    compare: [versions: any[]];
  }>();

  // 响应式数据
  const loading = ref(false);
  const versionHistory = ref([]);
  const filteredVersions = ref([]);
  const selectedVersions = ref([]);
  const searchKeyword = ref('');

  // 字段显示名称映射
  const fieldDisplayNames = reactive({
    title: '标题',
    description: '描述',
    urgency_level: '紧急程度',
    project_id: '项目',
    report_no: '工单编号',
    create_time: '创建时间',
    update_time: '更新时间',
  });

  // 计算属性
  const latestVersion = computed(() => {
    return versionHistory.value.length > 0 ? Math.max(...versionHistory.value.map((v) => v.versionNumber)) : 0;
  });

  const createTime = computed(() => {
    return versionHistory.value.length > 0 ? versionHistory.value[versionHistory.value.length - 1].timestamp : null;
  });

  const lastUpdateTime = computed(() => {
    return versionHistory.value.length > 0 ? versionHistory.value[0].timestamp : null;
  });

  // 监听processInstanceId变化
  watch(
    () => props.processInstanceId,
    () => {
      if (props.processInstanceId) {
        loadVersionHistory();
      }
    },
    { immediate: true }
  );

  // 监听搜索关键词
  watch(searchKeyword, () => {
    filterVersions();
  });

  /**
   * 加载版本历史
   */
  async function loadVersionHistory() {
    if (!props.processInstanceId) return;

    loading.value = true;

    try {
      const data = await getVersionHistory(props.processInstanceId);
      versionHistory.value = data.map((item) => ({
        ...item,
        showPreview: false,
      }));
      filteredVersions.value = [...versionHistory.value];
    } catch (error) {
      console.error('加载版本历史失败:', error);
      message.error('加载版本历史失败');
    } finally {
      loading.value = false;
    }
  }

  /**
   * 过滤版本
   */
  function filterVersions() {
    if (!searchKeyword.value.trim()) {
      filteredVersions.value = [...versionHistory.value];
      return;
    }

    const keyword = searchKeyword.value.toLowerCase();
    filteredVersions.value = versionHistory.value.filter(
      (version) =>
        version.nodeCode.toLowerCase().includes(keyword) ||
        version.operatorName?.toLowerCase().includes(keyword) ||
        version.changedFields?.some((field) => getFieldDisplayName(field).toLowerCase().includes(keyword))
    );
  }

  /**
   * 切换版本选择状态
   */
  function toggleVersionSelection(snapshot: any) {
    const index = selectedVersions.value.findIndex((v) => v.id === snapshot.id);

    if (index > -1) {
      selectedVersions.value.splice(index, 1);
    } else {
      if (selectedVersions.value.length >= 2) {
        message.warning('最多只能选择2个版本进行对比');
        return;
      }
      selectedVersions.value.push(snapshot);
    }
  }

  /**
   * 判断版本是否被选中
   */
  function isVersionSelected(snapshot: any): boolean {
    return selectedVersions.value.some((v) => v.id === snapshot.id);
  }

  /**
   * 全选版本
   */
  function selectAllVersions() {
    if (filteredVersions.value.length <= 2) {
      selectedVersions.value = [...filteredVersions.value];
    } else {
      selectedVersions.value = filteredVersions.value.slice(0, 2);
      message.info('已选择前2个版本');
    }
  }

  /**
   * 清除选择
   */
  function clearSelection() {
    selectedVersions.value = [];
  }

  /**
   * 对比选中的版本
   */
  function compareSelectedVersions() {
    if (selectedVersions.value.length !== 2) {
      message.warning('请选择2个版本进行对比');
      return;
    }

    emit('compare', selectedVersions.value);
  }

  /**
   * 与当前版本对比
   */
  function compareWithCurrent(snapshot: any) {
    // 获取当前版本（第一个）
    const currentVersion = versionHistory.value[0];
    emit('compare', [snapshot, currentVersion]);
  }

  /**
   * 与上一版本对比
   */
  function compareWithPrevious(snapshot: any, index: number) {
    if (index >= filteredVersions.value.length - 1) {
      message.warning('已经是最早的版本');
      return;
    }

    const previousVersion = filteredVersions.value[index + 1];
    emit('compare', [snapshot, previousVersion]);
  }

  /**
   * 切换预览状态
   */
  function togglePreview(snapshot: any) {
    snapshot.showPreview = !snapshot.showPreview;
  }

  /**
   * 导出快照
   */
  async function exportSnapshot(snapshot: any) {
    // 🎯 导出版本快照功能实现
    try {
      // 导出版本对比结果
      const { handleExportXls } = useMethods();

      const exportParams = {
        formId: props.formId,
        versions: props.versions,
      };

      const fileName = `版本对比_${props.formId}_${dayjs().format('YYYY-MM-DD-HH-mm-ss')}`;
      await handleExportXls(fileName, '/workflow/version/export', exportParams);

      message.success('导出成功');
    } catch (error) {
      console.error('导出失败:', error);
      message.error('导出失败，请重试');
    }
  }

  /**
   * 回滚到指定版本
   */
  function rollbackToSnapshot(snapshot: any) {
    Modal.confirm({
      title: '确认回滚',
      icon: ExclamationCircleOutlined,
      content: `确定要回滚到版本 v${snapshot.versionNumber} 吗？此操作不可撤销。`,
      okText: '确认回滚',
      cancelText: '取消',
      onOk() {
        // 🎯 版本回滚功能实现
        message.info('回滚功能开发中...');
      },
    });
  }

  /**
   * 下载版本报告
   */
  function downloadVersionReport(snapshot: any) {
    // 🎯 下载版本报告功能实现
    message.info('下载报告功能开发中...');
  }

  /**
   * 判断是否可以回滚
   */
  function canRollback(snapshot: any): boolean {
    // 只有当前用户有权限且不是最新版本时才可以回滚
    return snapshot.versionNumber < latestVersion.value;
  }

  /**
   * 获取时间线颜色
   */
  function getTimelineColor(nodeCode: string): string {
    const colorMap = {
      start: 'green',
      submit: 'blue',
      approve: 'green',
      reject: 'red',
      complete: 'purple',
    };
    return colorMap[nodeCode] || 'blue';
  }

  /**
   * 获取节点头像样式
   */
  function getNodeAvatarStyle(nodeCode: string) {
    const styleMap = {
      start: { backgroundColor: '#52c41a', color: 'white' },
      submit: { backgroundColor: '#1890ff', color: 'white' },
      approve: { backgroundColor: '#52c41a', color: 'white' },
      reject: { backgroundColor: '#ff4d4f', color: 'white' },
      complete: { backgroundColor: '#722ed1', color: 'white' },
    };
    return styleMap[nodeCode] || { backgroundColor: '#1890ff', color: 'white' };
  }

  /**
   * 获取节点图标
   */
  function getNodeIcon(nodeCode: string): string {
    const iconMap = {
      start: '始',
      submit: '提',
      approve: '批',
      reject: '拒',
      complete: '完',
    };
    return iconMap[nodeCode] || nodeCode.charAt(0).toUpperCase();
  }

  /**
   * 获取节点显示名称
   */
  function getNodeDisplayName(nodeCode: string): string {
    const nameMap = {
      start: '流程开始',
      submit: '提交申请',
      approve: '审批节点',
      reject: '拒绝节点',
      complete: '流程完成',
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
   * 获取重要字段
   */
  function getImportantFields(formData: any) {
    const importantFields = ['title', 'description', 'urgency_level', 'project_id'];
    const result = {};

    importantFields.forEach((field) => {
      if (formData && formData[field] !== undefined) {
        result[field] = formData[field];
      }
    });

    return result;
  }

  /**
   * 格式化字段值
   */
  function formatFieldValue(value: any): string {
    if (value === null || value === undefined) {
      return '未设置';
    }

    if (typeof value === 'object') {
      return JSON.stringify(value);
    }

    return String(value);
  }
</script>

<style lang="less" scoped>
  .version-history-timeline {
    .version-stats {
      .ant-statistic {
        text-align: center;
      }
    }

    .version-toolbar {
      border-bottom: 1px solid #f0f0f0;
      padding-bottom: 16px;
    }

    .timeline-content {
      .timeline-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 12px;

        h4 {
          margin: 0;
          font-size: 14px;
          font-weight: 600;
          color: rgba(0, 0, 0, 0.85);
          display: flex;
          align-items: center;
        }

        .timeline-time {
          font-size: 12px;
          color: rgba(0, 0, 0, 0.45);
          white-space: nowrap;
          margin-left: 12px;
        }
      }

      .timeline-meta {
        margin-bottom: 12px;

        .changed-fields {
          font-size: 12px;
          color: rgba(0, 0, 0, 0.65);
        }

        .more-fields {
          font-size: 12px;
          color: rgba(0, 0, 0, 0.45);
        }
      }

      .version-preview {
        background: #fafafa;
        border-radius: 6px;
        padding: 12px;
        margin: 12px 0;

        .field-changed {
          color: #fa8c16;
          font-weight: 500;
        }
      }

      .timeline-actions {
        margin-top: 8px;
      }
    }

    .selected-avatar {
      box-shadow: 0 0 0 2px #1890ff;
      transform: scale(1.1);
      transition: all 0.2s ease;
    }
  }

  :deep(.ant-timeline-item-content) {
    min-height: auto;
  }

  :deep(.ant-descriptions-item-label) {
    font-weight: 500;
    width: 80px;
  }

  :deep(.ant-checkbox) {
    .ant-checkbox-inner {
      border-radius: 2px;
    }
  }
</style>
