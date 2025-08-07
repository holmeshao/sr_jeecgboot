# Flowable 7.0 兼容性迁移指南

## 🎯 概述

本项目已完成Flowable 7.0兼容性升级，主要解决了API变化导致的编译错误和功能问题。

## 🔧 主要变化

### 1. 事件监听机制变化
- **之前**：自动监听流程部署事件 `@EventListener`
- **现在**：手动触发机制，基于IELE引擎架构

### 2. API修复
- `orderByDeploymentTime()` → `orderByProcessDefinitionVersion()`
- `getDbFieldComment()` → `getDbFieldTxt()`
- `getFormFields()` → 正确的查询方式

## 🚀 使用方式

### 后端API调用

#### 1. 手动触发流程部署事件处理
```bash
POST /jeecg-boot/workflow/triggerDeploymentEvent?processDefinitionKey=yourProcessKey
```

#### 2. 批量处理所有流程
```bash
POST /jeecg-boot/workflow/triggerAllDeploymentEvents
```

#### 3. 检查兼容性状态
```bash
GET /jeecg-boot/workflow/flowable7Status
```

### 前端调用示例

#### Vue3 + TypeScript
```typescript
// 在 src/api/workflow.ts 中添加新的API
export const triggerDeploymentEvent = (processDefinitionKey: string) => {
  return defHttp.post<string>({
    url: '/workflow/triggerDeploymentEvent',
    params: { processDefinitionKey }
  });
};

export const triggerAllDeploymentEvents = () => {
  return defHttp.post<string>({
    url: '/workflow/triggerAllDeploymentEvents'
  });
};

export const checkFlowable7Status = () => {
  return defHttp.get<string>({
    url: '/workflow/flowable7Status'
  });
};
```

#### 在组件中使用
```vue
<template>
  <div>
    <a-button @click="handleTriggerEvents">触发事件处理</a-button>
    <a-button @click="checkStatus">检查状态</a-button>
  </div>
</template>

<script setup lang="ts">
import { triggerAllDeploymentEvents, checkFlowable7Status } from '/@/api/workflow';
import { useMessage } from '/@/hooks/web/useMessage';

const { createMessage } = useMessage();

const handleTriggerEvents = async () => {
  try {
    await triggerAllDeploymentEvents();
    createMessage.success('批量处理完成');
  } catch (error) {
    createMessage.error('处理失败');
  }
};

const checkStatus = async () => {
  try {
    const result = await checkFlowable7Status();
    createMessage.info(result);
  } catch (error) {
    createMessage.error('检查失败');
  }
};
</script>
```

### 移动端调用 (UniApp)

```typescript
// 在 JeecgUniapp/src/api/workflow.ts 中添加
export const triggerDeploymentEventMobile = (processDefinitionKey: string) => {
  return http.post('/workflow/triggerDeploymentEvent', {
    processDefinitionKey
  });
};

// 在页面中使用
const handleDeployment = async () => {
  try {
    await triggerDeploymentEventMobile('orderProcess');
    uni.showToast({ title: '处理完成', icon: 'success' });
  } catch (error) {
    uni.showToast({ title: '处理失败', icon: 'error' });
  }
};
```

## ⚠️ 重要注意事项

### 1. 流程部署后必须手动触发
```bash
# 部署流程后，需要调用这个API来完成字段权限解析
curl -X POST "http://localhost:8080/jeecg-boot/workflow/triggerDeploymentEvent?processDefinitionKey=yourProcessKey"
```

### 2. 系统初始化时批量处理
```bash
# 系统启动后，建议执行一次批量处理
curl -X POST "http://localhost:8080/jeecg-boot/workflow/triggerAllDeploymentEvents"
```

### 3. 前端工作流表单组件无需调整
现有的 `WorkflowOnlineForm.vue` 和移动端组件无需修改，权限加载机制保持不变。

## 📋 迁移检查清单

### 后端
- [x] API兼容性修复
- [x] 事件监听机制适配
- [x] 新增WorkflowEventService
- [x] 更新部署流程提示
- [ ] 添加数据库初始化脚本（如需要）

### 前端
- [ ] 添加新的API接口调用（可选）
- [ ] 在管理页面添加手动触发按钮（推荐）
- [ ] 更新工作流部署页面的操作提示

### 移动端
- [ ] 添加移动端API调用（可选）
- [ ] 更新相关页面的操作流程

### 运维
- [ ] 更新部署文档
- [ ] 添加监控告警（可选）
- [ ] 更新API文档

## 🔮 未来规划

1. **完整的IELE引擎集成**：待Flowable 7.0 API文档完善后，实现完整的事件监听机制
2. **事件注册表API集成**：支持JMS、Kafka、RabbitMQ等消息源
3. **性能优化**：基于新架构的性能优化

## 📞 技术支持

如果在迁移过程中遇到问题，请：
1. 检查 `/workflow/flowable7Status` API的返回状态
2. 查看应用日志中的相关错误信息
3. 确认是否正确调用了手动触发API

## 📚 相关文档

- [Flowable 7.0 Release Notes](https://flowable.com/releases)
- [JeecgBoot 工作流集成文档](./workflow-integration.md)
- [IELE引擎使用指南](./iele-engine-guide.md)