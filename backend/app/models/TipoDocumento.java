package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import models.pdf.PDFTemplate;
import play.db.jpa.Model;

@Entity
@Table(schema = "licenciamento", name = "tipo_documento")
public class TipoDocumento extends Model {

	public static Long DOCUMENTO_REPRESENTACAO = 1l;
	public static Long DISPENSA_LICENCIAMENTO = 2l;
	public static Long DOCUMENTO_ARRECADACAO_ESTADUAL = 3l;

	public static Long LICENCA_PREVIA = 66l;
	public static Long LICENCA_INSTALACAO = 67l;
	public static Long LICENCA_OPERACAO = 68l;
	public static Long LICENCA_AMBIENTAL_UNICA = 69l;
	public static Long RENOVACAO_LICENCA_DE_INSTALACAO = 70l;
	public static Long RENOVACAO_LICENCA_DE_OPERACAO = 71l;
	public static Long ATUALIZACAO_DE_LICENCA_PREVIA = 562l;
	public static Long RENOVACAO_DE_LICENCA_AMBIENTAL_UNICA = 561l;
	public static Long CADASTRO_AQUICULTURA = 557l;

	public static final String COD_DISPENSA_LICENCA_AMBIENTAL = "DOC_DI";
	public static final String COD_LICENCA_PREVIA = "DOC_LP";
	public static final String COD_LICENCA_INSTALACAO = "DOC_LI";
	public static final String COD_LICENCA_OPERACAO = "DOC_LO";
	public static final String COD_LICENCA_AMBIENTAL_UNICA = "DOC_LAU";
	public static final String COD_RENOVACAO_LICENCA_DE_INSTALACAO = "DOC_RLI";
	public static final String COD_RENOVACAO_LICENCA_DE_OPERACAO = "DOC_RLO";
	public static final String COD_ATUALIZACAO_DE_LICENCA_PREVIA = "DOC_ALP";
	public static final String COD_RENOVACAO_LICENCA_AMBIENTAL_UNICA = "DOC_RLAU";
	public static final String COD_CADASTRO_AQUICULTURA = "DOC_CA";

	public String nome;

	@Column(name="caminho_modelo")
	public String caminhoModelo;

	@Column(name="caminho_pasta")
	public String caminhoPasta;

	@Column(name="prefixo_nome_arquivo")
	public String prefixoNomeArquivo;

	public String codigo;


	public PDFTemplate getPdfTemplate() {

		return PDFTemplate.getByTipoDocumento(this.id);
	}
}
