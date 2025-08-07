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

 Date: 22/07/2025 10:48:39
*/


-- ----------------------------
-- Table structure for data_ingest_moudle_ingest_task
-- ----------------------------
DROP TABLE IF EXISTS "public"."data_ingest_moudle_ingest_task";
CREATE TABLE "public"."data_ingest_moudle_ingest_task" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "task_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "task_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "data_source_id" varchar(32) COLLATE "pg_catalog"."default",
  "task_config" text COLLATE "pg_catalog"."default",
  "target_table" varchar(100) COLLATE "pg_catalog"."default",
  "schedule_config" text COLLATE "pg_catalog"."default",
  "status" int4 DEFAULT 0,
  "last_execute_time" timestamp(6),
  "next_execute_time" timestamp(6),
  "execute_count" int8 DEFAULT 0,
  "success_count" int8 DEFAULT 0,
  "fail_count" int8 DEFAULT 0,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "create_by" varchar(32) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_by" varchar(32) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_task"."id" IS '主键';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_task"."task_name" IS '任务名称';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_task"."task_type" IS '任务类型';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_task"."data_source_id" IS '数据源ID';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_task"."task_config" IS '任务配置';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_task"."target_table" IS '目标表名';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_task"."schedule_config" IS '调度配置';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_task"."status" IS '任务状态';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_task"."last_execute_time" IS '上次执行时间';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_task"."next_execute_time" IS '下次执行时间';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_task"."execute_count" IS '执行次数';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_task"."success_count" IS '成功次数';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_task"."fail_count" IS '失败次数';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_task"."remark" IS '备注';
COMMENT ON TABLE "public"."data_ingest_moudle_ingest_task" IS '数据接入任务表';

-- ----------------------------
-- Indexes structure for table data_ingest_moudle_ingest_task
-- ----------------------------
CREATE INDEX "idx_data_ingest_moudle_ingest_task_source" ON "public"."data_ingest_moudle_ingest_task" USING btree (
  "data_source_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_data_ingest_moudle_ingest_task_status" ON "public"."data_ingest_moudle_ingest_task" USING btree (
  "status" "pg_catalog"."int4_ops" ASC NULLS LAST
);
CREATE INDEX "idx_data_ingest_moudle_ingest_task_type" ON "public"."data_ingest_moudle_ingest_task" USING btree (
  "task_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table data_ingest_moudle_ingest_task
-- ----------------------------
ALTER TABLE "public"."data_ingest_moudle_ingest_task" ADD CONSTRAINT "data_ingest_moudle_ingest_task_pkey" PRIMARY KEY ("id");
