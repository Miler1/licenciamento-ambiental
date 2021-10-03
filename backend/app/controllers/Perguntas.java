package controllers;

import java.util.List;

import models.Empreendimento;
import models.caracterizacao.Pergunta;
import serializers.PerguntaSerializer;

public class Perguntas extends InternalController {
	
	public static void perguntasSimplificado(Long idEmpreendimento) {
		
		Empreendimento empreendimento = Empreendimento.findById(idEmpreendimento);
		
		List<Pergunta> perguntas = Pergunta
				.find("localizacaoEmpreendimento", empreendimento.localizacao)
				.fetch();
		
		renderJSON(perguntas, PerguntaSerializer.perguntasSimplificado);
		
	}

}
