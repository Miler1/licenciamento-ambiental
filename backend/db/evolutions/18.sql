# --- !Ups

-- Adicionar coluna ordem na tabela "rel_atividade_pergunta"
ALTER TABLE licenciamento.rel_atividade_pergunta ADD COLUMN ordem integer;

-- Atualizar com a coluna ordem da tabela "pergunta"
UPDATE licenciamento.rel_atividade_pergunta SET ordem=(SELECT ordem FROM licenciamento.pergunta WHERE licenciamento.rel_atividade_pergunta.id_pergunta=licenciamento.pergunta.id);

-- 18.sql
begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,2 hectare (ha) e está localizada em Manaus?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,2 hectare (ha) e está localizada em Manaus?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,2 hectare (ha) e está localizada em Manaus?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,2 hectare (ha) e está localizada em Manaus?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,2 hectare (ha) e está localizada em Manaus?' )
delete from licenciamento.pergunta where texto ='A área total do projeto é menor que 0,2 hectare (ha) e está localizada em Manaus?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' )
delete from licenciamento.pergunta where texto ='A atividade atende aos requisitos e normas de segurança pessoal privada?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A tensão da rede de distribuição de energia elétrica é acima de 13,8kV?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A tensão da rede de distribuição de energia elétrica é acima de 13,8kV?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A tensão da rede de distribuição de energia elétrica é acima de 13,8kV?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A tensão da rede de distribuição de energia elétrica é acima de 13,8kV?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A tensão da rede de distribuição de energia elétrica é acima de 13,8kV?' )
delete from licenciamento.pergunta where texto ='A tensão da rede de distribuição de energia elétrica é acima de 13,8kV?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O serviço é executado em uma escola?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O serviço é executado em uma escola?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O serviço é executado em uma escola?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O serviço é executado em uma escola?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'O serviço é executado em uma escola?' )
delete from licenciamento.pergunta where texto ='O serviço é executado em uma escola?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,5 hectare (ha) e está localizada em Manaus?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,5 hectare (ha) e está localizada em Manaus?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,5 hectare (ha) e está localizada em Manaus?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,5 hectare (ha) e está localizada em Manaus?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,5 hectare (ha) e está localizada em Manaus?' )
delete from licenciamento.pergunta where texto ='A área total do projeto é menor que 0,5 hectare (ha) e está localizada em Manaus?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A área total do projeto é menor que 1 hectare (ha) e está localizada em Manaus?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 1 hectare (ha) e está localizada em Manaus?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A área total do projeto é menor que 1 hectare (ha) e está localizada em Manaus?' ) WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 1 hectare (ha) e está localizada em Manaus?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A área total do projeto é menor que 1 hectare (ha) e está localizada em Manaus?' )
delete from licenciamento.pergunta where texto ='A área total do projeto é menor que 1 hectare (ha) e está localizada em Manaus?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' )
delete from licenciamento.pergunta where texto ='A atividade atende às normas regulamentadoras das ações de vigilância sanitária?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = ' A atividade envolve explotação de óleo, essência, resinas, gomas, frutos, folhas, ramos, raízes e produtos voltados para a produção de fármacos, cosméticos e outras finalidades?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = ' A atividade envolve explotação de óleo, essência, resinas, gomas, frutos, folhas, ramos, raízes e produtos voltados para a produção de fármacos, cosméticos e outras finalidades?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = ' A atividade envolve explotação de óleo, essência, resinas, gomas, frutos, folhas, ramos, raízes e produtos voltados para a produção de fármacos, cosméticos e outras finalidades?' ) WHERE id_pergunta in (select id from licenciamento.pergunta where texto = ' A atividade envolve explotação de óleo, essência, resinas, gomas, frutos, folhas, ramos, raízes e produtos voltados para a produção de fármacos, cosméticos e outras finalidades?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = ' A atividade envolve explotação de óleo, essência, resinas, gomas, frutos, folhas, ramos, raízes e produtos voltados para a produção de fármacos, cosméticos e outras finalidades?' )
delete from licenciamento.pergunta where texto =' A atividade envolve explotação de óleo, essência, resinas, gomas, frutos, folhas, ramos, raízes e produtos voltados para a produção de fármacos, cosméticos e outras finalidades?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade realizada em Manaus e fora de um flutuante?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade realizada em Manaus e fora de um flutuante?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade realizada em Manaus e fora de um flutuante?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade realizada em Manaus e fora de um flutuante?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A atividade realizada em Manaus e fora de um flutuante?' )
delete from licenciamento.pergunta where texto ='A atividade realizada em Manaus e fora de um flutuante?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade realizada envolve serviços de laboratórios de análises clínicas, radiologia, análise química, físico-química e/ou microbiológica?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade realizada envolve serviços de laboratórios de análises clínicas, radiologia, análise química, físico-química e/ou microbiológica?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade realizada envolve serviços de laboratórios de análises clínicas, radiologia, análise química, físico-química e/ou microbiológica?' ) WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade realizada envolve serviços de laboratórios de análises clínicas, radiologia, análise química, físico-química e/ou microbiológica?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A atividade realizada envolve serviços de laboratórios de análises clínicas, radiologia, análise química, físico-química e/ou microbiológica?' )
delete from licenciamento.pergunta where texto ='A atividade realizada envolve serviços de laboratórios de análises clínicas, radiologia, análise química, físico-química e/ou microbiológica?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?' )
delete from licenciamento.pergunta where texto ='A atividade visa a construção do empreendimento com área superior a 1 hectare?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A carga é combustível?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A carga é combustível?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A carga é combustível?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A carga é combustível?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A carga é combustível?' )
delete from licenciamento.pergunta where texto ='A carga é combustível?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A carga é perigosa?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A carga é perigosa?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A carga é perigosa?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A carga é perigosa?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A carga é perigosa?' )
delete from licenciamento.pergunta where texto ='A carga é perigosa?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A carga é produto químico e/ou perigoso?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A carga é produto químico e/ou perigoso?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A carga é produto químico e/ou perigoso?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A carga é produto químico e/ou perigoso?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A carga é produto químico e/ou perigoso?' )
delete from licenciamento.pergunta where texto ='A carga é produto químico e/ou perigoso?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A prestação de serviços de manutenção se dá em uma estrutura de oficina de motores máquinas e equipamentos?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A prestação de serviços de manutenção se dá em uma estrutura de oficina de motores máquinas e equipamentos?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A prestação de serviços de manutenção se dá em uma estrutura de oficina de motores máquinas e equipamentos?' ) WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A prestação de serviços de manutenção se dá em uma estrutura de oficina de motores máquinas e equipamentos?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A prestação de serviços de manutenção se dá em uma estrutura de oficina de motores máquinas e equipamentos?' )
delete from licenciamento.pergunta where texto ='A prestação de serviços de manutenção se dá em uma estrutura de oficina de motores máquinas e equipamentos?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Armazenamento de produto químico e/ou perigoso?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Armazenamento de produto químico e/ou perigoso?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Armazenamento de produto químico e/ou perigoso?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Armazenamento de produto químico e/ou perigoso?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'Armazenamento de produto químico e/ou perigoso?' )
delete from licenciamento.pergunta where texto ='Armazenamento de produto químico e/ou perigoso?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Atividade desenvolvida de modo artesanal ou semi-artesanal?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Atividade desenvolvida de modo artesanal ou semi-artesanal?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Atividade desenvolvida de modo artesanal ou semi-artesanal?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Atividade desenvolvida de modo artesanal ou semi-artesanal?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'Atividade desenvolvida de modo artesanal ou semi-artesanal?' )
delete from licenciamento.pergunta where texto ='Atividade desenvolvida de modo artesanal ou semi-artesanal?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A transformação do produto se dá de modo artesanal ou semiartesanal?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A transformação do produto se dá de modo artesanal ou semiartesanal?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A transformação do produto se dá de modo artesanal ou semiartesanal?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'A transformação do produto se dá de modo artesanal ou semiartesanal?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A transformação do produto se dá de modo artesanal ou semiartesanal?' )
delete from licenciamento.pergunta where texto ='A transformação do produto se dá de modo artesanal ou semiartesanal?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' )
delete from licenciamento.pergunta where texto ='Há utilização de práticas que submetam os animais à crueldadel?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Haverá transporte de produtos perigosos?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Haverá transporte de produtos perigosos?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Haverá transporte de produtos perigosos?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Haverá transporte de produtos perigosos?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'Haverá transporte de produtos perigosos?' )
delete from licenciamento.pergunta where texto ='Haverá transporte de produtos perigosos?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' )
delete from licenciamento.pergunta where texto ='Haverá uso de produtos de origem animal, vegetal ou mineral?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O carga é combustíveis?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O carga é combustíveis?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O carga é combustíveis?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O carga é combustíveis?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'O carga é combustíveis?' )
delete from licenciamento.pergunta where texto ='O carga é combustíveis?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservaçao Permanente - APP?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservaçao Permanente - APP?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservaçao Permanente - APP?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservaçao Permanente - APP?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservaçao Permanente - APP?' )
delete from licenciamento.pergunta where texto ='O empreendimento encontra-se inserido em Área de Preservaçao Permanente - APP?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservação Permanente - APP?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservação Permanente - APP?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservação Permanente - APP?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservação Permanente - APP?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservação Permanente - APP?' )
delete from licenciamento.pergunta where texto ='O empreendimento encontra-se inserido em Área de Preservação Permanente - APP?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante?' )
delete from licenciamento.pergunta where texto ='O empreendimento encontra-se instalado em estrutura flutuante?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante? ' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante? ' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante? ' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante? ' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante? ' )
delete from licenciamento.pergunta where texto ='O empreendimento encontra-se instalado em estrutura flutuante? '  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O empreendimento é Shopping Center ou Centro de Compras?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O empreendimento é Shopping Center ou Centro de Compras?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O empreendimento é Shopping Center ou Centro de Compras?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O empreendimento é Shopping Center ou Centro de Compras?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'O empreendimento é Shopping Center ou Centro de Compras?' )
delete from licenciamento.pergunta where texto ='O empreendimento é Shopping Center ou Centro de Compras?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação? ' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação? ' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação? ' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação? ' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação? ' )
delete from licenciamento.pergunta where texto ='O empreendimento necessita suprimir vegetação? '  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação?' )
delete from licenciamento.pergunta where texto ='O empreendimento necessita suprimir vegetação?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O número de empregados é menor que 20?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 20?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O número de empregados é menor que 20?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 20?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'O número de empregados é menor que 20?' )
delete from licenciamento.pergunta where texto ='O número de empregados é menor que 20?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O número de empregados é menor que 30?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 30?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O número de empregados é menor que 30?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 30?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'O número de empregados é menor que 30?' )
delete from licenciamento.pergunta where texto ='O número de empregados é menor que 30?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O número de empregados é menor que 50?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 50?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'O número de empregados é menor que 50?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 50?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'O número de empregados é menor que 50?' )
delete from licenciamento.pergunta where texto ='O número de empregados é menor que 50?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' )
delete from licenciamento.pergunta where texto ='Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? '  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' )
delete from licenciamento.pergunta where texto ='Os resíduos gerados na atividade estão sendo adequadamente destinados?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' )
delete from licenciamento.pergunta where texto ='Os resíduos hospitalares estão sendo adequadamente destinados?'  and id <(select id_max from max_id);
commit;

begin;
UPDATE licenciamento.rel_atividade_pergunta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Somente prestação de serviço de manutenção?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Somente prestação de serviço de manutenção?' );

UPDATE licenciamento.resposta
SET id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Somente prestação de serviço de manutenção?' )
WHERE id_pergunta in (select id from licenciamento.pergunta where texto = 'Somente prestação de serviço de manutenção?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'Somente prestação de serviço de manutenção?' )
delete from licenciamento.pergunta where texto ='Somente prestação de serviço de manutenção?'  and id <(select id_max from max_id);
commit;

--- refatoracao da tabela resposta

UPDATE licenciamento.resposta SET texto = 'Não' where texto = 'Nao';

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A tensão da rede de distribuição de energia elétrica é acima de 13,8kV?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A tensão da rede de distribuição de energia elétrica é acima de 13,8kV?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A tensão da rede de distribuição de energia elétrica é acima de 13,8kV?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A tensão da rede de distribuição de energia elétrica é acima de 13,8kV?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O serviço é executado em uma escola?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O serviço é executado em uma escola?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O serviço é executado em uma escola?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O serviço é executado em uma escola?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A carga é perigosa?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A carga é perigosa?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A carga é perigosa?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A carga é perigosa?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A carga é produto químico e/ou perigoso?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A carga é produto químico e/ou perigoso?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A carga é produto químico e/ou perigoso?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A carga é produto químico e/ou perigoso?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A transformação do produto se dá de modo artesanal ou semiartesanal?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A transformação do produto se dá de modo artesanal ou semiartesanal?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A transformação do produto se dá de modo artesanal ou semiartesanal?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A transformação do produto se dá de modo artesanal ou semiartesanal?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Haverá transporte de produtos perigosos?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Haverá transporte de produtos perigosos?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'Haverá transporte de produtos perigosos?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'Haverá transporte de produtos perigosos?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O carga é combustíveis?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O carga é combustíveis?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O carga é combustíveis?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O carga é combustíveis?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante? ' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante? ' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante? ' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se instalado em estrutura flutuante? ' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação? ' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação? ' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação? ' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação? ' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento necessita suprimir vegetação?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,2 hectare (ha) e está localizada em Manaus?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,2 hectare (ha) e está localizada em Manaus?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,2 hectare (ha) e está localizada em Manaus?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,2 hectare (ha) e está localizada em Manaus?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,5 hectare (ha) e está localizada em Manaus?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,5 hectare (ha) e está localizada em Manaus?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,5 hectare (ha) e está localizada em Manaus?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 0,5 hectare (ha) e está localizada em Manaus?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 1 hectare (ha) e está localizada em Manaus?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 1 hectare (ha) e está localizada em Manaus?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 1 hectare (ha) e está localizada em Manaus?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A área total do projeto é menor que 1 hectare (ha) e está localizada em Manaus?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = ' A atividade envolve explotação de óleo, essência, resinas, gomas, frutos, folhas, ramos, raízes e produtos voltados para a produção de fármacos, cosméticos e outras finalidades?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = ' A atividade envolve explotação de óleo, essência, resinas, gomas, frutos, folhas, ramos, raízes e produtos voltados para a produção de fármacos, cosméticos e outras finalidades?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = ' A atividade envolve explotação de óleo, essência, resinas, gomas, frutos, folhas, ramos, raízes e produtos voltados para a produção de fármacos, cosméticos e outras finalidades?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = ' A atividade envolve explotação de óleo, essência, resinas, gomas, frutos, folhas, ramos, raízes e produtos voltados para a produção de fármacos, cosméticos e outras finalidades?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade realizada em Manaus e fora de um flutuante?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade realizada em Manaus e fora de um flutuante?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade realizada em Manaus e fora de um flutuante?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade realizada em Manaus e fora de um flutuante?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade realizada envolve serviços de laboratórios de análises clínicas, radiologia, análise química, físico-química e/ou microbiológica?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade realizada envolve serviços de laboratórios de análises clínicas, radiologia, análise química, físico-química e/ou microbiológica?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade realizada envolve serviços de laboratórios de análises clínicas, radiologia, análise química, físico-química e/ou microbiológica?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade realizada envolve serviços de laboratórios de análises clínicas, radiologia, análise química, físico-química e/ou microbiológica?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A carga é combustível?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A carga é combustível?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A carga é combustível?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A carga é combustível?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A prestação de serviços de manutenção se dá em uma estrutura de oficina de motores máquinas e equipamentos?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'A prestação de serviços de manutenção se dá em uma estrutura de oficina de motores máquinas e equipamentos?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A prestação de serviços de manutenção se dá em uma estrutura de oficina de motores máquinas e equipamentos?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'A prestação de serviços de manutenção se dá em uma estrutura de oficina de motores máquinas e equipamentos?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'Armazenamento de produto químico e/ou perigoso?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'Armazenamento de produto químico e/ou perigoso?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Armazenamento de produto químico e/ou perigoso?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Armazenamento de produto químico e/ou perigoso?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'Atividade desenvolvida de modo artesanal ou semi-artesanal?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'Atividade desenvolvida de modo artesanal ou semi-artesanal?' ) and id <(select id_max from max_id);
begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Atividade desenvolvida de modo artesanal ou semi-artesanal?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Atividade desenvolvida de modo artesanal ou semi-artesanal?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservaçao Permanente - APP?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservaçao Permanente - APP?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservaçao Permanente - APP?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservaçao Permanente - APP?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservação Permanente - APP?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservação Permanente - APP?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservação Permanente - APP?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento encontra-se inserido em Área de Preservação Permanente - APP?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento é Shopping Center ou Centro de Compras?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento é Shopping Center ou Centro de Compras?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento é Shopping Center ou Centro de Compras?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O empreendimento é Shopping Center ou Centro de Compras?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 20?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 20?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 20?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 20?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 30?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 30?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 30?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 30?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 50?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 50?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 50?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'O número de empregados é menor que 50?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Não' and permite_licenciamento = false and id_pergunta = (select id from licenciamento.pergunta where texto = 'Somente prestação de serviço de manutenção?' )
)
 delete from licenciamento.resposta where texto = 'Não' and permite_licenciamento =false and id_pergunta = (select id from licenciamento.pergunta where texto = 'Somente prestação de serviço de manutenção?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
 select max(id) as id_max
 from licenciamento.resposta
    where texto = 'Sim' and permite_licenciamento = true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Somente prestação de serviço de manutenção?' )
)
 delete from licenciamento.resposta where texto = 'Sim' and permite_licenciamento =true and id_pergunta = (select id from licenciamento.pergunta where texto = 'Somente prestação de serviço de manutenção?' ) and id <(select id_max from max_id);
commit;

-- Deletar a coluna ordem da tabela "pergunta"
ALTER TABLE licenciamento.pergunta DROP COLUMN ordem;
 
 
# --- !Downs

-- não passível de down fazer backup das tabelas licenciamento.rel_atividade_pergunta, licenciamento.resposta e licenciamento.pergunta
