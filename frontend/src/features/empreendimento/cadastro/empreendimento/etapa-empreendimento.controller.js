var EtapaEmpreendimentoController = function($scope, empreendimentoService, municipioService, $rootScope, mensagem, $location) {

	var etapaEmpreendimento = this;
	etapaEmpreendimento.cadastrar = cadastrar;
	etapaEmpreendimento.novoEmpreendimento = novoEmpreendimento;
	etapaEmpreendimento.eRenovacao = eRenovacao;
	etapaEmpreendimento.copiarEnderecoEmpreendimento = copiarEnderecoEmpreendimento;
	etapaEmpreendimento.enderecoCorrespondenciaCopiado = false;
	etapaEmpreendimento.localizacoesEmpreendimento = app.LOCALIZACOES_EMPREENDIMENTO;
	etapaEmpreendimento.tipoPessoa = app.TIPO_PESSOA;
	etapaEmpreendimento.tipoEndereco = app.TIPO_ENDERECO;
	etapaEmpreendimento.tipoContato = app.TIPO_CONTATO;
	$scope.municipioEmpreendimento = {};

	etapaEmpreendimento.contatoPrincipalEmpreendimento = {
	 	email: null,
	 	telefone:null,
	 	celular: null
	 };

	etapaEmpreendimento.enderecoEmpreendimento = {
	 	principal: {},
	 	correspondencia: {}
	};

	if(!$scope.cadastro.etapas.EMPREENDIMENTO.abas) {

		$scope.cadastro.etapas.EMPREENDIMENTO.abas = {
			DADOS: {indice: 0, abaValida: undefined},
			LOCALIZACAO: {indice: 1, abaValida: undefined}
		};
	}

	$scope.cadastro.etapas.EMPREENDIMENTO.tabIndex = $scope.cadastro.etapas.EMPREENDIMENTO.abas.DADOS.indice;


	if(!$scope.cadastro.etapas.EMPREENDIMENTO.passoValido){

		$scope.cadastro.etapas.EMPREENDIMENTO.passoValido = passoValido;
	}

	function passoValido() {

		let dadosAbaValida = $scope.cadastro.etapas.EMPREENDIMENTO.abas.DADOS.abaValida();
		let localizacaoAbaValida = $scope.cadastro.etapas.EMPREENDIMENTO.abas.LOCALIZACAO.abaValida();

		//setTipoEnderecoEmpreendimento();

		return dadosAbaValida && localizacaoAbaValida;
	}

	function cadastrar(origemEmpreendimento) {

		var element = document.getElementById("voltarEmpreendimento");
		element.classList.add("ng-hide");

		$scope.cadastro.origemEmpreendimento = origemEmpreendimento;

		if (origemEmpreendimento === $scope.cadastro.origensEmpreendimento.EMPREENDEDOR) {

			$scope.cadastro.cpfCnpjPesquisado = true;
			copiarPessoaEmpreendedor();

		}
	}

	/**
	 * Caso o usuário altere o Empreendedor e volte para a tela de 'Empreendimento,
	 * é preciso copiar os dados atualizados.
	 */
	$scope.$watch("cadastro.etapa", function(etapa) {

		if(etapa && etapa.class === $scope.cadastro.etapas.EMPREENDIMENTO.class){

			let pessoaEmpreendimento = $scope.cadastro.empreendimento.pessoa;
			let	pessoaEmpreendedor = $scope.cadastro.empreendimento.empreendedor.pessoa;

			if($scope.cadastro.origemEmpreendimento === $scope.cadastro.origensEmpreendimento.EMPREENDEDOR &&
				pessoasSaoDiferentes(pessoaEmpreendedor, pessoaEmpreendimento)){
				copiarPessoaEmpreendedor();
			}
			if ( $scope.cadastro.empreendimento.empreendedor.pessoa){
				$scope.cpfCnpjEmpreendedor = $scope.cadastro.empreendimento.empreendedor.pessoa.tipo.codigo === app.TIPO_PESSOA.PESSOA_FISICA? $scope.cadastro.empreendimento.empreendedor.pessoa.cpf :
					$scope.cadastro.empreendimento.empreendedor.pessoa.cnpj;
			}

		}else{
			// if ($scope.cadastro.empreendimento.empreendedor !== undefined && $scope.cadastro.empreendimento.empreendedor !== null){
				if($scope.cadastro.empreendimento.empreendedor){
					if ( $scope.cadastro.empreendimento.empreendedor.pessoa){
							$scope.cpfCnpjEmpreendedor = $scope.cadastro.empreendimento.empreendedor.pessoa.tipo.codigo === app.TIPO_PESSOA.PESSOA_FISICA? $scope.cadastro.empreendimento.empreendedor.pessoa.cpf :
						$scope.cadastro.empreendimento.empreendedor.pessoa.cnpj;
					}
				}
				 if($scope.cadastro.empreendimento.empreendimentoEU){
					$scope.cpfCnpjEmpreendedor = $scope.cadastro.empreendimento.empreendimentoEU.empreendedor.pessoa.tipo.codigo === app.TIPO_PESSOA.PESSOA_FISICA? $scope.cadastro.empreendimento.empreendimentoEU.empreendedor.pessoa.cpf :
						$scope.cadastro.empreendimento.empreendimentoEU.empreendedor.pessoa.cnpj;
					$scope.cadastro.empreendimento.empreendedor = $scope.cadastro.empreendimento.empreendimentoEU.empreendedor;

			}

		}

	});

	var copiarPessoaEmpreendedor = function() {

		let cpfCnpj, empreendedor;

		if (!$scope.cadastro.empreendimento.empreendedor.pessoa.nova) {

			cpfCnpj = $scope.cadastro.empreendimento.empreendedor.pessoa.tipo.codigo === app.TIPO_PESSOA.PESSOA_JURIDICA ?
				$scope.cadastro.empreendimento.empreendedor.pessoa.cnpj : $scope.cadastro.empreendimento.empreendedor.pessoa.cpf;

		} else {

			cpfCnpj = $scope.cadastro.empreendimento.empreendedor.pessoa.type !== "PessoaFisica"?
				$scope.cadastro.empreendimento.empreendedor.pessoa.cnpj : $scope.cadastro.empreendimento.empreendedor.pessoa.cpf;
		}

		empreendedor = angular.copy($scope.cadastro.empreendimento.empreendedor);

		if ($scope.cadastro.origemEmpreendimento === 'outroCpfCnpj') {
			empreendimentoService.buscarEmpreendimentoOutroEmpreendedor(cpfCnpj).then(function (response) {
				if (response.data) {
					$scope.cadastro.cpfCnpjPesquisado = true;
					response.data.empreendedor = empreendedor;
					$scope.cadastro.empreendimento = response.data;
					$scope.cpfEmpreendedor = response.data.empreendedor.empreendimento.pessoa;
				}else{
					mensagem.error('CNPJ não encontrado, favor cadastrar ou atualizar o cadastro em: portalservicos.jucap.ap.gov.br');
				}

			});
		} else{
			empreendimentoService.buscarEmpreendimentoPorCpfCnpj(cpfCnpj).then(
				function(response) {

					if (response.data) {

						response.data.empreendedor = empreendedor;

						$scope.cadastro.empreendimento = response.data;

						if($scope.cadastro.empreendimento !== null) {
							$scope.cadastro.empreendimentoCadastrado = true;
						}
						else {
							$scope.cadastro.empreendimentoCadastrado = false;
						}

						if ($scope.cadastro.empreendimento.empreendimentoEU.pessoa && cpfCnpj.length <= 11) {

							$scope.cadastro.empreendimento.empreendimentoEU.pessoa = angular.copy(empreendedor.pessoa);

							if(!empreendedor.pessoa.enderecos) {
								$scope.cadastro.empreendimento.enderecos = [{tipo: 'ZONA_URBANA', correspondencia: false}, {tipo: 'ZONA_URBANA', correspondencia: true}];
							}
							else {
								$scope.cadastro.empreendimento.enderecos = angular.copy(empreendedor.pessoa.enderecos);
							}

							$scope.cadastro.empreendimento.contatos = empreendedor.pessoa.contatos;
							$scope.cadastro.enderecoEmpreendimento = getEndereco(empreendedor.pessoa.enderecos, etapaEmpreendimento.enderecoEmpreendimento);

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
							etapaEmpreendimento.disabledUf = true;

							$scope.cadastro.contatoPrincipalEmpreendimento = getContatoPessoa(empreendedor.pessoa.contatos,etapaEmpreendimento.contatoPrincipalEmpreendimento);
							$scope.municipioEmpreendimento = etapaEmpreendimento.enderecoEmpreendimento.principal.municipio;

						}
						else if($scope.cadastro.empreendimento.empreendimentoEU.pessoa && cpfCnpj.length > 11) {

							$scope.cadastro.empreendimento.empreendimentoEU.empreendedor = $scope.cadastro.empreendimento.empreendedor;

							$scope.cadastro.empreendimento.contatos = $scope.cadastro.empreendimento.empreendimentoEU.contatos;

							if(!$scope.cadastro.empreendimento.empreendimentoEU.enderecos) {
								$scope.cadastro.empreendimento.enderecos = [{tipo: 'ZONA_URBANA', correspondencia: false}, {tipo: 'ZONA_URBANA', correspondencia: true}];
							}
							else {
								$scope.cadastro.empreendimento.enderecos = angular.copy($scope.cadastro.empreendimento.empreendimentoEU.enderecos);
							}

							formataEnderecosSemNumero($scope.cadastro.empreendimento.empreendimentoEU.enderecos);

							$scope.cadastro.enderecoEmpreendimento = getEndereco($scope.cadastro.empreendimento.empreendimentoEU.enderecos, etapaEmpreendimento.enderecoEmpreendimento);
							$scope.cadastro.contatoPrincipalEmpreendimento = getContatoPessoa($scope.cadastro.empreendimento.empreendimentoEU.contatos,etapaEmpreendimento.contatoPrincipalEmpreendimento);

							$scope.cadastro.empreendimento.municipio = $scope.cadastro.empreendimento.municipioLicenciamento;
							$scope.cadastro.empreendimento.denominacao = $scope.cadastro.empreendimento.empreendimentoEU.denominacao;

						} else {
							mensagem.warning('CNPJ não encontrado, favor cadastrar ou atualizar o cadastro em: portalservicos.jucap.ap.gov.br');
						}
					} else {
						$scope.cadastro.origemEmpreendimento = null;
						mensagem.warning('CPF/CNPJ já cadastrado como empreendimento.');
					}

					if($scope.cadastro.empreendimento.empreendimentoEU.pessoa.dataNascimento){

						$scope.cadastro.empreendimento.empreendimentoEU.pessoa.dataNascimento = $scope.cadastro.empreendimento.empreendimentoEU.pessoa.dataNascimento.dataNascimento.toDate();

					} else if ($scope.cadastro.empreendimento.empreendimentoEU.pessoa.dataConstituicao) {

						$scope.cadastro.empreendimento.empreendimentoEU.pessoa.dataConstituicao = $scope.cadastro.empreendimento.empreendimentoEU.pessoa.dataConstituicao.toDate();
					}

				},
				function(error){

					if (error.data.texto) {

						mensagem.warning(error.data.texto);
					}
				}
			);
		}

	};

	$rootScope.$on('selectMunicipioEnderecoPrincipalEmpreendimento', function(event, municipio) {

		if ($scope.cadastro.enderecoEmpreendimento.principal !== undefined && etapaEmpreendimento !== null && etapaEmpreendimento !== undefined) {

			if($scope.cadastro.enderecoEmpreendimento.principal.municipio === undefined || municipio.id !== $scope.cadastro.enderecoEmpreendimento.principal.municipio.id){
				$scope.cadastro.enderecoEmpreendimento.principal.municipio = municipio;
				return;
			}

		}
    });

	var pessoasSaoDiferentes = function(pessoaEmpreendedor, pessoaEmpreendimento) {

		var docEmpreendedor = pessoaEmpreendedor? pessoaEmpreendedor.cpf || pessoaEmpreendedor.cnpj: null;
		var docEmpreendimento = pessoaEmpreendimento ? pessoaEmpreendimento.cpf || pessoaEmpreendimento.cnpj : null;

		return docEmpreendedor !== docEmpreendimento;

	};

	function formataEnderecosSemNumero(listaEnderecos){
		_.forEach(listaEnderecos, function(endereco){
			if(endereco.numero === null){
				endereco.semNumero = true;
			}
		});
	}

	function novoEmpreendimento() {

		$scope.cadastro.empreendimento.pessoa = undefined;
	}

	function eRenovacao() {

		var url = $location.path();
		if(url.indexOf('renovacao') > -1) {

			return true;

		} else {

			return false;
		}
	}

	function copiarEnderecoEmpreendimento() {

		etapaEmpreendimento.enderecoCorrespondenciaCopiado  = !etapaEmpreendimento.enderecoCorrespondenciaCopiado;

		if(etapaEmpreendimento.enderecoCorrespondenciaCopiado && etapaEmpreendimento.enderecoEmpreendimento.principal){

			etapaEmpreendimento.enderecoEmpreendimento.correspondencia =
				_.clone(etapaEmpreendimento.enderecoEmpreendimento.principal);
		}
		else if(!etapaEmpreendimento.enderecoCorrespondenciaCopiado){

			etapaEmpreendimento.enderecoEmpreendimento.correspondencia = {};
			etapaEmpreendimento.enderecoEmpreendimento.correspondencia.cep = null;
			etapaEmpreendimento.enderecoEmpreendimento.correspondencia.bairro = null;
			etapaEmpreendimento.enderecoEmpreendimento.correspondencia.semNumero = null;
			etapaEmpreendimento.enderecoEmpreendimento.correspondencia.numero = null;
			etapaEmpreendimento.enderecoEmpreendimento.correspondencia.logradouro = null;
			etapaEmpreendimento.enderecoEmpreendimento.correspondencia.complemento = null;
			etapaEmpreendimento.enderecoEmpreendimento.correspondencia.correspondencia = true;
			etapaEmpreendimento.enderecoEmpreendimento.correspondencia.tipo = null;

			if (etapaEmpreendimento.enderecoEmpreendimento.correspondencia.municipio) {

				etapaEmpreendimento.enderecoEmpreendimento.correspondencia.municipio = {};
				angular.copy(etapaEmpreendimento.enderecoEmpreendimento.principal.municipio, etapaEmpreendimento.enderecoEmpreendimento.correspondencia.municipio);
				etapaEmpreendimento.enderecoEmpreendimento.correspondencia.municipio.estado.sigla = '';
				etapaEmpreendimento.enderecoEmpreendimento.correspondencia.municipio = {};
				$scope.$broadcast('refreshMunicipios');

			}
		}
	}

	//function setTipoEnderecoEmpreendimento(){
	// 	$scope.cadastro.empreendimento.enderecos[0].tipo = cadastro.empreendimento.localizacao;
	//}

	 function getContatoPessoa(listaContatos, contatoPrincipal){

	 	_.forEach(listaContatos, function (contato){

	 		if(contato.principal === true && contato.tipo.id === etapaEmpreendimento.tipoContato.EMAIL)
	 			contatoPrincipal.email = contato.valor;
			else if (contato.tipo.id === etapaEmpreendimento.tipoContato.TELEFONE_RESIDENCIAL)
	 			contatoPrincipal.telefone = contato.valor;
	 		else if (contato.tipo.id === etapaEmpreendimento.tipoContato.TELEFONE_CELULAR)
	 			contatoPrincipal.celular = contato.valor;

	 	});
	 	return contatoPrincipal;
	 }

	function getEndereco(listaEnderecos, enderecos) {

	 	_.forEach(listaEnderecos, function(endereco){

	 		if(endereco.tipo.id === etapaEmpreendimento.tipoEndereco.PRINCIPAL){
				enderecos.principal = endereco;
	 		}
	 		else{
	 			enderecos.correspondencia = endereco;
	 		}
	 	});
	 	return angular.copy(enderecos);
	}

};

exports.controllers.EtapaEmpreendimentoController = EtapaEmpreendimentoController;
