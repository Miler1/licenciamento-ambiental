# --- !Ups

--Alterando a tabela de perguntas
ALTER TABLE licenciamento.pergunta ADD COLUMN id_usuario_licenciamento INTEGER;
ALTER TABLE licenciamento.pergunta ADD COLUMN data_cadastro timestamp without time zone;
ALTER TABLE licenciamento.pergunta ADD COLUMN ativo boolean DEFAULT TRUE NOT NULL;

COMMENT ON COLUMN licenciamento.pergunta.id_usuario_licenciamento IS 'Identificador único da entidade usuario_licenciamento.';
COMMENT ON COLUMN licenciamento.pergunta.data_cadastro IS 'Data de cadastro da pergunta.';
COMMENT ON COLUMN licenciamento.pergunta.ativo IS 'Flag que indica se a pergunta está ativa no sistema.';

CREATE SEQUENCE licenciamento.pergunta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

SELECT setval('licenciamento.pergunta_id_seq', COALESCE((SELECT MAX(id) + 1 FROM licenciamento.pergunta), 1));

GRANT ALL PRIVILEGES ON SEQUENCE licenciamento.pergunta_id_seq TO configurador_ap;

ALTER TABLE licenciamento.pergunta ALTER COLUMN id SET DEFAULT nextval('licenciamento.pergunta_id_seq');

--Alterando a tabela de respostas
ALTER TABLE licenciamento.resposta ADD COLUMN id_usuario_licenciamento INTEGER;
ALTER TABLE licenciamento.resposta ADD COLUMN data_cadastro timestamp without time zone;

COMMENT ON COLUMN licenciamento.resposta.id_usuario_licenciamento IS 'Identificador único da entidade usuario_licenciamento.';
COMMENT ON COLUMN licenciamento.resposta.data_cadastro IS 'Data de cadastro da resposta.';

CREATE SEQUENCE licenciamento.resposta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

SELECT setval('licenciamento.resposta_id_seq', COALESCE((SELECT MAX(id) + 1 FROM licenciamento.resposta), 1));

GRANT ALL PRIVILEGES ON SEQUENCE licenciamento.resposta_id_seq TO configurador_ap;

ALTER TABLE licenciamento.resposta ALTER COLUMN id SET DEFAULT nextval('licenciamento.resposta_id_seq');


# --- !Downs

DROP SEQUENCE licenciamento.resposta_id_seq;
ALTER TABLE licenciamento.resposta DROP COLUMN id_usuario_licenciamento;
ALTER TABLE licenciamento.resposta DROP COLUMN data_cadastro;

DROP SEQUENCE licenciamento.pergunta_id_seq;
ALTER TABLE licenciamento.resposta DROP COLUMN id_usuario_licenciamento;
ALTER TABLE licenciamento.resposta DROP COLUMN data_cadastro;
ALTER TABLE licenciamento.pergunta DROP COLUMN ativo;



