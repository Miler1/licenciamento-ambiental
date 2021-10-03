var TiposLicencaService = function (request) {

	this.listaTiposLicenca = function (licencasAtividade) {
		var data = {
			licencasAtividade
		};

		return request.post('tiposlicenca/listLicencas', data);
	};

	this.listarFluxo = function () {
		return request.get('tiposlicenca/listarFluxo');
	};

};

exports.services.TiposLicencaService = TiposLicencaService;
