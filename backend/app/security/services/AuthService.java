package security.services;

import play.mvc.Http;
import security.ModelAutenticar;

public interface AuthService {

	ModelAutenticar autenticar(Http.Request request);

	ModelAutenticar usuarioLogadoBySessionKey(String sessionKey);
}
