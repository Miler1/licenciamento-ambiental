package models.analise;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

@Entity
@Table(schema="analise", name="analise")
public class Analise extends GenericModel {

	private final static String SEQ = "analise.analise_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_processo")
	public Processo processo;
	
	@Required
	@Column(name="data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;
	
	@Required
	@Column(name="data_vencimento_prazo")
	public Date dataVencimentoPrazo;
	
	@OneToMany(mappedBy="analise")
	public List<AnaliseJuridica> analisesJuridica;
	
	@OneToMany(mappedBy="analise")
	public List<AnaliseTecnica> analisesTecnicas;	
	
	public Boolean ativo;
	
	@Transient
	public AnaliseJuridica analiseJuridica;
	
	@Transient 
	public AnaliseTecnica analiseTecnica;

	@Transient
	public AnaliseGeo analiseGeo;
	
	@Column(name="notificacao_aberta")
	public Boolean temNotificacaoAberta;
	
	public AnaliseJuridica getAnaliseJuridica() {
		this.analiseJuridica = this.analiseJuridica != null ?
				this.analiseJuridica : AnaliseJuridica.findByProcesso(this.processo);
		return this.analiseJuridica;
	}
	
	public AnaliseTecnica getAnaliseTecnica() {
		this.analiseTecnica = this.analiseTecnica != null ?
				this.analiseTecnica : AnaliseTecnica.findByProcesso(this.processo);
		return this.analiseTecnica;
	}

	public AnaliseGeo getAnaliseGeo() {
		this.analiseGeo = this.analiseGeo != null ? this.analiseGeo : AnaliseGeo.findByProcesso(this.processo);
		return this.analiseGeo;
	}
	
	public static Analise findByProcesso(Processo processo) {
		return Analise.find("processo.id = :idProcesso AND ativo = true")
				.setParameter("idProcesso", processo.id)
				.first();
	}
	
}
