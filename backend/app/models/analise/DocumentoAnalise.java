package models.analise;

import java.io.File;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

@Entity
@Table(schema = "analise", name = "documento")
public class DocumentoAnalise extends GenericModel {

	private static final String SEQ = "analise.documento_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@Required
	public String caminho;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_tipo_documento", referencedColumnName="id")
	public TipoDocumentoAnalise tipo;
	
	@Transient
	public String key;
	
	@Transient
	public String base64;
	
	@Transient
	public File arquivo;
	
	@Transient
	public String extensao;
	
	@Required
	@Column(name="data_cadastro")
	public Date dataCadastro;

}
