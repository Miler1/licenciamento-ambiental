# --- !Ups

<<<<<<< HEAD
-- Removendo perguntas e respostas duplicadas

begin;
update licenciamento.rel_atividade_pergunta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?')
where id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' );

update licenciamento.resposta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' )
where id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' )
delete from licenciamento.pergunta where texto ='A atividade atende aos requisitos e normas de segurança pessoal privada?'  and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' )
)
delete from licenciamento.resposta where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' )
)
delete from licenciamento.resposta where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende aos requisitos e normas de segurança pessoal privada?' ) and id <(select id_max from max_id);
commit;

begin;
update licenciamento.rel_atividade_pergunta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?')
where id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' );

update licenciamento.resposta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' )
where id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' )
delete from licenciamento.pergunta where texto ='A atividade atende às normas regulamentadoras das ações de vigilância sanitária?'  and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' )
)
delete from licenciamento.resposta where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' )
)
delete from licenciamento.resposta where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade atende às normas regulamentadoras das ações de vigilância sanitária?' ) and id <(select id_max from max_id);
commit;

begin;
update licenciamento.rel_atividade_pergunta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?')
where id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?' );

update licenciamento.resposta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?' )
where id_pergunta in (select id from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?' )
delete from licenciamento.pergunta where texto ='A atividade visa a construção do empreendimento com área superior a 1 hectare?'  and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
   where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?' )
    )
delete from licenciamento.resposta where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?' ) and id <(select id_max from max_id);
    commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?' )
)
delete from licenciamento.resposta where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'A atividade visa a construção do empreendimento com área superior a 1 hectare?' ) and id <(select id_max from max_id);
commit;

begin;
update licenciamento.rel_atividade_pergunta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?')
where id_pergunta in (select id from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' );

update licenciamento.resposta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' )
where id_pergunta in (select id from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' )
delete from licenciamento.pergunta where texto ='Há utilização de práticas que submetam os animais à crueldadel?'  and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' )
)
delete from licenciamento.resposta where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' )
)
delete from licenciamento.resposta where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Há utilização de práticas que submetam os animais à crueldadel?' ) and id <(select id_max from max_id);
commit;

begin;
update licenciamento.rel_atividade_pergunta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?')
where id_pergunta in (select id from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' );

update licenciamento.resposta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' )
where id_pergunta in (select id from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' )
delete from licenciamento.pergunta where texto ='Haverá uso de produtos de origem animal, vegetal ou mineral?'  and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' )
)
delete from licenciamento.resposta where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
   where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' )
     )
delete from licenciamento.resposta where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Haverá uso de produtos de origem animal, vegetal ou mineral?' ) and id <(select id_max from max_id);
commit;

begin;
update licenciamento.rel_atividade_pergunta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ')
where id_pergunta in (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' );

update licenciamento.resposta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' )
where id_pergunta in (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' )
delete from licenciamento.pergunta where texto ='Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? '  and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' )
)
delete from licenciamento.resposta where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' )
)
delete from licenciamento.resposta where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído? ' ) and id <(select id_max from max_id);
commit;

begin;
update licenciamento.rel_atividade_pergunta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído?')
where id_pergunta in (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído?' );

update licenciamento.resposta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído?')
where id_pergunta in (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído?');

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído?')
delete from licenciamento.pergunta where texto ='Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído?'  and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído?' )
)
delete from licenciamento.resposta where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído?' )
)
delete from licenciamento.resposta where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os equipamentos técnicos utilizados atendem aos limites de tolerância de ruído?' ) and id <(select id_max from max_id);
commit;

begin;
update licenciamento.rel_atividade_pergunta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?')
where id_pergunta in (select id from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' );

update licenciamento.resposta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' )
where id_pergunta in (select id from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' )
delete from licenciamento.pergunta where texto ='Os resíduos gerados na atividade estão sendo adequadamente destinados?'  and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' )
)
delete from licenciamento.resposta where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' )
)
delete from licenciamento.resposta where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos gerados na atividade estão sendo adequadamente destinados?' ) and id <(select id_max from max_id);
commit;

begin;
update licenciamento.rel_atividade_pergunta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?')
where id_pergunta in (select id from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' );

update licenciamento.resposta
set id_pergunta = (select max(id) from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' )
where id_pergunta in (select id from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' );

WITH max_id as ( select max(id) as id_max from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' )
delete from licenciamento.pergunta where texto ='Os resíduos hospitalares estão sendo adequadamente destinados?'  and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' )
)
delete from licenciamento.resposta where texto = 'Não' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' ) and id <(select id_max from max_id);
commit;

begin;
with max_id as
(
select max(id) as id_max
from licenciamento.resposta
where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' )
)
delete from licenciamento.resposta where texto = 'Sim' and id_pergunta = (select id from licenciamento.pergunta where texto = 'Os resíduos hospitalares estão sendo adequadamente destinados?' ) and id <(select id_max from max_id);
commit;

begin;
delete from licenciamento.rel_atividade_pergunta where id_pergunta = 5324;
delete from licenciamento.pergunta where id not in (select id_pergunta from licenciamento.resposta);
commit;

# --- !Downs
-- não passível de down fazer backup das tabelas licenciamento.rel_atividade_pergunta, licenciamento.resposta e licenciamento.pergunta
=======
CREATE OR REPLACE FUNCTION licenciamento.f_gerar_numero_processo() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
DECLARE
    v_ultimo_ano INTEGER;

BEGIN
    SELECT extract(YEAR FROM (data_cadastro))::INTEGER INTO v_ultimo_ano
    FROM licenciamento.caracterizacao ORDER BY 1 DESC LIMIT 1;

    IF v_ultimo_ano <> (SELECT extract(YEAR FROM (current_date))::INTEGER) THEN

        PERFORM setval('licenciamento.numero_processo_seq', 1, FALSE);

    END IF;

    IF NEW.numero IS NULL OR NEW.numero = '' THEN

        NEW.numero_processo_automatico =
                CONCAT(lpad(cast(nextval('licenciamento.numero_processo_seq'::REGCLASS) AS VARCHAR), 7, '0'), '/' , extract(YEAR FROM (current_date)));

    END IF;

    RETURN NEW;

END;
$$;

ALTER FUNCTION licenciamento.f_gerar_numero_processo() OWNER TO postgres;

GRANT EXECUTE ON FUNCTION licenciamento.f_gerar_numero_processo() TO licenciamento_ap;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE licenciamento.numero_processo_seq TO licenciamento_ap;


# --- !Downs

create function f_gerar_numero_processo() returns trigger
    language plpgsql
as
$$
DECLARE
    v_ultimo_ano INTEGER;

BEGIN
    SELECT extract(YEAR FROM (data_cadastro))::INTEGER INTO v_ultimo_ano
    FROM licenciamento.caracterizacao ORDER BY 1 DESC LIMIT 1;

    IF v_ultimo_ano <> (SELECT extract(YEAR FROM (current_date))::INTEGER) THEN

        PERFORM setval('licenciamento.numero_processo_seq', 1, FALSE);

    END IF;

    IF NEW.numero IS NULL OR NEW.numero = '' THEN

        NEW.numero_processo_automatico =
                CONCAT(lpad(cast(nextval('licenciamento.numero_processo_seq'::REGCLASS) AS VARCHAR), 7, '0'), '/' , extract(YEAR FROM (current_date)));

    END IF;

    RETURN NEW;

END;
$$;

alter function f_gerar_numero_processo() owner to postgres;

>>>>>>> be5e6114fdba424e557def050534361300f5fa8a
