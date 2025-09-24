#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
生成全国省/市/区三级行政区划 INSERT SQL（PostgreSQL 版）

输出：sys_region_pg_data.sql

使用：python3 region_fetch_generate_pgsql.py
"""

import json
import os
import sys
from urllib.request import urlopen

DATA_URL = (
    "https://raw.githubusercontent.com/modood/Administrative-divisions-of-China/master/dist/pcas-code.json"
)
OUTPUT = "sys_region_pg_data.sql"


def fetch():
    try:
        with urlopen(DATA_URL, timeout=30) as r:
            return json.load(r)
    except Exception as e:
        print("下载失败：", e)
        sys.exit(2)


def parse(tree):
    rows = []

    def add(code, name, parent, level, path, sort):
        rows.append({
            "code": code[:6],
            "name": name,
            "parent": parent,
            "level": level,
            "path": path,
            "sort": sort,
        })

    s1 = 0
    for p in tree:
        s1 += 10
        pcode = p["code"]
        pname = p["name"]
        add(pcode, pname, None, 1, pcode, s1)

        s2 = 0
        for c in p.get("children", []):
            s2 += 10
            ccode = c["code"]
            cname = c["name"]
            add(ccode, cname, pcode, 2, f"{pcode}/{ccode}", s2)

            s3 = 0
            for a in c.get("children", []):
                s3 += 10
                acode = a["code"]
                aname = a["name"]
                add(acode, aname, ccode, 3, f"{pcode}/{ccode}/{acode}", s3)

    return rows


def to_sql(rows):
    head = (
        "-- 生成自 region_fetch_generate_pgsql.py\n"
        "SET client_encoding TO 'UTF8';\n\n"
    )
    parts = [head]
    batch = 500
    for i in range(0, len(rows), batch):
        vs = []
        for r in rows[i:i+batch]:
            name = r["name"].replace("'", "''")
            parent = f"'{r['parent'][:6]}'" if r["parent"] else "NULL"
            path = f"'{r['path']}'" if r["path"] else "NULL"
            vs.append(
                f"('{r['code'][:6]}','{name}',{parent},{r['level']},{path},{r['sort']},1)"
            )
        parts.append(
            "INSERT INTO public.sys_region(code,name,parent_code,level,path,sort,status) VALUES\n"
            + ",\n".join(vs)
            + ";\n\n"
        )
    return "".join(parts)


def main():
    data = fetch()
    rows = parse(data)
    sql = to_sql(rows)
    out = os.path.join(os.path.dirname(__file__), OUTPUT)
    with open(out, "w", encoding="utf-8") as f:
        f.write(sql)
    print(f"生成完成：{out}，共 {len(rows)} 行")


if __name__ == "__main__":
    main()


