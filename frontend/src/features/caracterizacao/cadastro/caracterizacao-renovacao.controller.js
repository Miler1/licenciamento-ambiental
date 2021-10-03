var RenovarCaracterizacao = function($scope, breadcrumb, $route, growlMessages, $routeParams, caracterizacaoService, empreendimentoService, $location, mensagem) {

	var renovacao = this;

	function getEmpreendimento() {

		empreendimentoService.getEmpreendimentoPorId($routeParams.idEmpreendimento).then(function (response) {

			renovacao.empreendimento = response.data;

		});
	}

	getEmpreendimento();

	function voltarCaracterizacoes(idEmpreendimento) {

		$location.path('/empreendimento/'+ idEmpreendimento +'/caracterizacoes');

	}

	renovacao.voltarCaracterizacoes = function (idEmpreendimento) {

		$location.path('/empreendimento/'+ idEmpreendimento +'/caracterizacoes');
	};

	breadcrumb.set([{title:'Empreendimento', href:'#/empreendimentos/listagem'},
			{title:'Solicitação', href:'#/empreendimento/'+ $routeParams.idEmpreendimento +'/caracterizacoes'},
			{title:'Solicitação', href:''},
			{title:'Renovação da licença', href:''}]);

	renovacao.renovarLicencaSemAlteracao = function() {

		$location.path('/empreendimento/' + $routeParams.idEmpreendimento + '/caracterizacao/' + $routeParams.idCaracterizacao + '/visualizar');
	};

	renovacao.renovarLicencaComAlteracoes = function() {

		$location.path('/empreendimentos/editar/' + $routeParams.idEmpreendimento + '/renovacao/' + $routeParams.idCaracterizacao);
	};


};

exports.controllers.RenovarCaracterizacao = RenovarCaracterizacao;