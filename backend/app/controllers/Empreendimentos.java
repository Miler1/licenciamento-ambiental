package controllers;

import beans.EmpreendimentoVinculoRespostaVO;
import beans.VinculoEmpreendimentoCpfCnpjVO;
import exceptions.AppException;
import br.ufla.lemaf.beans.FiltroEmpreendimento;
import models.*;
import play.db.jpa.JPABase;
import security.Acao;
import security.services.Auth;
import serializers.EmpreendedorSerializer;
import serializers.EmpreendimentoSerializer;
import serializers.PessoasSerializer;
import utils.Mensagem;
import utils.WebServiceEntradaUnica;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Empreendimentos extends InternalController {

	private static List<Empreendimento> empreendimentosVinculadosPessoaEU;

	public static void verificaUsuario(Empreendimento empreendimento) {
		empreendimento.validarSeUsuarioCadastrante();
	}

	public static void getEmpreendimentoPorId(Long idEmpreendimento) {

		verificarPermissao(Acao.LISTAR_EMPREENDIMENTO);

		Empreendimento empreendimento = Empreendimento.findById(idEmpreendimento);

		empreendimento.empreendimentoEU = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(empreendimento.cpfCnpj).empreendimentoEU;

//		Empreendimento empreendimentoEU = Empreendimento.convert(WebServiceEntradaUnica.findEmpreendimentosEU(empreendimento));

		renderJSON(empreendimento, EmpreendimentoSerializer.getEmpreendimento);

	}

	public static void getEmpreendimentoCompleto(String cpfCnpj) {

		verificarPermissao(Acao.LISTAR_EMPREENDIMENTO);

		Empreendimento emp = Empreendimento.getByCpfCnpj(cpfCnpj);
		emp.validarSeUsuarioCadastrante();
		br.ufla.lemaf.beans.Empreendimento empEU = WebServiceEntradaUnica.findEmpreendimentosEU(emp);
		Empreendimento empreendimento = Empreendimento.convert(empEU);
		renderJSON(empreendimento, EmpreendimentoSerializer.getEmpreendimento);

	}

	public static void getEmpreendimentoPorCpfCnpj(String cpfCnpj) {

		verificarPermissao(Acao.LISTAR_EMPREENDIMENTO);

		Empreendimento empreendimento = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpjCadastroEmpreendimento(cpfCnpj);

		if(cpfCnpj.length() > 11 && empreendimento != null) {

			if (empreendimento.empreendimentoEU.enderecos.get(0).tipo.id == 1) {
				Municipio municipio = Municipio.findById(Long.valueOf(empreendimento.empreendimentoEU.enderecos.get(0).municipio.codigoIbge));
				empreendimento.municipioLicenciamento = municipio;

			} else {
				Municipio municipio = Municipio.findById(Long.valueOf(empreendimento.empreendimentoEU.enderecos.get(1).municipio.codigoIbge));
				empreendimento.municipioLicenciamento = municipio;
			}

		}

		renderJSON(empreendimento, EmpreendimentoSerializer.getEmpreendimento);

	}

	public static void getEmpreendimentosParaOutroEmpreendedor(String cpfCnpj) {

		verificarPermissao(Acao.LISTAR_EMPREENDIMENTO);

		Empreendimento empreendimento = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpjCadastroEmpreendimento(cpfCnpj);

		renderJSON(empreendimento, EmpreendimentoSerializer.getEmpreendimento);

	}


	public static void getEmpreendimentoReduzido(String cpfCnpj) {

		verificarPermissao(Acao.LISTAR_EMPREENDIMENTO);

		Empreendimento emp = Empreendimento.getByCpfCnpj(cpfCnpj);
		br.ufla.lemaf.beans.Empreendimento empEU = WebServiceEntradaUnica.findEmpreendimentosEU(emp);
		Empreendimento empreendimento = Empreendimento.convert(empEU);

		renderJSON(empreendimento, EmpreendimentoSerializer.getEmpreendimentoReduzido);

	}

	public static void list(String pesquisa, Integer numeroPagina, Integer qtdItensPorPagina) {

		verificarPermissao(Acao.LISTAR_EMPREENDIMENTO);

//		List<Empreendimento> empreendimentosLicenciamento = Empreendimento.list(getUsuarioSessao(), pesquisa, numeroPagina, qtdItensPorPagina);
		List<Empreendimento> empreendimentosLicenciamento = Empreendimento.find("byCpfCnpjCadastrante",getUsuarioSessao().login).fetch();

		if (empreendimentosLicenciamento == null || empreendimentosLicenciamento.isEmpty()) {
			renderJSON(new Pagination<>(0L, empreendimentosLicenciamento), EmpreendimentoSerializer.listGrid);
		}

//		int totalEmpreendimentosLicenciamento = empreendimentosLicenciamento.size();

		List<String> cpfcnpjs = empreendimentosLicenciamento.stream().map(empreendimento -> empreendimento.cpfCnpj).collect(Collectors.toList());

		FiltroEmpreendimento filtroEmpreendimento = new FiltroEmpreendimento();

		filtroEmpreendimento.ordenacao = "DENOMINACAO_ASC";

		filtroEmpreendimento.cpfsCnpjs = cpfcnpjs;

		filtroEmpreendimento.busca = pesquisa;

		filtroEmpreendimento.tamanhoPagina = qtdItensPorPagina;
		filtroEmpreendimento.numeroPagina = numeroPagina;

		Pagination<Empreendimento> pagination = WebServiceEntradaUnica.listEmpreendimento(filtroEmpreendimento);

		pagination.getPageItems().forEach(empreendimento ->
				empreendimento.possuiCaracterizacoes = empreendimento.temCaracterizacoes()
		);

		renderJSON(pagination, EmpreendimentoSerializer.listGrid);

	}

	public static void getPessoaPorCpfCnpj(String cpfCnpj) {

		verificarPermissao(Acao.CADASTRAR_EMPREENDIMENTO);

		Pessoa pessoa = (Pessoa) WebServiceEntradaUnica.findPessoaByCpfCnpj(cpfCnpj);

		renderJSON(pessoa, PessoasSerializer.findByCpfCnpj);
	}

	public static void create(Empreendimento empreendimento) {

		verificarPermissao(Acao.CADASTRAR_EMPREENDIMENTO);

		empreendimento.cadastrante = Auth.getUsuarioSessao().findPessoa();

		empreendimento.save();

		renderMensagem(Mensagem.EMPREENDIMENTO_CADASTRADO_SUCESSO);
	}


	public static void update(Long id, Empreendimento empreendimentoAtualizado) {

		verificarPermissao(Acao.ALTERAR_EMPREENDIMENTO);

		Empreendimento empreendimento = Empreendimento.findById(id);

		empreendimento.update(empreendimentoAtualizado);

		renderMensagem(Mensagem.EMPREENDIMENTO_ATUALIZADO_SUCESSO);
	}

	public static void findByIdResumido(Long id) {

		verificarPermissao(Acao.LISTAR_EMPREENDIMENTO);

		Empreendimento empreendimentoLocal = Empreendimento.findById(id);

		if (empreendimentoLocal == null) {
			throw new AppException(Mensagem.EMPREENDIMENTO_NAO_ENCONTRADO);
		}

		Empreendimento empreendimento = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(empreendimentoLocal.cpfCnpj);

		if (empreendimento == null) {
			throw new AppException(Mensagem.EMPREENDIMENTO_NAO_ENCONTRADO);
		}

		empreendimento.validarSeUsuarioCadastrante();

		renderJSON(empreendimento, EmpreendimentoSerializer.findByIdResumido);

	}

	public static void findToUpdate(Long id) {

		verificarPermissao(Acao.ALTERAR_EMPREENDIMENTO);

		Empreendimento empreendimentoLocal = Empreendimento.findToUpdate(id);

		if (empreendimentoLocal == null) {
			throw new AppException(Mensagem.EMPREENDIMENTO_NAO_ENCONTRADO);
		}

		if (empreendimentoLocal.getEmpreendimentoEU() == null) {
			throw new AppException(Mensagem.EMPREENDIMENTO_NAO_ENCONTRADO);
		}

		Pessoa cadastrante = getUsuarioSessao().findPessoa();

		if (!empreendimentoLocal.cpfCnpjCadastrante.equals(cadastrante.getCpfCnpj())) {

			throw new AppException(Mensagem.PERMISSAO_NEGADA);
		}

		renderJSON(empreendimentoLocal, EmpreendimentoSerializer.findToUpdate);
	}

	public static void delete(Long id) {

		verificarPermissao(Acao.EXCLUIR_EMPREENDIMENTO);

		Empreendimento empreendimento = Empreendimento.findById(id);

		if(empreendimento != null){
			empreendimento.delete();
		}else{
			throw new AppException(Mensagem.EMPREENDIMENTO_NAO_ENCONTRADO);
		}

		renderMensagem(Mensagem.EMPREENDIMENTO_EXCLUIDO_SUCESSO);

	}

	public static void getVinculos(VinculoEmpreendimentoCpfCnpjVO cpfCnpjVO){

		verificarPermissao(Acao.LISTAR_EMPREENDIMENTO);

		Empreendimento empreendimento = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(cpfCnpjVO.cpfCnpjEmpreendedor);

		EmpreendimentoVinculoRespostaVO respostaVO = new EmpreendimentoVinculoRespostaVO(false,"");

		if (empreendimento == null) {
			respostaVO.mensagem = Mensagem.CPF_CNPJ_NAO_VINCULADO_EMPREENDEDOR.getTexto();
		} else {
			respostaVO = empreendimento.getPessoasVinculadas(cpfCnpjVO);
		}

		renderJSON(respostaVO, EmpreendimentoSerializer.getEmpreendimentoVinculo);

	}

	public void getCadastrante(String cpfCnpj) {

		verificarPermissao(Acao.LISTAR_EMPREENDIMENTO);

		renderJSON(WebServiceEntradaUnica.findPessoaByCpfCnpjEU(cpfCnpj), PessoasSerializer.findByCpfCnpj);

	}

}
