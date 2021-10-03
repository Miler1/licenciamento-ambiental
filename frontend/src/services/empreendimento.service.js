var EmpreendimentoService = function(request, config) {

	this.getEmpreendimentoPorId = function(idEmpreendimento) {

		return request.get(config.BASE_URL() + "empreendimento/"+idEmpreendimento);
	};

	this.list = function(campoPesquisa, paginaAtual, itensPorPagina) {

		return request
			.get(config.BASE_URL() + "empreendimentos", {
				pesquisa: campoPesquisa,
				numeroPagina: paginaAtual,
				qtdItensPorPagina: itensPorPagina
			});
	};

	this.getPessoaPorCpfCnpj = function(cpfCnpj) {

		return request.get(config.BASE_URL() + "empreendimentos/pessoas/cpfCnpj/"+cpfCnpj);
	};

	this.save = function(empreendimento) {

		return request.post(config.BASE_URL() + "empreendimentos", empreendimento);

	};

	this.update = function(id, empreendimento) {

		return request.post(config.BASE_URL() + "empreendimentos/" + id + "/update", empreendimento);

	};

	this.buscarPessoa = function(id) {

		return request.get(config.BASE_URL() + "empreendimentos/" + id);
	};

	this.buscarEmpreendimentoReduzido = function(cpfCnpj) {

		return request.get(config.BASE_URL() + "empreendimento/reduzido/" + cpfCnpj);
	};

	this.buscarEmpreendimentoCompleto= function(cpfCnpj) {

		return request.get(config.BASE_URL() + "empreendimento/completo/" + cpfCnpj);
	};

	this.getEmpreendimentoToUpdate = function(id) {

		return request.get(config.BASE_URL() + "empreendimentos/" + id + "/findToUpdate");
	};

	this.buscarEmpreendimentoPorCpfCnpj = function(cpfCnpj) {

		return request.get(config.BASE_URL() + "empreendimento/cpfCnpj/" + cpfCnpj);
	};

	this.buscarEmpreendimentoOutroEmpreendedor = function(cpfCnpj){

		return request.get(config.BASE_URL() + "empreendimento/cpfCnpj/outro/" + cpfCnpj);

	};

	this.validaVinculoEmpreendedorPessoaDoEmpreendimento = function(cpfCnpj,cpfCnpjEmpdor){

		let cpfCnpjs = {
			cpfCnpjEmpreendimento: cpfCnpj,
			cpfCnpjEmpreendedor: cpfCnpjEmpdor
		};

		return request.post(config.BASE_URL() + "empreendimento/cpfCnpj/vinculos", cpfCnpjs);
	};

	this.delete = function(id) {

		return request.delete(config.BASE_URL() + "empreendimento/" + id);
	};

	this.getCadastrante = function(cpfCnpj) {

		return request.get(config.BASE_URL() + "empreendimentos/pessoa/cadastrante/cpfCnpj/"+cpfCnpj);
	};


};

exports.services.EmpreendimentoService = EmpreendimentoService;
