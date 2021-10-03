# --- !Ups

ALTER TABLE licenciamento.atividade ADD COLUMN rascunho boolean DEFAULT false NOT NULL;

ALTER TABLE licenciamento.atividade ALTER COLUMN id_tipologia DROP NOT NULL;

ALTER TABLE licenciamento.atividade ALTER COLUMN dentro_empreendimento DROP NOT NULL;

ALTER TABLE licenciamento.porte_atividade ALTER COLUMN id_porte_empreendimento DROP NOT NULL;


# --- !Downs

ALTER TABLE licenciamento.atividade DROP COLUMN rascunho;

ALTER TABLE licenciamento.atividade ALTER COLUMN id_tipologia SET DEFAULT NOT NULL;

ALTER TABLE licenciamento.atividade ALTER COLUMN dentro_empreendimento SET DEFAULT NOT NULL;

ALTER TABLE licenciamento.porte_atividade ALTER COLUMN id_porte_empreendimento SET DEFAULT NOT NULL;
