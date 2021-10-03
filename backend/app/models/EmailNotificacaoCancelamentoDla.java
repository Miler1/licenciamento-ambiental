package models;

import exceptions.AppException;
import models.analise.DlaCancelada;
import notifiers.Emails;
import utils.Configuracoes;

import java.util.Set;
import java.util.concurrent.ExecutionException;

public class EmailNotificacaoCancelamentoDla extends EmailNotificacao {

    private DlaCancelada dlaCancelada;

    public EmailNotificacaoCancelamentoDla(DlaCancelada dlaCancelada, Set<String> emailsDestinatarios) {

        super(emailsDestinatarios);
        this.dlaCancelada = dlaCancelada;

    }

    @Override
    public void enviar() {

        if(Configuracoes.SEND_MAIL_ENABLED) {
            try {

                if (!Emails.notificarRequerenteCancelamentoDla(this.emailsDestinatarios, this.dlaCancelada).get()) {

                    throw new AppException();

                }

            } catch (InterruptedException | ExecutionException | AppException e) {

                ReenvioEmail reenvioEmail = new ReenvioEmail(this.dlaCancelada.id, e.getMessage(), this.emailsDestinatarios);
                reenvioEmail.save();

                e.printStackTrace();

            }
        }
    }

}
