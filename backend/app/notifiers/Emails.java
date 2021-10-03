package notifiers;

import models.Empreendimento;
import models.analise.DlaCancelada;
import play.Play;
import play.mvc.Mailer;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

public class Emails extends Mailer {

    public static Future<Boolean> notificarRequerenteCancelamentoDla(Set<String> destinatarios, DlaCancelada dlaCancelada) {

        setSubject("Notificação referente ao cancelamento %s(%s)", dlaCancelada.dispensaLicenciamento.caracterizacao.tipoLicenca.nome, dlaCancelada.dispensaLicenciamento.numero);
        setFrom("Licenciamento Ambiental <"+ Play.configuration.getProperty("mail.smtp.sender") + ">");
        for(String email:destinatarios) {

            addRecipient(email);
        }
        return send(dlaCancelada);

    }

    public static Future<Boolean> notificarCadastroCAR(List<String> destinatario, Empreendimento empreendimento) {

        setSubject("Comunicado do Sistema de Licenciamento Ambiental/CAR do empreendimento " + empreendimento.empreendimentoEU.denominacao);

        setFrom("Licenciamento <"+ Play.configuration.getProperty("mail.smtp.sender") + ">");

        for(String email: destinatario) {

            addRecipient(email);
        }

        return send(empreendimento);

    }
}
