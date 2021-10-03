# --- !Ups

ALTER TABLE licenciamento.tipo_documento_tipo_licenca ADD COLUMN id_usuario_licenciamento INTEGER;
ALTER TABLE licenciamento.tipo_documento_tipo_licenca ADD COLUMN data_cadastro timestamp without time zone;
ALTER TABLE licenciamento.tipo_documento_tipo_licenca ADD COLUMN ativo BOOLEAN DEFAULT 'true';

COMMENT ON COLUMN licenciamento.tipo_documento_tipo_licenca.id_usuario_licenciamento IS 'Identificador único da entidade usuario_licenciamento.';
COMMENT ON COLUMN licenciamento.tipo_documento_tipo_licenca.data_cadastro IS 'Data de cadastro do grupo_documento.';
COMMENT ON COLUMN licenciamento.tipo_documento_tipo_licenca.ativo IS 'Determina se um item grupo_documento está ativo ou inativo';

# --- !Downs

ALTER TABLE licenciamento.tipo_documento_tipo_licenca DROP COLUMN ativo;
ALTER TABLE licenciamento.tipo_documento_tipo_licenca DROP COLUMN data_cadastro;
ALTER TABLE licenciamento.tipo_documento_tipo_licenca DROP COLUMN id_usuario_licenciamento;
