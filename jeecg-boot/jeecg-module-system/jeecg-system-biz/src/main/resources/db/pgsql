-- PostgreSQL DDL for Workflow Model Repository
-- wf_model
CREATE TABLE IF NOT EXISTS public.wf_model (
  id               varchar(32) PRIMARY KEY,
  model_key        varchar(128) NOT NULL,
  name             varchar(256) NOT NULL,
  category         varchar(128),
  latest_version   integer DEFAULT 0,
  status           varchar(32) DEFAULT 'DRAFT',
  create_by        varchar(64),
  create_time      timestamp(6) without time zone,
  update_by        varchar(64),
  update_time      timestamp(6) without time zone
);

CREATE INDEX IF NOT EXISTS idx_wf_model_key ON public.wf_model (model_key);
CREATE INDEX IF NOT EXISTS idx_wf_model_update_time ON public.wf_model (update_time);

-- wf_model_version
CREATE TABLE IF NOT EXISTS public.wf_model_version (
  id          varchar(32) PRIMARY KEY,
  model_id    varchar(32) NOT NULL REFERENCES public.wf_model(id) ON DELETE CASCADE,
  version     integer NOT NULL,
  xml         text NOT NULL,
  comment     varchar(512),
  create_by   varchar(64),
  create_time timestamp(6) without time zone
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_wf_model_version ON public.wf_model_version (model_id, version);
CREATE INDEX IF NOT EXISTS idx_wf_model_version_time ON public.wf_model_version (create_time);


