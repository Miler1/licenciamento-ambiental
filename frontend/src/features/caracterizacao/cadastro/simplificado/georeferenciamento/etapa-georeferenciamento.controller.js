var CaracterizacaoLSAGeoController = function($scope, $rootScope, mensagem, municipioService,estadoService, imovelService, config, $q) {

	var etapaGeo = this;

	//area para armazenar cálculo de sobreposicao com APP
	var areaAPP = "";

	etapaGeo.passoValido = passoValido;
	etapaGeo.proximo = proximo;
	etapaGeo.initGeo = initGeo;
	etapaGeo.localizacaoEmpreendimento = getGeoLocalizacao();

	etapaGeo.editarGeo = function editarGeo() {
		if ($scope.cadastro.caracterizacao.notificacao){
			return $scope.cadastro.caracterizacao.notificacao.retificacaoSolicitacaoComGeo;
		}
		return true;
	};

	etapaGeo.temAtividadeRural = function (){

		return $rootScope.empreendimento.localizacao === "ZONA_RURAL";
	};

	
	var areasProibidas = ['APP_TOTAL', 'NASCENTE_OLHO_DAGUA','ARL_TOTAL','AREA_USO_RESTRITO_DECLIVIDADE_25_A_45','AREA_USO_RESTRITO_PANTANEIRA'];
	var geometriasAreasProibidas = {};

	$scope.cadastro.etapas.GEO.passoValido = passoValido;
	$scope.cadastro.etapas.GEO.beforeEscolherEtapa = beforeEscolherEtapa;

	var passoValidoMSG;
	var isAreaVerificada = false;
	var isIntersectionsVerificado = false;
	var hasIntersection = false;
	var imovelValidoParaLicenciamento = false;

	$scope.$on('atividadeAlterada', function(event, args) {
		negarVerificacoes();
	});

	$scope.$on('geometriesUpdated', function(event, args) {
		negarVerificacoes();
	});

	function negarVerificacoes() {
		isAreaVerificada = false;
		isIntersectionsVerificado = false;
		hasIntersection = false;
		imovelValidoParaLicenciamento = false;
	}

	function isEmpreendimentoRural() {
		return $rootScope.empreendimento.localizacao === app.LOCALIZACOES_EMPREENDIMENTO.ZONA_RURAL;
	}

	function getMenssagemSobreposicao(nomeCamada) {
		return 'Atenção! A área desenhada possui sobreposição com área de ' + nomeCamada +
			' do imóvel e por isso a solicitação não pode ser concluída. Por favor, apresente nova área da atividade.';
	}

	function passoValido() {

		if(todasAtividadesTemGeometria() && !isIntersectionsVerificado && isEmpreendimentoRural()){

			let collectionUnnion = $scope.cadastro.caracterizacao.complexo ? getUniaoComplexo() : getUniaoAtividades();

			if(verificaIntercao(collectionUnnion,geometriasAreasProibidas.AREA_USO_RESTRITO_DECLIVIDADE_25_A_45)) {
				passoValidoMSG = getMenssagemSobreposicao('Uso Restrito com declide de 25° até 45°');
				isIntersectionsVerificado = true;
				hasIntersection = true;
				return false;
			}

			if(verificaIntercao(collectionUnnion,geometriasAreasProibidas.AREA_USO_RESTRITO_PANTANEIRA)) {
				passoValidoMSG = getMenssagemSobreposicao('Uso Restrito Pantaneira');
				isIntersectionsVerificado = true;
				hasIntersection = true;
				return false;
			}

			if(verificaIntercao(collectionUnnion,geometriasAreasProibidas.ARL_TOTAL)) {
				passoValidoMSG = getMenssagemSobreposicao('Reserva Legal ');
				isIntersectionsVerificado = true;
				hasIntersection = true;
				return false;
			}

			if(verificaIntercao(collectionUnnion,geometriasAreasProibidas.APP_TOTAL)) {
				passoValidoMSG = getMenssagemSobreposicao('Área de preservação permamente');
				isIntersectionsVerificado = true;
				hasIntersection = true;
				return false;
			}

			if(verificaIntercao(collectionUnnion,geometriasAreasProibidas.NASCENTE_OLHO_DAGUA)) {
				passoValidoMSG = getMenssagemSobreposicao('Nascente ou olho d\'água ');
				isIntersectionsVerificado = true;
				hasIntersection = true;
				return false;
			}

			isIntersectionsVerificado = true;
		}

		return verificaAreaInformadaComDesenho() && !hasIntersection;

	}

	function getUniaoComplexo() {

		reducer = (accumulator, currentValue) => turf.union(accumulator, currentValue);

		return $scope.cadastro.caracterizacao.geometriasComplexo ?
			$scope.cadastro.caracterizacao.geometriasComplexo.features.reduce(reducer) :
			null;
	}


	function getUniaoAtividades() {

		reducer = (accumulator, currentValue) => turf.union(accumulator, currentValue);

		atvReducer = (acc, curr) =>
			turf.union(
			acc.geometriasAtividade.features.reduce(reducer), curr.geometriasAtividade.features.reduce(reducer)
		);

		return $scope.cadastro.caracterizacao.atividadesCaracterizacao ?
			$scope.cadastro.caracterizacao.atividadesCaracterizacao.length === 1 ?
			$scope.cadastro.caracterizacao.atividadesCaracterizacao[0].geometriasAtividade.features.reduce(reducer) :
			$scope.cadastro.caracterizacao.atividadesCaracterizacao.reduce(atvReducer) :
			null;
	}

	function verificaIntercao(collectionUnion, feature){
		return collectionUnion && feature ? turf.intersect(collectionUnion, feature) : false;
	}


	function todasAtividadesTemGeometria() {

		if ($scope.cadastro.caracterizacao.complexo) {

			return $scope.cadastro.caracterizacao.geometriasComplexo &&
				$scope.cadastro.caracterizacao.geometriasComplexo.features &&
				$scope.cadastro.caracterizacao.geometriasComplexo.features.length > 0;

		}

		return $scope.cadastro.caracterizacao.atividadesCaracterizacao &&
			$scope.cadastro.caracterizacao.atividadesCaracterizacao.length > 0 &&
			$scope.cadastro.caracterizacao.atividadesCaracterizacao.filter(ac =>
				ac.geometriasAtividade &&
				ac.geometriasAtividade.features &&
				ac.geometriasAtividade.features.length > 0
			).length === $scope.cadastro.caracterizacao.atividadesCaracterizacao.length;

	}

	function verificaAreaInformadaComDesenho() {

		if(isAreaVerificada){
			return true;
		}

		if($scope.cadastro.caracterizacao.complexo){
			if((_.isEmpty($scope.cadastro.caracterizacao.geometriasComplexo))){
				passoValidoMSG = "Adicione uma geometria para o complexo de atividades.";
				return false;
			}
			return true;
		}
		else if (todasAtividadesTemGeometria()) {
			return true;
		}
		else {
			passoValidoMSG = "Adicione uma geometria no mapa para cada atividade.";
			return false;
		}

	}

    function allTrue(obj) {
		for(var o in obj)
			if(!obj[o]) return false;
		return true;
    }

	function beforeEscolherEtapa() {

		if(etapaGeo.passoValido()){

			return true;

		} else {
			passoValidoMSG = "Adicione uma geometria no mapa para cada atividade.";
			return false;
		}

	}

	var controleLayerAdicionada = {};

	function addOverlay(nomeLayer, leafletLayer) {

		if(!etapaGeo.layerGroup) {
			etapaGeo.layerGroup = {};
		}

		if(!etapaGeo.layerGroup.overlays) {
			etapaGeo.layerGroup.overlays = {};
		}

		if(controleLayerAdicionada[nomeLayer]){
			controleLayerAdicionada[nomeLayer].addLayer(leafletLayer);
		} else {
			etapaGeo.layerGroup.overlays[nomeLayer] = leafletLayer;
			controleLayerAdicionada[nomeLayer] = leafletLayer;
		}

	}

	function proximo() {

		//Validações serão feitas no método beforeEscolherEtapa
		isAreaVerificada = false;
		isIntersectionsVerificado = false;
		hasIntersection = false;
		imovelValidoParaLicenciamento = false;

		if(etapaGeo.passoValido()){
			$scope.cadastro.proximo();
		} else {
			mensagem.warning(passoValidoMSG, {ttl: 10000});
		}
	}

	function getGeometriaMunicipio(id) {

		var deffered = $q.defer();

		municipioService.getMunicipioGeometryById(id)
			.then(function(response) {

				etapaGeo.limite = response.data.limite;
				deffered.resolve();
			})
			.catch(function() {

				mensagem.error("Não foi possível obter o limite do município.");
				deffered.reject();
			});

			return deffered.promise;

	}

	function getGeometriaEstado(codigo) {

		var deffered = $q.defer();

		estadoService.getEstadoGeometryByCodigo(codigo)
			.then(function(response) {

				etapaGeo.limite = response.data.limite;
				deffered.resolve();

			})
			.catch(function() {

				mensagem.error("Não foi possível obter o limite do estado.");
				deffered.reject();

			});

			return deffered.promise;

	}

	function getDadosImovel(codigoImovel) {

		var deffered = $q.defer();

		imovelService.getImoveisCompletoByCodigo(codigoImovel)
			.then(function(response){

				if($scope.cadastro.caracterizacao.atividadesCaracterizacao[0].atividade.dentroEmpreendimento){

					etapaGeo.limite = response.data.geo;
				}

				imovelService.verificarLicenciamento(codigoImovel)
					.then((response) => {
						imovelValidoParaLicenciamento = true;
						deffered.resolve();
					})
					.catch((err) => {
						imovelValidoParaLicenciamento = false;
						mensagem.error("A condição do CAR não permite a solicitação de Licenciamento. Favor entrar em contato com a SEMA para verificação.");
						deffered.reject();
					});

			})
			.catch(function(err){

				mensagem.error("Não foi possível obter os dados do imóvel no CAR.");
				deffered.reject();

			});

		return deffered.promise;

	}

	function getTemasImovel() {

		var deffered = $q.defer();

		imovelService
			.getTemasByImovel($scope.cadastro.empreendimento.imovel.codigo, areasProibidas)
			.then(function(response){

				_(response.data).each(function(geoJson){

					var geo = JSON.parse(geoJson.geo);
					let feature = {type: 'Feature', geometry: geo};
					let codigo = geoJson.tema.codigo;

					if(areasProibidas.indexOf(codigo) !== -1) {

						if(!geometriasAreasProibidas[codigo]) {

							geometriasAreasProibidas[codigo] = feature;

						} else {

							geometriasAreasProibidas[codigo] = turf.union(geometriasAreasProibidas[codigo], feature);
						}

						if(codigo === 'APP_TOTAL') {
							areaAPP = geometriasAreasProibidas.APP_TOTAL;
						}
					}

					addOverlay(geoJson.tema.codigo, L.geoJson(geo, {
						style: function(){
							return app.Geo.overlays[geoJson.tema.codigo].style;
						}
					}));

				});
				deffered.resolve();
			});

		return deffered.promise;

	}

	var init = function() {

		if (!$scope.cadastro.dadosGEOCarregados){

			delete $scope.cadastro.geometry;

		} else {
			etapaGeo.controleAtualizacao = true;
		}

		$scope.cadastro.dadosGEOCarregados = true;

	};

	function initGeo() {
		
		if($scope.cadastro.empreendimento.imovel instanceof Object){

			if(!$scope.cadastro.caracterizacao.atividadesCaracterizacao[0].atividade.dentroEmpreendimento){

				$q.all([getDadosImovel($scope.cadastro.empreendimento.imovel.codigo), getGeometriaEstado($scope.cadastro.empreendimento.municipio.estado.codigo),getTemasImovel()])
					.then(init);
					
			}else{

				$q.all([getDadosImovel($scope.cadastro.empreendimento.imovel.codigo), getGeometriaMunicipio($scope.cadastro.empreendimento.municipio.id),getTemasImovel()])
					.then(init);
			}

		}else if (!$scope.cadastro.caracterizacao.atividadesCaracterizacao[0].atividade.dentroEmpreendimento){
		
			$q.all([getGeometriaEstado($scope.cadastro.empreendimento.municipio.estado.codigo)])
				.then(init);
		
		}else{

			$q.all([getGeometriaMunicipio($scope.cadastro.empreendimento.municipio.id)])
				.then(init);
		}
	}

	function getGeoLocalizacao() {
		return $rootScope.empreendimento.localizacao === "ZONA_RURAL" ?
			$rootScope.empreendimento.imovel.limite : $rootScope.empreendimento.empreendimentoEU.localizacao.geometria;
	}

};

exports.controllers.CaracterizacaoLSAGeoController = CaracterizacaoLSAGeoController;
