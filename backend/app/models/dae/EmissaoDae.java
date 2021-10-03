package models.dae;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.ufla.lemaf.beans.pessoa.Contato;
import br.ufla.lemaf.beans.pessoa.TipoContato;
import models.Endereco;
import models.Municipio;
import models.Pessoa;


public class EmissaoDae {

	private static final String PERIODO_REFERENCIA_FORMAT = "yyyyMM";
	private static final String DATA_FORMAT = "dd-MM-yyyy";
	
	private static final String TIPO_DOCUMENTO_CNPJ = "2";
	private static final String TIPO_DOCUMENTO_CPF = "3";
	
	private static final int MAX_ENDERECO_CONTRIBUINTE = 200;

	String periodoReferencia;
	String numeroDocumentoOrigem = "0";
	String valorPrincipal;
	String valorJuros = "0";
	String valorMulta = "0";
	String dataCalculo;
	String dataVencimento;
	String tipoDocumentoContribuinte;
	String numeroDocumentoContribuinte;
	String idMunicipioContribuinte;
	String nomeContribuinte;
	String telefoneContribuinte = "-";
	String enderecoContribuinte = "-";
	String informacoesAdicionais;

	
	public EmissaoDae comPeriodoReferencia(Date periodoReferencia) {
		
		this.periodoReferencia = new SimpleDateFormat(PERIODO_REFERENCIA_FORMAT)
			.format(periodoReferencia);
		
		return this;
	}
	
	public EmissaoDae comValorPrincipal(Double valor) {
		
		this.valorPrincipal = valor.toString();
		return this;
	}
	
	public EmissaoDae comDataCalculo(Date dataCalculo) {
		
		this.dataCalculo = new SimpleDateFormat(DATA_FORMAT).format(dataCalculo);
		return this;
	}
	
	public EmissaoDae comDataVencimento(Date dataVencimento) {
		
		this.dataVencimento = new SimpleDateFormat(DATA_FORMAT).format(dataVencimento);
		return this;
	}
	
	public EmissaoDae comContribuinte(Pessoa pessoa) {
		
		this.numeroDocumentoContribuinte = pessoa.getCpfCnpj();
		
		if (pessoa.isPessoaFisica()) {
			
			this.tipoDocumentoContribuinte = TIPO_DOCUMENTO_CPF;
			this.nomeContribuinte = pessoa.nome;
			
		} else {
			
			this.tipoDocumentoContribuinte = TIPO_DOCUMENTO_CNPJ;
			this.nomeContribuinte = pessoa.razaoSocial;
		}
		
		return this;
	}

	public EmissaoDae comContatoContribuinte(Contato contato) {

		if (contato == null)
			return this;

		if(!contato.tipo.equals(TipoContato.ID_EMAIL) && contato.valor != null) {
			this.telefoneContribuinte = contato.valor;
		}
		
		return this;
	}
	
	public EmissaoDae comMunicipio(Municipio municipio) {
		
		if (municipio.codigoTse == null) {
			
			throw new RuntimeException("Código do TSE não configurado para o município: " + 
					municipio.id + " - " + municipio.nome);
		}
		
		this.idMunicipioContribuinte = municipio.codigoTse.toString();
		
		return this;
	}
	
	public EmissaoDae comEnderecoContribuinte(Endereco endereco) {
		
		this.enderecoContribuinte = endereco.getDescricao(MAX_ENDERECO_CONTRIBUINTE);
		return this;
	}

	public EmissaoDae comInformacoesAdicionais(String info) {
		
		this.informacoesAdicionais = info;
		return this;
	}
}
