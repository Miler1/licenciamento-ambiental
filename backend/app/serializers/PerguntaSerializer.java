package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class PerguntaSerializer {
	
	public static JSONSerializer perguntasSimplificado = SerializerUtil.create(
			"id", 
			"texto",
			"codigo",
			// "ordem",
			"localizacaoEmpreendimento",
			"tipoPergunta",
			"respostas.id",
			"respostas.texto",
			"respostas.tipoValidacao",
			"respostas.codigo",
			"respostas.permiteLicenciamento");

}
