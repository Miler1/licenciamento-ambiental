var MunicipiosLiberadosService = function(request, config) {

	this.list = function() {

		return request.getWithCache(config.BASE_URL() + "municipiosLiberados/all");

	};

};

exports.services.MunicipiosLiberadosService = MunicipiosLiberadosService;
