<template>
  <div class="smart-button-group">
    <!-- 基于现有JeecgBoot按钮系统的简单扩展 -->
    <a-space :size="size" :wrap="wrap">
      <a-button v-for="button in workflowButtons" :key="button.code" :type="button.type" :loading="button.loading" @click="handleButtonClick(button)">
        <template #icon v-if="button.icon">
          <component :is="button.icon" />
        </template>
        {{ button.text }}
      </a-button>
    </a-space>
  </div>
</template>

<script setup lang="ts">
  import { ref, computed, onMounted, watch } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { SaveOutlined, SendOutlined, CheckCircleOutlined, CloseCircleOutlined } from '@ant-design/icons-vue';
  import { defHttp } from '/@/utils/http/axios';
  import { useUserStoreWithOut } from '/@/store/modules/user';
  import { usePermission } from '/@/hooks/web/usePermission';

  // 定义组件属性 - 简化
  interface Props {
    formId: string; // 这里实际传的是 tableName（与后端Name模式一致）
    dataId?: string;
    taskId?: string;
    size?: 'small' | 'middle' | 'large';
    wrap?: boolean;
  }

  const props = withDefaults(defineProps<Props>(), {
    size: 'middle',
    wrap: true,
  });

  // 定义事件 - 简化
  const emit = defineEmits<{
    save: [];
    submit: [];
    approve: [];
    reject: [];
  }>();

  const workflowButtons = ref<any[]>([]);
  const userStore = useUserStoreWithOut();
  const { hasPermission } = usePermission();
  const paramsVisible = ref(false);
  const paramsSchema = ref<any[]>([]);
  const paramsModel = ref<Record<string, any>>({});
  const pendingButton = ref<any>(null);

  async function loadButtons() {
    try {
      const res: any = await defHttp.get({ url: '/workflow/onlineForm/smartButtons', params: { tableName: props.formId, dataId: props.dataId, taskId: props.taskId } });
      const list = res?.result || res; // 兼容不同返回包裹
      const mapped = (list || []).map((b: any) => ({
        ...b,
        loading: false,
        // icon 映射到实际组件
        icon: mapIcon(b.icon),
        actionType: (b.action || '').toUpperCase(),
      }));
      // 前端权限兜底（基于后端permission）
      const roles = userStore.getRoleList || [];
      const hasPerm = (perm?: string | string[]) => {
        if (!perm || (Array.isArray(perm) && perm.length === 0)) return true;
        return hasPermission(perm as any);
      };
      workflowButtons.value = mapped
        .filter((b: any) => hasPerm(b.permission))
        .sort((a: any, b: any) => (a.order || 0) - (b.order || 0));
      // 附加“认领/释放”在有任务时的兜底按钮（后端未下发但可按需显示）
      if (props.taskId) {
        const hasClaim = workflowButtons.value.some((x) => x.code === 'claim' || x.code === 'unclaim');
        if (!hasClaim) {
          workflowButtons.value.push({ id: 'claim', code: 'claim', text: '认领', type: 'default', icon: null, actionType: 'CLAIM', order: 90 });
          workflowButtons.value.push({ id: 'unclaim', code: 'unclaim', text: '释放', type: 'default', icon: null, actionType: 'UNCLAIM', order: 91 });
        }
      }
    } catch (e) {
      // 降级到默认按钮
      workflowButtons.value = getDefaultButtons();
    }
  }

  function mapIcon(name?: string) {
    if (!name) return null;
    switch (name) {
      case 'SaveOutlined': return SaveOutlined;
      case 'SendOutlined': return SendOutlined;
      case 'CheckCircleOutlined': return CheckCircleOutlined;
      case 'CloseCircleOutlined': return CloseCircleOutlined;
      default: return null;
    }
  }

  /**
   * 处理按钮点击
   */
  async function handleButtonClick(button: any) {
    // 确认消息
    if (button.confirmMessage) {
      const confirmed = await showConfirm(button.confirmMessage);
      if (!confirmed) return;
    }

    // 设置加载状态
    button.loading = true;

    try {
      // 若有参数Schema，先收集参数再发出事件
      if (Array.isArray(button.paramsSchema) && button.paramsSchema.length) {
        pendingButton.value = button;
        paramsSchema.value = button.paramsSchema;
        paramsModel.value = buildDefaultParams(button.paramsSchema);
        paramsVisible.value = true;
      } else {
        dispatchButton(button, {});
      }

      // 成功消息
      if (button.successMessage) {
        message.success(button.successMessage);
      }
    } catch (error) {
      console.error('按钮操作失败:', error);
      message.error('操作失败，请重试');
    } finally {
      button.loading = false;
    }
  }

  function buildDefaultParams(schema: any[]) {
    const model: Record<string, any> = {};
    schema.forEach((f: any) => {
      if (f && f.key) model[f.key] = f.defaultValue ?? '';
    });
    return model;
  }

  function dispatchButton(button: any, extraParams: Record<string, any>) {
    const payload = { ...(button.actionParams || {}), ...(extraParams || {}) };
    emit('buttonClick', button, button.actionType);
    switch (button.actionType) {
      case 'SAVE':
      case 'SAVE_DRAFT':
        emit('save', payload);
        break;
      case 'SUBMIT':
      case 'SUBMIT_REVIEW':
        emit('submit', payload);
        break;
      case 'APPROVE':
        emit('approve', payload);
        break;
      case 'REJECT':
        emit('reject', payload);
        break;
      case 'TRANSFER':
        emit('transfer', payload);
        break;
      case 'GO_BACK':
        emit('goBack', payload);
        break;
      case 'CLAIM':
        emit('claim', payload);
        break;
      case 'UNCLAIM':
        emit('unclaim', payload);
        break;
      case 'DELETE':
        emit('delete', payload);
        break;
      case 'CANCEL':
        emit('cancel');
        break;
      default:
        console.warn('未知的按钮动作类型:', button.actionType);
    }
  }

  /**
   * 显示确认对话框
   */
  function showConfirm(content: string): Promise<boolean> {
    return new Promise((resolve) => {
      Modal.confirm({
        title: '确认操作',
        content,
        okText: '确定',
        cancelText: '取消',
        onOk() {
          resolve(true);
        },
        onCancel() {
          resolve(false);
        },
      });
    });
  }

  /**
   * 获取默认按钮（兜底方案）
   */
  function getDefaultButtons() {
    const defaultButtons = [];

    // 保存按钮
    if (!props.taskId) {
      defaultButtons.push({
        id: 'save',
        text: '保存',
        type: 'default',
        icon: 'SaveOutlined',
        actionType: 'SAVE',
        sortOrder: 10,
        enabled: true,
        loading: false,
      });
    }

    // 提交按钮
    defaultButtons.push({
      id: 'submit',
      text: props.taskId ? '提交' : '提交审核',
      type: 'primary',
      icon: 'SendOutlined',
      actionType: props.taskId ? 'SUBMIT' : 'SUBMIT_REVIEW',
      sortOrder: 20,
      enabled: true,
      loading: false,
    });

    return defaultButtons;
  }

  /**
   * 刷新按钮配置
   */
  function refreshButtons() { loadButtons(); }

  /**
   * 设置按钮加载状态
   */
  function setButtonLoading(buttonId: string, loading: boolean) {
    const button = workflowButtons.value.find((btn) => btn.code === buttonId);
    if (button) {
      button.loading = loading;
    }
  }

  /**
   * 设置按钮启用状态
   */
  function setButtonEnabled(buttonId: string, enabled: boolean) {
    const button = workflowButtons.value.find((btn) => btn.code === buttonId);
    if (button) {
      // button.enabled = enabled; // 暂时注释，简化版没有enabled属性
    }
  }

  // 暴露方法给父组件
  defineExpose({
    refreshButtons,
    setButtonLoading,
    setButtonEnabled,
  });

  onMounted(loadButtons);

  watch(() => [props.formId, props.dataId, props.taskId], () => loadButtons());
</script>

<style lang="less" scoped>
  .smart-button-group {
    .ant-btn {
      min-width: 80px;

      &:not(:last-child) {
        margin-right: 8px;
      }

      // 按钮类型样式
      &.ant-btn-primary {
        box-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
      }

      &.ant-btn-success {
        background: #52c41a;
        border-color: #52c41a;
        color: white;

        &:hover {
          background: #73d13d;
          border-color: #73d13d;
        }
      }

      &.ant-btn-warning {
        background: #faad14;
        border-color: #faad14;
        color: white;

        &:hover {
          background: #ffc53d;
          border-color: #ffc53d;
        }
      }
    }
  }

  // 响应式布局
  @media (max-width: 768px) {
    .smart-button-group {
      .ant-btn {
        min-width: 70px;
        font-size: 12px;
      }
    }
  }
</style>
