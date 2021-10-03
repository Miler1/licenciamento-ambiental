# --- !Ups

ALTER TABLE licenciamento.atividade DROP COLUMN data_cadastro;
ALTER TABLE licenciamento.atividade DROP COLUMN id_usuario_licenciamento;


# --- !Downs

ALTER TABLE licenciamento.atividade ADD COLUMN data_cadastro;
COMMENT ON COLUMN licenciamento.atividade.data_cadastro IS 'Data de cadastro da atividade.';

ALTER TABLE licenciamento.atividade ADD COLUMN id_usuario_licenciamento;
COMMENT ON COLUMN licenciamento.atividade.id_usuario_licenciamento IS 'Identificador único da entidade usuario_licenciamento.';
