<template>
  <div class="workflow-online-form">
    <a-spin :spinning="loading" tip="正在加载表单...">
      <!-- 表单容器 -->
      <div v-if="formLoaded" class="form-container" :class="formModeClass">
        <!-- 动态表单渲染区域 -->
        <a-form
          ref="formRef"
          :model="formData"
          :rules="formRules"
          layout="vertical"
          @finish="handleSubmit"
        >
          <!-- 根据schema动态渲染字段 -->
          <template v-for="field in visibleFields" :key="field.key">
            <a-form-item
              :name="field.key"
              :label="field.label"
              :required="isFieldRequired(field.key)"
              v-show="!isFieldHidden(field.key)"
            >
              <!-- 根据字段类型渲染不同组件 -->
              <component
                :is="getFieldComponent(field)"
                v-model:value="formData[field.key]"
                :disabled="isFieldReadonly(field.key)"
                :placeholder="getFieldPlaceholder(field)"
                v-bind="getFieldProps(field)"
                @change="handleFieldChange(field.key, $event)"
              />
              
              <!-- 字段权限提示 -->
              <div v-if="showPermissionTip && isFieldReadonly(field.key)" 
                   class="permission-tip">
                <ExclamationCircleOutlined />
                当前节点此字段为只读
              </div>
            </a-form-item>
          </template>
          
          <!-- 子表单支持 -->
          <template v-if="!isSingleTable && subTables.length > 0">
            <a-divider>关联信息</a-divider>
            <a-tabs v-model:activeKey="activeSubTab">
              <a-tab-pane 
                v-for="subTable in subTables" 
                :key="subTable.key"
                :tab="subTable.label"
              >
                <!-- 复用JeecgBoot现有的JVxeTable子表功能 -->
                <JVxeTable
                  :ref="el => setSubTableRef(subTable.key, el)"
                  :table-config="subTable"
                  :parent-data="formData"
                  :readonly="isSubTableReadonly(subTable.key)"
                  @data-change="handleSubTableChange"
                />
              </a-tab-pane>
            </a-tabs>
          </template>
        </a-form>
      </div>
      
      <!-- 表单操作区域 -->
      <div v-if="showActions && formLoaded" class="form-actions">
        <slot name="actions" :form-data="formData" :validate="validateForm" :submit="handleSubmit">
          <!-- 默认操作按钮 -->
          <a-space size="large">
            <a-button v-if="allowSave" @click="handleSave" :loading="saving">
              <SaveOutlined />
              保存草稿
            </a-button>
            <a-button 
              v-if="allowSubmit" 
              type="primary" 
              @click="handleSubmit"
              :loading="submitting"
            >
              <SendOutlined />
              {{ submitButtonText }}
            </a-button>
          </a-space>
        </slot>
      </div>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch, onMounted, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import { 
  ExclamationCircleOutlined, 
  SaveOutlined, 
  SendOutlined 
} from '@ant-design/icons-vue';
import { defHttp } from '/@/utils/http/axios';
// 复用JeecgBoot现有的JVxeTable子表功能，无需专门的OnlineSubTable组件
import { JVxeTable } from '/@/components/jeecg/JVxeTable';
import { 
  handleWorkflowError, 
  withErrorHandler, 
  validateRequiredFields,
  showSuccess,
  showError 
} from '/@/utils/workflow/errorHandler';
import { 
  useLazyFormConfig, 
  useLazyFormData,
  useLazyPermissionConfig 
} from '/@/utils/workflow/lazyLoader';
import { useWorkflowCache } from '/@/utils/workflow/cacheManager';
import { getNodePermissionConfig, applyPermissionToFormConfig } from '/@/api/workflow/permission';

// 组件props - 基于online-loader.vue扩展
interface Props {
  // === 继承online-loader的核心props ===
  table: string;                           // 表名
  dataId?: string;                        // 数据ID
  taskId?: string;                        // 任务ID
  edit?: boolean;                         // 编辑模式
  flowEdit?: boolean;                     // 流程编辑模式
  disabled?: boolean;                     // 禁用状态
  
  // === 工作流扩展props ===
  workflowMode?: 'CREATE' | 'EDIT' | 'VIEW' | 'OPERATE' | 'TRACK'; // 工作流模式
  nodeId?: string;                        // 当前节点ID
  processInstanceId?: string;             // 流程实例ID
  processDefinitionKey?: string;          // 流程定义Key
  
  // 🎯 字段权限配置（支持显式配置和智能默认）
  fieldPermissions?: {
    editableFields?: string[];
    readonlyFields?: string[];
    hiddenFields?: string[];
    requiredFields?: string[];
  };
  
  // 🎯 权限控制选项
  enablePermissionControl?: boolean;      // 是否启用权限控制
  useSmartDefaults?: boolean;             // 是否使用智能默认策略
  autoLoadPermissions?: boolean;          // 是否自动加载权限配置
  
  // 显示控制
  showActions?: boolean;                  // 是否显示操作按钮
  showPermissionTip?: boolean;            // 是否显示权限提示
  
  // 按钮控制
  allowSave?: boolean;                    // 允许保存
  allowSubmit?: boolean;                  // 允许提交
  submitButtonText?: string;              // 提交按钮文本
}

const props = withDefaults(defineProps<Props>(), {
  workflowMode: 'EDIT',
  fieldPermissions: () => ({}),
  showActions: true,
  showPermissionTip: true,
  allowSave: true,
  allowSubmit: true,
  submitButtonText: '提交',
  enablePermissionControl: true,
  useSmartDefaults: true,
  autoLoadPermissions: true
});

// 组件事件
const emit = defineEmits<{
  'data-change': [data: Record<string, any>];
  'field-change': [field: string, value: any];
  'submit': [data: Record<string, any>];
  'save': [data: Record<string, any>];
  'loaded': [config: any];
}>();

// 🎯 缓存和懒加载集成
const { cache } = useWorkflowCache();

// 🎯 创建懒加载refs
const tableRef = ref(props.table);
const formIdRef = ref('');
const dataIdRef = ref(props.dataId);
const nodeIdRef = ref(props.nodeId);

// 🎯 懒加载表单配置
const {
  data: lazyFormConfig,
  loading: configLoading,
  error: configError,
  execute: reloadConfig
} = useLazyFormConfig(tableRef);

// 🎯 懒加载表单数据
const {
  data: lazyFormData,
  loading: dataLoading,
  error: dataError,
  execute: reloadData
} = useLazyFormData(formIdRef, dataIdRef);

// 🎯 懒加载权限配置
const {
  data: lazyPermissionConfig,
  loading: permissionLoading,
  execute: reloadPermissions
} = useLazyPermissionConfig(nodeIdRef, formIdRef);

// 响应式数据
const loading = computed(() => configLoading.value || dataLoading.value || permissionLoading.value);
const saving = ref(false);
const submitting = ref(false);
const formLoaded = ref(false);

const formRef = ref();
const formData = reactive<Record<string, any>>({});
const formConfig = computed(() => lazyFormConfig.value);
const formFields = ref<any[]>([]);
const subTables = ref<any[]>([]);
const subTableRefs = ref<Record<string, any>>({});

const activeSubTab = ref('');
const isSingleTable = ref(true);

// 🎯 权限控制相关状态
const permissionConfig = ref<any>({});
const permissionApplied = ref(false);
const permissionError = ref<string>('');

// 🎯 合并后的权限配置（props权限 + 动态加载权限）
const finalPermissionConfig = computed(() => {
  const dynamicPermissions = lazyPermissionConfig.value || {};
  const propsPermissions = props.fieldPermissions || {};
  
  return {
    editableFields: [
      ...(propsPermissions.editableFields || []),
      ...(dynamicPermissions.editableFields || [])
    ],
    readonlyFields: [
      ...(propsPermissions.readonlyFields || []),
      ...(dynamicPermissions.readonlyFields || [])
    ],
    hiddenFields: [
      ...(propsPermissions.hiddenFields || []),
      ...(dynamicPermissions.hiddenFields || [])
    ],
    requiredFields: [
      ...(propsPermissions.requiredFields || []),
      ...(dynamicPermissions.requiredFields || [])
    ]
  };
});

// 表单规则
const formRules = computed(() => {
  const rules: Record<string, any> = {};
  
  formFields.value.forEach(field => {
    const fieldRules: any[] = [];
    
    // 必填验证
    if (isFieldRequired(field.key)) {
      fieldRules.push({
        required: true,
        message: `请输入${field.label}`
      });
    }
    
    // 字段类型验证
    if (field.fieldValidType) {
      fieldRules.push({
        pattern: getValidationPattern(field.fieldValidType),
        message: `${field.label}格式不正确`
      });
    }
    
    if (fieldRules.length > 0) {
      rules[field.key] = fieldRules;
    }
  });
  
  return rules;
});

// 可见字段
const visibleFields = computed(() => {
  return formFields.value.filter(field => !isFieldHidden(field.key));
});

// 表单模式样式类
const formModeClass = computed(() => {
  return {
    'form-readonly': props.workflowMode === 'VIEW' || props.workflowMode === 'TRACK',
    'form-disabled': props.disabled,
    'workflow-mode': !!props.taskId
  };
});

// 🎯 字段权限判断方法（集成智能权限策略）
const isFieldReadonly = (fieldKey: string): boolean => {
  // 全局只读模式
  if (props.workflowMode === 'VIEW' || props.workflowMode === 'TRACK' || props.disabled) {
    return true;
  }
  
  // 检查是否在可编辑字段列表中
  const isEditable = finalPermissionConfig.value.editableFields?.includes(fieldKey);
  if (isEditable) {
    return false;
  }
  
  // 检查是否在只读字段列表中
  const isReadonly = finalPermissionConfig.value.readonlyFields?.includes(fieldKey);
  if (isReadonly) {
    return true;
  }
  
  // 默认根据工作流模式决定
  return props.workflowMode !== 'CREATE' && props.workflowMode !== 'EDIT';
};

const isFieldHidden = (fieldKey: string): boolean => {
  return finalPermissionConfig.value.hiddenFields?.includes(fieldKey) || false;
};

const isFieldRequired = (fieldKey: string): boolean => {
  // 原始字段必填 + 工作流权限必填
  const field = formFields.value.find(f => f.key === fieldKey);
  const originalRequired = field?.isRequired || field?.formSchema?.require;
  const workflowRequired = finalPermissionConfig.value.requiredFields?.includes(fieldKey);
  
  return originalRequired || workflowRequired || false;
};

const isFieldEditable = (fieldKey: string): boolean => {
  // 如果字段隐藏，则不可编辑
  if (isFieldHidden(fieldKey)) {
    return false;
  }
  
  // 如果字段只读，则不可编辑
  if (isFieldReadonly(fieldKey)) {
    return false;
  }
  
  // 检查是否在可编辑字段列表中
  return finalPermissionConfig.value.editableFields?.includes(fieldKey) || 
         (props.workflowMode === 'CREATE' || props.workflowMode === 'EDIT');
};

const isSubTableReadonly = (subTableKey: string): boolean => {
  return isFieldReadonly(subTableKey);
};

// 字段组件渲染
const getFieldComponent = (field: any) => {
  // 根据JeecgBoot字段类型映射到Ant Design Vue组件
  const componentMap: Record<string, string> = {
    'input': 'a-input',
    'textarea': 'a-textarea',
    'password': 'a-input-password',
    'number': 'a-input-number',
    'select': 'a-select',
    'radio': 'a-radio-group',
    'checkbox': 'a-checkbox-group',
    'date': 'a-date-picker',
    'datetime': 'a-date-picker',
    'time': 'a-time-picker',
    'switch': 'a-switch',
    'upload': 'a-upload',
    'editor': 'a-textarea', // 富文本编辑器降级
    'file': 'a-upload',
    'image': 'a-upload',
    'popup': 'a-input', // 弹出选择降级
    'list': 'a-select',
    'list_multi': 'a-select',
    'cat_tree': 'a-tree-select',
    'sel_depart': 'a-select',
    'sel_user': 'a-select'
  };
  
  return componentMap[field.type] || 'a-input';
};

const getFieldProps = (field: any) => {
  const props: Record<string, any> = {};
  
  // 通用属性
  if (field.fieldLength) {
    props.maxlength = field.fieldLength;
  }
  
  // 特定组件属性
  switch (field.type) {
    case 'textarea':
      props.rows = 4;
      props.showCount = true;
      break;
    case 'number':
      if (field.fieldPointLength) {
        props.precision = field.fieldPointLength;
      }
      break;
    case 'select':
    case 'list':
      props.options = field.dictOptions || [];
      props.placeholder = `请选择${field.label}`;
      break;
    case 'list_multi':
      props.mode = 'multiple';
      props.options = field.dictOptions || [];
      props.placeholder = `请选择${field.label}`;
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
    case 'upload':
    case 'file':
    case 'image':
      props.action = '/sys/common/upload';
      props.listType = field.type === 'image' ? 'picture-card' : 'text';
      break;
  }
  
  return props;
};

const getFieldPlaceholder = (field: any): string => {
  if (field.placeholder) return field.placeholder;
  
  const prefixMap: Record<string, string> = {
    'input': '请输入',
    'textarea': '请输入',
    'number': '请输入',
    'select': '请选择',
    'date': '请选择',
    'datetime': '请选择',
    'time': '请选择'
  };
  
  const prefix = prefixMap[field.type] || '请输入';
  return `${prefix}${field.label}`;
};

// 数据处理方法
const setSubTableRef = (key: string, el: any) => {
  if (el) {
    subTableRefs.value[key] = el;
  }
};

const handleFieldChange = (fieldKey: string, value: any) => {
  formData[fieldKey] = value;
  emit('field-change', fieldKey, value);
  emit('data-change', { ...formData });
};

const handleSubTableChange = (subTableKey: string, data: any[]) => {
  formData[subTableKey] = data;
  emit('data-change', { ...formData });
};

// 表单操作方法
const validateForm = async (): Promise<boolean> => {
  try {
    await formRef.value?.validate();
    
    // 验证子表
    for (const subTable of subTables.value) {
      const subRef = subTableRefs.value[subTable.key];
      if (subRef && typeof subRef.validate === 'function') {
        await subRef.validate();
      }
    }
    
    return true;
  } catch (error) {
    console.warn('表单验证失败:', error);
    return false;
  }
};

const handleSave = async () => {
  if (saving.value) return;
  
  saving.value = true;
  
  const result = await withErrorHandler(async () => {
    // 调用JeecgBoot原生保存API
    const saveData = await saveFormData(false);
    showSuccess('保存成功');
    emit('save', saveData);
    return saveData;
  }, {
    showMessage: true,
    title: '保存表单'
  });
  
  saving.value = false;
};

const handleSubmit = async () => {
  if (submitting.value) return;
  
  const isValid = await validateForm();
  if (!isValid) {
    showError('请检查表单填写，确保所有必填项已正确填写');
    return;
  }
  
  submitting.value = true;
  
  const result = await withErrorHandler(async () => {
    // 调用JeecgBoot原生提交API
    const submitData = await saveFormData(true);
    showSuccess('提交成功');
    emit('submit', submitData);
    return submitData;
  }, {
    showMessage: true,
    title: '提交表单'
  });
  
  submitting.value = false;
};

// 🎯 优化的表单配置加载方法（使用懒加载和缓存）
const loadFormConfig = async () => {
  const result = await withErrorHandler(async () => {
    // 先尝试从缓存获取
    const cachedConfig = cache.formConfig.get(props.table);
    if (cachedConfig) {
      processFormConfig(cachedConfig);
      return cachedConfig;
    }
    
    // 使用懒加载
    await reloadConfig();
    const config = lazyFormConfig.value;
    
    if (config && config.success) {
      processFormConfig(config.result);
      
      // 缓存结果
      cache.formConfig.set(props.table, config.result);
      
      return config.result;
    } else {
      throw new Error(config?.message || '加载表单配置失败');
    }
  }, {
    showMessage: true,
    title: '加载表单配置'
  });
};

// 🎯 处理表单配置的通用方法
const processFormConfig = (config: any) => {
  // 设置formId用于后续数据加载
  formIdRef.value = config.head?.id || '';
  
  // 解析表单字段
  if (config.schema) {
    formFields.value = Array.isArray(config.schema) ? config.schema : Object.values(config.schema);
  }
  
  // 解析子表
  if (config.head?.tableType !== 1 && config.subTableStr) {
    isSingleTable.value = false;
    subTables.value = JSON.parse(config.subTableStr || '[]');
    if (subTables.value.length > 0) {
      activeSubTab.value = subTables.value[0].key;
    }
  }
  
  formLoaded.value = true;
  emit('loaded', config);
  
  // 如果是编辑模式，加载数据
  if (props.dataId && (props.edit || props.flowEdit)) {
    loadFormData();
  }
  
  // 如果有节点权限需求，加载权限配置
  if (props.nodeId && formIdRef.value) {
    loadPermissionConfig();
  }
};

// 🎯 优化的表单数据加载方法（使用懒加载和缓存）
const loadFormData = async () => {
  if (!formIdRef.value || !props.dataId) return;
  
  const result = await withErrorHandler(async () => {
    // 先尝试从缓存获取
    const cachedData = cache.formData.get(formIdRef.value, props.dataId);
    if (cachedData) {
      Object.assign(formData, cachedData);
      emit('data-change', { ...formData });
      return cachedData;
    }
    
    // 更新dataId引用
    dataIdRef.value = props.dataId;
    
    // 使用懒加载
    await reloadData();
    const data = lazyFormData.value;
    
    if (data && data.success) {
      Object.assign(formData, data.result);
      
      // 缓存结果（数据缓存时间较短）
      cache.formData.set(formIdRef.value, props.dataId, data.result);
      
      emit('data-change', { ...formData });
      return data.result;
    } else {
      throw new Error(data?.message || '加载表单数据失败');
    }
  }, {
    showMessage: true,
    title: '加载表单数据'
  });
};

// 🎯 加载权限配置方法
const loadPermissionConfig = async () => {
  if (!nodeIdRef.value || !formIdRef.value) return;
  
  const result = await withErrorHandler(async () => {
    // 先尝试从缓存获取
    const cachedPermissions = cache.permissions.get(nodeIdRef.value, formIdRef.value);
    if (cachedPermissions) {
      applyFieldPermissions(cachedPermissions);
      return cachedPermissions;
    }
    
    // 使用懒加载
    await reloadPermissions();
    const permissions = lazyPermissionConfig.value;
    
    if (permissions && permissions.success) {
      // 缓存权限配置
      cache.permissions.set(nodeIdRef.value, formIdRef.value, permissions.result);
      
      applyFieldPermissions(permissions.result);
      return permissions.result;
    }
    
    return null;
  }, {
    showMessage: false, // 权限配置失败不显示错误消息
    title: '加载权限配置'
  });
};

// 🎯 应用字段权限
const applyFieldPermissions = (permissions: any) => {
  if (!permissions) return;
  
  try {
    const editableFields = JSON.parse(permissions.editableFields || '[]');
    const readonlyFields = JSON.parse(permissions.readonlyFields || '[]');
    const hiddenFields = JSON.parse(permissions.hiddenFields || '[]');
    const requiredFields = JSON.parse(permissions.requiredFields || '[]');
    
    // 更新字段权限状态
    formFields.value.forEach(field => {
      field.editable = editableFields.includes(field.key);
      field.readonly = readonlyFields.includes(field.key);
      field.hidden = hiddenFields.includes(field.key);
      field.required = requiredFields.includes(field.key) || field.required;
    });
    
    console.log('应用字段权限:', {
      editable: editableFields.length,
      readonly: readonlyFields.length,
      hidden: hiddenFields.length,
      required: requiredFields.length
    });
    
  } catch (error) {
    console.error('应用字段权限失败:', error);
  }
};

const saveFormData = async (isSubmit: boolean = false): Promise<any> => {
  if (!formConfig.value?.head?.id) {
    throw new Error('表单配置无效');
  }
  
  const isUpdate = !!props.dataId;
  let url = `/online/cgform/api/form/${formConfig.value.head.id}`;
  
  if (isUpdate) {
    url += `/${props.dataId}`;
  }
  
  // 处理子表数据
  const submitData = { ...formData };
  
  // 🎯 调用JeecgBoot现有API
  const response = await defHttp.request({
    url,
    method: isUpdate ? 'PUT' : 'POST',
    data: submitData
  });
  
  if (!response.success) {
    throw new Error(response.message || '保存失败');
  }
  
  return response.result;
};

// 工具方法
const getValidationPattern = (validType: string): RegExp => {
  const patterns: Record<string, RegExp> = {
    'email': /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
    'phone': /^1[3-9]\d{9}$/,
    'idcard': /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
    'url': /^https?:\/\/.+/,
    'number': /^\d+$/,
    'decimal': /^\d+(\.\d+)?$/
  };
  
  return patterns[validType] || /.*/;
};

// 监听props变化
watch(() => props.dataId, () => {
  if (formLoaded.value && props.dataId) {
    loadFormData();
  }
});

watch(() => props.table, () => {
  if (props.table) {
    loadFormConfig();
  }
});

// 组件挂载
// 🎯 初始化时加载表单配置和权限
onMounted(async () => {
  if (props.table) {
    await loadFormConfig();
    
    // 如果启用权限控制且有节点ID，加载权限配置
    if (props.enablePermissionControl && props.autoLoadPermissions && props.nodeId) {
      await loadPermissionConfig();
    }
  }
});

// 🎯 监听权限相关props变化
watch([() => props.nodeId, () => props.table, () => props.fieldPermissions], 
  async () => {
    if (props.enablePermissionControl && props.nodeId && formIdRef.value) {
      await loadPermissionConfig();
    }
  },
  { deep: true }
);

// 暴露方法给父组件
defineExpose({
  formData,
  validateForm,
  loadFormConfig,
  loadFormData,
  loadPermissionConfig,
  saveFormData,
  handleSubmit,
  handleSave,
  // 🎯 权限相关方法
  isFieldReadonly,
  isFieldHidden,
  isFieldRequired,
  isFieldEditable,
  finalPermissionConfig,
  applyFieldPermissions
});
</script>

<style lang="less" scoped>
.workflow-online-form {
  .form-container {
    &.form-readonly {
      :deep(.ant-input),
      :deep(.ant-select),
      :deep(.ant-date-picker),
      :deep(.ant-input-number) {
        border-color: #d9d9d9;
        background-color: #f5f5f5;
      }
    }
    
    &.form-disabled {
      opacity: 0.6;
      pointer-events: none;
    }
    
    &.workflow-mode {
      border: 1px solid #1890ff;
      border-radius: 6px;
      padding: 16px;
      background: #fafafa;
    }
  }
  
  .permission-tip {
    color: #faad14;
    font-size: 12px;
    margin-top: 4px;
    
    .anticon {
      margin-right: 4px;
    }
  }
  
  .form-actions {
    margin-top: 24px;
    padding-top: 16px;
    border-top: 1px solid #f0f0f0;
    text-align: center;
  }
  
  // 响应式布局
  @media (max-width: 768px) {
    .form-container.workflow-mode {
      padding: 12px;
    }
    
    .form-actions {
      :deep(.ant-space) {
        flex-direction: column;
        width: 100%;
        
        .ant-btn {
          width: 100%;
        }
      }
    }
  }
}
</style>