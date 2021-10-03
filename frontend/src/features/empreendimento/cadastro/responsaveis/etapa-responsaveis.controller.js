var EtapaResponsaveisController = function($scope, mensagem, modalSimplesService, uploadService, $filter, $q) {

	var etapaResponsaveis = this;

	etapaResponsaveis.passoValido = passoValido;
	etapaResponsaveis.proximo = proximo;
	etapaResponsaveis.cpfBuscado = cpfBuscado;
	etapaResponsaveis.vincularResponsavel = vincularResponsavel;
	etapaResponsaveis.desvincularResponsavel = desvincularResponsavel;
	etapaResponsaveis.iniciarAtualizacaoResponsavel = iniciarAtualizacaoResponsavel;
	etapaResponsaveis.atualizarResponsavel = atualizarResponsavel;
	etapaResponsaveis.confirmarDesvinculacaoResponsavel = confirmarDesvinculacaoResponsavel;
	etapaResponsaveis.selecionarArquivos = selecionarArquivos;
	etapaResponsaveis.removerArquivo = removerArquivo;
	etapaResponsaveis.tiposResponsaveis = app.TIPOS_RESPONSAVEIS;
	etapaResponsaveis.cancelarAlteracao = cancelarAlteracao;
	etapaResponsaveis.copiarEnderecoResponsavel = copiarEnderecoResponsavel;
	etapaResponsaveis.enderecoCorrespondenciaCopiado = false;
	etapaResponsaveis.tipoContato = app.TIPO_CONTATO;
	etapaResponsaveis.tipoEndereco = app.TIPO_ENDERECO;
	etapaResponsaveis.zonaLocalizacao = app.LOCALIZACOES_EMPREENDIMENTO;

	etapaResponsaveis.contatoPrincipalResponsavel = {
		email: null,
		telefone:null,
		celular: null
	};

	etapaResponsaveis.enderecoResponsavel = {
		principal: {},
		correspondencia: {}
	};

	etapaResponsaveis.countResponsaveis = {
		LEGAL: 0,
		TECNICO: 0
	};

	etapaResponsaveis.responsavelAtual = {};

	if(!$scope.cadastro.etapas.RESPONSAVEIS.passoValido){
		$scope.cadastro.etapas.RESPONSAVEIS.passoValido = passoValido;
	}

	var indexResponsavelASerAtualizado,
		indexResponsavelASerDesvinculado,
		empreendedorVerificadoParaVinculacao = false;

	function passoValido() {

		return true;
	}

	function proximo() {

		// Força validação de erros no formulário
		$scope.formResponsaveisTecnicos.$setSubmitted();

		if(etapaResponsaveis.passoValido()){
			validaFormularioResponsavelTecnico();
			$scope.cadastro.proximo();
		}
		else if($scope.cadastro.empreendimento.responsaveis.length === 0){
			validaFormularioResponsavelTecnico();
			mensagem.warning('É necessário que, ao menos, um responsável  seja vinculado.');
		}else if(etapaResponsaveis.countResponsaveis.LEGAL === 0){
			validaFormularioResponsavelTecnico();
			//mensagem.warning('Nenhum responsável legal vinculado ao empreendimento.');
		}

	}

	/**
	 * Caso o empreendimento seja vinculado a um cnpj e o cadastro de responsavel tecnico tenha sido iniciado mas não foi vinculado
	 * ao empreendimento e já existe um responsável legal vinculado, limpa os dados do responsavel tecnico.
	 */
	function validaFormularioResponsavelTecnico() {

		if(etapaResponsaveis.responsavelAtual && etapaResponsaveis.responsavelAtual.tipo === 'TECNICO') {
			etapaResponsaveis.responsavelAtual = {};
		}
	}

	function cpfBuscado(dadosPessoa, cpf) {

		var responsavel = _.find($scope.cadastro.empreendimento.responsaveis, function(responsavel) {
			if(cpf === responsavel.pessoa.cpf){
				return angular.copy(responsavel.pessoa);
			}
		});

		if (responsavel){
			etapaResponsaveis.responsavelAtual = null;
			mensagem.info('Um responsável com o CPF ' + $filter('brCpf')(responsavel.pessoa.cpf) + ' já está vinculado.', {ttl: 12000});
			return;
		}

		// $scope.$broadcast('limparUf');

		/* Se a pessoa veio do banco de dados, não deve ser possível editar seus dados */
		if(dadosPessoa) {
			dadosPessoa.editavel = false;
		}else{
			dadosPessoa = {cpf: cpf, editavel: true};
			$scope.formResponsaveisTecnicos.$setPristine();
		}

		dadosPessoa.excluivel = true;

		etapaResponsaveis.responsavelAtual = {atualizandoResponsavel: false, tipo: etapaResponsaveis.responsavelAtual.tipo};

		// etapaResponsaveis.responsavelAtual.pessoa.enderecos = dadosPessoa.enderecos;

		etapaResponsaveis.responsavelAtual.contato = {};

		etapaResponsaveis.responsavelAtual.documentosRepresentacao = [];

		etapaResponsaveis.responsavelAtual.podeSerEditado = true;
		etapaResponsaveis.responsavelAtual.pessoa = dadosPessoa;
		
		if(etapaResponsaveis.responsavelAtual.pessoa.contatos && etapaResponsaveis.responsavelAtual.pessoa.enderecos){

			etapaResponsaveis.contatoPrincipalResponsavel = angular.copy(getContatoPessoa(etapaResponsaveis.responsavelAtual.pessoa.contatos, etapaResponsaveis.contatoPrincipalResponsavel));
			etapaResponsaveis.enderecoResponsavel = angular.copy(getEndereco(etapaResponsaveis.responsavelAtual.pessoa.enderecos, etapaResponsaveis.enderecoResponsavel));
	
		}else{

			// etapaResponsaveis.responsavelAtual.pessoa = dadosPessoa;
		
			if(etapaResponsaveis.responsavelAtual.pessoa.enderecos === undefined){
				etapaResponsaveis.responsavelAtual.pessoa.enderecos = $scope.etapaResponsaveis.enderecoResponsavel;
				etapaResponsaveis.responsavelAtual.pessoa.enderecos.novoResponsavel = true;
			}

			if(etapaResponsaveis.responsavelAtual.pessoa.contatos === undefined){
				etapaResponsaveis.responsavelAtual.pessoa.contatos = $scope.etapaResponsaveis.contatoPrincipalResponsavel;
				etapaResponsaveis.responsavelAtual.pessoa.contatos.novoResponsavel = true;
			}
		}
		inicializarEnderecos(etapaResponsaveis.enderecoResponsavel);

		
	}

	function inicializarEnderecos(enderecosResponsaveis) {
		
		if (enderecosResponsaveis.principal) {
			if(enderecosResponsaveis.principal.cep) {

				enderecosResponsaveis.principal.cep = enderecosResponsaveis.principal.cep.zeroEsquerda(8);
				enderecosResponsaveis.principal.cep = enderecosResponsaveis.principal.cep.includes("-") ? enderecosResponsaveis.principal.cep.replace("-", "") : enderecosResponsaveis.principal.cep;
	
			}
		}

		if (enderecosResponsaveis.correspondencia) {
			if(enderecosResponsaveis.correspondencia.cep) {

				enderecosResponsaveis.correspondencia.cep = enderecosResponsaveis.correspondencia.cep.zeroEsquerda(8);
				enderecosResponsaveis.correspondencia.cep = enderecosResponsaveis.correspondencia.cep.includes("-") ? enderecosResponsaveis.correspondencia.cep.replace("-", "") : enderecosResponsaveis.correspondencia.cep;
	
			}
		}
		
	}

	function vincularResponsavel() {

		if($scope.formResponsaveisTecnicos.$valid){

			adicionarResponsavel(etapaResponsaveis.responsavelAtual);

			etapaResponsaveis.contatoPrincipalResponsavel.email = null;
			etapaResponsaveis.contatoPrincipalResponsavel.telefone =null;
			etapaResponsaveis.contatoPrincipalResponsavel.celular = null;
				
			etapaResponsaveis.enderecoResponsavel.principal = {};
			etapaResponsaveis.enderecoResponsavel.correspondencia = {};

			etapaResponsaveis.responsavelAtual = {};

			$scope.$broadcast('cleanCpfCnpj');
			$scope.$broadcast('limparUf');

		}else{

			$scope.formResponsaveisTecnicos.$setSubmitted();
			mensagem.warning('Verifique os campos destacados em vermelho para prosseguir com o cadastro.');

		}

	}

	function desvincularResponsavel(indexResponsavel) {

		indexResponsavelASerDesvinculado = indexResponsavel;

		var configModal = {
			titulo: 'Confirmar desvinculação de responsável',
			conteudo: 'Tem certeza que deseja desvincular esse responsável?'
		};

		var instanciaModal = modalSimplesService.abrirModal(configModal);

		instanciaModal.result
			.then(etapaResponsaveis.confirmarDesvinculacaoResponsavel, function(){});

	}

	function confirmarDesvinculacaoResponsavel() {

		$scope.cadastro.empreendimento.responsaveis.splice(indexResponsavelASerDesvinculado, 1);

		mensagem.success('Responsável desvinculado com sucesso');

		indexResponsavelASerDesvinculado = null;

	}

	function iniciarAtualizacaoResponsavel(indexResponsavel) {

		indexResponsavelASerAtualizado = indexResponsavel;

		etapaResponsaveis.atualizandoResponsavel = true;

		var responsavel = $scope.cadastro.empreendimento.responsaveis[indexResponsavel];

		etapaResponsaveis.responsavelAtual = angular.copy(responsavel);

        etapaResponsaveis.contatoPrincipalResponsavel   = etapaResponsaveis.responsavelAtual.pessoa.contatos;
        etapaResponsaveis.enderecoResponsavel.principal = etapaResponsaveis.responsavelAtual.pessoa.enderecos.principal;
        etapaResponsaveis.enderecoResponsavel.correspondencia = etapaResponsaveis.responsavelAtual.pessoa.enderecos.correspondencia;

	}

	function cancelarAlteracao() {

		indexResponsavelASerAtualizado = null;

		etapaResponsaveis.atualizandoResponsavel = false;

		etapaResponsaveis.responsavelAtual = {};

		$scope.$broadcast('cleanCpfCnpj');
		$scope.$broadcast('limparUf');

	}

	function atualizarResponsavel(responsavel) {

		if($scope.formResponsaveisTecnicos.$valid){

			$scope.cadastro.empreendimento.responsaveis.splice(indexResponsavelASerAtualizado, 1, etapaResponsaveis.responsavelAtual);

			indexResponsavelASerAtualizado = null;

			etapaResponsaveis.atualizandoResponsavel = false;

			etapaResponsaveis.responsavelAtual = {};

			$scope.$broadcast('cleanCpfCnpj');
			$scope.$broadcast('limparUf');

			mensagem.success('Responsável atualizado com sucesso');

		}else{

			$scope.formResponsaveisTecnicos.$setSubmitted();

		}
	}

	function adicionarResponsavel(responsavel, silent) {

		var responsavelExistente = _.find($scope.cadastro.empreendimento.responsaveis,
			function(resp){
				return resp.pessoa.cpf === responsavel.pessoa.cpf;
			}
		);
		if(!responsavelExistente) {

			responsavel.contato = responsavel.pessoa.contato;

			if (responsavel.pessoa.cpf) {

				responsavel.pessoa.type = "PessoaFisica";
			} else if (responsavel.pessoa.cnpj) {

				responsavel.pessoa.type = "PessoaJuridica";
			}

			if (!$scope.cadastro.empreendimento.responsaveis) {

				$scope.cadastro.empreendimento.responsaveis = [];
			
			}

			$scope.cadastro.empreendimento.responsaveis.push(angular.copy(responsavel));

			if(!silent){
				mensagem.success("Responsável vinculado com sucesso");
			}

			etapaResponsaveis.countResponsaveis[responsavel.tipo] += 1;

		}
	}

	function selecionarArquivos(files, file) {

		// 2465792 = 2 * 1024 * 1024 = 2MB
		if(file && file.size > 2465792) {

			mensagem.warning('Os arquivos selecionados não devem exceder 2MB.', {ttl: 10000});

			return;

		}

		_.each(files, uploadArquivo);

	}

	function uploadArquivo(arquivo) {

		uploadService.uploadArquivo(arquivo)
			.then(function(response){

				var documentoRepresentacao = {
					key: response.data,
					nome: arquivo.name
				};

				etapaResponsaveis.responsavelAtual.documentosRepresentacao.push(documentoRepresentacao);

			})
			.catch(function(response){
				mensagem.warning(response.data.texto);
				return [];
			});

	}

	function removerArquivo(indexArquivo) {

		etapaResponsaveis.responsavelAtual.documentosRepresentacao.splice(indexArquivo, 1);

	}

	var unregisterWatcher = $scope.$watch("cadastro.etapa", function(etapa) {

		if(etapa && etapa.class === $scope.cadastro.etapas.RESPONSAVEIS.class &&
			!empreendedorVerificadoParaVinculacao){

			empreendedorVerificadoParaVinculacao = true;
		}
	});

	function copiarEnderecoResponsavel() {

		etapaResponsaveis.responsavelAtual.enderecoCorrespondenciaCopiado  = !etapaResponsaveis.responsavelAtual.enderecoCorrespondenciaCopiado;

		if(etapaResponsaveis.responsavelAtual.enderecoCorrespondenciaCopiado && etapaResponsaveis.responsavelAtual.pessoa.enderecos && etapaResponsaveis.enderecoResponsavel.principal){

			etapaResponsaveis.enderecoResponsavel.correspondencia = {};
			etapaResponsaveis.enderecoResponsavel.correspondencia.cep = etapaResponsaveis.enderecoResponsavel.principal.cep.zeroEsquerda(8);
			etapaResponsaveis.enderecoResponsavel.correspondencia.bairro = etapaResponsaveis.enderecoResponsavel.principal.bairro;
			etapaResponsaveis.enderecoResponsavel.correspondencia.semNumero = etapaResponsaveis.enderecoResponsavel.principal.semNumero;
			etapaResponsaveis.enderecoResponsavel.correspondencia.numero = etapaResponsaveis.enderecoResponsavel.principal.numero;
			etapaResponsaveis.enderecoResponsavel.correspondencia.logradouro = etapaResponsaveis.enderecoResponsavel.principal.logradouro;
			etapaResponsaveis.enderecoResponsavel.correspondencia.complemento = etapaResponsaveis.enderecoResponsavel.principal.complemento;
			etapaResponsaveis.enderecoResponsavel.correspondencia.correspondencia = true;
			etapaResponsaveis.enderecoResponsavel.correspondencia.tipo = etapaResponsaveis.enderecoResponsavel.principal.tipo;

			if (etapaResponsaveis.enderecoResponsavel.principal.municipio) {

				etapaResponsaveis.enderecoResponsavel.correspondencia.municipio = {};
				angular.copy(etapaResponsaveis.enderecoResponsavel.principal.municipio, etapaResponsaveis.enderecoResponsavel.correspondencia.municipio);
				$scope.$broadcast('refreshMunicipios');

			} 
		}
		
		else if(!etapaResponsaveis.responsavelAtual.enderecoCorrespondenciaCopiado){

			etapaResponsaveis.enderecoResponsavel.correspondencia = {};
			etapaResponsaveis.enderecoResponsavel.correspondencia.cep = null;
			etapaResponsaveis.enderecoResponsavel.correspondencia.bairro = null;
			etapaResponsaveis.enderecoResponsavel.correspondencia.semNumero = null;
			etapaResponsaveis.enderecoResponsavel.correspondencia.numero = null;
			etapaResponsaveis.enderecoResponsavel.correspondencia.logradouro = null;
			etapaResponsaveis.enderecoResponsavel.correspondencia.complemento = null;
			etapaResponsaveis.enderecoResponsavel.correspondencia.correspondencia = true;
			etapaResponsaveis.enderecoResponsavel.correspondencia.tipo = null;

			if (etapaResponsaveis.enderecoResponsavel.principal) {

				etapaResponsaveis.enderecoResponsavel.correspondencia.municipio = {};
				angular.copy(etapaResponsaveis.enderecoResponsavel.principal.municipio, etapaResponsaveis.enderecoResponsavel.correspondencia.municipio);
				etapaResponsaveis.enderecoResponsavel.correspondencia.municipio.estado.sigla = '';
				etapaResponsaveis.enderecoResponsavel.correspondencia.municipio = {};
				$scope.$broadcast('refreshMunicipios');

			}
		}
	}

	function getContatoPessoa(listaContatos, contatoPrincipal){

		_.forEach(listaContatos, function (contato){
					
			if(contato.principal === true && contato.tipo.id === etapaResponsaveis.tipoContato.EMAIL)
				contatoPrincipal.email = contato.valor;
			else if (contato.tipo.id === etapaResponsaveis.tipoContato.TELEFONE_RESIDENCIAL)
				contatoPrincipal.telefone = contato.valor;
			else if (contato.tipo.id === etapaResponsaveis.tipoContato.TELEFONE_CELULAR)
				contatoPrincipal.celular = contato.valor;

		});	
		return angular.copy(contatoPrincipal);
	}

	function getEndereco(listaEnderecos, enderecos) {

		_.forEach(listaEnderecos, function(endereco){

			if(endereco.tipo.id === etapaResponsaveis.tipoEndereco.PRINCIPAL)
				enderecos.principal = endereco; 
			else
				enderecos.correspondencia = endereco;
		});
		return angular.copy(enderecos);
	}

};

exports.controllers.EtapaResponsaveisController = EtapaResponsaveisController;
