package models.analise;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.persistence.*;

import org.apache.tika.Tika;

import exceptions.ValidacaoException;
import models.caracterizacao.Caracterizacao;
import play.db.jpa.GenericModel;
import utils.Mensagem;

@Entity
@Table(schema="analise", name="notificacao")
public class Notificacao extends GenericModel {
	
	private final static String SEQ = "analise.notificacao_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@ManyToOne
	@JoinColumn(name="id_analise_juridica", nullable=true)
	public AnaliseJuridica analiseJuridica;
	
	@ManyToOne
	@JoinColumn(name="id_analise_tecnica", nullable=true)
	public AnaliseTecnica analiseTecnica;

	@ManyToOne
	@JoinColumn(name="id_analise_geo", nullable=true)
	public AnaliseGeo analiseGeo;
	
	@ManyToOne
	@JoinColumn(name="id_tipo_documento", referencedColumnName="id")
	public TipoDocumento tipoDocumento;
	
	@ManyToOne
	@JoinColumn(name="id_documento_corrigido", nullable=true)
	public models.Documento documentoCorrigido;
	
	@ManyToOne
	@JoinColumn(name="id_analise_documento")
	public AnaliseDocumento analiseDocumento;
	
	public Boolean resolvido;
	
	public Boolean ativo;

	@Column(name="data_notificacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataNotificacao;

	@Column(name="data_final_notificacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataFinalNotificacao;

	@ManyToMany
	@JoinTable(schema="analise", name="rel_documento_notificacao",
			joinColumns=@JoinColumn(name="id_notificacao"),
			inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> documentos;

	@Column(name="documentacao")
	public Boolean documentacao;

	@Column(name="retificacao_empreendimento")
	public Boolean retificacaoEmpreendimento;

	@Column(name="retificacao_solicitacao")
	public Boolean retificacaoSolicitacao;

	@Column(name="retificacao_solicitacao_com_geo")
	public Boolean retificacaoSolicitacaoComGeo;

	@Column(name="justificativa_documentacao")
	public String justificativaDocumentacao;

	@Column(name="justificativa_retificacao_empreendimento")
	public String justificativaRetificacaoEmpreendimento;

	@Column(name="justificativa_retificacao_solicitacao")
	public String justificativaRetificacaoSolicitacao;

	@Transient
	public Long prazo;


	private static JPAQuery getAnaliseGeoQuery(Analise analise){
		return Notificacao.find("analiseGeo.id = :idAnaliseGeo AND ativo = true")
				.setParameter("idAnaliseGeo", analise.getAnaliseGeo().id);
	}

	private static JPAQuery getAnaliseTecnicaQuery(Analise analise){
		return Notificacao.find("analiseTecnica.id = :idAnaliseTecnica AND ativo = true")
				.setParameter("idAnaliseTecnica", analise.getAnaliseTecnica().id);
	}

	private static JPAQuery getAnaliseJuridicaQuery(Analise analise){
		return Notificacao.find("analiseJuridica.id = :idAnaliseJuridica AND ativo = true")
				.setParameter("idAnaliseJuridica", analise.getAnaliseJuridica().id);
	}
	
	public static List<Notificacao> getByAnalise(Analise analise) {

		if(analise.getAnaliseGeo() != null){

			return getAnaliseGeoQuery(analise).fetch();

		} else if(analise.getAnaliseTecnica() != null) {

			return getAnaliseTecnicaQuery(analise).fetch();
		}

		return getAnaliseJuridicaQuery(analise).fetch();
		
	}

	public static Notificacao getAtivaByAnalise(Analise analise) {

		if(analise.getAnaliseGeo() != null && analise.getAnaliseTecnica() == null) {

			return getAnaliseGeoQuery(analise).first();

		} else if(analise.getAnaliseTecnica() != null){

			return getAnaliseTecnicaQuery(analise).first();

		}

		return getAnaliseJuridicaQuery(analise).first();

	}

	private static Processo findByIdCaracterizacao(Long idCaracterizacao){
		return Processo.find("id_caracterizacao = :idCaracterizacao ORDER BY id DESC")
				.setParameter("idCaracterizacao",idCaracterizacao).first();
	}

	public static List<Notificacao> findByCaracterizacao(Caracterizacao caracterizacao) {

		Processo processo = findByIdCaracterizacao(caracterizacao.id);

		return processo != null ? Notificacao.getByAnalise(processo.getAnalise()) : null;

	}

	public static Notificacao findAtivaByCaracterizacao(Long idCaracterizacao) {

		Caracterizacao c = Caracterizacao.findById(idCaracterizacao);
		return findAtivaByCaracterizacao(c);
	}

	public static Notificacao findAtivaByCaracterizacao(Caracterizacao c) {

		Long idCaracterizacao = c.isNotificacaoEmAndamento() && c.idCaracterizacaoOrigem != null ? c.idCaracterizacaoOrigem : c.id;

		Processo processo = findByIdCaracterizacao(idCaracterizacao);

		return processo != null ? Notificacao.getAtivaByAnalise(processo.getAnalise()) : null;
	}
	
	private boolean isExtensaoDocumentoValida(File file) throws IOException {
		
		String realType = null;
		
		// Detecta o tipo de arquivo pela assinatura (Magic)
		Tika tika = new Tika();
		realType = tika.detect(file);

		return (realType != null && realType.contains("application/pdf"));
	}

	public Documento saveDocumento(File file) throws IOException {

		if (!isExtensaoDocumentoValida(file)) {

			throw new ValidacaoException(Mensagem.UPLOAD_EXTENSAO_NAO_SUPORTADA);
		}

		Documento documento = new Documento();

		documento.tipo = new TipoDocumento();

		documento.tipo = TipoDocumento.findById(TipoDocumento.NOTIFICACAO_ANALISE_GEO);
		documento.arquivo = file;
		documento.save();

		this.documentos.add(documento);
		this.justificativaDocumentacao = "";
		super.save();

		return documento;
	}
	
	public File getFile() {

		if (this.documentoCorrigido == null){

			throw new ValidacaoException(Mensagem.DOCUMENTO_DA_SOLICITACAO_NAO_ENCONTRADO);
		}

		return this.documentoCorrigido.getFile();
	}
	
	public Documento deleteDocumento(Long idDocumento) {

		Documento documento = Documento.findById(idDocumento);
		
		if (isBlank(this.documentos) || documento == null){
			
			throw new ValidacaoException(Mensagem.DOCUMENTO_DA_SOLICITACAO_NAO_ENCONTRADO);
		}

		this.documentos.remove(documento);
		
		super.save();
		
		return documento.delete();
	}

	public void justificarRetificacao(String justificativa) {
		
		this.justificativaRetificacaoSolicitacao = justificativa;
		super.save();		
		
	}

	public void justificarRetificacaoEmpreendimento(String justificativa) {

		this.justificativaRetificacaoEmpreendimento = justificativa;
		super.save();

	}

	public void justificarDocumentacao(String justificativa) {

		this.justificativaDocumentacao = justificativa;
		super.save();

	}

	public void removerJustificativa() {

//		this.justificativa = null;
		this.resolvido = false;

		super.save();

	}

	public static void setDataLeitura(List<Notificacao> notificacaos) {

		for (Notificacao notificacao : notificacaos) {

			if (notificacao.dataFinalNotificacao == null) {

				notificacao.dataFinalNotificacao = new Date();
				notificacao._save();
			}
		}
	}

	public void inicializaPrazo(){
		long diff = this.dataFinalNotificacao.getTime() - new Date().getTime();
		this.prazo = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	public Boolean documentacaoFinalizada(){
		return !this.documentacao || !isBlank(this.justificativaDocumentacao) || !isBlank(this.documentos);
	}

	private Boolean isBlank(String str){
		return  str == null || str.trim().isEmpty();
	}

	private Boolean isBlank(Collection<?> list){
		return  list == null || list.isEmpty();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Notificacao that = (Notificacao) o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), id);
	}

	public void initParecerNotificacao(){

		if(this.analiseGeo != null){
			this.analiseGeo.initParecerNotificacao();
		}else if(this.analiseTecnica != null){
			this.analiseTecnica.initParecerNotificacao();
		}else if(this.analiseJuridica != null){

		}

	}
}
