package controllers;

import exceptions.AppException;
import br.ufla.lemaf.beans.pessoa.Pessoa;
import br.ufla.lemaf.services.CadastroUnificadoPessoaService;
import models.Documento;
import models.UsuarioLicenciamento;
import models.analise.DlaCancelada;
import models.caracterizacao.Caracterizacao;
import models.caracterizacao.Dae;
import models.caracterizacao.DispensaLicenciamento;
import models.caracterizacao.Licenca;
import org.apache.commons.collections.FastHashMap;
import play.libs.Crypto;
import security.models.UsuarioSessao;
import security.services.Auth;
import serializers.ApplicationSerializer;
import utils.Configuracoes;
import utils.Mensagem;
import utils.WebServiceEntradaUnica;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class Application extends GenericController {

	
	public static void index() {

		UsuarioSessao usuarioSessao = Auth.getAuthenticatedUser(session.current());
		
		if (usuarioSessao != null)
			redirect(Configuracoes.INDEX_URL);
		else
			redirect(Configuracoes.LOGIN_URL);
	}
	
	
	
	public static void findInfo() {
		
		DadosApp dados = new DadosApp();
		dados.usuarioSessao = Auth.getAuthenticatedUser(session.current());
		String jsonConfig = ApplicationSerializer.findInfo.serialize(dados);
		
		render(jsonConfig);
	}
	
	public static void versao() {
		
		render();
	}
	
	public static class DadosApp {

		public UsuarioSessao usuarioSessao;
		public ConfiguracoesApp configuracoes = new ConfiguracoesApp();
	}
	
	public static class ConfiguracoesApp {
		
		public Double toleranciaGeometria = Configuracoes.TOLERANCIA_GEOMETRIA;
		public String baseURL = Configuracoes.HTTP_PATH;
	}

	
	public static void qrcodeView(String numero) throws UnsupportedEncodingException {
		
		Licenca licenca = Licenca.findByNumero(Crypto.decryptAES(numero));

		licenca.caracterizacao.empreendimento.empreendimentoEU = WebServiceEntradaUnica.findEmpreendimentosEU(licenca.caracterizacao.empreendimento);
		
		String url = Configuracoes.APP_URL + "licenca/" + Crypto.encryptAES(licenca.numero) + "/download";
		String nomeArquivo = licenca.caracterizacao.tipoLicenca.nome + "_" + licenca.numero + ".pdf";
		
		Map<String, Object> args = new FastHashMap(3);
		args.put("licenca", licenca);
		args.put("urlDownload", url);
		args.put("nomeArquivo", nomeArquivo);
		
		renderTemplate(Configuracoes.PDF_TEMPLATES_FOLDER_PATH + "/qrcode/informacoes.html", args);

	}
	
	public static void downloadLicenca(String numero) throws Exception {

		Licenca licenca = Licenca.findByNumero(Crypto.decryptAES(numero));

		if(licenca.isSuspensa())
			throw new AppException(Mensagem.LICENCA_CANCELADA_OU_SUSPENSA);

		Documento documento = Documento.findById(licenca.documento.id);

		if (licenca.caracterizacao.dae.status != Dae.Status.PAGO) {
			throw new AppException(Mensagem.DAE_NAO_PAGA);
		}

		File file = documento.getFile();
		
		if(!file.exists()) {
			Licenca.gerarPdfLicenca(licenca);
			file = ((Documento) Documento.findById(licenca.documento.id)).getFile();
		}
		
		renderBinary(file, file.getName());

	}



	public static void qrCodeViewDla(String idCaracterizacao) {

		DispensaLicenciamento dla = DispensaLicenciamento.find("byCaracterizacao", Caracterizacao.findById(Long.valueOf(Crypto.decryptAES(idCaracterizacao)))).first();

		dla.caracterizacao.empreendimento.empreendimentoEU = WebServiceEntradaUnica.findEmpreendimentosEU(dla.caracterizacao.empreendimento);

		DlaCancelada dlaCancelada = new DlaCancelada();

		UsuarioLicenciamento executorCancelamento = new UsuarioLicenciamento();

		if (dla.ativo == false) {

			dlaCancelada = DlaCancelada.find("byDispensaLicenciamento", dla).first();

			executorCancelamento = UsuarioLicenciamento.find("id", dlaCancelada.usuario.id).first();
		}

		String url = Configuracoes.APP_URL + "dla/" + Crypto.encryptAES(dla.caracterizacao.id.toString()) + "/download";
		String nomeArquivo = "di_" + dla.documento.id + ".pdf";
				
		
		Map<String, Object> args = new FastHashMap(3);
		args.put("dla", dla);
		args.put("dlaCancelada", dlaCancelada);
		args.put("executorCancelamento", executorCancelamento);
		args.put("urlDownload", url);
		args.put("nomeArquivo", nomeArquivo);
		
		renderTemplate(Configuracoes.PDF_TEMPLATES_FOLDER_PATH + "/qrcode/informacoes_dla.html", args);		
	}



	public static void downloadDla(String idCaracterizacao) throws Exception {

		DispensaLicenciamento dla = DispensaLicenciamento.find("byCaracterizacao", Caracterizacao.findById(Long.valueOf(Crypto.decryptAES(idCaracterizacao)))).first();

		Documento documento = Documento.findById(dla.documento.id);

		if (dla.caracterizacao.dae.status.equals(Dae.Status.PAGO)) {

			File file = documento.getFile();

			if(!file.exists()) {
				dla.gerarPDFDispensa();
				file = ((Documento) Documento.findById(dla.documento.id)).getFile();
			}

			renderBinary(file, file.getName());
		}
	}

	public static void validaUsuario(String cpfCnpjUsuario) {

		if(cpfCnpjUsuario != null && cpfCnpjUsuario.length() > 0){
			renderJSON(WebServiceEntradaUnica.verificarUsuarioByLogin(cpfCnpjUsuario));
		}
	}
}
