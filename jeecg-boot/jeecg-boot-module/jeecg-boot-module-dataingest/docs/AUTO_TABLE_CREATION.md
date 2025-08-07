# 自动建表功能使用说明

## 功能概述

自动建表功能能够根据 Debezium CDC 监听到的数据自动创建 PostgreSQL ODS 层表，无需手动建表，提高数据接入的灵活性。

## 支持的配置模式

### 模式1：配置了表映射和字段映射

当您在 `data_ingest_module_field_mapping` 表中配置了字段映射时，系统会根据映射配置自动建表。

**配置示例：**
```sql
INSERT INTO data_ingest_module_field_mapping (
    id, task_id, source_field, target_field, field_type, is_required, default_value
) VALUES 
('1', 'task_001', 'user_id', 'user_id', 'INTEGER', 'true', NULL),
('2', 'task_001', 'user_name', 'user_name', 'VARCHAR(100)', 'true', NULL),
('3', 'task_001', 'email', 'email', 'VARCHAR(255)', 'false', NULL),
('4', 'task_001', 'create_date', 'create_date', 'TIMESTAMP', 'false', 'CURRENT_TIMESTAMP');
```

**生成的表结构：**
```sql
CREATE TABLE ods_user_info (
    id VARCHAR(32) PRIMARY KEY,           -- JeecgBoot雪花算法ID
    user_id INTEGER NOT NULL,             -- 映射配置的字段
    user_name VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- ODS层元数据
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    batch_id VARCHAR(32),
    source_system VARCHAR(50)
);
```

### 模式2：只配置了CDC源表名

当您只在 `data_ingest_module_data_cdc_table` 表中配置了源表名时，系统会根据 CDC 事件中的字段信息自动推断表结构。

**配置示例：**
```sql
INSERT INTO data_ingest_module_data_cdc_table (
    id, task_id, source_table_name, target_table_name
) VALUES 
('1', 'task_002', 'user_info', 'ods_user_info');
```

**自动推断逻辑：**
- `INTEGER` → PostgreSQL `INTEGER`
- `LONG/BIGINT` → PostgreSQL `BIGINT`
- `DOUBLE/FLOAT` → PostgreSQL `DECIMAL(20,6)`
- `BOOLEAN` → PostgreSQL `BOOLEAN`
- `DATE/TIME` → PostgreSQL `TIMESTAMP`
- `STRING` → PostgreSQL `VARCHAR(255/500/1000)` 或 `TEXT`
- 未知类型 → PostgreSQL `VARCHAR(500)`

## 关键特性

### 1. JeecgBoot ID 方案
- 使用雪花算法生成唯一ID：`IdWorker.getIdStr()`
- 兼容JeecgBoot的统一ID方案

### 2. ODS层标准字段
每个自动创建的表都包含以下ODS层标准字段：
```sql
id VARCHAR(32) PRIMARY KEY,              -- 雪花算法ID
create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 创建时间
update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 更新时间  
batch_id VARCHAR(32),                    -- 批次ID
source_system VARCHAR(50)                -- 源系统标识
```

### 3. 并发安全
- 使用 `ConcurrentHashMap` 缓存表存在状态
- 表级锁机制避免并发创建同一张表
- 线程安全的表创建过程

### 4. 字段类型智能推断
- 根据CDC数据的实际值推断PostgreSQL字段类型
- 字符串长度自适应（VARCHAR(255) → VARCHAR(1000) → TEXT）
- 支持常见数据类型的自动映射

## 使用步骤

### 1. 配置CDC任务
```java
// 创建CDC任务配置
DebeziumTaskConfig config = new DebeziumTaskConfig();
config.setTaskId("auto_table_task");
config.setTaskName("自动建表CDC任务");
config.setTaskType("MYSQL_CDC");

// 配置数据源
JSONArray dataSourceConfigs = new JSONArray();
JSONObject dataSourceConfig = new JSONObject();
dataSourceConfig.put("hostname", "localhost");
dataSourceConfig.put("port", "3306");
dataSourceConfig.put("username", "root");
dataSourceConfig.put("password", "password");
dataSourceConfig.put("database", "source_db");
dataSourceConfigs.add(dataSourceConfig);
config.setDataSourceConfigs(dataSourceConfigs);
```

### 2. 配置CDC表映射
```java
// 配置CDC表
JSONArray cdcTables = new JSONArray();
JSONObject cdcTable = new JSONObject();
cdcTable.put("sourceTableName", "user_info");
cdcTable.put("targetTableName", "ods_user_info");
cdcTables.add(cdcTable);
config.setCdcTables(cdcTables);
```

### 3. 启动CDC任务
```java
// 启动任务，自动建表功能已内置
debeziumTaskManager.startOrRestartTask("auto_table_task", config);
```

### 4. 验证自动建表
```sql
-- 检查表是否自动创建
SELECT table_name, column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'ods_user_info' 
ORDER BY ordinal_position;
```

## 配置参数

### 字段类型映射配置
支持以下字段类型的自动转换：
```java
"STRING" → "VARCHAR(500)"
"TEXT" → "TEXT"  
"INT" → "INTEGER"
"LONG" → "BIGINT"
"DOUBLE" → "DECIMAL(20,6)"
"BOOLEAN" → "BOOLEAN"
"DATE" → "TIMESTAMP"
```

### 默认字段长度
- 短字符串（≤255字符）：`VARCHAR(255)`
- 中等字符串（256-1000字符）：`VARCHAR(1000)`
- 长字符串（>1000字符）：`TEXT`
- 未知类型：`VARCHAR(500)`

## 监控和日志

### 关键日志
```
INFO  - 表不存在，开始自动创建: ods_user_info
INFO  - 为表 ods_user_info 构建列定义: {user_id=INTEGER, user_name=VARCHAR(100), ...}
INFO  - 创建ODS表成功: ods_user_info
INFO  - CDC数据自动建表并写入ODS层成功: 源表=user_info, 目标表=ods_user_info, 操作=CREATE
```

### 性能监控
- 表存在性检查被缓存，避免重复查询
- 建表操作加锁，确保并发安全
- 失败重试机制，提高可靠性

## 注意事项

1. **字段名命名规范**：建议使用下划线命名风格，符合PostgreSQL规范
2. **字段类型一致性**：同一字段在不同记录中的类型应保持一致
3. **表名长度限制**：PostgreSQL表名长度限制为63字符
4. **权限要求**：确保数据库用户有CREATE TABLE权限
5. **字段动态增加**：目前不支持已存在表的字段动态增加，后续版本会支持

## 扩展功能（规划中）

- [ ] 支持已存在表的字段动态增加（ALTER TABLE）
- [ ] 支持更多数据类型的智能推断
- [ ] 支持表结构变更的版本管理
- [ ] 支持分区表的自动创建
- [ ] 支持索引的自动创建

## 故障排除

### 常见问题

**Q: 表创建失败，提示权限不足**
A: 确保PostgreSQL用户具有CREATE TABLE权限

**Q: 字段类型推断不准确**  
A: 可以通过字段映射配置明确指定字段类型

**Q: 并发创建表时出现冲突**
A: 系统已内置表级锁，如果仍有问题请检查数据库连接池配置

**Q: ODS表缺少某些字段**
A: 检查CDC事件中是否包含该字段的数据，空字段不会被自动创建 