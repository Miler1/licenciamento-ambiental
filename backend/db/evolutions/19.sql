# --- !Ups

ALTER TABLE licenciamento.taxa_administrativa_dae ADD COLUMN isento BOOLEAN DEFAULT 'false';

COMMENT ON COLUMN licenciamento.taxa_administrativa_dae.isento IS 'Determina se no ano a taxa administrativa será isenta de cobrança';

 
# --- !Downs

ALTER TABLE licenciamento.taxa_administrativa_dae DROP COLUMN isento;
