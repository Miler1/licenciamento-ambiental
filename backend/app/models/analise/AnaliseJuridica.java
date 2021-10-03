package models.analise;

import models.UsuarioLicenciamento;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema="analise", name="analise_juridica")
public class AnaliseJuridica extends GenericModel {

	public static final String SEQ = "analise.analise_juridica_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@ManyToOne
	@JoinColumn(name="id_analise")
	public Analise analise;
	
	public String parecer;
	
	@Required
	@Column(name="data_vencimento_prazo")
	public Date dataVencimentoPrazo;
	
	@Required
	@Column(name="revisao_solicitada")
	public Boolean revisaoSolicitada;
	
	public Boolean ativo;
	
	@OneToOne
	@JoinColumn(name="id_analise_juridica_revisada", referencedColumnName="id")
	public AnaliseJuridica analiseJuridicaRevisada;
	
	@Column(name="data_inicio")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataInicio;
	
	@Column(name="data_fim")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataFim;
	
	@ManyToOne
	@JoinColumn(name="id_tipo_resultado_analise")
	public TipoResultadoAnalise tipoResultadoAnalise;

	@ManyToOne
	@JoinColumn(name="id_tipo_resultado_validacao")
	public TipoResultadoAnalise tipoResultadoValidacao;	
	
	@ManyToMany
	@JoinTable(schema="analise", name="rel_documento_analise_juridica", 
		joinColumns=@JoinColumn(name="id_analise_juridica"), 
		inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<DocumentoAnalise> documentos;
	
	@OneToMany(mappedBy="analiseJuridica", cascade=CascadeType.ALL)
	public List<AnaliseDocumento> analisesDocumentos;
	
	@OneToMany(mappedBy="analiseJuridica", cascade=CascadeType.ALL)
	public List<ConsultorJuridico> consultoresJuridicos;
	
	@Column(name="parecer_validacao")
	public String parecerValidacao;
	
 	@ManyToOne(fetch=FetchType.LAZY)
 	@JoinColumn(name = "id_usuario_validacao", referencedColumnName = "id")
	public UsuarioLicenciamento usuarioValidacao;
	
	@ManyToOne
	@JoinColumn(name="id_tipo_resultado_validacao_aprovador")
	public TipoResultadoAnalise tipoResultadoValidacaoAprovador;
	
	@Column(name="parecer_validacao_aprovador")
	public String parecerValidacaoAprovador;
	
 	@ManyToOne(fetch=FetchType.LAZY)
 	@JoinColumn(name = "id_usuario_validacao_aprovador", referencedColumnName = "id")
	public UsuarioLicenciamento usuarioValidacaoAprovador;
 	
	public static AnaliseJuridica findByProcesso(Processo processo) {
		return AnaliseJuridica.find("analise.processo.id = :idProcesso")
				.setParameter("idProcesso", processo.id)
				.first();
	}
 	
}