package models.caracterizacao;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "documento_arrecadacao_licenciamento")
public class DocumentoArrecadacaoLicenciamento extends GenericModel {

	private static final String SEQ = "licenciamento.documento_arrecadacao_licenciamento_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	@Column(name = "id_documento_arrecadacao_licenciamento")
	public Integer idDocumentoArrecadacao;

	@Required
	@ManyToOne
	@JoinColumn(name = "id_dae_licenciamento", referencedColumnName="id")
	public DaeLicenciamento daeLicenciamento;

	@Required
	@Column(name = "nosso_numero")
	public String nossoNumero;

	public static List<DocumentoArrecadacaoLicenciamento> findDocumentosArrecadacaoByIdDaeLicenciamento(Long idDae){

		String BY_ID_DAE_LICENCIAMENTO = "id_dae_licenciamento = :id";

		return find(BY_ID_DAE_LICENCIAMENTO)
				.setParameter("id", idDae)
				.fetch();

	}

}
