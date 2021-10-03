package serializers;

import utils.SerializerUtil;
import flexjson.JSONSerializer;

public class DaesSerializer {

	public static JSONSerializer emitirDaeCaracterizacao = SerializerUtil.create(
			"texto",
			"dados.id",
			"dados.valor",
			"dados.numero",
			"dados.status",
			"dados.dataEmissao",
			"dados.dataVencimento",
			"dados.isento");
	
	public static JSONSerializer findDaeCaracterizacao = SerializerUtil.create(
			"id",
			"valor",
			"numero",
			"status",
			"dataEmissao",
			"dataVencimento",
			"isento"
			// "codigoReceita"
			);

	public static JSONSerializer findDaeEmissaoErro = SerializerUtil.create(
			"texto"
	);
}
