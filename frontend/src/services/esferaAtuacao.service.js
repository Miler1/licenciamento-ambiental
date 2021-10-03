var EsferaAtuacaoService = function(request, config) {

	this.list = function() {

		return request.getWithCache(config.BASE_URL() + "esferasAtuacao");

	};

};

exports.services.EsferaAtuacaoService = EsferaAtuacaoService;
