# --- !Ups
--2
INSERT INTO portal_seguranca.permissao(
codigo, data_cadastro, nome, id_modulo)
VALUES ('ALTERAR_EMPREENDIMENTO', now(),'Alterar Empreendimento', 2);

INSERT INTO portal_seguranca.permissao_perfil(
id_perfil, id_permissao)
VALUES (2,(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ALTERAR_EMPREENDIMENTO'));

-- --4
-- ALTER TABLE licenciamento.atividade_cnae ADD CONSTRAINT fk_ac_setor FOREIGN KEY(id_setor)
-- REFERENCES portal_seguranca.setor(id);


-- --5

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome IN('Cultivo de uva','Cultivo de Açaí','Cultivo de banana','Cultivo de caju','Cultivo de cítricos, exceto laranja','Cultivo de coco-da-baia','cultivo de guaraná','Cultivo de maçã',' Cultivo de mamão','Cultivo de maracujá','Cultivo de manga','Cultivo de pêssego','Cultivo de frutas de lavoura permanente não especificadas anteriormente','Cultivo de café','Cultivo de cacau','Cultivo de chá da índia','Cultivo de erva-mate','Cultivo de pimenta do reino','Cultivo de plantas para condimento, exceto pimenta do reino',' Cultivo de Seringueira','Cultivo de outras plantas de lavoura permanente não especificadas anteriormente','Cultivo de laranja')) 
-- AND id_atividade =(SELECT id FROM licenciamento.atividade where nome ='Cultura de ciclo longo');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome IN('Produção sementes certificadas, exceto de forrageiras para formação de pasto.', 'Produção sementes certificadas, de forrageiras para formação de pasto', 'Cultivo de mudas em viveiros florestais', 'Produção de mudas e outras formas de propagação vegetal, certificadas')) 
-- AND id_atividade =(SELECT id FROM licenciamento.atividade where nome ='Viveiro de mudas');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEPAF') WHERE id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome IN('Cultivo de Eucalipto','Cultivo de acácia-negra','Cultivo de pinus','Cultivo de teca','Cultivo de espécies madeireiras, exceto eucalipto, acácia-negra, pinus e teca')) 
-- AND id_atividade =(SELECT id FROM licenciamento.atividade where nome ='Reflorestamento em área alterada e/ou sub-utilizada');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome IN('Cultivo de milho', 'Cultivo de trigo', 'Cultivo de outros cereais não especificados anteriormente', 'Cultivo de algodão herbáceo', 'Cultivo de juta', 'Cultivo de outras fibras de lavoura temporária não especificadas anteriormente', 'Cultivo de amendoim', 'Cultivo de girassol', 'Cultivo de mamona', 'Cultivo de outras oleaginosas de lavoura temporária não especificadas anteriormente.', 'Cultivo de abacaxi', 'Cultivo de alho', 'Cultivo de batata-inglesa', 'Cultivo de cebola', 'Cultivo de feijão', 'Cultivo de mandioca', 'Cultivo de melão', 'Cultivo de melancia', 'Cultivo de tomate rasteiro', 'Cultivo de outras plantas de lavoura temporária não especificadas anteriormente', 'Horticultura, exceto morango', 'Cultivo de morango', 'Cultivo de flores e plantas ornamentais')) 
-- AND id_atividade =(SELECT id FROM licenciamento.atividade where nome ='Agricultura em área alterada e /ou subutilizada');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome IN('Criação de bovinos','Criação de Bovinos para leite ', 'Criação de bovinos, exceto para corte e leite', 'Criação de Bubalinos')) 
-- AND id_atividade =(SELECT id FROM licenciamento.atividade where nome ='Pecuária em área alterada e /ou subutilizada (simplificado)');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome IN('Criação de Equinos','Criação de asininos e muares','Criação de caprinos','Criação de ovinos, inclusive para produção de lã','Criação de frangos para corte','Produção de pintos de um dia','Criação de outros galináceos, exceto para corte','Criação de aves, exceto galináceos','Produção de ovos')) 
-- AND id_atividade =(SELECT id FROM licenciamento.atividade where nome ='Pecuária em área alterada e /ou subutilizada (declaratório)');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor= (SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade = (SELECT id FROM licenciamento.atividade where nome ='Centro Receptivo')
-- AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome = 'Serviços de reservas e outros serviços de turismo não especificados anteriormente');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor= (SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade = (SELECT id FROM licenciamento.atividade where nome ='Hotel')
-- AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ='Construção de edifícios');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor= (SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade = (SELECT id FROM licenciamento.atividade where nome ='Pousada')
-- AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ='Construção de edifícios');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor= (SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade = (SELECT id FROM licenciamento.atividade where nome ='Parque temático/diversão')
-- AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ='Construção de edifícios');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor= (SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade = (SELECT id FROM licenciamento.atividade where nome ='Quiosque (barraca) de praia')
-- AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ='Obras civis e de infraestruturas');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor= (SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade = (SELECT id FROM licenciamento.atividade where nome ='Hotel de ecoturismo /hotel fazenda')
-- AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ='Hotéis');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor= (SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade = (SELECT id FROM licenciamento.atividade where nome ='Balneário público ou similares')
-- AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ='Outras atividades de recreação e lazer não especificadas anteriormente');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor= (SELECT id FROM portal_seguranca.setor WHERE sigla='GEINFRA') WHERE id_atividade = (SELECT id FROM licenciamento.atividade where nome ='Implantação de equipamentos comunitários (públicos de educação, cultura, saúde, lazer e similares)')
-- AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ='obras civis e de infraestruturas');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor= (SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade = (SELECT id FROM licenciamento.atividade where nome ='Terminal logístico de cargas gerais')
-- AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ='Operador de transporte multimodal - OTM');	

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Clínica de reabilitação') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Atividades de assistência psicossocial e à saúde a portadores de distúrbios psíquicos, deficiência mental e dependência química não especificadas anteriormente');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Clínica de reabilitação') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Atividades de assistência social prestadas em residências coletivas e particulares não especificadas anteriormente');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEINFRA') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Estrada vicinal / vias urbanas') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Obras de urbanização - ruas, praças e calçadas');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEINFRA') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Canteiro de obras somente com instalações administrativas (alojamento, almoxarifado, refeitório, etc)') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Outros alojamentos não especificados anteriormente');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEINFRA') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Canteiro de obras somente com instalações administrativas (alojamento, almoxarifado, refeitório, etc)') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Depósitos de mercadorias para terceiros, exceto armazéns gerais e guarda móveis');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEINFRA') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Hangar') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Construção de edifícios');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEINFRA') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Pátio regulador (triagem) de caminhões') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Estacionamento de veículos');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Motel') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Construção de edifícios');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEINFRA') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Instalação Portuária de Pequeno Porte, instalação portuária de turismo, trapiche, ancoradouro e rampa de acesso.') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Obras portuárias, marítimas e fluviais');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEINFRA') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Instalação Portuária de Pequeno Porte, instalação portuária de turismo, trapiche, ancoradouro e rampa de acesso.') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Administração Logística');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEINFRA') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Marina') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Obras portuárias, marítimas e fluviais');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEINFRA') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Ponte e pontilhão') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Obras portuárias, marítimas e fluviais');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEMINA') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Pesquisa mineral') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'(não metálico)');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEMIM') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Pesquisa mineral') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'(minerais metálicos não ferrosos)');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEMIM') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Pesquisa mineral') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'( minério de ferro)');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEIND') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Fabricação de artefatos de couro natural/ peles e produtos similares.') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Fabricação de artefatos de couro não especificados anteriormente');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEIND') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Fabricação de cola animal') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Fabricação de adesivos e selantes');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Reciclagem de papel') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Recuperação de materiais não especificados anteriormente');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEIND') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Fabricação de caramelos, doces e similares.') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Fabricação de frutas cristalizadas, balas e semelhantes');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEIND') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Fabricação de gelo') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'(Gelo exceto gelo seco)');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEIND') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Beneficiamento de frutas') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Fabricação de conservas de frutas');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEIND') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Beneficiamento de mel') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Fabricação de outros produtos alimentícios não especificados anteriormente');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Cozinha Industrial') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Fornecimento de alimentos preparados preponderantemente para empresas');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEIND') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Fabricação de condimentos') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Fabricação de especiarias, molhos, temperos e condimentos');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEIND') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Fabricação de artefatos de serralheria artística') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Fabricação de esquadrias de metal');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEPROF') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Aproveitamento de aparas de madeiras') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Fabricação de esquadrias de madeira e de peças de madeira para instalações industriais e comerciais.');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEPROF') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Briqueteira/pellets') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Fabricação de artefatos diversos de madeira, exceto móveis');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEPROF') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Movelaria/ Marcenaria/ Carpintaria/Secagem') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Fabricação de esquadrias de madeira e de peças de madeira para instalações industriais e comerciais.');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEPROF') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Movelaria/ Marcenaria/ Carpintaria/Secagem') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Fabricação de outros artigos de carpintaria para construção.');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEPROF') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Movelaria/ Marcenaria/ Carpintaria/Secagem') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Fabricação de móveis com predominância de madeira');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEIND') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Fabricação de velas') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Fabricação de velas, inclusive decorativas');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEIND') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Fabricação de artefatos de couro sintético') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'FABRICAÇÃO DE LAMINADOS PLANOS E TUBULARES DE MATERIAL PLÁSTICO');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Armazém para grãos/cereais') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Depósitos de mercadorias para terceiros, exceto armazéns gerais e guarda móveis');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Silos para grãos /cereais') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Depósitos de mercadorias para terceiros, exceto armazéns gerais e guarda móveis');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Telefonia celular') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Instalação de antena');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Telefonia celular') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Funcionamento da empresa');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Prensagem de material reciclável/ enfardamento trituração e outros') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Recuperação de materiais plásticos');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Lavanderia') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Lavanderias');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Sucataria em geral') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Recuperação de materiais metálicos, exceto alumínio');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEFAP') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Terminal ou entreposto de recepção, armazenamento, comercialização e/ou frigorificação de pescados.') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Construção do terminal');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEFAP') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Terminal ou entreposto de recepção, armazenamento, comercialização e/ou frigorificação de pescados.') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Conservação do pescado');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEFAP') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Infra estrutura especializada em turismo de pesca esportiva') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Outras atividades esportivas não especificadas anteriormente');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE'Comércio de substâncias e produtos perigosos') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE'Comércio atacadista de outros produtos químicos e petroquímicos não especificados anteriormente');

-- --6
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =1 AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Cultivo de soja');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =1 AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Cultivo de guaraná');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =1 AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Cultivo de mamão');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =1 AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Cultivo de Seringueira');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =1 AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Cultivo de outras plantas de lavoura permanente não especificadas anteriormente');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =1 AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Cultivo de dendê');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Viveiro de mudas') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Produção sementes certificadas, exceto de forrageiras para formação de pasto');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Viveiro de mudas') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Cultivo de outras oleaginosas de lavoura temporária não especificadas anteriormente');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de bovinos');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de Bovinos para leite');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de bovinos, exceto para corte e leite');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de Bubalinos');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de Equinos');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de asininos e muares');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de caprinos');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de ovinos, inclusive para produção de lã');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de frangos para corte');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Produção de pintos de um dia');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de outros galináceos, exceto para corte');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de aves, exceto galináceos');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Produção de ovos');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEPROF') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Aproveitamento de aparas de madeiras') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Fabricação de esquadrias de madeira e de peças de madeira para instalações industriais e comerciais');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEPROF') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Movelaria/ Marcenaria/ Carpintaria/Secagem') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Fabricação de esquadrias de madeira e de peças de madeira para instalações industriais e comerciais');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEPROF') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Movelaria/ Marcenaria/ Carpintaria/Secagem') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Fabricação de outros artigos de carpintaria para construção');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEFAP') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Terminal ou entreposto de recepção, armazenamento, comercialização e/ou frigorificação de pescados. /SIMPLIFICADO') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Construção do terminal');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEINFRA') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Implantação de equipamentos comunitários (públicos de educação, cultura, saúde, lazer e similares)') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'obras civis e de infraestruturas');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEMIM') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pesquisa mineral') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Atividades de apoio à extração de minério de ferro');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Hoteis') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Construção de edifícios');--Não existe relacionamento
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Moteis') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Construção de edifícios'); -- Não existe atividade relacionada a motel
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEMINA') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pesquisa mineral') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Atividades de apoio à extração de minerais não-metálicos'); -- Não existe atividade nada relacionada a essa atividade
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEMIM') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pesquisa mineral') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Atividades de apoio à extração de minerais metálicos não-ferrosos'); -- Não existe atividade nada relacionada a essa atividade
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEIND') WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Fabricação de gelo') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Fabricação de gelo'); -- Não existe atividade nada relacionada a essa atividade


-- --7
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade where nome ILIKE 'Cultura de ciclo longo') AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae WHERE codigo = '0133-4/06');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade WHERE nome ILIKE 'Cultura de ciclo longo') AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae where codigo = '0133-04/08');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade WHERE nome ILIKE 'Cultura de ciclo longo') AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae WHERE codigo = '0139-39/06');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade where nome ILIKE 'Cultura de ciclo longo (Dendê)') AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae WHERE codigo = '0139-3/05');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEFAP') WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade where nome ILIKE 'Terminal ou entreposto de recepção, armazenamento, comercialização e/ou frigorificação de pescados.') AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae WHERE codigo = '4120-4/00');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO') WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade where nome ILIKE 'Agricultura em área alterada e /ou subutilizada') AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae WHERE codigo = '0116-4/99');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade where nome ILIKE 'Hotéis' AND id_tipologia = 5) AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae WHERE codigo = '4120-4/00');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS') WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade where nome ILIKE 'Motéis' AND id_tipologia = 5) AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae WHERE codigo = '4120-4/00');

-- --13
-- UPDATE licenciamento.tipo_caracterizacao_atividade tca SET sigla_setor = 
-- (SELECT sigla FROM portal_seguranca.setor WHERE id = tca.id_setor) 
-- WHERE id_setor IS NOT NULL;





# --- !Downs

-- --13
-- UPDATE licenciamento.contato SET  email='' where  email is null;
-- --7
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade where nome ILIKE 'Agricultura em área alterada e /ou subutilizada') AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae WHERE codigo = '0116-4/99');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade where nome ILIKE 'Hotéis' AND id_tipologia = 5) AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae WHERE codigo = '4120-4/00');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade where nome ILIKE 'Motéis' AND id_tipologia = 5) AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae WHERE codigo = '4120-4/00');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade where nome ILIKE 'Cultura de ciclo longo') AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae WHERE codigo = '0133-4/06');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade WHERE nome ILIKE 'Cultura de ciclo longo') AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae where codigo = '0133-04/08');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade WHERE nome ILIKE 'Cultura de ciclo longo') AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae WHERE codigo = '0139-39/06');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade where nome ILIKE 'Cultura de ciclo longo (Dendê)') AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae WHERE codigo = '0139-3/05');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE 
-- id_atividade = (SELECT id FROM licenciamento.atividade where nome ILIKE 'Terminal ou entreposto de recepção, armazenamento, comercialização e/ou frigorificação de pescados.') AND 
-- id_atividade_cnae IN (SELECT id FROM licenciamento.atividade_cnae WHERE codigo = '4120-4/00');



-- --6
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =1 AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Cultivo de soja');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =1 AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Cultivo de guaraná');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =1 AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Cultivo de mamão');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =1 AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Cultivo de Seringueira');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =1 AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Cultivo de outras plantas de lavoura permanente não especificadas anteriormente');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =1 AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Cultivo de dendê');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Viveiro de mudas') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Produção sementes certificadas, exceto de forrageiras para formação de pasto');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Viveiro de mudas') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Cultivo de outras oleaginosas de lavoura temporária não especificadas anteriormente');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de bovinos');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de Bovinos para leite');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de bovinos, exceto para corte e leite');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de Bubalinos');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de Equinos');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de asininos e muares');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de caprinos');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de ovinos, inclusive para produção de lã');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de frangos para corte');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Produção de pintos de um dia');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de outros galináceos, exceto para corte');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Criação de aves, exceto galináceos');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pecuária em área alterada e /ou subutilizada') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Produção de ovos');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Aproveitamento de aparas de madeiras') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Fabricação de esquadrias de madeira e de peças de madeira para instalações industriais e comerciais');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Movelaria/ Marcenaria/ Carpintaria/Secagem') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Fabricação de esquadrias de madeira e de peças de madeira para instalações industriais e comerciais');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Movelaria/ Marcenaria/ Carpintaria/Secagem') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Fabricação de outros artigos de carpintaria para construção');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Terminal ou entreposto de recepção, armazenamento, comercialização e/ou frigorificação de pescados. /SIMPLIFICADO') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Construção do terminal');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Implantação de equipamentos comunitários (públicos de educação, cultura, saúde, lazer e similares)') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'obras civis e de infraestruturas');
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pesquisa mineral') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Atividades de apoio à extração de minério de ferro');

-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Hoteis') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Construção de edifícios');--Não existe relacionamento
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Moteis') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Construção de edifícios'); -- Não existe atividade relacionada a motel
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pesquisa mineral') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Atividades de apoio à extração de minerais não-metálicos'); -- Não existe atividade nada relacionada a essa atividade
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Pesquisa mineral') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Atividades de apoio à extração de minerais metálicos não-ferrosos'); -- Não existe atividade nada relacionada a essa atividade
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL WHERE id_atividade =(SELECT id FROM licenciamento.atividade where nome ILIKE 'Fabricação de gelo') AND id_atividade_cnae IN(SELECT id FROM licenciamento.atividade_cnae where nome ILIKE 'Fabricação de gelo'); -- Não existe atividade nada relacionada a essa atividade


-- --5
-- UPDATE licenciamento.tipo_caracterizacao_atividade SET id_setor=NULL;

-- --4
-- UPDATE licenciamento.atividade_cnae SET id_setor=NULL;
-- ALTER TABLE licenciamento.atividade_cnae DROP CONSTRAINT fk_ac_setor;

--2
DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil=2 AND id_permissao=(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ALTERAR_EMPREENDIMENTO');

DELETE FROM portal_seguranca.permissao WHERE codigo='ALTERAR_EMPREENDIMENTO';