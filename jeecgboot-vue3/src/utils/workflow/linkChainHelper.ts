/**
 * 链式关联辅助工具
 * 
 * 核心特性：
 * 1. 支持单级和多级链式关联
 * 2. 支持字段刷新机制
 * 3. 静默失败（查不出来返回空）
 * 4. 批量查询优化（列表页）
 * 
 * @author jeecg
 * @since 2024-12-26
 */

import { defHttp } from '/@/utils/http/axios';

/**
 * 链式关联配置
 */
export interface LinkChainConfig {
  tableName: string;      // 目标表名
  keyField: string;       // 关联字段
  resultField: string;    // 显示字段
  valueFromField: string; // 来源字段
}

/**
 * 多级链式关联配置
 */
export interface MultiLevelLinkChainConfig {
  chain: Array<{
    tableName: string;
    keyField: string;
    resultField: string;
  }>;
}

/**
 * 🎯 解析单个链式关联值
 * 
 * @param config 链式配置
 * @param keyValue 关联值
 * @returns 显示值
 */
export async function resolveLinkChainValue(
  config: LinkChainConfig,
  keyValue: any
): Promise<string | null> {
  try {
    if (!keyValue || keyValue === '') {
      return null;
    }

    const res = await defHttp.get<string>({
      url: '/workflow/onlineForm/linkChain/resolve',
      params: {
        tableName: config.tableName,
        keyField: config.keyField,
        keyValue: String(keyValue),
        resultField: config.resultField,
      },
    });

    return res || null;
  } catch (e) {
    console.warn('链式关联查询失败:', config, keyValue, e);
    return null; // 静默失败
  }
}

/**
 * 🎯 解析多级链式关联值
 * 
 * @param chainConfig 多级链式配置
 * @param initialValue 初始值
 * @returns 最终显示值
 */
export async function resolveMultiLevelLinkChain(
  chainConfig: MultiLevelLinkChainConfig,
  initialValue: any
): Promise<string | null> {
  try {
    if (!initialValue || initialValue === '') {
      return null;
    }

    const res = await defHttp.post<string>({
      url: '/workflow/onlineForm/linkChain/resolveMultiLevel',
      data: {
        chainConfig: JSON.stringify(chainConfig),
        initialValue: String(initialValue),
      },
    });

    return res || null;
  } catch (e) {
    console.warn('多级链式关联查询失败:', chainConfig, initialValue, e);
    return null; // 静默失败
  }
}

/**
 * 🎯 刷新表单中的所有链式关联字段
 * 
 * @param tableName 表名
 * @param formData 表单数据
 * @returns 刷新后的表单数据
 */
export async function refreshLinkChainFields(
  tableName: string,
  formData: Record<string, any>
): Promise<Record<string, any>> {
  try {
    const res = await defHttp.post<Record<string, any>>({
      url: '/workflow/onlineForm/linkChain/refresh',
      data: {
        tableName,
        formData,
      },
    });

    return res || formData;
  } catch (e) {
    console.warn('刷新链式关联字段失败:', tableName, e);
    return formData; // 静默失败，返回原数据
  }
}

/**
 * 🎯 解析字段的链式关联配置
 * 
 * @param field 字段配置
 * @returns 链式配置或 null
 */
export function parseLinkChainConfig(field: any): LinkChainConfig | null {
  try {
    // 检查是否是链式关联字段
    if (field.view !== 'link_table_chain' && field.dbType !== 'link_table_chain') {
      return null;
    }

    // 解析 fieldExtendJson
    let valueFromField = '';
    if (field.fieldExtendJson) {
      try {
        const ext = JSON.parse(field.fieldExtendJson);
        valueFromField = ext.valueFromField || '';
      } catch (e) {
        console.warn('解析 fieldExtendJson 失败:', field.fieldName, e);
      }
    }

    if (!valueFromField) {
      return null;
    }

    return {
      tableName: field.dictTable,
      keyField: field.dictField,
      resultField: field.dictText,
      valueFromField,
    };
  } catch (e) {
    console.warn('解析链式关联配置失败:', field, e);
    return null;
  }
}

/**
 * 🎯 解析多级链式关联配置
 * 
 * @param field 字段配置
 * @returns 多级链式配置或 null
 */
export function parseMultiLevelLinkChainConfig(field: any): {
  config: MultiLevelLinkChainConfig;
  valueFromField: string;
} | null {
  try {
    if (!field.fieldExtendJson) {
      return null;
    }

    const ext = JSON.parse(field.fieldExtendJson);
    if (!ext.chain || !Array.isArray(ext.chain)) {
      return null;
    }

    return {
      config: { chain: ext.chain },
      valueFromField: ext.valueFromField || '',
    };
  } catch (e) {
    console.warn('解析多级链式关联配置失败:', field, e);
    return null;
  }
}

/**
 * 🎯 批量填充链式关联字段（用于列表页）
 * 
 * 注意：列表页的批量填充由后端 Java 增强机制处理
 * 前端只需要在表单详情页处理单条记录的链式关联
 */
export async function fillLinkChainFieldsForForm(
  fields: any[],
  formData: Record<string, any>
): Promise<void> {
  // 收集所有链式关联字段
  const linkChainFields: Array<{
    field: any;
    config: LinkChainConfig;
  }> = [];

  for (const field of fields) {
    const config = parseLinkChainConfig(field);
    if (config) {
      linkChainFields.push({ field, config });
    }
  }

  if (linkChainFields.length === 0) {
    return;
  }

  // 并发查询所有链式关联字段
  await Promise.all(
    linkChainFields.map(async ({ field, config }) => {
      try {
        const keyValue = formData[config.valueFromField];
        if (keyValue !== undefined && keyValue !== null && keyValue !== '') {
          const resultValue = await resolveLinkChainValue(config, keyValue);
          formData[field.fieldName] = resultValue;
        } else {
          formData[field.fieldName] = null;
        }
      } catch (e) {
        console.warn('填充链式关联字段失败:', field.fieldName, e);
        formData[field.fieldName] = null; // 静默失败
      }
    })
  );
}

/**
 * 🎯 监听来源字段变化，自动更新链式关联字段
 * 
 * @param fields 字段配置列表
 * @param formData 表单数据
 * @param changedField 变化的字段名
 */
export async function handleLinkChainFieldChange(
  fields: any[],
  formData: Record<string, any>,
  changedField: string
): Promise<void> {
  // 查找依赖于该字段的链式关联字段
  const dependentFields = fields.filter((field) => {
    const config = parseLinkChainConfig(field);
    return config && config.valueFromField === changedField;
  });

  if (dependentFields.length === 0) {
    return;
  }

  // 并发更新所有依赖字段
  await Promise.all(
    dependentFields.map(async (field) => {
      try {
        const config = parseLinkChainConfig(field);
        if (!config) return;

        const keyValue = formData[changedField];
        if (keyValue !== undefined && keyValue !== null && keyValue !== '') {
          const resultValue = await resolveLinkChainValue(config, keyValue);
          formData[field.fieldName] = resultValue;
        } else {
          formData[field.fieldName] = null;
        }
      } catch (e) {
        console.warn('更新链式关联字段失败:', field.fieldName, e);
        formData[field.fieldName] = null; // 静默失败
      }
    })
  );
}

/**
 * 🎯 创建链式关联字段的刷新函数
 * 
 * @param tableName 表名
 * @param formData 表单数据
 * @returns 刷新函数
 */
export function createLinkChainRefresher(
  tableName: string,
  formData: Record<string, any>
) {
  return async () => {
    try {
      const refreshedData = await refreshLinkChainFields(tableName, formData);
      // 更新 formData
      Object.assign(formData, refreshedData);
      return true;
    } catch (e) {
      console.warn('刷新链式关联字段失败:', e);
      return false;
    }
  };
}
