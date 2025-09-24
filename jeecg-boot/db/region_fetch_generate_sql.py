#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
生成全国省/市/区三级行政区划 INSERT SQL

输出文件：sys_region_data.sql（与本脚本同目录）

数据来源：
- 首选 民政部/国家统计局 行政区划公开数据；
- 为避免在线依赖，本脚本默认使用稳定的社区整理 JSON（结构包含 code/name/parentCode/level）。

使用：
  python3 region_fetch_generate_sql.py

说明：
- 仅生成三级（省/市/区县），不含乡镇/街道。
- 如需替换数据源，将 DATA_URL 替换为内网/自有 JSON 地址，字段映射按 parse_records 调整。
"""

import json
import os
import sys
from urllib.request import urlopen


# 备选公开数据（含 code/name/parentCode/level）
# 若访问受限，可将 JSON 文件下载到本目录，改用 file:// 路径
DATA_URL = (
    "https://raw.githubusercontent.com/modood/Administrative-divisions-of-China/master/dist/pcas-code.json"
)

OUTPUT = "sys_region_data.sql"


def fetch_json() -> list:
    try:
        with urlopen(DATA_URL, timeout=30) as resp:
            return json.load(resp)
    except Exception as e:
        print("[error] 下载数据失败：", e)
        print("请手动下载 JSON 后，将 DATA_URL 改为本地 file:// 路径再试。")
        sys.exit(2)


def parse_records(pcas_tree: list) -> list:
    """
    pcas-code.json 结构：[{ code, name, children: [{ code, name, children: [...] }] }]
    这里展开为省(1)/市(2)/区县(3)
    """
    rows = []

    def push(code: str, name: str, parent: str, level: int, path: str, sort: int):
        rows.append(
            {
                "code": code,
                "name": name,
                "parent_code": parent or None,
                "level": level,
                "path": path,
                "sort": sort,
            }
        )

    sort_prov = 0
    for prov in pcas_tree:
        sort_prov += 10
        p_code = prov["code"][:6]
        p_name = prov["name"]
        push(p_code, p_name, None, 1, p_code, sort_prov)

        sort_city = 0
        for city in prov.get("children", []):
            sort_city += 10
            c_code = city["code"][:6]
            c_name = city["name"]
            push(c_code, c_name, p_code, 2, f"{p_code}/{c_code}", sort_city)

            sort_area = 0
            for area in city.get("children", []):
                sort_area += 10
                a_code = area["code"][:6]
                a_name = area["name"]
                push(a_code, a_name, c_code, 3, f"{p_code}/{c_code}/{a_code}", sort_area)

    return rows


def to_insert_sql(rows: list) -> str:
    values = []
    for r in rows:
        code = r["code"]
        name = r["name"].replace("'", "''")
        parent = r["parent_code"]
        level = r["level"]
        path = r["path"]
        sort = r["sort"]
        parent_sql = f"'{parent}'" if parent else "NULL"
        path_sql = f"'{path}'" if path else "NULL"
        values.append(
            f"('{code}','{name}',{parent_sql},{level},{path_sql},{sort},1)"
        )

    # 分批插入，避免过长 SQL
    sql_parts = [
        "-- 生成自 region_fetch_generate_sql.py\n",
        "SET NAMES utf8mb4;\n",
        "SET FOREIGN_KEY_CHECKS=0;\n\n",
    ]

    batch = 500
    for i in range(0, len(values), batch):
        chunk = ",\n".join(values[i : i + batch])
        sql_parts.append(
            "INSERT INTO `sys_region`(`code`,`name`,`parent_code`,`level`,`path`,`sort`,`status`)\nVALUES\n"
            + chunk
            + ";\n\n"
        )

    sql_parts.append("SET FOREIGN_KEY_CHECKS=1;\n")
    return "".join(sql_parts)


def main():
    data = fetch_json()
    rows = parse_records(data)
    sql = to_insert_sql(rows)
    out_path = os.path.join(os.path.dirname(__file__), OUTPUT)
    with open(out_path, "w", encoding="utf-8") as f:
        f.write(sql)
    print(f"生成完成：{out_path}，共 {len(rows)} 行")


if __name__ == "__main__":
    main()


