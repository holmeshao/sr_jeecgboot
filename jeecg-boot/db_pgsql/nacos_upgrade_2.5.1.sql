-- Nacos 2.5.1 升级脚本
-- 添加缺失的表和列

-- 1. 添加 config_info_gray 表
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'config_info_gray') THEN
        CREATE TABLE config_info_gray (
            id BIGSERIAL PRIMARY KEY,
            data_id VARCHAR(255) NOT NULL,
            group_id VARCHAR(255) DEFAULT NULL,
            content TEXT NOT NULL,
            md5 VARCHAR(32) DEFAULT NULL,
            gmt_create TIMESTAMP NOT NULL DEFAULT '2010-05-05 00:00:00',
            gmt_modified TIMESTAMP NOT NULL DEFAULT '2010-05-05 00:00:00',
            src_user TEXT,
            src_ip VARCHAR(20) DEFAULT NULL,
            app_name VARCHAR(128) DEFAULT NULL,
            tenant_id VARCHAR(128) DEFAULT ''
        );
        CREATE UNIQUE INDEX uk_configinfogray_datagrouptenant ON config_info_gray (data_id, group_id, tenant_id);
        RAISE NOTICE 'config_info_gray table created successfully';
    END IF;
END $$;

-- 2. 为 his_config_info 表添加缺失的列
DO $$
BEGIN
    -- 添加 publish_type 列
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'his_config_info' AND column_name = 'publish_type') THEN
        ALTER TABLE his_config_info ADD COLUMN publish_type VARCHAR(20) DEFAULT NULL;
        RAISE NOTICE 'Added publish_type column to his_config_info';
    END IF;
    
    -- 添加 gray_name 列
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'his_config_info' AND column_name = 'gray_name') THEN
        ALTER TABLE his_config_info ADD COLUMN gray_name VARCHAR(255) DEFAULT NULL;
        RAISE NOTICE 'Added gray_name column to his_config_info';
    END IF;
    
    -- 添加 ext_info 列
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'his_config_info' AND column_name = 'ext_info') THEN
        ALTER TABLE his_config_info ADD COLUMN ext_info TEXT DEFAULT NULL;
        RAISE NOTICE 'Added ext_info column to his_config_info';
    END IF;
    
    -- 添加 encrypted_data_key 列
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'his_config_info' AND column_name = 'encrypted_data_key') THEN
        ALTER TABLE his_config_info ADD COLUMN encrypted_data_key TEXT DEFAULT NULL;
        RAISE NOTICE 'Added encrypted_data_key column to his_config_info';
    END IF;
END $$;

-- 3. 为 config_info 表添加缺失的列
DO $$
BEGIN
    -- 添加 encrypted_data_key 列
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'config_info' AND column_name = 'encrypted_data_key') THEN
        ALTER TABLE config_info ADD COLUMN encrypted_data_key TEXT DEFAULT NULL;
        RAISE NOTICE 'Added encrypted_data_key column to config_info';
    END IF;
END $$;

-- 4. 修复 his_config_info 表的 nid 列约束
DO $$
BEGIN
    -- 检查 nid 列是否存在且为非空约束
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'his_config_info' AND column_name = 'nid' AND is_nullable = 'NO') THEN
        -- 将 nid 列改为可空
        ALTER TABLE his_config_info ALTER COLUMN nid DROP NOT NULL;
        RAISE NOTICE 'Removed NOT NULL constraint from nid column in his_config_info';
    END IF;
END $$; 