package models.analise;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema="analise", name="condicionante")
public class Condicionante extends GenericModel {

    public static final String SEQ = "analise.condicionante_id_seq";

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

    @Required
    @Column(name = "prazo")
    public Integer prazo;

}
