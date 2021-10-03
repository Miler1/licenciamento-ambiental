package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class AtividadeCnaeSerializer {
	
	public static JSONSerializer findAtividadeCnae = SerializerUtil.create(
			"id",
			"nome",
			"codigo",
			"atividades.id",
			"atividades.nome",
			"atividades.codigo",
			"atividades.temPoligono",
			"atividades.temLinha",
			"atividades.temPonto",
			"atividades.tipologia.id",
			"atividades.tipologia.nome",
			"atividades.tiposAtividade.nome",
			"atividades.tiposAtividade.codigo",
			"atividades.tiposAtividade.id",
			"atividades.tiposLicenca.id",
			"atividades.tiposLicenca.nome",
			"atividades.tiposLicenca.codigo",
			"atividades.parametros.nome",
			"atividades.parametros.codigo",
			"atividades.parametros.casasDecimais",
			"atividades.temLinha",
			"atividades.temPonto",
			"atividades.temPoligono",
			"atividades.licenciamentoMunicipal",
			"atividades.limiteParametroMunicipal",
			"atividades.limiteInferiorLicenciamentoSimplificado",
			"atividades.limiteSuperiorLicenciamentoSimplificado",
			"dispensaLicenciamento",
			"licenciamentoSimplificado",
			"licenciamentoDeclaratorio"
	);
}
