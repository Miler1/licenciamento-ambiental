package models;

import java.util.Set;

public abstract class EmailNotificacao {

    protected Set<String> emailsDestinatarios;

    public EmailNotificacao(Set<String> emailsDestinatarios) {
        this.emailsDestinatarios = emailsDestinatarios;
    }

    abstract void enviar();


}
