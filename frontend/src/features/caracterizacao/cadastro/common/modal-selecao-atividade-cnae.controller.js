var ModalSelecaoAtividadeCnaeController = function ($uibModalInstance, mensagem, atividadeCnae, $filter) {

	var modalCtrl = this;

	modalCtrl.atividadeCnae = atividadeCnae;

	modalCtrl.atividadesSelecionadas = [];

	modalCtrl.texto = $("input[name='tipoCaracterizacaoRadioSimplificadoDLa']:checked").val();

	modalCtrl.ok = function () {

		if(modalCtrl.atividadesSelecionadas.length === 0) {

			mensagem.warning('Selecione uma atividade para prosseguir com o cadastro.');
			return;
		}

		$uibModalInstance.close({atividadesSelecionadas: modalCtrl.atividadesSelecionadas, atividadeCnae: atividadeCnae});
	};

	modalCtrl.selectedAtividades = function () {
		modalCtrl.atividadesSelecionadas = $filter('filter')(modalCtrl.atividadeCnae.atividades, {checked: true});
	};

	modalCtrl.canSelectAtividade = function() {

		if(modalCtrl.atividadesSelecionadas.length === 0) {
			return true;
		}

		return false;
	};

	modalCtrl.cancelar = function () {
		$uibModalInstance.dismiss();
	};

};

exports.controllers.ModalSelecaoAtividadeCnaeController = ModalSelecaoAtividadeCnaeController;
