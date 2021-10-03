package models.caracterizacao;

import com.vividsolutions.jts.geom.Geometry;

import com.vividsolutions.jts.io.WKBWriter;
import org.hibernate.annotations.Type;

import play.db.jpa.GenericModel;
import utils.GeoCalc;

import javax.persistence.*;
import java.util.stream.Collectors;

@Entity
@Table(schema = "licenciamento", name = "geometria_complexo")
public class GeometriaComplexo extends GenericModel implements GeometriaCaracterizacao{

    private static final String SEQ = "licenciamento.geometria_complexo_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    public Long id;

    public Double area;

    @Column(name = "geometria", columnDefinition="Geometry")
    public Geometry geometria;

    @ManyToOne
    @JoinColumn(name = "id_caracterizacao", referencedColumnName = "id")
    public Caracterizacao caracterizacao;

    @PrePersist
    @PreUpdate
    private void preSave() {

        this.area = GeoCalc.area(this.geometria)/10000;
    }

    @Transient
    public Boolean isDentroEmpreendimento () {
        return this.caracterizacao.atividadesCaracterizacao.stream().anyMatch(ac -> ac.atividade.dentroEmpreendimento);
    }

    @Override
    public String getGeomWkt() {
        return WKBWriter.toHex(new WKBWriter().write(this.geometria));
    }
}
