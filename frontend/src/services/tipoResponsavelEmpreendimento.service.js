var TipoResponsavelEmpreendimentoService = function(request, config) {

	this.list = function() {

		return request.getWithCache(config.BASE_URL() + "tiposResponsaveisEmpreendimento");

	};

};

exports.services.TipoResponsavelEmpreendimentoService = TipoResponsavelEmpreendimentoService;
