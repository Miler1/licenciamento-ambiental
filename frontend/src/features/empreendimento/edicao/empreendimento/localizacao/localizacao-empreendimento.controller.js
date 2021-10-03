var LocalizacaoEmpreendimentoControllerEdicao = function($scope, mensagem, imovelService, $anchorScroll, municipioService, $injector) {

	// Criando extensão da controller LocalizacaoEmpreendimentoController
    $injector.invoke(exports.controllers.LocalizacaoEmpreendimentoController, this,
        {
            $scope: $scope,
			mensagem: mensagem,
			imovelService: imovelService,
			$anchorScroll: $anchorScroll,
			municipioService: municipioService
        }
    );

	var localizacaoEmpreendimento = this;

	

	localizacaoEmpreendimento.getImoveis = getImoveis;

	function getImoveis(getImoveis) {

		var cpfCnpj = $scope.cadastro.empreendimento.cpfCnpj;
		var idMunicipio = $scope.cadastro.empreendimento.municipio.id;
		
		imovelService.getImoveisSimplificadosPorCpfCnpj(cpfCnpj, idMunicipio)
			.then(function(response){

				$scope.imoveisPesquisados = true;

				localizacaoEmpreendimento.imoveis = (response.data && response.data.length) ? response.data : null;

				getImoveis();

			})
			.catch(function(){

				mensagem.warning("Não foi possível obter os dados dos imóveis no CAR.");

			});
	}

	
	function initLocalizacao() {

		if ($scope.cadastro.empreendimento.localizacao === 'ZONA_URBANA') {

			localizacaoEmpreendimento.getGeometriaMunicipio();

		} else if ($scope.cadastro.empreendimento.localizacao === 'ZONA_RURAL') {

			localizacaoEmpreendimento.getImoveis(function(){

				if (!$scope.cadastro.empreendimento.imovel){

					return;
				}

				_.forEach(localizacaoEmpreendimento.imoveis, function(imovel){

					if (imovel.id === $scope.cadastro.empreendimento.imovel.idCar) {

						$scope.cadastro.empreendimento.imovel = imovel;
						return false;	
					}
				});
			});

			localizacaoEmpreendimento.getDadosImovel($scope.cadastro.empreendimento.imovel.codigo);
		}
	}

	initLocalizacao();
};

exports.controllers.LocalizacaoEmpreendimentoControllerEdicao = LocalizacaoEmpreendimentoControllerEdicao;