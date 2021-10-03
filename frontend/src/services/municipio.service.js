var MunicipioService = function(request, config) {

	this.getMunicipioGeometryById = function(id) {

		return request.get(config.BASE_URL() + 'municipios/' + id + '/limite');
	};
};

exports.services.MunicipioService = MunicipioService;