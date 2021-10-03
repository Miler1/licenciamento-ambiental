var EtapaResumoControllerEdicao = function($scope, $rootScope, $route, $location, empreendimentoService, mensagem, $routeParams) {

	var etapaResumo = this;

	etapaResumo.concluir = concluir;
	etapaResumo.formatarEmpreendimento = formatarEmpreendimento;
	etapaResumo.getCpfCnpjEmpreendimento = getCpfCnpjEmpreendimento;
	etapaResumo.getPessoaEmpreendimento = getPessoaEmpreendimento;
	etapaResumo.zonaLocalizacaoEmp = app.LOCALIZACOES_EMPREENDIMENTO;
	etapaResumo.tipoEndereco = app.TIPO_ENDERECO;
	etapaResumo.getTipoEndereco = getTipoEndereco;
	etapaResumo.tiposResponsaveis = app.TIPOS_RESPONSAVEIS;
	etapaResumo.tipoContato = app.TIPO_CONTATO;
	etapaResumo.tipoPessoa = app.TIPO_PESSOA;
	etapaResumo.responsaveis = {
		legais: [],
		tecnicos:[]
	};
	etapaResumo.proprietarios = [];
	etapaResumo.representantesLegais = [];

	function getEtapaResponsaveis(){

		// _.forEach($scope.cadastro.empreendimento.responsaveis, function(responsavelLegal){

		// 	if(responsavelLegal.tipo === etapaResumo.tiposResponsaveis.LEGAL){
		// 		etapaResumo.responsaveis.legais.push(responsavelLegal.pessoa);
		// 	}else{
		// 		etapaResumo.responsaveis.tecnicos.push(responsavelLegal.pessoa);
		// 	}
			
		// });

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

				responsavel.pessoa.contatos = getContatoPessoa(responsavel.pessoa.contatos);

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

		var cpfCnpjEmpreendedor = $scope.cadastro.empreendimento.empreendedor.pessoa.cpf !== null ? 
		$scope.cadastro.empreendimento.empreendedor.pessoa.cpf : $scope.cadastro.empreendimento.empreendedor.pessoa.cnpj;

		var cpfCnpjOutroEmpreendedor = $scope.cadastro.empreendimento.empreendimentoEU.pessoa.cpf !== null ?
		$scope.cadastro.empreendimento.empreendimentoEU.pessoa.cpf : $scope.cadastro.empreendimento.empreendimentoEU.pessoa.cnpj;

		return cpfCnpjEmpreendedor === cpfCnpjOutroEmpreendedor ? cpfCnpjEmpreendedor : cpfCnpjOutroEmpreendedor;
		
	}

	function getPessoaEmpreendimento(){

		var cpfCnpjEmpreendedor = $scope.cadastro.empreendimento.empreendedor.pessoa.cpf !== null ? 
		$scope.cadastro.empreendimento.empreendedor.pessoa.cpf : $scope.cadastro.empreendimento.empreendedor.pessoa.cnpj;

		var cpfCnpjOutroEmpreendedor = $scope.cadastro.empreendimento.empreendimentoEU.pessoa.cpf !== null ?
		$scope.cadastro.empreendimento.empreendimentoEU.pessoa.cpf : $scope.cadastro.empreendimento.empreendimentoEU.pessoa.cnpj;

		return cpfCnpjEmpreendedor === cpfCnpjOutroEmpreendedor ? $scope.cadastro.empreendimento.empreendedor.pessoa : $scope.cadastro.empreendimento.empreendimentoEU.pessoa;
		
	}

	function formataMunicipio (listaEnderecos){

		var enderecos = [];

		_.forEach(listaEnderecos, function(endereco){
			 let estado ={
				sigla: endereco.municipio.estado.codigo ? endereco.municipio.estado.codigo : endereco.municipio.estado.sigla ,
				nome: endereco.municipio.estado.nome
			 };
			 endereco.municipio.estado = estado;

			 enderecos.push(endereco);
		});
		return enderecos;
	}

	function getTipoEndereco(listaEnderecos){

		var enderecos = [];

		var tipoEndereco = {
			principal: {
				id: etapaResumo.tipoEndereco.PRINCIPAL
			},
			correspondencia:{
				id: etapaResumo.tipoEndereco.CORRESPONDENCIA
			}
		};

		listaEnderecos.principal.tipo = tipoEndereco.principal;
		listaEnderecos.correspondencia.tipo = tipoEndereco.correspondencia;
		enderecos.push(listaEnderecos.principal);
		enderecos.push(listaEnderecos.correspondencia);

		return enderecos;
	}

	function getProprietarios(){
		
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
		return etapaResumo.proprietarios;
		
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

	function getRepresentantesLegais(){

		_.forEach($scope.cadastro.empreendimento.representantesLegais, function(representanteLegal){

			if(representanteLegal.pessoa.contatos.novoRepresentante !==true && representanteLegal.pessoa.enderecos.novoRepresentante !==true ){

				_.forEach(representanteLegal.pessoa.contatos, function(contato){
					contato.id = null;		
				});
				representanteLegal.pessoa.enderecos[0].id = null;
				representanteLegal.pessoa.enderecos[1].id = null;
				representanteLegal.pessoa.tipo.nome = representanteLegal.pessoa.tipo.codigo === etapaResumo.tipoPessoa.PESSOA_FISICA ? "FISICA" : "JURIDICA";

				etapaResumo.representantesLegais.push(representanteLegal.pessoa);

			}else{

				representanteLegal.pessoa.contatos = getContatoPessoa(representanteLegal.pessoa.contatos);

				representanteLegal.pessoa.enderecos = getEndereco(representanteLegal.pessoa.enderecos);

				representanteLegal.pessoa.sexo.nome = representanteLegal.pessoa.sexo.codigo === 0 ? "MASCULINO" : "FEMININO";
				representanteLegal.pessoa.tipo.nome = representanteLegal.pessoa.tipo.codigo === etapaResumo.tipoPessoa.PESSOA_FISICA ? "FISICA" : "JURIDICA";

				etapaResumo.representantesLegais.push(representanteLegal.pessoa);
			}

		});
		return etapaResumo.representantesLegais;
	}

	function formatarEmpreendimento () {

		getEtapaResponsaveis();

		let empreendimento = {

			id: $scope.cadastro.empreendimento.id,
			cpfCnpj:  getCpfCnpjEmpreendimento(),

			cpfCnpjCadastrante: $rootScope.usuarioSessao.login,

			empreendimentoEU:{
				contatos: getContatoPessoa($scope.cadastro.contatoPrincipalEmpreendimento),
				denominacao: $scope.cadastro.empreendimento.denominacao,
				enderecos: getTipoEndereco($scope.cadastro.enderecoEmpreendimento),
				proprietarios: getProprietarios(),
				responsaveisLegais: etapaResumo.responsaveis.legais,
				responsaveisTecnicos: etapaResumo.responsaveis.tecnicos,
				empreendedor: {
					pessoa: $scope.cadastro.empreendimento.empreendedor.pessoa,
					id: $scope.cadastro.empreendimento.empreendedor.id
				},
				pessoa: getPessoaEmpreendimento(),
				localizacao: {
					geometria: JSON.stringify($scope.cadastro.empreendimento.coordenadas)
				},
				representantesLegais: getRepresentantesLegais()
			},
			municipio: $scope.cadastro.empreendimento.municipio,
			jurisdicao: $scope.cadastro.empreendimento.jurisdicao,
			localizacao:$scope.cadastro.empreendimento.localizacao
			
		};

		empreendimento.imovel = $scope.cadastro.empreendimento.imovel;
		empreendimento.empreendimentoEU.empreendedor.pessoa.contatos = getContatoPessoa($scope.cadastro.contatoPrincipalEmpreendedor);
		empreendimento.empreendimentoEU.empreendedor.pessoa.enderecos = getTipoEndereco($scope.cadastro.enderecoEmpreendedor);
		empreendimento.empreendimentoEU.pessoa.enderecos = formataMunicipio(empreendimento.empreendimentoEU.pessoa.enderecos);

		return empreendimento;
	}

	function concluir() {

		var renovacao = false;

		let retificacao = false;

		let empreendimentoAlteradoViaNotificacao = false;

		// if ($scope.cadastro.empreendimento.enderecos){
		// 	$scope.cadastro.empreendimento.pessoa.enderecos = $scope.cadastro.empreendimento.enderecos;
		// }

		// let dadosNotificacao = $scope.cadastro.caracterizacao || undefined;

		if ($route.current.$$route.originalPath.search('renovacao') !== -1) {

			$scope.cadastro.empreendimento.emRenovacao = true;
			renovacao = true;
		}

		if($route.current.$$route.originalPath.search('notificacao') !== -1) {

			$scope.cadastro.empreendimento.emRetificacao = true;
			retificacao = true;
		}

		$scope.cadastro.empreendimento.empreendedor.representantesLegais = $scope.cadastro.empreendimento.representantesLegais;

		if (typeof $scope.cadastro.empreendimento.coordenadas === "string"){
			$scope.cadastro.empreendimento.coordenadas = JSON.parse($scope.cadastro.empreendimento.coordenadas);
		}

		let geometria = $scope.cadastro.empreendimento.empreendimentoEU.localizacao.geometria ? $scope.cadastro.empreendimento.empreendimentoEU.localizacao.geometria : $scope.cadastro.empreendimento.empreendimentoEU.coordenadas;

		if(geometria.type === 'FeatureCollection'){
			$scope.cadastro.empreendimento.coordenadas = geometria.features[0].geometry;
		}

		empreendimentoService.update($scope.cadastro.empreendimento.id, formatarEmpreendimento())
			.then(function(response){

				mensagem.success(response.data.texto || response.data);

				empreendimentoAlteradoViaNotificacao = true;

				$scope.$broadcast('onEmpreendimentoUpdated',{empreendimentoAlteradoViaNotificacao : empreendimentoAlteradoViaNotificacao } );

				$scope.$on("onEmpreendimentoUpdated", function(evt,data){
					empreendimentoAlteradoViaNotificacao = data.empreendimentoAlteradoViaNotificacao;
					idCaracterizacao = $routeParams.idCaracterizacao;
				});

				if(empreendimentoAlteradoViaNotificacao){
					if (renovacao) {
						$location.path('/empreendimento/' + $routeParams.id + '/caracterizacao/' + $routeParams.idCaracterizacao + '/editar/renovacao');
					}else if (retificacao) {
						$location.path('/empreendimento/' + $routeParams.id + '/caracterizacao/' + $routeParams.idCaracterizacao + '/notificacao/'+ empreendimentoAlteradoViaNotificacao);
					} else {
						$location.path("/empreendimentos/listagem");
					}
				}


			})
			.catch(function(response){

				$location.path("/empreendimentos/listagem");

			});

		$scope.$on("onEmpreendimentoUpdated", function(evt,data){
			empreendimentoAlteradoViaNotificacao = data.empreendimentoAlteradoViaNotificacao;
			idCaracterizacao = $routeParams.idCaracterizacao;
		});

		if(empreendimentoAlteradoViaNotificacao){
			if (renovacao) {
				$location.path('/empreendimento/' + $routeParams.id + '/caracterizacao/' + $routeParams.idCaracterizacao + '/editar/renovacao');
			}else if (retificacao) {
				$location.path('/empreendimento/' + $routeParams.id + '/caracterizacao/' + $routeParams.idCaracterizacao + '/retificar/'+ empreendimentoAlteradoViaNotificacao);
			} else {
				$location.path("/empreendimentos/listagem");
			}
		}

	}

};

exports.controllers.EtapaResumoControllerEdicao = EtapaResumoControllerEdicao;