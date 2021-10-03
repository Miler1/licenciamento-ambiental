package sefaz.VO.processamentoDAE;

public class DaePagoVO {

    public String cpfCnpj;
    public String nossoNumero;
    public String data;
    public String valor;
    public String classificacaoDaReceita;

    public DaePagoVO (String[] dados){

        this.cpfCnpj = dados[0];
        this.nossoNumero = dados[1];
        this.data = dados[2];
        this.valor = dados[3];
        this.classificacaoDaReceita = dados[4];
    }

}
