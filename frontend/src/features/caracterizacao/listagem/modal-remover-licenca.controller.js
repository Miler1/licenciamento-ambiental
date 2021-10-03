var ModalRemoverLicencaController = function ($uibModalInstance, caracterizacao, caracterizacaoService, mensagem) {

	var modalCtrl = this;

	modalCtrl.idCaracterizacao = caracterizacao.id;
	modalCtrl.numero = caracterizacao.numero;

	modalCtrl.cancelar = function () {
		$uibModalInstance.close();
	};

	modalCtrl.ok = function () {

		caracterizacaoService.removerCaracterizacao(modalCtrl.idCaracterizacao)
			.then(function(response){

				$uibModalInstance.close(true);
				mensagem.success(response.data.texto);

			})
			.catch(function(response){
				mensagem.error(response.data.texto, {ttl: 10000});
			});

	};

};

exports.controllers.ModalRemoverLicencaController = ModalRemoverLicencaController;
