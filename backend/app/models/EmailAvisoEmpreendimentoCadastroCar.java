package models;

import exceptions.AppException;
import notifiers.Emails;
import utils.Configuracoes;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;


public class EmailAvisoEmpreendimentoCadastroCar extends EmailAviso {

    private Empreendimento empreendimento;

    public EmailAvisoEmpreendimentoCadastroCar(Empreendimento empreendimento, List<String> emailsDestinatarios) {

        super(emailsDestinatarios);
        this.empreendimento = empreendimento;

    }

    @Override
    public void enviar() {

        if(Configuracoes.SEND_MAIL_ENABLED) {
            try {

                if (!Emails.notificarCadastroCAR(this.emailsDestinatarios, this.empreendimento).get()) {

                    throw new AppException();

                }

            } catch (InterruptedException | ExecutionException | AppException e) {

//                ReenvioEmail reenvioEmail = new ReenvioEmail(this.empreendimento.id, e.getMessage(), this.emailsDestinatarios);
//                reenvioEmail.save();

                e.printStackTrace();

            }
        }
    }

}
