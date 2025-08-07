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

 Date: 22/07/2025 10:48:00
*/


-- ----------------------------
-- Table structure for data_ingest_moudle_field_mapping
-- ----------------------------
DROP TABLE IF EXISTS "public"."data_ingest_moudle_field_mapping";
CREATE TABLE "public"."data_ingest_moudle_field_mapping" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "task_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "source_field" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "target_field" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "field_type" varchar(50) COLLATE "pg_catalog"."default",
  "is_required" bool DEFAULT false,
  "default_value" varchar(500) COLLATE "pg_catalog"."default",
  "transform_rule" text COLLATE "pg_catalog"."default",
  "create_by" varchar(32) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_by" varchar(32) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."data_ingest_moudle_field_mapping"."id" IS '主键';
COMMENT ON COLUMN "public"."data_ingest_moudle_field_mapping"."task_id" IS '任务ID';
COMMENT ON COLUMN "public"."data_ingest_moudle_field_mapping"."source_field" IS '源字段名';
COMMENT ON COLUMN "public"."data_ingest_moudle_field_mapping"."target_field" IS '目标字段名';
COMMENT ON COLUMN "public"."data_ingest_moudle_field_mapping"."field_type" IS '字段类型';
COMMENT ON COLUMN "public"."data_ingest_moudle_field_mapping"."is_required" IS '是否必填';
COMMENT ON COLUMN "public"."data_ingest_moudle_field_mapping"."default_value" IS '默认值';
COMMENT ON COLUMN "public"."data_ingest_moudle_field_mapping"."transform_rule" IS '转换规则';
COMMENT ON TABLE "public"."data_ingest_moudle_field_mapping" IS '字段映射配置表';

-- ----------------------------
-- Indexes structure for table data_ingest_moudle_field_mapping
-- ----------------------------
CREATE INDEX "idx_data_ingest_moudle_field_mapping_task" ON "public"."data_ingest_moudle_field_mapping" USING btree (
  "task_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table data_ingest_moudle_field_mapping
-- ----------------------------
ALTER TABLE "public"."data_ingest_moudle_field_mapping" ADD CONSTRAINT "data_ingest_moudle_field_mapping_pkey" PRIMARY KEY ("id");
