package models;

import com.vividsolutions.jts.geom.Geometry;
import models.caracterizacao.Atividade;
import play.db.jpa.GenericModel;
import utils.WebService;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

import static utils.Configuracoes.*;

@Entity
@Table(schema = "licenciamento", name = "municipio")
public class Municipio extends GenericModel implements Serializable {

	@Id
	@Column(name="id_municipio")
	public Long id;
	
	public String nome;

	@ManyToOne
	@JoinColumn(name = "cod_estado")
	public Estado estado;

	@Column(name="apto_licenciamento")
	public Boolean aptoLicenciamento;

	@Column(name="cod_tse")
	public Long codigoTse;

	@ManyToMany
	@JoinTable(schema="licenciamento", name="rel_municipio_atividade_nao_apta", 
		joinColumns = @JoinColumn(name = "id_municipio"), 
		inverseJoinColumns = @JoinColumn(name="id_atividade"))
	public List<Atividade> atividadesNaoAptas;

	public Municipio() {}
	
	public Municipio(long id) {
		this.id = id;
	}

	@Column(name = "the_geom")
	public Geometry limite;

	public Geometry getLimite() {

		String jpql = "SELECT m.limite FROM " + Municipio.class.getSimpleName() + " m " + " WHERE id = :idMunicipio";

		Geometry geometry = Municipio.find(jpql)
				.setParameter("idMunicipio", this.id)
				.first();
		return geometry;
	}
	
	public static List<Municipio> findByEstado(String uf) {
		return Municipio.find("estado.codigo = :uf order by nome")
				.setParameter("uf", uf.toUpperCase()).fetch();
	}

	public br.ufla.lemaf.beans.pessoa.Municipio toMunicipioEU() {

		WebService ws = new WebService();

		Integer idEstado = findEstado(ws);

		if (idEstado != null) {

			return findMunicipio(ws, idEstado);
		}

		return null;
	}

	private Integer findEstado(WebService ws) {

		br.ufla.lemaf.beans.pessoa.Estado[] estados =
				ws.getJson(ENTRADA_UNICA_URL_CADASTRO_UNIFICADO + URL_FIND_ESTADOS.replace("{id}", ID_BRASIL_EU.toString()),
				br.ufla.lemaf.beans.pessoa.Estado[].class);

		for (br.ufla.lemaf.beans.pessoa.Estado estado : estados) {

			if (estado.sigla.equals(this.estado.codigo)) {

				return estado.id;
			}
		}

		return null;
	}

	private br.ufla.lemaf.beans.pessoa.Municipio findMunicipio(WebService ws, Integer idEstado) {

		br.ufla.lemaf.beans.pessoa.Municipio[] municipios =
				ws.getJson(ENTRADA_UNICA_URL_CADASTRO_UNIFICADO + URL_FIND_MUNICIPIOS.replace("{id}", idEstado.toString()),
				br.ufla.lemaf.beans.pessoa.Municipio[].class);

		for (br.ufla.lemaf.beans.pessoa.Municipio municipio : municipios) {

			if (municipio.codigoIbge.toString().equals(this.id.toString())) {

				return municipio;
			}
		}

		return null;
	}
}
