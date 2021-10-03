var ListagemEmpreendimentos = function($scope, mensagem, $location, breadcrumb, empreendimentoService, modalSimplesService, config) {

	var listagem = this;

	listagem.pesquisar = pesquisar;
	listagem.onPaginaAlterada = onPaginaAlterada;
	listagem.buscarEmpreendimento = buscarEmpreendimento;
	listagem.limparPesquisa = limparPesquisa;
	listagem.alterarEmpreendimento = alterarEmpreendimento;
	listagem.removerEmpreendimento = removerEmpreendimento;
	listagem.confirmarRemoverEmpreendimento = confirmarRemoverEmpreendimento;
	listagem.cadastrarCaracterizacao = cadastrarCaracterizacao;

	listagem.empreendimentos = [];
	listagem.campoPesquisa = "";
	listagem.empreendimentoVisualizar = {};

	listagem.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);

	function onPaginaAlterada() {

		pesquisar(null);
	}

	function atualizarLista(response) {

		listagem.empreendimentos = response.data.pageItems;
		listagem.paginacao.update(response.data.totalResults, listagem.paginacao.paginaAtual);
	}

	function pesquisar(pagina, textoMensagem) {

		var pesquisa = listagem.campoPesquisa;

		if(pagina) {
			listagem.paginacao.paginaAtual = pagina;
		}

		if (pesquisa.isPartOfCpfCnpj()) {

			pesquisa = pesquisa.deixarSomenteNumeros();
		}

		empreendimentoService.list(
			pesquisa,
			listagem.paginacao.paginaAtual,
			listagem.paginacao.itensPorPagina
		).then(function(response) {
			atualizarLista(response);
			if(textoMensagem) {
				mensagem.success(textoMensagem);
			}
		})
		.catch(function(){
			mensagem.error("Ocorreu um erro ao buscar a lista de empreendimentos.");
		});
	}

	function limparPesquisa() {

		listagem.campoPesquisa = '';

		empreendimentoService.list(
			'',
			1,
			listagem.paginacao.itensPorPagina
		).then(atualizarLista)
		.catch(function(){
			mensagem.error("Ocorreu um erro ao buscar a lista de empreendimentos.");
		});

	}

	listagem.caracterizacoes = function (idEmpreendimento) {
		$location.path('/empreendimento/'+ idEmpreendimento +'/caracterizacoes');
	};

	function alterarEmpreendimento(id) {

		$location.path("/empreendimentos/editar/"+id);
	}

	function removerEmpreendimento(empreendimento) {

		if(empreendimento.removivel) {

			var configModal = {
				titulo: 'Confirmar exclusão de empreendimento',
				conteudo: 'Tem certeza que deseja excluir esse empreendimento? Ao selecionar a opção "Sim" a ação não poderá ser revertida.'
			};

			var instanciaModal = modalSimplesService.abrirModal(configModal);

			instanciaModal.result
				.then(function(){
					 listagem.confirmarRemoverEmpreendimento(empreendimento);
				});
		}
	}

	function confirmarRemoverEmpreendimento(empreendimento) {

		empreendimentoService.delete(empreendimento.id)
			.then(function(response){
				listagem.pesquisar(null, response.data.texto);
			})
			.catch(function(response){
				mensagem.error(response.data.texto);
			});
	}

	function cadastrarCaracterizacao(idEmpreendimento) {

		$location.path('/empreendimento/' + idEmpreendimento + '/caracterizacoes/cadastrar');
	}

	function buscarEmpreendimento(cpfCnpj) {

		empreendimentoService.buscarEmpreendimentoCompleto(cpfCnpj)
			.then(function(response) {

				listagem.empreendimentoVisualizar = response.data;
				listagem.empreendimentoVisualizar.exibir = true;
				$('#modalVisualizar').modal("show");

				$('#modalVisualizar').one('hide.bs.modal', function (e) {

					listagem.empreendimentoVisualizar = {};
					$scope.$broadcast('destroyMap');

				});

			})
			.catch(function() {

				mensagem.error('Ocorreu um erro ao buscar as informações do o empreendimento.');

			});

	}


	this.pesquisar(null);

	breadcrumb.set([{title:'Empreendimento', href:''}]);

};

exports.controllers.ListagemEmpreendimentos = ListagemEmpreendimentos;
