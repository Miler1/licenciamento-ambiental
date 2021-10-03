package models.analise;

import models.UsuarioLicenciamento;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema="analise", name="analista_tecnico")
public class AnalistaTecnico extends GenericModel {
	
	public static final String SEQ = "analise.analista_tecnico_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_analise_tecnica")
	public AnaliseTecnica analiseTecnica;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_usuario")
	public UsuarioLicenciamento usuario;
	
	@Required
	@Column(name="data_vinculacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataVinculacao;

}
