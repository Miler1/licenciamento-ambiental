# --- !Ups

ALTER TABLE licenciamento.tipologia ADD COLUMN id_usuario_licenciamento INTEGER;
ALTER TABLE licenciamento.tipologia ADD COLUMN data_cadastro timestamp without time zone;

COMMENT ON COLUMN licenciamento.tipologia.id_usuario_licenciamento IS 'Identificador único da entidade usuario_licenciamento.';
COMMENT ON COLUMN licenciamento.tipologia.data_cadastro IS 'Data de cadastro da tipologia.';

CREATE SEQUENCE licenciamento.tipologia_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

SELECT setval('licenciamento.tipologia_id_seq', COALESCE((SELECT MAX(id) + 1 FROM licenciamento.tipologia), 1));

GRANT ALL PRIVILEGES ON SEQUENCE licenciamento.tipologia_id_seq TO configurador_ap;

ALTER TABLE licenciamento.tipologia ALTER COLUMN id SET DEFAULT nextval('licenciamento.tipologia_id_seq');


# --- !Downs

DROP SEQUENCE licenciamento.tipologia_id_seq;

ALTER TABLE licenciamento.tipologia DROP COLUMN data_cadastro;
ALTER TABLE licenciamento.tipologia DROP COLUMN id_usuario_licenciamento;

