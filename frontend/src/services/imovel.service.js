var ImovelService = function(request, config) {

	this.getImoveisSimplificadosPorCpfCnpj = function(cpfCnpj, idMunicipio) {

		return request.get(config.BASE_URL() + 'imoveis/simplificados', {
			cpfCnpj: cpfCnpj,
			idMunicipio: idMunicipio
		});
	};

	this.enviarEmail = function(cpfCnpj, denominacao){
		return request.post(config.BASE_URL() + 'empreendimento/enviar/' + cpfCnpj + '/' + denominacao);
	};

	this.getImoveisCompletoByCodigo = function(codigoImovel) {

		return request.get(config.BASE_URL() + 'imoveis/' + codigoImovel + '/completo');
	};

	this.verificarLicenciamento = function(codigoImovel) {

		return request.get(config.BASE_URL() + 'imoveis/' + codigoImovel + '/verificarLicenciamento');
	};

	this.getTemasByImovel = function(codigoImovel, temas) {

		return request.get(config.BASE_URL() + 'imoveis/' + codigoImovel + '/tema', {temas : temas});

	};

	this.camadaLDI = function(codigoImovel) {

		return request.get(config.BASE_URL() + 'imoveis/' + codigoImovel + "/camadaLDI");

	};

	this.camadaPRODES = function(codigoImovel) {

		return request.get(config.BASE_URL() + 'imoveis/' + codigoImovel + "/camadaPRODES");

	};

	this.camadaAreaConsolidada = function(codigoImovel) {

		return request.get(config.BASE_URL() + 'imoveis/' + codigoImovel + "/camadaAreaConsolidada");

	};

};

exports.services.ImovelService = ImovelService;
