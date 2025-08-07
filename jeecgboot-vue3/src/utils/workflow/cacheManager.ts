/**
 * 🎯 工作流缓存管理器
 * 基于JeecgBoot应用场景，优化表单和工作流数据的缓存策略
 */

// 缓存项接口
interface CacheItem<T = any> {
  key: string;
  data: T;
  timestamp: number;
  expireTime: number;
  version?: string;
}

// 缓存配置
interface CacheConfig {
  maxSize?: number;           // 最大缓存数量
  defaultTTL?: number;        // 默认过期时间（毫秒）
  cleanupInterval?: number;   // 清理间隔（毫秒）
  enableCompression?: boolean; // 是否启用压缩
}

// 缓存类型枚举
export enum CacheType {
  FORM_CONFIG = 'form_config',        // 表单配置缓存
  FORM_DATA = 'form_data',           // 表单数据缓存
  WORKFLOW_BUTTONS = 'workflow_buttons', // 工作流按钮缓存
  PERMISSION_CONFIG = 'permission_config', // 权限配置缓存
  PROCESS_INFO = 'process_info',     // 流程信息缓存
  USER_INFO = 'user_info',           // 用户信息缓存
  DICTIONARY = 'dictionary'          // 字典数据缓存
}

/**
 * 🎯 工作流缓存管理器类
 */
export class WorkflowCacheManager {
  private cache: Map<string, CacheItem> = new Map();
  private config: Required<CacheConfig>;
  private cleanupTimer?: number;

  constructor(config: CacheConfig = {}) {
    this.config = {
      maxSize: config.maxSize || 100,
      defaultTTL: config.defaultTTL || 5 * 60 * 1000, // 5分钟
      cleanupInterval: config.cleanupInterval || 60 * 1000, // 1分钟
      enableCompression: config.enableCompression || false
    };

    this.startCleanupTimer();
  }

  /**
   * 🎯 生成缓存键
   */
  private generateKey(type: CacheType, identifier: string): string {
    return `${type}:${identifier}`;
  }

  /**
   * 🎯 设置缓存
   */
  set<T>(
    type: CacheType, 
    identifier: string, 
    data: T, 
    ttl?: number, 
    version?: string
  ): void {
    const key = this.generateKey(type, identifier);
    const now = Date.now();
    const expireTime = now + (ttl || this.config.defaultTTL);

    // 检查缓存大小限制
    if (this.cache.size >= this.config.maxSize) {
      this.evictLRU();
    }

    const cacheItem: CacheItem<T> = {
      key,
      data: this.config.enableCompression ? this.compress(data) : data,
      timestamp: now,
      expireTime,
      version
    };

    this.cache.set(key, cacheItem);
    
    console.debug(`缓存设置: ${key}`, { ttl, version });
  }

  /**
   * 🎯 获取缓存
   */
  get<T>(type: CacheType, identifier: string, version?: string): T | null {
    const key = this.generateKey(type, identifier);
    const item = this.cache.get(key);

    if (!item) {
      return null;
    }

    // 检查过期时间
    if (Date.now() > item.expireTime) {
      this.cache.delete(key);
      console.debug(`缓存过期删除: ${key}`);
      return null;
    }

    // 检查版本
    if (version && item.version && item.version !== version) {
      this.cache.delete(key);
      console.debug(`缓存版本不匹配删除: ${key}`, { 
        cached: item.version, 
        requested: version 
      });
      return null;
    }

    // 更新访问时间（LRU）
    item.timestamp = Date.now();
    
    console.debug(`缓存命中: ${key}`);
    return this.config.enableCompression ? this.decompress(item.data) : item.data;
  }

  /**
   * 🎯 检查缓存是否存在且有效
   */
  has(type: CacheType, identifier: string, version?: string): boolean {
    return this.get(type, identifier, version) !== null;
  }

  /**
   * 🎯 删除指定缓存
   */
  delete(type: CacheType, identifier: string): boolean {
    const key = this.generateKey(type, identifier);
    const result = this.cache.delete(key);
    console.debug(`缓存删除: ${key}`, { success: result });
    return result;
  }

  /**
   * 🎯 清除指定类型的所有缓存
   */
  clearType(type: CacheType): void {
    const keysToDelete = Array.from(this.cache.keys()).filter(key => 
      key.startsWith(`${type}:`)
    );
    
    keysToDelete.forEach(key => this.cache.delete(key));
    console.debug(`清除类型缓存: ${type}`, { count: keysToDelete.length });
  }

  /**
   * 🎯 清除所有缓存
   */
  clear(): void {
    this.cache.clear();
    console.debug('清除所有缓存');
  }

  /**
   * 🎯 获取缓存统计信息
   */
  getStats(): {
    size: number;
    maxSize: number;
    hitRate?: number;
    types: Record<string, number>;
  } {
    const types: Record<string, number> = {};
    
    for (const key of this.cache.keys()) {
      const type = key.split(':')[0];
      types[type] = (types[type] || 0) + 1;
    }

    return {
      size: this.cache.size,
      maxSize: this.config.maxSize,
      types
    };
  }

  /**
   * 🎯 LRU淘汰策略
   */
  private evictLRU(): void {
    let oldestKey = '';
    let oldestTime = Date.now();

    for (const [key, item] of this.cache.entries()) {
      if (item.timestamp < oldestTime) {
        oldestTime = item.timestamp;
        oldestKey = key;
      }
    }

    if (oldestKey) {
      this.cache.delete(oldestKey);
      console.debug(`LRU淘汰缓存: ${oldestKey}`);
    }
  }

  /**
   * 🎯 清理过期缓存
   */
  private cleanup(): void {
    const now = Date.now();
    const expiredKeys: string[] = [];

    for (const [key, item] of this.cache.entries()) {
      if (now > item.expireTime) {
        expiredKeys.push(key);
      }
    }

    expiredKeys.forEach(key => this.cache.delete(key));
    
    if (expiredKeys.length > 0) {
      console.debug('清理过期缓存', { count: expiredKeys.length });
    }
  }

  /**
   * 🎯 启动定时清理
   */
  private startCleanupTimer(): void {
    this.cleanupTimer = window.setInterval(() => {
      this.cleanup();
    }, this.config.cleanupInterval);
  }

  /**
   * 🎯 停止定时清理
   */
  destroy(): void {
    if (this.cleanupTimer) {
      clearInterval(this.cleanupTimer);
      this.cleanupTimer = undefined;
    }
    this.clear();
  }

  /**
   * 🎯 数据压缩（简单实现）
   */
  private compress<T>(data: T): string {
    try {
      return btoa(JSON.stringify(data));
    } catch (error) {
      console.warn('数据压缩失败', error);
      return data as any;
    }
  }

  /**
   * 🎯 数据解压
   */
  private decompress<T>(compressed: any): T {
    if (typeof compressed !== 'string') {
      return compressed;
    }
    
    try {
      return JSON.parse(atob(compressed));
    } catch (error) {
      console.warn('数据解压失败', error);
      return compressed;
    }
  }
}

// 全局缓存管理器实例
export const workflowCacheManager = new WorkflowCacheManager({
  maxSize: 200,
  defaultTTL: 10 * 60 * 1000, // 10分钟
  cleanupInterval: 2 * 60 * 1000, // 2分钟清理一次
  enableCompression: true
});

/**
 * 🎯 缓存装饰器工厂
 */
export function cached(
  type: CacheType, 
  ttl?: number, 
  keyGenerator?: (...args: any[]) => string
) {
  return function(target: any, propertyKey: string, descriptor: PropertyDescriptor) {
    const originalMethod = descriptor.value;

    descriptor.value = async function(...args: any[]) {
      const cacheKey = keyGenerator ? keyGenerator(...args) : JSON.stringify(args);
      
      // 尝试从缓存获取
      const cached = workflowCacheManager.get(type, cacheKey);
      if (cached !== null) {
        return cached;
      }

      // 执行原方法
      const result = await originalMethod.apply(this, args);
      
      // 缓存结果
      if (result !== null && result !== undefined) {
        workflowCacheManager.set(type, cacheKey, result, ttl);
      }

      return result;
    };

    return descriptor;
  };
}

/**
 * 🎯 特定场景的缓存辅助函数
 */
export const WorkflowCache = {
  
  /**
   * 表单配置缓存
   */
  formConfig: {
    get: (tableName: string) => workflowCacheManager.get(CacheType.FORM_CONFIG, tableName),
    set: (tableName: string, config: any) => workflowCacheManager.set(CacheType.FORM_CONFIG, tableName, config, 15 * 60 * 1000), // 15分钟
    delete: (tableName: string) => workflowCacheManager.delete(CacheType.FORM_CONFIG, tableName)
  },

  /**
   * 表单数据缓存（短期）
   */
  formData: {
    get: (formId: string, dataId: string) => workflowCacheManager.get(CacheType.FORM_DATA, `${formId}-${dataId}`),
    set: (formId: string, dataId: string, data: any) => workflowCacheManager.set(CacheType.FORM_DATA, `${formId}-${dataId}`, data, 5 * 60 * 1000), // 5分钟
    delete: (formId: string, dataId: string) => workflowCacheManager.delete(CacheType.FORM_DATA, `${formId}-${dataId}`)
  },

  /**
   * 工作流按钮缓存
   */
  buttons: {
    get: (taskId: string) => workflowCacheManager.get(CacheType.WORKFLOW_BUTTONS, taskId),
    set: (taskId: string, buttons: any[]) => workflowCacheManager.set(CacheType.WORKFLOW_BUTTONS, taskId, buttons, 3 * 60 * 1000), // 3分钟
    delete: (taskId: string) => workflowCacheManager.delete(CacheType.WORKFLOW_BUTTONS, taskId)
  },

  /**
   * 权限配置缓存
   */
  permissions: {
    get: (nodeId: string, formId: string) => workflowCacheManager.get(CacheType.PERMISSION_CONFIG, `${nodeId}-${formId}`),
    set: (nodeId: string, formId: string, permissions: any) => workflowCacheManager.set(CacheType.PERMISSION_CONFIG, `${nodeId}-${formId}`, permissions, 30 * 60 * 1000), // 30分钟
    delete: (nodeId: string, formId: string) => workflowCacheManager.delete(CacheType.PERMISSION_CONFIG, `${nodeId}-${formId}`)
  },

  /**
   * 字典数据缓存（长期）
   */
  dictionary: {
    get: (dictCode: string) => workflowCacheManager.get(CacheType.DICTIONARY, dictCode),
    set: (dictCode: string, data: any) => workflowCacheManager.set(CacheType.DICTIONARY, dictCode, data, 60 * 60 * 1000), // 1小时
    delete: (dictCode: string) => workflowCacheManager.delete(CacheType.DICTIONARY, dictCode)
  },

  /**
   * 清除与表单相关的所有缓存
   */
  clearFormRelated: (formId: string) => {
    workflowCacheManager.clearType(CacheType.FORM_CONFIG);
    workflowCacheManager.clearType(CacheType.FORM_DATA);
    workflowCacheManager.clearType(CacheType.PERMISSION_CONFIG);
  },

  /**
   * 清除与任务相关的所有缓存
   */
  clearTaskRelated: (taskId: string) => {
    workflowCacheManager.delete(CacheType.WORKFLOW_BUTTONS, taskId);
    workflowCacheManager.clearType(CacheType.PROCESS_INFO);
  }
};

// Vue 3 组合式API 的缓存Hook
export function useWorkflowCache() {
  return {
    cache: WorkflowCache,
    cacheManager: workflowCacheManager,
    
    // 获取缓存统计
    getCacheStats: () => workflowCacheManager.getStats(),
    
    // 清除所有缓存
    clearAllCache: () => workflowCacheManager.clear()
  };
}