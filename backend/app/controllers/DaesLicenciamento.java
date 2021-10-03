package controllers;

import models.caracterizacao.Caracterizacao;
import models.caracterizacao.Dae;
import models.caracterizacao.DaeLicenciamento;
import play.mvc.Http.StatusCode;
import security.Acao;
import serializers.DaesSerializer;
import utils.Mensagem;

public class DaesLicenciamento extends InternalController {

	public static void emitirDaeCaracterizacao(Long id) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Caracterizacao caracterizacao = Caracterizacao.findById(id);
		caracterizacao.empreendimento.validarSeUsuarioCadastrante();

		DaeLicenciamento daeLicenciamento = caracterizacao.emitirDaeLicenciamento();

		if (daeLicenciamento.status == DaeLicenciamento.Status.EMITIDO) {

			MensagemResponse response = new MensagemResponse(Mensagem.CARACTERIZACAO_DAE_EMITIDO_SUCESSO, daeLicenciamento);
			renderJSON(response, DaesSerializer.emitirDaeCaracterizacao);

		} else if (daeLicenciamento.status == DaeLicenciamento.Status.ERRO_EMISSAO) {

			MensagemResponse res = new MensagemResponse(Mensagem.CARACTERIZACAO_ERRO_EMISSAO_DAE, daeLicenciamento);
			res.texto = daeLicenciamento.mensagemErro;
			response.status = StatusCode.INTERNAL_ERROR;
			renderJSON(res, DaesSerializer.findDaeEmissaoErro);

		} else {

			response.status = StatusCode.INTERNAL_ERROR;
			renderMensagem(Mensagem.CARACTERIZACAO_ERRO_EMISSAO_DAE);
		}
	}

	public static void findDaeCaracterizacao(Long id) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Caracterizacao caracterizacao = Caracterizacao.findById(id);
		caracterizacao.empreendimento.validarSeUsuarioCadastrante();

		DaeLicenciamento daeLicenciamento = DaeLicenciamento.findByCaracterizacao(caracterizacao);
		daeLicenciamento.valor = caracterizacao.valorTaxaLicenciamento;

		if (daeLicenciamento.valor == 0) {
			daeLicenciamento.isento = true;
		}

		daeLicenciamento._save();

		renderJSON(daeLicenciamento, DaesSerializer.findDaeCaracterizacao);
	}

	public static void downloadDaeCaracterizacao(Long id) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Caracterizacao caracterizacao = Caracterizacao.findById(id);
		caracterizacao.empreendimento.validarSeUsuarioCadastrante();

		DaeLicenciamento daeLicenciamento = DaeLicenciamento.findByCaracterizacao(caracterizacao);

		renderBinary(daeLicenciamento.documento);
	}

}
