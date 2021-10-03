var CaracterizacaoLSAEnquadramentoController = function($scope, mensagem, config, caracterizacaoService, modalSimplesService, $location, $rootScope, $uibModal, $routeParams, documentoService) {

	var etapaEnquadramento = this;

	etapaEnquadramento.proximo = proximo;
	etapaEnquadramento.getTotalGeral = getTotalGeral;
	etapaEnquadramento.isEmpreendimentoIsento = isEmpreendimentoIsento;
	etapaEnquadramento.isCaracterizacaoIsento = isCaracterizacaoIsento;
	etapaEnquadramento.goToNotificacoes = redirectToNotificacoes;
	etapaEnquadramento.finalizarNotificacao = finalizarNotificacao;
	etapaEnquadramento.isRetificacao =  $scope.cadastro.retificacao || false;
	etapaEnquadramento.finalizarSolicitacao = finalizarSolicitacao;
	etapaEnquadramento.hideVoltar = $scope.cadastro.continuacao || false;

	$scope.cadastro.etapas.ENQUADRAMENTO.passoValido = undefined;
	$scope.cadastro.etapas.ENQUADRAMENTO.beforeEscolherEtapa = undefined;

	this.$onInit = function(){
		etapaEnquadramento.isRetificacao = $scope.cadastro.caracterizacao.status.id === app.STATUS_CARACTERIZACAO.NOTIFICADO.id ||
			$scope.cadastro.caracterizacao.status.id === app.STATUS_CARACTERIZACAO.NOTIFICADO_EM_ANDAMENTO.id ||
			$scope.cadastro.caracterizacao.status.id === app.STATUS_CARACTERIZACAO.NOTIFICADO_EMPREENDIMENTO_ALTERADO.id;

		const idCaracterizacao = $scope.cadastro.caracterizacao.id || $routeParams.idCaracterizacao;

		caracterizacaoService.dadosCaracterizacao(idCaracterizacao)
			.then(function(response) {

				$scope.cadastro.dadosCaracterizacao = response.data;

			})
			.catch(function() {

				mensagem.error("Não foi possível obter os dados da caracterizacao.");

			});
	};

	function finalizarNotificacao() {
		caracterizacaoService.finalizarSimplificado($scope.cadastro.caracterizacao.id).then(result => {
			redirectToNotificacoes($scope.cadastro.caracterizacao.id);
		}).catch(err => {
			console.log(err);
		});
	}

	function proximo() {

		const modalInstance = $uibModal.open({
			controller: 'modalConfirmacaoLicencaController',
			controllerAs: 'modalCtrl',
			backdrop: 'static',
			keyboard  : false,
			templateUrl: './features/caracterizacao/cadastro/common/modal-confirmacao-licenca.html',
			resolve: {
				cadastro: function() {
					return $scope.cadastro;
				},
			}
		});

		modalInstance.result.then(function() {});

		return modalInstance;

	}

	function redirectToNotificacoes(idCaracterizacao){
		$location.path('/empreendimento/' + $routeParams.idEmpreendimento + '/caracterizacao/' + idCaracterizacao + '/notificacao');
	}

	function finalizarSolicitacao(objCaracterizacao){

		const serviceMetodo = $scope.cadastro.modo !== 'Cadastrar' ? 'finalizarRenovacaoSimplificado' : 'finalizarSimplificado';
		const caracterizacao =  objCaracterizacao || $scope.cadastro.caracterizacao;

		caracterizacaoService[serviceMetodo](caracterizacao.id)
			.then(function(response){
				$rootScope.caracterizacoes = response.data.dados;
				$location.path('/empreendimento/' + $routeParams.idEmpreendimento + '/caracterizacoes/finalizado');
			})
			.catch(function(response){
				mensagem.error(response.data.texto, {ttl: 10000});
			});
	}

	function getTotalGeral() {

		if (!$scope.cadastro.dadosCaracterizacao) {
			return 0;
		}

		if($scope.cadastro.dadosCaracterizacao){
			if ($scope.cadastro.modo === 'Cadastrar') {

				return _.reduce($scope.cadastro.dadosCaracterizacao.tiposLicencaEmAndamento, function(total, licenca){
					return total + licenca.valorDae;
				}, 0);

			} else {

				return $scope.cadastro.dadosCaracterizacao.tipoLicenca.valorDae;
			}
		}

	}

	//verifica se um empreedimento é isento
	function isEmpreendimentoIsento() {
		if($scope.cadastro.dadosCaracterizacao) {

			if ($scope.cadastro.modo === 'Vizualizar'){
				return $scope.cadastro.dadosCaracterizacao.tipoLicenca;
			}

			return $scope.cadastro.dadosCaracterizacao.tiposLicencaEmAndamento[0].isento;

		}

		return false;
	}

	//verifica se uma atividade é isenta
	function isCaracterizacaoIsento() {
		if ($scope.cadastro.dadosCaracterizacao !== undefined) {
			return $scope.cadastro.dadosCaracterizacao.tiposLicencaEmAndamento[0].isento ||
				$scope.cadastro.dadosCaracterizacao.valorTaxaLicenciamento === 0;
		}
	}

	$scope.getDescricao = (atividadeCaracterizacao, atividadeCaracterizacaoParametro) => {
		return atividadeCaracterizacao.atividade.parametros.filter(
			p => p.id === atividadeCaracterizacaoParametro.parametroAtividade.id
		)[0].descricao;
	};

	$scope.getLinkTabelaValoresTaxa = () => {
		return documentoService.getLinkTabelaValoresTaxa();
	};

	//
	// function romanToInt(romano) {
	//
	// 	if(romano == null) return -1;
	//
	// 	var num = charToInt(romano.charAt(0));
	// 	var pre, curr;
	//
	// 	for(var i = 1; i < romano.length; i++) {
	//
	// 		curr = charToInt(romano.charAt(i));
	// 		pre = charToInt(romano.charAt(i-1));
	//
	// 		if(curr <= pre) {
	//
	// 			num += curr;
	//
	// 		} else {
	//
	// 			num = num - pre*2 + curr;
	//
	// 		}
	//
	// 	}
	//
	// 	return num;
	//
	// }
	//
	// function charToInt(c) {
	//
	// 	switch (c) {
	//
	// 		case 'I': return 1;
	// 		case 'V': return 5;
	// 		case 'X': return 10;
	// 		case 'L': return 50;
	// 		case 'C': return 100;
	// 		case 'D': return 500;
	// 		case 'M': return 1000;
	// 		default: return -1;
	//
	// 	}
	//
	// }

};

exports.controllers.CaracterizacaoLSAEnquadramentoController = CaracterizacaoLSAEnquadramentoController;
