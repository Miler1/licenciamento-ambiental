var CaracterizacaoDLACondicoesController = function($scope, $location, $routeParams, mensagem, caracterizacaoService, modalSimplesService, tiposTituloOutorga, $q) {

	var etapaCondicoes = this;

	etapaCondicoes.passoValido = passoValido;
	etapaCondicoes.proximo = proximo;
	etapaCondicoes.perguntas = [];
	etapaCondicoes.respostas = {};

	$scope.cadastro.etapas.CONDICOES.passoValido = undefined;
	$scope.cadastro.etapas.CONDICOES.beforeEscolherEtapa = beforeEscolherEtapa;

	$scope.$on('atividadeAlterada', function(event, args) {
		etapaCondicoes.perguntas = [];
		etapaCondicoes.respostas = {};
	});	

	$scope.$watch('cadastro.etapa', function(etapa){

		if(etapa && etapa.class === $scope.cadastro.etapas.CONDICOES.class){

			carregarPerguntasDLA();
			$scope.cadastro.caracterizacao.perguntasCarregadas = true;

		}

	});

	// Before escolher etapa, injeta ao trocar de etapa, as respostas escolhidas 
	// no processo de cadastro de uma DI
	function beforeEscolherEtapa() {
		$scope.cadastro.respostas = etapaCondicoes.respostas;

		return passoValido();
	}

	function carregarPerguntasDLA() {

		if(!_.isEmpty(etapaCondicoes.respostas))
			return;

		$scope.cadastro.caracterizacao.atividadesCaracterizacao.forEach(function(ac) {

			if(ac.atividade){

				etapaCondicoes.perguntas = [];
				etapaCondicoes.respostas = {};
				$scope.cadastro.caracterizacao.tipoTituloOutorga = null;
				$scope.cadastro.caracterizacao.numeroTituloOutorga = null;

				caracterizacaoService.perguntasDLA(ac.atividade.id).then(function(response){

					etapaCondicoes.perguntas = response.data;

				});

			}
		});

	}

	$scope.cadastro.etapas.CONDICOES.passoValido = passoValido;

	function passoValido() {

		var passoValido = true;

		if(!$scope.cadastro.caracterizacao.perguntasCarregadas) {
			return false;
		}

		_(etapaCondicoes.perguntas).each(function (pergunta) {

			if (passoValido) {
				
				if(etapaCondicoes.respostas[pergunta.id]){

					passoValido = etapaCondicoes.respostas[pergunta.id].permiteLicenciamento;

					//verifica se é a pergunta de supressao
				} else {

					//invalido sempre que nao existir pergunta
					passoValido = false;

				}

			}

		});

		return passoValido;

	}

	function proximo() {

		if(etapaCondicoes.passoValido()){
			//$scope.cadastro.respostas = etapaCondicoes.respostas;
			$scope.cadastro.proximo();
			return;
		}

		if (!todasPerguntasRespondidas()){

			mensagem.warning('Todas as perguntas devem ser respondidas.');
			$scope.formInformacoesEnquadramento.$setSubmitted();
			return;
		}

		if(!todasRespostasPermitemDLA()){

			abrirModalNaoEnquadramento();
		}
	}

	function todasPerguntasRespondidas() {

		var todasPerguntasRespondidas = true;

		_(etapaCondicoes.perguntas).each(function (pergunta) {
			
			if (!etapaCondicoes.respostas[pergunta.id]) {

				   todasPerguntasRespondidas = false;
				   return false;
			   }				
		});

		return todasPerguntasRespondidas;
	}

	var configModal = {
		titulo: 'Solicitação de DDLA',
		conteudo: 'As respostas para as questões não permitem a emissão da DDLA (Declaração de Dispensa de Licenciamento Ambiental), conforme a Lei nº3.009, de 17 de novembro de 1998. Por favor, procure a SEMA para realizar sua solicitação.',
		labelBotaoConfirmar: 'Cancelar solicitação de DDLA',
		labelBotaoCancelar: 'Fechar'
	};

	var abrirModalNaoEnquadramento = function(){

		var instanciaModal = modalSimplesService.abrirModal(configModal);

		instanciaModal.result.then(function() {
			$location.path('/empreendimento/' + $routeParams.idEmpreendimento + '/caracterizacoes');
		});

	};

	var todasRespostasPermitemDLA = function() {

		var permiteLicenciamento = true;

		_.each(etapaCondicoes.perguntas, function(pergunta){

			if(permiteLicenciamento){

				permiteLicenciamento = etapaCondicoes.respostas[pergunta.id].permiteLicenciamento;
			}

		});

		return permiteLicenciamento;

	};

};

exports.controllers.CaracterizacaoDLACondicoesController = CaracterizacaoDLACondicoesController;
