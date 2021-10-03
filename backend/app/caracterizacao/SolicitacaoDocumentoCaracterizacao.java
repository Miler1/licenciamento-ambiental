package models.caracterizacao;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.tika.Tika;

import exceptions.ValidacaoException;
import models.Documento;
import models.TipoDocumento;
import play.db.jpa.GenericModel;
import utils.Configuracoes;
import utils.Mensagem;

@Entity
@Table(schema = "licenciamento", name = "solicitacao_documento_caracterizacao")
public class SolicitacaoDocumentoCaracterizacao extends GenericModel {

	private static final String SEQ = "licenciamento.solicitacao_documento_caracterizacao_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@ManyToOne
	@JoinColumn(name="id_caracterizacao", referencedColumnName="id")
	public Caracterizacao caracterizacao;

	@ManyToOne
	@JoinColumn(name="id_tipo_documento", referencedColumnName="id")
	public TipoDocumento tipoDocumento;

	public Boolean obrigatorio;

	@OneToOne
	@JoinColumn(name = "id_documento", referencedColumnName="id")
	public Documento documento;

	public SolicitacaoDocumentoCaracterizacao(){
	}

	public SolicitacaoDocumentoCaracterizacao(Caracterizacao caracterizacao, SolicitacaoDocumentoCaracterizacao so){
		this.caracterizacao = caracterizacao;
		this.tipoDocumento = so.tipoDocumento;
		this.obrigatorio = so.obrigatorio;
		this.documento = so.documento;
	}

	public SolicitacaoDocumentoCaracterizacao(Caracterizacao caracterizacao, TipoDocumento tipoDocumento, Documento documento) {
		this.caracterizacao = caracterizacao;
		this.tipoDocumento = tipoDocumento;
		this.documento = documento;
	}

	public SolicitacaoDocumentoCaracterizacao(TipoDocumentoTipoLicenca tipoDocumentoSolicitado, Caracterizacao caracterizacao){
		this.tipoDocumento = tipoDocumentoSolicitado.tipoDocumento;
		this.obrigatorio = tipoDocumentoSolicitado.obrigatorio;
		this.caracterizacao = caracterizacao;
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
	public SolicitacaoDocumentoCaracterizacao save() {

		if (this.obrigatorio == null)
			this.obrigatorio = true;

		return super.save();
	}


	/**
	 * Gera uma cópia da solicitação de documento, duplicando todas as informações
	 * declaradas pelo cadastrante. Utilizado quando duplicar a caracterização.
	 * Ver também {@link Caracterizacao#gerarCopia()}
	 */
	public SolicitacaoDocumentoCaracterizacao gerarCopia() {
		
		SolicitacaoDocumentoCaracterizacao copia = new SolicitacaoDocumentoCaracterizacao();
		copia.tipoDocumento = this.tipoDocumento;
		copia.obrigatorio = this.obrigatorio;
		copia.documento = this.documento;
		
		return copia;
	}

	public static boolean containsTipoDocumento(List<SolicitacaoDocumentoCaracterizacao> solicitacoes, SolicitacaoDocumentoCaracterizacao solicitacao) {

		for (SolicitacaoDocumentoCaracterizacao solicitacaoSalva : solicitacoes) {

			if (solicitacaoSalva.tipoDocumento.nome.compareTo(solicitacao.tipoDocumento.nome) == 0) {

				return true;
			}
		}

		return false;
	}

	public void zerarDocumentos(Caracterizacao caracterizacao){
		this.id = null;
		this.caracterizacao = caracterizacao;
		this.documento = null;
	}
}
