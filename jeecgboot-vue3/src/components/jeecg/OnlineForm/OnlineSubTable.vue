<template>
  <div class="online-sub-table">
    <div class="sub-table-header">
      <span class="table-title">{{ tableConfig.label }}</span>
      <a-space v-if="!readonly">
        <a-button type="primary" size="small" @click="handleAdd">
          <PlusOutlined />
          新增
        </a-button>
        <a-button v-if="selectedRowKeys.length > 0" danger size="small" @click="handleBatchDelete">
          <DeleteOutlined />
          删除选中
        </a-button>
      </a-space>
    </div>
    
    <a-table
      :columns="tableColumns"
      :dataSource="tableData"
      :row-selection="readonly ? null : rowSelection"
      :pagination="pagination"
      size="small"
      :scroll="{ x: 800 }"
    >
      <template #bodyCell="{ column, record, index }">
        <template v-if="column.key === 'action'">
          <a-space v-if="!readonly">
            <a-button type="link" size="small" @click="handleEdit(record, index)">
              编辑
            </a-button>
            <a-popconfirm title="确定删除?" @confirm="handleDelete(index)">
              <a-button type="link" danger size="small">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
        <template v-else>
          {{ formatCellValue(record[column.dataIndex], column) }}
        </template>
      </template>
    </a-table>
    
    <!-- 编辑弹窗 -->
    <a-modal
      v-model:open="editModalVisible"
      :title="editModalTitle"
      width="80%"
      :destroyOnClose="true"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
    >
      <a-form
        ref="modalFormRef"
        :model="editingRecord"
        :rules="formRules"
        layout="vertical"
      >
        <a-row :gutter="16">
          <a-col :span="12" v-for="field in editableFields" :key="field.key">
            <a-form-item
              :name="field.key"
              :label="field.label"
              :required="field.required"
            >
              <component
                :is="getFieldComponent(field)"
                v-model:value="editingRecord[field.key]"
                :placeholder="getFieldPlaceholder(field)"
                v-bind="getFieldProps(field)"
              />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch } from 'vue';
import { message } from 'ant-design-vue';
import { PlusOutlined, DeleteOutlined } from '@ant-design/icons-vue';

interface Props {
  tableConfig: any;
  parentData: Record<string, any>;
  readonly?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  readonly: false
});

const emit = defineEmits<{
  'data-change': [key: string, data: any[]];
}>();

// 响应式数据
const tableData = ref<any[]>([]);
const selectedRowKeys = ref<string[]>([]);
const editModalVisible = ref(false);
const editingRecord = reactive<Record<string, any>>({});
const editingIndex = ref(-1);
const modalFormRef = ref();

// 表格列配置
const tableColumns = computed(() => {
  const columns = props.tableConfig.fields?.map((field: any) => ({
    title: field.label,
    dataIndex: field.key,
    key: field.key,
    width: field.width || 120,
    ellipsis: true
  })) || [];
  
  // 添加操作列
  if (!props.readonly) {
    columns.push({
      title: '操作',
      key: 'action',
      width: 120,
      fixed: 'right'
    });
  }
  
  return columns;
});

// 可编辑字段
const editableFields = computed(() => {
  return props.tableConfig.fields || [];
});

// 表单规则
const formRules = computed(() => {
  const rules: Record<string, any> = {};
  
  editableFields.value.forEach((field: any) => {
    if (field.required) {
      rules[field.key] = [
        { required: true, message: `请输入${field.label}` }
      ];
    }
  });
  
  return rules;
});

// 行选择配置
const rowSelection = computed(() => ({
  selectedRowKeys: selectedRowKeys.value,
  onChange: (keys: string[]) => {
    selectedRowKeys.value = keys;
  }
}));

// 分页配置
const pagination = computed(() => ({
  pageSize: 10,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total: number) => `共 ${total} 条`
}));

// 弹窗标题
const editModalTitle = computed(() => {
  return editingIndex.value >= 0 ? '编辑记录' : '新增记录';
});

// 方法
const handleAdd = () => {
  editingIndex.value = -1;
  resetEditingRecord();
  editModalVisible.value = true;
};

const handleEdit = (record: any, index: number) => {
  editingIndex.value = index;
  Object.assign(editingRecord, { ...record });
  editModalVisible.value = true;
};

const handleDelete = (index: number) => {
  tableData.value.splice(index, 1);
  emitDataChange();
  message.success('删除成功');
};

const handleBatchDelete = () => {
  const newData = tableData.value.filter((_, index) => 
    !selectedRowKeys.value.includes(index.toString())
  );
  tableData.value = newData;
  selectedRowKeys.value = [];
  emitDataChange();
  message.success(`删除成功，共删除 ${selectedRowKeys.value.length} 条记录`);
};

const handleModalOk = async () => {
  try {
    await modalFormRef.value?.validate();
    
    if (editingIndex.value >= 0) {
      // 编辑
      Object.assign(tableData.value[editingIndex.value], editingRecord);
    } else {
      // 新增
      tableData.value.push({ ...editingRecord });
    }
    
    emitDataChange();
    editModalVisible.value = false;
    message.success('保存成功');
    
  } catch (error) {
    console.warn('表单验证失败:', error);
  }
};

const handleModalCancel = () => {
  editModalVisible.value = false;
  resetEditingRecord();
};

const resetEditingRecord = () => {
  Object.keys(editingRecord).forEach(key => {
    delete editingRecord[key];
  });
  
  // 设置默认值
  editableFields.value.forEach((field: any) => {
    if (field.defaultValue !== undefined) {
      editingRecord[field.key] = field.defaultValue;
    }
  });
};

const emitDataChange = () => {
  emit('data-change', props.tableConfig.key, [...tableData.value]);
};

const formatCellValue = (value: any, column: any): string => {
  if (value === null || value === undefined) return '';
  
  // 根据字段类型格式化显示
  const field = editableFields.value.find((f: any) => f.key === column.dataIndex);
  if (!field) return String(value);
  
  switch (field.type) {
    case 'date':
      return value ? new Date(value).toLocaleDateString() : '';
    case 'datetime':
      return value ? new Date(value).toLocaleString() : '';
    case 'select':
    case 'list':
      const option = field.options?.find((opt: any) => opt.value === value);
      return option ? option.label : String(value);
    case 'switch':
      return value ? '是' : '否';
    default:
      return String(value);
  }
};

const getFieldComponent = (field: any): string => {
  const componentMap: Record<string, string> = {
    'input': 'a-input',
    'textarea': 'a-textarea',
    'number': 'a-input-number',
    'select': 'a-select',
    'date': 'a-date-picker',
    'datetime': 'a-date-picker',
    'switch': 'a-switch'
  };
  
  return componentMap[field.type] || 'a-input';
};

const getFieldProps = (field: any) => {
  const props: Record<string, any> = {};
  
  switch (field.type) {
    case 'select':
    case 'list':
      props.options = field.options || [];
      break;
    case 'date':
      props.format = 'YYYY-MM-DD';
      props.valueFormat = 'YYYY-MM-DD';
      break;
    case 'datetime':
      props.format = 'YYYY-MM-DD HH:mm:ss';
      props.valueFormat = 'YYYY-MM-DD HH:mm:ss';
      props.showTime = true;
      break;
    case 'number':
      if (field.precision) {
        props.precision = field.precision;
      }
      break;
  }
  
  return props;
};

const getFieldPlaceholder = (field: any): string => {
  const prefixMap: Record<string, string> = {
    'input': '请输入',
    'textarea': '请输入',
    'number': '请输入',
    'select': '请选择',
    'date': '请选择',
    'datetime': '请选择'
  };
  
  const prefix = prefixMap[field.type] || '请输入';
  return `${prefix}${field.label}`;
};

// 初始化数据
const initTableData = (data: any[]) => {
  tableData.value = Array.isArray(data) ? [...data] : [];
};

// 监听父数据变化
watch(() => props.parentData[props.tableConfig.key], (newData) => {
  if (Array.isArray(newData)) {
    initTableData(newData);
  }
}, { immediate: true });

// 暴露方法
defineExpose({
  validate: () => Promise.resolve(true),
  getData: () => tableData.value,
  setData: initTableData
});
</script>

<style lang="less" scoped>
.online-sub-table {
  .sub-table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    
    .table-title {
      font-weight: 600;
      font-size: 16px;
    }
  }
  
  :deep(.ant-table-thead > tr > th) {
    background: #fafafa;
    font-weight: 600;
  }
  
  :deep(.ant-table-tbody > tr:hover > td) {
    background: #f5f5f5;
  }
}
</style>