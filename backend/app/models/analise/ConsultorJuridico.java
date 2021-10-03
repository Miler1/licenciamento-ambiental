package models.analise;

import models.UsuarioLicenciamento;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema="analise", name="consultor_juridico")
public class ConsultorJuridico extends GenericModel {
	
	public static final String SEQ = "analise.consultor_juridico_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_analise_juridica")
	public AnaliseJuridica analiseJuridica;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_usuario")
	public UsuarioLicenciamento usuario;
	
	@Required
	@Column(name="data_vinculacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataVinculacao;

}
