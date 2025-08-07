-- 为data_ingest_module_data_cdc_table表增加NiFi相关字段
-- 执行前请备份数据库

-- 1. 增加NiFi DWD处理器ID字段
ALTER TABLE data_ingest_module_data_cdc_table 
ADD COLUMN nifi_dwd_processor_id VARCHAR(100) COMMENT 'NiFi DWD处理器ID';

-- 2. 增加NiFi DWS处理器ID字段
ALTER TABLE data_ingest_module_data_cdc_table 
ADD COLUMN nifi_dws_processor_id VARCHAR(100) COMMENT 'NiFi DWS处理器ID';

-- 3. 增加业务域标识字段
ALTER TABLE data_ingest_module_data_cdc_table 
ADD COLUMN business_domain VARCHAR(50) COMMENT '业务域标识';

-- 4. 增加是否启用NiFi通知字段
ALTER TABLE data_ingest_module_data_cdc_table 
ADD COLUMN enable_nifi_notify TINYINT(1) DEFAULT 0 COMMENT '是否启用NiFi通知:0-禁用,1-启用';

-- 5. 增加NiFi通知模式字段
ALTER TABLE data_ingest_module_data_cdc_table 
ADD COLUMN nifi_notify_mode TINYINT(1) DEFAULT 1 COMMENT 'NiFi通知模式:1-立即通知,2-批量通知,3-定时通知';

-- 6. 增加通知延迟秒数字段
ALTER TABLE data_ingest_module_data_cdc_table 
ADD COLUMN notify_delay_seconds INT DEFAULT 0 COMMENT '通知延迟秒数';

-- 7. 创建业务域索引（提升查询性能）
CREATE INDEX idx_business_domain ON data_ingest_module_data_cdc_table(business_domain);

-- 8. 创建启用通知状态索引
CREATE INDEX idx_enable_nifi_notify ON data_ingest_module_data_cdc_table(enable_nifi_notify);

-- 9. 创建复合索引（源表名+任务ID，用于快速查找CDC配置）
CREATE INDEX idx_source_table_task ON data_ingest_module_data_cdc_table(source_table_name, task_id);

-- 10. 插入示例数据（可选）
-- INSERT INTO data_ingest_module_data_cdc_table (
--     id, source_table_name, target_table_name, task_id,
--     nifi_dwd_processor_id, nifi_dws_processor_id, business_domain,
--     enable_nifi_notify, nifi_notify_mode, notify_delay_seconds,
--     create_time, create_by
-- ) VALUES (
--     'example_001', 'contract_info', 'ods_contract_info', 'task_001',
--     'dwd-contract-processor-001', 'dws-contract-summary-001', 'contract',
--     1, 1, 0,
--     NOW(), 'system'
-- );

-- 验证字段是否添加成功
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT,
    COLUMN_COMMENT
FROM 
    INFORMATION_SCHEMA.COLUMNS 
WHERE 
    TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'data_ingest_module_data_cdc_table'
    AND COLUMN_NAME IN (
        'nifi_dwd_processor_id',
        'nifi_dws_processor_id', 
        'business_domain',
        'enable_nifi_notify',
        'nifi_notify_mode',
        'notify_delay_seconds'
    );

-- 验证索引是否创建成功
SHOW INDEX FROM data_ingest_module_data_cdc_table WHERE Key_name IN (
    'idx_business_domain',
    'idx_enable_nifi_notify', 
    'idx_source_table_task'
); 