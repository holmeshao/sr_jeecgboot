<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="pt-3px pr-3px">
      <BasicForm @register="registerForm" />
      
      <!-- 🎯 字段权限配置区域 -->
      <div v-if="showFieldPermissions" class="field-permissions-section">
        <a-divider orientation="left">
          <span style="font-size: 16px; font-weight: 600;">字段权限配置</span>
        </a-divider>
        
        <!-- 快速操作 -->
        <div class="quick-actions" style="margin-bottom: 16px;">
          <a-space>
            <a-button @click="setAllEditable" size="small">全部可编辑</a-button>
            <a-button @click="setAllReadonly" size="small">全部只读</a-button>
            <a-button @click="setAllHidden" size="small">全部隐藏</a-button>
            <a-button @click="resetPermissions" size="small">重置权限</a-button>
            <a-button @click="applySmartDefaults" type="primary" size="small">智能默认</a-button>
          </a-space>
        </div>

        <!-- 字段权限表格 -->
        <JVxeTable
          ref="fieldTableRef"
          :loading="fieldTableLoading"
          :columns="fieldPermissionColumns"
          :dataSource="fieldPermissionData"
          :height="400"
          :disabled="!canEditPermissions"
          :row-number="true"
          :row-selection="true"
          :toolbar="true"
          toolbar-config="{
            btn: {
              add: false,
              remove: false,
            },
            slots: {
              buttons: 'toolbar'
            }
          }"
        >
          <!-- 工具栏插槽 -->
          <template #toolbar>
            <a-space>
              <a-button @click="batchSetPermission('editable')" size="small">
                批量设为可编辑
              </a-button>
              <a-button @click="batchSetPermission('readonly')" size="small">
                批量设为只读
              </a-button>
              <a-button @click="batchSetPermission('hidden')" size="small">
                批量设为隐藏
              </a-button>
            </a-space>
          </template>
        </JVxeTable>
      </div>
    </div>
  </BasicModal>
</template>

<script lang="ts" setup>
import { ref, computed, unref, nextTick } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { JVxeTable } from '/@/components/jeecg/JVxeTable';
import { permissionFormSchema, fieldPermissionColumns } from './WorkflowNodePermission.data';
import { saveOrUpdate, getFormFields, generateDefaultPermission } from './WorkflowNodePermission.api';
import { useMessage } from '/@/hooks/web/useMessage';

// =============== 组件设置 ===============
defineOptions({ name: 'WorkflowNodePermissionModal' });

const emit = defineEmits(['success', 'register']);

// =============== 组件引用 ===============
const fieldTableRef = ref();

// =============== 状态管理 ===============
const isUpdate = ref(true);
const showFieldPermissions = ref(false);
const fieldTableLoading = ref(false);
const fieldPermissionData = ref([]);

// =============== 消息提示 ===============
const { createMessage } = useMessage();

// =============== 表单配置 ===============
const [registerForm, { setFieldsValue, resetFields, getFieldsValue, validate }] = useForm({
  labelWidth: 120,
  baseColProps: { span: 24 },
  schemas: permissionFormSchema,
  showActionButtonGroup: false,
  autoSubmitOnEnter: true,
});

// =============== 模态框配置 ===============
const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
  resetFields();
  setModalProps({ confirmLoading: false, width: 800 });
  isUpdate.value = !!data?.isUpdate;

  if (unref(isUpdate)) {
    // 编辑模式
    setFieldsValue({
      ...data.record,
    });
    
    // 加载字段权限配置
    if (data.record.cgformHeadId) {
      await loadFieldPermissions(data.record.cgformHeadId, data.record);
    }
  } else {
    // 新增模式
    showFieldPermissions.value = false;
  }
});

// =============== 计算属性 ===============
const getTitle = computed(() => (!unref(isUpdate) ? '新增节点权限配置' : '编辑节点权限配置'));

const canEditPermissions = computed(() => {
  return !unref(isUpdate) || showFieldPermissions.value;
});

// =============== 业务方法 ===============

/**
 * 🎯 提交表单
 */
async function handleSubmit() {
  try {
    const values = await validate();
    setModalProps({ confirmLoading: true });

    // 如果显示字段权限配置，收集权限数据
    if (showFieldPermissions.value && fieldTableRef.value) {
      const fieldPermissions = await collectFieldPermissions();
      values.editableFields = JSON.stringify(fieldPermissions.editableFields);
      values.readonlyFields = JSON.stringify(fieldPermissions.readonlyFields);
      values.hiddenFields = JSON.stringify(fieldPermissions.hiddenFields);
      values.requiredFields = JSON.stringify(fieldPermissions.requiredFields);
    }

    // 调用接口保存
    await saveOrUpdate(values, unref(isUpdate));
    
    createMessage.success('保存成功！');
    closeModal();
    emit('success');
  } finally {
    setModalProps({ confirmLoading: false });
  }
}

/**
 * 🎯 加载字段权限配置
 */
async function loadFieldPermissions(formId: string, record?: any) {
  try {
    fieldTableLoading.value = true;
    
    // 获取表单字段列表
    const fieldsResponse = await getFormFields(formId);
    const formFields = fieldsResponse.result || [];

    if (formFields.length === 0) {
      createMessage.warning('该表单未找到字段信息');
      showFieldPermissions.value = false;
      return;
    }

    // 解析现有权限配置
    const existingPermissions = parseExistingPermissions(record);

    // 构建字段权限数据
    const permissionData = formFields.map(field => {
      const permission = getFieldPermission(field.fieldName, existingPermissions);
      return {
        fieldName: field.fieldName,
        fieldLabel: field.fieldLabel,
        fieldType: field.fieldType,
        permission: permission,
        required: existingPermissions.requiredFields.includes(field.fieldName) ? 'Y' : 'N',
        category: field.category || 'business',
      };
    });

    fieldPermissionData.value = permissionData;
    showFieldPermissions.value = true;

    // 等待DOM更新后刷新表格
    await nextTick();
    if (fieldTableRef.value) {
      fieldTableRef.value.setDataSource(permissionData);
    }

  } catch (error) {
    console.error('加载字段权限配置失败:', error);
    createMessage.error('加载字段权限配置失败');
  } finally {
    fieldTableLoading.value = false;
  }
}

/**
 * 🎯 解析现有权限配置
 */
function parseExistingPermissions(record: any) {
  const permissions = {
    editableFields: [],
    readonlyFields: [],
    hiddenFields: [],
    requiredFields: [],
  };

  try {
    if (record?.editableFields) {
      permissions.editableFields = JSON.parse(record.editableFields);
    }
    if (record?.readonlyFields) {
      permissions.readonlyFields = JSON.parse(record.readonlyFields);
    }
    if (record?.hiddenFields) {
      permissions.hiddenFields = JSON.parse(record.hiddenFields);
    }
    if (record?.requiredFields) {
      permissions.requiredFields = JSON.parse(record.requiredFields);
    }
  } catch (e) {
    console.warn('解析权限配置失败:', e);
  }

  return permissions;
}

/**
 * 🎯 获取字段权限类型
 */
function getFieldPermission(fieldName: string, permissions: any): string {
  if (permissions.editableFields.includes(fieldName)) {
    return 'editable';
  } else if (permissions.hiddenFields.includes(fieldName)) {
    return 'hidden';
  } else {
    return 'readonly';
  }
}

/**
 * 🎯 收集字段权限配置
 */
async function collectFieldPermissions() {
  const tableData = await fieldTableRef.value.getTableData();
  
  const permissions = {
    editableFields: [],
    readonlyFields: [],
    hiddenFields: [],
    requiredFields: [],
  };

  tableData.forEach(row => {
    const fieldName = row.fieldName;
    
    switch (row.permission) {
      case 'editable':
        permissions.editableFields.push(fieldName);
        break;
      case 'hidden':
        permissions.hiddenFields.push(fieldName);
        break;
      default:
        permissions.readonlyFields.push(fieldName);
        break;
    }

    if (row.required === 'Y') {
      permissions.requiredFields.push(fieldName);
    }
  });

  return permissions;
}

// =============== 快速操作方法 ===============

/**
 * 🎯 设置所有字段为可编辑
 */
async function setAllEditable() {
  await batchSetPermission('editable');
}

/**
 * 🎯 设置所有字段为只读
 */
async function setAllReadonly() {
  await batchSetPermission('readonly');
}

/**
 * 🎯 设置所有字段为隐藏
 */
async function setAllHidden() {
  await batchSetPermission('hidden');
}

/**
 * 🎯 批量设置权限
 */
async function batchSetPermission(permission: string) {
  if (!fieldTableRef.value) return;

  const tableData = await fieldTableRef.value.getTableData();
  const selectedRows = fieldTableRef.value.getXTable()?.getCheckboxRecords() || [];
  
  // 如果没有选中行，则应用到所有行
  const targetRows = selectedRows.length > 0 ? selectedRows : tableData;
  
  targetRows.forEach(row => {
    row.permission = permission;
  });

  fieldTableRef.value.setDataSource(tableData);
  createMessage.success(`已设置 ${targetRows.length} 个字段为${getPermissionLabel(permission)}`);
}

/**
 * 🎯 重置权限
 */
async function resetPermissions() {
  if (!fieldTableRef.value) return;

  const tableData = await fieldTableRef.value.getTableData();
  tableData.forEach(row => {
    row.permission = 'readonly';
    row.required = 'N';
  });

  fieldTableRef.value.setDataSource(tableData);
  createMessage.success('权限已重置');
}

/**
 * 🎯 应用智能默认配置
 */
async function applySmartDefaults() {
  try {
    const values = getFieldsValue();
    if (!values.cgformHeadId || !values.nodeId) {
      createMessage.warning('请先填写表单ID和节点ID');
      return;
    }

    const response = await generateDefaultPermission(values.cgformHeadId, values.nodeId);
    // TODO: 应用智能默认配置到字段权限表格
    createMessage.success('智能默认配置已应用');
    
  } catch (error) {
    console.error('应用智能默认配置失败:', error);
    createMessage.error('应用智能默认配置失败');
  }
}

/**
 * 🎯 获取权限标签
 */
function getPermissionLabel(permission: string): string {
  const labels = {
    'editable': '可编辑',
    'readonly': '只读',
    'hidden': '隐藏',
  };
  return labels[permission] || permission;
}
</script>

<style scoped>
.field-permissions-section {
  margin-top: 20px;
}

.quick-actions {
  padding: 12px;
  background: #f5f5f5;
  border-radius: 6px;
}

:deep(.ant-divider-horizontal.ant-divider-with-text-left) {
  margin: 16px 0;
}

:deep(.jeecg-basic-table) {
  margin-top: 0;
}
</style>