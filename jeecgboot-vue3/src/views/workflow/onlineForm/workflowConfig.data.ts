import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

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
    title: '启用工作流',
    align: 'center',
    dataIndex: 'workflowEnabled',
    width: 100,
    customRender: ({ record }) => {
      return record.workflowEnabled ? '是' : '否';
    },
  },
  {
    title: '启用版本控制',
    align: 'center',
    dataIndex: 'versionControlEnabled',
    width: 120,
    customRender: ({ record }) => {
      return record.versionControlEnabled ? '是' : '否';
    },
  },
  {
    title: '启用权限控制',
    align: 'center',
    dataIndex: 'permissionControlEnabled',
    width: 120,
    customRender: ({ record }) => {
      return record.permissionControlEnabled ? '是' : '否';
    },
  },
  {
    title: '业务主键字段',
    align: 'center',
    dataIndex: 'businessKeyField',
    width: 120,
  },
  {
    title: '状态字段',
    align: 'center',
    dataIndex: 'statusField',
    width: 100,
  },
  {
    title: '流程实例字段',
    align: 'center',
    dataIndex: 'processInstanceField',
    width: 120,
  },
  {
    title: '快照策略',
    align: 'center',
    dataIndex: 'snapshotStrategy',
    width: 100,
  },
  {
    title: '状态',
    align: 'center',
    dataIndex: 'status',
    width: 80,
    customRender: ({ record }) => {
      return record.status === 1 ? '启用' : '禁用';
    },
  },
  {
    title: '创建时间',
    align: 'center',
    dataIndex: 'createTime',
    width: 180,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '表单ID',
    field: 'cgformHeadId',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    label: '流程定义Key',
    field: 'processDefinitionKey',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    label: '启用工作流',
    field: 'workflowEnabled',
    component: 'Select',
    componentProps: {
      options: [
        { label: '全部', value: '' },
        { label: '是', value: '1' },
        { label: '否', value: '0' },
      ],
    },
    colProps: { span: 8 },
  },
];

export const formSchema: FormSchema[] = [
  {
    label: '表单ID',
    field: 'cgformHeadId',
    component: 'Input',
    required: true,
  },
  {
    label: '流程定义Key',
    field: 'processDefinitionKey',
    component: 'Input',
    required: true,
  },
  {
    label: '启用工作流',
    field: 'workflowEnabled',
    component: 'Switch',
    defaultValue: false,
  },
  {
    label: '启用版本控制',
    field: 'versionControlEnabled',
    component: 'Switch',
    defaultValue: false,
  },
  {
    label: '启用权限控制',
    field: 'permissionControlEnabled',
    component: 'Switch',
    defaultValue: false,
  },
  {
    label: '业务主键字段',
    field: 'businessKeyField',
    component: 'Input',
  },
  {
    label: '状态字段',
    field: 'statusField',
    component: 'Input',
    defaultValue: 'bmp_status',
  },
  {
    label: '流程实例字段',
    field: 'processInstanceField',
    component: 'Input',
    defaultValue: 'process_instance_id',
  },
  {
    label: '快照策略',
    field: 'snapshotStrategy',
    component: 'Select',
    componentProps: {
      options: [
        { label: '节点级', value: 'NODE' },
        { label: '任务级', value: 'TASK' },
      ],
    },
    defaultValue: 'NODE',
  },
  {
    label: '需要快照的节点',
    field: 'snapshotNodes',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入JSON数组格式的节点列表',
      rows: 4,
    },
  },
  {
    label: '状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: [
        { label: '启用', value: 1 },
        { label: '禁用', value: 0 },
      ],
    },
    defaultValue: 1,
  },
]; 