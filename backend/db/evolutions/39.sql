# --- !Ups

CREATE SEQUENCE licenciamento.documento_arrecadacao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

SELECT setval('licenciamento.documento_arrecadacao_id_seq', COALESCE((SELECT MAX(id_documento_arrecadacao) + 1 FROM licenciamento.documento_arrecadacao), 1));

GRANT ALL PRIVILEGES ON SEQUENCE licenciamento.documento_arrecadacao_id_seq TO licenciamento_ap;

ALTER TABLE licenciamento.documento_arrecadacao ALTER COLUMN id_documento_arrecadacao SET DEFAULT nextval('licenciamento.documento_arrecadacao_id_seq');

ALTER TABLE licenciamento.dae DROP COLUMN nosso_numero;

ALTER TABLE licenciamento.documento_arrecadacao ADD COLUMN nosso_numero varchar(20);
COMMENT ON COLUMN licenciamento.documento_arrecadacao.nosso_numero IS 'Número gerado junto ao boleto para ser usado na verificação de boleto pago na SEFAZ.';

# --- !Downs

ALTER TABLE licenciamento.documento_arrecadacao DROP COLUMN nosso_numero;

ALTER TABLE licenciamento.dae ADD COLUMN nosso_numero varchar(20);
COMMENT ON COLUMN licenciamento.dae.nosso_numero IS 'Número gerado junto ao boleto para ser usado na verificação de boleto pago na SEFAZ.';

DROP SEQUENCE licenciamento.documento_arrecadacao_id_seq;