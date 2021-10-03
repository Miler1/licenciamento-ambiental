package models.analise;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(schema = "analise", name = "parecer_analista_geo")
public class ParecerAnalistaGeo extends GenericModel implements ParecerAnalista {

	public static final String SEQ = "analise.parecer_analista_geo_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	@Column(name = "id")
	public Long id;

	@OneToOne
	@JoinColumn(name = "id_analise_geo")
	public AnaliseGeo analiseGeo;

	@ManyToOne
	@JoinColumn(name = "id_tipo_resultado_analise")
	public TipoResultadoAnalise tipoResultadoAnalise;

	@Column(name = "parecer")
	public String parecer;

	@Column(name = "situacao_fundiaria")
	public String situacaoFundiaria;

	@Column(name = "analise_temporal")
	public String analiseTemporal;

	@Column(name = "conclusao")
	public String conclusao;

	@Column(name = "data_parecer")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataParecer;

	@OneToMany(fetch=FetchType.EAGER)
	@JoinTable(schema="analise", name="rel_documento_parecer_analista_geo",
			joinColumns=@JoinColumn(name="id_parecer_analista_geo"),
			inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<models.analise.Documento> documentos;

	@Override
	public List<Documento> getDocumentosNotificacao(){
		return this.documentos == null ? Collections.emptyList() :
				this.documentos.stream().filter(Documento::isAnaliseGeo).collect(Collectors.toList());
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public TipoResultadoAnalise getTipoResultadoAnalise() {
		return this.tipoResultadoAnalise;
	}

	@Override
	public String getParecer() {
		return this.parecer;
	}
}
