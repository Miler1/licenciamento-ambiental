package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.ufla.lemaf.beans.pessoa.Pais;
import br.ufla.lemaf.beans.pessoa.TipoEndereco;
import br.ufla.lemaf.beans.pessoa.ZonaLocalizacao;
import play.db.jpa.GenericModel;
import utils.Identificavel;
import utils.LicenciamentoUtils;
import utils.validacao.CustomValidation;
import utils.validacao.ICustomValidation;
import utils.validacao.Validacao;

import static utils.Configuracoes.*;


//@Entity
//@Table(schema = "licenciamento", name = "endereco")
//@CustomValidation
public class Endereco extends br.ufla.lemaf.beans.pessoa.Endereco {

//	private static final String SEQ = "licenciamento.endereco_id_seq";
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
//	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
//	public Long id;
//
//	@Column(name="tipo_endereco")
//	@Enumerated(EnumType.ORDINAL)
//	public TipoLocalizacao tipo;
//
//	public Integer cep;
//
//	public String logradouro;
//
//	public String numero;
//
//	@Transient
//	public boolean semNumero;
//
//	public String complemento;
//
//	public Boolean correspondencia;
//
//	public String bairro;
//
//	@Column(name = "caixa_postal")
//	public String caixaPostal;
//
//	@Column(name = "roteiro_acesso")
//	public String roteiroAcesso;
//
//	@ManyToOne
//	@JoinColumn(name="id_municipio", referencedColumnName="id_municipio")
//	public Municipio municipio;
//
//	@Transient
//	public Integer idEntradaUnica;
//
//
//	public Endereco() {
//
//	}
//
//	public Endereco(Endereco endereco) {
//		this.tipo = endereco.tipo;
//		this.cep = endereco.cep;
//		this.logradouro = endereco.logradouro;
//		this.numero = endereco.numero;
//		this.correspondencia = endereco.correspondencia;
//		this.bairro = endereco.bairro;
//		this.caixaPostal = endereco.caixaPostal;
//		this.roteiroAcesso = endereco.roteiroAcesso;
//		this.municipio = endereco.municipio;
//		this.semNumero = endereco.numero == null;
//	}
//
//	@Override
//	public Long getId() {
//
//		return this.id;
//	}
//
//	public boolean isValid() {
//
//		if (tipo == null)
//			return false;
//
//		if (tipo == TipoLocalizacao.ZONA_RURAL) {
//
//			return !correspondencia && Validacao.checkRequired(municipio, roteiroAcesso);
//
//		} else {
//
//			return Validacao.checkRequired(cep, logradouro, municipio);
//		}
//	}
//
//	@PrePersist
//	@PreUpdate
//	private void preSave() {
//
//		if (tipo == TipoLocalizacao.ZONA_RURAL) {
//
//			this.logradouro = null;
//			this.cep = null;
//			this.complemento = null;
//			this.bairro = null;
//			this.numero = null;
//
//		} else {
//
//			this.roteiroAcesso = null;
//		}
//
//		if (this.correspondencia == null || !this.correspondencia) {
//
//			this.correspondencia = false;
//			this.caixaPostal = null;
//		}
//	}
//
//	public void update(Endereco dados) {
//
//		this.tipo = dados.tipo;
//		this.cep = dados.cep;
//		this.logradouro = dados.logradouro;
//		this.numero = dados.numero;
//		this.complemento = dados.complemento;
//		this.correspondencia = dados.correspondencia;
//		this.bairro = dados.bairro;
//		this.caixaPostal = dados.caixaPostal;
//		this.roteiroAcesso = dados.roteiroAcesso;
//		this.municipio = dados.municipio;
//
//		Validacao.validar(this);
//		super.save();
//	}

	public static Endereco getByCorrespondencia(Collection<Endereco> enderecos, boolean correspondencia) {

		if (enderecos == null)
			return null;

		Integer idTipoEndereco = correspondencia ? ID_TIPO_ENDERECO_CORRESPONDENCIA : ID_TIPO_ENDERECO_PRINCIPAL;

		return enderecos.stream().filter(e -> e.tipo.id.equals(idTipoEndereco)).findFirst().orElse(null);

	}

//	public static int countByCorrespondencia(Collection<Endereco> enderecos, boolean correspondencia) {
//
//		int count = 0;
//
//		if (enderecos == null)
//			return 0;
//
//		for (Endereco endereco : enderecos) {
//
//			if (endereco.correspondencia.equals(correspondencia))
//				count++;
//		}
//
//		return count;
//	}
//
	public String getDescricao() {

		return getDescricao(Integer.MAX_VALUE, true);
	}

	public String getDescricao(int max) {

		return getDescricao(max, true);
	}

	public String getDescricao(Boolean comCep) {

		return getDescricao(Integer.MAX_VALUE, comCep);
	}

	private String getDescricao(int max, Boolean comCep) {

//		if (this.tipo == TipoLocalizacao.ZONA_RURAL) {
//
//			return this.roteiroAcesso.length() > max
//					? this.roteiroAcesso.substring(0, max) : this.roteiroAcesso;
//
//		} else {
//
//			StringBuilder sb = new StringBuilder(this.logradouro);
//
//			if (sb.length() > max)
//				return sb.substring(0, max).toString();
//
//			String separador = ", ";
//
//			boolean temp =
//				concatenarDescricao(sb, numero, separador, max) &&
//				concatenarDescricao(sb, complemento, separador, max) &&
//				concatenarDescricao(sb, bairro, separador, max);
//
//			if(comCep)
//				temp = temp && concatenarDescricao(sb, "CEP " + LicenciamentoUtils.formatarCep(this.cep.toString()), separador, max);
//
//			return sb.toString();
//		}

		return "";
	}
//
//	private boolean concatenarDescricao(StringBuilder sb, String texto, String prefixo, int max) {
//
//		if (texto == null)
//			return true;
//
//		if ((sb.length() + texto.length() + prefixo.length()) <= max) {
//
//			sb.append(prefixo).append(texto);
//			return true;
//
//		} else {
//
//			return false;
//		}
//	}
//
//	public boolean isZonaRural() {
//
//		return (tipo == TipoLocalizacao.ZONA_RURAL);
//
//	}
//
//	public static List<br.ufla.lemaf.beans.pessoa.Endereco> toEnderecosEU(Endereco enderecoPrincipal, Endereco enderecoCorrespondencia) {
//
//		if (enderecoPrincipal == null || (enderecoCorrespondencia == null && enderecoPrincipal.tipo.id == TipoLocalizacao.ZONA_RURAL.id)) {
//
//			return null;
//		}
//
//		List<br.ufla.lemaf.beans.pessoa.Endereco> enderecosEU = new ArrayList<>();
//
//		br.ufla.lemaf.beans.pessoa.Endereco enderecoPrincipalEU = new br.ufla.lemaf.beans.pessoa.Endereco();
//		enderecoPrincipalEU.bairro = enderecoPrincipal.bairro;
//		enderecoPrincipalEU.caixaPostal = enderecoPrincipal.caixaPostal;
//		enderecoPrincipalEU.cep = enderecoPrincipal.cep != null ? enderecoPrincipal.cep.toString() : null;
//		enderecoPrincipalEU.complemento = enderecoPrincipal.complemento;
//		enderecoPrincipalEU.descricaoAcesso = enderecoPrincipal.roteiroAcesso;
//		enderecoPrincipalEU.logradouro = enderecoPrincipal.logradouro;
//		enderecoPrincipalEU.municipio = enderecoPrincipal.municipio.toMunicipioEU();
//		enderecoPrincipalEU.numero = enderecoPrincipal.numero != null ? Integer.valueOf(enderecoPrincipal.numero) : null;
//		enderecoPrincipalEU.pais = new Pais();
//		enderecoPrincipalEU.pais.id = ID_BRASIL_EU;
//		enderecoPrincipalEU.semNumero = enderecoPrincipal.numero == null;
//		enderecoPrincipalEU.tipo = new TipoEndereco();
//		enderecoPrincipalEU.tipo.id = ID_TIPO_ENDERECO_PRINCIPAL;
//		enderecoPrincipalEU.zonaLocalizacao = enderecoPrincipal.createZonaLocalizacaoEU();
//		enderecoPrincipalEU.id = enderecoPrincipal.idEntradaUnica;
//
//		enderecosEU.add(enderecoPrincipalEU);
//
//		if (enderecoCorrespondencia != null) {
//
//			br.ufla.lemaf.beans.pessoa.Endereco enderecoCorrespondenciaEU = new br.ufla.lemaf.beans.pessoa.Endereco();
//			enderecoCorrespondenciaEU.bairro = enderecoCorrespondencia.bairro;
//			enderecoCorrespondenciaEU.caixaPostal = enderecoCorrespondencia.caixaPostal;
//			enderecoCorrespondenciaEU.cep = enderecoCorrespondencia.cep != null ? enderecoCorrespondencia.cep.toString() : null;
//			enderecoCorrespondenciaEU.complemento = enderecoCorrespondencia.complemento;
//			enderecoCorrespondenciaEU.logradouro = enderecoCorrespondencia.logradouro;
//			enderecoCorrespondenciaEU.municipio = enderecoCorrespondencia.municipio.toMunicipioEU();
//			enderecoCorrespondenciaEU.numero = enderecoCorrespondencia.numero != null ? Integer.valueOf(enderecoCorrespondencia.numero) : null;
//			enderecoCorrespondenciaEU.pais = new Pais();
//			enderecoCorrespondenciaEU.pais.id = ID_BRASIL_EU;
//			enderecoCorrespondenciaEU.semNumero = enderecoCorrespondencia.numero == null;
//			enderecoCorrespondenciaEU.tipo = new TipoEndereco();
//			enderecoCorrespondenciaEU.tipo.id = ID_TIPO_ENDERECO_CORRESPONDENCIA;
//			enderecoCorrespondenciaEU.zonaLocalizacao = enderecoCorrespondencia.createZonaLocalizacaoEU();
//			enderecoCorrespondenciaEU.id = enderecoCorrespondencia.idEntradaUnica;
//
//			enderecosEU.add(enderecoCorrespondenciaEU);
//
//		} else {
//
//			br.ufla.lemaf.beans.pessoa.Endereco enderecoCorrespondenciaEU = new br.ufla.lemaf.beans.pessoa.Endereco();
//			enderecoCorrespondenciaEU.bairro = enderecoPrincipalEU.bairro;
//			enderecoCorrespondenciaEU.caixaPostal = enderecoPrincipalEU.caixaPostal;
//			enderecoCorrespondenciaEU.cep = enderecoPrincipalEU.cep;
//			enderecoCorrespondenciaEU.complemento = enderecoPrincipalEU.complemento;
//			enderecoCorrespondenciaEU.logradouro = enderecoPrincipalEU.logradouro;
//			enderecoCorrespondenciaEU.municipio = enderecoPrincipalEU.municipio;
//			enderecoCorrespondenciaEU.numero = enderecoPrincipalEU.numero;
//			enderecoCorrespondenciaEU.pais = enderecoPrincipalEU.pais;
//			enderecoCorrespondenciaEU.semNumero = enderecoPrincipalEU.semNumero;
//			enderecoCorrespondenciaEU.tipo = new TipoEndereco();
//			enderecoCorrespondenciaEU.tipo.id = ID_TIPO_ENDERECO_CORRESPONDENCIA;
//			enderecoCorrespondenciaEU.zonaLocalizacao = enderecoPrincipalEU.zonaLocalizacao;
//
//			enderecosEU.add(enderecoCorrespondenciaEU);
//		}
//
//		return enderecosEU;
//	}
//
//
//	public static List<Endereco> convert(List<br.ufla.lemaf.beans.pessoa.Endereco> enderecosEU) {
//
//		List<Endereco> enderecos = new ArrayList<>();
//
//		for (br.ufla.lemaf.beans.pessoa.Endereco enderecoEU : enderecosEU) {
//
//			Endereco endereco = new Endereco();
//
//			endereco.idEntradaUnica = enderecoEU.id;
//
//			if (enderecoEU.zonaLocalizacao != null) {
//
//                endereco.tipo = enderecoEU.zonaLocalizacao.codigo
//                        .equals(ID_ZONA_LOCALIZACAO_ENDERECO_URBANA)
//                        ? TipoLocalizacao.ZONA_URBANA : TipoLocalizacao.ZONA_RURAL;
//            } else {
//
//			    endereco.tipo = TipoLocalizacao.ZONA_RURAL;
//            }
//			endereco.cep = (enderecoEU.cep == null || enderecoEU.cep.equals("N/A")) ? null : Integer.valueOf(enderecoEU.cep);
//			endereco.logradouro = enderecoEU.logradouro;
//			endereco.numero = enderecoEU.numero != null ? enderecoEU.numero.toString() : null;
//			endereco.complemento = enderecoEU.complemento;
//			endereco.correspondencia = !enderecoEU.tipo.id
//					.equals(ID_TIPO_ENDERECO_PRINCIPAL);
//			endereco.bairro = enderecoEU.bairro;
//			endereco.caixaPostal = enderecoEU.caixaPostal;
//			endereco.roteiroAcesso = (String) enderecoEU.descricaoAcesso;
//			endereco.municipio = Municipio.findById(Long.valueOf(enderecoEU.municipio.codigoIbge));
//			endereco.semNumero = enderecoEU.numero == null;
//
//
//			enderecos.add(endereco);
//		}
//
//		return enderecos;
//	}
//
//	private ZonaLocalizacao createZonaLocalizacaoEU() {
//
//		ZonaLocalizacao zonaLocalizacao = new ZonaLocalizacao();
//
//		if (this.isZonaRural()) {
//
//			zonaLocalizacao.codigo = ID_ZONA_LOCALIZACAO_ENDERECO_RURAL;
//			zonaLocalizacao.nome = "RURAL";
//			zonaLocalizacao.descricao = "Rural";
//
//		} else {
//
//			zonaLocalizacao.codigo = ID_ZONA_LOCALIZACAO_ENDERECO_URBANA;
//			zonaLocalizacao.nome = "URBANA";
//			zonaLocalizacao.descricao = "Urbana";
//		}
//
//		return zonaLocalizacao;
//	}
}
