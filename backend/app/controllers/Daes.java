package controllers;

import exceptions.AppException;
import exceptions.LoginException;
import exceptions.ValidacaoException;
import play.mvc.Http.StatusCode;
import models.caracterizacao.Caracterizacao;
import models.caracterizacao.Dae;
import security.Acao;
import serializers.DaesSerializer;
import utils.Mensagem;

public class Daes extends InternalController {

	public static void emitirDaeCaracterizacao(Long id) {
		
		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Caracterizacao caracterizacao = Caracterizacao.findById(id);
		caracterizacao.empreendimento.validarSeUsuarioCadastrante();
		
		Dae dae = caracterizacao.emitirDae();
		
		if (dae.status == Dae.Status.EMITIDO) {

			MensagemResponse response = new MensagemResponse(Mensagem.CARACTERIZACAO_DAE_EMITIDO_SUCESSO, dae);
			renderJSON(response, DaesSerializer.emitirDaeCaracterizacao);

		} else if (dae.status == Dae.Status.ERRO_EMISSAO) {

			MensagemResponse res = new MensagemResponse(Mensagem.CARACTERIZACAO_ERRO_EMISSAO_DAE, dae);
			res.texto = dae.mensagemErro;
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
		
		Dae dae = Dae.findByCaracterizacao(caracterizacao);
		
		renderJSON(dae, DaesSerializer.findDaeCaracterizacao);
	}
	
	public static void downloadDaeCaracterizacao(Long id) {
		
		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Caracterizacao caracterizacao = Caracterizacao.findById(id);
		caracterizacao.empreendimento.validarSeUsuarioCadastrante();
		
		Dae dae = Dae.findByCaracterizacao(caracterizacao);
		
		renderBinary(dae.documento);
	}

}
