var ModalListagemNotificacoesController = function ($uibModalInstance, mensagem, idCaracterizacao,
	notificacoesService, numero, $uibModal) {

	var modalCtrl = this;

	modalCtrl.idCaracterizacao = idCaracterizacao;
	modalCtrl.numero = numero;

	notificacoesService.getNotificacoes(modalCtrl.idCaracterizacao).then(function (response) {

		modalCtrl.notificacoes = response.data;

	});

	modalCtrl.cancelar = function () {
		$uibModalInstance.close();
	};

	modalCtrl.abrirModalNotificacoes = function(caracterizacao) {

		notificacoesService.read(modalCtrl.idCaracterizacao);

		var modalInstance = $uibModal.open({
			controller: 'modalNotificacoesController',
			controllerAs: 'modalCtrl',
			backdrop: 'static',
			keyboard  : false,
			templateUrl: './features/caracterizacao/listagem/modal-notificacoes.html',
			resolve: {
				idCaracterizacao: function () {
					return modalCtrl.idCaracterizacao;
				},
				numero: function () {
					return modalCtrl.numero;
				},
				notificacoes: function () {
					return modalCtrl.notificacoes;
				}
			}
		});

		modalInstance.result.then(function (reload) {

			if (reload){

				$route.reload();
			}
		});

		$uibModalInstance.close();
	};

};

exports.controllers.ModalListagemNotificacoesController = ModalListagemNotificacoesController;
