package models.caracterizacao;

import beans.DadosSobreposicaoVO;
import com.vividsolutions.jts.geom.Geometry;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "licenciamento", name = "sobreposicao_caracterizacao_empreendimento")
public class SobreposicaoCaracterizacaoEmpreendimento extends GenericModel implements SobreposicaoDistancia {

    private static final String SEQ = "licenciamento.sobreposicao_caracterizacao_empreendimento_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "id_tipo_sobreposicao", referencedColumnName="id")
    public TipoSobreposicao tipoSobreposicao;

    @ManyToOne
    @JoinColumn(name = "id_caracterizacao", referencedColumnName="id")
    public Caracterizacao caracterizacao;

    @Column(name = "geometria", columnDefinition="Geometry")
    public Geometry geometria;

    @Column(name = "distancia")
    public Double distancia;

    @Column(name = "nome_area_sobreposicao")
    public String nomeAreaSobreposicao;

    @Column(name = "data_area_sobreposicao")
    public String dataAreaSobreposicao;

    @Column(name = "cpf_cnpj_area_sobreposicao")
    public String cpfCnpjAreaSobreposicao;

    public SobreposicaoCaracterizacaoEmpreendimento(TipoSobreposicao tipoSobreposicao, Caracterizacao caracterizacao, DadosSobreposicaoVO dadosSobreposicao) {
        this.tipoSobreposicao = tipoSobreposicao;
        this.caracterizacao = caracterizacao;
        this.geometria = dadosSobreposicao.geometria;
        this.nomeAreaSobreposicao = dadosSobreposicao.nomeAreaSobreposicao;
        this.dataAreaSobreposicao = dadosSobreposicao.dataAreaSobreposicao;
        this.cpfCnpjAreaSobreposicao = dadosSobreposicao.cpfCnpjAreaSobreposicao;
    }

    @Override
    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    @Override
    public Double getDistancia() {
        return this.distancia;
    }
}