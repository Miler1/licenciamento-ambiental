# --- !Ups

ALTER TABLE licenciamento.rel_atividade_parametro_atividade ADD COLUMN id INTEGER;
COMMENT ON COLUMN licenciamento.rel_atividade_parametro_atividade.id IS 'Identificador único da entidade de ralacionamento.';

CREATE SEQUENCE licenciamento.rel_atividade_parametro_atividade_id_seq
    OWNED BY licenciamento.rel_atividade_parametro_atividade.id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE licenciamento.rel_atividade_parametro_atividade 
	DROP CONSTRAINT pk_rel_atividade_parametro_atividade;

UPDATE licenciamento.rel_atividade_parametro_atividade set id = nextval('licenciamento.rel_atividade_parametro_atividade_id_seq');

ALTER TABLE licenciamento.rel_atividade_parametro_atividade 
	ADD CONSTRAINT pk_id_rel_atividade_parametro_atividade PRIMARY KEY (id);

SELECT setval('licenciamento.rel_atividade_parametro_atividade_id_seq', COALESCE((SELECT MAX(id) + 1 FROM licenciamento.rel_atividade_parametro_atividade), 1));

GRANT ALL PRIVILEGES ON SEQUENCE licenciamento.rel_atividade_parametro_atividade_id_seq TO configurador_ap;

ALTER TABLE licenciamento.rel_atividade_parametro_atividade ALTER COLUMN id SET DEFAULT nextval('licenciamento.rel_atividade_parametro_atividade_id_seq');


# --- !Downs

ALTER TABLE licenciamento.rel_atividade_parametro_atividade 
	DROP CONSTRAINT pk_id_rel_atividade_parametro_atividade

ALTER TABLE  licenciamento.historico_configurador
    ADD CONSTRAINT pk_rel_atividade_parametro_atividade PRIMARY KEY (id_atividade, id_parametro_atividade);

DROP SEQUENCE licenciamento.rel_atividade_parametro_atividade_id_seq;

ALTER TABLE licenciamento.rel_atividade_parametro_atividade DROP COLUMN id;
