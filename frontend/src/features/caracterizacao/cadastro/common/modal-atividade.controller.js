var ModalCaracterizacaoAtividadeController = function ($uibModalInstance, mensagem, atividade, empreendimento, $filter) {

	var modalCtrl = this;

	modalCtrl.atividade = atividade;

	modalCtrl.empreendimento = empreendimento;

	modalCtrl.atividadesCnae = angular.copy(atividade.atividadesCnae);

	modalCtrl.qtdAtividade = modalCtrl.atividadesCnae.length;

	modalCtrl.atividadesCnaeSelecionadas = [];

	modalCtrl.texto = $("input[name='tipoCaracterizacaoRadioSimplificadoDLa']:checked").val();

	if(modalCtrl.texto == "dispensa") {

		modalCtrl.texto = "COEMA 107/2013";

	} else {

		modalCtrl.texto = "SIMPLIFICADO 127/2016";

	}

	modalCtrl.ok = function () {

		if(modalCtrl.atividadesCnaeSelecionadas.length === 0) {

			mensagem.warning('Selecione uma atividade para prosseguir com o cadastro.');
			return;
		}

		$uibModalInstance.close({atividadesCnaeSelecionadas: modalCtrl.atividadesCnaeSelecionadas, atividade: atividade});
	};

	modalCtrl.selectedAtividades = function () {
		modalCtrl.atividadesCnaeSelecionadas = $filter('filter')(modalCtrl.atividadesCnae, {checked: true});
	};

	modalCtrl.canSelectAtividade = function() {

		if(modalCtrl.atividadesCnaeSelecionadas.length === 0) {
			return true;
		}

		return false;
	};

	modalCtrl.cancelar = function () {
		$uibModalInstance.dismiss();
	};

};

exports.controllers.ModalCaracterizacaoAtividadeController = ModalCaracterizacaoAtividadeController;
