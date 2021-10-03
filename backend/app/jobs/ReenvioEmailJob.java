package jobs;


import models.EmailNotificacaoCancelamentoDla;
import models.ReenvioEmail;
import models.analise.DlaCancelada;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.jobs.On;
import utils.Configuracoes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@On("cron.reenviarEmail")
public class ReenvioEmailJob extends GenericJob {

	@Override
	public void executar() throws Exception {

		Logger.info("[INICIO-JOB] ::ReenvioEmail:: [INICIO-JOB]");

		List<ReenvioEmail> reenviosEmail = ReenvioEmail.findAll();

		for(ReenvioEmail reenvioEmail : reenviosEmail) {

			sendMail(reenvioEmail);

		}

		Logger.info("[FIM-JOB] ::ReenvioEmail:: [FIM-JOB]");

	}
	private void sendMail(ReenvioEmail reenvioEmail) {

		if(Configuracoes.SEND_MAIL_ENABLED) {
			try {

				Set<String> emailsDestinatarios = new HashSet<>(Arrays.asList(StringUtils.split(reenvioEmail.emailsDestinatario, ";")));

				DlaCancelada dlaCancelada = DlaCancelada.findById(reenvioEmail.idItensEmail);
				new EmailNotificacaoCancelamentoDla(dlaCancelada, emailsDestinatarios).enviar();

				reenvioEmail.delete();

			} catch (Exception e) {

				reenvioEmail.log = e.getMessage();

				reenvioEmail.save();

			}
		}

	}

}