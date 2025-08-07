# JeecgBoot DataIngest 数据接入模块 - 完整概览

## 🎯 模块简介

DataIngest 是基于 JeecgBoot 在线表单设计的企业级数据接入解决方案，专门用于实现 CDC（Change Data Capture）数据同步。该模块完全基于 JeecgBoot 生态，无需额外的 Kafka 等中间件，直接支持 Debezium + PostgreSQL + NiFi 的完整数据处理链路。

## 🏗️ 核心架构

### 数据流转架构
```
用户配置任务 → Debezium监听CDC → 数据保存PostgreSQL → 通知NiFi处理
```

### 技术栈组合
- **前端配置**：JeecgBoot 在线表单设计器
- **CDC监听**：Debezium（支持 MySQL、PostgreSQL、SQL Server）
- **数据存储**：PostgreSQL（目标数据湖）
- **后续处理**：NiFi（DWD/DWS 层处理）
- **集群协调**：Redis（分布式任务管理）

## 📊 核心实体设计

### 主表：DataIngestMoudleIngestTask（任务配置表）
**作用**：存储数据接入任务的主要配置信息

| 字段名 | 类型 | 说明 | 备注 |
|--------|------|------|------|
| taskName | String | 任务名称 | 用户友好的任务标识 |
| taskType | String | 任务类型 | 字典：data_ingest_task_type |
| taskConfig | String | 任务配置 | JSON格式的详细配置 |
| targetTableNamePre | String | 目标表名前缀 | 用于批量表名生成 |
| scheduleConfig | String | 调度配置 | 定时任务配置 |
| status | Integer | 任务状态 | 启用/禁用状态 |
| executeCount | Integer | 执行次数 | 统计字段 |
| successCount | Integer | 成功次数 | 统计字段 |
| failCount | Integer | 失败次数 | 统计字段 |

### 子表1：DataIngestMoudleDataSourceConfig（数据源配置表）
**作用**：配置CDC监听的数据源连接信息

| 字段名 | 类型 | 说明 | 示例值 |
|--------|------|------|--------|
| sourceName | String | 数据源名称 | "用户中心MySQL数据源" |
| connectionConfig | String | 连接配置 | JSON：hostname、port、database |
| authConfig | String | 认证配置 | JSON：username、password |
| tableNamePreDefault | String | 默认表名前缀 | "ods_user_" |

### 子表2：DataIngestMoudleDataCdcTable（CDC表配置）
**作用**：具体配置每个需要监听的源表和目标表的映射关系

| 字段名 | 类型 | 说明 | 备注 |
|--------|------|------|------|
| sourceTableName | String | 源表名 | 源数据库中的表名 |
| targetTableName | String | 目标表名 | PostgreSQL中的目标表名 |
| businessDomain | String | 业务域标识 | 用于数据分类和血缘追踪 |
| nifiDwdProcessorId | String | NiFi DWD处理器ID | 数据仓库明细层处理 |
| nifiDwsProcessorId | String | NiFi DWS处理器ID | 数据仓库汇总层处理 |
| enableNifiNotify | Integer | 是否启用NiFi通知 | 0=禁用，1=启用 |
| nifiNotifyMode | Integer | NiFi通知模式 | 1=立即，2=批量，3=定时 |
| notifyDelaySeconds | Integer | 延迟通知秒数 | 0表示立即触发 |

### 子表3：DataIngestMoudleFieldMapping（字段映射表）
**作用**：配置源表字段到目标表字段的映射关系和转换规则

### 日志表：DataIngestMoudleIngestLog（执行日志表）
**作用**：记录每次数据接入任务的执行情况

| 字段名 | 类型 | 说明 | 用途 |
|--------|------|------|------|
| taskId | String | 关联任务ID | 与主表关联 |
| batchId | String | 执行批次 | 批次追踪 |
| status | Integer | 执行状态 | 成功/失败状态 |
| recordCount | Integer | 处理记录数 | 统计信息 |
| successCount | Integer | 成功记录数 | 统计信息 |
| failCount | Integer | 失败记录数 | 统计信息 |
| errorMessage | String | 错误信息 | 失败时的详细错误 |
| executeLog | String | 执行日志 | 详细的执行过程记录 |

## 🔧 核心技术实现

### 1. Debezium集群任务管理器（DebeziumClusterTaskManager）
**核心功能**：
- ✅ 支持集群模式的CDC任务管理
- ✅ 基于Redis的分布式协调和锁机制
- ✅ 支持MySQL、PostgreSQL、SQL Server CDC
- ✅ 自动故障转移和负载均衡
- ✅ 实时任务状态监控和统计
- ✅ 任务生命周期管理（启动/停止/重启）

**技术特点**：
- 无需Kafka，直接基于Debezium Engine
- Redis分布式锁确保任务不重复执行
- 支持多节点部署和动态扩缩容
- 完整的任务状态同步机制

### 2. NiFi通知服务（INiFiNotificationService）
**核心功能**：
- ✅ CDC数据变更后自动通知NiFi
- ✅ 支持DWD（数据仓库明细层）处理器触发
- ✅ 支持DWS（数据仓库汇总层）处理器触发
- ✅ 多种通知模式（立即/批量/定时）
- ✅ NiFi处理器状态检查和监控
- ✅ 延迟通知机制优化性能

**集成方式**：
- RESTful API调用NiFi处理器
- 支持处理器组和单个处理器控制
- 完整的错误处理和重试机制

## 🎨 设计亮点

### 1. 完全基于JeecgBoot在线表单
- ✅ 利用在线表单设计器快速配置数据接入任务
- ✅ 主子表结构支持复杂的一对多关系配置
- ✅ 支持Excel导入导出、权限控制等标准功能
- ✅ 代码生成器生成完整的CRUD功能
- ✅ 完整的审计日志和操作记录

### 2. 智能表名生成策略
**两级优先级配置**：
- **优先级1**：CDC表一对一精确配置（targetTableName字段）
- **优先级2**：数据源统一前缀配置（tableNamePreDefault + sourceTableName）

**示例**：
```sql
-- 优先级1：精确控制
user_info -> ods_user_center_info

-- 优先级2：自动拼接
user_profile -> ods_user_ + user_profile = ods_user_user_profile
```

### 3. 数据血缘追溯
**ODS表自动包含血缘字段**：
- `source_database`：源数据库名
- `source_table`：源表名  
- `business_domain`：业务域
- `data_lineage`：完整血缘路径
- `transformation_rules`：转换规则记录
- `data_quality_score`：数据质量评分
- `sync_timestamp`：同步时间戳
- `cdc_operation`：CDC操作类型（CREATE/UPDATE/DELETE）
- `record_status`：记录状态（ACTIVE/DELETED）

### 4. 软删除支持
CDC DELETE事件不会真正删除数据，而是标记为软删除：
```sql
UPDATE ods_user_info SET 
    record_status = 'DELETED',
    cdc_operation = 'DELETE',
    update_time = CURRENT_TIMESTAMP,
    sync_timestamp = 1704067200000
WHERE id = 'primary_key_value';
```

### 5. 集群化部署支持
- ✅ 基于Redis的分布式任务管理
- ✅ 支持多节点负载均衡和故障转移
- ✅ 任务状态实时同步和监控
- ✅ 自动节点发现和健康检查
- ✅ 分布式锁确保任务执行唯一性

## 🚀 核心优势

### 1. 零中间件依赖
- ❌ 无需Kafka消息队列
- ❌ 无需额外的任务调度系统
- ✅ 直接基于Debezium Engine
- ✅ 利用现有的Redis和PostgreSQL

### 2. 企业级特性
- ✅ 完整的权限控制和审计
- ✅ 多租户支持
- ✅ 集群部署和高可用
- ✅ 完善的监控和告警
- ✅ 数据质量检查和评分

### 3. 开发效率
- ✅ 基于JeecgBoot快速开发
- ✅ 在线表单可视化配置
- ✅ 代码生成器自动生成标准功能
- ✅ 完整的前后端分离架构

### 4. 运维友好
- ✅ 完整的执行日志和统计
- ✅ 可视化的任务监控界面
- ✅ 灵活的告警通知机制
- ✅ 简单的部署和配置

## 📋 适用场景

### 1. 数据湖建设
- 实时同步业务数据到数据湖
- 支持多源数据统一接入
- 完整的数据血缘和质量管控

### 2. 实时数据仓库
- CDC实时同步到ODS层
- 自动触发DWD/DWS层处理
- 支持Lambda架构和Kappa架构

### 3. 微服务数据同步
- 跨服务的数据实时同步
- 事件驱动的数据更新
- 服务间数据一致性保证

### 4. 数据迁移和备份
- 大规模数据迁移
- 实时数据备份
- 跨环境数据同步

## 🎯 总结

DataIngest模块是一个设计精良、功能完整的企业级数据接入解决方案。它完美结合了JeecgBoot的快速开发能力和现代数据处理技术，为企业提供了一个无需复杂中间件、易于部署和维护的CDC数据同步方案。

无论是数据湖建设、实时数据仓库，还是微服务数据同步，该模块都能提供稳定、高效、可扩展的解决方案。