package models.sicar;

public class CondicaoInscricao {

    public Long id;
    public String nome;

    public Boolean aptoParaLicenciamento(StatusImovel statusImovel) {
        if (statusImovel.equals(StatusImovel.AT)) {
            return this.id.equals(CondicaoInscricaoEnum.ANALISADO_SEM_PENDENCIA.id) ||
                    this.id.equals(CondicaoInscricaoEnum.ANALISADO_AGUARDANDO_REGULARIZACAO_AMBIENTAL.id) ||
                    this.id.equals(CondicaoInscricaoEnum.ANALISADO_COM_PENDENCIA_AGUARDANDO_ATENDIMENTO_OUTRAS_RESTRICOES.id);
        } else if (statusImovel.equals(StatusImovel.PE)){
            return this.id.equals(CondicaoInscricaoEnum.ANALISADO_SEM_PENDENCIA_PASSIVEL_NOVA_ANALISE.id) ||
                    this.id.equals(CondicaoInscricaoEnum.ANALISADO_AGUARDANDO_REGULARIZACAO_AMBIENTAL.id) ||
                    this.id.equals(CondicaoInscricaoEnum.ANALISADO_COM_PENDENCIA_AGUARDANDO_ATENDIMENTO_OUTRAS_RESTRICOES.id);
        }
        return false;
    }

}
