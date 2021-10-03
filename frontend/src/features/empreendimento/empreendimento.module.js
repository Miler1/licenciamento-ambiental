var empreendimentos = angular.module("empreendimentos", ["ngRoute"]);

var utils = app.utils,
    controllers = app.controllers,
    directives = app.directives;

empreendimentos.config(["$routeProvider", function($routeProvider) {

	$routeProvider
		.when("/empreendimentos/cadastrar", {
			templateUrl: "features/empreendimento/cadastro/empreendimento-cadastro.html",
			controller: controllers.CadastrarEmpreendimentos,
			controllerAs: 'cadastro'
		})
		.when("/empreendimentos/editar/:id", {
			templateUrl: "features/empreendimento/edicao/empreendimento-edicao.html",
			controller: controllers.EditarEmpreendimentos,
			controllerAs: 'cadastro',
			resolve: {
				getEmpreendimento: getEmpreendimento
			}
		})

		.when("/empreendimentos/editar/:id/notificacao/:idCaracterizacao", {
			templateUrl: "features/empreendimento/edicao/empreendimento-edicao.html",
			controller: controllers.EditarEmpreendimentos,
			controllerAs: 'cadastro',
			resolve: {
				getEmpreendimento: getEmpreendimento
			}
		})
		.when("/empreendimentos/editar/:id/renovacao/:idCaracterizacao", {
			templateUrl: "features/empreendimento/edicao/empreendimento-edicao.html",
			controller: controllers.EditarEmpreendimentos,
			controllerAs: 'cadastro',
			resolve: {
				getEmpreendimento: getEmpreendimento
			}
		})
		.otherwise({
			redirectTo: "/"
		});

}]);

empreendimentos
	.controller('etapaEmpreendedorController', controllers.EtapaEmpreendedorController)
	.controller('dadosEmpreendimentoController', controllers.DadosEmpreendimentoController)
	.controller('etapaProprietarioController', controllers.EtapaProprietarioController)
	.controller('etapaRepresentantesController', controllers.EtapaRepresentantesController)
	.controller('etapaEmpreendimentoController', controllers.EtapaEmpreendimentoController)
	.controller('etapaResponsaveisController', controllers.EtapaResponsaveisController)
	.controller('etapaResumoController', controllers.EtapaResumoController)
	.controller('localizacaoEmpreendimentoController', controllers.LocalizacaoEmpreendimentoController)
	.controller('etapaResumoControllerEdicao', controllers.EtapaResumoControllerEdicao)
	.controller('localizacaoEmpreendimentoControllerEdicao', controllers.LocalizacaoEmpreendimentoControllerEdicao)
	.controller('etapaResponsaveisControllerEdicao', controllers.EtapaResponsaveisControllerEdicao)
	.controller('dadosEmpreendimentoControllerEdicao', controllers.DadosEmpreendimentoControllerEdicao);

empreendimentos
	.component('selecaoUfMunicipio', directives.SelecaoUfMunicipio)
	.component('buscarPessoa', directives.BuscarPessoa)
	.component('buscarEmpreendimento', directives.BuscarEmpreendimento)
	.component('estadoCivil', directives.EstadoCivil)
	.component('esferaAtuacao', directives.EsferaAtuacao)
	.component('orgaoClasse', directives.OrgaoClasse)
	.component('endereco', directives.Endereco)
	.component('tipoResponsavelEmpreendimento', directives.TipoResponsavelEmpreendimento)
	.component('mapa', directives.Mapa)
	.component('resumoEmpreendimento', directives.ResumoEmpreendimento)
	.component('adicionarCoordenadas', directives.AdicionarCoordenadas);

function getEmpreendimento(empreendimentoService, $route, mensagem) {

	return $route.current.params.id ? empreendimentoService.getEmpreendimentoToUpdate($route.current.params.id) : null;

}
