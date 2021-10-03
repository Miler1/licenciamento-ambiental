package security.services;


import exceptions.LoginException;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.Play;
import play.mvc.Http.Request;
import security.ModelAutenticar;
import security.models.UsuarioSessao;
import utils.WebServiceEntradaUnica;


public class AuthenticationServiceCadastroUnificado implements AuthService {

    @Override
    public ModelAutenticar autenticar(Request request) {

        if(request.params == null)
            return null;

        String login = request.params.get("login");
        String senha = request.params.get("password");
        ModelAutenticar modelAutenticar = new ModelAutenticar();
        modelAutenticar.autenticado = false;

        if (StringUtils.isBlank(login) || StringUtils.isBlank(senha)) {
            throw new RuntimeException("Digite um CPF e senha!");
        }

        login = login.replace(".", "").replace("-", "").replace("/", "");

        br.ufla.lemaf.beans.pessoa.Usuario usuarioEntradaUnica;

        try {

            this.verificarCadastro(login);

            usuarioEntradaUnica = WebServiceEntradaUnica.loginEntradaUnica(login, senha);

            UsuarioSessao usuarioSessao = new UsuarioSessao(usuarioEntradaUnica);
            usuarioSessao.perfil = usuarioEntradaUnica.perfis.get(0);

            modelAutenticar.usuarioSessao = usuarioSessao;
            modelAutenticar.autenticado = true;

            return modelAutenticar;

        }
        catch (Exception e) {

            modelAutenticar.mensagem =  ((LoginException) e).getMensagem();

            return modelAutenticar;

        }

    }

    //verifica se o usuario foi cadastrado no portal de segurança e se os dados complementares foram preenchidos
    private void verificarCadastro(String login) {
        boolean cadastroRealizado = WebServiceEntradaUnica.verificarCadastroUsuario(login);
        if (!cadastroRealizado){
            String urlCadastroUnificado = Play.configuration.getProperty("entrada.unica.url.cadastro.unificado");
            String linkClicavelCadastroUnificado = "<a href=\"" + urlCadastroUnificado+ "\" target=\"_blank\">" + urlCadastroUnificado + "</a>";
            throw  new LoginException("Usuário não encontrado, favor finalizar o cadastro em: " + linkClicavelCadastroUnificado );
        }
    }

    @Override
    public ModelAutenticar usuarioLogadoBySessionKey(String sessionKey) {

        br.ufla.lemaf.beans.pessoa.Usuario usuarioEntradaUnica;
        ModelAutenticar modelAutenticar = new ModelAutenticar();
        WebServiceEntradaUnica wsEntradaUnica = new WebServiceEntradaUnica();

        try {

            if(wsEntradaUnica == null) {
                throw new RuntimeException("Não foi possível realizar a autenticação. Contate o administrador do sistema.");
            }

            usuarioEntradaUnica = wsEntradaUnica.searchBySessionKey(sessionKey);

            Logger.info("[CADASTRO-UNIFICADO-AUTHENTICATION - autenticar()]");

            UsuarioSessao usuarioSessao = new UsuarioSessao(usuarioEntradaUnica);

            usuarioSessao.perfil = usuarioEntradaUnica.perfilSelecionado;

            modelAutenticar.usuarioSessao = usuarioSessao;
            modelAutenticar.autenticado = true;

            return modelAutenticar;

        }
        catch (Exception e) {

            e.printStackTrace();
            Logger.error(e.getMessage());

            Logger.error("[CADASTRO-UNIFICADO-AUTHENTICATION - autenticar()]");

            modelAutenticar.mensagem = e.getMessage();

            return modelAutenticar;

        }

    }

}
