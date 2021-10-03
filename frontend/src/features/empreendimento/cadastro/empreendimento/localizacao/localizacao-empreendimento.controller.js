var LocalizacaoEmpreendimentoController = function($scope, mensagem, imovelService, municipioService) {

	var localizacaoEmpreendimento = this;

	localizacaoEmpreendimento.abaValida = abaValida;
	localizacaoEmpreendimento.anterior = anterior;
	localizacaoEmpreendimento.proximo = proximo;
	localizacaoEmpreendimento.getImoveis = getImoveis;
	localizacaoEmpreendimento.getDadosImovel = getDadosImovel;
	localizacaoEmpreendimento.getGeometriaMunicipio = getGeometriaMunicipio;
	localizacaoEmpreendimento.formatarDados = formatarDados;
	localizacaoEmpreendimento.imoveis = [];
	localizacaoEmpreendimento.localizacaoEmpreendimento = app.LOCALIZACOES_EMPREENDIMENTO;
	localizacaoEmpreendimento.tipoGeometria = {
		polygon: true,
		circle: false,
		rectangle: false,
		polyline: false,
		marker: false
	};

	localizacaoEmpreendimento.errors= {
		roteiroAcesso: false
	};

	$scope.imoveisPesquisados = false;

	if(!$scope.cadastro.etapas.EMPREENDIMENTO.abas.LOCALIZACAO.abaValida){
		$scope.cadastro.etapas.EMPREENDIMENTO.abas.LOCALIZACAO.abaValida = abaValida;
	}

	function abaValida() {

		if ($scope.cadastro.enderecoEmpreendimento.principal !== undefined) {

			if($scope.cadastro.enderecoEmpreendimento.principal.zonaLocalizacao.codigo === localizacaoEmpreendimento.localizacaoEmpreendimento.RURAL){

				if ($scope.formLocalizacaoEmpreendimento.roteiro.$viewValue !== undefined) {

					return $scope.formLocalizacaoEmpreendimento.$valid &&
						$scope.cadastro.empreendimento.coordenadas && $scope.formLocalizacaoEmpreendimento.roteiro.$viewValue;

				}

				return $scope.formLocalizacaoEmpreendimento.$valid && $scope.cadastro.empreendimento.coordenadas;

			}else{

				return $scope.formLocalizacaoEmpreendimento.$valid && $scope.cadastro.empreendimento.coordenadas;

			}
		}

	}

	function anterior() {

		$scope.cadastro.etapas.EMPREENDIMENTO.tabIndex--;
	}

	function formatarDados() {

		if ($scope.cadastro.empreendimento.proprietarios === null || $scope.cadastro.empreendimento.proprietarios === undefined ){
			var listaProprietarios = [];

			_.forEach( $scope.cadastro.empreendimento.empreendimentoEU.proprietarios, function(proprietario){

				var proprietarioPessoa = {
					pessoa:null
				};

				proprietarioPessoa.pessoa = proprietario;
				listaProprietarios.push(proprietarioPessoa);

			});
			$scope.cadastro.empreendimento.proprietarios = listaProprietarios;
		}

		if ( $scope.cadastro.empreendimento.representantesLegais === null || $scope.cadastro.empreendimento.representantesLegais === undefined ){
			var listaRepresentantesLegais = [];

			_.forEach( $scope.cadastro.empreendimento.empreendimentoEU.representantesLegais, function(representanteLegal){

				var representanteLegalPessoa = {
					pessoa:null,
					dataVinculacao:null,
				};

				representanteLegalPessoa.pessoa = representanteLegal;
				representanteLegalPessoa.dataVinculacao = representanteLegal.dataCadastro;
				listaRepresentantesLegais.push(representanteLegalPessoa);

			});
			$scope.cadastro.empreendimento.representantesLegais = listaRepresentantesLegais;
		}

		if ($scope.cadastro.empreendimento.responsaveis === null || $scope.cadastro.empreendimento.responsaveis === undefined ){

			$scope.cadastro.empreendimento.responsaveis = [];

			_.forEach( $scope.cadastro.empreendimento.empreendimentoEU.responsaveisTecnicos, function(responsavelTecnico){

				var responsavelTecnicoPessoa = {
					pessoa:null,
					tipo: 'TECNICO'
				};

				responsavelTecnicoPessoa.pessoa = responsavelTecnico;
				$scope.cadastro.empreendimento.responsaveis.push(responsavelTecnicoPessoa);

			});

			_.forEach( $scope.cadastro.empreendimento.empreendimentoEU.responsaveisLegais, function(responsavelLegal){

				var responsavelLegalPessoa = {
					pessoa:null,
					tipo:'LEGAL',
				};

				responsavelLegalPessoa.pessoa = responsavelLegal;
				$scope.cadastro.empreendimento.responsaveis.push(responsavelLegalPessoa);

			});
		}
	}

	function proximo() {

		$scope.formLocalizacaoEmpreendimento.$setSubmitted();

		//$scope.cadastro.empreendimento.localizacao = 'ZONA_URBANA';

		var cpfCnpj = $scope.cadastro.empreendimento.empreendimentoEU.pessoa.cpf ? $scope.cadastro.empreendimento.empreendimentoEU.pessoa.cpf : $scope.cadastro.empreendimento.empreendimentoEU.pessoa.cnpj;
		var denominacao = $scope.cadastro.empreendimento.empreendimentoEU.denominacao;

		if ($scope.formLocalizacaoEmpreendimento.roteiro.$viewValue !== undefined) {
			$scope.cadastro.enderecoEmpreendimento.principal.descricaoAcesso = $scope.formLocalizacaoEmpreendimento.roteiro.$viewValue;
		}

		localizacaoEmpreendimento.errors.roteiroAcesso = $scope.formLocalizacaoEmpreendimento.roteiro.$viewValue === undefined ? true : false;

		if (localizacaoEmpreendimento.abaValida()) {

			localizacaoEmpreendimento.formatarDados();

			$scope.cadastro.proximo();

		} else {

			if(!$scope.cadastro.empreendimento.coordenadas && $scope.cadastro.enderecoEmpreendimento.principal.zonaLocalizacao.codigo === localizacaoEmpreendimento.localizacaoEmpreendimento.URBANA ) {

				mensagem.warning('É preciso adicionar o perímetro do empreendimento dentro dos limites mostrados no mapa');

			} else if($scope.cadastro.enderecoEmpreendimento.principal.zonaLocalizacao.codigo === localizacaoEmpreendimento.localizacaoEmpreendimento.RURAL && $scope.imoveisPesquisados && !localizacaoEmpreendimento.imoveis) {

				$scope.imoveisPesquisados = true;

				imovelService.enviarEmail(cpfCnpj, denominacao)

					.then(function(response){
						$scope.cadastro.emailEnviado = response.data.emailEnviado;
						mensagem.warning('Providencie o Cadastro Ambiental Rural para prosseguir com o cadastro do Empreendimento. Foi comunicado ao setor responsável na SEMA para análise do cadastro vinculado ao Empreendimento.', {ttl: 15000});
					});

			}else{

				mensagem.warning('Verifique os campos destacados em vermelho para prosseguir com o cadastro.');
			}

		}

	}

	function getImoveis() {

		var cpfCnpj = $scope.cadastro.empreendimento.empreendimentoEU.pessoa.cpf ? $scope.cadastro.empreendimento.empreendimentoEU.pessoa.cpf : $scope.cadastro.empreendimento.empreendimentoEU.pessoa.cnpj;
		var idMunicipio = $scope.cadastro.empreendimento.municipio.id;
		$scope.cadastro.empreendimento.enderecos[0].tipo = 'ZONA_RURAL';
		$scope.cadastro.enderecoEmpreendimento.principal.zonaLocalizacao.codigo = localizacaoEmpreendimento.localizacaoEmpreendimento.RURAL;

		localizacaoEmpreendimento.estiloGeo = app.Geo.overlays.AREA_IMOVEL.style;

		imovelService.getImoveisSimplificadosPorCpfCnpj(cpfCnpj, idMunicipio)
			.then(function(response){

				$scope.imoveisPesquisados = true;

				localizacaoEmpreendimento.imoveis = (response.data && response.data.length) ? response.data : null;

				localizacaoEmpreendimento.controleAtualizacao = false;

			})
			.catch(function(){

				mensagem.warning("Não foi possível obter os dados dos imóveis no CAR.");

			});
	}

	function getDadosImovel(codigoImovel) {

		imovelService.getImoveisCompletoByCodigo(codigoImovel)
			.then(function(response){

				localizacaoEmpreendimento.imovelSelecionado = response.data;

			})
			.catch(function(){

				mensagem.error("Não foi possível obter os dados do imóvel no CAR.");

			});
	}

	function getGeometriaMunicipio(idMunicipio) {

		$scope.cadastro.empreendimento.enderecos[0].tipo = 'ZONA_URBANA';

		localizacaoEmpreendimento.estiloGeo = app.Geo.overlays.AREA_MUNICIPIO.style;

		var municipio = idMunicipio !== null && idMunicipio !== undefined ? idMunicipio : $scope.cadastro.empreendimento.municipio.id;

		municipioService.getMunicipioGeometryById(municipio)
			.then(function(response) {

				localizacaoEmpreendimento.municipio = response.data;

				localizacaoEmpreendimento.controleAtualizacao = false;

			})
			.catch(function() {
				mensagem.error("Não foi possível obter o limite do município.");
			});

	}

	function initLocalizacao() {

		var municipio = $scope.cadastro.empreendimento.municipio.id;

		switch ($scope.cadastro.empreendimento.localizacao) {
			case "ZONA_RURAL":
				getImoveis();
				break;
			case "ZONA_URBANA":
				getGeometriaMunicipio(municipio);
				break;
		}
	}

	$scope.$watch('cadastro.etapas.EMPREENDIMENTO.tabIndex', function(tabIndex){

		if($scope.$parent.alterouMunicipioEmpreendimento && tabIndex &&
			tabIndex === $scope.cadastro.etapas.EMPREENDIMENTO.abas.LOCALIZACAO.indice){

			$scope.cadastro.empreendimento.coordenadas = null;

			initLocalizacao();

			$scope.$parent.alterouMunicipioEmpreendimento = false;
		}

	});

	function setZonaUrbana(){
		$scope.cadastro.empreendimento.localizacao = 'ZONA_URBANA';
		localizacaoEmpreendimento.getGeometriaMunicipio($scope.cadastro.empreendimento.municipio.id);
	}

	$scope.$watch('cadastro.empreendimento.municipio', function(newMunicipio){

		if(newMunicipio){

			setZonaUrbana();
		}
		
	}, true);
};

exports.controllers.LocalizacaoEmpreendimentoController = LocalizacaoEmpreendimentoController;