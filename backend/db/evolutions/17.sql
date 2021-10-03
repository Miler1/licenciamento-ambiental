# --- !Ups

ALTER TABLE licenciamento.resposta ALTER COLUMN id_pergunta DROP NOT NULL;


# --- !Downs

ALTER TABLE licenciamento.resposta ALTER COLUMN id_pergunta SET NOT NULL;



