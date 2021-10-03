# --- !Ups

ALTER TABLE licenciamento.historico_configurador
    ADD CONSTRAINT fk_historico_usuario FOREIGN KEY (id_usuario_licenciamento)
    REFERENCES licenciamento.usuario_licenciamento (id);


# --- !Downs

ALTER TABLE  licenciamento.historico_configurador
    DROP CONSTRAINT fk_historico_usuario;
