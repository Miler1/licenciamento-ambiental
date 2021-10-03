var ModalConfirmacaoLicencaController = function ($location, $scope, $uibModalInstance, mensagem, cadastro, caracterizacaoService, $rootScope, $routeParams) {

	var modalCtrl = this;

	modalCtrl.caracterizacao = cadastro.dadosCaracterizacao;

	modalCtrl.ok = function () {

		var serviceMetodo = cadastro.modo !== 'Cadastrar' ? 'finalizarRenovacaoSimplificado' : 'finalizarSimplificado';

		caracterizacaoService[serviceMetodo](modalCtrl.caracterizacao.id)
			.then(function(response){

				$rootScope.caracterizacoes = response.data.dados;

				$uibModalInstance.close();

				$location.path('/empreendimento/' + $routeParams.idEmpreendimento + '/caracterizacoes/finalizado');

			})
			.catch(function(response){

				mensagem.error(response.data.texto, {ttl: 10000});
			});

	};

	modalCtrl.cancelar = function () {
		$uibModalInstance.close({caracterizacao: undefined});
	};

	$scope.getDescricao = (atividadeCaracterizacao, atividadeCaracterizacaoParametro) => {
		return atividadeCaracterizacao.atividade.parametros.filter(
			p => p.id === atividadeCaracterizacaoParametro.parametroAtividade.id
		)[0].descricao;
	};

};

exports.controllers.ModalConfirmacaoLicencaController = ModalConfirmacaoLicencaController;
