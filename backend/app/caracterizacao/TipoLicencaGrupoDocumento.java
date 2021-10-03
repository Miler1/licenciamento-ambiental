package models.caracterizacao;

import models.TipoDocumento;
import play.db.jpa.GenericModel;

import javax.persistence.*;

//@Embeddable
@Entity
@Table(schema = "licenciamento", name = "rel_tipo_licenca_grupo_documento")
public class TipoLicencaGrupoDocumento extends GenericModel {

    @Id
    @ManyToOne
    @JoinColumn(name="id_grupo_documento", referencedColumnName="id" )
    public GrupoDocumento grupoDocumento;

    @Id
    @ManyToOne
    @JoinColumn(name="id_tipo_licenca", referencedColumnName="id")
    public TipoLicenca tipoLicenca;

    @Id
    @ManyToOne
    @JoinColumn(name="id_tipo_documento", referencedColumnName="id")
    public TipoDocumento tipoDocumento;

    @Column(name = "obrigatorio")
    public Boolean obrigatorio;

}
