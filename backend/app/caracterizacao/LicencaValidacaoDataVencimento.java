package models.caracterizacao;

import jdk.nashorn.internal.ir.annotations.Immutable;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

@Immutable
@Entity
@Table(schema = "licenciamento", name = "licenca")
public class LicencaValidacaoDataVencimento extends GenericModel {

    @Id
    @Column(name = "id")
    public Long id;

    @Column(name = "data_validade")
    public Date dataValidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_caracterizacao", referencedColumnName = "id", nullable = false)
    public Caracterizacao caracterizacao;

}