var OrgaoClasseService = function(request, config) {

	this.list = function() {

		return request.getWithCache(config.BASE_URL() + "orgaosClasse");

	};

};

exports.services.OrgaoClasseService = OrgaoClasseService;
