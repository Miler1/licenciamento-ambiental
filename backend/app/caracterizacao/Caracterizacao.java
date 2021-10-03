package models.caracterizacao;

import beans.*;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.prep.PreparedGeometry;
import com.vividsolutions.jts.geom.prep.PreparedGeometryFactory;
import exceptions.ValidacaoException;
import models.Empreendimento;
import models.Esfera;
import models.Pessoa;
import models.TipoLocalizacao;
import models.analise.Analise;
import models.analise.Notificacao;
import models.analise.Processo;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import security.services.Auth;
import utils.*;
import utils.validacao.Validacao;

import javax.persistence.*;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(schema = "licenciamento", name = "caracterizacao")
public class Caracterizacao extends GenericModel implements Identificavel, GuardaSobreposicao {

    private static final String SEQ = "licenciamento.caracterizacao_id_seq";
    private static final Integer PESSOA_FISICA = 0;
    private static final Integer PESSOA_JURIDICA = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    public Long id;

    @Column(name = "numero")
    public String numero;

    @Column(name = "data_cadastro")
    public Date dataCadastro;

    @Column(name = "data_finalizacao")
    public Date dataFinalizacao;

    @Column(name = "data_retificacao")
    public Date dataRetificacao;

    @ManyToOne
    @JoinColumn(name = "id_processo")
    public models.caracterizacao.Processo processo;

    @ManyToOne
    @JoinColumn(name = "id_empreendimento")
    public Empreendimento empreendimento;

    @ManyToOne
    @JoinColumn(name = "id_status")
    public StatusCaracterizacao status;

    @ManyToOne
    @JoinColumn(name = "id_tipo_licenca")
    public TipoLicenca tipoLicenca;

    @ManyToOne
    @JoinColumn(name = "id_tipo")
    public TipoCaracterizacao tipo;

    @OneToOne(mappedBy = "caracterizacao")
    public DispensaLicenciamento dispensa;

    @OneToMany(mappedBy = "caracterizacao", cascade = CascadeType.ALL)
    public List<AtividadeCaracterizacao> atividadesCaracterizacao;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(schema = "licenciamento", name = "rel_caracterizacao_resposta",
            joinColumns = @JoinColumn(name = "id_caracterizacao"),
            inverseJoinColumns = @JoinColumn(name = "id_resposta"))
    public List<Resposta> respostas;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caracterizacao")
    public Questionario3 questionario3;

    //    @Required
//    @ManyToOne
//    @JoinColumn(name = "id_cadastrante", referencedColumnName = "id")
    @Transient
    public Pessoa cadastrante;

    @Required
    @Column(name = "cpf_cnpj_cadastrante")
    public String cpfCnpjCadastrante;

    @ManyToMany
    @JoinTable(schema = "licenciamento", name = "rel_tipo_licenca_caracterizacao_andamento",
            joinColumns = @JoinColumn(name = "id_caracterizacao"),
            inverseJoinColumns = @JoinColumn(name = "id_tipo_licenca"))
    public List<TipoLicenca> tiposLicencaEmAndamento;

    @OneToMany(mappedBy = "caracterizacao", cascade = CascadeType.ALL)
    public List<SolicitacaoDocumentoCaracterizacao> solicitacoesDocumentoCaracterizacao;

    @OneToMany(mappedBy = "caracterizacao", cascade = CascadeType.ALL)
    public List<SolicitacaoGrupoDocumento> solicitacoesGruposDocumentos;

    @Column(name = "declaracao_veracidade_informacoes")
    public Boolean declaracaoVeracidadeInformacoes;

    @Column(name = "analise")
    public boolean emAnalise;

    @Column(name = "numero_processo_automatico")
    public String numeroProcessoAutomatico;

    @OneToMany(mappedBy = "caracterizacao")
    public List<Licenca> licencas;

    @Column
    public boolean renovacao;

    @Column(name = "descricao_atividade")
    public String descricaoAtividade;

    @Column(name = "vigencia_solicitada")
    public Integer vigenciaSolicitada;

    @OneToOne(mappedBy = "caracterizacao")
    public Dae dae;

    @OneToOne(mappedBy = "caracterizacao")
    public DaeLicenciamento daeLicenciamento;

    @OneToMany(mappedBy = "caracterizacao", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<GeometriaComplexo> geometriasComplexo;

    @OneToMany(mappedBy = "caracterizacao", cascade = CascadeType.ALL)
    public List<SobreposicaoComplexo> sobreposicaoComplexos;

    @OneToMany(mappedBy = "caracterizacao", cascade = CascadeType.ALL)
    public List<SobreposicaoCaracterizacaoEmpreendimento> sobreposicoesEmpreendimento;

    @Column(name = "origem_sobreposicao")
    @Enumerated(EnumType.STRING)
    public OrigemSobreposicao origemSobreposicao;

    @Column(name = "id_origem")
    public Long idCaracterizacaoOrigem;

    @Column(name = "bloqueada")
    public Boolean bloqueada = false;

    @Column(name = "ativo")
    public Boolean ativo = true;

    @Column(name = "valor_taxa_administrativa")
    public Double valorTaxaAdministrativa;

    @Column
    public Boolean retificacao = false;

    @Column
    public Boolean complexo = false;

    @Column(name = "valor_taxa_licenciamento")
    public Double valorTaxaLicenciamento;

    @Column(name = "ocultar_listagem", insertable = false)
    public Boolean ocultarListagem;

    @Transient
    public String linkTaxasLicenciamento;

    @Transient
    public Tipologia tipologia;

    @Transient
    public boolean renovacaoLicenca;

    @Transient
    public Notificacao notificacao;

    @Transient
    public Empreendimento empreendimentoEU;

    @Transient
    public models.analise.Documento documentoMinuta;

    @Transient
    models.analise.ParecerAnalistaTecnico parecerAnalistaTecnico;

    @Transient
    public String numeroLicenca;

    @Transient
    public String nomePessoaEmpreendimento;

    @Transient
    public String cpfCnpjPessoaEmpreendimento;

    @Override
    public Long getId() {
        return this.id;
    }

    private Boolean isSimplificado() {
        return tipoLicenca != null && tipoLicenca.id.equals(TipoLicenca.DISPENSA_LICENCA_AMBIENTAL);
    }

    private Atividade getPrimeiraAtividade() {
        return this.atividadesCaracterizacao.get(0).atividade;
    }

    private Boolean isDentroEmpreendimento() {
        return this.getPrimeiraAtividade().dentroEmpreendimento;
    }

    private void inicializaEmpreendimento() {

        Empreendimento emp = Empreendimento.findById(this.empreendimento.id);
        this.empreendimento = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(emp.cpfCnpj);
        this.empreendimento.validarSeUsuarioCadastrante();
        this.cpfCnpjCadastrante = Auth.getUsuarioSessao().findPessoa().getCpfCnpj();
        this.empreendimento.cadastrante = Auth.getUsuarioSessao().findPessoa();
        //this.empreendimentoEU = new Empreendimento(this.empreendimento.empreendimentoEU.pessoa,this.empreendimento.id, this.empreendimento.getGeometry());
    }

    private void inicializaCadastrante() {
        this.cadastrante = Auth.getUsuarioSessao().findPessoa();
    }

    public Caracterizacao salvar() throws ScriptException {
        this.inicializaEmpreendimento();
        this.inicializaCadastrante();
        return isSimplificado() ? saveDla() : saveSimplificado();
    }

    private void validaAtividadeCaracterizacao(AtividadeCaracterizacao atividadeCaracterizacao) {
        atividadeCaracterizacao.validarTipoLicenca(this.tipoLicenca);
        validarLocalizacao(atividadeCaracterizacao, this);
        atividadeCaracterizacao.atividade = Atividade.findById(atividadeCaracterizacao.atividade.id);
        inicializaGeometriasAtividade(atividadeCaracterizacao);
        atividadeCaracterizacao.caracterizacao = this;
    }

    private void inicializaGeometriasComplexo() {
        if (this.geometriasComplexo != null) {
            this.geometriasComplexo.forEach(this::inicializaGeometriaComplexo);
        }
    }

    private void inicializaGeometriaComplexo(GeometriaComplexo geo) {
        geo.geometria.setSRID(Configuracoes.SRID);
        geo.caracterizacao = this;
    }

    private void inicializaGeometriasAtividade(AtividadeCaracterizacao atividadeCaracterizacao) {
        if (atividadeCaracterizacao.geometriasAtividade != null) {
            atividadeCaracterizacao.geometriasAtividade.forEach(geo ->
                    inicializaGeometriaAtividade(atividadeCaracterizacao, geo));
        }
    }


    private void inicializaGeometriaAtividade(AtividadeCaracterizacao atividadeCaracterizacao, GeometriaAtividade geo) {
        geo.geometria.setSRID(Configuracoes.SRID);
        geo.atividadeCaracterizacao = atividadeCaracterizacao;
    }

    private void inicializaGeometriasAtividade() {
        this.atividadesCaracterizacao.forEach(ac -> ac.geometriasAtividade.forEach(
                ga -> this.inicializaGeometriaAtividade(ac, ga))
        );
    }

    private void validaAtividadesCaracterizacao() {
        this.atividadesCaracterizacao.forEach(this::validaAtividadeCaracterizacao);
    }

    private void inicializaDla() {
        Validacao.validar(this);
        validaAtividadesCaracterizacao();
        inicializaGeometriasComplexo();
        validateAndSetRespostas();
        this.dataCadastro = new Date();
        this.status = StatusCaracterizacao.findById(StatusCaracterizacao.DEFERIDO);
        this.tipoLicenca = TipoLicenca.findById(TipoLicenca.DISPENSA_LICENCA_AMBIENTAL);
        this.tipo = TipoCaracterizacao.findById(TipoCaracterizacao.DISPENSA);
        this.declaracaoVeracidadeInformacoes = true;
        this.valorTaxaLicenciamento = 0.00;
        this.valorTaxaAdministrativa = TaxaAdministrativaDae.findValorAtual(this);
    }

    private void inicializaProcesso() {
        if (this.isRenovacao() || this.isRetificacao()) {
            this.processo = ((Caracterizacao) findById(this.idCaracterizacaoOrigem)).processo;
        } else {
            this.processo = new models.caracterizacao.Processo();
            this.processo.gerar();
        }
    }

    private void salvarDispensa(DispensaLicenciamento dispensa) {
        this.dispensa = dispensa;
        if (dispensa != null) {
            this.dispensa.caracterizacao = this;
            this.dispensa.caracterizacao.empreendimento = this.empreendimento;
        }
        this.valorTaxaLicenciamento = 0.00;
        this.dispensa.save();
    }

    private Caracterizacao saveDla() throws ScriptException {

        this.inicializaDla();

        DispensaLicenciamento dispensa = this.dispensa;
        this.dispensa = null;

        super.save();

        JPA.em().refresh(this);

        this.gerarNumeroProtocolo();

        this.salvarDispensa(dispensa);

        this.inicializaProcesso();

        super.save();

        this.finalizar();

        return this;
    }

    public Boolean isComplexo() {
        return this.complexo;
    }

    /**
     * Valida se todas as respostas se enquadram na solicitação
     */
    private void validateAndSetRespostas() {
        if (this.respostas != null) {
            this.respostas = this.respostas.stream().
                    <Resposta>map(resposta -> Resposta.findById(resposta.id))
                    .map(this::validaResposta).collect(Collectors.toList());
        }
    }

    private Resposta validaResposta(Resposta resposta) {
        validaRespostaPermiteLicenciamento(resposta);
        validaPerguntaNaoRespondida(resposta);
        return resposta;
    }

    private void validaRespostaPermiteLicenciamento(Resposta resposta) {
        if (!resposta.permiteLicenciamento) {
            throw new ValidacaoException(Mensagem.CARACTERIZACAO_RESPOSTAS_NAO_PERMITE_LICENCIAMENTO);
        }
    }

    private void validaPerguntaNaoRespondida(Resposta resposta) {
        this.atividadesCaracterizacao.stream()
                .filter(atividadeCaracterizacao -> !atividadeCaracterizacao.containsPergunta(resposta.pergunta))
                .forEach(atividadeCaracterizacao -> {
                    throw new ValidacaoException(Mensagem.CARACTERIZACAO_PERGUNTAS_NAO_RESPONDIDAS);
                });
    }

    private void inicializaSimplificado() {

        Validacao.validar(this);

        if (this.retificacao) {
            Caracterizacao c = findById(this.id);
            this.dataCadastro = c.dataCadastro;
            this.dataFinalizacao = c.dataFinalizacao;
            this.numero = c.numero;
            this.tipoLicenca = c.tipoLicenca;
            this.status = StatusCaracterizacao.findById(StatusCaracterizacao.NOTIFICADO_EM_ANDAMENTO);
            this.solicitacoesDocumentoCaracterizacao = c.solicitacoesDocumentoCaracterizacao.stream()
                    .map(so -> new SolicitacaoDocumentoCaracterizacao(this, so))
                    .collect(Collectors.toList());
            this.solicitacoesGruposDocumentos = c.solicitacoesGruposDocumentos.stream()
                    .map(so -> new SolicitacaoGrupoDocumento(this, so))
                    .collect(Collectors.toList());
            this.tipo = c.tipo;
            this.desativaCaracterizacaoDeOrigem();
            this.dataRetificacao = new Date();
            this.emAnalise = false;
            this.dae = c.dae;
        } else {
            this.dataCadastro = new Date();
            this.numero = "";
            this.status = StatusCaracterizacao.findById(StatusCaracterizacao.EM_ANDAMENTO);
            this.valorTaxaLicenciamento = 0.00;
        }

        this.id = null;
        this.declaracaoVeracidadeInformacoes = false;
        this.dispensa = null;

        this.inicializaProcesso();

        if (this.isRenovacao()) {
            this.tiposLicencaEmAndamento = Collections.singletonList(this.tipoLicenca);
            this.atividadesCaracterizacao.forEach(AtividadeCaracterizacao::zeraParametros);
            this.verificarTiposPermitidosRenovacao();
            this.bloqueiaCaracterizacaoDeOrigem();
        }
    }

    private void etapaAtividade() {

        this.atividadesCaracterizacao.forEach(ac -> {
            ac.validarTipoLicenca(this.tipoLicenca);
            this.validarTiposLicencaEmAndamento(ac);
            ac.atividade = Atividade.findById(ac.atividade.id);
            ac.atividadesCnae = AtividadeCnae.findAllWithIds(ListUtil.getIds(ac.atividadesCnae));
            ac.caracterizacao = this;
            ac.porteEmpreendimento = PorteAtividade.calcularPorteEmpreendimento(ac);
            ac.porteEmpreendimentoParaCalculoDoPorte = ac.getPorteParaCalculoDeTaxa();
            ac.atividadeCaracterizacaoParametros.stream()
                    .filter(acp -> acp.atividadeCaracterizacao == null)
                    .forEach(acp -> acp.atividadeCaracterizacao = ac);
        });

        this.setTaxasCaracterizacao();

    }

    public Boolean isCaracterizacaoIsenta() {

        TaxaAdministrativaDae taxaAdministrativaDae = TaxaAdministrativaDae.getTaxaAtual(this);

        return taxaAdministrativaDae.isento ||
                (getTipoLicenca().sigla.equals("DDLA") && !taxaAdministrativaDae.atividadeDispensavel) ||
                (!getTipoLicenca().sigla.equals("DDLA") && !taxaAdministrativaDae.atividadeLicenciavel);

    }

    private void etapaLocalizacao() {

        this.empreendimento.validarSeZonaRuralSemImovel();

        this.atividadesLocalizacaoValidacao();

        this.atividadesCaracterizacao.forEach(ac -> validarLocalizacao(ac, this));

        this.origemSobreposicao = OrigemSobreposicao.SEM_SOBREPOSICAO;

        if (this.isDentroEmpreendimento()) {

            //Bloco que salva as sobreposições do empreendimento
            this.inicializaGeometriasComplexo();
            this.inicializaGeometriasAtividade();
            setSobreposicao(this, this.empreendimento.getGeometriaEU(), OrigemSobreposicao.EMPREENDIMENTO);

        } else if (this.isComplexo()) {

            //Bloco que salva as sobreposições do complexo
            this.geometriasComplexo.forEach(gc -> {
                inicializaGeometriaComplexo(gc);
                GeoCalc.getGeometries(gc.geometria).forEach(geom ->
                        setSobreposicao(this, geom, OrigemSobreposicao.COMPLEXO));
            });

        } else {

            //Bloco que salva as sobreposições das atividades
            this.atividadesCaracterizacao.forEach(ac -> {
                ac.geometriasAtividade.forEach(ga -> {
                    inicializaGeometriaAtividade(ac, ga);
                    GeoCalc.getGeometries(ga.geometria).forEach(geom ->
                            setSobreposicao(ac, geom, OrigemSobreposicao.ATIVIDADE));
                });
            });

        }

    }

    private void etapaCondicoes() {
        this.validateAndSetRespostas();
        this.questionario3.validar(this);
    }

    private void finalizarRascunho() {
        super.save();
        JPA.em().refresh(this);
        this.gerarNumeroProtocolo();
        super.save();
    }

    public Caracterizacao saveSimplificado() {

        TimeLogger timeLogger = new TimeLogger();

        this.inicializaSimplificado();

        this.etapaAtividade();

        this.etapaLocalizacao();

        this.etapaDocumentacao();

        this.etapaCondicoes();

        this.etapaEnquadramento();

        this.finalizarRascunho();

        timeLogger.print("Tempo gasto para salvar caracterização em andamento");

        return this;
    }

    private void verificarTiposPermitidosRenovacao() {
        if (this.isRenovacao()) {

            List<Caracterizacao> caracterizacoes = find("idCaracterizacaoOrigem = :origem AND tipoLicenca.id = :tipo")
                    .setParameter("origem", this.idCaracterizacaoOrigem)
                    .setParameter("tipo", TipoLicenca.ATUALIZACAO_DE_LICENCA_PREVIA).fetch();

            if (this.tipoLicenca.id == TipoLicenca.ATUALIZACAO_DE_LICENCA_PREVIA && !caracterizacoes.isEmpty()) {
                throw new ValidacaoException(Mensagem.ALP_NAO_PODE_SER_ATUALIZADA);
            }

            Caracterizacao origem = findById(this.idCaracterizacaoOrigem);
            if (origem.tipoLicenca.tiposLicencasPermitidas.stream().noneMatch(tp -> tp.id.equals(this.tipoLicenca.id))) {
                throw new ValidacaoException(Mensagem.TIPO_DE_RENOVACAO_DE_LICENCA_NAO_PERMITIDO);
            }
        }
    }

    private Caracterizacao bloqueiaCaracterizacaoDeOrigem() {
        if (this.isRenovacao() && this.idCaracterizacaoOrigem != null) {
            Caracterizacao origem = findById(this.idCaracterizacaoOrigem);
            origem.bloqueada = true;
            return origem.save();
        }
        throw new ValidacaoException(Mensagem.ATIVIDADE_DE_ORIGEM_NAO_PODE_SER_BLOQUEDA);
    }

    private Caracterizacao desativaCaracterizacaoDeOrigem() {
        if (this.retificacao && this.idCaracterizacaoOrigem != null) {
            Caracterizacao origem = findById(this.idCaracterizacaoOrigem);
            origem.ativo = false;
            origem.status = StatusCaracterizacao.findById(StatusCaracterizacao.NOTIFICADO_HISTORICO);
            origem.dataRetificacao = new Date();
            return origem.save();
        }
        throw new ValidacaoException(Mensagem.ATIVIDADE_DE_ORIGEM_NAO_PODE_SER_BLOQUEDA);
    }

    private void atividadesLocalizacaoValidacao() {
        Atividade at = this.getPrimeiraAtividade();
        if (this.atividadesCaracterizacao.stream().anyMatch(ac -> !ac.atividadeDentroEmpreendimento(at))) {
            throw new ValidacaoException(Mensagem.CARACTERIZACAO_ATIVIDADES_DENTRO_E_FORA_EMPREENDIMENTO);
        }
    }

    private <T extends SobreposicaoDistancia> void calculaDistanciaAreaRestricao(
            Geometry geo, T entidade, Long idTipo, Long idTipoVerificacao, String nomeCamada) {

        if (idTipo.equals(idTipoVerificacao)) {
            entidade.setDistancia(GeoCalc.getDistanceGeoserver(nomeCamada, geo));
        }
    }

    private <T extends SobreposicaoDistancia> void setDistanciaAreaRestricao(
            Geometry geo, T entidade, Long tipoSobreposicaoId) {

        calculaDistanciaAreaRestricao(geo, entidade, tipoSobreposicaoId, TipoSobreposicao.TERRA_INDIGENA_ZA,
                Configuracoes.GEOSERVER_LAYER_TERRA_INDIGENA);

        calculaDistanciaAreaRestricao(geo, entidade, tipoSobreposicaoId, TipoSobreposicao.UC_FEDERAL_ZA,
                Configuracoes.GEOSERVER_LAYER_UC_FEDERAL);

        calculaDistanciaAreaRestricao(geo, entidade, tipoSobreposicaoId, TipoSobreposicao.UC_ESTADUAL_ZA_PI_FORA,
                Configuracoes.GEOSERVER_LAYER_UC_ESTADUAL);
    }

    private <T extends GuardaSobreposicao> void setSobreposicao(
            T entidade, Geometry geo, OrigemSobreposicao origemSobreposicao) {

        if (entidade.getListaSobreposicao() == null) {
            entidade.setListaSobreposicao(new ArrayList<>());
        }

        buscarSobreposicoesSimplificado(geo).forEach(sobreposicao ->
                GeoCalc.getInformacoesSobreposicoes(sobreposicao, geo).forEach(dadoSobreposicao -> {
                    dadoSobreposicao.geometria.setSRID(Configuracoes.SRID);
                    SobreposicaoDistancia sobreposicaoEntidade = entidade.getObjetoSobreposicao(
                            dadoSobreposicao, entidade, sobreposicao.tipoSobreposicao
                    );
                    setDistanciaAreaRestricao(geo, sobreposicaoEntidade, sobreposicao.tipoSobreposicao.id);
                    entidade.getListaSobreposicao().add(sobreposicaoEntidade);
                })
        );

        if (!entidade.getListaSobreposicao().isEmpty()) {
            this.origemSobreposicao = origemSobreposicao;
        }

    }

    public Caracterizacao update(Caracterizacao caracterizacaoAtualizada) {

        empreendimento.validarSeZonaRuralSemImovel();

        excluirAtividadesCaracterizacao();

        this.atividadesCaracterizacao = caracterizacaoAtualizada.atividadesCaracterizacao;

        etapaEnquadramento();

        updateSolicitacoesDocumento();

        for (AtividadeCaracterizacao atividadeCaracterizacao : this.atividadesCaracterizacao) {

            this.tipoLicenca = caracterizacaoAtualizada.tipoLicenca;
            atividadeCaracterizacao.validarTipoLicenca(this.tipoLicenca);

            validarLocalizacao(atividadeCaracterizacao, this);

            atividadeCaracterizacao.atividade = Atividade.findById(atividadeCaracterizacao.atividade.id);

            atividadeCaracterizacao.atividadesCnae =
                    AtividadeCnae.findAllWithIds(ListUtil.getIds(atividadeCaracterizacao.atividadesCnae));

            atividadeCaracterizacao.geometriasAtividade.forEach(geo -> inicializaGeometriaAtividade(atividadeCaracterizacao, geo));

            atividadeCaracterizacao.caracterizacao = this;

            this.dispensa = null;

            atividadeCaracterizacao.porteEmpreendimento = PorteAtividade.calcularPorteEmpreendimento(atividadeCaracterizacao);

        }

        this.geometriasComplexo.forEach(this::inicializaGeometriaComplexo);

        this.respostas = caracterizacaoAtualizada.respostas;

        this.questionario3.validar(this);

        super.save();

        return this;
    }

    private void etapaDocumentacao() {

        if (this.isRetificacao()) {
            return;
        }

        List<TipoDocumentoTipoLicenca> tiposDocumentosSolicitados = new ArrayList<>();

        if (this.isRenovacao()) {
            this.solicitacoesDocumentoCaracterizacao.forEach(sdc -> sdc.zerarDocumentos(this));
            this.solicitacoesGruposDocumentos.forEach(sdc -> sdc.zerarDocumentos(this));
        }

        if (this.empreendimento.empreendimentoEU.pessoa.tipo.codigo == PESSOA_FISICA) {
            tiposDocumentosSolicitados = TipoDocumentoTipoLicenca
                    .find("tipoLicenca IN :tiposLicenca And tipoPessoa = :tipoPessoa")
                    .setParameter("tiposLicenca", this.tiposLicencaEmAndamento)
                    .setParameter("tipoPessoa", "PF")
                    .fetch();
        } else {
            tiposDocumentosSolicitados = TipoDocumentoTipoLicenca
                    .find("tipoLicenca IN :tiposLicenca And tipoPessoa = :tipoPessoa")
                    .setParameter("tiposLicenca", this.tiposLicencaEmAndamento)
                    .setParameter("tipoPessoa", "PJ")
                    .fetch();
        }

        this.solicitacoesDocumentoCaracterizacao = tiposDocumentosSolicitados.stream().map(tipoDocumentoSolicitado ->
                new SolicitacaoDocumentoCaracterizacao(tipoDocumentoSolicitado, this))
                .collect(Collectors.toList());

        Atividade ativ = this.atividadesCaracterizacao.get(0).atividade;
        TipoLicenca tp = this.tiposLicencaEmAndamento != null && !this.tiposLicencaEmAndamento.isEmpty() ?
                this.tiposLicencaEmAndamento.get(0) : this.tipoLicenca;

        if (ativ.grupoDocumento != null && ativ.grupoDocumento.tipoLicencaGrupoDoctos != null) {
            this.solicitacoesGruposDocumentos = ativ.grupoDocumento.tipoLicencaGrupoDoctos.stream()
                    .filter(tlg -> tlg.tipoLicenca.id.equals(tp.id))
                    .map(td -> new SolicitacaoGrupoDocumento(this, ativ.grupoDocumento, td.tipoDocumento, td.obrigatorio))
                    .collect(Collectors.toList());
        }


    }

    private void updateSolicitacoesDocumento() {

        List<TipoDocumentoTipoLicenca> tiposDocumentosSolicitados = TipoDocumentoTipoLicenca
                .find("tipoLicenca IN :tiposLicenca")
                .setParameter("tiposLicenca", this.tiposLicencaEmAndamento)
                .fetch();

        this.solicitacoesDocumentoCaracterizacao = tiposDocumentosSolicitados.stream().map(tipoDocumentoSolicitado ->
                new SolicitacaoDocumentoCaracterizacao(tipoDocumentoSolicitado, this))
                .collect(Collectors.toList());
    }

    /*
     * Valida os tipos de licenciamento válidos para o licenciamento simplificado
     */
    private void validarTiposLicencaEmAndamento(AtividadeCaracterizacao atividadeCaracterizacao) {

        if (this.tiposLicencaEmAndamento.isEmpty() && this.tipoLicenca != null) {
            this.tiposLicencaEmAndamento.add(this.tipoLicenca);
        }

        if (this.tiposLicencaEmAndamento.isEmpty()) {
            throw new ValidacaoException(Mensagem.CARACTERIZACAO_TIPO_LICENCA_OBRIGATORIO);
        }

        int qtdeLicencas = this.tiposLicencaEmAndamento.size();

        List<Long> idsLicencas = new ArrayList<>();

        for (TipoLicenca tipoLicenca : this.tiposLicencaEmAndamento) {

            if (!atividadeCaracterizacao.atividade.containsTipoLicenca(tipoLicenca.id)) {

                throw new ValidacaoException(Mensagem.CARACTERIZACAO_SIMPLIFICADO_TIPO_LICENCA_INVALIDO);
            }

            if (tipoLicenca.id == TipoLicenca.LICENCA_AMBIENTAL_UNICA && qtdeLicencas > 1) {

                throw new ValidacaoException(Mensagem.CARACTERIZACAO_SIMPLIFICADO_TIPO_LICENCA_INVALIDO);
            }

            if (idsLicencas.contains(tipoLicenca.id)) {

                throw new ValidacaoException(Mensagem.CARACTERIZACAO_TIPO_LICENCA_REPETIDO);
            }

            idsLicencas.add(tipoLicenca.id);
        }
    }

    /**
     * Valida a localizacao e coordenadas do imovel.
     * Caso o empreendimento esteja na Zona Urbana, valida se suas coordenadas estão inseridas no
     * município do empreendimento. Caso esteja na Zona Rural, verifica se a
     * geometria desenhada na caracterizacao está inserida nos limites do imóvel.
     * Também valida se o empreendimento está localizado dentro do estado
     */
    private void validarLocalizacao(AtividadeCaracterizacao atividadeCaracterizacao, Caracterizacao caracterizacao) {
        // Verificar se é necessário continuar com essa verificação
        //Municipio municipio = Municipio.find("id_municipio", empreendimento.municipio.id).first();

        //if (!municipio.estado.codigo.equals(Configuracoes.ESTADO))
        //throw new ValidacaoException(Mensagem.EMPREENDIMENTO_FORA_DO_ESTADO);

        //Preenche atividadeCaracterizacao da geometria da atividade
        atividadeCaracterizacao.geometriasAtividade.forEach(geometriaAtividade ->
                geometriaAtividade.atividadeCaracterizacao = atividadeCaracterizacao);

        validarGeometriasAtividade(atividadeCaracterizacao.geometriasAtividade, this.empreendimento);

        if (caracterizacao.geometriasComplexo != null) {
            //Preenche atividadeCaracterizacao da geometria da atividade
            caracterizacao.geometriasComplexo.forEach(gc -> gc.caracterizacao = caracterizacao);
            validarGeometriasComplexo(caracterizacao.geometriasComplexo, this.empreendimento);
        }
    }

    private void validarGeometriasAtividade(List<GeometriaAtividade> geometrias, Empreendimento empreendimento) {

        Empreendimento emp = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(empreendimento.cpfCnpj);

        for (GeometriaAtividade geo : geometrias) {

            if (emp.empreendimentoEU.enderecos.get(0).zonaLocalizacao.codigo == TipoLocalizacao.ZONA_URBANA.id) {

                this.empreendimento.imovel = null;
                Geometry limite = null;
                Mensagem mensagem = Mensagem.CARACTERIZACAO_COORDENADAS_FORA_DO_ESTADO;

                if (geo.atividadeCaracterizacao.atividade.dentroEmpreendimento) {
                    limite = empreendimento.getGeometriaEU();
                    mensagem = Mensagem.CARACTERIZACAO_COORDENADAS_FORA_DO_EMPREENDIMENTO;
                } else {
                    //Esse limite estava bugando do mesmo jeito que o findbyid do município, verificar se é necessário
                    //limite = empreendimento.municipio.estado.getLimite();
                }

                //if (limite == null || !geometriaAtividadeEstaContidaNoLimite(geo, limite)) {
                //throw new ValidacaoException(mensagem);
                //}

            } else {

                this.empreendimento.imovel = null;
                Geometry limite = null;
                Mensagem mensagem = Mensagem.CARACTERIZACAO_COORDENADAS_FORA_DO_ESTADO;

                if (geo.atividadeCaracterizacao.atividade.dentroEmpreendimento) {
                    limite = empreendimento.getGeometriaEU();
                    mensagem = Mensagem.CARACTERIZACAO_COORDENADAS_FORA_DO_EMPREENDIMENTO;
                } else {
                    //Esse limite estava bugando do mesmo jeito que o findbyid do município, verificar se é necessário
                    //limite = empreendimento.municipio.estado.getLimite();
                }

                //if (limite == null || !geometriaAtividadeEstaContidaNoLimite(geo, limite)) {
                //throw new ValidacaoException(mensagem);
                //}

                /****************** REGRA QDO O EMPREENDIMENTO DO IMOVEL É RURAL ENTÃO (CAR) ***********************/
//                Validacao.validateRequired(empreendimento.imovel);
//                Validacao.validar(empreendimento.imovel);
//
//                Geometry limite = null;
//                Mensagem mensagem = Mensagem.CARACTERIZACAO_COORDENADAS_FORA_DO_ESTADO;
//
//                if (geo.atividadeCaracterizacao.atividade.dentroEmpreendimento) {
//                    limite = empreendimento.imovel.limite;
//                    mensagem = Mensagem.CARACTERIZACAO_COORDENADAS_FORA_DO_EMPREENDIMENTO;
//                } else {
//                    Estado estado = Estado.findById(empreendimento.getEstado().id);
//                    limite = estado.getLimite();
//                }
//                if (limite == null || !geometriaAtividadeEstaContidaNoLimite(geo, limite)) {
//                    throw new ValidacaoException(mensagem);
//                }
                /******************************************************************************/
            }
        }

    }

    private boolean geometriaAtividadeEstaContidaNoLimite(GeometriaAtividade geometriaAtividade, Geometry limite) {

        PreparedGeometry limitePreparado = PreparedGeometryFactory.prepare(limite);

        if (limitePreparado.contains(geometriaAtividade.geometria)) {
            return true;
        } else {

            Geometry intersecao = limite.buffer(0).intersection(geometriaAtividade.geometria.buffer(0));

            if (intersecao != null && !intersecao.isEmpty()) {

                geometriaAtividade.geometria = intersecao;
                return true;

            }
        }

        return false;
    }

    private void validarGeometriasComplexo(List<GeometriaComplexo> geometrias, Empreendimento empreendimento) {

        for (GeometriaComplexo geo : geometrias) {

            if (empreendimento.localizacao.equals(TipoLocalizacao.ZONA_URBANA)) {

                this.empreendimento.imovel = null;
                Geometry limite = null;
                Mensagem mensagem = null;

                if (this.empreendimento.jurisdicao == Esfera.MUNICIPAL) {

                    limite = empreendimento.municipio.limite;
                    mensagem = Mensagem.CARACTERIZACAO_COORDENADAS_FORA_DO_MUNICIPIO;

                } else if (this.empreendimento.jurisdicao == Esfera.ESTADUAL ||
                        this.empreendimento.jurisdicao == Esfera.FEDERAL) {

                    limite = empreendimento.municipio.estado.limite;


                    mensagem = Mensagem.CARACTERIZACAO_COORDENADAS_FORA_DO_ESTADO;
                }

                if (!geometriaComplexoEstaContidaNoLimite(geo, limite))
                    throw new ValidacaoException(mensagem);

            } else {

                Validacao.validateRequired(empreendimento.imovel);
                Validacao.validar(empreendimento.imovel);

                Geometry limite = null;

                if (this.empreendimento.jurisdicao == Esfera.MUNICIPAL) {

                    limite = empreendimento.getLimiteMunicipio();

                } else if (this.empreendimento.jurisdicao == Esfera.ESTADUAL ||
                        this.empreendimento.jurisdicao == Esfera.FEDERAL) {

                    limite = empreendimento.getLimiteEstado();
                }

                if (limite == null || !geometriaComplexoEstaContidaNoLimite(geo, limite))
                    throw new ValidacaoException(Mensagem.CARACTERIZACAO_COORDENADAS_FORA_DO_IMOVEL);
            }

            if (geo.isDentroEmpreendimento()) {

                Geometry limite = null;
                Mensagem mensagem = null;

                br.ufla.lemaf.beans.Empreendimento empreendimentosEU = WebServiceEntradaUnica.findEmpreendimentosEU(empreendimento);
                limite = GeoJsonUtils.toGeometry(empreendimentosEU.localizacao.geometria);

                if (!geometriaComplexoEstaContidaNoLimite(geo, limite))
                    throw new ValidacaoException(Mensagem.CARACTERIZACAO_COORDENADAS_FORA_DO_EMPREENDIMENTO);
            }
        }

    }

    private boolean geometriaComplexoEstaContidaNoLimite(GeometriaComplexo geometriaComplexo, Geometry limite) {

        PreparedGeometry limitePreparado = PreparedGeometryFactory.prepare(limite);

        if (limitePreparado.contains(geometriaComplexo.geometria)) {
            return true;
        } else {

            Geometry intersecao = limite.buffer(0).intersection(geometriaComplexo.geometria.buffer(0));

            if (intersecao != null && !intersecao.isEmpty()) {

                geometriaComplexo.geometria = intersecao;
                return true;

            }
        }

        return false;
    }

    private void salvarDae() {

        this.empreendimento.empreendimentoEU = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(this.empreendimento.cpfCnpj).empreendimentoEU;

        Dae dae = new Dae(this);

        dae.valor = this.valorTaxaAdministrativa;

        dae.save();
    }

    public DaeLicenciamento salvarDaeLicenciamento() throws ScriptException {

        this.empreendimento.empreendimentoEU = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(this.empreendimento.cpfCnpj).empreendimentoEU;

        DaeLicenciamento daeLicenciamento = new DaeLicenciamento(this);

        daeLicenciamento.valor = this.valorTaxaLicenciamento;

        daeLicenciamento.save();

        return daeLicenciamento;
    }

    public Dae emitirDae() {

        if (!isStatus(StatusCaracterizacao.AGUARDANDO_EMISSAO_TAXA_EXPEDIENTE)
                && !isStatus(StatusCaracterizacao.VENCIDO_AGUARDANDO_EMISSAO_TAXA_EXPEDIENTE)
                && !isStatus(StatusCaracterizacao.VENCIDO_AGUARDANDO_PAGAMENTO_TAXA_EXPEDIENTE)) {
            throw new ValidacaoException(Mensagem.CARACTERIZACAO_NAO_AGUARDANDO_EMISSAO_DAE);
        }

        Dae dae = Dae.findByCaracterizacao(this);

        dae.emitirApartirDaSEFAZ();

        if (dae.status == Dae.Status.EMITIDO) {

            this.status = StatusCaracterizacao.findById(StatusCaracterizacao.AGUARDANDO_QUITACAO_TAXA_EXPEDIENTE);
            super.save();
        }

        return dae;

    }

    public DaeLicenciamento emitirDaeLicenciamento() {

        if (!isStatus(StatusCaracterizacao.AGUARDANDO_EMISSAO_TAXA_LICENCIAMENTO)
                && !isStatus(StatusCaracterizacao.VENCIDO_AGUARDANDO_EMISSAO_TAXA_LICENCIAMENTO)
                && !isStatus(StatusCaracterizacao.VENCIDO_AGUARDANDO_PAGAMENTO_TAXA_LICENCIAMENTO)) {
            throw new ValidacaoException(Mensagem.CARACTERIZACAO_NAO_AGUARDANDO_EMISSAO_DAE);
        }

        DaeLicenciamento daeLicenciamento = DaeLicenciamento.findByCaracterizacao(this);

        //daeLicenciamento.emitirApartirDoGestaoPagamentos();
        daeLicenciamento.emitirApartirDaSEFAZ();

        if (daeLicenciamento.status == DaeLicenciamento.Status.EMITIDO) {

            this.status = StatusCaracterizacao.findById(StatusCaracterizacao.AGUARDANDO_QUITACAO_TAXA_LICENCIAMENTO);
            super.save();
        }

        return daeLicenciamento;

    }

    public void gerarPDFLicencasCaracterizacao() throws Exception {

        List<Licenca> licencas = Licenca.find("byCaracterizacao", this).fetch();

        for (Licenca licenca : licencas) {
            licenca.documento = licenca.gerarPDF();
            licenca.save();
        }
    }

    public boolean isStatus(Long idStatus) {

        return this.status != null && this.status.id.equals(idStatus);
    }

    @Override
    public List getListaSobreposicao() {
        return this.isDentroEmpreendimento() ? this.sobreposicoesEmpreendimento : this.isComplexo() ? this.sobreposicaoComplexos : Collections.emptyList();
    }

    @Override
    public void setListaSobreposicao(List sobreposicoes) {
        if (this.isDentroEmpreendimento()) {
            this.sobreposicoesEmpreendimento = sobreposicoes;
        } else if (this.isComplexo()) {
            this.sobreposicaoComplexos = sobreposicoes;
        }
    }

    @Override
    public <T extends SobreposicaoDistancia> T getObjetoSobreposicao
            (DadosSobreposicaoVO dadosSobreposicao, GuardaSobreposicao guardaSobreposicao, TipoSobreposicao tp) {

        return (T) (this.isDentroEmpreendimento() ? new SobreposicaoCaracterizacaoEmpreendimento(tp, this, dadosSobreposicao) :
                this.isComplexo() ? new SobreposicaoComplexo(tp, this, dadosSobreposicao) : null);
    }

    public enum OrdenacaoCaracterizacao {

        NUMERO_PROCESSO_ASC("processo.numero ASC"),
        NUMERO_PROCESSO_DESC("processo.numero DESC"),
        NUMERO_CARACTERIZACAO_ASC("numero ASC"),
        NUMERO_CARACTERIZACAO_DESC("numero DESC"),
        NUMERO_DLA_ASC("dispensa.numero ASC"),
        NUMERO_DLA_DESC("dispensa.numero DESC"),
        DATA_ASC("dataCadastro ASC"),
        DATA_DESC("dataCadastro DESC"),
        STATUS_ASC("status.nome ASC"),
        STATUS_DESC("status.nome DESC"),
        NUMERO_PROCESSO_CARACT_ASC("processo.numero ASC, c.numero ASC");

        private String valor;

        OrdenacaoCaracterizacao(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return this.valor;
        }

    }

    public static List<Caracterizacao> list(Long idEmpreendimento, Integer numeroPagina, Integer qtdItensPorPagina, OrdenacaoCaracterizacao ordenacao) {

        JPAQuery jpaQuery;

        if (ordenacao == null)
            ordenacao = OrdenacaoCaracterizacao.NUMERO_PROCESSO_CARACT_ASC;

        StringBuilder sb = new StringBuilder();

        String select = "";

        select = "SELECT c FROM " + Caracterizacao.class.getCanonicalName() + " c " +
                "INNER JOIN c.empreendimento emp LEFT JOIN c.dispensa ";

        String where = "WHERE emp.id = :idEmpreendimento AND c.ativo = TRUE AND c.ocultarListagem = false";
        String ordenacaoSql = " ORDER BY c." + ordenacao.getValor();

        sb.append(select);

        sb.append(where);
        sb.append(ordenacaoSql);

        jpaQuery = Empreendimento.find(sb.toString());

        jpaQuery.setParameter("idEmpreendimento", idEmpreendimento);

        List<Caracterizacao> caracterizacoes = null;

        if (numeroPagina > 0 && qtdItensPorPagina > 0) {

            caracterizacoes = jpaQuery.fetch(numeroPagina, qtdItensPorPagina);
        } else {

            caracterizacoes = jpaQuery.fetch();
        }

        caracterizacoes = DistinctRootEntityResultTransformer.INSTANCE.transformList(caracterizacoes);

        return caracterizacoes;

    }

    public static Long countByEmpreendimento(Long idEmpreendimento) {

        JPAQuery jpaQuery;

        String select =
                "SELECT COUNT(*) FROM " + Caracterizacao.class.getCanonicalName() + " c " +
                        "INNER JOIN c.empreendimento emp " +
                        "WHERE emp.id = :idEmpreendimento AND c.ativo = TRUE AND " +
                        "c.ocultarListagem = FALSE";

        jpaQuery = Empreendimento.find(select)
                .setParameter("idEmpreendimento", idEmpreendimento);

        return jpaQuery.first();

    }

    public void updateEtapaDocumentacao(Caracterizacao caracterizacaoAtualizada) {

        this.declaracaoVeracidadeInformacoes = caracterizacaoAtualizada.declaracaoVeracidadeInformacoes;

        super.save();
    }

    private void etapaEnquadramento() {

        if (!this.retificacao) {

            this.tipo = AtividadeCaracterizacao.getEnquadramento(this.atividadesCaracterizacao);

            if (this.tipo.id.equals(TipoCaracterizacao.ORDINARIO)) {
                throw new ValidacaoException(Mensagem.CARACTERIZACAO_ENQUADRAMENTO_ORDINARIO);
            }
        }
    }


    /**
     * Finaliza a caracterização. No caso de solicitação de mais de um tipo de licença,
     * será gerada uma cópia da caracterização para cada um destes tipos de licença e
     * também será salvo um DAE para cada uma. Após finalizadas, o status "Aguardando
     * Emissão do DAE" será atribuído às caracterizações.
     */
    public void finalizar() throws ScriptException {

        if (!this.isStatus(StatusCaracterizacao.EM_ANDAMENTO) &&
                !this.isStatus(StatusCaracterizacao.DEFERIDO) &&
                !this.isStatus(StatusCaracterizacao.NOTIFICADO) &&
                !this.isStatus(StatusCaracterizacao.NOTIFICADO_EM_ANDAMENTO) &&
                !this.isStatus(StatusCaracterizacao.AGUARDANDO_DOCUMENTACAO)) {

            throw new IllegalStateException("Caracterização não pode ser finalizada pois não está no status Em andamento, Vigente ou Atendendo Notificação");
        }

        if (possuiSolicitacoesDocumentoPendentes())
            throw new ValidacaoException(Mensagem.SOLICITACOES_DOCUMENTO_NAO_ATENDIDAS);

        if (!this.declaracaoVeracidadeInformacoes)
            throw new ValidacaoException(Mensagem.CARACTERIZACAO_DECLARACAO_VERACIDADE_INFORMACOES_PENDENTE);

        if (this.isStatus(StatusCaracterizacao.NOTIFICADO_EM_ANDAMENTO) || this.isStatus(StatusCaracterizacao.AGUARDANDO_DOCUMENTACAO)) {

            Notificacao notificacao = Notificacao.findAtivaByCaracterizacao(this.idCaracterizacaoOrigem);

            if (notificacao == null) {
                throw new IllegalStateException("Solicitação notificada em andamento não possui notificação");
            }


            if (notificacao.documentacaoFinalizada()) {
                this.status = StatusCaracterizacao.findById(StatusCaracterizacao.NOTIFICADO_GEO_FINALIZADA);
            } else {
                this.status = StatusCaracterizacao.findById(StatusCaracterizacao.AGUARDANDO_DOCUMENTACAO);
            }

//            if(this.dae == null){
//                this.dae = new Dae(this);
//                this.dae.valor = TaxaAdministrativaDae.findValorAtual();
//                this.dae.save();
//            }

//            this.dae.processamentoDeferimentoDAE();
//            this.dae.dataPagamento = new Date();
//            this.dae._save();

            this.retificacao = true;
            this.emAnalise = false;
            super.save();

            return;
        }


        if (this.isStatus(StatusCaracterizacao.NOTIFICADO)) {

            Notificacao notificacao = Notificacao.findAtivaByCaracterizacao(this.id);

            if (notificacao == null) {
                throw new IllegalStateException("Solicitação notificada em andamento não possui notificação");
            }

            if (notificacao.documentacaoFinalizada()) {
                this.status = StatusCaracterizacao.findById(StatusCaracterizacao.NOTIFICACAO_ATENDIDA);
            }

            this.retificacao = true;
            this.emAnalise = false;
            super.save();

            return;
        }

        if (!this.isStatus(StatusCaracterizacao.DEFERIDO) && !this.isRenovacao()) {
            this.tipoLicenca = this.tiposLicencaEmAndamento.get(0);
        }

        TaxaAdministrativaDae taxaAdministrativaDae = TaxaAdministrativaDae.getTaxaAtual();

        boolean atividadeDispensavel = taxaAdministrativaDae.atividadeDispensavel;
        boolean atividadeLicenciavel = taxaAdministrativaDae.atividadeLicenciavel;

        if(taxaAdministrativaDae.isento ||
                (this.tipoLicenca.sigla.equals("DI") && !atividadeDispensavel) ||
                (!this.tipoLicenca.sigla.equals("DI") && !atividadeLicenciavel)){

        if (this.tipoLicenca.sigla.equals("DDLA") && isCaracterizacaoIsenta) {
            this.status = StatusCaracterizacao.findById(StatusCaracterizacao.DEFERIDO);
        } else if (isCaracterizacaoIsenta) {
            this.status = StatusCaracterizacao.findById(StatusCaracterizacao.EM_ANALISE);

        } else {
            this.status = StatusCaracterizacao.findById(StatusCaracterizacao.AGUARDANDO_EMISSAO_TAXA_EXPEDIENTE);
        }

        this.dataFinalizacao = new Date();

        if (this.tiposLicencaEmAndamento.size() > 1) {

            for (int i = 1; i < this.tiposLicencaEmAndamento.size(); i++) {

                // Caracterização
                Caracterizacao caracterizacao = this.gerarCopia();
                caracterizacao.tipoLicenca = this.tiposLicencaEmAndamento.get(i);

                caracterizacao._save();
                caracterizacao.salvarDae();
                if (!caracterizacao.tipoLicenca.id.equals(TipoLicenca.DISPENSA_LICENCA_AMBIENTAL)) {
                    caracterizacao.salvarDaeLicenciamento();
                }
                caracterizacao._save();

            }
        }

        this.tiposLicencaEmAndamento.clear();

        super.save();
        this.salvarDae();

        if (!this.tipoLicenca.id.equals(TipoLicenca.DISPENSA_LICENCA_AMBIENTAL)) {
            this.salvarDaeLicenciamento();
        }

        JPA.em().refresh(this);

        if (this.empreendimento.isIsento()) {
            this.dae.processamentoDeferimentoDAE();
            this.dae.dataPagamento = new Date();
            this.dae._save();
            super.save();
        }

    }

    public void finalizarNotificacao() {

        if (!this.isStatus(StatusCaracterizacao.NOTIFICADO) &&
                !this.isStatus(StatusCaracterizacao.NOTIFICADO_EM_ANDAMENTO) &&
                !this.isStatus(StatusCaracterizacao.AGUARDANDO_DOCUMENTACAO) &&
                !this.isStatus(StatusCaracterizacao.NOTIFICADO_GEO_FINALIZADA) &&
                !this.isStatus(StatusCaracterizacao.NOTIFICADO_EMPREENDIMENTO_ALTERADO)) {

            throw new IllegalStateException("Caracterização não pode ser finalizada pois não está no status Em andamento, Vigente ou Atendendo Notificação");
        }

        if (possuiSolicitacoesDocumentoPendentes())
            throw new ValidacaoException(Mensagem.SOLICITACOES_DOCUMENTO_NAO_ATENDIDAS);

        if (!this.declaracaoVeracidadeInformacoes)
            throw new ValidacaoException(Mensagem.CARACTERIZACAO_DECLARACAO_VERACIDADE_INFORMACOES_PENDENTE);


        Notificacao notificacao = Notificacao.findAtivaByCaracterizacao(this.id);

        if (notificacao == null) {
            throw new IllegalStateException("Solicitação notificada em andamento não possui notificação");
        }

        if (notificacao.documentacaoFinalizada()) {
            this.status = StatusCaracterizacao.findById(StatusCaracterizacao.NOTIFICACAO_ATENDIDA);
        } else {
            this.status = StatusCaracterizacao.findById(StatusCaracterizacao.AGUARDANDO_DOCUMENTACAO);
        }

        if (this.dae == null) {
            this.dae = new Dae(this);
            this.dae.valor = this.valorTaxaAdministrativa;
            this.dae.save();
        }

//        this.dae.processamentoDeferimentoDAE();
        this.dae.dataPagamento = new Date();
        this.dae._save();

        this.retificacao = true;
        this.emAnalise = false;
        super.save();
    }


    /**
     * Gera uma cópia da caracterização, duplicando todas as informações
     * declaradas pelo cadastrante. Utilizado em casos em que foi solicitado
     * mais de um tipo de licença, onde, ao finalizar, é gerada uma cópia da
     * caracterização para cada tipo de licença.
     */
    private Caracterizacao gerarCopia() {

        Caracterizacao copia = new Caracterizacao();
        copia.tipoLicenca = this.tipoLicenca;
        copia.tipo = this.tipo;
        copia.dataCadastro = this.dataCadastro;
        copia.status = this.status;
        copia.empreendimento = this.empreendimento;
        copia.dataFinalizacao = this.dataFinalizacao;
        copia.declaracaoVeracidadeInformacoes = this.declaracaoVeracidadeInformacoes;

        for (AtividadeCaracterizacao atividadeCaracterizacao : this.atividadesCaracterizacao) {

            if (copia.atividadesCaracterizacao == null) {
                copia.atividadesCaracterizacao = new ArrayList<>();
            }

            AtividadeCaracterizacao novaAC = atividadeCaracterizacao.gerarCopia();
            novaAC.caracterizacao = copia;
            copia.atividadesCaracterizacao.add(novaAC);


        }

        copia.respostas = new ArrayList<>(this.respostas);
        copia.solicitacoesDocumentoCaracterizacao = new ArrayList<>();

        if (this.solicitacoesDocumentoCaracterizacao != null) {

            for (SolicitacaoDocumentoCaracterizacao solicitacao : this.solicitacoesDocumentoCaracterizacao) {

                SolicitacaoDocumentoCaracterizacao copiaSolic = solicitacao.gerarCopia();
                copiaSolic.caracterizacao = copia;
                copia.solicitacoesDocumentoCaracterizacao.add(copiaSolic);
            }
        }

        return copia;
    }

    private void gerarNumeroProtocolo() {

        if (!this.retificacao) {
            this.numero = this.numeroProcessoAutomatico;
        }
    }

    /**
     * Verifica se existe alguma solicitação de documento obrigatória não atendida.
     */
    private boolean possuiSolicitacoesDocumentoPendentes() {
        return this.solicitacoesDocumentoCaracterizacao != null && this.solicitacoesDocumentoCaracterizacao.stream()
                .anyMatch(solicitacao -> solicitacao.obrigatorio && solicitacao.documento == null);
    }

    public static Caracterizacao findDadosCaracterizacao(Long id){

        Caracterizacao caracterizacao = Caracterizacao.findById(id);
        caracterizacao.empreendimento.validarSeUsuarioCadastrante();
        caracterizacao.empreendimento.empreendimentoEU = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(caracterizacao.empreendimento.cpfCnpj).empreendimentoEU;

        caracterizacao.inicializaParametros();

        caracterizacao.renovacaoLicenca = false;

        // Antes de finalizar o cadastro há somente a lista de tipos de licencas em andamento
        for (TipoLicenca tipoLicenca : caracterizacao.tiposLicencaEmAndamento) {
            tipoLicenca.valorDae = TaxaLicenciamento.calcular(caracterizacao, tipoLicenca);
            tipoLicenca.isento = caracterizacao.empreendimento.isIsento();
        }
        // Após finalizar o cadastro há somente o tipo de licença (um para cada caracterização)
        if (caracterizacao.tipoLicenca != null) {
            caracterizacao.tipoLicenca.valorDae = TaxaLicenciamento.calcular(caracterizacao, caracterizacao.tipoLicenca);
            caracterizacao.tipoLicenca.isento = caracterizacao.empreendimento.isIsento();
        }

        caracterizacao.linkTaxasLicenciamento = TaxaAdministrativaDae.getTaxaAtual(caracterizacao).linkTaxasLicenciamento;

        return caracterizacao;
    }

    public static Caracterizacao findDadosRenovacao(Long id) {

        Caracterizacao caracterizacao = Caracterizacao.findById(id);


        caracterizacao.inicializaParametros();

        caracterizacao.renovacao = true;
        caracterizacao.idCaracterizacaoOrigem = caracterizacao.id;
        caracterizacao.notificacao = Notificacao.findAtivaByCaracterizacao(caracterizacao);

        return caracterizacao;
    }

    public static Caracterizacao findDadosNotificacao(Long id) {

        Caracterizacao caracterizacao = Caracterizacao.findById(id);
        caracterizacao.empreendimento.validarSeUsuarioCadastrante();
        caracterizacao.notificacao = Notificacao.findAtivaByCaracterizacao(caracterizacao);
        caracterizacao.notificacao.inicializaPrazo();
        caracterizacao.notificacao.initParecerNotificacao();
        caracterizacao.cadastrante = Pessoa.convert(WebServiceEntradaUnica.findPessoaByCpfCnpjEU(caracterizacao.cpfCnpjCadastrante));

        return caracterizacao;
    }

    public models.analise.Documento getDadosMinutaLicenca(Caracterizacao caracterizacao) {

        caracterizacao.notificacao = Notificacao.findAtivaByCaracterizacao(caracterizacao);

        if (caracterizacao.notificacao != null && caracterizacao.notificacao.analiseTecnica != null && caracterizacao.notificacao.analiseTecnica.pareceresAnalistaTecnico != null
                && caracterizacao.notificacao.analiseTecnica.pareceresAnalistaTecnico.get(0).documentoMinuta != null) {
            documentoMinuta = caracterizacao.notificacao.analiseTecnica.pareceresAnalistaTecnico.get(0).documentoMinuta;
            return documentoMinuta;
        }
        return new models.analise.Documento();
    }

    public static List<Caracterizacao> findCaracterizacoesEmAnalise() {
        return Caracterizacao.find("status.id IN (:x, :y, :z, :n) and emAnalise = false")
                .setParameter("x", StatusCaracterizacao.EM_ANALISE)
                .setParameter("y", StatusCaracterizacao.EM_RENOVACAO_SEM_ALTERACAO)
                .setParameter("z", StatusCaracterizacao.EM_RENOVACAO_COM_ALTERACAO)
                .setParameter("n", StatusCaracterizacao.NOTIFICACAO_ATENDIDA)
                .fetch();
    }

    public void setEmAnalise(Boolean emAnalise) {
        this.emAnalise = emAnalise;
        super.save();
    }

    public void alterarStatusAposEditarRetificacaoDoEmpreendimento(Caracterizacao caracterizacao) {
        caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacao.NOTIFICADO_EMPREENDIMENTO_ALTERADO);
        this.status = caracterizacao.status;
        super.save();
    }

    public AtividadeCaracterizacao getAtividadeCaracterizacaoMaiorPotencialPoluidorEPorte() {

        AtividadeCaracterizacao atividadeCaracterizacaoMaiorPPDPorte = null;

        for (AtividadeCaracterizacao atividadeCaracterizacao : this.atividadesCaracterizacao) {

            if (atividadeCaracterizacaoMaiorPPDPorte == null) {
                atividadeCaracterizacaoMaiorPPDPorte = atividadeCaracterizacao;
            } else if (atividadeCaracterizacao.atividade.potencialPoluidor.compareTo(atividadeCaracterizacaoMaiorPPDPorte.atividade.potencialPoluidor) == 1) {
                atividadeCaracterizacaoMaiorPPDPorte = atividadeCaracterizacao;
            } else if (atividadeCaracterizacao.atividade.potencialPoluidor.compareTo(atividadeCaracterizacaoMaiorPPDPorte.atividade.potencialPoluidor) == 0) {
                // Caso PPD seja igual, comparar os portes.
                if (atividadeCaracterizacao.porteEmpreendimento.compareTo(atividadeCaracterizacaoMaiorPPDPorte.porteEmpreendimento) == 0
                        || atividadeCaracterizacao.porteEmpreendimento.compareTo(atividadeCaracterizacaoMaiorPPDPorte.porteEmpreendimento) == 1) {
                    atividadeCaracterizacaoMaiorPPDPorte = atividadeCaracterizacao;
                }
            }

        }

        return atividadeCaracterizacaoMaiorPPDPorte;
    }

    public boolean getHasNotificacao() {

        Processo processo = Processo.find("byNumero", this.numero).first();

        if (processo != null) {

            Analise analise = processo.getAnalise();

            if (analise != null && analise.temNotificacaoAberta != null) {
                return analise.temNotificacaoAberta;
            }
        }

        return false;

    }

    public String getNumeroLicenca() {

        StringBuilder numeroLicenca = new StringBuilder();

        String separador = ", ";
        if (this.dispensa != null) {
            numeroLicenca.append(this.dispensa.numero);
        } else if (this.licencas != null && !this.licencas.isEmpty()) {
            this.licencas.forEach(l -> numeroLicenca.append(l.numero).append(separador));
            if (numeroLicenca.length() > 0) {
                numeroLicenca.setLength(numeroLicenca.length() - separador.length());
            }
        } else {
            numeroLicenca.append("-");
        }

        return numeroLicenca.toString();

    }

    public void excluirAtividadesCaracterizacao() {
        this.atividadesCaracterizacao.forEach(GenericModel::delete);
    }

    public Tipologia getTipologia() {
        return atividadesCaracterizacao.stream().findFirst().get().atividade.tipologia;
    }

    public static List<ResultadoSobreposicaoCamadaVO> buscarSobreposicoesNaoPermitidasDI(String geometria) {

        ArrayList<CamadaSobreposicaoVO> camadas = new ArrayList<>();
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_UC_FEDERAL_APA_FORA, TipoSobreposicao.findById(TipoSobreposicao.UC_FEDERAL_APA_FORA)));
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_UC_ESTADUAL_PI_DENTRO, TipoSobreposicao.findById(TipoSobreposicao.UC_ESTADUAL_PI_DENTRO)));
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_TERRA_INDIGENA, TipoSobreposicao.findById(TipoSobreposicao.TERRA_INDIGENA)));
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_TERRA_INDIGENA_ESTUDO, TipoSobreposicao.findById(TipoSobreposicao.TERRA_INDIGENA_ESTUDO)));

        return GeoCalc.getSobreposicoesGeoserver(GeoJsonUtils.toGeometry(geometria), camadas);
    }

    public static List<ResultadoSobreposicaoCamadaVO> buscarSobreposicoesNaoPermitidasSimplificado(String geometria) {

        ArrayList<CamadaSobreposicaoVO> camadas = new ArrayList<>();
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_UC_FEDERAL_APA_FORA, TipoSobreposicao.findById(TipoSobreposicao.UC_FEDERAL_APA_FORA)));
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_UC_ESTADUAL_PI_DENTRO, TipoSobreposicao.findById(TipoSobreposicao.UC_ESTADUAL_PI_DENTRO)));
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_TERRA_INDIGENA, TipoSobreposicao.findById(TipoSobreposicao.TERRA_INDIGENA)));

        return GeoCalc.getSobreposicoesGeoserver(GeoJsonUtils.toGeometry(geometria), camadas);
    }

    private static List<ResultadoSobreposicaoCamadaVO> buscarSobreposicoesSimplificado(Geometry geometria) {

        ArrayList<CamadaSobreposicaoVO> camadas = new ArrayList<>();

        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_UC_FEDERAL_APA_DENTRO, TipoSobreposicao.findById(TipoSobreposicao.UC_FEDERAL_APA_DENTRO)));
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_UC_FEDERAL_ZA, TipoSobreposicao.findById(TipoSobreposicao.UC_FEDERAL_ZA)));
        //TODO: Tive que comentar essa camada pq estava dando erro na hora de salvar a licença. assim que arrumar, descomentar!!!
        //camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_UC_ESTADUAL_PI_FORA, TipoSobreposicao.findById(TipoSobreposicao.UC_ESTADUAL_PI_FORA)));
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_UC_ESTADUAL_ZA_PI_FORA, TipoSobreposicao.findById(TipoSobreposicao.UC_ESTADUAL_ZA_PI_FORA)));
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_UC_MUNICIPAL, TipoSobreposicao.findById(TipoSobreposicao.UC_MUNICIPAL)));
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_AREAS_EMBARGADAS_IBAMA, TipoSobreposicao.findById(TipoSobreposicao.AREAS_EMBARGADAS_IBAMA)));
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_AUTO_DE_INFRACAO_IBAMA, TipoSobreposicao.findById(TipoSobreposicao.AUTO_DE_INFRACAO_IBAMA)));
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_SAUIM_DE_COLEIRA, TipoSobreposicao.findById(TipoSobreposicao.SAUIM_DE_COLEIRA)));
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_BENS_ACAUTELADOS_IPHAN_PT, TipoSobreposicao.findById(TipoSobreposicao.BENS_ACAUTELADOS_IPHAN_PT)));
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_BENS_ACAUTELADOS_IPHAN_POL, TipoSobreposicao.findById(TipoSobreposicao.BENS_ACAUTELADOS_IPHAN_POL)));
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_TERRA_INDIGENA_ZA, TipoSobreposicao.findById(TipoSobreposicao.TERRA_INDIGENA_ZA)));
        camadas.add(new CamadaSobreposicaoVO(Configuracoes.GEOSERVER_LAYER_TERRA_INDIGENA_ESTUDO, TipoSobreposicao.findById(TipoSobreposicao.TERRA_INDIGENA_ESTUDO)));

        return GeoCalc.getSobreposicoesGeoserver(geometria, camadas);
    }

    public static void updateStatus(UpadateStatusCaracterizacaoVO upadateStatusCaracterizacaoVO) {
        Caracterizacao c = find("numero = :numero AND ativo = TRUE")
                .setParameter("numero", upadateStatusCaracterizacaoVO.numeroCaracterizacao).first();
        c.status = StatusCaracterizacao.find("byCodigo", upadateStatusCaracterizacaoVO.codigoStatus).first();
        c._save();
    }

    public static List<HistoricoCaracterizacaoVO> getHistoricos(Long id, String nomeResponsavel) {
        Caracterizacao c = findById(id);
        List<Caracterizacao> caracterizacoes = find("id < :idCaracterizacao AND numero = :numero AND ativo = FALSE ORDER BY id DESC")
                .setParameter("numero", c.numero)
                .setParameter("idCaracterizacao", id).fetch();
        return caracterizacoes.stream().map(ca -> new HistoricoCaracterizacaoVO(ca, nomeResponsavel))
                .collect(Collectors.toList());
    }

    public Boolean isNotificacaoEmAndamento() {
        return this.status.id.equals(StatusCaracterizacao.NOTIFICADO_EM_ANDAMENTO) ||
                this.status.id.equals(StatusCaracterizacao.AGUARDANDO_DOCUMENTACAO) ||
                this.status.id.equals(StatusCaracterizacao.NOTIFICADO_GEO_FINALIZADA);
    }

    private Boolean caracterizacaoAnteriorAtiva() {
        return this.idCaracterizacaoOrigem != null && ((Caracterizacao) findById(this.idCaracterizacaoOrigem)).ativo;
    }

    public Boolean isRenovacao() {
        return this.renovacao && this.caracterizacaoAnteriorAtiva();
    }

    public Boolean isRetificacao() {
        return this.retificacao && (this.idCaracterizacaoOrigem == null || !this.caracterizacaoAnteriorAtiva());
    }

    public void setTaxasCaracterizacao() {

        boolean isEmpreendimentIsento = this.empreendimento.isIsento();
        boolean isCaracterizacaoIsenta = this.isCaracterizacaoIsenta();

        if (isEmpreendimentIsento) {

            this.valorTaxaLicenciamento = 0.00;
            this.valorTaxaAdministrativa = 0.00;

        } else if (isCaracterizacaoIsenta) {

            this.valorTaxaAdministrativa = 0.00;
            this.setTaxaLicenciamento();

        } else {

            this.setTaxaLicenciamento();
            this.setTaxaAdministrativa();

        }

    }

    public void setTaxaLicenciamento() {

        this.tiposLicencaEmAndamento.forEach(tipoLicencaEmAndamento ->
                this.valorTaxaLicenciamento = TaxaLicenciamento.getTaxaLicenca(this, tipoLicencaEmAndamento)
        );

        this.valorTaxaLicenciamento = this.valorTaxaLicenciamento * this.vigenciaSolicitada;

    }

    public void setTaxaAdministrativa() {
        this.valorTaxaAdministrativa = TaxaAdministrativaDae.findValorAtual(this);
    }

    public void remover() {

        if (this.ocultarListagem) {
            throw new ValidacaoException(Mensagem.CARACTERIZACAO_REMOVIDA_ERRO);
        }

        if (this.status.id.equals(StatusCaracterizacao.EM_ANDAMENTO)) {

            this.delete();

        } else {

            this.ocultarListagem = true;
            this.save();

        }
    }

    public static void ocultaExibicaoListagem(Long idCaracterizacao) {

        if (idCaracterizacao != null) {

            Caracterizacao caracterizacao = findById(idCaracterizacao);

            if (caracterizacao == null) {
                throw new ValidacaoException(Mensagem.CARACTERIZACAO_OCULTAR_LISTAGEM);
            }

            caracterizacao.ocultarListagem = true;
            caracterizacao.save();
        }

    }

    public static void atualizarVigenciaCaracterizacao(Caracterizacao caracterizacao, Integer novaVigencia) {
        caracterizacao.vigenciaSolicitada = novaVigencia;
    }

    public void calcularTaxaLicenciamentoAposAprovadaNoAnalise(int validadeParaCalculoTaxa) {
        this.valorTaxaLicenciamento = TaxaLicenciamento.getTaxaLicenca(this, getTipoLicenca()) * validadeParaCalculoTaxa;

    }

    public TipoLicenca getTipoLicenca(){
        return (this.tiposLicencaEmAndamento != null && !this.tiposLicencaEmAndamento.isEmpty()) ? this.tiposLicencaEmAndamento.get(0) : this.tipoLicenca;
    }

    public Double getValorTaxaLicenciamentoOriginal(Caracterizacao caracterizacao) {
        return TaxaLicenciamento.getTaxaLicenca(caracterizacao, caracterizacao.tipoLicenca);
    }

    public String textoLocalizacaoPDF() {

        Point centroide = this.empreendimento.centroid();
        String textoLocalizacao = "LOCALIZAÇÃO: X: " + centroide.getX() + ", Y: " + centroide.getY();

        if (!this.empreendimento.getGeometryType().equals("Point")) {
            textoLocalizacao += " (centroide)";
        }

        return textoLocalizacao;
    }

//    public void inicializaParametros() {
//        this.atividadesCaracterizacao.forEach(ac ->
//                ac.atividade.parametros = ac.atividade.parametros.stream().map(p ->
//                    new ParametroAtividade(p, ac.atividade.parametrosAtividadeComIdEDescricaoMap.get(p).descricaoUnidade))
//                        .collect(Collectors.toList())
//        );
//    }

    public void inicializaParametros() {

        this.atividadesCaracterizacao.forEach(ac ->
                ac.atividade.parametros.forEach(p -> {
                    p.descricao = ac.atividade.parametrosAtividadeComIdEDescricaoMap.get(p).descricaoUnidade;
                })
        );

    }

}
