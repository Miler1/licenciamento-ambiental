package sefaz.VO.retorno;

import org.apache.commons.lang.StringUtils;

public class DadosVO {

    public String pdf;
    public String nossoNumero;
    public Double valorTaxa;
    public Double valorTotal;
    public Double valorPrincipal;
    public Double valorMulta;
    public Double valorJuros;
    public Integer codigoTaxa;
    public EnderecoVO endereco;

    public DadosVO(String dados) {

        this.pdf = StringUtils.substringBetween(dados, "<dar:pdf>", "</dar:pdf>");
        this.nossoNumero = StringUtils.substringBetween(dados, "<dar:nossoNumero>", "</dar:nossoNumero>");
        this.valorTaxa = Double.parseDouble(StringUtils.substringBetween(dados, "<dar:valorTaxa>", "</dar:valorTaxa>"));
        this.valorTotal = Double.parseDouble(StringUtils.substringBetween(dados, "<dar:valorTotal>", "</dar:valorTotal>"));
        this.valorPrincipal = Double.parseDouble(StringUtils.substringBetween(dados, "<dar:valorPrincipal>", "</dar:valorPrincipal>"));
        this.valorMulta = Double.parseDouble(StringUtils.substringBetween(dados, "<dar:valorMulta>", "</dar:valorMulta>"));
        this.valorJuros = Double.parseDouble(StringUtils.substringBetween(dados, "<dar:valorJuros>", "</dar:valorJuros>"));
        this.codigoTaxa = Integer.parseInt(StringUtils.substringBetween(dados, "<dar:codigoTaxa>", "</dar:codigoTaxa>"));

        this.endereco = new EnderecoVO(StringUtils.substringBetween(dados, "<dar:endereco>", "</dar:endereco>"));
    }

}
