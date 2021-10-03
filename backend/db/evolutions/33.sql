# --- !Ups

ALTER TABLE licenciamento.taxa_licenciamento DROP CONSTRAINT uq_taxa_licenciamento; 
ALTER TABLE licenciamento.taxa_licenciamento ADD COLUMN ativo boolean DEFAULT true NOT NULL;

# --- !Downs

ALTER TABLE licenciamento.taxa_licenciamento ADD CONSTRAINT uq_taxa_licenciamento UNIQUE (id_porte_empreendimento, id_potencial_poluidor, id_tipo_licenca, codigo );
ALTER TABLE licenciamento.taxa_licenciamento DROP COLUMN ativo;
