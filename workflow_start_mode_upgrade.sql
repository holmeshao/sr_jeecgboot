-- ============================================================================
-- JeecgBoot工作流启动模式升级脚本
-- 功能：支持多种工作流启动模式，满足不同用户习惯
-- ============================================================================

-- 1. 为工作流配置表添加启动模式字段
ALTER TABLE onl_cgform_workflow_config 
ADD COLUMN workflow_start_mode VARCHAR(20) DEFAULT 'MANUAL';

-- 2. 添加字段注释
COMMENT ON COLUMN onl_cgform_workflow_config.workflow_start_mode IS '工作流启动模式(AUTO自动启动,MANUAL手动启动,OPTIONAL可选启动)';

-- 3. 为业务表添加草稿状态支持
-- 扩展 bmp_status 状态值支持
-- DRAFT: 草稿状态，未启动流程
-- PROCESSING: 流程处理中
-- COMPLETED: 流程已完成
-- CANCELLED: 已取消

-- 4. 创建启动模式枚举检查约束
ALTER TABLE onl_cgform_workflow_config 
ADD CONSTRAINT chk_workflow_start_mode 
CHECK (workflow_start_mode IN ('AUTO', 'MANUAL', 'OPTIONAL'));

-- 5. 为维保工单表添加流程控制字段
ALTER TABLE maintenance_report 
ADD COLUMN workflow_start_mode VARCHAR(20) DEFAULT 'MANUAL',
ADD COLUMN can_start_workflow BOOLEAN DEFAULT TRUE,
ADD COLUMN workflow_start_user VARCHAR(32),
ADD COLUMN workflow_start_time TIMESTAMP;

-- 6. 添加字段注释
COMMENT ON COLUMN maintenance_report.workflow_start_mode IS '工作流启动模式';
COMMENT ON COLUMN maintenance_report.can_start_workflow IS '是否可以启动工作流';
COMMENT ON COLUMN maintenance_report.workflow_start_user IS '工作流启动人';
COMMENT ON COLUMN maintenance_report.workflow_start_time IS '工作流启动时间';

-- 7. 创建索引优化查询
CREATE INDEX idx_maintenance_report_workflow_start ON maintenance_report (workflow_start_mode, can_start_workflow);
CREATE INDEX idx_workflow_config_start_mode ON onl_cgform_workflow_config (workflow_start_mode);

-- 8. 更新现有配置数据（示例）
-- 将现有的配置设置为手动启动模式
UPDATE onl_cgform_workflow_config 
SET workflow_start_mode = 'MANUAL' 
WHERE workflow_enabled = 1;

-- 9. 插入示例配置
INSERT INTO onl_cgform_workflow_config (
    id, cgform_head_id, process_definition_key,
    workflow_enabled, workflow_start_mode, version_control_enabled, permission_control_enabled,
    business_key_field, status_field, process_instance_field,
    snapshot_strategy, snapshot_nodes,
    status, create_time, update_time
) VALUES 
-- 自动启动模式示例
('config_auto_001', 'form_urgent_report', 'urgent_approval_process',
 1, 'AUTO', 1, 1,
 'id', 'bmp_status', 'process_instance_id',
 'NODE', '["submit", "approve", "complete"]',
 1, NOW(), NOW()),

-- 手动启动模式示例 
('config_manual_001', 'form_maintenance_report', 'maintenance_approval_process',
 1, 'MANUAL', 1, 1,
 'id', 'bmp_status', 'process_instance_id',
 'NODE', '["submit", "review", "approve", "complete"]',
 1, NOW(), NOW()),

-- 可选启动模式示例
('config_optional_001', 'form_suggestion', 'suggestion_process',
 1, 'OPTIONAL', 0, 1,
 'id', 'bmp_status', 'process_instance_id',
 'TASK', '["submit", "review"]',
 1, NOW(), NOW());

-- 10. 验证升级结果
SELECT 
    cgform_head_id,
    process_definition_key,
    workflow_start_mode,
    workflow_enabled,
    CASE workflow_start_mode
        WHEN 'AUTO' THEN '提交即启动流程'
        WHEN 'MANUAL' THEN '需手动启动流程'
        WHEN 'OPTIONAL' THEN '用户可选择启动'
        ELSE '未知模式'
    END as mode_description
FROM onl_cgform_workflow_config 
WHERE status = 1
ORDER BY workflow_start_mode;

-- 输出升级完成信息
SELECT 
    '工作流启动模式升级完成!' as message,
    COUNT(*) as total_configs,
    COUNT(CASE WHEN workflow_start_mode = 'AUTO' THEN 1 END) as auto_mode_count,
    COUNT(CASE WHEN workflow_start_mode = 'MANUAL' THEN 1 END) as manual_mode_count,
    COUNT(CASE WHEN workflow_start_mode = 'OPTIONAL' THEN 1 END) as optional_mode_count
FROM onl_cgform_workflow_config 
WHERE status = 1;