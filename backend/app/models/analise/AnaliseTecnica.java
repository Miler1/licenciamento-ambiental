package models.analise;

import models.UsuarioLicenciamento;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(schema="analise", name="analise_tecnica")
public class AnaliseTecnica extends AbstractAnalise {

	public static final String SEQ = "analise.analise_tecnica_id_seq";

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
	@JoinColumn(name="id_analise_tecnica_revisada", referencedColumnName="id")
	public AnaliseTecnica analiseTecnicaRevisada;

	@Column(name="data_inicio")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataInicio;

	@Column(name="data_fim")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataFim;

	@ManyToOne
	@JoinColumn(name="id_tipo_resultado_analise")
	public TipoResultadoAnalise tipoResultadoAnalise;

	@ManyToMany
	@JoinTable(schema="analise", name="rel_documento_analise_tecnica",
		joinColumns=@JoinColumn(name="id_analise_tecnica"),
		inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> documentos;

	@OneToMany(mappedBy="analiseTecnica", cascade=CascadeType.ALL)
	public List<AnaliseDocumento> analisesDocumentos;

	@OneToMany(mappedBy="analiseTecnica", cascade=CascadeType.ALL)
	public List<AnalistaTecnico> analistasTecnicos;

	@Column(name="parecer_validacao")
	public String parecerValidacao;

 	@ManyToOne(fetch=FetchType.LAZY)
 	@JoinColumn(name = "id_usuario_validacao", referencedColumnName = "id")
	public UsuarioLicenciamento usuarioValidacao;

	@Column(name="justificativa_coordenador")
	public String justificativaCoordenador;

	@ManyToOne
	@JoinColumn(name="id_tipo_resultado_validacao_coordenador")
	public TipoResultadoAnalise tipoResultadoValidacaoCoordenador;

	@Column(name="parecer_validacao_coordenador")
	public String parecerValidacaoCoordenador;

 	@ManyToOne(fetch=FetchType.LAZY)
 	@JoinColumn(name = "id_usuario_validacao_coordenador", referencedColumnName = "id")
	public UsuarioLicenciamento usuarioValidacaoCoordenador;

	@Required
	@Column(name="data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;

	@ManyToOne
	@JoinColumn(name="id_tipo_resultado_validacao_aprovador")
	public TipoResultadoAnalise tipoResultadoValidacaoAprovador;

	@Column(name="parecer_validacao_aprovador")
	public String parecerValidacaoAprovador;

 	@ManyToOne(fetch=FetchType.LAZY)
 	@JoinColumn(name = "id_usuario_validacao_aprovador", referencedColumnName = "id")
	public UsuarioLicenciamento usuarioValidacaoAprovador;

 	@Column(name="data_fim_validacao_aprovador")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataFimValidacaoAprovador;


	@OneToMany(mappedBy = "analiseTecnica", fetch = FetchType.LAZY)
	public List<ParecerAnalistaTecnico> pareceresAnalistaTecnico;


	public static AnaliseTecnica findByProcesso(Processo processo) {
		return AnaliseTecnica.find("analise.processo.id = :idProcesso")
				.setParameter("idProcesso", processo.id)
				.first();
	}
	@Override
	public List<? extends ParecerAnalista> getPareceresAnalistas() {
		return this.pareceresAnalistaTecnico;
	}

	@Override
	public void setParecer(String parecer) {
		this.parecer = parecer;
	}

	@Override
	public void setDocumentos(List<Documento> documentos) {
		this.documentos = documentos;
	}

}
