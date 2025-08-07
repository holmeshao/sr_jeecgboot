#
# XXL-JOB v2.2.0
# Copyright (c) 2015-present, xuxueli.

-- Create database (PostgreSQL syntax)
-- CREATE DATABASE xxl_job WITH ENCODING 'UTF8' LC_COLLATE='en_US.UTF-8' LC_CTYPE='en_US.UTF-8';

/*
 Navicat Premium Data Transfer

 Source Server         : postgresql
 Source Server Type    : PostgreSQL
 Source Server Version : 16.0
 Source Host           : 127.0.0.1:5432
 Source Schema         : xxl_job

 Target Server Type    : PostgreSQL
 Target Server Version : 16.0
 File Encoding         : 65001

 Date: 10/02/2025 13:49:31
*/

-- Set client encoding
SET client_encoding = 'UTF8';

-- ----------------------------
-- Table structure for xxl_job_group
-- ----------------------------
DROP TABLE IF EXISTS xxl_job_group CASCADE;
CREATE TABLE xxl_job_group (
  id SERIAL PRIMARY KEY,
  app_name VARCHAR(64) NOT NULL, -- 执行器AppName
  title VARCHAR(12) NOT NULL, -- 执行器名称
  address_type SMALLINT NOT NULL DEFAULT 0, -- 执行器地址类型：0=自动注册、1=手动录入
  address_list TEXT, -- 执行器地址列表，多地址逗号分隔
  update_time TIMESTAMP
);

-- ----------------------------
-- Records of xxl_job_group
-- ----------------------------
INSERT INTO xxl_job_group (id, app_name, title, address_type, address_list, update_time) VALUES 
(1, 'xxl-job-executor-sample', '示例执行器', 0, NULL, '2025-02-10 13:49:04'),
(2, 'jeecg-demo', '测试Demo模块', 0, NULL, '2025-02-10 13:49:04'),
(3, 'jeecg-system', '系统System模块', 0, NULL, '2025-02-10 13:49:04');

-- Reset sequence
SELECT setval('xxl_job_group_id_seq', 4, false);

-- ----------------------------
-- Table structure for xxl_job_info
-- ----------------------------
DROP TABLE IF EXISTS xxl_job_info CASCADE;
CREATE TABLE xxl_job_info (
  id SERIAL PRIMARY KEY,
  job_group INTEGER NOT NULL, -- 执行器主键ID
  job_desc VARCHAR(255) NOT NULL,
  add_time TIMESTAMP,
  update_time TIMESTAMP,
  author VARCHAR(64), -- 作者
  alarm_email VARCHAR(255), -- 报警邮件
  schedule_type VARCHAR(50) NOT NULL DEFAULT 'NONE', -- 调度类型
  schedule_conf VARCHAR(128), -- 调度配置，值含义取决于调度类型
  misfire_strategy VARCHAR(50) NOT NULL DEFAULT 'DO_NOTHING', -- 调度过期策略
  executor_route_strategy VARCHAR(50), -- 执行器路由策略
  executor_handler VARCHAR(255), -- 执行器任务handler
  executor_param VARCHAR(512), -- 执行器任务参数
  executor_block_strategy VARCHAR(50), -- 阻塞处理策略
  executor_timeout INTEGER NOT NULL DEFAULT 0, -- 任务执行超时时间，单位秒
  executor_fail_retry_count INTEGER NOT NULL DEFAULT 0, -- 失败重试次数
  glue_type VARCHAR(50) NOT NULL, -- GLUE类型
  glue_source TEXT, -- GLUE源代码
  glue_remark VARCHAR(128), -- GLUE备注
  glue_updatetime TIMESTAMP, -- GLUE更新时间
  child_jobid VARCHAR(255), -- 子任务ID，多个逗号分隔
  trigger_status SMALLINT NOT NULL DEFAULT 0, -- 调度状态：0-停止，1-运行
  trigger_last_time BIGINT NOT NULL DEFAULT 0, -- 上次调度时间
  trigger_next_time BIGINT NOT NULL DEFAULT 0 -- 下次调度时间
);

-- ----------------------------
-- Records of xxl_job_info
-- ----------------------------
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES 
(1, 1, '测试任务1', '2018-11-03 22:21:31', '2024-08-21 22:30:30', 'XXL', '', 'CRON', '0 0 0 * * ? *', 'DO_NOTHING', 'FIRST', 'demoJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2018-11-03 22:21:31', '', 1, 1729353600000, 1739203200000),
(2, 3, '测试jeecg xxljob', '2024-08-21 22:41:10', '2024-08-21 22:41:30', 'JEECG', '', 'CRON', '* * * * * ?', 'DO_NOTHING', 'FIRST', 'demoJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-08-21 22:41:10', '', 1, 1739166572000, 1739166573000);

-- Reset sequence
SELECT setval('xxl_job_info_id_seq', 3, false);

-- ----------------------------
-- Table structure for xxl_job_lock
-- ----------------------------
DROP TABLE IF EXISTS xxl_job_lock CASCADE;
CREATE TABLE xxl_job_lock (
  lock_name VARCHAR(50) PRIMARY KEY -- 锁名称
);

-- ----------------------------
-- Records of xxl_job_lock
-- ----------------------------
INSERT INTO xxl_job_lock (lock_name) VALUES ('schedule_lock');

-- ----------------------------
-- Table structure for xxl_job_log
-- ----------------------------
DROP TABLE IF EXISTS xxl_job_log CASCADE;
CREATE TABLE xxl_job_log (
  id BIGSERIAL PRIMARY KEY,
  job_group INTEGER NOT NULL, -- 执行器主键ID
  job_id INTEGER NOT NULL, -- 任务，主键ID
  executor_address VARCHAR(255), -- 执行器地址，本次执行的地址
  executor_handler VARCHAR(255), -- 执行器任务handler
  executor_param VARCHAR(512), -- 执行器任务参数
  executor_sharding_param VARCHAR(20), -- 执行器任务分片参数，格式如 1/2
  executor_fail_retry_count INTEGER NOT NULL DEFAULT 0, -- 失败重试次数
  trigger_time TIMESTAMP, -- 调度-时间
  trigger_code INTEGER NOT NULL, -- 调度-结果
  trigger_msg TEXT, -- 调度-日志
  handle_time TIMESTAMP, -- 执行-时间
  handle_code INTEGER NOT NULL, -- 执行-状态
  handle_msg TEXT, -- 执行-日志
  alarm_status SMALLINT NOT NULL DEFAULT 0 -- 告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败
);

-- Create indexes
CREATE INDEX I_trigger_time ON xxl_job_log (trigger_time);
CREATE INDEX I_handle_code ON xxl_job_log (handle_code);

-- ----------------------------
-- Records of xxl_job_log
-- ----------------------------
INSERT INTO xxl_job_log (id, job_group, job_id, executor_address, executor_handler, executor_param, executor_sharding_param, executor_fail_retry_count, trigger_time, trigger_code, trigger_msg, handle_time, handle_code, handle_msg, alarm_status) VALUES 
(6618, 3, 2, NULL, 'demoJob', '', NULL, 0, '2025-02-10 13:47:09', 500, '任务触发类型：Cron触发<br>调度机器：192.168.1.11<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style="color:#00c0ef;" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 2),
(6619, 3, 2, NULL, 'demoJob', '', NULL, 0, '2025-02-10 13:47:10', 500, '任务触发类型：Cron触发<br>调度机器：192.168.1.11<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style="color:#00c0ef;" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 2),
(6620, 3, 2, NULL, 'demoJob', '', NULL, 0, '2025-02-10 13:47:11', 500, '任务触发类型：Cron触发<br>调度机器：192.168.1.11<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style="color:#00c0ef;" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 2);

-- Reset sequence
SELECT setval('xxl_job_log_id_seq', 6761, false);

-- ----------------------------
-- Table structure for xxl_job_log_report
-- ----------------------------
DROP TABLE IF EXISTS xxl_job_log_report CASCADE;
CREATE TABLE xxl_job_log_report (
  id SERIAL PRIMARY KEY,
  trigger_day TIMESTAMP, -- 调度-时间
  running_count INTEGER NOT NULL DEFAULT 0, -- 运行中-日志数量
  suc_count INTEGER NOT NULL DEFAULT 0, -- 执行成功-日志数量
  fail_count INTEGER NOT NULL DEFAULT 0, -- 执行失败-日志数量
  update_time TIMESTAMP
);

-- ----------------------------
-- Records of xxl_job_log_report
-- ----------------------------
INSERT INTO xxl_job_log_report (id, trigger_day, running_count, suc_count, fail_count, update_time) VALUES 
(1, '2025-02-10 00:00:00', 0, 0, 0, '2025-02-10 13:49:04'),
(2, '2025-02-11 00:00:00', 0, 0, 0, '2025-02-10 13:49:04'),
(3, '2025-02-12 00:00:00', 0, 0, 0, '2025-02-10 13:49:04'),
(4, '2025-02-13 00:00:00', 0, 0, 0, '2025-02-10 13:49:04'),
(5, '2025-02-14 00:00:00', 0, 0, 0, '2025-02-10 13:49:04'),
(6, '2025-02-15 00:00:00', 0, 0, 0, '2025-02-10 13:49:04'),
(7, '2025-02-16 00:00:00', 0, 0, 0, '2025-02-10 13:49:04'),
(8, '2025-02-17 00:00:00', 0, 0, 0, '2025-02-10 13:49:04'),
(9, '2025-02-18 00:00:00', 0, 0, 0, '2025-02-10 13:49:04'),
(10, '2025-02-19 00:00:00', 0, 0, 0, '2025-02-10 13:49:04'),
(11, '2025-02-20 00:00:00', 0, 0, 0, '2025-02-10 13:49:04'),
(12, '2025-02-21 00:00:00', 0, 0, 0, '2025-02-10 13:49:04'),
(13, '2025-02-22 00:00:00', 0, 0, 0, '2025-02-10 13:49:04');

-- Reset sequence
SELECT setval('xxl_job_log_report_id_seq', 14, false);

-- ----------------------------
-- Table structure for xxl_job_logglue
-- ----------------------------
DROP TABLE IF EXISTS xxl_job_logglue CASCADE;
CREATE TABLE xxl_job_logglue (
  id SERIAL PRIMARY KEY,
  job_id INTEGER NOT NULL, -- 任务，主键ID
  glue_type VARCHAR(50), -- GLUE类型
  glue_source TEXT, -- GLUE源代码
  glue_remark VARCHAR(128), -- GLUE备注
  add_time TIMESTAMP,
  update_time TIMESTAMP
);

-- ----------------------------
-- Records of xxl_job_logglue
-- ----------------------------

-- Reset sequence
SELECT setval('xxl_job_logglue_id_seq', 1, false);

-- ----------------------------
-- Table structure for xxl_job_registry
-- ----------------------------
DROP TABLE IF EXISTS xxl_job_registry CASCADE;
CREATE TABLE xxl_job_registry (
  id SERIAL PRIMARY KEY,
  registry_group VARCHAR(50) NOT NULL, -- 执行器地址类型：0=自动注册、1=手动录入
  registry_key VARCHAR(255) NOT NULL, -- 执行器地址列表，多地址逗号分隔
  registry_value VARCHAR(255) NOT NULL, -- 执行器地址列表，多地址逗号分隔
  update_time TIMESTAMP
);

-- ----------------------------
-- Records of xxl_job_registry
-- ----------------------------

-- Reset sequence
SELECT setval('xxl_job_registry_id_seq', 1, false);

-- ----------------------------
-- Table structure for xxl_job_user
-- ----------------------------
DROP TABLE IF EXISTS xxl_job_user CASCADE;
CREATE TABLE xxl_job_user (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL, -- 账号
  password VARCHAR(50) NOT NULL, -- 密码
  role VARCHAR(50) NOT NULL, -- 角色：0-普通用户、1-管理员
  permission VARCHAR(255), -- 权限：执行器ID列表，多个逗号分割
  add_time TIMESTAMP,
  update_time TIMESTAMP
);

-- ----------------------------
-- Records of xxl_job_user
-- ----------------------------
INSERT INTO xxl_job_user (id, username, password, role, permission, add_time, update_time) VALUES 
(1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL, '2025-02-10 13:49:04', '2025-02-10 13:49:04'),
(2, 'user', 'e10adc3949ba59abbe56e057f20f883e', 0, '1', '2025-02-10 13:49:04', '2025-02-10 13:49:04');

-- Reset sequence
SELECT setval('xxl_job_user_id_seq', 3, false);

SET FOREIGN_KEY_CHECKS = 1;

