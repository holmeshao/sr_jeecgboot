-- UI模式与UI Schema扩展（MySQL 5.7+）

-- 1) 为 onl_cgform_workflow_config 增加 ui_mode 与 ui_schema_json
ALTER TABLE `onl_cgform_workflow_config`
  ADD COLUMN `ui_mode` varchar(20) DEFAULT 'SPLIT' COMMENT 'UI模式(SPLIT/INTEGRATED)';

ALTER TABLE `onl_cgform_workflow_config`
  ADD COLUMN `ui_schema_json` MEDIUMTEXT COMMENT '融合模式节点UI Schema(JSON)';

-- 2) 可视化UI Schema设计器表（可选）
CREATE TABLE IF NOT EXISTS `onl_cgform_workflow_ui` (
  `id` varchar(32) NOT NULL,
  `cgform_head_id` varchar(32) NOT NULL COMMENT '表单ID',
  `process_definition_key` varchar(100) NOT NULL COMMENT '流程定义Key',
  `ui_schema_json` MEDIUMTEXT COMMENT '节点UI Schema(JSON)',
  `status` tinyint(1) DEFAULT 1,
  `version` int DEFAULT 1,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_form_process_ui` (`cgform_head_id`, `process_definition_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作流节点UI Schema（可视化设计器）';

