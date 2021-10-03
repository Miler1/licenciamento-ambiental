package models.analise;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import models.Empreendimento;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

@Entity
@Table(schema="analise", name="processo")
public class Processo extends GenericModel{
	
	private static final String SEQ = "analise.processo_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	public String numero;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_empreendimento")
	public Empreendimento empreendimento;
	
	@Column(name = "id_objeto_tramitavel")
	public Long idObjetoTramitavel;

	@OneToMany(mappedBy="processo")
	public List<Analise> analises;
	
	@Required
	@Column(name="data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;
	
	@Transient
	public Analise analise;

	public Analise getAnalise() {

		if(this.analise != null)
			return this.analise;

		if(this.analises != null)
			this.analise = this.analises.stream().filter(analise -> analise.ativo).findFirst().orElse(null);
		
		if(this.analise == null)
			this.analise = Analise.findByProcesso(this);

		return this.analise;
		
	}
	
}
