package models.caracterizacao;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.*;

import models.TipoDocumento;
import play.db.jpa.Model;

@Entity
@Table(schema = "licenciamento", name = "tipo_documento_tipo_licenca")
public class TipoDocumentoTipoLicenca extends Model {

	@ManyToOne
	@JoinColumn(name="id_tipo_documento", referencedColumnName="id")
	public TipoDocumento tipoDocumento;

	@ManyToOne
	@JoinColumn(name="id_tipo_licenca", referencedColumnName="id")
	public TipoLicenca tipoLicenca;

	public Boolean obrigatorio;

	@Column(name = "tipo_pessoa")
	public String tipoPessoa;


}
