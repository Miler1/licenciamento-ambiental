package models.caracterizacao;

import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "licenciamento", name = "rel_atividade_pergunta")
public class RelAtividadePergunta extends GenericModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "id_atividade")
    public Atividade atividade;

    @ManyToOne
    @JoinColumn(name = "id_pergunta")
    public Pergunta pergunta;

    @Column(name = "ordem")
    public Integer ordem;

    public static Integer getOrdem(Long idAtividade, Long idPergunta) {
        RelAtividadePergunta relAtividadePergunta = find("atividade.id = :idAtividade AND pergunta.id = :idPergunta")
                .setParameter("idAtividade", idAtividade)
                .setParameter("idPergunta", idPergunta)
                .first();

        return relAtividadePergunta.ordem;
    }

}
