# --- !Ups

ALTER TABLE licenciamento.caracterizacao DROP COLUMN id_cadastrante;

# --- !Downs

ALTER TABLE licenciamento.caracterizacao ADD COLUMN id_cadastrante integer;
COMMENT ON COLUMN licenciamento.caracterizacao.id_cadastrante IS 'Identificador do cadastrante da caracterização';
