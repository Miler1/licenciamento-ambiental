var SolicitacaoGrupoDocumento = function(request, Upload, config) {

	this.uploadDocumento = function(idSolicitacaoDocumento, arquivo) {

		return Upload.upload({
			url: config.BASE_URL() + 'solicitacoesGruposDocumentos/'+idSolicitacaoDocumento+'/documento/upload',
			data: {file: arquivo}
		});
	};

	this.getRotaDownloadDocumento = function(idSolicitacaoDocumento) {

		return config.BASE_URL() + "solicitacoesGruposDocumentos/"+idSolicitacaoDocumento+"/documento/download";
	};

	this.deleteDocumento = function(idSolicitacaoDocumento) {

		return request.delete(config.BASE_URL() + "solicitacoesGruposDocumentos/"+idSolicitacaoDocumento+"/documento");
	};

	this.desvincularDocumento = function(idSolicitacaoDocumento) {

		return request.put(config.BASE_URL() + "solicitacoesGruposDocumentos/"+idSolicitacaoDocumento+"/documento", {});
	};
};

exports.services.SolicitacaoGrupoDocumento = SolicitacaoGrupoDocumento;
