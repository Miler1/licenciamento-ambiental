package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class EstadoCivilSerializer {

	public static JSONSerializer findAll = SerializerUtil.create(
			"id",
			"nome",
			"codigo");
}
