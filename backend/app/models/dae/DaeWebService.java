package models.dae;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.xml.ws.WebServiceException;

import org.apache.commons.io.FileUtils;

import models.caracterizacao.Dae;
import utils.Configuracoes;
import utils.WebService;

import com.google.gson.reflect.TypeToken;

/**
 * Classe responsável por fazer a interface de comunicação com os webservices
 * de emissão de DAE da Secretaria da Fazenda.
 */
public class DaeWebService {

	public WebService webService = createWebService();
	
	
	public ResultadoEmissaoDae emitirDae(EmissaoDae emissaoDae) {
		
		ResultadoEmissaoDae resultado = webService.postJSON(
				Configuracoes.DAE_WS_EMISSAO_URL, emissaoDae, ResultadoEmissaoDae.class);
		
		if (resultado == null)
			throw new WebServiceException("Resultado nulo ao emitir DAE");
		
		if (resultado.numeroDae == null || resultado.numeroDae.isEmpty())
			throw new WebServiceException("Número do DAE não retornado ao emitir DAE");
		
		if (resultado.pdfDae == null || resultado.pdfDae.isEmpty())
			throw new WebServiceException("PDF não retornado ao emitir DAE");
		
		
		return resultado;
	}
	
	
	public RegistroPagamentoDae obterRegistroPagamento(Dae dae) {
		
		String url = Configuracoes.DAE_WS_REGISTRO_PAGAMENTO_URL.replace("{numeroDae}", dae.numero);
		
		RegistroPagamentoDae registro = webService.getJson(url, RegistroPagamentoDae.class);
		
		return registro;
	}
	
	
	/**
	 * Cria uma nova instância de WebService já configurando o token de autenticação
	 * necessário para as comunicações desta classe.
	 */
	private WebService createWebService() {
		
		WebService ws = new WebService();
		File token = new File(Configuracoes.PATH_AUTH_TOKEN + Configuracoes.FILE_NAME_AUTH_TOKEN);
		
		if (token != null) {
			
			if (!token.exists()) {
				
				throw new RuntimeException("Arquivo '" + token.getAbsolutePath() 
						+ "' contendo token de autenticação do webservice de dae não encontrado.");
			}
		
			try {
				ws.setDefaultHeader("Authorization", "Bearer " + FileUtils.readFileToString(token, StandardCharsets.UTF_8));
				
			} catch (IOException e) {
				
				throw new RuntimeException(e);
			}
		
		}
		
		return ws;
	}
}
