var NotificacoesService = function(request, config, Upload) {

	this.getNotificacoes = function(idCaracterizacao) {

		return request.get(config.BASE_URL() + "notificacoes/" + idCaracterizacao);

	};

	this.getNotificacao = function(idCaracterizacao) {

		return request.get(config.BASE_URL() + "notificacao/" + idCaracterizacao);

	};

	this.uploadDocumento = function(idNotificacao, arquivo) {

		return Upload.upload({
			url: config.BASE_URL() + "notificacoes/" + idNotificacao + "/documento/upload",
			data: {file: arquivo}
		});
	};

	this.getRotaDownloadDocumento = function(idDocumento) {

		return config.BASE_URL() + "notificacoes/documento/download/" + idDocumento;
	};

	this.deleteDocumento = function(idNotificacao, idDocumento) {

		return request.delete(config.BASE_URL() +  "notificacoes/" + idNotificacao + "/documento/" + idDocumento);
	};

	this.justificarRetificacao = function(idNotificacao, justificativa) {

		let data = {justificativa : justificativa};

		return request.postFormUrlEncoded(
			config.BASE_URL() + "notificacoes/justificar/retificacao/" + idNotificacao, data
		);

	};

	this.justificarRetificacaoEmpreendimento = function(idNotificacao, justificativa) {

		let data = {justificativa : justificativa};

		return request.postFormUrlEncoded(
			config.BASE_URL() + "notificacoes/justificar/retificacao/empreendimento/" + idNotificacao, data
		);

	};

	this.justificarDocumentacao = function(idNotificacao, justificativa) {

		let data = { justificativa: justificativa };

		return request.postFormUrlEncoded(
			config.BASE_URL() + "notificacoes/justificar/documentacao/" + idNotificacao, data
		);

	};

	this.removerJustificativa = function(idNotificacao) {

		return request.delete(config.BASE_URL() + "notificacoes/" + idNotificacao + "/justificativa");
	};

	this.read = function(idCaracterizacao) {

		return request.put(config.BASE_URL() + "notificacoes/" + idCaracterizacao + "/ler", {});
	};

	this.downloadAnaliseDocumento = function(idDocumento) {

		window.location.href = config.BASE_URL() + "notificacoes/documento/" + idDocumento + "/download";

	};

};

exports.services.NotificacoesService = NotificacoesService;
