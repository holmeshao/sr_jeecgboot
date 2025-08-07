# CDC到ODS层数据写入配置示例

## 功能说明

现在CDC数据已经可以自动写入到PostgreSQL ODS层！当CDC事件发生时，系统会：

1. ✅ **接收CDC事件** - Debezium捕获数据库变更
2. ✅ **写入ODS层** - 自动写入到PostgreSQL目标表
3. ✅ **触发NiFi** - 通知NiFi进行DWD/DWS处理

## 配置步骤

### 1. 确保CDC表配置正确

在 `data_ingest_module_data_cdc_table` 表中配置CDC表信息：

```sql
INSERT INTO data_ingest_module_data_cdc_table (
    id,
    source_table_name,      -- 源表名（必须）
    target_table_name,      -- ODS目标表名（必须）
    task_id,               -- 任务ID（必须）
    business_domain,       -- 业务域（可选）
    enable_nifi_notify,    -- 是否启用NiFi通知
    nifi_dwd_processor_id, -- DWD处理器ID
    create_time,
    create_by
) VALUES (
    'cdc_001',
    'contract_info',           -- 源表：合同信息表
    'ods_contract_info',       -- ODS表：会自动创建并写入数据
    'task_001',               
    'contract',               -- 合同业务域
    1,                        -- 启用NiFi通知
    'dwd-contract-processor-001',
    NOW(),
    'system'
);
```

### 2. 创建ODS目标表

系统会自动处理数据写入，但你需要先创建ODS表结构：

```sql
-- 创建ODS合同信息表
CREATE TABLE ods_contract_info (
    id VARCHAR(32) PRIMARY KEY,           -- 系统自动生成
    contract_id VARCHAR(50),              -- 来自源表数据
    contract_name VARCHAR(200),           -- 来自源表数据
    customer_name VARCHAR(100),           -- 来自源表数据
    amount DECIMAL(18,2),                 -- 来自源表数据
    status VARCHAR(20),                   -- 来自源表数据
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 系统自动添加
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 系统自动添加
    batch_id VARCHAR(50),                 -- 系统自动添加（批次追踪）
    source_system VARCHAR(50)             -- 系统自动添加（来源标识='CDC'）
);
```

### 3. 数据写入流程

当源表 `contract_info` 发生变更时：

1. **Debezium捕获变更**：
   ```json
   {
     "op": "c",  // 操作类型：c=创建, u=更新, d=删除
     "after": {
       "contract_id": "CT001",
       "contract_name": "测试合同",
       "customer_name": "测试客户",
       "amount": 100000.00,
       "status": "active"
     },
     "source": {
       "table": "contract_info"
     }
   }
   ```

2. **自动写入ODS层**：
   ```sql
   INSERT INTO ods_contract_info (
       id, contract_id, contract_name, customer_name, amount, status,
       create_time, update_time, batch_id, source_system
   ) VALUES (
       'auto_generated_id',  -- 系统生成32位ID
       'CT001',              -- 来自CDC数据
       '测试合同',            -- 来自CDC数据
       '测试客户',            -- 来自CDC数据
       100000.00,           -- 来自CDC数据
       'active',            -- 来自CDC数据
       '2025-01-01 10:00:00', -- 系统时间
       '2025-01-01 10:00:00', -- 系统时间
       'task_001_1704085200000', -- 批次ID：任务ID_时间戳
       'CDC'                -- 固定值，标识来源
   );
   ```

3. **触发NiFi处理**（如果启用）：
   - 发送HTTP通知到NiFi DWD处理器
   - 包含完整的变更数据和元信息

## 日志监控

系统会记录详细的处理日志：

```log
INFO  - 收到CDC事件: taskId=task_001, event={...}
INFO  - CDC数据写入ODS层成功: 源表=contract_info, 目标表=ods_contract_info, 操作=CREATE, 批次ID=task_001_1704085200000
INFO  - 已触发NiFi处理器: 表=contract_info, DWD=dwd-contract-processor-001, DWS=null
```

## 支持的操作类型

- ✅ **CREATE (c)** - 新增记录，写入ODS表
- ✅ **UPDATE (u)** - 更新记录，写入ODS表（使用after数据）
- ✅ **DELETE (d)** - 删除记录，记录日志（不删除ODS数据）
- ✅ **READ (r)** - 快照读取，作为CREATE处理

## 错误处理

- 如果目标表不存在，写入会失败并记录错误日志
- 如果CDC数据格式异常，会跳过处理并记录警告
- 如果PostgreSQL连接失败，会记录错误并重试

## 性能特点

- **批次追踪** - 每个批次都有唯一标识，便于数据追溯
- **异步处理** - NiFi通知是异步的，不阻塞CDC主流程
- **错误恢复** - 单条记录失败不影响其他记录处理
- **监控友好** - 详细的日志记录，便于问题排查

## 验证方法

1. **检查CDC配置**：
   ```sql
   SELECT * FROM data_ingest_module_data_cdc_table WHERE task_id = 'task_001';
   ```

2. **检查ODS数据**：
   ```sql
   SELECT * FROM ods_contract_info ORDER BY create_time DESC LIMIT 10;
   ```

3. **查看处理日志**：
   ```bash
   tail -f logs/dataingest.log | grep "CDC数据写入ODS层"
   ```

现在您的CDC数据流程已经完整：**源数据库 → Debezium CDC → PostgreSQL ODS → NiFi DWD/DWS** 🎉 