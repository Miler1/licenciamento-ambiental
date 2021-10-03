package models.caracterizacao;

import beans.DadosSobreposicaoVO;
import com.vividsolutions.jts.geom.Geometry;
import exceptions.AppException;
import exceptions.ValidacaoException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import play.db.jpa.GenericModel;
import utils.Mensagem;
import utils.WebServiceEntradaUnica;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "atividade_caracterizacao")
public class AtividadeCaracterizacao extends GenericModel implements GuardaSobreposicao{

	private static final String SEQ = "licenciamento.atividade_caracterizacao_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@ManyToOne
	@JoinColumn(name="id_porte_empreendimento")
	public PorteEmpreendimento porteEmpreendimento;

	@ManyToOne
	@JoinColumn(name="id_porte_empreendimento_para_calculo_do_porte")
	public PorteEmpreendimento porteEmpreendimentoParaCalculoDoPorte;

	@ManyToOne
	@JoinColumn(name="id_atividade")
	public Atividade atividade;

	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_atividade_caracterizacao_cnae",
		joinColumns = @JoinColumn(name = "id_atividade_caracterizacao"),
		inverseJoinColumns = @JoinColumn(name = "id_atividade_cnae"))
	public List<AtividadeCnae> atividadesCnae;

	@OneToMany(mappedBy = "atividadeCaracterizacao", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<GeometriaAtividade> geometriasAtividade;

	@ManyToOne
	@JoinColumn(name = "id_caracterizacao", referencedColumnName = "id")
	public Caracterizacao caracterizacao;

	@OneToMany(mappedBy = "atividadeCaracterizacao", cascade = CascadeType.ALL)
	public List<AtividadeCaracterizacaoParametros> atividadeCaracterizacaoParametros;

	@OneToMany(mappedBy = "atividadeCaracterizacao", cascade = CascadeType.ALL)
	public List<SobreposicaoCaracterizacaoAtividade> sobreposicaoCaracterizacaoAtividades;

	public static TipoCaracterizacao getEnquadramento(List<AtividadeCaracterizacao> atividadesCaracterizacao) {

		List<TipoCaracterizacaoAtividade> tipos = new ArrayList<>();

		atividadesCaracterizacao.stream().map(TipoCaracterizacaoAtividade::findByAtividadeCaracterizacao)
				.forEach(tipos::addAll);

		if (TipoCaracterizacaoAtividade.isSimplificado(tipos)) {
			return TipoCaracterizacao.findById(TipoCaracterizacao.SIMPLIFICADO);
		}

		if (TipoCaracterizacaoAtividade.isDispensaLicenciamento(tipos)) {
			return TipoCaracterizacao.findById(TipoCaracterizacao.DISPENSA);
		}

		throw new AppException(Mensagem.TIPOS_CARACTERIZACAOES_DIFERENTES);
	}

	/*
	 * Valida se a atividade selecionada pertence ao tipo de licença selecionado
	 */
	public void validarTipoLicenca(TipoLicenca tipoLicenca) {

		List<TipoCaracterizacaoAtividade> tipos = TipoCaracterizacaoAtividade.findByAtividadeCaracterizacao(this);

		if(tipoLicenca != null && tipoLicenca.id.equals(TipoLicenca.DISPENSA_LICENCA_AMBIENTAL)) {

			if(!TipoCaracterizacaoAtividade.isDispensaLicenciamento(tipos)) {

				throw new ValidacaoException(Mensagem.CARACTERIZACAO_ATIVIDADE_NAO_PERTENCE_TIPO_LICENCA);
			}

		} else {

			if (!TipoCaracterizacaoAtividade.isSimplificado(tipos) && !TipoCaracterizacaoAtividade.isDeclaratorio(tipos)) {

				throw new ValidacaoException(Mensagem.CARACTERIZACAO_ATIVIDADE_NAO_PERTENCE_TIPO_LICENCA);
			}
		}

	}

	/**
	 * Gera uma cópia da atividade caracterização, duplicando todas as informações
	 * declaradas pelo cadastrante. Utilizado quando duplicar a caracterização.
	 */
	public AtividadeCaracterizacao gerarCopia() {

		AtividadeCaracterizacao copia = new AtividadeCaracterizacao();
		copia.atividade = this.atividade;
		copia.porteEmpreendimento = this.porteEmpreendimento;
		copia.atividadeCaracterizacaoParametros = this.atividadeCaracterizacaoParametros;
		copia.geometriasAtividade = new ArrayList<>();
		copia.atividadesCnae = new ArrayList<>();

		if (this.atividadesCnae != null) {

			for (AtividadeCnae atividadeCnae : this.atividadesCnae) {

				copia.atividadesCnae.add(atividadeCnae);
			}
		}

		if (this.geometriasAtividade != null) {

			for (GeometriaAtividade geo : this.geometriasAtividade) {

				GeometriaAtividade copiaGeo = geo.gerarCopia();
				copiaGeo.atividadeCaracterizacao = copia;
				copia.geometriasAtividade.add(copiaGeo);
			}
		}

		return copia;
	}

	public PorteEmpreendimento getPorteParaCalculoDeTaxa() {

		if(this.porteEmpreendimento == null) {
			return null;
		}

		PorteEmpreendimento porte = null;
		br.ufla.lemaf.beans.Empreendimento empreendimento =  WebServiceEntradaUnica.findEmpreendimentosEU(this.caracterizacao.empreendimento);

		if(empreendimento.porte != null && (empreendimento.porte.equals("ME") || empreendimento.porte.equals("EPP"))){
			porte = PorteEmpreendimento.find("codigo = :codigo")
					.setParameter("codigo", "MICRO")
					.first();
		}
		else {
			porte = PorteEmpreendimento.find("codigo = :codigo")
					.setParameter("codigo", this.porteEmpreendimento.codigo)
					.first();
		}
		return porte;
	}

	public String getNomeAtividade(){
		return this.atividade != null ? this.atividade.nome : "";
	}

	public String getCodigoAtividade(){
		return this.atividade != null ? this.atividade.codigo : "";
	}

	public Boolean containsPergunta(Pergunta pergunta){
		return this.atividade.containsPergunta(pergunta);
	}

	public Boolean atividadeDentroEmpreendimento(Atividade at){
		return this.atividade.dentroEmpreendimento.equals(at.dentroEmpreendimento);
	}

	public void zeraParametros(){
		this.atividadeCaracterizacaoParametros.forEach(acp -> acp.id = null);
	}

	@Override
	public List getListaSobreposicao() {
		return this.sobreposicaoCaracterizacaoAtividades;
	}

	@Override
	public void setListaSobreposicao(List sobreposicoes) {
		this.sobreposicaoCaracterizacaoAtividades = sobreposicoes;
	}

	@Override
	public <T extends SobreposicaoDistancia> T
	getObjetoSobreposicao(DadosSobreposicaoVO dadosSobreposicao, GuardaSobreposicao guardaSobreposicao, TipoSobreposicao tipoSobreposicao) {
		return (T) new SobreposicaoCaracterizacaoAtividade(tipoSobreposicao,(AtividadeCaracterizacao) guardaSobreposicao, dadosSobreposicao);
	}
}
