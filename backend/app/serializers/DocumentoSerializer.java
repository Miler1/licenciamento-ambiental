package serializers;

import java.util.Date;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class DocumentoSerializer {
	
	public static JSONSerializer uploadDocumento = SerializerUtil.create(
			"id",
			"nome",
			"dataCadastro").transform(DateSerializer.getTransformer(), Date.class);

	public static JSONSerializer uploadAnaliseDocumento = SerializerUtil.create(
			"id",
			"nomeDoArquivo"
	).transform(DateSerializer.getTransformer(), Date.class);
}
