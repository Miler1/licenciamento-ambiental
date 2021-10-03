package controllers;

import br.ufla.lemaf.beans.Empreendedor;
import br.ufla.lemaf.beans.pessoa.Pessoa;
import br.ufla.lemaf.beans.pessoa.ZonaLocalizacao;
import security.Acao;
import serializers.EmpreendedorSerializer;
import utils.WebServiceEntradaUnica;

import java.util.ArrayList;

public class Empreendedores extends InternalController {
	

	public static void findByCpfCnpj(String cpfCnpj) {
		
		verificarPermissao(Acao.CADASTRAR_EMPREENDIMENTO);

		Empreendedor empreendedor = new Empreendedor();

		empreendedor.pessoa = WebServiceEntradaUnica.findPessoaByCpfCnpjEU(cpfCnpj) ;

		if (empreendedor.pessoa != null) {

			empreendedor.pessoa.enderecos.forEach(endereco -> {
				if(endereco.zonaLocalizacao.descricao.equals("Urbana") && endereco.numero == null){
					endereco.semNumero = true;
				}
			});
		}

		renderJSON(empreendedor, EmpreendedorSerializer.findByCpfCnpj);
		
	}
}
