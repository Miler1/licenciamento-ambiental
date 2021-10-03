package jobs;

import models.DashboardAlertas.Alerta;
import models.caracterizacao.ComunicacaoDashboard;
import play.Logger;
import play.jobs.On;
import play.jobs.OnApplicationStart;
import utils.Configuracoes;
import utils.WebServiceDashboardAlertas;

import java.util.List;

/**
 * Job que enviar Alerta ao Dashboard".
 */
@On("cron.envioDashboardAlerta")
@OnApplicationStart
public class EnvioDashboardAlerta extends GenericJob {

	@Override
	public void executar() {

//		Logger.info("EnvioDashboardAlerta:: Iniciando job");
//
//		List<ComunicacaoDashboard> comunicacaoDashboards = ComunicacaoDashboard.findAll();
//
//		for(ComunicacaoDashboard comunicacaoDashboard : comunicacaoDashboards) {
//
//			processarEnvioDashboardAlerta(comunicacaoDashboard);
//		}
//
//		Logger.info("EnvioDashboardAlerta:: Encerrando job");
	}

	private void processarEnvioDashboardAlerta(ComunicacaoDashboard comunicacaoDashboard) {

		if(Configuracoes.SEND_DASHBOARD_ENABLED) {
			try {

				Logger.info("EnvioDashboardAlerta:: Processando envio id " + comunicacaoDashboard.id + " e caracterização id " + comunicacaoDashboard.caracterizacao.id);

				Alerta alerta = new Alerta(comunicacaoDashboard.caracterizacao);
				Boolean alertaEnviado = WebServiceDashboardAlertas.enviarAlerta(alerta);

				if (alertaEnviado) {
					comunicacaoDashboard.delete();
				}

				commitTransaction();

				Logger.info("EnvioDashboardAlerta:: Finalizado envio id " + comunicacaoDashboard.id + " e caracterização id " + comunicacaoDashboard.caracterizacao.id);

			} catch (Exception e) {

				Logger.error(e.getMessage());

				comunicacaoDashboard.tentativas++;
				comunicacaoDashboard.save();
				commitTransaction();
			}
		}

	}

}
