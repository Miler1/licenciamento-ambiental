package security.services;

import br.ufla.lemaf.beans.pessoa.Pessoa;
import exceptions.ValidacaoException;
import br.ufla.lemaf.beans.pessoa.Usuario;
import models.EntradaUnica.Perfil;
import models.UsuarioLicenciamento;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.mvc.Http;
import play.mvc.Scope.Session;
import security.AuthServiceFactory;
import security.ModelAutenticar;
import security.models.UsuarioSessao;
import utils.Mensagem;
import utils.WebServiceEntradaUnica;

public class Auth {

	private static final String CACHE_PREFIX = "LIC_USER_";

	public static ModelAutenticar autenticar(Http.Request request, Session session) {

		AuthService authService = new AuthServiceFactory().getInstance();

		if(request.params == null)
			return null;

		String login = request.params.get("login").replace(".","").replace("-","");

		//Tentativa de login sem adicionar perfil. Caso não consiga tenta adicionar perfil e logar novamente
		ModelAutenticar modelAutenticar = authService.autenticar(request);

		if (modelAutenticar.usuarioSessao == null) {

			adicionarPerfilExterno(login);

			modelAutenticar = authService.autenticar(request);
		}

		if (modelAutenticar.usuarioSessao == null) {

			modelAutenticar.autenticado = false;
			return modelAutenticar;
		}

		UsuarioLicenciamento usuarioLicenciamento = UsuarioLicenciamento.findByLogin(modelAutenticar.usuarioSessao.login);

		if (usuarioLicenciamento == null) {

			createUsuarioLicenciamento(modelAutenticar.usuarioSessao);
		}
		
		Logger.debug("ID da Sessão: %s", new Object[]{session.getId()});

		Cache.set(CACHE_PREFIX + session.getId(), modelAutenticar.usuarioSessao, Play.configuration.getProperty("application.session.maxAge"));

		return modelAutenticar;
	}

	public static UsuarioLicenciamento createUsuarioLicenciamento(UsuarioSessao usuario) {

		UsuarioLicenciamento usuarioLicenciamento = new UsuarioLicenciamento();

		usuarioLicenciamento.login = usuario.login;

		usuarioLicenciamento.save();

		return usuarioLicenciamento;
	}


	private static void adicionarPerfilExterno(String username) {

		WebServiceEntradaUnica.adicionarPerfilPeloCpfCnpj(username, Perfil.EXTERNO.codigo);
	}

	public static void logout(Session session) {

		String key = CACHE_PREFIX +  session.getId();

		if (Cache.get(key) != null)
			Cache.delete(key);

		session.current().clear();
	}

	public static UsuarioSessao getAuthenticatedUser(Session session) {

		return Cache.get(CACHE_PREFIX + session.current().getId(), UsuarioSessao.class);
	}

	public static UsuarioSessao getUsuarioSessao() {

		return getAuthenticatedUser(Session.current());
	}

	public static void setUsuarioSessao(UsuarioSessao usuario, Session session) {

		Cache.set(CACHE_PREFIX +  session.getId(), usuario);
	}

}