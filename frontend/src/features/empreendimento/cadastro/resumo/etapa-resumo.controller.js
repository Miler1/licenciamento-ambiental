var EtapaResumoController = function($scope, $rootScope, $location, empreendimentoService, mensagem) {

	var etapaResumo = this;

	etapaResumo.concluir = concluir;
	etapaResumo.tipoContato = app.TIPO_CONTATO;
	etapaResumo.tipoEndereco = app.TIPO_ENDERECO;
	etapaResumo.zonaLocalizacaoEmp = app.LOCALIZACOES_EMPREENDIMENTO;
	etapaResumo.tiposResponsaveis = app.TIPOS_RESPONSAVEIS;
	etapaResumo.enderecosEmpreendimento = [];
	etapaResumo.tipoPessoa = app.TIPO_PESSOA;
	etapaResumo.estadoCivil = app.ESTADO_CIVIL_EU;
	etapaResumo.responsaveis = {
		legais: [],
		tecnicos:[]
	};
	etapaResumo.representantesLegais = [];
	etapaResumo.proprietarios = [];

	function getEtapaEmpreendedor () {

		let estadoEmpreendedor = {
			principal: {
				sigla:null
			},
			correspondencia: {
				sigla: null
			}
		};

		estadoEmpreendedor.principal.sigla = $scope.cadastro.empreendimento.empreendedor.pessoa.enderecos[0].municipio.estado.codigo;
		estadoEmpreendedor.correspondencia.sigla = $scope.cadastro.empreendimento.empreendedor.pessoa.enderecos[1].municipio.estado.codigo;
		$scope.cadastro.empreendimento.empreendedor.pessoa.enderecos[0].municipio.estado = estadoEmpreendedor.principal;
		$scope.cadastro.empreendimento.empreendedor.pessoa.enderecos[1].municipio.estado = estadoEmpreendedor.correspondencia;

	}

	function getEtapaEmpreendimento () {

		let estadoEnderecoPrincipal = {
			sigla: null,
			nome: null,
			id: null
		};

		let estadoEnderecoCorrespondencia = {
			sigla: null,
			nome: null,
			id: null
		};

		let zonaLocalizacaoPrincipal = {
			codigo:null
		};

		let zonaLocalizacaoCorrespondencia = {
			codigo:null
		};

		let tipoEnderecoPrincipal = {
			id:null
		};

		let tipoEnderecoCorrespondencia = {
			id:null
		};

		zonaLocalizacaoPrincipal.codigo = $scope.cadastro.empreendimento.enderecos[0].tipo === 'ZONA_URBANA' ? etapaResumo.zonaLocalizacaoEmp.URBANA : etapaResumo.zonaLocalizacaoEmp.RURAL;
		
		if (zonaLocalizacaoPrincipal.codigo === etapaResumo.zonaLocalizacaoEmp.RURAL) {
			$scope.cadastro.enderecoEmpreendimento.principal.bairro = null;
			$scope.cadastro.enderecoEmpreendimento.principal.cep = null;
			$scope.cadastro.enderecoEmpreendimento.principal.logradouro = null;
			$scope.cadastro.enderecoEmpreendimento.principal.numero = null;
			$scope.cadastro.enderecoEmpreendimento.principal.semNumero = null;
		}
		
		tipoEnderecoPrincipal.id = etapaResumo.tipoEndereco.PRINCIPAL;
		$scope.cadastro.empreendimento.enderecos[0].zonaLocalizacao = zonaLocalizacaoPrincipal;
		$scope.cadastro.empreendimento.enderecos[0].tipo = tipoEnderecoPrincipal;

		zonaLocalizacaoCorrespondencia.codigo = $scope.cadastro.empreendimento.enderecos[1].tipo === 'ZONA_URBANA' ? etapaResumo.zonaLocalizacaoEmp.URBANA : etapaResumo.zonaLocalizacaoEmp.RURAL;
		tipoEnderecoCorrespondencia.id = etapaResumo.tipoEndereco.CORRESPONDENCIA;
		$scope.cadastro.empreendimento.enderecos[1].zonaLocalizacao = zonaLocalizacaoPrincipal;
		$scope.cadastro.empreendimento.enderecos[1].tipo = tipoEnderecoCorrespondencia;

		estadoEnderecoPrincipal.sigla = $scope.cadastro.empreendimento.enderecos[0].municipio.estado.codigo;
		estadoEnderecoCorrespondencia.sigla = $scope.cadastro.empreendimento.enderecos[1].municipio.estado.codigo;
		estadoEnderecoPrincipal.nome = $scope.cadastro.empreendimento.enderecos[0].municipio.estado.nome;
		estadoEnderecoCorrespondencia.nome = $scope.cadastro.empreendimento.enderecos[1].municipio.estado.nome;
		$scope.cadastro.empreendimento.enderecos[0].municipio.estado = estadoEnderecoPrincipal;
		$scope.cadastro.empreendimento.enderecos[1].municipio.estado = estadoEnderecoCorrespondencia;


		let geometria = $scope.cadastro.empreendimento.coordenadas;

		if($scope.cadastro.empreendimento.coordenadas.type === 'FeatureCollection'){
			$scope.cadastro.empreendimento.coordenadas = geometria.features[0].geometry;
		}

		$scope.cadastro.enderecoEmpreendimento.principal.id = null;
		$scope.cadastro.enderecoEmpreendimento.correspondencia.id = null;
		$scope.cadastro.enderecoEmpreendimento.principal.semNumero = $scope.cadastro.enderecoEmpreendimento.principal.numero == null ? true : null;
		$scope.cadastro.enderecoEmpreendimento.correspondencia.semNumero = $scope.cadastro.enderecoEmpreendimento.correspondencia.numero == null ? true : null;
		_.forEach($scope.cadastro.empreendimento.contatos, function(contato){
			contato.id = null;

			if(contato.principal === true && contato.tipo.id === etapaResumo.tipoContato.EMAIL)
				contato.valor = $scope.cadastro.contatoPrincipalEmpreendimento.email;
			else if (contato.tipo.id === etapaResumo.tipoContato.TELEFONE_RESIDENCIAL)
				contato.valor = $scope.cadastro.contatoPrincipalEmpreendimento.telefone;
			else if (contato.tipo.id === etapaResumo.tipoContato.TELEFONE_CELULAR)
				contato.valor = $scope.cadastro.contatoPrincipalEmpreendimento.celular;
			
		});
		
		etapaResumo.enderecosEmpreendimento.push($scope.cadastro.enderecoEmpreendimento.principal);
		etapaResumo.enderecosEmpreendimento.push($scope.cadastro.enderecoEmpreendimento.correspondencia);

	}

	function getEtapaProprietarios(){
		
		_.forEach($scope.cadastro.empreendimento.proprietarios, function(proprietario){

			if(proprietario.pessoa.contatos.novoProprietario !==true && proprietario.pessoa.enderecos.novoProprietario !==true ){

				_.forEach(proprietario.pessoa.contatos, function(contato){
					contato.id = null;		
				});
				proprietario.pessoa.enderecos[0].id = null;
				proprietario.pessoa.enderecos[1].id = null;

				etapaResumo.proprietarios.push(proprietario.pessoa);

			}else{
				proprietario.pessoa.contatos = getContatoPessoa(proprietario.pessoa.contatos);

				proprietario.pessoa.enderecos = getEndereco(proprietario.pessoa.enderecos);

				proprietario.pessoa.sexo.nome = proprietario.pessoa.sexo.codigo === 0 ? "MASCULINO" : "FEMININO";
				proprietario.pessoa.tipo.nome = proprietario.pessoa.tipo.codigo === etapaResumo.tipoPessoa.PESSOA_FISICA ? "FISICA" : "JURIDICA";

				etapaResumo.proprietarios.push(proprietario.pessoa);
			}

		});
	}

	function getEtapaRepresentantes(){

		_.forEach($scope.cadastro.empreendimento.representantesLegais, function(representanteLegal){

			if(representanteLegal.pessoa.contatos.novoRepresentante !==true && representanteLegal.pessoa.enderecos.novoRepresentante !==true ){

				_.forEach(representanteLegal.pessoa.contatos, function(contato){
					contato.id = null;		
				});
				representanteLegal.pessoa.enderecos[0].id = null;
				representanteLegal.pessoa.enderecos[1].id = null;
				etapaResumo.representantesLegais.push(representanteLegal.pessoa);

			}else{

				// representanteLegal.pessoa.contatos = getContatoPessoa(representanteLegal.pessoa.contatos);

				representanteLegal.pessoa.enderecos = getEndereco(representanteLegal.pessoa.enderecos);

				representanteLegal.pessoa.sexo.nome = representanteLegal.pessoa.sexo.codigo === 0 ? "MASCULINO" : "FEMININO";
				representanteLegal.pessoa.tipo.nome = representanteLegal.pessoa.tipo.codigo === etapaResumo.tipoPessoa.PESSOA_FISICA ? "FISICA" : "JURIDICA";

				etapaResumo.representantesLegais.push(representanteLegal.pessoa);
			}

		});

	}

	function getEtapaResponsaveis(){

		_.forEach($scope.cadastro.empreendimento.responsaveis, function(responsavel){

			if(responsavel.pessoa.contatos.novoResponsavel !==true && responsavel.pessoa.enderecos.novoResponsavel !==true ){

				_.forEach(responsavel.pessoa.contatos, function(contato){
					contato.id = null;		
				});
				responsavel.pessoa.enderecos[0].id = null;
				responsavel.pessoa.enderecos[1].id = null;

				if(responsavel.tipo === etapaResumo.tiposResponsaveis.LEGAL){
					etapaResumo.responsaveis.legais.push(responsavel.pessoa);
				}else{
					etapaResumo.responsaveis.tecnicos.push(responsavel.pessoa);
				}

			}else{

				// responsavel.pessoa.contatos = getContatoPessoa(responsavel.pessoa.contatos);

				responsavel.pessoa.enderecos = getEndereco(responsavel.pessoa.enderecos);

				responsavel.pessoa.sexo.nome = responsavel.pessoa.sexo.codigo === 0 ? "MASCULINO" : "FEMININO";
				responsavel.pessoa.tipo = {
					nome: responsavel.pessoa.type === 'PessoaFisica' ? "FISICA" : "JURIDICA",
					codigo: responsavel.pessoa.type === 'PessoaFisica' ? etapaResumo.tipoPessoa.PESSOA_FISICA : etapaResumo.tipoPessoa.PESSOA_JURIDICA 
				};
				if(responsavel.tipo === etapaResumo.tiposResponsaveis.LEGAL){
					etapaResumo.responsaveis.legais.push(responsavel.pessoa);
				}else{
					etapaResumo.responsaveis.tecnicos.push(responsavel.pessoa);
				}
			}

		});

	}

	function getCpfCnpjEmpreendimento(){

		if($scope.cadastro.origemEmpreendimento === $scope.cadastro.origensEmpreendimento.EMPREENDEDOR){
			return $scope.cadastro.empreendimento.empreendedor.pessoa.cpf !== null ? 
				$scope.cadastro.empreendimento.empreendedor.pessoa.cpf : $scope.cadastro.empreendimento.empreendedor.pessoa.cnpj;
		}

		if($scope.cadastro.origemEmpreendimento === $scope.cadastro.origensEmpreendimento.OUTRO_CPF_CNPJ){
			return $scope.cadastro.empreendimento.empreendimentoEU.pessoa.cpf !== null ?
				$scope.cadastro.empreendimento.empreendimentoEU.pessoa.cpf : $scope.cadastro.empreendimento.empreendimentoEU.pessoa.cnpj; 
		}
	}

	function getPessoaEmpreendimento(){

		if($scope.cadastro.origemEmpreendimento === $scope.cadastro.origensEmpreendimento.EMPREENDEDOR){
			return $scope.cadastro.empreendimento.empreendedor.pessoa;
		}

		if($scope.cadastro.origemEmpreendimento === $scope.cadastro.origensEmpreendimento.OUTRO_CPF_CNPJ){
			return $scope.cadastro.empreendimento.empreendimentoEU.pessoa;
				
		}
	}

	function getContatoPessoa(objetoContato){

		var listaContatos = [];

		if(objetoContato.email){
			var email = {
				principal: true,
				valor: objetoContato.email,
				tipo: {
					id: etapaResumo.tipoContato.EMAIL,
					descricao: "Email"
				}
			};

			listaContatos.push(email);
		}

		if(objetoContato.telefone){
			var telefone = {
				principal: false,
				valor: objetoContato.telefone,
				tipo: {
					id: etapaResumo.tipoContato.TELEFONE_RESIDENCIAL,
					descricao: "Telefone residencial"
				}
			};
			
			listaContatos.push(telefone);
		}

		if(objetoContato.celular){
			var celular = {
				principal: false,
				valor: objetoContato.celular,
				tipo: {
					id: etapaResumo.tipoContato.TELEFONE_CELULAR,
					descricao: "Telefone celular"
				}
			};
			
			listaContatos.push(celular);
		}

		return listaContatos;

	}

	function getEndereco(objetoEndereco){

		var listaEnderecos = [];

		if(objetoEndereco.principal){
			var enderecoPrincipal = {
				bairro: objetoEndereco.principal.bairro,
				caixaPostal: objetoEndereco.principal.caixaPostal,
				cep: objetoEndereco.principal.cep,
				complemento: objetoEndereco.principal.complemento,
				logradouro: objetoEndereco.principal.logradouro,
				numero: objetoEndereco.principal.numero,
				municipio: objetoEndereco.principal.municipio,
				semNumero: objetoEndereco.principal.semNumero,
				tipo:{
					id: etapaResumo.tipoEndereco.PRINCIPAL
				},
				zonaLocalizacao:{
					nome: objetoEndereco.principal.zonaLocalizacao.codigo === "ZONA_URBANA" ? 'Zona Urbana': 'Zona Rural',
					codigo: objetoEndereco.principal.zonaLocalizacao.codigo === "ZONA_URBANA" ? etapaResumo.zonaLocalizacaoEmp.URBANA: etapaResumo.zonaLocalizacaoEmp.RURAL
				} 
			};

			listaEnderecos.push(enderecoPrincipal);
		}

		if(objetoEndereco.correspondencia){
			var enderecoCorrespondencia = {
				bairro: objetoEndereco.correspondencia.bairro,
				caixaPostal: objetoEndereco.correspondencia.caixaPostal,
				cep: objetoEndereco.correspondencia.cep,
				complemento: objetoEndereco.correspondencia.complemento,
				logradouro: objetoEndereco.correspondencia.logradouro,
				numero: objetoEndereco.correspondencia.numero,
				municipio: objetoEndereco.correspondencia.municipio,
				semNumero: objetoEndereco.correspondencia.semNumero,
				tipo:{
					id:etapaResumo.tipoEndereco.CORRESPONDENCIA
				},
				zonaLocalizacao:{
					nome:'Zona Urbana',
					codigo:etapaResumo.zonaLocalizacaoEmp.URBANA
				} 
			};
			
			listaEnderecos.push(enderecoCorrespondencia);
		}

		return listaEnderecos;

	}

	function concluir() {

		getEtapaEmpreendedor();

		getEtapaEmpreendimento();

		getEtapaProprietarios();

		getEtapaRepresentantes();

		getEtapaResponsaveis();

		let empreendimento = {

			id: $scope.cadastro.empreendimento.id,
			denominacao: $scope.cadastro.empreendimento.denominacao,
			cpfCnpj:  getCpfCnpjEmpreendimento(),

			cpfCnpjCadastrante: $rootScope.usuarioSessao.login,

			empreendimentoEU:{
				contatos: getContatoPessoa($scope.cadastro.contatoPrincipalEmpreendimento),
				denominacao: $scope.cadastro.empreendimento.denominacao,
				enderecos: etapaResumo.enderecosEmpreendimento,
				proprietarios: etapaResumo.proprietarios,
				responsaveisLegais: etapaResumo.responsaveis.legais,
				responsaveisTecnicos: etapaResumo.responsaveis.tecnicos,
				empreendedor: {
					pessoa: $scope.cadastro.empreendimento.empreendedor.pessoa
				},
				pessoa: getPessoaEmpreendimento(),
				localizacao: {
					geometria: JSON.stringify($scope.cadastro.empreendimento.coordenadas)
				},
				representantesLegais: etapaResumo.representantesLegais
			},	
			municipio: $scope.cadastro.empreendimento.municipio,
			jurisdicao: $scope.cadastro.empreendimento.jurisdicao,
			localizacao:$scope.cadastro.empreendimento.localizacao
			
		};
		empreendimento.imovel = $scope.cadastro.empreendimento.imovel;
		empreendimento.coordenadas= $scope.cadastro.empreendimento.coordenadas;
		empreendimento.empreendimentoEU.empreendedor.pessoa.contatos = getContatoPessoa($scope.cadastro.contatoPrincipalEmpreendedor);

		empreendimentoService.save(empreendimento)
			.then(function(response){

				mensagem.success(response.data.texto || response.data);
				$location.path("/empreendimentos/listagem");

			})
			.catch(function(response){

				mensagem.error((response && response.data) ? response.data.texto : 'Ocorreu um erro ao salvar o empreendimento');

			});
	}
};

exports.controllers.EtapaResumoController = EtapaResumoController;
