package controllers;

import br.ufla.lemaf.beans.pessoa.Endereco;
import br.ufla.lemaf.enums.TipoEndereco;
import models.Empreendimento;
import models.caracterizacao.TipoCaracterizacaoAtividade.FiltroAtividade;
import models.caracterizacao.Tipologia;
import models.caracterizacao.Tipologia.FiltroTipologia;
import security.Acao;
import serializers.TipologiaSerializer;
import utils.WebServiceEntradaUnica;

import java.util.List;

public class Tipologias extends InternalController {

	public static void list() {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		FiltroTipologia filtro = new FiltroTipologia();
		filtro.licenciamentoSimplificado = getParamAsBoolean("licenciamentoSimplificado");
		filtro.dispensaLicenciamento = getParamAsBoolean("dispensaLicenciamento");
		String cpfCnpj = getParamAsString("cpfCnpj");

		Empreendimento empreendimento = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(cpfCnpj);
		returnIfNull(empreendimento, "Empreendimento");

//		Integer tipoEndereco = empreendimento.localizacao.id;
		Endereco endereco = empreendimento.empreendimentoEU.enderecos.stream().filter(end -> end.tipo.id == TipoEndereco.ID_PRINCIPAL).findFirst().get();

		filtro.tipoEndereco = endereco.zonaLocalizacao.codigo;

		if(cpfCnpj.length() > 11){
			filtro.listaCodigosCnaes = empreendimento.getCodigosCnaes();
		}

		List<Tipologia> tipologias = Tipologia.findTipologias(filtro);

		renderJSON(tipologias, TipologiaSerializer.findTipologias);

	}

}
