#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import re

def convert_file(input_file, output_file):
    with open(input_file, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # 基本转换规则
    replacements = [
        # 数据库创建语句
        (r'CREATE database if NOT EXISTS `([^`]+)` default character set ([^;]+);', 
         r'-- CREATE DATABASE \1 WITH ENCODING \'UTF8\' LC_COLLATE=\'en_US.UTF-8\' LC_CTYPE=\'en_US.UTF-8\';'),
        
        # USE 语句
        (r'use `([^`]+)`;', r'-- USE \1;'),
        
        # SET 语句
        (r'SET NAMES ([^;]+);', r'SET client_encoding = \'UTF8\';'),
        (r'SET FOREIGN_KEY_CHECKS = 0;', r'-- SET FOREIGN_KEY_CHECKS = 0;'),
        (r'SET FOREIGN_KEY_CHECKS = 1;', r'-- SET FOREIGN_KEY_CHECKS = 1;'),
        
        # 注释中的服务器信息
        (r'Source Server Type\s*:\s*MySQL', 'Source Server Type    : PostgreSQL'),
        (r'Target Server Type\s*:\s*MySQL', 'Target Server Type    : PostgreSQL'),
        (r'Source Server Version\s*:\s*[0-9.]+', 'Source Server Version : 16.0'),
        (r'Target Server Version\s*:\s*[0-9.]+', 'Target Server Version : 16.0'),
        (r'Source Host\s*:\s*[^:]+:\d+', 'Source Host           : 127.0.0.1:5432'),
        
        # 数据类型转换 - 更精确的匹配
        (r'\bint\([0-9]+\)\b', 'INTEGER'),
        (r'\bbigint\([0-9]+\)\b', 'BIGINT'),
        (r'\btinyint\([0-9]+\)\b', 'SMALLINT'),
        (r'\bmediumtext\b', 'TEXT'),
        (r'\blongtext\b', 'TEXT'),
        (r'\bdatetime\b', 'TIMESTAMP'),
        
        # AUTO_INCREMENT
        (r'NOT NULL AUTO_INCREMENT', 'SERIAL'),
        (r'AUTO_INCREMENT = [0-9]+', ''),
        
        # 移除 MySQL 特定语法
        (r'ENGINE = InnoDB[^)]*', ''),
        (r'CHARACTER SET = [^ ]+', ''),
        (r'COLLATE = [^ ]+', ''),
        (r'ROW_FORMAT = [^)]*', ''),
        (r'USING BTREE', ''),
        (r'UNSIGNED ', ''),
        
        # 处理 CHARACTER SET 和 COLLATE
        (r'CHARACTER SET [^ ]+ COLLATE [^ ]+', ''),
        (r'COLLATE [^ ]+', ''),
        
        # 反引号处理
        (r'`([^`]+)`', r'\1'),
        
        # COMMENT 语法
        (r'COMMENT \'([^\']+)\'', r'-- \1'),
        
        # 修复 CREATE TABLE 语句的结尾
        (r'\)\s*\);', ');'),
        (r'\)\s*,\s*\);', ');'),
    ]
    
    for pattern, replacement in replacements:
        content = re.sub(pattern, replacement, content)
    
    # 清理多余的空行和格式
    content = re.sub(r'\n\s*\n\s*\n', '\n\n', content)
    content = re.sub(r'--\s*\n', '--\n', content)
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(content)
    
    print(f"转换完成: {input_file} -> {output_file}")

if __name__ == "__main__":
    convert_file('jeecgboot-pgsql-16.sql', 'jeecgboot-pgsql-16-converted.sql') 