package utils;

import models.Pessoa;
import models.TipoLocalizacao;
import models.caracterizacao.Caracterizacao;
import models.caracterizacao.TipoLicenca;
import play.Logger;
import play.Play;
import sefaz.QJUDarRecepcaoSoapBinding;
import sefaz.VO.envio.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebServiceSefaz {

    private static final String SEFAZ_CODIGO_RECEITA = Play.configuration.getProperty("sefaz.codigo.receita");
    private static final String SEFAZ_CODIGO_TAXA_EXPEDIENTE = Play.configuration.getProperty("sefaz.codigo.taxa.expediente");
    private static final String SEFAZ_CODIGO_TAXA_EXPEDIENTE_DISPENSA = Play.configuration.getProperty("sefaz.codigo.taxa.expediente.dispensa");
    private static final String SEFAZ_CODIGO_TAXA_EXPEDIENTE_LICENCA = Play.configuration.getProperty("sefaz.codigo.taxa.expediente.licenca");
    private static final String SEFAZ_CODIGO_TAXA_LICENCA_PREVIA = Play.configuration.getProperty("sefaz.codigo.taxa.licenca.previa");
    private static final String SEFAZ_CODIGO_TAXA_LICENCA_INSTALACAO = Play.configuration.getProperty("sefaz.codigo.taxa.licenca.instalacao");
    private static final String SEFAZ_CODIGO_TAXA_LICENCA_OPERACAO = Play.configuration.getProperty("sefaz.codigo.taxa.licenca.operacao");
    private static final String SEFAZ_CODIGO_TAXA_RENOVACAO_LICENCA_INSTALACAO = Play.configuration.getProperty("sefaz.codigo.taxa.renovacao.licenca.instalacao");
    private static final String SEFAZ_CODIGO_TAXA_RENOVACAO_LICENCA_OPERACAO = Play.configuration.getProperty("sefaz.codigo.taxa.renovacao.licenca.operacao");
    private static final String SEFAZ_QUANTIDADE_EMISSAO = Play.configuration.getProperty("sefaz.quantidade.emissao");
    private static final Integer TIPO_DOCUMENTO_CNPJ = 2;
    private static final Integer TIPO_DOCUMENTO_CPF = 3;

    private static WebServiceSefaz instace;

    private WebServiceSefaz() {

    }

    public static WebServiceSefaz getInstace() {

        if (instace == null) {

            instace = new WebServiceSefaz();
        }

        return instace;
    }

    public static String gerarDaeArrecadacaoTaxaLicenciamento(BigDecimal valor, br.ufla.lemaf.beans.pessoa.Pessoa pagador, Date dataVencimento, Long codigoLicenca) {

        DarEnvioVO darEnvio = montarDarEnvioTaxaLicenciamento(valor, pagador, dataVencimento, codigoLicenca);

        String retorno = null;

        QJUDarRecepcaoSoapBinding darRecepcaoSoapBinding = new QJUDarRecepcaoSoapBinding();

        try {

            retorno = darRecepcaoSoapBinding.processar("", darEnvio.getDarDados());

        } catch (Exception e) {

            Logger.error(e, e.getMessage());

        }

        return retorno;

    }

    public static String gerarDaeArrecadacaoTaxaExpediente(BigDecimal valor, br.ufla.lemaf.beans.pessoa.Pessoa pagador, Date dataVencimento, Long codigoLicenca) {

        DarEnvioVO darEnvio = montarDarEnvioTaxaExpediente(valor, pagador, dataVencimento, codigoLicenca);

        String retorno = null;

        QJUDarRecepcaoSoapBinding darRecepcaoSoapBinding = new QJUDarRecepcaoSoapBinding();

        try {

            retorno = darRecepcaoSoapBinding.processar("", darEnvio.getDarDados());

        } catch (Exception e) {

            Logger.error(e, e.getMessage());

        }

        return retorno;

    }

    private static DarEnvioVO montarDarEnvioTaxaExpediente(BigDecimal valor, br.ufla.lemaf.beans.pessoa.Pessoa pagador, Date dataVencimento, Long codigoLicenca) {

        EnderecoVO endereco = setarEndereco(pagador);

        EmitenteVO emitente = setarEmitente(pagador, endereco);

        DadosVO dados = setarDadosTaxaExpediente(valor, dataVencimento, emitente, codigoLicenca);

        DarEnvioVO darEnvio = new DarEnvioVO();

        String dadosTeste = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<DarEnvio xmlns=\"http://www.daronline.ap.gov.br\">\n" +
                "    <dados>\n" +
                "        <emitente>\n" +
                "            <nome>"+ emitente.getNome() + "</nome>\n" +
                "            <tipoDocumento>" + emitente.getTipoDocumento() + "</tipoDocumento>\n" +
                "            <numeroDocumento>" + emitente.getNumeroDocumento() + "</numeroDocumento>\n" +
                "            <endereco>\n" +
                "                <cep>" + endereco.getCep() + "</cep>\n" +
                "                <complemento>" + endereco.getComplemento() + "</complemento>\n" +
                "                <numero>" + endereco.getNumero() + "</numero>\n" +
                "            </endereco>\n" +
                "        </emitente>\n" +
                "        <referencia>" + dados.getReferencia() + "</referencia>\n" +
                "        <receita>" + dados.getReceita() + "</receita>\n" +
                "        <taxa>" + dados.getTaxa() + "</taxa>\n" +
                "        <dataPagamento>" + dados.getDataPagamento() + "</dataPagamento>\n" +
                "        <dataVencimento>" + dados.getDataVencimento() + "</dataVencimento>\n" +
//                (dados.getTaxa().equals(Integer.parseInt(SEFAZ_CODIGO_TAXA_EXPEDIENTE)) ?
//                "" : "        <valorPrincipal>" + dados.getValorPrincipal() + "</valorPrincipal>\n") +
                "        <quantidade>" + dados.getQuantidade() + "</quantidade>\n" +
                "    </dados>\n" +
                "</DarEnvio>";

        darEnvio.setDarDados(dadosTeste);

        return darEnvio;

    }

    private static DarEnvioVO montarDarEnvioTaxaLicenciamento(BigDecimal valor, br.ufla.lemaf.beans.pessoa.Pessoa pagador, Date dataVencimento, Long codigoLicenca) {

        EnderecoVO endereco = setarEndereco(pagador);

        EmitenteVO emitente = setarEmitente(pagador, endereco);

        DadosVO dados = setarDadosTaxaLicenciamento(valor, dataVencimento, emitente, codigoLicenca);

        DarEnvioVO darEnvio = new DarEnvioVO();

        String valorPrincipal = String.valueOf(dados.getValorPrincipal());
        valorPrincipal = valorPrincipal.replace(".",",");

        String dadosTeste = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<DarEnvio xmlns=\"http://www.daronline.ap.gov.br\">\n" +
                "    <dados>\n" +
                "        <emitente>\n" +
                "            <nome>"+ emitente.getNome() + "</nome>\n" +
                "            <tipoDocumento>" + emitente.getTipoDocumento() + "</tipoDocumento>\n" +
                "            <numeroDocumento>" + emitente.getNumeroDocumento() + "</numeroDocumento>\n" +
                "            <endereco>\n" +
                "                <cep>" + endereco.getCep() + "</cep>\n" +
                "                <complemento>" + endereco.getComplemento() + "</complemento>\n" +
                "                <numero>" + endereco.getNumero() + "</numero>\n" +
                "            </endereco>\n" +
                "        </emitente>\n" +
                "        <referencia>" + dados.getReferencia() + "</referencia>\n" +
                "        <receita>" + dados.getReceita() + "</receita>\n" +
                "        <taxa>"+ dados.getTaxa() +"</taxa>\n" +
                "        <dataPagamento>" + dados.getDataPagamento() + "</dataPagamento>\n" +
                "        <dataVencimento>" + dados.getDataVencimento() + "</dataVencimento>\n" +
                "        <quantidade>" + valorPrincipal + "</quantidade>\n" +
                "    </dados>\n" +
                "</DarEnvio>";

        darEnvio.setDarDados(dadosTeste);

        return darEnvio;

    }

    private static EnderecoVO setarEndereco(br.ufla.lemaf.beans.pessoa.Pessoa pagador) {

        EnderecoVO endereco = new EnderecoVO();

        if (pagador.enderecos.get(0).zonaLocalizacao.codigo == TipoLocalizacao.ZONA_RURAL.id) {
            endereco.setCep(Integer.parseInt(pagador.enderecos.get(1).cep.replace("-", "")));
            endereco.setComplemento(pagador.enderecos.get(1).logradouro);
            endereco.setNumero(pagador.enderecos.get(1).numero);
        } else {
            endereco.setCep(Integer.parseInt(pagador.enderecos.get(0).cep.replace("-", "")));
            endereco.setComplemento(pagador.enderecos.get(0).logradouro);
            endereco.setNumero(pagador.enderecos.get(0).numero);
        }

        return endereco;

    }

    private static EmitenteVO setarEmitente(br.ufla.lemaf.beans.pessoa.Pessoa pagador, EnderecoVO endereco) {

        String cpfCnpjFormatado;
        Integer tipoDocumento;

        if (Pessoa.isPessoaFisicaEU(pagador)) {

            cpfCnpjFormatado = Pessoa.getCpfCnpjPessoaEU(pagador);
            tipoDocumento = TIPO_DOCUMENTO_CPF;

        } else {

            cpfCnpjFormatado = Pessoa.getCpfCnpjPessoaEU(pagador);
            tipoDocumento = TIPO_DOCUMENTO_CNPJ;

        }

        EmitenteVO emitente = new EmitenteVO();
        emitente.setEndereco(endereco);
        emitente.setNome(Pessoa.getNomeRazaoSocialPessoaEU(pagador));
        emitente.setNumeroDocumento(cpfCnpjFormatado);
        emitente.setTipoDocumento(tipoDocumento);

        return emitente;
    }

    private static DadosVO setarDadosTaxaExpediente(BigDecimal valor, Date dataVencimento, EmitenteVO emitente, Long codigoLicenca) {

        DateFormat dfVencimento = new SimpleDateFormat("dd/MM/yyyy");
        String dataVencimentoFormatada = dfVencimento.format(dataVencimento);

        DateFormat dfReferencia = new SimpleDateFormat("MM/yyyy");
        String dataReferenciaFormatada = dfReferencia.format(new Date());

        DadosVO dados = new DadosVO();
        dados.setEmitente(emitente);
        dados.setReferencia(dataReferenciaFormatada);
        dados.setReceita(Integer.parseInt(SEFAZ_CODIGO_RECEITA));
//        dados.setTaxa(Integer.parseInt(SEFAZ_CODIGO_TAXA_EXPEDIENTE));

        if (checkCodigoLicencaSefaz(codigoLicenca)) {
            dados.setTaxa(Integer.parseInt(SEFAZ_CODIGO_TAXA_EXPEDIENTE_LICENCA));
        } else if (codigoLicenca.equals(TipoLicenca.DISPENSA_LICENCA_AMBIENTAL)) {
            dados.setTaxa(Integer.parseInt(SEFAZ_CODIGO_TAXA_EXPEDIENTE_DISPENSA));
        }

        dados.setDataPagamento(dataVencimentoFormatada);
        dados.setDataVencimento(dataVencimentoFormatada);
        dados.setQuantidade(Integer.parseInt(SEFAZ_QUANTIDADE_EMISSAO));
        dados.setValorPrincipal(valor);

        return dados;

    }

    private static DadosVO setarDadosTaxaLicenciamento(BigDecimal valor, Date dataVencimento, EmitenteVO emitente, Long codigoLicenca) {

        DateFormat dfVencimento = new SimpleDateFormat("dd/MM/yyyy");
        String dataVencimentoFormatada = dfVencimento.format(dataVencimento);

        DateFormat dfReferencia = new SimpleDateFormat("MM/yyyy");
        String dataReferenciaFormatada = dfReferencia.format(new Date());

        DadosVO dados = new DadosVO();
        dados.setEmitente(emitente);
        dados.setReferencia(dataReferenciaFormatada);
        dados.setReceita(Integer.parseInt(SEFAZ_CODIGO_RECEITA));

        if (codigoLicenca.equals(TipoLicenca.LICENCA_PREVIA)) {
            dados.setTaxa(Integer.parseInt(SEFAZ_CODIGO_TAXA_LICENCA_PREVIA));
        } else if (codigoLicenca.equals(TipoLicenca.LICENCA_INSTALACAO)) {
            dados.setTaxa(Integer.parseInt(SEFAZ_CODIGO_TAXA_LICENCA_INSTALACAO));
        } else if (codigoLicenca.equals(TipoLicenca.LICENCA_OPERACAO)) {
            dados.setTaxa(Integer.parseInt(SEFAZ_CODIGO_TAXA_LICENCA_OPERACAO));
        } else if (codigoLicenca.equals(TipoLicenca.RENOVACAO_LICENCA_DE_INSTALACAO)) {
            dados.setTaxa(Integer.parseInt(SEFAZ_CODIGO_TAXA_RENOVACAO_LICENCA_INSTALACAO));
        } else if (codigoLicenca.equals(TipoLicenca.RENOVACAO_LICENCA_DE_OPERACAO)) {
            dados.setTaxa(Integer.parseInt(SEFAZ_CODIGO_TAXA_RENOVACAO_LICENCA_OPERACAO));
        }

        dados.setQuantidade(Integer.parseInt(SEFAZ_QUANTIDADE_EMISSAO));
        dados.setValorPrincipal(valor);
        dados.setDataPagamento(dataVencimentoFormatada);
        dados.setDataVencimento(dataVencimentoFormatada);

        return dados;

    }

    private static Boolean checkCodigoLicencaSefaz(Long codigoLicencaSefaz) {

        if ((codigoLicencaSefaz.equals(TipoLicenca.LICENCA_PREVIA)) ||
                (codigoLicencaSefaz.equals(TipoLicenca.LICENCA_INSTALACAO)) ||
                (codigoLicencaSefaz.equals(TipoLicenca.LICENCA_OPERACAO)) ||
                (codigoLicencaSefaz.equals(TipoLicenca.RENOVACAO_LICENCA_DE_INSTALACAO)) ||
                (codigoLicencaSefaz.equals(TipoLicenca.RENOVACAO_LICENCA_DE_OPERACAO))) {

            return true;

        }

        return false;
    }

    public static Boolean islicencasPermitidas(Caracterizacao caracterizacao) {

        return caracterizacao.tipoLicenca.id.equals(TipoLicenca.LICENCA_PREVIA) || caracterizacao.tipoLicenca.id.equals(TipoLicenca.LICENCA_INSTALACAO) ||
                caracterizacao.tipoLicenca.id.equals(TipoLicenca.LICENCA_OPERACAO) || caracterizacao.tipoLicenca.id.equals(TipoLicenca.RENOVACAO_LICENCA_DE_INSTALACAO) ||
                caracterizacao.tipoLicenca.id.equals(TipoLicenca.RENOVACAO_LICENCA_DE_OPERACAO);

    }

}
