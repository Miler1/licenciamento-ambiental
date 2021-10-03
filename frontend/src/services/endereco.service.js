var EnderecoService = function(request, config) {

	this.listEstados = function() {

		return request.getWithCache(config.BASE_URL() + "estados");

	};

	this.listMunicipios = function(uf) {

		return request.getWithCache(config.BASE_URL() + "estados/" + uf + "/municipios", null, null, false);

	};

	this.getEnderecoByCEP = function(cep) {

		if(!cep) {
			return request.getWithCache(config.BASE_URL() + "enderecos/cep/0");
		}

		return request.getWithCache(config.BASE_URL() + "enderecos/cep/" + cep.toString().replace('-', ''));

	};

};

exports.services.EnderecoService = EnderecoService;
