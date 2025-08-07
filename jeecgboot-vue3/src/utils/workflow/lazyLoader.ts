import { ref, computed, watch, onMounted, onUnmounted, type Ref } from 'vue';

/**
 * 🎯 工作流懒加载管理器
 * 基于Vue 3组合式API的高性能懒加载解决方案
 */

// 懒加载状态枚举
export enum LoadState {
  IDLE = 'idle',
  LOADING = 'loading', 
  SUCCESS = 'success',
  ERROR = 'error'
}

// 懒加载配置
export interface LazyLoadConfig {
  immediate?: boolean;           // 是否立即加载
  retry?: number;               // 重试次数
  retryDelay?: number;          // 重试延迟（毫秒）
  timeout?: number;             // 超时时间（毫秒）
  cache?: boolean;              // 是否缓存结果
  deps?: Ref<any>[];           // 依赖项，变化时重新加载
}

// 懒加载结果接口
export interface LazyLoadResult<T> {
  data: Ref<T | null>;
  loading: Ref<boolean>;
  error: Ref<Error | null>;
  state: Ref<LoadState>;
  execute: () => Promise<T>;
  retry: () => Promise<T>;
  reset: () => void;
}

/**
 * 🎯 基础懒加载Hook
 */
export function useLazyLoad<T>(
  loader: () => Promise<T>,
  config: LazyLoadConfig = {}
): LazyLoadResult<T> {
  
  const {
    immediate = false,
    retry = 3,
    retryDelay = 1000,
    timeout = 10000,
    cache = true,
    deps = []
  } = config;

  // 状态管理
  const data = ref<T | null>(null);
  const loading = ref(false);
  const error = ref<Error | null>(null);
  const state = ref<LoadState>(LoadState.IDLE);
  
  // 内部状态
  const currentRetry = ref(0);
  const abortController = ref<AbortController | null>(null);
  const cachedResult = ref<T | null>(null);

  /**
   * 🎯 执行加载逻辑
   */
  const execute = async (): Promise<T> => {
    // 如果有缓存且启用缓存，直接返回
    if (cache && cachedResult.value !== null) {
      data.value = cachedResult.value;
      state.value = LoadState.SUCCESS;
      return cachedResult.value;
    }

    // 取消之前的请求
    if (abortController.value) {
      abortController.value.abort();
    }

    // 重置状态
    loading.value = true;
    error.value = null;
    state.value = LoadState.LOADING;
    abortController.value = new AbortController();

    try {
      // 设置超时
      const timeoutPromise = new Promise<never>((_, reject) => {
        setTimeout(() => reject(new Error('加载超时')), timeout);
      });

      // 执行加载
      const loadPromise = loader();
      const result = await Promise.race([loadPromise, timeoutPromise]);

      // 成功处理
      data.value = result;
      state.value = LoadState.SUCCESS;
      currentRetry.value = 0;

      // 缓存结果
      if (cache) {
        cachedResult.value = result;
      }

      return result;

    } catch (err) {
      const errorObj = err instanceof Error ? err : new Error(String(err));
      error.value = errorObj;
      state.value = LoadState.ERROR;
      
      console.error('懒加载失败:', errorObj);
      throw errorObj;

    } finally {
      loading.value = false;
      abortController.value = null;
    }
  };

  /**
   * 🎯 重试逻辑
   */
  const retryExecution = async (): Promise<T> => {
    if (currentRetry.value >= retry) {
      throw new Error(`重试次数已达上限 (${retry})`);
    }

    currentRetry.value++;
    
    // 延迟重试
    if (retryDelay > 0) {
      await new Promise(resolve => setTimeout(resolve, retryDelay * currentRetry.value));
    }

    return await execute();
  };

  /**
   * 🎯 重置状态
   */
  const reset = () => {
    data.value = null;
    loading.value = false;
    error.value = null;
    state.value = LoadState.IDLE;
    currentRetry.value = 0;
    
    if (abortController.value) {
      abortController.value.abort();
      abortController.value = null;
    }

    // 清除缓存
    cachedResult.value = null;
  };

  // 监听依赖变化
  if (deps.length > 0) {
    watch(deps, () => {
      reset();
      if (immediate) {
        execute();
      }
    }, { deep: true });
  }

  // 立即加载
  if (immediate) {
    execute();
  }

  // 清理资源
  onUnmounted(() => {
    if (abortController.value) {
      abortController.value.abort();
    }
  });

  return {
    data: data as Ref<T | null>,
    loading,
    error,
    state,
    execute,
    retry: retryExecution,
    reset
  };
}

/**
 * 🎯 表单配置懒加载Hook
 */
export function useLazyFormConfig(tableName: Ref<string>) {
  return useLazyLoad(
    async () => {
      const { defHttp } = await import('/@/utils/http/axios');
      const response = await defHttp.get({
        url: `/online/cgform/api/getFormItemBytbname/${tableName.value}`
      });
      return response;
    },
    {
      immediate: true,
      cache: true,
      deps: [tableName],
      timeout: 15000 // 表单配置加载超时时间稍长
    }
  );
}

/**
 * 🎯 表单数据懒加载Hook
 */
export function useLazyFormData(formId: Ref<string>, dataId: Ref<string>) {
  const shouldLoad = computed(() => formId.value && dataId.value);
  
  return useLazyLoad(
    async () => {
      if (!shouldLoad.value) return null;
      
      const { defHttp } = await import('/@/utils/http/axios');
      const response = await defHttp.get({
        url: `/online/cgform/api/form/${formId.value}/${dataId.value}`
      });
      return response;
    },
    {
      immediate: false,
      cache: true,
      deps: [formId, dataId]
    }
  );
}

/**
 * 🎯 工作流按钮懒加载Hook
 */
export function useLazyWorkflowButtons(taskId: Ref<string>, processInstanceId: Ref<string>) {
  const shouldLoad = computed(() => taskId.value || processInstanceId.value);

  return useLazyLoad(
    async () => {
      if (!shouldLoad.value) return [];
      
      const { generateWorkflowButtons } = await import('/@/utils/workflow/buttonManager');
      return await generateWorkflowButtons(
        taskId.value,
        processInstanceId.value
      );
    },
    {
      immediate: false,
      cache: false, // 按钮状态变化频繁，不缓存
      deps: [taskId, processInstanceId]
    }
  );
}

/**
 * 🎯 权限配置懒加载Hook
 */
export function useLazyPermissionConfig(nodeId: Ref<string>, formId: Ref<string>) {
  const shouldLoad = computed(() => nodeId.value && formId.value);

  return useLazyLoad(
    async () => {
      if (!shouldLoad.value) return null;
      
      const { defHttp } = await import('/@/utils/http/axios');
      const response = await defHttp.get({
        url: '/workflow/permission/node/detail',
        params: { 
          nodeId: nodeId.value, 
          formId: formId.value 
        }
      });
      return response;
    },
    {
      immediate: false,
      cache: true,
      deps: [nodeId, formId],
      timeout: 8000
    }
  );
}

/**
 * 🎯 字典数据懒加载Hook
 */
export function useLazyDictionary(dictCode: Ref<string>) {
  return useLazyLoad(
    async () => {
      const { defHttp } = await import('/@/utils/http/axios');
      const response = await defHttp.get({
        url: '/sys/dict/getDictItems/' + dictCode.value
      });
      return response;
    },
    {
      immediate: true,
      cache: true,
      deps: [dictCode],
      timeout: 5000
    }
  );
}

/**
 * 🎯 分页懒加载Hook
 */
export function useLazyPagination<T>(
  loader: (page: number, size: number) => Promise<{ records: T[], total: number }>,
  config: { pageSize?: number } = {}
) {
  const { pageSize = 10 } = config;
  
  const currentPage = ref(1);
  const total = ref(0);
  const records = ref<T[]>([]);
  const hasMore = computed(() => records.value.length < total.value);
  
  const { loading, error, execute: loadPage } = useLazyLoad(
    async () => {
      const result = await loader(currentPage.value, pageSize);
      
      if (currentPage.value === 1) {
        records.value = result.records;
      } else {
        records.value.push(...result.records);
      }
      
      total.value = result.total;
      return result;
    },
    {
      immediate: true
    }
  );

  const loadMore = async () => {
    if (loading.value || !hasMore.value) return;
    
    currentPage.value++;
    await loadPage();
  };

  const refresh = async () => {
    currentPage.value = 1;
    records.value = [];
    await loadPage();
  };

  return {
    records,
    loading,
    error,
    hasMore,
    total,
    currentPage,
    loadMore,
    refresh
  };
}

/**
 * 🎯 图片懒加载Hook
 */
export function useLazyImage(src: Ref<string>) {
  const imageRef = ref<HTMLImageElement>();
  const loaded = ref(false);
  const error = ref(false);

  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting && imageRef.value && src.value) {
          const img = new Image();
          img.onload = () => {
            if (imageRef.value) {
              imageRef.value.src = src.value;
              loaded.value = true;
            }
          };
          img.onerror = () => {
            error.value = true;
          };
          img.src = src.value;
          
          observer.unobserve(entry.target);
        }
      });
    },
    { threshold: 0.1 }
  );

  onMounted(() => {
    if (imageRef.value) {
      observer.observe(imageRef.value);
    }
  });

  onUnmounted(() => {
    observer.disconnect();
  });

  return {
    imageRef,
    loaded,
    error
  };
}

/**
 * 🎯 虚拟滚动懒加载Hook
 */
export function useVirtualList<T>(
  list: Ref<T[]>,
  options: {
    itemHeight: number;
    containerHeight: number;
    buffer?: number;
  }
) {
  const { itemHeight, containerHeight, buffer = 5 } = options;
  
  const scrollTop = ref(0);
  const visibleCount = Math.ceil(containerHeight / itemHeight);
  
  const visibleData = computed(() => {
    const start = Math.floor(scrollTop.value / itemHeight);
    const end = start + visibleCount + buffer;
    
    return {
      start: Math.max(0, start - buffer),
      end: Math.min(list.value.length, end),
      items: list.value.slice(
        Math.max(0, start - buffer),
        Math.min(list.value.length, end)
      )
    };
  });

  const totalHeight = computed(() => list.value.length * itemHeight);
  const offsetY = computed(() => visibleData.value.start * itemHeight);

  const onScroll = (e: Event) => {
    scrollTop.value = (e.target as HTMLElement).scrollTop;
  };

  return {
    visibleData,
    totalHeight,
    offsetY,
    onScroll
  };
}

/**
 * 🎯 组合懒加载Hook（用于复杂场景）
 */
export function useComposedLazyLoad<T extends Record<string, any>>(
  loaders: Record<keyof T, () => Promise<any>>,
  config: LazyLoadConfig = {}
) {
  const results = {} as Record<keyof T, LazyLoadResult<any>>;
  const allLoaded = ref(false);
  const anyLoading = ref(false);
  const anyError = ref(false);

  // 为每个加载器创建懒加载实例
  Object.keys(loaders).forEach((key) => {
    results[key as keyof T] = useLazyLoad(loaders[key as keyof T], {
      ...config,
      immediate: false
    });
  });

  // 计算组合状态
  const updateComposedState = () => {
    const loadResults = Object.values(results);
    anyLoading.value = loadResults.some(r => r.loading.value);
    anyError.value = loadResults.some(r => r.error.value !== null);
    allLoaded.value = loadResults.every(r => r.state.value === LoadState.SUCCESS);
  };

  // 监听所有加载器的状态变化
  Object.values(results).forEach((result) => {
    watch([result.loading, result.error, result.state], updateComposedState);
  });

  // 执行所有加载器
  const executeAll = async () => {
    const promises = Object.values(results).map(result => result.execute());
    return await Promise.allSettled(promises);
  };

  // 重置所有加载器
  const resetAll = () => {
    Object.values(results).forEach(result => result.reset());
    updateComposedState();
  };

  return {
    results,
    allLoaded,
    anyLoading,
    anyError,
    executeAll,
    resetAll
  };
}