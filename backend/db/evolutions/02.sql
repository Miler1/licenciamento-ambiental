# --- !Ups

ALTER TABLE licenciamento.caracterizacao DROP CONSTRAINT fk_c_pessoa ;

ALTER TABLE licenciamento.empreendimento ADD COLUMN cpf_cnpj TEXT;
COMMENT ON COLUMN licenciamento.empreendimento.cpf_cnpj IS 'CPF ou CNPJ da pessoa que representa o empreendimento';


UPDATE licenciamento.empreendimento SET cpf_cnpj = emps.cpf_cnpj
FROM (
    SELECT e.id, coalesce(pj.cnpj, pf.cpf) as cpf_cnpj FROM licenciamento.empreendimento e
    INNER JOIN licenciamento.pessoa p ON e.id_pessoa = p.id
    LEFT JOIN licenciamento.pessoa_juridica pj ON p.id = pj.id_pessoa
    LEFT JOIN licenciamento.pessoa_fisica pf ON p.id = pf.id_pessoa
) emps
WHERE empreendimento.id = emps.id;


ALTER TABLE licenciamento.empreendimento ADD COLUMN cpf_cnpj_cadastrante TEXT;
COMMENT ON COLUMN licenciamento.empreendimento.cpf_cnpj_cadastrante IS 'CPF ou CNPJ da pessoa cadastrante da caracterização referenciado no Entrada Única';


UPDATE licenciamento.empreendimento SET cpf_cnpj_cadastrante = emps.cpf_cnpj
FROM (
    SELECT e.id, coalesce(pj.cnpj, pf.cpf) as cpf_cnpj FROM licenciamento.empreendimento e
    INNER JOIN licenciamento.pessoa p ON e.id_cadastrante = p.id
    LEFT JOIN licenciamento.pessoa_juridica pj ON p.id = pj.id_pessoa
    LEFT JOIN licenciamento.pessoa_fisica pf ON p.id = pf.id_pessoa
) emps
WHERE empreendimento.id = emps.id;


ALTER TABLE licenciamento.empreendimento
    ALTER COLUMN cpf_cnpj SET NOT NULL,
    ALTER COLUMN cpf_cnpj_cadastrante SET NOT NULL;


ALTER TABLE licenciamento.caracterizacao ADD COLUMN cpf_cnpj_cadastrante TEXT;
COMMENT ON COLUMN licenciamento.caracterizacao.cpf_cnpj_cadastrante IS 'CPF ou CNPJ da pessoa cadastrante da caracterização referenciado no Entrada Única';


UPDATE licenciamento.caracterizacao SET cpf_cnpj_cadastrante = carac.cpf_cnpj
FROM (
    SELECT c.id, coalesce(pj.cnpj, pf.cpf) cpf_cnpj FROM licenciamento.caracterizacao c
    INNER JOIN licenciamento.pessoa p ON c.id_cadastrante = p.id
    LEFT JOIN licenciamento.pessoa_juridica pj ON p.id = pj.id_pessoa
    LEFT JOIN licenciamento.pessoa_fisica pf ON p.id = pf.id_pessoa
) as carac
WHERE caracterizacao.id = carac.id;


ALTER TABLE licenciamento.caracterizacao ALTER COLUMN cpf_cnpj_cadastrante SET NOT NULL;


ALTER TABLE licenciamento.empreendimento
    DROP COLUMN id_pessoa,
    DROP COLUMN id_contato,
    DROP COLUMN id_empreendedor,
    DROP COLUMN id_endereco,
    DROP COLUMN id_redesim,
    DROP COLUMN id_cadastrante,
    DROP COLUMN the_geom,
    DROP COLUMN houve_alteracoes;

DROP TABLE licenciamento.historico_alteracao_empreendimento_empreendedor;
DROP TABLE licenciamento.historico_alteracao_empreendimento_pessoa;
DROP TABLE licenciamento.representante_legal;
DROP TABLE licenciamento.empreendedor;
DROP TABLE licenciamento.endereco_empreendimento;
DROP TABLE licenciamento.endereco_pessoa;
DROP TABLE licenciamento.documento_responsavel_empreendimento;
DROP TABLE licenciamento.responsavel_empreendimento;
DROP TABLE licenciamento.endereco;
DROP TABLE licenciamento.pessoa_fisica;
DROP TABLE licenciamento.pessoa_juridica;
DROP TABLE licenciamento.proprietario;
DROP TABLE licenciamento.pessoa;
DROP TABLE licenciamento.contato;

# --- !Downs


CREATE TABLE licenciamento.contato
(
  id serial NOT NULL, -- Identificado único da entidade.
  email character varying(150), -- Email de contato.
  telefone character varying(11), -- Telefone de contato.
  celular character varying(11), -- Celular de contato.
  CONSTRAINT pk_contato PRIMARY KEY (id)
);
ALTER TABLE licenciamento.contato
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.contato TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.contato TO licenciamento_ap;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.contato TO portal_seguranca_licenciamento_ap;
GRANT SELECT ON TABLE licenciamento.contato TO tramitacao;
COMMENT ON TABLE licenciamento.contato
  IS 'Entidade responsavel por armazenar as informações referentes aos contatos.';
COMMENT ON COLUMN licenciamento.contato.id IS 'Identificado único da entidade.';
COMMENT ON COLUMN licenciamento.contato.email IS 'Email de contato.';
COMMENT ON COLUMN licenciamento.contato.telefone IS 'Telefone de contato.';
COMMENT ON COLUMN licenciamento.contato.celular IS 'Celular de contato.';


CREATE TABLE licenciamento.pessoa
(
  id serial NOT NULL, -- Identificado único da entidade.
  id_contato integer, -- Identificador único da entidade contato que realizará o relacionamento entre contato e pessoa.
  ativo boolean NOT NULL, -- Indica se a pessoa está ativa.
  data_cadastro timestamp without time zone NOT NULL, -- Data de Cadastro.
  CONSTRAINT pk_pessoa PRIMARY KEY (id),
  CONSTRAINT fk_e_municipio FOREIGN KEY (id_contato)
      REFERENCES licenciamento.contato (id) 
);
ALTER TABLE licenciamento.pessoa
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.pessoa TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.pessoa TO licenciamento_ap;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.pessoa TO portal_seguranca_licenciamento_ap;
GRANT SELECT ON TABLE licenciamento.pessoa TO tramitacao;
COMMENT ON TABLE licenciamento.pessoa
  IS 'Entidade responsavel por armazenar as informações referentes as pessoas.';
COMMENT ON COLUMN licenciamento.pessoa.id IS 'Identificado único da entidade.';
COMMENT ON COLUMN licenciamento.pessoa.id_contato IS 'Identificador único da entidade contato que realizará o relacionamento entre contato e pessoa.';
COMMENT ON COLUMN licenciamento.pessoa.ativo IS 'Indica se a pessoa está ativa.';
COMMENT ON COLUMN licenciamento.pessoa.data_cadastro IS 'Data de Cadastro.';

CREATE TABLE licenciamento.proprietario
(
  id serial NOT NULL, -- Identificador único da entidade.
  id_pessoa integer, -- Identificador da entidade pessoa que realizará o relacionamento entre as entidades proprietario e pessoa.
  id_empreendimento integer, -- Identificador da entidade empreendimento que realizará o relacionamento entre as entidades proprietario e empreendimento.
  CONSTRAINT pk_proprietario PRIMARY KEY (id),
  CONSTRAINT fk_p_empreendimento FOREIGN KEY (id_empreendimento)
      REFERENCES licenciamento.empreendimento (id),
  CONSTRAINT fk_p_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES licenciamento.pessoa (id) 
);
ALTER TABLE licenciamento.proprietario
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.proprietario TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.proprietario TO licenciamento_ap;
GRANT SELECT ON TABLE licenciamento.proprietario TO tramitacao;
COMMENT ON TABLE licenciamento.proprietario
  IS 'Entidade responsável por armazenar os proprietários dos empreendimentos.';
COMMENT ON COLUMN licenciamento.proprietario.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN licenciamento.proprietario.id_pessoa IS 'Identificador da entidade pessoa que realizará o relacionamento entre as entidades proprietario e pessoa.';
COMMENT ON COLUMN licenciamento.proprietario.id_empreendimento IS 'Identificador da entidade empreendimento que realizará o relacionamento entre as entidades proprietario e empreendimento.';

CREATE TABLE licenciamento.pessoa_juridica
(
  id_pessoa integer NOT NULL, -- Identificado único da entidade. É também o identificador único da entidade pessoa que realizará o relacionamento entre pessoa_juridica e pessoa.
  razao_social character varying(200) NOT NULL, -- Razão Social.
  nome_fantasia character varying(200), -- Nome Fantasia.
  id_municipio integer NOT NULL, -- Identificador único da entidade municipio que realizará o relacionamento entre municipio e pessoa_juridica.
  data_fundacao timestamp without time zone NOT NULL, -- Data de Fundação.
  inscricao_estadual character varying(20), -- Inscrição estadual.
  cnpj character varying(14) NOT NULL, -- CNPJ.
  CONSTRAINT pk_pessoa_juridica PRIMARY KEY (id_pessoa),
  CONSTRAINT fk_pj_municipio FOREIGN KEY (id_municipio)
      REFERENCES licenciamento.municipio (id_municipio),
  CONSTRAINT fk_pj_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES licenciamento.pessoa (id),
  CONSTRAINT un_pessoa_juridica_cnpj UNIQUE (cnpj)
);
ALTER TABLE licenciamento.pessoa_juridica
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.pessoa_juridica TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.pessoa_juridica TO licenciamento_ap;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.pessoa_juridica TO portal_seguranca_licenciamento_ap;
GRANT SELECT ON TABLE licenciamento.pessoa_juridica TO tramitacao;
COMMENT ON TABLE licenciamento.pessoa_juridica
  IS 'Entidade responsavel por armazenar as informações referentes as pessoas jurídicas.';
COMMENT ON COLUMN licenciamento.pessoa_juridica.id_pessoa IS 'Identificado único da entidade. É também o identificador único da entidade pessoa que realizará o relacionamento entre pessoa_juridica e pessoa.';
COMMENT ON COLUMN licenciamento.pessoa_juridica.razao_social IS 'Razão Social.';
COMMENT ON COLUMN licenciamento.pessoa_juridica.nome_fantasia IS 'Nome Fantasia.';
COMMENT ON COLUMN licenciamento.pessoa_juridica.id_municipio IS 'Identificador único da entidade municipio que realizará o relacionamento entre municipio e pessoa_juridica.';
COMMENT ON COLUMN licenciamento.pessoa_juridica.data_fundacao IS 'Data de Fundação.';
COMMENT ON COLUMN licenciamento.pessoa_juridica.inscricao_estadual IS 'Inscrição estadual.';
COMMENT ON COLUMN licenciamento.pessoa_juridica.cnpj IS 'CNPJ.';


CREATE TABLE licenciamento.pessoa_fisica
(
  id_pessoa integer NOT NULL, -- Identificado único da entidade. É também o identificador único da entidade pessoa que realizará o relacionamento entre pessoa_fisica e pessoa.
  nome character varying(200) NOT NULL, -- Nome da pessoa.
  cpf character varying(11) NOT NULL, -- CPF da pessoa.
  sexo character(1) NOT NULL, -- Sexo da pessoa.
  data_nascimento timestamp without time zone NOT NULL, -- Data de Nascimento da pessoa.
  nome_mae character varying(200) NOT NULL, -- Nome da mãe da pessoa.
  rg character varying(15), -- RG da pessoa.
  titulo_eleitor character varying(20), -- Título de eleitor da pessoa.
  id_estado_civil integer, -- Identificador único da entidade estado_civil que realizará o relacionamento entre estado_civil e pessoa_fisica.
  id_municipio_nascimento integer, -- Identificador único da entidade municipio que realizará o relacionamento entre municipio e pessoa_fisica. É o município de nascimento da pessoa.
  naturalidade character varying(50), -- Naturalidade da pessoa.
  CONSTRAINT pk_pessoa_fisica PRIMARY KEY (id_pessoa),
  CONSTRAINT fk_pf_municipio_nascimento FOREIGN KEY (id_municipio_nascimento)
      REFERENCES licenciamento.municipio (id_municipio),
  CONSTRAINT fk_pf_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES licenciamento.pessoa (id),
  CONSTRAINT un_pessoa_fisica_cpf UNIQUE (cpf)
);
ALTER TABLE licenciamento.pessoa_fisica
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.pessoa_fisica TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.pessoa_fisica TO licenciamento_ap;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.pessoa_fisica TO portal_seguranca_licenciamento_ap;
GRANT SELECT ON TABLE licenciamento.pessoa_fisica TO tramitacao;
COMMENT ON TABLE licenciamento.pessoa_fisica
  IS 'Entidade responsavel por armazenar as informações referentes as pessoas físicas.';
COMMENT ON COLUMN licenciamento.pessoa_fisica.id_pessoa IS 'Identificado único da entidade. É também o identificador único da entidade pessoa que realizará o relacionamento entre pessoa_fisica e pessoa.';
COMMENT ON COLUMN licenciamento.pessoa_fisica.nome IS 'Nome da pessoa.';
COMMENT ON COLUMN licenciamento.pessoa_fisica.cpf IS 'CPF da pessoa.';
COMMENT ON COLUMN licenciamento.pessoa_fisica.sexo IS 'Sexo da pessoa.';
COMMENT ON COLUMN licenciamento.pessoa_fisica.data_nascimento IS 'Data de Nascimento da pessoa.';
COMMENT ON COLUMN licenciamento.pessoa_fisica.nome_mae IS 'Nome da mãe da pessoa.';
COMMENT ON COLUMN licenciamento.pessoa_fisica.rg IS 'RG da pessoa.';
COMMENT ON COLUMN licenciamento.pessoa_fisica.titulo_eleitor IS 'Título de eleitor da pessoa.';
COMMENT ON COLUMN licenciamento.pessoa_fisica.id_estado_civil IS 'Identificador único da entidade estado_civil que realizará o relacionamento entre estado_civil e pessoa_fisica.';
COMMENT ON COLUMN licenciamento.pessoa_fisica.id_municipio_nascimento IS 'Identificador único da entidade municipio que realizará o relacionamento entre municipio e pessoa_fisica. É o município de nascimento da pessoa.';
COMMENT ON COLUMN licenciamento.pessoa_fisica.naturalidade IS 'Naturalidade da pessoa.';

CREATE TABLE licenciamento.endereco
(
  id serial NOT NULL, -- Identificado único da entidade.
  cep integer, -- CEP.
  correspondencia boolean NOT NULL, -- Flag que indica se o endereço é de correspondência.
  tipo_endereco integer NOT NULL, -- Indica se o endereço é zona rual ou zona urbana (0 - zona urbana e 1 - zona rural).
  logradouro character varying(200), -- Logradouro.
  numero character varying(20), -- Número.
  complemento character varying(200), -- Complemento.
  bairro character varying(100), -- Bairro.
  id_municipio integer NOT NULL, -- Identificador único da entidade municipio que realizará o relacionamento entre municipio e endereco.
  caixa_postal character varying(10), -- Caixa Postal.
  roteiro_acesso text, -- Descrição de acesso, no caso de ser zona rural.
  CONSTRAINT pk_endereco PRIMARY KEY (id),
  CONSTRAINT fk_e_municipio FOREIGN KEY (id_municipio)
      REFERENCES licenciamento.municipio (id_municipio) 
 );
ALTER TABLE licenciamento.endereco
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.endereco TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.endereco TO licenciamento_ap;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.endereco TO portal_seguranca_licenciamento_ap;
GRANT SELECT ON TABLE licenciamento.endereco TO tramitacao;
COMMENT ON TABLE licenciamento.endereco
  IS 'Entidade responsavel por armazenar aos enderecos.';
COMMENT ON COLUMN licenciamento.endereco.id IS 'Identificado único da entidade.';
COMMENT ON COLUMN licenciamento.endereco.cep IS 'CEP.';
COMMENT ON COLUMN licenciamento.endereco.correspondencia IS 'Flag que indica se o endereço é de correspondência.';
COMMENT ON COLUMN licenciamento.endereco.tipo_endereco IS 'Indica se o endereço é zona rual ou zona urbana (0 - zona urbana e 1 - zona rural).';
COMMENT ON COLUMN licenciamento.endereco.logradouro IS 'Logradouro.';
COMMENT ON COLUMN licenciamento.endereco.numero IS 'Número.';
COMMENT ON COLUMN licenciamento.endereco.complemento IS 'Complemento.';
COMMENT ON COLUMN licenciamento.endereco.bairro IS 'Bairro.';
COMMENT ON COLUMN licenciamento.endereco.id_municipio IS 'Identificador único da entidade municipio que realizará o relacionamento entre municipio e endereco.';
COMMENT ON COLUMN licenciamento.endereco.caixa_postal IS 'Caixa Postal.';
COMMENT ON COLUMN licenciamento.endereco.roteiro_acesso IS 'Descrição de acesso, no caso de ser zona rural.';

CREATE TABLE licenciamento.responsavel_empreendimento
(
  id serial NOT NULL, -- Identificado único da entidade.
  tipo_responsavel integer NOT NULL, -- Indica o tipo do responsável: 0 - Responsável Legal e 1 - Responsável Técnico.
  id_pessoa integer NOT NULL, -- Identificador único da entidade pessoa que realizará o relacionamento pessoa e responsavel_empreendimento.
  id_orgao_classe integer, -- Identificador único da entidade orgao_classe que realizará o relacionamento orgao_classe e responsavel_empreendimento.
  numero_registro character varying(20), -- Número do registro no Órgão de Classe
  numero_credenciamento character varying(20), -- Número do Credenciamento.
  id_endereco_correspondencia integer NOT NULL, -- Identificador único da entidade endereco que realizará o relacionamento endereco e responsavel_empreendimento.
  id_empreendimento integer NOT NULL, -- Identificador único da entidade empreendimento que realizará o relacionamento empreendimento e responsavel_empreendimento.
  id_contato integer NOT NULL, -- Identificador único da entidade contato que realizará o relacionamento contato e responsavel_empreendimento.
  art text, -- Armazena a Anotação de Responsabilidade Técnica do responsável do empreendimento.
  CONSTRAINT pk_responsavel_empreendimento PRIMARY KEY (id),
  CONSTRAINT fk_re_contato FOREIGN KEY (id_contato)
      REFERENCES licenciamento.contato (id),
  CONSTRAINT fk_re_empreendimento FOREIGN KEY (id_empreendimento)
      REFERENCES licenciamento.empreendimento (id),
  CONSTRAINT fk_re_endereco_correspondencia FOREIGN KEY (id_endereco_correspondencia)
      REFERENCES licenciamento.endereco (id),
  CONSTRAINT fk_re_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES licenciamento.pessoa (id),
  CONSTRAINT fk_rl_orgao_classe FOREIGN KEY (id_orgao_classe)
      REFERENCES licenciamento.orgao_classe (id) 
);
ALTER TABLE licenciamento.responsavel_empreendimento
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.responsavel_empreendimento TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.responsavel_empreendimento TO licenciamento_ap;
GRANT SELECT ON TABLE licenciamento.responsavel_empreendimento TO tramitacao;
COMMENT ON TABLE licenciamento.responsavel_empreendimento
  IS 'Entidade responsavel por armazenar as informações referentes aos responsáveis pelo empreendimento.';
COMMENT ON COLUMN licenciamento.responsavel_empreendimento.id IS 'Identificado único da entidade.';
COMMENT ON COLUMN licenciamento.responsavel_empreendimento.tipo_responsavel IS 'Indica o tipo do responsável: 0 - Responsável Legal e 1 - Responsável Técnico.';
COMMENT ON COLUMN licenciamento.responsavel_empreendimento.id_pessoa IS 'Identificador único da entidade pessoa que realizará o relacionamento pessoa e responsavel_empreendimento.';
COMMENT ON COLUMN licenciamento.responsavel_empreendimento.id_orgao_classe IS 'Identificador único da entidade orgao_classe que realizará o relacionamento orgao_classe e responsavel_empreendimento.';
COMMENT ON COLUMN licenciamento.responsavel_empreendimento.numero_registro IS 'Número do registro no Órgão de Classe';
COMMENT ON COLUMN licenciamento.responsavel_empreendimento.numero_credenciamento IS 'Número do Credenciamento.';
COMMENT ON COLUMN licenciamento.responsavel_empreendimento.id_endereco_correspondencia IS 'Identificador único da entidade endereco que realizará o relacionamento endereco e responsavel_empreendimento.';
COMMENT ON COLUMN licenciamento.responsavel_empreendimento.id_empreendimento IS 'Identificador único da entidade empreendimento que realizará o relacionamento empreendimento e responsavel_empreendimento.';
COMMENT ON COLUMN licenciamento.responsavel_empreendimento.id_contato IS 'Identificador único da entidade contato que realizará o relacionamento contato e responsavel_empreendimento.';
COMMENT ON COLUMN licenciamento.responsavel_empreendimento.art IS 'Armazena a Anotação de Responsabilidade Técnica do responsável do empreendimento.';

CREATE TABLE licenciamento.documento_responsavel_empreendimento
(
  id serial NOT NULL, -- Identificado único da entidade.
  id_documento integer NOT NULL, -- Identificador único da entidade documento que realizará o relacionamento entre documento e documento_responsavel_empreendimento.
  id_responsavel_empreendimento integer NOT NULL, -- Identificador único da entidade responsavel_empreendimento que realizará o relacionamento entre responsavel_empreendimento e documento_responsavel_empreendimento.
  CONSTRAINT pk_documento_responsavel_empreendimento PRIMARY KEY (id),
  CONSTRAINT fk_dre_documento FOREIGN KEY (id_documento)
      REFERENCES licenciamento.documento (id),
  CONSTRAINT fk_dre_responsavel_empreendimento FOREIGN KEY (id_responsavel_empreendimento)
      REFERENCES licenciamento.responsavel_empreendimento (id) 
);
ALTER TABLE licenciamento.documento_responsavel_empreendimento
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.documento_responsavel_empreendimento TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.documento_responsavel_empreendimento TO licenciamento_ap;
GRANT SELECT ON TABLE licenciamento.documento_responsavel_empreendimento TO tramitacao;
COMMENT ON TABLE licenciamento.documento_responsavel_empreendimento
  IS 'Entidade responsavel por armazenar aos documentos.';
COMMENT ON COLUMN licenciamento.documento_responsavel_empreendimento.id IS 'Identificado único da entidade.';
COMMENT ON COLUMN licenciamento.documento_responsavel_empreendimento.id_documento IS 'Identificador único da entidade documento que realizará o relacionamento entre documento e documento_responsavel_empreendimento.';
COMMENT ON COLUMN licenciamento.documento_responsavel_empreendimento.id_responsavel_empreendimento IS 'Identificador único da entidade responsavel_empreendimento que realizará o relacionamento entre responsavel_empreendimento e documento_responsavel_empreendimento.';

CREATE TABLE licenciamento.endereco_pessoa
(
  id serial NOT NULL, -- Identificado único da entidade.
  id_pessoa integer NOT NULL, -- Identificador único da entidade pessoa que realizará o relacionamento pessoa e endereco_pessoa.
  id_endereco integer NOT NULL, -- Identificador único da entidade endereco que realizará o relacionamento endereco e endereco_pessoa.
  CONSTRAINT pk_endereco_pessoa PRIMARY KEY (id),
  CONSTRAINT fk_ep_endereco FOREIGN KEY (id_endereco)
      REFERENCES licenciamento.endereco (id),
  CONSTRAINT fk_ep_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES licenciamento.pessoa (id) 
);
ALTER TABLE licenciamento.endereco_pessoa
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.endereco_pessoa TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.endereco_pessoa TO licenciamento_ap;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.endereco_pessoa TO portal_seguranca_licenciamento_ap;
GRANT SELECT ON TABLE licenciamento.endereco_pessoa TO tramitacao;
COMMENT ON TABLE licenciamento.endereco_pessoa
  IS 'Entidade responsavel por armazenar as informações referentes aos endereços que uma pessoa pode ter.';
COMMENT ON COLUMN licenciamento.endereco_pessoa.id IS 'Identificado único da entidade.';
COMMENT ON COLUMN licenciamento.endereco_pessoa.id_pessoa IS 'Identificador único da entidade pessoa que realizará o relacionamento pessoa e endereco_pessoa.';
COMMENT ON COLUMN licenciamento.endereco_pessoa.id_endereco IS 'Identificador único da entidade endereco que realizará o relacionamento endereco e endereco_pessoa.';

CREATE TABLE licenciamento.endereco_empreendimento
(
  id serial NOT NULL, -- Identificador único da entidade.
  id_empreendimento integer NOT NULL, -- Identificador único da entidade empreendimento que realizará o relacionamento empreendimento e endereco_empreendimento.
  id_endereco integer NOT NULL, -- Identificador único da entidade endereco que realizará o relacionamento endereco e endereco_empreendimento.
  CONSTRAINT pk_endereco_empreendimento PRIMARY KEY (id),
  CONSTRAINT fk_ee_empreendimento FOREIGN KEY (id_empreendimento)
      REFERENCES licenciamento.empreendimento (id),
  CONSTRAINT fk_ee_endereco FOREIGN KEY (id_endereco)
      REFERENCES licenciamento.endereco (id) 
);
ALTER TABLE licenciamento.endereco_empreendimento
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.endereco_empreendimento TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.endereco_empreendimento TO licenciamento_ap;
GRANT SELECT ON TABLE licenciamento.endereco_empreendimento TO tramitacao;
COMMENT ON TABLE licenciamento.endereco_empreendimento
  IS 'Entidade responsavel por armazenar as informações referentes aos endereços que um empreendimento pode ter.';
COMMENT ON COLUMN licenciamento.endereco_empreendimento.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN licenciamento.endereco_empreendimento.id_empreendimento IS 'Identificador único da entidade empreendimento que realizará o relacionamento empreendimento e endereco_empreendimento.';
COMMENT ON COLUMN licenciamento.endereco_empreendimento.id_endereco IS 'Identificador único da entidade endereco que realizará o relacionamento endereco e endereco_empreendimento.';

CREATE TABLE licenciamento.empreendedor
(
  id serial NOT NULL, -- Identificado único da entidade.
  id_pessoa integer NOT NULL, -- Identificador único da entidade pessoa que realizará o relacionamento entre pessoa e empreendedor.
  tipo_esfera integer, -- Tipo de Esfera, podendo ser 0 - Municipal, 1 - Estadual e 2 - Federal.
  ativo boolean DEFAULT true, -- Indica se o empreendedor está ativo (TRUE - Ativo; FALSE - Inativo).
  data_cadastro timestamp without time zone DEFAULT now(), -- Data de cadastro do empreendedor.
  CONSTRAINT pk_empreendedor PRIMARY KEY (id),
  CONSTRAINT fk_e_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES licenciamento.pessoa (id) 
);
ALTER TABLE licenciamento.empreendedor
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.empreendedor TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.empreendedor TO licenciamento_ap;
GRANT SELECT ON TABLE licenciamento.empreendedor TO tramitacao;
COMMENT ON TABLE licenciamento.empreendedor
  IS 'Entidade responsavel por armazenar as informações referentes as pessoas que são empreendedores.';
COMMENT ON COLUMN licenciamento.empreendedor.id IS 'Identificado único da entidade.';
COMMENT ON COLUMN licenciamento.empreendedor.id_pessoa IS 'Identificador único da entidade pessoa que realizará o relacionamento entre pessoa e empreendedor.';
COMMENT ON COLUMN licenciamento.empreendedor.tipo_esfera IS 'Tipo de Esfera, podendo ser 0 - Municipal, 1 - Estadual e 2 - Federal.';
COMMENT ON COLUMN licenciamento.empreendedor.ativo IS 'Indica se o empreendedor está ativo (TRUE - Ativo; FALSE - Inativo).';
COMMENT ON COLUMN licenciamento.empreendedor.data_cadastro IS 'Data de cadastro do empreendedor.';

CREATE TABLE licenciamento.representante_legal
(
  id serial NOT NULL, -- Identificado único da entidade.
  id_pessoa integer NOT NULL, -- Identificador único da entidade pessoa que realizará o relacionamento pessoa e representante_legal.
  id_empreendedor integer NOT NULL, -- Identificador único da entidade empreendedor que realizará o relacionamento empreendedor e representante_legal.
  data_vinculacao timestamp without time zone NOT NULL, -- Data de vinculação do Representante Legal ao empreendedor.
  tipo integer DEFAULT 0, -- Armazena o tipo do representante legal, com os possíveis valores: 0 - Representante, 1 - Proprietário, 2 - Arrendatário.
  id_empreendimento integer, -- Identificador de referência para o empreendimento.
  CONSTRAINT pk_representante_legal PRIMARY KEY (id),
  CONSTRAINT fk_rl_empreendedor FOREIGN KEY (id_empreendedor)
      REFERENCES licenciamento.empreendedor (id),
  CONSTRAINT fk_rl_empreendimento FOREIGN KEY (id_empreendimento)
      REFERENCES licenciamento.empreendimento (id),
  CONSTRAINT fk_rl_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES licenciamento.pessoa (id) 
);
ALTER TABLE licenciamento.representante_legal
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.representante_legal TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.representante_legal TO licenciamento_ap;
GRANT SELECT ON TABLE licenciamento.representante_legal TO tramitacao;
COMMENT ON TABLE licenciamento.representante_legal
  IS 'Entidade responsavel por armazenar as informações referentes aos representantes legais vinculados a um empreendedor.';
COMMENT ON COLUMN licenciamento.representante_legal.id IS 'Identificado único da entidade.';
COMMENT ON COLUMN licenciamento.representante_legal.id_pessoa IS 'Identificador único da entidade pessoa que realizará o relacionamento pessoa e representante_legal.';
COMMENT ON COLUMN licenciamento.representante_legal.id_empreendedor IS 'Identificador único da entidade empreendedor que realizará o relacionamento empreendedor e representante_legal.';
COMMENT ON COLUMN licenciamento.representante_legal.data_vinculacao IS 'Data de vinculação do Representante Legal ao empreendedor.';
COMMENT ON COLUMN licenciamento.representante_legal.tipo IS 'Armazena o tipo do representante legal, com os possíveis valores: 0 - Representante, 1 - Proprietário, 2 - Arrendatário.';
COMMENT ON COLUMN licenciamento.representante_legal.id_empreendimento IS 'Identificador de referência para o empreendimento.';

CREATE TABLE licenciamento.historico_alteracao_empreendimento_pessoa
(
  id serial NOT NULL, -- Identificador primário da entidade.
  data timestamp without time zone NOT NULL, -- Data do histórico.
  id_empreendimento integer NOT NULL, -- Identificador da entidade empreendimento.
  id_pessoa integer NOT NULL, -- Identificador da entidade pessoa.
  CONSTRAINT pk_historico_alteracao_empreendimento_pessoa PRIMARY KEY (id),
  CONSTRAINT fk_haep_empreendimento FOREIGN KEY (id_empreendimento)
      REFERENCES licenciamento.empreendimento (id),
  CONSTRAINT fk_haep_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES licenciamento.pessoa (id) 
);
ALTER TABLE licenciamento.historico_alteracao_empreendimento_pessoa
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.historico_alteracao_empreendimento_pessoa TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.historico_alteracao_empreendimento_pessoa TO licenciamento_ap;
GRANT SELECT ON TABLE licenciamento.historico_alteracao_empreendimento_pessoa TO tramitacao;
COMMENT ON TABLE licenciamento.historico_alteracao_empreendimento_pessoa
  IS 'Entidade responsável por armazenar o relacionamento entre empreendimento e pessoa que representa um histórico.';
COMMENT ON COLUMN licenciamento.historico_alteracao_empreendimento_pessoa.id IS 'Identificador primário da entidade.';
COMMENT ON COLUMN licenciamento.historico_alteracao_empreendimento_pessoa.data IS 'Data do histórico.';
COMMENT ON COLUMN licenciamento.historico_alteracao_empreendimento_pessoa.id_empreendimento IS 'Identificador da entidade empreendimento.';
COMMENT ON COLUMN licenciamento.historico_alteracao_empreendimento_pessoa.id_pessoa IS 'Identificador da entidade pessoa.';

CREATE TABLE licenciamento.historico_alteracao_empreendimento_empreendedor
(
  id serial NOT NULL, -- Identificador primário da entidade.
  data timestamp without time zone NOT NULL, -- Data do histórico.
  id_empreendimento integer NOT NULL, -- Identificador da entidade empreendimento.
  id_empreendedor integer NOT NULL, -- Identificador da entidade empreendedor.
  CONSTRAINT pk_historico_alteracao_empreendimento_empreendedor PRIMARY KEY (id),
  CONSTRAINT fk_haee_empreendedor FOREIGN KEY (id_empreendedor)
      REFERENCES licenciamento.empreendedor (id),
  CONSTRAINT fk_haee_empreendimento FOREIGN KEY (id_empreendimento)
      REFERENCES licenciamento.empreendimento (id) 
);
ALTER TABLE licenciamento.historico_alteracao_empreendimento_empreendedor
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.historico_alteracao_empreendimento_empreendedor TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE licenciamento.historico_alteracao_empreendimento_empreendedor TO licenciamento_ap;
GRANT SELECT ON TABLE licenciamento.historico_alteracao_empreendimento_empreendedor TO tramitacao;
COMMENT ON TABLE licenciamento.historico_alteracao_empreendimento_empreendedor
  IS 'Entidade responsável por armazenar o relacionamento entre empreendimento e empreendedor que representa um histórico.';
COMMENT ON COLUMN licenciamento.historico_alteracao_empreendimento_empreendedor.id IS 'Identificador primário da entidade.';
COMMENT ON COLUMN licenciamento.historico_alteracao_empreendimento_empreendedor.data IS 'Data do histórico.';
COMMENT ON COLUMN licenciamento.historico_alteracao_empreendimento_empreendedor.id_empreendimento IS 'Identificador da entidade empreendimento.';
COMMENT ON COLUMN licenciamento.historico_alteracao_empreendimento_empreendedor.id_empreendedor IS 'Identificador da entidade empreendedor.';


ALTER TABLE licenciamento.empreendimento
    ADD COLUMN id_pessoa integer,
    ADD COLUMN id_contato integer,
    ADD COLUMN id_empreendedor integer,
    ADD COLUMN id_endereco integer,
    ADD COLUMN id_redesim integer,
    ADD COLUMN id_cadastrante integer,
    ADD COLUMN the_geom geometry,
    ADD COLUMN houve_alteracoes boolean DEFAULT false;
COMMENT ON COLUMN licenciamento.empreendimento.id_pessoa IS 'Identificador único da entidade pessoa que realizará o relacionamento entre pessoa e empreendimento.';
COMMENT ON COLUMN licenciamento.empreendimento.id_contato IS 'Identificador único da entidade contato que realizará o relacionamento entre contato e empreendimento.';
COMMENT ON COLUMN licenciamento.empreendimento.id_empreendedor IS 'Identificador único da entidade empreendedor que realizará o relacionamento entreempreendedor e empreendimento.';
COMMENT ON COLUMN licenciamento.empreendimento.id_endereco IS 'Identificador único da entidade endereco que realizará o relacionamento entre endereco e empreendimento.';
COMMENT ON COLUMN licenciamento.empreendimento.id_redesim IS 'Identificador do empreendimento cadastrado na base RedeSim.';
COMMENT ON COLUMN licenciamento.empreendimento.id_cadastrante IS 'Identificador do usuário responsável pelo cadastro da caracterização';
COMMENT ON COLUMN licenciamento.empreendimento.the_geom IS 'Campo georeferenciado que indica a localização do empreendimento.';
COMMENT ON COLUMN licenciamento.empreendimento.houve_alteracoes IS 'Flag para identificar se houveram alterações nos dados do empreendimento.';

ALTER TABLE licenciamento.caracterizacao DROP COLUMN cpf_cnpj_cadastrante;

ALTER TABLE licenciamento.empreendimento DROP COLUMN cpf_cnpj_cadastrante;

ALTER TABLE licenciamento.empreendimento DROP COLUMN cpf_cnpj;

ALTER TABLE licenciamento.caracterizacao 
  ADD CONSTRAINT fk_c_pessoa FOREIGN KEY (id_cadastrante) 
    REFERENCES licenciamento.pessoa (id);

