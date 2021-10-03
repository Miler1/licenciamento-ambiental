# --- !Ups

ALTER TABLE licenciamento.grupo_documento ADD COLUMN id_usuario_licenciamento INTEGER;
ALTER TABLE licenciamento.grupo_documento ADD COLUMN data_cadastro timestamp without time zone;
ALTER TABLE licenciamento.grupo_documento ADD COLUMN ativo BOOLEAN DEFAULT 'true';

COMMENT ON COLUMN licenciamento.grupo_documento.id_usuario_licenciamento IS 'Identificador único da entidade usuario_licenciamento.';
COMMENT ON COLUMN licenciamento.grupo_documento.data_cadastro IS 'Data de cadastro do grupo_documento.';
COMMENT ON COLUMN licenciamento.grupo_documento.ativo IS 'Determina se um item grupo_documento está ativo ou inativo';


# --- !Downs

ALTER TABLE licenciamento.grupo_documento DROP COLUMN ativo;
ALTER TABLE licenciamento.grupo_documento DROP COLUMN data_cadastro;
ALTER TABLE licenciamento.grupo_documento DROP COLUMN id_usuario_licenciamento;
