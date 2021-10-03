package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.DashboardAlertas.Alerta;
import play.Logger;
import play.Play;
import play.i18n.Messages;
import play.libs.WS;
import play.mvc.Http;

public class WebServiceDashboardAlertas {

	private static final String URL_DASHBOARD = Play.configuration.getProperty("dashboard.url");
	private static final String URL_ENVIAR_RELATORIO = URL_DASHBOARD + Play.configuration.getProperty("dashboard.servico.enviarAlerta");

	private static WebService ws = new WebService().setDefaultHeader("www-authenticate", Play.secretKey);

	public static boolean enviarAlerta(Alerta alerta) {

		final String url = URL_ENVIAR_RELATORIO;

		WS.HttpResponse response = ws.postJSON(url, alerta);

		verificarEnvio(response, url, alerta);

		return true;
	}

	/**
	 * Verifica se o retorno do serviço foi erro e exibe a mensagem recebida
	 */
	private static void verificarEnvio(WS.HttpResponse response, String url, Alerta alerta) {

		if (response.getStatus() != Http.StatusCode.OK) {

			Gson gsonEnvio = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

			String jsonEnvio = gsonEnvio.toJson(alerta);

			throw new RuntimeException("Erro ao comunicar com serviço na URL - " + url + " (" + response.getString() + " - " + jsonEnvio + ")");
		}
	}

}
