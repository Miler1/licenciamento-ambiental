package models.caracterizacao;

import com.vividsolutions.jts.geom.Geometry;

import com.vividsolutions.jts.io.WKBWriter;
import org.hibernate.annotations.Type;

import play.db.jpa.GenericModel;
import utils.GeoCalc;

import javax.persistence.*;

@Entity
@Table(schema = "licenciamento", name = "geometria_atividade")
public class GeometriaAtividade extends GenericModel implements GeometriaCaracterizacao {

	private static final String SEQ = "licenciamento.geometria_atividade_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	public Double area;

	@Column(name = "the_geom", columnDefinition="Geometry")
	public Geometry geometria;

	@ManyToOne
	@JoinColumn(name = "id_atividade_caracterizacao", referencedColumnName = "id")
	public AtividadeCaracterizacao atividadeCaracterizacao;
	
	@PrePersist
	@PreUpdate
	private void preSave() {
		
		this.area = GeoCalc.area(this.geometria)/10000;
	}
	
	/**
	 * Gera uma cópia da geometria da atividade, duplicando todas as informações
	 * declaradas pelo cadastrante. Utilizado quando duplicar a caracterização.
	 * Ver também {@link Caracterizacao#gerarCopia()}
	 */
	public GeometriaAtividade gerarCopia() {
				
		GeometriaAtividade copia = new GeometriaAtividade();
		copia.geometria = this.geometria;
		copia.area = this.area;
		
		return copia;
	}

	@Override
	public String getGeomWkt() {
		return WKBWriter.toHex(new WKBWriter().write(this.geometria));
	}
}
