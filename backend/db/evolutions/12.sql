# --- !Ups

ALTER TABLE licenciamento.rel_tipo_licenca_grupo_documento ADD COLUMN id INTEGER;

COMMENT ON COLUMN licenciamento.grupo_documento.id IS 'Identificador único da entidade de ralacionamento.';

CREATE SEQUENCE licenciamento.rel_tipo_licenca_grupo_documento_id_seq
OWNED BY licenciamento.rel_tipo_licenca_grupo_documento.id
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER TABLE licenciamento.rel_tipo_licenca_grupo_documento DROP CONSTRAINT pk_rel_tipo_licenca_grupo_documento;

UPDATE licenciamento.rel_tipo_licenca_grupo_documento set id = nextval('licenciamento.rel_tipo_licenca_grupo_documento_id_seq');

ALTER TABLE licenciamento.rel_tipo_licenca_grupo_documento ADD CONSTRAINT pk_id_tipo_licenca_grupo_documento PRIMARY KEY (id);

SELECT setval('licenciamento.rel_tipo_licenca_grupo_documento_id_seq', COALESCE((SELECT MAX(id) + 1 FROM licenciamento.rel_tipo_licenca_grupo_documento), 1));

GRANT ALL PRIVILEGES ON SEQUENCE licenciamento.rel_tipo_licenca_grupo_documento_id_seq TO configurador_ap;

ALTER TABLE licenciamento.rel_tipo_licenca_grupo_documento ALTER COLUMN id SET DEFAULT nextval('licenciamento.rel_tipo_licenca_grupo_documento_id_seq');

# --- !Downs

ALTER TABLE licenciamento.rel_tipo_licenca_grupo_documento DROP CONSTRAINT pk_id_tipo_licenca_grupo_documento;
ALTER TABLE licenciamento.rel_tipo_licenca_grupo_documento ADD CONSTRAINT pk_rel_tipo_licenca_grupo_documento;
ALTER TABLE licenciamento.rel_tipo_licenca_grupo_documento DROP SEQUENCE licenciamento.rel_tipo_licenca_grupo_documento_id_seq;
ALTER TABLE licenciamento.rel_tipo_licenca_grupo_documento DROP COLUMN id;
