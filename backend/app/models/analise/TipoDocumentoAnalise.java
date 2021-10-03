package models.analise;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(schema = "analise", name = "tipo_documento")
public class TipoDocumentoAnalise extends Model {
	
	public static Long DOCUMENTO_ANALISE_JURIDICA = 1l;
	public static Long DOCUMENTO_ANALISE_TECNICA = 2l;	


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
