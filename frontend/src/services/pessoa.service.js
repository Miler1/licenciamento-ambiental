var PessoaService = function(request, config){

	this.byCpfCnpj = function(cpfCnpj) {

		return request.get(config.BASE_URL() + 'pessoas/cpfCnpj/' + cpfCnpj);

	};

	this.byCpf = function(cpf) {

		return request.get(config.BASE_URL() + 'pessoas/cpf/' + cpf);

	};	

};

exports.services.PessoaService = PessoaService;