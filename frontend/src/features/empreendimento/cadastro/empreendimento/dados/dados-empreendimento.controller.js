var DadosEmpreendimentoController = function($scope, mensagem, empreendimentoService, $anchorScroll, $q, $uibModal, tipoResponsavelEmpreendimentoService) {

	var dadosEmpreendimento = this;

	dadosEmpreendimento.abaValida = abaValida;
	dadosEmpreendimento.anterior = anterior;
	dadosEmpreendimento.proximo = proximo;
	dadosEmpreendimento.getPessoaPorCpfCnpj = empreendimentoService.getPessoaPorCpfCnpj;
	dadosEmpreendimento.buscarEmpreendimentoPorCpfCnpj = empreendimentoService.buscarEmpreendimentoPorCpfCnpj;
	dadosEmpreendimento.copiarEnderecoEmpreendimento = copiarEnderecoEmpreendimento;
	dadosEmpreendimento.pessoaBuscada = pessoaBuscada;
	dadosEmpreendimento.onBeforeUpdateCpfCnpj = onBeforeUpdateCpfCnpj;
	dadosEmpreendimento.mostrarDados = mostrarDados;
	dadosEmpreendimento.validaPessoaFisica = validaPessoaFisica;
	dadosEmpreendimento.bloqueaCNPJ = bloqueaCNPJ;
	dadosEmpreendimento.localizacoesEmpreendimento = app.LOCALIZACOES_EMPREENDIMENTO;
	dadosEmpreendimento.tipoPessoa = app.TIPO_PESSOA;
	dadosEmpreendimento.tipoEndereco = app.TIPO_ENDERECO;
	dadosEmpreendimento.tipoContato = app.TIPO_CONTATO;

	dadosEmpreendimento.openedAccordion = true;
	$scope.$parent.alterouMunicipioEmpreendimento = false;

	dadosEmpreendimento.contatoPrincipalEmpreendimento = {
		email: null,
		telefone:null,
		celular: null
	};

	dadosEmpreendimento.enderecoEmpreendimento = {
		principal: {},
		correspondencia: {}
	};

	if(!$scope.cadastro.etapas.EMPREENDIMENTO.abas.DADOS.abaValida){
		$scope.cadastro.etapas.EMPREENDIMENTO.abas.DADOS.abaValida = abaValida;
	}

	function abaValida() {

		if (!$scope.formDadosEmpreendimento) {
			return false;
		}

		if ($scope.formDadosEmpreendimento.telefone) {
			$scope.formDadosEmpreendimento.telefone.$error = null;
		}

		if ($scope.formDadosEmpreendimento.celular) {
			$scope.formDadosEmpreendimento.celular.$error = null;
		}

		return $scope.formDadosEmpreendimento.$valid;
	}

	function anterior() {

		$scope.cadastro.anterior();
	}

	function proximo() {
		$scope.formDadosEmpreendimento.$setSubmitted();

		$scope.cadastro.contatoPrincipalEmpreendimento.telefone = $scope.formDadosEmpreendimento.telefone.$viewValue;
		$scope.cadastro.contatoPrincipalEmpreendimento.celular = $scope.formDadosEmpreendimento.celular.$viewValue;

		if (dadosEmpreendimento.abaValida()) {

			$scope.cadastro.etapas.EMPREENDIMENTO.tabIndex++;
		} else {

			mensagem.warning('Verifique os campos destacados em vermelho para prosseguir com o cadastro.');
			$anchorScroll();
		}
	}

	/* Busca os tipos de responsáveis (TECNICO, LEGAL) de acordo com a regra.
	 * Foi necessário implementar nessa controller pois somente nessa etapa tem-se a definição
	 * do tipo do empreendimento (CPF ou CNPJ).
	*/
	function buscarTiposResponsaveis () {

		tipoResponsavelEmpreendimentoService.list().then(

			function(response) {
				$scope.cadastro.tiposResponsavel = response.data;
			},
			function(){
				mensagem.error('Ocorreu um erro ao buscar as informações de tipos de responsáveis.');
			}

		);
	}

	function pessoaBuscada(empreendimento, cpfCnpj) {

		let pessoa = empreendimento.empreendimentoEU.pessoa;

		if(empreendimento.idEntradaUnica !== null && empreendimento.idEntradaUnica !== undefined ) {
			$scope.cadastro.empreendimentoCadastrado = true;
		}
		else {
			$scope.cadastro.empreendimentoCadastrado = false;
			$scope.cadastro.cpfCnpjPesquisado = true;
		}

		if(!pessoa) {

			if (cpfCnpj.isCPF()) {

				// Recebe os dados do empreendedor se o cpf do empreendimento for igual ao do empreendedor
				if (cpfCnpj === $scope.cadastro.empreendimento.empreendedor.pessoa.cpf) {
					copiarPessoaEmpreendedor();
					return;
				}

				if (!proprietariosHaveCpf(cpfCnpj)) {

					$scope.cadastro.empreendimento.pessoa = {type: 'PessoaFisica', cpf: cpfCnpj};
				}

			} else {

				// Recebe os dados do empreendedor se o cnpj do empreendimento for igual ao do empreendedor
				if (cpfCnpj === $scope.cadastro.empreendimento.empreendedor.pessoa.cnpj) {

					copiarPessoaEmpreendedor();
				} else {
					$scope.cadastro.empreendimentoCadastrado = false;
					// TODO: Avaliar essa condição para verificar se está correta.
					$scope.cadastro.empreendimento.pessoa = {type: 'PessoaJuridica', cnpj: cpfCnpj};
				}
			}
		}

		if(!$scope.cadastro.empreendimento.municipio) {
			$scope.cadastro.empreendimento.municipio = {};
			if(!$scope.cadastro.empreendimento.municipio.estado) {
				$scope.cadastro.empreendimento.municipio.estado = {};
				if(!$scope.cadastro.empreendimento.municipio.estado.codigo) {
					$scope.cadastro.empreendimento.municipio.estado.codigo = 'AP';
				}
			}
		}
		$scope.cadastro.contatoPrincipalEmpreendimento = getContatoPessoa($scope.cadastro.empreendimento.empreendedor.pessoa.contatos, dadosEmpreendimento.contatoPrincipalEmpreendimento);
		$scope.cadastro.enderecoEmpreendimento = getEndereco($scope.cadastro.empreendimento.empreendedor.pessoa.enderecos, dadosEmpreendimento.enderecoEmpreendimento);

		$scope.cadastro.enderecoEmpreendimento.principal.bairro = null;
		$scope.cadastro.enderecoEmpreendimento.principal.caixaPostal = null;
		$scope.cadastro.enderecoEmpreendimento.principal.cep = null;
		$scope.cadastro.enderecoEmpreendimento.principal.complemento = null;
		$scope.cadastro.enderecoEmpreendimento.principal.id = null;
		$scope.cadastro.enderecoEmpreendimento.principal.logradouro = null;
		$scope.cadastro.enderecoEmpreendimento.principal.municipio = {};
		$scope.cadastro.enderecoEmpreendimento.principal.municipio.estado = {codigo: 'AP'};
		$scope.cadastro.enderecoEmpreendimento.principal.numero = null;
		$scope.cadastro.enderecoEmpreendimento.principal.semNumero = null;
		// etapaEmpreendimento.disabledUf = true;
	}

	function proprietariosHaveCpf(cpf) {

		var proprietario = getProprietarioByCpf(cpf);

		if (proprietario) {

			$scope.cadastro.empreendimento.pessoa = angular.copy(proprietario.pessoa);
			$scope.cadastro.empreendimento.pessoa.type = 'PessoaFisica';
			return true;
		}

		return false;
	}

	function getProprietarioByCpf(cpf) {

		return _.find($scope.cadastro.empreendimento.proprietarios,

			function(proprietario){

				return cpf === proprietario.pessoa.cpf;
			}
		);
	}

	function copiarEnderecoEmpreendimento() {

		$scope.cadastro.enderecoCorrespondenciaCopiado  = !$scope.cadastro.enderecoCorrespondenciaCopiado;

		if($scope.cadastro.enderecoCorrespondenciaCopiado && $scope.cadastro.enderecoEmpreendimento.principal){

			$scope.cadastro.enderecoEmpreendimento.correspondencia.cep = $scope.cadastro.enderecoEmpreendimento.principal.cep;
			$scope.cadastro.enderecoEmpreendimento.correspondencia.bairro = $scope.cadastro.enderecoEmpreendimento.principal.bairro;
			$scope.cadastro.enderecoEmpreendimento.correspondencia.semNumero = $scope.cadastro.enderecoEmpreendimento.principal.semNumero;
			$scope.cadastro.enderecoEmpreendimento.correspondencia.numero = $scope.cadastro.enderecoEmpreendimento.principal.numero;
			$scope.cadastro.enderecoEmpreendimento.correspondencia.logradouro = $scope.cadastro.enderecoEmpreendimento.principal.logradouro;
			$scope.cadastro.enderecoEmpreendimento.correspondencia.complemento = $scope.cadastro.enderecoEmpreendimento.principal.complemento;
			$scope.cadastro.enderecoEmpreendimento.correspondencia.municipio = $scope.cadastro.enderecoEmpreendimento.principal.municipio;

		}
		else if(!$scope.cadastro.enderecoCorrespondenciaCopiado){

			$scope.cadastro.enderecoEmpreendimento.correspondencia.cep = null;
			$scope.cadastro.enderecoEmpreendimento.correspondencia.bairro = null;
			$scope.cadastro.enderecoEmpreendimento.correspondencia.semNumero = null;
			$scope.cadastro.enderecoEmpreendimento.correspondencia.numero = null;
			$scope.cadastro.enderecoEmpreendimento.correspondencia.logradouro = null;
			$scope.cadastro.enderecoEmpreendimento.correspondencia.complemento = null;
			$scope.cadastro.enderecoEmpreendimento.correspondencia.correspondencia = true;
			$scope.cadastro.enderecoEmpreendimento.correspondencia.municipio = null;
			$scope.$broadcast('refreshMunicipios');

		}
	}

	function copiarPessoaEmpreendedor() {

		$scope.cadastro.empreendimento.pessoa = angular.copy($scope.cadastro.empreendimento.empreendedor.pessoa);

	}

	function onBeforeUpdateCpfCnpj(cpfCnpj) {

		var deferred = $q.defer();

		if (!$scope.cadastro.empreendimento.pessoa) {

			deferred.resolve();
			return deferred.promise;

		} else if ((cpfCnpj.isCPF() && cpfCnpj === $scope.cadastro.empreendimento.pessoa.cpf) ||
					(cpfCnpj.isCNPJ() && cpfCnpj === $scope.cadastro.empreendimento.pessoa.cnpj)) {

			deferred.reject();
			return deferred.promise;

		} else {

			cleanDadosEmpreendimento();
			$scope.cadastro.empreendimento.imovel = null;

			deferred.resolve();
			return deferred.promise;

		}

	}

	function cleanDadosEmpreendimento() {

		$scope.cadastro.empreendimento.denominacao = null;
		$scope.cadastro.empreendimento.jurisdicao = null;
		$scope.cadastro.empreendimento.municipio = null;
		$scope.cadastro.empreendimento.contato = null;
		$scope.cadastro.empreendimento.enderecos = [{tipo: 'ZONA_URBANA', correspondencia: false}, {correspondencia: true}];
		$scope.$broadcast('limparUf');

	}

	// function bloqueaPessoa() {

	// 	return $scope.cadastro.empreendimentoCadastrado;
	// }

	function bloqueaCNPJ() {
		//Bloquea os campos do empreendimento ja cadastrado... CNPJ.
		return $scope.cadastro.empreendimento && (!validaPessoaFisica());
	}

	function mostrarDados () {

		return ($scope.cadastro.empreendimento.pessoa && $scope.cadastro.empreendimento.pessoa.tipo.codigo === app.TIPO_PESSOA.PESSOA_FISICA) ||
			($scope.cadastro.empreendimento.pessoa && $scope.cadastro.empreendimento.pessoa.tipo.codigo === app.TIPO_PESSOA.PESSOA_JURIDICA && $scope.cadastro.empreendimento.pessoa.cnpj) ||
			($scope.cadastro.empreendimento.idEntradaUnica);

	}

	// function isPessoaFisica () {
	// 	return ($scope.cadastro.empreendimento.pessoa && $scope.cadastro.empreendimento.pessoa.tipo.codigo === app.TIPO_PESSOA.PESSOA_FISICA);
	// }

	function validaPessoaFisica () {

		if($scope.cadastro.empreendimento.empreendimentoEU && $scope.cadastro.empreendimento){
			return $scope.cadastro.empreendimento.empreendimentoEU.pessoa.tipo.codigo === app.TIPO_PESSOA.PESSOA_FISICA ? true : false ;
		}
		if($scope.cadastro.empreendimento && $scope.cadastro.empreendimento.pessoa){
			return $scope.cadastro.empreendimento.pessoa.tipo.codigo === app.TIPO_PESSOA.PESSOA_FISICA ? true : false ;
		}

	}

	function preencherTiposResponsavelDisponiveis (tipoPessoa, tipos) {
		$scope.cadastro.tiposResponsavelDisponiveis = {};
		if (tipoPessoa === dadosEmpreendimento.tipoPessoa.PESSOA_FISICA) {
			$scope.cadastro.tiposResponsavelDisponiveis.TECNICO = tipos.TECNICO;
			$scope.cadastro.tiposResponsavelDisponiveis.LEGAL = tipos.LEGAL;
		}
		else if (tipoPessoa === dadosEmpreendimento.tipoPessoa.PESSOA_JURIDICA) {
			$scope.cadastro.tiposResponsavelDisponiveis.TECNICO = tipos.TECNICO;
			$scope.cadastro.tiposResponsavelDisponiveis.LEGAL = tipos.LEGAL;
		}
	}

	$scope.$watch('cadastro.empreendimento.municipio', function(newMunicipio, oldMunicipio){

		if (newMunicipio && oldMunicipio && newMunicipio.id != oldMunicipio.id) {

			$scope.$parent.alterouMunicipioEmpreendimento = true;
		}
	}, true);

	$scope.$watch('cadastro.empreendimento.pessoa', function(newPessoa){

		if(newPessoa && $scope.cadastro.tiposResponsavel && $scope.cadastro.tiposResponsavel) {
			//preencherTiposResponsavelDisponiveis(newPessoa.type, $scope.cadastro.tiposResponsavel);
		}
	}, true);

	$scope.$watch('cadastro.tiposResponsavel', function(newTiposResponsavel){

		if(newTiposResponsavel && $scope.cadastro.empreendimento.empreendimentoEU && $scope.cadastro.empreendimento.empreendimentoEU.pessoa) {
			//preencherTiposResponsavelDisponiveis($scope.cadastro.empreendimento.empreendimentoEU.pessoa.tipo, newTiposResponsavel);
		}
	}, true);

	buscarTiposResponsaveis();

	function getContatoPessoa(listaContatos, contatoPrincipal){

		_.forEach(listaContatos, function (contato){

			if(contato.principal === true && contato.tipo.id === dadosEmpreendimento.tipoContato.EMAIL)
				contatoPrincipal.email = contato.valor;
			else if (contato.tipo.id === dadosEmpreendimento.tipoContato.TELEFONE_RESIDENCIAL)
				contatoPrincipal.telefone = contato.valor;
			else if (contato.tipo.id === dadosEmpreendimento.tipoContato.TELEFONE_CELULAR)
				contatoPrincipal.celular = contato.valor;

		});
		return contatoPrincipal;
	}

	function getEndereco(listaEnderecos, enderecos) {

		_.forEach(listaEnderecos, function(endereco){

			if(endereco.tipo.id === dadosEmpreendimento.tipoEndereco.PRINCIPAL){
				enderecos.principal = endereco;
			}
			else{
				enderecos.correspondencia = endereco;
			}
		});
		return angular.copy(enderecos);
	}

};

exports.controllers.DadosEmpreendimentoController = DadosEmpreendimentoController;
