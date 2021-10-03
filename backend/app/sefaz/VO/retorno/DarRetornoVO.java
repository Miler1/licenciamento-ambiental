package sefaz.VO.retorno;

import org.apache.commons.lang.StringUtils;

public class DarRetornoVO {

    public DadosVO dados;

    public DarRetornoVO(String retorno) {

        this.dados = new DadosVO(StringUtils.substringBetween(retorno, "<dar:dados>", "</dar:dados>"));

    }

}
