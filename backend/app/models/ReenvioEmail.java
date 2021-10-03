package models;

import org.apache.commons.lang.StringUtils;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "reenvio_email", schema = "licenciamento")
public class ReenvioEmail extends GenericModel {

    public static final String SEQ = "analise.reenvio_email_id_seq";

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

    @Column(name = "id_itens_email")
    public Long idItensEmail;

    public String log;

    @Column(name = "emails_destinatario")
    public String emailsDestinatario;

    public ReenvioEmail(Long idItensEmail, String log, Set<String> emailsDestinatario) {

        super();
        this.idItensEmail = idItensEmail;
        this.log = log;

        this.emailsDestinatario = StringUtils.join(emailsDestinatario, ";");

    }

}
