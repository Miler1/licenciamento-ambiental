var licenciamento = angular.module("licenciamento", [
	"ngRoute",
	"ngSanitize",
	"ui.bootstrap",
	"empreendimentos",
	"caracterizacoes",
	"angular-growl",
	"ngAnimate",
	"ngMessages",
	"idf.br-filters",
	"ui.utils.masks",
	"angularMoment",
	"ngFileUpload",
	"ngCpfCnpj",
	'ui.select',
	"ngSanitize"
]);

/***
 *
 * Gente, essa é uma diretiva customizada, para gerar uma ação
 * ao pressionar qualquer um das teclas Enter do teclado
 *
 *  ***/
licenciamento.directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if (event.which === 13) {
                scope.$apply(function () {
                    scope.$eval(attrs.ngEnter);
                });

                event.preventDefault();
            }
        });
    };
});

/***
 *
 * Gente, essa é uma diretiva customizada, para gerar uma ação
 * ao pressionar a tecla Esc do teclado
 *
 *  ***/
licenciamento.directive('ngEsc', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if (event.which === 27) {
                scope.$apply(function () {
                    scope.$eval(attrs.ngEsc);
                });

                event.preventDefault();
            }
        });
    };
});

licenciamento.config(["$routeProvider",	function($routeProvider) {
	$routeProvider
		.when("/login", {
			templateUrl: 'features/login/login.html',
			controller: 'loginController',
			controllerAs: "login"
		})
		.when("/empreendimentos/listagem", {
			templateUrl: "features/empreendimento/listagem/empreendimento-listagem.html",
			controller: "listagemEmpreendimentosController",
			controllerAs: "listagem"
		})
		.when("/empreendimento/:idEmpreendimento/caracterizacoes", {
			templateUrl: "features/caracterizacao/listagem/caracterizacao-listagem.html",
			controller: "listagemCaracterizacoesController",
			controllerAs: "listagemCaracterizacoes"
		})
		.otherwise({
			redirectTo: "/login"
		});

}]).config(['growlProvider', function(growlProvider) {

	growlProvider.globalDisableCountDown(false)
				.globalTimeToLive(5000);

}]).config(['$locationProvider', function($locationProvider) {

	$locationProvider.html5Mode(false).hashPrefix('');

}]).run(function(amMoment, $rootScope, growlMessages, loginService, $location) {

	amMoment.changeLocale('pt-br');

	$rootScope.usuarioLogado = false;

	$rootScope.$on('$routeChangeSuccess', function(){
		growlMessages.destroyAllMessages();
	});

	loginService.getUsuarioSessao().then(function(response) {
		if(response.data){
			$rootScope.usuarioSessao = response.data;
			$rootScope.usuarioLogado = true;
		} else {
			$rootScope.usuarioLogado = false;
		}
	}).catch(function(){
		$location.path('/login');
		$rootScope.usuarioLogado = false;
	});

	$rootScope.verificaRotaLogin = function() {
		if($rootScope.usuarioLogado && $location.$$path === "/login"){
			$location.path('/empreendimentos/listagem');
		}
	};

	$rootScope.verificaBreadCrumbHome = function() {
		return !($rootScope.usuarioLogado && $location.$$path === "/empreendimentos/listagem");
	};

});

licenciamento.controller("AppController", ["$scope", "$rootScope", "applicationService", "$location", "breadcrumb", "mensagem", "$timeout", "$window",
	function($scope, $rootScope, applicationService, $location, breadcrumb, mensagem, $timeout, $window) {

	$rootScope.location = $location;
	$rootScope.confirmacao = {};
	$rootScope.mensagens = app.utils.Mensagens;

	$rootScope.usuarioSessao = LICENCIAMENTO_CONFIG.usuarioSessao;
	$rootScope.config = LICENCIAMENTO_CONFIG.configuracoes;

	if(!$rootScope.usuarioSessao){
		$location.path('/login');
	}


	/*  Limpando o breadcrumb ao acessar a tela inicial */
	$scope.$on('$routeChangeSuccess', function(event, rotaAtual, rotaAnterior){

		if($rootScope.usuarioLogado && rotaAtual.$$route.originalPath === $rootScope.config.baseURL){

			breadcrumb.set([]);

		}

		// if($rootScope.usuarioLogado && rotaAtual.$$route.originalPath ==! $rootScope.config.baseUrl + "#/login"){
		// 	debugger;
		// 	$location.path('/empreendimento/listagem');
		// }

	});

	$scope.$on("$routeChangeError", function (event, rotaAtual, rotaAnterior, error) {

		if (error.data.texto) {

			mensagem.error(error.data.texto);
		}

		if (rotaAnterior) {

            $timeout(function() {

            	$window.history.back();
            }, 0);
        }
        else {

            $location.path($rootScope.config.baseURL);
        }
	});

}]);

licenciamento.constant('config', (function(){

	var getBaseUrl = function() {

		if(LICENCIAMENTO_CONFIG.configuracoes.baseURL === "/")
			return LICENCIAMENTO_CONFIG.configuracoes.baseURL;
		else
			return LICENCIAMENTO_CONFIG.configuracoes.baseURL + "/";
	};

	return {
		BASE_URL: getBaseUrl,
		LDI_COMO_SAIR_LISTA_URL: "http://monitoramento.semas.pa.gov.br/ldi/#como-sair-lista",
		QTDE_ITENS_POR_PAGINA: 10,
		RESOLUCAO_107_URL: "https://www.semas.pa.gov.br/2016/07/18/resolucao-coema-n-o-107-de-8-de-marco-de-2013/",
		ANEXO_1_RESOLUCAO_107_URL: "https://www.semas.pa.gov.br/wp-content/uploads/2016/07/ANEXO-I.pdf",
		ANEXO_2_RESOLUCAO_107_URL: "https://www.semas.pa.gov.br/wp-content/uploads/2016/07/anexo_II.pdf",
		MODELO_URL: "modelo/"
	};

})())
.constant('tiposTituloOutorga', {

	OUTORGA: 112,
	OUTORGA_PREVIA: 113,
	DISPENSA: 114
})
.constant('ID_TIPOLOGIA_AGROSSILVIPASTORIL', 1);


var services = app.services,
	utils = app.utils,
	filters = app.filters,
	controllers = app.controllers,
	directives = app.directives;


utils.services(licenciamento)
	.add('applicationService', services.ApplicationService)
	.add('mensagem', services.Mensagem)
	.add('request', services.Request)
	.add('animacaoLoader', services.AnimacaoLoader)
	.add('breadcrumb', services.BreadcrumbService)
	.add('empreendimentoService', services.EmpreendimentoService)
	.add('modalSimplesService', services.ModalSimples)
	.add('enderecoService', services.EnderecoService)
	.add('pessoaService', services.PessoaService)
	.add('empreendedorService', services.EmpreendedorService)
	.add('imovelService', services.ImovelService)
	.add('estadoCivilService', services.EstadoCivilService)
	.add('estadoService', services.EstadoService)
	.add('esferaAtuacaoService', services.EsferaAtuacaoService)
	.add('orgaoClasseService', services.OrgaoClasseService)
	.add('tipoResponsavelEmpreendimentoService', services.TipoResponsavelEmpreendimentoService)
	.add('municipioService', services.MunicipioService)
	.add('uploadService', services.UploadService)
	.add('caracterizacaoService', services.CaracterizacaoService)
	.add('solicitacaoDocumentoCaracterizacaoService', services.SolicitacaoDocumentoCaracterizacao)
	.add('solicitacaoGrupoDocumentoService', services.SolicitacaoGrupoDocumento)
	.add('restricoesLicenciamentoService', services.RestricoesLicenciamentoSimplificadoService)
	.add('coordenadasService', services.CoordenadasService)
	.add('notificacaoService', services.NotificacaoService)
	.add('notificacoesService', services.NotificacoesService)
	.add('municipiosLiberadosService', services.MunicipiosLiberadosService)
	.add('loginService', services.LoginService)
	.add('tiposLicencaService', services.TiposLicencaService)
	.add('documentoService', services.DocumentoService);

utils.filters(licenciamento)
	.add('textoTruncado', filters.TextoTruncado)
	.add('capitalize', filters.Capitalize);

utils.directives(licenciamento)
	.add('enter', directives.Enter, {link: directives.Enter.link, require: 'ngModel'})
	.add('mascara', directives.Mascara, {link: directives.Mascara.link, require: 'ngModel'});

licenciamento
	.controller('listagemCaracterizacoesController', controllers.ListagemCaracterizacoes)
	.controller('renovarCaracterizacaoController', controllers.RenovarCaracterizacao)
	.controller('listagemEmpreendimentosController', controllers.ListagemEmpreendimentos)
	.controller('breadcrumbController', controllers.BreadcrumbController)
	.controller('modalSimplesController', controllers.ModalSimplesController)
	.controller('loginController', controllers.Login);
