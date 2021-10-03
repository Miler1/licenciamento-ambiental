package models.caracterizacao;

import play.db.jpa.GenericModel;
import utils.ListUtil;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "tipo_caracterizacao_atividade")
public class TipoCaracterizacaoAtividade extends GenericModel {

	private static final String SEQ = "licenciamento.tipo_caracterizacao_atividade_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@ManyToOne
	@JoinColumn(name="id_atividade")
	public Atividade atividade;

	@ManyToOne
	@JoinColumn(name="id_atividade_cnae")
	public AtividadeCnae atividadeCnae;

	@Column(name = "dispensa_licenciamento")
	public Boolean dispensaLicenciamento;

	@Column(name = "licenciamento_simplificado")
	public Boolean licenciamentoSimplificado;

	@Column(name = "licenciamento_declaratorio")
	public Boolean licenciamentoDeclaratorio;

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof TipoCaracterizacaoAtividade)) {
			return false;
		}

		TipoCaracterizacaoAtividade that = (TipoCaracterizacaoAtividade) other;

		return this.dispensaLicenciamento.equals(that.dispensaLicenciamento) &&
				this.licenciamentoSimplificado.equals(that.licenciamentoSimplificado) &&
				this.licenciamentoDeclaratorio.equals(that.licenciamentoDeclaratorio);
	}

	public static List<TipoCaracterizacaoAtividade> findByAtividadeCaracterizacao(AtividadeCaracterizacao atividade) {

		return find("atividade.id = :idAtividade AND atividadeCnae.id in (:cnaes)")
				.setParameter("idAtividade", atividade.atividade.id)
				.setParameter("cnaes", ListUtil.getIds(atividade.atividadesCnae))
				.fetch();
	}

	public static boolean isDeclaratorio(List<TipoCaracterizacaoAtividade> lista) {
		return lista.stream().allMatch(tipo -> tipo.licenciamentoDeclaratorio.equals(true));
	}

	public static boolean isDispensaLicenciamento(List<TipoCaracterizacaoAtividade> lista) {
		return lista.stream().allMatch(tipo -> tipo.dispensaLicenciamento.equals(true));
	}

	public static boolean isSimplificado(List<TipoCaracterizacaoAtividade> lista) {
		return lista.stream().allMatch(tipo -> tipo.licenciamentoSimplificado.equals(true));
	}

	public static class FiltroAtividade {

		public Long idTipologia;
		public Boolean licenciamentoSimplificado;
		public Boolean dispensaLicenciamento;
		public List<String> listaCodigosCnaes;
		public Integer tipoEndereco; //0- Urbana, 1- Rural

		public FiltroAtividade() {

		}
	}
}
