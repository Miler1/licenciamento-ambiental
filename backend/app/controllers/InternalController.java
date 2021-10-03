package controllers;

import play.mvc.Before;
import play.mvc.Http;
import security.Acao;
import security.models.UsuarioSessao;
import security.services.Auth;
import security.services.ExternalService;
import utils.Configuracoes;
import utils.Mensagem;

public class InternalController extends GenericController {

	@Before
	public static void verificarAutenticacao() {
		
		if (isPublicResource()) {

			return;

		} else if (isExternalResource()) {

			// Verifica se o serviço externo esta sendo requisitado por um IP cadastrado
			ExternalService.validateAdress(request);

		} else {


			UsuarioSessao usuarioSessao = Auth.getAuthenticatedUser(session.current());
			
		}
			
	}
	
	private static boolean isPublicResource() {

		return request.path.indexOf(Configuracoes.PUBLIC_ROUTE) != -1;

	}

	private static boolean isExternalResource() {

		return request.path.indexOf(Configuracoes.EXTERNAL_ROUTE) != -1;

	}

	protected static UsuarioSessao getUsuarioSessao() {
		
		return Auth.getAuthenticatedUser(session.current());
	}
	
	protected static void verificarPermissao(Acao ... acoes) {

		UsuarioSessao usuarioSessao = getUsuarioSessao();
		
		boolean permitido = false;

		for (Acao acao : acoes)
			permitido = permitido || (usuarioSessao != null && usuarioSessao.hasPermissao(acao.codigo));

		if (!permitido) {

			response.status = Http.StatusCode.FORBIDDEN;
			renderMensagem(Mensagem.PERMISSAO_NEGADA);
		}
	}
}
