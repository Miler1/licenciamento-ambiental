package models.pdf;

import java.io.File;

import models.TipoDocumento;
import utils.Configuracoes;

public enum PDFTemplate {

	DISPENSA_LICENCIAMENTO (TipoDocumento.DISPENSA_LICENCIAMENTO),
	LICENCA_PREVIA (TipoDocumento.LICENCA_PREVIA),
	LICENCA_INSTALACAO (TipoDocumento.LICENCA_INSTALACAO),
	LICENCA_OPERACAO (TipoDocumento.LICENCA_OPERACAO),
	LICENCA_AMBIENTAL_UNICA (TipoDocumento.LICENCA_AMBIENTAL_UNICA),
	RENOVACAO_LICENCA_DE_OPERACAO (TipoDocumento.RENOVACAO_LICENCA_DE_OPERACAO),
	RENOVACAO_LICENCA_DE_INSTALACAO(TipoDocumento.RENOVACAO_LICENCA_DE_INSTALACAO),
	ATUALIZACAO_DE_LICENCA_PREVIA(TipoDocumento.ATUALIZACAO_DE_LICENCA_PREVIA),
	RENOVACAO_LICENCA_AMBIENTAL_UNICA(TipoDocumento.RENOVACAO_DE_LICENCA_AMBIENTAL_UNICA),
	CADASTRO_AQUICULTURA(TipoDocumento.CADASTRO_AQUICULTURA);

	private static final File TEMPLATES_FOLDER = new File(Configuracoes.PDF_TEMPLATES_FOLDER_PATH);
	private static final String LIBS_PATH = new File(TEMPLATES_FOLDER, "libs").getPath();
	private static final String BODY_TEMPLATE_NAME = "corpo.html";
	private static final String HEADER_TEMPLATE_NAME = "cabecalho.html";
	private static final String FOOTER_TEMPLATE_NAME = "rodape.html";
	
	private Long idTipoDocumento;
	private File folder;
	
	PDFTemplate() {

	}
	
	PDFTemplate(Long idTipoDocumento) {
		
		this.idTipoDocumento = idTipoDocumento;
		this.folder = new File(Configuracoes.PDF_TEMPLATES_FOLDER_PATH, this.name().toLowerCase());
	}
	
	public String getRootFolderPath() {
		
		return TEMPLATES_FOLDER.getPath();
	}
	
	public String getFolderPath() {
		
		return this.folder.getPath();
	}
	
	public String getFolderAbsolutePath() {
		
		return Configuracoes.PDF_TEMPLATES_FOLDER_ABSOLUTE + this.folder;
	}

	public String getBodyTemplatePath() {
	
		return getFilePathIfExists(BODY_TEMPLATE_NAME);
	}
	
	public String getHeaderTemplatePath() {
		
		return getFilePathIfExists(HEADER_TEMPLATE_NAME);
	}
	
	public String getFooterTemplatePath() {
		
		return getFilePathIfExists(FOOTER_TEMPLATE_NAME);
	}
	
	public String getLibsPath() {
		
		return LIBS_PATH;
	}
	
	private String getFilePathIfExists(String fileName) {
		
		File file = new File(Configuracoes.PDF_TEMPLATES_FOLDER_ABSOLUTE + this.folder, fileName);
		
		return file.exists() ? getFolderPath() + File.separator + fileName : null;
	}
	
	public static PDFTemplate getByTipoDocumento(Long idTipoDocumento) {
		
		for (PDFTemplate template : values()) {
			
			if (template.idTipoDocumento != null && template.idTipoDocumento.equals(idTipoDocumento))
				return template;
		}
		
		return null;
	}
}
