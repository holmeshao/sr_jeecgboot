-- 数据接入微服务数据库表结构
-- 适用于PostgreSQL

-- 数据源配置表
CREATE TABLE IF NOT EXISTS data_source_config (
    id VARCHAR(32) PRIMARY KEY,
    source_name VARCHAR(100) NOT NULL COMMENT '数据源名称',
    source_type VARCHAR(50) NOT NULL COMMENT '数据源类型',
    connection_config TEXT COMMENT '连接配置',
    auth_config TEXT COMMENT '认证配置',
    status INTEGER DEFAULT 1 COMMENT '状态',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(32) COMMENT '创建人',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(32) COMMENT '更新人',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 数据接入任务表
CREATE TABLE IF NOT EXISTS ingest_task (
    id VARCHAR(32) PRIMARY KEY,
    task_name VARCHAR(100) NOT NULL COMMENT '任务名称',
    task_type VARCHAR(50) NOT NULL COMMENT '任务类型',
    data_source_id VARCHAR(32) COMMENT '数据源ID',
    task_config TEXT COMMENT '任务配置',
    target_table VARCHAR(100) COMMENT '目标表名',
    schedule_config TEXT COMMENT '调度配置',
    status INTEGER DEFAULT 0 COMMENT '任务状态',
    last_execute_time TIMESTAMP COMMENT '上次执行时间',
    next_execute_time TIMESTAMP COMMENT '下次执行时间',
    execute_count BIGINT DEFAULT 0 COMMENT '执行次数',
    success_count BIGINT DEFAULT 0 COMMENT '成功次数',
    fail_count BIGINT DEFAULT 0 COMMENT '失败次数',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(32) COMMENT '创建人',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(32) COMMENT '更新人',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 数据接入日志表
CREATE TABLE IF NOT EXISTS ingest_log (
    id VARCHAR(32) PRIMARY KEY,
    task_id VARCHAR(32) NOT NULL COMMENT '任务ID',
    batch_id VARCHAR(32) COMMENT '执行批次',
    status INTEGER NOT NULL COMMENT '执行状态',
    start_time TIMESTAMP COMMENT '开始时间',
    end_time TIMESTAMP COMMENT '结束时间',
    record_count BIGINT DEFAULT 0 COMMENT '处理记录数',
    success_count BIGINT DEFAULT 0 COMMENT '成功记录数',
    fail_count BIGINT DEFAULT 0 COMMENT '失败记录数',
    error_message TEXT COMMENT '错误信息',
    execute_log TEXT COMMENT '执行日志',
    create_by VARCHAR(32) COMMENT '创建人',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(32) COMMENT '更新人',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 字段映射配置表
CREATE TABLE IF NOT EXISTS field_mapping (
    id VARCHAR(32) PRIMARY KEY,
    task_id VARCHAR(32) NOT NULL COMMENT '任务ID',
    source_field VARCHAR(100) NOT NULL COMMENT '源字段名',
    target_field VARCHAR(100) NOT NULL COMMENT '目标字段名',
    field_type VARCHAR(50) COMMENT '字段类型',
    is_required BOOLEAN DEFAULT FALSE COMMENT '是否必填',
    default_value VARCHAR(500) COMMENT '默认值',
    transform_rule TEXT COMMENT '转换规则',
    create_by VARCHAR(32) COMMENT '创建人',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(32) COMMENT '更新人',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
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
    invoice_no VARCHAR(50) COMMENT '发票号',
    invoice_date DATE COMMENT '发票日期',
    customer_name VARCHAR(100) COMMENT '客户名称',
    amount DECIMAL(18,2) COMMENT '金额',
    status VARCHAR(20) COMMENT '状态',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    batch_id VARCHAR(32) COMMENT '批次ID',
    source_system VARCHAR(50) COMMENT '来源系统'
);

CREATE TABLE IF NOT EXISTS ods_material (
    id VARCHAR(32) PRIMARY KEY,
    material_code VARCHAR(50) COMMENT '物料编码',
    material_name VARCHAR(100) COMMENT '物料名称',
    material_type VARCHAR(50) COMMENT '物料类型',
    unit VARCHAR(20) COMMENT '单位',
    price DECIMAL(18,2) COMMENT '价格',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    batch_id VARCHAR(32) COMMENT '批次ID',
    source_system VARCHAR(50) COMMENT '来源系统'
);

CREATE TABLE IF NOT EXISTS ods_order (
    id VARCHAR(32) PRIMARY KEY,
    order_no VARCHAR(50) COMMENT '订单号',
    order_date DATE COMMENT '订单日期',
    customer_id VARCHAR(50) COMMENT '客户ID',
    total_amount DECIMAL(18,2) COMMENT '总金额',
    order_status VARCHAR(20) COMMENT '订单状态',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    batch_id VARCHAR(32) COMMENT '批次ID',
    source_system VARCHAR(50) COMMENT '来源系统'
); 