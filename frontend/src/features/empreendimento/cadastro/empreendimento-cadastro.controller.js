var CadastrarEmpreendimentos = function($scope, $routeParams, breadcrumb, $route, growlMessages, $location, modalSimplesService) {

	var cadastro = this;

	cadastro.proximo = proximo;
	cadastro.anterior = anterior;
	cadastro.escolherEtapa = escolherEtapa;
	cadastro.etapaEstaBloqueada = etapaEstaBloqueada;
	cadastro.initPessoa = initPessoa;
	cadastro.onInit = onInit;
	cadastro.cancelar = cancelar;
	cadastro.dataAtual = new Date();
	cadastro.botaoCancelar = botaoCancelar;
	cadastro.cpfCnpjPesquisado = false;

	cadastro.empreendimento = {
		empreendedor: {},
		proprietarios: [],
		responsaveis: [],
		representantes: []
	};

	cadastro.contatoPrincipalEmpreendedor = {
		email: null,
		telefone: null,
		celular: null
	};

	cadastro.enderecoEmpreendedor = {};
	
	cadastro.contatoPrincipalEmpreendimento = {
		email: null,
		telefone: null,
		celular: null
	};

	cadastro.enderecoEmpreendimento = {};

	/* A propriedade 'passoValido' é definida pelas respectivas controllers filhas */
	cadastro.etapas = {
		EMPREENDEDOR: {nome: 'Empreendedor', indice: 0, passoValido: undefined, class: 'empreendedor'},
		EMPREENDIMENTO: {nome: 'Empreendimento', indice: 1, passoValido: undefined, class: 'empreendimento'},
		PROPRIETARIO: {nome: 'Proprietários', indice: 2, passoValido: undefined, class: 'proprietarios'},
		REPRESENTANTES: {nome: 'Representantes legais', indice: 3, passoValido: undefined, class: 'representantes'},
		RESPONSAVEIS: {nome: 'Responsáveis técnicos e legais', indice: 4, passoValido: undefined, class: 'responsaveis'},
		RESUMO: {nome: 'Resumo', indice: 5, passoValido: undefined, class: 'resumo', isUltimo: true}
	};

	cadastro.listaEtapas = [
		cadastro.etapas.EMPREENDEDOR,
		cadastro.etapas.EMPREENDIMENTO,
		cadastro.etapas.PROPRIETARIO,
		cadastro.etapas.REPRESENTANTES,
		cadastro.etapas.RESPONSAVEIS,
		cadastro.etapas.RESUMO,
	];

	cadastro.origensEmpreendedor = {
		USUARIO_LOGADO: 'usuarioLogado',
		OUTRO_CPF_CNPJ: 'outroCpfCnpj'
	};

	cadastro.origensEmpreendimento = {
		EMPREENDEDOR: 'empreendedor',
		OUTRO_CPF_CNPJ: 'outroCpfCnpj'
	};

	function escolherEtapa(etapa) {

		growlMessages.destroyAllMessages();

		cadastro.etapa = etapa;

		breadcrumb.set([{title:'Empreendimento', href:'#/empreendimentos/listagem'},
						{title:'Cadastrar (Passo ' + (etapa.indice + 1) + ' de ' + cadastro.listaEtapas.length + ')', href:''}]);

	}

	/*  Se todas as etapas anteriores não estiverem válidas, bloqueia a etapa atual.
	 Talvez seja uma forma pesada de fazer, mas é a mais simples e garante que as
	 etapas seguintes sejam bloqueadas se o usuário invalidar qualquer etapa anterior. */
	function etapaEstaBloqueada(etapa) {

		for (let i = 0; i < etapa.indice; i++) {

			if(!cadastro.listaEtapas[i].passoValido()){
				return true;
			}
		}
	}

	function proximo() {
		escolherEtapa(cadastro.listaEtapas[cadastro.etapa.indice + 1]);
	}

	function anterior() {
		escolherEtapa(cadastro.listaEtapas[cadastro.etapa.indice - 1]);
	}

	function cancelar() {
		$location.path('empreendimentos');
	}

	function initPessoa() {

		const origem = $route.current.params.origem;

		// Cadastrar usuário logado como empreendedor
		if(origem === 'usuario') {

			cadastro.origemEmpreendedor = cadastro.origensEmpreendedor.USUARIO_LOGADO;

		}
		// Cadastrar outro CPF/CNPJ como empreendedor
		else if(origem === 'outro') {

			cadastro.origemEmpreendedor = cadastro.origensEmpreendedor.OUTRO_CPF_CNPJ;

		}
	}

	function botaoCancelar() {
		const configModal = {
			titulo: 'Confirmar cancelamento',
			conteudo: 'Você tem certeza que deseja cancelar o preenchimento do empreendimento?'
		};

		const instanciaModal = modalSimplesService.abrirModal(configModal);

        instanciaModal.result.then(function() {

            if (!!$routeParams.idCaracterizacao){

				$location.path('/empreendimento/' + $routeParams.id + '/caracterizacao/' + $routeParams.idCaracterizacao + '/notificacao');
				return;
            }

            $location.path('empreendimentos');
        });
	}

	function onInit() {

		initPessoa();
	}

	escolherEtapa(cadastro.listaEtapas[0]);

};

exports.controllers.CadastrarEmpreendimentos = CadastrarEmpreendimentos;
