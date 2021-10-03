var CaracterizacaoDLAEnquadramentoController = function($scope, $rootScope, mensagem, config, caracterizacaoService, modalSimplesService, $location, $q) {

	var etapaEnquadramento = this;

	etapaEnquadramento.passoValido = passoValido;
	etapaEnquadramento.proximo = proximo;

	$scope.urlResolucao = config.RESOLUCAO_107_URL;
	$scope.urlResolucaoAnexo1 = config.ANEXO_1_RESOLUCAO_107_URL;
	$scope.urlResolucaoAnexo2 = config.ANEXO_2_RESOLUCAO_107_URL;

	$scope.concordaComResolucao = false;

	$scope.cadastro.etapas.ENQUADRAMENTO.passoValido = passoValido;
	$scope.cadastro.etapas.ENQUADRAMENTO.beforeEscolherEtapa = undefined;

	function passoValido() {
		return $scope.concordaComResolucao;
	}

	function proximo() {

		abrirModalCadastrar();
	}

	// $scope.dadosEnquadramento = {
	// 	informacoesAdicionais: ""
	// };

	var configModal = {
		titulo: 'Confirmação de solicitação de DDLA',
		conteudo: 'Deseja realmente confirmar a solicitação da DDLA - Declaração de Dispensa de Licenciamento Ambiental?',
		labelBotaoConfirmar: 'Confirmar solicitação de DDLA',
		labelBotaoCancelar: 'Cancelar'
	};

	function abrirModalCadastrar(){

		if(!etapaEnquadramento.passoValido()) {
			mensagem.warning('É necessário concordar com as disposições finais para prosseguir com o cadastro.');
			return;
		}

		if(!$scope.empreendimento.isento) {
			configModal.conteudo = 'Deseja realmente confirmar a solicitação da DDLA - Declaração de Dispensa de Licenciamento Ambiental? A DDLA será vinculada ao empreendimento e ficará disponível para download após a confirmação.';
		}

		var instanciaModal = modalSimplesService.abrirModal(configModal);

		instanciaModal.result.then(function() {
			cadastrar();
		});

	}

	function preparaObjetoAtividadeCaracterizacao(atividadeCaracterizacao) {

		delete atividadeCaracterizacao.atividade.atividadesCnae;
		if (atividadeCaracterizacao.atividadeCnae != undefined){
			delete atividadeCaracterizacao.atividadeCnae.atividades;
		}

		return atividadeCaracterizacao;
	}

	function cadastrar() {

		$q.all([verificarSobreposicoesNaoPermitidasDI($scope.cadastro.caracterizacao.atividadesCaracterizacao)])
			.then(function(data) {

				var sobreposicaoNaoPermitida = data[0];
				if(!sobreposicaoNaoPermitida) {

					var respostas = montaObjRespostas();

					$scope.cadastro.caracterizacao.tipoLicenca = app.TIPOS_LICENCA_DISPENSA.DDLA;
					$scope.cadastro.caracterizacao.respostas = respostas;
					// $scope.cadastro.caracterizacao.dispensa.informacaoAdicional = $scope.dadosEnquadramento.informacoesAdicionais;

					$scope.cadastro.caracterizacao.atividadesCaracterizacao.forEach(function(ac) {

						var atividadeCaracterizacao = preparaObjetoAtividadeCaracterizacao(ac);

						let coordenadaGeoPoint = {type: 'Geometry' , geometries: []};

						if(atividadeCaracterizacao.geometriasAtividade.features) {

							var geometryCollection = {type: "GeometryCollection", geometries: []};

							for(var j=0; j< atividadeCaracterizacao.geometriasAtividade.features.length; j++) {

								var feature = atividadeCaracterizacao.geometriasAtividade.features[j];

								geometryCollection.geometries.push(feature.geometry);

							}

							ac.geometriasAtividade = [{geometria: geometryCollection}];

						}

						coordenadaGeoPoint = atividadeCaracterizacao.geometriasAtividade[0].geometria.geometries[0];
						if(coordenadaGeoPoint.type === 'Point'){
							ac.geometriasAtividade = [];
							ac.geometriasAtividade.push(coordenadaGeoPoint);
							ac.geometriasAtividade = [{geometria: coordenadaGeoPoint}];
						}

					});

					caracterizacaoService.save($scope.cadastro.caracterizacao)
						.then(function(response){

							mensagem.success(response.data.texto);

							$location.path('/empreendimento/'+ $scope.cadastro.empreendimento.id +'/caracterizacoes');

						},function(response){

							mensagem.error(response.data.texto);

						});
				}
				else {
					mensagem.warning('A localização da atividade está em sobreposição com áreas restritas.');
				}

			});

	}

	function verificarSobreposicoesNaoPermitidasDI(atividadesCaracterizacao) {

		var geometrias = [];
		atividadesCaracterizacao.forEach(function(ac) {
			var features = ac.geometriasAtividade.features;
			if(features) {
				features.forEach(function(f) {
					geometrias.push(JSON.stringify(f.geometry));
				});
			}
		});

		var deffered = $q.defer();
		caracterizacaoService.sobreposicoesNaoPermitidasDI(geometrias)
			.then(function(response) {
				deffered.resolve(response.data);
			})
			.catch(function() {
				deffered.reject('"Não foi possível obter as sobreposições."');
			});

		return deffered.promise;
	}

	function montaObjRespostas() {

		var idRespostas = [];

		_.each($scope.cadastro.respostas, function(resposta){

			idRespostas.push({
				id: resposta.id
			});
		});

		return idRespostas;

	}

};

exports.controllers.CaracterizacaoDLAEnquadramentoController = CaracterizacaoDLAEnquadramentoController;
