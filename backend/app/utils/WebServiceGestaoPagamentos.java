package utils;

import arrecadacao.dtos.DocumentoArrecadacaoDTO;
import arrecadacao.dtos.RetornoArrecadacaoDTO;
import arrecadacao.services.DocumentoArrecadacaoService;
import models.Pessoa;
import pessoa.dtos.BeneficiarioDTO;
import pessoa.dtos.EnderecoDTO;
import pessoa.dtos.PagadorDTO;

import java.math.BigDecimal;
import java.util.Date;

public class WebServiceGestaoPagamentos {

    private static WebServiceGestaoPagamentos instace;

    private DocumentoArrecadacaoService documentoArrecadacaoService;

    private BeneficiarioDTO beneficiario;

    private static final String CODIGO_BENEFICIARIO = Configuracoes.CODIGO_BENEFICIARIO;
    private static final String CODIGO_MODULO = Configuracoes.CODIGO_MODULO;
    private static final String URL_GESTAO_PAGAMENTOS = Configuracoes.URL_GESTAO_PAGAMENTOS;

    private WebServiceGestaoPagamentos() {

        this.documentoArrecadacaoService = new DocumentoArrecadacaoService(URL_GESTAO_PAGAMENTOS, CODIGO_MODULO);
        this.beneficiario = new BeneficiarioDTO();
        this.beneficiario.codigo = CODIGO_BENEFICIARIO;
    }

    public static WebServiceGestaoPagamentos getInstace() {

        if (instace == null) {

            instace = new WebServiceGestaoPagamentos();
        }

        return instace;
    }

    public RetornoArrecadacaoDTO gerarDae(BigDecimal valor, br.ufla.lemaf.beans.pessoa.Pessoa pagador, Date dataVencimento ) {

        DocumentoArrecadacaoDTO documentoArrecadacaoDTO = new DocumentoArrecadacaoDTO();

        documentoArrecadacaoDTO.beneficiario = beneficiario;
        documentoArrecadacaoDTO.valor = valor;
        documentoArrecadacaoDTO.dataVencimento = dataVencimento;

        setarPagador(pagador, documentoArrecadacaoDTO);

        return documentoArrecadacaoService.cadastraDocumentoArrecadacao(documentoArrecadacaoDTO);
    }

    private void setarPagador(br.ufla.lemaf.beans.pessoa.Pessoa pagador, DocumentoArrecadacaoDTO documentoArrecadacaoDTO) {

        String cpfCnpjFormatado;

        if (Pessoa.isPessoaFisicaEU(pagador)) {

            cpfCnpjFormatado = LicenciamentoUtils.formatarCpf(Pessoa.getCpfCnpjPessoaEU(pagador));
        } else {

            cpfCnpjFormatado = LicenciamentoUtils.formatarCnpj(Pessoa.getCpfCnpjPessoaEU(pagador));
        }

        documentoArrecadacaoDTO.pagador = new PagadorDTO();
        documentoArrecadacaoDTO.pagador.cpfCnpj = cpfCnpjFormatado;
        documentoArrecadacaoDTO.pagador.nome = Pessoa.getNomeRazaoSocialPessoaEU(pagador);

        setarEndereco(pagador, documentoArrecadacaoDTO);

    }

    private void setarEndereco(br.ufla.lemaf.beans.pessoa.Pessoa pagador, DocumentoArrecadacaoDTO documentoArrecadacaoDTO) {

        documentoArrecadacaoDTO.pagador.endereco = new EnderecoDTO();
        documentoArrecadacaoDTO.pagador.endereco.logradouro = pagador.enderecos.get(0).logradouro;
        documentoArrecadacaoDTO.pagador.endereco.municipio = pagador.enderecos.get(0).municipio.nome;
        documentoArrecadacaoDTO.pagador.endereco.cep = pagador.enderecos.get(0).cep.toString();
        documentoArrecadacaoDTO.pagador.endereco.numero = pagador.enderecos.get(0).numero.toString();
        documentoArrecadacaoDTO.pagador.endereco.bairro = pagador.enderecos.get(0).bairro;
        documentoArrecadacaoDTO.pagador.endereco.estado = pagador.enderecos.get(0).municipio.estado.sigla;
    }

    public RetornoArrecadacaoDTO obterRegistroPagamento(Integer idDocumentoArrecadacao) {

        return documentoArrecadacaoService.buscaDocumentoArrecadacaoPorId(idDocumentoArrecadacao);
    }
}
