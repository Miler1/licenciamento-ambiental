package models.analise;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import models.caracterizacao.Caracterizacao;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Identificavel;

@Entity
@Table(schema="analise", name="licenca_analise")
public class LicencaAnalise extends GenericModel implements Identificavel {
	
	public static final String SEQ = "analise.licenca_analise_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	public Integer validade;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_caracterizacao")
	public Caracterizacao caracterizacao;
	
	public String observacao;
	
	public Boolean emitir;
	
	private boolean emitirIsTrue() {
		
		return Boolean.TRUE.equals(this.emitir);
	}
	
	public Integer getValidadeMaxima() {
		return caracterizacao.tipoLicenca.validadeEmAnos;
	}

	@Override
	public Long getId() {
		
		return this.id;
	}	
}
