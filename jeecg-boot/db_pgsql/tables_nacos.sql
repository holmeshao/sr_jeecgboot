-- Create database (PostgreSQL syntax)
-- CREATE DATABASE nacos WITH ENCODING 'UTF8' LC_COLLATE='en_US.UTF-8' LC_CTYPE='en_US.UTF-8';

/*
 Navicat Premium Data Transfer

 Source Server         : postgresql
 Source Server Type    : PostgreSQL
 Source Server Version : 16.0
 Source Host           : 127.0.0.1:5432
 Source Schema         : nacos-os

 Target Server Type    : PostgreSQL
 Target Server Version : 16.0
 File Encoding         : 65001

 Date: 28/05/2025 15:48:34
*/

-- Set client encoding
SET client_encoding = 'UTF8';

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS config_info CASCADE;
CREATE TABLE config_info (
  id BIGSERIAL PRIMARY KEY, -- id
  data_id VARCHAR(255) NOT NULL, -- data_id
  group_id VARCHAR(255) DEFAULT NULL,
  content TEXT NOT NULL, -- content
  md5 VARCHAR(32) DEFAULT NULL, -- md5
  gmt_create TIMESTAMP NOT NULL DEFAULT '2010-05-05 00:00:00', -- 创建时间
  gmt_modified TIMESTAMP NOT NULL DEFAULT '2010-05-05 00:00:00', -- 修改时间
  src_user TEXT, -- source user
  src_ip VARCHAR(20) DEFAULT NULL, -- source ip
  app_name VARCHAR(128) DEFAULT NULL,
  tenant_id VARCHAR(128) DEFAULT '' -- 租户字段
);

-- Create unique index
CREATE UNIQUE INDEX uk_configinfo_datagrouptenant ON config_info (data_id, group_id, tenant_id);

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id) VALUES 
(1, 'jeecg-dev.yaml', 'DEFAULT_GROUP', 'spring:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
        allow:
      web-stat-filter:
        enabled: true
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,wall,slf4j
        wall:
          selectWhereAlwayTrueCheck: false
        stat:
          merge-sql: true
          slow-sql-millis: 5000
      datasource:
        master:
          url: jdbc:postgresql://jeecg-boot-pgsql:5432/jeecg-boot?stringtype=unspecified
          username: postgres
          password: postgres
          driver-class-name: org.postgresql.Driver
  redis:
    database: 0
    host: jeecg-boot-redis
    password:
    port: 6379
  rabbitmq:
    host: jeecg-boot-rabbitmq
    username: guest
    password: guest
    port: 5672
    publisher-confirms: true
    publisher-returns: true
    virtual-host: /
    listener:
      simple:
        acknowledge-mode: manual
        concurrency: 1
        max-concurrency: 1
        retry:
          enabled: true
  flyway:
    enabled: false
    locations: classpath:flyway/sql/postgresql
minidao:
  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*
jeecg:
  firewall:
    dataSourceSafe: false
    lowCodeMode: dev
  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a
  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys,/sys/sendChangePwdSms,/sys/user/sendChangePhoneSms,/sys/sms,/desform/api/sendVerifyCode
  uploadType: local
  domainUrl:
    pc: http://localhost:3100
    app: http://localhost:8051
  path:
    upload: /opt/upFiles
    webapp: /opt/webapp
  shiro:
    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**
  oss:
    endpoint: oss-cn-beijing.aliyuncs.com
    accessKey: ??
    secretKey: ??
    bucketName: jeecgdev
    staticDomain: ??
  file-view-domain: 127.0.0.1:8012
  minio:
    minio_url: http://minio.jeecg.com
    minio_name: ??
    minio_pass: ??
    bucketName: otatest
  jmreport:
    saasMode:
    firewall:
      dataSourceSafe: false
      lowCodeMode: dev
  wps:
    domain: https://wwo.wps.cn/office/
    appid: ??
    appsecret: ??
  xxljob:
    enabled: true
    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin
    appname: ${spring.application.name}
    accessToken: ''
    logPath: logs/jeecg/job/jobhandler/
    logRetentionDays: 30
  redisson:
    address: jeecg-boot-redis:6379
    password:
    type: STANDALONE
    enabled: true
  ai-chat:
    enabled: false
    apiKey: "？？？？"
    apiHost: "https://api.openai.com"
    timeout: 60
  ai-rag:
    embed-store:
      host: 127.0.0.1
      port: 5432
      database: postgres
      user: postgres
      password: postgres
      table: embeddings
logging:
  level:
    org.jeecg.modules.system.mapper : info
cas:
  prefixUrl: http://localhost:8888/cas
knife4j:
  production: false
  basic:
    enable: false
    username: jeecg
    password: jeecg1314
justauth:
  enabled: true
  type:
    GITHUB:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback
    WECHAT_ENTERPRISE:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback
      agent-id: ??
    DINGTALK:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback
  cache:
    type: default
    prefix: 'demo::'
    timeout: 1h
third-app:
  enabled: false
  type:
    WECHAT_ENTERPRISE:
      enabled: false
      client-id: ??
      client-secret: ??
      agent-id: ??
    DINGTALK:
      enabled: false
      client-id: ??
      client-secret: ??
      agent-id: ??', '68112d529219e88a44245402ccf54676', '2021-03-03 13:01:11', '2025-05-28 07:47:53', NULL, '0:0:0:0:0:0:0:1', '', '');

-- Reset sequence
SELECT setval('config_info_id_seq', 49, false);

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS config_info_aggr CASCADE;
CREATE TABLE config_info_aggr (
  id BIGSERIAL PRIMARY KEY, -- id
  data_id VARCHAR(255) NOT NULL, -- data_id
  group_id VARCHAR(255) NOT NULL, -- group_id
  datum_id VARCHAR(255) NOT NULL, -- datum_id
  content TEXT NOT NULL, -- 内容
  gmt_modified TIMESTAMP NOT NULL, -- 修改时间
  app_name VARCHAR(128) DEFAULT NULL,
  tenant_id VARCHAR(128) DEFAULT '' -- 租户字段
);

-- Create unique index
CREATE UNIQUE INDEX uk_configinfoaggr_datagrouptenantdatum ON config_info_aggr (data_id, group_id, tenant_id, datum_id);

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- Reset sequence
SELECT setval('config_info_aggr_id_seq', 1, false);

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS config_info_beta CASCADE;
CREATE TABLE config_info_beta (
  id BIGSERIAL PRIMARY KEY, -- id
  data_id VARCHAR(255) NOT NULL, -- data_id
  group_id VARCHAR(128) NOT NULL, -- group_id
  app_name VARCHAR(128) DEFAULT NULL, -- app_name
  content TEXT NOT NULL, -- content
  beta_ips VARCHAR(1024) DEFAULT NULL, -- betaIps
  md5 VARCHAR(32) DEFAULT NULL, -- md5
  gmt_create TIMESTAMP NOT NULL DEFAULT '2010-05-05 00:00:00', -- 创建时间
  gmt_modified TIMESTAMP NOT NULL DEFAULT '2010-05-05 00:00:00', -- 修改时间
  src_user TEXT, -- source user
  src_ip VARCHAR(20) DEFAULT NULL, -- source ip
  tenant_id VARCHAR(128) DEFAULT '' -- 租户字段
);

-- Create unique index
CREATE UNIQUE INDEX uk_configinfobeta_datagrouptenant ON config_info_beta (data_id, group_id, tenant_id);

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- Reset sequence
SELECT setval('config_info_beta_id_seq', 1, false);

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS config_info_tag CASCADE;
CREATE TABLE config_info_tag (
  id BIGSERIAL PRIMARY KEY, -- id
  data_id VARCHAR(255) NOT NULL, -- data_id
  group_id VARCHAR(128) NOT NULL, -- group_id
  tenant_id VARCHAR(128) DEFAULT '', -- tenant_id
  tag_id VARCHAR(128) NOT NULL, -- tag_id
  app_name VARCHAR(128) DEFAULT NULL, -- app_name
  content TEXT NOT NULL, -- content
  md5 VARCHAR(32) DEFAULT NULL, -- md5
  gmt_create TIMESTAMP NOT NULL DEFAULT '2010-05-05 00:00:00', -- 创建时间
  gmt_modified TIMESTAMP NOT NULL DEFAULT '2010-05-05 00:00:00', -- 修改时间
  src_user TEXT, -- source user
  src_ip VARCHAR(20) DEFAULT NULL -- source ip
);

-- Create unique index
CREATE UNIQUE INDEX uk_configinfotag_datagrouptenanttag ON config_info_tag (data_id, group_id, tenant_id, tag_id);

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- Reset sequence
SELECT setval('config_info_tag_id_seq', 1, false);

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS config_tags_relation CASCADE;
CREATE TABLE config_tags_relation (
  id BIGINT NOT NULL, -- id
  tag_name VARCHAR(128) NOT NULL, -- tag_name
  tag_type VARCHAR(64) DEFAULT NULL, -- tag_type
  data_id VARCHAR(255) NOT NULL, -- data_id
  group_id VARCHAR(128) NOT NULL, -- group_id
  tenant_id VARCHAR(128) DEFAULT '', -- tenant_id
  nid BIGSERIAL PRIMARY KEY
);

-- Create indexes
CREATE UNIQUE INDEX uk_configtagrelation_configidtag ON config_tags_relation (id, tag_name, tag_type);
CREATE INDEX idx_tenant_id ON config_tags_relation (tenant_id);

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- Reset sequence
SELECT setval('config_tags_relation_nid_seq', 1, false);

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS group_capacity CASCADE;
CREATE TABLE group_capacity (
  id BIGSERIAL PRIMARY KEY, -- 主键ID
  group_id VARCHAR(128) NOT NULL DEFAULT '', -- Group ID，空字符表示整个集群
  quota INTEGER NOT NULL DEFAULT 0, -- 配额，0表示使用默认值
  usage INTEGER NOT NULL DEFAULT 0, -- 使用量
  max_size INTEGER NOT NULL DEFAULT 0, -- 单个配置大小上限，单位为字节，0表示使用默认值
  max_aggr_count INTEGER NOT NULL DEFAULT 0, -- 聚合子配置最大个数，，0表示使用默认值
  max_aggr_size INTEGER NOT NULL DEFAULT 0, -- 单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值
  max_history_count INTEGER NOT NULL DEFAULT 0, -- 最大变更历史数量
  gmt_create TIMESTAMP NOT NULL DEFAULT '2010-05-05 00:00:00', -- 创建时间
  gmt_modified TIMESTAMP NOT NULL DEFAULT '2010-05-05 00:00:00' -- 修改时间
);

-- Create unique index
CREATE UNIQUE INDEX uk_group_id ON group_capacity (group_id);

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- Reset sequence
SELECT setval('group_capacity_id_seq', 1, false);

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS his_config_info CASCADE;
CREATE TABLE his_config_info (
  id BIGINT NOT NULL,
  nid BIGSERIAL PRIMARY KEY,
  data_id VARCHAR(255) NOT NULL,
  group_id VARCHAR(128) NOT NULL,
  app_name VARCHAR(128) DEFAULT NULL,
  content TEXT NOT NULL,
  md5 VARCHAR(32) DEFAULT NULL,
  gmt_create TIMESTAMP NOT NULL DEFAULT '2010-05-05 00:00:00',
  gmt_modified TIMESTAMP NOT NULL DEFAULT '2010-05-05 00:00:00',
  src_user TEXT,
  src_ip VARCHAR(20) DEFAULT NULL,
  op_type CHAR(10) DEFAULT NULL,
  tenant_id VARCHAR(128) DEFAULT '',
  encrypted_data_key VARCHAR(255) DEFAULT NULL
);

-- Create indexes
CREATE INDEX idx_did_gid_tid ON his_config_info (data_id, group_id, tenant_id);
CREATE INDEX idx_gmt_create ON his_config_info (gmt_create);

-- ----------------------------
-- Records of his_config_info
-- ----------------------------

-- Reset sequence
SELECT setval('his_config_info_nid_seq', 1, false);

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS permissions CASCADE;
CREATE TABLE permissions (
  role VARCHAR(50) NOT NULL,
  resource VARCHAR(255) NOT NULL,
  action VARCHAR(8) NOT NULL,
  PRIMARY KEY (role, resource, action)
);

-- ----------------------------
-- Records of permissions
-- ----------------------------
INSERT INTO permissions (role, resource, action) VALUES 
('ROLE_ADMIN', 'config:*', 'r'),
('ROLE_ADMIN', 'config:*', 'w'),
('ROLE_GUEST', 'config:*:public:*', 'r');

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS roles CASCADE;
CREATE TABLE roles (
  username VARCHAR(50) NOT NULL,
  role VARCHAR(50) NOT NULL,
  CONSTRAINT uk_username_role UNIQUE (username, role)
);

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO roles (username, role) VALUES 
('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS tenant_capacity CASCADE;
CREATE TABLE tenant_capacity (
  id BIGSERIAL PRIMARY KEY, -- 主键ID
  tenant_id VARCHAR(128) NOT NULL DEFAULT '', -- Tenant ID，空字符表示整个集群
  quota INTEGER NOT NULL DEFAULT 0, -- 配额，0表示使用默认值
  usage INTEGER NOT NULL DEFAULT 0, -- 使用量
  max_size INTEGER NOT NULL DEFAULT 0, -- 单个配置大小上限，单位为字节，0表示使用默认值
  max_aggr_count INTEGER NOT NULL DEFAULT 0, -- 聚合子配置最大个数，，0表示使用默认值
  max_aggr_size INTEGER NOT NULL DEFAULT 0, -- 单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值
  max_history_count INTEGER NOT NULL DEFAULT 0, -- 最大变更历史数量
  gmt_create TIMESTAMP NOT NULL DEFAULT '2010-05-05 00:00:00', -- 创建时间
  gmt_modified TIMESTAMP NOT NULL DEFAULT '2010-05-05 00:00:00' -- 修改时间
);

-- Create unique index
CREATE UNIQUE INDEX uk_tenant_id ON tenant_capacity (tenant_id);

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- Reset sequence
SELECT setval('tenant_capacity_id_seq', 1, false);

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS tenant_info CASCADE;
CREATE TABLE tenant_info (
  id BIGSERIAL PRIMARY KEY, -- id
  kp VARCHAR(128) NOT NULL,
  tenant_id VARCHAR(128) DEFAULT '', -- tenant_id
  tenant_name VARCHAR(128) DEFAULT '', -- tenant_name
  tenant_desc VARCHAR(256) DEFAULT NULL, -- tenant_desc
  create_source VARCHAR(32) DEFAULT NULL, -- create_source
  gmt_create BIGINT NOT NULL, -- 创建时间
  gmt_modified BIGINT NOT NULL -- 修改时间
);

-- Create unique index
CREATE UNIQUE INDEX uk_tenant_info_kptenantid ON tenant_info (kp, tenant_id);

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
INSERT INTO tenant_info (id, kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified) VALUES 
(1, '1', 'c4a90b5c-0f5c-4c8b-8c4a-90b5c0f5c4c8', 'tenant1', 'Tenant 1', 'nacos', 1621234567890, 1621234567890),
(2, '1', 'd5b91c6d-1g6d-5d9c-9d5b-91c6d1g6d5d9', 'tenant2', 'Tenant 2', 'nacos', 1621234567891, 1621234567891),
(3, '1', 'e6c92d7e-2h7e-6e0d-0e6c-92d7e2h7e6e0', 'tenant3', 'Tenant 3', 'nacos', 1621234567892, 1621234567892),
(4, '1', '', 'public', 'Public Tenant', 'nacos', 1621234567893, 1621234567893);

-- Reset sequence
SELECT setval('tenant_info_id_seq', 5, false);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
  username VARCHAR(50) NOT NULL PRIMARY KEY,
  password VARCHAR(500) NOT NULL,
  enabled BOOLEAN NOT NULL
);

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO users (username, password, enabled) VALUES 
('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);

SET FOREIGN_KEY_CHECKS = 1;