-- 行政区划表（省/市/区县）
-- 适用于 MySQL 5.7+/8.0
-- 使用方式：
-- 1) 先执行本建表脚本
-- 2) 再导入同目录生成的 sys_region_data.sql 全量数据

DROP TABLE IF EXISTS `sys_region`;
CREATE TABLE `sys_region` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `code` CHAR(6) NOT NULL COMMENT 'GB/T 2260 六位行政区划码，如 110000/110100/110105',
  `name` VARCHAR(64) NOT NULL COMMENT '名称',
  `parent_code` CHAR(6) DEFAULT NULL COMMENT '父级 code；省级为 NULL',
  `level` TINYINT NOT NULL COMMENT '层级：1省 2市 3区县',
  `path` VARCHAR(64) DEFAULT NULL COMMENT '层级路径：如 110000/110100/110105',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '同级排序',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0停用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_parent` (`parent_code`),
  KEY `idx_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='行政区划（省/市/区县）';


