package models.DashboardAlertas;

import models.caracterizacao.Caracterizacao;
import models.caracterizacao.GeometriaCaracterizacao;
import models.caracterizacao.TipoLicenca;
import play.Logger;
import play.i18n.Messages;
import utils.DataUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Alerta {

	/**
	 * Attributes classes
	 */
	public String dataCadastro;
	public String severidade;
	public String classificacao;
	public List<Municipio> municipios;
	public Municipio municipioDestino;
	public String descricaoAcesso;
	public String informacoesGerais;
	public Setor setor;
	public Tipo tipo;
	public String objetivo;
	public List<Localizacao> localizacoes;


	/**
	 * Static classes
	 */
	static class Municipio {
		String id;
	}

	static class Setor {
		String sigla;
	}

	static class Tipo {
		// A1_LICENCIAMENTO - Caracterização DEFERIDA
		String codigo;
	}

	static class Localizacao {
		String geometry;
		String descricao;
	}


	/**
	 * Construtor
	 * @param caracterizacao
	 */
	public Alerta(Caracterizacao caracterizacao) {

		if(caracterizacao == null){

			Logger.error("Relatório informado é nulo.");
			throw new RuntimeException(Messages.get("integracao.monitoramento.enviarLote.relatorioNull"));
		}

		this.dataCadastro = DataUtils.formataData(new Date(), "dd/MM/yyyy HH:mm:ss");
		this.severidade = "1";
		this.classificacao = "INFORMACAO";
		this.municipios = new ArrayList<>();
		Municipio municipio = new Municipio();
		municipio.id = String.valueOf(caracterizacao.empreendimento.getMunicipio().id);
		this.municipios.add(municipio);
		this.municipioDestino = null;
		this.descricaoAcesso = "Não possui.";
		this.setor = new Setor();
		this.setor.sigla = "GEFA";
		this.tipo = new Tipo();
		this.tipo.codigo = "A1_LICENCIAMENTO";
		this.objetivo = "Publicidade e transparência ambiental";
		this.localizacoes = new ArrayList<>();

		StringBuilder nomesAtividades = new StringBuilder();
		final String sulfix = ", ";

		caracterizacao.atividadesCaracterizacao.forEach(ac -> {
			nomesAtividades.append(ac.atividade.nome);
			nomesAtividades.append(sulfix);
		});

		if (caracterizacao.isComplexo()) {
			this.preencheLocalizacao(caracterizacao.geometriasComplexo);
		} else {
			caracterizacao.atividadesCaracterizacao.forEach(ac -> this.preencheLocalizacao(ac.geometriasAtividade));
		}

		nomesAtividades.setLength(nomesAtividades.length() - sulfix.length());

		this.informacoesGerais = "<b>Informações gerais:</b><br>" +
				"<b>Número da licença:</b> " + caracterizacao.getNumeroLicenca() + "<br>" +
				"<b>Data de deferimento:</b> " + (caracterizacao.tipoLicenca.id.equals(TipoLicenca.DISPENSA_LICENCA_AMBIENTAL) ? DataUtils.formataData(caracterizacao.dae.dataPagamento, "dd/MM/yyyy") : DataUtils.formataData(caracterizacao.daeLicenciamento.dataPagamento, "dd/MM/yyyy")) + "<br>" +
				"<b>Nome/Razão Social do interessado:</b> " + caracterizacao.empreendimento.pessoa.getNomeRazaoSocial() + "<br>" +
				"<b>Atividade(s) licenciada(s):</b> " + nomesAtividades.toString() + "<br>"
		;
	}

	private <T extends GeometriaCaracterizacao> void preencheLocalizacao(Collection<T> lista){
		lista.forEach(item -> {
			Localizacao localizacao = new Localizacao();
			localizacao.geometry = item.getGeomWkt();
			localizacao.descricao = "-";
			this.localizacoes.add(localizacao);
		});
	}
}
