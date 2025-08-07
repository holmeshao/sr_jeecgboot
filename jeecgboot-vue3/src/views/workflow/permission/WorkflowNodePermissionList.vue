<template>
  <div>
    <!-- 🎯 查询区域 -->
    <div class="jeecg-basic-table-form-container">
      <a-form ref="formRef" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="表单ID">
              <a-input placeholder="请输入表单ID" v-model:value="queryParam.cgformHeadId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="流程定义Key">
              <a-input placeholder="请输入流程定义Key" v-model:value="queryParam.processDefinitionKey"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="节点ID">
              <a-input placeholder="请输入节点ID" v-model:value="queryParam.nodeId"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="节点名称">
                <a-input placeholder="请输入节点名称" v-model:value="queryParam.nodeName"></a-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-col :lg="6">
                <a-button type="primary" @click="searchQuery" icon="search-outlined">查询</a-button>
                <a-button type="primary" @click="searchReset" icon="reload-outlined" style="margin-left: 8px">重置</a-button>
                <a @click="handleToggleSearch" style="margin-left: 8px">
                  {{ toggleSearchStatus ? '收起' : '展开' }}
                  <template v-if="toggleSearchStatus">
                    <UpOutlined />
                  </template>
                  <template v-else>
                    <DownOutlined />
                  </template>
                </a>
              </a-col>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 🎯 操作按钮区域 -->
    <div class="jeecg-basic-table-action-container">
      <div class="jeecg-basic-table-action-left-button">
        <a-button @click="handleAdd" type="primary" icon="plus-outlined">新增配置</a-button>
        <a-button @click="openPermissionDesigner" type="primary" icon="setting-outlined" style="margin-left: 8px">
          权限配置器
        </a-button>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined"></Icon>
                删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button style="margin-left: 8px">
            批量操作
            <Icon icon="mdi:chevron-down"></Icon>
          </a-button>
        </a-dropdown>
      </div>
    </div>

    <!-- 🎯 数据表格 -->
    <div class="jeecg-basic-table">
      <a-table
        ref="tableRef"
        size="middle"
        :scroll="{ x: true }"
        bordered
        :dataSource="dataSource"
        :columns="columns"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="rowSelection"
        @change="handleTableChange">
        
        <!-- 表单模式 -->
        <template #formModeSlot="{ text }">
          <a-tag v-if="text === 'VIEW'" color="blue">只读</a-tag>
          <a-tag v-else-if="text === 'EDIT'" color="green">编辑</a-tag>
          <a-tag v-else-if="text === 'OPERATE'" color="orange">操作</a-tag>
          <a-tag v-else color="default">{{ text }}</a-tag>
        </template>

        <!-- 字段权限摘要 -->
        <template #permissionSummarySlot="{ record }">
          <div style="max-width: 200px;">
            <a-space direction="vertical" size="small">
              <div v-if="getFieldCount(record.editableFields) > 0">
                <a-tag color="green" size="small">
                  可编辑: {{ getFieldCount(record.editableFields) }}
                </a-tag>
              </div>
              <div v-if="getFieldCount(record.readonlyFields) > 0">
                <a-tag color="orange" size="small">
                  只读: {{ getFieldCount(record.readonlyFields) }}
                </a-tag>
              </div>
              <div v-if="getFieldCount(record.hiddenFields) > 0">
                <a-tag color="red" size="small">
                  隐藏: {{ getFieldCount(record.hiddenFields) }}
                </a-tag>
              </div>
            </a-space>
          </div>
        </template>

        <!-- 操作列 -->
        <template #action="{ record }">
          <TableAction :actions="getTableAction(record)" />
        </template>
      </a-table>
    </div>

    <!-- 🎯 权限配置模态框 -->
    <WorkflowNodePermissionModal
      ref="modalRef"
      @register="registerModal"
      @success="handleSuccess" />

    <!-- 🎯 权限配置器 -->
    <WorkflowPermissionDesigner
      ref="designerRef"
      @success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed, unref, onMounted } from 'vue';
import { DownOutlined, UpOutlined } from '@ant-design/icons-vue';
import { useTable } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import WorkflowNodePermissionModal from './WorkflowNodePermissionModal.vue';
import WorkflowPermissionDesigner from './WorkflowPermissionDesigner.vue';
import { columns, searchFormSchema } from './WorkflowNodePermission.data';
import { 
  list, 
  deleteOne, 
  batchDelete, 
  getExportUrl, 
  getImportUrl 
} from './WorkflowNodePermission.api';
import { TableAction } from '/@/components/Table';
import { Icon } from '/@/components/Icon';

defineOptions({ name: 'WorkflowNodePermissionList' });

// =============== 组件引用 ===============
const tableRef = ref();
const modalRef = ref();
const designerRef = ref();
const formRef = ref();

// =============== 状态管理 ===============
const toggleSearchStatus = ref<boolean>(false);
const queryParam = reactive<any>({});

// =============== 表格配置 ===============
const [registerModal, { openModal }] = useModal();
const { prefixCls, tableContext, onExportXls, onImportXls } = useListPage({
  tableRef,
  getListApi: list,
  deleteApi: deleteOne,
  batchDeleteApi: batchDelete,
  exportApi: getExportUrl,
  importApi: getImportUrl,
  useSearchForm: false,
});

// 从 tableContext 中解构需要的响应式数据
const {
  loading,
  dataSource,
  pagination: ipagination,
  searchQuery,
  searchReset,
  handleTableChange,
  getSelectRows,
  selectedRowKeys,
} = tableContext;

// =============== 行选择配置 ===============
const rowSelection = computed(() => {
  return {
    selectedRowKeys: unref(selectedRowKeys),
    onChange: (changableRowKeys) => {
      selectedRowKeys.value = changableRowKeys;
    },
    checkStrictly: false,
  };
});

// =============== 业务方法 ===============

/**
 * 新增配置
 */
function handleAdd() {
  openModal(true, {
    isUpdate: false,
  });
}

/**
 * 编辑配置
 */
function handleEdit(record: Recordable) {
  openModal(true, {
    record,
    isUpdate: true,
  });
}

/**
 * 详情
 */
function handleDetail(record: Recordable) {
  openModal(true, {
    record,
    isUpdate: false,
    showFooter: false,
  });
}

/**
 * 删除事件
 */
async function handleDelete(record) {
  await deleteOne({ id: record.id }, handleSuccess);
}

/**
 * 批量删除事件
 */
async function batchHandleDelete() {
  await batchDelete({ ids: selectedRowKeys.value }, handleSuccess);
}

/**
 * 成功回调
 */
function handleSuccess() {
  (selectedRowKeys.value = []) && searchQuery();
}

/**
 * 操作栏
 */
function getTableAction(record) {
  return [
    {
      label: '编辑',
      onClick: handleEdit.bind(null, record),
    },
    {
      label: '详情',
      onClick: handleDetail.bind(null, record),
    },
    {
      label: '删除',
      popConfirm: {
        title: '是否确认删除',
        confirm: handleDelete.bind(null, record),
      },
    },
  ];
}

/**
 * 下拉搜索切换
 */
function handleToggleSearch() {
  toggleSearchStatus.value = !toggleSearchStatus.value;
}

/**
 * 打开权限配置器
 */
function openPermissionDesigner() {
  if (designerRef.value) {
    designerRef.value.open();
  }
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

// =============== 生命周期 ===============
onMounted(() => {
  searchQuery();
});
</script>

<style scoped>
.jeecg-basic-table-form-container {
  padding: 16px;
  background: #fff;
  border-radius: 6px;
  margin-bottom: 16px;
}

.jeecg-basic-table-action-container {
  padding: 16px;
  background: #fff;
  border-radius: 6px;
  margin-bottom: 16px;
}

.jeecg-basic-table-action-left-button {
  text-align: left;
}

.jeecg-basic-table {
  background: #fff;
  border-radius: 6px;
}

.table-page-search-submitButtons {
  display: block;
  margin-bottom: 24px;
  white-space: nowrap;
}
</style>