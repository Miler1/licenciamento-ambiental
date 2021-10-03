var EstadoCivil = {

	bindings: {
		estadoCivil: '=',
		disabled: '<',
		required: '<',
		form: '<'
	},

	controller: function(estadoCivilService, mensagem) {

		var ctrl = this;

		this.$postLink = function() {

			estadoCivilService.list().then(
				function(response) {
					ctrl.estadosCivis = response.data;
				},
				function(){
					mensagem.error('Ocorreu um erro ao buscar as informações de estados civis.');
				}
			);

		};

	},

	templateUrl: 'components/estadoCivil/estadoCivil.html'

};

exports.directives.EstadoCivil = EstadoCivil;