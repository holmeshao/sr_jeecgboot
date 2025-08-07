<template>
  <div class="field-permission-designer">
    <!-- 头部工具栏 -->
    <div class="designer-header">
      <a-row justify="space-between" align="middle">
        <a-col>
          <a-space>
            <h3 style="margin: 0;">字段权限配置</h3>
            <a-tag color="blue">{{ formName }}</a-tag>
            <a-tag color="green">{{ processName }}</a-tag>
          </a-space>
        </a-col>
        <a-col>
          <a-space>
            <a-button @click="resetAllPermissions">重置所有</a-button>
            <a-button @click="savePermissions" type="primary" :loading="saving">
              <SaveOutlined />
              保存配置
            </a-button>
          </a-space>
        </a-col>
      </a-row>
    </div>

    <a-row :gutter="24">
      <!-- 左侧：流程节点列表 -->
      <a-col :span="8">
        <a-card title="流程节点" size="small" class="nodes-panel">
          <div class="search-box">
            <a-input-search
              v-model:value="nodeSearchKeyword"
              placeholder="搜索节点..."
              style="margin-bottom: 16px;"
              @search="filterNodes"
            />
          </div>
          
          <div class="process-nodes">
            <div 
              v-for="node in filteredNodes" 
              :key="node.id"
              :class="['node-item', { 
                active: selectedNode?.id === node.id,
                configured: hasNodeConfig(node.id)
              }]"
              @click="selectNode(node)"
            >
              <div class="node-content">
                <div class="node-info">
                  <a-badge 
                    :count="getNodeFieldCount(node.id)" 
                    :number-style="{ backgroundColor: getNodeBadgeColor(node.id) }"
                    show-zero
                  >
                    <div class="node-icon">
                      <component :is="getNodeIcon(node.type)" />
                    </div>
                  </a-badge>
                  <div class="node-details">
                    <div class="node-name">{{ node.name }}</div>
                    <div class="node-type">{{ getNodeTypeText(node.type) }}</div>
                  </div>
                </div>
                <div class="node-status">
                  <a-tooltip :title="getNodeStatusTooltip(node.id)">
                    <a-tag 
                      :color="getNodeStatusColor(node.id)" 
                      size="small"
                    >
                      {{ getNodeStatusText(node.id) }}
                    </a-tag>
                  </a-tooltip>
                </div>
              </div>
            </div>
          </div>
        </a-card>
      </a-col>
      
      <!-- 右侧：字段权限配置 -->
      <a-col :span="16">
        <a-card 
          :title="selectedNode ? `${selectedNode.name} - 字段权限配置` : '请选择节点'"
          size="small"
          class="permission-panel"
        >
          <template v-if="selectedNode">
            <!-- 快速操作工具栏 -->
            <div class="quick-actions">
              <a-space wrap>
                <a-button @click="setAllEditable" size="small">
                  <EditOutlined />
                  全部可编辑
                </a-button>
                <a-button @click="setAllReadonly" size="small">
                  <EyeOutlined />
                  全部只读
                </a-button>
                <a-button @click="setAllHidden" size="small">
                  <EyeInvisibleOutlined />
                  全部隐藏
                </a-button>
                <a-button @click="resetNodePermissions" size="small">
                  <RedoOutlined />
                  重置权限
                </a-button>
                <a-divider type="vertical" />
                <a-button @click="copyFromOtherNode" size="small">
                  <CopyOutlined />
                  复制配置
                </a-button>
                <a-button @click="applyToOtherNodes" size="small">
                  <DeploymentUnitOutlined />
                  应用到其他节点
                </a-button>
              </a-space>
            </div>
            
            <!-- 字段搜索和过滤 -->
            <div class="field-filters">
              <a-row :gutter="16">
                <a-col :span="12">
                  <a-input-search
                    v-model:value="fieldSearchKeyword"
                    placeholder="搜索字段..."
                    @search="filterFields"
                  />
                </a-col>
                <a-col :span="12">
                  <a-select
                    v-model:value="fieldFilter"
                    placeholder="过滤字段"
                    style="width: 100%"
                    @change="filterFields"
                  >
                    <a-select-option value="all">全部字段</a-select-option>
                    <a-select-option value="editable">可编辑</a-select-option>
                    <a-select-option value="readonly">只读</a-select-option>
                    <a-select-option value="hidden">隐藏</a-select-option>
                    <a-select-option value="required">必填</a-select-option>
                    <a-select-option value="business">业务字段</a-select-option>
                    <a-select-option value="system">系统字段</a-select-option>
                  </a-select>
                </a-col>
              </a-row>
            </div>
            
            <!-- 字段权限表格 -->
            <a-table 
              :columns="fieldColumns" 
              :data-source="filteredFields"
              :pagination="{ pageSize: 20, showSizeChanger: true }"
              size="small"
              :scroll="{ y: 400 }"
              row-key="fieldName"
              :row-selection="fieldRowSelection"
            >
              <template #bodyCell="{ column, record, index }">
                <template v-if="column.key === 'fieldName'">
                  <div class="field-name-cell">
                    <a-tooltip :title="record.comment || record.fieldName">
                      <div class="field-name">{{ record.fieldName }}</div>
                      <div class="field-comment">{{ record.comment || '无描述' }}</div>
                    </a-tooltip>
                  </div>
                </template>
                
                <template v-if="column.key === 'fieldType'">
                  <a-tag :color="getFieldTypeColor(record.fieldType)" size="small">
                    {{ getFieldTypeText(record.fieldType) }}
                  </a-tag>
                </template>
                
                <template v-if="column.key === 'permission'">
                  <a-select 
                    :value="getFieldPermission(record.fieldName)" 
                    size="small"
                    style="width: 100px;"
                    @change="(value) => updateFieldPermission(record.fieldName, value)"
                  >
                    <a-select-option value="editable">
                      <a-tag color="green" size="small">可编辑</a-tag>
                    </a-select-option>
                    <a-select-option value="readonly">
                      <a-tag color="orange" size="small">只读</a-tag>
                    </a-select-option>
                    <a-select-option value="hidden">
                      <a-tag color="red" size="small">隐藏</a-tag>
                    </a-select-option>
                  </a-select>
                </template>
                
                <template v-if="column.key === 'required'">
                  <a-checkbox 
                    :checked="isFieldRequired(record.fieldName)"
                    :disabled="getFieldPermission(record.fieldName) === 'hidden'"
                    @change="(e) => updateFieldRequired(record.fieldName, e.target.checked)" 
                  />
                </template>
                
                <template v-if="column.key === 'condition'">
                  <a-button 
                    size="small" 
                    type="link" 
                    @click="editFieldCondition(record)"
                    :disabled="getFieldPermission(record.fieldName) === 'hidden'"
                  >
                    {{ hasFieldCondition(record.fieldName) ? '编辑条件' : '添加条件' }}
                  </a-button>
                </template>
              </template>
            </a-table>
            
            <!-- 权限预览 -->
            <div class="permission-preview">
              <h4>权限配置预览</h4>
              <a-row :gutter="16}>
                <a-col :span="8">
                  <a-card size="small" title="可编辑字段" class="preview-card editable">
                    <a-space wrap>
                      <a-tag 
                        v-for="field in editableFields" 
                        :key="field"
                        color="green"
                        size="small"
                      >
                        {{ getFieldDisplayName(field) }}
                      </a-tag>
                    </a-space>
                    <div v-if="editableFields.length === 0" class="empty-state">
                      暂无可编辑字段
                    </div>
                  </a-card>
                </a-col>
                <a-col :span="8">
                  <a-card size="small" title="只读字段" class="preview-card readonly">
                    <a-space wrap>
                      <a-tag 
                        v-for="field in readonlyFields" 
                        :key="field"
                        color="orange"
                        size="small"
                      >
                        {{ getFieldDisplayName(field) }}
                      </a-tag>
                    </a-space>
                    <div v-if="readonlyFields.length === 0" class="empty-state">
                      暂无只读字段
                    </div>
                  </a-card>
                </a-col>
                <a-col :span="8">
                  <a-card size="small" title="隐藏字段" class="preview-card hidden">
                    <a-space wrap>
                      <a-tag 
                        v-for="field in hiddenFields" 
                        :key="field"
                        color="red"
                        size="small"
                      >
                        {{ getFieldDisplayName(field) }}
                      </a-tag>
                    </a-space>
                    <div v-if="hiddenFields.length === 0" class="empty-state">
                      暂无隐藏字段
                    </div>
                  </a-card>
                </a-col>
              </a-row>
            </div>
          </template>
          
          <a-empty v-else description="请选择左侧节点进行配置" />
        </a-card>
      </a-col>
    </a-row>

    <!-- 条件配置模态框 -->
    <a-modal
      v-model:open="conditionModalVisible"
      title="条件权限配置"
      width="800px"
      @ok="saveFieldCondition"
    >
      <div v-if="editingField">
        <h4>字段：{{ editingField.fieldName }} ({{ editingField.comment }})</h4>
        <a-form layout="vertical">
          <a-form-item label="条件表达式">
            <a-textarea 
              v-model:value="editingCondition"
              placeholder="例如：#{currentUser.deptId == record.deptId}"
              :rows="3"
            />
          </a-form-item>
          <a-form-item label="条件说明">
            <a-input 
              v-model:value="editingConditionDesc"
              placeholder="条件的说明文字"
            />
          </a-form-item>
        </a-form>
      </div>
    </a-modal>

    <!-- 复制配置模态框 -->
    <a-modal
      v-model:open="copyModalVisible"
      title="复制权限配置"
      @ok="executeCopyConfig"
    >
      <a-form layout="vertical">
        <a-form-item label="选择源节点">
          <a-select v-model:value="copySourceNode" placeholder="选择要复制的节点" style="width: 100%">
            <a-select-option 
              v-for="node in processNodes.filter(n => n.id !== selectedNode?.id)" 
              :key="node.id" 
              :value="node.id"
            >
              {{ node.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { message } from 'ant-design-vue';
import { 
  SaveOutlined,
  EditOutlined,
  EyeOutlined,
  EyeInvisibleOutlined,
  RedoOutlined,
  CopyOutlined,
  DeploymentUnitOutlined,
  PlayCircleOutlined,
  CheckCircleOutlined,
  StopOutlined,
  UserOutlined
} from '@ant-design/icons-vue';
import { FormPermissionApi } from '@/api/workflow/form';

// 定义组件属性
interface Props {
  formId: string;
  processDefinitionKey: string;
  formName?: string;
  processName?: string;
}

const props = defineProps<Props>();

// 响应式数据
const saving = ref(false);
const selectedNode = ref(null);
const nodeSearchKeyword = ref('');
const fieldSearchKeyword = ref('');
const fieldFilter = ref('all');
const processNodes = ref([]);
const formFields = ref([]);
const nodePermissions = reactive({});

// 模态框状态
const conditionModalVisible = ref(false);
const copyModalVisible = ref(false);
const editingField = ref(null);
const editingCondition = ref('');
const editingConditionDesc = ref('');
const copySourceNode = ref('');

// 字段选择
const selectedFieldKeys = ref([]);
const fieldRowSelection = {
  selectedRowKeys: selectedFieldKeys,
  onChange: (keys) => {
    selectedFieldKeys.value = keys;
  }
};

// 表格列定义
const fieldColumns = [
  {
    title: '字段名称',
    dataIndex: 'fieldName',
    key: 'fieldName',
    width: 200,
    fixed: 'left'
  },
  {
    title: '字段类型',
    dataIndex: 'fieldType',
    key: 'fieldType',
    width: 100
  },
  {
    title: '权限',
    key: 'permission',
    width: 120
  },
  {
    title: '必填',
    key: 'required',
    width: 80,
    align: 'center'
  },
  {
    title: '条件',
    key: 'condition',
    width: 100,
    align: 'center'
  }
];

// 计算属性
const filteredNodes = computed(() => {
  if (!nodeSearchKeyword.value) return processNodes.value;
  
  return processNodes.value.filter(node => 
    node.name.toLowerCase().includes(nodeSearchKeyword.value.toLowerCase()) ||
    node.id.toLowerCase().includes(nodeSearchKeyword.value.toLowerCase())
  );
});

const filteredFields = computed(() => {
  let fields = [...formFields.value];
  
  // 按关键词搜索
  if (fieldSearchKeyword.value) {
    const keyword = fieldSearchKeyword.value.toLowerCase();
    fields = fields.filter(field => 
      field.fieldName.toLowerCase().includes(keyword) ||
      (field.comment && field.comment.toLowerCase().includes(keyword))
    );
  }
  
  // 按类型过滤
  if (fieldFilter.value !== 'all') {
    fields = fields.filter(field => {
      const permission = getFieldPermission(field.fieldName);
      const isRequired = isFieldRequired(field.fieldName);
      const isBusinessField = !isSystemField(field.fieldName);
      
      switch (fieldFilter.value) {
        case 'editable': return permission === 'editable';
        case 'readonly': return permission === 'readonly';
        case 'hidden': return permission === 'hidden';
        case 'required': return isRequired;
        case 'business': return isBusinessField;
        case 'system': return !isBusinessField;
        default: return true;
      }
    });
  }
  
  return fields;
});

const editableFields = computed(() => {
  if (!selectedNode.value) return [];
  const permissions = nodePermissions[selectedNode.value.id];
  return permissions?.editableFields || [];
});

const readonlyFields = computed(() => {
  if (!selectedNode.value) return [];
  const permissions = nodePermissions[selectedNode.value.id];
  return permissions?.readonlyFields || [];
});

const hiddenFields = computed(() => {
  if (!selectedNode.value) return [];
  const permissions = nodePermissions[selectedNode.value.id];
  return permissions?.hiddenFields || [];
});

// 初始化
onMounted(async () => {
  await loadData();
});

/**
 * 加载数据
 */
async function loadData() {
  try {
    // 加载表单字段
    formFields.value = await FormPermissionApi.getFormFields(props.formId);
    
    // 模拟流程节点数据（实际应该从Flowable获取）
    processNodes.value = [
      { id: 'start', name: '开始节点', type: 'startEvent' },
      { id: 'submit', name: '提交申请', type: 'userTask' },
      { id: 'review', name: '审核节点', type: 'userTask' },
      { id: 'approve', name: '审批节点', type: 'userTask' },
      { id: 'end', name: '结束节点', type: 'endEvent' }
    ];
    
    // 加载已有的权限配置
    for (const node of processNodes.value) {
      await loadNodePermissions(node.id);
    }
    
  } catch (error) {
    console.error('加载数据失败:', error);
    message.error('加载数据失败');
  }
}

/**
 * 加载节点权限配置
 */
async function loadNodePermissions(nodeId: string) {
  try {
    const config = await FormPermissionApi.getNodePermission(
      props.formId, 
      props.processDefinitionKey, 
      nodeId
    );
    
    nodePermissions[nodeId] = {
      editableFields: config.editableFields || [],
      readonlyFields: config.readonlyFields || [],
      hiddenFields: config.hiddenFields || [],
      requiredFields: config.requiredFields || [],
      conditionalPermissions: config.conditionalPermissions || {}
    };
    
  } catch (error) {
    // 如果没有配置，使用默认配置
    nodePermissions[nodeId] = {
      editableFields: [],
      readonlyFields: [],
      hiddenFields: [],
      requiredFields: [],
      conditionalPermissions: {}
    };
  }
}

/**
 * 选择节点
 */
function selectNode(node: any) {
  selectedNode.value = node;
  selectedFieldKeys.value = [];
}

/**
 * 过滤节点
 */
function filterNodes() {
  // 过滤逻辑在计算属性中处理
}

/**
 * 过滤字段
 */
function filterFields() {
  // 过滤逻辑在计算属性中处理
}

/**
 * 获取字段权限
 */
function getFieldPermission(fieldName: string): string {
  if (!selectedNode.value) return 'readonly';
  
  const permissions = nodePermissions[selectedNode.value.id];
  if (!permissions) return 'readonly';
  
  if (permissions.hiddenFields.includes(fieldName)) return 'hidden';
  if (permissions.editableFields.includes(fieldName)) return 'editable';
  return 'readonly';
}

/**
 * 更新字段权限
 */
function updateFieldPermission(fieldName: string, permission: string) {
  if (!selectedNode.value) return;
  
  const permissions = nodePermissions[selectedNode.value.id];
  if (!permissions) return;
  
  // 从所有权限数组中移除
  permissions.editableFields = permissions.editableFields.filter(f => f !== fieldName);
  permissions.readonlyFields = permissions.readonlyFields.filter(f => f !== fieldName);
  permissions.hiddenFields = permissions.hiddenFields.filter(f => f !== fieldName);
  
  // 添加到对应权限数组
  if (permission === 'editable') {
    permissions.editableFields.push(fieldName);
  } else if (permission === 'hidden') {
    permissions.hiddenFields.push(fieldName);
    // 隐藏字段不能是必填的
    permissions.requiredFields = permissions.requiredFields.filter(f => f !== fieldName);
  } else {
    permissions.readonlyFields.push(fieldName);
  }
}

/**
 * 判断字段是否必填
 */
function isFieldRequired(fieldName: string): boolean {
  if (!selectedNode.value) return false;
  
  const permissions = nodePermissions[selectedNode.value.id];
  return permissions?.requiredFields.includes(fieldName) || false;
}

/**
 * 更新字段必填状态
 */
function updateFieldRequired(fieldName: string, required: boolean) {
  if (!selectedNode.value) return;
  
  const permissions = nodePermissions[selectedNode.value.id];
  if (!permissions) return;
  
  if (required) {
    if (!permissions.requiredFields.includes(fieldName)) {
      permissions.requiredFields.push(fieldName);
    }
  } else {
    permissions.requiredFields = permissions.requiredFields.filter(f => f !== fieldName);
  }
}

/**
 * 判断是否有字段条件
 */
function hasFieldCondition(fieldName: string): boolean {
  if (!selectedNode.value) return false;
  
  const permissions = nodePermissions[selectedNode.value.id];
  return permissions?.conditionalPermissions[fieldName] != null;
}

/**
 * 编辑字段条件
 */
function editFieldCondition(field: any) {
  editingField.value = field;
  
  if (selectedNode.value) {
    const permissions = nodePermissions[selectedNode.value.id];
    const condition = permissions?.conditionalPermissions[field.fieldName];
    editingCondition.value = condition?.expression || '';
    editingConditionDesc.value = condition?.description || '';
  }
  
  conditionModalVisible.value = true;
}

/**
 * 保存字段条件
 */
function saveFieldCondition() {
  if (!selectedNode.value || !editingField.value) return;
  
  const permissions = nodePermissions[selectedNode.value.id];
  if (!permissions) return;
  
  if (editingCondition.value.trim()) {
    permissions.conditionalPermissions[editingField.value.fieldName] = {
      expression: editingCondition.value,
      description: editingConditionDesc.value
    };
  } else {
    delete permissions.conditionalPermissions[editingField.value.fieldName];
  }
  
  conditionModalVisible.value = false;
  editingField.value = null;
  editingCondition.value = '';
  editingConditionDesc.value = '';
}

/**
 * 设置所有字段为可编辑
 */
function setAllEditable() {
  if (!selectedNode.value) return;
  
  const permissions = nodePermissions[selectedNode.value.id];
  if (!permissions) return;
  
  const allFieldNames = formFields.value.map(f => f.fieldName);
  permissions.editableFields = [...allFieldNames];
  permissions.readonlyFields = [];
  permissions.hiddenFields = [];
}

/**
 * 设置所有字段为只读
 */
function setAllReadonly() {
  if (!selectedNode.value) return;
  
  const permissions = nodePermissions[selectedNode.value.id];
  if (!permissions) return;
  
  const allFieldNames = formFields.value.map(f => f.fieldName);
  permissions.editableFields = [];
  permissions.readonlyFields = [...allFieldNames];
  permissions.hiddenFields = [];
}

/**
 * 设置所有字段为隐藏
 */
function setAllHidden() {
  if (!selectedNode.value) return;
  
  const permissions = nodePermissions[selectedNode.value.id];
  if (!permissions) return;
  
  const allFieldNames = formFields.value.map(f => f.fieldName);
  permissions.editableFields = [];
  permissions.readonlyFields = [];
  permissions.hiddenFields = [...allFieldNames];
  permissions.requiredFields = []; // 隐藏字段不能必填
}

/**
 * 重置节点权限
 */
function resetNodePermissions() {
  if (!selectedNode.value) return;
  
  nodePermissions[selectedNode.value.id] = {
    editableFields: [],
    readonlyFields: [],
    hiddenFields: [],
    requiredFields: [],
    conditionalPermissions: {}
  };
}

/**
 * 复制其他节点配置
 */
function copyFromOtherNode() {
  copyModalVisible.value = true;
}

/**
 * 执行复制配置
 */
function executeCopyConfig() {
  if (!selectedNode.value || !copySourceNode.value) return;
  
  const sourcePermissions = nodePermissions[copySourceNode.value];
  if (sourcePermissions) {
    nodePermissions[selectedNode.value.id] = JSON.parse(JSON.stringify(sourcePermissions));
    message.success('配置复制成功');
  }
  
  copyModalVisible.value = false;
  copySourceNode.value = '';
}

/**
 * 应用到其他节点
 */
function applyToOtherNodes() {
  // TODO: 实现应用到其他节点的功能
  message.info('功能开发中...');
}

/**
 * 重置所有权限
 */
function resetAllPermissions() {
  processNodes.value.forEach(node => {
    nodePermissions[node.id] = {
      editableFields: [],
      readonlyFields: [],
      hiddenFields: [],
      requiredFields: [],
      conditionalPermissions: {}
    };
  });
  message.success('所有权限已重置');
}

/**
 * 保存权限配置
 */
async function savePermissions() {
  saving.value = true;
  
  try {
    // 批量保存所有节点的权限配置
    for (const node of processNodes.value) {
      const permissions = nodePermissions[node.id];
      if (permissions) {
        await FormPermissionApi.saveNodePermission({
          formId: props.formId,
          processDefinitionKey: props.processDefinitionKey,
          nodeId: node.id,
          nodeName: node.name,
          ...permissions
        });
      }
    }
    
    message.success('权限配置保存成功');
    
  } catch (error) {
    console.error('保存失败:', error);
    message.error('保存失败，请重试');
  } finally {
    saving.value = false;
  }
}

// 辅助函数
function hasNodeConfig(nodeId: string): boolean {
  const permissions = nodePermissions[nodeId];
  if (!permissions) return false;
  
  return permissions.editableFields.length > 0 || 
         permissions.readonlyFields.length > 0 || 
         permissions.hiddenFields.length > 0;
}

function getNodeFieldCount(nodeId: string): number {
  const permissions = nodePermissions[nodeId];
  if (!permissions) return 0;
  
  return permissions.editableFields.length + 
         permissions.readonlyFields.length + 
         permissions.hiddenFields.length;
}

function getNodeBadgeColor(nodeId: string): string {
  return hasNodeConfig(nodeId) ? '#52c41a' : '#d9d9d9';
}

function getNodeIcon(nodeType: string) {
  const iconMap = {
    'startEvent': PlayCircleOutlined,
    'userTask': UserOutlined,
    'endEvent': CheckCircleOutlined,
    'serviceTask': StopOutlined
  };
  return iconMap[nodeType] || UserOutlined;
}

function getNodeTypeText(nodeType: string): string {
  const typeMap = {
    'startEvent': '开始事件',
    'userTask': '用户任务',
    'endEvent': '结束事件',
    'serviceTask': '服务任务'
  };
  return typeMap[nodeType] || nodeType;
}

function getNodeStatusColor(nodeId: string): string {
  return hasNodeConfig(nodeId) ? 'success' : 'default';
}

function getNodeStatusText(nodeId: string): string {
  return hasNodeConfig(nodeId) ? '已配置' : '未配置';
}

function getNodeStatusTooltip(nodeId: string): string {
  const count = getNodeFieldCount(nodeId);
  return hasNodeConfig(nodeId) ? `已配置 ${count} 个字段权限` : '尚未配置字段权限';
}

function getFieldTypeColor(fieldType: string): string {
  const colorMap = {
    'VARCHAR': 'blue',
    'INTEGER': 'green',
    'DATETIME': 'orange',
    'TEXT': 'purple',
    'DECIMAL': 'cyan'
  };
  return colorMap[fieldType] || 'default';
}

function getFieldTypeText(fieldType: string): string {
  const textMap = {
    'VARCHAR': '字符串',
    'INTEGER': '整数',
    'DATETIME': '日期时间',
    'TEXT': '文本',
    'DECIMAL': '小数'
  };
  return textMap[fieldType] || fieldType;
}

function getFieldDisplayName(fieldName: string): string {
  const field = formFields.value.find(f => f.fieldName === fieldName);
  return field?.comment || fieldName;
}

function isSystemField(fieldName: string): boolean {
  const systemFields = ['id', 'create_time', 'update_time', 'create_by', 'update_by', 'process_instance_id', 'bmp_status'];
  return systemFields.includes(fieldName);
}
</script>

<style lang="less" scoped>
.field-permission-designer {
  .designer-header {
    margin-bottom: 16px;
    padding: 16px;
    background: white;
    border-radius: 6px;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);
  }

  .nodes-panel {
    .search-box {
      margin-bottom: 16px;
    }

    .process-nodes {
      max-height: 600px;
      overflow-y: auto;

      .node-item {
        padding: 12px;
        margin-bottom: 8px;
        border: 1px solid #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        transition: all 0.2s;

        &:hover {
          border-color: #1890ff;
          box-shadow: 0 2px 4px rgba(24, 144, 255, 0.1);
        }

        &.active {
          border-color: #1890ff;
          background: #e6f7ff;
        }

        &.configured {
          border-left: 4px solid #52c41a;
        }

        .node-content {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .node-info {
            display: flex;
            align-items: center;
            gap: 12px;

            .node-icon {
              display: flex;
              align-items: center;
              justify-content: center;
              width: 32px;
              height: 32px;
              border-radius: 50%;
              background: #f0f0f0;
              color: #666;
            }

            .node-details {
              .node-name {
                font-weight: 500;
                color: rgba(0, 0, 0, 0.85);
              }

              .node-type {
                font-size: 12px;
                color: rgba(0, 0, 0, 0.45);
              }
            }
          }

          .node-status {
            .ant-tag {
              margin: 0;
            }
          }
        }
      }
    }
  }

  .permission-panel {
    .quick-actions {
      margin-bottom: 16px;
      padding: 12px;
      background: #fafafa;
      border-radius: 6px;
    }

    .field-filters {
      margin-bottom: 16px;
    }

    .field-name-cell {
      .field-name {
        font-weight: 500;
        color: rgba(0, 0, 0, 0.85);
      }

      .field-comment {
        font-size: 12px;
        color: rgba(0, 0, 0, 0.45);
      }
    }

    .permission-preview {
      margin-top: 24px;

      h4 {
        margin-bottom: 16px;
        font-weight: 600;
      }

      .preview-card {
        &.editable {
          border-left: 4px solid #52c41a;
        }

        &.readonly {
          border-left: 4px solid #fa8c16;
        }

        &.hidden {
          border-left: 4px solid #ff4d4f;
        }

        .empty-state {
          color: rgba(0, 0, 0, 0.45);
          font-style: italic;
        }
      }
    }
  }
}

:deep(.ant-table-tbody > tr > td) {
  padding: 8px 12px;
}

:deep(.ant-card-head-title) {
  font-weight: 600;
}

:deep(.ant-badge-count) {
  min-width: 18px;
  height: 18px;
  line-height: 18px;
  font-size: 12px;
}
</style>