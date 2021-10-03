var SolicitacaoDocumentoCaracterizacao = function(request, Upload, config) {

	this.uploadDocumento = function(idSolicitacaoDocumento, arquivo) {

		return Upload.upload({
			url: config.BASE_URL() + 'solicitacoesDocumentosCaracterizacao/'+idSolicitacaoDocumento+'/documento/upload',
			data: {file: arquivo}
		});
	};

	this.getRotaDownloadDocumento = function(idSolicitacaoDocumento) {

		return config.BASE_URL() + "solicitacoesDocumentosCaracterizacao/"+idSolicitacaoDocumento+"/documento/download";
	};

	this.deleteDocumento = function(idSolicitacaoDocumento) {

		return request.delete(config.BASE_URL() + "solicitacoesDocumentosCaracterizacao/"+idSolicitacaoDocumento+"/documento");
	};

	this.desvincularDocumento = function(idSolicitacaoDocumento) {

		return request.put(config.BASE_URL() + "solicitacoesDocumentosCaracterizacao/"+idSolicitacaoDocumento+"/documento", {});
	};
};

exports.services.SolicitacaoDocumentoCaracterizacao = SolicitacaoDocumentoCaracterizacao;