var AdicionarCoordenadas = {

	bindings: {
		onGeometriaAdicionadaManualmente: '=',
		toggleComponente: '=',
		configGeometrias: '='
	},

	controller: function(coordenadasService, mensagem, $timeout, $element){

		var ctrl = this;

		ctrl.sistemasCoordenadas = sistemasCoordenadas;

		var sistemaAtual = ctrl.sistemaCoordenadas = ctrl.sistemasCoordenadas.gms;

		ctrl.coordenadas = [];

		ctrl.permitirEscolherTipoGeometria = false;

		this.$postLink = function() {

			/**
			 * Recebe a configuração dos desenhos passados para o mapa.
			 * Por padrão, permitir desenhar apenas pontos.
			 */
			if(ctrl.configGeometrias) {

				if(ctrl.configGeometrias.polygon && ctrl.configGeometrias.marker) {
					ctrl.permitirEscolherTipoGeometria = true;
				}

				if(!ctrl.configGeometrias.polygon) {
					ctrl.tipoGeometria = 'marker';
				}else{
					ctrl.tipoGeometria = 'polygon';
				}

			}else{

				ctrl.tipoGeometria = 'marker';
				
			}

			ctrl.adicionarCoordenada();

		};

		ctrl.alterarSistemaCoordenadas = function(id) {

			if(ctrl.sistemaCoordenadas && ctrl.sistemaCoordenadas.id === id){
				return;
			}

			ctrl.sistemaCoordenadas = ctrl.sistemasCoordenadas[id];

			atualizarSistemaCoordenadas();

		};

		ctrl.adicionarCoordenada = function() {

			if(ctrl.coordenadas.length > 0){
				var ultimaCoordenada = ctrl.coordenadas[ctrl.coordenadas.length - 1];
				if(!(ultimaCoordenada.x && ultimaCoordenada.y)){
					mensagem.warning('Preencha a última coordenada antes de adicionar uma nova');
					return;
				}
			}

			ctrl.coordenadas.push({x: undefined, y: undefined});

			atualizarMascara();

		};

		ctrl.removerCoordenada = function() {

			ctrl.coordenadas.pop();

		};

		ctrl.podeAdicionarCoordenadas = function() {

			if(!coordenadasSaoValidas()){
				return false;
			}

			if(ctrl.tipoGeometria === 'marker'){
				return true;
			}

			if(ctrl.tipoGeometria === 'polygon' && ctrl.coordenadas.length >= 3){
				return true;
			}

		};

		/**
		 * @return {boolean} Indica se há coordenadas e se todas são válidas
		 */
		var coordenadasSaoValidas = function() {

			if(!ctrl.coordenadas || ctrl.coordenadas.length === 0){
				return false;
			}

			for (var i = 0; i < ctrl.coordenadas.length; i++) {
				if(!(ctrl.coordenadas[i].x && ctrl.coordenadas[i].y)){
					return false;
				}
			}

			return true;

		};

		/**
		 * Aceita as coordenadas informadas. Reprojeta os valores para LatLng e dispara um
		 * evento a ser capturado pela controller do mapa.
		 */
		ctrl.aceitarCoordenadas = function() {

			if(!coordenadasSaoValidas()){
				return;
			}

			var coordenadasConvertidas,
				latLngs = [];

			/* Garantindo que as coordenadas enviada são LatLng (EPSG:4674) */
			for (var i = 0; i < ctrl.coordenadas.length; i++) {
				coordenadasConvertidas = coordenadasService.converter(sistemaAtual.crs, ctrl.sistemasCoordenadas.dd.crs,
																	sistemaAtual.formato, ctrl.sistemasCoordenadas.dd.formato,
																	ctrl.coordenadas[i].x, ctrl.coordenadas[i].y);

				latLngs.push(L.latLng(coordenadasConvertidas.y, coordenadasConvertidas.x));

			}

			var sucessoAdicaoGeometria;

			if(ctrl.tipoGeometria === 'marker'){

				sucessoAdicaoGeometria = ctrl.onGeometriaAdicionadaManualmente({tipo: 'marker', geometria: L.marker(latLngs[0])});

			}else{

				sucessoAdicaoGeometria = ctrl.onGeometriaAdicionadaManualmente({tipo: 'polygon', geometria: L.polygon(latLngs)});

			}

			if(sucessoAdicaoGeometria){
				limpar();
			}

		};

		ctrl.esconder = function() {

			if(ctrl.toggleComponente){
				ctrl.toggleComponente();
			}

		};

		ctrl.atualizarPoligono = function() {

			if(ctrl.coordenadas.length > 2){
				var latLngs = latLngsFromCoordenadas();
				if(latLngs){
					camadaPoligono.setLatLngs(latLngs);
				}
			}

		};

		/**
		 * Recalcula todas as coordenadas para o sistema de coordenadas escolhido
		 */
		function atualizarSistemaCoordenadas() {

			var coordenadasConvertidas;

			for (var i = 0; i < ctrl.coordenadas.length; i++) {

				coordenadasConvertidas = coordenadasService.converter(sistemaAtual.crs, ctrl.sistemaCoordenadas.crs,
																	sistemaAtual.formato, ctrl.sistemaCoordenadas.formato,
																	ctrl.coordenadas[i].x, ctrl.coordenadas[i].y, true);

				atualizarMascara();
				ctrl.coordenadas[i] = coordenadasConvertidas;
			}


			sistemaAtual = ctrl.sistemaCoordenadas;

		}

		function atualizarMascara() {

			$timeout(function(){

				var mask = coordenadasService.getMask(ctrl.sistemaCoordenadas.tipo);

				$($element).find('.coordenadas-x').mask(mask.x[0], mask.x[1]);
				$($element).find('.coordenadas-y').mask(mask.y[0], mask.y[1]);

			}, 10);

		}

		function latLngsFromCoordenadas() {

			var latLngs = [];
			for (var i = 0; i < ctrl.coordenadas.length; i++) {
				if(!(ctrl.coordenadas[i].x && ctrl.coordenadas[i].y)){
					return null;
				}
				latLngs.push(L.latLng(ctrl.coordenadas[i].y, ctrl.coordenadas[i].x));
			}

			return latLngs;

		}

		function limpar() {

			ctrl.coordenadas = [];

			ctrl.adicionarCoordenada();

			sistemaAtual = ctrl.sistemaCoordenadas = ctrl.sistemasCoordenadas.gms;

		}

	},

	templateUrl: 'components/adicionarCoordenadas/adicionarCoordenadas.html'

};

var sistemasCoordenadas = {
	gms: {
		id: 'gms',
		nome: 'Graus, minutos e segundos',
		tipo: 'gms',
		crs: 'EPSG:4674',
		formato: 'gms',
		labelX: 'Longitude',
		labelY: 'Latitude',
		placeholderX: 'Ex: W53°11\'22.1000"',
		placeholderY: 'Ex: S04°17\'25.0000"'
	},
	dd: {
		id: 'dd',
		nome: 'SIRGAS 2000, Geográfico',
		tipo: 'dd',
		crs: 'EPSG:4674',
		formato: 'dd',
		labelX: 'Longitude',
		labelY: 'Latitude',
		placeholderX: 'Ex: -53,4567',
		placeholderY: 'Ex: -4,3210'
	},
	utm21N: {
		id: 'utm21N',
		nome: 'SIRGAS 2000, Projeção UTM - 21N',
		tipo: 'utm',
		crs: 'EPSG:31975',
		formato: 'dd',
		labelX: 'X',
		labelY: 'Y',
		placeholderX: 'Ex: 946803,54',
		placeholderY: 'Ex: 9588777,123'
	},
	utm21S: {
		id: 'utm21S',
		nome: 'SIRGAS 2000, Projeção UTM - 21S',
		tipo: 'utm',
		crs: 'EPSG:31981',
		formato: 'dd',
		labelX: 'X',
		labelY: 'Y',
		placeholderX: 'Ex: 946803,54',
		placeholderY: 'Ex: 9588777,123'
	},
	utm22N: {
		id: 'utm22N',
		nome: 'SIRGAS 2000, Projeção UTM - 22N',
		tipo: 'utm',
		crs: 'EPSG:31976',
		formato: 'dd',
		labelX: 'X',
		labelY: 'Y',
		placeholderX: 'Ex: 946803,54',
		placeholderY: 'Ex: 9588777,123'
	},
	utm22S: {
		id: 'utm22S',
		nome: 'SIRGAS 2000, Projeção UTM - 22S',
		tipo: 'utm',
		crs: 'EPSG:31982',
		formato: 'dd',
		labelX: 'X',
		labelY: 'Y',
		placeholderX: 'Ex: 946803,54',
		placeholderY: 'Ex: 9588777,123'
	},
	utm23S: {
		id: 'utm23S',
		nome: 'SIRGAS 2000, Projeção UTM - 23S',
		tipo: 'utm',
		crs: 'EPSG:31983',
		formato: 'dd',
		labelX: 'X',
		labelY: 'Y',
		placeholderX: 'Ex: 946803,54',
		placeholderY: 'Ex: 9588777,123'
	}
};

exports.directives.AdicionarCoordenadas = AdicionarCoordenadas;
