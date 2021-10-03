package sefaz.VO.retorno;

import org.apache.commons.lang.StringUtils;

public class EnderecoVO {

    public String logradouro;
    public String numero;
    public String bairro;
    public String cep;
    public String complemento;
    public String municipio;
    public String uf;

    public EnderecoVO(String endereco) {

        this.logradouro = StringUtils.substringBetween(endereco, "<dar:logradouro>", "</dar:logradouro>");
        this.numero = StringUtils.substringBetween(endereco, "<dar:numero>", "</dar:numero>");
        this.bairro = StringUtils.substringBetween(endereco, "<dar:bairro>", "</dar:bairro>");
        this.cep = StringUtils.substringBetween(endereco, "<dar:cep>", "</dar:cep>");
        this.complemento = StringUtils.substringBetween(endereco, "<dar:complemento>", "</dar:complemento>");
        this.municipio = StringUtils.substringBetween(endereco, "<dar:municipio>", "</dar:municipio>");
        this.uf = StringUtils.substringBetween(endereco, "<dar:uf>", "</dar:uf>");

    }

}
