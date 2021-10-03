var EsferaAtuacao = {

	bindings: {
		esferaAtuacao: '=',
		disabled: '<',
		required: '<',
		label: '<',
		form: '<'
	},

	controller: function(esferaAtuacaoService, mensagem) {

		var ctrl = this;

		this.$postLink = function() {

			esferaAtuacaoService.list().then(
				function(response) {
					ctrl.esferasAtuacao = response.data;
				},
				function(){
					mensagem.error('Ocorreu um erro ao buscar as informações de esferas de atuação.');
				}
			);

		};

	},

	templateUrl: 'components/esferaAtuacao/esferaAtuacao.html'

};

exports.directives.EsferaAtuacao = EsferaAtuacao;