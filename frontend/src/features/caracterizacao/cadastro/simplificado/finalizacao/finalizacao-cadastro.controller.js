var CaracterizacaoLASFinalizacaoController = function($scope, $routeParams, breadcrumb, $rootScope, $location, caracterizacaoService, mensagem) {

	var finalizacao = this;

	finalizacao.goCaracterizacoes = goCaracterizacoes;
	finalizacao.processaDae = processaDae;
	
	finalizacao.idEmpreendimento = $routeParams.id;
	finalizacao.caracterizacoes = $rootScope.caracterizacoes;

	finalizacao.status = {
		EMITIDO: 'EMITIDO'
	};

	function goCaracterizacoes() {

		$location.path('/empreendimento/' + $routeParams.id + '/caracterizacoes');
	}

	function processaDae(caracterizacao) {

		if (caracterizacao.dae.status === finalizacao.status.EMITIDO) {

			downloadDae(caracterizacao.id);
		} else {

			emitirDae(caracterizacao);
		}

	}

	function emitirDae(caracterizacao) {

		var idCaracterizacao = caracterizacao.id;

		caracterizacaoService.emitirDaeCaracterizacao(idCaracterizacao)
			.then(function(response){

				caracterizacao.dae = response.data.dados;

				downloadDae(idCaracterizacao);
			})
			.catch(function(response){

				mensagem.error(response.data.texto, {ttl: 10000});
			});
	}

	function downloadDae(idCaracterizacao) {

		caracterizacaoService.downloadDaeCaracterizacao(idCaracterizacao);
	}

	function initFinalizacaoCadastro() {

		if (finalizacao.caracterizacoes) {

			finalizacao.caracterizacao = finalizacao.caracterizacoes[0];
		} else {

			goCaracterizacoes();
		}
	}

	initFinalizacaoCadastro();

	breadcrumb.set([{title:'Empreendimento', href:'#/empreendimentos/listagem'},
		{title:'Solicitação', href:'#/empreendimento/'+ $routeParams.id +'/caracterizacoes'},
		{title:'Finalizada', href:''}]);
};

exports.controllers.CaracterizacaoLASFinalizacaoController = CaracterizacaoLASFinalizacaoController;