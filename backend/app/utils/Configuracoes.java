package utils;

import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import play.Logger;
import play.Play;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Configuracoes {

	public static String HTTP_PATH = getConfig("http.path", "");
	public static String DOMAIN_URL = getConfig("application.domainURL", null);
	public static String APP_URL = DOMAIN_URL + HTTP_PATH + "/" ;

	private static String DEFAULT_LOGIN_URL = "/login";
	public static String ENTRADA_UNICA_URL_PORTAL_SEGURANCA = Play.configuration.getProperty("entrada.unica.url.portal.seguranca");
	public static String ENTRADA_UNICA_URL_CADASTRO_UNIFICADO = Play.configuration.getProperty("entrada.unica.url.cadastro.unificado");
	public static String URL_PORTAL_AP = Play.configuration.getProperty("portal.ap.url");
	public static String URL_RECUPERAR_SENHA_PRODAP = Play.configuration.getProperty("prodap.url.recuperar.senha");

	public static String AUTH_SERVICE = getConfig("auth.service", null);
	public static String LOGIN_URL = getConfig("auth.login.url", DEFAULT_LOGIN_URL);
	public static Boolean EXTERNAL_LOGIN = !LOGIN_URL.equals(DEFAULT_LOGIN_URL);
	public static String INDEX_URL = "app/index.html";

	public static String ADRESS_LIST = getConfig("authentication.external.httpAddress", "");
	public static String VALIDATE_ADDRESS = getConfig("authentication.external.validateAddress", false);

	public static String PUBLIC_ROUTE = getConfig("authentication.url.public", "/public/");
	public static String EXTERNAL_ROUTE = getConfig("authentication.url.external", "/external/");

	public static String APPLICATION_TEMP_FOLDER = getConfig("application.tempFolder", Play.applicationPath + "/tmp/");

	public static long TAMANHO_MAXIMO_ARQUIVO = getLongConfig("sistema.tamanhoMaximoArquivoUpload");
	public static long TAMANHO_MAXIMO_DOCUMENTO = getLongConfig("sistema.tamanhoMaximoDocumentoUpload");

	public static CoordinateReferenceSystem CRS_DEFAULT = null;
	public static String GEOSERVER_URL = getConfig("geoserver.url", null);
	public static String GEOSERVER_SICAR_LAYER_LDI = getConfig("geoserver.layer.ldi", null);
	public static String GEOSERVER_SEMAS = getConfig("geoserver.semas.url", null);
	public static String GEOSERVER_SEMAS_LAYER_PRODES = getConfig("geoserver.semas.layer.prodes", null);
	public static String GEOSERVER_SEMAS_LAYER_AREA_CONSOLIDADA_CLASSIFICADA = getConfig("geoserver.semas.layer.area.consoliada.classificada", null);
	public static String GEOSERVER_WMS = GEOSERVER_URL + getConfig("geoserver.wms", null);
	public static String GEOSERVER_GETCAPABILITIES = GEOSERVER_URL + getConfig("geoserver.getCapabilities", null);
	public static String GEOSERVER_LAYER_TERRA_INDIGENA = getConfig("geoserver.layer.terraIndigena", null);
	public static String GEOSERVER_LAYER_TERRA_INDIGENA_ZA = getConfig("geoserver.layer.terraIndigena.za", null);
	public static String GEOSERVER_LAYER_TERRA_INDIGENA_ESTUDO = getConfig("geoserver.layer.terraIndigena.estudo", null);
	public static String GEOSERVER_LAYER_UC_FEDERAL = getConfig("geoserver.layer.uc.federal", null);
	public static String GEOSERVER_LAYER_UC_FEDERAL_ZA = getConfig("geoserver.layer.uc.federal.za", null);
	public static String GEOSERVER_LAYER_UC_FEDERAL_APA_DENTRO = getConfig("geoserver.layer.uc.federal.apa.dentro", null);
	public static String GEOSERVER_LAYER_UC_FEDERAL_APA_FORA = getConfig("geoserver.layer.uc.federal.apa.fora", null);
	public static String GEOSERVER_LAYER_UC_ESTADUAL = getConfig("geoserver.layer.uc.estadual", null);
	public static String GEOSERVER_LAYER_UC_ESTADUAL_ZA = getConfig("geoserver.layer.uc.estadual.za", null);
	public static String GEOSERVER_LAYER_UC_ESTADUAL_ZA_PI_FORA = getConfig("geoserver.layer.uc.estadual.za.pi.fora", null);
	public static String GEOSERVER_LAYER_UC_ESTADUAL_PI_DENTRO = getConfig("geoserver.layer.uc.estadual.protecaoIntegral.dentro", null);
	public static String GEOSERVER_LAYER_UC_ESTADUAL_PI_FORA = getConfig("geoserver.layer.uc.estadual.protecaoIntegral.fora", null);
	public static String GEOSERVER_LAYER_UC_MUNICIPAL = getConfig("geoserver.layer.uc.municipal", null);
	public static String GEOSERVER_LAYER_TOMBAMENTO_ENCONTRO_AGUAS = getConfig("geoserver.layer.tombamentoEncontroAguas", null);
	public static String GEOSERVER_LAYER_TOMBAMENTO_ENCONTRO_AGUAS_ZA = getConfig("geoserver.layer.tombamentoEncontroAguas.za", null);
	public static String GEOSERVER_LAYER_SAUIM_DE_COLEIRA = getConfig("geoserver.layer.sauimDeColeira", null);
	public static String GEOSERVER_LAYER_AUTO_DE_INFRACAO_IBAMA = getConfig("geoserver.layer.autoDeInfracao.ibama", null);
	public static String GEOSERVER_LAYER_AREAS_EMBARGADAS_IBAMA = getConfig("geoserver.layer.areasEmbargadas.ibama", null);
	public static String GEOSERVER_LAYER_SITIOS_ARQUEOLOGICOS = getConfig("geoserver.layer.sitiosArqueologicos", null);
	public static String GEOSERVER_LAYER_BENS_ACAUTELADOS_IPHAN_PT = getConfig("geoserver.layer.bensAcautelados.iphan.pt", null);
	public static String GEOSERVER_LAYER_BENS_ACAUTELADOS_IPHAN_POL = getConfig("geoserver.layer.bensAcautelados.iphan.pol", null);

	public static String URL_SICAR = getConfig("sicar.url", null);
	public static String URL_SICAR_IMOVEIS_SIMPLIFICADOS = URL_SICAR + getConfig("sicar.imoveisSimplificados.url", null);
	public static String URL_SICAR_IMOVEIS_COMPLETOS = URL_SICAR + getConfig("sicar.imoveisCompletos.url", null);
	public static String URL_SICAR_IMOVEL_COMPLETO = URL_SICAR + getConfig("sicar.imovelCompleto.url", null);
	public static String URL_SICAR_TEMAS_IMOVEIS_COMPLETOS = URL_SICAR + getConfig("sicar.imoveis.temas.url", null);
	public static String URL_SICAR_SOBREPOSICAO_MZEE = URL_SICAR + getConfig("sicar.sobreposicoes.mzee.url", null);
	public static String URL_SICAR_SOBREPOSICAO_TEMAS_CAR = URL_SICAR + getConfig("sicar.sobreposicoes.temasCAR.url", null);
	public static String URL_SICAR_EXCEDENTE_PASSIVO = URL_SICAR + getConfig("sicar.sobreposicoes.excedentePassivo.url", null);

	public static String ARQUIVOS_PATH = getConfig("arquivos.path", null);
	public static String ARQUIVOS_DOCUMENTOS_PATH = ARQUIVOS_PATH + getConfig("arquivos.documentos.path", null);

	public static String ESTADO = "AP";

	public static String APPLICATION_TEMPLATES_DOCUMENTOS_FOLDER = getConfig("application.templates.documentos.folder", null);

	public static int SRID = getIntConfig("geometrias.srid");
	public static double TOLERANCIA_GEOMETRIA = getDoubleConfig("geometrias.tolerancia");

	public static int DAE_LICENCA_DIAS_VENCIMENTO = getIntConfig("dae.licenca.vencimento");
	public static String PATH_AUTH_TOKEN = Play.applicationPath.getAbsolutePath() + File.separator + "conf" + File.separator + "dae" + File.separator;
	public static String FILE_NAME_AUTH_TOKEN = getConfig("dae.ws.auth.token", null);
	public static String DAE_WS_URL = getConfig("dae.ws.url", null);
	public static String DAE_WS_EMISSAO_URL = DAE_WS_URL + getConfig("dae.ws.emissaoDae.url", null);
	public static String DAE_WS_REGISTRO_PAGAMENTO_URL = DAE_WS_URL + getConfig("dae.ws.registroPagamento", null);
	public static int DAE_PERIODO_PERMITIDO_EMISSAO_NOVO_DAE = getIntConfig("dae.periodoPermitidoGeracaoNovoDae");

	public static String MAIL_CAR_SEMA = getConfig("mail.smtp.sema.sender", null);
	public static String MAIL_SEMA_COPIA = getConfig("mail.smtp.sema.copia", null);

	public static String PDF_TEMPLATES_FOLDER_PATH = "templates" + File.separator + "pdf";
	public static String PDF_TEMPLATES_FOLDER_ABSOLUTE = Play.applicationPath.getAbsolutePath() + File.separator + "app" + File.separator + "views" + File.separator;

	public static boolean JOBS_ENABLED = getBooleanConfig("jobs.enabled");
	public static boolean SEND_MAIL_ENABLED = getBooleanConfig("mail.send.enabled");
	public static boolean SEND_DASHBOARD_ENABLED = getBooleanConfig("dashboard.send.enabled");
	public static boolean SEND_REDE_SIMPLES_ENABLED = getBooleanConfig("redeSimples.send.enabled");

	public static Integer ID_BRASIL_EU = getIntConfig("id.brasil.eu");

	public static String URL_FIND_ESTADOS = getConfig("url.find.estados", null);
	public static String URL_FIND_MUNICIPIOS = getConfig("url.find.municipios", null);
	public static Integer CODIGO_SEXO_MASCULINO = getIntConfig("codigo.sexo.masculino");
	public static Integer CODIGO_SEXO_FEMININO = getIntConfig("codigo.sexo.feminino");
	public static Integer CODIGO_TIPO_PESSOA_FISICA = getIntConfig("codigo.tipo.pessoa.fisica");
	public static Integer CODIGO_TIPO_PESSOA_JURIDICA = getIntConfig("codigo.tipo.pessoa.juridica");
	public static Integer ID_TIPO_ENDERECO_PRINCIPAL = getIntConfig("id.tipo.endereco.principal");
	public static Integer ID_TIPO_ENDERECO_CORRESPONDENCIA = getIntConfig("id.tipo.endereco.correspondencia");
	public static Integer ID_ZONA_LOCALIZACAO_ENDERECO_URBANA = getIntConfig("id.zona.localizacao.endereco.urbana");
	public static Integer ID_ZONA_LOCALIZACAO_ENDERECO_RURAL = getIntConfig("id.zona.localizacao.endereco.rural");
	public static String URL_PESSOA_FISICA = getConfig("url.pessoa.fisica", null);
	public static String URL_EMPREENDIMENTO = getConfig("url.empreendimento", null);
	public static String URL_PESSOA_JURIDICA = getConfig("url.pessoa.juridica", null);
	public static String URL_VINCULAR_GESTAO_EMPREENDIMENTOS = getConfig("url.vincular.gestaoEmpreendimentos", null);

	// Gestão de Pagamentos
	public static String CODIGO_BENEFICIARIO = getConfig("pagamentos.codigo.beneficiario", "SEMA");
	public static String CODIGO_MODULO = getConfig("pagamentos.codigo.modulo", "LICENCIAMENTO");
	public static String URL_GESTAO_PAGAMENTOS = getConfig("pagamentos.url", "http://runners.ti.lemaf.ufla.br/gestao-pagamentos");
	public static Boolean VERIFICAR_PAGAMENTO_GESTAO_PAGAMENTO = getBooleanConfig("pagamentos.verifica.pagamento.gestao.pagamentos");

	public static String ARQUIVOS_ANALISE_PATH = getConfig("arquivos.path.analise", null);
	public static String ARQUIVOS_DOCUMENTOS_ANALISE_PATH = ARQUIVOS_ANALISE_PATH + getConfig("arquivos.documentos.path", null);

	public static String ARQUIVOS_LICENCIAMENTO_PATH = getConfig("arquivos.path.licenciamento", null);
	public static String ARQUIVOS_DOCUMENTOS_LICENCIAMENTO_PATH = ARQUIVOS_LICENCIAMENTO_PATH + getConfig("arquivos.documentos.path", null);

	public static String PATH_TABELA_TAXA_VALORES_LIENCIAMENTO = Play.applicationPath.getAbsolutePath() + File.separator + "public" + File.separator + "documentos" + File.separator + "tabela_valores_taxas.pdf";

	static {

		try {

			CRS_DEFAULT = CRS.parseWKT(getConfig("sistema.crs.default", null));

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/*
	 * Métodos utilitários
	 */

	private static String getConfig(String configKey, Object defaultValue) {

		String defaultTextValue = defaultValue != null ? defaultValue.toString() : null;

		String configValue = Play.configuration.getProperty(configKey, defaultTextValue);

		return configValue.isEmpty() ? defaultValue.toString() : configValue;
	}

	private static Integer getIntConfig(String configKey) {

		String config = Play.configuration.getProperty(configKey);

		return config != null ? Integer.parseInt(config) : null;
	}

	private static Double getDoubleConfig(String configKey) {

		String config = Play.configuration.getProperty(configKey);

		return config != null ? Double.parseDouble(config) : null;
	}

	private static boolean getBooleanConfig(String configKey) {

		String config = Play.configuration.getProperty(configKey);

		return config != null ? Boolean.parseBoolean(config) : null;
	}

	private static List<String> getStringListConfig(String configKey, String separator, String defaultValue) {

		String config = Play.configuration.getProperty(configKey);

		if (config == null || config.isEmpty()) {
			config = defaultValue;
		}

		if (config != null && !config.isEmpty()) {

			String [] values = config.split(separator);

			return Arrays.asList(values);
		}

		return null;

	}

	private static File getFileConfig(String property, String defaultPath) {

		String path = getConfig(property, null);

		if (path != null) {
			return new File(path);
		}

		if (defaultPath != null) {
			return new File(defaultPath);
		}

		return null;
	}

	private static Long getLongConfig(String configKey) {

		String config = Play.configuration.getProperty(configKey);

		if (config != null && !config.isEmpty()) {
			return Long.parseLong(config);
		}
		else {
			return null;
		}
	}

}
