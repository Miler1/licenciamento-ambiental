package security.models;

import br.ufla.lemaf.beans.pessoa.Perfil;
import br.ufla.lemaf.beans.pessoa.Permissao;
import br.ufla.lemaf.beans.pessoa.Usuario;
import models.Pessoa;
import utils.WebServiceEntradaUnica;

public class UsuarioSessao extends Usuario {

    public Perfil perfil;

    public UsuarioSessao() {}

    public UsuarioSessao(Usuario usuario) {

        this.login = usuario.login;
        this.nome = usuario.nome;
        this.email = usuario.email;
        this.sessionKeyEntradaUnica = usuario.sessionKeyEntradaUnica;
        this.perfilSelecionado = usuario.perfis.get(0);
        this.perfis = usuario.perfis;

    }

    public br.ufla.lemaf.beans.pessoa.Perfil getPerfilUsuario(String codigoPerfil) {

        for (br.ufla.lemaf.beans.pessoa.Perfil perfilUsuario : perfis) {

            if (perfilUsuario.codigo.equals(codigoPerfil)) {

                return perfilUsuario;
            }
        }

        return null;
    }

    public boolean hasPermissao(String codigoPermissao) {

        for (Permissao permissao : this.perfilSelecionado.permissoes) {

            if (permissao.codigo.equals(codigoPermissao)) {
                return true;
            }
        }

        return false;
    }

    public Pessoa findPessoa() {

        if (login == null)
            return null;

        return Pessoa.convert(WebServiceEntradaUnica.findPessoaByCpfCnpjEU(login));
    }


}
