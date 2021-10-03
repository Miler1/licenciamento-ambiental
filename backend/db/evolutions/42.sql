# --- !Ups

CREATE SEQUENCE licenciamento.empreendimento_estatal_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MINVALUE
	NO MAXVALUE
	CACHE 1;
ALTER TABLE licenciamento.empreendimento_estatal_id_seq OWNER TO postgres;

CREATE TABLE licenciamento.empreendimento_estatal (
	id integer DEFAULT nextval('licenciamento.empreendimento_estatal_id_seq'::regclass) NOT NULL,
	cnpj varchar(14) NOT NULL,
	nome varchar(200) NOT NULL,
	isento_taxa_expediente boolean default false,
	isento_taxa_licenciamento boolean default false, 
	CONSTRAINT pk_empreendimento_estatal PRIMARY KEY (id),
	CONSTRAINT un_empreendimento_estatal_cnpj UNIQUE (cnpj)
);
GRANT ALL PRIVILEGES ON SEQUENCE licenciamento.empreendimento_estatal_id_seq TO licenciamento_ap;
GRANT ALL PRIVILEGES ON SEQUENCE licenciamento.empreendimento_estatal_id_seq TO configurador_ap;
ALTER TABLE licenciamento.empreendimento_estatal OWNER TO postgres;
ALTER TABLE licenciamento.empreendimento_estatal OWNER TO configurador_ap;
ALTER TABLE licenciamento.empreendimento_estatal OWNER TO licenciamento_ap;
COMMENT ON TABLE licenciamento.empreendimento_estatal IS 'Entidade responsável por armazenar as empreendimentos estatais isentas.';
COMMENT ON COLUMN licenciamento.empreendimento_estatal.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN licenciamento.empreendimento_estatal.cnpj IS 'CNPJ da empreendimento estatal.';
COMMENT ON COLUMN licenciamento.empreendimento_estatal.nome IS 'Nome da empreendimento estatal.';
COMMENT ON COLUMN licenciamento.empreendimento_estatal.isento_taxa_expediente IS 'Determina se a Taxa de Expediente é isenta.';
COMMENT ON COLUMN licenciamento.empreendimento_estatal.isento_taxa_licenciamento IS 'Determina se a Taxa de Licenciamento é isenta.';


# --- !Downs

DROP TABLE licenciamento.empreendimento_estatal;

DROP SEQUENCE licenciamento.empreendimento_estatal_id_seq;
