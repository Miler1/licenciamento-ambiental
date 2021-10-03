package controllers;

import java.io.File;
import java.io.IOException;

import exceptions.AppException;
import models.caracterizacao.SolicitacaoDocumentoCaracterizacao;
import play.data.Upload;
import security.Acao;
import serializers.DocumentoSerializer;
import utils.Mensagem;

public class SolicitacoesDocumentosCaracterizacao extends InternalController {

	private static SolicitacaoDocumentoCaracterizacao getSolicitacaoDocumentoCaracterizacao(Long id) {

		SolicitacaoDocumentoCaracterizacao solicitacao = SolicitacaoDocumentoCaracterizacao.findById(id);

		if (solicitacao == null){

			throw new AppException(Mensagem.SOLICITACAO_DOCUMENTO_NAO_ENCONTRADA);
		}

		return solicitacao;
	}

	public static void uploadDocumento(Long idSolicitacao, Upload file) throws IOException {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		returnIfNull(file, "Upload");

		SolicitacaoDocumentoCaracterizacao solicitacao = getSolicitacaoDocumentoCaracterizacao(idSolicitacao);

		renderJSON(solicitacao.saveDocumento(file.asFile()), DocumentoSerializer.uploadDocumento);
	}


	public static void downloadDocumento(Long idSolicitacao) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		SolicitacaoDocumentoCaracterizacao solicitacao = getSolicitacaoDocumentoCaracterizacao(idSolicitacao);

		if (solicitacao.documento == null) {

			throw new AppException(Mensagem.DOCUMENTO_DA_SOLICITACAO_NAO_ENCONTRADO);
		}

		File file = solicitacao.getFile();

		renderBinary(file, file.getName());		
	}

	public static void deleteDocumento(Long idSolicitacao) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		SolicitacaoDocumentoCaracterizacao solicitacao = getSolicitacaoDocumentoCaracterizacao(idSolicitacao);

		solicitacao.deleteDocumento();

		renderMensagem(Mensagem.DOCUMENTO_REMOVIDO_SUCESSO);
	}

	public static void desvincularDocumento(Long idSolicitacao) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		SolicitacaoDocumentoCaracterizacao solicitacao = getSolicitacaoDocumentoCaracterizacao(idSolicitacao);

		solicitacao.desvincularDocumento();

		renderMensagem(Mensagem.DOCUMENTO_REMOVIDO_SUCESSO);
	}
}
