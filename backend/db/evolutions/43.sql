# --- !Ups
--Alterar coordenadorias de atividades licenciáveis para Coordenadoria de Licenciamento Ambiental(CLA)
UPDATE licenciamento.atividade SET sigla_setor = 'CLA' WHERE sigla_setor IN ('CERM', 'CELI', 'CECP', 'CCAP', 'CERH', 'CECF', 'CFAU', 'CERM', 'GCAP, GELI, GERM, GERH, GECP, GECF');

# --- !Downs

-- Fazer backup da tabela 
SELECT * FROM licenciamento.atividade
WHERE sigla_setor IN ('CERM', 'CELI', 'CECP', 'CCAP', 'CERH', 'CECF', 'CFAU', 'CERM', 'GCAP, GELI, GERM, GERH, GECP, GECF');
