# --- !Ups

ALTER TABLE licenciamento.tipo_caracterizacao_atividade DROP COLUMN data_cadastro;
ALTER TABLE licenciamento.tipo_caracterizacao_atividade DROP COLUMN id_usuario_licenciamento;


# --- !Downs

ALTER TABLE licenciamento.tipo_caracterizacao_atividade ADD COLUMN data_cadastro;
COMMENT ON COLUMN licenciamento.tipo_caracterizacao_atividade.data_cadastro IS 'Data de cadastro da atividade.';

ALTER TABLE licenciamento.tipo_caracterizacao_atividade ADD COLUMN id_usuario_licenciamento;
COMMENT ON COLUMN licenciamento.tipo_caracterizacao_atividade.id_usuario_licenciamento IS 'Identificador único da entidade usuario_licenciamento.';
