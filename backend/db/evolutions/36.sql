# --- !Ups

UPDATE licenciamento.tipo_licenca
SET nome = 'DDLA - Declaração de Dispensa de Licenciamento Ambiental', sigla = 'DDLA'
WHERE id = 1 and sigla = 'DI';

# --- !Downs

UPDATE licenciamento.tipo_licenca
SET nome = 'Declaração de Dispensa de Licenciamento Ambiental', sigla = 'DDLA'
WHERE id = 1 and sigla = 'DI';

