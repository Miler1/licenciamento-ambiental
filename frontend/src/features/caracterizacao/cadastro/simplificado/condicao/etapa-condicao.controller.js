var CaracterizacaoLSACondicoesController = function($scope, $location, $routeParams, mensagem, caracterizacaoService, $q,
													restricoesLicenciamentoService, modalSimplesService) {

	var etapaCondicoes = this;

	etapaCondicoes.passoValido = passoValido;
	etapaCondicoes.proximo = proximo;
	etapaCondicoes.validarPerguntasForaQuestionario = validarPerguntasForaQuestionario;
	etapaCondicoes.respostas = {};
	etapaCondicoes.questionario = $scope.cadastro.caracterizacao.questionario3 ? $scope.cadastro.caracterizacao.questionario3 : {};

	etapaCondicoes.restricoesMZEE = null;
	etapaCondicoes.restricoesCAR = null;

	etapaCondicoes.nomesAreasSobrepostas = {
		TERRA_INDIGENA: "Terra indígena",
		UNIDADE_CONSERVACAO: "Unidade de conservação",
		AREA_MILITAR: "Área militar",
		ZONA_CONSOLIDACAO: "Zona de consolidação",
		AREA_QUILOMBOLA: "Área quilombola",
		AREA_CONSOLIDADA: "Área consolidada",
		ARL_PROPOSTA: "Reserva legal proposta",
		ARL_AVERBADA: "Reserva legal averbada",
		ARL_APROVADA_NAO_AVERBADA: "Reserva legal aprovada e não averbada"
	};

	$scope.cadastro.etapas.CONDICOES.passoValido = passoValido;
	$scope.cadastro.etapas.CONDICOES.beforeEscolherEtapa = beforeEscolherEtapa;
	etapaCondicoes.validadorPerguntas = new app.ValidadorPerguntasQuestionario3();

	$scope.$on('atividadeAlterada', function(event, args) {
		etapaCondicoes.respostas = {};
		etapaCondicoes.questionario = {};
	});

	function passoValido() {

		if($scope.cadastro.continuacao) {
			return true;
		}

		return todasAsPerguntasForamRespondidas();

	}

	function beforeEscolherEtapa() {

		var deferred = $q.defer();

		if(!etapaCondicoes.passoValido()){

			$scope.formCondicaoQuestionario.$setSubmitted();

			if(!todasAsPerguntasForamRespondidas()) {

				mensagem.warning('Todas as perguntas devem ser respondidas ou suas respostas não permitem prosseguir com a solicitação.');
				deferred.reject();
				return deferred.promise;
			}

			if(etapaCondicoes.tituloOutorgaValido !== undefined && etapaCondicoes.tituloOutorgaValido === false) {

				mensagem.error('O titulo de outorga não foi preenchido ou não foi encontrado ou está vencido. Procure a SEMA para regularizar a situação da outorga.');
				deferred.reject();
				return deferred.promise;
			}
		}

		$scope.cadastro.caracterizacao.questionario3 = etapaCondicoes.questionario;

		$scope.cadastro.caracterizacao.respostas = montaObjRespostas();

		var salvouCaracterizacao = false;

		if(!$scope.cadastro.caracterizacao || !$scope.cadastro.caracterizacao.id || $scope.cadastro.renovacao || $scope.cadastro.retificacao){

			// $scope.cadastro.caracterizacao.renovacao = false;
			salvouCaracterizacao = salvarCaracterizacao();
			if(salvouCaracterizacao) {
				deferred.resolve();
				return deferred.promise;
			}
			else {
				deferred.reject();
				return deferred.promise;
			}

		} else if ($scope.cadastro.modo === 'Editar') {

			salvouCaracterizacao = updateCaracterizacao();
			if(salvouCaracterizacao) {
				deferred.resolve();
				return deferred.promise;
			}
			else {
				deferred.reject();
				return deferred.promise;
			}
		}

		deferred.resolve();
		return deferred.promise;
	}

	function montaObjRespostas() {

		var idRespostas = [];

		_.each(etapaCondicoes.respostas, function(resposta){

			idRespostas.push({
				id: resposta.id
			});
		});

		return idRespostas;

	}

	function proximo() {

		//Validações serão feitas no método beforeEscolherEtapa
		$scope.cadastro.proximo();
	}

	function unirGeometriasAtividade() {

		$scope.cadastro.caracterizacao.atividadesCaracterizacao.forEach((ac) => {

			if(!(_.isEmpty(ac.geometriasAtividade.features))) {

				ac.geometriasAtividade = ac.geometriasAtividade.features.map((f) => {
					return {geometria: unionGeometries(f)};
				});
			}
		});
	}

	function unirGeometriasComplexo() {

		if(!(_.isEmpty($scope.cadastro.caracterizacao.geometriasComplexo.features))) {

			$scope.cadastro.caracterizacao.geometriasComplexo = $scope.cadastro.caracterizacao.geometriasComplexo
			.features.map((f) => {
				return {geometria: unionGeometries(f)};
			});
		}
	}

	unionGeometries = (geometries) => {
		if(geometries.type === 'FeatureCollection') {
			let reducer = (accumulator, currentValue) => turf.union(accumulator, currentValue);
			return geometries.features.reduce(reducer);
		}
		return geometries.geometry;
	};


	function adequarGeometrias() {
		if($scope.cadastro.caracterizacao.complexo) {

			unirGeometriasComplexo();

			// var geometriaComplexo = $scope.cadastro.caracterizacao.geometriasComplexo;
			//
			// if(!(_.isEmpty(geometriaComplexo.features))) {
			//
			// 	var novasGeometrias = [];
			//
			// 	geometriaComplexo.features.forEach(function(f) {
			// 		var geometryCollection = {type: "GeometryCollection", geometries: []};
			// 		geometryCollection.geometries.push(f.geometry);
			// 		novasGeometrias.push({geometria: geometryCollection});
			// 	});
			//
			// 	$scope.cadastro.caracterizacao.geometriasComplexo = novasGeometrias;
			// }

			_.forEach($scope.cadastro.caracterizacao.atividadesCaracterizacao , function(ac){

				ac.geometriasAtividade = [];
			});

		} else {

			unirGeometriasAtividade();

			$scope.cadastro.caracterizacao.geometriasComplexo = [];
		}
	}

	function salvarCaracterizacao() {
		$q.all([verificarSobreposicoesNaoPermitidasSimplificado($scope.cadastro.caracterizacao)])
			.then(function(data){

				var sobreposicaoNaoPermitida = data[0];

				if(!sobreposicaoNaoPermitida) {

					// Adição de elementos selecionados da lista escolhida
					if($scope.cadastro.renovacao || $scope.cadastro.retificacao) {
						$scope.cadastro.caracterizacao.tipoLicenca = $scope.cadastro.listaSelecionada ?
							$scope.cadastro.listaSelecionada[0] : $scope.cadastro.caracterizacao.tipoLicenca;
					}else {
						$scope.cadastro.caracterizacao.tiposLicencaEmAndamento =
							$scope.cadastro.listaSelecionada.filter((element) => element.selecionado);
					}

					adequarGeometrias();

					caracterizacaoService.saveSimplificado($scope.cadastro.caracterizacao)
						.then(function(response){
							var caracterizacaoSalva = response.data;
							// Adiciona atributos retornados, como o id, ao objeto 'caracterizacao' atual
							_.extend($scope.cadastro.caracterizacao, caracterizacaoSalva);
							$location.path('/empreendimento/' + $routeParams.idEmpreendimento + '/caracterizacao/' + $scope.cadastro.caracterizacao.id + '/edit');
							return true;
						})
						.catch(function(response){
							if(response && response.data && response.data.texto) {
								mensagem.error(response.data.texto, {ttl: 15000});
							}
							else {
								mensagem.error('Falha ao salvar rascunho da solicitação.' , {ttl: 15000});
							}
							return false;
						});
				}else {
					mensagem.warning('A localização da atividade está em sobreposição com áreas restritas.');
					return false;
				}
		});
	}

	function updateCaracterizacao() {

		unirGeometriasAtividade();

		var caracterizacao = JSON.parse(JSON.stringify($scope.cadastro.caracterizacao));

		caracterizacao.respostas = montaObjRespostas();
		caracterizacao.porteEmpreendimento = null;

		_.forEach(caracterizacao.atividadesCaracterizacao, function(atividade) {

			_.forEach(atividade.geometriasAtividade, function(geometria) {

				geometria.area = null;
			});
		});

		if($scope.cadastro.renovacao) {
			caracterizacao.tipoLicenca = $scope.licencasEmAndamento;
		}

		caracterizacaoService.updateSimplificado(caracterizacao)
			.then(function(response){
				var caracterizacaoSalva = response.data;
				// Adiciona atributos retornados, como o id, ao objeto 'caracterizacao' atual
				_.extend($scope.cadastro.caracterizacao, caracterizacaoSalva);
				$scope.cadastro.irParaEtapa($scope.cadastro.etapas.DOCUMENTACAO);
				return true;
			})
			.catch(function(response){
				mensagem.error(response.data.texto, {ttl: 15000});
				return false;
			});
	}

	var configModal = {
		titulo: 'Solicitação de licença',
		conteudo: '<span>Os requisitos informados NÃO atendem a Resolução COEMA nº 127 de 16 de Novembro de 2016.</span>' +
				'<br><strong>Seu licenciamento foi classificado como licenciamento ordinário.</strong>',
		conteudoDestaque: 'Dirija-se a SEMA para abertura do processo.',
		labelBotaoConfirmar: 'Cancelar solicitação de licença',
		labelBotaoCancelar: 'Fechar'
	};

	function todasAsPerguntasForamRespondidas() {

		return validarPerguntasForaQuestionario() && etapaCondicoes.validadorPerguntas.validar(etapaCondicoes.questionario);
	}

	function validarPerguntasForaQuestionario() {

		var valido = true;

		_($scope.cadastro.caracterizacao.atividadesCaracterizacao).each(function (atividadeCaracterizacao) {
			_(atividadeCaracterizacao.atividade.perguntas).each(function (pergunta) {
				if (valido) {
					if(etapaCondicoes.respostas[pergunta.id]){
						valido = etapaCondicoes.respostas[pergunta.id].permiteLicenciamento;
					} else {
						valido = false;
					}
				}
			});
		});

		return valido;
	}

	function verificarSobreposicoesNaoPermitidasSimplificado(caracterizacao) {

		var geometrias = [];

		if(caracterizacao.complexo){

			var features = caracterizacao.geometriasComplexo.features;
			if(features) {
				features.forEach(function(f) {
					geometrias.push(JSON.stringify(f.geometry));
				});
			}

		} else {

			caracterizacao.atividadesCaracterizacao.forEach(function(ac) {
				var features = ac.geometriasAtividade.features;
				if(features) {
					features.forEach(function(f) {
						geometrias.push(JSON.stringify(f.geometry));
					});
				}
			});
		}


		var deffered = $q.defer();
		caracterizacaoService.sobreposicoesNaoPermitidasSimplificado(geometrias)
			.then(function(response) {
				deffered.resolve(response.data);
			})
			.catch(function() {
				deffered.reject('"Não foi possível obter as sobreposições."');
			});

		return deffered.promise;
	}

};

exports.controllers.CaracterizacaoLSACondicoesController = CaracterizacaoLSACondicoesController;
