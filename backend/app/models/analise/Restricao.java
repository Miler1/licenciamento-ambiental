package models.analise;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema="analise", name="restricao")
public class Restricao extends GenericModel {

    public static final String SEQ = "analise.restricao_id_seq";

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

    @ManyToOne
    @JoinColumn(name="id_parecer_analista_tecnico")
    public ParecerAnalistaTecnico parecerAnalistaTecnico;

    @Required
    @Column(name = "texto")
    public String texto;

}
