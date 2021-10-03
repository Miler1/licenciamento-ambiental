# --- !Ups

ALTER TABLE licenciamento.historico_configurador ADD COLUMN id_item_antigo INTEGER; 
COMMENT ON COLUMN licenciamento.historico_configurador.id_item_antigo IS 'Identificador único do item editado que deixou de ser item atual';

ALTER TABLE licenciamento.atividade ADD COLUMN item_antigo BOOLEAN DEFAULT false NOT NULL; 
COMMENT ON COLUMN licenciamento.atividade.item_antigo IS 'Campo que identifica se a atividade é um item editado que tornou-se uma cópia do item atual';


# --- !Downs

ALTER TABLE licenciamento.historico_configurador DROP COLUMN id_item_antigo; 
ALTER TABLE licenciamento.atividade DROP COLUMN item_antigo; 
