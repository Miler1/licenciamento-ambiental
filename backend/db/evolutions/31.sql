# --- !Ups
--174.sql
-- alterar nome de atividades
UPDATE licenciamento.atividade
SET nome = 'Viveiro escavado, tanque, reservatório, laboratório de reprodução induzida de organismos aquáticos e viveiro de barragem, todos com área inundada total até 5 ha, sistema com fluxo contínuo até 500 m³ desde que não seja resultante de áreas de exploração mineral na forma de Plano de Recuperação de Área Degradada - PRAD e aquário até 1.000 m³ (LAU)'
WHERE nome = 'Viveiro escavado, tanque, reservatório, laboratório de reprodução induzida de organismos   aquáticos e viveiro de barragem, todos com área inundada total até 5 ha, sistema com fluxo contínuo até 500 m³  desde que não seja resultante de áreas de exploração mineral na forma de Plano de Recuperação de Área Degradada - PRAD e aquário até 1.000 m³';

UPDATE licenciamento.atividade
SET nome = 'Pequena Central Hidrelétrica - PCH'
WHERE nome = 'Pequena Central Hidrelétrica';

--sobrescrever o m³
UPDATE licenciamento.atividade
SET nome ='Sistema com fluxo contínuo (até 500 m³) (CADASTRO)'
WHERE nome ='Sistema com fluxo contínuo (até 500 m3) (CADASTRO)';

UPDATE licenciamento.atividade
SET nome ='Sistema com fluxo contínuo (acima de 500 m³) (LICENÇA)'
WHERE nome ='Sistema com fluxo contínuo (acima de 500 m3) (LICENÇA)';

--  REQ-26A não pode ter RLI
DELETE FROM licenciamento.rel_tipo_licenca_grupo_documento where
id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-26-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLI');


-- remover atividades são somente cnae
UPDATE licenciamento.atividade SET ativo=false, v1=false where nome = 'Atividades veterinárias';
UPDATE licenciamento.atividade SET ativo=false, v1=false where nome = 'Laboratório de análise veterinária';
UPDATE licenciamento.atividade SET ativo=false, v1=false where nome = 'Coleta de materiais biológicos de qualquer espécie para uso veterinário';
UPDATE licenciamento.atividade SET ativo=false, v1=false where nome = 'Clínica, consultório ou hospital veterinário';
UPDATE licenciamento.atividade SET ativo=false, v1=false where nome = 'Banco de sangue e de órgãos de animais';
UPDATE licenciamento.atividade SET ativo=false, v1=false where nome = 'Atividades veterinárias';

-- atividades não apareciam no portal, faltando grupo documento
UPDATE licenciamento.atividade 
SET id_grupo_documento = (select id from licenciamento.grupo_documento where codigo='REQ-04') 
WHERE nome = 'Lavra Garimpeira (leito de rio)';

UPDATE licenciamento.atividade 
SET id_grupo_documento = (select id from licenciamento.grupo_documento where codigo='REQ-17') 
WHERE nome = 'Agroindústrias de pequeno porte e baixo potencial de impacto ambiental (Resolução CONAMA  nº 385/2006)';

UPDATE licenciamento.atividade 
SET id_grupo_documento = (select id from licenciamento.grupo_documento where codigo='REQ-19') 
WHERE nome = 'Extração de óleo oriundo do extrativismo vegetal de pequeno porte e baixo potencial de impacto ambiental (Resolução CONAMA nº 385/2006)';

-- 175.sql
UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-01-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LP')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Concessão de direito real de uso – CDRU, se for o caso.');

UPDATE licenciamento.tipo_documento 
SET nome = 'Caracterização hidrogeológica com definição do sentido de fluxo das águas subterrâneas, identificação das áreas de recarga, localização do poço, se for o caso'
WHERE nome = 'Caracterização hidrogeológica com definição do sentido de fluxo das águas subterrâneas identificação das áreas de recarga, localização do poço, se for o caso';

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-30')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificados de Inspeção para o Transporte de Produtos Perigosos (CIPP) expedido pela INMETRO/IPEM-AM ou entidade por ele credenciada, se for transportar a granel');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-30')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado da Comissão de Energia Nuclear, se material radioativo');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-30')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado da Policia Federal, produtos controlados pela Divisão de Repressão e Entorpecentes');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-31')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificados de Inspeção para o Transporte de Produtos Perigosos (CIPP) expedido pela INMETRO/IPEM-AM ou entidade por ele credenciada, se for transportar a granel');

INSERT INTO licenciamento.rel_tipo_licenca_grupo_documento(id_grupo_documento, id_tipo_licenca, id_tipo_documento, obrigatorio, ordem) VALUES
((select id from licenciamento.grupo_documento where codigo = 'REQ-26-A'),
 (select id from licenciamento.tipo_licenca where sigla = 'RLAU'),
 (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica – ART do responsável pela elaboração do Memorial Descritivo da atividade'),
 true, 2);

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-31')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado da Comissão de Energia Nuclear, se material radioativo');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-31')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado da Policia Federal, produtos controlados pela Divisão de Repressão e Entorpecentes');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-31')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificados de Inspeção para o Transporte de Produtos Perigosos (CIPP) expedido pela INMETRO/IPEM-AM ou entidade por ele credenciada, se for transportar a granel');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-34')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LI')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado da Comissão de Energia Nuclear, se material radioativo');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-34')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LI')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado da Policia Federal, produtos controlados pela Divisão de Repressão e Entorpecentes');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-45')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificados de Inspeção para o Transporte de Produtos Perigosos (CIPP) expedido pela INMETRO/IPEM-AM ou entidade por ele credenciada, se for transportar a granel');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-45')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado da Comissão de Energia Nuclear, se material radioativo');

UPDATE licenciamento.tipo_documento 
SET nome = 'Termo de Responsabilidade, para embarcação com Arqueação Bruta, menor que 20 toneladas'
WHERE nome = 'Termo de Responsabilidade, para embarcação com Arqueação Bruta, menor que 20';

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-49')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificados de Inspeção para o Transporte de Produtos Perigosos (CIPP) expedido pela INMETRO/IPEM-AM ou entidade por ele credenciada, se for transportar a granel');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-54')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'CA')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Comprovante de origem dos organismos aquáticos, quando a atividade de aquicultura estiver sendo exercida');

UPDATE licenciamento.tipo_documento 
SET nome = 'Plano de Monitoramento Ambiental – PMA, para empreendimentos com atividades desenvolvidas em tanque-rede/gaiola com volume útil acima de 1.000 m³ até 5.000 m³'
WHERE nome = 'Plano de Monitoramento Ambiental – PMA, para empreendimentos com atividades desenvolvidas em tanque-rede/gaiola com volume útil acima de 1.000 m³';

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-54-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Plano de Monitoramento Ambiental – PMA, para empreendimentos com atividades desenvolvidas em tanque-rede/gaiola com volume útil acima de 1.000 m³ até 5.000 m³');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-54-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica - ART do profissional responsável pela elaboração do Plano de Monitoramento Ambiental – PMA ');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-54-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Plano de Controle Ambiental – PCA, para empreendimentos com atividades desenvolvidas em tanque-rede/gaiola com volume útil acima de 5.000 m³ até 10.000 m³');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-54-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica - ART do profissional responsável pela elaboração do Plano de Controle Ambiental – PCA');

INSERT INTO licenciamento.rel_tipo_licenca_grupo_documento(id_grupo_documento, id_tipo_licenca, id_tipo_documento, obrigatorio, ordem) VALUES
((select id from licenciamento.grupo_documento where codigo = 'REQ-58'),
 (select id from licenciamento.tipo_licenca where sigla = 'RLO'),
 (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica – ART do responsável pela elaboração do Memorial Descritivo da atividade'),
 true, 2);

INSERT INTO licenciamento.rel_tipo_licenca_grupo_documento(id_grupo_documento, id_tipo_licenca, id_tipo_documento, obrigatorio, ordem) VALUES
((select id from licenciamento.grupo_documento where codigo = 'REQ-61'),
 (select id from licenciamento.tipo_licenca where sigla = 'LAU'),
 (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica – ART do responsável pela elaboração do Projeto Técnico'),
 true, 2);

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET ordem = 3 
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-61')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LAU')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Responsável técnico pelo projeto e empreendimento');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET ordem = 4 
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-61')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LAU')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Programa de Gerenciamento de Resíduos da Construção Civil (PGRCC),');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET ordem = 5 
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-61')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LAU')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica – ART do responsável pela elaboração do Programa de Gerenciamento de Resíduos da Construção Civil (PGRCC)');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET ordem = 6 
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-61')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LAU')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Projeto de Terraplenagem, se houver');

INSERT INTO licenciamento.rel_tipo_licenca_grupo_documento(id_grupo_documento, id_tipo_licenca, id_tipo_documento, obrigatorio, ordem) VALUES
((select id from licenciamento.grupo_documento where codigo = 'REQ-61'),
 (select id from licenciamento.tipo_licenca where sigla = 'LAU'),
 (select max(id) from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica – ART do responsável pela elaboração do Projeto de Terraplenagem, se houver'),
 false, 7);

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET ordem = 8 
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-61')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LAU')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Projeto do Sistema de Tratamento de Esgoto Doméstico/sanitário, aprovado pela companhia de água e esgoto, se houver');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET ordem = 9 
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-61')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LAU')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica – ART do do responsável pela elaboração do Projeto do Sistema de Tratamento de Esgoto Doméstico/sanitário, se houver');

INSERT INTO licenciamento.rel_tipo_licenca_grupo_documento(id_grupo_documento, id_tipo_licenca, id_tipo_documento, obrigatorio, ordem) VALUES
((select id from licenciamento.grupo_documento where codigo = 'REQ-64'),
 (select id from licenciamento.tipo_licenca where sigla = 'LI'),
 (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica do responsável pela elaboração do Projeto de Terraplenagem, se houver'),
 false, 12);

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET ordem = 13 
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-64')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LI')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Estudos ambientais, conforme peculiaridade do projeto, características ambientais da área e porte do empreendimento (Decreto nº 10.028/87 e Resolução CONAMA nº 237/97)');

----

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = true
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-02-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LP')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica do responsável pela elaboração do Memorial Descritivo da atividade');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = true
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-02-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LI')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Plano de Gerenciamento de Resíduos, conforme Termo de Referência IPAAM');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-02-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LI')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado de Registro expedido pelo Ministério da Defesa/Exército Brasileiro, caso haja uso de explosivos');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = true
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-02-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Plano de Recuperação de Área Degradada (PRAD), conforme Termo de Referência IPAAM');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-02-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado de Registro expedido pelo Ministério da Defesa/Exército Brasileiro, caso haja uso de explosivos');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = true
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-02-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica do responsável pela execução da lavra');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-02-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado de Registro expedido pelo Ministério da Defesa/Exército Brasileiro, caso haja uso de explosivos');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-02-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LP')
AND	id_tipo_documento = (select max(id) from licenciamento.tipo_documento where nome = 'Estudos ambientais, conforme peculiaridade do projeto, características ambientais da área e porte do empreendimento (Decreto nº 10.028/87 e Resolução CONAMA nº 237/97)');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = true
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-03')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LP')
AND	id_tipo_documento = (select max(id) from licenciamento.tipo_documento where nome = 'Minuta do título de lavra ou Declaração de dispensa de título minerário (se for o caso) expedido pela Agência Nacional de Mineração – ANM');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-03')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LI')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Projeto de Drenagem de águas pluviais (superficial e profundo)');


UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = true
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-03')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica do responsável pela elaboração do Plano de Desmobilização do Empreendimento e Gerenciamento de Resíduos');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = true
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-03')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Plano de Recuperação de Área Degradada (PRAD) ou Proposta de Uso Alternativo, conforme Termo de Referência IPAAM');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-03')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado de Registro expedido pelo Ministério da Defesa/Exército Brasileiro, caso haja uso de explosivos');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = true
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-03')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica pela execução da lavra');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-03')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado de Registro expedido pelo Ministério da Defesa/Exército Brasileiro, caso haja uso de explosivos');

UPDATE licenciamento.tipo_documento 
SET nome = 'Projeto de Terraplenagem'
WHERE nome = 'Projeto de Terraplenagem, caso haja necessidade, com respectiva Anotação de Responsabilidade Técnica do responsável pela elaboração do projeto';

UPDATE licenciamento.tipo_documento 
SET nome = 'Anotação de Responsabilidade Técnica do responsável pela execução da atividade ou processo produtivo'
WHERE nome = 'Anotação de Responsabilidade Técnica do responsável pelo empreendimento ou processo produtivo';

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = true
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-15')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LI')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica do responsável pela elaboração do Projeto Técnico');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = true
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-20-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LI')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Plano de Desmobilização do Empreendimento e/ou Gerenciamento de Resíduos, conforme Termo de Referência IPAAM');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = true
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-20-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LI')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Plano de Desmobilização do Empreendimento e/ou Gerenciamento de Resíduos, conforme Termo de Referência IPAAM');

UPDATE licenciamento.tipo_documento 
SET nome = 'Anotação de Responsabilidade Técnica – ART do responsável pela elaboração do PGRSL'
WHERE nome = 'Anotação de Responsabilidade Técnica – ART do PGRSL';

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-25')
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-26')
and id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = true
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-32')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LI')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Programa de Gerenciamento de Resíduos da Construção Civil (PGRCC), conforme Termo de Referência IPAAM');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = true
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-38')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LI')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Programa de Gerenciamento de Resíduos da Construção Civil (PGRCC) , conforme Termo de Referência IPAAM');

UPDATE licenciamento.tipo_documento 
SET nome = 'Anotação de Responsabilidade Técnica – ART do responsável pela elaboração do PCA'
WHERE nome = 'Anotação de Responsabilidade Técnica – ART do PCA';

DELETE FROM licenciamento.rel_tipo_licenca_grupo_documento
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-43')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLAU')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Cadastro da atividade, conforme modelo IPAAM');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET ordem= 7
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-46')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Termo de Responsabilidade, para embarcação com Arqueação Bruta, menor que 20 toneladas');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-46')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Termo de Responsabilidade, para embarcação com Arqueação Bruta, menor que 20 toneladas');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET ordem= 8
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-46')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado da Comissão de Energia Nuclear, se material radioativo');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-46')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado da Comissão de Energia Nuclear, se material radioativo');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET ordem= 9
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-46')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado de Registro do Ministério do Exército, se explosivo, insumo de explosivo');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-46')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificado de Registro do Ministério do Exército, se explosivo, insumo de explosivo');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET ordem= 6
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-46')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica – ART do responsável pela elaboração do Programa de Gerenciamento de Resíduos Sólidos e Líquidos (PGRSL)');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-46')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica – ART do responsável pela elaboração do Programa de Gerenciamento de Resíduos Sólidos e Líquidos (PGRSL)');

UPDATE licenciamento.tipo_documento 
SET nome = 'Relatório de Monitoramento Ambiental – RMA anual do empreendimento - Termo de Referência IPAAM, para empreendimentos que apresentam PCA, em tanque-rede/gaiola com volume útil acima de 5.000 m³ até 10.000 m³'
WHERE nome = 'Relatório de Monitoramento Ambiental – RMA anual do empreendimento - Termo de Referência IPAAM, para empreendimentos em viveiros escavados ou barragem com área alagada acima de 10,0 ha; tanque-rede/gaiola com volume útil acima de 1.000 m³, e sistema de fluxo contínuo com volume útil acima de 500 m³. Somente para renovação';

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-54-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLO')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Relatório de Monitoramento Ambiental – RMA anual do empreendimento - Termo de Referência IPAAM, para empreendimentos que apresentam PCA, em tanque-rede/gaiola com volume útil acima de 5.000 m³ até 10.000 m³');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-54-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLO')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica - ART do profissional/equipe elaborador do RMA, habilitado e cadastrado no IPAAM');

----
UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = true
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-03')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LI')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Projeto de Drenagem de águas pluviais (superficial e profundo)');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-03')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LI')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Projeto de Terraplenagem');

DELETE FROM licenciamento.rel_tipo_licenca_grupo_documento
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-43')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLAU')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Apresentar descrição do cumprimento das restrições/condicionantes constantes no verso da licença anterior');

DELETE FROM licenciamento.rel_tipo_licenca_grupo_documento
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-43')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLAU')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Restrições/condicionantes da licença anterior');

DELETE FROM licenciamento.rel_tipo_licenca_grupo_documento
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-43')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLAU')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Licença anterior');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-54-A')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica - ART do profissional responsável pela elaboração do Plano de Monitoramento Ambiental – PMA');

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = false
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-34')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LI')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Certificados de Inspeção para o Transporte de Produtos Perigosos (CIPP) expedido pela INMETRO/IPEM-AM ou entidade por ele credenciada, se for transportar a granel');

---
UPDATE licenciamento.atividade
SET v1 = true, ativo = true
WHERE codigo = '0707';

UPDATE licenciamento.atividade
SET v1 = true
WHERE codigo = '3605';

-- 176.sql
INSERT INTO licenciamento.atividade (nome, id_tipologia, geo_linha, geo_ponto, geo_poligono, codigo, licenciamento_municipal, limite_parametro_municipal, limite_inferior_simplificado, limite_superior_simplificado, id_potencial_poluidor, observacoes, sigla_setor, ativo, dentro_empreendimento, id_grupo_documento, dentro_municipio, v1) VALUES
    ('Supressão Vegetal', (select id from licenciamento.tipologia where nome = 'Outros Serviços'), false, false, true, '9990', null, null, null, null, null, null, 'GCAP, GELI, GERM, GERH, GECP, GECF', true, true, (select id from licenciamento.grupo_documento where codigo = 'REQ-63'), false, true);

INSERT INTO licenciamento.rel_atividade_tipo_licenca (id_atividade, id_tipo_licenca) VALUES
((select id from licenciamento.atividade where nome = 'Supressão Vegetal'), (select id from licenciamento.tipo_licenca where sigla = 'LAU-SV'));

INSERT INTO licenciamento.rel_atividade_parametro_atividade (id_atividade, id_parametro_atividade) VALUES
((select id from licenciamento.atividade where nome = 'Supressão Vegetal'), (select id from licenciamento.parametro_atividade where codigo = 'NH'));

-- 177.sql 
-- inserindo documentos
INSERT INTO licenciamento.tipo_documento (nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo, codigo, tipo_analise) VALUES 
('Declaração para renovação de LO (Modelo IPAAM)', null, '', '', null, null);
INSERT INTO licenciamento.tipo_documento (nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo, codigo, tipo_analise) VALUES 
('Declaração de quantidade de produtos que pretende comercializar em 1 ano,  (modelo IPAAM), se atividade for comercialização', null, '', '', null, null);
INSERT INTO licenciamento.tipo_documento (nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo, codigo, tipo_analise) VALUES 
('Autorização Prévia para Manejo de Fauna obtida com o Cadastro Técnico Federal – CTF/IBAMA (www.ibama.gov.br/cadastros/ctf/ctf-app). Cadastrar-se como utilizador de Recursos Naturais (Código 20) e Atividade de Criação e exploração econômica de fauna silvestre nativa e fauna exótica - Comercialização de Partes Produtos e Subprodutos (Código 24) dentro do Sistema Nacional de Gestão da Fauna SISFAUNA, se atividade for comercialização', null, '', '', null, null);
INSERT INTO licenciamento.tipo_documento (nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo, codigo, tipo_analise) VALUES 
('Declaração para renovação de LAU (Modelo IPAAM)', null, '', '', null, null);

--inserindo na rel - acrescentar
INSERT INTO licenciamento.rel_tipo_licenca_grupo_documento(id_grupo_documento, id_tipo_licenca, id_tipo_documento, obrigatorio, ordem) VALUES
((select id from licenciamento.grupo_documento where codigo = 'REQ-15'),
 (select id from licenciamento.tipo_licenca where sigla = 'RLO'),
 (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica do responsável pela elaboração do Relatório de Controle Ambiental das atividades desenvolvidas na LO'),
 true, 4);

INSERT INTO licenciamento.rel_tipo_licenca_grupo_documento(id_grupo_documento, id_tipo_licenca, id_tipo_documento, obrigatorio, ordem) VALUES
((select id from licenciamento.grupo_documento where codigo = 'REQ-58'),
 (select id from licenciamento.tipo_licenca where sigla = 'LO'),
 (select id from licenciamento.tipo_documento where nome = 'Declaração de quantidade de produtos que pretende comercializar em 1 ano,  (modelo IPAAM), se atividade for comercialização'),
 false, 5);

INSERT INTO licenciamento.rel_tipo_licenca_grupo_documento(id_grupo_documento, id_tipo_licenca, id_tipo_documento, obrigatorio, ordem) VALUES
((select id from licenciamento.grupo_documento where codigo = 'REQ-58'),
 (select id from licenciamento.tipo_licenca where sigla = 'LP'),
 (select id from licenciamento.tipo_documento where nome = 'Autorização Prévia para Manejo de Fauna obtida com o Cadastro Técnico Federal – CTF/IBAMA (www.ibama.gov.br/cadastros/ctf/ctf-app). Cadastrar-se como utilizador de Recursos Naturais (Código 20) e Atividade de Criação e exploração econômica de fauna silvestre nativa e fauna exótica - Comercialização de Partes Produtos e Subprodutos (Código 24) dentro do Sistema Nacional de Gestão da Fauna SISFAUNA, se atividade for comercialização'),
 false, 3);

-- alterando documento , alterar descrição
UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Declaração para renovação de LO (Modelo IPAAM)')
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-57')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Memorial Descritivo da atividade, conforme Termo de referência IPAAM')
AND ordem = 1;

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Declaração para renovação de LO (Modelo IPAAM)')
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-58')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLO')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Memorial Descritivo da atividade, conforme Termo de referência IPAAM')
AND ordem = 1;

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Declaração para renovação de LAU (Modelo IPAAM)')
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-59')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLAU')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Cadastro Técnico Federal – CTF/IBAMA (www.ibama.gov.br/cadastros/ctf/ctf-app) Cadastrar-se como utilizador de Recursos Naturais (Código 21) e Criação de Passeriformes Silvestres Nativos (Código 60) SISPASS')
AND ordem = 1;

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Declaração para renovação de LAU (Modelo IPAAM)')
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-60')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLAU')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Memorial Descritivo da atividade, conforme Termo de referência IPAAM')
AND ordem = 1;

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Declaração para renovação de LAU (Modelo IPAAM)')
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-61')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLAU')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Memorial Descritivo da atividade, conforme Termo de referência IPAAM')
AND ordem = 1;

UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Declaração para renovação de LAU (Modelo IPAAM)')
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-62')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLAU')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Memorial Descritivo da atividade, conforme Termo de referência IPAAM')
AND ordem = 1;

-- alterar tipo requisicao
UPDATE licenciamento.rel_tipo_licenca_grupo_documento
SET obrigatorio = true
WHERE id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-63')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LAU-SV')
AND	id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Inventário de FAUNA')
AND ordem = 7;

-- remover do requisito
DELETE FROM licenciamento.rel_tipo_licenca_grupo_documento where
id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-57')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLO')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica do responsável pela elaboração do Memorial Descritivo da atividade')
AND ordem = 2;

DELETE FROM licenciamento.rel_tipo_licenca_grupo_documento where
id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-58')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLO')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica – ART do responsável pela elaboração do Memorial Descritivo da atividade')
AND ordem = 2;

DELETE FROM licenciamento.rel_tipo_licenca_grupo_documento where
id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-60')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLAU')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica – ART do responsável pela elaboração do Memorial Descritivo da atividade')
AND ordem = 2;

DELETE FROM licenciamento.rel_tipo_licenca_grupo_documento where
id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-61')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLAU')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica – ART do responsável pela elaboração do Memorial Descritivo da atividade')
AND ordem = 2;

DELETE FROM licenciamento.rel_tipo_licenca_grupo_documento where
id_grupo_documento = (select id from licenciamento.grupo_documento where codigo = 'REQ-62')
AND id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'RLAU')
AND id_tipo_documento = (select id from licenciamento.tipo_documento where nome = 'Anotação de Responsabilidade Técnica - ART do responsável pela elaboração do Memorial Descritivo da atividade')
AND ordem = 2;

-- 178.sql

UPDATE licenciamento.atividade SET sigla_setor = 'GELI' WHERE codigo = '3217';

INSERT INTO licenciamento.parametro_atividade (nome, codigo, casas_decimais) VALUES
('Capacidade de Tratamento de Esgoto Sanitário', 'CTES', 4);

INSERT INTO licenciamento.rel_atividade_parametro_atividade (id_atividade, id_parametro_atividade, descricao_unidade) VALUES 
((select id from licenciamento.atividade where codigo='3217'), 
 (select id from licenciamento.parametro_atividade where codigo= 'CTES'), 
 'Capacidade de Tratamento de Esgoto Sanitário');

UPDATE licenciamento.porte_atividade 
SET id_parametro_um = (SELECT id FROM licenciamento.parametro_atividade WHERE codigo = 'CTES') 
WHERE codigo = 131;

-- 179.sql

UPDATE licenciamento.orgao 
SET nome = 'Secretaria de Estado do Meio Ambiente', sigla = 'SEMA', email = 'sema@email.com' 
WHERE sigla = 'DEMUC';

INSERT INTO licenciamento.orgao (nome, sigla, email) VALUES
('Secretaria Municipal de Meio Ambiente e Sustentabilidade', 'SEMMAS', 'semmas@email.com');

UPDATE licenciamento.rel_tipo_sobreposicao_orgao 
SET id_orgao = (SELECT id FROM licenciamento.orgao WHERE sigla = 'SEMMAS')
WHERE id_tipo_sobreposicao = (SELECT id FROM licenciamento.tipo_sobreposicao WHERE codigo = 'UC_MUNICIPAL');


# --- !Downs

-- 179.sql
UPDATE licenciamento.rel_tipo_sobreposicao_orgao 
SET id_orgao = (SELECT id FROM licenciamento.orgao WHERE sigla = 'DEMUC')
WHERE id_tipo_sobreposicao = (SELECT id FROM licenciamento.tipo_sobreposicao WHERE codigo = 'UC_MUNICIPAL');

DELETE FROM licenciamento.orgao WHERE sigla = 'SEMMAS';

UPDATE licenciamento.orgao 
SET nome = 'Departamento Mudanças Climáticas e gestão de UC', sigla = 'DEMUC', email = 'sema-demuc@email.com' 
WHERE sigla = 'SEMA';


-- 178.sql
UPDATE licenciamento.porte_atividade 
SET id_parametro_um = (SELECT id FROM licenciamento.parametro_atividade WHERE codigo = 'M3') 
WHERE codigo = 131;

DELETE FROM licenciamento.rel_atividade_parametro_atividade 
WHERE id_atividade = (select id from licenciamento.atividade where codigo='3217') AND
id_parametro_atividade =  (select id from licenciamento.parametro_atividade where codigo= 'CTES'); 

DELETE FROM licenciamento.parametro_atividade WHERE codigo = 'CTES';

UPDATE licenciamento.atividade SET sigla_setor = 'GERH' WHERE codigo = '3217';

-- 177.sql
-- fazer backup da tabela licenciamento.rel_tipo_licenca_grupo_documento

-- 176.sql
DELETE FROM licenciamento.rel_atividade_parametro_atividade WHERE
id_atividade =(select id from licenciamento.atividade where nome = 'Supressão Vegetal') AND 
id_parametro_atividade = (select id from licenciamento.parametro_atividade where codigo = 'NH');

DELETE FROM licenciamento.rel_atividade_tipo_licenca WHERE 
id_atividade = (select id from licenciamento.atividade where nome = 'Supressão Vegetal') AND 
id_tipo_licenca = (select id from licenciamento.tipo_licenca where sigla = 'LAU-SV');

DELETE FROM licenciamento.atividade WHERE nome = 'Supressão Vegetal';


-- 175.sql
-- fazer backup da tabela licenciamento.tipo_documento e licenciamento.rel_tipo_licenca_grupo_documento

-- 174.sql
-- atividades não apareciam no portal, faltando grupo documento
UPDATE licenciamento.atividade 
SET id_grupo_documento = NULL
WHERE nome = 'Lavra Garimpeira (leito de rio)';

UPDATE licenciamento.atividade 
SET id_grupo_documento = NULL
WHERE nome = 'Agroindústrias de pequeno porte e baixo potencial de impacto ambiental (Resolução CONAMA  nº 385/2006)';

UPDATE licenciamento.atividade 
SET id_grupo_documento = NULL
WHERE nome = 'Extração de óleo oriundo do extrativismo vegetal de pequeno porte e baixo potencial de impacto ambiental (Resolução CONAMA nº 385/2006)';


-- remover atividades são somente cnae
UPDATE licenciamento.atividade SET ativo=true, v1=true where nome = 'Atividades veterinárias';
UPDATE licenciamento.atividade SET ativo=true, v1=true where nome = 'Laboratório de análise veterinária';
UPDATE licenciamento.atividade SET ativo=true, v1=true where nome = 'Coleta de materiais biológicos de qualquer espécie para uso veterinário';
UPDATE licenciamento.atividade SET ativo=true, v1=true where nome = 'Clínica, consultório ou hospital veterinário';
UPDATE licenciamento.atividade SET ativo=true, v1=true where nome = 'Banco de sangue e de órgãos de animais';
UPDATE licenciamento.atividade SET ativo=true, v1=true where nome = 'Atividades veterinárias';


-- alterar nome de atividades
UPDATE licenciamento.atividade
SET nome = 'Viveiro escavado, tanque, reservatório, laboratório de reprodução induzida de organismos   aquáticos e viveiro de barragem, todos com área inundada total até 5 ha, sistema com fluxo contínuo até 500 m³  desde que não seja resultante de áreas de exploração mineral na forma de Plano de Recuperação de Área Degradada - PRAD e aquário até 1.000 m³'
WHERE nome = 'Viveiro escavado, tanque, reservatório, laboratório de reprodução induzida de organismos aquáticos e viveiro de barragem, todos com área inundada total até 5 ha, sistema com fluxo contínuo até 500 m³ desde que não seja resultante de áreas de exploração mineral na forma de Plano de Recuperação de Área Degradada - PRAD e aquário até 1.000 m³ (LAU)';

UPDATE licenciamento.atividade
SET nome = 'Pequena Central Hidrelétrica'
WHERE nome = 'Pequena Central Hidrelétrica - PCH';

--  REQ-26A não pode ter RLI
INSERT INTO licenciamento.rel_tipo_licenca_grupo_documento (id_grupo_documento, id_tipo_licenca, id_tipo_documento, obrigatorio, ordem) VALUES 
((select id from licenciamento.grupo_documento where codigo = 'REQ-26-A')
, (select id from licenciamento.tipo_licenca where sigla = 'RLI'), (select id from licenciamento.tipo_documento where nome = 'Apresentar descrição do cumprimento das restrições/condicionantes constantes no verso da licença anterior'), true),
((select id from licenciamento.grupo_documento where codigo = 'REQ-26-A'), (select id from licenciamento.tipo_licenca where sigla = 'RLI'), (select id from licenciamento.tipo_documento where nome = 'Restrições/condicionantes da licença anterior'), true),
((select id from licenciamento.grupo_documento where codigo = 'REQ-26-A'), (select id from licenciamento.tipo_licenca where sigla = 'RLI'), (select id from licenciamento.tipo_documento where nome ='Licença anterior'), true);

--sobrescrever o m³
UPDATE licenciamento.atividade
SET nome ='Sistema com fluxo contínuo (até 500 m3) (CADASTRO)'
WHERE nome ='Sistema com fluxo contínuo (até 500 m³) (CADASTRO)';

UPDATE licenciamento.atividade
SET nome ='Sistema com fluxo contínuo (acima de 500 m3) (LICENÇA)'
WHERE nome ='Sistema com fluxo contínuo (acima de 500 m³) (LICENÇA)';