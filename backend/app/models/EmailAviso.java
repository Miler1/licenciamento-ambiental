package models;

import java.util.List;

public abstract class EmailAviso {

    protected List<String> emailsDestinatarios;

    public EmailAviso(List<String> emailsDestinatarios) {
        this.emailsDestinatarios = emailsDestinatarios;
    }

    abstract void enviar();


}
