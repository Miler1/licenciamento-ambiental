package utils;

import exceptions.LicenciamentoException;
import exceptions.LoginException;
import exceptions.ValidacaoException;
import exceptions.WebServiceException;
import br.ufla.lemaf.OAuthClientCadastroUnificadoException;
import br.ufla.lemaf.beans.EmpreendimentoFiltroResult;
import br.ufla.lemaf.beans.EmpreendimentosPessoa;
import br.ufla.lemaf.beans.FiltroEmpreendimento;
import br.ufla.lemaf.beans.Message;
import br.ufla.lemaf.beans.pessoa.ConfirmaRespostaOrgaoRedeSimples;
import br.ufla.lemaf.beans.pessoa.Pessoa;
import br.ufla.lemaf.beans.pessoa.Usuario;
import br.ufla.lemaf.services.CadastroUnificadoPessoaService;
import models.Empreendimento;
import models.Pagination;
import play.Play;
import play.libs.WS;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class WebServiceEntradaUnica {

	public static String CLIENTE_ID = Play.configuration.getProperty("entrada.unica.cliente.id");
	public static String CLIENTE_SECRET = Play.configuration.getProperty("entrada.unica.cliente.secret");
	public static String ENTRADA_UNICA_PORTAL_SEGURANCA = Play.configuration.getProperty("entrada.unica.url.portal.seguranca");
	public static String ENTRADA_UNICA_CADASTRO_UNIFICADO= Play.configuration.getProperty("entrada.unica.url.cadastro.unificado");

	public static final CadastroUnificadoPessoaService oAuthClient = new CadastroUnificadoPessoaService(CLIENTE_ID, CLIENTE_SECRET, ENTRADA_UNICA_PORTAL_SEGURANCA, ENTRADA_UNICA_CADASTRO_UNIFICADO);

	public static void createOrUpdatePessoaFisica(Pessoa pessoaFisica) {

		try {

			Pessoa pessoaEU = oAuthClient.buscarPessoaFisicaPeloCpf(pessoaFisica.cpf);
			Pessoa pessoaAtual = pessoaFisica;

			if (pessoaEU != null) {

				pessoaAtual.id = pessoaEU.id;
				oAuthClient.alterarDadosPessoaFisica(pessoaAtual);

			} else {
				oAuthClient.cadastrarPessoaFisica(pessoaAtual);
			}

		} catch (OAuthClientCadastroUnificadoException e) {

			throw new ValidationException(e.getMessage());
		}
	}


	public static models.Pessoa findPessoaByCpfCnpj(String cpfCnpj) {

		if(cpfCnpj == null || cpfCnpj.isEmpty()) {
			return null;
		}
		return cpfCnpj.length() <= 11 ? (models.Pessoa) oAuthClient.buscarPessoaFisicaPeloCpf(cpfCnpj) :
				(models.Pessoa) oAuthClient.buscarPessoaJuridicaPeloCnpj(cpfCnpj);
	}

	public static Pessoa findPessoaByCpfCnpjEU(String cpfCnpj) {

		if(cpfCnpj == null || cpfCnpj.isEmpty()) {
			return null;
		}
		return cpfCnpj.length() <= 11 ? oAuthClient.buscarPessoaFisicaPeloCpf(cpfCnpj) :
				 oAuthClient.buscarPessoaJuridicaPeloCnpj(cpfCnpj);
	}

	public static Pessoa findPessoaByCpfEU(String cpf) {

		try {

			return oAuthClient.buscarPessoaFisicaPeloCpf(cpf);

		} catch (OAuthClientCadastroUnificadoException e) {

			throw new ValidationException(e.getMessage());
		}
	}

	public static Boolean verificarUsuarioByLogin(String cpfCnpj) {

		try {

			return oAuthClient.verificarUsuarioByLogin(cpfCnpj);

		} catch (OAuthClientCadastroUnificadoException e) {

			throw new ValidationException(e.getMessage());
		}
	}

	public static Usuario buscarUsuarioPorLogin(String cpfCnpj) {

		try {

			return oAuthClient.buscarUsuarioPorLogin(cpfCnpj);

		} catch (OAuthClientCadastroUnificadoException e) {

			throw new ValidationException(e.getMessage());
		}
	}

	public static Message createOrUpdateEmpreendimento(Empreendimento empreendimento, String login) {

		br.ufla.lemaf.beans.Empreendimento empreendimentoEU = findEmpreendimentosEU(empreendimento);
		br.ufla.lemaf.beans.Empreendimento empreendimentoAtual = empreendimento.getEmpreendimentoEU();

		if (empreendimentoEU != null) {
			empreendimentoAtual.id = empreendimentoEU.id;
			return oAuthClient.editarEmpreendimentoPessoa(empreendimentoAtual, login);

		} else {
			if (empreendimento.empreendimentoEU.pessoa.cpf != null || empreendimento.empreendimentoEU.pessoa.cnpj != null) {
				return oAuthClient.cadastrarEmpreendimentoPessoa(empreendimentoAtual);
			}

		}

		return new Message("");

	}

	public static void updateEmpreendimento(Empreendimento empreendimento, String login) {

		try {

			br.ufla.lemaf.beans.Empreendimento empreendimentoEU = findEmpreendimentosEU(empreendimento);
			br.ufla.lemaf.beans.Empreendimento empreendimentoAtual = empreendimento.getEmpreendimentoEU();

			empreendimentoAtual.id = empreendimentoEU.id;

			oAuthClient.editarEmpreendimentoPessoa(empreendimentoAtual, login);

		} catch (OAuthClientCadastroUnificadoException e) {

			throw new ValidationException(e.getMessage());
		}
	}

	public static Empreendimento findEmpreendimentosByCpfCnpjCadastroEmpreendimento(String cpfCnpj) {

		if (Empreendimento.verificaExistenciaDeEmpreendimentoLicencimento(cpfCnpj))
			return null;

		FiltroEmpreendimento filtro = new FiltroEmpreendimento();

		filtro.cpfsCnpjs = new ArrayList<>();

		filtro.cpfsCnpjs.add(cpfCnpj);

		filtro.ordenacao = "DENOMINACAO_ASC";

		EmpreendimentoFiltroResult listaEmpEU = oAuthClient.buscarEmpreendimentosComFiltro(filtro);

		br.ufla.lemaf.beans.Empreendimento empreendimentoEU = listaEmpEU.pageItems != null &&  listaEmpEU.pageItems.size() > 0 ? listaEmpEU.pageItems.get(0): null;

		return Empreendimento.convert(empreendimentoEU, cpfCnpj);
	}

	public static br.ufla.lemaf.beans.Empreendimento findEmpreendimentosEU(Empreendimento empreendimento) {

		try {

			String cpfCnpj = empreendimento.cpfCnpj;

			FiltroEmpreendimento filtro = new FiltroEmpreendimento();

			filtro.cpfsCnpjs = new ArrayList<>();

			filtro.cpfsCnpjs.add(cpfCnpj);

			filtro.ordenacao = "DENOMINACAO_ASC";

			EmpreendimentoFiltroResult listaEmpEU = oAuthClient.buscarEmpreendimentosComFiltro(filtro);

			return listaEmpEU.totalItems > 0 ? listaEmpEU.pageItems.get(0) : null;

		} catch (WebServiceException e) {

			throw new ValidacaoException(MensagemPortalSegurancaUtil.convertMensagem(e.getMessage()));
		}
	}

	public static Empreendimento findEmpreendimentosByCpfCnpj(String cpfCnpj) {

		try {

			FiltroEmpreendimento filtro = new FiltroEmpreendimento();
			filtro.cpfsCnpjs = new ArrayList<>();

			filtro.cpfsCnpjs.add(cpfCnpj);

			filtro.ordenacao = "DENOMINACAO_ASC";

			EmpreendimentoFiltroResult listaEmpEU = oAuthClient.buscarEmpreendimentosComFiltro(filtro);

			return listaEmpEU.totalItems > 0 ? Empreendimento.convert(listaEmpEU.pageItems.get(0)) : null;

		} catch (WebServiceException e) {

			throw new ValidacaoException(MensagemPortalSegurancaUtil.convertMensagem(e.getMessage()));
		}
	}

	public static Pagination<Empreendimento> listEmpreendimento(FiltroEmpreendimento filtroEmpreendimento) {

		try {

			EmpreendimentoFiltroResult empreendimentoFiltroResult = oAuthClient.buscarEmpreendimentosComFiltro(filtroEmpreendimento);

			List<Empreendimento> empreendimentosConvertidos = new ArrayList<>();

			if (empreendimentoFiltroResult.pageItems != null) {

				empreendimentosConvertidos = empreendimentoFiltroResult.pageItems.stream().map(Empreendimento::convert).collect(Collectors.toList());
			}

			Pagination<Empreendimento> pagination = new Pagination<>();

			pagination.setPageItems(empreendimentosConvertidos);

			pagination.setTotalResults(empreendimentoFiltroResult.totalItems);

			return pagination;

		} catch (OAuthClientCadastroUnificadoException e) {

			throw new ValidationException(e.getMessage());
		}
	}

	public static void createOrUpdatePessoaJuridica(Pessoa pessoaJuridica) {

		try {

			Pessoa pessoaEU = oAuthClient.buscarPessoaJuridicaPeloCnpj(pessoaJuridica.cnpj);
			Pessoa pessoaAtual = pessoaJuridica;

			if (pessoaEU != null) {

				pessoaAtual.id = pessoaEU.id;
				oAuthClient.alterarDadosPessoaJuridica(pessoaAtual);

			} else {
				oAuthClient.cadastrarPessoaJuridica(pessoaAtual);

			}

		} catch (OAuthClientCadastroUnificadoException e) {

			throw new ValidationException(e.getMessage());
		}
	}

	public static void createOrUpdatePessoa(models.Pessoa pessoa) {

		if (pessoa.isPessoaFisica()) {
			createOrUpdatePessoaFisica(pessoa);
		} else {
			createOrUpdatePessoaJuridica(pessoa);
		}
	}

	public static Boolean validaEmpreendimentosCadastrado(String cpfCnpj) {

		try {

			Empreendimento emp = Empreendimento.getByCpfCnpj(cpfCnpj);

			return Empreendimento.convert(findEmpreendimentosEU(emp)) != null;

		} catch (OAuthClientCadastroUnificadoException e) {

			throw new ValidationException(e.getMessage());
		}
	}

	public static Usuario loginEntradaUnica(String login, String senha) {

		try {

			if (oAuthClient == null) {

				throw new LicenciamentoException(Mensagem.ENTRADA_UNICA_FALHA_COMUNICACAO.getTexto());
			}

			return oAuthClient.login(login, senha);

		} catch (Exception e) {

			throw new LoginException(e.getMessage());

		}

	}

	public Usuario searchBySessionKey(String sessionKey) {

		return oAuthClient.searchBySessionKey(sessionKey);
	}

	public static Message vincularGestaoEmpreendimentos(String cpfCnpj) {

		try {

			return oAuthClient.vincularGestaoEmpreendimentos(cpfCnpj);

		} catch (WebServiceException e) {

			throw new ValidacaoException(MensagemPortalSegurancaUtil.convertMensagem(e.getMessage()));
		}
	}

	public static void adicionarPerfilPeloCpfCnpj(String login, String codigoPerfil) {

		try {

			oAuthClient.adicionarPerfilPeloCpfCnpj(login, codigoPerfil);

		} catch (OAuthClientCadastroUnificadoException e) {

			throw new ValidationException(e.getMessage());
		}
	}

	public static Message enviarDocumentoRedeSimples(ConfirmaRespostaOrgaoRedeSimples confirmaRespostaOrgaoRedeSimples) {

		try {

			return oAuthClient.enviarDocumentoRedeSimples(confirmaRespostaOrgaoRedeSimples);

		} catch (OAuthClientCadastroUnificadoException e) {

			throw new ValidacaoException(MensagemPortalSegurancaUtil.convertMensagem(e.getMessage()));
		}
	}

	public static WS.HttpResponse resgatarSenhaViaEmail(String cpf) {

		String url = (Configuracoes.ENTRADA_UNICA_URL_PORTAL_SEGURANCA
				+ "/usuario/emailRedefinirSenha/"
				+ cpf);

		WS.HttpResponse response = new WebService().get(url);

		return response;
	}

	public static boolean verificarCadastroUsuario(String login) {

		String url = (Configuracoes.ENTRADA_UNICA_URL_PORTAL_SEGURANCA
				+ "/usuario/verificarCadastro/"
				+ login);

		WS.HttpResponse response = new WebService().get(url);

		return response.getJson().getAsBoolean();

	}

}
