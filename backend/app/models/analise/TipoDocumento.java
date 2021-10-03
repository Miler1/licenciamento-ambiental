package models.analise;

import models.pdf.PDFTemplate;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "analise", name = "tipo_documento")
public class TipoDocumento extends Model {

	public static final Long DOCUMENTO_ANALISE_JURIDICA = 1L;
	public static final Long DOCUMENTO_ANALISE_TECNICA = 2L;
	public static final Long PARECER_ANALISE_JURIDICA = 3L;
	public static final Long PARECER_ANALISE_TECNICA = 4L;
	public static final Long NOTIFICACAO_ANALISE_JURIDICA = 5L;
	public static final Long NOTIFICACAO_ANALISE_TECNICA = 6L;
	public static final Long DOCUMENTO_ANALISE_MANEJO = 7L;
	public static final Long AREA_DE_MANEJO_FLORESTAL_SOLICITADA = 8L;
	public static final Long AREA_DA_PROPRIEDADE = 9L;
	public static final Long AREA_SEM_POTENCIAL = 10L;
	public static final Long DOCUMENTO_COMPLEMENTAR_MANEJO = 12L;
	public static final Long TERMO_DELIMITACAO_AREA_RESERVA_LEGAL_APROVADA = 13L;
	public static final Long TERMO_AJUSTAMENTO_CONDUTA = 14L;
	public static final Long ANEXO_PROCESSO_MANEJO_DIGITAL = 15L;
	public static final Long PARECER_ANALISE_GEO = 16L;
	public static final Long NOTIFICACAO_ANALISE_GEO = 17L;
	public static final Long DOCUMENTO_INCONSISTENCIA = 18L;
	public static final Long CARTA_IMAGEM = 19L;
	public static final Long DOCUMENTO_COMUNICADO = 20L;
	public static final Long DOCUMENTO_OFICIO_ORGAO = 21L;
	public static final Long DOCUMENTO_NOTIFICACAO_ANALISE_GEO = 22L;
	public static final	Long DOCUMENTO_ANALISE_TEMPORAL = 23l;
	public static final Long DOCUMENTO_INCONSISTENCIA_TECNICA = 26l;
	public static final Long DOCUMENTO_RIT = 24L;
	public static final Long DOCUMENTO_VISTORIA = 25L;
	public static final Long INCONSISTENCIA_VISTORIA = 27L;
	public static final Long AUTO_INFRACAO = 29L;

	@Required
	public String nome;

	@Column(name="caminho_modelo")
	public String caminhoModelo;

	@Required
	@Column(name="caminho_pasta")
	public String caminhoPasta;

	@Required
	@Column(name="prefixo_nome_arquivo")
	public String prefixoNomeArquivo;

}
