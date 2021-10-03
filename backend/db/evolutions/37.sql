# --- !Ups

UPDATE licenciamento.atividade SET sigla_setor = 'CCAP' WHERE sigla_setor = 'GCAP';
UPDATE licenciamento.atividade SET sigla_setor = 'CFAU' WHERE sigla_setor = 'GFAU';
UPDATE licenciamento.atividade SET sigla_setor = 'CERM' WHERE sigla_setor = 'GERM';
UPDATE licenciamento.atividade SET sigla_setor = 'CECF' WHERE sigla_setor = 'GECF';
UPDATE licenciamento.atividade SET sigla_setor = 'CELI' WHERE sigla_setor = 'GELI';
UPDATE licenciamento.atividade SET sigla_setor = 'CECP' WHERE sigla_setor = 'GECP';
UPDATE licenciamento.atividade SET sigla_setor = 'CERH' WHERE sigla_setor = 'GERH';

# --- !Downs

UPDATE licenciamento.atividade SET sigla_setor = 'GCAP' WHERE sigla_setor = 'CCAP';
UPDATE licenciamento.atividade SET sigla_setor = 'GFAU' WHERE sigla_setor = 'CFAU';
UPDATE licenciamento.atividade SET sigla_setor = 'GERM' WHERE sigla_setor = 'CERM';
UPDATE licenciamento.atividade SET sigla_setor = 'GECF' WHERE sigla_setor = 'CECF';
UPDATE licenciamento.atividade SET sigla_setor = 'GELI' WHERE sigla_setor = 'CELI';
UPDATE licenciamento.atividade SET sigla_setor = 'GECP' WHERE sigla_setor = 'CECP';
UPDATE licenciamento.atividade SET sigla_setor = 'GERH' WHERE sigla_setor = 'CERH';