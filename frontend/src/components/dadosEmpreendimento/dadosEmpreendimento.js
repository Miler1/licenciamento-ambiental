var DadosEmpreendimento = {

	bindings: {
		id: '=',
		empreendimento: '='
	},

	controller: function(empreendimentoService, mensagem) {

		var ctrl = this;

		this.$onInit = function() {

			empreendimentoService.getEmpreendimentoPorId(ctrl.id)
			.then(function(response) {

				ctrl.empreendimento = response.data;

			})
			.catch(function(response) {

				mensagem.error("Ocorreu um erro ao buscar os dados do empreendimento");

			});
		};

	},

	templateUrl: 'components/dadosEmpreendimento/dadosEmpreendimento.html'

};

exports.directives.DadosEmpreendimento = DadosEmpreendimento;