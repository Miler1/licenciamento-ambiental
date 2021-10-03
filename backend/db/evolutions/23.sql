# --- !Ups

UPDATE licenciamento.funcionalidade_configurador SET tipo = 'CNAES_DISPENSAVEIS' where id = 11;

ALTER TABLE licenciamento.historico_configurador
    ADD CONSTRAINT pk_historico_configurador PRIMARY KEY (id);

ALTER TABLE licenciamento.funcionalidade_configurador
    ADD CONSTRAINT pk_funcionalidade_configurador PRIMARY KEY (id);

ALTER TABLE licenciamento.acao_configurador
    ADD CONSTRAINT pk_acao_configurador PRIMARY KEY (id);

ALTER TABLE licenciamento.historico_configurador
    ADD CONSTRAINT fk_hc_funcionalidade_configurador FOREIGN KEY (id_funcionalidade_configurador)
	REFERENCES licenciamento.funcionalidade_configurador(id);

ALTER TABLE licenciamento.historico_configurador
    ADD CONSTRAINT fk_hc_acao_configurador FOREIGN KEY (id_acao_configurador)
	REFERENCES licenciamento.acao_configurador(id);

# --- !Downs

UPDATE licenciamento.funcionalidade_configurador SET tipo = 'CNAES_DIPONSAVEIS' where id = 11;

ALTER TABLE licenciamento.historico_configurador
    DROP CONSTRAINT pk_historico_configurador;

ALTER TABLE licenciamento.funcionalidade_configurador
    DROP CONSTRAINT pk_funcionalidade_configurador;

ALTER TABLE licenciamento.acao_configurador
    DROP CONSTRAINT pk_acao_configurador;

ALTER TABLE licenciamento.historico_configurador
    DROP CONSTRAINT fk_hc_funcionalidade_configurador;

ALTER TABLE licenciamento.historico_configurador
    DROP CONSTRAINT fk_hc_acao_configurador;

