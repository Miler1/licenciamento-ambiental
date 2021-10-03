var ModalDadosDaeLicenciamentoController = function ($uibModalInstance, mensagem, caracterizacao, caracterizacaoService) {

	var modalCtrl = this;

	modalCtrl.caracterizacao = caracterizacao;

	modalCtrl.idCaracterizacao = caracterizacao.id;

	modalCtrl.statusCaracterizacao = app.STATUS_CARACTERIZACAO;

	modalCtrl.status = {
		EMITIDO: 'EMITIDO',
		NAO_EMITIDO: 'NAO_EMITIDO'
	};

	caracterizacaoService.dadosDaeLicenciamento(modalCtrl.idCaracterizacao).then(function (response) {

		modalCtrl.dae = response.data;
	});

	modalCtrl.processaDae = function() {

		if (modalCtrl.dae.status === modalCtrl.status.EMITIDO) {

			downloadDae(modalCtrl.caracterizacao.id, false);
		} else {

			emitirDae(modalCtrl.caracterizacao.id);
		}

	};

	modalCtrl.cancelar = function () {
		$uibModalInstance.close();
	};

	modalCtrl.daeEmitidoOuPago = function () {

		return modalCtrl.dae && 
			(modalCtrl.dae.status === modalCtrl.status.EMITIDO || modalCtrl.dae.status === modalCtrl.status.PAGO); 
	};	

	function downloadDae(idCaracterizacao, reload) {

		caracterizacaoService.downloadDaeLicenciamentoCaracterizacao(idCaracterizacao);
		$uibModalInstance.close(reload);
	}	

	function emitirDae(idCaracterizacao) {

		caracterizacaoService.emitirDaeLicenciamentoCaracterizacao(idCaracterizacao)
			.then(function(response){

				modalCtrl.dae = response.data;

				downloadDae(idCaracterizacao, true);
			})
			.catch(function(response){

				mensagem.error(response.data.texto, {ttl: 10000});
			});
	}

};

exports.controllers.ModalDadosDaeLicenciamentoController = ModalDadosDaeLicenciamentoController;
