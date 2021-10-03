var Mapa = {

	bindings: {
		geo: '<',
		idMapa: '<',
		model: '=',
		isVisualizacao: '<',
		tipoGeometry : '<',
		layerGroup: '<',
		unidadeMedida: '=',
		controleAtualizacao: '=',
		isCaracterizacao: '<',
		cadastro: '=',
		estiloGeo: '<'
	},

	controller: function($scope, $timeout, mensagem, growlMessages) {

		var ctrl = this;

		ctrl.map = null;
		ctrl.geoAntigo = null;
		ctrl.modelAntiga = null;
		ctrl.areaGeometria = null;
		ctrl.drawLayers = null;
		ctrl.uploadLayer = null;

		ctrl.exibirControleCoordenadas = false;

		this.centralizarGeometria = function() {

			if(ctrl.areaGeometria){
				 ctrl.map.fitBounds(ctrl.areaGeometria.getBounds());
			}

			if(ctrl.model && ctrl.drawLayers){
				//ctrl.drawLayers.bringToFront();
				this.centralizarGeometriasDesenhadas();
			}
		};

		this.alterarGeometria = function(geo, estilo) {

			if(geo && ctrl.map) {

				var objectGeo = JSON.parse(geo);

				if(ctrl.areaGeometria)
					ctrl.map.removeLayer(ctrl.areaGeometria);

				if(!estilo) {
					estilo = app.Geo.overlays.AREA_MUNICIPIO.style;
				}

				ctrl.areaGeometria = L.geoJson({
					'type': 'Feature',
					'geometry': objectGeo
				}, estilo);

				ctrl.map.addLayer(ctrl.areaGeometria);

				if(ctrl.drawLayers) {
					ctrl.drawLayers.clearLayers();
					ctrl.modelAntiga = null;
					ctrl.controleAtualizacao = true;
				}

				if(ctrl.model && (ctrl.model !== ctrl.modelAntiga)) {

					ctrl.modelAntiga = ctrl.model;

					var geometria = null;

					if(typeof ctrl.model === "string")
						geometria = JSON.parse(ctrl.model);
					else
						geometria = ctrl.model;

					var ponto = getGeometriaPonto(geometria);

					if(!ctrl.drawLayers) {
						ctrl.drawLayers = new L.FeatureGroup();
					}

					ctrl.drawLayers.addLayer(ponto.getLayers()[0]);
				}

				this.centralizarGeometria();
				
			}

		};

		this.adicionarControleDeLayers = function () {

			/* Termos de uso: http://downloads2.esri.com/ArcGISOnline/docs/tou_summary.pdf */
			var arcGis = L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
				attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
			});
			var osm = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
				attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
			}).addTo(ctrl.map);
			var baseLayers = {
			  'Satélite': arcGis,
			  'Mapa': osm
			};
			var overlayLayers = {};
			var layerControl = L.control.layers(baseLayers, overlayLayers, {
			  collapsed: true,
			  position: 'bottomright'
			}).addTo(ctrl.map);
		};

		this.inicializarMapa = function() {

			ctrl.map = new L.Map(ctrl.idMapa, {
				minZoom: 4,
				maxZoom: 17
			}).setView([-3, -52.497545]);

			if(ctrl.idMapa === 'zonaRural' || ctrl.cadastro && ctrl.cadastro.empreendimento.localizacao && ctrl.cadastro.empreendimento.localizacao === 'ZONA_RURAL') {
				ctrl.drawTools = {
					polygon: false,
					circle: false,
					rectangle: false,
					polyline: false,
					marker: false,
					edit: false,
					remove: false
				};
				ctrl.setLimitesEmpreendimentoComLimiteCar();
			}
			else{
				ctrl.drawTools = ctrl.tipoGeometry ||  {
					polygon: false,
					circle: false,
					rectangle: false,
					polyline: false,
					marker: false
				};
			}


			if(!ctrl.drawLayers) {
				ctrl.drawLayers = new L.FeatureGroup();
			}

			if(ctrl.idMapa !== 'zonaRural'){
				ctrl.alterarGeometria(ctrl.geo, ctrl.estiloGeo);
			}

			var drawControl = new L.Control.Draw({
				draw: ctrl.drawTools,
				edit: {
					featureGroup: ctrl.drawLayers
				}
			});

			var graphicScale = L.control.graphicScale({
				position: 'bottomleft',
				fill: 'fill'
			});

			var botaoCentralizar = L.easyButton('fa-crosshairs', function() {
				ctrl.centralizarGeometria();
			},  'Centralizar na geometria');

			ctrl.map.addControl(new L.Control.Fullscreen({position: 'topleft'}));

			var botaoCoordenadasManualmente = L.easyButton('fa-object-ungroup', function() {
				ctrl.toggleComponenteCoordenadasManualmente();
			},  'Adicionar coordenadas manualmente');

			ctrl.map.addLayer(ctrl.drawLayers);

			ctrl.map.addControl(L.control.mousePosition());
			ctrl.map.addControl(graphicScale);
			ctrl.map.addControl(botaoCentralizar);

			ctrl.adicionarControleDeLayers();

			if(ctrl.unidadeMedida) {

				ctrl.geometryArea = L.Control.geometryArea( {
					geometry: ctrl.model,
					metric: ctrl.unidadeMedida
				});

				ctrl.map.addControl(ctrl.geometryArea);
				$('.geometry-area').prependTo($('.leaflet-control-mouseposition').parent());

			}

			if(ctrl.layerGroup){
				ctrl.map.addControl(ctrl.layerGroup);
			}

			if(ctrl.model && (ctrl.model !== ctrl.modelAntiga)) {

				ctrl.modelAntiga = ctrl.model;

				var geometria = null;

				if(typeof ctrl.model === "string")
					geometria = JSON.parse(ctrl.model);
				else
					geometria = ctrl.model;

				var ponto = getGeometriaPonto(geometria);

				if(!ctrl.drawLayers) {
					ctrl.drawLayers = new L.FeatureGroup();
				}

				ctrl.drawLayers.addLayer(ponto.getLayers()[0]);

			}

			if (!ctrl.isVisualizacao && ctrl.idMapa !== 'zonaRural') {

				if(ctrl.idMapa === 'zonaUrbana' || ctrl.cadastro && ctrl.cadastro.empreendimento.localizacao && ctrl.cadastro.empreendimento.localizacao === 'ZONA_URBANA'){
					ctrl.map.addControl(botaoCoordenadasManualmente);
				}

				ctrl.map.addControl(drawControl);

				if(ctrl.idMapa === 'zonaRural'){
					changeDrawControl(drawControl);
				}

				ctrl.uploadLayer = new app.utils.UploadLayer(ctrl.map, ctrl.verificarPonto, ctrl.verificarPoligono, ctrl.verificarLinha, ctrl.tipoGeometry, mensagem, growlMessages);

				ctrl.map.on(L.Draw.Event.CREATED, function (e) {

					var layer = e.layer;

					if(e.layerType === 'marker'){

						ctrl.verificarPonto(layer);

					}else{
						ctrl.verificarPoligono(layer);
					}

				});

				ctrl.map.on(L.Draw.Event.DELETED, function (e) {

					if(!e.layers.getLayers()[0])
					 	return true;
					
					ctrl.model = undefined;

					ctrl.drawLayers.removeLayer(e.layer);

					if(ctrl.unidadeMedida)
						ctrl.geometryArea.update(ctrl.model);

					updateScope();

				});

				ctrl.map.on(L.Draw.Event.EDITED, function (e) {

					var layers = e.layers;

					if(!layers)
					 	return true;

					layers.eachLayer(function (layer) {
						if (layer instanceof L.Marker){
							ctrl.verificarPonto(layer);
						}else {
							ctrl.verificarPoligono(layer);
						}
					});

				});

				ctrl.map.on("overlayadd", function (e) {
					
					if(ctrl.drawLayers && ctrl.drawLayers.getLayers().length > 0){

						if (ctrl.model && ctrl.model.type === 'Polygon')
							ctrl.drawLayers.getLayers()[0].bringToFront();
					}
				});

			}

			 $timeout(function(){
			 	ctrl.map._onResize();
			 	ctrl.centralizarGeometria();
			 }, 200);

			 $('.leaflet-control-attribution').remove();

		};

		this.toggleComponenteCoordenadasManualmente = function() {

			ctrl.exibirControleCoordenadas = !ctrl.exibirControleCoordenadas;

			updateScope();

		};

		this.onGeometriaAdicionadaManualmente = function(data){

			var geometriaValida;

			if(data.tipo === 'marker'){
				geometriaValida = ctrl.verificarPonto(data.geometria);
			}else if(data.tipo === 'polygon'){
				geometriaValida = ctrl.verificarPoligono(data.geometria);
			}

			if(geometriaValida){
				ctrl.exibirControleCoordenadas = false;
			}

			return geometriaValida;

		};

		$scope.$on('destroyMap', function (event, data) {
			ctrl.map.remove();
		});

		this.centralizarGeometriasDesenhadas = function() {
			ctrl.map.fitBounds(ctrl.drawLayers.getBounds());
		};

		this.verificarPonto = function(layer) {

			if(leafletPip.pointInLayer([layer._latlng.lng, layer._latlng.lat], ctrl.areaGeometria, true).length > 0) {
				
				if(ctrl.drawLayers && ctrl.drawLayers.getLayers().length !== 0){
					ctrl.drawLayers.clearLayers();
				}

				ctrl.drawLayers.addLayer(layer);

				ctrl.model = layer.toGeoJSON().geometry;

				updateScope();

				if(ctrl.unidadeMedida)
					ctrl.geometryArea.update(ctrl.model);

				ctrl.centralizarGeometriasDesenhadas();

				return true;

			} else {

				ctrl.drawLayers.removeLayer(layer);

				ctrl.areaGeometria.bindPopup("O ponto deve ser marcado dentro do limite da geometria.").openPopup();

				return false;

			}

		};

		this.verificarPoligono = function(layer){
			
			var areaGeometria = ctrl.areaGeometria.toGeoJSON();

			if(areaGeometria.type === 'FeatureCollection'){
				areaGeometria = areaGeometria.features[0].geometry;
			}else{
				areaGeometria = areaGeometria.geometry;
			}
			
			var intersection = turf.intersect(areaGeometria, layer.toGeoJSON().geometry);

			if(intersection){

				if(ctrl.drawLayers && ctrl.drawLayers.getLayers().length !== 0){
					ctrl.drawLayers.clearLayers();
				}

				ctrl.drawLayers.addLayer(L.geoJson(intersection).getLayers()[0]);

				ctrl.model = intersection.geometry;

				if(ctrl.unidadeMedida)
					ctrl.geometryArea.update(ctrl.model);

				updateScope();

				ctrl.centralizarGeometriasDesenhadas();

				return true;

			}else{

				let layer = ctrl.drawLayers.getLayers()[0];

				if (layer) {
					ctrl.drawLayers.removeLayer(layer);
					ctrl.model = null;
					ctrl.cadastro.empreendimento.coordenadas = null;
				}
				
				ctrl.areaGeometria.bindPopup("Os pontos devem ser marcados dentro do limite da geometria.").openPopup();

				return false;

			}

		};

		this.setLimitesEmpreendimentoComLimiteCar = function(){

			let geom = JSON.parse(ctrl.geo);
			let geoEmp = geom;

			if(geom.type === 'FeatureCollection'){
				geoEmp = geom.features[0].geometry;
			}

			let estilo = ctrl.estiloGeo;

			ctrl.areaGeometria = L.geoJson({
				'type': 'Feature',
				'geometry': geoEmp
			}, estilo);

			var areaGeometria = ctrl.areaGeometria.toGeoJSON();

			if( ctrl.idMapa === 'zonaRural' || ctrl.cadastro && ctrl.cadastro.empreendimento.localizacao && ctrl.cadastro.empreendimento.localizacao === 'ZONA_RURAL' ){
				ctrl.model = areaGeometria;
			}

		};

		function getGeometriaPonto (geometria) {

			var ponto = null;

			if(geometria.type === 'FeatureCollection'){
				ponto = L.geoJson({
					'type': 'Feature',
					'geometry': geometria.features[0].geometry
				});
			}else{
				ponto = L.geoJson({
					'type': 'Feature',
					'geometry': geometria
				});
			}
			return ponto;
		}

		function changeDrawControl(editLayers){
			if(ctrl.drawLayers && ctrl.drawLayers._map) {
				ctrl.drawLayers.remove();
			}
			defineDrawControl(editLayers);
		}

		function defineDrawControl(editLayers){

			var drawTools = editLayers.options.draw ||  {
				polygon: false,
				circle: false,
				rectangle: false,
				polyline: false,
				marker: false
			};

			ctrl.drawControl = new L.Control.Draw({
				draw: drawTools,
				edit: {
					featureGroup: editLayers
				}
			});

			ctrl.map.addControl(ctrl.drawLayers);
		}

		this.$onInit = function(){

			ctrl.tipoGeometry = ctrl.tipoGeometry ||  {
				polygon: false,
				circle: false,
				rectangle: false,
				polyline: false,
				marker: false
			};

			$timeout(ctrl.inicializarMapa);

		};

		this.$doCheck = function() {
			
			if(ctrl.geoAntigo !== ctrl.geo) {
				ctrl.geoAntigo = ctrl.geo;

				this.alterarGeometria(ctrl.geo, ctrl.estiloGeo);
				
			}

		};

		var updateScope = function() {

			try{
				_.defer(function(){

					$scope.$apply();
				});
			}catch(e){}

		};

	},

	templateUrl: 'components/mapa/mapa.html'

};

exports.directives.Mapa = Mapa;
