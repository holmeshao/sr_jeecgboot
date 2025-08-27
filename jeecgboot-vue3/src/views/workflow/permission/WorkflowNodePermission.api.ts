import { defHttp } from '/@/utils/http/axios';
import { useMessage } from '/@/hooks/web/useMessage';

const { createConfirm } = useMessage();

enum Api {
  list = '/workflow/onlineForm/node/list',
  save = '/workflow/onlineForm/node/add', 
  edit = '/workflow/onlineForm/node/edit',
  deleteOne = '/workflow/onlineForm/node/delete',
  deleteBatch = '/workflow/onlineForm/node/deleteBatch',
  importExcel = '/workflow/onlineForm/node/importExcel',
  exportXls = '/workflow/onlineForm/node/exportXls',
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

/**
 * 🎯 加载表单字段用于权限配置
 */
export const loadFormFieldsForPermission = async (formId: string, nodeId?: string) => {
  try {
    const response = await getFormFields(formId);
    const fields = response.result || [];
    
    // 如果提供了nodeId，可以获取该节点的现有权限配置
    if (nodeId) {
      const nodePermissions = await getByFormId(formId);
      const nodeConfig = nodePermissions.result?.find(item => item.nodeId === nodeId);
      
      return {
        fields,
        nodeConfig: nodeConfig || null
      };
    }
    
    return { fields, nodeConfig: null };
  } catch (error) {
    console.error('加载表单字段失败:', error);
    throw error;
  }
};

/**
 * 🎯 验证权限配置
 */
export const validatePermissionConfig = (config: {
  editableFields?: string[];
  readonlyFields?: string[];
  hiddenFields?: string[];
  requiredFields?: string[];
}) => {
  const errors: string[] = [];
  const warnings: string[] = [];
  
  const { editableFields = [], readonlyFields = [], hiddenFields = [], requiredFields = [] } = config;
  
  // 检查字段冲突
  const editableSet = new Set(editableFields);
  const readonlySet = new Set(readonlyFields);
  const hiddenSet = new Set(hiddenFields);
  const requiredSet = new Set(requiredFields);
  
  // 可编辑字段不能同时是只读字段
  editableSet.forEach(field => {
    if (readonlySet.has(field)) {
      errors.push(`字段 "${field}" 不能同时设置为可编辑和只读`);
    }
    if (hiddenSet.has(field)) {
      warnings.push(`字段 "${field}" 设置为可编辑但同时被隐藏`);
    }
  });
  
  // 必填字段不能是隐藏字段
  requiredSet.forEach(field => {
    if (hiddenSet.has(field)) {
      errors.push(`必填字段 "${field}" 不能设置为隐藏`);
    }
  });
  
  // 只读字段如果是必填的，给出警告
  readonlySet.forEach(field => {
    if (requiredSet.has(field)) {
      warnings.push(`只读字段 "${field}" 设置为必填可能无意义`);
    }
  });
  
  return {
    valid: errors.length === 0,
    errors,
    warnings
  };
};

/**
 * 🎯 生成权限预览
 */
export const generatePermissionPreview = (config: {
  editableFields?: string[];
  readonlyFields?: string[];
  hiddenFields?: string[];
  requiredFields?: string[];
  formMode?: string;
  conditionalPermissions?: string;
}) => {
  const { 
    editableFields = [], 
    readonlyFields = [], 
    hiddenFields = [], 
    requiredFields = [],
    formMode = 'EDIT',
    conditionalPermissions = ''
  } = config;
  
  // 生成预览数据
  const preview = {
    summary: {
      总字段数: editableFields.length + readonlyFields.length + hiddenFields.length,
      可编辑字段: editableFields.length,
      只读字段: readonlyFields.length,
      隐藏字段: hiddenFields.length,
      必填字段: requiredFields.length,
      表单模式: formMode
    },
    fieldDetails: {
      可编辑字段: editableFields,
      只读字段: readonlyFields,
      隐藏字段: hiddenFields,
      必填字段: requiredFields
    },
    conditionalRules: conditionalPermissions ? JSON.parse(conditionalPermissions) : null,
    
    // 生成权限规则描述
    rules: []
  };
  
  // 生成规则描述
  if (formMode === 'VIEW') {
    preview.rules.push('表单处于查看模式，所有字段默认只读');
  } else if (formMode === 'EDIT') {
    preview.rules.push('表单处于编辑模式，字段权限按配置执行');
  }
  
  if (editableFields.length > 0) {
    preview.rules.push(`可编辑字段：${editableFields.join(', ')}`);
  }
  
  if (readonlyFields.length > 0) {
    preview.rules.push(`只读字段：${readonlyFields.join(', ')}`);
  }
  
  if (hiddenFields.length > 0) {
    preview.rules.push(`隐藏字段：${hiddenFields.join(', ')}`);
  }
  
  if (requiredFields.length > 0) {
    preview.rules.push(`必填字段：${requiredFields.join(', ')}`);
  }
  
  if (conditionalPermissions) {
    preview.rules.push('包含条件权限规则');
  }
  
  return preview;
};

/**
 * 🎯 保存字段权限配置
 */
export const saveFieldPermissions = async (config: {
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
  id?: string;
}) => {
  try {
    // 验证配置
    const validation = validatePermissionConfig({
      editableFields: config.editableFields,
      readonlyFields: config.readonlyFields,
      hiddenFields: config.hiddenFields,
      requiredFields: config.requiredFields
    });
    
    if (!validation.valid) {
      throw new Error(`权限配置验证失败: ${validation.errors.join(', ')}`);
    }
    
    // 准备保存数据
    const saveData = {
      ...config,
      editableFields: JSON.stringify(config.editableFields || []),
      readonlyFields: JSON.stringify(config.readonlyFields || []),
      hiddenFields: JSON.stringify(config.hiddenFields || []),
      requiredFields: JSON.stringify(config.requiredFields || []),
      formMode: config.formMode || 'EDIT',
      conditionalPermissions: config.conditionalPermissions || ''
    };
    
    // 调用保存API
    const response = await saveNodePermission(saveData);
    
    if (response.success) {
      return response.result;
    } else {
      throw new Error(response.message || '保存失败');
    }
  } catch (error) {
    console.error('保存字段权限配置失败:', error);
    throw error;
  }
};