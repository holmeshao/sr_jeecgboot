-- 数据接入微服务数据库表结构
-- PostgreSQL版本
-- 适用于JeecgBoot数据接入服务

-- 数据源配置表
CREATE TABLE IF NOT EXISTS data_source_config (
    id VARCHAR(32) PRIMARY KEY,
    source_name VARCHAR(100) NOT NULL,
    source_type VARCHAR(50) NOT NULL,
    connection_config TEXT,
    auth_config TEXT,
    status INTEGER DEFAULT 1,
    remark VARCHAR(500),
    create_by VARCHAR(32),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(32),
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 数据接入任务表
CREATE TABLE IF NOT EXISTS ingest_task (
    id VARCHAR(32) PRIMARY KEY,
    task_name VARCHAR(100) NOT NULL,
    task_type VARCHAR(50) NOT NULL,
    data_source_id VARCHAR(32),
    task_config TEXT,
    target_table VARCHAR(100),
    schedule_config TEXT,
    status INTEGER DEFAULT 0,
    last_execute_time TIMESTAMP,
    next_execute_time TIMESTAMP,
    execute_count BIGINT DEFAULT 0,
    success_count BIGINT DEFAULT 0,
    fail_count BIGINT DEFAULT 0,
    remark VARCHAR(500),
    create_by VARCHAR(32),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(32),
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 数据接入日志表
CREATE TABLE IF NOT EXISTS ingest_log (
    id VARCHAR(32) PRIMARY KEY,
    task_id VARCHAR(32) NOT NULL,
    batch_id VARCHAR(32),
    status INTEGER NOT NULL,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    record_count BIGINT DEFAULT 0,
    success_count BIGINT DEFAULT 0,
    fail_count BIGINT DEFAULT 0,
    error_message TEXT,
    execute_log TEXT,
    create_by VARCHAR(32),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(32),
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 字段映射配置表
CREATE TABLE IF NOT EXISTS field_mapping (
    id VARCHAR(32) PRIMARY KEY,
    task_id VARCHAR(32) NOT NULL,
    source_field VARCHAR(100) NOT NULL,
    target_field VARCHAR(100) NOT NULL,
    field_type VARCHAR(50),
    is_required BOOLEAN DEFAULT FALSE,
    default_value VARCHAR(500),
    transform_rule TEXT,
    create_by VARCHAR(32),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(32),
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_data_source_config_type ON data_source_config(source_type);
CREATE INDEX IF NOT EXISTS idx_data_source_config_status ON data_source_config(status);
CREATE INDEX IF NOT EXISTS idx_ingest_task_type ON ingest_task(task_type);
CREATE INDEX IF NOT EXISTS idx_ingest_task_status ON ingest_task(status);
CREATE INDEX IF NOT EXISTS idx_ingest_task_source ON ingest_task(data_source_id);
CREATE INDEX IF NOT EXISTS idx_ingest_log_task ON ingest_log(task_id);
CREATE INDEX IF NOT EXISTS idx_ingest_log_status ON ingest_log(status);
CREATE INDEX IF NOT EXISTS idx_ingest_log_time ON ingest_log(start_time);
CREATE INDEX IF NOT EXISTS idx_field_mapping_task ON field_mapping(task_id);

-- 添加表注释
COMMENT ON TABLE data_source_config IS '数据源配置表';
COMMENT ON COLUMN data_source_config.id IS '主键';
COMMENT ON COLUMN data_source_config.source_name IS '数据源名称';
COMMENT ON COLUMN data_source_config.source_type IS '数据源类型';
COMMENT ON COLUMN data_source_config.connection_config IS '连接配置';
COMMENT ON COLUMN data_source_config.auth_config IS '认证配置';
COMMENT ON COLUMN data_source_config.status IS '状态';
COMMENT ON COLUMN data_source_config.remark IS '备注';

COMMENT ON TABLE ingest_task IS '数据接入任务表';
COMMENT ON COLUMN ingest_task.id IS '主键';
COMMENT ON COLUMN ingest_task.task_name IS '任务名称';
COMMENT ON COLUMN ingest_task.task_type IS '任务类型';
COMMENT ON COLUMN ingest_task.data_source_id IS '数据源ID';
COMMENT ON COLUMN ingest_task.task_config IS '任务配置';
COMMENT ON COLUMN ingest_task.target_table IS '目标表名';
COMMENT ON COLUMN ingest_task.schedule_config IS '调度配置';
COMMENT ON COLUMN ingest_task.status IS '任务状态';
COMMENT ON COLUMN ingest_task.last_execute_time IS '上次执行时间';
COMMENT ON COLUMN ingest_task.next_execute_time IS '下次执行时间';
COMMENT ON COLUMN ingest_task.execute_count IS '执行次数';
COMMENT ON COLUMN ingest_task.success_count IS '成功次数';
COMMENT ON COLUMN ingest_task.fail_count IS '失败次数';
COMMENT ON COLUMN ingest_task.remark IS '备注';

COMMENT ON TABLE ingest_log IS '数据接入日志表';
COMMENT ON COLUMN ingest_log.id IS '主键';
COMMENT ON COLUMN ingest_log.task_id IS '任务ID';
COMMENT ON COLUMN ingest_log.batch_id IS '执行批次';
COMMENT ON COLUMN ingest_log.status IS '执行状态';
COMMENT ON COLUMN ingest_log.start_time IS '开始时间';
COMMENT ON COLUMN ingest_log.end_time IS '结束时间';
COMMENT ON COLUMN ingest_log.record_count IS '处理记录数';
COMMENT ON COLUMN ingest_log.success_count IS '成功记录数';
COMMENT ON COLUMN ingest_log.fail_count IS '失败记录数';
COMMENT ON COLUMN ingest_log.error_message IS '错误信息';
COMMENT ON COLUMN ingest_log.execute_log IS '执行日志';

COMMENT ON TABLE field_mapping IS '字段映射配置表';
COMMENT ON COLUMN field_mapping.id IS '主键';
COMMENT ON COLUMN field_mapping.task_id IS '任务ID';
COMMENT ON COLUMN field_mapping.source_field IS '源字段名';
COMMENT ON COLUMN field_mapping.target_field IS '目标字段名';
COMMENT ON COLUMN field_mapping.field_type IS '字段类型';
COMMENT ON COLUMN field_mapping.is_required IS '是否必填';
COMMENT ON COLUMN field_mapping.default_value IS '默认值';
COMMENT ON COLUMN field_mapping.transform_rule IS '转换规则';

-- 插入示例数据
INSERT INTO data_source_config (id, source_name, source_type, connection_config, status, remark) VALUES
('1', 'SQLServer测试库', 'SQLSERVER', '{"hostname":"localhost","port":1433,"database":"testdb","username":"sa","password":"password"}', 1, 'SQLServer测试数据源'),
('2', '金蝶Cloud测试', 'KINGDEE_CLOUD', '{"baseUrl":"https://api.kingdee.com","appId":"test-app","appSecret":"test-secret","username":"test","password":"test","lcid":2052,"dbId":"test-db"}', 1, '金蝶Cloud测试数据源'),
('3', '通用API测试', 'OPENAPI', '{"baseUrl":"https://api.example.com","method":"GET","headers":{"Authorization":"Bearer token"}}', 1, '通用API测试数据源');

INSERT INTO ingest_task (id, task_name, task_type, data_source_id, target_table, status, remark) VALUES
('1', 'SQLServer发票数据同步', 'CDC', '1', 'ods_invoice', 0, 'SQLServer发票表CDC同步'),
('2', '金蝶物料数据同步', 'API', '2', 'ods_material', 0, '金蝶物料数据API同步'),
('3', '第三方订单数据同步', 'API', '3', 'ods_order', 0, '第三方订单数据API同步');

-- 创建ODS层示例表
CREATE TABLE IF NOT EXISTS ods_invoice (
    id VARCHAR(32) PRIMARY KEY,
    invoice_no VARCHAR(50),
    invoice_date DATE,
    customer_name VARCHAR(100),
    amount DECIMAL(18,2),
    status VARCHAR(20),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    batch_id VARCHAR(32),
    source_system VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS ods_material (
    id VARCHAR(32) PRIMARY KEY,
    material_code VARCHAR(50),
    material_name VARCHAR(100),
    material_type VARCHAR(50),
    unit VARCHAR(20),
    price DECIMAL(18,2),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    batch_id VARCHAR(32),
    source_system VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS ods_order (
    id VARCHAR(32) PRIMARY KEY,
    order_no VARCHAR(50),
    order_date DATE,
    customer_id VARCHAR(50),
    total_amount DECIMAL(18,2),
    order_status VARCHAR(20),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    batch_id VARCHAR(32),
    source_system VARCHAR(50)
);

-- 为ODS表添加注释
COMMENT ON TABLE ods_invoice IS 'ODS层发票数据表';
COMMENT ON COLUMN ods_invoice.invoice_no IS '发票号';
COMMENT ON COLUMN ods_invoice.invoice_date IS '发票日期';
COMMENT ON COLUMN ods_invoice.customer_name IS '客户名称';
COMMENT ON COLUMN ods_invoice.amount IS '金额';
COMMENT ON COLUMN ods_invoice.status IS '状态';
COMMENT ON COLUMN ods_invoice.batch_id IS '批次ID';
COMMENT ON COLUMN ods_invoice.source_system IS '来源系统';

COMMENT ON TABLE ods_material IS 'ODS层物料数据表';
COMMENT ON COLUMN ods_material.material_code IS '物料编码';
COMMENT ON COLUMN ods_material.material_name IS '物料名称';
COMMENT ON COLUMN ods_material.material_type IS '物料类型';
COMMENT ON COLUMN ods_material.unit IS '单位';
COMMENT ON COLUMN ods_material.price IS '价格';
COMMENT ON COLUMN ods_material.batch_id IS '批次ID';
COMMENT ON COLUMN ods_material.source_system IS '来源系统';

COMMENT ON TABLE ods_order IS 'ODS层订单数据表';
COMMENT ON COLUMN ods_order.order_no IS '订单号';
COMMENT ON COLUMN ods_order.order_date IS '订单日期';
COMMENT ON COLUMN ods_order.customer_id IS '客户ID';
COMMENT ON COLUMN ods_order.total_amount IS '总金额';
COMMENT ON COLUMN ods_order.order_status IS '订单状态';
COMMENT ON COLUMN ods_order.batch_id IS '批次ID';
COMMENT ON COLUMN ods_order.source_system IS '来源系统';

-- 创建ODS表索引
CREATE INDEX IF NOT EXISTS idx_ods_invoice_batch ON ods_invoice(batch_id);
CREATE INDEX IF NOT EXISTS idx_ods_invoice_source ON ods_invoice(source_system);
CREATE INDEX IF NOT EXISTS idx_ods_material_batch ON ods_material(batch_id);
CREATE INDEX IF NOT EXISTS idx_ods_material_source ON ods_material(source_system);
CREATE INDEX IF NOT EXISTS idx_ods_order_batch ON ods_order(batch_id);
CREATE INDEX IF NOT EXISTS idx_ods_order_source ON ods_order(source_system);

-- 创建序列（如果需要自增ID）
CREATE SEQUENCE IF NOT EXISTS dataingest_seq START 1;

-- 创建触发器函数，用于自动更新update_time
CREATE OR REPLACE FUNCTION update_updated_time_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.update_time = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 为所有表创建update_time触发器
CREATE TRIGGER update_data_source_config_updated_time 
    BEFORE UPDATE ON data_source_config 
    FOR EACH ROW EXECUTE FUNCTION update_updated_time_column();

CREATE TRIGGER update_ingest_task_updated_time 
    BEFORE UPDATE ON ingest_task 
    FOR EACH ROW EXECUTE FUNCTION update_updated_time_column();

CREATE TRIGGER update_ingest_log_updated_time 
    BEFORE UPDATE ON ingest_log 
    FOR EACH ROW EXECUTE FUNCTION update_updated_time_column();

CREATE TRIGGER update_field_mapping_updated_time 
    BEFORE UPDATE ON field_mapping 
    FOR EACH ROW EXECUTE FUNCTION update_updated_time_column();

CREATE TRIGGER update_ods_invoice_updated_time 
    BEFORE UPDATE ON ods_invoice 
    FOR EACH ROW EXECUTE FUNCTION update_updated_time_column();

CREATE TRIGGER update_ods_material_updated_time 
    BEFORE UPDATE ON ods_material 
    FOR EACH ROW EXECUTE FUNCTION update_updated_time_column();

CREATE TRIGGER update_ods_order_updated_time 
    BEFORE UPDATE ON ods_order 
    FOR EACH ROW EXECUTE FUNCTION update_updated_time_column(); 