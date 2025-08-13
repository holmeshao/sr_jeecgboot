<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" :width="1200" :min-height="600" @ok="handleSubmit" destroyOnClose>
    <div class="permission-config-container">
      <a-row :gutter="16">
        <!-- 左侧：基本信息表单 -->
        <a-col :span="8">
          <div class="config-section">
            <h4 class="section-title">
              <Icon icon="ant-design:setting-outlined" />
              基本配置
            </h4>
            <BasicForm @register="registerForm" />
          </div>
        </a-col>

        <!-- 右侧：权限配置 -->
        <a-col :span="16">
          <div class="config-section">
            <h4 class="section-title">
              <Icon icon="ant-design:lock-outlined" />
              字段权限配置
              <a-button type="link" size="small" @click="loadFormFields" :loading="loadingFields">
                <Icon icon="ant-design:reload-outlined" />
                重新加载字段
              </a-button>
            </h4>

            <!-- 权限配置工具栏 -->
            <div class="permission-toolbar">
              <a-space>
                <a-button size="small" @click="handleBatchSet('editable', true)">
                  <Icon icon="ant-design:edit-outlined" />
                  全部可编辑
                </a-button>
                <a-button size="small" @click="handleBatchSet('readonly', true)">
                  <Icon icon="ant-design:eye-outlined" />
                  全部只读
                </a-button>
                <a-button size="small" @click="handleBatchSet('required', true)">
                  <Icon icon="ant-design:exclamation-circle-outlined" />
                  全部必填
                </a-button>
                <a-button size="small" @click="handleClearAll">
                  <Icon icon="ant-design:clear-outlined" />
                  清空权限
                </a-button>
              </a-space>
            </div>

            <!-- 字段权限表格 -->
            <div class="field-permission-table">
              <JVxeTable
                ref="fieldPermissionTable"
                :loading="loadingFields"
                :columns="fieldPermissionColumns"
                :dataSource="fieldPermissionData"
                :max-height="400"
                :toolbar="true"
                :row-number="true"
                :row-selection="true"
                size="small"
                keep-source
              />
            </div>

            <!-- 权限预览 -->
            <div class="permission-preview" v-if="permissionSummary">
              <h5>权限配置预览：</h5>
              <a-space>
                <a-tag color="blue">可编辑: {{ permissionSummary.editableCount }}个</a-tag>
                <a-tag color="orange">只读: {{ permissionSummary.readonlyCount }}个</a-tag>
                <a-tag color="red">隐藏: {{ permissionSummary.hiddenCount }}个</a-tag>
                <a-tag color="purple">必填: {{ permissionSummary.requiredCount }}个</a-tag>
                <a-tag color="gray">总计: {{ permissionSummary.totalCount }}个字段</a-tag>
              </a-space>
            </div>
          </div>
        </a-col>
      </a-row>
    </div>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref, computed, unref, nextTick, watch } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { JVxeTable } from '/@/components/jeecg/JVxeTable';
  import { Icon } from '/@/components/Icon';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { permissionFormSchema, fieldPermissionColumns } from '../WorkflowNodePermission.data';
  import {
    saveFieldPermissions,
    loadFormFieldsForPermission,
    validatePermissionConfig,
    generatePermissionPreview,
  } from '../WorkflowNodePermission.api';

  // 组件名称
  defineOptions({ name: 'WorkflowNodePermissionModal' });

  // 事件
  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();

  // 状态
  const isUpdate = ref(true);
  const loadingFields = ref(false);
  const fieldPermissionData = ref<any[]>([]);
  const fieldPermissionTable = ref();

  // 弹窗
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    // 重置表单
    resetFields();
    setModalProps({ confirmLoading: false });

    isUpdate.value = !!data?.isUpdate;

    if (unref(isUpdate)) {
      // 编辑模式：设置表单数据
      await setFieldsValue({
        ...data.record,
      });

      // 加载字段权限配置
      if (data.record.cgformHeadId && data.record.nodeId) {
        await loadFormFields(data.record.cgformHeadId, data.record.nodeId);
      }
    }
  });

  // 表单
  const [registerForm, { resetFields, setFieldsValue, validate, getFieldsValue }] = useForm({
    labelWidth: 100,
    schemas: permissionFormSchema,
    showActionButtonGroup: false,
    autoSubmitOnEnter: true,
  });

  // 计算属性
  const getTitle = computed(() => (!unref(isUpdate) ? '新增节点权限' : '编辑节点权限'));

  const permissionSummary = computed(() => {
    if (fieldPermissionData.value.length === 0) return null;
    return generatePermissionPreview(fieldPermissionData.value);
  });

  // 监听表单变化，自动加载字段
  watch(
    () => getFieldsValue(),
    async (values) => {
      if (values.cgformHeadId && !unref(isUpdate)) {
        await loadFormFields(values.cgformHeadId);
      }
    },
    { deep: true }
  );

  /**
   * 🎯 加载表单字段权限配置
   */
  async function loadFormFields(formId?: string, nodeId?: string) {
    const formValues = getFieldsValue();
    const targetFormId = formId || formValues.cgformHeadId;
    const targetNodeId = nodeId || formValues.nodeId;

    if (!targetFormId) {
      createMessage.warning('请先选择表单');
      return;
    }

    try {
      loadingFields.value = true;

      // 调用API加载字段权限配置
      const fields = await loadFormFieldsForPermission(targetFormId, targetNodeId);

      // 设置表格数据
      fieldPermissionData.value = fields;

      // 刷新表格
      await nextTick();
      if (fieldPermissionTable.value) {
        fieldPermissionTable.value.setTableData(fields);
      }

      createMessage.success(`成功加载 ${fields.length} 个字段的权限配置`);
    } catch (error) {
      console.error('加载字段权限失败:', error);
      createMessage.error('加载字段权限失败');
    } finally {
      loadingFields.value = false;
    }
  }

  /**
   * 🎯 批量设置权限
   */
  function handleBatchSet(permissionType: string, value: boolean) {
    const selectedRows = fieldPermissionTable.value?.getSelectedRows() || [];

    if (selectedRows.length === 0) {
      createMessage.warning('请先选择要设置的字段');
      return;
    }

    selectedRows.forEach((row: any) => {
      // 清空其他权限（互斥设置）
      if (permissionType === 'editable' && value) {
        row.readonly = false;
        row.hidden = false;
      } else if (permissionType === 'readonly' && value) {
        row.editable = false;
        row.hidden = false;
      } else if (permissionType === 'hidden' && value) {
        row.editable = false;
        row.readonly = false;
        row.required = false;
      }

      row[permissionType] = value;
    });

    // 刷新表格
    fieldPermissionTable.value?.updateTableData(selectedRows);

    createMessage.success(`批量设置 ${selectedRows.length} 个字段的权限成功`);
  }

  /**
   * 🎯 清空所有权限
   */
  function handleClearAll() {
    fieldPermissionData.value.forEach((field: any) => {
      field.editable = false;
      field.readonly = false;
      field.hidden = false;
      field.required = false;
    });

    // 刷新表格
    fieldPermissionTable.value?.setTableData(fieldPermissionData.value);

    createMessage.success('清空所有字段权限成功');
  }

  /**
   * 🎯 提交表单
   */
  async function handleSubmit() {
    try {
      // 1. 验证基本表单
      const values = await validate();

      // 2. 获取字段权限配置
      const fieldPermissions = fieldPermissionTable.value?.getTableData() || [];

      // 3. 验证权限配置
      const validation = validatePermissionConfig(fieldPermissions);
      if (!validation.valid) {
        createMessage.error(validation.message);
        return;
      }

      // 4. 准备提交数据
      const submitData = {
        ...values,
        fieldPermissions: fieldPermissions,
      };

      setModalProps({ confirmLoading: true });

      // 5. 保存数据
      await saveFieldPermissions(submitData);

      createMessage.success('保存成功！');
      closeModal();
      emit('success');
    } catch (error) {
      console.error('保存失败:', error);
      createMessage.error('保存失败，请重试');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>

<style lang="less" scoped>
  .permission-config-container {
    padding: 0;

    .config-section {
      border: 1px solid #f0f0f0;
      border-radius: 6px;
      padding: 16px;
      margin-bottom: 16px;

      .section-title {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-bottom: 16px;
        font-size: 14px;
        font-weight: 600;
        color: #333;

        .anticon {
          color: #1890ff;
        }
      }
    }

    .permission-toolbar {
      margin-bottom: 12px;
      padding: 8px;
      background: #fafafa;
      border-radius: 4px;
    }

    .field-permission-table {
      margin-bottom: 12px;

      :deep(.j-vxe-table) {
        .vxe-table--header {
          background: #f5f5f5;
        }

        .vxe-checkbox {
          transform: scale(0.8);
        }
      }
    }

    .permission-preview {
      padding: 12px;
      background: #f8f9fa;
      border-radius: 4px;
      border-left: 4px solid #1890ff;

      h5 {
        margin: 0 0 8px 0;
        font-size: 13px;
        color: #666;
      }

      .ant-tag {
        margin-bottom: 4px;
      }
    }
  }

  // 响应式设计
  @media (max-width: 1200px) {
    .permission-config-container {
      .ant-col:first-child {
        margin-bottom: 16px;
      }
    }
  }
</style>
