var EtapaRepresentantesController = function($scope, $rootScope, empreendedorService, pessoaService, mensagem, empreendimentoService, modalSimplesService) {

	var etapaRepresentantes = this;

	var dadosUsuarioLogado;
	var indexRepresentanteASerAtualizado;
	var indexProprietarioSerDesvinculado;

	etapaRepresentantes.passoValido = passoValido;
	etapaRepresentantes.proximo = proximo;
	etapaRepresentantes.vincularRepresentante = vincularRepresentante;
	etapaRepresentantes.desvincularRepresentante = desvincularRepresentante;
	etapaRepresentantes.buscarRepresentantesLegais = buscarRepresentantesLegais;
	etapaRepresentantes.findDadosUsuarioLogado = findDadosUsuarioLogado;
	etapaRepresentantes.iniciarAtualizacaoRepresentante = iniciarAtualizacaoRepresentante;
	etapaRepresentantes.atualizarRepresentante = atualizarRepresentante;
	etapaRepresentantes.confirmarDesvincularRepresentante = confirmarDesvincularRepresentante;
	etapaRepresentantes.habilitaVincular = podeVincular();
	etapaRepresentantes.copiarEnderecoRepresentante = copiarEnderecoRepresentante;
	etapaRepresentantes.enderecoCorrespondenciaCopiado = false;
	etapaRepresentantes.cpfBuscado = cpfBuscado;
	etapaRepresentantes.cancelarAlteracao = cancelarAlteracao;
	etapaRepresentantes.representanteAtual = {};

	etapaRepresentantes.localizacoesEmpreendimento = app.LOCALIZACOES_EMPREENDIMENTO;
	etapaRepresentantes.tipoPessoa = app.TIPO_PESSOA;
	etapaRepresentantes.tipoEndereco = app.TIPO_ENDERECO;
	etapaRepresentantes.tipoContato = app.TIPO_CONTATO;
	etapaRepresentantes.tiposRepresentanteLegal = app.TIPOS_REPRESENTANTE_LEGAL;

	etapaRepresentantes.enderecoRepresentanteLegal = {
		principal: {},
		correspondencia: {}
	};

	etapaRepresentantes.contatoPrincipalRepresentanteLegal = {
		email: null,
		telefone:null,
		celular: null
	};

	if(!$scope.cadastro.etapas.REPRESENTANTES.passoValido){
		$scope.cadastro.etapas.REPRESENTANTES.passoValido = passoValido;
	}

	function passoValido() {
		
		return true;
	}

	function proximo() {
		if(etapaRepresentantes.passoValido()){
			$scope.cadastro.proximo();
		}else{
			mensagem.warning("É necessário que, ao menos, um representante legal seja vinculado.");
		}
	}

	function podeVincular() {

		if(etapaRepresentantes.representanteAtual && findIndexRepresentante(etapaRepresentantes.representanteAtual) === -1) {

			etapaRepresentantes.vincularTitle = "Vincular novo representante legal.";
			etapaRepresentantes.habilitaVincular =  true;
		} else if(etapaRepresentantes.representanteAtual && findIndexRepresentante(etapaRepresentantes.representanteAtual) !== -1) {

			etapaRepresentantes.vincularTitle = "Este representante legal já está vinculado a este empreendedor.";
			etapaRepresentantes.habilitaVincular =  false;
		} else {

			etapaRepresentantes.vincularTitle = "É preciso buscar uma pessoa para vinculá-la como representante legal.";
			etapaRepresentantes.habilitaVincular = false;
		}

	}

	function vincularRepresentante() {

		if($scope.formDadosRepresentanteLegal.$valid){

			var representante = {
				dataVinculacao: moment().format('DD/MM/YYYY'),
				pessoa: etapaRepresentantes.representanteAtual.pessoa,
				podeSerDesvinculado: true,
				tipo: etapaRepresentantes.representanteAtual.pessoa.tipo || app.TIPOS_REPRESENTANTE_LEGAL.REPRESENTANTE,
				enderecos: etapaRepresentantes.representanteAtual.pessoa.enderecos
			};

			representante.pessoa.tipo= {
				codigo: representante.pessoa.cpf != null ? etapaRepresentantes.tipoPessoa.PESSOA_FISICA : etapaRepresentantes.tipoPessoa.PESSOA_JURIDICA
			};

			if(!$scope.cadastro.empreendimento.representantesLegais){

				$scope.cadastro.empreendimento.representantesLegais = [];
			
			}

			$scope.cadastro.empreendimento.representantesLegais.push(angular.copy(representante));

			etapaRepresentantes.contatoPrincipalRepresentanteLegal.email = null;
			etapaRepresentantes.contatoPrincipalRepresentanteLegal.telefone =null;
			etapaRepresentantes.contatoPrincipalRepresentanteLegal.celular = null;
				
			etapaRepresentantes.enderecoRepresentanteLegal.principal = {};
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia = {};

			etapaRepresentantes.representanteAtual = null;
			$scope.$broadcast('cleanCpfCnpj');

			podeVincular();

		}else{

			$scope.formDadosRepresentanteLegal.$setSubmitted();
			mensagem.warning('Verifique os campos destacados em vermelho para prosseguir com o cadastro.');

		}
	}

	function findIndexRepresentante(representante) {

		if(!representante.pessoa){
			representante = {
				pessoa: representante
			};
		}

		var index = _.findIndex($scope.cadastro.empreendimento.representantesLegais,
			function(element) {
				return element.pessoa.cpf === representante.pessoa.cpf;
			}
		);

		return index;

	}

	function findProprietario(proprietarioCPF) {
		return _.find($scope.cadastro.empreendimento.proprietarios, function(proprietario) {
			return proprietarioCPF === proprietario.pessoa.cpf;
		});	

	}

	function desvincularRepresentante(indexRepresentante) {

		indexProprietarioSerDesvinculado = indexRepresentante;

		var configModal = {
			titulo: 'Confirmar desvinculação de representante',
			conteudo: 'Tem certeza que deseja desvincular esse representante?'
		};

		var instanciaModal = modalSimplesService.abrirModal(configModal);

		instanciaModal.result
			.then(etapaRepresentantes.confirmarDesvincularRepresentante, function removerRepresentante(){});

	}

	function confirmarDesvincularRepresentante() {

		$scope.cadastro.empreendimento.representantesLegais.splice(indexProprietarioSerDesvinculado, 1);
		indexProprietarioSerDesvinculado = null;

	}

	function findDadosUsuarioLogado() {

		pessoaService.byCpf($rootScope.usuarioSessao.login).then(

			function(response) {

				dadosUsuarioLogado = {
					dataVinculacao: moment().format('DD/MM/YYYY'),
					pessoa: response.data,
					podeSerDesvinculado: false,
					tipo: response.data.tipo || app.TIPOS_REPRESENTANTE_LEGAL.REPRESENTANTE
				};

				etapaRepresentantes.buscarRepresentantesLegais();

			}
		);
	}

	function buscarRepresentantesLegais() {

		if($scope.cadastro.empreendimento.empreendedor.pessoa.id){

			empreendedorService.getRepresentantes($scope.cadastro.empreendimento.empreendedor.pessoa.id).then(

				function(response) {

					$scope.cadastro.empreendimento.representantesLegais = response.data;

					var proprietario = findProprietario(dadosUsuarioLogado.pessoa.cpf);
					var indexUsuarioLogado = findIndexRepresentante(dadosUsuarioLogado);

					if(indexUsuarioLogado === -1){

						if(proprietario)
							dadosUsuarioLogado.tipo = app.TIPOS_REPRESENTANTE_LEGAL.PROPRIETARIO;

						$scope.cadastro.empreendimento.representantesLegais.unshift(dadosUsuarioLogado);

					} else {

						if(proprietario)
							$scope.cadastro.empreendimento.representantesLegais[indexUsuarioLogado].tipo = app.TIPOS_REPRESENTANTE_LEGAL.PROPRIETARIO;
						else
							$scope.cadastro.empreendimento.representantesLegais[indexUsuarioLogado].tipo = app.TIPOS_REPRESENTANTE_LEGAL.REPRESENTANTE;

					}

					/* Setando flag que indica se o representante pode ser desvinculado, isto é, se ele não é o empreendedor */
					_.each($scope.cadastro.empreendimento.representantesLegais,
						function(representante){
							if(representante.pessoa.cpf && (representante.pessoa.cpf !== dadosUsuarioLogado.pessoa.cpf) ||
								representante.pessoa.cnpj && (representante.pessoa.cnpj !== dadosUsuarioLogado.pessoa.cnpj)){
								representante.podeSerDesvinculado = true;
							}
						}
					);

				}

			);

		}else{

			$scope.cadastro.empreendimento.representantesLegais = [dadosUsuarioLogado];

		}
	}

	$scope.$watch("cadastro.etapa", function(etapa) {

		if(etapa && etapa.class === $scope.cadastro.etapas.REPRESENTANTES.class &&
			!$scope.cadastro.empreendimento.representantesLegais){
			
		}
	});

	function cpfBuscado(dadosPessoa, cpf) {

		var representante = _.find($scope.cadastro.empreendimento.representantesLegais, function(representante) {
			if(cpf === representante.pessoa.cpf){
				return angular.copy(representante.pessoa);
			}
		});

		if (representante) {
			etapaRepresentantes.representanteAtual.pessoa = null;
			mensagem.info('Um representante legal com o CPF ' + representante.pessoa.cpf + ' já está vinculado. ', {ttl: 12000});
			return;
		}

		/* Se a pessoa veio do banco de dados, não deve ser possível editar seus dados */
		if (dadosPessoa) {

			dadosPessoa.editavel = false;

		} else {

			$scope.formDadosRepresentanteLegal.$setSubmitted();
			dadosPessoa = {cpf: cpf, editavel: true, type: 'PessoaFisica'};
			$scope.formDadosRepresentanteLegal.$setPristine();
			$scope.formDadosRepresentanteLegal.$setUntouched();
		}

		dadosPessoa.excluivel = true;

		if (etapaRepresentantes.representanteAtual.pessoa){
			etapaRepresentantes.enderecoRepresentanteLegal = angular.copy(getEndereco(etapaRepresentantes.representanteAtual.pessoa.enderecos, etapaRepresentantes.enderecoRepresentanteLegal));
			etapaRepresentantes.contatoPrincipalRepresentanteLegal = angular.copy(getContatoPessoa(etapaRepresentantes.representanteAtual.pessoa.contatos, etapaRepresentantes.contatoPrincipalRepresentanteLegal));
		}else{
			etapaRepresentantes.representanteAtual.pessoa = dadosPessoa;

			if(etapaRepresentantes.representanteAtual.pessoa.enderecos === undefined){
				etapaRepresentantes.representanteAtual.pessoa.enderecos = $scope.etapaRepresentantes.enderecoRepresentanteLegal;
				etapaRepresentantes.representanteAtual.pessoa.enderecos.novoRepresentante = true;
			}

			if(etapaRepresentantes.representanteAtual.pessoa.contatos === undefined){
				etapaRepresentantes.representanteAtual.pessoa.contatos = $scope.etapaRepresentantes.contatoPrincipalRepresentanteLegal;
				etapaRepresentantes.representanteAtual.pessoa.contatos.novoRepresentante = true;
			}
		}
		inicializarEnderecos(etapaRepresentantes.enderecoRepresentanteLegal);
		podeVincular();
	}

	function inicializarEnderecos(enderecosRepresentanteLegal) {
		
		if (enderecosRepresentanteLegal.principal) {
			if(enderecosRepresentanteLegal.principal.cep) {

				enderecosRepresentanteLegal.principal.cep = enderecosRepresentanteLegal.principal.cep.zeroEsquerda(8);
				enderecosRepresentanteLegal.principal.cep = enderecosRepresentanteLegal.principal.cep.includes("-") ? enderecosRepresentanteLegal.principal.cep.replace("-", "") : enderecosRepresentanteLegal.principal.cep;
	
			}
		}

		if (enderecosRepresentanteLegal.correspondencia) {
			if(enderecosRepresentanteLegal.correspondencia.cep) {

				enderecosRepresentanteLegal.correspondencia.cep = enderecosRepresentanteLegal.correspondencia.cep.zeroEsquerda(8);
				enderecosRepresentanteLegal.correspondencia.cep = enderecosRepresentanteLegal.correspondencia.cep.includes("-") ? enderecosRepresentanteLegal.correspondencia.cep.replace("-", "") : enderecosRepresentanteLegal.correspondencia.cep;
	
			}
		}
		
	}

	function iniciarAtualizacaoRepresentante(indexRepresentante) {

		indexRepresentanteASerAtualizado = indexRepresentante;

		etapaRepresentantes.atualizandoRepresentante = true;

		var representante = $scope.cadastro.empreendimento.representantesLegais[indexRepresentante];

		etapaRepresentantes.representanteAtual = angular.copy(representante);

		etapaRepresentantes.contatoPrincipalRepresentanteLegal         = etapaRepresentantes.representanteAtual.pessoa.contatos;
		etapaRepresentantes.enderecoRepresentanteLegal.principal       = etapaRepresentantes.representanteAtual.pessoa.enderecos.principal;
        etapaRepresentantes.enderecoRepresentanteLegal.correspondencia = etapaRepresentantes.representanteAtual.pessoa.enderecos.correspondencia;

	}

	function cancelarAlteracao() {

		indexRepresentanteASerAtualizado = null;

		etapaRepresentantes.atualizandoRepresentante = false;

		etapaRepresentantes.representanteAtual = {};

		$scope.$broadcast('cleanCpfCnpj');
		$scope.$broadcast('limparUf');

	}


	function atualizarRepresentante() {

		$scope.cadastro.empreendimento.representantesLegais.splice(indexRepresentanteASerAtualizado, 1, etapaRepresentantes.representanteAtual);

		indexRepresentanteASerAtualizado = undefined;

		etapaRepresentantes.atualizandoRepresentante = false;

		etapaRepresentantes.representanteAtual = {};

		mensagem.success('Representante legal atualizado com sucesso');

	}

	function copiarEnderecoRepresentante() {

		etapaRepresentantes.representanteAtual.enderecoCorrespondenciaCopiado  = !etapaRepresentantes.representanteAtual.enderecoCorrespondenciaCopiado;

		if(etapaRepresentantes.representanteAtual.enderecoCorrespondenciaCopiado && etapaRepresentantes.enderecoRepresentanteLegal.principal ){

			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia = {};
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.cep = etapaRepresentantes.enderecoRepresentanteLegal.principal .cep.zeroEsquerda(8);
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.bairro = etapaRepresentantes.enderecoRepresentanteLegal.principal .bairro;
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.semNumero = etapaRepresentantes.enderecoRepresentanteLegal.principal .semNumero;
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.numero = etapaRepresentantes.enderecoRepresentanteLegal.principal .numero;
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.logradouro = etapaRepresentantes.enderecoRepresentanteLegal.principal .logradouro;
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.complemento = etapaRepresentantes.enderecoRepresentanteLegal.principal .complemento;
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.correspondencia = true;
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.tipo = etapaRepresentantes.enderecoRepresentanteLegal.principal .tipo;

			if (etapaRepresentantes.enderecoRepresentanteLegal.principal .municipio) {

				etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.municipio = {};
				angular.copy(etapaRepresentantes.enderecoRepresentanteLegal.principal .municipio, etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.municipio);
				$scope.$broadcast('refreshMunicipios');

			} 

		}
		
		else if(!etapaRepresentantes.representanteAtual.enderecoCorrespondenciaCopiado ){

			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia = {};
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.cep = null;
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.bairro = null;
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.semNumero = null;
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.numero = null;
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.logradouro = null;
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.complemento = null;
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.correspondencia = true;
			etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.tipo = null;

			if (etapaRepresentantes.enderecoRepresentanteLegal.principal ) {

				etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.municipio = {};
				angular.copy(etapaRepresentantes.enderecoRepresentanteLegal.principal .municipio, etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.municipio);
				etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.municipio.estado.sigla = '';
				etapaRepresentantes.enderecoRepresentanteLegal.correspondencia.municipio = {};
				$scope.$broadcast('refreshMunicipios');

			}

		}
	}


	function getContatoPessoa(listaContatos, contatoPrincipal){

		_.forEach(listaContatos, function (contato){
					
			if(contato.principal === true && contato.tipo.id === etapaRepresentantes.tipoContato.EMAIL)
				contatoPrincipal.email = contato.valor;
			else if (contato.tipo.id === etapaRepresentantes.tipoContato.TELEFONE_RESIDENCIAL)
				contatoPrincipal.telefone = contato.valor;
			else if (contato.tipo.id === etapaRepresentantes.tipoContato.TELEFONE_CELULAR)
				contatoPrincipal.celular = contato.valor;
			 
		});	
		return angular.copy(contatoPrincipal);
	}

	function getEndereco(listaEnderecos, enderecos) {

		_.forEach(listaEnderecos, function(endereco){

			if(endereco.tipo.id === etapaRepresentantes.tipoEndereco.PRINCIPAL)
				enderecos.principal = endereco; 
			else
				enderecos.correspondencia = endereco;
		});
		return angular.copy(enderecos);
	}

};

exports.controllers.EtapaRepresentantesController = EtapaRepresentantesController;