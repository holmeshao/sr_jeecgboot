import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

/**
 * 🎯 工作流节点权限配置 - 列定义
 * 基于JeecgBoot标准表格配置
 */
export const columns: BasicColumn[] = [
  {
    title: '表单ID',
    align: 'center',
    dataIndex: 'cgformHeadId',
    width: 120,
  },
  {
    title: '流程定义Key',
    align: 'center',
    dataIndex: 'processDefinitionKey',
    width: 150,
  },
  {
    title: '节点ID',
    align: 'center',
    dataIndex: 'nodeId',
    width: 120,
  },
  {
    title: '节点名称',
    align: 'center',
    dataIndex: 'nodeName',
    width: 120,
  },
  {
    title: '表单模式',
    align: 'center',
    dataIndex: 'formMode',
    width: 80,
    customRender: ({ text }) => {
      const color = getFormModeColor(text);
      const label = getFormModeLabel(text);
      return h(Tag, { color }, () => label);
    },
  },
  {
    title: '权限摘要',
    align: 'center',
    dataIndex: 'permissionSummary',
    width: 200,
    customRender: ({ record }) => {
      return h('div', { style: 'max-width: 200px;' }, [
        renderPermissionSummary(record),
      ]);
    },
  },
  {
    title: '排序',
    align: 'center',
    dataIndex: 'sortOrder',
    width: 60,
  },
  {
    title: '状态',
    align: 'center',
    dataIndex: 'status',
    width: 80,
    customRender: ({ text }) => {
      const color = text === 1 ? 'green' : 'red';
      const label = text === 1 ? '启用' : '禁用';
      return h(Tag, { color }, () => label);
    },
  },
  {
    title: '创建时间',
    align: 'center',
    dataIndex: 'createTime',
    width: 120,
    customRender: ({ text }) => {
      return text ? new Date(text).toLocaleString() : '';
    },
  },
];

/**
 * 🎯 查询表单配置
 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '表单ID',
    field: 'cgformHeadId',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '流程定义Key',
    field: 'processDefinitionKey',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '节点ID',
    field: 'nodeId',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '节点名称',
    field: 'nodeName',
    component: 'Input',
    colProps: { span: 6 },
  },
];

/**
 * 🎯 权限配置表单Schema
 */
export const permissionFormSchema: FormSchema[] = [
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '表单ID',
    field: 'cgformHeadId',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入表单ID',
    },
  },
  {
    label: '流程定义Key',
    field: 'processDefinitionKey',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入流程定义Key',
    },
  },
  {
    label: '节点ID',
    field: 'nodeId',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入节点ID',
    },
  },
  {
    label: '节点名称',
    field: 'nodeName',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入节点名称',
    },
  },
  {
    label: '表单模式',
    field: 'formMode',
    component: 'Select',
    componentProps: {
      placeholder: '请选择表单模式',
      options: [
        { label: '只读模式', value: 'VIEW' },
        { label: '编辑模式', value: 'EDIT' },
        { label: '操作模式', value: 'OPERATE' },
      ],
    },
    defaultValue: 'VIEW',
  },
  {
    label: '排序',
    field: 'sortOrder',
    component: 'InputNumber',
    componentProps: {
      placeholder: '请输入排序号',
      min: 0,
      max: 9999,
    },
    defaultValue: 100,
  },
  {
    label: '状态',
    field: 'status',
    component: 'RadioButtonGroup',
    componentProps: {
      options: [
        { label: '启用', value: 1 },
        { label: '禁用', value: 0 },
      ],
    },
    defaultValue: 1,
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入备注',
      rows: 3,
    },
  },
];

/**
 * 🎯 字段权限配置列定义（用于JVxeTable）
 */
export const fieldPermissionColumns = [
  {
    title: '字段名称',
    key: 'fieldName',
    type: 'input',
    width: '150px',
    placeholder: '字段名称',
    validateRules: [{ required: true, message: '请输入字段名称' }],
  },
  {
    title: '字段标签',
    key: 'fieldLabel',
    type: 'input',
    width: '150px',
    placeholder: '字段标签',
  },
  {
    title: '字段类型',
    key: 'fieldType',
    type: 'input',
    width: '100px',
    placeholder: '字段类型',
  },
  {
    title: '权限设置',
    key: 'permission',
    type: 'select',
    width: '120px',
    placeholder: '请选择权限',
    options: [
      { value: 'editable', text: '可编辑' },
      { value: 'readonly', text: '只读' },
      { value: 'hidden', text: '隐藏' },
    ],
    defaultValue: 'readonly',
  },
  {
    title: '是否必填',
    key: 'required',
    type: 'checkbox',
    width: '80px',
    customValue: ['Y', 'N'],
    defaultChecked: false,
  },
  {
    title: '字段分类',
    key: 'category',
    type: 'select',
    width: '100px',
    options: [
      { value: 'business', text: '业务字段' },
      { value: 'workflow', text: '流程字段' },
      { value: 'system', text: '系统字段' },
      { value: 'file', text: '文件字段' },
    ],
    defaultValue: 'business',
  },
];

// =============== 辅助函数 ===============

/**
 * 获取表单模式颜色
 */
function getFormModeColor(mode: string): string {
  switch (mode) {
    case 'VIEW':
      return 'blue';
    case 'EDIT':
      return 'green';
    case 'OPERATE':
      return 'orange';
    default:
      return 'default';
  }
}

/**
 * 获取表单模式标签
 */
function getFormModeLabel(mode: string): string {
  switch (mode) {
    case 'VIEW':
      return '只读';
    case 'EDIT':
      return '编辑';
    case 'OPERATE':
      return '操作';
    default:
      return mode || '';
  }
}

/**
 * 渲染权限摘要
 */
function renderPermissionSummary(record: any) {
  const summary = [];
  
  // 可编辑字段
  const editableCount = getFieldCount(record.editableFields);
  if (editableCount > 0) {
    summary.push(
      h(Tag, { color: 'green', size: 'small', style: 'margin: 2px' }, () => `可编辑: ${editableCount}`)
    );
  }

  // 只读字段
  const readonlyCount = getFieldCount(record.readonlyFields);
  if (readonlyCount > 0) {
    summary.push(
      h(Tag, { color: 'orange', size: 'small', style: 'margin: 2px' }, () => `只读: ${readonlyCount}`)
    );
  }

  // 隐藏字段
  const hiddenCount = getFieldCount(record.hiddenFields);
  if (hiddenCount > 0) {
    summary.push(
      h(Tag, { color: 'red', size: 'small', style: 'margin: 2px' }, () => `隐藏: ${hiddenCount}`)
    );
  }

  // 必填字段
  const requiredCount = getFieldCount(record.requiredFields);
  if (requiredCount > 0) {
    summary.push(
      h(Tag, { color: 'purple', size: 'small', style: 'margin: 2px' }, () => `必填: ${requiredCount}`)
    );
  }

  return h('div', { style: 'display: flex; flex-wrap: wrap;' }, summary);
}

/**
 * 获取字段数量
 */
function getFieldCount(fieldsJson: string): number {
  if (!fieldsJson) return 0;
  try {
    const fields = JSON.parse(fieldsJson);
    return Array.isArray(fields) ? fields.length : 0;
  } catch (e) {
    return 0;
  }
}

/**
 * 🎯 权限配置器的节点列定义
 */
export const nodeListColumns: BasicColumn[] = [
  {
    title: '节点ID',
    dataIndex: 'nodeId',
    width: 120,
  },
  {
    title: '节点名称',
    dataIndex: 'nodeName',
    width: 150,
  },
  {
    title: '节点类型',
    dataIndex: 'nodeType',
    width: 100,
    customRender: ({ text }) => {
      const typeMap = {
        'startEvent': '开始事件',
        'userTask': '用户任务',
        'endEvent': '结束事件',
        'exclusiveGateway': '排他网关',
        'parallelGateway': '并行网关',
      };
      return typeMap[text] || text;
    },
  },
  {
    title: '配置状态',
    dataIndex: 'configStatus',
    width: 100,
    customRender: ({ text }) => {
      if (text === 'configured') {
        return h(Tag, { color: 'green' }, () => '已配置');
      } else if (text === 'default') {
        return h(Tag, { color: 'blue' }, () => '默认策略');
      } else {
        return h(Tag, { color: 'default' }, () => '未配置');
      }
    },
  },
  {
    title: '字段数量',
    dataIndex: 'fieldCount',
    width: 80,
    align: 'center',
  },
];