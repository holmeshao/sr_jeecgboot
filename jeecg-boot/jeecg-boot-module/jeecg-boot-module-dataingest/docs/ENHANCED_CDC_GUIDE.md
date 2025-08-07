# 增强版CDC数据接入功能使用指南

## 功能升级概述

在原有自动建表功能基础上，新增了数据血缘追溯和灵活的表名配置策略，让您的CDC数据接入更加智能和可追溯。

## 🚀 核心特性

### 1. 智能表名生成策略
**两级优先级配置，极大提升灵活性：**

**优先级1：CDC表一对一配置**
```sql
-- 精确控制每个表的目标表名
INSERT INTO data_ingest_module_data_cdc_table (
    id, task_id, source_table_name, target_table_name
) VALUES 
('1', 'task_001', 'user_info', 'ods_user_center_info'),
('2', 'task_001', 'user_profile', 'ods_user_profile_data');
```

**优先级2：数据源配置默认前缀**
```sql
-- 为数据源设置统一前缀，自动拼接源表名
INSERT INTO data_ingest_module_data_source_config (
    id, source_name, table_name_pre_default, database_name, business_domain
) VALUES 
('ds_001', '用户中心数据源', 'ods_user_', 'user_center_db', 'user_management');

-- 自动生成：user_info -> ods_user_user_info
-- 自动生成：user_profile -> ods_user_user_profile
```

### 2. 完整数据血缘追溯
**ODS表自动包含以下血缘字段：**

| 字段名 | 类型 | 说明 | 示例值 |
|-------|------|------|--------|
| `source_database` | VARCHAR(100) | 源数据库名 | user_center_db |
| `source_table` | VARCHAR(100) | 源表名 | user_info |
| `business_domain` | VARCHAR(50) | 业务域 | user_management |
| `data_lineage` | TEXT | 完整血缘路径 | 数据源: 用户中心 -> 数据库: user_center_db -> 源表: user_info -> 目标表: ods_user_info -> ODS层 |
| `transformation_rules` | TEXT | 转换规则记录 | JSON格式的转换配置 |
| `data_quality_score` | DECIMAL(3,2) | 数据质量评分 | 1.00 |
| `sync_timestamp` | BIGINT | 同步时间戳 | 1704067200000 |
| `cdc_operation` | VARCHAR(10) | CDC操作类型 | CREATE/UPDATE/DELETE |
| `task_id` | VARCHAR(32) | 任务ID | task_001 |
| `data_source_id` | VARCHAR(32) | 数据源ID | ds_001 |
| `record_status` | VARCHAR(10) | 记录状态 | ACTIVE/DELETED |

### 3. 软删除支持
CDC DELETE事件不会真正删除数据，而是标记为软删除：
```sql
-- 软删除逻辑
UPDATE ods_user_info SET 
    record_status = 'DELETED',
    cdc_operation = 'DELETE',
    update_time = CURRENT_TIMESTAMP,
    sync_timestamp = 1704067200000
WHERE id = 'primary_key_value';
```

### 4. 自动索引创建
每个ODS表自动创建性能优化索引：
```sql
CREATE INDEX idx_tablename_source_table ON tablename(source_table);
CREATE INDEX idx_tablename_batch_id ON tablename(batch_id);
CREATE INDEX idx_tablename_create_time ON tablename(create_time);
CREATE INDEX idx_tablename_business_domain ON tablename(business_domain);
CREATE INDEX idx_tablename_task_id ON tablename(task_id);
CREATE INDEX idx_tablename_record_status ON tablename(record_status);
```

## 📋 配置步骤

### 步骤1：配置数据源（新增前缀配置）

```sql
-- 执行数据库升级脚本
\i docs/data_source_config_enhanced.sql

-- 配置数据源
INSERT INTO data_ingest_module_data_source_config (
    id, source_name, source_type, table_name_pre_default, 
    database_name, business_domain, connection_config, auth_config, 
    status, remark, create_by, create_time
) VALUES (
    'mysql_user_center', 
    '用户中心MySQL数据源', 
    'MYSQL_CDC', 
    'ods_user_',                    -- 关键：表名前缀配置
    'user_center_db', 
    'user_management',
    '{"hostname":"192.168.1.100","port":"3306","database":"user_center_db"}',
    '{"username":"cdc_user","password":"password123"}',
    1,
    '用户中心CDC数据源，使用ods_user_前缀',
    'admin',
    CURRENT_TIMESTAMP
);
```

### 步骤2：配置CDC任务

```java
// 创建CDC任务配置
DebeziumTaskConfig config = new DebeziumTaskConfig();
config.setTaskId("user_center_cdc");
config.setTaskName("用户中心CDC任务");
config.setTaskType("MYSQL_CDC");

// 配置CDC表（可选，不配置则使用前缀策略）
JSONArray cdcTables = new JSONArray();

// 情况A：精确配置（优先级高）
JSONObject userInfoTable = new JSONObject();
userInfoTable.put("sourceTableName", "user_info");
userInfoTable.put("targetTableName", "ods_user_center_info"); // 精确指定
cdcTables.add(userInfoTable);

// 情况B：不配置目标表名，使用前缀策略
JSONObject userProfileTable = new JSONObject();
userProfileTable.put("sourceTableName", "user_profile");
// 不设置targetTableName，将自动生成：ods_user_user_profile
cdcTables.add(userProfileTable);

config.setCdcTables(cdcTables);
```

### 步骤3：启动任务并验证

```java
// 启动CDC任务
debeziumTaskManager.startOrRestartTask("user_center_cdc", config);

// 验证表自动创建
```

**验证生成的表结构：**
```sql
-- 检查自动创建的表
SELECT table_name, column_name, data_type, is_nullable, column_default
FROM information_schema.columns 
WHERE table_name IN ('ods_user_center_info', 'ods_user_user_profile')
ORDER BY table_name, ordinal_position;

-- 检查数据血缘信息
SELECT source_database, source_table, target_table, business_domain, 
       data_lineage, cdc_operation, sync_timestamp
FROM ods_user_center_info 
LIMIT 5;
```

## 📊 使用场景示例

### 场景1：多业务域统一配置

```sql
-- 用户域数据源
INSERT INTO data_ingest_module_data_source_config (
    source_name, table_name_pre_default, business_domain
) VALUES 
('用户中心', 'ods_user_', 'user_management');

-- 订单域数据源  
INSERT INTO data_ingest_module_data_source_config (
    source_name, table_name_pre_default, business_domain
) VALUES 
('订单中心', 'ods_order_', 'order_management');

-- 自动生成结果：
-- user_info -> ods_user_user_info (业务域: user_management)
-- order_main -> ods_order_order_main (业务域: order_management)
```

### 场景2：混合配置策略

```sql
-- 数据源配置通用前缀
table_name_pre_default = 'ods_erp_'

-- CDC表配置
INSERT INTO data_ingest_module_data_cdc_table VALUES
('1', 'task_001', 'important_table', 'ods_business_critical_data'), -- 精确配置
('2', 'task_001', 'normal_table', NULL);                            -- 使用前缀: ods_erp_normal_table
```

### 场景3：数据血缘查询

```sql
-- 查询数据流向
SELECT DISTINCT 
    source_database,
    source_table,
    COUNT(*) as record_count,
    business_domain,
    MAX(sync_timestamp) as latest_sync
FROM ods_user_user_info 
WHERE record_status = 'ACTIVE'
GROUP BY source_database, source_table, business_domain;

-- 查询数据质量
SELECT 
    business_domain,
    AVG(data_quality_score) as avg_quality,
    COUNT(CASE WHEN record_status = 'DELETED' THEN 1 END) as deleted_count,
    COUNT(*) as total_count
FROM ods_user_user_info 
GROUP BY business_domain;
```

## 🔧 配置参数说明

### 数据源配置参数

| 参数 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| `table_name_pre_default` | VARCHAR(50) | 否 | 默认表名前缀 | `'ods_user_'` |
| `database_name` | VARCHAR(100) | 否 | 源数据库名 | `'user_center_db'` |
| `business_domain` | VARCHAR(50) | 否 | 业务域标识 | `'user_management'` |
| `data_quality_rules` | TEXT | 否 | 数据质量规则 | JSON配置 |
| `transformation_rules` | TEXT | 否 | 转换规则 | JSON配置 |

### 表名生成规则

1. **优先使用CDC表配置**：`target_table_name`
2. **使用前缀配置**：`table_name_pre_default` + `source_table_name`  
3. **默认策略**：`'ods_' + source_table_name`
4. **名称规范化**：转小写、替换特殊字符、限制长度

### 数据血缘字段自动填充

```java
// 自动填充逻辑
data.put("source_database", dataSourceConfig.getDatabaseName());
data.put("source_table", cdcConfig.getSourceTableName());
data.put("business_domain", dataSourceConfig.getBusinessDomain());
data.put("data_lineage", buildDataLineage(dataSourceConfig, cdcConfig));
data.put("cdc_operation", operation); // CREATE/UPDATE/DELETE
data.put("sync_timestamp", System.currentTimeMillis());
data.put("data_quality_score", 1.00); // 默认满分
```

## 🚨 注意事项

1. **表名长度限制**：PostgreSQL表名最大63字符，系统自动截取
2. **前缀命名规范**：建议使用 `ods_业务域_` 格式
3. **数据血缘性能**：大数据量时建议定期归档血缘数据
4. **软删除策略**：定期清理DELETED状态的数据
5. **索引维护**：定期分析索引使用情况，优化性能

## 💡 最佳实践

1. **分业务域配置前缀**：不同业务域使用不同前缀，便于管理
2. **重要表精确配置**：核心业务表使用精确的目标表名配置
3. **数据血缘监控**：建立血缘数据的监控和告警机制
4. **质量评分标准**：制定统一的数据质量评分标准
5. **定期维护**：定期检查和优化ODS表结构和索引

## 🔍 故障排除

### 常见问题

**Q: 表名生成不符合预期**
```sql
-- 检查配置优先级
SELECT task_id, source_table_name, target_table_name 
FROM data_ingest_module_data_cdc_table 
WHERE task_id = 'your_task_id';

SELECT table_name_pre_default, database_name 
FROM data_ingest_module_data_source_config 
WHERE task_id = 'your_task_id';
```

**Q: 数据血缘信息缺失**
```sql
-- 检查血缘字段填充
SELECT source_database, source_table, business_domain, data_lineage
FROM your_ods_table 
WHERE data_lineage IS NULL OR source_table IS NULL;
```

**Q: 软删除不生效**
```bash
# 检查日志
grep "软删除" /var/log/jeecg-boot/dataingest.log
```

您的CDC数据接入平台现在具备了完整的数据血缘追溯和灵活的表名配置能力！🎉 