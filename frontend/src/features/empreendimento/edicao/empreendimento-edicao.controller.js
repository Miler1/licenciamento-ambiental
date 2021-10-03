var EditarEmpreendimentos = function($scope, breadcrumb, $route,  tipoResponsavelEmpreendimentoService, growlMessages, $injector, getEmpreendimento, $rootScope) {

	// Criando extensão da controller CadastrarEmpreendimentos
    $injector.invoke(exports.controllers.CadastrarEmpreendimentos, this,
        {
            $scope: $scope,
            breadcrumb: breadcrumb,
            $route: $route,
			growlMessages: growlMessages
        }
	);

	var cadastro = this;

	cadastro.edicao = true;
	cadastro.tipoPessoa = app.TIPO_PESSOA;
	cadastro.zonaLocalizacao = app.LOCALIZACOES_EMPREENDIMENTO;
	cadastro.tipoContato = app.TIPO_CONTATO;
	cadastro.tipoEndereco = app.TIPO_ENDERECO;
	cadastro.tiposResponsaveis = app.TIPOS_RESPONSAVEIS;
	cadastro.getResponsaveisLegaisETecnicos = getResponsaveisLegaisETecnicos;
	cadastro.tiposResponsavelDisponiveis = app.TIPOS_RESPONSAVEIS;

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

	cadastro.contatoPrincipalRepresentanteLegal = {
		email: null,
		telefone:null,
		celular: null
	};

	cadastro.contatoPrincipalResponsavel = {
		email: null,
		telefone:null,
		celular: null
	};

	if (getEmpreendimento) {
		cadastro.cpfCnpjPesquisado = true;
		cadastro.empreendimento = getEmpreendimento.data.empreendimentoEU;
		cadastro.empreendimento.id = getEmpreendimento.data.id;
		cadastro.empreendimento.municipio = getEmpreendimento.data.municipio;
		cadastro.empreendimento.coordenadas = getEmpreendimento.data.empreendimentoEU.localizacao.geometria;
		cadastro.empreendimento.localizacao = getLocalizacao(getEmpreendimento.data.empreendimentoEU);
		cadastro.empreendimento.jurisdicao = getEmpreendimento.data.jurisdicao;
		cadastro.empreendimento.municipio = getEmpreendimento.data.municipio;
		cadastro.empreendimento.empreendimentoEU = getEmpreendimento.data.empreendimentoEU;
		
		tipoResponsavelEmpreendimentoService.list()
			.then(function(response){
				cadastro.tiposResponsavelDisponiveis = response.data;
		});

		setEmpreendimento(cadastro.empreendimento);

	}

	function getContatoPessoa(listaContatos, contatoPrincipal){

		_.forEach(listaContatos, function (contato){
					
			if(contato.principal === true && contato.tipo.id === cadastro.tipoContato.EMAIL)
				contatoPrincipal.email = contato.valor;
			else if (contato.tipo.id === cadastro.tipoContato.TELEFONE_RESIDENCIAL)
				contatoPrincipal.telefone = contato.valor;
			else if (contato.tipo.id === cadastro.tipoContato.TELEFONE_CELULAR)
				contatoPrincipal.celular = contato.valor;
			 
		});	

		if (contatoPrincipal.email == null) {
			contatoPrincipal.email = "";
		}
		if (contatoPrincipal.telefone == null) {
			contatoPrincipal.telefone = "";
		} 
		if (contatoPrincipal.celular == null) {
			contatoPrincipal.celular = "";
		}

		return contatoPrincipal;
	}

	function getEndereco(listaEnderecos, enderecos) {

		_.forEach(listaEnderecos, function(endereco){

			if(endereco.tipo.id === cadastro.tipoEndereco.PRINCIPAL){
				enderecos.principal = endereco;
			}
			else{
				enderecos.correspondencia = endereco;
			}
		});
		return enderecos;
	}

	function getLocalizacao(empreendimento){

		var localizacao = null;

		_.forEach(empreendimento.enderecos, function(endereco){
			if(endereco.tipo.id === cadastro.tipoEndereco.PRINCIPAL){
				localizacao =  endereco.zonaLocalizacao.codigo === cadastro.zonaLocalizacao.URBANA ? "ZONA_URBANA" : "ZONA_RURAL";
			}
		});

		return localizacao;
	}

	function setEmpreendimento(empreendimento) {

		//Seleciona a opção de sem número dos endereços do empreendimento
		setEnderecos(empreendimento.enderecos);
		getContatoPessoa(empreendimento.contatos, cadastro.contatoPrincipalEmpreendimento);
		getEndereco(empreendimento.enderecos ,cadastro.enderecoEmpreendimento);

		//Seleciona a opção de sem número dos endereços do empreendedor
		setEnderecos(empreendimento.empreendedor.pessoa.enderecos);
		getContatoPessoa(empreendimento.empreendedor.pessoa.contatos, cadastro.contatoPrincipalEmpreendedor);
		getEndereco(empreendimento.empreendedor.pessoa.enderecos ,cadastro.enderecoEmpreendedor);

		setProprietarios(empreendimento.proprietarios);

		setRepresentantes(empreendimento.representantesLegais);

		setResponsaveis(empreendimento);

		configurarDatas(empreendimento);
	}

	function configurarDatas(empreendimento) {

		configurarDatasPessoa(empreendimento.pessoa);
		configurarDatasPessoa(empreendimento.empreendedor.pessoa);

		if (empreendimento.responsaveisLegais) {
			_.forEach(empreendimento.responsaveisLegais, function(responsavelLegal){
				configurarDatasPessoa(responsavelLegal);
			});
		}

		if(empreendimento.responsaveisTecnicos) {
			_.forEach(empreendimento.responsaveisTecnicos, function(responsavelTecnico){
				configurarDatasPessoa(responsavelTecnico);
			});
		}
	}

	function configurarDatasPessoa(pessoa) {

		if(pessoa.dataNascimento)
			pessoa.dataNascimento = pessoa.dataNascimento.toDate();

		if (pessoa.dataConstituicao)
			pessoa.dataConstituicao = pessoa.dataConstituicao.toDate();
	}

	function setEnderecos(enderecos) {

		if (!enderecos) return;

		_.forEach(enderecos, function(endereco){

			setEndereco(endereco);
		});
	}

	function setEndereco(endereco) {

		if (endereco.zonaLocalizacao.codigo === cadastro.zonaLocalizacao.URBANA) {

			endereco.semNumero = !endereco.numero;
			
			if (endereco.cep) {

				endereco.cep = endereco.cep.zeroEsquerda(8);
			}
		}
	}

	// function setEnderecoResponsaveis(responsaveis) {

	// 	_.forEach(responsaveis, function(responsavel){

	// 		setEndereco(responsavel.enderecoCorrespondencia);

	// 		//obtendo o nome dos documentos
	// 		_.forEach(responsavel.documentosRepresentacao, function(documento) {

	// 			documento.nome = documento.caminho.split('/').pop();
	// 		});
	// 	});
	// }

	function setRepresentantes(representantes) {

		var listaRepresentantes = [];

		// _.forEach(cadastro.empreendimento.representantesLegais, function (rep){

			_.forEach(representantes, function(representante){
				var rep = null;
				representante.podeSerDesvinculado = true;

				if ($rootScope.usuarioSessao.login.isCPF() && representante.cpf === $rootScope.usuarioSessao.login)
					representante.podeSerDesvinculado = false;
				else if (representante.cnpj === $rootScope.usuarioSessao.login)
					representante.podeSerDesvinculado = false;
				
				// if(rep.cpf === representante.cpf || rep.cnpj === representante.cnpj){

					representante.contato = getContatoPessoa(representante.contatos, cadastro.contatoPrincipalRepresentanteLegal);
					representante.editavel = false;
					rep = {
						pessoa: representante
					};
					
					listaRepresentantes.push(rep);
				// }
			});
		// });

		cadastro.empreendimento.representantesLegais = listaRepresentantes;
		
	}

	function getResponsaveisLegaisETecnicos (empreendimento){

		var listaResponsaveisLegaisETecnicos = [];

		_.forEach(empreendimento.responsaveisTecnicos, function(responsavelTecnico){
			
			var responsavelFormatado = {
				tipo: null,
				pessoa: null
			};

			responsavelFormatado.tipo = cadastro.tiposResponsaveis.TECNICO;
			responsavelFormatado.pessoa = responsavelTecnico;

			listaResponsaveisLegaisETecnicos.push(responsavelFormatado);

		});

		_.forEach(empreendimento.responsaveisLegais, function(responsavelLegal){
			
			var responsavelFormatado = {
				tipo: null,
				pessoa: null
			};

			responsavelFormatado.tipo = cadastro.tiposResponsaveis.LEGAL;
			responsavelFormatado.pessoa = responsavelLegal;
			
			listaResponsaveisLegaisETecnicos.push(responsavelFormatado);

		});

		return listaResponsaveisLegaisETecnicos;
	}

	function setResponsaveis(empreendimento) {

		var listaResponsaveis = getResponsaveisLegaisETecnicos(empreendimento);
		var listaResponsaveisTecLeg = [];

			_.forEach(listaResponsaveis, function(responsavel){

				responsavel.podeSerEditado = !(empreendimento.empreendedor.pessoa.tipo.codigo === cadastro.tipoPessoa.PESSOA_FISICA &&
					empreendimento.empreendedor.pessoa.cpf === responsavel.cpf);

					responsavel.pessoa.contato = getContatoPessoa(responsavel.pessoa.contatos, cadastro.contatoPrincipalResponsavel);

					responsavel.editavel = false;

						listaResponsaveisTecLeg.push(responsavel);					

			});

		cadastro.empreendimento.responsaveis = listaResponsaveisTecLeg;

	}

	function setProprietarios(proprietarios) {

		var listaProprietarios = [];

		// _.forEach(cadastro.empreendimento.proprietarios, function(prop){
			
			_.forEach(proprietarios, function(proprietario){

				// if(prop.cpf === proprietario.cpf || prop.cnpj === proprietario.cnpj){

					proprietario.editavel = false;
					var prop = {
						pessoa: proprietario
					};
					listaProprietarios.push(prop);

				// }

			// });

		});

		cadastro.empreendimento.proprietarios = listaProprietarios;
	}

	function novoEmpreendimento() {

		window.location.href = "#/empreendimentos/cadastrar?origem=outro";
	}

	function eRenovacao() {

		var url = $location.path();
		return url.indexOf('renovacao') > -1;
	}

};

exports.controllers.EditarEmpreendimentos = EditarEmpreendimentos;
