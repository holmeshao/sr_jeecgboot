-- 行政区划表（省/市/区县）- PostgreSQL
-- 使用步骤：
-- 1) psql -d yourdb -f sys_region_pg_schema.sql
-- 2) 导入生成的数据文件 sys_region_pg_data.sql

DROP TABLE IF EXISTS public.sys_region;
CREATE TABLE public.sys_region (
  id           BIGSERIAL PRIMARY KEY,
  code         CHAR(6)      NOT NULL UNIQUE,
  name         VARCHAR(64)  NOT NULL,
  parent_code  CHAR(6),
  level        SMALLINT     NOT NULL, -- 1省 2市 3区县
  path         VARCHAR(64),
  sort         INTEGER      NOT NULL DEFAULT 0,
  status       SMALLINT     NOT NULL DEFAULT 1
);

CREATE INDEX idx_sys_region_parent ON public.sys_region(parent_code);
CREATE INDEX idx_sys_region_level  ON public.sys_region(level);

COMMENT ON TABLE  public.sys_region IS '行政区划（省/市/区县）';
COMMENT ON COLUMN public.sys_region.code        IS 'GB/T 2260 六位行政区划码，如 110000/110100/110105';
COMMENT ON COLUMN public.sys_region.level       IS '层级：1省 2市 3区县';


