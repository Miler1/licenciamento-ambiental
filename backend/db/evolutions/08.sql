# --- !Ups

ALTER TABLE licenciamento.parametro_atividade ADD COLUMN id_usuario_licenciamento INTEGER;
ALTER TABLE licenciamento.parametro_atividade ADD COLUMN data_cadastro timestamp without time zone;
ALTER TABLE licenciamento.parametro_atividade ADD COLUMN ativo BOOLEAN DEFAULT 'true';

COMMENT ON COLUMN licenciamento.parametro_atividade.id_usuario_licenciamento IS 'Identificador único da entidade usuario_licenciamento.';
COMMENT ON COLUMN licenciamento.parametro_atividade.data_cadastro IS 'Data de cadastro do parâmetro.';
COMMENT ON COLUMN licenciamento.parametro_atividade.ativo IS 'Determina se um item tipo_licenca está ativo ou inativo';

ALTER TABLE licenciamento.tipo_licenca ADD CONSTRAINT uq_sigla_tipo_licenca UNIQUE (sigla);


# --- !Downs

ALTER TABLE licenciamento.tipo_licenca DROP CONSTRAINT uq_sigla_tipo_licenca;

ALTER TABLE licenciamento.parametro_atividade DROP COLUMN ativo;
ALTER TABLE licenciamento.parametro_atividade DROP COLUMN data_cadastro;
ALTER TABLE licenciamento.parametro_atividade DROP COLUMN id_usuario_licenciamento;
