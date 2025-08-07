-- =============================================================================
-- JeecgBoot工作流按钮扩展 - 基于现有按钮系统
-- 理念：不重新发明轮子，基于现有onl_cgform_button表扩展
-- =============================================================================

-- 1. 为现有按钮表添加工作流相关字段（可选，保持兼容性）
ALTER TABLE onl_cgform_button ADD COLUMN IF NOT EXISTS workflow_action varchar(50) COMMENT '工作流动作类型';
ALTER TABLE onl_cgform_button ADD COLUMN IF NOT EXISTS show_condition text COMMENT '显示条件表达式';
ALTER TABLE onl_cgform_button ADD COLUMN IF NOT EXISTS confirm_message varchar(500) COMMENT '确认消息';
ALTER TABLE onl_cgform_button ADD COLUMN IF NOT EXISTS success_message varchar(500) COMMENT '成功消息';
ALTER TABLE onl_cgform_button ADD COLUMN IF NOT EXISTS button_permissions varchar(500) COMMENT '按钮权限编码';

-- 2. 插入工作流相关的内置按钮
INSERT INTO onl_cgform_button (
    id, button_code, button_icon, button_name, button_status, button_style, 
    exp, cgform_head_id, opt_type, order_num, button_type, workflow_action, 
    show_condition, confirm_message, success_message
) VALUES 
-- 工作流基础按钮
('wf_save_draft_001', 'save_draft', 'ant-design:save-outlined', '保存草稿', '1', 'button', 
 '', '', 'js', 10, '2', 'SAVE_DRAFT', 
 'status == "CREATE" || status == "DRAFT"', null, '草稿保存成功'),

('wf_submit_review_001', 'submit_review', 'ant-design:send-outlined', '提交审核', '1', 'button', 
 '', '', 'js', 20, '2', 'SUBMIT_REVIEW', 
 'status == "DRAFT"', '确定要提交审核吗？提交后将无法修改', '提交成功，请等待审核'),

('wf_approve_001', 'approve', 'ant-design:check-circle-outlined', '审核通过', '1', 'button', 
 '', '', 'js', 30, '2', 'APPROVE', 
 'currentTask != null && hasPermission("approve")', '确定审核通过吗？', '审核通过'),

('wf_reject_001', 'reject', 'ant-design:close-circle-outlined', '审核拒绝', '1', 'button', 
 '', '', 'js', 40, '2', 'REJECT', 
 'currentTask != null && hasPermission("reject")', '确定要拒绝吗？请填写拒绝理由', '已拒绝'),

-- 工作流高级按钮
('wf_transfer_001', 'transfer', 'ant-design:share-alt-outlined', '转办', '1', 'button', 
 '', '', 'js', 50, '2', 'TRANSFER', 
 'currentTask != null && hasPermission("transfer")', '请选择转办人员', '转办成功'),

('wf_go_back_001', 'go_back', 'ant-design:rollback-outlined', '退回', '1', 'button', 
 '', '', 'js', 60, '2', 'GO_BACK', 
 'currentTask != null && hasPermission("goback")', '确定要退回到上一步吗？', '退回成功'),

('wf_delegate_001', 'delegate', 'ant-design:user-switch-outlined', '委派', '1', 'button', 
 '', '', 'js', 70, '2', 'DELEGATE', 
 'currentTask != null && hasPermission("delegate")', '请选择委派人员', '委派成功'),

('wf_suspend_001', 'suspend', 'ant-design:pause-circle-outlined', '挂起', '1', 'button', 
 '', '', 'js', 80, '2', 'SUSPEND', 
 'currentTask != null && hasPermission("suspend")', '挂起后流程将暂停，确定要挂起吗？', '流程已挂起'),

('wf_revoke_001', 'revoke', 'ant-design:undo-outlined', '撤回', '1', 'button', 
 '', '', 'js', 15, '2', 'REVOKE', 
 'status == "PROCESSING" && userId == createBy', '撤回后流程将终止，确定要撤回吗？', '撤回成功'),

-- 特殊场景按钮
('wf_emergency_001', 'emergency_approve', 'ant-design:thunderbolt-outlined', '紧急审批', '1', 'button', 
 '', '', 'js', 25, '2', 'EMERGENCY_APPROVE', 
 'formData.priority == "EMERGENCY" && hasPermission("emergency")', '这是紧急审批，确定要立即通过吗？', '紧急审批完成'),

('wf_batch_approve_001', 'batch_approve', 'ant-design:check-square-outlined', '批量审批', '1', 'button', 
 '', '', 'js', 35, '2', 'BATCH_APPROVE', 
 'selectedItems.length > 1 && hasPermission("batch_approve")', '确定要批量审批选中项吗？', '批量审批完成');

-- 3. 更新现有的bpm按钮，使其与新的工作流按钮兼容
UPDATE onl_cgform_button 
SET workflow_action = 'START_WORKFLOW',
    show_condition = 'status == "DRAFT" && workflowEnabled',
    confirm_message = '确定要启动工作流吗？',
    success_message = '工作流启动成功'
WHERE button_code = 'bpm';

-- 4. 创建按钮分组视图（方便管理）
CREATE OR REPLACE VIEW v_cgform_button_groups AS
SELECT 
    'BASIC' as button_group,
    '基础操作' as group_name,
    button_code,
    button_name,
    button_status
FROM onl_cgform_button 
WHERE button_code IN ('add', 'edit', 'detail', 'delete', 'batch_delete')

UNION ALL

SELECT 
    'DATA' as button_group,
    '数据操作' as group_name,
    button_code,
    button_name,
    button_status
FROM onl_cgform_button 
WHERE button_code IN ('import', 'export', 'query', 'reset', 'super_query')

UNION ALL

SELECT 
    'WORKFLOW' as button_group,
    '工作流操作' as group_name,
    button_code,
    button_name,
    button_status
FROM onl_cgform_button 
WHERE workflow_action IS NOT NULL;

-- 5. 创建按钮权限配置表（链接表单和按钮）
CREATE TABLE IF NOT EXISTS onl_cgform_button_config (
    id varchar(32) NOT NULL PRIMARY KEY,
    cgform_head_id varchar(32) NOT NULL COMMENT '表单ID',
    button_code varchar(100) NOT NULL COMMENT '按钮编码',
    button_status char(1) DEFAULT '1' COMMENT '按钮状态(0禁用1启用)',
    show_condition text COMMENT '显示条件(覆盖默认条件)',
    sort_order int DEFAULT 0 COMMENT '排序',
    create_by varchar(50) COMMENT '创建人',
    create_time datetime COMMENT '创建时间',
    update_by varchar(50) COMMENT '更新人',
    update_time datetime COMMENT '更新时间',
    UNIQUE KEY uk_cgform_button_config (cgform_head_id, button_code)
) COMMENT = '表单按钮配置表';

-- 6. 创建索引
CREATE INDEX IF NOT EXISTS idx_button_workflow_action ON onl_cgform_button(workflow_action);
CREATE INDEX IF NOT EXISTS idx_button_config_form ON onl_cgform_button_config(cgform_head_id);

-- 7. 插入默认配置示例
INSERT INTO onl_cgform_button_config (
    id, cgform_head_id, button_code, button_status, sort_order, create_time
) VALUES
-- 假设有一个表单需要工作流按钮
('config_001', 'your_form_id_here', 'save_draft', '1', 10, NOW()),
('config_002', 'your_form_id_here', 'submit_review', '1', 20, NOW()),
('config_003', 'your_form_id_here', 'approve', '1', 30, NOW()),
('config_004', 'your_form_id_here', 'reject', '1', 40, NOW());

-- 完成扩展
SELECT '工作流按钮扩展完成！基于现有onl_cgform_button表，添加了工作流相关按钮' as result;