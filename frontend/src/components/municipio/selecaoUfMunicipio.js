var SelecaoUfMunicipio = {

	bindings: {
		municipio: '=',
		exibirLabel: '@',
		disabledEstado: '<',
		disabledMunicipio: '<',
		required: '@',
		form: '<',
		ufInputName: '<',
		municipioInputName: '<',
		labelUf: '<',
		labelMunicipio: '<',
		limitarMunicipiosAmapa: '@'
	},

	controller: function(enderecoService, $rootScope, mensagem, $scope) {

		var ctrl = this;

		this.$onInit = function() {

			ctrl.ufInputName = ctrl.ufInputName || 'uf';
			ctrl.municipioInputName = ctrl.municipioInputName || 'municipio';
		};

		this.$postLink = function() {

			enderecoService.listEstados().then(
				
				function(response) {
					
					if(ctrl.limitarMunicipiosAmapa) {

						ctrl.ufs = [_.find(response.data, {codigo: 'AP'})];
						ctrl.uf = ctrl.ufs[0];
						ctrl.carregarMunicipios();

					}else {

						ctrl.ufs = response.data;

					}

					if(ctrl.municipio){
						ctrl.uf = _.find(ctrl.ufs, {codigo: ctrl.municipio.estado.codigo});
						ctrl.carregarMunicipios(ctrl.municipio);
					}
				})
				.catch(function(){
					mensagem.warning('Não foi possível obter a lista de unidades federativas.');
				});

		};

		this.$doCheck = function() {
			
			if(ctrl.municipio && ctrl.municipio.estado){
				if(!ctrl.uf){
					updateUfModel(ctrl.municipio);
				}
				else if(ctrl.municipios) {
					
					if(ctrl.municipio.estado.codigo != ctrl.uf.codigo) {
						return;
					}

					ctrl.uf = _.find(ctrl.ufs, {codigo: ctrl.municipio.estado.codigo});
					ctrl.municipio = _.find(ctrl.municipios, {id: ctrl.municipio.id});

					if ($scope.$parent.cadastro && $scope.$parent.cadastro.etapas && $scope.$parent.cadastro.etapa.class === $scope.$parent.cadastro.etapas.EMPREENDIMENTO.class) {
						$rootScope.$broadcast('selectMunicipioEnderecoPrincipalEmpreendimento', ctrl.municipio);
					}
					
				}
			}

		};

		this.carregarMunicipios = function(municipioModel){

			if(!ctrl.uf){
				return;
			}

			enderecoService.listMunicipios(ctrl.uf.codigo).then(
				function(response){
					
					ctrl.municipios = response.data;

					if(municipioModel){
						if(municipioModel.codigoIbge){
							ctrl.municipio = municipioModel;
						}else{
							ctrl.municipio = _.find(ctrl.municipios, {nome: municipioModel.nome} );
						}
					} 
				})
				.catch(function(){
					mensagem.warning('Não foi possível obter a lista de municípios.');
				});

		};

		function updateUfModel(municipio) {

			siglaEstado = municipio.estado.sigla  ? municipio.estado.sigla : municipio.estado.codigo;
			 ctrl.uf = _.find(ctrl.ufs, {codigo: siglaEstado});
			 ctrl.carregarMunicipios(municipio);

		}

		$scope.$on('municipioAtualizado', function(event, municipio){
			updateUfModel(municipio);
		});

		$scope.$on('limparUf', function(event){
			ctrl.uf = null;
		});

		$scope.$on('refreshMunicipios', function(event){

			if(ctrl.municipio && ctrl.municipio.estado && 
				ctrl.uf.codigo !== ctrl.municipio.estado.sigla){

				updateUfModel(ctrl.municipio);
			}

		});

	},

	templateUrl: 'components/municipio/selecaoUfMunicipio.html'

};

exports.directives.SelecaoUfMunicipio = SelecaoUfMunicipio;