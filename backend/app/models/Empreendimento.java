package models;

import beans.EmpreendimentoVinculoRespostaVO;
import beans.VinculoEmpreendimentoCpfCnpjVO;
import br.ufla.lemaf.beans.Message;
import br.ufla.lemaf.beans.pessoa.Estado;
import br.ufla.lemaf.beans.pessoa.TipoPessoa;
import br.ufla.lemaf.beans.pessoa.Tipo;
import br.ufla.lemaf.enums.TipoEndereco;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import exceptions.AppException;
import exceptions.PermissaoNegadaException;
import exceptions.ValidacaoException;
import br.ufla.lemaf.beans.Cnae;
import br.ufla.lemaf.beans.pessoa.Localizacao;
import br.ufla.lemaf.beans.pessoa.TipoContato;
import models.caracterizacao.Caracterizacao;
import models.sicar.ImovelSicar;
import models.sicar.SicarWebService;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import play.Logger;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import security.models.UsuarioSessao;
import security.services.Auth;
import serializers.EmpreendimentoSerializer;
import utils.*;
import utils.validacao.Validacao;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static utils.Configuracoes.ID_TIPO_ENDERECO_CORRESPONDENCIA;
import static utils.Configuracoes.ID_TIPO_ENDERECO_PRINCIPAL;

@Entity
@Table(schema = "licenciamento", name = "empreendimento")
@FilterDefs(value = {
		@FilterDef( name = "empreendimentoAtivo", parameters = @ParamDef(name = "ativo", type = "boolean"), defaultCondition = "ativo = TRUE" )
})
public class 	Empreendimento extends GenericModel implements EmpreendimentoEu {

	private static final String SEQ = "licenciamento.empreendimento_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@Required
	@Column(name = "cpf_cnpj")
	public String cpfCnpj;

	@Required
	@Column(name = "denominacao")
	public String denominacao;

	@Required
	@Column(name = "cpf_cnpj_cadastrante")
	public String cpfCnpjCadastrante;
	
	@Transient
	public Pessoa pessoa;
	
	@Transient
	public Pessoa cadastrante;

	@Transient
	public br.ufla.lemaf.beans.Empreendimento empreendimentoEU;

	@Transient
	public Empreendedor empreendedor;
	
	@Column(name="tipo_localizacao")
	@Enumerated(EnumType.ORDINAL)
	public TipoLocalizacao localizacao;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "empreendimento", orphanRemoval = true)
	public ImovelEmpreendimento imovel;

	@Column(name = "data_cadastro")
	public Date dataCadastro;
	
	public boolean ativo;

	@Required
	@ManyToOne
	@JoinColumn(name="id_municipio", referencedColumnName="id_municipio")
	public Municipio municipio;
	
	@Required
	@Column(name="tipo_esfera")
	@Enumerated(EnumType.ORDINAL)
	public Esfera jurisdicao;

	@Transient
	public boolean possuiCaracterizacoes;

	@Transient
	public boolean emRenovacao;

	@Transient
	public Integer idEntradaUnica;

	@Transient
	public List<Cnae> cnaes;

	@Transient
	public Boolean removivel;

	@Transient
	public List<String> cpfCnpjPessoasVinculadas;

	@Transient
    public List<Empreendimento> empreendimentosParaVincular;

	@Transient
	public boolean empreendimentoPessoaFisica;

	@Transient
	public Municipio municipioLicenciamento;

	private enum TipoPessoaAtualizada {

		NOVA_PESSOA,
		OUTRA_PESSOA,
		MESMA_PESSOA
	}

	@Override
	public Empreendimento save() {

//		Validacao.validar(this);

		if (this.imovel != null)
			this.imovel.id = null;

		Logger.info("\n\n EMPREENDIMENTO: \n %s", this.cadastrante.getCpfCnpj());

		validarLocalizacao();

		this.dataCadastro = new Date();
		this.ativo = true;
		this.denominacao = this.empreendimentoEU.denominacao;

		this.formataEnderecosEmpreendimento(WebServiceEntradaUnica.findPessoaByCpfCnpjEU(this.cpfCnpj).enderecos);

		br.ufla.lemaf.beans.pessoa.Pessoa pessoa = this.empreendimentoEU.pessoa.cpf != null ?
				WebServiceEntradaUnica.findPessoaByCpfEU(this.empreendimentoEU.pessoa.cpf) : WebServiceEntradaUnica.findPessoaByCpfCnpjEU(this.empreendimentoEU.pessoa.cnpj);

		this.empreendimentoEU.pessoa.enderecos = this.getEnderecos(this.empreendimentoEU.pessoa.enderecos, pessoa.enderecos);
		this.empreendimentoEU.pessoa.estadoCivil = pessoa.estadoCivil;
		this.empreendimentoEU.pessoa.sexo = pessoa.sexo;
		this.empreendimentoEU.pessoa.tipo = pessoa.tipo;

		this.setEmpreendedorEU(pessoa);

		this.setProprietariosEU();

		this.setRepresentantesLegaisEU();

		this.setResponsaveisEU();

		Pessoa cadastrante = Auth.getUsuarioSessao().findPessoa();

		try {
			Logger.info(" ::Salvando empreendimento no Entrada Única:: ");
			Message empreendimentoSalvo = WebServiceEntradaUnica.createOrUpdateEmpreendimento(this, cadastrante.getCpfCnpj());
			if (!empreendimentoSalvo.getText().contains("Error")) {
				Logger.info(" ::Empreendimento salvo no Entrada Única:: ");

//				WebServiceEntradaUnica.vincularGestaoEmpreendimentos(this.cadastrante.getCpfCnpj());

				Logger.info(" ::Salvando referência do empreendimento no Licenciamento:: ");
				super.save();
				Logger.info(" ::Referência do empreendimento salva no Licenciamento:: ");
			} else {

				throw new Exception("Ocorreu um erro ao salvar o empreendimento no Entrada Única. Por favor, tente mais tarde ou contate o administrador.");
			}


		}catch (Exception e){

			throw new AppException(e.getMessage());

		}

		return this;

	}

	@Override
	public Empreendimento delete() {

		this.validarSeUsuarioCadastrante();

		if(!getRemovivel()) {
			throw new ValidacaoException(Mensagem.EMPREENDIMENTO_NAO_REMOVIVEL_POSSUI_CARACTERIZACOES_VINCULADAS);
		}
		
		return super.delete();
	}

	@Override
	public Endereco getEnderecoPrincipal() {

		if (this.empreendimentoEU.enderecos == null)
			return null;

		return this.empreendimentoEU.enderecos.stream().filter(e -> e.tipo.id.equals(ID_TIPO_ENDERECO_PRINCIPAL))
				.map(e -> (Endereco) e)
				.findFirst().orElse(null);
	}

	@Override
	public Endereco getEnderecoCorrespondencia() {

		if (this.empreendimentoEU.enderecos == null)
			return null;

		return this.empreendimentoEU.enderecos.stream().filter(e -> e.tipo.id.equals(ID_TIPO_ENDERECO_CORRESPONDENCIA))
				.map(e -> (Endereco) e)
				.findFirst().orElse(null);
	}

	@Override
	public Municipio getMunicipio() {
		return this.municipio;
	}

	@Override
	public TipoLocalizacao getLocalizacao() {
		return this.localizacao;
	}

	@Override
	public TipoLocalizacao getTipoLocalizacao() {
		return null;
	}

	@Override
	public Estado getEstado() {

		Optional<br.ufla.lemaf.beans.pessoa.Endereco> end = this.empreendimentoEU.enderecos.stream()
				.filter(en -> en.tipo.id.equals(TipoEndereco.ID_PRINCIPAL))
				.findFirst();
		return end.map(endereco -> endereco.municipio.estado).orElse(null);

	}

	@Override
	public List<Pessoa> getResponsaveisTecnicos() {
		return null;
	}

	@Override
	public List<br.ufla.lemaf.beans.pessoa.Pessoa> getProprietarios() {

		List<br.ufla.lemaf.beans.pessoa.Pessoa> proprietarios = new ArrayList<>();

		br.ufla.lemaf.beans.pessoa.Pessoa pessoa;

		for (br.ufla.lemaf.beans.pessoa.Pessoa proprietario : this.empreendimentoEU.proprietarios) {

			pessoa = WebServiceEntradaUnica.findPessoaByCpfCnpjEU(Pessoa.getCpfCnpjPessoaEU(proprietario));

			proprietarios.add(pessoa);
		}

		return proprietarios;

	}

	@Override
	public List<br.ufla.lemaf.beans.pessoa.Endereco> getEnderecos(List<br.ufla.lemaf.beans.pessoa.Endereco> enderecos, List<br.ufla.lemaf.beans.pessoa.Endereco> enderecosFormatados ) {

		for (br.ufla.lemaf.beans.pessoa.Endereco end : enderecosFormatados) {
			if(end.tipo.id.equals(TipoEndereco.ID_PRINCIPAL)){

				enderecos.get(0).tipo = end.tipo;
				enderecos.get(0).zonaLocalizacao = end.zonaLocalizacao;
				enderecos.get(0).municipio = end.municipio;
				enderecos.get(0).descricaoAcesso = end.descricaoAcesso;
				enderecos.get(0).pais = end.pais;
				enderecos.get(0).caixaPostal = end.caixaPostal;
				enderecos.get(0).bairro = end.bairro;
				enderecos.get(0).complemento = end.complemento;
				enderecos.get(0).logradouro = end.logradouro;
				enderecos.get(0).numero = end.numero;
				enderecos.get(0).cep = end.cep;
				enderecos.get(0).semNumero = end.numero == null && end.zonaLocalizacao.descricao.equals("Urbana") ? true : false;

			}else {

				enderecos.get(1).tipo = end.tipo;
				enderecos.get(1).zonaLocalizacao = end.zonaLocalizacao;
				enderecos.get(1).municipio = end.municipio;
				enderecos.get(1).descricaoAcesso = end.descricaoAcesso;
				enderecos.get(1).pais = end.pais;
				enderecos.get(1).caixaPostal = end.caixaPostal;
				enderecos.get(1).bairro = end.bairro;
				enderecos.get(1).complemento = end.complemento;
				enderecos.get(1).logradouro = end.logradouro;
				enderecos.get(1).numero = end.numero;
				enderecos.get(1).cep = end.cep;
				enderecos.get(1).semNumero =end.numero == null && end.zonaLocalizacao.descricao.equals("Urbana") ? true : false;
			}
		}
		return enderecos;

	}

	@Override
	public Geometry getGeometry() {
		return GeoJsonUtils.toGeometry(this.empreendimentoEU.localizacao.geometria);
	}

	@Override
	public Geometry getLimiteMunicipio() {
		return null;
	}

	@Override
	public Geometry getLimiteEstado() {
		return null;
	}

	@Override
	public Point centroid() {
		return null;
	}

	@Override
	public String getGeometryType() {
		return null;
	}

	@Override
	public br.ufla.lemaf.beans.Empreendimento getEmpreendimentoEU(){

		if(this.empreendimentoEU == null){
			this.empreendimentoEU = WebServiceEntradaUnica.findEmpreendimentosEU(this);
		}

		return this.empreendimentoEU;
	}

	public void formataEnderecosEmpreendimento(List<br.ufla.lemaf.beans.pessoa.Endereco> enderecosFormatados) {

		this.empreendimentoEU.enderecos.forEach(endereco -> {
			endereco.zonaLocalizacao.nome = endereco.zonaLocalizacao.codigo == 0 ? "Urbana" : "Rural";
			endereco.semNumero = endereco.numero == null && endereco.zonaLocalizacao.nome.equals("Urbana") ? true : false;
			endereco.pais = enderecosFormatados.get(0).pais;
		});

	}

	public String getDescricao() {

		return getDescricao(Integer.MAX_VALUE, true);
	}

	public String getDescricao(int max) {

		return getDescricao(max, true);
	}

	public String getDescricao(Boolean comCep) {

		return getDescricao(Integer.MAX_VALUE, comCep);
	}

	@Override
	public String getDescricao(int max, Boolean comCep) {

		if (this.empreendimentoEU.enderecos.get(0).zonaLocalizacao.codigo == TipoLocalizacao.ZONA_RURAL.id) {

			return this.empreendimentoEU.enderecos.get(0).descricaoAcesso.toString().length() > max
					? this.empreendimentoEU.enderecos.get(0).descricaoAcesso.toString().substring(0, max) : this.empreendimentoEU.enderecos.get(0).descricaoAcesso.toString();

		} else {

			StringBuilder sb = new StringBuilder(this.empreendimentoEU.enderecos.get(0).logradouro);

			if (sb.length() > max)
				return sb.substring(0, max).toString();

			String separador = ", ";

			boolean temp =
					concatenarDescricao(sb, this.empreendimentoEU.enderecos.get(0).numero.toString(), separador, max) &&
							concatenarDescricao(sb, this.empreendimentoEU.enderecos.get(0).complemento, separador, max) &&
							concatenarDescricao(sb, this.empreendimentoEU.enderecos.get(0).bairro, separador, max);

			if(comCep)
				temp = temp && concatenarDescricao(sb, "CEP " + LicenciamentoUtils.formatarCep(this.empreendimentoEU.enderecos.get(0).cep), separador, max);

			return sb.toString();
		}

	}

	private boolean concatenarDescricao(StringBuilder sb, String texto, String prefixo, int max) {

		if (texto == null)
			return true;

		if ((sb.length() + texto.length() + prefixo.length()) <= max) {

			sb.append(prefixo).append(texto);
			return true;

		} else {

			return false;
		}
	}

	public Boolean isIsento() {

		return this.empreendimentoEU != null &&
				this.empreendimentoEU.classificacaoEmpreendimento != null &&
				this.empreendimentoEU.classificacaoEmpreendimento.isento;
	}

	public Boolean getRemovivel() {

		return !temCaracterizacoes();
	}

	public boolean temCaracterizacoes() {

		return Caracterizacao.countByEmpreendimento(this.id) > 0;
	}

	public void update(Empreendimento dados) {

		this.validarSeUsuarioCadastrante();

		this.empreendimentoEU = dados.empreendimentoEU;
		this.denominacao = dados.empreendimentoEU.denominacao;
		this.empreendimentoEU.contatos = dados.empreendimentoEU.contatos;

		if (this.empreendimentoEU.pessoa.cpf != null) {
			this.empreendimentoEU.pessoa.sexo.nome = dados.empreendimentoEU.pessoa.sexo.descricao;
		}

		if (this.empreendimentoEU.empreendedor.pessoa.cpf != null) {
			this.empreendimentoEU.empreendedor.pessoa.sexo.nome = dados.empreendimentoEU.empreendedor.pessoa.sexo.descricao;
		}

		this.empreendimentoEU.pessoa.tipo.nome = dados.empreendimentoEU.pessoa.tipo.codigo == 0 ? "Pessoa Física": "Pessoa Jurídica";
		this.empreendimentoEU.empreendedor.pessoa.tipo.nome = dados.empreendimentoEU.empreendedor.pessoa.tipo.codigo == 0 ? "Pessoa Física":"Pessoa Jurídica";


		if (!temCaracterizacoes()) {
			this.imovel = dados.imovel;
//			validarLocalizacao();
		}

		super.save();

		Pessoa cadastrante = Auth.getUsuarioSessao().findPessoa();

		WebServiceEntradaUnica.updateEmpreendimento(this, cadastrante.getCpfCnpj());
	}

	public void validarSeUsuarioCadastrante() {

		Pessoa cadastrante = Auth.getUsuarioSessao().findPessoa();

		if (!this.cpfCnpjCadastrante.equals(cadastrante.getCpfCnpj())) {
			throw new PermissaoNegadaException(Mensagem.EMPREENDEDOR_USUARIO_NAO_REPRESENTANTE);
		}
	}

//	private boolean isProprietario(Pessoa pessoa) {
//		return this.getProprietarios().stream().anyMatch(p -> p.getCpfCnpj().equals(pessoa.getCpfCnpj()));
//	}

	/**
	 * Valida a localizacao e coordenadas do imovel.
	 * Caso o empreendimento esteja na Zona Urbana, valida se suas coordenadas estão inseridas no
	 * município do empreendimento. Caso esteja na Zona Rural, valida se o imóvel especificado
	 * existe no CAR e está vinculado ao CPF/CNPJ do empreendimento, e também verifica se as
	 * coordenadas do empreendimento estão inseridas nos limites do imóvel.
	 * Também valida se o empreendimento está localizado dentro do estado
	 */
	private void validarLocalizacao() {

		Municipio municipio = Municipio.findById(this.municipio.id);

		if (!municipio.estado.codigo.equals(Configuracoes.ESTADO))
			throw new ValidacaoException(Mensagem.EMPREENDIMENTO_FORA_DO_ESTADO);

		if (this.localizacao.equals(TipoLocalizacao.ZONA_RURAL)) {

			Validacao.validateRequired(this.imovel);
			Validacao.validar(this.imovel);

			carregarImovelSicar();
			this.imovel.empreendimento = this;

			if (this.imovel.limite == null || !this.imovel.limite.contains(this.getGeometry()))
				throw new ValidacaoException(Mensagem.EMPREENDIMENTO_COORDENADAS_FORA_DO_IMOVEL);
		}
	}


	/**
	 * Busca as informações do imóvel no CAR e verifica se ele está vinculado ao CPF/CNPJ
	 * do empreendimento.
	 */
	private void carregarImovelSicar() {

		SicarWebService sicarWS = new SicarWebService();
		List<ImovelSicar> imoveisSicar = sicarWS.getImoveisSimplificadosPorCpfCnpj(this.cpfCnpj, Municipio.findById(this.getMunicipio().id));

		ImovelSicar imovelSicar = ImovelSicar.getByCodigo(this.imovel.codigo, imoveisSicar);

		if (imovelSicar == null)
			throw new ValidacaoException(Mensagem.EMPREENDIMENTO_IMOVEL_NAO_VINCULADO_AO_CPF_CNPJ);

		imovelSicar = sicarWS.getImovelByCodigo(imovelSicar.codigo);

		this.imovel = new ImovelEmpreendimento(imovelSicar);
	}

	private static JPAQuery setWhere(String select, String cpfPessoaUsuarioSessao, String pesquisa, boolean orderBy) {

		pesquisa = PersistUtils.like(pesquisa);
		JPAQuery jpaQuery;

		String jpql = select + "WHERE (UPPER(emp.cpfCnpjCadastrante) LIKE UPPER (:cpfCnpjCadastrante)) AND emp.ativo = true %s ";

		if (orderBy) {

//			jpql += "ORDER BY emp.denominacao, CASE WHEN pe.nome IS NOT NULL THEN pe.nome ELSE pe.razaoSocial END";
//			jpql += "ORDER BY emp.denominacao";
		}

		if (pesquisa != null) {

			String where =
					"AND (UPPER(emp.cpfCnpj) LIKE UPPER (:cpfCnpj))" ;


			jpql = String.format(jpql, where);

			jpaQuery = Empreendimento.find(jpql);

			jpaQuery.setParameter("cpfCnpjCadastrante", cpfPessoaUsuarioSessao);


		} else {

			jpql = String.format(jpql, "");

			jpaQuery = Empreendimento.find(jpql);

			jpaQuery.setParameter("cpfCnpjCadastrante", cpfPessoaUsuarioSessao);
		}

		return jpaQuery;
	}


	public static List<Empreendimento> list(UsuarioSessao usuarioSessao, String pesquisa, Integer numeroPagina, Integer qtdItensPorPagina) {

		String select =
				"SELECT emp FROM " + Empreendimento.class.getCanonicalName() + " emp " ;

		JPAQuery jpaQuery = setWhere(select, usuarioSessao.login, pesquisa, true);

		List<Empreendimento> empreendimentos = null;

		if ( numeroPagina > 0 && qtdItensPorPagina > 0) {
			empreendimentos = jpaQuery.fetch(numeroPagina, qtdItensPorPagina);
		} else {

			empreendimentos = jpaQuery.fetch();
		}

		empreendimentos = DistinctRootEntityResultTransformer.INSTANCE.transformList(empreendimentos);

		return empreendimentos;
	}


	public static Long count(UsuarioSessao usuarioSessao, String pesquisa) {

		String select =
				"SELECT COUNT(*) FROM " + Empreendimento.class.getCanonicalName() + " emp ";

		JPAQuery jpaQuery = setWhere(select, usuarioSessao.login, pesquisa, false);

		return jpaQuery.first();
	}

	public static Pessoa getPessoaPorCpfCnpj(String cpfCnpj) {

		Pessoa pessoa = (Pessoa) WebServiceEntradaUnica.findPessoaByCpfCnpj(cpfCnpj);

		if (pessoa != null) {

			if (Empreendimento.find("byPessoa", pessoa).first() != null
					&& WebServiceEntradaUnica.validaEmpreendimentosCadastrado(cpfCnpj)) {

				throw new AppException(Mensagem.EMPREENDIMENTO_JA_CADASTRADO);
			}

		} else {

			pessoa = (Pessoa) WebServiceEntradaUnica.findPessoaByCpfCnpj(cpfCnpj);

			if (WebServiceEntradaUnica.validaEmpreendimentosCadastrado(cpfCnpj)) {

				throw new AppException(Mensagem.EMPREENDIMENTO_JA_CADASTRADO);
			}
		}

		return pessoa;
	}


	public boolean existeEmpreendimento(Pessoa pessoa) {
		return count("byPessoa", pessoa) > 0;
	}

	public static Empreendimento findToUpdate(Long id) {

		Empreendimento empreendimento = Empreendimento.findById(id);

		empreendimento.possuiCaracterizacoes = empreendimento.temCaracterizacoes();

		return empreendimento;
	}

	public void validarSeZonaRuralSemImovel() {
		br.ufla.lemaf.beans.pessoa.Endereco tipoLocalizacao = this.empreendimentoEU.enderecos.stream().filter(endereco -> endereco.tipo.id == TipoEndereco.ID_PRINCIPAL).findFirst().get();

		if (localizacao == TipoLocalizacao.ZONA_RURAL && imovel == null) {

			throw new PermissaoNegadaException(Mensagem.EMPRENDIMENTO_ZONA_RURAL_SEM_IMOVEL);
		};
	}

	public boolean getIsPessoaFisica() {

		return this.pessoa.isPessoaFisica();
	}

	public static Empreendimento getByCpfCnpj(String cpfCnpj) {

		Pessoa pessoa = Pessoa.convert(WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(cpfCnpj).empreendimentoEU.pessoa);

		if (pessoa != null) {

			Empreendimento empreendimento = Empreendimento.find("cpf_cnpj = :cpfCnpj")
					.setParameter("cpfCnpj",pessoa.getCpfCnpj()).first();

			if(empreendimento == null)
				throw new AppException(Mensagem.EMPREENDIMENTO_NAO_ENCONTRADO);

			return empreendimento;
		}

		throw new AppException(Mensagem.EMPREENDIMENTO_NAO_ENCONTRADO);

	}

	public Set<String> getEmailsProprietariosECadastrantesEU() {

		Set<String> emails = new HashSet();

		for(br.ufla.lemaf.beans.pessoa.Pessoa proprietario : this.getProprietarios()) {

			proprietario.contatos.forEach(c -> {
				if(c.tipo.id.equals(TipoContato.ID_EMAIL)) {
					emails.add(c.valor);
				}
			});
		}

		this.empreendimentoEU.pessoa.contatos.forEach(c -> {
			if(c.tipo.id.equals(TipoContato.ID_EMAIL)) {
				emails.add(c.valor);
			}
		});

		return emails;

	}

	private boolean isSameProprietarios(List<Pessoa> proprietarios) {

		boolean resultado = true;

		if (this.getProprietarios().size() != proprietarios.size()) {

			return false;
		}

		for (br.ufla.lemaf.beans.pessoa.Pessoa proprietario  : this.getProprietarios()) {

			if (!proprietarios.contains(proprietario)) {

				resultado = false;
			}
		}

		return resultado;
	}

	public Geometry getGeometriaEU() {
		return GeoJsonUtils.toGeometry(WebServiceEntradaUnica.findEmpreendimentosEU(this).localizacao.geometria);
	}

	public static boolean verificaExistenciaDeEmpreendimentoLicencimento(String cpfCnpj) {
		String select = "";
		String parametro;

		select =
				" SELECT 1 FROM " + Empreendimento.class.getCanonicalName();
//				" INNER JOIN emp.pessoa p ";
//
//		select += cpfCnpj.length() > 11 ?
//				" INNER JOIN PessoaJuridica pj ON p.id = pj.id WHERE pj.cnpj = :cpfCnpj" :
//				" INNER JOIN PessoaFisica pf ON p.id = pf.id WHERE pf.cpf = :cpfCnpj" ;

		if(WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(cpfCnpj) != null){
			return true; //tem empreendimento dessa pessoa
		}else if (cpfCnpj.length() > 11){
			parametro = WebServiceEntradaUnica.findPessoaByCpfCnpjEU(cpfCnpj).cnpj;
			br.ufla.lemaf.beans.pessoa.Pessoa pessoa = WebServiceEntradaUnica.findPessoaByCpfCnpjEU(cpfCnpj);
			return false;
		}else{
			parametro = WebServiceEntradaUnica.findPessoaByCpfCnpjEU(cpfCnpj).cpf;
			br.ufla.lemaf.beans.pessoa.Pessoa pessoa = WebServiceEntradaUnica.findPessoaByCpfCnpjEU(cpfCnpj);
			return false;
		}


//		return !Empreendimento.find(select)
//				.setParameter("cpfCnpj",parametro)
//				.fetch().isEmpty();
	}

	public static Empreendimento convert(br.ufla.lemaf.beans.Empreendimento empreendimentoEU) {

		if (empreendimentoEU == null || empreendimentoEU.pessoa == null) {

			return null;
		}

		Pessoa p = Pessoa.convert(empreendimentoEU.pessoa);

		Empreendimento emp = Empreendimento.find("byCpfCnpj", p.getCpfCnpj()).first();

		if (emp != null) {

			emp.empreendimentoEU = empreendimentoEU;
		}

		return emp;

	}

	public static Empreendimento convert(br.ufla.lemaf.beans.Empreendimento empreendimentoEU, String cpfCnpj) {

		if (empreendimentoEU == null) {

			Empreendimento empreendimento = new Empreendimento();
			empreendimento.empreendimentoEU = new br.ufla.lemaf.beans.Empreendimento();

			br.ufla.lemaf.beans.pessoa.Pessoa pessoa = WebServiceEntradaUnica.findPessoaByCpfCnpjEU(cpfCnpj);

			empreendimento.empreendimentoEU.pessoa = Pessoa.convert(pessoa);

			return empreendimento;
		}

		Empreendimento empreendimento = convert(empreendimentoEU);

		if (empreendimento == null) {

			empreendimento = new Empreendimento();
			empreendimento.empreendimentoEU = empreendimentoEU;

			br.ufla.lemaf.beans.pessoa.Pessoa pessoa = WebServiceEntradaUnica.findPessoaByCpfCnpjEU(cpfCnpj);

			empreendimento.empreendimentoEU.pessoa = Pessoa.convert(pessoa);


			return empreendimento;
		}

		return empreendimento;
	}


	public Endereco getEndereco() {

		return this.getEnderecoPrincipal();
	}

	public List<String> getCodigosCnaes() {

		return this.empreendimentoEU.cnaes.stream().map(c -> c.codigo).collect(Collectors.toList());

	}

	public static List<Empreendimento> getAllEmpreendimentosByCpfCnpjEmpreendedor(String cpfCnpj){

		JPAQuery jpaQuery;

		String select =
				"SELECT emp FROM " + Empreendimento.class.getCanonicalName() + " emp " +
						"INNER JOIN FETCH emp.pessoa p " +
						"INNER JOIN FETCH emp.empreendedor e "+
						"INNER JOIN FETCH e.pessoa pe ";

		String jpql = select + "WHERE (p.cpf = :cpfCnpjPessoa OR p.cnpj = :cpfCnpjPessoa) AND emp.ativo = true %s ";

		jpql = String.format(jpql, "");
		jpaQuery = Empreendimento.find(jpql);
		jpaQuery.setParameter("cpfCnpjPessoa", cpfCnpj);

		return jpaQuery.fetch();

	}

	public Empreendimento(){

	}

	public Empreendimento(Pessoa pessoa, Long id ,Geometry theGeom){
		this.id = id;
		this.pessoa = pessoa;
		this.empreendimentoEU.localizacao.geometria = theGeom.toString();
	}

	public Boolean possuiVinculocpfCnpjEmpreendedor(String cpfCnpj){

		if(this.empreendimentoEU.responsaveisLegais != null){
			if(this.empreendimentoEU.responsaveisLegais.stream().anyMatch(res -> (res.cpf != null ? res.cpf : res.cnpj).equals(cpfCnpj))){
				return true;
			}
		}

		if(this.empreendimentoEU.proprietarios != null){
			if(this.empreendimentoEU.proprietarios.stream().anyMatch(res -> (res.cpf != null ? res.cpf : res.cnpj).equals(cpfCnpj))){
				return true;
			}
		}

		if(this.empreendimentoEU.representantesLegais != null){
			return this.empreendimentoEU.representantesLegais.stream().anyMatch(res -> (res.cpf != null ? res.cpf : res.cnpj).equals(cpfCnpj));
		}

		return false;
	}

	public static Municipio buscaMunicipioEmpreendimento (String cpfCnpj){

		Empreendimento emp = Empreendimento.find("byCpfCnpj", cpfCnpj).first();
		return emp.municipio;
	}

	public EmpreendimentoVinculoRespostaVO getPessoasVinculadas(VinculoEmpreendimentoCpfCnpjVO cpfCnpjVO) {

		List<String> pessoasVinculadas = new ArrayList<>();

		EmpreendimentoVinculoRespostaVO respostaVO = new EmpreendimentoVinculoRespostaVO(false,"");

		if (cpfCnpjVO == null || cpfCnpjVO.cpfCnpjEmpreendimento == null){
			respostaVO.mensagem = Mensagem.EMPREENDIMENTO_NAO_ENCONTRADO.getTexto();

		}else if(cpfCnpjVO.cpfCnpjEmpreendedor == null){
			respostaVO.mensagem = Mensagem.CPF_CNPJ_NAO_ENCONTRADO_EMPREENDEDOR.getTexto();

		}else {

			for (br.ufla.lemaf.beans.pessoa.Pessoa pessoa : this.empreendimentoEU.responsaveisLegais) {

				pessoasVinculadas.add(pessoa.cpf != null ? pessoa.cpf : pessoa.cnpj);
			}

			for (br.ufla.lemaf.beans.pessoa.Pessoa pessoa : this.empreendimentoEU.responsaveisTecnicos) {

				pessoasVinculadas.add(pessoa.cpf != null ? pessoa.cpf : pessoa.cnpj);
			}

			for (br.ufla.lemaf.beans.pessoa.Pessoa pessoa : this.empreendimentoEU.representantesLegais) {

				pessoasVinculadas.add(pessoa.cpf != null ? pessoa.cpf : pessoa.cnpj);
			}

			for (br.ufla.lemaf.beans.pessoa.Pessoa pessoa : this.empreendimentoEU.proprietarios) {

				pessoasVinculadas.add(pessoa.cpf != null ? pessoa.cpf : pessoa.cnpj);
			}

			if (pessoasVinculadas.contains(cpfCnpjVO.cpfCnpjEmpreendimento)) {
				respostaVO.existeVinculo = true;

			}else{
				respostaVO.mensagem = Mensagem.CPF_CNPJ_NAO_VINCULADO_EMPREENDEDOR.getTexto();
			}

		}

		return respostaVO;

	}

	public void setEmpreendedorEU (br.ufla.lemaf.beans.pessoa.Pessoa pessoa){

		if (pessoa.cpf != null) {

			if (pessoa.cpf.equals(this.empreendimentoEU.empreendedor.pessoa.cpf)) {

				this.empreendimentoEU.empreendedor.pessoa.enderecos = this.getEnderecos(this.empreendimentoEU.empreendedor.pessoa.enderecos, pessoa.enderecos);
				this.empreendimentoEU.empreendedor.pessoa.estadoCivil = pessoa.estadoCivil;
				this.empreendimentoEU.empreendedor.pessoa.sexo = pessoa.sexo;
				this.empreendimentoEU.empreendedor.pessoa.tipo = pessoa.tipo;

			}else{

				br.ufla.lemaf.beans.pessoa.Pessoa empreendedor = this.empreendimentoEU.empreendedor.pessoa.cpf != null ?
						WebServiceEntradaUnica.findPessoaByCpfEU(this.empreendimentoEU.empreendedor.pessoa.cpf) : WebServiceEntradaUnica.findPessoaByCpfCnpjEU(this.empreendimentoEU.empreendedor.pessoa.cnpj);
				this.empreendimentoEU.empreendedor.pessoa.enderecos = this.getEnderecos(this.empreendimentoEU.empreendedor.pessoa.enderecos, empreendedor.enderecos);
				this.empreendimentoEU.empreendedor.pessoa.estadoCivil = empreendedor.estadoCivil;
				this.empreendimentoEU.empreendedor.pessoa.sexo = empreendedor.sexo;
				this.empreendimentoEU.empreendedor.pessoa.tipo = empreendedor.tipo;
			}

		} else if(pessoa.cnpj != null) {

			if (pessoa.cnpj.equals(this.empreendimentoEU.empreendedor.pessoa.cnpj)) {
				this.empreendimentoEU.empreendedor.pessoa.enderecos = this.getEnderecos(this.empreendimentoEU.empreendedor.pessoa.enderecos, pessoa.enderecos);
				this.empreendimentoEU.empreendedor.pessoa.dataConstituicao = pessoa.dataConstituicao;
				this.empreendimentoEU.empreendedor.pessoa.inscricaoEstadual = pessoa.inscricaoEstadual;
				this.empreendimentoEU.empreendedor.pessoa.razaoSocial = pessoa.razaoSocial;
				this.empreendimentoEU.empreendedor.pessoa.tipo = pessoa.tipo;

			}else{

				br.ufla.lemaf.beans.pessoa.Pessoa empreendedor = this.empreendimentoEU.empreendedor.pessoa.cpf != null ?
						WebServiceEntradaUnica.findPessoaByCpfEU(this.empreendimentoEU.empreendedor.pessoa.cpf) : WebServiceEntradaUnica.findPessoaByCpfCnpjEU(this.empreendimentoEU.empreendedor.pessoa.cnpj);
				this.empreendimentoEU.empreendedor.pessoa.enderecos = this.getEnderecos(this.empreendimentoEU.empreendedor.pessoa.enderecos, empreendedor.enderecos);
				this.empreendimentoEU.empreendedor.pessoa.dataConstituicao = empreendedor.dataConstituicao;
				this.empreendimentoEU.empreendedor.pessoa.inscricaoEstadual = empreendedor.inscricaoEstadual;
				this.empreendimentoEU.empreendedor.pessoa.razaoSocial = empreendedor.razaoSocial;
				this.empreendimentoEU.empreendedor.pessoa.tipo = empreendedor.tipo;
			}
		}
	}

	public void setProprietariosEU(){

		List<br.ufla.lemaf.beans.pessoa.Pessoa> listaProprietarios = new ArrayList<>();

		for(br.ufla.lemaf.beans.pessoa.Pessoa proprietario : this.empreendimentoEU.proprietarios){

			br.ufla.lemaf.beans.pessoa.Pessoa proprietarioEU = proprietario.cpf != null ?
					WebServiceEntradaUnica.findPessoaByCpfEU(proprietario.cpf) : WebServiceEntradaUnica.findPessoaByCpfCnpjEU(proprietario.cnpj);

			if(proprietario.id != null){

				proprietario.enderecos = this.getEnderecos(proprietario.enderecos,proprietarioEU.enderecos);
				proprietario.estadoCivil = proprietarioEU.estadoCivil;
				proprietario.sexo = proprietarioEU.sexo;
				proprietario.tipo = proprietarioEU.tipo;
				listaProprietarios.add(proprietario);
			}else{
				listaProprietarios.add(proprietario);
			}

		}
		this.empreendimentoEU.proprietarios = listaProprietarios;
	}

	public void setRepresentantesLegaisEU(){

		List<br.ufla.lemaf.beans.pessoa.Pessoa> listaRepresentantes = new ArrayList<>();

		for(br.ufla.lemaf.beans.pessoa.Pessoa representanteLegal : this.empreendimentoEU.representantesLegais){

			br.ufla.lemaf.beans.pessoa.Pessoa representante = representanteLegal.cpf != null ?
					WebServiceEntradaUnica.findPessoaByCpfEU(representanteLegal.cpf) : WebServiceEntradaUnica.findPessoaByCpfCnpjEU(representanteLegal.cnpj);

			if(representanteLegal.id != null){
				representanteLegal.enderecos = this.getEnderecos(representanteLegal.enderecos,representante.enderecos);
				representanteLegal.estadoCivil = representante.estadoCivil;
				representanteLegal.sexo = representante.sexo;
				representanteLegal.tipo = representante.tipo;
				listaRepresentantes.add(representanteLegal);
			}else{
				listaRepresentantes.add(representanteLegal);
			}

		}

		this.empreendimentoEU.representantesLegais = listaRepresentantes;
	}

	public void setResponsaveisEU(){

		List<br.ufla.lemaf.beans.pessoa.Pessoa> listaResponsaveisLegais = new ArrayList<>();
		List<br.ufla.lemaf.beans.pessoa.Pessoa> listaResponsaveisTecnicos = new ArrayList<>();

		for(br.ufla.lemaf.beans.pessoa.Pessoa responsavelLegal : this.empreendimentoEU.responsaveisLegais){

			br.ufla.lemaf.beans.pessoa.Pessoa responsavel = responsavelLegal.cpf != null ?
					WebServiceEntradaUnica.findPessoaByCpfEU(responsavelLegal.cpf) : WebServiceEntradaUnica.findPessoaByCpfCnpjEU(responsavelLegal.cnpj);

			if(responsavelLegal.id != null ) {
				responsavelLegal.enderecos = this.getEnderecos(responsavelLegal.enderecos, responsavel.enderecos);
				responsavelLegal.estadoCivil = responsavel.estadoCivil;
				responsavelLegal.sexo = responsavel.sexo;
				responsavelLegal.tipo = responsavel.tipo;
				listaResponsaveisLegais.add(responsavelLegal);
			}else{
				listaResponsaveisLegais.add(responsavelLegal);
			}
		}

		this.empreendimentoEU.responsaveisLegais = listaResponsaveisLegais;

		for(br.ufla.lemaf.beans.pessoa.Pessoa responsavelTecnico : this.empreendimentoEU.responsaveisTecnicos){

			br.ufla.lemaf.beans.pessoa.Pessoa responsavel = responsavelTecnico.cpf != null ?
					WebServiceEntradaUnica.findPessoaByCpfEU(responsavelTecnico.cpf) : WebServiceEntradaUnica.findPessoaByCpfCnpjEU(responsavelTecnico.cnpj);

			if(responsavelTecnico.id != null){
				responsavelTecnico.enderecos = this.getEnderecos(responsavelTecnico.enderecos,responsavel.enderecos);
				responsavelTecnico.estadoCivil = responsavel.estadoCivil;
				responsavelTecnico.sexo = responsavel.sexo;
				responsavelTecnico.tipo = responsavel.tipo;
				listaResponsaveisTecnicos.add(responsavelTecnico);
			}else{
				listaResponsaveisTecnicos.add(responsavelTecnico);
			}

		}

		this.empreendimentoEU.responsaveisTecnicos = listaResponsaveisTecnicos;
	}
}
