# --- !Ups

ALTER TABLE licenciamento.historico_configurador RENAME COLUMN "data_cadastro" TO "data_acao";

# --- !Downs

ALTER TABLE licenciamento.historico_configurador RENAME COLUMN "data_acao" TO "data_cadastro";
