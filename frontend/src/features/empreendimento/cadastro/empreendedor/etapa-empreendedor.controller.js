var EtapaEmpreendedorController = function($scope, $rootScope, $route, empreendedorService, loginService, mensagem, modalSimplesService, $q, $location, $window) {

	var etapaEmpreendedor = this;

	etapaEmpreendedor.passoValido = passoValido;
	etapaEmpreendedor.proximo = proximo;
	etapaEmpreendedor.anterior = anterior;
	etapaEmpreendedor.inicializarPessoa = inicializarPessoa;
	etapaEmpreendedor.inicializarEnderecos = inicializarEnderecos;
	etapaEmpreendedor.buscarPessoaPorCpfCnpj = buscarPessoaPorCpfCnpj;
	etapaEmpreendedor.pessoaBuscada = pessoaBuscada;
	etapaEmpreendedor.onBeforeUpdateCpfCnpj = onBeforeUpdateCpfCnpj;
	etapaEmpreendedor.copiarEnderecoEmpreendedor = copiarEnderecoEmpreendedor;	
	etapaEmpreendedor.novoEmpreendedor = novoEmpreendedor;	
	etapaEmpreendedor.eRenovacao = eRenovacao;
	etapaEmpreendedor.bloqueaCNPJ = bloqueaCNPJ;
	etapaEmpreendedor.mostrarMensagemRedeSim = mostrarMensagemRedeSim;
	etapaEmpreendedor.isPessoaFisica = isPessoaFisica;
	etapaEmpreendedor.isPessoaJuridica = isPessoaJuridica;
	etapaEmpreendedor.mostrarDados = mostrarDados;
	etapaEmpreendedor.getUrlEntradaUnicaCadastro = getUrlEntradaUnicaCadastro;
	etapaEmpreendedor.urlEntradaUnica = '';
	etapaEmpreendedor.hideVoltar = true;
	etapaEmpreendedor.usuarioSemCadastro = false;
	etapaEmpreendedor.tipoContato = app.TIPO_CONTATO;
	etapaEmpreendedor.tipoEndereco = app.TIPO_ENDERECO;
	etapaEmpreendedor.zonaLocalizacao = app.LOCALIZACOES_EMPREENDIMENTO;

	etapaEmpreendedor.contatoPrincipalEmpreendedor = {
		email: null,
		telefone:null,
		celular: null
	};

	etapaEmpreendedor.enderecoEmpreendedor = {
		principal: {},
		correspondencia: {}
	};

	//Variáveis de controle campos disabled
	etapaEmpreendedor.controleCampos = {};
	etapaEmpreendedor.enderecoCorrespondenciaCopiado = false;

	if(!$scope.cadastro.etapas.EMPREENDEDOR.passoValido){
		$scope.cadastro.etapas.EMPREENDEDOR.passoValido = passoValido;
	}

	etapaEmpreendedor.cpfCnpjEmpreendedor = undefined;
	$scope.cadastro.empreendedorCnpjJaCadastrado = true;


	function mostrarDados() {

		return ($scope.cadastro.empreendedorCnpjJaCadastrado && $scope.cadastro.empreendimento && !etapaEmpreendedor.usuarioSemCadastro &&
			$scope.cadastro.empreendimento.empreendedor && $scope.cadastro.empreendimento.empreendedor.pessoa) || this.isPessoaFisica() && !etapaEmpreendedor.usuarioSemCadastro;
	}

	function mostrarMensagemRedeSim() {
		return !$scope.cadastro.empreendedorCnpjJaCadastrado && $scope.cadastro.empreendimento &&
			$scope.cadastro.empreendimento.empreendedor && $scope.cadastro.empreendimento.empreendedor.pessoa &&
			this.isPessoaJuridica();
	}

	function isPessoaFisica() {
		return $scope.cadastro.empreendimento && $scope.cadastro.empreendimento.empreendedor &&
			$scope.cadastro.empreendimento.empreendedor.pessoa && $scope.cadastro.empreendimento.empreendedor.pessoa.cpf;
	}

	function isPessoaJuridica() {
		return $scope.cadastro.empreendimento && $scope.cadastro.empreendimento.empreendedor &&
			$scope.cadastro.empreendimento.empreendedor.pessoa && !!$scope.cadastro.empreendimento.empreendedor.pessoa.cnpj;
	}

	function bloqueaCNPJ() {

		//Bloquea os campos do empreendimento ja cadastrado... CNPJ.
		return $scope.cadastro.empreendimento.empreendedor && $scope.cadastro.empreendimento.empreendedor.pessoa &&
			!!$scope.cadastro.empreendimento.empreendedor.pessoa.cnpj;
	}

	function getUrlEntradaUnicaCadastro() {

		loginService.getUrlEntradaUnicaCadastro().then(function(response) {

			etapaEmpreendedor.urlEntradaUnica = response.data;
			
		});
	}

	function passoValido() {

		if ($scope.formDadosEmpreendedor.telefone) {

			$scope.formDadosEmpreendedor.telefone.$error = null;
			if (($scope.cadastro.empreendimento.empreendedor) && ($scope.cadastro.empreendimento) &&
			   	($scope.cadastro.empreendimento.empreendedor.pessoa)){

					   _.forEach($scope.cadastro.empreendimento.empreendedor.pessoa.contatos, function(contato){
							if(contato.tipo.id === etapaEmpreendedor.tipoContato.TELEFONE_RESIDENCIAL){
								contato = $scope.formDadosEmpreendedor.telefone.$viewValue;
							}
					   });
			}

		}

		if ($scope.formDadosEmpreendedor.celular) {

			$scope.formDadosEmpreendedor.celular.$error = null;
			if (($scope.cadastro.empreendimento.empreendedor) && ($scope.cadastro.empreendimento) &&
				($scope.cadastro.empreendimento.empreendedor.pessoa)){

					_.forEach($scope.cadastro.empreendimento.empreendedor.pessoa.contatos, function(contato){
						if(contato.tipo.id === etapaEmpreendedor.tipoContato.TELEFONE_CELULAR){
							contato = $scope.formDadosEmpreendedor.celular.$viewValue;
						}
				   });
			}
		}

		return $scope.cadastro.empreendimento && $scope.cadastro.empreendimento.empreendedor &&
			$scope.cadastro.empreendimento.empreendedor.pessoa && $scope.formDadosEmpreendedor.$valid;
	}

	function proximo() {

		// Força validação de erros no formulário
		$scope.formDadosEmpreendedor.$setSubmitted();

		$scope.cadastro.contatoPrincipalEmpreendedor.celular = $scope.formDadosEmpreendedor.celular.$viewValue;
		$scope.cadastro.contatoPrincipalEmpreendedor.telefone = $scope.formDadosEmpreendedor.telefone.$viewValue;

		if(etapaEmpreendedor.passoValido()){
			if ($scope.cadastro.empreendimento.empreendedor.pessoa &&
				!$scope.cadastro.empreendimento.empreendedor.pessoa.enderecos[0].tipo){
				$scope.cadastro.empreendimento.empreendedor.pessoa.enderecos[0].tipo = 'ZONA_URBANA';
			}
			$scope.cadastro.proximo();
		}else{
			mensagem.warning('Verifique os campos destacados em vermelho para prosseguir com o cadastro.');
		}

	}

	function inicializarPessoa() {

		if($scope.cadastro.origemEmpreendedor === $scope.cadastro.origensEmpreendedor.USUARIO_LOGADO){

			buscarPessoaPorCpfCnpj($rootScope.usuarioSessao.login);

		}

	}

	function controlaDisabledCampos() {
		etapaEmpreendedor.controleCampos = {
			disabledEmail			   : !!$scope.cadastro.contatoPrincipalEmpreendedor.email,
			disabledTelefone		   : !!$scope.cadastro.contatoPrincipalEmpreendedor.telefone,
			disabledCelular			   : !!$scope.cadastro.contatoPrincipalEmpreendedor.celular,
			disabledCep 		  	   : !!$scope.cadastro.enderecoEmpreendedor.principal.cep,
			disabledLogradouro		   : !!$scope.cadastro.enderecoEmpreendedor.principal.logradouro,
			disabledNumero 			   : !!$scope.cadastro.enderecoEmpreendedor.principal.numero,
			disabledComplemento 	   : !!$scope.cadastro.enderecoEmpreendedor.principal.complemento,
			disabledBairro 	 		   : !!$scope.cadastro.enderecoEmpreendedor.principal.bairro,
			disabledUf 	      		   : !!$scope.cadastro.enderecoEmpreendedor.principal.municipio.estado.sigla,
			disabledMunicipio   	   : !!$scope.cadastro.enderecoEmpreendedor.principal.municipio.nome,
			disabledCaixaPostal 	   : !!$scope.cadastro.enderecoEmpreendedor.principal.caixaPostal,
			disabledRoteiro 		   : !!$scope.cadastro.enderecoEmpreendedor.principal.roteiroAcesso,
			disabledCepCorresp  	   : !!$scope.cadastro.enderecoEmpreendedor.correspondencia.cep,
			disabledLogradouroCorresp  : !!$scope.cadastro.enderecoEmpreendedor.correspondencia.logradouro,
			disabledNumeroCorresp      : !!$scope.cadastro.enderecoEmpreendedor.correspondencia.numero,
			disabledComplementoCorresp : !!$scope.cadastro.enderecoEmpreendedor.correspondencia.complemento,
			disabledBairroCorresp 	   : !!$scope.cadastro.enderecoEmpreendedor.correspondencia.bairro,
			disabledUfCorresp 	       : !!$scope.cadastro.enderecoEmpreendedor.correspondencia.municipio.estado.sigla,
			disabledMunicipioCorresp   : !!$scope.cadastro.enderecoEmpreendedor.correspondencia.municipio.nome,
			disabledCaixaPostalCorresp : !!$scope.cadastro.enderecoEmpreendedor.correspondencia.caixaPostal,
			disabledRoteiroCorresp	   : !!$scope.cadastro.enderecoEmpreendedor.correspondencia.roteiroAcesso

		};

	}

	function buscarPessoaPorCpfCnpj(cpfCnpj) {

		loginService.getUrlEntradaUnicaCadastro().then(function(response) {

			etapaEmpreendedor.urlEntradaUnica = response.data;

			empreendedorService.findByCpfCnpj(cpfCnpj)
			.then(function(response){

				etapaEmpreendedor.usuarioSemCadastro = false;

				$scope.cadastro.empreendedorCnpjJaCadastrado = !!response.data.pessoa && !!response.data.pessoa.cnpj;
				$scope.cadastro.empreendimento.empreendedor = response.data;

				var pessoa = $scope.cadastro.empreendimento.empreendedor.pessoa;

				if (!pessoa && cpfCnpj.length === 11) {

					var configModal = {
						titulo: 'CPF sem vínculo com o Entrada Única',
						conteudo: 'Para CADASTRO DE CPF, você será redirecionado para a página de cadastro do ENTRADA ÚNICA. Após o cadastro, você deverá retornar ao Licenciamento para continuar o cadastro do Empreendimento.'
					};

					etapaEmpreendedor.usuarioSemCadastro = true;

					var instanciaModal = modalSimplesService.abrirModal(configModal);
		
					instanciaModal.result.then(function () {
						
						$window.open( etapaEmpreendedor.urlEntradaUnica + "/#/public/validacao/" + cpfCnpj, '_blank');
						$route.reload();
					}, function () {
						
					});
				}

				etapaEmpreendedor.pessoaBuscada(pessoa, cpfCnpj);

				if(pessoa) {

					if(pessoa.dataNascimento){

						if (pessoa.dataNascimento.toDate !== undefined) {
							$scope.cadastro.empreendimento.empreendedor.pessoa.dataNascimento = pessoa.dataNascimento.toDate();
						}

					} else if (pessoa.dataConstituicao) {
						
						if (pessoa.dataConstituicao.toDate !== undefined) {
							$scope.cadastro.empreendimento.empreendedor.pessoa.dataConstituicao = pessoa.dataConstituicao.toDate();
						}
						
					}

					_.forEach($scope.cadastro.empreendimento.empreendedor.pessoa.enderecos, function(endereco){

						if(endereco.cep)
							endereco.cep = endereco.cep.zeroEsquerda(8);
					});

				} else {
					$scope.cadastro.empreendedorCnpjJaCadastrado = false;
				}

				if (etapaEmpreendedor.eRenovacao()) {

					$scope.cadastro.origemEmpreendedor = $scope.cadastro.origensEmpreendedor.EMPREENDEDOR;
				}

				$scope.cadastro.contatoPrincipalEmpreendedor = getContatoPessoa($scope.cadastro.empreendimento.empreendedor.pessoa.contatos, $scope.cadastro.contatoPrincipalEmpreendedor);
				$scope.cadastro.enderecoEmpreendedor = getEndereco($scope.cadastro.empreendimento.empreendedor.pessoa.enderecos,  $scope.cadastro.enderecoEmpreendedor);
				controlaDisabledCampos();
			})
			.catch(function(error){

				if (error.data && error.data.texto) {

					mensagem.warning(error.data.texto);

				}

			});
			
		});

	}

	function onBeforeUpdateCpfCnpj(cpfCnpj) {

		var deferred = $q.defer();

		if (!$scope.cadastro.empreendimento.empreendedor || !$scope.cadastro.empreendimento.empreendedor.pessoa) {

			deferred.resolve();
			return deferred.promise;

		} else if ((cpfCnpj.isCPF() && cpfCnpj === $scope.cadastro.empreendimento.empreendedor.pessoa.cpf) ||
					(cpfCnpj.isCNPJ() && cpfCnpj === $scope.cadastro.empreendimento.empreendedor.pessoa.cnpj)) {

			deferred.reject();
			return deferred.promise;

		}

		var configModal = {};

		if(!$scope.cadastro.edicao) {
			configModal = {
				titulo: 'Confirmar novo CPF/CNPJ',
				conteudo: 'Ao buscar um novo CPF/CNPJ você perderá todo o cadastro que fez até agora. Você tem certeza disso?'
			};
		}
		else {
			configModal = {
				titulo: 'Confirmar novo CPF/CNPJ',
				conteudo: 'Ao buscar um novo CPF/CNPJ você irá alterar o empreendedor. Você tem certeza disso?'
			};
		}

		var instanciaModal = modalSimplesService.abrirModal(configModal);

		instanciaModal.result.then(
			function() {

				if(!$scope.cadastro.edicao) {
					$scope.cadastro.empreendimento = {
						empreendedor: {},
						responsaveis: []
					};
				}
				else {
					$scope.cadastro.empreendimento.empreendedor = {};
				}

				deferred.resolve();

			},
			function() {
				deferred.reject();
			}
		);

		return deferred.promise;

	}

	function pessoaBuscada(pessoa, cpfCnpj) {
		$scope.$broadcast('limparUf');
		if(!pessoa){

			pessoa = {nova: true};

			/* Caso seja um usuário novo, determinamos o tipo pelo formato do CPF/CNPJ */
			if(cpfCnpj.isCPF()){
				pessoa.type = 'PessoaFisica';
				pessoa.cpf = cpfCnpj;
			}else if(cpfCnpj.isCNPJ()){
				pessoa.type = 'PessoaJuridica';
				pessoa.cnpj = cpfCnpj;
			}

			pessoa.enderecos = [];
			pessoa.contato = {};

			$scope.cadastro.empreendimento.empreendedor.pessoa = pessoa;
		}
	}

	function inicializarEnderecos() {

		_.forEach($scope.cadastro.empreendimento.empreendedor.pessoa.enderecos, function(endereco){

			if(endereco.cep) {

				endereco.cep = endereco.cep.includes("-") ? endereco.cep.replace("-", "") : endereco.cep;
				endereco.cep = endereco.cep.zeroEsquerda(8);			
	
			}
		});

		etapaEmpreendedor.enderecoCorrespondenciaCopiado = false;
	}

	function copiarEnderecoEmpreendedor() {

		if($scope.cadastro.enderecoEmpreendedor.principal &&
			!etapaEmpreendedor.enderecoCorrespondenciaCopiado){

			$scope.cadastro.enderecoEmpreendedor.correspondencia.cep = $scope.cadastro.enderecoEmpreendedor.principal.cep;
			$scope.cadastro.enderecoEmpreendedor.correspondencia.bairro = $scope.cadastro.enderecoEmpreendedor.principal.bairro;
			$scope.cadastro.enderecoEmpreendedor.correspondencia.semNumero = $scope.cadastro.enderecoEmpreendedor.principal.semNumero;
			$scope.cadastro.enderecoEmpreendedor.correspondencia.numero = $scope.cadastro.enderecoEmpreendedor.principal.numero;
			$scope.cadastro.enderecoEmpreendedor.correspondencia.logradouro = $scope.cadastro.enderecoEmpreendedor.principal.logradouro;
			$scope.cadastro.enderecoEmpreendedor.correspondencia.complemento = $scope.cadastro.enderecoEmpreendedor.principal.complemento;
			$scope.cadastro.enderecoEmpreendedor.correspondencia.municipio = $scope.cadastro.enderecoEmpreendedor.principal.municipio;
			
		}
		else {
			
			$scope.cadastro.enderecoEmpreendedor.correspondencia.cep = null;
			$scope.cadastro.enderecoEmpreendedor.correspondencia.bairro = null;
			$scope.cadastro.enderecoEmpreendedor.correspondencia.semNumero = null;
			$scope.cadastro.enderecoEmpreendedor.correspondencia.numero = null;
			$scope.cadastro.enderecoEmpreendedor.correspondencia.logradouro = null;
			$scope.cadastro.enderecoEmpreendedor.correspondencia.complemento = null;
			$scope.cadastro.enderecoEmpreendedor.correspondencia.correspondencia = true;
			$scope.cadastro.enderecoEmpreendedor.correspondencia.municipio = null;
			$scope.$broadcast('refreshMunicipios');
		}

	}

	function anterior() {

		$location.path("/empreendimentos/listagem");
	}

	$scope.$watch('cadastro.empreendimento.empreendedor.pessoa', function(pessoa) {

		if(pessoa){

			etapaEmpreendedor.inicializarEnderecos();

			if(pessoa.dataNascimento){

				if (pessoa.dataNascimento.toDate !== undefined) {
					$scope.cadastro.empreendimento.empreendedor.pessoa.dataNascimento = pessoa.dataNascimento.toDate();
				}

			} else if (pessoa.dataConstituicao) {

				if (pessoa.dataConstituicao.toDate !== undefined) {
					$scope.cadastro.empreendimento.empreendedor.pessoa.dataConstituicao = pessoa.dataConstituicao.toDate();
				}
				
			}

		}

	});

	function novoEmpreendedor() {

		$scope.cadastro.origemEmpreendedor = $scope.cadastro.origensEmpreendedor.OUTRO_CPF_CNPJ;
		$scope.cadastro.empreendimento.empreendedor.pessoa = undefined;
	}

	function eRenovacao() {

		var url = $location.path();

		if(url.indexOf('renovacao') > -1) {

			return true;

		} else {

			return false;
		}
	}

	function getContatoPessoa(listaContatos, contatoPrincipal){

		_.forEach(listaContatos, function (contato){
					
			if(contato.principal === true && contato.tipo.id === etapaEmpreendedor.tipoContato.EMAIL)
				contatoPrincipal.email = contato.valor;
			else if (contato.tipo.id === etapaEmpreendedor.tipoContato.TELEFONE_RESIDENCIAL)
				contatoPrincipal.telefone = contato.valor;
			else if (contato.tipo.id === etapaEmpreendedor.tipoContato.TELEFONE_CELULAR)
				contatoPrincipal.celular = contato.valor;
			 
		});	

		if (contatoPrincipal.email == null) {
			contatoPrincipal.email = "";
		} else if (contatoPrincipal.telefone == null) {
			contatoPrincipal.telefone = "";
		} else if (contatoPrincipal.celular == null) {
			contatoPrincipal.celular = "";
		}

		return contatoPrincipal;
	}

	function getEndereco(listaEnderecos, enderecos) {

		_.forEach(listaEnderecos, function(endereco){

			if(endereco.tipo.id === etapaEmpreendedor.tipoEndereco.PRINCIPAL)
				enderecos.principal = endereco; 
			else
				enderecos.correspondencia = endereco;
		});
		return enderecos;
	}

};

exports.controllers.EtapaEmpreendedorController = EtapaEmpreendedorController;
