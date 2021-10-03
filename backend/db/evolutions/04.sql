# --- !Ups

ALTER TABLE licenciamento.estado_civil ADD COLUMN codigo int;
COMMENT ON COLUMN licenciamento.estado_civil.codigo IS 'Identificador do estado civil';


UPDATE licenciamento.estado_civil SET codigo = 0 WHERE nome ILIKE '%Solteiro(a)%';
UPDATE licenciamento.estado_civil SET codigo = 1 WHERE nome ILIKE '%Casado(a)%';
UPDATE licenciamento.estado_civil SET codigo = 2 WHERE nome ILIKE '%Divorciado(a)%';
UPDATE licenciamento.estado_civil SET codigo = 3 WHERE nome ILIKE '%Viúvo(a)%';
UPDATE licenciamento.estado_civil SET codigo = 4 WHERE nome ILIKE '%Separado%';
UPDATE licenciamento.estado_civil SET codigo = 5 WHERE nome ILIKE '%União Estável%';

# --- !Downs

ALTER TABLE licenciamento.estado_civil DROP COLUMN codigo;
