var BuscarPessoa = {

	bindings: {
		exibirCnpj: '<',
		pessoa: '=',
		placeholderText: '@',
		onUpdate: '=',
		buscarService: '=',
		onBeforeUpdate: '='
	},

	controller: function(pessoaService, mensagem, $timeout, $scope) {

		var ctrl = this;

		this.buscarPessoaByCpfCnpj = function(){

			if (ctrl.onBeforeUpdate) {

				ctrl.onBeforeUpdate(ctrl.cpfCnpj).then(this.executarBusca).catch(function(){});
			} else {
				this.executarBusca();
			}
		};

		this.executarBusca = function() {

			ctrl.pessoa = null;

			if(!ctrl.cpfCnpj){
				mensagem.warning('O CPF/CNPJ informado não é válido');
				return;
			}

			var buscarService = ctrl.buscarService || pessoaService.byCpfCnpj;

			buscarService(ctrl.cpfCnpj).then(

				function(response) {

					ctrl.pessoa = response.data;

					if (ctrl.pessoa || ctrl.cpfCnpj.length <= 11) {
						if (ctrl.pessoa && ctrl.pessoa.dataNascimento) {

							ctrl.pessoa.dataNascimento = ctrl.pessoa.dataNascimento.toDate();

						} else if (ctrl.pessoa && ctrl.pessoa.dataConstituicao) {

							ctrl.pessoa.dataConstituicao = ctrl.pessoa.dataConstituicao.toDate();

						}

						if (ctrl.onUpdate) {

							$timeout(function () {

								ctrl.onUpdate(ctrl.pessoa, ctrl.cpfCnpj);
							});
						}

						if (ctrl.pessoa) {

							_.forEach(ctrl.pessoa.enderecos, function (endereco) {

								if (endereco.cep) {
									endereco.cep = endereco.cep.zeroEsquerda(8);
								}
							});

						}
					} else {
						mensagem.error('CNPJ não encontrado, favor cadastrar ou atualizar o cadastro em: portalservicos.jucap.ap.gov.br');
					}
				},
				function(error){

					if (error.data.texto) {

						mensagem.error(error.data.texto);
					}
				}
			);

		};

		$scope.$on('cleanCpfCnpj', function(event){

			ctrl.cpfCnpj = null;
		});
	},

	templateUrl: 'components/buscarPessoa/buscarPessoa.html'

};

exports.directives.BuscarPessoa = BuscarPessoa;
