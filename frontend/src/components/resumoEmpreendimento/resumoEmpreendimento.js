var ResumoEmpreendimento = {
	
	bindings: {
		empreendimento: '='
	},
	
	controller: function($scope, $rootScope, municipioService, imovelService, empreendimentoService, mensagem) {
		
		var ctrl = this;

		ctrl.getEndereco = getEndereco;
		ctrl.enderecoEmpreendedorPrincipal = null;
		ctrl.enderecoEmpreendedorCorrespondencia = null;
		ctrl.enderecoEmpreendimentoPrincipal = null;
		ctrl.enderecoEmpreendimentoCorrespondencia = null;
		ctrl.geo = null;
		ctrl.controleAtualizacao = false;
		ctrl.cadastrante = {};
		ctrl.cpfCnpjCadastrante = $rootScope.usuarioSessao.login;
		ctrl.tipoContato = app.TIPO_CONTATO;

		ctrl.contatoPrincipalCadastrante = {
			email: null,
			telefone:null,
			celular: null
		};

		ctrl.contatoPrincipalEmpreendedor = {
			email: null,
			telefone:null,
			celular: null
		};

		ctrl.contatoPrincipalEmpreendimento = {
			email: null,
			telefone:null,
			celular: null
		};
		ctrl.enderecos = {
			cadastrante: {},
			empreendedor:{},
			empreendimento: {},
			proprietario: {},
			representanteLegal: {}
		};

		ctrl.listaResponsaveisLegais = {};

		ctrl.tipoContato = app.TIPO_CONTATO;
		ctrl.tipoEndereco = app.TIPO_ENDERECO;
		ctrl.localizacaoEmpreendimento = app.LOCALIZACOES_EMPREENDIMENTO;
		ctrl.tipoPessoa = app.TIPO_PESSOA;
		
		$scope.$parent.$watch("cadastro.etapa", function(etapa) {
			
			if(etapa && etapa.class === $scope.$parent.cadastro.etapas.RESUMO.class){
				
				init();
			}
		});
		
		this.$onInit = function() {
			
			if(!$scope.$parent.cadastro)
			init();
			
		};
		
		function visualizaEmpreendimentoValidacao(){

			if (ctrl.empreendimento.proprietarios === null || ctrl.empreendimento.proprietarios === undefined ){
				var listaProprietarios = [];

				_.forEach( ctrl.empreendimento.empreendimentoEU.proprietarios, function(proprietario){

					var proprietarioPessoa = {
						pessoa:null
					};

					proprietarioPessoa.pessoa = proprietario;
					listaProprietarios.push(proprietarioPessoa);

				});
				ctrl.empreendimento.proprietarios = listaProprietarios;
			}

			if (ctrl.empreendimento.representantesLegais === null || ctrl.empreendimento.representantesLegais === undefined ){
				var listaRepresentantesLegais = [];

				_.forEach( ctrl.empreendimento.empreendimentoEU.representantesLegais, function(representanteLegal){

					var representanteLegalPessoa = {
						pessoa:null,
						dataVinculacao:null,
					};

					representanteLegalPessoa.pessoa = representanteLegal;
					representanteLegalPessoa.dataVinculacao = representanteLegal.dataCadastro;
					listaRepresentantesLegais.push(representanteLegalPessoa);

				});
				ctrl.empreendimento.representantesLegais = listaRepresentantesLegais;
			}

			if (ctrl.empreendimento.responsaveis === null || ctrl.empreendimento.responsaveis === undefined ){

				ctrl.empreendimento.responsaveis = [];

				_.forEach( ctrl.empreendimento.empreendimentoEU.responsaveisTecnicos, function(responsavelTecnico){

					var responsavelTecnicoPessoa = {
						pessoa:null,
						tipo: 'TECNICO'
					};

					responsavelTecnicoPessoa.pessoa = responsavelTecnico;
					ctrl.empreendimento.responsaveis.push(responsavelTecnicoPessoa);

				});

				_.forEach( ctrl.empreendimento.empreendimentoEU.responsaveisLegais, function(responsavelLegal){

					var responsavelLegalPessoa = {
						pessoa:null,
						tipo:'LEGAL',
					};

					responsavelLegalPessoa.pessoa = responsavelLegal;
					ctrl.empreendimento.responsaveis.push(responsavelLegalPessoa);

				});
			}

			if((ctrl.empreendimento.empreendedor === null || ctrl.empreendimento.empreendedor === undefined) && ctrl.empreendimento.empreendimentoEU.empreendedor !== null){
				ctrl.empreendimento.empreendedor = ctrl.empreendimento.empreendimentoEU.empreendedor;
			}
			if((ctrl.empreendimento.enderecos === null || ctrl.empreendimento.enderecos === undefined) && ctrl.empreendimento.empreendimentoEU.enderecos !== null){
				ctrl.empreendimento.enderecos = ctrl.empreendimento.empreendimentoEU.enderecos;
			}

			if((ctrl.empreendimento.coordenadas === null || ctrl.empreendimento.coordenadas === undefined) && ctrl.empreendimento.empreendimentoEU.localizacao){
				ctrl.empreendimento.coordenadas = JSON.parse(ctrl.empreendimento.empreendimentoEU.localizacao.geometria);
			}

		}

		function init() {

			visualizaEmpreendimentoValidacao();

			getEmail(ctrl.empreendimento.representantesLegais,false);
			getEmail(ctrl.empreendimento.responsaveis,true);
			getCadastrante(ctrl.cpfCnpjCadastrante);

			if ($scope.$parent.cadastro == undefined) {
				getContato(ctrl.empreendimento.empreendedor.pessoa.contatos, ctrl.contatoPrincipalEmpreendedor);	
			} else {
				getContatoEmpreendedor(angular.copy($scope.$parent.cadastro.contatoPrincipalEmpreendedor));	
			}
			
			if ($scope.$parent.cadastro == undefined && ctrl.empreendimento.cpfCnpj.length <= 11) {

				getContato(ctrl.empreendimento.empreendimentoEU.contatos,ctrl.contatoPrincipalEmpreendimento);

				ctrl.enderecos.empreendimento = ctrl.getEndereco(ctrl.empreendimento.enderecos, ctrl.enderecos.empreendimento);

			} else if ($scope.$parent.cadastro == undefined && ctrl.empreendimento.cpfCnpj.length > 11) {

				getContato(ctrl.empreendimento.empreendimentoEU.contatos, ctrl.contatoPrincipalEmpreendimento);
	
				ctrl.enderecos.empreendimento = ctrl.getEndereco(ctrl.empreendimento.enderecos, ctrl.enderecos.empreendimento);
			
			} else {
				getContatoEmpreendimento(angular.copy($scope.$parent.cadastro.contatoPrincipalEmpreendimento));				
				ctrl.enderecos.empreendimento = angular.copy(ctrl.getEndereco($scope.$parent.cadastro.enderecoEmpreendimento, ctrl.enderecos.empreendimento));
			}

			ctrl.enderecos.empreendedor = ctrl.getEndereco(ctrl.empreendimento.empreendedor.pessoa.enderecos, ctrl.enderecos.empreendedor);
			ctrl.empreendimento.empreendimentoEU.denominacao = ctrl.empreendimento.denominacao;
			
			if (ctrl.enderecos.empreendimento.principal.zonaLocalizacao.codigo === ctrl.localizacaoEmpreendimento.URBANA) {
				getGeometriaMunicipio(ctrl.empreendimento.municipio.id);
			} else {
				getDadosImovel();
			}
		}
		
		function getEmail(listaRepresentantesOuResponsaveis, temTelefone) {

		   _.forEach(listaRepresentantesOuResponsaveis, function(representanteOuResponsavel){

				representanteOuResponsavel.email =_.find(representanteOuResponsavel.pessoa.contatos, function(contato) {
					return contato.principal === true && contato.tipo.id === ctrl.tipoContato.EMAIL ? contato.valor : '-';
				});
				if(typeof representanteOuResponsavel.email === 'string'){
					representanteOuResponsavel.pessoa.contatos = getContatoRepresentantesResponsaveis(representanteOuResponsavel.pessoa.contatos);
					representanteOuResponsavel.email =_.find(representanteOuResponsavel.pessoa.contatos, function(contato) {
						return contato.principal === true && contato.tipo.id === ctrl.tipoContato.EMAIL ? contato.valor : '-';
					});
				}

				if(temTelefone === true){
					representanteOuResponsavel.celular =_.find(representanteOuResponsavel.pessoa.contatos, function(contato) {
						if(contato.tipo.id === ctrl.tipoContato.TELEFONE_CELULAR){
							return contato.valor;
						}
					});
				}
		   });

	   }

	   function getContatoRepresentantesResponsaveis(objetoContato){

		var listaContatos = [];

		if(objetoContato.email){
			var email = {
				principal: true,
				valor: objetoContato.email,
				tipo: {
					id: ctrl.tipoContato.EMAIL,
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
					id: ctrl.tipoContato.TELEFONE_RESIDENCIAL,
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
					id: ctrl.tipoContato.TELEFONE_CELULAR,
					descricao: "Telefone celular"
				}
			};
			
			listaContatos.push(celular);
		}

		return listaContatos;

	}

		function getEndereco(listaEnderecos, enderecos) {

			_.forEach(listaEnderecos, function(endereco){

				if(endereco.tipo.id === ctrl.tipoEndereco.CORRESPONDENCIA)
					enderecos.correspondencia = endereco; 
				else
					enderecos.principal = endereco;
			});
			return enderecos;
		}

		function getGeometriaMunicipio(idMunicipio) {

			ctrl.estiloGeo = app.Geo.overlays.AREA_MUNICIPIO.style;

			municipioService.getMunicipioGeometryById(idMunicipio)
				.then(function(response) {
					ctrl.geo = response.data.limite;
				})
				.catch(function() {
					mensagem.error("Não foi possível obter o limite do município.");
				});
		}

		function getDadosImovel() {

			var codigoImovel = ctrl.empreendimento.imovel.codigo;

			ctrl.estiloGeo = app.Geo.overlays.AREA_IMOVEL.style;

			imovelService.getImoveisCompletoByCodigo(codigoImovel)
				.then(function(response){
					ctrl.geo = response.data.geo;
					ctrl.nomeEstado = response.data.municipio.estado.nome;
					ctrl.nomeMunicipio = response.data.municipio.nome;
					ctrl.nomeImovel = response.data.nome;
				})
				.catch(function(){
					mensagem.error("Não foi possível obter os dados do imóvel no CAR.");
				});
		}

		function getContatoPessoa(listaContatos, contatoPrincipal){

			_.forEach(listaContatos, function (contato){
						
				if(contato.principal === true && contato.tipo.id === ctrl.tipoContato.EMAIL)
					contatoPrincipal.email = contato.valor;
				else if (contato.tipo.id === ctrl.tipoContato.TELEFONE_RESIDENCIAL)
					contatoPrincipal.telefone = contato.valor;
				else if (contato.tipo.id === ctrl.tipoContato.TELEFONE_CELULAR)
					contatoPrincipal.celular = contato.valor;
				 
			});	
			return contatoPrincipal;
		}

		function getCadastrante (cpfCnpj){

			empreendimentoService.getCadastrante(cpfCnpj)
				.then(function(response){
					ctrl.cadastrante = response.data;
					getContatoPessoa(ctrl.cadastrante.contatos, ctrl.contatoPrincipalCadastrante);
					ctrl.enderecos.cadastrante = ctrl.getEndereco(ctrl.cadastrante.enderecos, ctrl.enderecos.cadastrante );
				});
		}

		function getContatoEmpreendedor (contatoPrincipalEmpreendedor){

			ctrl.contatoPrincipalEmpreendedor.email = contatoPrincipalEmpreendedor.email !== null ? contatoPrincipalEmpreendedor.email : null;
			ctrl.contatoPrincipalEmpreendedor.telefone = contatoPrincipalEmpreendedor.telefone !== null ? contatoPrincipalEmpreendedor.telefone : null;
			ctrl.contatoPrincipalEmpreendedor.celular = contatoPrincipalEmpreendedor.celular !== null ? contatoPrincipalEmpreendedor.celular : null;

		}

		function getContatoEmpreendimento (contatoPrincipalEmpreendimento){

			ctrl.contatoPrincipalEmpreendimento.email = contatoPrincipalEmpreendimento.email !== null ? contatoPrincipalEmpreendimento.email : null;
			ctrl.contatoPrincipalEmpreendimento.telefone = contatoPrincipalEmpreendimento.telefone !== null ? contatoPrincipalEmpreendimento.telefone : null;
			ctrl.contatoPrincipalEmpreendimento.celular = contatoPrincipalEmpreendimento.celular !== null ? contatoPrincipalEmpreendimento.celular : null;
			
		}

		function getContato(listaContatos, contatoPrincipal){

			_.forEach(listaContatos, function (contato){
					   
				if(contato.principal === true && contato.tipo.id === ctrl.tipoContato.EMAIL)
					contatoPrincipal.email = contato.valor;
			   else if (contato.tipo.id === ctrl.tipoContato.TELEFONE_RESIDENCIAL)
					contatoPrincipal.telefone = contato.valor;
				else if (contato.tipo.id === ctrl.tipoContato.TELEFONE_CELULAR)
					contatoPrincipal.celular = contato.valor;
				
			});	
			return contatoPrincipal;
		}

	},

	templateUrl: 'components/resumoEmpreendimento/resumoEmpreendimento.html'
};

exports.directives.ResumoEmpreendimento = ResumoEmpreendimento;