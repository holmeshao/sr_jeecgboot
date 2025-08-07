-- 增强版数据源配置表DDL
-- 为data_ingest_module_data_source_config表添加新字段

-- 添加表名前缀配置字段
ALTER TABLE data_ingest_module_data_source_config 
ADD COLUMN IF NOT EXISTS table_name_pre_default VARCHAR(50) COMMENT '默认表名前缀，当未指定目标表名时使用此前缀+源表名';

-- 添加数据源类型字段（如果不存在）
ALTER TABLE data_ingest_module_data_source_config 
ADD COLUMN IF NOT EXISTS source_type VARCHAR(50) COMMENT '数据源类型：MYSQL_CDC/POSTGRESQL_CDC/SQLSERVER_CDC等';

-- 添加状态字段（如果不存在）
ALTER TABLE data_ingest_module_data_source_config 
ADD COLUMN IF NOT EXISTS status INTEGER DEFAULT 1 COMMENT '状态：1-启用，0-禁用';

-- 添加数据库名字段
ALTER TABLE data_ingest_module_data_source_config 
ADD COLUMN IF NOT EXISTS database_name VARCHAR(100) COMMENT '源数据库名称';

-- 添加业务域字段
ALTER TABLE data_ingest_module_data_source_config 
ADD COLUMN IF NOT EXISTS business_domain VARCHAR(50) COMMENT '业务域标识';

-- 添加数据质量规则字段
ALTER TABLE data_ingest_module_data_source_config 
ADD COLUMN IF NOT EXISTS data_quality_rules TEXT COMMENT '数据质量规则配置';

-- 添加转换规则字段
ALTER TABLE data_ingest_module_data_source_config 
ADD COLUMN IF NOT EXISTS transformation_rules TEXT COMMENT '默认转换规则配置';

-- 更新表注释
COMMENT ON TABLE data_ingest_module_data_source_config IS '数据接入模块数据源配置表（增强版，支持数据血缘和表名前缀配置）';

-- 更新字段注释
COMMENT ON COLUMN data_ingest_module_data_source_config.table_name_pre_default IS '默认表名前缀，当CDC表配置中未指定目标表名时，使用此前缀拼接源表名作为目标表名';
COMMENT ON COLUMN data_ingest_module_data_source_config.source_type IS '数据源类型：MYSQL_CDC/POSTGRESQL_CDC/SQLSERVER_CDC等';
COMMENT ON COLUMN data_ingest_module_data_source_config.status IS '状态：1-启用，0-禁用';
COMMENT ON COLUMN data_ingest_module_data_source_config.database_name IS '源数据库名称，用于数据血缘追溯';
COMMENT ON COLUMN data_ingest_module_data_source_config.business_domain IS '业务域标识，用于数据分类和权限控制';
COMMENT ON COLUMN data_ingest_module_data_source_config.data_quality_rules IS '数据质量规则配置，JSON格式存储质量检查规则';
COMMENT ON COLUMN data_ingest_module_data_source_config.transformation_rules IS '默认转换规则配置，JSON格式存储字段转换和数据清洗规则';

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_data_source_config_source_type ON data_ingest_module_data_source_config(source_type);
CREATE INDEX IF NOT EXISTS idx_data_source_config_status ON data_ingest_module_data_source_config(status);
CREATE INDEX IF NOT EXISTS idx_data_source_config_business_domain ON data_ingest_module_data_source_config(business_domain);
CREATE INDEX IF NOT EXISTS idx_data_source_config_task_id ON data_ingest_module_data_source_config(task_id);

-- 插入示例数据
INSERT INTO data_ingest_module_data_source_config (
    id, source_name, source_type, table_name_pre_default, database_name, business_domain,
    connection_config, auth_config, status, remark, create_by, create_time, update_by, update_time
) VALUES (
    'example_mysql_001', 
    '用户中心MySQL数据源', 
    'MYSQL_CDC', 
    'ods_user_', 
    'user_center_db', 
    'user_management',
    '{"hostname":"localhost","port":"3306","database":"user_center_db"}',
    '{"username":"root","password":"password"}',
    1,
    '用户中心数据源配置示例，使用ods_user_前缀',
    'system',
    CURRENT_TIMESTAMP,
    'system',
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING;

INSERT INTO data_ingest_module_data_source_config (
    id, source_name, source_type, table_name_pre_default, database_name, business_domain,
    connection_config, auth_config, status, remark, create_by, create_time, update_by, update_time
) VALUES (
    'example_postgresql_001', 
    '订单系统PostgreSQL数据源', 
    'POSTGRESQL_CDC', 
    'ods_order_', 
    'order_system_db', 
    'order_management',
    '{"hostname":"localhost","port":"5432","database":"order_system_db"}',
    '{"username":"postgres","password":"password"}',
    1,
    '订单系统数据源配置示例，使用ods_order_前缀',
    'system',
    CURRENT_TIMESTAMP,
    'system',
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING; 