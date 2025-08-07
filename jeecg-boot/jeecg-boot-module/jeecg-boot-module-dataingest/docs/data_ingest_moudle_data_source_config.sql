/*
 Navicat Premium Dump SQL

 Source Server         : pgsql-jeecg-boot
 Source Server Type    : PostgreSQL
 Source Server Version : 170005 (170005)
 Source Host           : localhost:5432
 Source Catalog        : jeecg-boot
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 170005 (170005)
 File Encoding         : 65001

 Date: 22/07/2025 10:47:50
*/


-- ----------------------------
-- Table structure for data_ingest_moudle_data_source_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."data_ingest_moudle_data_source_config";
CREATE TABLE "public"."data_ingest_moudle_data_source_config" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "create_by" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" date NOT NULL,
  "update_by" varchar(50) COLLATE "pg_catalog"."default",
  "update_time" date,
  "sys_org_code" varchar(64) COLLATE "pg_catalog"."default",
  "source_name" varchar(100) COLLATE "pg_catalog"."default",
  "source_type" varchar(50) COLLATE "pg_catalog"."default",
  "connection_config" text COLLATE "pg_catalog"."default",
  "auth_config" text COLLATE "pg_catalog"."default",
  "status" int4,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "tenant_id" varchar(36) COLLATE "pg_catalog"."default",
  "task_id" varchar(36) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."data_ingest_moudle_data_source_config"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."data_ingest_moudle_data_source_config"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."data_ingest_moudle_data_source_config"."update_by" IS '更新人';
COMMENT ON COLUMN "public"."data_ingest_moudle_data_source_config"."update_time" IS '更新日期';
COMMENT ON COLUMN "public"."data_ingest_moudle_data_source_config"."sys_org_code" IS '所属部门';
COMMENT ON COLUMN "public"."data_ingest_moudle_data_source_config"."source_name" IS '数据源名称';
COMMENT ON COLUMN "public"."data_ingest_moudle_data_source_config"."source_type" IS '数据源类型';
COMMENT ON COLUMN "public"."data_ingest_moudle_data_source_config"."connection_config" IS '连接配置';
COMMENT ON COLUMN "public"."data_ingest_moudle_data_source_config"."auth_config" IS '认证配置';
COMMENT ON COLUMN "public"."data_ingest_moudle_data_source_config"."status" IS '状态';
COMMENT ON COLUMN "public"."data_ingest_moudle_data_source_config"."remark" IS '备注';
COMMENT ON COLUMN "public"."data_ingest_moudle_data_source_config"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."data_ingest_moudle_data_source_config"."task_id" IS '任务ID';

-- ----------------------------
-- Primary Key structure for table data_ingest_moudle_data_source_config
-- ----------------------------
ALTER TABLE "public"."data_ingest_moudle_data_source_config" ADD CONSTRAINT "data_ingest_moudle_data_source_config_pkey" PRIMARY KEY ("id");
