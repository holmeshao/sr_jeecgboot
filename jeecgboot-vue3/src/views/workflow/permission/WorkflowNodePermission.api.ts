import { defHttp } from '/@/utils/http/axios';
import { useMessage } from '/@/hooks/web/useMessage';

const { createConfirm } = useMessage();

enum Api {
  list = '/workflow/node-permission/list',
  save = '/workflow/node-permission/add',
  edit = '/workflow/node-permission/edit',
  deleteOne = '/workflow/node-permission/delete',
  deleteBatch = '/workflow/node-permission/deleteBatch',
  importExcel = '/workflow/node-permission/importExcel',
  exportXls = '/workflow/node-permission/exportXls',
  getByFormId = '/workflow/node-permission/getByFormId',
  getByProcessKey = '/workflow/node-permission/getByProcessKey',
  getFormFields = '/workflow/node-permission/getFormFields',
  saveNodePermission = '/workflow/node-permission/saveNodePermission',
  batchSaveNodePermissions = '/workflow/node-permission/batchSaveNodePermissions',
  copyToNewVersion = '/workflow/node-permission/copyToNewVersion',
  generateDefaultPermission = '/workflow/node-permission/generateDefaultPermission',
}

/**
 * 🎯 导出API
 */
export const getExportUrl = Api.exportXls;

/**
 * 🎯 导入API
 */
export const getImportUrl = Api.importExcel;

/**
 * 🎯 列表接口
 */
export const list = (params) => defHttp.get({ url: Api.list, params });

/**
 * 🎯 删除单个
 */
export const deleteOne = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteOne, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};

/**
 * 🎯 批量删除
 */
export const batchDelete = (params, handleSuccess) => {
  createConfirm({
    iconType: 'warning',
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.deleteBatch, data: params }, { joinParamsToUrl: true }).then(() => {
        handleSuccess();
      });
    },
  });
};

/**
 * 🎯 保存或者更新
 */
export const saveOrUpdate = (params, isUpdate) => {
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params }, { isTransformResponse: false });
};

/**
 * 🎯 根据表单ID获取节点权限配置
 */
export const getByFormId = (formId: string) => {
  return defHttp.get({ url: Api.getByFormId, params: { formId } });
};

/**
 * 🎯 根据流程定义Key获取节点权限配置
 */
export const getByProcessKey = (processKey: string) => {
  return defHttp.get({ url: Api.getByProcessKey, params: { processKey } });
};

/**
 * 🎯 获取表单字段列表（用于权限配置）
 */
export const getFormFields = (formId: string) => {
  return defHttp.get({ url: Api.getFormFields, params: { formId } });
};

/**
 * 🎯 保存节点权限配置
 */
export const saveNodePermission = (params: {
  formId: string;
  processKey: string;
  nodeId: string;
  nodeName: string;
  editableFields?: string[];
  readonlyFields?: string[];
  hiddenFields?: string[];
  requiredFields?: string[];
  formMode?: string;
  conditionalPermissions?: string;
}) => {
  return defHttp.post({ url: Api.saveNodePermission, params });
};

/**
 * 🎯 批量保存节点权限配置
 */
export const batchSaveNodePermissions = (params: { formId: string; processKey: string; nodePermissions: Record<string, any> }) => {
  return defHttp.post({ url: Api.batchSaveNodePermissions, params });
};

/**
 * 🎯 复制权限配置到新版本
 */
export const copyToNewVersion = (sourceProcessKey: string, targetProcessKey: string) => {
  return defHttp.post({
    url: Api.copyToNewVersion,
    params: { sourceProcessKey, targetProcessKey },
  });
};

/**
 * 🎯 生成智能默认权限配置
 */
export const generateDefaultPermission = (formId: string, nodeId: string) => {
  return defHttp.get({
    url: Api.generateDefaultPermission,
    params: { formId, nodeId },
  });
};

/**
 * 🎯 权限配置相关的业务API
 */
export class PermissionConfigApi {
  /**
   * 获取表单的完整权限配置
   */
  static async getFormPermissionConfig(formId: string) {
    try {
      const [nodePermissions, formFields] = await Promise.all([getByFormId(formId), getFormFields(formId)]);

      return {
        nodePermissions: nodePermissions.result || [],
        formFields: formFields.result || [],
      };
    } catch (error) {
      console.error('获取表单权限配置失败:', error);
      throw error;
    }
  }

  /**
   * 获取流程的完整权限配置
   */
  static async getProcessPermissionConfig(processKey: string) {
    try {
      const response = await getByProcessKey(processKey);
      return response.result || [];
    } catch (error) {
      console.error('获取流程权限配置失败:', error);
      throw error;
    }
  }

  /**
   * 保存完整的表单权限配置
   */
  static async saveFormPermissionConfig(formId: string, processKey: string, nodePermissions: Record<string, any>) {
    try {
      const response = await batchSaveNodePermissions({
        formId,
        processKey,
        nodePermissions,
      });

      if (response.success) {
        return response.result;
      } else {
        throw new Error(response.message || '保存失败');
      }
    } catch (error) {
      console.error('保存表单权限配置失败:', error);
      throw error;
    }
  }

  /**
   * 导入权限配置模板
   */
  static async importPermissionTemplate(templateData: any) {
    // 这里可以实现权限配置模板的导入逻辑
    console.log('导入权限配置模板:', templateData);
  }

  /**
   * 导出权限配置模板
   */
  static async exportPermissionTemplate(formId: string) {
    try {
      const config = await this.getFormPermissionConfig(formId);

      // 构建导出数据
      const exportData = {
        formId,
        exportTime: new Date().toISOString(),
        nodePermissions: config.nodePermissions,
        formFields: config.formFields,
      };

      // 生成下载文件
      const blob = new Blob([JSON.stringify(exportData, null, 2)], {
        type: 'application/json',
      });

      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `workflow-permission-${formId}-${Date.now()}.json`;
      link.click();
      window.URL.revokeObjectURL(url);

      return exportData;
    } catch (error) {
      console.error('导出权限配置失败:', error);
      throw error;
    }
  }
}
