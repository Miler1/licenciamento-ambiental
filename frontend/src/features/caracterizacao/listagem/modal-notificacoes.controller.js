var ModalNotificacoesController = function ($uibModalInstance, mensagem, notificacoesService, numero, notificacoes) {

	var modalCtrl = this;

	modalCtrl.selecionarArquivo = selecionarArquivo;
	modalCtrl.baixarDocumento = baixarDocumento;
	modalCtrl.removerDocumento = removerDocumento;
	modalCtrl.justificarNotificacao = justificarNotificacao;
	modalCtrl.removerJustificativa = removerJustificativa;
	modalCtrl.podeSalvarJustificativa = podeSalvarJustificativa;
	modalCtrl.numero = numero;
	modalCtrl.notificacoes = notificacoes;
	modalCtrl.downloadAnaliseDocumento = downloadAnaliseDocumento;

	modalCtrl.cancelar = function () {
		$uibModalInstance.close();
	};

	function selecionarArquivo(files, file, notificacao) {

		// 2465792 = 2 * 1024 * 1024 = 2MB
		if(file && file.size > 2465792) {

			mensagem.warning('Os arquivos selecionados não devem exceder 2MB.', {ttl: 10000, referenceId: 15});

			return;
		}

		uploadArquivo(file, notificacao);
	}

	function uploadArquivo(arquivo, notificacao) {

		if (!arquivo){

			return;
		}

		notificacoesService.uploadDocumento(notificacao.id, arquivo)
			.then(function(response){

				notificacao.documentoCorrigido = response.data;
				notificacao.resolvido = true;
				notificacao.exibirCampoJustificativa = false;
				mensagem.success('Documento adicionado com sucesso.', {dontScroll: true, referenceId: 15});
			})
			.catch(function(response){

				mensagem.warning(response.data.texto, {dontScroll: true, referenceId: 15});
				return;
			});
	}

	function baixarDocumento(idNotificacao) {

		location.href = notificacoesService.getRotaDownloadDocumento(idNotificacao);
	}

	function removerDocumento(notificacao) {

		notificacoesService.deleteDocumento(notificacao.id)
			.then(function(response){

				notificacao.documentoCorrigido = null;
				notificacao.resolvido = false;
				mensagem.success(response.data.texto, {dontScroll: true, referenceId: 15});
			})
			.catch(function(response){

				mensagem.warning(response.data.texto, {dontScroll: true, referenceId: 15});
				return;
			});
	}

	function justificarNotificacao(notificacao) {

		if (!notificacao){

			return;
		}

		notificacoesService.justificar(notificacao.id, notificacao.justificativa)
			.then(function(response){

				notificacao.resolvido = true;
				mensagem.success(response.data.texto, {dontScroll: true, referenceId: 15});
			})
			.catch(function(response){

				mensagem.warning(response.data.texto, {dontScroll: true, referenceId: 15});
				return;
			});
	}

	function removerJustificativa(notificacao, index) {

		notificacoesService.removerJustificativa(notificacao.id)
			.then(function(response){

				notificacao.justificativa = null;
				notificacao.exibirCampoJustificativa = false;
				notificacao.resolvido = false;
				mensagem.success(response.data.texto, {dontScroll: true, referenceId: 15});
			})
			.catch(function(response){

				mensagem.warning(response.data.texto, {dontScroll: true, referenceId: 15});
				return;
			});
	}

	function podeSalvarJustificativa(notificacao) {

		if(!notificacao.justificativa) {
			return false;
		}

		if(notificacao.justificativa.length === 0) {
			return false;
		}

		if(notificacao.justificativa.length > 0 && notificacao.resolvido && !modalCtrl.justificativaAlterada) {
			return false;
		}

		return true;

	}

	function downloadAnaliseDocumento(documento) {

		if(documento !== null) {

			notificacoesService.downloadAnaliseDocumento(documento.id);

		}
	}

};

exports.controllers.ModalNotificacoesController = ModalNotificacoesController;
