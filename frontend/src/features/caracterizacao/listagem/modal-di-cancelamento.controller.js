var ModalDICancelamentoController = function ($uibModalInstance, idCaracterizacao, numero, caracterizacaoService, mensagem) {

	var modalCtrl = this;

	modalCtrl.idCaracterizacao = idCaracterizacao;
	modalCtrl.numero = numero;
	modalCtrl.justificativa = "";

	modalCtrl.cancelar = function () {
		$uibModalInstance.close();
	};

	modalCtrl.ok = function () {

		if (!validarFormulario()) {

			return false;
		}

		var cancelamento = {
			justificativa: modalCtrl.justificativa
		};

		caracterizacaoService.cancelarDI(modalCtrl.idCaracterizacao, cancelamento)
			.then(function(response){

				mensagem.success(response.data.texto);

				$uibModalInstance.close(true);
			})
			.catch(function(response){

				mensagem.error(response.data.texto, {ttl: 10000});
			});
	};

	function validarFormulario() {

		modalCtrl.formCancelarDla.$setSubmitted();

		return modalCtrl.formCancelarDla.$valid;
	}

};

exports.controllers.ModalDICancelamentoController = ModalDICancelamentoController;
