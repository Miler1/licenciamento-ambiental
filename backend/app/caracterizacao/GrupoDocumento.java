package models.caracterizacao;

import models.TipoDocumento;
import play.db.jpa.GenericModel;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "grupo_documento")
public class GrupoDocumento extends GenericModel {

    private static final String SEQ = "licenciamento.grupo_documento_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    public Long id;

    @Column(name = "codigo")
    public String codigo;

    @Column(name = "descricao")
    public String descricao;

    @ManyToMany
    @JoinTable(schema = "licenciamento", name = "rel_tipo_licenca_grupo_documento",
            joinColumns = @JoinColumn(name = "id_grupo_documento"),
            inverseJoinColumns = @JoinColumn(name = "id_tipo_documento"))
    public List<TipoDocumento> tiposDocumento;

    @OneToMany(mappedBy = "grupoDocumento")
    public List<TipoLicencaGrupoDocumento> tipoLicencaGrupoDoctos;


}
