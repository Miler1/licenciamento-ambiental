package controllers;

import models.caracterizacao.Caracterizacao;
import models.caracterizacao.Dae;
import play.mvc.Http.StatusCode;
import security.Acao;
import serializers.DaesSerializer;
import utils.Configuracoes;
import utils.Mensagem;

import java.io.File;

public class Documentos extends InternalController {

	public static void downloadTabelaValoresTaxa() {

		renderBinary(new File(Configuracoes.PATH_TABELA_TAXA_VALORES_LIENCIAMENTO));

	}

}
