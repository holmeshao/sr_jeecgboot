import { defHttp } from '/@/utils/http/axios';

/**
 * 🎯 工作流权限相关API
 * 提供字段权限获取和应用的接口
 */

/**
 * 获取节点权限配置
 */
export function getNodePermissionConfig(params: {
  formId: string;
  processKey?: string;
  nodeId: string;
}) {
  return defHttp.get({
    url: '/workflow/permission/getNodePermission',
    params,
  });
}

/**
 * 应用权限配置到表单
 */
export function applyPermissionToFormConfig(formConfig: any, permissionConfig: any) {
  // 这是一个客户端函数，用于将权限配置应用到表单配置
  if (!formConfig || !permissionConfig) {
    return formConfig;
  }

  // 遍历表单字段，应用权限
  if (formConfig.schema && Array.isArray(formConfig.schema)) {
    formConfig.schema.forEach((field: any) => {
      const fieldName = field.key || field.dbFieldName;
      if (fieldName) {
        applyFieldPermission(field, fieldName, permissionConfig);
      }
    });
  }

  return formConfig;
}

/**
 * 应用单个字段权限
 */
function applyFieldPermission(field: any, fieldName: string, permissionConfig: any) {
  const { editableFields = [], readonlyFields = [], hiddenFields = [], requiredFields = [] } = permissionConfig;

  // 应用编辑权限
  if (editableFields.includes(fieldName)) {
    field.disabled = false;
    field.readonly = false;
  } else if (readonlyFields.includes(fieldName)) {
    field.disabled = true;
    field.readonly = true;
  }

  // 应用显示权限
  if (hiddenFields.includes(fieldName)) {
    field.hidden = true;
    field.visible = false;
  } else {
    field.hidden = false;
    field.visible = true;
  }

  // 应用必填权限
  if (requiredFields.includes(fieldName)) {
    field.required = true;
  }
}

/**
 * 获取智能默认权限配置
 */
export function getSmartDefaultPermission(params: {
  formId: string;
  nodeId: string;
}) {
  return defHttp.get({
    url: '/workflow/permission/getSmartDefault',
    params,
  });
}