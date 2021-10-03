package controllers;

import beans.ResultadoSobreposicaoCamadaVO;
import beans.UpadateStatusCaracterizacaoVO;
import exceptions.AppException;
import models.*;
import models.analise.DlaCancelada;
import models.caracterizacao.*;
import models.caracterizacao.Caracterizacao.OrdenacaoCaracterizacao;
import security.Acao;
import security.models.UsuarioSessao;
import serializers.CaracterizacaoSerializer;
import utils.Mensagem;
import utils.WebServiceEntradaUnica;
import utils.WebServiceSefaz;

import javax.script.ScriptException;
import java.io.File;
import java.util.Collections;
import java.util.List;

public class Caracterizacoes extends InternalController {

	public static void list(Long idEmpreendimento, Integer numeroPagina, Integer qtdItensPorPagina, OrdenacaoCaracterizacao ordenacao) {

		verificarPermissao(Acao.LISTAR_CARACTERIZACAO);

		Empreendimento empreendimento = Empreendimento.findById(idEmpreendimento);
		empreendimento.validarSeUsuarioCadastrante();

		List<Caracterizacao> caracterizacaos = Caracterizacao.list(idEmpreendimento, numeroPagina, qtdItensPorPagina, ordenacao);
		Long totalResults = Caracterizacao.countByEmpreendimento(idEmpreendimento);

		//TODO: FAZER ALTERAÇÃO DA TAXA DE EXPEDIENTE NO CONFIGURADOR DO LICENCIAMENTO
		caracterizacaos.forEach(caracterizacao -> {

			boolean licencasPermitidas = WebServiceSefaz.islicencasPermitidas(caracterizacao);

			if (caracterizacao.dae != null) {

				if (caracterizacao.tipoLicenca.id.equals(TipoLicenca.DISPENSA_LICENCA_AMBIENTAL)) {
					caracterizacao.dae.valor = 30.0;
				} else if (licencasPermitidas) {
					caracterizacao.dae.valor = 100.0;
				}

//				caracterizacao.dae._save();

			}

		});

		Pagination<Caracterizacao> paginaItems = new Pagination<Caracterizacao>(totalResults, caracterizacaos);

		renderJSON(paginaItems, CaracterizacaoSerializer.listGrid);

	}

	public static void create(Caracterizacao caracterizacao) throws Exception {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Caracterizacao caracterizacaoSalva = caracterizacao.salvar();

		renderMensagem(Mensagem.CARACTERIZACAO_CADASTRADA_SUCESSO, caracterizacaoSalva, CaracterizacaoSerializer.save);
	}

	public static void createSimplificado(Caracterizacao caracterizacao) throws ScriptException {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Caracterizacao caracterizacaoSalva = caracterizacao.salvar();

		renderJSON(caracterizacaoSalva, CaracterizacaoSerializer.createSimplificado);
	}

	public static void updateSimplificado(Caracterizacao caracterizacao) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Caracterizacao caracterizacaoSalva = Caracterizacao.findById(caracterizacao.id);

		caracterizacaoSalva.update(caracterizacao);

		renderJSON(caracterizacaoSalva, CaracterizacaoSerializer.updateSimplificado);
	}

	public static void updateEtapaDocumentacao(Long id, Caracterizacao caracterizacaoAtualizada) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		returnIfNull(caracterizacaoAtualizada, "Caracterizacao");

		Caracterizacao caracterizacao = Caracterizacao.findById(id);

		returnIfNull(caracterizacao, "Caracterizacao");

		caracterizacao.updateEtapaDocumentacao(caracterizacaoAtualizada);
	}

	public static void downloadDla(Long idCaracterizacao) throws Exception {

		Caracterizacao caracterizacao = Caracterizacao.findById(idCaracterizacao);
		caracterizacao.empreendimento.validarSeUsuarioCadastrante();

		DispensaLicenciamento dispensa = DispensaLicenciamento.findById(caracterizacao.dispensa.id);

		Documento documento = Documento.findById(dispensa.documento.id);

		File file = documento.getFile();


		response.setHeader("Content-Type", "application/x-download");
		response.setHeader("Content-Disposition", "attachment; filename=DDLA.pdf");

		if(!file.exists()) {
			dispensa.gerarPDFDispensa();
			file = ((Documento) Documento.findById(dispensa.documento.id)).getFile();
			renderBinary(file, file.getName());
		}
		else {
			file = ((Documento) Documento.findById(dispensa.documento.id)).getFile();
			renderBinary(dispensa.regerarPdf(), file.getName());
		}
	}

	public static void findDadosCaracterizacao(Long id) throws ScriptException {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		renderJSON(Caracterizacao.findDadosCaracterizacao(id), CaracterizacaoSerializer.findDadosCaracterizacao);
	}

	public static void findDadosRenovacao(Long id) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		renderJSON(Caracterizacao.findDadosRenovacao(id), CaracterizacaoSerializer.findDadosCaracterizacao);
	}

	public static void findDadosNotificacao(Long id) {

		verificarPermissao(Acao.LISTAR_CARACTERIZACAO);

		renderJSON(Caracterizacao.findDadosNotificacao(id), CaracterizacaoSerializer.findDadosNotificacao);
	}

	public static void finalizar(Long id) throws ScriptException {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Caracterizacao caracterizacao = Caracterizacao.findById(id);
		caracterizacao.finalizar();

		renderMensagem(Mensagem.CARACTERIZACAO_FINALIZADA_SUCESSO, Collections.singletonList(caracterizacao),
				CaracterizacaoSerializer.finalizar);

	}

	public static void finalizarRenovacao(Long id) throws ScriptException {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Caracterizacao caracterizacao = Caracterizacao.findById(id);
		caracterizacao.finalizar();

		renderMensagem(Mensagem.CARACTERIZACAO_FINALIZADA_SUCESSO, Collections.singletonList(caracterizacao),
				CaracterizacaoSerializer.finalizar);
	}

	public static void finalizarNotificacao(Long id) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Caracterizacao caracterizacao = Caracterizacao.findById(id);
		caracterizacao.finalizarNotificacao();

		renderMensagem(Mensagem.CARACTERIZACAO_FINALIZADA_SUCESSO, Collections.singletonList(caracterizacao),
				CaracterizacaoSerializer.finalizar);
	}

	public static void alterarStatusPosNotificacaoEmpreendimento (Long  id){

		Caracterizacao caracterizacao = Caracterizacao.findById(id);
		caracterizacao.alterarStatusAposEditarRetificacaoDoEmpreendimento(caracterizacao);
	}

	public static void downloadLicenca(Long idCaracterizacao) throws Exception {

		Licenca licenca = Licenca.find("byCaracterizacaoAndAtivo", Caracterizacao.findById(idCaracterizacao), true).first();
		licenca.caracterizacao.empreendimento.validarSeUsuarioCadastrante();

		licenca.caracterizacao.cadastrante =  Pessoa.convert(WebServiceEntradaUnica.findPessoaByCpfCnpjEU(licenca.caracterizacao.cpfCnpjCadastrante));

		if(licenca.isSuspensa())
			throw new AppException(Mensagem.LICENCA_CANCELADA_OU_SUSPENSA);

		File file = null;

		Licenca.gerarPdfLicenca(licenca);
		file = ((Documento) Documento.findById(licenca.documento.id)).getFile();

		renderBinary(file, file.getName());

	}

	public static void findCaracterizacoesEmAnalise() {
		renderJSON(Caracterizacao.findCaracterizacoesEmAnalise(), CaracterizacaoSerializer.emAnalise);
	}

	public static void adicionarCaracterizacoesEmAnalise(Long...ids) {

		if(ids != null) {

			for(Long id : ids) {

				Caracterizacao caracterizacao = Caracterizacao.findById(id);
				caracterizacao.setEmAnalise(true);
			}

		}

	}

	public static void cancelarDI (Long id, DlaCancelada dlaCancelada){

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		returnIfNull(dlaCancelada, "DlaCancelada");

		UsuarioSessao usuarioSessao = getUsuarioSessao();

		UsuarioLicenciamento usuarioExecutor = UsuarioLicenciamento.findByLogin(usuarioSessao.login);

		Caracterizacao caracterizacao = Caracterizacao.findById(id);
		caracterizacao.empreendimento.validarSeUsuarioCadastrante();

		dlaCancelada.dispensaLicenciamento = caracterizacao.dispensa;

		dlaCancelada.cancelarDla(usuarioExecutor);

		renderMensagem(Mensagem.LICENCA_CANCELADA_SUCESSO);
	}

	public static void possuiSobreposicoesNaoPermitidasDI(List<String> geometrias) {

		for(String geometria : geometrias) {
			List<ResultadoSobreposicaoCamadaVO> sobreposicoes = Caracterizacao.buscarSobreposicoesNaoPermitidasDI(geometria);
			if(sobreposicoes.size() > 0) {
				renderJSON(true);
			}
		}

		renderJSON(false);
	}

	public static void possuiSobreposicoesNaoPermitidasSimplificado(List<String> geometrias) {

		for(String geometria : geometrias) {
			List<ResultadoSobreposicaoCamadaVO> sobreposicoes = Caracterizacao.buscarSobreposicoesNaoPermitidasSimplificado(geometria);
			if(sobreposicoes.size() > 0) {
				renderJSON(true);
			}
		}

		renderJSON(false);
	}

	public static void listCompleto(Long id) throws ScriptException {
		verificarPermissao(Acao.LISTAR_CARACTERIZACAO);

		Caracterizacao caracterizacao = Caracterizacao.findById(id);
		caracterizacao.empreendimento.validarSeUsuarioCadastrante();

		if ((caracterizacao.tipoLicenca != null && caracterizacao.tipoLicenca.siglaLicenca())
				|| (caracterizacao.tiposLicencaEmAndamento != null && caracterizacao.tiposLicencaEmAndamento.stream().anyMatch(tlea -> tlea.siglaLicenca()))) {
			caracterizacao = Caracterizacao.findDadosCaracterizacao(id);
		}

		if( (caracterizacao.tipoLicenca != null) && (!caracterizacao.tipoLicenca.sigla.equals("DDLA"))){
			caracterizacao.tipoLicenca.valorDae = TaxaLicenciamento.calcular(caracterizacao, caracterizacao.tipoLicenca);
		}

		caracterizacao.numeroLicenca = caracterizacao.getNumeroLicenca();
		caracterizacao.empreendimento = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(caracterizacao.empreendimento.cpfCnpj);
		caracterizacao.nomePessoaEmpreendimento = caracterizacao.empreendimento.empreendimentoEU.pessoa.nome != null ? caracterizacao.empreendimento.empreendimentoEU.pessoa.nome : caracterizacao.empreendimento.empreendimentoEU.pessoa.razaoSocial;
		caracterizacao.cpfCnpjPessoaEmpreendimento = caracterizacao.empreendimento.cpfCnpj;

		br.ufla.lemaf.beans.Empreendimento empEU = WebServiceEntradaUnica.findEmpreendimentosEU(caracterizacao.empreendimento);
		caracterizacao.empreendimento = Empreendimento.convert(empEU);

		Long idAtividade = caracterizacao.atividadesCaracterizacao.get(0).atividade.id;

		caracterizacao.respostas.forEach(resposta -> {
			resposta.pergunta.ordem = RelAtividadePergunta.getOrdem(idAtividade, resposta.pergunta.id);
		});

		renderJSON(caracterizacao, CaracterizacaoSerializer.completo);
	}

	public static void updateStatus(UpadateStatusCaracterizacaoVO upadateStatusCaracterizacaoVO) {

		Caracterizacao.updateStatus(upadateStatusCaracterizacaoVO);
		ok();
	}

	public static void getHistoricos(Long id){
		verificarPermissao(Acao.LISTAR_CARACTERIZACAO);

		renderJSON(Caracterizacao.getHistoricos(id, getUsuarioSessao().nome), CaracterizacaoSerializer.historico);
	}

	public static void remover(Long id) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Caracterizacao caracterizacao = Caracterizacao.findById(id);
		caracterizacao.remover();

		renderMensagem(Mensagem.CARACTERIZACAO_REMOVIDA_SUCESSO);

	}

}