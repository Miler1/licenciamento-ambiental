L.drawLocal = {
	"change": {
		"toolbar": {
			"swapper": "Selecionar feição para edição"
		},
		"cancel": "Cancelar"
	},
	"draw": {
		"toolbar": {
			"actions": {
				"title": "Cancelar desenho",
				"text": "Cancelar"
			},
			"buttons": {
				"polyline": "Desenhar linhas",
				"polygon": "Desenhar um polígono",
				"rectangle": "Desenhar um retângulo",
				"circle": "Desenhar um círculo",
				"marker": "Desenhar um marcador",
				"imports": "Importar shapefile"
			},
			"imports": {
				"shapeZip": {
					"text": "Shapefile",
					"title": "Arquivo zip contendo os arquivos do shapefile"
				}
			},
			"undo": {
				"title": "Desfaz o último ponto",
				"text": "Desfazer"

			},
			"finish": {
				"title": "Finalizar desenho",
				"text": "Finalizar"

			}
		},
		"handlers": {
			"circle": {
				"tooltip": {
					"start": "Clique e arraste para desenhar um círculo."
				}
			},
			"marker": {
				"tooltip": {
					"start": "Clique no mapa para adicionar um marcador."
				}
			},
			"polygon": {
				"tooltip": {
					"start": "Clique para começar desenhar uma geometria.",
					"cont": "Clique para continuar desenhando uma geometria.",
					"end": "Clique no primeiro ponto para fechar a geometria."
				}
			},
			"polyline": {
				"error": "<strong>Erro:</strong> as arestas da geometria não podem se cruzar.",
				"tooltip": {
					"start": "Clique para começar a desenhar uma linha.",
					"cont": "Clique para continuar desenhando a linha.",
					"end": "Duplo-clique para terminar o desenho da linha."
				}
			},
			"rectangle": {
				"tooltip": {
					"start": "Clique e arraste para desenhar um retêngulo."
				}
			},
			"simpleshape": {
				"tooltip": {
					"end": "Solte o mouse para terminar o desenho."
				}
			}
		}
	},
	"edit": {
		"toolbar": {
			"actions": {
				"save": {
					"title": "Salvar alterações.",
					"text": "Salvar"
				},
				"cancel": {
					"title": "Cancelar edições, descartar todas as alterações.",
					"text": "Cancelar"
				}
			},
			"buttons": {
				"edit": "Editar feição",
				"remove": "Deletar feição"
			}
		},
		"handlers": {
			"edit": {
				"tooltip": {
					"text": "Arraste o marcador para alterar sua localização.",
					"subtext": "Clique 'Cancelar' para desfazer as mudanças."
				}
			},
			"remove": {
				"tooltip": {
					"text": "Clique em um desenho para removê-lo."

				}
			}
		}
	}
};

L.Control.Fullscreen.addInitHook(function() {
	L.setOptions(this, {
		title: {
			'false': 'Ver em tela cheia',
			'true': 'Sair da tela cheia'
		}
	});
});

L.Control.Zoom.addInitHook(function() {
	L.setOptions(this, {
		zoomInTitle: 'Aumentar Zoom',
		zoomOutTitle: 'Diminuir Zoom'
	});
});