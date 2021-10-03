var NotificacaoController = function($scope, breadcrumb, $window, growlMessages, $routeParams, caracterizacaoService,
									 $location, mensagem, $q, notificacoesService) {

	var ctrl = this;

	ctrl.idEmpreendimento = $routeParams.idEmpreendimento;

	ctrl.idCaracterizacao = $routeParams.idCaracterizacao;
	ctrl.acao = $routeParams.acao;
	ctrl.anterior = anterior;
	ctrl.escolherEtapa = escolherEtapa;
	$scope.selecionarArquivo = selecionarArquivo;
	$scope.baixarDocumento = baixarDocumento;
	$scope.removerDocumento = removerDocumento;
	ctrl.irParaEtapa = irParaEtapa;
	ctrl.etapaEstaBloqueada = etapaEstaBloqueada;
	ctrl.modo = 'Visualizar';
	ctrl.renovacao = false;
	ctrl.acoesFluxoLicenca = app.ACOES_FLUXO_LICENCA;
	ctrl.justificativaNotificacaoEnviada = false;
	ctrl.justificativaEmpreendimentoEnviada = false;
	ctrl.justificativaSolicitacaoEnviada = false;
	$scope.notificacao = {};
	$scope.caracterizacao = {};
	$scope.documentosNotificacaoTecnica = [];
	$scope.documentosNotificacaoGeo = [];

	$scope.arquivosAnexados=["Arquivo 1", "Arquivo 2", "Arquivo 3"];

	/* A propriedade 'passoValido' é definida pelas respectivas controllers filhas */
	ctrl.etapas = {
		ATIVIDADE: {nome: 'Atividade e parâmetro', indice: 0, passoValido: undefined, class: 'empreendimento', disabled:false, beforeEscolherEtapa: undefined},
		GEO: {nome: 'Localização geográfica', indice: 1, passoValido: undefined, class: 'geo', disabled:false, beforeEscolherEtapa: undefined},
		CONDICOES: {nome: 'Condições', indice: 2, passoValido: undefined, class: 'condicao', disabled:false, beforeEscolherEtapa: undefined},
		ENQUADRAMENTO: {nome: 'Enquadramento da solicitação', indice: 3, passoValido: undefined, class: 'resumo', isUltimo: true, disabled:false}
	};

	ctrl.listaEtapas = [
		ctrl.etapas.ATIVIDADE,
		ctrl.etapas.GEO,
		ctrl.etapas.CONDICOES,
		ctrl.etapas.ENQUADRAMENTO
	];

	ctrl.tiposCaracterizacao = {
		DI: 'dispensa',
		SIMPLIFICADO: 'simplificado'
	};

	function getBreadcrumbTitle(etapa) {

		return ctrl.modo + ' Notificação';
	}

	function irParaEtapa(etapa) {

		growlMessages.destroyAllMessages();

		ctrl.etapa = etapa;

		breadcrumb.set([{title:'Empreendimento', href:'#/empreendimentos/listagem'},
			{title:'Solicitação', href:'#/empreendimento/'+ $routeParams.idEmpreendimento +'/caracterizacoes'},
			{title:getBreadcrumbTitle(etapa), href:''}]);
	}

	function escolherEtapa(etapa) {

		var funcoesParaExecutar = [];

		/*
			Se escolher uma etapa posterior e a etapa atual tiver implementado o método beforeEscolherEtapa,
			então chama este método para validar alguma regra.
		*/
		if(ctrl.etapa && etapa.indice > ctrl.etapa.indice && ctrl.modo !== 'Visualizar') {

			if(ctrl.etapa.beforeEscolherEtapa) {
				funcoesParaExecutar.push(ctrl.etapa.beforeEscolherEtapa);
			}

			// Caso pule uma etapa pelo step by step
			if(etapa.indice > (ctrl.etapa.indice + 1)) {
				var etapaAnterior = null;
				Object.keys(ctrl.etapas).forEach(function(index){
					if(ctrl.etapas[index].indice === (etapa.indice - 1)) {
						etapaAnterior = ctrl.etapas[index];
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
				});
		}
		else if(funcoesParaExecutar.length === 2) {

			$q.all([funcoesParaExecutar[0](), funcoesParaExecutar[1]()])
				.then(function(data) {
					if(data[0] && data[1]) {
						irParaEtapa(etapa);
					}
				});
		}
	}

	function selecionarArquivo(file) {

		// 2465792 = 2 * 1024 * 1024 = 2MB
		if(file && file.size > 25000000) {

			mensagem.warning('Os arquivos selecionados não devem exceder 25MB.', {ttl: 10000});

			return;
		}

		uploadArquivo(file);
	}

	function uploadArquivo(arquivo) {

		if (!arquivo){

			return;
		}

		notificacoesService.uploadDocumento($scope.caracterizacao.notificacao.id, arquivo)
			.then(function(response){
				$scope.caracterizacao.notificacao.documentos.push(response.data);
				mensagem.success('Documento adicionado com sucesso.', {dontScroll: true});
			})
			.catch(function(response){

				mensagem.warning(response.data.texto);
				return;
		});

	}

	function baixarDocumento(id) {
		location.href = notificacoesService.getRotaDownloadDocumento(id);
	}

    function removerDocumento(idDocumento) {
		const idNotificacao = $scope.caracterizacao.notificacao.id;
		notificacoesService.deleteDocumento(idNotificacao, idDocumento)
			.then(function(response){
				$scope.caracterizacao.notificacao.documentos = $scope.caracterizacao.notificacao.documentos.filter(d => d.id !== idDocumento);
				mensagem.success(response.data.texto, {ttl: 10000});
			})
			.catch(function(response){
				mensagem.warning(response.data.texto, {ttl: 10000});
				return;
			});
    }

	/*  Se todas as etapas anteriores não estiverem válidas, bloqueia a etapa atual.
	 Talvez seja uma forma pesada de fazer, mas é a mais simples e garante que as
	 etapas seguintes sejam bloqueadas se o usuário invalidar qualquer etapa anterior. */
	function etapaEstaBloqueada(etapa) {

		if (ctrl.modo === 'Visualizar') {

			return false;
		}

		if(etapa.disabled) {
			return true;
		}

		for (var i = 0; i < etapa.indice; i++) {
			if(!ctrl.listaEtapas[i].passoValido || !ctrl.listaEtapas[i].passoValido()){
				return true;
			}
		}

	}

	function anterior() {

		escolherEtapa(ctrl.listaEtapas[ctrl.etapa.indice - 1]);
	}

	function dragAndDrop(){
		let dropArea = document.getElementById('drop-area');
		dropArea.addEventListener('dragenter', handlerFunction, false);
		dropArea.addEventListener('drop', handlerFunction, false);

		;['dragenter', 'dragover'].forEach(eventName => {
			dropArea.addEventListener(eventName, highlight, false);
		})

		;['dragleave', 'drop'].forEach(eventName => {
			dropArea.addEventListener(eventName, unhighlight, false);
		});

		dropArea.addEventListener('drop', handleDrop, false);

		function handleDrop(e) {
			let dt = e.dataTransfer;
			let files = dt.files;
			handleFiles(files);
		}

	}

	$scope.listarSolicitacoes = () => {
		$location.path('/empreendimento/' + $routeParams.idEmpreendimento + '/caracterizacoes/');
	};

	escolherEtapa(ctrl.listaEtapas[0]);

	function findDadosCaracterizacao() {

		if(ctrl.idCaracterizacao){
			caracterizacaoService.dadosNotificacao(ctrl.idCaracterizacao).then((response) => {

				$scope.caracterizacao = response.data;

				if(ctrl.idCaracterizacao && $scope.caracterizacao.status.id === app.STATUS_CARACTERIZACAO.NOTIFICACAO_ATENDIDA.id){
					$scope.listarSolicitacoes();
				}

				if($scope.caracterizacao.notificacao.analiseTecnica){
					$scope.documentosNotificacaoTecnica =	_.findLast($scope.caracterizacao.notificacao.analiseTecnica.pareceresAnalistaTecnico,function(ultimoParecerTecnico){
							return ultimoParecerTecnico.documentos;
					});

				}else if($scope.caracterizacao.notificacao.analiseGeo){
					$scope.documentosNotificacaoGeo =	_.findLast($scope.caracterizacao.notificacao.analiseGeo.pareceresAnalistaGeo,function(ultimoParecerGeo){
						return ultimoParecerGeo.documentos;
				});


				}


				ctrl.justificativaNotificacaoEnviada = !!$scope.caracterizacao.notificacao.justificativaDocumentacao;
				ctrl.justificativaEmpreendimentoEnviada = !!$scope.caracterizacao.notificacao.justificativaRetificacaoEmpreendimento;
				ctrl.justificativaSolicitacaoEnviada = !!$scope.caracterizacao.notificacao.justificativaRetificacaoSolicitacao;

				if($routeParams.empreendimentoalteradovianotificacao && $scope.caracterizacao.status.id){
					caracterizacaoService.alterarStatusAposEditarRetificacao($scope.caracterizacao.id).then(function(response) {
						$scope.caracterizacao.status.id = app.STATUS_CARACTERIZACAO.NOTIFICADO_EMPREENDIMENTO_ALTERADO.id;
					}).catch( );
				}

			})
			.catch((response) => {

				mensagem.warning(response.data.texto, {dontScroll: true, referenceId: 15});
			});
		}

	}

	findDadosCaracterizacao();


	$scope.goToRetificacao = () => {
		$location.path('/empreendimento/' + $routeParams.idEmpreendimento + '/caracterizacao/' + $routeParams.idCaracterizacao + '/retificar');
	};

	$scope.redirectToEdicaoEmpreendimentos = (id) => {
		$location.path('/empreendimentos/editar/'+ $routeParams.idEmpreendimento +'/notificacao/'+ $routeParams.idCaracterizacao);
	};

	$scope.habilitarRetificacaoGeo = () => {
		return $scope.caracterizacao.notificacao.retificacaoSolicitacaoComGeo &&
			($scope.caracterizacao.status.id === app.STATUS_CARACTERIZACAO.NOTIFICADO.id ||
			$scope.caracterizacao.status.id === app.STATUS_CARACTERIZACAO.NOTIFICADO_EMPREENDIMENTO_ALTERADO.id) &&
			!$scope.caracterizacao.notificacao.justificativaRetificacaoSolicitacao;
	};

	$scope.podeRetificacarEmpreendimento = () => {
		return $scope.caracterizacao.notificacao.retificacaoEmpreendimento &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICADO_EMPREENDIMENTO_ALTERADO.id &&
			!$scope.caracterizacao.notificacao.justificativaRetificacaoEmpreendimento &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICACAO_ATENDIDA.id &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.AGUARDANDO_DOCUMENTACAO.id &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICADO_GEO_FINALIZADA.id &&
			!$scope.caracterizacao.notificacao.justificativaRetificacaoSolicitacao;
	};

	$scope.podeRetificacarSolicitacao = () => {
		return $scope.caracterizacao.notificacao.retificacaoSolicitacao &&
			(ctrl.justificativaEmpreendimentoEnviada ||
			$scope.caracterizacao.status.id === app.STATUS_CARACTERIZACAO.NOTIFICADO_EMPREENDIMENTO_ALTERADO.id ||
			!$scope.caracterizacao.notificacao.retificacaoEmpreendimento) &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICACAO_ATENDIDA.id &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.AGUARDANDO_DOCUMENTACAO.id &&
			!$scope.caracterizacao.notificacao.justificativaRetificacaoSolicitacao;
	};

	$scope.podeJustificarEmpreendimento = () => {
		return $scope.caracterizacao.notificacao.retificacaoEmpreendimento &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICADO_EMPREENDIMENTO_ALTERADO.id &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICACAO_ATENDIDA.id &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.AGUARDANDO_DOCUMENTACAO.id &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICADO_GEO_FINALIZADA.id &&
			!$scope.caracterizacao.notificacao.justificativaRetificacaoSolicitacao;
	};

	$scope.podeJustificarRetificacao = () => {
		return $scope.caracterizacao.notificacao.retificacaoSolicitacao &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICADO_EM_ANDAMENTO.id &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.AGUARDANDO_DOCUMENTACAO.id &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICADO_GEO_FINALIZADA.id &&
			(ctrl.justificativaEmpreendimentoEnviada ||
			$scope.caracterizacao.status.id === app.STATUS_CARACTERIZACAO.NOTIFICADO_EMPREENDIMENTO_ALTERADO.id ||
			!$scope.caracterizacao.notificacao.retificacaoEmpreendimento);
	};

	$scope.podeSalvarJustificativaEmpreendimento = () => {
		return $scope.caracterizacao.notificacao.retificacaoEmpreendimento &&
			$scope.caracterizacao.notificacao.justificativaRetificacaoEmpreendimento &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICADO_EMPREENDIMENTO_ALTERADO.id &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICACAO_ATENDIDA.id &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.AGUARDANDO_DOCUMENTACAO.id &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICADO_GEO_FINALIZADA.id;
	};

	$scope.podeSalvarJustificativaSoliticacao = () => {
		const n = $scope.caracterizacao.notificacao;

		//sem alterações geo e com documentação || com geo, sem documentacao
		if ( (n.retificacaoSolicitacao && !n.retificacaoSolicitacaoComGeo) ||
			(n.retificacaoSolicitacao && n.retificacaoSolicitacaoComGeo && !n.retificacaoEmpreendimento)) {

			return $scope.caracterizacao.notificacao.retificacaoSolicitacao &&
				$scope.caracterizacao.notificacao.justificativaRetificacaoSolicitacao &&
				$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICADO_EM_ANDAMENTO.id &&
				$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.AGUARDANDO_DOCUMENTACAO.id &&
				$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICADO_GEO_FINALIZADA.id;
		}

		return $scope.caracterizacao.notificacao.retificacaoSolicitacao &&
			$scope.caracterizacao.notificacao.justificativaRetificacaoSolicitacao &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICADO_EM_ANDAMENTO.id &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.AGUARDANDO_DOCUMENTACAO.id &&
			$scope.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICADO_GEO_FINALIZADA.id &&
			(ctrl.justificativaEmpreendimentoEnviada ||
				$scope.caracterizacao.status.id === app.STATUS_CARACTERIZACAO.NOTIFICADO_EMPREENDIMENTO_ALTERADO.id ||
				$scope.caracterizacao.notificacao.retificacaoEmpreendimento);
	};

	$scope.justificarDocumentacao = () => {

		let idNotificacao = $scope.caracterizacao.notificacao.id;
		let justificativa = $scope.caracterizacao.notificacao.justificativaDocumentacao;

		notificacoesService.justificarDocumentacao(idNotificacao,justificativa).then( result => {

			ctrl.justificativaNotificacaoEnviada = !!$scope.caracterizacao.notificacao.justificativaDocumentacao;
			mensagem.success(result.data.texto, {ttl: 10000});

		}).catch(() => {
			mensagem.warning('Não foi possível justificar a documentação!', {ttl: 10000});
		});

	};

	$scope.justificarRetificacao = () => {

		let idNotificacao = $scope.caracterizacao.notificacao.id;
		let justificativa = $scope.caracterizacao.notificacao.justificativaRetificacaoSolicitacao;

		notificacoesService.justificarRetificacao(idNotificacao,justificativa).then( result => {

			ctrl.justificativaSolicitacaoEnviada = !!$scope.caracterizacao.notificacao.justificativaRetificacaoSolicitacao;
			mensagem.success(result.data.texto, {ttl: 10000});

		}).catch(() => {
			mensagem.warning('Não foi possível justificar a documentação!', {ttl: 10000});
		});

	};

	$scope.justificarRetificacaoEmpreendimento = () => {

		let idNotificacao = $scope.caracterizacao.notificacao.id;
		let justificativa = $scope.caracterizacao.notificacao.justificativaRetificacaoEmpreendimento;

		notificacoesService.justificarRetificacaoEmpreendimento(idNotificacao,justificativa).then( result => {

			ctrl.justificativaEmpreendimentoEnviada = !!$scope.caracterizacao.notificacao.justificativaRetificacaoEmpreendimento;
			mensagem.success(result.data.texto, {ttl: 10000});
			findDadosCaracterizacao();

		}).catch(() => {
			mensagem.warning('Não foi possível justificar a documentação!', {ttl: 10000});
		});

	};

	$scope.enviar = () => {
		caracterizacaoService.finalizarNotificacaoSimplificado($scope.caracterizacao.id).then(() => {
			$scope.listarSolicitacoes();
			mensagem.success('Notificação atendida com sucesso!', {ttl: 10000});
		}).catch(() => {
			mensagem.warning('Não foi possível finalizar a retificação!', {ttl: 10000});
		});
	};

	$scope.download = (idDocumento) => {
		return notificacoesService.downloadAnaliseDocumento(idDocumento);
	};

	$scope.podeJustificarDocumentacao = () => {
		let n = $scope.caracterizacao.notificacao;
		return n && _.isEmpty(n.documentos);
	};

	$scope.documentacaoFinalizada = () => {
		let n = $scope.caracterizacao.notificacao;
		return n && (!n.documentacao || (!!n.justificativaDocumentacao && ctrl.justificativaNotificacaoEnviada) || !_.isEmpty(n.documentos));
	};

	$scope.retificacaoEmpreendimentoFinalizada = () => {
		let n = $scope.caracterizacao.notificacao;
		if(n && n.retificacaoSolicitacao && n.retificacaoEmpreendimento) {
			return (n.justificativaRetificacaoEmpreendimento && ctrl.justificativaEmpreendimentoEnviada) ||
				$scope.caracterizacao.status.id === app.STATUS_CARACTERIZACAO.NOTIFICADO_EMPREENDIMENTO_ALTERADO.id ||
				$scope.retificacaoSolicitacaoFinalizada();
		}
		return $scope.retificacaoSolicitacaoFinalizada();
	};

	$scope.retificacaoSolicitacaoFinalizada = () => {
		let n = $scope.caracterizacao.notificacao;
		if(n && n.retificacaoSolicitacao) {
			return (n.justificativaRetificacaoSolicitacao && ctrl.justificativaSolicitacaoEnviada) ||
				$scope.caracterizacao.status.id === app.STATUS_CARACTERIZACAO.NOTIFICADO_GEO_FINALIZADA.id ||
				$scope.caracterizacao.status.id === app.STATUS_CARACTERIZACAO.AGUARDANDO_DOCUMENTACAO.id ;
		}
		return true;
	};

	$scope.podeFinalizar = () => {
		const n = $scope.caracterizacao.notificacao;
		if (n != undefined && n.retificacaoSolicitacao && !n.retificacaoSolicitacaoComGeo){
			return $scope.documentacaoFinalizada() &&
				$scope.retificacaoSolicitacaoFinalizada();
		}
		return $scope.documentacaoFinalizada() &&
			$scope.retificacaoEmpreendimentoFinalizada() &&
			$scope.retificacaoSolicitacaoFinalizada();
	};

	$scope.voltar = () => {
		$location.path('/empreendimento/'+ $routeParams.idEmpreendimento +'/caracterizacoes');
	};


};

exports.controllers.NotificacaoController = NotificacaoController;
