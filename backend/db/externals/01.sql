# --- !Ups
--8
ALTER TABLE licenciamento.licenca ADD COLUMN id_licenca_analise INTEGER;
ALTER TABLE licenciamento.licenca ADD CONSTRAINT fk_l_licenca_analise FOREIGN KEY (id_licenca_analise) REFERENCES analise.licenca_analise (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

COMMENT ON COLUMN licenciamento.licenca.id_licenca_analise IS 'Identificador da entidade analise.licenca_analise que faz o relacionamento entre as entidades licenca e analise.licenca_analise.';

--9
CREATE OR REPLACE VIEW licenciamento.licenca_emitida AS
    WITH licencas_emitidas AS
    (
        SELECT  dispensa_licenciamento.id,
                dispensa_licenciamento.id_documento,
                dispensa_licenciamento.id_caracterizacao,
                dispensa_licenciamento.data_cadastro,
                dispensa_licenciamento.informacao_adicional,
                NULL::timestamp without time zone AS data_validade,
                dispensa_licenciamento.numero,
                0 AS tipo_dispensa
        FROM licenciamento.dispensa_licenciamento

        UNION

        SELECT  licenca.id,
                licenca.id_documento,
                licenca.id_caracterizacao,
                licenca.data_cadastro,
                NULL::text AS informacao_adicional,
                licenca.data_validade,
                licenca.numero,
                1 AS tipo_dispensa
        FROM licenciamento.licenca
        WHERE ativo=true
        AND id NOT IN (SELECT id_licenca FROM analise.licenca_suspensa)
    )

    SELECT  row_number() OVER () AS id_sequencial,
            licencas_emitidas.id,
            licencas_emitidas.id_documento,
            licencas_emitidas.id_caracterizacao,
            licencas_emitidas.data_cadastro,
            licencas_emitidas.informacao_adicional,
            licencas_emitidas.data_validade,
            licencas_emitidas.numero,
            licencas_emitidas.tipo_dispensa
    FROM licencas_emitidas;

GRANT SELECT ON TABLE licenciamento.licenca_emitida TO licenciamento_ap;

COMMENT ON VIEW licenciamento.licenca_emitida IS 'View responsável por fazer a união das tabelas "licenciamento.dispensa_licenciamento" e "licenciamento.licenca" para que possa ser utilizada na consulta de licenças emitidas.';
COMMENT ON COLUMN licenciamento.licenca_emitida.tipo_dispensa IS 'Indica o tipo de licenca emitida (0 - Dispensa; 1-Simplificado);';

--10
CREATE OR REPLACE VIEW licenciamento.licenca_emitida AS
 WITH licencas_emitidas AS (
         SELECT dispensa_licenciamento.id,
            dispensa_licenciamento.id_documento,
            dispensa_licenciamento.id_caracterizacao,
            dispensa_licenciamento.data_cadastro,
            dispensa_licenciamento.informacao_adicional,
            NULL::timestamp without time zone AS data_validade,
            dispensa_licenciamento.numero,
            0 AS tipo_dispensa
           FROM licenciamento.dispensa_licenciamento
    WHERE dispensa_licenciamento.ativo = true
        UNION
         SELECT licenca.id,
            licenca.id_documento,
            licenca.id_caracterizacao,
            licenca.data_cadastro,
            NULL::text AS informacao_adicional,
            licenca.data_validade,
            licenca.numero,
            1 AS tipo_dispensa
           FROM licenciamento.licenca
          WHERE licenca.ativo = true AND NOT (licenca.id IN ( SELECT licenca_suspensa.id_licenca
                   FROM analise.licenca_suspensa))
        )
 SELECT row_number() OVER () AS id_sequencial,
    licencas_emitidas.id,
    licencas_emitidas.id_documento,
    licencas_emitidas.id_caracterizacao,
    licencas_emitidas.data_cadastro,
    licencas_emitidas.informacao_adicional,
    licencas_emitidas.data_validade,
    licencas_emitidas.numero,
    licencas_emitidas.tipo_dispensa
   FROM licencas_emitidas;

ALTER TABLE licenciamento.licenca_emitida
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.licenca_emitida TO postgres;
GRANT SELECT ON TABLE licenciamento.licenca_emitida TO licenciamento_ap;

COMMENT ON VIEW licenciamento.licenca_emitida IS 'View responsável por fazer a união das tabelas "licenciamento.dispensa_licenciamento" e "licenciamento.licenca" para que possa ser utilizada na consulta de licenças emitidas.';
COMMENT ON COLUMN licenciamento.licenca_emitida.tipo_dispensa IS 'Indica o tipo de licenca emitida (0 - Dispensa; 1-Simplificado);';

--11
CREATE OR REPLACE VIEW licenciamento.licenca_emitida AS
 WITH licencas_emitidas AS (
         SELECT dispensa_licenciamento.id,
            dispensa_licenciamento.id_documento,
            dispensa_licenciamento.id_caracterizacao,
            dispensa_licenciamento.data_cadastro,
            dispensa_licenciamento.informacao_adicional,
            NULL::timestamp without time zone AS data_validade,
            dispensa_licenciamento.numero,
            0 AS tipo_dispensa,
            dispensa_licenciamento.ativo
           FROM licenciamento.dispensa_licenciamento
        UNION
         SELECT licenca.id,
            licenca.id_documento,
            licenca.id_caracterizacao,
            licenca.data_cadastro,
            NULL::text AS informacao_adicional,
            licenca.data_validade,
            licenca.numero,
            1 AS tipo_dispensa,
            licenca.ativo
           FROM licenciamento.licenca
          WHERE licenca.ativo = true AND NOT (licenca.id IN ( SELECT licenca_suspensa.id_licenca
                   FROM analise.licenca_suspensa))
        )
 SELECT row_number() OVER () AS id_sequencial,
    licencas_emitidas.id,
    licencas_emitidas.id_documento,
    licencas_emitidas.id_caracterizacao,
    licencas_emitidas.data_cadastro,
    licencas_emitidas.informacao_adicional,
    licencas_emitidas.data_validade,
    licencas_emitidas.numero,
    licencas_emitidas.tipo_dispensa,
    licencas_emitidas.ativo
   FROM licencas_emitidas;

ALTER TABLE licenciamento.licenca_emitida
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.licenca_emitida TO postgres;
GRANT SELECT ON TABLE licenciamento.licenca_emitida TO licenciamento_ap;

COMMENT ON VIEW licenciamento.licenca_emitida IS 'View responsável por fazer a união das tabelas "licenciamento.dispensa_licenciamento" e "licenciamento.licenca" para que possa ser utilizada na consulta de licenças emitidas.';
COMMENT ON COLUMN licenciamento.licenca_emitida.tipo_dispensa IS 'Indica o tipo de licenca emitida (0 - Dispensa; 1-Simplificado);';
--12
CREATE OR REPLACE VIEW licenciamento.licenca_emitida AS
 WITH licencas_emitidas AS (
         SELECT dispensa_licenciamento.id,
            dispensa_licenciamento.id_documento,
            dispensa_licenciamento.id_caracterizacao,
            dispensa_licenciamento.data_cadastro,
            dispensa_licenciamento.informacao_adicional,
            NULL::timestamp without time zone AS data_validade,
            dispensa_licenciamento.numero,
            0 AS tipo_dispensa,
            dispensa_licenciamento.ativo
           FROM licenciamento.dispensa_licenciamento
        UNION
         SELECT licenca.id,
            licenca.id_documento,
            licenca.id_caracterizacao,
            licenca.data_cadastro,
            NULL::text AS informacao_adicional,
            licenca.data_validade,
            licenca.numero,
            1 AS tipo_dispensa,
            licenca.ativo
           FROM licenciamento.licenca

        )
 SELECT row_number() OVER () AS id_sequencial,
    licencas_emitidas.id,
    licencas_emitidas.id_documento,
    licencas_emitidas.id_caracterizacao,
    licencas_emitidas.data_cadastro,
    licencas_emitidas.informacao_adicional,
    licencas_emitidas.data_validade,
    licencas_emitidas.numero,
    licencas_emitidas.tipo_dispensa,
    licencas_emitidas.ativo
   FROM licencas_emitidas;

ALTER TABLE licenciamento.licenca_emitida
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.licenca_emitida TO postgres;
GRANT SELECT ON TABLE licenciamento.licenca_emitida TO licenciamento_ap;

COMMENT ON VIEW licenciamento.licenca_emitida IS 'View responsável por fazer a união das tabelas "licenciamento.dispensa_licenciamento" e "licenciamento.licenca" para que possa ser utilizada na consulta de licenças emitidas.';
COMMENT ON COLUMN licenciamento.licenca_emitida.tipo_dispensa IS 'Indica o tipo de licenca emitida (0 - Dispensa; 1-Simplificado);';

--103 evolution am
UPDATE analise.processo SET numero = regexp_replace(numero, '.*/', '') || '/' || regexp_replace(numero, '/.*', '');
UPDATE analise.processo SET numero = lpad(ltrim(numero,'0'), 12, '0');


# --- !Downs
--103 evolution am
UPDATE analise.processo SET numero = regexp_replace(numero, '.*/', '') || '/' || lpad(ltrim(regexp_replace(numero, '/.*', ''),'0'),10,'0');


--12
DROP VIEW licenciamento.licenca_emitida;

CREATE OR REPLACE VIEW licenciamento.licenca_emitida AS
 WITH licencas_emitidas AS (
         SELECT dispensa_licenciamento.id,
            dispensa_licenciamento.id_documento,
            dispensa_licenciamento.id_caracterizacao,
            dispensa_licenciamento.data_cadastro,
            dispensa_licenciamento.informacao_adicional,
            NULL::timestamp without time zone AS data_validade,
            dispensa_licenciamento.numero,
            0 AS tipo_dispensa,
            dispensa_licenciamento.ativo
           FROM licenciamento.dispensa_licenciamento
        UNION
         SELECT licenca.id,
            licenca.id_documento,
            licenca.id_caracterizacao,
            licenca.data_cadastro,
            NULL::text AS informacao_adicional,
            licenca.data_validade,
            licenca.numero,
            1 AS tipo_dispensa,
            licenca.ativo
           FROM licenciamento.licenca
          WHERE licenca.ativo = true AND NOT (licenca.id IN ( SELECT licenca_suspensa.id_licenca
                   FROM analise.licenca_suspensa))
        )
 SELECT row_number() OVER () AS id_sequencial,
    licencas_emitidas.id,
    licencas_emitidas.id_documento,
    licencas_emitidas.id_caracterizacao,
    licencas_emitidas.data_cadastro,
    licencas_emitidas.informacao_adicional,
    licencas_emitidas.data_validade,
    licencas_emitidas.numero,
    licencas_emitidas.tipo_dispensa,
    licencas_emitidas.ativo
   FROM licencas_emitidas;

ALTER TABLE licenciamento.licenca_emitida
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.licenca_emitida TO postgres;
GRANT SELECT ON TABLE licenciamento.licenca_emitida TO licenciamento_ap;

COMMENT ON VIEW licenciamento.licenca_emitida IS 'View responsável por fazer a união das tabelas "licenciamento.dispensa_licenciamento" e "licenciamento.licenca" para que possa ser utilizada na consulta de licenças emitidas.';
COMMENT ON COLUMN licenciamento.licenca_emitida.tipo_dispensa IS 'Indica o tipo de licenca emitida (0 - Dispensa; 1-Simplificado);';



--11
DROP VIEW licenciamento.licenca_emitida;

CREATE OR REPLACE VIEW licenciamento.licenca_emitida AS
 WITH licencas_emitidas AS (
         SELECT dispensa_licenciamento.id,
            dispensa_licenciamento.id_documento,
            dispensa_licenciamento.id_caracterizacao,
            dispensa_licenciamento.data_cadastro,
            dispensa_licenciamento.informacao_adicional,
            NULL::timestamp without time zone AS data_validade,
            dispensa_licenciamento.numero,
            0 AS tipo_dispensa
           FROM licenciamento.dispensa_licenciamento
    WHERE dispensa_licenciamento.ativo = true
        UNION
         SELECT licenca.id,
            licenca.id_documento,
            licenca.id_caracterizacao,
            licenca.data_cadastro,
            NULL::text AS informacao_adicional,
            licenca.data_validade,
            licenca.numero,
            1 AS tipo_dispensa
           FROM licenciamento.licenca
          WHERE licenca.ativo = true AND NOT (licenca.id IN ( SELECT licenca_suspensa.id_licenca
                   FROM analise.licenca_suspensa))
        )
 SELECT row_number() OVER () AS id_sequencial,
    licencas_emitidas.id,
    licencas_emitidas.id_documento,
    licencas_emitidas.id_caracterizacao,
    licencas_emitidas.data_cadastro,
    licencas_emitidas.informacao_adicional,
    licencas_emitidas.data_validade,
    licencas_emitidas.numero,
    licencas_emitidas.tipo_dispensa
   FROM licencas_emitidas;

ALTER TABLE licenciamento.licenca_emitida
  OWNER TO postgres;
GRANT ALL ON TABLE licenciamento.licenca_emitida TO postgres;
GRANT SELECT ON TABLE licenciamento.licenca_emitida TO licenciamento_ap;

COMMENT ON VIEW licenciamento.licenca_emitida IS 'View responsável por fazer a união das tabelas "licenciamento.dispensa_licenciamento" e "licenciamento.licenca" para que possa ser utilizada na consulta de licenças emitidas.';
COMMENT ON COLUMN licenciamento.licenca_emitida.tipo_dispensa IS 'Indica o tipo de licenca emitida (0 - Dispensa; 1-Simplificado);';



--10
CREATE OR REPLACE VIEW licenciamento.licenca_emitida AS
    WITH licencas_emitidas AS
    (
        SELECT  dispensa_licenciamento.id,
                dispensa_licenciamento.id_documento,
                dispensa_licenciamento.id_caracterizacao,
                dispensa_licenciamento.data_cadastro,
                dispensa_licenciamento.informacao_adicional,
                NULL::timestamp without time zone AS data_validade,
                dispensa_licenciamento.numero,
                0 AS tipo_dispensa
        FROM licenciamento.dispensa_licenciamento
        WHERE ativo=true

        UNION

        SELECT  licenca.id,
                licenca.id_documento,
                licenca.id_caracterizacao,
                licenca.data_cadastro,
                NULL::text AS informacao_adicional,
                licenca.data_validade,
                licenca.numero,
                1 AS tipo_dispensa
        FROM licenciamento.licenca
        WHERE ativo=true
        AND id NOT IN (SELECT id_licenca FROM analise.suspensao)
    )

    SELECT  row_number() OVER () AS id_sequencial,
            licencas_emitidas.id,
            licencas_emitidas.id_documento,
            licencas_emitidas.id_caracterizacao,
            licencas_emitidas.data_cadastro,
            licencas_emitidas.informacao_adicional,
            licencas_emitidas.data_validade,
            licencas_emitidas.numero,
            licencas_emitidas.tipo_dispensa
    FROM licencas_emitidas;

GRANT SELECT ON TABLE licenciamento.licenca_emitida TO licenciamento_ap;

COMMENT ON VIEW licenciamento.licenca_emitida IS 'View responsável por fazer a união das tabelas "licenciamento.dispensa_licenciamento" e "licenciamento.licenca" para que possa ser utilizada na consulta de licenças emitidas.';
COMMENT ON COLUMN licenciamento.licenca_emitida.tipo_dispensa IS 'Indica o tipo de licenca emitida (0 - Dispensa; 1-Simplificado);';



--9
CREATE OR REPLACE VIEW licenciamento.licenca_emitida AS
	WITH licencas_emitidas AS
	(
        SELECT 	dispensa_licenciamento.id,
            	dispensa_licenciamento.id_documento,
            	dispensa_licenciamento.id_caracterizacao,
            	dispensa_licenciamento.data_cadastro,
            	dispensa_licenciamento.informacao_adicional,
            	NULL::timestamp without time zone AS data_validade,
            	dispensa_licenciamento.numero,
            	0 AS tipo_dispensa
        FROM licenciamento.dispensa_licenciamento

        UNION

        SELECT 	licenca.id,
            	licenca.id_documento,
            	licenca.id_caracterizacao,
            	licenca.data_cadastro,
            	NULL::text AS informacao_adicional,
            	licenca.data_validade,
            	licenca.numero,
            	1 AS tipo_dispensa
        FROM licenciamento.licenca
    )

 	SELECT 	row_number() OVER () AS id_sequencial,
    		licencas_emitidas.id,
    		licencas_emitidas.id_documento,
   			licencas_emitidas.id_caracterizacao,
    		licencas_emitidas.data_cadastro,
    		licencas_emitidas.informacao_adicional,
    		licencas_emitidas.data_validade,
    		licencas_emitidas.numero,
    		licencas_emitidas.tipo_dispensa
   	FROM licencas_emitidas;

GRANT SELECT ON TABLE licenciamento.licenca_emitida TO licenciamento_ap;

COMMENT ON VIEW licenciamento.licenca_emitida IS 'View responsável por fazer a união das tabelas "licenciamento.dispensa_licenciamento" e "licenciamento.licenca" para que possa ser utilizada na consulta de licenças emitidas.';
COMMENT ON COLUMN licenciamento.licenca_emitida.tipo_dispensa IS 'Indica o tipo de licenca emitida (0 - Dispensa; 1-Simplificado);';

--8
ALTER TABLE licenciamento.licenca DROP COLUMN id_licenca_analise;


--3
REVOKE SELECT ON licenciamento.municipio FROM portal_seguranca_licenciamento_ap;
REVOKE SELECT ON licenciamento.estado FROM portal_seguranca_licenciamento_ap;

--1
REVOKE USAGE ON SCHEMA licenciamento FROM portal_seguranca_licenciamento_ap;

REVOKE SELECT, INSERT, UPDATE, DELETE FROM  licenciamento.endereco TO portal_seguranca_licenciamento_ap;
REVOKE SELECT, INSERT, UPDATE, DELETE FROM  licenciamento.endereco_pessoa TO portal_seguranca_licenciamento_ap;
REVOKE SELECT, INSERT, UPDATE, DELETE FROM  licenciamento.pessoa TO portal_seguranca_licenciamento_ap;
REVOKE SELECT, INSERT, UPDATE, DELETE FROM  licenciamento.pessoa_fisica TO portal_seguranca_licenciamento_ap;
REVOKE SELECT, INSERT, UPDATE, DELETE FROM  licenciamento.pessoa_juridica TO portal_seguranca_licenciamento_ap;
REVOKE SELECT, INSERT, UPDATE, DELETE FROM licenciamento.contato TO portal_seguranca_licenciamento_ap;
REVOKE SELECT, INSERT, UPDATE, DELETE FROM licenciamento.estado_civil TO portal_seguranca_licenciamento_ap;
REVOKE SELECT, USAGE ON ALL SEQUENCES IN SCHEMA licenciamento  FROM portal_seguranca_licenciamento_ap;