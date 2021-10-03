package controllers;

import models.Pessoa;
import security.Acao;
import serializers.PessoasSerializer;
import utils.WebServiceEntradaUnica;

public class Pessoas extends InternalController{
	
	public static void findByCpfCnpj(String cpfCnpj) {
		
		verificarPermissao(Acao.CADASTRAR_EMPREENDIMENTO);

		br.ufla.lemaf.beans.pessoa.Pessoa pessoa = WebServiceEntradaUnica.findPessoaByCpfCnpjEU(cpfCnpj);

		if(pessoa != null){

			pessoa.enderecos.forEach(endereco -> {
				if(endereco.zonaLocalizacao.descricao.equals("Urbana") && endereco.numero == null){
					endereco.semNumero = true;
				}
			});
		}

		renderJSON(pessoa, PessoasSerializer.findByCpfCnpj);
		
	}
	
	public static void findByCpf(String cpf) {
		
		verificarPermissao(Acao.CADASTRAR_EMPREENDIMENTO);
		
		Pessoa pessoa = (Pessoa) WebServiceEntradaUnica.findPessoaByCpfCnpj(cpf);
		if(pessoa != null) {
			pessoa.enderecos.forEach(endereco -> {
				if (endereco.zonaLocalizacao.descricao.equals("Urbana") && endereco.numero == null) {
					endereco.semNumero = true;
				}
			});
		}
		
		renderJSON(pessoa, PessoasSerializer.findByCpf);
		
	}

}
