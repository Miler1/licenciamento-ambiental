package models.analise;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(schema="analise", name="analise_geo")
public class AnaliseGeo extends AbstractAnalise {

    public static final String SEQ = "analise.analise_geo_id_seq";

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

    @ManyToOne
    @JoinColumn(name="id_analise")
    public Analise analise;

    @Transient
    public String parecer;

    @Transient
    public List<Documento> documentos;

    @Required
    @Column(name="data_vencimento_prazo")
    public Date dataVencimentoPrazo;

    @Required
    @Column(name="revisao_solicitada")
    public Boolean revisaoSolicitada;

    @Required
    @Column(name="notificacao_atendida")
    public Boolean notificacaoAtendida;

    public Boolean ativo;

    @OneToMany(mappedBy = "analiseGeo", fetch = FetchType.EAGER)
    public List<ParecerAnalistaGeo> pareceresAnalistaGeo;

    public static AnaliseGeo findByProcesso(Processo processo) {
        return AnaliseGeo.find("analise.processo.id = :idProcesso")
                .setParameter("idProcesso", processo.id)
                .first();
    }

    @Override
    public List<? extends ParecerAnalista> getPareceresAnalistas() {
        return this.pareceresAnalistaGeo;
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
