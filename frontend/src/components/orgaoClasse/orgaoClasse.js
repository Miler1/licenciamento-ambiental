var OrgaoClasse = {

	bindings: {
		orgaoClasse: '=',
		disabled: '<',
		required: '<',
		label: '<',
		form: '<'
	},

	controller: function(orgaoClasseService, mensagem) {

		var ctrl = this;

		this.$postLink = function() {

			orgaoClasseService.list().then(
				function(response) {
					ctrl.orgaosClasse = response.data;
				},
				function(){
					mensagem.error('Ocorreu um erro ao buscar as informações de órgãos de classe.');
				}
			);

		};

	},

	templateUrl: 'components/orgaoClasse/orgaoClasse.html'

};

exports.directives.OrgaoClasse = OrgaoClasse;