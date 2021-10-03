package models.caracterizacao;

import models.AtividadeParametroLimites;
import play.db.jpa.GenericModel;
import utils.ListUtil;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(schema = "licenciamento", name = "atividade")
public class Atividade extends GenericModel {

	@Id
	public Long id;

	public String nome;

	@ManyToOne
	@JoinColumn(name="id_tipologia")
	public Tipologia tipologia;

	@Column(name = "geo_linha")
	public Boolean temLinha;

	@Column(name = "geo_ponto")
	public Boolean temPonto;

	@Column(name = "geo_poligono")
	public Boolean temPoligono;

	@Column(name = "ativo")
	public Boolean ativo;

	@Column(name = "v1")
	public Boolean v1;

	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_tipo_licenca_caracterizacao_andamento",
			joinColumns = @JoinColumn(name = "id_caracterizacao"),
			inverseJoinColumns = @JoinColumn(name = "id_tipo_licenca"))
	public List<TipoLicenca> tiposLicencaEmAndamento;

	// @OrderBy("ordem ASC")
	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_atividade_pergunta",
			joinColumns = @JoinColumn(name = "id_atividade"),
			inverseJoinColumns = @JoinColumn(name = "id_pergunta"))
	public List<Pergunta> perguntas;

	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_atividade_tipo_atividade",
			joinColumns = @JoinColumn(name = "id_atividade"),
			inverseJoinColumns = @JoinColumn(name = "id_tipo_atividade"))
	public List<TipoAtividade> tiposAtividade;

	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_atividade_porte_atividade",
			joinColumns = @JoinColumn(name = "id_atividade"),
			inverseJoinColumns = @JoinColumn(name = "id_porte_atividade"))
	public List<PorteAtividade> portesAtividade;

	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_atividade_taxa_licenciamento",
			joinColumns = @JoinColumn(name = "id_atividade"),
			inverseJoinColumns = @JoinColumn(name = "id_taxa_licenciamento"))
	public List<TaxaLicenciamento> taxasLicenca;

	public String codigo;

	@Column(name = "licenciamento_municipal")
	public Boolean licenciamentoMunicipal;

	@Column(name = "limite_parametro_municipal")
	public Double limiteParametroMunicipal;

	@Column(name = "limite_inferior_simplificado")
	public Double limiteInferiorLicenciamentoSimplificado;

	@Column(name = "limite_superior_simplificado")
	public Double limiteSuperiorLicenciamentoSimplificado;

	@Column(name = "observacoes")
	public String observacoes;

	@Column(name = "sigla_setor")
	public String siglaSetor;

	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_atividade_parametro_atividade",
			joinColumns = @JoinColumn(name = "id_atividade"),
			inverseJoinColumns = @JoinColumn(name = "id_parametro_atividade"))
	public List<ParametroAtividade> parametros;

	@ManyToOne
	@JoinColumn(name="id_potencial_poluidor")
	public PotencialPoluidor potencialPoluidor;

	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_atividade_tipo_licenca",
			joinColumns = @JoinColumn(name = "id_atividade"),
			inverseJoinColumns = @JoinColumn(name = "id_tipo_licenca"))
	public List<TipoLicenca> tiposLicenca;

	@OneToMany(mappedBy = "atividade")
	public List<TipoCaracterizacaoAtividade> tiposCaracterizacaoAtividade;

	@OneToOne(mappedBy = "atividade")
	public AtividadeParametroLimites limites;

	@Column(name = "dentro_empreendimento")
	public Boolean dentroEmpreendimento;

	@Column(name = "dentro_municipio")
	public Boolean dentroMunicipio;

	@Transient
	public List<AtividadeCnae> atividadesCnae;

	@ElementCollection
	@CollectionTable(
			schema = "licenciamento", name = "rel_atividade_parametro_atividade",
			joinColumns = @JoinColumn(name = "id_atividade")
	)
	@MapKeyJoinColumn(name = "id_parametro_atividade", updatable = false, insertable = false)
	public Map<ParametroAtividade, RelAtividadeParametrosAtividade> parametrosAtividadeComIdEDescricaoMap;

	@ManyToOne
	@JoinColumn(name="id_grupo_documento")
	public GrupoDocumento grupoDocumento;

	@Column(name = "item_antigo")
	public Boolean itemAntigo;

	public static List<Atividade> findAtividades(TipoCaracterizacaoAtividade.FiltroAtividade filtro) {

		String jpql = "SELECT DISTINCT atividade " +
				"FROM " + Atividade.class.getSimpleName() + " atividade " +
				"   JOIN atividade.tiposCaracterizacaoAtividade tiposCaracterizacaoAtividade " +
				"   JOIN atividade.tiposAtividade tiposAtividade " +
				(filtro.licenciamentoSimplificado != null ? " JOIN atividade.tiposLicenca tiposLicenca " : " ") +
				"WHERE ";

		if(filtro.dispensaLicenciamento != null) {
			jpql += " (tiposCaracterizacaoAtividade.dispensaLicenciamento = :dispensaLicenciamento) ";
		}

		else if(filtro.licenciamentoSimplificado != null) {

			jpql += " (tiposCaracterizacaoAtividade.licenciamentoSimplificado = :licenciamentoSimplificado " +
					"OR tiposCaracterizacaoAtividade.licenciamentoDeclaratorio = :licenciamentoDeclaratorio) ";
		}

		if(filtro.idTipologia != null) {
			jpql += " AND (atividade.tipologia.id = :idTipologia) " ;
		}

		if(filtro.listaCodigosCnaes != null) {
			if(filtro.listaCodigosCnaes.isEmpty()) {
				return new ArrayList<>();
			}

			jpql += " AND (tiposCaracterizacaoAtividade.atividadeCnae.codigo IN :listaCodigosCnae) ";
		}

		if (filtro.tipoEndereco == 0){
			jpql+= " AND (tiposAtividade.codigo = 'URBANA') ";
		}
		else{
			jpql+= " AND (tiposAtividade.codigo = 'RURAL') ";
		}

		//jpql+= " AND v1=true";

		jpql+= " AND atividade.ativo=true AND item_antigo=false";

		JPAQuery jpaQuery = Atividade.find(jpql);

		if(filtro.dispensaLicenciamento != null) {

			jpaQuery.setParameter("dispensaLicenciamento", filtro.dispensaLicenciamento);
		}

		else if(filtro.licenciamentoSimplificado != null) {

			jpaQuery.setParameter("licenciamentoSimplificado", filtro.licenciamentoSimplificado);
			jpaQuery.setParameter("licenciamentoDeclaratorio", filtro.licenciamentoSimplificado);
		}

		if(filtro.idTipologia != null) {

 			jpaQuery.setParameter("idTipologia", filtro.idTipologia);
		}

		if(filtro.listaCodigosCnaes != null) {

			jpaQuery.setParameter("listaCodigosCnae", filtro.listaCodigosCnaes);
		}


		List<Atividade> atividades = jpaQuery.fetch();

		//Se o filtro é nulo, não precisa filtrar novamente
		if(filtro.listaCodigosCnaes != null) {

			for (Atividade atividade : atividades) {

				List<TipoCaracterizacaoAtividade> tiposCaracterizacaoAtividadeFiltrados = new ArrayList<>();

				for (TipoCaracterizacaoAtividade tipoCaracterizacaoAtividade : atividade.tiposCaracterizacaoAtividade) {

					if(filtro.listaCodigosCnaes.contains(tipoCaracterizacaoAtividade.atividadeCnae.codigo)) {

						tiposCaracterizacaoAtividadeFiltrados.add(tipoCaracterizacaoAtividade);
					}
				}

				atividade.tiposCaracterizacaoAtividade = tiposCaracterizacaoAtividadeFiltrados;
			}
		}

		//Popular cada atividadesCnae
		atividades.forEach(atividade -> {

			atividade.parametros = atividade.parametros.stream().map(p ->
					new ParametroAtividade(p, atividade.parametrosAtividadeComIdEDescricaoMap.get(p).descricaoUnidade)
			).collect(Collectors.toList());

			atividade.atividadesCnae = atividade.tiposCaracterizacaoAtividade.stream().map(tca -> tca.atividadeCnae).collect(Collectors.toList());
		});

		return atividades;
	}

	public static List<TipoCaracterizacaoAtividade> findByAtividadeCaracterizacao(AtividadeCaracterizacao atividade) {

		return find("atividade.id = :idAtividade AND atividadeCnae.id in (:cnaes)")
				.setParameter("idAtividade", atividade.atividade.id)
				.setParameter("cnaes", ListUtil.getIds(atividade.atividadesCnae))
				.fetch();
	}

	public boolean containsTipoLicenca(Long idTipoLicenca) {

		if(this.tiposLicenca == null) {
			this.tiposLicenca = ((Atividade)findById(this.id)).tiposLicenca;
		}

		return this.tiposLicenca.stream().anyMatch(tl -> tl.id.equals(idTipoLicenca));
	}

	@Embeddable
	public class RelAtividadeParametrosAtividade {
		@Column(name = "descricao_unidade", updatable = false, insertable = false)
		public String descricaoUnidade;
	}

	public Boolean containsPergunta(Pergunta pergunta){
		return this.perguntas.contains(pergunta);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Atividade)) return false;
		if (!super.equals(o)) return false;
		Atividade atividade = (Atividade) o;
		return id.equals(atividade.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), id, nome, siglaSetor);
	}
}

