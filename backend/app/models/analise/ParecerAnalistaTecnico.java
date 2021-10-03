package models.analise;

import models.caracterizacao.Caracterizacao;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(schema = "analise", name = "parecer_analista_tecnico")
public class ParecerAnalistaTecnico extends GenericModel implements ParecerAnalista {

    public static final String SEQ = "analise.parecer_analista_tecnico_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @Column(name = "id")
    public Long id;

    @ManyToOne
    @JoinColumn(name = "id_analise_tecnica")
    public AnaliseTecnica analiseTecnica;

    @OneToOne
    @JoinColumn(name = "id_tipo_resultado_analise")
    public TipoResultadoAnalise tipoResultadoAnalise;

    @Column(name = "data_parecer")
    public Date dataParecer;

    @Column(name = "do_processo")
    public String doProcesso;

    @Column(name = "da_analise_tecnica")
    public String daAnaliseTecnica;

    @Column(name = "da_conclusao")
    public String daConclusao;

    @Column(name = "parecer")
    public String parecer;

    @Column(name = "validade_permitida")
    public Integer validadePermitida;

    @Column(name = "finalidade_atividade")
    public String finalidadeAtividade;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(schema = "analise", name = "rel_documento_parecer_analista_tecnico",
            joinColumns = @JoinColumn(name = "id_parecer_analista_tecnico"),
            inverseJoinColumns = @JoinColumn(name = "id_documento"))
    public List<Documento> documentos;

    @Column(name = "id_historico_tramitacao")
    public Long idHistoricoTramitacao;

    @OneToOne
    @JoinColumn(name = "id_documento_minuta", referencedColumnName = "id")
    public models.analise.Documento documentoMinuta;

    @OneToMany(mappedBy = "parecerAnalistaTecnico", orphanRemoval = true)
    public List<Condicionante> condicionantes;

    @OneToMany(mappedBy = "parecerAnalistaTecnico", orphanRemoval = true)
    public List<Restricao> restricoes;

    @Override
    public List<models.analise.Documento> getDocumentosNotificacao() {
        return this.documentos == null ? Collections.emptyList() :
                this.documentos.stream().filter(Documento::isAnaliseTecnica).collect(Collectors.toList());
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

    public static Integer findValidadePermitida(Caracterizacao caracterizacao) {

        List<ParecerAnalistaTecnico> listaParecerAnalistaTecnico = ParecerAnalistaTecnico.find(
                "SELECT a FROM ParecerAnalistaTecnico a " +
                        "JOIN a.analiseTecnica b " +
                        "JOIN b.analise c " +
                        "JOIN c.processo d " +
                        "WHERE d.numero = :idNumero ORDER BY a.id ASC")
                .setParameter("idNumero", caracterizacao.numero).fetch();

        ParecerAnalistaTecnico parecerAnalistaTecnico = new ParecerAnalistaTecnico();

        if (listaParecerAnalistaTecnico.size() > 0) {
            parecerAnalistaTecnico = listaParecerAnalistaTecnico.get(listaParecerAnalistaTecnico.size()-1 );
        }

        return parecerAnalistaTecnico.validadePermitida == null ? 0 : parecerAnalistaTecnico.validadePermitida;
    }

}