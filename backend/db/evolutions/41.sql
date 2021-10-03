# --- !Ups

ALTER TABLE licenciamento.documento_arrecadacao_licenciamento ADD nosso_numero VARCHAR(20);
COMMENT ON COLUMN licenciamento.documento_arrecadacao_licenciamento.nosso_numero IS 'Número gerado junto ao boleto para ser usado na verificação de boleto pago na SEFAZ.'

CREATE SEQUENCE licenciamento.documento_arrecadacao_licenciamento_id_seq;
ALTER SEQUENCE licenciamento.documento_arrecadacao_licenciamento_id_seq OWNER TO postgres;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE licenciamento.documento_arrecadacao_licenciamento_id_seq TO configurador_ap;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE licenciamento.documento_arrecadacao_licenciamento_id_seq TO licenciamento_ap;


# --- !Downs

DROP SEQUENCE licenciamento.documento_arrecadacao_licenciamento_id_seq;

ALTER TABLE licenciamento.documento_arrecadacao_licenciamento DROP nosso_numero;
