var CadastrarCaracterizacoes = function($scope, breadcrumb, $route, growlMessages, $routeParams, caracterizacaoService, $location, mensagem, $q, tiposLicencaService, modalSimplesService, $rootScope) {

	var cadastro = this;

	cadastro.idEmpreendimento = $routeParams.idEmpreendimento;

	cadastro.idCaracterizacao = $routeParams.idCaracterizacao;
	cadastro.acao = $routeParams.acao;
	cadastro.botaoCancelar = botaoCancelar;
	cadastro.proximo = proximo;
	cadastro.anterior = anterior;
	cadastro.escolherEtapa = escolherEtapa;
	cadastro.irParaEtapa = irParaEtapa;
	cadastro.etapaEstaBloqueada = etapaEstaBloqueada;
	cadastro.cancelar = cancelar;
	cadastro.modo = 'Cadastrar';
	cadastro.renovacao = false;
	cadastro.retificacao = false;
	cadastro.acoesFluxoLicenca = app.ACOES_FLUXO_LICENCA;
	cadastro.tipoLicencaEvoluir = {};
	//cadastro.idTipoLicenca = idTipoLicenca;
	var licencaEmEvolucao;

	/* A propriedade 'passoValido' é definida pelas respectivas controllers filhas */
	cadastro.etapas = {
		ATIVIDADE: {nome: 'Atividade e parâmetro', indice: 0, passoValido: undefined, class: 'empreendimento', disabled:false, beforeEscolherEtapa: undefined},
		GEO: {nome: 'Localização geográfica', indice: 1, passoValido: undefined, class: 'geo', disabled:false, beforeEscolherEtapa: undefined},
		CONDICOES: {nome: 'Condições', indice: 2, passoValido: undefined, class: 'condicao', disabled:false, beforeEscolherEtapa: undefined},
		ENQUADRAMENTO: {nome: 'Enquadramento da solicitação', indice: 3, passoValido: undefined, class: 'resumo', isUltimo: true, disabled:false}
	};

	cadastro.listaEtapas = [
		cadastro.etapas.ATIVIDADE,
		cadastro.etapas.GEO,
		cadastro.etapas.CONDICOES,
		cadastro.etapas.ENQUADRAMENTO
	];

	cadastro.tiposCaracterizacao = {
		DI: 'dispensa',
		SIMPLIFICADO: 'simplificado'
	};

	cadastro.tipoCaracterizacao = cadastro.tiposCaracterizacao.DI;

	resetCaracterizacao();

	function resetCaracterizacao() {
		cadastro.caracterizacao = {
			empreendimento: {
				id: cadastro.idEmpreendimento
			},
			atividadesCaracterizacao: [],
			complexo: undefined
		};

		for(var index in cadastro.etapas) {
			cadastro.etapas[index].beforeEscolherEtapa = undefined;
		}

	}

	function getBreadcrumbTitle(etapa) {

		return cadastro.modo + ' (Passo ' + (etapa.indice + 1) + ' de ' + cadastro.listaEtapas.length + ')';
	}

	function irParaEtapa(etapa) {

		growlMessages.destroyAllMessages();

		cadastro.etapa = etapa;
		let etapaIncrement = false;

		if(cadastro.renovacao ){

			$scope.$on("onEmissor", function(evt,data){
				etapaIncrement = true;
				breadcrumb.set([{title:'Empreendimento', href:'#/empreendimentos/listagem'},
						{title:'Solicitação', href:'#/empreendimento/'+ $routeParams.idEmpreendimento +'/caracterizacoes'},
						{title: cadastro.modo + data.sigla + ' (Passo ' + (etapa.indice + 1) + ' de ' + cadastro.listaEtapas.length + ')',href:'' }]);
			});

			if(!etapaIncrement && $scope.nomeLicencaParaTitulo){
				breadcrumb.set([{title:'Empreendimento', href:'#/empreendimentos/listagem'},
					{title:'Solicitação', href:'#/empreendimento/'+ $routeParams.idEmpreendimento +'/caracterizacoes'},
					{title: cadastro.modo + $scope.nomeLicencaParaTitulo + ' (Passo ' + (etapa.indice + 1) + ' de ' + cadastro.listaEtapas.length + ')',href:'' }]);
			}

		}else{
			breadcrumb.set([{title:'Empreendimento', href:'#/empreendimentos/listagem'},
				{title:'Solicitação', href:'#/empreendimento/'+ $routeParams.idEmpreendimento +'/caracterizacoes'},
				{title:getBreadcrumbTitle(etapa), href:''}]);
		}

	}

	function botaoCancelar() {
		var configModal = {
			titulo: 'Confirmar cancelamento',
			conteudo: 'Você tem certeza que deseja cancelar o preenchimento da solicitação?'
		};

		var instanciaModal = modalSimplesService.abrirModal(configModal);

		instanciaModal.result.then(function() {
			if (cadastro.caracterizacao.retificacao){
				$location.path('/empreendimento/' + $routeParams.idEmpreendimento + '/caracterizacao/' + cadastro.idCaracterizacao + '/notificacao');
				return;
			}
			$location.path('/empreendimento/'+ $routeParams.idEmpreendimento +'/caracterizacoes');
		});
	}

	function escolherEtapa(etapa) {

		var funcoesParaExecutar = [];
		var todosBeforeValidos = true;

		/*
			Se escolher uma etapa posterior e a etapa atual tiver implementado o método beforeEscolherEtapa,
			então chama este método para validar alguma regra.
		*/
		if(cadastro.etapa && etapa.indice > cadastro.etapa.indice && cadastro.modo !== 'Visualizar') {

			if(cadastro.etapa.beforeEscolherEtapa) {
				funcoesParaExecutar.push(cadastro.etapa.beforeEscolherEtapa);
			}

			// Caso pule uma etapa pelo step by step
			if(etapa.indice > (cadastro.etapa.indice + 1)) {
				var etapaAnterior = null;
				Object.keys(cadastro.etapas).forEach(function(index){
					if(cadastro.etapas[index].indice === (etapa.indice - 1)) {
						etapaAnterior = cadastro.etapas[index];
					}
				});
				if(etapaAnterior && etapaAnterior.beforeEscolherEtapa) {
					funcoesParaExecutar.push(etapaAnterior.beforeEscolherEtapa);
				}
			}

		}

		if(funcoesParaExecutar.length === 0) {
			irParaEtapa(etapa);
		}
		else if(funcoesParaExecutar.length === 1) {

			$q.all([funcoesParaExecutar[0]()])
				.then(function(data) {
					if(data[0]) {
						irParaEtapa(etapa);
					}
					else {
						return;
					}
				})
				.catch(function() {
					return;
				});
		}
		else if(funcoesParaExecutar.length === 2) {

			$q.all([funcoesParaExecutar[0](), funcoesParaExecutar[1]()])
				.then(function(data) {
					if(data[0] && data[1]) {
						irParaEtapa(etapa);
					}
					else {
						return;
					}
				})
				.catch(function() {
					return;
				});
		}
		else {
			return;
		}
	}

	/*  Se todas as etapas anteriores não estiverem válidas, bloqueia a etapa atual.
	 Talvez seja uma forma pesada de fazer, mas é a mais simples e garante que as
	 etapas seguintes sejam bloqueadas se o usuário invalidar qualquer etapa anterior. */
	function etapaEstaBloqueada(etapa) {


		if (cadastro.modo === 'Visualizar') {

			return false;
		}

		if(etapa.disabled) {
			return true;
		}

		for (var i = 0; i < etapa.indice; i++) {
			if(!cadastro.listaEtapas[i].passoValido || !cadastro.listaEtapas[i].passoValido()){
				return true;
			}
		}

	}

	function proximo() {

		escolherEtapa(cadastro.listaEtapas[cadastro.etapa.indice + 1]);

	}

	function anterior() {

		escolherEtapa(cadastro.listaEtapas[cadastro.etapa.indice - 1]);

	}

	function cancelar() {
		$location.path('/empreendimento/'+ $routeParams.idEmpreendimento +'/caracterizacoes');
	}

	function findDadosCaracterizacao() {

		if(cadastro.idCaracterizacao && (cadastro.renovacao || cadastro.retificacao)){
			caracterizacaoService.dadosRenovacao(cadastro.idCaracterizacao)
			.then(function(response) {

				cadastro.caracterizacao = response.data;
				cadastro.caracterizacao.renovacao = cadastro.renovacao;
				cadastro.caracterizacao.retificacao = cadastro.retificacao;

				cadastro.tipoCaracterizacao = cadastro.tiposCaracterizacao.SIMPLIFICADO;

				cadastro.listaFluxoLicencas = [];

				if(!cadastro.etapas.DOCUMENTACAO) {
					cadastro.etapas.DOCUMENTACAO = {nome: 'Documentação', indice: 3, passoValido: undefined, class: 'documentacao', disabled: false};
					cadastro.etapas.ENQUADRAMENTO.indice += 1;
					cadastro.listaEtapas.splice(cadastro.etapas.DOCUMENTACAO.indice, 0, cadastro.etapas.DOCUMENTACAO);
				}

				cadastro.tipologia = cadastro.caracterizacao.tipologia;
				if(!cadastro.caracterizacao.tipoLicenca){
					cadastro.caracterizacao.tipoLicenca = cadastro.caracterizacao.tiposLicencaEmAndamento[0];
				}

				if(cadastro.caracterizacao.renovacao) {
					cadastro.fluxoLicencas = tiposLicencaService.listarFluxo().then((response) => {
						cadastro.listaFluxoLicencas = response.data.fluxos;
						cadastro.tipoLicencaEvoluir = cadastro.listaFluxoLicencas[cadastro.caracterizacao.tipoLicenca.sigla][cadastro.acao];
						cadastro.listaSelecionada = [cadastro.tipoLicencaEvoluir];
						licencaEmEvolucao = cadastro.tipoLicencaEvoluir !== undefined? cadastro.tipoLicencaEvoluir : false;

						$scope.$broadcast('onEmissor',licencaEmEvolucao);
						$scope.nomeLicencaParaTitulo = licencaEmEvolucao.sigla;
					})
					.catch(() => cancelar());
				}

				setDadosRenovacaoLicenca(cadastro.caracterizacao);

				getValorParametros();

				tratarDadosQuestionario(cadastro.caracterizacao.questionario3);

				if (cadastro.caracterizacao.status.id === app.STATUS_CARACTERIZACAO.NOTIFICADO_EM_ANDAMENTO.id) {

					cadastro.continuacao = true;
					cadastro.listaEtapas[0].disabled = true;
					cadastro.listaEtapas[1].disabled = true;
					cadastro.listaEtapas[2].disabled = true;
					cadastro.etapas.ATIVIDADE.beforeEscolherEtapa = function() {
						return true;
					};
					cadastro.etapas.GEO.beforeEscolherEtapa = function() {
						return true;
					};
					cadastro.etapas.CONDICOES.beforeEscolherEtapa = function() {
						return true;
					};

					if(cadastro.caracterizacao.declaracaoVeracidadeInformacoes) {
						escolherEtapa(cadastro.listaEtapas[4]);
						cadastro.listaEtapas[3].disabled = true;
					}else {
						escolherEtapa(cadastro.listaEtapas[3]);
					}

				}

				if($routeParams.empreendimentoalteradovianotificacao){
					caracterizacaoService.alterarStatusAposEditarRetificacao(cadastro.idCaracterizacao).then(function() {
						cadastro.caracterizacao.status.id = app.STATUS_CARACTERIZACAO.NOTIFICADO_EMPREENDIMENTO_ALTERADO.id;
					}).catch( mensagem.warning('Não foi possível obter dados do empreendimento!', {ttl: 10000}) );
				}

			})
			.catch(() => cancelar());

		} else if(cadastro.idCaracterizacao) {

			caracterizacaoService.dadosCaracterizacao(cadastro.idCaracterizacao)
			.then(function(response) {

				cadastro.caracterizacao = response.data;

				cadastro.tipoCaracterizacao = cadastro.tiposCaracterizacao.SIMPLIFICADO;

				if(!cadastro.etapas.DOCUMENTACAO) {

					cadastro.etapas.DOCUMENTACAO = {nome: 'Documentação', indice: 3, passoValido: undefined, class: 'documentacao', disabled: false};
					cadastro.etapas.ENQUADRAMENTO.indice += 1;
					cadastro.listaEtapas.splice(cadastro.etapas.DOCUMENTACAO.indice, 0, cadastro.etapas.DOCUMENTACAO);
				}

				if (cadastro.modo === 'Cadastrar') {

					cadastro.continuacao = true;
					cadastro.listaEtapas[0].disabled = true;
					cadastro.listaEtapas[1].disabled = true;
					cadastro.listaEtapas[2].disabled = true;
					cadastro.etapas.ATIVIDADE.beforeEscolherEtapa = function() {
						return true;
					};
					cadastro.etapas.GEO.beforeEscolherEtapa = function() {
						return true;
					};
					cadastro.etapas.CONDICOES.beforeEscolherEtapa = function() {
						return true;
					};

					if(cadastro.caracterizacao.declaracaoVeracidadeInformacoes) {

						escolherEtapa(cadastro.listaEtapas[4]);
						cadastro.listaEtapas[3].disabled = true;
					}else {
						escolherEtapa(cadastro.listaEtapas[3]);
					}

				}

				cadastro.tipologia = cadastro.caracterizacao.tipologia;
				$scope.cadastro.tipologia = cadastro.caracterizacao.tipologia;
			})
			.catch(function(response){
				cancelar();
			});
		}

	}

	function setDadosRenovacaoLicenca(caracterizacao) {
		$scope.cadastro.tipologia = caracterizacao.tipologia;
		$scope.cadastro.descricao = caracterizacao.descricao;
		$scope.cadastro.finalidadeLicenca = caracterizacao.tipoLicenca.finalidade;
		$scope.licencasEmAndamento = undefined;
		//$scope.cadastro.caracterizacao.renovacao = true;

		if(!caracterizacao.tipoLicenca){
			$scope.licencasEmAndamento = cadastro.caracterizacao.tiposLicencaEmAndamento[0];
			caracterizacao.tipoLicenca = cadastro.caracterizacao.tiposLicencaEmAndamento[0];
		}else{
			$scope.licencasEmAndamento = caracterizacao.tipoLicenca;
		}
	}

	function getValorParametros() {

		var atividadesCaracterizacao = cadastro.caracterizacao.atividadesCaracterizacao;

		_.forEach(atividadesCaracterizacao, function(ac) {
			_.forEach(ac.atividade.parametros, function(parametro, index) {
				parametro.valorParametro = ac.atividadeCaracterizacaoParametros[index].valorParametro;
			});
		});
	}

	function tratarDadosQuestionario(questionario) {
		if(questionario){
			questionario.consumoAgua = questionario.consumoAgua ? "true" : "false";
			questionario.efluentes = questionario.efluentes ? "true" : "false";
			questionario.residuosSolidos = questionario.residuosSolidos ? "true" : "false";
		}
	}

	if ($route.current.$$route.originalPath.search('visualizar') !== -1) {

		irParaEtapa(cadastro.etapas.ENQUADRAMENTO);
		cadastro.modo = 'Visualizar';
		cadastro.continuacao = true;

	} else if ($route.current.$$route.originalPath.search('editar') !== -1) {

		cadastro.modo = 'Editar';
		cadastro.idCaracterizacao = $routeParams.idCaracterizacao;
		irParaEtapa(cadastro.etapas.ATIVIDADE);

	} else if ($route.current.$$route.originalPath.search('renovar') !== -1) {
		cadastro.idCaracterizacao = $routeParams.idCaracterizacao;
		cadastro.acao = $routeParams.acao;
		cadastro.modo = cadastro.acoesFluxoLicenca[cadastro.acao].descricao;
		cadastro.renovacao = true;
		irParaEtapa(cadastro.etapas.ATIVIDADE);

	} else if ($route.current.$$route.originalPath.search('retificar') !== -1) {

		cadastro.idCaracterizacao = $routeParams.idCaracterizacao;
		cadastro.retificacao = true;
		cadastro.modo = 'Retificar';
		irParaEtapa(cadastro.etapas.ATIVIDADE);

	} else {

		escolherEtapa(cadastro.listaEtapas[0]);
	}

	findDadosCaracterizacao();

	cadastro.nomePagina = function nomePagina() {
		return cadastro.modo + (cadastro.renovacao ? cadastro.tipoLicencaEvoluir.sigla : ' Solicitação');
	};

};

exports.controllers.CadastrarCaracterizacoes = CadastrarCaracterizacoes;
