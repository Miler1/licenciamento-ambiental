var RestricoesLicenciamentoSimplificadoService = function(caracterizacaoService, $q) {

	/**
	 * Função que busca as restrições no SICAR de acordo com a localização do Empreendimento.
	 * Retorna uma promise que será resolvida quando todas as restrições forem buscadas e processadas.
	 */
	this.processarRestricoesEmpreendimento = function(empreendimento, atividadesCaracterizacao, perguntas, respostas) {

		var deferred = $q.defer(),
			restricoesZEEBuscadas = false,
			restricoesCARBuscadas = false,
			passivoImovelBuscado = false;

		// Objeto retornado na promise com as perguntas para as quais foram encontradas restrições
		var perguntasComRestricao = [];

		_.forEach(atividadesCaracterizacao, function(atividadeCaracterizacao) {

			/* Busca as restrições do Macro ZEE */
			caracterizacaoService.sobreposicoesCaracterizacaoZEE(atividadeCaracterizacao.geometriasAtividade.geometria)

				.then(function(response){

					var restricoesMZEE = response.data.sobreposicoes;

					if(empreendimento.localizacao === 'ZONA_URBANA'){

						var perguntasComRestricaoZonaUrbana = processarPerguntas(perguntas, respostas, restricoesMZEE,
																['UNIDADE_CONSERVACAO', 'TERRA_INDIGENA', 'AREA_MILITAR'], 'P14');

						perguntasComRestricao = _.concat(perguntasComRestricao, perguntasComRestricaoZonaUrbana);

					}else if(empreendimento.localizacao === 'ZONA_RURAL'){

						var perguntasComRestricaoZEE = processarPerguntas(perguntas, respostas, restricoesMZEE,
																['UNIDADE_CONSERVACAO', 'TERRA_INDIGENA', 'AREA_MILITAR', 'AREA_QUILOMBOLA'], 'P22');

						var perguntasComRestricaoConsolidacao = processarPerguntasZonaConsolidacao(perguntas, respostas, restricoesMZEE,
																['ZONA_CONSOLIDACAO'], 'P25');

						perguntasComRestricao = _.concat(perguntasComRestricao, perguntasComRestricaoZEE);
						perguntasComRestricao = _.concat(perguntasComRestricao, perguntasComRestricaoConsolidacao);

					}

					restricoesZEEBuscadas = true;

					if(restricoesZEEBuscadas && restricoesCARBuscadas && passivoImovelBuscado){
						deferred.resolve(perguntasComRestricao);
					}

				})
				.catch(function(response){
					deferred.reject("Erro ao buscar dados de restrições do empreendimento no Cadastro Ambiental Rural. " + response.message || response.data.texto);
				});
			
		});

		/*	Busca as restrições do CAR.
			Apenas os empreendimentos na zonal rural precisam ser verificados para sobreposições com os temas do CAR. */
		if(empreendimento.localizacao === 'ZONA_RURAL'){

			_.forEach(atividadesCaracterizacao, function(atividadeCaracterizacao) {

				// Busca as sobreposições com Área Consolidada e Reservas Legais
				caracterizacaoService.sobreposicoesCaracterizacaoCAR(atividadeCaracterizacao.geometriasAtividade.geometria, empreendimento.imovel.codigo)
					.then(function(response){

						var restricoesCAR = response.data.sobreposicoes;

						var perguntasComRestricaoAC = processarPerguntasZonaConsolidacao(perguntas, respostas, restricoesCAR,
																['AREA_CONSOLIDADA'], 'P24');

						var perguntasComRestricaoRL = processarPerguntas(perguntas, respostas, restricoesCAR,
																['ARL_PROPOSTA', 'ARL_AVERBADA', 'ARL_APROVADA_NAO_AVERBADA'], 'P30');

						perguntasComRestricao = _.concat(perguntasComRestricao, perguntasComRestricaoAC);
						perguntasComRestricao = _.concat(perguntasComRestricao, perguntasComRestricaoRL);

						restricoesCARBuscadas = true;

						if(restricoesZEEBuscadas && restricoesCARBuscadas && passivoImovelBuscado){
							deferred.resolve(perguntasComRestricao);
						}

					})
					.catch(function(response){
						deferred.reject("Erro ao buscar dados de restrições do empreendimento no Cadastro Ambiental Rural. " + response.data.texto);
					});

			});

			// Busca o excedente/passivo ambiental do imóvel rural vinculado ao empreendimento
			caracterizacaoService.excedentePassivoImovelDoEmpreendimento(empreendimento.id)
				.then(function(response) {

					var excedentePassivo = response.data,
						possuiPassivo = (excedentePassivo < 0);

					/*
						Caso não tenha passivo ambiental, a pergunta é respondida de forma que
						permita o licenciamento e não é exibida para o usuário.
					 */
					if(!possuiPassivo){

						var perguntaComRestricao = obterPerguntaPorCodigo(perguntas, 'P21');

						perguntaComRestricao.bloqueada = true;

						responderPergunta(perguntaComRestricao, respostas, true);
						
					}

					passivoImovelBuscado = true;

					if(restricoesZEEBuscadas && restricoesCARBuscadas && passivoImovelBuscado){
						deferred.resolve(perguntasComRestricao);
					}
					
				})
				.catch(function() {
					console.error(arguments);
				});

		}else{
			restricoesCARBuscadas = true;
			passivoImovelBuscado = true;
			if(restricoesZEEBuscadas && restricoesCARBuscadas && passivoImovelBuscado){
				deferred.resolve(perguntasComRestricao);
			}
		}

		return deferred.promise;

	};

	/*
		Avalia as restrições retornadas do CAR e, se elas existirem, marca automaticamente a resposta equivalente.
	 */
	var processarPerguntas = function(perguntas, respostas, restricoes, temasRestritos, codigoPergunta) {

		var perguntasComRestricao = [];

		if(!restricoes){
			return perguntasComRestricao;
		}

		var restricoesZona = obterRestricoesDosTipos(restricoes, temasRestritos);

		var perguntaComRestricao = obterPerguntaPorCodigo(perguntas, codigoPergunta);

		perguntaComRestricao.bloqueada = true;

		// Existe restrição no Sicar. A pergunta é respondida de forma a NÃO permitir licenciamento.
		if(restricoesZona && restricoesZona.length > 0){

			responderPergunta(perguntaComRestricao, respostas, false);

		}
		// Não existe restrição no Sicar. A pergunta é respondida de forma a permitir licenciamento.
		else{

			responderPergunta(perguntaComRestricao, respostas, true);

		}

		perguntasComRestricao.push({
			pergunta: perguntaComRestricao,
			restricoes: restricoesZona
		});

		return perguntasComRestricao;

	};

	/*
		Avalia as restrições relacionadas à zona de consolidacao retornadas do CAR.
		Nesse caso, a regra é o contrário das outras restrições: se a geometria não estiver sobre áreas
		consolidadas, o licenciamento não deve prosseguir.
	 */
	var processarPerguntasZonaConsolidacao = function(perguntas, respostas, restricoes, temasRestritos, codigoPergunta) {

		if(!restricoes){
			return perguntasComRestricao;
		}

		var restricoesZona = obterRestricoesDosTipos(restricoes, temasRestritos);

		return processarPerguntasConsolidacao(perguntas, respostas, restricoesZona, codigoPergunta);
	};

	/*
		Avalia as restrições relacionadas a áreas consolidadas.
		Nesse caso, a regra é o contrário das outras restrições: se a geometria não estiver sobre áreas
		consolidadas, o licenciamento não deve prosseguir.
	 */
	var processarPerguntasConsolidacao = function(perguntas, respostas, restricoesZona, codigoPergunta) {

		var perguntasComRestricao = [];

		var perguntaComRestricao = obterPerguntaPorCodigo(perguntas, codigoPergunta);

		perguntaComRestricao.bloqueada = true;

		// Existe restrição. A pergunta é respondida de forma a permitir licenciamento.
		if(restricoesZona && restricoesZona.length > 0){

			responderPergunta(perguntaComRestricao, respostas, true);

		}
		// Não existe restrição. A pergunta é respondida de forma a NÃO permitir licenciamento.
		// Além disso, a mensagem é exibida, informando que o empreendimento não está sobre áreas de consolidação
		else{

			responderPergunta(perguntaComRestricao, respostas, false);

			perguntasComRestricao.push({
				pergunta: perguntaComRestricao,
				restricoes: [{
								nome: 'Área consolidada',
								areasSobrepostas: 'Não sobreposta.'
							}]
			});

		}

		return perguntasComRestricao;

	};	

	/*
		Busca nas restrições retornadas do CAR aquelas do tipo especificado.
	 */
	var obterRestricoesDosTipos = function(restricoes, tipos) {

		var restricoesEncontradas = [],
			restricao;

		for (var i = 0; i < tipos.length; i++) {

			restricao = _.find(restricoes, {nome: tipos[i]});

			if(restricao && restricao.areasSobrepostas){
				restricoesEncontradas.push(restricao);
			}

		}

		return restricoesEncontradas;

	};

	var obterPerguntaPorCodigo = function(perguntas, codigo) {

		return _.find(perguntas, {codigo: codigo});

	};

	/*
		"Responde" às perguntas com a resposta passada no parâmetro 'permiteLicenciamento'
	 */
	var responderPergunta = function(pergunta, respostas, permiteLicenciamento) {

		var resposta = _.find(pergunta.respostas, {permiteLicenciamento: permiteLicenciamento});

		respostas[pergunta.id] = resposta;

	};

};

exports.services.RestricoesLicenciamentoSimplificadoService = RestricoesLicenciamentoSimplificadoService;
