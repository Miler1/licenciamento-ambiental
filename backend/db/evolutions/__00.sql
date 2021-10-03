# --- !Ups
CREATE DATABASE licenciamento_ap
  WITH TEMPLATE template1;

-- executar daqui em diante de dentro do banco licenciamento_ap
CREATE ROLE licenciamento_ap LOGIN
  ENCRYPTED PASSWORD 'licenciamento_ap'
  SUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;


CREATE SCHEMA licenciamento;
CREATE SCHEMA correios;

ALTER SCHEMA licenciamento OWNER TO postgres;
ALTER SCHEMA correios OWNER TO postgres;

GRANT USAGE ON SCHEMA public TO licenciamento_ap;
GRANT USAGE ON SCHEMA licenciamento TO licenciamento_ap;
GRANT USAGE ON SCHEMA correios TO licenciamento_ap;

ALTER DEFAULT PRIVILEGES FOR USER licenciamento_ap IN SCHEMA licenciamento, correios
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO licenciamento_ap;

ALTER DEFAULT PRIVILEGES FOR USER licenciamento_ap IN SCHEMA licenciamento, correios
    GRANT SELECT, USAGE ON SEQUENCES TO licenciamento_ap;

GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA licenciamento TO licenciamento_ap;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA licenciamento TO licenciamento_ap;

# --- !Downs

DROP SCHEMA licenciamento CASCADE;
DROP SCHEMA correios CASCADE;

DROP ROLE licenciamento_ap;

DROP DATABASE licenciamento_ap;
  