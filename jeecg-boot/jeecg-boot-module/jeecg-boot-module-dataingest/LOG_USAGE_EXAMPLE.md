# DataIngest 日志记录功能使用说明

## 功能概述

现在DataIngest项目已经完整实现了日志记录功能，完全基于您现有的JeecgBoot在线表单设计：

1. **完整的日志表设计**：`data_ingest_moudle_ingest_log` 表记录任务执行的详细信息
2. **基于现有实体**：直接使用 `DataIngestMoudleIngestTask` 实体，无需额外转换
3. **实时日志记录**：CDC监听、任务执行过程中的实时日志记录
4. **数据血缘追踪**：记录数据来源、处理时间、任务ID等信息
5. **错误处理和统计**：记录成功/失败数量、错误信息等

## 日志表字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | varchar(32) | 主键 |
| task_id | varchar(32) | 任务ID |
| batch_id | varchar(32) | 执行批次ID |
| status | int4 | 执行状态：0-开始执行，1-执行中，2-执行成功，3-执行失败 |
| start_time | timestamp | 开始时间 |
| end_time | timestamp | 结束时间 |
| record_count | int8 | 处理记录数 |
| success_count | int8 | 成功记录数 |
| fail_count | int8 | 失败记录数 |
| error_message | text | 错误信息 |
| execute_log | text | 执行日志详情 |

## 使用方式

### 1. 启动CDC监听（带日志记录）

```bash
# 启动CDC监听
curl -X POST "http://localhost:8080/dataingest/task/cdc/start?taskId=CDC_TASK_001"

# 查看日志记录
SELECT * FROM data_ingest_moudle_ingest_log WHERE task_id = 'CDC_TASK_001';
```

### 2. 执行数据接入任务（带日志记录）

```bash
# 执行指定任务
curl -X POST "http://localhost:8080/dataingest/task/execute/your-task-id"

# 查看任务状态
curl -X GET "http://localhost:8080/dataingest/task/status/your-task-id"
```

### 3. 查看日志记录

#### 通过JeecgBoot在线表单查看
访问：`http://localhost:8080/jeecg-boot/dataingest/dataIngestMoudleIngestLog`

#### 通过SQL直接查询
```sql
-- 查看所有日志
SELECT * FROM data_ingest_moudle_ingest_log ORDER BY create_time DESC;

-- 查看特定任务的日志
SELECT * FROM data_ingest_moudle_ingest_log WHERE task_id = 'your-task-id';

-- 查看执行失败的任务
SELECT * FROM data_ingest_moudle_ingest_log WHERE status = 2 AND fail_count > 0;

-- 统计任务执行情况
SELECT 
    task_id,
    COUNT(*) as execution_count,
    SUM(record_count) as total_records,
    SUM(success_count) as total_success,
    SUM(fail_count) as total_failures,
    AVG(EXTRACT(EPOCH FROM (end_time - start_time))) as avg_duration_seconds
FROM data_ingest_moudle_ingest_log 
WHERE start_time >= CURRENT_DATE - INTERVAL '7 days'
GROUP BY task_id;
```

## 日志记录的关键位置

### 1. CDC事件处理日志
- **位置**：`DebeziumService.handleEvent()`
- **记录内容**：每个CDC事件的处理情况、成功/失败计数
- **更新频率**：每处理100条记录更新一次数据库

### 2. 任务执行日志
- **位置**：`DataIngestServiceImpl.executeTaskWithLog()`
- **记录内容**：任务开始、执行过程、结束状态
- **包含信息**：任务类型、执行结果、错误信息

### 3. 数据写入日志
- **位置**：`PostgresWriteServiceImpl` 各个写入方法
- **记录内容**：数据写入PostgreSQL的结果、NiFi通知状态

## 数据血缘追踪

每条写入PostgreSQL的数据都会包含以下血缘信息：

```sql
-- 数据血缘字段
data_source_task_id VARCHAR(64),     -- 来源任务ID
data_ingest_time TIMESTAMP,          -- 数据接入时间  
data_update_time TIMESTAMP,          -- 数据更新时间
is_deleted INTEGER DEFAULT 0,        -- 软删除标记
delete_time TIMESTAMP                 -- 删除时间
```

## 监控和告警

### 1. 任务执行监控
```sql
-- 监控最近1小时内失败的任务
SELECT task_id, error_message, create_time 
FROM data_ingest_moudle_ingest_log 
WHERE status = 2 AND fail_count > 0 
AND create_time >= NOW() - INTERVAL '1 hour';
```

### 2. 数据处理量监控
```sql
-- 监控每小时数据处理量
SELECT 
    DATE_TRUNC('hour', create_time) as hour,
    SUM(record_count) as total_records,
    COUNT(DISTINCT task_id) as active_tasks
FROM data_ingest_moudle_ingest_log 
WHERE create_time >= CURRENT_DATE
GROUP BY DATE_TRUNC('hour', create_time)
ORDER BY hour;
```

## 注意事项

1. **日志表维护**：建议定期清理历史日志数据，避免表过大影响性能
2. **索引优化**：已为 task_id、status、start_time 创建索引
3. **错误处理**：所有日志记录操作都有异常捕获，不会影响主业务流程
4. **性能考虑**：CDC事件处理中，每100条记录才更新一次数据库日志

## 扩展功能

### 1. 自定义日志级别
可以通过配置文件控制日志记录的详细程度：

```yaml
dataingest:
  log:
    level: DEBUG  # DEBUG, INFO, WARN, ERROR
    batch-size: 100  # 批量更新日志的记录数
```

### 2. 日志数据导出
支持通过JeecgBoot的Excel导出功能导出日志数据进行分析。

### 3. 实时监控接口
```bash
# 获取系统健康状态
curl -X GET "http://localhost:8080/dataingest/task/health"
```

这样就完成了完整的日志记录功能实现！