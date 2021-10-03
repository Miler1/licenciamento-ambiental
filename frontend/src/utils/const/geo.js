var copy = function(original, modificacoes) {
	return $.extend({}, original, modificacoes);
};

var Geo = {

	center: 'AREA_IMOVEL',

	baselayers: {

		MOSAICO_RAPIDEYE: {
			name: 'Mosaico Rapideye',
			type: 'xyz',
			url: 'http://www.car.gov.br/mosaicos/{z}/{x}/{y}.jpg',
			layerOptions: {
				group: 'Mapas de Fundo',
				title: 'Mosaico Rapideye',
				tms: true,
				legend: {
					'display': 'none'
				}
			}
		},
		ARCGIS: {
			name: 'ArcGIS',
			type: 'xyz',
			url: 'http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}',
			layerOptions: {
				group: 'Mapas de Fundo',
				title: 'ArcGIS',
				tms: true,
				legend: {
					'display': 'none'
				}
			}
		}

	},

	overlays: {
		AREA_IMOVEL: {
			name: 'Área do Imóvel',
			type: 'featureGroup',
			visible: true,
			legend: {
				'background-color': 'none',
				'border': '2px dashed #FFEB00'
			},
			style: {
				fill: false,
				color: '#FFEB00',
				opacity: 0.5,
				dashArray: '5, 10'
			},
			layerOptions: {
				group: 'CAR-PA',
				title: 'Área do Imóvel'
			}
		},
		AREA_MUNICIPIO: {
			name: 'Área do município',
			type: 'featureGroup',
			visible: true,
			legend: {
				'background-color': 'none',
				'border': '2px solid #FFFFFF'
			},
			style: {
				fill: false,
				color: '#FFFFFF',
				opacity: 1
			},
			layerOptions: {
				group: 'CAR-PA',
				title: 'Área do município'
			}
		},
		VEGETACAO_NATIVA: {
			name: 'Remanescente de Vegetação Nativa',
			type: 'featureGroup',
			legend: {
				'background-color': '#52B573',
				'border': '2px solid #099C3A'
			},
			style: {
				color: '#099C3A',
				fillColor: '#52B573',
				fillOpacity: 0.8,
				stroke: false
			},
			layerOptions: {
				group: 'CAR-PA',
				title: 'RVN - Remanescente de Vegetação Nativa'
			}
		},
		APP_TOTAL: {
			name: 'Área de Preservação Permanente',
			type: 'featureGroup',
			legend: {
				'background-color': '#FFFB00'
			},
			style: {
				stroke: false,
				fillColor: '#FFFB00',
				fillOpacity: 0.8
			},
			layerOptions: {
				group: 'CAR-PA',
				title: 'APP - Área de Preservação Permanente'
			}
		},
		ARL_TOTAL: {
			name: 'Reserva Legal',
			type: 'featureGroup',
			legend: {
				'background-color': '#339F31',
				'border': '2px solid #A86015'
			},
			style: {
				color: '#A86015',
				weight: 2,
				fillColor: '#339F31',
				fillOpacity: 0.8
			},
			layerOptions: {
				group: 'CAR-PA',
				title: 'RL - Reserva Legal'
			}
		},
		AREA_CONSOLIDADA: {
			name: 'Área consolidada (desenhada no CAR)',
			type: 'featureGroup',
			legend: {
				'background-color': '#DDDDDD'
			},
			style: {
				stroke: false,
				fillColor: '#DDDDDD',
				fillOpacity: 0.8
			},
			layerOptions: {
				group: 'CAR-PA',
				title: 'AC - Área consolidada'
			}
		},
		NASCENTE_OLHO_DAGUA: {
			name: 'Nascente',
			type: 'featureGroup',
			legendIcon: {
				css: {
					'color': '#2b82cb',
					'padding-left': '4px',
					'font-size': '16px'
				},
				icon: 'fa fa-map-marker'
			},
			layerOptions: {
				group: 'CAR-PA',
				title: 'Nascente'
			}
		},
		PRODES: {
			name: 'PRODES',
			type: 'featureGroup',
			legend: {
				'background-color': 'red',
				'border': '2px solid red'
			},
			style: {
				color: 'red',
				weight: 2,
				fillColor: 'red',
				fillOpacity: 0.8
			},
			layerOptions: {
				group: 'CAR-PA',
				title: 'PRODES'
			}
		},
		LDI: {
			name: 'Lista de desmatamento ilegal',
			type: 'featureGroup',
			legend: {
				'background-color': 'red',
				'border': '2px solid red'
			},
			style: {
				color: 'red',
				weight: 2,
				fillColor: 'red',
				fillOpacity: 0.8
			},
			layerOptions: {
				group: 'CAR-PA',
				title: 'LDI - Lista de desmatamento ilegal'
			}
		}

	}
};

exports.Geo = Geo;