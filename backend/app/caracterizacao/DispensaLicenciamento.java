package models.caracterizacao;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.*;

import br.ufla.lemaf.beans.pessoa.Contato;
import br.ufla.lemaf.beans.pessoa.Endereco;
import br.ufla.lemaf.beans.pessoa.TipoContato;
import br.ufla.lemaf.enums.TipoEndereco;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import exceptions.AppException;
import models.*;
import models.pdf.PDFGenerator;
import play.Logger;
import play.db.jpa.GenericModel;
import play.libs.Crypto;
import utils.*;

@Entity
@Table(schema = "licenciamento", name = "dispensa_licenciamento")
public class DispensaLicenciamento extends GenericModel {

	private static final String SEQ = "licenciamento.dispensa_licenciamento_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@Column(name = "data_cadastro")
	public Date dataCadastro;

	@Column(name = "informacao_adicional")
	public String informacaoAdicional;

	@OneToOne
	@JoinColumn(name = "id_documento")
	public Documento documento;

	@ManyToOne
	@JoinColumn(name = "id_caracterizacao", referencedColumnName = "id", nullable = false)
	public Caracterizacao caracterizacao;

	public String numero;

	@Column
	public boolean ativo;

	@Override
	public DispensaLicenciamento save() {

		this.dataCadastro = new Date();
		try {

			this._save();
			this.gerarNumero();

			this.documento = gerarPDF();
			this.ativo = true;
			return super.save();

		} catch (Exception e) {

			Logger.error(e, e.getMessage());
			throw new AppException(Mensagem.ERRO_PROCESSAR_DLA);
		}
	}

	public void reprocessarDLA() {

		try {

			this.gerarNumero();

			this.documento = gerarPDF();
			super.save();

		} catch (Exception e) {

			Logger.error(e, e.getMessage());
			throw new AppException(Mensagem.ERRO_PROCESSAR_DLA);
		}

	}

	private Documento gerarPDF() throws Exception {

		TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.DISPENSA_LICENCIAMENTO);

		String url = Configuracoes.APP_URL + "dla/" + Crypto.encryptAES(this.caracterizacao.id.toString());

		this.caracterizacao.empreendimento = getEmpreendimentoEU(this.caracterizacao.empreendimento.cpfCnpj);

		String telefone = null;

		if (this.caracterizacao.empreendimento.empreendimentoEU.pessoa.contatos.size() > 0) {
			Contato telefoneEmpreendimento = this.caracterizacao.empreendimento.empreendimentoEU.pessoa.contatos.stream().filter(contato -> contato.tipo.id == TipoContato.ID_TELEFONE_CELULAR ||
					contato.tipo.id == TipoContato.ID_TELEFONE_COMERCIAL ||
					contato.tipo.id == TipoContato.ID_TELEFONE_RESIDENCIAL).findFirst().orElse(null);

			telefone = telefoneEmpreendimento == null ? "-" : telefoneEmpreendimento.valor;

		}
		Endereco enderecoPrincipal = this.caracterizacao.empreendimento.empreendimentoEU.pessoa.enderecos.stream().filter(endereco -> endereco.tipo.id == TipoEndereco.ID_PRINCIPAL).findFirst().orElseThrow(null);

		String numero = (enderecoPrincipal.numero != null) ? String.valueOf(enderecoPrincipal.numero) : "S/N";

		String enderecoCompleto = enderecoPrincipal.logradouro +", "+ numero+", "+enderecoPrincipal.bairro;

		PDFGenerator pdf = new PDFGenerator()
				.setTemplate(tipoDocumento.getPdfTemplate())
				.addParam("dispensa", this)
				.addParam("qrcode", new QRCode(url).getBase64())
				.addParam("enderecoPrincipal", enderecoPrincipal)
				.addParam("enderecoCompleto",enderecoCompleto)
				.addParam("telefone", telefone)
				.setPageSize(21.0D, 30.0D, 0.5D, 0.5D, 1.5D, 1.5D);

		pdf.generate();

		Documento documento = new Documento(tipoDocumento, pdf.getFile());
		documento.save();

		return documento;

	}

	public File regerarPdf() throws Exception {

		TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.DISPENSA_LICENCIAMENTO);

		String url = Configuracoes.APP_URL + "dla/" + Crypto.encryptAES(this.caracterizacao.id.toString());

		this.caracterizacao.empreendimento = getEmpreendimentoEU(this.caracterizacao.empreendimento.cpfCnpj);

		String telefone = null;

		if (this.caracterizacao.empreendimento.empreendimentoEU.pessoa.contatos.size() > 0) {
			Contato telefoneEmpreendimento = this.caracterizacao.empreendimento.empreendimentoEU.pessoa.contatos.stream().filter(contato -> contato.tipo.id == TipoContato.ID_TELEFONE_CELULAR ||
					contato.tipo.id == TipoContato.ID_TELEFONE_COMERCIAL ||
					contato.tipo.id == TipoContato.ID_TELEFONE_RESIDENCIAL).findFirst().orElse(null);

			telefone = telefoneEmpreendimento == null ? "-" : telefoneEmpreendimento.valor;

		}

		Endereco enderecoPrincipal = this.caracterizacao.empreendimento.empreendimentoEU.pessoa.enderecos.stream().filter(endereco -> endereco.tipo.id == TipoEndereco.ID_PRINCIPAL).findFirst().orElseThrow(null);

		String numero = (enderecoPrincipal.numero != null) ? String.valueOf(enderecoPrincipal.numero) : "S/N";

		String enderecoCompleto = enderecoPrincipal.logradouro +", "+ numero + ", "+enderecoPrincipal.bairro;

		PDFGenerator pdf = new PDFGenerator()
				.setTemplate(tipoDocumento.getPdfTemplate())
				.addParam("dispensa", this)
				.addParam("qrcode", new QRCode(url).getBase64())
				.addParam("enderecoPrincipal", enderecoPrincipal)
				.addParam("enderecoCompleto",enderecoCompleto)
				.addParam("telefone", telefone)
				.setPageSize(21.0D, 30.0D, 0.5D, 0.5D, 1.5D, 1.5D);

		pdf.generate();
		return pdf.getFile();
	}

	private Empreendimento getEmpreendimentoEU (String cpfCnpj){

		return WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(cpfCnpj);

	}

	public String textoLocalizacaoPDF() {

		Geometry geo = GeoJsonUtils.toGeometry(this.caracterizacao.empreendimento.empreendimentoEU.localizacao.geometria);
		Point centroide = geo.getCentroid();
		String textoLocalizacao = "LOCALIZAÇÃO: X: " + centroide.getX() + ", Y: " + centroide.getY();
		if(!geo.getGeometryType().equals("Point")) {
			textoLocalizacao += " (centroide)";
		}
		return textoLocalizacao;
	}

	public void gerarPDFDispensa() throws Exception {

		this.documento = gerarPDF();
		super.save();

	}

	private void gerarNumero() {

		if (this.id == null)
			throw new IllegalStateException("Licença não salva.");

		this.numero = String.format("%06d", this.id) + "/" + Calendar.getInstance().get(Calendar.YEAR);
	}

}