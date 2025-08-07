# DataIngest 使用指南

## 🚀 快速开始

### 前置条件
- ✅ JeecgBoot 平台已部署运行
- ✅ Redis 服务可用（集群协调）
- ✅ PostgreSQL 数据库可用（目标存储）
- ✅ 源数据库已开启 CDC 功能
- ✅ NiFi 服务可用（可选，用于后续处理）

### 部署步骤

#### 1. 数据库初始化
```sql
-- 执行数据库脚本，创建相关表
\i docs/data_ingest_moudle_ingest_task.sql
\i docs/data_ingest_moudle_data_source_config.sql
\i docs/data_ingest_moudle_data_cdc_table.sql
\i docs/data_ingest_moudle_field_mapping.sql
\i docs/data_ingest_moudle_ingest_log.sql
```

#### 2. 启动服务
```bash
# 启动 DataIngest 微服务
cd jeecg-boot-module-dataingest
mvn spring-boot:run

# 或使用 Docker 部署
docker-compose up -d
```

#### 3. 访问管理界面
```
http://localhost:8080/jeecg-boot/dataingest/task
```

## 📋 配置指南

### 步骤1：创建数据接入任务

通过 JeecgBoot 在线表单创建数据接入任务：

#### 主表配置（DataIngestMoudleIngestTask）
```json
{
  "taskName": "用户中心数据同步",
  "taskType": "MYSQL_CDC",
  "taskConfig": {
    "batchSize": 1000,
    "syncInterval": 5000,
    "retryTimes": 3,
    "enableDataQuality": true
  },
  "targetTableNamePre": "ods_",
  "scheduleConfig": {
    "enabled": true,
    "cronExpression": "0 */5 * * * ?"
  },
  "status": 1,
  "remark": "用户中心核心业务表实时同步"
}
```

**字段说明**：
- `taskName`：任务显示名称，便于识别
- `taskType`：任务类型，支持 MYSQL_CDC、POSTGRESQL_CDC、SQLSERVER_CDC
- `taskConfig`：JSON格式的任务详细配置
- `targetTableNamePre`：目标表名前缀，用于批量生成表名
- `scheduleConfig`：调度配置，支持定时执行
- `status`：任务状态，1=启用，0=禁用

### 步骤2：配置数据源

#### 子表1：数据源配置（DataIngestMoudleDataSourceConfig）
```json
{
  "sourceName": "用户中心MySQL数据源",
  "connectionConfig": {
    "hostname": "192.168.1.100",
    "port": "3306",
    "database": "user_center_db",
    "serverTimezone": "Asia/Shanghai",
    "useSSL": false,
    "characterEncoding": "utf8mb4"
  },
  "authConfig": {
    "username": "cdc_user",
    "password": "password123"
  },
  "tableNamePreDefault": "ods_user_",
  "remark": "用户中心主数据库，包含用户、角色等核心表"
}
```

**配置要点**：
- `connectionConfig`：数据库连接参数，JSON格式
- `authConfig`：认证信息，建议使用专门的CDC用户
- `tableNamePreDefault`：默认表名前缀，会与源表名拼接

#### MySQL CDC 用户权限配置
```sql
-- 创建专用CDC用户
CREATE USER 'cdc_user'@'%' IDENTIFIED BY 'password123';

-- 授予必要权限
GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'cdc_user'@'%';

-- 开启 binlog
SET GLOBAL binlog_format = 'ROW';
SET GLOBAL binlog_row_image = 'FULL';
```

### 步骤3：配置CDC表映射

#### 子表2：CDC表配置（DataIngestMoudleDataCdcTable）
```json
[
  {
    "sourceTableName": "user_info",
    "targetTableName": "ods_user_center_info",
    "businessDomain": "user_management",
    "nifiDwdProcessorId": "dwd-user-processor-001",
    "nifiDwsProcessorId": "dws-user-summary-001",
    "enableNifiNotify": 1,
    "nifiNotifyMode": 1,
    "notifyDelaySeconds": 0
  },
  {
    "sourceTableName": "user_profile", 
    "targetTableName": "ods_user_profile_data",
    "businessDomain": "user_management",
    "nifiDwdProcessorId": "dwd-profile-processor-001",
    "nifiDwsProcessorId": "dws-profile-summary-001",
    "enableNifiNotify": 1,
    "nifiNotifyMode": 2,
    "notifyDelaySeconds": 30
  }
]
```

**字段说明**：
- `sourceTableName`：源数据库中的表名
- `targetTableName`：PostgreSQL中的目标表名（优先级高于前缀拼接）
- `businessDomain`：业务域标识，用于数据分类和血缘追踪
- `nifiDwdProcessorId`：NiFi DWD层处理器ID
- `nifiDwsProcessorId`：NiFi DWS层处理器ID
- `enableNifiNotify`：是否启用NiFi通知（0=禁用，1=启用）
- `nifiNotifyMode`：通知模式（1=立即，2=批量，3=定时）
- `notifyDelaySeconds`：延迟通知秒数

### 步骤4：配置字段映射

#### 子表3：字段映射配置（DataIngestMoudleFieldMapping）
```json
[
  {
    "sourceFieldName": "user_id",
    "targetFieldName": "user_id",
    "fieldType": "BIGINT",
    "transformRule": "DIRECT",
    "isRequired": 1
  },
  {
    "sourceFieldName": "user_name",
    "targetFieldName": "user_name",
    "fieldType": "VARCHAR(100)",
    "transformRule": "TRIM_UPPER",
    "isRequired": 1
  },
  {
    "sourceFieldName": "phone",
    "targetFieldName": "phone_masked",
    "fieldType": "VARCHAR(20)",
    "transformRule": "PHONE_MASK",
    "isRequired": 0
  }
]
```

**转换规则类型**：
- `DIRECT`：直接映射，无转换
- `TRIM_UPPER`：去空格并转大写
- `PHONE_MASK`：手机号脱敏（保留前3位和后4位）
- `ID_CARD_MASK`：身份证脱敏
- `EMAIL_MASK`：邮箱脱敏
- `CUSTOM`：自定义转换规则

## 🔧 操作指南

### 启动CDC任务

#### 1. 通过管理界面启动
1. 登录 JeecgBoot 管理后台
2. 导航到「数据接入」->「任务管理」
3. 找到目标任务，点击「启动」按钮
4. 确认配置信息，点击「确定」

#### 2. 通过API启动
```bash
# 启动单个任务
curl -X POST "http://localhost:8080/jeecg-boot/dataingest/cluster/start/{taskId}" \
  -H "Content-Type: application/json" \
  -H "X-Access-Token: your-token" \
  -d '{
    "taskName": "用户中心数据同步",
    "connectionConfig": {...},
    "cdcTables": [...]
  }'

# 批量启动任务
curl -X POST "http://localhost:8080/jeecg-boot/dataingest/cluster/batch-start" \
  -H "Content-Type: application/json" \
  -H "X-Access-Token: your-token" \
  -d '{
    "taskIds": ["task_001", "task_002", "task_003"]
  }'
```

### 监控任务状态

#### 1. 实时状态查看
```bash
# 查看任务状态
curl -X GET "http://localhost:8080/jeecg-boot/dataingest/cluster/status/{taskId}" \
  -H "X-Access-Token: your-token"

# 查看集群状态
curl -X GET "http://localhost:8080/jeecg-boot/dataingest/cluster/list" \
  -H "X-Access-Token: your-token"
```

#### 2. 执行日志查看
```sql
-- 查看任务执行日志
SELECT * FROM data_ingest_moudle_ingest_log 
WHERE task_id = 'your_task_id' 
ORDER BY create_time DESC 
LIMIT 10;

-- 查看错误日志
SELECT * FROM data_ingest_moudle_ingest_log 
WHERE status = 0 AND error_message IS NOT NULL
ORDER BY create_time DESC;
```

### 停止CDC任务

#### 1. 优雅停止
```bash
# 停止单个任务
curl -X POST "http://localhost:8080/jeecg-boot/dataingest/cluster/stop/{taskId}" \
  -H "X-Access-Token: your-token"

# 停止所有任务
curl -X POST "http://localhost:8080/jeecg-boot/dataingest/cluster/stop-all" \
  -H "X-Access-Token: your-token"
```

#### 2. 强制停止
```bash
# 强制停止任务（紧急情况）
curl -X POST "http://localhost:8080/jeecg-boot/dataingest/cluster/force-stop/{taskId}" \
  -H "X-Access-Token: your-token"
```

## 📊 监控和运维

### 关键监控指标

#### 1. 业务指标
```sql
-- 任务执行成功率
SELECT 
    task_id,
    COUNT(*) as total_executions,
    SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as success_count,
    ROUND(SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) * 100.0 / COUNT(*), 2) as success_rate
FROM data_ingest_moudle_ingest_log 
WHERE create_time >= CURRENT_DATE - INTERVAL '7 days'
GROUP BY task_id;

-- 数据同步延迟统计
SELECT 
    task_id,
    AVG(EXTRACT(EPOCH FROM (end_time - start_time))) as avg_duration_seconds,
    MAX(EXTRACT(EPOCH FROM (end_time - start_time))) as max_duration_seconds
FROM data_ingest_moudle_ingest_log 
WHERE status = 1 AND create_time >= CURRENT_DATE - INTERVAL '24 hours'
GROUP BY task_id;
```

#### 2. 系统指标监控
```bash
# Redis连接状态
redis-cli info clients

# PostgreSQL连接状态  
psql -c "SELECT * FROM pg_stat_activity WHERE application_name LIKE '%dataingest%';"

# JVM内存使用情况
curl -X GET "http://localhost:8080/jeecg-boot/actuator/metrics/jvm.memory.used"
```

### 常见问题排查

#### 1. 任务启动失败
**问题现象**：任务状态显示启动失败
**排查步骤**：
```bash
# 1. 检查数据库连接
telnet source_db_host 3306

# 2. 检查用户权限
mysql -h source_db_host -u cdc_user -p -e "SHOW GRANTS;"

# 3. 检查binlog配置
mysql -h source_db_host -u cdc_user -p -e "SHOW VARIABLES LIKE 'binlog%';"

# 4. 查看详细错误日志
tail -f logs/dataingest.log | grep ERROR
```

#### 2. 数据同步延迟
**问题现象**：数据同步存在明显延迟
**排查步骤**：
```sql
-- 检查任务执行频率
SELECT task_id, AVG(record_count), AVG(EXTRACT(EPOCH FROM (end_time - start_time)))
FROM data_ingest_moudle_ingest_log 
WHERE create_time >= CURRENT_DATE - INTERVAL '1 hour'
GROUP BY task_id;

-- 检查是否有大事务阻塞
SELECT * FROM information_schema.innodb_trx WHERE trx_started < NOW() - INTERVAL 30 SECOND;
```

**优化建议**：
- 调整批量处理大小
- 增加并发处理线程
- 优化网络连接配置

#### 3. NiFi通知失败
**问题现象**：CDC数据已同步，但NiFi未收到通知
**排查步骤**：
```bash
# 1. 检查NiFi服务状态
curl -X GET "http://nifi-host:8080/nifi-api/system-diagnostics"

# 2. 检查处理器状态
curl -X GET "http://nifi-host:8080/nifi-api/processors/{processor-id}"

# 3. 查看通知日志
grep "NiFi notification" logs/dataingest.log
```

### 性能调优

#### 1. 批量处理优化
```json
{
  "taskConfig": {
    "batchSize": 2000,           // 增加批量大小
    "syncInterval": 3000,        // 减少同步间隔
    "maxRetryTimes": 5,          // 增加重试次数
    "connectionPoolSize": 20,    // 增加连接池大小
    "enableParallelProcessing": true  // 启用并行处理
  }
}
```

#### 2. 内存优化
```yaml
# application.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 30
      minimum-idle: 10
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000

# JVM参数优化
-Xms2g -Xmx4g -XX:+UseG1GC -XX:MaxGCPauseMillis=200
```

#### 3. 网络优化
```json
{
  "connectionConfig": {
    "hostname": "192.168.1.100",
    "port": "3306",
    "database": "user_center_db",
    "useSSL": false,
    "autoReconnect": true,
    "failOverReadOnly": false,
    "maxReconnects": 3,
    "initialTimeout": 2,
    "connectTimeout": 5000,
    "socketTimeout": 30000
  }
}
```

## 🔒 安全配置

### 1. 数据库安全
```sql
-- 限制CDC用户权限
CREATE USER 'cdc_user'@'app_server_ip' IDENTIFIED BY 'strong_password';
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'cdc_user'@'app_server_ip';

-- 启用SSL连接
ALTER USER 'cdc_user'@'app_server_ip' REQUIRE SSL;
```

### 2. 敏感数据处理
```json
{
  "fieldMappings": [
    {
      "sourceFieldName": "phone",
      "targetFieldName": "phone_masked", 
      "transformRule": "PHONE_MASK",
      "encryptionEnabled": true
    },
    {
      "sourceFieldName": "id_card",
      "targetFieldName": "id_card_hash",
      "transformRule": "HASH_SHA256",
      "encryptionEnabled": true
    }
  ]
}
```

### 3. 访问控制
```yaml
# 配置IP白名单
dataingest:
  security:
    allowedIps: 
      - "192.168.1.0/24"
      - "10.0.0.0/8"
    enableApiAuth: true
    tokenExpiration: 3600
```

## 📈 最佳实践

### 1. 任务设计原则
- **单一职责**：每个任务专注于特定的业务域
- **表分组**：相关表放在同一个任务中
- **合理批量**：根据数据量和延迟要求设置批量大小
- **错误隔离**：单表失败不影响其他表的同步

### 2. 监控告警设置
```sql
-- 设置关键指标告警
-- 1. 任务失败率超过5%
-- 2. 数据同步延迟超过10分钟  
-- 3. 单次处理时间超过5分钟
-- 4. 错误日志出现特定关键词
```

### 3. 运维建议
- **定期备份**：定期备份任务配置和执行日志
- **版本管理**：对任务配置进行版本控制
- **容量规划**：根据数据增长趋势规划存储和计算资源
- **灾备方案**：制定完整的灾备和恢复方案

### 4. 开发建议
- **测试先行**：在测试环境充分验证后再上生产
- **渐进发布**：先同步少量表，逐步扩大范围
- **监控优先**：优先建立监控体系，再进行功能扩展
- **文档同步**：及时更新配置文档和操作手册

通过以上配置和操作指南，您可以快速上手并高效使用 DataIngest 数据接入模块。如有问题，请参考故障排查章节或联系技术支持。