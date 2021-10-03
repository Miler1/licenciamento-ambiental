package controllers;

import models.Endereco;
import models.correios.Correio;
import security.Acao;

public class Correios extends InternalController {

	public static void findEnderecoByCEP(String cep) {

		verificarPermissao(Acao.CADASTRAR_EMPREENDIMENTO);

		Endereco endereco = Correio.getEnderecoByCep(Integer.valueOf(cep));

		renderJSON(endereco);
	}

}
