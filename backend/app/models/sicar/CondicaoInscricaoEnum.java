package models.sicar;

public enum CondicaoInscricaoEnum {

    AGUARDANDO_ANALISE(1L,"Aguardando análise"),
    EM_ANALISE(2L,"Em análise"),
    ANALISADO_SEM_PENDENCIA(3L,"Analisado sem pendências"),
    ANALISADO_COM_PENDENCIA_AGUARDANDO_RETIFICACAO(4L,"Analisado com pendências, aguardando retificação"),
    ANALISADO_COM_PENDENCIA_AGUARDANDO_APRESENTACAO_DOCUMENTOS(5L,"Analisado com pendências, aguardando apresentação de documentos"),
    ANALISADO_COM_PENDENCIA_AGUARDANDO_RETIFICACAO_E_OU_APRESENTACAO_DOCUMENTOS(6L,"Analisado com pendências, aguardando retificação e/ou apresentação de documentos"),
    ANALISADO_AGUARDANDO_REGULARIZACAO_AMBIENTAL(7L,"Analisado, aguardando regularização ambiental (Lei 12.651/12)"),
    ANALISADO_SEM_PENDENCIA_PASSIVEL_NOVA_ANALISE(10L,"Analisado sem pendências, passível de nova análise"),
    ANALISADO_COM_PENDENCIA_AGUARDANDO_ATENDIMENTO_OUTRAS_RESTRICOES(14L,"Analisado com pendências, aguardando atendimento a outras restrições"),
    CANCELADO_POR_DECISAO_JUDICIAL(16L,"Cancelado por decisão judicial"),
    CANCELADO_POR_DECISAO_ADMINISTRATIVA(17L,"Cancelado por decisão administrativa"),
    CANCELADO_POR_SOLICITACAO_PROPIETARIO(18L,"Cancelado por solicitação do proprietário"),
    CANCELADO_POR_DUPLICIDADE(19L,"Cancelado por duplicidade"),
    ANALISADO_PELO_FILTRO_AUTOMATICO(20L,"Analisado pelo Filtro Automático");

    public Long id;
    public String descricao;


    CondicaoInscricaoEnum(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }
}
