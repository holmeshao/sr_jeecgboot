-- UI模式与UI Schema扩展（PostgreSQL）

-- 1) 为 onl_cgform_workflow_config 增加 ui_mode 与 ui_schema_json
ALTER TABLE IF EXISTS onl_cgform_workflow_config
  ADD COLUMN IF NOT EXISTS ui_mode varchar(20) DEFAULT 'SPLIT';

ALTER TABLE IF EXISTS onl_cgform_workflow_config
  ADD COLUMN IF NOT EXISTS ui_schema_json jsonb;

COMMENT ON COLUMN onl_cgform_workflow_config.ui_mode IS 'UI模式(SPLIT/INTEGRATED)';
COMMENT ON COLUMN onl_cgform_workflow_config.ui_schema_json IS '融合模式节点UI Schema(JSON)';

-- 2) 可视化UI Schema设计器表（可选）
CREATE TABLE IF NOT EXISTS onl_cgform_workflow_ui (
  id varchar(32) PRIMARY KEY,
  cgform_head_id varchar(32) NOT NULL,
  process_definition_key varchar(100) NOT NULL,
  ui_schema_json jsonb,
  status smallint DEFAULT 1,
  version int DEFAULT 1,
  create_time timestamp DEFAULT CURRENT_TIMESTAMP,
  update_time timestamp DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_form_process_ui
  ON onl_cgform_workflow_ui (cgform_head_id, process_definition_key);

COMMENT ON TABLE onl_cgform_workflow_ui IS '工作流节点UI Schema（可视化设计器）';
COMMENT ON COLUMN onl_cgform_workflow_ui.cgform_head_id IS '表单ID';
COMMENT ON COLUMN onl_cgform_workflow_ui.process_definition_key IS '流程定义Key';
COMMENT ON COLUMN onl_cgform_workflow_ui.ui_schema_json IS '节点UI Schema(JSON)';

