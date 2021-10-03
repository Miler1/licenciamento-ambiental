package models.caracterizacao;

import exceptions.ValidacaoException;
import models.Documento;
import models.TipoDocumento;
import org.apache.tika.Tika;
import play.db.jpa.GenericModel;
import utils.Configuracoes;
import utils.Mensagem;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "solicitacao_grupo_documento")
public class SolicitacaoGrupoDocumento extends GenericModel {

	private static final String SEQ = "licenciamento.solicitacao_grupo_documento_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@ManyToOne
	@JoinColumn(name="id_caracterizacao", referencedColumnName="id")
	public Caracterizacao caracterizacao;

	@ManyToOne
	@JoinColumn(name="id_grupo_documento", referencedColumnName="id")
	public GrupoDocumento grupoDocumento;

	@ManyToOne
	@JoinColumn(name="id_tipo_documento", referencedColumnName="id")
	public TipoDocumento tipoDocumento;

	@Column(name = "obrigatorio")
	public Boolean obrigatorio = true;

	@OneToOne
	@JoinColumn(name = "id_documento", referencedColumnName="id")
	public Documento documento;

	public SolicitacaoGrupoDocumento(){
	}

	public SolicitacaoGrupoDocumento(Caracterizacao caracterizacao, GrupoDocumento grupoDocumento, TipoDocumento tipoDocumento, Boolean obrigatorio) {
		this.caracterizacao = caracterizacao;
		this.grupoDocumento = grupoDocumento;
		this.tipoDocumento = tipoDocumento;
		this.obrigatorio = obrigatorio;
	}

	public SolicitacaoGrupoDocumento(Caracterizacao caracterizacao, SolicitacaoGrupoDocumento so){
		this.caracterizacao = caracterizacao;
		this.grupoDocumento = so.grupoDocumento;
		this.tipoDocumento = so.tipoDocumento;
		this.obrigatorio = so.obrigatorio;
		this.documento = so.documento;
	}

	public SolicitacaoGrupoDocumento(Caracterizacao caracterizacao, GrupoDocumento grupoDocumento, TipoDocumento tipoDocumento, Documento documento) {
		this.caracterizacao = caracterizacao;
		this.grupoDocumento = grupoDocumento;
		this.tipoDocumento = tipoDocumento;
		this.documento = documento;
	}

	public SolicitacaoGrupoDocumento(Caracterizacao caracterizacao, GrupoDocumento grupoDocumento, TipoDocumento tipoDocumento) {
		this.caracterizacao = caracterizacao;
		this.grupoDocumento = grupoDocumento;
		this.tipoDocumento = tipoDocumento;
	}

	public SolicitacaoGrupoDocumento(Caracterizacao caracterizacao, GrupoDocumento grupoDocumento) {
		this.caracterizacao = caracterizacao;
		this.grupoDocumento = grupoDocumento;
	}



	public File getFile() {

		if (this.documento == null){

			throw new ValidacaoException(Mensagem.DOCUMENTO_DA_SOLICITACAO_NAO_ENCONTRADO);
		}

		return this.documento.getFile();
	}

	public Documento deleteDocumento() {

		if (this.documento == null){

			throw new ValidacaoException(Mensagem.DOCUMENTO_DA_SOLICITACAO_NAO_ENCONTRADO);
		}

		Documento documentoASerExcluido = this.documento;

		this.documento = null;

		super.save();

		return documentoASerExcluido.delete();
	}

	public void desvincularDocumento() {

		if (this.documento == null){

			throw new ValidacaoException(Mensagem.DOCUMENTO_DA_SOLICITACAO_NAO_ENCONTRADO);
		}

		this.documento = null;

		super.save();
	}

	public Documento saveDocumento(File file) throws IOException {
		
		if (!isExtensaoDocumentoValida(file)) {

			throw new ValidacaoException(Mensagem.UPLOAD_EXTENSAO_NAO_SUPORTADA);
		}
		if (!isTamanhoDocumentoValido(file)) {

			throw new ValidacaoException(Mensagem.UPLOAD_TAMANHO_DOCUMENTO);
		}

		if (this.documento == null) {

			this.documento = new Documento();
		}

		this.documento.tipo = this.tipoDocumento;
		this.documento.arquivo = file;
		this.documento.save();
		
		super.save();
		
		return this.documento;
	}

	private boolean isExtensaoDocumentoValida(File file) throws IOException {

		String realType = null;

		// Detecta o tipo de arquivo pela assinatura (Magic)
		Tika tika = new Tika();
		realType = tika.detect(file);

		return (realType != null && realType.contains("application/pdf"));
	}

	private boolean isTamanhoDocumentoValido(File file) throws IOException {
		if(file.length() > Configuracoes.TAMANHO_MAXIMO_DOCUMENTO){
			return false;
		}
		else{
			return true;
		}
	}

	@Override
	public SolicitacaoGrupoDocumento save() {

		if (this.obrigatorio == null)
			this.obrigatorio = true;

		return super.save();
	}


	/**
	 * Gera uma cópia da solicitação de documento, duplicando todas as informações
	 * declaradas pelo cadastrante. Utilizado quando duplicar a caracterização.
	 * Ver também {@link Caracterizacao#gerarCopia()}
	 */
	public SolicitacaoGrupoDocumento gerarCopia() {
		
		SolicitacaoGrupoDocumento copia = new SolicitacaoGrupoDocumento();
		copia.tipoDocumento = this.tipoDocumento;
		copia.obrigatorio = this.obrigatorio;
		copia.documento = this.documento;
		
		return copia;
	}

	public static boolean containsTipoDocumento(List<SolicitacaoGrupoDocumento> solicitacoes, SolicitacaoGrupoDocumento solicitacao) {

		for (SolicitacaoGrupoDocumento solicitacaoSalva : solicitacoes) {

			if (solicitacaoSalva.tipoDocumento.nome.compareTo(solicitacao.tipoDocumento.nome) == 0) {

				return true;
			}
		}

		return false;
	}

	public void zerarDocumentos(Caracterizacao caracterizacao){
		this.id = null;
		this.caracterizacao = caracterizacao;
		this.grupoDocumento = caracterizacao.atividadesCaracterizacao.get(0).atividade.grupoDocumento;
		this.documento = null;
	}
}
