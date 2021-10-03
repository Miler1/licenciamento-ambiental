package models.sicar;

public class ImovelCompleto {

    public Long id;

    public String codigoImovel;

    public Boolean isAtivo;

    //Indica o status do imóvel (AT - Ativo; PE - Pendente; CA - Cancelado; SU - Suspenso).
    public String statusImovel;

    public CondicaoInscricao condicaoInscricao;

    public Boolean podeSolicitarLicenca() {
        return this.isAtivo &&
                this.condicaoInscricao.aptoParaLicenciamento(StatusImovel.getInstance(this.statusImovel));
    }
}
