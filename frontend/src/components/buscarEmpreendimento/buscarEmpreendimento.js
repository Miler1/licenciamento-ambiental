var BuscarEmpreendimento = {

	bindings: {
		exibirCnpj: '<',
		empreendimento: '=',
		placeholderText: '@',
		onUpdate: '=',
		buscarService: '=',
		onBeforeUpdate: '='
	},

	controller: function(mensagem, $timeout, $scope,empreendimentoService) {

		var ctrl = this;

		this.buscarEmpreendimentoByCpfCnpj = function(){

			if (ctrl.onBeforeUpdate) {

				ctrl.onBeforeUpdate(ctrl.cpfCnpj).then(this.executarBusca).catch(function(){});
			} else {
				this.executarBusca();
			}
		};

		this.executarBusca = function() {

			let empreendimentoAux = {empreendedor: ctrl.empreendimento.empreendedor};

			if(!ctrl.cpfCnpj){
				mensagem.warning('O CPF/CNPJ informado não é válido');
				return;
			}

			let cpfCnpjEmp = ctrl.empreendimento.empreendedor.pessoa.cnpj || ctrl.empreendimento.empreendedor.pessoa.cpf;

			empreendimentoService.validaVinculoEmpreendedorPessoaDoEmpreendimento(ctrl.cpfCnpj, cpfCnpjEmp).then(resp => {

				if(resp.data.existeVinculo) {
					var buscarService = ctrl.buscarService;

					buscarService(ctrl.cpfCnpj).then(
						function (response) {

							if (response.data) {

								response.data.empreendedor = empreendimentoAux.empreendedor;

								ctrl.empreendimento = response.data;

								ctrl.empreendimento.enderecos = ctrl.empreendimento.empreendimentoEU.pessoa.enderecos ? ctrl.empreendimento.empreendimentoEU.pessoa.enderecos : [{
									tipo: 'ZONA_URBANA',
									correspondencia: false
								}, {correspondencia: true}];

								if (ctrl.empreendimento.empreendimentoEU.pessoa.cpf) {
									ctrl.empreendimento.empreendimentoEU.pessoa.cpf = ctrl.cpfCnpj;
								}

								if (ctrl.empreendimento.empreendimentoEU.pessoa || ctrl.cpfCnpj.length <= 11) {
									if (ctrl.empreendimento.empreendimentoEU.pessoa && ctrl.empreendimento.empreendimentoEU.pessoa.dataNascimento) {

										ctrl.empreendimento.empreendimentoEU.pessoa.dataNascimento = ctrl.empreendimento.empreendimentoEU.pessoa.dataNascimento.toDate();

									} else if (ctrl.empreendimento.empreendimentoEU.pessoa && ctrl.empreendimento.empreendimentoEU.pessoa.dataConstituicao) {

										ctrl.empreendimento.empreendimentoEU.pessoa.dataConstituicao = ctrl.empreendimento.empreendimentoEU.pessoa.dataConstituicao.toDate();

									}

									if (ctrl.onUpdate) {

										$timeout(function () {

											ctrl.onUpdate(ctrl.empreendimento, ctrl.cpfCnpj);
										});
									}

									if (ctrl.empreendimento.empreendimentoEU.pessoa) {

										_.forEach(ctrl.empreendimento.empreendimentoEU.pessoa.enderecos, function (endereco) {

											if (endereco.cep) {
												endereco.cep = endereco.cep.zeroEsquerda(8);
											}
										});

									}
								} else {
									mensagem.warning('CNPJ não encontrado, favor cadastrar ou atualizar o cadastro em: portalservicos.jucap.ap.gov.br');
									const element = document.getElementById("voltarEmpreendimento");
									element.classList.remove("ng-hide");
								}
							} else {
								mensagem.warning('CPF/CNPJ já cadastrado como empreendimento.');
								const element = document.getElementById("voltarEmpreendimento");
								element.classList.remove("ng-hide");
							}
						},
						function (error) {

							if (error.data.texto) {
								mensagem.error(error.data.texto);
							}
						}
					);
				} else {
					mensagem.warning(resp.data.mensagem);
					const element = document.getElementById("voltarEmpreendimento");
					element.classList.remove("ng-hide");
				}
			});

		};

		$scope.$on('cleanCpfCnpj', function(event){
			ctrl.cpfCnpj = null;
		});
	},

	templateUrl: 'components/buscarEmpreendimento/buscarEmpreendimento.html'

};

exports.directives.BuscarEmpreendimento = BuscarEmpreendimento;