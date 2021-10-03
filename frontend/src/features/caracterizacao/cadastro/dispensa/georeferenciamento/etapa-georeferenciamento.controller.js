var CaracterizacaoDLAGeoController = function($scope, $rootScope, mensagem, municipioService,estadoService, imovelService, config, $q) {

	var etapaGeo = this;

	//area para armazenar cálculo de sobreposicao com APP
	var areaAPP = "";

	var areasProibidas = ['APP_TOTAL', 'NASCENTE_OLHO_DAGUA','ARL_TOTAL','AREA_USO_RESTRITO_DECLIVIDADE_25_A_45','AREA_USO_RESTRITO_PANTANEIRA'];
	var geometriasAreasProibidas = {};

	var tipologiasRetristivasDLA = [app.ATIVIDADE_COM_RESTRICAO_DLA.ID_ENERGIA_REDE_DISTRIBUICAO,
	app.ATIVIDADE_COM_RESTRICAO_DLA.ID_ENERGIA_SUBDISTRICAO_URBANA,
	app.ATIVIDADE_COM_RESTRICAO_DLA.ID_ENERGIA_SUBDISTRICAO_RURAL,
	app.ATIVIDADE_COM_RESTRICAO_DLA.ID_SANEAMENTO_SISTEMA_SIMPLIFICADO,
	app.ATIVIDADE_COM_RESTRICAO_DLA.ID_SANEAMENTO_DRENAGEM_SUPERFICIAL,
	app.ATIVIDADE_COM_RESTRICAO_DLA.ID_PAVIMENTACAO_EXECUCAO,
	app.ATIVIDADE_COM_RESTRICAO_DLA.ID_RODOVIAS_RAMAIS_ESTRADA_VICINAL,
	app.ATIVIDADE_COM_RESTRICAO_DLA.ID_RODOVIAS_RAMAIS_SUBSTITUICAO_ESTRADA_VICINAL];

	etapaGeo.passoValido = passoValido;
	etapaGeo.proximo = proximo;
	etapaGeo.initGeo = initGeo;
	etapaGeo.localizacaoEmpreendimento = getGeoLocalizacao();

	$scope.cadastro.etapas.GEO.passoValido = passoValido;
	$scope.cadastro.etapas.GEO.beforeEscolherEtapa = undefined;

	var mensagemErro;
	var isAreaVerificada = false;
	var isIntersectionsVerificado = false;
	var hasIntersection = false;
	var imovelValidoParaLicenciamento = false;
	var posicaoAtividadeInicialmenteSelecionada = 0;
	var GeoJSONReader = new jsts.io.GeoJSONReader();

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

	function getMenssagemSobreposicao(nomeCamada) {
		return 'Atenção! A área desenhada possui sobreposição com área de ' + nomeCamada +
			' do imóvel e por isso a solicitação não pode ser concluída. Por favor, apresente nova área da atividade.';
	}

	function passoValido() {

		if(todasAtividadesTemGeometria() && !isIntersectionsVerificado &&
			$rootScope.empreendimento.localizacao === app.LOCALIZACOES_EMPREENDIMENTO.ZONA_RURAL){

			if(interseccaoComAtividade(geometriasAreasProibidas.AREA_USO_RESTRITO_DECLIVIDADE_25_A_45)) {
				mensagemErro = getMenssagemSobreposicao('Uso Restrito com declide de 25° até 45°');
				isIntersectionsVerificado = true;
				hasIntersection = true;
				return false;
			}

			if(interseccaoComAtividade(geometriasAreasProibidas.AREA_USO_RESTRITO_PANTANEIRA)) {
				mensagemErro = getMenssagemSobreposicao('Uso Restrito Pantaneira');
				isIntersectionsVerificado = true;
				hasIntersection = true;
				return false;
			}

			if(interseccaoComAtividade(geometriasAreasProibidas.ARL_TOTAL)) {
				mensagemErro = getMenssagemSobreposicao('Reserva Legal ');
				isIntersectionsVerificado = true;
				hasIntersection = true;
				return false;
			}

			if( interseccaoComAtividade(geometriasAreasProibidas.APP_TOTAL)) {
				mensagemErro = getMenssagemSobreposicao('Área de preservação permamente');
				isIntersectionsVerificado = true;
				hasIntersection = true;
				return false;
			}

			if(interseccaoComAtividade(geometriasAreasProibidas.NASCENTE_OLHO_DAGUA)) {
				mensagemErro = getMenssagemSobreposicao('Nascente ou olho d\'água ');
				isIntersectionsVerificado = true;
				hasIntersection = true;
				return false;
			}

			isIntersectionsVerificado = true;

		}

		return verificaAreaInformadaComDesenho() && !hasIntersection;
	}

	function verificaAreaInformadaComDesenho() {

		if(isAreaVerificada || todasAtividadesTemGeometria()){
			return true;
		} else {
			mensagemErro = "Adicione uma geometria no mapa para cada atividade.";
			return false;
		}

	}

	function todasAtividadesTemGeometria() {

		var hasGeometry = true;

		_.forEach($scope.cadastro.caracterizacao.atividadesCaracterizacao, function(atividadeCaracterizacao) {
			if(_.isEmpty(atividadeCaracterizacao.geometriasAtividade) ||
				_.isEmpty(atividadeCaracterizacao.geometriasAtividade.features)) {
				hasGeometry = false;
			}
		});
		return hasGeometry;
	}

	function interseccaoComAtividade(feature) {

		if(!feature) {
			return null;
		}

		var atividadeCaracterizacao = $scope.cadastro.caracterizacao.atividadesCaracterizacao[0];
		var geometryCollection = {type: "GeometryCollection", geometries: []};

		if(atividadeCaracterizacao.geometriasAtividade.features) {

			for(var j=0; j< atividadeCaracterizacao.geometriasAtividade.features.length; j++) {

				var featureAtividade = atividadeCaracterizacao.geometriasAtividade.features[j];

				addToGeometryCollection(geometryCollection, featureAtividade);

			}

		}

		var jstsGeometry = GeoJSONReader.read(geometryCollection);
		var jstsFeature = GeoJSONReader.read(feature);

		return jstsGeometry.intersects(jstsFeature.geometry);
	}

	function addToGeometryCollection(geometryCollection, feature) {

		if(feature.type === 'FeatureCollection') {

			for(var j=0; j< feature.features.length; j++) {

				var featureAtividade = feature.features[j];

				addToGeometryCollection(geometryCollection, featureAtividade);

			}

		} else {

			geometryCollection.geometries.push(feature.geometry);

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

		if(etapaGeo.passoValido()){
			$scope.cadastro.proximo();
		} else {
			mensagem.warning(mensagemErro, {ttl: 10000});
		}
	}

	function getGeometriaMunicipio(id) {

		etapaGeo.estiloLimite = app.Geo.overlays.AREA_MUNICIPIO.style;

		municipioService.getMunicipioGeometryById(id)
			.then(function(response) {

				etapaGeo.limite = response.data.limite;
				init();

			})
			.catch(function() {

				mensagem.error("Não foi possível obter o limite do município.");

			});

	}

	function getGeometriaEstado(codigo) {

		etapaGeo.estiloLimite = app.Geo.overlays.AREA_MUNICIPIO.style;

		estadoService.getEstadoGeometryByCodigo(codigo)
			.then(function(response) {

				etapaGeo.limite = response.data.limite;
				init();

			})
			.catch(function() {

				mensagem.error("Não foi possível obter o limite do estado.");

			});

	}

	function getDadosImovel(codigoImovel) {

		etapaGeo.estiloLimite = app.Geo.overlays.AREA_IMOVEL.style;

		imovelService.getImoveisCompletoByCodigo(codigoImovel)
			.then(function(response){

				etapaGeo.limite = response.data.geo;

				imovelService.verificarLicenciamento(codigoImovel)
					.then((response) => {
						imovelValidoParaLicenciamento = true;
					})
					.catch((err) => {
						imovelValidoParaLicenciamento = false;
						mensagem.error("A condição do CAR não permite a solicitação de Licenciamento. Favor entrar em contato com a SEMA para verificação.");
					});

			})
			.catch(function(){

				mensagem.error("Não foi possível obter os dados do imóvel no CAR.");

			});
	}

	function getTemasImovel() {

		imovelService
			.getTemasByImovel($scope.cadastro.empreendimento.imovel.codigo, areasProibidas)
			.then(function(response){

				_(response.data).each(function(geoJson){

					var geo = JSON.parse(geoJson.geo),
						feature = {type: 'Feature', geometry: geo},
						codigo = geoJson.tema.codigo;

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

					addOverlay(codigo, L.geoJson(geo, {
						style: function(){
							return app.Geo.overlays[codigo].style;
						}
					}));

				});

			});

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

		var atividade = $scope.cadastro.caracterizacao.atividadesCaracterizacao[posicaoAtividadeInicialmenteSelecionada].atividade;

		if(tipologiasRetristivasDLA.indexOf(atividade.id) > -1) {

			var jurisdicao = $scope.cadastro.empreendimento.jurisdicao;

			if(jurisdicao == 'MUNICIPAL' ) {

				getGeometriaMunicipio($scope.cadastro.empreendimento.municipio.id);

			}else if(jurisdicao == 'ESTADUAL' || jurisdicao == 'FEDERAL') {

				getGeometriaEstado($scope.cadastro.empreendimento.municipio.estado.codigo);
				
			}

		} else if($scope.cadastro.empreendimento.imovel instanceof Object){


			getDadosImovel($scope.cadastro.empreendimento.imovel.codigo);

			getTemasImovel();


		} else{
			if (atividade.dentroMunicipio) {

				getGeometriaMunicipio($scope.cadastro.empreendimento.municipio.id);
				
			} else {

				getGeometriaEstado($scope.cadastro.empreendimento.municipio.estado.codigo);
				
			}
		}
	}

	etapaGeo.temAtividadeRural = function (){

		return $rootScope.empreendimento.localizacao === "ZONA_RURAL";
	};

	function getGeoLocalizacao() {
		return $rootScope.empreendimento.localizacao === "ZONA_RURAL" ?
			$rootScope.empreendimento.imovel.limite : $rootScope.empreendimento.empreendimentoEU.localizacao.geometria;
	}
};

exports.controllers.CaracterizacaoDLAGeoController = CaracterizacaoDLAGeoController;
