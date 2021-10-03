package models.caracterizacao;

import br.ufla.lemaf.beans.pessoa.Contato;
import br.ufla.lemaf.beans.pessoa.Endereco;
import br.ufla.lemaf.beans.pessoa.TipoContato;
import br.ufla.lemaf.enums.TipoEndereco;
import com.vividsolutions.jts.geom.Geometry;
import exceptions.AppException;
import jdk.nashorn.internal.ir.annotations.Immutable;
import models.Documento;
import models.Pessoa;
import models.TipoDocumento;
import models.analise.LicencaAnalise;
import models.analise.ParecerAnalistaTecnico;
import models.integracao.GeometriaNumeroLicenca;
import models.pdf.PDFGenerator;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.libs.Crypto;
import utils.*;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(schema = "licenciamento", name = "licenca")
public class Licenca extends GenericModel {

	private static final String SEQ = "licenciamento.licenca_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_caracterizacao", referencedColumnName = "id", nullable = false)
	public Caracterizacao caracterizacao;
	
	@Column(name = "data_cadastro")
	public Date dataCadastro;
	
	@OneToOne
	@JoinColumn(name = "id_documento")
	public Documento documento;
	
	public String numero;
	
	@Column(name = "data_validade")
	public Date dataValidade;

	//@ManyToOne
	//@JoinColumn(name="id_licenca_analise")
	@Transient
	public LicencaAnalise licencaAnalise;

	@OneToOne
	@JoinColumn(name="id_licenca_anterior")
	public Licenca licencaAnterior;
	
	@Column(name = "data_validade_prorrogada")
	public Date dataValidadeProrrogada;

	public Boolean ativo;

	public Boolean prorrogacao;
	
	public Licenca(Caracterizacao caracterizacao) {
		
		this.caracterizacao = caracterizacao;
		
	}
	
	public Licenca(String numero, Date dataCadastro, Caracterizacao caracterizacao) {
		this.dataCadastro = dataCadastro;
		this.numero = numero;
		this.caracterizacao = caracterizacao;
	}

		
	public void gerar() {
		
		this.dataCadastro = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(this.dataCadastro);
		
		c.add(Calendar.YEAR, this.caracterizacao.vigenciaSolicitada);
		this.dataValidade = c.getTime();
		
		this.ativo = true;
		
		this.save();
		
		this.gerarNumero();
		
		this.save();
		
	}


	public Documento gerarPDF() throws Exception {

		TipoDocumento tipoDocumento = this.caracterizacao.tipoLicenca.findTipoDocumento();

		String url = Configuracoes.APP_URL + "licenca/" + Crypto.encryptAES(this.numero);

//		this.caracterizacao.documentoMinuta = this.caracterizacao.getDadosMinutaLicenca(this.caracterizacao);

		if(this.caracterizacao.notificacao != null && this.caracterizacao.notificacao.analiseTecnica != null &&
				this.caracterizacao.notificacao.analiseTecnica.pareceresAnalistaTecnico.size() > 0 &&
				this.caracterizacao.parecerAnalistaTecnico.tipoResultadoAnalise.id.equals(1L) ){
			this.caracterizacao.parecerAnalistaTecnico = this.caracterizacao.notificacao.analiseTecnica.pareceresAnalistaTecnico.get(0);
		}else{

			List<ParecerAnalistaTecnico> listaPareceres = ParecerAnalistaTecnico.find(
			"SELECT a FROM ParecerAnalistaTecnico a " +
					"JOIN a.analiseTecnica b " +
					"JOIN b.analise c " +
					"JOIN c.processo d " +
					"WHERE d.numero = :idNumero ORDER BY a.id ASC")
			.setParameter("idNumero", caracterizacao.numero).fetch();

			if (listaPareceres.size() > 0) {
				this.caracterizacao.parecerAnalistaTecnico = listaPareceres.get(listaPareceres.size()-1);

			}
		}

		this.caracterizacao.inicializaParametros();
		this.caracterizacao.cadastrante = Pessoa.convert(WebServiceEntradaUnica.findPessoaByCpfCnpjEU(this.caracterizacao.cpfCnpjCadastrante));

		String telefone = null;

		if (this.caracterizacao.empreendimento.empreendimentoEU.empreendedor.pessoa.contatos.size() > 0) {
			Contato telefoneEmpreededor= this.caracterizacao.empreendimento.empreendimentoEU.empreendedor.pessoa.contatos.stream().filter(contato -> contato.tipo.id == TipoContato.ID_TELEFONE_CELULAR ||
					contato.tipo.id == TipoContato.ID_TELEFONE_COMERCIAL ||
					contato.tipo.id == TipoContato.ID_TELEFONE_RESIDENCIAL).findFirst().orElse(null);

			telefone = telefoneEmpreededor == null ? "-" : telefoneEmpreededor.valor;

		}
		Geometry geo = GeoJsonUtils.toGeometry(this.caracterizacao.empreendimento.empreendimentoEU.localizacao.geometria);

		//Endereços
		Endereco enderecoPrincipalEmpreendimento = this.caracterizacao.empreendimento.empreendimentoEU.enderecos.stream().filter(endereco -> endereco.tipo.id == TipoEndereco.ID_PRINCIPAL).findFirst().orElseThrow(null);
		Endereco enderecoCorrespEmpreendimento = this.caracterizacao.empreendimento.empreendimentoEU.enderecos.stream().filter(endereco -> endereco.tipo.id == TipoEndereco.ID_CORRESPONDENCIA).findFirst().orElseThrow(null);
		Endereco enderecoPrincipalEmpreendedor = this.caracterizacao.empreendimento.empreendimentoEU.empreendedor.pessoa.enderecos.stream().filter(endereco -> endereco.tipo.id == TipoEndereco.ID_PRINCIPAL).findFirst().orElseThrow(null);
		Endereco enderecoCorrespEmpreendedor = this.caracterizacao.empreendimento.empreendimentoEU.empreendedor.pessoa.enderecos.stream().filter(endereco -> endereco.tipo.id == TipoEndereco.ID_CORRESPONDENCIA).findFirst().orElseThrow(null);


		PDFGenerator pdf = new PDFGenerator()
				.setTemplate(tipoDocumento.getPdfTemplate())
				.addParam("licenca", this)
				.addParam("telefone", telefone)
				.addParam("geometry", geo)
				.addParam("enderecoPrincipalEmpreendimento",enderecoPrincipalEmpreendimento)
				.addParam("enderecoCorrespEmpreendimento",enderecoCorrespEmpreendimento)
				.addParam("enderecoPrincipalEmpreendedor",enderecoPrincipalEmpreendedor)
				.addParam("enderecoCorrespEmpreendedor",enderecoCorrespEmpreendedor)
				.addParam("qrcode", new QRCode(url).getBase64())
				.addParam("parecer",this.caracterizacao.parecerAnalistaTecnico);

		pdf.generate();

		Documento documento = new Documento(tipoDocumento, pdf.getFile());
		documento.save();

		return documento;
	}

	private String lpad(String numero, String sufixo, String separador){

		String[] nums = numero.split("/");

		if (sufixo!=null){
			if(sufixo.contains("/")) {
				String[] tmp = sufixo.split("/");
				if(tmp.length > 0){
					return nums[0] + separador + String.format("%02d",Integer.parseInt(tmp != null && !tmp[0].contains("/") ? tmp[0] : "0") + 1) + "/" + tmp[1];
				}else{
					return nums[0] + separador + String.format("%02d",Integer.parseInt(tmp != null && !tmp[0].contains("/") ? tmp[0] : "0") + 1);
				}
			}
		}

		return nums[0] + separador + String.format("%02d",  Integer.parseInt(sufixo == null ? "0" : sufixo) + 1) + "/" + nums[1];
	}
	
	
	private void gerarNumero() {

		if(this.caracterizacao == null || !this.caracterizacao.ativo)
			throw new IllegalStateException("Licenca não possui caracterização.");

		if (this.id == null)
			throw new IllegalStateException("Licença não salva.");

		if(this.caracterizacao.renovacao && this.caracterizacao.tipoLicenca.isRenovacao()){
			Caracterizacao c =  Caracterizacao.findById(this.caracterizacao.idCaracterizacaoOrigem);
			String[] numeros = c.licencas.get(0).numero.split("-");
			this.numero = lpad(numeros[0], numeros.length > 1 ? numeros[1] : null,"-");
		} else {
			this.numero = String.format("%06d", this.id) + "/" + Calendar.getInstance().get(Calendar.YEAR);
		}
	}
	
	public static Licenca findByNumero(String numero) {
		
		return Licenca.find("byNumero", numero).first();
		
	}
	
	public static List<GeometriaNumeroLicenca> getLicencaEmitidaImovel(String codigoImovel, Long idTipoLicenca, Long idAtividade, Long idTipologia) {
		
		Query query = JPA.em().createQuery("select new models.integracao.GeometriaNumeroLicenca(li.numero, ga.geometria) from "
				+ "Licenca as li, GeometriaAtividade as ga, ImovelEmpreendimento as ie "
				+ "join li.caracterizacao.atividadesCaracterizacao atividadeCaracterizacao "
				+ "where "
				+ "li.caracterizacao.id = ga.atividadeCaracterizacao.caracterizacao.id and "
				+ "li.caracterizacao.tipoLicenca.id = :idTipoLicenca and "
				+ "atividadeCaracterizacao.atividade.id = :idAtividade and "
				+ "atividadeCaracterizacao.atividade.tipologia.id = :idTipologia and "
				+ "li.caracterizacao.empreendimento.id = ie.empreendimento.id and "
				+ "li.ativo = true and "
				+ "ie.codigo = :codigoImovel")
					.setParameter("idTipoLicenca", idTipoLicenca)
					.setParameter("idAtividade", idAtividade)
					.setParameter("idTipologia", idTipologia)
					.setParameter("codigoImovel", codigoImovel);
		
		List<GeometriaNumeroLicenca> licencasLar = query.getResultList();
		
		return licencasLar;		
	}
	
	public static List<Licenca> getLicencaEmitidaEmpreendimento(Long idEmpreendimento, Long idTipoLicenca, Long idAtividade, Long idTipologia, Long idStatus) {
		
		Query query = JPA.em().createQuery("select new models.caracterizacao.Licenca(li.numero, li.dataCadastro, ca) from "
				+ "Licenca as li, Caracterizacao as ca "
				+ "join li.caracterizacao.atividadesCaracterizacao atividadeCaracterizacao "
				+ "where "
				+ "li.caracterizacao.id = ca.id and "
				+ "li.caracterizacao.tipoLicenca.id = :idTipoLicenca and "
				+ "li.caracterizacao.status.id = :status and "
				+ "atividadeCaracterizacao.atividade.id = :idAtividade and "
				+ "atividadeCaracterizacao.atividade.tipologia.id = :idTipologia and "
				+ "li.caracterizacao.empreendimento.id = :idEmpreendimento and "
				+ "li.ativo = true")
					.setParameter("idTipoLicenca", idTipoLicenca)
					.setParameter("idAtividade", idAtividade)
					.setParameter("idTipologia", idTipologia)
					.setParameter("idEmpreendimento", idEmpreendimento)
					.setParameter("status", idStatus);
		
		List<Licenca> licencasLO = query.getResultList();
		
		return licencasLO;
	}
	
	public static void gerarPdfLicencas(List<Long> idsLicencas) throws Exception {
		
		List<Licenca> licencas = Licenca.find("id IN :ids").setParameter("ids", idsLicencas).fetch();
		
		if (licencas.size() != idsLicencas.size()) {
			
			throw new AppException(Mensagem.NAO_FORAM_ENCONTRADAS_TODAS_LICENCAS);
		}
		
		for (Licenca licenca : licencas) {

			licenca.prorrogacao = false;
			gerarPdfLicenca(licenca);
		}
	}
	
	public static void gerarPdfLicenca(Licenca licenca) throws Exception {
		
		licenca.documento = licenca.gerarPDF();
		licenca.save();
		
	}
	
	public boolean isSuspensa() {
		return this.caracterizacao.status.id.equals(StatusCaracterizacao.SUSPENSO);
	}
	
	public boolean isCancelada() {
		return this.caracterizacao.status.id.equals(StatusCaracterizacao.CANCELADO);
	}

	public void prorrogar() throws Exception {

		this.prorrogacao = true;
		this.documento = this.gerarPDF();
		this.save();
	}

	public static void atualizarDataValidadeLicenca(Caracterizacao caracterizacao, Integer novaVigencia){

		Licenca licenca = Licenca.findByNumero(caracterizacao.id.toString());

		if (licenca == null){
			licenca = new Licenca(caracterizacao);
		}

		Calendar c = Calendar.getInstance();
		c.setTime(caracterizacao.dataCadastro);

		//subtrai vigencia antiga para renovar
		c.add(Calendar.YEAR, -caracterizacao.vigenciaSolicitada);
		//adicionar com nova vigencia
		c.add(Calendar.YEAR, novaVigencia);

		licenca.dataValidade = c.getTime();
	}


	public static void finalizarProrrogacao(List<Long> ids) throws Exception {

		List<Licenca> licencas = Licenca.find("SELECT l FROM Licenca l WHERE l.id IN :lista")
				.setParameter("lista", ids)
				.fetch();

		for (Licenca licenca : licencas) {

			licenca.prorrogacao = false;
			licenca.documento = licenca.gerarPDF();
			licenca.save();
		}

	}
}

