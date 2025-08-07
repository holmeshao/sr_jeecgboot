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

 Date: 22/07/2025 10:48:07
*/


-- ----------------------------
-- Table structure for data_ingest_moudle_ingest_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."data_ingest_moudle_ingest_log";
CREATE TABLE "public"."data_ingest_moudle_ingest_log" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "task_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "batch_id" varchar(32) COLLATE "pg_catalog"."default",
  "status" int4 NOT NULL,
  "start_time" timestamp(6),
  "end_time" timestamp(6),
  "record_count" int8 DEFAULT 0,
  "success_count" int8 DEFAULT 0,
  "fail_count" int8 DEFAULT 0,
  "error_message" text COLLATE "pg_catalog"."default",
  "execute_log" text COLLATE "pg_catalog"."default",
  "create_by" varchar(32) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_by" varchar(32) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_log"."id" IS '主键';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_log"."task_id" IS '任务ID';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_log"."batch_id" IS '执行批次';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_log"."status" IS '执行状态';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_log"."start_time" IS '开始时间';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_log"."end_time" IS '结束时间';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_log"."record_count" IS '处理记录数';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_log"."success_count" IS '成功记录数';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_log"."fail_count" IS '失败记录数';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_log"."error_message" IS '错误信息';
COMMENT ON COLUMN "public"."data_ingest_moudle_ingest_log"."execute_log" IS '执行日志';
COMMENT ON TABLE "public"."data_ingest_moudle_ingest_log" IS '数据接入日志表';

-- ----------------------------
-- Indexes structure for table data_ingest_moudle_ingest_log
-- ----------------------------
CREATE INDEX "idx_data_ingest_moudle_ingest_log_status" ON "public"."data_ingest_moudle_ingest_log" USING btree (
  "status" "pg_catalog"."int4_ops" ASC NULLS LAST
);
CREATE INDEX "idx_data_ingest_moudle_ingest_log_task" ON "public"."data_ingest_moudle_ingest_log" USING btree (
  "task_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_data_ingest_moudle_ingest_log_time" ON "public"."data_ingest_moudle_ingest_log" USING btree (
  "start_time" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table data_ingest_moudle_ingest_log
-- ----------------------------
ALTER TABLE "public"."data_ingest_moudle_ingest_log" ADD CONSTRAINT "data_ingest_moudle_ingest_log_pkey" PRIMARY KEY ("id");
