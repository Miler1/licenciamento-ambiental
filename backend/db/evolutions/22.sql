# --- !Ups

ALTER TABLE licenciamento.rel_atividade_pergunta ADD COLUMN id INTEGER;

COMMENT ON COLUMN licenciamento.rel_atividade_pergunta.id IS 'Identificador único da entidade de ralacionamento.';

CREATE SEQUENCE licenciamento.rel_atividade_pergunta_id_seq
    OWNED BY licenciamento.rel_atividade_pergunta.id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE licenciamento.rel_atividade_pergunta DROP CONSTRAINT pk_rel_atividade_pergunta;

UPDATE licenciamento.rel_atividade_pergunta set id = nextval('licenciamento.rel_atividade_pergunta_id_seq');

ALTER TABLE licenciamento.rel_atividade_pergunta ADD CONSTRAINT pk_id_rel_atividade_pergunta PRIMARY KEY (id);

SELECT setval('licenciamento.rel_atividade_pergunta_id_seq', COALESCE((SELECT MAX(id) + 1 FROM licenciamento.rel_atividade_pergunta), 1));

GRANT ALL PRIVILEGES ON SEQUENCE licenciamento.rel_atividade_pergunta_id_seq TO configurador_ap;

ALTER TABLE licenciamento.rel_atividade_pergunta ALTER COLUMN id SET DEFAULT nextval('licenciamento.rel_atividade_pergunta_id_seq');

 
# --- !Downs

DROP SEQUENCE licenciamento.rel_atividade_pergunta_id_seq;

ALTER TABLE licenciamento.rel_atividade_pergunta DROP COLUMN id;
