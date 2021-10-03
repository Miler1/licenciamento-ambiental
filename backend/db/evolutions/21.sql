# --- !Ups

-- TABELA HISTORICO_CONFIGURADOR
CREATE SEQUENCE licenciamento.historico_configurador_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE licenciamento.historico_configurador_id_seq OWNER TO postgres;

CREATE TABLE licenciamento.historico_configurador (
    id integer DEFAULT nextval('licenciamento.historico_configurador_id_seq'::regclass) NOT NULL,
    id_funcionalidade_configurador integer NOT NULL,
    id_item integer NOT NULL,
    id_acao_configurador integer NOT NULL,
    justificativa text,
    id_usuario_licenciamento integer,
    data_cadastro timestamp without time zone NOT NULL
);
GRANT ALL PRIVILEGES ON SEQUENCE licenciamento.historico_configurador_id_seq TO configurador_ap;
ALTER TABLE licenciamento.historico_configurador OWNER TO postgres;
ALTER TABLE licenciamento.historico_configurador OWNER TO configurador_ap;
COMMENT ON TABLE licenciamento.historico_configurador IS 'Entidade responsável por armazenar o histórico de operações executadas no configurador do licenciamento.';
COMMENT ON COLUMN licenciamento.historico_configurador.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN licenciamento.historico_configurador.id_funcionalidade_configurador IS 'Identificador da entidade funcionalidade_configurador';
COMMENT ON COLUMN licenciamento.historico_configurador.id_item IS 'Identificador da item que sofreu a ação.';
COMMENT ON COLUMN licenciamento.historico_configurador.id_acao_configurador IS 'Identificador da entidade acao_configurador';
COMMENT ON COLUMN licenciamento.historico_configurador.justificativa IS 'Descrição que justifica, só e somente só, a ação de editar um item';
COMMENT ON COLUMN licenciamento.historico_configurador.id_usuario_licenciamento IS 'Id do usuário que excutou alguma ação de uma funcionalidade no configurador';
COMMENT ON COLUMN licenciamento.historico_configurador.data_cadastro IS 'Data de cadastro das ações realizadas no histórico.';

-- TABELA FUNCINALIDADE --
CREATE SEQUENCE licenciamento.funcionalidade_configurador_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE licenciamento.funcionalidade_configurador_id_seq OWNER TO postgres;

CREATE TABLE licenciamento.funcionalidade_configurador (
    id integer DEFAULT nextval('licenciamento.funcionalidade_configurador_id_seq'::regclass) NOT NULL,
    tipo character varying(50) UNIQUE NOT NULL
);
GRANT ALL PRIVILEGES ON SEQUENCE licenciamento.funcionalidade_configurador_id_seq TO configurador_ap;
ALTER TABLE licenciamento.funcionalidade_configurador OWNER TO postgres;
ALTER TABLE licenciamento.funcionalidade_configurador OWNER TO configurador_ap;
COMMENT ON TABLE licenciamento.funcionalidade_configurador IS 'Entidade responsável por armazenar as funcionalidades disponíveis no configurador.';
COMMENT ON COLUMN licenciamento.funcionalidade_configurador.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN licenciamento.funcionalidade_configurador.tipo IS 'Descreve o tipo da funcionalidade do configurador. Está associado a uma entidade responsável por gerar o histórico (cnae, licenca, taxa, parâmetro, etc).';

-- TABELA AÇÃO CONFIGURADOR --
CREATE SEQUENCE licenciamento.acao_configurador_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE licenciamento.acao_configurador_id_seq OWNER TO postgres;

CREATE TABLE licenciamento.acao_configurador (
    id integer DEFAULT nextval('licenciamento.acao_configurador_id_seq'::regclass) NOT NULL,
    acao character varying(50) UNIQUE NOT NULL
);
GRANT ALL PRIVILEGES ON SEQUENCE licenciamento.acao_configurador_id_seq TO configurador_ap;
ALTER TABLE licenciamento.acao_configurador OWNER TO postgres;
ALTER TABLE licenciamento.acao_configurador OWNER TO configurador_ap;
COMMENT ON TABLE licenciamento.acao_configurador IS 'Entidade responsável por armazenar as ações do configurador. Será utilizada para mapear operações executadas no sistema.';
COMMENT ON COLUMN licenciamento.acao_configurador.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN licenciamento.acao_configurador.acao IS 'Descrição do tipo de ação executada no configurador.';



-- INSERT FUNCIONALIDADES DO CONFIGURADOR --
INSERT INTO licenciamento.funcionalidade_configurador VALUES (1, 'CNAE');
INSERT INTO licenciamento.funcionalidade_configurador VALUES (2, 'LICENCA');
INSERT INTO licenciamento.funcionalidade_configurador VALUES (3, 'TIPOLOGIA');
INSERT INTO licenciamento.funcionalidade_configurador VALUES (4, 'PARAMETRO');
INSERT INTO licenciamento.funcionalidade_configurador VALUES (5, 'TAXA_ADMINISTRATIVA');
INSERT INTO licenciamento.funcionalidade_configurador VALUES (6, 'TAXA_LICENCIAMENTO');
INSERT INTO licenciamento.funcionalidade_configurador VALUES (7, 'REQUISITO_ADMINISTRATIVO');
INSERT INTO licenciamento.funcionalidade_configurador VALUES (8, 'REQUISITO_TECNICO');
INSERT INTO licenciamento.funcionalidade_configurador VALUES (9, 'ATIVIDADES_LICENCIAVEIS');
INSERT INTO licenciamento.funcionalidade_configurador VALUES (10, 'PERGUNTAS');
INSERT INTO licenciamento.funcionalidade_configurador VALUES (11, 'CNAES_DIPONSAVEIS');

-- INSERT AÇÕES DO CONFIGURADOR --
INSERT INTO licenciamento.acao_configurador VALUES (1, 'CADASTRAR');
INSERT INTO licenciamento.acao_configurador VALUES (2, 'EDITAR');
INSERT INTO licenciamento.acao_configurador VALUES (3, 'ATIVAR');
INSERT INTO licenciamento.acao_configurador VALUES (4, 'DESATIVAR');

-- deletando
ALTER TABLE licenciamento.codigos_taxa_licenciamento DROP COLUMN id_usuario_licenciamento;
ALTER TABLE licenciamento.codigos_taxa_licenciamento DROP COLUMN data_cadastro;
 
# --- !Downs

DELETE FROM licenciamento.acao_configurador WHERE id IN (1,2,3,4);

DELETE FROM licenciamento.funcionalidade_configurador WHERE id IN (1,2,3,4,5,6,7,8,9,10,11);

DROP TABLE licenciamento.acao_configurador;

DROP SEQUENCE licenciamento.acao_configurador_id_seq;

DROP TABLE licenciamento.funcionalidade_configurador;

DROP SEQUENCE licenciamento.funcionalidade_configurador_id_seq;

DROP TABLE licenciamento.historico_configurador ;

DROP SEQUENCE licenciamento.historico_configurador_id_seq;