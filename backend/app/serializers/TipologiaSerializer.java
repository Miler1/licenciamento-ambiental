package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class TipologiaSerializer {

    public static JSONSerializer findTipologias = SerializerUtil.create(
            "id",
            "nome",
            "codigo"
    );
}
