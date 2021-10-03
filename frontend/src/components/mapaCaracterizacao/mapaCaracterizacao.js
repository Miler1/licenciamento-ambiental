var MapaCaracterizacao = {

	bindings: {
		geo: '<',
		idMapa: '<',
		model: '=',
		isVisualizacao: '<',
		layerGroup: '<',
		atividadesCaracterizacao: '=',
		controleAtualizacao: '=',
		isCaracterizacao: '<',
		isEdicao: '<',
		localizacaoEmpreendimento: '=',
		estiloGeo: '<',
		isComplexo: '<',
		geometriasComplexo: '=',
		possuiAtividadeRural: '=',
		limiteEstado: '<',
		isEdicaoSemGeo: '<'
	},

	controller: function($scope, $timeout, mensagem, growlMessages, $rootScope) {

		var ctrl = this;

		ctrl.leafletMap = {};
		ctrl.addedLayers = {};
		ctrl.uploadLayer = null;
		ctrl.exibirControleCoordenadas = false;
		var posicaoAtividadeInicialmenteSelecionada = 0;
		ctrl.mapLoaded = false;
		ctrl.areaEmpreendimento = {};
		ctrl.areaEstadoDoEmpreendimento = {};
		var limiteInvalido = false;
		var isEdicaoDaGeometriaComplexo = false;

		//Flag para controle de atividades rurais vindas do entrada única para mostrar as camadas do CAR

		//TODO: Ajustar para aparecer as camadas do CAR
		$scope.possuiAtividadesRurais = ctrl.possuiAtividadeRural;

		var GeoJSONReader = new jsts.io.GeoJSONReader();

		angular.extend(ctrl, {
			layers: {
				baselayers: app.Geo.baselayers,
				overlays: app.Geo.overlays
			},
			controls: {
				fullscreen: {
					position: 'topright'
				},
				scale: {
					position: 'bottomleft',
					maxWidth: 100,
					metric: true,
					imperial: false,
					updateWhenIdle: false
				}
			},
			defaults: {
				controls: {
					layers: false
				},
				zoomControlPosition: 'bottomleft',
				attributionControl: false
			}

		});

		if(this.drawLayers) {
			ctrl.layers.overlays = angular.extend(angular.extend({}, app.Geo.overlays), this.drawLayers);
		}

		function alterarGeometria(geo, estilo) {

			if(geo && ctrl.leafletMap) {

				var objectGeo = JSON.parse(geo);

				if(ctrl.areaGeometria) {
					ctrl.leafletMap.removeLayer(ctrl.areaGeometria);
				}

				if(!estilo) {
					estilo = app.Geo.overlays.AREA_MUNICIPIO.style;
				}

				ctrl.areaGeometria = L.geoJson({
					'type': 'Feature',
					'geometry': objectGeo
				}, estilo);

				if(ctrl.drawLayers && ctrl.controleAtualizacao === false) {
					ctrl.drawLayers.clearLayers();
					ctrl.controleAtualizacao = true;
				}

				ctrl.leafletMap.addLayer(ctrl.areaGeometria);

				centralizarGeometria();

				if(ctrl.isComplexo && ctrl.geometriasComplexo){

					if (!ctrl.geometriasComplexo.features) {

						var featuresModoVisualizar = [];

						_.forEach(ctrl.geometriasComplexo, function (geometriaComplexo) {

							var features;

							if (!geometriaComplexo.geometria.geometries) {

								features = [JSON.parse(geometriaComplexo.geometria)];

							} else {

								features = geometriaComplexo.geometria.geometries;
							}

							_.forEach(features, function (feature) {

								var objeto = {
									'type': 'Feature',
									'geometry': feature
								};

								featuresModoVisualizar.push(objeto);
							});
						});

						ctrl.geometriasComplexo.features = featuresModoVisualizar;
					}

					ctrl.geometriasComplexo.features.forEach(addFeatureTo());

				} else {

					_.forEach(ctrl.atividadesCaracterizacao, function(atividadeCaracterizacao) {

						if(atividadeCaracterizacao.geometriasAtividade) {

							if (!atividadeCaracterizacao.geometriasAtividade.features) {

								var featuresModoVisualizar = [];

								_.forEach(atividadeCaracterizacao.geometriasAtividade, function (geometriasAtividade) {

									var features;

									if (!geometriasAtividade.geometria.geometries) {

										var geoms = JSON.parse(geometriasAtividade.geometria);

										if (geoms.geometries) {
											features = geoms.geometries;
										}else {
											features = [geoms];
										}

									} else {

										features = geometriasAtividade.geometria.geometries;
									}

									_.forEach(features, function (feature) {

										var objeto = {
											'type': 'Feature',
											'geometry': feature
										};

										featuresModoVisualizar.push(objeto);
									});
								});

								atividadeCaracterizacao.geometriasAtividade.features = featuresModoVisualizar;
							}

							atividadeCaracterizacao.geometriasAtividade.features.forEach(addFeatureTo(atividadeCaracterizacao.atividade.id));

						}

					});
				}
			}

		}

		function addFeatureTo(atividadeID) {
			return function(feature) {

				feature = L.geoJson(feature).getLayers()[0];

				if(ctrl.isComplexo) {
					ctrl.verificarPoligono(feature, undefined);
				} else {

					if (feature.feature.geometry.type === 'Point'){

						ctrl.verificarPonto(feature, atividadeID);

					}else if (feature.feature.geometry.type === 'LineString'){

						ctrl.verificarLinha(feature, atividadeID);

					} else {

						ctrl.verificarPoligono(feature, atividadeID);

					}
				}
			};
		}

		this.adicionarControleDeLayers = function () {

			/* Termos de uso: http://downloads2.esri.com/ArcGISOnline/docs/tou_summary.pdf */
			var arcGis = L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
				attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
			});
			var osm = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
				attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
			}).addTo(ctrl.leafletMap);
			var baseLayers = {
			  'Satélite': arcGis,
			  'Mapa': osm
			};
			var overlayLayers = {};
			var layerControl = L.control.layers(baseLayers, overlayLayers, {
			  collapsed: true,
			  position: 'bottomright'
			}).addTo(ctrl.leafletMap);
		};


		function defineMap( map ) {

			// Adiciona a localização do empeendimento no mapa
			if(ctrl.localizacaoEmpreendimento) {
				ctrl.areaEmpreendimento = L.geoJSON(JSON.parse(ctrl.localizacaoEmpreendimento));
				ctrl.areaEmpreendimento.addTo(map);
			}

			if(ctrl.limiteEstado){
				getGeometriasDoEstado();
			}

			ctrl.leafletMap = map;
			ctrl.showSidebar = true;

			defineLayers(ctrl.layerGroup);
			defineEvents();

			if(!ctrl.drawLayers) {

				ctrl.drawLayers = {};

				if (ctrl.isComplexo) {

					ctrl.drawLayers = new L.FeatureGroup();
					ctrl.leafletMap.addLayer(ctrl.drawLayers);

				} else {
					ctrl.atividadesCaracterizacao.forEach(function(ac) {

						var cor = '#'+Math.floor(Math.random()*16777215).toString(16);

						ctrl.drawLayers[ac.atividade.id] = new L.FeatureGroup();
						ctrl.drawLayers[ac.atividade.id].title = ac.atividade.nome;
						ac.color = cor;

						ctrl.leafletMap.addLayer(ctrl.drawLayers[ac.atividade.id]);
					});
				}
			}

			ctrl.leafletMap.addControl(
				new L.Control.Fullscreen({
					position: 'topleft',
				})
			);

			ctrl.leafletMap.addControl(
				L.easyButton('fa-crosshairs', function() {
					centralizarGeometria();
				},  'Centralizar na geometria')
			);

			ctrl.leafletMap.addControl(L.control.mousePosition());

			$timeout(function(){
				ctrl.leafletMap._onResize();
				alterarGeometria(ctrl.geo, ctrl.estiloGeo);
			}, 200);

			ctrl.adicionarControleDeLayers();

			if(ctrl.addedLayers) {
				var sidebar = L.control.sidebar({
					autopan: true,       // whether to maintain the centered map point when opening the sidebar
					closeButton: true,    // whether t add a close button to the panes
					container: 'sidebar', // the DOM container or #ID of a predefined sidebar container that should be used
					position: 'right',     // left or right
				}).addTo(ctrl.leafletMap);
			}

			$('.leaflet-control-attribution').remove();
			$('.leaflet-sidebar').prependTo($('.leaflet-control-container'));

			// Pega primeira posição para selecionar uma atividade inicialmente
			ctrl.atividadeSelecionada = ctrl.atividadesCaracterizacao[posicaoAtividadeInicialmenteSelecionada];

			if (!ctrl.isVisualizacao) {

				if (ctrl.isComplexo) {
					ctrl.setControlsGeometriaComplexo();
				} else {
					ctrl.changeAtividadeSelecionada();
				}
				if(ctrl.areaEmpreendimento){
					ctrl.leafletMap.fitBounds(ctrl.areaEmpreendimento.getBounds());
				}
			}

			ctrl.mapLoaded = true;

		}

		function getAtividadeCaracterizacao(atividadeID) {

			return _.find(ctrl.atividadesCaracterizacao, function(atividadeCaracterizacao) {
				return atividadeCaracterizacao.atividade.id === atividadeID;
			});

		}

		function changeDrawControl(tipoGeometry, editLayers) {

			if(ctrl.drawControl && ctrl.drawControl._map) {
				ctrl.drawControl.remove();
			}

			defineDrawControl(tipoGeometry, editLayers);

		}

		function defineDrawControl(tipoGeometry, editLayers) {

			if(!ctrl.isEdicao) {

				var drawTools = tipoGeometry ||  {
						polygon: false,
						circle: false,
						rectangle: false,
						polyline: false,
						marker: false
					};

				if (!ctrl.isEdicaoSemGeo){
					ctrl.drawControl = new L.Control.Draw({
						draw: {
							polygon: false,
							circle: false,
							rectangle: false,
							polyline: false,
							marker: false
						},
						edit: {
							featureGroup: editLayers,
							edit: false,
							remove: false
						}
					});
					ctrl.drawControl.remove();
					redefineDrawRetificacaoSolicitacaoSemGeo();
				}

				if(limiteInvalido){
					ctrl.drawControl = new L.Control.Draw({
						draw: {
							polygon: false,
							circle: false,
							rectangle: false,
							polyline: false,
							marker: false
						},
						edit: {
							featureGroup: editLayers,
							edit: false,
							remove: false
						}
					});
					ctrl.drawControl.remove();
					redefineDrawControlGeometriasComplexoQuandoInvalidas();
				}
				else{
					ctrl.drawControl = new L.Control.Draw({
						draw: drawTools,
						edit: {
							featureGroup: editLayers
						}
					});
				}

			} else if (!ctrl.isVisualizacao) {

				ctrl.drawControl = new L.Control.Draw({
					draw: {
						polygon: ctrl.atividadeSelecionada.atividade.temPoligono ? {allowIntersection: false, showArea: true } : false,
						circle: false,
						rectangle: false,
						polyline: ctrl.atividadeSelecionada.atividade.temLinha ? {metric: true, feet: false } : false,
						marker: ctrl.atividadeSelecionada.atividade.temPonto ? ctrl.atividadeSelecionada.atividade.temPonto : false
					},
					edit: {
						featureGroup: editLayers,
						remove: true
					}
				});
			}
			else {
				ctrl.drawControl = new L.Control.Draw({
					draw: false,
					edit: {
						featureGroup: editLayers,
						remove: false
				}
			});
		}

			if(!ctrl.isVisualizacao){
				ctrl.leafletMap.addControl(ctrl.drawControl);
				 $('.leaflet-draw-edit-remove').prop('title',"Deletar feição");
				 $('.leaflet-draw-edit-edit').prop('title',"Editar feição");
			}
		}

		function defineEvents() {

			ctrl.leafletMap.on(L.Draw.Event.CREATED, function (e) {

				var layer = e.layer;

				if (ctrl.isComplexo){

					ctrl.verificarPoligono(layer, undefined);

				} else {

					if(e.layerType === 'marker'){

						ctrl.verificarPonto(layer, ctrl.atividadeSelecionada.atividade.id);

					}else if (e.layerType === 'polyline'){

						ctrl.verificarLinha(layer, ctrl.atividadeSelecionada.atividade.id);

					} else{
						ctrl.verificarPoligono(layer, ctrl.atividadeSelecionada.atividade.id);
					}
				}

				$rootScope.$broadcast('geometriesUpdated');

			});

			ctrl.leafletMap.on(L.Draw.Event.DELETED, function (e) {

				var layers = e.layers;

				if(!layers)
					 return true;

				var drawLayer = ctrl.isComplexo ? ctrl.drawLayers : ctrl.drawLayers[ctrl.atividadeSelecionada.atividade.id];

				layers.eachLayer(function (layer) {

					if(drawLayer.hasLayer(layer)) {

						drawLayer.removeLayer(layer);
					}

				});

				var model = drawLayer.toGeoJSON();

				if (ctrl.isComplexo) {
					ctrl.geometriasComplexo = model;
				} else {
					getAtividadeCaracterizacao(ctrl.atividadeSelecionada.atividade.id).geometriasAtividade = model;
				}

				updateScope();

				$rootScope.$broadcast('geometriesUpdated');

				if(ctrl.isComplexo){
					ctrl.geometriasComplexo = undefined;

					var tools = {
						polygon: true,
						circle: false,
						rectangle: false,
						polyline: false,
						marker: false
					};
					changeDrawControl(tools, ctrl.drawLayers);
					setUploadShapeFile(tools, undefined);
				}

			});

			ctrl.leafletMap.on(L.Draw.Event.EDITED, function (e) {

				var layers = e.layers;

				if(!layers)
				 	return true;

				layers.eachLayer(function (layer) {

					if (ctrl.isComplexo){

						isEdicaoDaGeometriaComplexo = true;

						ctrl.verificarPoligono(layer, undefined);

					} else {

						if (layer instanceof L.Marker){

							ctrl.verificarPonto(layer, ctrl.atividadeSelecionada.atividade.id);

						}else if (layer instanceof L.Polyline){

							if(layer instanceof L.Polygon) {

								ctrl.verificarPoligono(layer, ctrl.atividadeSelecionada.atividade.id);

							}
							else {

								ctrl.verificarLinha(layer, ctrl.atividadeSelecionada.atividade.id);

							}

						} else {

							ctrl.verificarPoligono(layer, ctrl.atividadeSelecionada.atividade.id);
						}
					}

				});

				$rootScope.$broadcast('geometriesUpdated');

			});

			ctrl.leafletMap.on("overlayadd", function (e) {

				if(ctrl.drawLayers){

					for(var index in ctrl.drawLayers) {

						if(ctrl.drawLayers[index].length > 0) {
							ctrl.drawLayers[index].getLayers()[0].bringToFront();
						}

					}

				}

			});

		}

		function centralizarGeometria(layer) {
			if (ctrl.areaEmpreendimento && Object.entries(ctrl.areaEmpreendimento).length > 0) {
				ctrl.leafletMap.fitBounds(ctrl.areaEmpreendimento.getBounds());
			} else {
				var centralLayer = layer || ctrl.areaGeometria;
				ctrl.leafletMap.fitBounds(centralLayer.getBounds());
			}
		}

		function defineLayers(layers) {

			if(!layers)
				return;

			for(var indexBl in layers.baselayers) {

				if(layers.baselayers.length > 1)
					ctrl.layerControl.addLayer(layers.baselayers[indexBl]);
				else
					ctrl.layerControl.hideLayer(layers.baselayers[indexBl]);

			}

			for(var indexOl in layers.overlays) {

				var layerConfig = app.Geo.overlays[ indexOl ] || ctrl.drawLayers[ indexOl ];

				if(layerConfig.layerOptions) {

					layers.overlays[indexOl].options = layerConfig.layerOptions;

					if(layerConfig.legend)
						layers.overlays[indexOl].options.legend = layerConfig.legend;

					if(layerConfig.legendIcon)
						layers.overlays[indexOl].options.legendIcon = layerConfig.legendIcon;

					if(layerConfig.style)
						layers.overlays[indexOl].options.style = layerConfig.style;

				}

				ctrl.mostrarLayer(layers.overlays[indexOl]);
				ctrl.addedLayers[indexOl] = layers.overlays[indexOl];
			}

		}

		this.mostrarLayer = function(layer, fillOpacity){

			if(!this.leafletMap.hasLayer(layer)) {

				this.leafletMap.addLayer(layer);

				if(layer.setStyle){

					layer.setStyle({
						opacity: 1,
						fillOpacity: fillOpacity || 0.3
					});

				}else{

					layer.setOpacity(1);

				}

				layer.options.escondida = false;

			}

		};

		this.esconderLayer = function(layer){

			if(this.leafletMap.hasLayer(layer)) {

				this.leafletMap.removeLayer(layer);
				layer.options.escondida = true;

			}

		};

		this.setControlsGeometriaComplexo = function () {

			var tools = {
				polygon: true,
				circle: false,
				rectangle: false,
				polyline: false,
				marker: false
			};

			changeDrawControl(tools, ctrl.drawLayers);

			setUploadShapeFile(tools, undefined);
		};

		function removeUploadShapeFile() {

			if(ctrl.uploadLayer){
				ctrl.uploadLayer.removeControl(ctrl.leafletMap);
			}
		}

		function setUploadShapeFile (tipoGeometria, idAtividade) {

			if (!ctrl.isEdicao) {

				if(ctrl.isCaracterizacao && (!ctrl.uploadLayer || !ctrl.uploadLayer.uploadControl._map)) {
					ctrl.uploadLayer = new app.utils.UploadLayer(ctrl.leafletMap, ctrl.verificarPonto, ctrl.verificarPoligono, ctrl.verificarLinha, tipoGeometria, mensagem, growlMessages, idAtividade);
				} else if(ctrl.isCaracterizacao && (ctrl.uploadLayer || ctrl.uploadLayer.uploadControl._map)) {
					removeUploadShapeFile();
					ctrl.uploadLayer = new app.utils.UploadLayer(ctrl.leafletMap, ctrl.verificarPonto, ctrl.verificarPoligono, ctrl.verificarLinha, tipoGeometria, mensagem, growlMessages, idAtividade);
				}
			}
		}

		this.changeAtividadeSelecionada = function() {

			if(ctrl.atividadeSelecionada.atividade.temPonto){

				if((!ctrl.coordenadasManual || !ctrl.coordenadasManual._map) && !ctrl.isEdicao) {

					ctrl.coordenadasManual = L.easyButton('fa-object-ungroup', function() {
						ctrl.toggleComponenteCoordenadasManualmente();
					},  'Adicionar coordenadas manualmente');

					ctrl.leafletMap.addControl(ctrl.coordenadasManual);

				}

			}

			var tipoGeometria = {
				polygon: ctrl.atividadeSelecionada.atividade.temPoligono ? {allowIntersection: false, showArea: true } : false,
				circle: false,
				rectangle: false,
				polyline: ctrl.atividadeSelecionada.atividade.temLinha ? {metric: true, feet: false } : false,
				marker: ctrl.atividadeSelecionada.atividade.temPonto ? ctrl.atividadeSelecionada.atividade.temPonto : false
			};


			changeDrawControl(tipoGeometria, ctrl.drawLayers[ctrl.atividadeSelecionada.atividade.id]);

			setUploadShapeFile(tipoGeometria, ctrl.atividadeSelecionada.atividade.id);

		};

		this.loadMap = function(leafletMapId) {

			$scope.possuiAtividadesRurais = ctrl.possuiAtividadeRural;

			var mapID = leafletMapId || ctrl.idMapa;

			defineMap(new L.Map(mapID, {
				minZoom: 4,
				maxZoom: 17
			}).setView([-3, -52.497545]));
		};

		this.$onInit = function(){

			$timeout(ctrl.loadMap, 300);

		};

		this.centralizarGeometriasDesenhadas = function(atividadeID) {

			if(atividadeID){
				ctrl.leafletMap.fitBounds(ctrl.drawLayers[atividadeID].getBounds());
			}else{
				ctrl.leafletMap.fitBounds(ctrl.drawLayers.getBounds());
			}

		};

		function verificaComplexoDentroEmpreendimento() {

			var atividadesFiltradas = ctrl.atividadesCaracterizacao.filter(function (atividadeCaracterizacao) {
				return atividadeCaracterizacao.atividade.dentroEmpreendimento === true;
			});

			return atividadesFiltradas !== undefined && atividadesFiltradas.length > 0;
		}

		function invalidarGeometriaDesenhada(layer, atividadeID) {

			if (atividadeID) {

				ctrl.drawLayers[atividadeID].removeLayer(layer);
				getAtividadeCaracterizacao(atividadeID).geometriasAtividade = ctrl.drawLayers[atividadeID].toGeoJSON();
				ctrl.model = ctrl.drawLayers[atividadeID].toGeoJSON();

			}else {

				ctrl.drawLayers.removeLayer(layer);
				ctrl.geometriasComplexo = ctrl.drawLayers.toGeoJSON();
				ctrl.model = ctrl.drawLayers.toGeoJSON();
			}
		}

		function validarLayerDentroGeometriaBase(layer, atividadeID, geometriaBase, tipo) {

			if(ctrl.limiteEstado){
				getGeometriasDoEstado();
			}

			switch (tipo) {

				case 'Polygon':

					var areaGeometria = geometriaBase.toGeoJSON();

					if(areaGeometria.type === 'FeatureCollection'){
						areaGeometria = areaGeometria.features[0].geometry;
					}else{
						areaGeometria = areaGeometria.geometry;
					}

					var intersection = getIntersection(areaGeometria,layer);

					if(intersection){
						return true;
					} else {
						invalidarGeometriaDesenhada(layer, atividadeID);
						return false;
					}

					break;

				case 'LineString':

					let areaGeometriaBase = geometriaBase.toGeoJSON();

					var jstsAreaGeometry = GeoJSONReader.read(areaGeometriaBase.features[0]);
					var jstsLineGeometry = GeoJSONReader.read(layer.toGeoJSON());

					if (jstsAreaGeometry.geometry.intersects(jstsLineGeometry.geometry)) {

						return true;

					} else {

						invalidarGeometriaDesenhada(layer, atividadeID);
						return false;
					}

					break;

				case 'Point':

					if (leafletPip.pointInLayer([layer._latlng.lng, layer._latlng.lat], geometriaBase, true).length > 0){

						return true;

					} else {

						invalidarGeometriaDesenhada(layer, atividadeID);
						return false;
					}

					break;
			}
		}

		this.verificarPonto = function(layer, atividadeID) {

			var atividadeCaracterizacao = getAtividadeCaracterizacao(atividadeID);

			var intersecaoEstado = validarIntersecaoAtividadeComEstado(layer) ? validarIntersecaoAtividadeComEstado(layer) : false;

			if (!atividadeCaracterizacao.atividade.dentroEmpreendimento && !validarLayerDentroGeometriaBase(layer, atividadeID, ctrl.areaGeometria, 'Point') && !intersecaoEstado){

				if(atividadeCaracterizacao.atividade.codigo ==="0000"){

					if(ctrl.possuiAtividadeRural){
						ctrl.areaGeometria.bindPopup("O ponto deve ser desenhado dentro do limite do empreendimento.").openPopup();

					}else if (atividadeCaracterizacao.atividade.dentroMunicipio){
						ctrl.areaGeometria.bindPopup("O ponto deve ser desenhado dentro do limite do município.").openPopup();

					}else{
						ctrl.areaGeometria.bindPopup("O ponto deve ser desenhado dentro do limite do estado.").openPopup();

					}

				}else{

					ctrl.areaGeometria.bindPopup("O ponto deve ser desenhado dentro do limite do estado.").openPopup();

				}
				return false;
			}

			if (atividadeCaracterizacao.atividade.dentroEmpreendimento && !validarLayerDentroGeometriaBase(layer, atividadeID, ctrl.areaEmpreendimento, 'Point')){

				ctrl.areaEmpreendimento.bindPopup("O ponto deve ser marcado dentro do limite do empreendimento.").openPopup();

				return false;
			}

			if(layer.options) {
				layer.options.title = ctrl.atividadeSelecionada.atividade.nome;
				layer.options.legendIcon = {
					css: {
						'color': '#2b82cb',
						'padding-left': '4px',
						'font-size': '16px'
					},
					icon: 'fa fa-map-marker'
				};
				layer.options.escondida = false;
			}

			ctrl.drawLayers[atividadeID].addLayer(layer);

			getAtividadeCaracterizacao(atividadeID).geometriasAtividade = ctrl.drawLayers[atividadeID].toGeoJSON();
			ctrl.model = ctrl.drawLayers[atividadeID].toGeoJSON();

			updateScope();

			if(ctrl.unidadeMedida)
				ctrl.geometryArea.update(ctrl.model);

			centralizarGeometriasDasAtividades(atividadeID);

			return true;

		};

		this.verificarLinha = function(layer, atividadeID) {

			var atividadeCaracterizacao = getAtividadeCaracterizacao(atividadeID);

			var intersecaoEstado = validarIntersecaoAtividadeComEstado(layer) ? validarIntersecaoAtividadeComEstado(layer) : false;

			if (!atividadeCaracterizacao.atividade.dentroEmpreendimento && !validarLayerDentroGeometriaBase(layer, atividadeID, ctrl.areaGeometria, 'LineString') && !intersecaoEstado){

				if(atividadeCaracterizacao.atividade.codigo === "0000"){

					if(ctrl.possuiAtividadeRural){
						ctrl.areaGeometria.bindPopup("A linha deve ser desenhada dentro do limite do empreendimento.").openPopup();

					}else if (atividadeCaracterizacao.atividade.dentroMunicipio){
						ctrl.areaGeometria.bindPopup("A linha  deve ser desenhado dentro do limite do município.").openPopup();

					}else{
						ctrl.areaGeometria.bindPopup("A linha  deve ser desenhado dentro do limite do estado.").openPopup();
					}

			}else{

					ctrl.areaGeometria.bindPopup("A linha deve ser desenhada dentro do limite do estado.").openPopup();

				}
				return false;
			}

			if (atividadeCaracterizacao.atividade.dentroEmpreendimento && !validarLayerDentroGeometriaBase(layer, atividadeID, ctrl.areaEmpreendimento, 'LineString')){

				ctrl.areaEmpreendimento.bindPopup("A linha deve ser desenhada dentro do limite do empreendimento.").openPopup();

				return false;
			}

			ctrl.drawLayers[atividadeID].addLayer(layer);

			getAtividadeCaracterizacao(atividadeID).geometriasAtividade = ctrl.drawLayers[atividadeID].toGeoJSON();
			ctrl.model = ctrl.drawLayers[atividadeID].toGeoJSON();

			if(layer.options) {
				layer.options.title = ctrl.atividadeSelecionada.atividade.nome;
				layer.options.legendIcon = {
					css: {
						'color': '#3388ff',
						'padding-left': '4px',
						'font-size': '16px'
					},
					icon: 'fa fa-arrows-v'
				};
				layer.options.escondida = false;
			}

			let tempLatLng = layer._latlngs[0];
			let totalDistance = 0.00000;
			layer._latlngs.forEach(latlng => {
				// if(tempLatLng === null){
				// 	tempLatLng = latlng;
				// 	return;
				// }

				totalDistance += tempLatLng.distanceTo(latlng);
				tempLatLng = latlng;
			});
			layer.bindPopup((totalDistance/1000).toFixed(2) + ' km');

			updateScope();

			centralizarGeometriasDasAtividades(atividadeID);

			return true;

		};

		this.verificarPoligono = function(layer, atividadeID) {

			if (atividadeID) {
				ctrl.verificarPoligonoAtividade (layer, atividadeID);
			} else {
				ctrl.verificarPoligonoComplexo(layer);
			}
		};

		this.verificarPoligonoAtividade = function(layer, atividadeID){

			var atividadeCaracterizacao = getAtividadeCaracterizacao(atividadeID);

			var intersecaoEstado = false;

			if(!ctrl.isVisualizacao){
				intersecaoEstado = validarIntersecaoAtividadeComEstado(layer);
			}


			if (!atividadeCaracterizacao.atividade.dentroEmpreendimento && !validarLayerDentroGeometriaBase(layer, atividadeID, ctrl.areaEstadoDoEmpreendimento, 'Polygon') && !validarIntersecaoAtividadeComEstado(layer) ){
				if(atividadeCaracterizacao.atividade.codigo === "0000"){

					if(ctrl.possuiAtividadeRural){
						ctrl.areaGeometria.bindPopup("O polígono deve ser desenhado dentro do limite do empreendimento.").openPopup();

					}else if (atividadeCaracterizacao.atividade.dentroMunicipio){
						ctrl.areaGeometria.bindPopup("O polígono deve ser desenhado dentro do limite do município.").openPopup();

					}else{
						ctrl.areaGeometria.bindPopup("O polígono deve ser desenhado dentro do limite do estado.").openPopup();
					}
				}else{
					ctrl.areaGeometria.bindPopup("O polígono deve ser desenhado dentro do limite do estado.").openPopup();

				}
				return false;
			}

			if (atividadeCaracterizacao.atividade.dentroEmpreendimento && !validarLayerDentroGeometriaBase(layer, atividadeID, ctrl.areaEmpreendimento, 'Polygon')){

				ctrl.leafletMap.fitBounds(ctrl.areaEmpreendimento.getBounds());

				ctrl.areaEmpreendimento.bindPopup("O polígono deve ser desenhado dentro dos limites do empreendimento").openPopup();
				return false;
			}

			var areaGeometria = atividadeCaracterizacao.atividade.dentroEmpreendimento ? ctrl.areaEmpreendimento.toGeoJSON() : ctrl.areaEstadoDoEmpreendimento.toGeoJSON();

			if(areaGeometria.type === 'FeatureCollection'){
				areaGeometria = areaGeometria.features[0].geometry;
			}else{
				areaGeometria = areaGeometria.geometry;
			}

			let layerToBeAdd;

			if(ctrl.isVisualizacao) {
				layerToBeAdd = layer;
			} else if(intersecaoEstado){
				layerToBeAdd = L.geoJson(intersecaoEstado).getLayers()[0];
			}else{
				var intersection = getIntersection(areaGeometria,layer);
				layerToBeAdd = L.geoJson(intersection).getLayers()[0];
			}

			if(ctrl.drawLayers[atividadeID].hasLayer(layer)) {
				ctrl.drawLayers[atividadeID].removeLayer(layer);
			}

			if(ctrl.isVisualizacao) {
				ctrl.atividadeSelecionada = ctrl.atividadesCaracterizacao.filter(ac => ac.atividade.id === atividadeID)[0];
			}

			if(layerToBeAdd.options) {
				layerToBeAdd.options.title = ctrl.atividadeSelecionada.atividade.nome;
				layerToBeAdd.options.legend = {
					'background-color': ctrl.atividadeSelecionada.color
				};
				layer.options.escondida = false;
			}

			layerToBeAdd.setStyle({
				color: ctrl.atividadeSelecionada.color,
				fillOpacity: 1,
				stroke: layerToBeAdd.feature.geometry.type === 'LineString'
			});

			addPopup(layerToBeAdd);

			ctrl.drawLayers[atividadeID].addLayer(layerToBeAdd);
			getAtividadeCaracterizacao(atividadeID).geometriasAtividade = ctrl.drawLayers[atividadeID].toGeoJSON();
			ctrl.model = ctrl.drawLayers[atividadeID].toGeoJSON();

			if(ctrl.unidadeMedida)
				ctrl.geometryArea.update(ctrl.model);

			updateScope();

			centralizarGeometriasDasAtividades(atividadeID);

			return true;

		};

		//Metodo que verifica a adição de poligonos
		this.verificarPoligonoComplexo = function(layer){

			var intersecaoEstado = validarIntersecaoAtividadeComEstado(layer) ? validarIntersecaoAtividadeComEstado(layer) : false;

			if (ctrl.drawLayers && Object.keys(ctrl.drawLayers._layers).length > 0 && !isEdicaoDaGeometriaComplexo) {
				ctrl.areaGeometria.bindPopup("Para o georreferenciamento de atividades em complexo é permitida apenas uma geometria.").openPopup();
				return false;
			}

			var complexoDentroEmpreendimento = verificaComplexoDentroEmpreendimento();

			if (!complexoDentroEmpreendimento && !validarLayerDentroGeometriaBase(layer, undefined, ctrl.areaGeometria, 'Polygon') && !intersecaoEstado){

				if(atividadeCaracterizacao.atividade.codigo === "0000"){

					if(ctrl.possuiAtividadeRural){
						ctrl.areaGeometria.bindPopup("O polígono deve ser desenhada dentro do limite do empreendimento.").openPopup();

					}else if (atividadeCaracterizacao.atividade.dentroMunicipio){
						ctrl.areaGeometria.bindPopup("O polígono deve ser desenhado dentro do limite do município.").openPopup();

					}else{
						ctrl.areaGeometria.bindPopup("O polígono deve ser desenhado dentro do limite do estado.").openPopup();
					}

				}else{
					ctrl.areaGeometria.bindPopup("O polígono deve ser desenhado dentro do limite do estado.").openPopup();

				}

				return false;
			}

			if (complexoDentroEmpreendimento && !validarLayerDentroGeometriaBase(layer, undefined, ctrl.areaEmpreendimento, 'Polygon')){

				if(ctrl.drawLayers && Object.keys(ctrl.drawLayers._layers).length === 0){
					limiteInvalido = true;
					ctrl.drawLayers = new L.FeatureGroup();
					ctrl.leafletMap.addLayer(ctrl.drawLayers);

					if(ctrl.drawControl){
						if(ctrl.drawControl && ctrl.drawControl._map) {
							ctrl.drawControl.remove();
						}
					}
					let ferramentasDesenho = {
						poligono: ctrl.atividadeSelecionada.atividade.temPoligono ? { allowIntersection: false,showArea: true } : false,
						circulo: false,
						retangulo: false,
						linha: ctrl.atividadeSelecionada.atividade.temLinha ? {metric: true, feet: false} : false,
						ponto: ctrl.atividadeSelecionada.atividade.temPonto ? ctrl.atividadeSelecionada.atividade.temPonto : false
					};
					let ferramentasEdicao =  { remover: false, editar: false };

					let toolBoxInvalid = getDrawControl(ferramentasDesenho,ferramentasEdicao);

					ctrl.leafletMap.addControl(toolBoxInvalid);

					redefineDrawControlGeometriasComplexoQuandoInvalidas();
				}

				ctrl.areaEmpreendimento.bindPopup("O polígono deve ser desenhado dentro do limite do empreendimento").openPopup();

				return false;
			}

			var areaGeometria = complexoDentroEmpreendimento ? ctrl.areaEmpreendimento.toGeoJSON() : ctrl.areaGeometria.toGeoJSON();

			if(areaGeometria.type === 'FeatureCollection'){
				areaGeometria = areaGeometria.features[0].geometry;
			}else{
				areaGeometria = areaGeometria.geometry;
			}

			let layerToBeAdd;

			if(intersecaoEstado){
				layerToBeAdd = L.geoJson(intersecaoEstado).getLayers()[0];
			}else{
				var intersection = getIntersection(areaGeometria,layer);
				layerToBeAdd = L.geoJson(intersection).getLayers()[0];
			}

			//var layerToBeAdd = L.geoJson(intersection).getLayers()[0];
			var cor = '#'+Math.floor(Math.random()*16777215).toString(16);

			if(ctrl.drawLayers.hasLayer(layer)) {
				ctrl.drawLayers.removeLayer(layer);
			}

			if(layerToBeAdd.options) {
				layerToBeAdd.options.legend = {
					'background-color': cor
				};
				layer.options.escondida = false;
			}

			layerToBeAdd.setStyle({
				color: cor,
				fillOpacity: 1,
				stroke: layerToBeAdd.feature.geometry.type === 'LineString'
			});

			ctrl.drawLayers.addLayer(layerToBeAdd);

			ctrl.geometriasComplexo = ctrl.drawLayers.toGeoJSON();
			ctrl.model = ctrl.drawLayers.toGeoJSON();

			if(ctrl.unidadeMedida) {
				ctrl.geometryArea.update(ctrl.model);
			}

			var tools = {
				polygon: false,
				circle: false,
				rectangle: false,
				polyline: false,
				marker: false
			};

			limiteInvalido = false;

			changeDrawControl(tools, ctrl.drawLayers);

			removeUploadShapeFile();

			updateScope();

			ctrl.centralizarGeometriasDesenhadas();

			return true;

		};

		function centralizarGeometriasDasAtividades(atividadeID){

			var latLngBounds = new L.latLngBounds();

			if(ctrl.isVisualizacao){

				for(var index in ctrl.drawLayers) {
					if(Object.keys(ctrl.drawLayers[index]).length > 0 && ( ctrl.drawLayers[index].getBounds() || ctrl.drawLayers[index]._layers !== undefined) ) {
						latLngBounds.extend(ctrl.drawLayers[index].getBounds());
					}
				}
				ctrl.leafletMap.fitBounds(latLngBounds);
			}else{
				ctrl.centralizarGeometriasDesenhadas(atividadeID);
			}
		}

		function addPopup(layer) {
			let areaLayer = (turf.area(turf.polygon(layer.feature.geometry.coordinates))/10000).toFixed(2);
			layer.bindPopup(`Atividade: ${ctrl.atividadeSelecionada.atividade.nome}<br>Área vetorizada: ${areaLayer} ha`);
		}

		function getIntersection(coordenadas,geometria){

			var geom = geometria.toGeoJSON().geometry;

			if(geom.type === 'GeometryCollection'){
				const reducer = (accumulator, currentValue) => turf.intersect(accumulator, currentValue);
				var arr = geom.geometries;
				arr.push(coordenadas);
				return arr.reduce(reducer);
			}
			return turf.intersect(coordenadas, geom);
		}

		this.toggleComponenteCoordenadasManualmente = function() {

			ctrl.exibirControleCoordenadas = !ctrl.exibirControleCoordenadas;

			updateScope();

		};

		this.onGeometriaAdicionadaManualmente = function(data){

			var geometriaValida;

			if(data.tipo === 'marker'){
				geometriaValida = ctrl.verificarPonto(data.geometria, ctrl.atividadeSelecionada.atividade.id);
			}else if(data.tipo === 'polyline'){
				geometriaValida = ctrl.verificarLinha(data.geometria, ctrl.atividadeSelecionada.atividade.id);
			}else if(data.tipo === 'polygon'){
				geometriaValida = ctrl.verificarPoligono(data.geometria, ctrl.atividadeSelecionada.atividade.id);
			}

			if(geometriaValida){
				ctrl.exibirControleCoordenadas = false;
			}

			return geometriaValida;

		};

		function getGeometriasDoEstado(){

			ctrl.areaEstadoDoEmpreendimento = ctrl.limiteEstado;

			if(typeof ctrl.areaEstadoDoEmpreendimento === "string"){
				ctrl.areaEstadoDoEmpreendimento = L.geoJSON(JSON.parse(ctrl.areaEstadoDoEmpreendimento));
			}

			if(ctrl.areaEstadoDoEmpreendimento === 'FeatureCollection'){
				ctrl.areaEstadoDoEmpreendimento.limite = ctrl.areaEstadoDoEmpreendimento.limite.features[0].geometry;
			}

		}

		function validarIntersecaoAtividadeComEstado(feature){

			let geom = feature.toGeoJSON().geometry;

			if( ctrl.areaEstadoDoEmpreendimento.limite && geom.type === 'GeometryCollection'){

				const reducer = (accumulator, currentValue) => turf.intersect(accumulator, currentValue);
				let arr = geom.geometries;
				arr.push(ctrl.areaEstadoDoEmpreendimento.limite);
				return arr.reduce(reducer);
			}

			if(ctrl.areaEstadoDoEmpreendimento.limite){
				return turf.intersect(ctrl.areaEstadoDoEmpreendimento.limite, feature.toGeoJSON().geometry);
			}
			return false;
		}

		function redefineDrawControlGeometriasComplexoQuandoInvalidas() {

			if (ctrl.drawLayers && Object.keys(ctrl.drawLayers._layers).length > 0) {

				if(ctrl.drawControl) {
					ctrl.drawControl.remove();
				}

				if(ctrl.drawControl._container){
					ctrl.drawControl._container.remove();
				}

				let ferramentasDesenho = { poligono: false, circulo: false, retangulo: false, linha: false, ponto: false };
				let ferramentasEdicao =  { remover: true, editar: true };

				let toolBox = getDrawControl(ferramentasDesenho,ferramentasEdicao);

				ctrl.leafletMap.addControl(toolBox);

			}
		}

		function redefineDrawRetificacaoSolicitacaoSemGeo() {

			const ferramentasDesenho = { poligono: false, circulo: false, retangulo: false, linha: false, ponto: false };
			const ferramentasEdicao =  { remover: false, editar: false };

			const toolBox = getDrawControl(ferramentasDesenho,ferramentasEdicao);

			ctrl.leafletMap.addControl(toolBox);
		}

		function getDrawControl(drawTools,editorTools){

			let toolBox = {
				polygon: drawTools.poligono,
				circle: drawTools.circulo,
				rectangle: drawTools.retangulo,
				polyline: drawTools.linha,
				marker: drawTools.ponto
			};

			ctrl.drawControl = {};

			ctrl.drawControl = new L.Control.Draw({
				draw: toolBox,
				edit: {
					featureGroup: ctrl.drawLayers,
					remove: editorTools.remover,
					edit: editorTools.editar
				}
			});

			return ctrl.drawControl;
		}

		$scope.$on('destroyMap', () => 	ctrl.leafletMap.remove());

		function updateScope() {

			try {

				_.defer(function(){$scope.$apply();});

			} catch(e) {}

		}

	},

	templateUrl: 'components/mapaCaracterizacao/mapaCaracterizacao.html'

};

exports.directives.MapaCaracterizacao = MapaCaracterizacao;
