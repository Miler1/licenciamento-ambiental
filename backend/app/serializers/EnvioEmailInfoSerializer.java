package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class EnvioEmailInfoSerializer {

    public static JSONSerializer informacoesEnvioEmail = SerializerUtil.create(
            "id",
            "cpfCnpj",
            "emailEnviado");
}
