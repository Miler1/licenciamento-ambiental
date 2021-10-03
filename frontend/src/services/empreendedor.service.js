var EmpreendedorService = function(request, config){

	this.getRepresentantes = function(idPessoa) {

		return request.get(config.BASE_URL() + 'empreendedores/pessoas/' + idPessoa + '/representantes');

	};

	this.findByCpfCnpj = function(cpfCnpj) {

		return request.get(config.BASE_URL() + 'empreendedores/pessoas/cpfCnpj/' + cpfCnpj);

	};
};

exports.services.EmpreendedorService = EmpreendedorService;