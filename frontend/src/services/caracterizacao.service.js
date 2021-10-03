var CaracterizacaoService = function(request, config) {

	this.getTipologias = function(params) {

		return request.getWithCache(config.BASE_URL() + "tipologias", params);
	};

	this.getAtividadesPorTipologia = function(params) {

		return request.getWithCache(config.BASE_URL() + "atividades", params);
	};

	this.perguntasDLA = function(idAtividade) {

		return request.get(config.BASE_URL() + "atividades/" + idAtividade + "/perguntasDLA");
	};

	this.perguntasSimplificado = function(idEmpreendimento) {

		return request.get(config.BASE_URL() + "perguntas/" + idEmpreendimento + "/simplificado");

	};

	this.sobreposicoesCaracterizacaoZEE = function(geoJSONCaracterizacao) {

		return request.post(config.BASE_URL() + "imoveis/sobreposicoesMZEE", geoJSONCaracterizacao, null, true);

	};

	this.sobreposicoesNaoPermitidasDI = function(geometrias) {

		return request.post(config.BASE_URL() + "caracterizacoes/sobreposicoesNaoPermitidasDI", geometrias, null, true);

	};


	this.sobreposicoesNaoPermitidasSimplificado = function(geometrias) {

		return request.post(config.BASE_URL() + "caracterizacoes/sobreposicoesNaoPermitidasSimplificado", geometrias, null, true);

	};

	this.sobreposicoesAreaConsolidada = function(geoJSONCaracterizacao) {

		return request.post(config.BASE_URL() + "imoveis/sobreposicoesAreaConsolidada", {geometria: geoJSONCaracterizacao}, null, true);

	};

	this.sobreposicoesCaracterizacaoCAR = function(geoJSONCaracterizacao, codigoImovel) {

		return request.post(config.BASE_URL() + "imoveis/" + codigoImovel + "/sobreposicoesTemasCAR", geoJSONCaracterizacao, null, true);

	};

	this.excedentePassivoImovelDoEmpreendimento = function(idEmpreendimento) {

		return request.get(config.BASE_URL() + "imoveis/passivoCAR/" + idEmpreendimento, null, null, true);

	};

	this.save = function(caracterizacao) {

		return request.post(config.BASE_URL() + "caracterizacoes", caracterizacao);
	};

	this.saveSimplificado = function(caracterizacao) {

		return request.post(config.BASE_URL() + "caracterizacoes/simplificado", caracterizacao);
	};

	this.updateSimplificado = function(caracterizacao) {

		return request.put(config.BASE_URL() + "caracterizacoes/simplificado", caracterizacao);
	};

	this.list = function(idEmpreendimento, paginaAtual, itensPorPagina, ordenacao) {

		return request
			.get(config.BASE_URL() + "caracterizacoes", {
				idEmpreendimento: idEmpreendimento,
				numeroPagina: paginaAtual,
				qtdItensPorPagina: itensPorPagina,
				ordenacao: ordenacao
			});
	};

	this.baixarDla = function(idCaracterizacao) {

		window.location.href = config.BASE_URL() + "downloadDla?idCaracterizacao=" + idCaracterizacao;

	};

	this.cancelarDI = function(idCaracterizacao, cancelamento) {

		return request.put(config.BASE_URL() + "caracterizacoes/" + idCaracterizacao + "/di/cancelamento", cancelamento);
	};

	this.baixarLicenca = function(idCaracterizacao) {

		window.location.href = config.BASE_URL() + "downloadLicenca?idCaracterizacao=" + idCaracterizacao;

	};

	this.updateEtapaDocumentacao = function(id, caracterizacao) {

		return request.put(config.BASE_URL() + "caracterizacoes/" + id + "/etapaDocumentacao", caracterizacao);
	};

	this.dadosCaracterizacao = function(idCaracterizacao) {

		return request.get(config.BASE_URL() + "caracterizacoes/" + idCaracterizacao);

	};

	this.dadosRenovacao = function(idCaracterizacao) {

		return request.get(config.BASE_URL() + "caracterizacoes/renovacao/" + idCaracterizacao);

	};

	this.dadosNotificacao = function(idCaracterizacao) {

		return request.get(config.BASE_URL() + "caracterizacoes/notificacao/" + idCaracterizacao);

	};

	this.finalizarSimplificado = function(idCaracterizacao) {

		return request.post(config.BASE_URL() + "caracterizacoes/" + idCaracterizacao + "/finalizacao");
	};

	this.finalizarRenovacaoSimplificado = function(idCaracterizacao) {

		return request.post(config.BASE_URL() + "caracterizacoes/" + idCaracterizacao + "/renovar/finalizacao");
	};

	this.finalizarNotificacaoSimplificado = function(idCaracterizacao) {

		return request.post(config.BASE_URL() + "caracterizacoes/" + idCaracterizacao + "/notificacao/finalizacao");
	};

	this.downloadDaeCaracterizacao = function(idCaracterizacao) {

		window.location.href = config.BASE_URL() + "caracterizacoes/" + idCaracterizacao + "/dae/arquivo";
	};

	this.emitirDaeCaracterizacao = function(idCaracterizacao) {

		return request.post(config.BASE_URL() + "caracterizacoes/" + idCaracterizacao + "/dae/emissao");
	};

	this.dadosDae = function(idCaracterizacao) {

		return request.get(config.BASE_URL() + "caracterizacoes/" + idCaracterizacao + "/dae");
	};

	this.listCompleto = function(idCaracterizacao) {

		return request.get(config.BASE_URL() + "caracterizacoes/completo/" + idCaracterizacao);
	};

	this.getHistorico = function(idCaracterizacao) {

		return request.get(config.BASE_URL() + "caracterizacoes/historico/" + idCaracterizacao);
	};

	this.downloadDaeLicenciamentoCaracterizacao = function(idCaracterizacao) {

		window.location.href = config.BASE_URL() + "caracterizacoes/" + idCaracterizacao + "/daeLicenciamento/arquivo";
	};

	this.emitirDaeLicenciamentoCaracterizacao = function(idCaracterizacao) {

		return request.post(config.BASE_URL() + "caracterizacoes/" + idCaracterizacao + "/daeLicenciamento/emissao");
	};

	this.dadosDaeLicenciamento = function(idCaracterizacao) {

		return request.get(config.BASE_URL() + "caracterizacoes/" + idCaracterizacao + "/daeLicenciamento");
	};

	this.alterarStatusAposEditarRetificacao = function(idCaracterizacao) {
		return request.post(config.BASE_URL() + "caracterizacoes/notificacao/"+ idCaracterizacao + "/alterarStatus");
	};

	this.removerCaracterizacao = function(idCaracterizacao) {
		return request.get(config.BASE_URL() + "caracterizacoes/" + idCaracterizacao + "/remover");
	};

};

exports.services.CaracterizacaoService = CaracterizacaoService;
