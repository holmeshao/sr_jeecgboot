<template>
  <div>
    <BasicTable @register="registerTable">
      <template #tableTitle>
        <a-button type="primary" @click="handleAdd" preIcon="ant-design:plus-outlined"> 新增</a-button>
        <a-button type="primary" preIcon="ant-design:export-outlined" @click="onExportXls"> 导出</a-button>
        <j-upload-button type="primary" preIcon="ant-design:import-outlined" @click="onImportXls">导入</j-upload-button>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined"></Icon>
                删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button>批量操作
            <Icon icon="mdi:chevron-down"></Icon>
          </a-button>
        </a-dropdown>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              label: '编辑',
              onClick: handleEdit.bind(null, record),
            },
            {
              label: '节点配置',
              onClick: handleNodeConfig.bind(null, record),
            },
            {
              label: '删除',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <!-- 复用JeecgBoot现有的在线表单编辑功能，通过路由跳转到编辑页面 -->
  </div>
</template>

<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage';
      // 复用JeecgBoot现有的Modal组件，无需创建专门的工作流Modal
  import { columns, searchFormSchema } from './workflowConfig.data';
  import { getWorkflowConfigList, deleteWorkflowConfig, addWorkflowConfig, editWorkflowConfig } from '/@/api/workflow';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';

  defineOptions({ name: 'WorkflowConfigList' });

  const checkedKeys = ref<Array<string | number>>([]);
  const { showMessage } = useMessage();
  // 使用JeecgBoot现有的在线表单编辑方式，无需专门的工作流Modal

  // 列表页面公共参数、方法
  const { prefixCls, tableContext, onExportXls, onImportXls } = useListPage({
    tableProps: {
      title: '表单工作流配置',
      api: getWorkflowConfigList,
      columns,
      canResize: false,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
        autoSubmitOnEnter: true,
      },
      actionColumn: {
        width: 120,
        fixed: 'right',
      },
      beforeFetch: (params) => {
        return Object.assign(params);
      },
    },
    exportConfig: {
      name: '表单工作流配置列表',
      url: '/workflow/onlCgformWorkflowConfig/exportXls',
    },
    importConfig: {
      url: `${import.meta.env.VITE_GLOB_API_URL}/workflow/onlineForm/config/importExcel`,
      success: handleImportSuccess,
    },
  });

  const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = useTable(tableContext);

  const selectedRowKeysRef = computed(() => unref(selectedRowKeys));

  /**
   * 新增事件 - 跳转到JeecgBoot现有的在线表单编辑页面
   */
  function handleAdd() {
    // 使用JeecgBoot现有的路由跳转方式
    showMessage.info('请在系统管理->在线开发->表单配置中进行工作流配置');
  }

  /**
   * 编辑事件 - 跳转到JeecgBoot现有的在线表单编辑页面
   */
  function handleEdit(record: Recordable) {
    // 使用JeecgBoot现有的路由跳转方式
    showMessage.info('请在系统管理->在线开发->表单配置中进行工作流配置');
  }

  /**
   * 详情
   */
  function handleDetail(record: Recordable) {
    showMessage.info('请在系统管理->在线开发->表单配置中查看详情');
  }

  /**
   * 节点配置 - 跳转到现有的按钮配置页面
   */
  function handleNodeConfig(record: Recordable) {
    // 跳转到现有的按钮配置页面
    showMessage.info('请在系统管理->在线开发->自定义按钮中配置工作流按钮');
  }

  /**
   * 删除事件
   */
      async function handleDelete(record) {
      await deleteWorkflowConfig(record.id);
      handleSuccess();
    }

  /**
   * 批量删除事件
   */
      async function batchHandleDelete() {
      await deleteWorkflowConfig(selectedRowKeysRef.value.join(','));
      handleSuccess();
      // 清空选中行
      selectedRowKeys.value = [];
    }

  /**
   * 成功回调
   */
  function handleSuccess() {
    (selectedRowKeys.value = []) && reload();
  }

  /**
   * 导入成功回调
   */
  function handleImportSuccess(result) {
    showMessage.success(`导入成功！共导入 ${result.length} 条数据`);
    reload();
  }
</script> 