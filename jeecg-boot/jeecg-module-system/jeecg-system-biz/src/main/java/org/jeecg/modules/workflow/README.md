# JeecgBoot + Flowable 工作流集成说明

## 概述

本项目成功集成了Flowable工作流引擎到JeecgBoot的system模块中，用于支持复杂的业务流程管理，特别是维保业务流程。

## 集成内容

### 1. 依赖配置
- 添加了Flowable 7.0.0相关依赖到system-biz模块的pom.xml
- 包含flowable-spring-boot-starter和flowable-json-converter

### 2. 后端实现

#### 配置类
- `FlowableConfig.java`: Flowable引擎配置，包含数据库、历史记录、字体设置等

#### 控制器
- `WorkflowDefinitionController.java`: 流程定义管理
- `WorkflowInstanceController.java`: 流程实例管理
- `WorkflowTaskController.java`: 任务管理
- `MaintenanceWorkflowController.java`: 维保业务工作流专用控制器

#### 工作流委托类
- `WarrantyCheckDelegate.java`: 质保期检查业务逻辑

#### 流程定义
- `maintenance-order-process.bpmn20.xml`: 维保工单完整业务流程定义

### 3. 前端对接
- 更新了`src/api/workflow/index.ts`，添加了维保工作流的API接口
- 前端已有完整的工作流UI框架，包括流程定义、实例、任务管理等页面

## 维保工作流程

### 流程节点
1. **用户提交** - 客户提交维保申请
2. **维修人员审核** - 审核故障并提供解决方案
3. **质保期检查** - 系统自动检查是否在质保期内
4. **审批流程** - 质保期外需要经过多级审批
5. **派单** - 分配给合适的劳务班组
6. **维修执行** - 劳务班组进行实际维修
7. **质量验收** - 质量检查和验收
8. **客户评价** - 客户满意度评价

### 流程特点
- 支持条件分支（质保期内外不同流程）
- 支持多次维修（验收不通过可重新维修）
- 完整的历史记录和审计轨迹
- 灵活的任务分配和权限控制

## 技术特点

### 优势
1. **标准化流程管理**: 使用成熟的工作流引擎，流程定义标准化
2. **可视化设计**: 支持BPMN 2.0标准，可用流程设计器进行可视化设计
3. **完整的历史记录**: 所有操作都有完整的审计轨迹
4. **灵活的扩展**: 容易添加新的业务流程
5. **权限集成**: 与JeecgBoot的权限系统无缝集成

### 数据库表
Flowable会自动创建以下数据库表：
- `ACT_RE_*`: 流程定义和部署相关表
- `ACT_RU_*`: 运行时数据表
- `ACT_HI_*`: 历史数据表
- `ACT_GE_*`: 通用数据表

## 使用说明

### 启动流程
```java
// 启动维保工单流程
maintenanceWorkflowApi.startProcess(orderId)
```

### 完成任务
```java
// 维修人员审核
maintenanceWorkflowApi.technicianReview(taskId, {
  reviewResult: 'APPROVED',
  solution: '更换故障部件',
  estimatedCost: 1000,
  estimatedHours: 4
})
```

### 查询当前任务
```java
// 获取工单当前任务
maintenanceWorkflowApi.getCurrentTask(orderId)
```

## 扩展建议

1. **流程设计器**: 可集成Flowable的流程设计器，提供可视化流程设计功能
2. **表单集成**: 可与JeecgBoot的Online表单功能结合，动态生成任务表单
3. **消息通知**: 可集成短信、邮件等通知功能，任务状态变更时自动通知相关人员
4. **移动端**: JeecgUniapp中可开发工作流移动端页面，支持移动端审批
5. **报表统计**: 可基于工作流历史数据生成各种统计报表

## 注意事项

1. **数据库权限**: 确保数据库用户有创建表的权限
2. **事务管理**: Flowable与业务数据操作需要在同一事务中
3. **性能优化**: 大量流程实例时需要注意数据库性能优化
4. **版本兼容**: 注意Flowable版本与Spring Boot版本的兼容性