# --- !Ups

CREATE SEQUENCE licenciamento.codigos_taxa_licenciamento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

SELECT setval('licenciamento.codigos_taxa_licenciamento_id_seq', COALESCE((SELECT MAX(id) + 1 FROM licenciamento.codigos_taxa_licenciamento), 1));

GRANT ALL PRIVILEGES ON SEQUENCE licenciamento.codigos_taxa_licenciamento_id_seq TO configurador_ap;

ALTER TABLE licenciamento.codigos_taxa_licenciamento ALTER COLUMN id SET DEFAULT nextval('licenciamento.codigos_taxa_licenciamento_id_seq');


# --- !Downs


DROP SEQUENCE licenciamento.codigos_taxa_licenciamento_id_seq;

