# --- !Ups

ALTER TABLE licenciamento.tipo_caracterizacao_atividade ADD COLUMN id_usuario_licenciamento INTEGER;
ALTER TABLE licenciamento.tipo_caracterizacao_atividade ADD COLUMN data_cadastro timestamp without time zone;
ALTER TABLE licenciamento.tipo_caracterizacao_atividade ADD COLUMN ativo boolean DEFAULT TRUE NOT NULL;

COMMENT ON COLUMN licenciamento.tipo_caracterizacao_atividade.id_usuario_licenciamento IS 'Identificador único da entidade usuario_licenciamento.';
COMMENT ON COLUMN licenciamento.tipo_caracterizacao_atividade.data_cadastro IS 'Data de cadastro do tipo_caracterizacao_atividade.';
COMMENT ON COLUMN licenciamento.tipo_caracterizacao_atividade.ativo IS 'Flag que indica se o tipo_caracterizacao_atividade está ativo no sistema.';

 
# --- !Downs

ALTER TABLE licenciamento.tipo_caracterizacao_atividade DROP COLUMN id_usuario_licenciamento;
ALTER TABLE licenciamento.tipo_caracterizacao_atividade DROP COLUMN data_cadastro;
ALTER TABLE licenciamento.tipo_caracterizacao_atividade DROP COLUMN ativo;
