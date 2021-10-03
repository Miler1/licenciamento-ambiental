package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class AtividadeSerializer {
	
	public static JSONSerializer findAtividade = SerializerUtil.create(
			"id",
			"nome",
			"codigo",
			"siglaSetor",
			"dentroEmpreendimento",
			"dentroMunicipio",
			"itemAntigo",
			"tiposAtividade.codigo",
			"tiposAtividade.nome",
			"atividadesCnae.id",
			"atividadesCnae.nome",
			"atividadesCnae.codigo",
			"parametros.id",
			"parametros.nome",
			"parametros.codigo",
			"parametros.casasDecimais",
			"parametros.descricao",
			"temLinha",
			"temPonto",
			"temPoligono",
			"licenciamentoMunicipal",
			"limiteParametroMunicipal",
			"limiteInferiorLicenciamentoSimplificado",
			"limiteSuperiorLicenciamentoSimplificado",
			"tiposLicenca.id",
			"perguntas.id",
			"perguntas.texto",
			"perguntas.codigo",
			// "perguntas.ordem",
			"perguntas.respostas.id",
			"perguntas.respostas.texto",
			"perguntas.respostas.codigo",
			"perguntas.respostas.permiteLicenciamento",
			"grupoDocumento.id",
			"limites.limiteInferior",
			"limites.limiteSuperior",
			"limites.parametroAtividade.codigo",
			"limites.parametroAtividade.id"

	);

	public static JSONSerializer findPerguntas = SerializerUtil.create(
			"id",
			"texto",
			"codigo",
			"tipoPergunta",
			"ordem",
			"ativo",
			"respostas.id",
			"respostas.texto",
			"respostas.tipoValidacao",
			"respostas.permiteLicenciamento"
	);
}
