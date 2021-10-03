package controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import exceptions.AppException;
import models.analise.Documento;
import models.analise.Notificacao;
import models.caracterizacao.Caracterizacao;
import play.data.Upload;
import security.Acao;
import serializers.DocumentoSerializer;
import serializers.NotificacaoSerializer;
import utils.Mensagem;

public class Notificacoes extends InternalController {

	public static void listByCaracterizacao(Long idCaracterizacao) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Caracterizacao caracterizacao = Caracterizacao.findById(idCaracterizacao);

		List<Notificacao> notificacoes = Notificacao.findByCaracterizacao(caracterizacao);

		renderJSON(notificacoes, NotificacaoSerializer.listByCaracterizacao);
	}

	public static void findAtivaByCaracterizacao(Long idCaracterizacao) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Caracterizacao caracterizacao = Caracterizacao.findById(idCaracterizacao);

		Notificacao notificacao = Notificacao.findAtivaByCaracterizacao(caracterizacao);

		renderJSON(notificacao, NotificacaoSerializer.listByCaracterizacao);
	}

	public static void read(Long idCaracterizacao) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Caracterizacao caracterizacao = Caracterizacao.findById(idCaracterizacao);

		List<Notificacao> notificacoes = Notificacao.findByCaracterizacao(caracterizacao);

		Notificacao.setDataLeitura(notificacoes);

		renderMensagem(Mensagem.LEITURA_REGISTRADA_SUCESSO);
	}

	public static void uploadDocumento(Long idNotificacao, Upload file) throws IOException {
		
		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);
		
		returnIfNull(file, "Upload");
		
		Notificacao notificacao = Notificacao.findById(idNotificacao);
		
		renderJSON(notificacao.saveDocumento(file.asFile()), DocumentoSerializer.uploadAnaliseDocumento);
	}
	
	public static void downloadDocumento(Long idDocumento) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);
		Documento documento = Documento.findById(idDocumento);

		if (documento == null) {
			throw new AppException(Mensagem.DOCUMENTO_DA_SOLICITACAO_NAO_ENCONTRADO);
		}

		File file = documento.getFile();
		renderBinary(file, file.getName());
	}
	
	public static void deleteDocumento(Long idNotificacao, Long idDocumento) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Notificacao notificacao = Notificacao.findById(idNotificacao);

		notificacao.deleteDocumento(idDocumento);

		renderMensagem(Mensagem.DOCUMENTO_REMOVIDO_SUCESSO);
	}

	public static void justificarRetificacao(Long idNotificacao, String justificativa) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Notificacao notificacao = Notificacao.findById(idNotificacao);
		notificacao.justificarRetificacao(justificativa);

		renderMensagem(Mensagem.JUSTIFICATIVA_ADICIONADA_SUCESSO);

	}

	public static void justificarRetificacaoEmpreendimento(Long idNotificacao, String justificativa) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Notificacao notificacao = Notificacao.findById(idNotificacao);
		notificacao.justificarRetificacaoEmpreendimento(justificativa);

		renderMensagem(Mensagem.JUSTIFICATIVA_ADICIONADA_SUCESSO);

	}

	public static void removerJustificativa(Long idNotificacao) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Notificacao notificacao = Notificacao.findById(idNotificacao);
		notificacao.removerJustificativa();

		renderMensagem(Mensagem.JUSTIFICATIVA_REMOVIDA_SUCESSO);

	}

	public static void justificarDocumentacao(Long idNotificacao, String justificativa) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Notificacao notificacao = Notificacao.findById(idNotificacao);
		notificacao.justificarDocumentacao(justificativa);

		renderMensagem(Mensagem.JUSTIFICATIVA_ADICIONADA_SUCESSO);

	}


	public static void downloadAnaliseDocumento(Long idDocumento) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		models.analise.Documento documento = models.analise.Documento.findById(idDocumento);

		if (documento == null) {

			throw new AppException(Mensagem.SOLICITACAO_DOCUMENTO_NAO_ENCONTRADA);
		}

		File file = documento.getFile();

		renderBinary(file, file.getName());
	}

}
