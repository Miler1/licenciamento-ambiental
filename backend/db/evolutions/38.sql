# --- !Ups

ALTER TABLE licenciamento.dae ADD COLUMN nosso_numero varchar(20);
COMMENT ON COLUMN licenciamento.dae.nosso_numero IS 'Número gerado junto ao boleto para ser usado na verificação de boleto pago na SEFAZ.';

# --- !Downs

ALTER TABLE licenciamento.dae DROP COLUMN nosso_numero;
