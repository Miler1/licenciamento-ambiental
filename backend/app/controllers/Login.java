package controllers;

import exceptions.ValidacaoException;
import models.Pessoa;
import models.UsuarioLicenciamento;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.libs.WS;
import play.mvc.Before;
import play.mvc.Http.Request;
import security.AuthServiceFactory;
import security.ModelAutenticar;
import security.models.UsuarioSessao;
import security.services.Auth;
import security.services.AuthService;
import security.services.ExternalService;
import utils.Configuracoes;
import utils.Mensagem;
import utils.MensagemPortalSegurancaUtil;
import utils.WebServiceEntradaUnica;

import java.util.HashMap;
import java.util.Map;

public class Login extends GenericController {

	private static final String TEMPLATE = "public/features/login/login.html";
	public static final String APP_URL = Configuracoes.APP_URL + "#/";
	public static final String LOGIN_URL = APP_URL + "login";
	public static final String HOME_URL = APP_URL + "empreendimentos/listagem";


	private static AuthService authenticationService = new AuthServiceFactory().getInstance();

	@Before(unless = { "login", "showLogin", "logout", "getUsuarioLogado", "showLoginEntradaUnica", "getUrlEntradaUnicaPortal","getUrlEntradaUnicaCadastro","getUrlPortalAp","getUrlProdapRecuperarSenha","recuperarSenha" })
	protected static void isAutenticado() {

		if(isExternalResource()) {

			ExternalService.validateAdress(request);

		} else if(Auth.getUsuarioSessao() == null) {

			showLogin();
		}

	}

	public static void showLogin() {


		// redirect(LOGIN_URL);
	}

	public static void showLoginEntradaUnica(String sessionKeyEntradaUnica) throws Exception {

		if(sessionKeyEntradaUnica != null) {

			if(!doAuthentication(sessionKeyEntradaUnica)) {
				showLogin();
			}

			redirect(HOME_URL);

		}

	}

	private static boolean doAuthentication(String sessionKeyEntradaUnica) throws Exception {

		ModelAutenticar modelAutenticar = new ModelAutenticar();

		if(sessionKeyEntradaUnica != null) {

			modelAutenticar = authenticationService.usuarioLogadoBySessionKey(sessionKeyEntradaUnica);
		}
		else {

			modelAutenticar = authenticationService.autenticar(request);
		}

		boolean autenticado = (modelAutenticar.usuarioSessao != null);

		if(autenticado) {

//			UsuarioLicenciamento usuarioLicenciamento = UsuarioLicenciamento.findByLogin(modelAutenticar.usuarioSessao.login);
//
//			if (usuarioLicenciamento == null) {
//
//				Auth.createUsuarioLicenciamento(modelAutenticar.usuarioSessao);
//			}
//
//			models.Pessoa pessoa = Pessoa.findByCpfCnpj(modelAutenticar.usuarioSessao.login);
//
//			if(pessoa == null) {
//
//				Auth.createPessoaLicenciamento(modelAutenticar.usuarioSessao);
//			}

			Auth.setUsuarioSessao(modelAutenticar.usuarioSessao, session);

			Logger.info("[LOGIN] - Usuário autenticado [ Login: " + modelAutenticar.usuarioSessao.login + " ]");
		}
		else {

			String login = request.params.get("login");

			Logger.error("[LOGIN] - Usuário não autenticado [ Login: " + (login != null ? login : "?") + " ] ");

			throw new ValidacaoException(Mensagem.LOGIN_ENTRADA_UNICA);
		}

		return autenticado;
	}

	public static void login() {

		ModelAutenticar modelAutenticar = Auth.autenticar(Request.current(), session.current());

		if (modelAutenticar.autenticado) {

			renderJSON(Auth.getUsuarioSessao());

		} else {

			throw new ValidacaoException(modelAutenticar.mensagem);
		}
	}

	public static void logout() {

		Auth.logout(session.current());

		redirect(LOGIN_URL);

	}

	public static void getUsuarioSessao() {

		UsuarioSessao usuarioSessao = Auth.getUsuarioSessao();

		if(usuarioSessao != null) {
			renderJSON(usuarioSessao);
		}
	}

	private static boolean isExternalResource() {

		return request.path.indexOf(Play.configuration.getProperty("authentication.url.external")) != -1;

	}

	public static void getUrlEntradaUnicaPortal() {

		renderText(Configuracoes.ENTRADA_UNICA_URL_PORTAL_SEGURANCA);
	}

	public static void getUrlEntradaUnicaCadastro() {

		renderText(Configuracoes.ENTRADA_UNICA_URL_CADASTRO_UNIFICADO);
	}

	public static void getUrlPortalAp() {
		
		renderText(Configuracoes.URL_PORTAL_AP);
	}

	public static void getUrlProdapRecuperarSenha() {

		renderText(Configuracoes.URL_RECUPERAR_SENHA_PRODAP);
	}


	private static void renderLoginTemplate(String errorMsg) {

		Map<String, Object> params = null;

		if (errorMsg != null) {

			params = new HashMap<String, Object>();
			params.put("errorMsg", errorMsg);

			renderTemplate(TEMPLATE, params);

		} else {

			renderTemplate(TEMPLATE);
		}
	}

	public static UsuarioLicenciamento getAuthenticatedUser() {

		Logger.debug("ID da Sessão: %s", new Object[]{session.getId()});

		return Cache.get(session.getId(), UsuarioLicenciamento.class);

	}

	public static void recuperarSenha(String cpf){

		WebServiceEntradaUnica.resgatarSenhaViaEmail(cpf);

	}

}