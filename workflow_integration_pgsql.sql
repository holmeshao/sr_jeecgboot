-- =============================================================================
-- JeecgBoot在线表单工作流集成 - PostgreSQL版本
-- 基于JeecgBoot在线表单的工作流集成方案
-- =============================================================================

-- 设置客户端编码
SET client_encoding = 'UTF8';

-- =============================================================================
-- 1. 业务表单设计（物理表模式）
-- =============================================================================

-- 以维保工单为例 - 扩展现有业务表
ALTER TABLE maintenance_report 
ADD COLUMN process_instance_id VARCHAR(64), -- Flowable流程实例ID
ADD COLUMN bmp_status VARCHAR(20) DEFAULT 'DRAFT'; -- 业务状态

-- 创建索引
CREATE INDEX idx_maintenance_report_process_instance ON maintenance_report (process_instance_id);
CREATE INDEX idx_maintenance_report_status ON maintenance_report (bmp_status);

-- 添加注释
COMMENT ON COLUMN maintenance_report.process_instance_id IS 'Flowable流程实例ID';
COMMENT ON COLUMN maintenance_report.bmp_status IS '业务状态';

-- =============================================================================
-- 2. 表单工作流配置表
-- =============================================================================

-- 删除表（如果存在）
DROP TABLE IF EXISTS onl_cgform_workflow_config CASCADE;

-- 创建表单工作流配置表
CREATE TABLE onl_cgform_workflow_config (
  id VARCHAR(32) NOT NULL,
  cgform_head_id VARCHAR(32) NOT NULL, -- 表单ID
  process_definition_key VARCHAR(100) NOT NULL, -- 流程定义Key
  
  -- 核心配置开关
  workflow_enabled SMALLINT DEFAULT 0, -- 是否启用工作流
  version_control_enabled SMALLINT DEFAULT 0, -- 是否启用版本控制
  permission_control_enabled SMALLINT DEFAULT 0, -- 是否启用权限控制
  
  -- 映射配置
  business_key_field VARCHAR(50), -- 业务主键字段名
  status_field VARCHAR(50) DEFAULT 'bmp_status', -- 状态字段名
  process_instance_field VARCHAR(50) DEFAULT 'process_instance_id', -- 流程实例字段名
  
  -- 版本控制配置
  snapshot_strategy VARCHAR(20) DEFAULT 'NODE', -- 快照策略(NODE节点级,TASK任务级)
  snapshot_nodes TEXT, -- 需要快照的节点JSON数组
  
  status SMALLINT DEFAULT 1,
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  
  PRIMARY KEY (id),
  UNIQUE (cgform_head_id, process_definition_key)
);

-- 添加注释
COMMENT ON TABLE onl_cgform_workflow_config IS '表单工作流配置表';
COMMENT ON COLUMN onl_cgform_workflow_config.cgform_head_id IS '表单ID';
COMMENT ON COLUMN onl_cgform_workflow_config.process_definition_key IS '流程定义Key';
COMMENT ON COLUMN onl_cgform_workflow_config.workflow_enabled IS '是否启用工作流';
COMMENT ON COLUMN onl_cgform_workflow_config.version_control_enabled IS '是否启用版本控制';
COMMENT ON COLUMN onl_cgform_workflow_config.permission_control_enabled IS '是否启用权限控制';
COMMENT ON COLUMN onl_cgform_workflow_config.business_key_field IS '业务主键字段名';
COMMENT ON COLUMN onl_cgform_workflow_config.status_field IS '状态字段名';
COMMENT ON COLUMN onl_cgform_workflow_config.process_instance_field IS '流程实例字段名';
COMMENT ON COLUMN onl_cgform_workflow_config.snapshot_strategy IS '快照策略(NODE节点级,TASK任务级)';
COMMENT ON COLUMN onl_cgform_workflow_config.snapshot_nodes IS '需要快照的节点JSON数组';

-- 创建索引
CREATE INDEX idx_workflow_config_form ON onl_cgform_workflow_config (cgform_head_id);
CREATE INDEX idx_workflow_config_process ON onl_cgform_workflow_config (process_definition_key);
CREATE INDEX idx_workflow_config_status ON onl_cgform_workflow_config (status);

-- =============================================================================
-- 3. 节点权限配置表
-- =============================================================================

-- 删除表（如果存在）
DROP TABLE IF EXISTS onl_cgform_workflow_node CASCADE;

-- 创建节点权限配置表
CREATE TABLE onl_cgform_workflow_node (
  id VARCHAR(32) NOT NULL, -- 主键ID
  cgform_head_id VARCHAR(32) NOT NULL, -- 表单ID
  process_definition_key VARCHAR(100) NOT NULL, -- 流程定义Key
  node_id VARCHAR(100) NOT NULL, -- 节点ID
  node_name VARCHAR(200), -- 节点名称
  
  -- 字段权限配置
  editable_fields TEXT, -- 可编辑字段JSON数组
  readonly_fields TEXT, -- 只读字段JSON数组
  hidden_fields TEXT, -- 隐藏字段JSON数组
  required_fields TEXT, -- 必填字段JSON数组
  
  -- 条件权限配置
  conditional_permissions TEXT, -- 条件权限配置JSON
  
  -- 表单行为控制
  form_mode VARCHAR(20) DEFAULT 'EDIT', -- 表单模式(VIEW只读,EDIT编辑)
  custom_buttons TEXT, -- 自定义按钮配置JSON
  hidden_buttons TEXT, -- 隐藏按钮JSON数组
  
  sort_order INTEGER DEFAULT 0, -- 排序
  status SMALLINT DEFAULT 1, -- 状态
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  
  PRIMARY KEY (id),
  UNIQUE (cgform_head_id, process_definition_key, node_id)
);

-- 添加注释
COMMENT ON TABLE onl_cgform_workflow_node IS '节点权限配置表';
COMMENT ON COLUMN onl_cgform_workflow_node.cgform_head_id IS '表单ID';
COMMENT ON COLUMN onl_cgform_workflow_node.process_definition_key IS '流程定义Key';
COMMENT ON COLUMN onl_cgform_workflow_node.node_id IS '节点ID';
COMMENT ON COLUMN onl_cgform_workflow_node.node_name IS '节点名称';
COMMENT ON COLUMN onl_cgform_workflow_node.editable_fields IS '可编辑字段JSON数组';
COMMENT ON COLUMN onl_cgform_workflow_node.readonly_fields IS '只读字段JSON数组';
COMMENT ON COLUMN onl_cgform_workflow_node.hidden_fields IS '隐藏字段JSON数组';
COMMENT ON COLUMN onl_cgform_workflow_node.required_fields IS '必填字段JSON数组';
COMMENT ON COLUMN onl_cgform_workflow_node.conditional_permissions IS '条件权限配置JSON';
COMMENT ON COLUMN onl_cgform_workflow_node.form_mode IS '表单模式(VIEW只读,EDIT编辑)';
COMMENT ON COLUMN onl_cgform_workflow_node.custom_buttons IS '自定义按钮配置JSON';
COMMENT ON COLUMN onl_cgform_workflow_node.hidden_buttons IS '隐藏按钮JSON数组';
COMMENT ON COLUMN onl_cgform_workflow_node.sort_order IS '排序';
COMMENT ON COLUMN onl_cgform_workflow_node.status IS '状态';

-- 创建索引
CREATE INDEX idx_workflow_node_form ON onl_cgform_workflow_node (cgform_head_id);
CREATE INDEX idx_workflow_node_process ON onl_cgform_workflow_node (process_definition_key);
CREATE INDEX idx_workflow_node_node ON onl_cgform_workflow_node (node_id);
CREATE INDEX idx_workflow_node_status ON onl_cgform_workflow_node (status);
CREATE INDEX idx_workflow_node_sort ON onl_cgform_workflow_node (sort_order);

-- =============================================================================
-- 4. 表单数据快照表（版本控制）
-- =============================================================================

-- 删除表（如果存在）
DROP TABLE IF EXISTS onl_cgform_workflow_snapshot CASCADE;

-- 创建表单数据快照表
CREATE TABLE onl_cgform_workflow_snapshot (
  id VARCHAR(32) NOT NULL, -- 主键ID
  cgform_head_id VARCHAR(32) NOT NULL, -- 表单ID
  process_instance_id VARCHAR(64) NOT NULL, -- 流程实例ID
  business_key VARCHAR(100) NOT NULL, -- 业务主键值
  node_id VARCHAR(100) NOT NULL, -- 节点ID
  node_name VARCHAR(200), -- 节点名称
  
  -- 快照数据
  form_data JSONB, -- 表单数据JSON
  form_schema JSONB, -- 表单结构JSON
  permission_config JSONB, -- 权限配置JSON
  
  -- 版本信息
  version_number INTEGER DEFAULT 1, -- 版本号
  snapshot_type VARCHAR(20) DEFAULT 'NODE', -- 快照类型(NODE节点级,TASK任务级)
  
  -- 元数据
  create_by VARCHAR(32), -- 创建人
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
  remark TEXT, -- 备注
  
  PRIMARY KEY (id),
  UNIQUE (process_instance_id, business_key, node_id, version_number)
);

-- 添加注释
COMMENT ON TABLE onl_cgform_workflow_snapshot IS '表单数据快照表';
COMMENT ON COLUMN onl_cgform_workflow_snapshot.cgform_head_id IS '表单ID';
COMMENT ON COLUMN onl_cgform_workflow_snapshot.process_instance_id IS '流程实例ID';
COMMENT ON COLUMN onl_cgform_workflow_snapshot.business_key IS '业务主键值';
COMMENT ON COLUMN onl_cgform_workflow_snapshot.node_id IS '节点ID';
COMMENT ON COLUMN onl_cgform_workflow_snapshot.node_name IS '节点名称';
COMMENT ON COLUMN onl_cgform_workflow_snapshot.form_data IS '表单数据JSON';
COMMENT ON COLUMN onl_cgform_workflow_snapshot.form_schema IS '表单结构JSON';
COMMENT ON COLUMN onl_cgform_workflow_snapshot.permission_config IS '权限配置JSON';
COMMENT ON COLUMN onl_cgform_workflow_snapshot.version_number IS '版本号';
COMMENT ON COLUMN onl_cgform_workflow_snapshot.snapshot_type IS '快照类型(NODE节点级,TASK任务级)';
COMMENT ON COLUMN onl_cgform_workflow_snapshot.create_by IS '创建人';
COMMENT ON COLUMN onl_cgform_workflow_snapshot.create_time IS '创建时间';
COMMENT ON COLUMN onl_cgform_workflow_snapshot.remark IS '备注';

-- 创建索引
CREATE INDEX idx_snapshot_form ON onl_cgform_workflow_snapshot (cgform_head_id);
CREATE INDEX idx_snapshot_process ON onl_cgform_workflow_snapshot (process_instance_id);
CREATE INDEX idx_snapshot_business ON onl_cgform_workflow_snapshot (business_key);
CREATE INDEX idx_snapshot_node ON onl_cgform_workflow_snapshot (node_id);
CREATE INDEX idx_snapshot_version ON onl_cgform_workflow_snapshot (version_number);
CREATE INDEX idx_snapshot_create_time ON onl_cgform_workflow_snapshot (create_time);

-- 创建JSONB索引（PostgreSQL特有）
CREATE INDEX idx_snapshot_form_data ON onl_cgform_workflow_snapshot USING GIN (form_data);
CREATE INDEX idx_snapshot_permission_config ON onl_cgform_workflow_snapshot USING GIN (permission_config);

-- =============================================================================
-- 5. 示例数据
-- =============================================================================

-- 插入示例工作流配置
INSERT INTO onl_cgform_workflow_config (
  id, cgform_head_id, process_definition_key, 
  workflow_enabled, version_control_enabled, permission_control_enabled,
  business_key_field, status_field, process_instance_field,
  snapshot_strategy, status
) VALUES (
  'workflow_config_001', 'maintenance_report_form', 'maintenance_order_process',
  1, 1, 1,
  'report_no', 'bmp_status', 'process_instance_id',
  'NODE', 1
);

-- 插入示例节点权限配置
INSERT INTO onl_cgform_workflow_node (
  id, cgform_head_id, process_definition_key, node_id, node_name,
  editable_fields, readonly_fields, hidden_fields, required_fields,
  form_mode, sort_order, status
) VALUES 
-- 提交节点
('node_001', 'maintenance_report_form', 'maintenance_order_process', 'submit', '提交申请',
 '["title", "description", "urgency_level", "project_id"]', '["report_no", "create_time"]', '["process_instance_id", "bmp_status"]', '["title", "urgency_level"]',
 'EDIT', 1, 1),

-- 审核节点
('node_002', 'maintenance_report_form', 'maintenance_order_process', 'review', '部门审核',
 '["urgency_level"]', '["title", "description", "project_id", "report_no", "create_time"]', '["process_instance_id"]', '[]',
 'EDIT', 2, 1),

-- 审批节点
('node_003', 'maintenance_report_form', 'maintenance_order_process', 'approve', '领导审批',
 '[]', '["title", "description", "urgency_level", "project_id", "report_no", "create_time"]', '["process_instance_id"]', '[]',
 'VIEW', 3, 1);

-- =============================================================================
-- 6. 创建触发器函数（用于自动更新update_time）
-- =============================================================================

-- 创建更新时间触发器函数
CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.update_time = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 为配置表创建触发器
CREATE TRIGGER update_workflow_config_modtime 
    BEFORE UPDATE ON onl_cgform_workflow_config 
    FOR EACH ROW EXECUTE FUNCTION update_modified_column();

-- 为节点表创建触发器
CREATE TRIGGER update_workflow_node_modtime 
    BEFORE UPDATE ON onl_cgform_workflow_node 
    FOR EACH ROW EXECUTE FUNCTION update_modified_column();

-- =============================================================================
-- 7. 创建视图（便于查询）
-- =============================================================================

-- 创建工作流配置视图
CREATE OR REPLACE VIEW v_workflow_config AS
SELECT 
    wc.id,
    wc.cgform_head_id,
    wc.process_definition_key,
    wc.workflow_enabled,
    wc.version_control_enabled,
    wc.permission_control_enabled,
    wc.business_key_field,
    wc.status_field,
    wc.process_instance_field,
    wc.snapshot_strategy,
    wc.status,
    wc.create_time,
    wc.update_time,
    -- 统计节点数量
    COUNT(wn.id) as node_count
FROM onl_cgform_workflow_config wc
LEFT JOIN onl_cgform_workflow_node wn ON wc.cgform_head_id = wn.cgform_head_id 
    AND wc.process_definition_key = wn.process_definition_key
WHERE wc.status = 1
GROUP BY wc.id, wc.cgform_head_id, wc.process_definition_key, wc.workflow_enabled, 
         wc.version_control_enabled, wc.permission_control_enabled, wc.business_key_field,
         wc.status_field, wc.process_instance_field, wc.snapshot_strategy, wc.status,
         wc.create_time, wc.update_time;

-- 添加视图注释
COMMENT ON VIEW v_workflow_config IS '工作流配置视图（包含节点统计）';

-- =============================================================================
-- 完成
-- =============================================================================

-- 显示创建结果
SELECT 'PostgreSQL工作流集成表创建完成' as message;
SELECT 'onl_cgform_workflow_config' as table_name, COUNT(*) as record_count FROM onl_cgform_workflow_config
UNION ALL
SELECT 'onl_cgform_workflow_node' as table_name, COUNT(*) as record_count FROM onl_cgform_workflow_node
UNION ALL
SELECT 'onl_cgform_workflow_snapshot' as table_name, COUNT(*) as record_count FROM onl_cgform_workflow_snapshot; 