package controllers;

import br.ufla.lemaf.beans.pessoa.Endereco;
import br.ufla.lemaf.enums.TipoEndereco;
import models.Empreendimento;
import models.caracterizacao.Atividade;
import models.caracterizacao.Pergunta;
import models.caracterizacao.RelAtividadePergunta;
import models.caracterizacao.TipoCaracterizacaoAtividade.FiltroAtividade;
import security.Acao;
import serializers.AtividadeSerializer;
import utils.WebServiceEntradaUnica;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Atividades extends InternalController {

	public static void findAtividade() {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		FiltroAtividade filtro = new FiltroAtividade();
		filtro.licenciamentoSimplificado = getParamAsBoolean("licenciamentoSimplificado");
		filtro.idTipologia = getParamAsLong("idTipologia");
		filtro.dispensaLicenciamento = getParamAsBoolean("dispensaLicenciamento");
		String cpfCnpj = getParamAsString("cpfCnpj");

		//busca o empreendimento selecionado
		Empreendimento empreendimento = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(cpfCnpj);
		returnIfNull(empreendimento, "Empreendimento");

//		filtro.tipoEndereco = empreendimento.localizacao.id;
		Endereco endereco = empreendimento.empreendimentoEU.enderecos.stream().filter(end -> end.tipo.id == TipoEndereco.ID_PRINCIPAL).findFirst().get();

		filtro.tipoEndereco = endereco.zonaLocalizacao.codigo;

		if(cpfCnpj.length() > 11){
			filtro.listaCodigosCnaes = empreendimento.getCodigosCnaes();
		}

		List<Atividade> atividades = Atividade.findAtividades(filtro);

		renderJSON(atividades, AtividadeSerializer.findAtividade);
	}

	public static void findPerguntasDLA(Long idAtividade) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		Atividade atividade = Atividade.findById(idAtividade);

		atividade.perguntas.forEach(pergunta -> {
			pergunta.ordem = RelAtividadePergunta.getOrdem(idAtividade, pergunta.id);
		});

		Collections.sort(atividade.perguntas, new Comparator<Pergunta>() {
			@Override
			public int compare(Pergunta pergunta, Pergunta t1) {
				return pergunta.ordem.compareTo(t1.ordem);
			}
		});

		renderJSON(atividade.perguntas, AtividadeSerializer.findPerguntas);
	}

}
