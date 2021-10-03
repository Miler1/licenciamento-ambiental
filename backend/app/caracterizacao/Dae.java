package models.caracterizacao;

import arrecadacao.dtos.RetornoArrecadacaoDTO;
import arrecadacao.enuns.CondicaoArrecadacaoEnum;
import models.Documento;
import models.Empreendimento;
import models.Pessoa;
import models.TipoDocumento;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import play.Logger;
import play.db.jpa.GenericModel;
import sefaz.VO.processamentoDAE.DaePagoVO;
import sefaz.VO.retorno.DarRetornoVO;
import utils.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "dae")
public class Dae extends GenericModel {

	public enum Status { NAO_EMITIDO, EMITIDO, ERRO_EMISSAO, PAGO, VENCIDO, VENCIDO_AGUARDANDO_PAGAMENTO, VENCIDO_AGUARDANDO_EMISSAO }

	private static final String SEQ = "licenciamento.dae_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@OneToOne
	@JoinColumn(name = "id_documento")
	public Documento documento;

	public Double valor;

	@OneToOne
	@JoinColumn(name = "id_caracterizacao", referencedColumnName = "id", nullable = false)
	public Caracterizacao caracterizacao;

	public String numero;

	public Date competencia;

	@Column(name = "data_cadastro")
	public Date dataCadastro;

	@Column(name = "data_emissao")
	public Date dataEmissao;

	@Column(name = "data_vencimento")
	public Date dataVencimento;

	@Column(name = "cpf_cnpj_contribuinte")
	public String cpfCnpjContribuinte;

	@Enumerated(EnumType.ORDINAL)
	public Status status;

	@Column(name = "erro_emissao")
	public String erroEmissao;

	@Column(name = "data_pagamento")
	public Date dataPagamento;

	@Transient
	public String mensagemErro;

	@OneToMany(mappedBy = "dae")
	public List<DocumentoArrecadacao> documentosArrecadacao;

	public Dae() {}

	public Dae(Caracterizacao caracterizacao) {

		this.caracterizacao = caracterizacao;
		this.cpfCnpjContribuinte = Pessoa.getCpfCnpjPessoaEU(caracterizacao.empreendimento.empreendimentoEU.pessoa);

	}

	@Override
	public Dae save() {

		if (this.id != null)
			throw new IllegalStateException("Dae já salvo no banco de dados.");

		this.dataCadastro = new Date();
		this.documento = null;
		this.numero = null;
		this.dataEmissao = null;
		this.dataVencimento = null;
		this.status = Status.NAO_EMITIDO;
		this.erroEmissao = null;

		super.save();

		return this;
	}

	public Boolean isIsento() {
		return this.caracterizacao.empreendimento.isIsento();
	}

	public void emitirApartirDoGestaoPagamentos() {

		jaEmitido();

        Date dataEmissao = new Date();
        Date dataVencimento = DateUtils.addDays(dataEmissao, Configuracoes.DAE_LICENCA_DIAS_VENCIMENTO);
        dataVencimento = DateUtils.setMinutes(dataVencimento, 59);
        dataVencimento = DateUtils.setSeconds(dataVencimento, 59);


        try {

            Empreendimento empreendimento = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(this.caracterizacao.empreendimento.cpfCnpj);
            RetornoArrecadacaoDTO retorno = WebServiceGestaoPagamentos.getInstace().gerarDae(BigDecimal.valueOf(this.valor), empreendimento.empreendimentoEU.pessoa, dataVencimento);

            this.competencia = dataEmissao;
            this.dataEmissao = dataEmissao;
            this.dataVencimento = retorno.dataVecimento;
            this.status = Status.EMITIDO;
            this.erroEmissao = null;

            this.documento = new Documento();
            this.documento.tipo = TipoDocumento.findById(
                    TipoDocumento.DOCUMENTO_ARRECADACAO_ESTADUAL);

            this.documento.base64 = retorno.documento.documentoBase64;

            DocumentoArrecadacao documentoArrecadacao = new DocumentoArrecadacao();
            documentoArrecadacao.dae = this;
            documentoArrecadacao.idDocumentoArrecadacao = retorno.idDocumentoArrecadacao;
            documentoArrecadacao.save();

            this.documento.extensao = "pdf";
            this.documento.save();

        } catch (Exception e) {

            e.printStackTrace();
			Logger.error(e.getMessage());
            saveErroEmissao(e);
        }

        super.save();

    }

	/**
	 * SEFAZ é um órgão do Ámapa responsável por geração de boletos
	 */
	public void emitirApartirDaSEFAZ() {

		jaEmitido();

		Date dataEmissao = new Date();
		Date dataVencimento = DateUtils.setDays(dataEmissao, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
		dataVencimento = DateUtils.setHours(dataVencimento, 23);
		dataVencimento = DateUtils.setMinutes(dataVencimento, 59);
		dataVencimento = DateUtils.setSeconds(dataVencimento, 59);

		try {

			Empreendimento empreendimento = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(this.caracterizacao.empreendimento.cpfCnpj);
			String retorno = WebServiceSefaz.getInstace().gerarDaeArrecadacaoTaxaExpediente(BigDecimal.valueOf(this.valor), empreendimento.empreendimentoEU.pessoa, dataVencimento, this.caracterizacao.tipoLicenca.id);

			if(retorno.contains("<dar:erro>")) {

				String texto[];
				String codErro = StringUtils.substringBetween(retorno, "<dar:codigo>", "</dar:codigo>");
				String msgErro = codErro + " - " + StringUtils.substringBetween(retorno, "<dar:mensagem>", "</dar:mensagem>");
				texto = msgErro.split(" - ");
				this.mensagemErro = texto[1];
				Logger.error(msgErro);
				throw new Exception(msgErro);

			}

			DarRetornoVO darRetorno = new DarRetornoVO(retorno);

			this.competencia = dataEmissao;
			this.dataEmissao = dataEmissao;
			this.dataVencimento = dataVencimento;
			this.status = Status.EMITIDO;
			this.erroEmissao = null;

			this.documento = new Documento();
			this.documento.tipo = TipoDocumento.findById(
					TipoDocumento.DOCUMENTO_ARRECADACAO_ESTADUAL);

			this.documento.base64 = darRetorno.dados.pdf;

			DocumentoArrecadacao documentoArrecadacao = new DocumentoArrecadacao();
			documentoArrecadacao.dae = this;
			documentoArrecadacao.nossoNumero = darRetorno.dados.nossoNumero;
			documentoArrecadacao.save();

			this.documento.extensao = "pdf";
			this.documento.save();

		} catch (Exception e) {

			e.printStackTrace();
			Logger.error(ExceptionUtils.getFullStackTrace(e));
			saveErroEmissao(e);

		}

		super.save();

	}

	private void jaEmitido() {

		if (this.status != Status.NAO_EMITIDO
				&& this.status != Status.ERRO_EMISSAO
				&& this.status != Status.VENCIDO_AGUARDANDO_EMISSAO
				&& this.status != Status.VENCIDO_AGUARDANDO_PAGAMENTO) {
			throw new IllegalStateException("Este DAE já foi emitido");
		}

	}

	/**
	 * Armazena erro após tentativa de emissão do DAE.
	 */
	private void saveErroEmissao(Exception e) {

		this.status = Status.ERRO_EMISSAO;
		this.erroEmissao = ExceptionUtils.getFullStackTrace(e);

		super.save();

		if (this.documento != null)
			this.documento.delete();
	}

	public void processarPagamento() {

		List<RetornoArrecadacaoDTO> listaArrecadacoes = new ArrayList<>();
		for(DocumentoArrecadacao documentoArrecadacao : this.documentosArrecadacao) {
			RetornoArrecadacaoDTO retornoArrecadacaoDTO = obterRegistroPagamento(documentoArrecadacao.idDocumentoArrecadacao);
			if(retornoArrecadacaoDTO != null) {
				listaArrecadacoes.add(retornoArrecadacaoDTO);
			}
		}

		if (!listaArrecadacoes.isEmpty()){

			List<CondicaoArrecadacaoEnum> condicoesArrecadacaoEnum = new ArrayList<>();
			for(RetornoArrecadacaoDTO retornoArrecadacaoDTO : listaArrecadacoes) {
				CondicaoArrecadacaoEnum condicaoArrecadacaoEnum = CondicaoArrecadacaoEnum.valueOf(retornoArrecadacaoDTO.condicao.codigo);
				condicoesArrecadacaoEnum.add(condicaoArrecadacaoEnum);
			}

			StatusCaracterizacao statusCaracterizacaoAguardandoQuitacao = StatusCaracterizacao.findById(StatusCaracterizacao.AGUARDANDO_QUITACAO_TAXA_EXPEDIENTE);
			StatusCaracterizacao statusCaracterizacaoVencidoAguardandoPagamento = StatusCaracterizacao.findById(StatusCaracterizacao.VENCIDO_AGUARDANDO_PAGAMENTO_TAXA_EXPEDIENTE);
			StatusCaracterizacao statusCaracterizacaoVencidoAguardandoEmissao = StatusCaracterizacao.findById(StatusCaracterizacao.VENCIDO_AGUARDANDO_EMISSAO_TAXA_EXPEDIENTE);

			if (condicoesArrecadacaoEnum.contains(CondicaoArrecadacaoEnum.PAGO)) {

				this.status = status.PAGO;
				this.dataPagamento = new Date();
				this._save();

				if(this.caracterizacao.status.equals(statusCaracterizacaoAguardandoQuitacao)
						|| this.caracterizacao.status.equals(statusCaracterizacaoVencidoAguardandoPagamento)
						|| this.caracterizacao.status.equals(statusCaracterizacaoVencidoAguardandoEmissao)) {
					processamentoDeferimentoDAE();
				}
			}
			else if (condicoesArrecadacaoEnum.contains(CondicaoArrecadacaoEnum.AGUARDANDO_PAGAMENTO)) {

				this.status = status.EMITIDO;

				if(this.caracterizacao.status.equals(statusCaracterizacaoVencidoAguardandoPagamento)
						|| this.caracterizacao.status.equals(statusCaracterizacaoVencidoAguardandoEmissao)) {
					this.caracterizacao.status = statusCaracterizacaoAguardandoQuitacao;
				}
			}
			else if (condicoesArrecadacaoEnum.contains(CondicaoArrecadacaoEnum.VENCIDO_AGUARDANDO_PAGAMENTO)) {

				this.status = status.VENCIDO_AGUARDANDO_PAGAMENTO;

				if(this.caracterizacao.status.equals(statusCaracterizacaoAguardandoQuitacao)
						|| this.caracterizacao.status.equals(statusCaracterizacaoVencidoAguardandoEmissao)) {
					this.caracterizacao.status = statusCaracterizacaoVencidoAguardandoPagamento;
				}
			}
			else if (condicoesArrecadacaoEnum.contains(CondicaoArrecadacaoEnum.VENCIDO)) {

				this.status = status.VENCIDO_AGUARDANDO_EMISSAO;

				if(this.caracterizacao.status.equals(statusCaracterizacaoAguardandoQuitacao)
						|| this.caracterizacao.status.equals(statusCaracterizacaoVencidoAguardandoPagamento)) {
					this.caracterizacao.status = statusCaracterizacaoVencidoAguardandoEmissao;
				}
			}

			this._save();
			this.caracterizacao._save();

		}

	}

	public void processarPagamentosefaz(List<DaePagoVO> daePagoVOList) {

		StatusCaracterizacao statusCaracterizacaoAguardandoQuitacao = StatusCaracterizacao.findById(StatusCaracterizacao.AGUARDANDO_QUITACAO_TAXA_EXPEDIENTE);
		StatusCaracterizacao statusCaracterizacaoVencidoAguardandoPagamento = StatusCaracterizacao.findById(StatusCaracterizacao.VENCIDO_AGUARDANDO_PAGAMENTO_TAXA_EXPEDIENTE);
		StatusCaracterizacao statusCaracterizacaoVencidoAguardandoEmissao = StatusCaracterizacao.findById(StatusCaracterizacao.VENCIDO_AGUARDANDO_EMISSAO_TAXA_EXPEDIENTE);

		Boolean daePago = false;

		for(DocumentoArrecadacao documentoArrecadacao : this.documentosArrecadacao) {
			daePago = verificaPagamento(documentoArrecadacao.nossoNumero, daePagoVOList) ? true : daePago;
		}

		if(daePago) {

			this.status = status.PAGO;
			this.dataPagamento = new Date();
			this._save();

			if(this.caracterizacao.status.equals(statusCaracterizacaoAguardandoQuitacao)
					|| this.caracterizacao.status.equals(statusCaracterizacaoVencidoAguardandoPagamento)
					|| this.caracterizacao.status.equals(statusCaracterizacaoVencidoAguardandoEmissao)) {
				processamentoDeferimentoDAE();
			}

		} else {

			Date dataAtual = DataUtils.tirarHoraData(new Date());
			Date dataVencimento = DataUtils.tirarHoraData(this.dataVencimento);

			if(dataAtual.equals(dataVencimento) || dataAtual.after(dataVencimento)) {

				this.status = status.VENCIDO_AGUARDANDO_EMISSAO;

				if(this.caracterizacao.status.equals(statusCaracterizacaoAguardandoQuitacao)
						|| this.caracterizacao.status.equals(statusCaracterizacaoVencidoAguardandoPagamento)) {
					this.caracterizacao.status = statusCaracterizacaoVencidoAguardandoEmissao;
				}

			} else {

				this.status = status.EMITIDO;

				if(this.caracterizacao.status.equals(statusCaracterizacaoVencidoAguardandoPagamento)
						|| this.caracterizacao.status.equals(statusCaracterizacaoVencidoAguardandoEmissao)) {
					this.caracterizacao.status = statusCaracterizacaoAguardandoQuitacao;
				}

			}

		}

		this._save();
		this.caracterizacao._save();

	}

	private Boolean verificaPagamento(String nossoNumero, List<DaePagoVO> daePagoVOList) {

		for(DaePagoVO daePagoVO : daePagoVOList) {
			if(daePagoVO.nossoNumero.equals(nossoNumero)) return true;
		}

		return false;

	}

	public void processaPagamentoSemVerificarGestaoPagamentos() {

		StatusCaracterizacao statusCaracterizacaoAguardandoQuitacao = StatusCaracterizacao.findById(StatusCaracterizacao.AGUARDANDO_QUITACAO_TAXA_EXPEDIENTE);
		StatusCaracterizacao statusCaracterizacaoVencidoAguardandoPagamento = StatusCaracterizacao.findById(StatusCaracterizacao.VENCIDO_AGUARDANDO_PAGAMENTO_TAXA_EXPEDIENTE);
		StatusCaracterizacao statusCaracterizacaoVencidoAguardandoEmissao = StatusCaracterizacao.findById(StatusCaracterizacao.VENCIDO_AGUARDANDO_EMISSAO_TAXA_EXPEDIENTE);

		this.status = status.PAGO;
		this.dataPagamento = new Date();
		this._save();

		if(this.caracterizacao.status.equals(statusCaracterizacaoAguardandoQuitacao)
				|| this.caracterizacao.status.equals(statusCaracterizacaoVencidoAguardandoPagamento)
				|| this.caracterizacao.status.equals(statusCaracterizacaoVencidoAguardandoEmissao)) {

			processamentoDeferimentoDAE();
		}

		this._save();
		this.caracterizacao._save();

	}

	private RetornoArrecadacaoDTO obterRegistroPagamento(Integer idDocumentoArrecadacao) {

		try {

		    return WebServiceGestaoPagamentos.getInstace().obterRegistroPagamento(idDocumentoArrecadacao);

		} catch (Exception e) {

			Logger.info(e, "Erro ao verificar pagamento do DAE " + this.id);
			return null;
		}

	}

	public static List<Dae> findByStatus(Status status) {

		return find("status = :status")
				.setParameter("status", status)
				.fetch();

	}

	public static Dae findByCaracterizacao(Caracterizacao caracterizacao) {

		return find("caracterizacao.id = :idCaracterizacao")
				.setParameter("idCaracterizacao", caracterizacao.id)
				.first();

	}

	public void processamentoDeferimentoDAE(){

		// Apenas agenda se for DI, o envio das Licenças será feito ao retornar do Análise.
		if(this.caracterizacao.tipoLicenca.id.equals(TipoLicenca.DISPENSA_LICENCA_AMBIENTAL)) {
			ComunicacaoDashboard comunicacaoDashboard = new ComunicacaoDashboard(this.caracterizacao);
			comunicacaoDashboard.save();
			ComunicacaoRedeSimples comunicacaoRedeSimples = new ComunicacaoRedeSimples(this.caracterizacao);
			comunicacaoRedeSimples.save();
			StatusCaracterizacao statusCaracterizacaoDeferido = StatusCaracterizacao.findById(StatusCaracterizacao.DEFERIDO);
			this.caracterizacao.status = statusCaracterizacaoDeferido;
		}
		else if(this.caracterizacao.tipoLicenca.id.equals(TipoLicenca.LICENCA_PREVIA)
				|| this.caracterizacao.tipoLicenca.id.equals(TipoLicenca.LICENCA_INSTALACAO)
				|| this.caracterizacao.tipoLicenca.id.equals(TipoLicenca.LICENCA_OPERACAO)
				|| this.caracterizacao.tipoLicenca.id.equals(TipoLicenca.LICENCA_AMBIENTAL_UNICA)
				|| this.caracterizacao.tipoLicenca.id.equals(TipoLicenca.RENOVACAO_LICENCA_DE_INSTALACAO)
				|| this.caracterizacao.tipoLicenca.id.equals(TipoLicenca.RENOVACAO_LICENCA_DE_OPERACAO)
				|| this.caracterizacao.tipoLicenca.id.equals(TipoLicenca.ATUALIZACAO_DE_LICENCA_PREVIA)
				|| this.caracterizacao.tipoLicenca.id.equals(TipoLicenca.CADASTRO_AQUICULTURA)
				|| this.caracterizacao.tipoLicenca.id.equals(TipoLicenca.RENOVACAO_LICENCA_AMBIENTAL_UNICA)
		) {
			StatusCaracterizacao statusCaracterizacaoEmAnalise = StatusCaracterizacao.findById(StatusCaracterizacao.EM_ANALISE);
			this.caracterizacao.status = statusCaracterizacaoEmAnalise;
		}
	}

}
