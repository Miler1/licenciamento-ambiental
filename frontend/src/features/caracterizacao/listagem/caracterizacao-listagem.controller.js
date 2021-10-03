var ListagemCaracterizacoes = function($scope, breadcrumb, config, empreendimentoService,
									$routeParams, $location, caracterizacaoService, $uibModal, $route, municipioService) {

	var listagem = this;

	listagem.onPaginaAlterada = onPaginaAlterada;
	listagem.caracterizacoes = [];
	listagem.getCaracterizacoes = getCaracterizacoes;

	$scope.tipoLicencaDDLA = app.TIPOS_LICENCA_DISPENSA.DDLA;

	$scope.tipoLicencaLP = app.TIPOS_LICENCA_DISPENSA.LP;

	$scope.statusCaracterizacao = app.STATUS_CARACTERIZACAO;

	$scope.tiposLicencas = app.TIPOS_LICENCAS;

	$scope.tiposLicencaAtualizacao = app.TIPOS_LICENCA_ATUALIZACAO;

	$scope.tiposLicencaSimplificado = app.TIPOS_LICENCA_SIMPLIFICADO;

	$scope.tipoLicencaRenovacao = app.TIPOS_LICENCA_RENOVACAO;

	$scope.tipoCaracterizacao = app.TIPO_CARACTERIZACAO;

	$scope.acoesFluxoLicenca = app.ACOES_FLUXO_LICENCA;

	listagem.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);

	listagem.campo = 'Processo';
	// listagem.ordenacaoReversa = true;

	let novaFase = false;

	function ordenarPor(campo, ordenacaoReversa) {

		let ordenacao = 'NUMERO_PROCESSO_CARACT_ASC';

		if(campo === 'Processo' && ordenacaoReversa === true)
			ordenacao = 'NUMERO_PROCESSO_ASC';
		else if(campo === 'Processo' && ordenacaoReversa === false)
			ordenacao = 'NUMERO_PROCESSO_DESC';
		else if(campo === 'Protocolo' && ordenacaoReversa === true)
			ordenacao = 'NUMERO_CARACTERIZACAO_ASC';
		else if(campo === 'Protocolo' && ordenacaoReversa === false)
			ordenacao = 'NUMERO_CARACTERIZACAO_DESC';
		else if(campo === 'NumeroDla' && ordenacaoReversa === true)
			ordenacao = 'NUMERO_DLA_ASC';
		else if(campo === 'NumeroDla' && ordenacaoReversa === false)
			ordenacao = 'NUMERO_DLA_DESC';
		else if(campo === 'DataCadastro' && ordenacaoReversa === true)
			ordenacao = 'DATA_ASC';
		else if(campo === 'DataCadastro' && ordenacaoReversa === false)
			ordenacao = 'DATA_DESC';
		else if(campo === 'Status' && ordenacaoReversa === true)
			ordenacao = 'STATUS_ASC';
		else if(campo === 'Status' && ordenacaoReversa === false)
			ordenacao = 'STATUS_DESC';

		return ordenacao;

	}

	function onPaginaAlterada() {

		getCaracterizacoes();
	}

	function getlist(response) {

		listagem.caracterizacoes = response.data.pageItems;
		listagem.paginacao.update(response.data.totalResults, listagem.paginacao.paginaAtual);
	}

	function getCaracterizacoes(campo, ordenacaoReversa) {

		listagem.campo = campo || listagem.campo;
		listagem.ordenacaoReversa = ordenacaoReversa || listagem.ordenacaoReversa;

		var ordenacao = ordenarPor(listagem.campo, listagem.ordenacaoReversa);

		caracterizacaoService.list(
			$routeParams.idEmpreendimento,
			listagem.paginacao.paginaAtual,
			listagem.paginacao.itensPorPagina,
			ordenacao
		).then(getlist);

	}

	getCaracterizacoes();

	function getEmpreendimentoPorId() {

		empreendimentoService.buscarPessoa($routeParams.idEmpreendimento).then(function (response) {

			listagem.empreendimento = response.data;

		});
	}

	getEmpreendimentoPorId();

	listagem.cadastrarCaracterizacao = function() {

		$location.path('/empreendimento/' + $routeParams.idEmpreendimento + '/caracterizacoes/cadastrar');

	};

	$scope.baixarDla = function(idCaracterizacao) {

		caracterizacaoService.baixarDla(idCaracterizacao);

	};

	$scope.visualizarCaracterizacao = (idCaracterizacao) => {
		$location.path('/empreendimento/'  + $routeParams.idEmpreendimento + '/caracterizacoes/' + idCaracterizacao + '/visualizar');
	};

	$scope.mostrarBotaoRenovacao = function(caracterizacao, statusCaracterizacao, tiposLicencaSimplificado, tiposLicencaAtualizacao) {
		const licenca = caracterizacao.tipoLicenca || caracterizacao.tiposLicencaEmAndamento[0];
		return (
			(caracterizacao.status.id === statusCaracterizacao.FINALIZADO.id || caracterizacao.status.id === statusCaracterizacao.VENCIDO.id) &&
			!caracterizacao.bloqueada &&
			licenca &&
			licenca.id !== tiposLicencaAtualizacao.ALP.id &&
			licenca.id !== tiposLicencaSimplificado.LP.id &&
			licenca.id !== $scope.tipoLicencaDDLA.id
		);
	};

	$scope.mostrarBotaoAtualizacao = function(caracterizacao, statusCaracterizacao, tiposLicencaSimplificado) {
		const licenca = caracterizacao.tipoLicenca || caracterizacao.tiposLicencaEmAndamento[0];
		return (
			(caracterizacao.status.id === statusCaracterizacao.FINALIZADO.id || caracterizacao.status.id === statusCaracterizacao.VENCIDO.id) &&
			!caracterizacao.bloqueada &&
			licenca &&
			licenca.id === tiposLicencaSimplificado.LP.id &&
			licenca.id !== $scope.tipoLicencaDDLA.id
		);
	};

	$scope.mostrarBotaoNovaFase = function(caracterizacao, statusCaracterizacao, tiposLicencaSimplificado, tipoLicencaRenovacao) {
		novaFase = true;
		const licenca = caracterizacao.tipoLicenca || caracterizacao.tiposLicencaEmAndamento[0];
		return (
			(caracterizacao.status.id === statusCaracterizacao.FINALIZADO.id || caracterizacao.status.id === statusCaracterizacao.VENCIDO.id) &&
			!caracterizacao.bloqueada &&
			licenca &&
			licenca.id !== tiposLicencaSimplificado.LO.id &&
			licenca.id !== tipoLicencaRenovacao.RLO.id &&
			licenca.id !== $scope.tipoLicencaDDLA.id &&
			licenca.id !==	tiposLicencaSimplificado.LAU.id
		);
	};

	$scope.continuarCaracterizacao = function(idCaracterizacao) {

		$location.path('/empreendimento/' + $routeParams.idEmpreendimento + '/caracterizacao/' + idCaracterizacao + '/edit');

	};

	$scope.removerSolicitacao = function(caracterizacao) {

		var modalInstance = $uibModal.open({
			controller: 'modalRemoverLicencaController',
			controllerAs: 'modalCtrl',
			backdrop: 'static',
			keyboard: false,
			templateUrl: './features/caracterizacao/listagem/modal-remover-licenca.html',
			resolve: {
				caracterizacao: function () {
					return caracterizacao;
				}
			}
		});

		modalInstance.result.then(function (reload) {

			if (reload){
				$route.reload();
			}

		});

	};

	$scope.atualizarCaracterizacao = function(idCaracterizacao,acao) {
		$location.path('/empreendimento/' + $routeParams.idEmpreendimento + '/caracterizacao/' + idCaracterizacao + '/renovar/' + acao);
	};

	$scope.irParaNotificacao = function(idCaracterizacao) {
		$location.path('/empreendimento/' + $routeParams.idEmpreendimento + '/caracterizacao/' + idCaracterizacao + '/notificacao');
	};

	$scope.emitirDae = function(caracterizacao) {

		var modalInstance = $uibModal.open({
			controller: 'modalDadosDaeController',
			controllerAs: 'modalCtrl',
			backdrop: 'static',
			keyboard  : false,
			templateUrl: './features/caracterizacao/cadastro/common/modal-dados-dae.html',
			resolve: {
				caracterizacao: function () {
					return caracterizacao;
				}
			}
		});

		modalInstance.result.then(function (reload) {

			if (reload){

				$route.reload();
			}
		});
	};

	$scope.emitirDaeLicenciamento = function(caracterizacao) {

		var modalInstance = $uibModal.open({
			controller: 'modalDadosDaeLicenciamentoController',
			controllerAs: 'modalCtrl',
			backdrop: 'static',
			keyboard  : false,
			templateUrl: './features/caracterizacao/cadastro/common/modal-dados-dae-licenciamento.html',
			resolve: {
				caracterizacao: function () {
					return caracterizacao;
				}
			}
		});

		modalInstance.result.then(function (reload) {

			if (reload){

				$route.reload();
			}
		});
	};

	$scope.baixarDae = function(caracterizacao) {

		caracterizacaoService.downloadDaeCaracterizacao(caracterizacao.id);
	};

	$scope.baixarDaeLicenciamento = function(caracterizacao) {

		caracterizacaoService.downloadDaeLicenciamentoCaracterizacao(caracterizacao.id);
	};

	$scope.abrirModalListagemNotificacoes = function(caracterizacao) {

		var modalInstance = $uibModal.open({
			controller: 'modalListagemNotificacoesController',
			controllerAs: 'modalCtrl',
			backdrop: 'static',
			keyboard  : false,
			templateUrl: './features/caracterizacao/listagem/modal-listagem-notificacoes.html',
			resolve: {
				idCaracterizacao: function () {
					return caracterizacao.id;
				},
				numero: function () {
					return caracterizacao.numero;
				}
			}
		});

		modalInstance.result.then(function (reload) {

			if (reload){

				$route.reload();
			}
		});
	};

	$scope.abrirModalCancelarDla = function(caracterizacao) {

		var modalInstance = $uibModal.open({
			controller: 'ModalDICancelamentoController',
			controllerAs: 'modalCtrl',
			backdrop: 'static',
			keyboard: false,
			templateUrl: './features/caracterizacao/listagem/modal-di-cancelamento.html',
			resolve: {
				idCaracterizacao: function () {
					return caracterizacao.id;
				},
				numero: function () {
					return caracterizacao.numero;
				}
			}
		});

		modalInstance.result.then(function (reload) {

			if (reload){

				$route.reload();
			}
		});
	};

	$scope.baixarLicenca = function(caracterizacao) {

		if (caracterizacao.numero){

			caracterizacaoService.baixarLicenca(caracterizacao.id);
		}

	};

	$scope.renovarLicenca = function(caracterizacao) {

		$location.path('/empreendimento/' + $routeParams.idEmpreendimento + '/caracterizacao/' + caracterizacao.id + '/renovar');

	};

	breadcrumb.set([{title:'Empreendimento', href:'#/empreendimentos/listagem'}, {title:'Solicitação', href:''}]);

	$scope.mostrarIconeNotificacao = function (caracterizacao, statusCaracterizacao) {
		return caracterizacao.status.id === statusCaracterizacao.NOTIFICADO.id ||
			caracterizacao.status.id === statusCaracterizacao.NOTIFICADO_EM_ANDAMENTO.id ||
			caracterizacao.status.id === statusCaracterizacao.AGUARDANDO_DOCUMENTACAO.id ||
			caracterizacao.status.id === statusCaracterizacao.NOTIFICADO_EMPREENDIMENTO_ALTERADO.id ||
			caracterizacao.status.id === statusCaracterizacao.NOTIFICADO_GEO_FINALIZADA.id;
	};

};

exports.controllers.ListagemCaracterizacoes = ListagemCaracterizacoes;
