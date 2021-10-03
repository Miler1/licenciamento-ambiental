var VisualizarCaracterizacaoController = function ($scope, $location, $routeParams, $window, mensagem, imovelService, caracterizacaoService, municipioService, estadoService, $q) {

	var ctrl = this;
	var controleLayerAdicionada = {};

	$scope.caracterizacao = {};
	$scope.caracterizacao.respostasPorAtividade = [];
	$scope.historicos = [];
	$scope.geometriaEmpreendimento = {};

	function agruparPerguntas() {
		if($scope.caracterizacao && $scope.caracterizacao.respostas) {
			_.each($scope.caracterizacao.respostas, function(resposta){
				var adicionado = false;
				_.each($scope.caracterizacao.respostasPorAtividade, function(respostaPorAtividade){
					if(!adicionado) {
						if(respostaPorAtividade.atividade.id === resposta.pergunta.atividades[0].id) {
							respostaPorAtividade.respostas.push(resposta);
							adicionado = true;
						}
					}
				});
				if(!adicionado) {
					$scope.caracterizacao.respostasPorAtividade = [];
					$scope.caracterizacao.respostasPorAtividade.push({
						atividade: resposta.pergunta.atividades[0],
						respostas: [resposta]
					});
				}
			});
		}
	}

	$scope.temAtividadeRural = function (){
		return $scope.controleAtualizacao && $scope.caracterizacao.empreendimento.localizacao === "ZONA_RURAL";
	};

	function obterGeometriaDoEmpreendimento() {

		if ($scope.controleAtualizacao) {

			if ($scope.temAtividadeRural()) {
				$scope.localizacaoEmpreendimento = $scope.caracterizacao.empreendimento.imovel.limite;
			} else {
				$scope.localizacaoEmpreendimento = $scope.caracterizacao.empreendimento.empreendimentoEU.localizacao.geometria;
			}

		}

	}

	function getTemasImovel(codigoImovel) {

		let areasProibidas = ['APP_TOTAL', 'NASCENTE_OLHO_DAGUA','ARL_TOTAL','AREA_USO_RESTRITO_DECLIVIDADE_25_A_45','AREA_USO_RESTRITO_PANTANEIRA'];
		let geometriasAreasProibidas = {};

		imovelService
		.getTemasByImovel(codigoImovel, areasProibidas)
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

	function addOverlay(nomeLayer, leafletLayer) {

		if(!$scope.layerGroup) {
			$scope.layerGroup = {};
		}

		if(!$scope.layerGroup.overlays) {
			$scope.layerGroup.overlays = {};
		}

		if(controleLayerAdicionada[nomeLayer]){
			controleLayerAdicionada[nomeLayer].addLayer(leafletLayer);
		} else {
			$scope.layerGroup.overlays[nomeLayer] = leafletLayer;
			controleLayerAdicionada[nomeLayer] = leafletLayer;
		}

	}

	function verificaEmprendimentoRuralEPegaTemas(empreendimento){
		if(empreendimento.imovel){
			getTemasImovel(empreendimento.imovel.codigo);
		}
	}

	let getHistorico = (idCaracterizacao) => {
		caracterizacaoService.getHistorico(idCaracterizacao).then(result => {
			$scope.historicos = result.data;
		}).catch(() =>
			mensagem.error("Não foi possível Listar os dados do histórico.")
		);
	};

	function ordenarPerguntas() {

		$scope.caracterizacao.respostas.sort(function (a, b) {

			if (a.pergunta.ordem > b.pergunta.ordem) {
			  return 1;
			}
			if (a.pergunta.ordem < b.pergunta.ordem) {
			  return -1;
			}
			// a must be equal to b
			return 0;
		  });

	}

	$scope.init = (idCaracterizacao) => {

		caracterizacaoService.listCompleto(idCaracterizacao)
		.then(function(response){
			$scope.caracterizacao = response.data;
			ordenarPerguntas();
			$scope.caracterizacao.valorTaxaLicenciamento = response.data.valorTaxaLicenciamento;
			agruparPerguntas();
			tratarDadosQuestionario($scope.caracterizacao.questionario3);
			verificaEmprendimentoRuralEPegaTemas($scope.caracterizacao.empreendimento);
			$scope.caracterizacao.estiloLimite = app.Geo.overlays.AREA_MUNICIPIO.style;

			municipioService.getMunicipioGeometryById($scope.caracterizacao.empreendimento.municipio.id)
			.then(function(res) {
				$scope.caracterizacao.limite = res.data.limite;
				getHistorico($scope.caracterizacao.id);
				$scope.controleAtualizacao = true;
				obterGeometriaDoEmpreendimento();
			})
			.catch(function() {
				mensagem.error("Não foi possível obter o limite do município.");
			});

			obterLimitesDoEstadoDoEmpreendimento();



		});

	};

	$scope.init($routeParams.idCaracterizacao);

	function tratarDadosQuestionario(questionario) {
		if(questionario){
			questionario.consumoAgua = questionario.consumoAgua ? "true" : "false";
			questionario.efluentes = questionario.efluentes ? "true" : "false";
			questionario.residuosSolidos = questionario.residuosSolidos ? "true" : "false";
		}
	}

	$scope.visualizarCaracterizacao = (idCaracterizacao) => {
		$location.path('/empreendimento/'  + $routeParams.idEmpreendimento + '/caracterizacoes/' + idCaracterizacao + '/visualizar');
	};

	$scope.voltar = () => {
		$window.history.back();
	};

	function getGeometriaEstado(codigo) {

		var deffered = $q.defer();

		estadoService.getEstadoGeometryByCodigo(codigo)
			.then(function(response) {
				$scope.caracterizacao.dadosEstadoEmpreendimento = response.data;
				deffered.resolve();
			})
			.catch(function() {
				mensagem.error("Não foi possível obter o limite do estado.");
				deffered.reject();
			});
		return deffered.promise;
	}

	function obterLimitesDoEstadoDoEmpreendimento () {
		$q.all(getGeometriaEstado($scope.caracterizacao.empreendimento.municipio.estado.sigla));
	}

	$scope.condicoesParaExibirVigencia = () => {
		if ($scope.caracterizacao.tipoLicenca){
			return $scope.caracterizacao.tipoLicenca.sigla && $scope.caracterizacao.tipoLicenca.sigla !== 'CA';
		}
		if ($scope.caracterizacao.tiposLicencaEmAndamento){
			return $scope.caracterizacao.tiposLicencaEmAndamento[0].sigla && $scope.caracterizacao.tiposLicencaEmAndamento[0].sigla !== 'CA';
		}
	};

};

exports.controllers.VisualizarCaracterizacaoController = VisualizarCaracterizacaoController;
