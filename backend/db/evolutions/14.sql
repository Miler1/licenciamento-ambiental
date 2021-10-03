# --- !Ups

ALTER TABLE licenciamento.taxa_administrativa_dae ADD COLUMN atividade_dispensavel BOOLEAN DEFAULT 'true';
ALTER TABLE licenciamento.taxa_administrativa_dae ADD COLUMN atividade_licenciavel BOOLEAN DEFAULT 'true';
ALTER TABLE licenciamento.taxa_administrativa_dae ADD COLUMN id_usuario_licenciamento INTEGER;
ALTER TABLE licenciamento.taxa_administrativa_dae ADD COLUMN data_cadastro timestamp without time zone;
ALTER TABLE licenciamento.taxa_administrativa_dae ADD COLUMN ativo BOOLEAN DEFAULT 'true';

ALTER TABLE licenciamento.taxa_administrativa_dae ADD CONSTRAINT uq_ano_taxa_administrativa UNIQUE (ano);

COMMENT ON COLUMN licenciamento.taxa_administrativa_dae.atividade_dispensavel IS 'Determina se a opção para cobrança é dispensável';
COMMENT ON COLUMN licenciamento.taxa_administrativa_dae.atividade_licenciavel IS 'Determina se a opção para cobrança é licenciável';
COMMENT ON COLUMN licenciamento.taxa_administrativa_dae.id_usuario_licenciamento IS 'Identificador único da entidade usuário_licenciamento.'; COMMENT ON COLUMN licenciamento.taxa_administrativa_dae.data_cadastro IS 'Data de cadastro da taxa administrativa.';
COMMENT ON COLUMN licenciamento.taxa_administrativa_dae.ativo IS 'Determina se um item taxa_administrativa está ativo ou inativo';

# --- !Downs

ALTER TABLE licenciamento.taxa_administrativa_dae DROP CONSTRAINT uq_ano_taxa_administrativa;
ALTER TABLE licenciamento.taxa_administrativa_dae DROP COLUMN ativo;
ALTER TABLE licenciamento.taxa_administrativa_dae DROP COLUMN data_cadastro;
ALTER TABLE licenciamento.taxa_administrativa_dae DROP COLUMN id_usuario_licenciamento;
ALTER TABLE licenciamento.taxa_administrativa_dae DROP COLUMN atividade_licenciavel;
ALTER TABLE licenciamento.taxa_administrativa_dae DROP COLUMN atividade_dispensavel;


