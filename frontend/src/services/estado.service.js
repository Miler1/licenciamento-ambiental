var EstadoService = function(request, config) {

	this.getEstadoGeometryByCodigo = function(codigo) {

		return request.get(config.BASE_URL() + 'estados/' + codigo + '/limite');
	};
};

exports.services.EstadoService = EstadoService;