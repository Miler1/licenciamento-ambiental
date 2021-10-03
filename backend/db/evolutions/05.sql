# --- !Ups

ALTER TABLE licenciamento.atividade_cnae ADD COLUMN id_usuario_licenciamento INTEGER;
ALTER TABLE licenciamento.atividade_cnae ADD COLUMN data_cadastro timestamp without time zone;

COMMENT ON COLUMN licenciamento.atividade_cnae.id_usuario_licenciamento IS 'Identificador único da entidade usuario_licenciamento.';
COMMENT ON COLUMN licenciamento.atividade_cnae.data_cadastro IS 'Data de cadastro da atividade Cnae.';


# --- !Downs

ALTER TABLE licenciamento.atividade_cnae DROP COLUMN data_cadastro;
ALTER TABLE licenciamento.atividade_cnae DROP COLUMN id_usuario_licenciamento;
