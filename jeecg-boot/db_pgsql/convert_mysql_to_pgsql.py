#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
MySQL to PostgreSQL SQL Converter
将 MySQL 格式的 SQL 文件转换为 PostgreSQL 格式
"""

import re
import sys
import os

def convert_mysql_to_pgsql(input_file, output_file):
    """转换 MySQL SQL 文件为 PostgreSQL 格式"""
    
    with open(input_file, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # 1. 替换数据库创建语句
    content = re.sub(
        r'CREATE database if NOT EXISTS `([^`]+)` default character set ([^;]+);',
        r'-- CREATE DATABASE \1 WITH ENCODING \'UTF8\' LC_COLLATE=\'en_US.UTF-8\' LC_CTYPE=\'en_US.UTF-8\';',
        content
    )
    
    # 2. 替换 USE 语句
    content = re.sub(r'use `([^`]+)`;', r'-- USE \1;', content)
    
    # 3. 替换 SET 语句
    content = re.sub(r'SET NAMES ([^;]+);', r'SET client_encoding = \'UTF8\';', content)
    content = re.sub(r'SET FOREIGN_KEY_CHECKS = 0;', r'-- SET FOREIGN_KEY_CHECKS = 0;', content)
    content = re.sub(r'SET FOREIGN_KEY_CHECKS = 1;', r'-- SET FOREIGN_KEY_CHECKS = 1;', content)
    
    # 4. 替换注释中的 MySQL 相关信息
    content = re.sub(r'Source Server Type\s*:\s*MySQL', 'Source Server Type    : PostgreSQL', content)
    content = re.sub(r'Target Server Type\s*:\s*MySQL', 'Target Server Type    : PostgreSQL', content)
    content = re.sub(r'Source Server Version\s*:\s*[0-9.]+', 'Source Server Version : 16.0', content)
    content = re.sub(r'Target Server Version\s*:\s*[0-9.]+', 'Target Server Version : 16.0', content)
    content = re.sub(r'Source Host\s*:\s*[^:]+:\d+', 'Source Host           : 127.0.0.1:5432', content)
    
    # 5. 替换数据类型
    # int(n) -> INTEGER
    content = re.sub(r'int\([0-9]+\)', 'INTEGER', content)
    # bigint(n) -> BIGINT
    content = re.sub(r'bigint\([0-9]+\)', 'BIGINT', content)
    # tinyint(n) -> SMALLINT
    content = re.sub(r'tinyint\([0-9]+\)', 'SMALLINT', content)
    # mediumtext -> TEXT
    content = re.sub(r'mediumtext', 'TEXT', content)
    # longtext -> TEXT
    content = re.sub(r'longtext', 'TEXT', content)
    # text CHARACTER SET -> text
    content = re.sub(r'text CHARACTER SET [^ ]+ COLLATE [^ ]+', 'TEXT', content)
    # varchar CHARACTER SET -> varchar
    content = re.sub(r'varchar\([0-9]+\) CHARACTER SET [^ ]+ COLLATE [^ ]+', 'VARCHAR', content)
    
    # 6. 替换 AUTO_INCREMENT
    content = re.sub(r'NOT NULL AUTO_INCREMENT', 'SERIAL', content)
    content = re.sub(r'AUTO_INCREMENT = [0-9]+', '', content)
    
    # 7. 替换 ENGINE 和字符集设置
    content = re.sub(r'ENGINE = InnoDB[^)]*', '', content)
    content = re.sub(r'CHARACTER SET = [^ ]+', '', content)
    content = re.sub(r'COLLATE = [^ ]+', '', content)
    content = re.sub(r'ROW_FORMAT = [^)]*', '', content)
    
    # 8. 替换反引号为双引号或去掉
    content = re.sub(r'`([^`]+)`', r'\1', content)
    
    # 9. 替换 USING BTREE
    content = re.sub(r'USING BTREE', '', content)
    
    # 10. 替换 INSERT 语句中的反引号
    content = re.sub(r'INSERT INTO `([^`]+)`', r'INSERT INTO \1', content)
    
    # 11. 替换 COMMENT 语法
    content = re.sub(r'COMMENT \'([^\']+)\'', r'-- \1', content)
    
    # 12. 替换 UNSIGNED
    content = re.sub(r'UNSIGNED ', '', content)
    
    # 13. 替换 datetime 为 timestamp
    content = re.sub(r'datetime', 'TIMESTAMP', content)
    
    # 14. 替换 DEFAULT 值中的 MySQL 特定语法
    content = re.sub(r'DEFAULT \'([^\']+)\' ON UPDATE CURRENT_TIMESTAMP', r'DEFAULT \1', content)
    
    # 15. 添加序列重置语句
    def add_sequence_reset(match):
        table_name = match.group(1)
        return f"{match.group(0)}\n\n-- Reset sequence\nSELECT setval('{table_name}_id_seq', (SELECT MAX(id) FROM {table_name}) + 1, false);"
    
    # 查找所有 CREATE TABLE 语句并添加序列重置
    table_pattern = r'CREATE TABLE ([a-zA-Z_][a-zA-Z0-9_]*) \([\s\S]*?\);'
    content = re.sub(table_pattern, add_sequence_reset, content)
    
    # 16. 清理多余的空行和格式
    content = re.sub(r'\n\s*\n\s*\n', '\n\n', content)
    
    # 写入输出文件
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(content)
    
    print(f"转换完成: {input_file} -> {output_file}")

def main():
    if len(sys.argv) != 3:
        print("用法: python convert_mysql_to_pgsql.py <input_file> <output_file>")
        sys.exit(1)
    
    input_file = sys.argv[1]
    output_file = sys.argv[2]
    
    if not os.path.exists(input_file):
        print(f"错误: 输入文件 {input_file} 不存在")
        sys.exit(1)
    
    convert_mysql_to_pgsql(input_file, output_file)

if __name__ == "__main__":
    main() 