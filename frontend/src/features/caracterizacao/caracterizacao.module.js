var caracterizacoes = angular.module("caracterizacoes", ["ngRoute"]);

var utils = app.utils,
	controllers = app.controllers,
	directives = app.directives;

caracterizacoes.config(["$routeProvider", function($routeProvider) {

	$routeProvider
		.when("/empreendimento/:idEmpreendimento/caracterizacoes/cadastrar", {
			templateUrl: "features/caracterizacao/cadastro/caracterizacao-cadastro.html",
			controller: controllers.CadastrarCaracterizacoes,
			controllerAs: 'cadastro'
		})
		.when("/empreendimento/:idEmpreendimento/caracterizacoes/:idCaracterizacao/visualizar", {
			templateUrl: "features/caracterizacao/visualizacao/visualizar-caracterizacao.html",
			controller: controllers.VisualizarCaracterizacaoController,
			controllerAs: 'cadastro'
		})
		.when("/empreendimento/:idEmpreendimento/caracterizacao/:idCaracterizacao/edit", {
			templateUrl: "features/caracterizacao/cadastro/caracterizacao-cadastro.html",
			controller: controllers.CadastrarCaracterizacoes,
			controllerAs: 'cadastro'
		})
		.when("/empreendimento/:idEmpreendimento/caracterizacao/:idCaracterizacao/renovar/:acao", {
			templateUrl: "features/caracterizacao/cadastro/caracterizacao-cadastro.html",
			controller: controllers.CadastrarCaracterizacoes,
			controllerAs: 'cadastro'
		})
		.when("/empreendimento/:idEmpreendimento/caracterizacao/:idCaracterizacao/retificar", {
			templateUrl: "features/caracterizacao/cadastro/caracterizacao-cadastro.html",
			controller: controllers.CadastrarCaracterizacoes,
			controllerAs: 'cadastro'
		})
		.when("/empreendimento/:idEmpreendimento/caracterizacao/:idCaracterizacao/retificar/:empreendimentoalteradovianotificacao", {
			templateUrl: "features/caracterizacao/cadastro/caracterizacao-cadastro.html",
			controller: controllers.CadastrarCaracterizacoes,
			controllerAs: 'cadastro'
		})
		.when("/empreendimento/:idEmpreendimento/caracterizacao/:idCaracterizacao/visualizar", {
			templateUrl: "features/caracterizacao/cadastro/caracterizacao-cadastro.html",
			controller: controllers.CadastrarCaracterizacoes,
			controllerAs: 'cadastro'
		})
		.when("/empreendimento/:id/caracterizacoes/finalizado", {
			templateUrl: "features/caracterizacao/cadastro/simplificado/finalizacao/finalizacao-cadastro.html",
			controller: 'caracterizacaoLASFinalizacaoController',
			controllerAs: 'finalizacao'
		})
		.when("/empreendimento/:idEmpreendimento/caracterizacao/:idCaracterizacao/notificacao", {
			templateUrl: "features/caracterizacao/notificacao/notificacao.html",
			controller: controllers.NotificacaoController,
			controllerAs: 'cadastro'
		})
		.when("/empreendimento/:idEmpreendimento/caracterizacao/:idCaracterizacao/notificacao/:empreendimentoalteradovianotificacao", {
			templateUrl: "features/caracterizacao/notificacao/notificacao.html",
			controller: controllers.NotificacaoController,
			controllerAs: 'cadastro'
		})
		.otherwise({
			redirectTo: "/"
		});

}]);

caracterizacoes.filter('existeAtividadeCaracterizacao', function() {
	return function(input) {
		var out = [];

		if(!input)
			return out;

		for (var i = 0; i < input.length; i++) {
			if(input[i].atividade){
				out.push(input[i]);
			}
		}
		return out;
	};
});

caracterizacoes.filter('atividadesSelecionadas', function() {
	return function(input, listaAtividades) {

		if(!input)
			return [];

		var atividades = [];
		listaAtividades.forEach(function(a) {
			atividades.push(a.atividade);
		});

		return input.filter(function(e) {return atividades.indexOf(e) < 0;});
	};
});

caracterizacoes.filter('atividadesMesmaGerencia', function() {
	return function(input, listaAtividades) {

		if(!input)
			return [];

		var gerencias = [];
		listaAtividades.forEach(function(a) {
			if(gerencias.indexOf(a.atividade.siglaSetor) < 0) {
				gerencias.push(a.atividade.siglaSetor);
			}
		});

		if(gerencias.length === 0) {
			return input;
		}

		return input.filter(function(e) {return gerencias.indexOf(e.siglaSetor) >= 0;});
	};
});

caracterizacoes.filter('atividadesMesmoTipoLicenca', function() {
	return function(input, listaAtividades) {

		if(!input)
			return [];

		var tiposLicenca = [];
		listaAtividades.forEach(function(a) {
			if(a.atividade && a.atividade.tiposLicenca) {
				a.atividade.tiposLicenca.forEach(function(tl) {
					if(tiposLicenca.indexOf(tl.id) < 0) {
						tiposLicenca.push(tl.id);
					}
				});
			}
		});

		if(tiposLicenca.length === 0) {
			return input;
		}

		return input.filter(function(e) {
			var tiposLicencaIdentico = true;
			if(e.tiposLicenca.length !== tiposLicenca.length) {
				tiposLicencaIdentico = false;
			}
			e.tiposLicenca.forEach(function(tl) {
				if(tiposLicenca.indexOf(tl.id) < 0) {
					tiposLicencaIdentico = false;
				}
			});
			return tiposLicencaIdentico;
		});
	};
});

caracterizacoes.filter('atividadesDentroEmpreendimento', function() {
	return function(input, dentroEmpreendimento) {

		if(!input)
			return [];

		if(typeof dentroEmpreendimento === "undefined"){
			return input;
		} else {
			return input.filter(function (e) {
				return e.dentroEmpreendimento === dentroEmpreendimento;
			});
		}
	};
});

caracterizacoes.filter('atividadesDoMesmoGrupoDeDocumentos', function() {
	return function(input, grupo) {

		if(!input)
			return [];

		if(typeof grupo === "undefined"){
			return input;
		}

		if (grupo) {
			return input.filter((e) => e.grupoDocumento && e.grupoDocumento.id === grupo.id);
		}

		return input.filter((e) => !e.grupoDocumento);
	};
});

caracterizacoes

	.controller('modalNotificacoesController', controllers.ModalNotificacoesController)
	.controller('modalListagemNotificacoesController', controllers.ModalListagemNotificacoesController)
	.controller('VisualizarCaracterizacaoController', controllers.VisualizarCaracterizacaoController)

	// Dispensa
	.controller('caracterizacaoDLAAtividadeController', controllers.CaracterizacaoDLAAtividadeController)
	.controller('caracterizacaoDLAGeoController', controllers.CaracterizacaoDLAGeoController)
	.controller('caracterizacaoDLACondicoesController', controllers.CaracterizacaoDLACondicoesController)
	.controller('caracterizacaoDLAEnquadramentoController', controllers.CaracterizacaoDLAEnquadramentoController)
	.controller('modalCaracterizacaoAtividadeController', controllers.ModalCaracterizacaoAtividadeController)
	.controller('ModalDICancelamentoController', controllers.ModalDICancelamentoController)

	// Simplificado
	.controller('caracterizacaoLSAAtividadeController', controllers.CaracterizacaoLSAAtividadeController)
	.controller('caracterizacaoLSAGeoController', controllers.CaracterizacaoLSAGeoController)
	.controller('caracterizacaoLSACondicoesController', controllers.CaracterizacaoLSACondicoesController)
	.controller('caracterizacaoLSAEnquadramentoController', controllers.CaracterizacaoLSAEnquadramentoController)
	.controller('caracterizacaoLASDocumentacaoController', controllers.CaracterizacaoLASDocumentacaoController)
	.controller('caracterizacaoLASFinalizacaoController', controllers.CaracterizacaoLASFinalizacaoController)
	.controller('modalConfirmacaoLicencaController', controllers.ModalConfirmacaoLicencaController)
	.controller('modalDadosDaeController', controllers.ModalDadosDaeController)
	.controller('modalDadosDaeLicenciamentoController', controllers.ModalDadosDaeLicenciamentoController)
	.controller('modalSelecaoAtividadeCnaeController', controllers.ModalSelecaoAtividadeCnaeController)
	.controller('modalRemoverLicencaController', controllers.ModalRemoverLicencaController);


caracterizacoes
	.component('dadosEmpreendimento', directives.DadosEmpreendimento)
	.component('mapaCaracterizacao', directives.MapaCaracterizacao);
