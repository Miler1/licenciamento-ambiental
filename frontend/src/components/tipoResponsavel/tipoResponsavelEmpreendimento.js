var TipoResponsavelEmpreendimento = {

	bindings: {
		tipoResponsavel: '=',
		onUpdate: '=',
		disabled: '<',
		tiposResponsavelDisponiveis: '='
	},

	controller: function(tipoResponsavelEmpreendimentoService, mensagem) {

		var ctrl = this;

		this.$postLink = function() {

			tipoResponsavelEmpreendimentoService.list().then(

				function(response) {
					ctrl.tiposResponsavelDisponiveis = response.data;
				},
				function(){
					mensagem.error('Ocorreu um erro ao buscar as informações de tipos de responsáveis.');
				}

			);

		};



	},

	templateUrl: 'components/tipoResponsavel/tipoResponsavelEmpreendimento.html'

};

exports.directives.TipoResponsavelEmpreendimento = TipoResponsavelEmpreendimento;