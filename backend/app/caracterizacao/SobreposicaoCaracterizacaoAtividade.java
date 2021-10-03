package models.caracterizacao;

import beans.DadosSobreposicaoVO;
import com.vividsolutions.jts.geom.Geometry;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "licenciamento", name = "sobreposicao_caracterizacao_atividade")
public class SobreposicaoCaracterizacaoAtividade extends GenericModel implements SobreposicaoDistancia {

private static final String SEQ = "licenciamento.sobreposicao_caracterizacao_atividade_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "id_tipo_sobreposicao", referencedColumnName="id")
    public TipoSobreposicao tipoSobreposicao;

    @ManyToOne
    @JoinColumn(name = "id_atividade_caracterizacao", referencedColumnName="id")
    public AtividadeCaracterizacao atividadeCaracterizacao;

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

    public SobreposicaoCaracterizacaoAtividade(TipoSobreposicao tipoSobreposicao, AtividadeCaracterizacao atividadeCaracterizacao, DadosSobreposicaoVO dadosSobreposicao) {
        this.tipoSobreposicao = tipoSobreposicao;
        this.atividadeCaracterizacao = atividadeCaracterizacao;
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