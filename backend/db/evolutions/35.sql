# --- !Ups

-- Removendo perguntas e respostas duplicadas

DO
$do$
	DECLARE _pergunta TEXT;
	DECLARE _id INTEGER;
BEGIN
	FOR _id, _pergunta IN
		SELECT id, texto FROM licenciamento.pergunta p WHERE p.id IN (SELECT id_pergunta FROM licenciamento.resposta r WHERE texto IS NOT NULL GROUP BY id_pergunta)
	LOOP
		RAISE NOTICE 'Tratando pergunta: %', _pergunta;

		IF
			(SELECT p.texto FROM licenciamento.pergunta p WHERE p.texto = _pergunta GROUP BY p.texto HAVING COUNT(texto) > 1) is not null
		THEN
            UPDATE licenciamento.rel_atividade_pergunta
            SET id_pergunta = (SELECT max(id) FROM licenciamento.pergunta WHERE texto = _pergunta)
            WHERE id_pergunta IN (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta);

            UPDATE licenciamento.resposta
            SET id_pergunta = (SELECT max(id) FROM licenciamento.pergunta WHERE texto = _pergunta)
            WHERE id_pergunta IN (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta);

            WITH max_id AS ( SELECT max(id) AS id_max FROM licenciamento.pergunta WHERE texto = _pergunta )
            DELETE FROM licenciamento.pergunta WHERE texto =_pergunta AND id <(SELECT id_max FROM max_id);
		END IF;

		IF
			(SELECT r.id FROM licenciamento.resposta r WHERE texto = 'Não' AND permite_licenciamento = true AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta AND id = _id) ) is not null
		THEN
			DELETE FROM licenciamento.resposta WHERE texto = 'Não' AND permite_licenciamento = false AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta AND id = _id);
		END IF;

		-- Sim permite licenciamento
		UPDATE licenciamento.rel_caracterizacao_resposta
		SET id_resposta = (SELECT max(id) FROM licenciamento.resposta
		WHERE texto = 'Sim' AND permite_licenciamento = true AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta ))
			WHERE id_resposta IN (SELECT id FROM licenciamento.resposta WHERE texto = 'Sim' AND permite_licenciamento = true AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta ));

		WITH max_id AS
		(
		 SELECT max(id) AS id_max
		 FROM licenciamento.resposta
		 WHERE texto = 'Sim' AND permite_licenciamento = true AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta)
		)
		 DELETE FROM licenciamento.resposta WHERE texto = 'Sim' AND permite_licenciamento = true AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta) AND id < (SELECT id_max FROM max_id);

		IF
			(SELECT r.id FROM licenciamento.resposta r WHERE texto = 'Sim' AND permite_licenciamento = true AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta AND id = _id)) IS NOT NULL
		THEN
			DELETE FROM licenciamento.resposta WHERE texto = 'Sim' AND permite_licenciamento = false AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta AND id = _id);
		END IF;

		-- Sim não permite licenciamento
		UPDATE licenciamento.rel_caracterizacao_resposta
		SET id_resposta = (SELECT max(id) FROM licenciamento.resposta
		WHERE texto = 'Sim' AND permite_licenciamento = false AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta))
			WHERE id_resposta IN (SELECT id FROM licenciamento.resposta WHERE texto = 'Sim' AND permite_licenciamento = false AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta));

		WITH max_id AS
		(
		 SELECT max(id) AS id_max
		 FROM licenciamento.resposta
		 WHERE texto = 'Sim' AND permite_licenciamento = false AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta)
		)
		 DELETE FROM licenciamento.resposta WHERE texto = 'Sim' AND permite_licenciamento = false AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta) AND id < (SELECT id_max FROM max_id);

		UPDATE licenciamento.rel_caracterizacao_resposta
		SET id_resposta = (SELECT max(id) FROM licenciamento.resposta
			    WHERE texto = 'Não' AND permite_licenciamento = true AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta))
		WHERE id_resposta IN (SELECT id FROM licenciamento.resposta
			    WHERE texto = 'Não' AND permite_licenciamento = true AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta));

		WITH max_id AS
		(
		 SELECT max(id) AS id_max
		 FROM licenciamento.resposta
		    WHERE texto = 'Não' AND permite_licenciamento = true AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta)
		)
		 DELETE FROM licenciamento.resposta WHERE texto = 'Não' AND permite_licenciamento = true AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta) AND id <(SELECT id_max FROM max_id);

		IF
			(SELECT r.id FROM licenciamento.resposta r WHERE texto = 'Não' AND permite_licenciamento = true AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta AND id = _id) ) is not null
		THEN
			DELETE FROM licenciamento.resposta WHERE texto = 'Não' AND permite_licenciamento = false AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta AND id = _id);
		END IF;

		UPDATE licenciamento.rel_caracterizacao_resposta
		SET id_resposta = (SELECT max(id) FROM licenciamento.resposta
			WHERE texto = 'Não' AND permite_licenciamento = false AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta)) WHERE id_resposta IN (SELECT id FROM licenciamento.resposta
				WHERE texto = 'Não' AND permite_licenciamento = false AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta));

		WITH max_id AS
		(
		 SELECT max(id) AS id_max
		 FROM licenciamento.resposta
		 WHERE texto = 'Não' AND permite_licenciamento = false AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta)
		)
		 DELETE FROM licenciamento.resposta WHERE texto = 'Não' AND permite_licenciamento = false AND id_pergunta = (SELECT id FROM licenciamento.pergunta WHERE texto = _pergunta) AND id < (SELECT id_max FROM max_id);


		RAISE NOTICE 'Termino ao tratar pergunta: %', _pergunta;
	END LOOP;
END
$do$;

-- deletando perguntas sem resposta

BEGIN;
DELETE FROM licenciamento.rel_atividade_pergunta WHERE id_pergunta = 5324; -- PARA 1701 O número de empregados é menor que 30?
DELETE FROM licenciamento.pergunta WHERE id not IN (SELECT id_pergunta FROM licenciamento.resposta);
COMMIT;

# --- !Downs
-- não passível de down fazer backup das tabelas licenciamento.rel_atividade_pergunta, licenciamento.resposta e licenciamento.pergunta
