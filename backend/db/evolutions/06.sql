# --- !Ups

ALTER TABLE licenciamento.tipo_licenca ADD COLUMN id_usuario_licenciamento INTEGER;
ALTER TABLE licenciamento.tipo_licenca ADD COLUMN data_cadastro timestamp without time zone;

COMMENT ON COLUMN licenciamento.tipo_licenca.id_usuario_licenciamento IS 'Identificador único da entidade usuario_licenciamento.';
COMMENT ON COLUMN licenciamento.tipo_licenca.data_cadastro IS 'Data de cadastro da licença.';

CREATE SEQUENCE licenciamento.tipo_licenca_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

SELECT setval('licenciamento.tipo_licenca_id_seq', COALESCE((SELECT MAX(id) + 1 FROM licenciamento.tipo_licenca), 1));

GRANT ALL PRIVILEGES ON SEQUENCE licenciamento.tipo_licenca_id_seq TO configurador_ap;

ALTER TABLE licenciamento.tipo_licenca ALTER COLUMN id SET DEFAULT nextval('licenciamento.tipo_licenca_id_seq');

ALTER TABLE licenciamento.tipo_licenca ADD COLUMN ativo BOOLEAN DEFAULT 'true';
COMMENT ON COLUMN licenciamento.tipo_licenca.ativo IS 'Determina se um item tipo_licenca está ativo ou inativo para ser utilizado';

# --- !Downs

ALTER TABLE licenciamento.tipo_licenca DROP COLUMN ativo;

ALTER TABLE licenciamento.tipo_licenca DROP COLUMN data_cadastro;
ALTER TABLE licenciamento.tipo_licenca DROP COLUMN id_usuario_licenciamento;

DROP SEQUENCE licenciamento.tipo_licenca_id_seq;


