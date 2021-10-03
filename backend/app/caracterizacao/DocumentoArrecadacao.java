package models.caracterizacao;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "licenciamento", name = "documento_arrecadacao")
public class DocumentoArrecadacao extends GenericModel {

	private static final String SEQ = "licenciamento.documento_arrecadacao_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	@Column(name = "id_documento_arrecadacao")
	public Integer idDocumentoArrecadacao;

	@Required
	@ManyToOne
	@JoinColumn(name = "id_dae", referencedColumnName="id")
	public Dae dae;

	@Required
	@Column(name = "nosso_numero")
	public String nossoNumero;

}
