package serializers;

import utils.SerializerUtil;
import flexjson.JSONSerializer;

public class NotificacaoSerializer {

	public static JSONSerializer listByCaracterizacao = SerializerUtil.createWithDateTime(
		"id",
		"analiseGeo.parecer",
		"analiseJuridica.parecer",
		"analiseTecnica.parecer",
		"analiseDocumento.parecer",
		"analiseDocumento.documento.id",
		"analiseDocumento.documento.tipo.nome",
		"documentoCorrigido",
		"resolvido",
		"justificativa",
		"dataCadastro"
	);
}
