var DocumentoService = function(request, config, $window) {

	this.getLinkTabelaValoresTaxa = function () {

		return config.BASE_URL() + "documentos/tabela_valores_taxas";
	};
};

exports.services.DocumentoService = DocumentoService;
