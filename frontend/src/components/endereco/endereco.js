var Endereco = {

	bindings: {
		endereco: '=',
		zonaRural: '<',
		correspondencia: '<',
		disabled: '<',
		required: '<',
		form: '<',
		cepInputName: '<',
		logradouroInputName: '<',
		numeroInputName: '<',
		ufInputName: '<',
		municipioInputName: '<',
		roteiroInputName: '<',
		disabledCep: '<',
		disabledLogradouro: '<',
		disabledNumero: '<',
		disabledCaixaPostal: '<',
		disabledComplemento: '<',
		disabledBairro: '<',
		disabledUf: '<',
		disabledMunicipio: '<',
		disabledRoteiro: '<'
	},

	controller: function(enderecoService, $scope, mensagem) {

		var ctrl = this;

		this.$onInit = function() {

			ctrl.cepInputName = ctrl.cepInputName || 'cep'; 
			ctrl.logradouroInputName = ctrl.logradouroInputName || 'logradouro';
			ctrl.numeroInputName = ctrl.numeroInputName || 'numero'; 
			ctrl.ufInputName = ctrl.ufInputName || 'uf';
			ctrl.municipioInputName = ctrl.municipioInputName || 'municipio';
			ctrl.roteiroInputName = ctrl.roteiroInputName || 'roteiro';
			this.semNumero = ctrl.semNumero || 'semNumero';


			if(ctrl.endereco){

				if(ctrl.endereco.cep){
					ctrl.endereco.cep = ctrl.endereco.cep.toString();
				}

				// if(ctrl.endereco.municipio.estado.sigla){
				// 	ctrl.ufInputName = ctrl.endereco.municipio.estado.sigla;
				// }

				// if(ctrl.endereco.municipio.nome){
				// 	ctrl.municipioInputName = ctrl.endereco.municipio.nome;
				// }
			}
		};

		this.pesquisarEnderecoPorCep = function() {

			var onSucess = function(response) {

				var endereco = response.data;

				if (!endereco || endereco === "null") {

					that.mensagem.warning(app.Messages.error.cepNaoEncontrado);

					endereco = {};

				}

				ctrl.endereco.municipio = endereco.municipio;
				ctrl.endereco.logradouro = endereco.logradouro;
				ctrl.endereco.bairro = endereco.bairro;
				ctrl.endereco.numero = endereco.numero;

				$scope.$broadcast('municipioAtualizado', endereco.municipio);

			};

			var onException = function(response) {
				mensagem.warning('Erro ao recuperar as infomações do CEP.');
			};

			enderecoService
				.getEnderecoByCEP(ctrl.endereco.cep)
				.then(onSucess)
				.catch(onException);

		};

		this.marcarSemNumero = function() {
			if(!ctrl.endereco.semNumero) {
				ctrl.endereco.numero = null;
			}

		};

	},

	templateUrl: 'components/endereco/endereco.html'

};

exports.directives.Endereco = Endereco;