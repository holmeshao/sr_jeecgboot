# NiFi集成使用说明

## 功能概述

本功能实现了CDC数据变更后自动通知NiFi处理器的能力，支持数据从ODS层到DWD/DWS层的自动化处理流程。

## 核心流程

```
源数据库 → Debezium CDC → ODS层写入 → 触发NiFi处理器 → DWD/DWS层处理
```

## 配置步骤

### 1. 数据库字段升级

首先执行数据库迁移脚本：

```sql
-- 执行 docs/cdc_table_nifi_fields.sql
-- 为 data_ingest_module_data_cdc_table 表增加NiFi相关字段
```

### 2. 配置NiFi连接

在 `application.yml` 中配置NiFi连接信息：

```yaml
nifi:
  api:
    base-url: http://your-nifi-host:8080/nifi-api
    timeout: 5000
  notification:
    enabled: true
    async: true
```

### 3. 配置CDC表

在系统中为每个需要通知NiFi的表配置相关信息：

```json
{
  "sourceTableName": "contract_info",
  "targetTableName": "ods_contract_info",
  "taskId": "task_001",
  "businessDomain": "contract",
  "nifiDwdProcessorId": "dwd-contract-processor-001",
  "nifiDwsProcessorId": "dws-contract-summary-001",
  "enableNifiNotify": 1,
  "nifiNotifyMode": 1,
  "notifyDelaySeconds": 0
}
```

## 字段说明

### CDC表配置字段

| 字段名 | 类型 | 说明 | 示例值 |
|--------|------|------|--------|
| nifiDwdProcessorId | VARCHAR(100) | NiFi DWD处理器ID | "dwd-contract-processor-001" |
| nifiDwsProcessorId | VARCHAR(100) | NiFi DWS处理器ID | "dws-contract-summary-001" |
| businessDomain | VARCHAR(50) | 业务域标识 | "contract", "project", "finance" |
| enableNifiNotify | TINYINT(1) | 是否启用通知 | 0=禁用, 1=启用 |
| nifiNotifyMode | TINYINT(1) | 通知模式 | 1=立即, 2=批量, 3=定时 |
| notifyDelaySeconds | INT | 延迟秒数 | 0=立即, >0=延迟触发 |

### 通知模式说明

1. **立即通知（nifiNotifyMode=1）**: CDC事件发生后立即触发NiFi处理器
2. **批量通知（nifiNotifyMode=2）**: 收集一定数量的变更后批量通知（待实现）
3. **定时通知（nifiNotifyMode=3）**: 按固定时间间隔通知（待实现）

## 通知数据格式

NiFi处理器会接收到如下格式的JSON数据：

```json
{
  "processorId": "dwd-contract-processor-001",
  "data": {
    "layer": "DWD",
    "taskId": "task_001",
    "sourceTable": "contract_info",
    "targetTable": "ods_contract_info",
    "businessDomain": "contract",
    "operation": "c",
    "eventTimestamp": 1704067200000,
    "beforeData": {},
    "afterData": {
      "id": 1,
      "contract_name": "测试合同",
      "amount": 100000.00
    },
    "sourceInfo": {
      "db": "business_db",
      "table": "contract_info"
    },
    "processTime": 1704067201000
  },
  "timestamp": 1704067201000
}
```

## NiFi端配置

### 1. 创建接收处理器

在NiFi中创建HTTP接收处理器，监听来自DataIngest的通知：

```
HandleHttpRequest → ExtractText → RouteOnAttribute → 后续处理流程
```

### 2. 处理器配置示例

**HandleHttpRequest配置:**
- Listening Port: 8081
- HTTP Context Map: HttpContextMap
- Allowed Paths: /trigger.*

**ExtractText配置:**
- 提取业务字段: businessDomain, sourceTable, operation等

**RouteOnAttribute配置:**
- 根据业务域或表名路由到不同的处理流程

### 3. 业务处理流程示例

```
合同数据流程:
HandleHttpRequest → [businessDomain=contract] → 
清洗合同数据 → 写入DWD层 → 
汇总统计 → 写入DWS层
```

## 监控和日志

### 1. 应用日志

设置日志级别查看详细信息：

```yaml
logging:
  level:
    org.jeecg.dataingest.service.impl.NiFiNotificationServiceImpl: DEBUG
    org.jeecg.dataingest.debezium.DebeziumTaskManager: DEBUG
```

### 2. 关键日志内容

- CDC事件接收: `收到CDC事件: taskId={}, event={}`
- NiFi通知发送: `异步/同步触发NiFi处理器成功/失败: {}`
- 配置查询: `根据源表名查询CDC配置: {} -> {}`
- 数据构建: `已触发NiFi处理器: 表={}, DWD={}, DWS={}`

### 3. 错误处理

常见错误及解决方案：

| 错误信息 | 原因 | 解决方案 |
|----------|------|----------|
| "处理器ID为空" | CDC配置中未设置处理器ID | 在CDC表配置中设置nifiDwdProcessorId |
| "无法从CDC事件中提取表名" | CDC数据格式异常 | 检查Debezium配置和数据源连接 |
| "NiFi通知已禁用" | 配置中禁用了通知 | 设置nifi.notification.enabled=true |
| "连接超时" | NiFi服务不可达 | 检查网络连接和NiFi服务状态 |

## 性能优化

### 1. 异步通知

推荐使用异步通知模式，避免阻塞CDC处理：

```yaml
nifi:
  notification:
    async: true
```

### 2. 批量处理

对于高频变更的表，可以考虑批量通知模式（需要自定义实现）。

### 3. 索引优化

确保已创建必要的数据库索引：

```sql
-- 业务域索引
CREATE INDEX idx_business_domain ON data_ingest_module_data_cdc_table(business_domain);

-- 复合索引
CREATE INDEX idx_source_table_task ON data_ingest_module_data_cdc_table(source_table_name, task_id);
```

## 扩展功能

### 1. 自定义通知逻辑

可以继承`INiFiNotificationService`接口实现自定义通知逻辑：

```java
@Service
public class CustomNiFiNotificationServiceImpl implements INiFiNotificationService {
    // 自定义实现
}
```

### 2. 业务域映射

在配置文件中定义业务域到处理器的映射关系：

```yaml
nifi:
  business-domain-mapping:
    contract: "contract-domain-processor-id"
    project: "project-domain-processor-id"
```

### 3. 重试机制

目前支持基础的重试配置，可根据需要扩展：

```yaml
nifi:
  notification:
    retry-times: 3
    retry-interval: 1000
```

## 故障排查

1. **检查CDC任务状态**: 确保Debezium CDC任务正常运行
2. **验证NiFi连通性**: 测试NiFi API是否可访问
3. **检查表配置**: 确认CDC表配置正确且启用了通知
4. **查看日志**: 检查应用和NiFi的日志信息
5. **验证数据格式**: 确认传递给NiFi的数据格式正确

## 总结

通过以上配置，可以实现CDC数据变更后自动触发NiFi处理的完整流程。系统支持：

- ✅ 多种数据库CDC支持（MySQL、PostgreSQL、SQL Server）
- ✅ 灵活的通知模式（立即、批量、定时）
- ✅ 业务域级别的处理器管理
- ✅ 异步通知提升性能
- ✅ 完整的监控和日志
- ✅ 可扩展的架构设计

这样的设计既保持了您原有架构的简洁性，又提供了足够的灵活性来应对不同的业务场景。 