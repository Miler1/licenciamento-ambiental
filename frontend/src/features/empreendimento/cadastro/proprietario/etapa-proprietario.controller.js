var EtapaProprietarioController = function($scope, mensagem, modalSimplesService) {

	var etapaProprietarios = this;

	etapaProprietarios.passoValido = passoValido;
	etapaProprietarios.anterior = anterior;
	etapaProprietarios.proximo = proximo;
	etapaProprietarios.pessoaBuscada = pessoaBuscada;
	etapaProprietarios.bloqueaCNPJ = bloqueaCNPJ;
	etapaProprietarios.vincularProprietario = vincularProprietario;
	etapaProprietarios.desvincularProprietario = desvincularProprietario;
	etapaProprietarios.confirmarDesvinculacaoProprietario = confirmarDesvinculacaoProprietario;
	etapaProprietarios.iniciarAtualizacaoProprietario = iniciarAtualizacaoProprietario;
	etapaProprietarios.atualizarProprietario = atualizarProprietario;
	etapaProprietarios.copiarEnderecoProprietario = copiarEnderecoProprietario;
	etapaProprietarios.cancelarAlteracao = cancelarAlteracao;
	etapaProprietarios.isPessoaFisica = isPessoaFisica;
	etapaProprietarios.isPessoaJuridica = isPessoaJuridica;
	etapaProprietarios.enderecoCorrespondenciaCopiado = false;
	etapaProprietarios.tipoPessoa = app.TIPO_PESSOA;
	etapaProprietarios.tipoContato = app.TIPO_CONTATO;
	etapaProprietarios.tipoEndereco = app.TIPO_ENDERECO;
	etapaProprietarios.localizacoesEmpreendimento = app.LOCALIZACOES_EMPREENDIMENTO;

	etapaProprietarios.contatoPrincipalProprietario = {
		email: null,
		telefone:null,
		celular: null
	};

	etapaProprietarios.enderecoProprietario = {
		principal: {},
		correspondencia: {}
	};

	etapaProprietarios.proprietarioAtual = {};

	if(!$scope.cadastro.etapas.PROPRIETARIO.passoValido){
		$scope.cadastro.etapas.PROPRIETARIO.passoValido = passoValido;
	}

	var indexProprietarioASerAtualizado,
		indexProprietarioSerDesvinculado;

	function passoValido() {

		return true;

	}

	function anterior() {

		$scope.cadastro.anterior();
	}

	function isPessoaFisica() {
		return etapaProprietarios.proprietarioAtual &&
				etapaProprietarios.proprietarioAtual.pessoa && 
				etapaProprietarios.proprietarioAtual.pessoa.tipo && 
				etapaProprietarios.proprietarioAtual.pessoa.tipo.codigo === etapaProprietarios.tipoPessoa.PESSOA_FISICA;
	}

	function isPessoaJuridica() {
		return etapaProprietarios.proprietarioAtual &&
				etapaProprietarios.proprietarioAtual.pessoa &&
				etapaProprietarios.proprietarioAtual.pessoa.tipo &&
				etapaProprietarios.proprietarioAtual.pessoa.tipo.codigo === etapaProprietarios.tipoPessoa.PESSOA_JURIDICA;
	}

	function proximo() {
		if(etapaProprietarios.passoValido()){
			$scope.cadastro.proximo();
		}else{
			if($scope.cadastro.empreendimento.empreendedor.pessoa.cpf){
				$scope.cadastro.proximo();
			}
			mensagem.warning("É necessário que, ao menos, um proprietário seja vinculado.");
		}
	}

	function bloqueaCNPJ() {

		//Bloquea os campos do empreendimento ja cadastrado... CNPJ.
		return etapaProprietarios.isPessoaJuridica();
	}

	function pessoaBuscada(dadosPessoa, cpfCnpj) {

		var proprietario = _.find($scope.cadastro.empreendimento.proprietarios, function(proprietario) {
			if(cpfCnpj === proprietario.pessoa.cpf || cpfCnpj === proprietario.pessoa.cnpj){
				return angular.copy(proprietario.pessoa);
			}
		});

		if (proprietario) {
			etapaProprietarios.proprietarioAtual.pessoa = null;
			mensagem.info('Um proprietário com este CPF/CNPJ ' + cpfCnpj + ' já está vinculado.', {ttl: 12000});

			return;
		}

		$scope.$broadcast('limparUf');

		/* Se a pessoa veio do banco de dados, não deve ser possível editar seus dados */
		if(dadosPessoa) {

			dadosPessoa.editavel = false;

		} else {

			dadosPessoa = {editavel: true};
			if(cpfCnpj.length === 11) {
				dadosPessoa.cpf = cpfCnpj;

				// dadosPessoa.dataNascimento = dadosPessoa.dataNascimento.toDate();

				dadosPessoa.tipo= {
					codigo: etapaProprietarios.tipoPessoa.PESSOA_FISICA
				};
			}
			else if(cpfCnpj.length === 14) {
				dadosPessoa.cnpj = cpfCnpj;

				// dadosPessoa.dataConstituicao = dadosPessoa.dataConstituicao.toDate();
				
				dadosPessoa.tipo = {
					codigo: etapaProprietarios.tipoPessoa.PESSOA_JURIDICA
				};
			}
			$scope.formProprietarios.$setPristine();
			$scope.formProprietarios.$setUntouched();
		}

		dadosPessoa.excluivel = true;

		//etapaProprietarios.proprietarioAtual.pessoa = dadosPessoa;
		//etapaProprietarios.proprietarioAtual.pessoa.enderecos = dadosPessoa.enderecos;
		if (etapaProprietarios.proprietarioAtual.pessoa){
			etapaProprietarios.contatoPrincipalProprietario = angular.copy(getContatoPessoa(etapaProprietarios.proprietarioAtual.pessoa.contatos, etapaProprietarios.contatoPrincipalProprietario));
			etapaProprietarios.enderecoProprietario = angular.copy(getEndereco(etapaProprietarios.proprietarioAtual.pessoa.enderecos, etapaProprietarios.enderecoProprietario));
		}else{
			etapaProprietarios.proprietarioAtual.pessoa = dadosPessoa;
			
			if(etapaProprietarios.proprietarioAtual.pessoa.enderecos === undefined){
				etapaProprietarios.proprietarioAtual.pessoa.enderecos = $scope.etapaProprietarios.enderecoProprietario;
				etapaProprietarios.proprietarioAtual.pessoa.enderecos.novoProprietario = true;
			}

			if(etapaProprietarios.proprietarioAtual.pessoa.contatos === undefined){
				etapaProprietarios.proprietarioAtual.pessoa.contatos = $scope.etapaProprietarios.contatoPrincipalProprietario;
				etapaProprietarios.proprietarioAtual.pessoa.contatos.novoProprietario = true;
			}
		}

		inicializarEnderecos(etapaProprietarios.enderecoProprietario);

	}

	function vincularProprietario() {

		if($scope.formProprietarios.$valid){

			adicionarProprietario(etapaProprietarios.proprietarioAtual);

			etapaProprietarios.contatoPrincipalProprietario.email = null;
			etapaProprietarios.contatoPrincipalProprietario.telefone =null;
			etapaProprietarios.contatoPrincipalProprietario.celular = null;
				
			etapaProprietarios.enderecoProprietario.principal = {};
			etapaProprietarios.enderecoProprietario.correspondencia = {};

			etapaProprietarios.proprietarioAtual = {};

			$scope.$broadcast('cleanCpfCnpj');
			$scope.$broadcast('limparUf');

		}else{

			$scope.formProprietarios.$setSubmitted();
			mensagem.warning('Verifique os campos destacados em vermelho para prosseguir com o cadastro.');

		}

	}

	function inicializarEnderecos(enderecosProprietario) {
		
		if (enderecosProprietario.principal) {
			if(enderecosProprietario.principal.cep) {

				enderecosProprietario.principal.cep = enderecosProprietario.principal.cep.includes("-") ? enderecosProprietario.principal.cep.replace("-", "") : enderecosProprietario.principal.cep;
				enderecosProprietario.principal.cep = enderecosProprietario.principal.cep.zeroEsquerda(8);			
	
			}
		}

		if (enderecosProprietario.correspondencia) {
			if(enderecosProprietario.correspondencia.cep) {

				enderecosProprietario.correspondencia.cep = enderecosProprietario.correspondencia.cep.includes("-") ? enderecosProprietario.correspondencia.cep.replace("-", "") : enderecosProprietario.correspondencia.cep;
				enderecosProprietario.correspondencia.cep = enderecosProprietario.correspondencia.cep.zeroEsquerda(8);
	
			}
		}
		
	}

	function confirmarDesvinculacaoProprietario() {

		var representantesLegaisTemp = $scope.cadastro.empreendimento.representantesLegais;

		var representanteIndex = _.findIndex(representantesLegaisTemp,
			function(element) {
				return element.pessoa.cpf === $scope.cadastro.empreendimento.proprietarios[indexProprietarioSerDesvinculado].pessoa.cpf;
			}
		);

		if(representanteIndex != -1 && representantesLegaisTemp[representanteIndex] &&
					representantesLegaisTemp[representanteIndex].podeSerDesvinculado)
			$scope.cadastro.empreendimento.representantesLegais.splice(representanteIndex, 1);
		else if(representanteIndex != -1 && representantesLegaisTemp[representanteIndex] &&
					!representantesLegaisTemp[representanteIndex].podeSerDesvinculado)
			$scope.cadastro.empreendimento.representantesLegais[representanteIndex].tipo = app.TIPOS_REPRESENTANTE_LEGAL.REPRESENTANTE;


		$scope.cadastro.empreendimento.proprietarios.splice(indexProprietarioSerDesvinculado, 1);

		mensagem.success('Proprietário desvinculado com sucesso');

		indexProprietarioSerDesvinculado = null;

	}

	function desvincularProprietario(indexProprietario) {

		indexProprietarioSerDesvinculado = indexProprietario;
		

		var configModal = {
			titulo: 'Confirmar desvinculação de proprietário',
			conteudo: 'Tem certeza que deseja desvincular esse proprietário?'
		};

		var instanciaModal = modalSimplesService.abrirModal(configModal);

		instanciaModal.result
			.then(etapaProprietarios.confirmarDesvinculacaoProprietario, function removerProprietarui(){});

	}

	function adicionarProprietario(proprietario, silent) {

		var proprietarioExistente = _.find($scope.cadastro.empreendimento.proprietarios,
			function(prop){
				if(prop.pessoa.cpf) {
					return prop.pessoa.cpf === proprietario.pessoa.cpf;
				}
				else {
					return prop.pessoa.cnpj === proprietario.pessoa.cnpj;
				}
			}
		);

		if(!proprietarioExistente) {

			if(!$scope.cadastro.empreendimento.proprietarios){ 

				$scope.cadastro.empreendimento.proprietarios = [];
			
			}

			if (proprietario.pessoa.cpf) {

				proprietario.pessoa.tipo.codigo = etapaProprietarios.tipoPessoa.PESSOA_FISICA;
			} else if (proprietario.pessoa.cnpj) {

				proprietario.pessoa.tipo.codigo = etapaProprietarios.tipoPessoa.PESSOA_JURIDICA;
			}

			$scope.cadastro.empreendimento.proprietarios.push(angular.copy(proprietario));

			if(!silent){
				mensagem.success("Proprietário vinculado com sucesso");
			}

		} else {

			var cpfCnpj = proprietario.pessoa.cpf ? proprietario.pessoa.cpf : proprietario.pessoa.cnpj;
			mensagem.info('Um proprietário com o CPF ' + proprietario.pessoa.cpf + ' já está vinculado.', {ttl: 12000});
		}

	}

	function iniciarAtualizacaoProprietario(indexProprietario) {

		indexProprietarioASerAtualizado = indexProprietario;

		etapaProprietarios.atualizandoProprietario = true;

		var proprietario = $scope.cadastro.empreendimento.proprietarios[indexProprietario];

		etapaProprietarios.proprietarioAtual = angular.copy(proprietario);

        etapaProprietarios.contatoPrincipalProprietario = etapaProprietarios.proprietarioAtual.pessoa.contatos;
        etapaProprietarios.enderecoProprietario.principal = etapaProprietarios.proprietarioAtual.pessoa.enderecos.principal;
        etapaProprietarios.enderecoProprietario.correspondencia = etapaProprietarios.proprietarioAtual.pessoa.enderecos.correspondencia;

    }

	function cancelarAlteracao() {

		indexProprietarioASerAtualizado = null;

		etapaProprietarios.atualizandoProprietario = false;

		etapaProprietarios.proprietarioAtual = {};

		$scope.$broadcast('cleanCpfCnpj');
		$scope.$broadcast('limparUf');

	}

	function atualizarProprietario() {

		$scope.cadastro.empreendimento.proprietarios.splice(indexProprietarioASerAtualizado, 1, etapaProprietarios.proprietarioAtual);

		indexProprietarioASerAtualizado = undefined;

		etapaProprietarios.atualizandoProprietario = false;

		etapaProprietarios.proprietarioAtual = {};

		mensagem.success('Proprietário atualizado com sucesso');

	}

	function copiarEnderecoProprietario() {

		etapaProprietarios.proprietarioAtual.enderecoCorrespondenciaCopiado  = !etapaProprietarios.proprietarioAtual.enderecoCorrespondenciaCopiado;

		if(etapaProprietarios.proprietarioAtual.enderecoCorrespondenciaCopiado && etapaProprietarios.proprietarioAtual.pessoa.enderecos && etapaProprietarios.enderecoProprietario.principal){

			etapaProprietarios.enderecoProprietario.correspondencia = {};
			etapaProprietarios.enderecoProprietario.correspondencia.cep = etapaProprietarios.enderecoProprietario.principal.cep.zeroEsquerda(8);
			etapaProprietarios.enderecoProprietario.correspondencia.bairro = etapaProprietarios.enderecoProprietario.principal.bairro;
			etapaProprietarios.enderecoProprietario.correspondencia.semNumero = etapaProprietarios.enderecoProprietario.principal.semNumero;
			etapaProprietarios.enderecoProprietario.correspondencia.numero = etapaProprietarios.enderecoProprietario.principal.numero;
			etapaProprietarios.enderecoProprietario.correspondencia.logradouro = etapaProprietarios.enderecoProprietario.principal.logradouro;
			etapaProprietarios.enderecoProprietario.correspondencia.complemento = etapaProprietarios.enderecoProprietario.principal.complemento;
			etapaProprietarios.enderecoProprietario.correspondencia.correspondencia = true;
			etapaProprietarios.enderecoProprietario.correspondencia.tipo = etapaProprietarios.enderecoProprietario.principal.tipo;

			if (etapaProprietarios.enderecoProprietario.principal.municipio) {

				etapaProprietarios.enderecoProprietario.correspondencia.municipio = {};
				angular.copy(etapaProprietarios.enderecoProprietario.principal.municipio, etapaProprietarios.enderecoProprietario.correspondencia.municipio);
				$scope.$broadcast('refreshMunicipios');

			}
		}

		else if(!etapaProprietarios.proprietarioAtual.enderecoCorrespondenciaCopiado){

			etapaProprietarios.enderecoProprietario.correspondencia = {};
			etapaProprietarios.enderecoProprietario.correspondencia.cep = null;
			etapaProprietarios.enderecoProprietario.correspondencia.bairro = null;
			etapaProprietarios.enderecoProprietario.correspondencia.semNumero = null;
			etapaProprietarios.enderecoProprietario.correspondencia.numero = null;
			etapaProprietarios.enderecoProprietario.correspondencia.logradouro = null;
			etapaProprietarios.enderecoProprietario.correspondencia.complemento = null;
			etapaProprietarios.enderecoProprietario.correspondencia.correspondencia = true;
			etapaProprietarios.enderecoProprietario.correspondencia.tipo = null;

			if (etapaProprietarios.enderecoProprietario.principal) {

				etapaProprietarios.enderecoProprietario.correspondencia.municipio = {};
				angular.copy(etapaProprietarios.enderecoProprietario.principal.municipio, etapaProprietarios.enderecoProprietario.correspondencia.municipio);
				etapaProprietarios.enderecoProprietario.correspondencia.municipio.estado.sigla = '';
				etapaProprietarios.enderecoProprietario.correspondencia.municipio = {};
				$scope.$broadcast('refreshMunicipios');

			}
		}
	}

	function getContatoPessoa(listaContatos, contatoPrincipal){

		_.forEach(listaContatos, function (contato){
					
			if(contato.principal === true && contato.tipo.id === etapaProprietarios.tipoContato.EMAIL)
				contatoPrincipal.email = contato.valor;
			else if (contato.tipo.id === etapaProprietarios.tipoContato.TELEFONE_RESIDENCIAL)
				contatoPrincipal.telefone = contato.valor;
			else if (contato.tipo.id === etapaProprietarios.tipoContato.TELEFONE_CELULAR)
				contatoPrincipal.celular = contato.valor;
			 
		});	
		return angular.copy(contatoPrincipal);
	}

	function getEndereco(listaEnderecos, enderecos) {

		_.forEach(listaEnderecos, function(endereco){

			if(endereco.tipo.id === etapaProprietarios.tipoEndereco.PRINCIPAL)
				enderecos.principal = endereco; 
			else
				enderecos.correspondencia = endereco;
		});

		return angular.copy(enderecos);
	}
	
};

exports.controllers.EtapaProprietarioController = EtapaProprietarioController;
