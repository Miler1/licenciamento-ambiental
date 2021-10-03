var EstadoCivilService = function(request, config) {

	this.list = function() {

		return request.getWithCache(config.BASE_URL() + "estadosCivis");

	};

};

exports.services.EstadoCivilService = EstadoCivilService;
