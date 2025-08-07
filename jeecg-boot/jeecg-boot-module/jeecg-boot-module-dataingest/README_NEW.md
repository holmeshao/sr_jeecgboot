# 数据接入模块重构版 - 使用指南

## 概述

本次重构完全基于用户需求，重点关注 Debezium CDC 功能，支持：
- ✅ **多数据库支持**: SQL Server、MySQL、PostgreSQL
- ✅ **JSON灵活配置**: 通过在线表单配置，JSON格式传递参数
- ✅ **插件化设计**: 通过 Java 增强自动调用 Controller
- ✅ **完整工作流**: 查证→编码→总结的完整实现

## 新架构特点

### 1. 表结构设计
- `data_ingest_module_ingest_task` - 主任务表
- `data_ingest_module_data_source_config` - 数据源配置表  
- `data_ingest_module_data_cdc_table` - CDC监听表配置
- `data_ingest_module_field_mapping` - 字段映射表

### 2. 核心组件
- `DebeziumTaskManager` - 强化的CDC任务管理器
- `DataIngestController` - 支持JSON配置的REST接口
- `DataIngestModuleIngestTaskService` - 完整的任务管理服务

## API 接口详细说明

### 1. 创建/更新CDC任务

**接口**: `POST /dataingest/task/saveOrUpdate`

**请求示例**:
```json
{
	"task_name": "sqlserver测试001",
	"task_type": "SQLSERVER_CDC",
	"target_table_name_pre": "ods_",
	"status": 1,
	"data_ingest_moudle_data_source_config": [
		{
			"source_name": "I8C的sqlserver环境",
			"connection_config": "{\"hostname\": \"192.168.1.100\", \"port\": \"1433\", \"database\": \"testdb\", \"username\": \"sa\", \"password\": \"password123\"}"
		}
	],
	"data_ingest_moudle_field_mapping": "",
	"data_ingest_moudle_data_cdc_table": [
		{
			"target_table_name": "test1",
			"source_table_name": "test1"
		},
		{
			"source_table_name": "test2"
		}
	]
}
```

**响应示例**:
```json
{
	"success": true,
	"message": "任务配置成功",
	"code": 200,
	"result": "1a2b3c4d5e6f7g8h9i0j",
	"timestamp": 1640995200000
}
```

### 2. MySQL CDC任务配置示例

```json
{
	"task_name": "MySQL CDC任务",
	"task_type": "MYSQL_CDC",
	"target_table_name_pre": "ods_",
	"status": 1,
	"data_ingest_moudle_data_source_config": [
		{
			"source_name": "MySQL生产环境",
			"connection_config": "{\"hostname\": \"mysql.example.com\", \"port\": \"3306\", \"database\": \"business_db\", \"username\": \"cdc_user\", \"password\": \"cdc_pass\"}"
		}
	],
	"data_ingest_moudle_data_cdc_table": [
		{
			"source_table_name": "business_db.orders",
			"target_table_name": "orders"
		},
		{
			"source_table_name": "business_db.customers",
			"target_table_name": "customers"
		}
	]
}
```

### 3. PostgreSQL CDC任务配置示例

```json
{
	"task_name": "PostgreSQL CDC任务",
	"task_type": "POSTGRESQL_CDC",
	"target_table_name_pre": "ods_",
	"status": 1,
	"data_ingest_moudle_data_source_config": [
		{
			"source_name": "PostgreSQL数据仓库",
			"connection_config": "{\"hostname\": \"pg.example.com\", \"port\": \"5432\", \"database\": \"warehouse\", \"username\": \"replicator\", \"password\": \"repl_pass\"}"
		}
	],
	"data_ingest_moudle_data_cdc_table": [
		{
			"source_table_name": "public.analytics_data",
			"target_table_name": "analytics_data"
		}
	]
}
```

### 4. 其他核心接口

#### 启动任务
```bash
POST /dataingest/task/start/{taskId}
```

#### 停止任务
```bash
POST /dataingest/task/stop/{taskId}
```

#### 删除任务
```bash
POST /dataingest/task/delete
Body: {"id": "taskId"}
```

#### 获取任务状态
```bash
GET /dataingest/task/status/{taskId}
```

#### 批量操作
```bash
POST /dataingest/task/startAll   # 启动所有启用任务
POST /dataingest/task/stopAll    # 停止所有任务
```

## 在线表单集成

### JeecgBoot 在线表单配置流程

1. **配置数据源表单**
   - 在 JeecgBoot 中创建对应的4张表单
   - 配置主子表关系
   - 设置字段验证规则

2. **Java 增强配置**
   
   在主任务表单的 Java 增强中添加：

```java
// 保存后增强
@Override  
public void afterAdd(DataIngestModuleIngestTask task) {
    // 构建完整配置JSON
    JSONObject config = buildTaskConfig(task);
    
    // 调用 DataIngest Controller
    try {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(config.toJSONString(), headers);
        
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:7009/dataingest/task/saveOrUpdate";
        
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("CDC任务自动配置成功: {}", task.getId());
        }
    } catch (Exception e) {
        log.error("CDC任务自动配置失败", e);
        throw new RuntimeException("CDC任务配置失败: " + e.getMessage());
    }
}

// 删除后增强
@Override
public void afterDelete(DataIngestModuleIngestTask task) {
    JSONObject deleteConfig = new JSONObject();
    deleteConfig.put("id", task.getId());
    
    // 调用删除接口...
}

private JSONObject buildTaskConfig(DataIngestModuleIngestTask task) {
    JSONObject config = new JSONObject();
    config.put("id", task.getId());
    config.put("task_name", task.getTaskName());
    config.put("task_type", task.getTaskType());
    config.put("target_table_name_pre", task.getTargetTableNamePre());
    config.put("status", task.getStatus());
    
    // 获取关联的数据源配置
    List<DataIngestModuleDataSourceConfig> dsConfigs = getDataSourceConfigs(task.getId());
    JSONArray dsArray = new JSONArray();
    for (DataIngestModuleDataSourceConfig ds : dsConfigs) {
        JSONObject dsJson = new JSONObject();
        dsJson.put("source_name", ds.getSourceName());
        dsJson.put("connection_config", ds.getConnectionConfig());
        dsArray.add(dsJson);
    }
    config.put("data_ingest_moudle_data_source_config", dsArray);
    
    // 获取CDC表配置...
    // 获取字段映射配置...
    
    return config;
}
```

## 多数据库支持详情

### 支持的数据库类型

| 数据库 | 任务类型 | 连接器类 | 特殊配置 |
|-------|---------|---------|---------|
| SQL Server | `SQLSERVER_CDC` | SqlServerConnector | 需要启用CDC |
| MySQL | `MYSQL_CDC` | MySqlConnector | 需要binlog权限 |
| PostgreSQL | `POSTGRESQL_CDC` | PostgresConnector | 需要逻辑复制 |

### 连接配置参数

#### SQL Server
```json
{
  "hostname": "sqlserver.example.com",
  "port": "1433",
  "database": "business_db",
  "username": "sa",
  "password": "password123"
}
```

#### MySQL
```json
{
  "hostname": "mysql.example.com", 
  "port": "3306",
  "database": "business_db",
  "username": "replicator",
  "password": "repl_password"
}
```

#### PostgreSQL
```json
{
  "hostname": "postgres.example.com",
  "port": "5432", 
  "database": "business_db",
  "username": "replicator",
  "password": "repl_password"
}
```

## 监控和管理

### 任务状态监控
- **RUNNING**: 任务正在运行
- **STOPPED**: 任务已停止
- **ERROR**: 任务运行出错

### 统计信息
```json
{
  "taskId": "1a2b3c4d5e6f7g8h9i0j",
  "processedCount": 1250,
  "errorCount": 3,
  "lastProcessTime": 1640995200000
}
```

### 日志查看
```bash
# 查看任务日志
tail -f logs/dataingest.log | grep "taskId=1a2b3c4d5e6f7g8h9i0j"

# 查看Redis中的任务状态
redis-cli get "debezium:status:1a2b3c4d5e6f7g8h9i0j"

# 查看任务统计
redis-cli get "debezium:statistics:1a2b3c4d5e6f7g8h9i0j"
```

## 部署配置

### 1. 环境要求
- JDK 17
- Redis
- PostgreSQL/MySQL (目标数据库)
- 源数据库 (SQL Server/MySQL/PostgreSQL)

### 2. 配置文件更新

在 `application.yml` 中添加：

```yaml
# Debezium配置 - 支持多数据库
org-jeecg-dataingest-debezium:
  common:
    offset-storage: "org.apache.kafka.connect.storage.FileOffsetBackingStore"
    offset-storage-file-path: "/tmp/debezium/offsets/"
    database-history: "io.debezium.relational.history.FileDatabaseHistory"
    database-history-file-path: "/tmp/debezium/history/"
    
# Redis配置 - 用于任务状态管理
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
```

### 3. 权限配置

#### SQL Server CDC权限
```sql
-- 启用数据库CDC
USE business_db;
EXEC sys.sp_cdc_enable_db;

-- 为表启用CDC
EXEC sys.sp_cdc_enable_table 
  @source_schema = N'dbo',
  @source_name = N'orders',
  @role_name = NULL;
```

#### MySQL Binlog权限
```sql
-- 创建CDC用户
CREATE USER 'cdc_user'@'%' IDENTIFIED BY 'cdc_password';

-- 授权
GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'cdc_user'@'%';

-- 启用binlog
SET GLOBAL binlog_format = 'ROW';
SET GLOBAL binlog_row_image = 'FULL';
```

#### PostgreSQL 逻辑复制权限
```sql
-- 创建复制用户
CREATE USER replicator WITH REPLICATION PASSWORD 'repl_password';

-- 授权
GRANT SELECT ON ALL TABLES IN SCHEMA public TO replicator;

-- 修改配置
-- postgresql.conf: wal_level = logical
-- pg_hba.conf: host replication replicator 0.0.0.0/0 md5
```

## 故障排查

### 常见问题

1. **任务启动失败**
   - 检查数据库连接配置
   - 确认CDC权限
   - 查看日志详细错误信息

2. **数据同步延迟**
   - 检查网络连接
   - 监控数据库负载
   - 调整Debezium缓冲区大小

3. **任务状态异常**
   - 检查Redis连接
   - 重启任务管理器
   - 清理Redis缓存

### 调试命令

```bash
# 检查任务状态
curl http://localhost:7009/dataingest/task/status/{taskId}

# 查看系统状态
curl http://localhost:7009/dataingest/system/status

# 重启特定任务
curl -X POST http://localhost:7009/dataingest/task/stop/{taskId}
curl -X POST http://localhost:7009/dataingest/task/start/{taskId}
```

## 总结

本次重构实现了您要求的所有功能：

1. ✅ **Debezium多数据库CDC支持** - SQL Server、MySQL、PostgreSQL
2. ✅ **JSON灵活配置** - 通过REST接口接收JSON参数
3. ✅ **在线表单集成** - 通过Java增强自动调用Controller  
4. ✅ **插件化设计** - 松耦合的模块化架构
5. ✅ **完整功能实现** - 从配置到监控的完整解决方案

整个系统现在完全支持您的使用场景，可以通过在线表单配置CDC任务，系统会自动处理Debezium的复杂配置，实现真正的"零代码"CDC配置体验。 