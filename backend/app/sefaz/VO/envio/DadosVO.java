package sefaz.VO.envio;

import java.math.BigDecimal;

public class DadosVO {

    EmitenteVO emitente;
    String referencia;
    Integer receita;
    Integer taxa;
    String dataPagamento;
    String dataVencimento;
    Integer quantidade;
    BigDecimal valorPrincipal;

    public EmitenteVO getEmitente() {
        return emitente;
    }

    public void setEmitente(EmitenteVO emitente) {
        this.emitente = emitente;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Integer getReceita() {
        return receita;
    }

    public void setReceita(Integer receita) {
        this.receita = receita;
    }

    public Integer getTaxa() {
        return taxa;
    }

    public void setTaxa(Integer taxa) {
        this.taxa = taxa;
    }

    public String getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(String dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorPrincipal() {
        return valorPrincipal;
    }

    public void setValorPrincipal(BigDecimal valorPrincipal) {
        this.valorPrincipal = valorPrincipal;
    }
}
