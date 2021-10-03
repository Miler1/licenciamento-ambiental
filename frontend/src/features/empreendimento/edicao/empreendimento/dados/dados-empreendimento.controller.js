var DadosEmpreendimentoControllerEdicao = function($scope, mensagem, empreendimentoService, $anchorScroll, $q, $injector) {

	// Criando extensão da controller DadosEmpreendimentoController
	$injector.invoke(exports.controllers.DadosEmpreendimentoController, this,
		{
			$scope: $scope,
			mensagem: mensagem,
			empreendimentoService: empreendimentoService,
			$anchorScroll: $anchorScroll,
			$q: $q
		}
	);

	var dadosEmpreendimento = this;

	dadosEmpreendimento.proximo = proximo;

	function proximo() {

		$scope.formDadosEmpreendimento.$setSubmitted();

		if (dadosEmpreendimento.abaValida()) {

			if ($scope.cadastro.empreendimento.possuiCaracterizacoes) {

				$scope.cadastro.proximo();
			} else {

				$scope.cadastro.etapas.EMPREENDIMENTO.tabIndex++;
			}
		} else {

			mensagem.warning('Verifique os campos destacados em vermelho para prosseguir com o cadastro.');
			$anchorScroll();
		}
	}
};

exports.controllers.DadosEmpreendimentoControllerEdicao = DadosEmpreendimentoControllerEdicao;