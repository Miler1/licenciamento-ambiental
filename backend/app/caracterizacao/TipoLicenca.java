package models.caracterizacao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.*;

import beans.TipoLicencaVO;
import models.TipoDocumento;
import play.db.jpa.GenericModel;

@Entity
@Table(schema = "licenciamento", name = "tipo_licenca")
public class TipoLicenca extends GenericModel implements Serializable {

	private static Map<Long, String> TIPOS_DOCUMENTO = new HashMap<>();

	public static final long DISPENSA_LICENCA_AMBIENTAL = 1L;
	public static final long LICENCA_PREVIA = 2L;
	public static final long LICENCA_INSTALACAO = 3L;
	public static final long LICENCA_OPERACAO = 4L;
	public static final long LICENCA_AMBIENTAL_UNICA = 5L;
	public static final long RENOVACAO_LICENCA_DE_INSTALACAO = 6L;
	public static final long RENOVACAO_LICENCA_DE_OPERACAO = 7L;
	public static final long ATUALIZACAO_DE_LICENCA_PREVIA = 8L;
	public static final long RENOVACAO_LICENCA_AMBIENTAL_UNICA = 9L;
	public static final long CADASTRO_AQUICULTURA = 10L;

	static {

		TIPOS_DOCUMENTO.put(DISPENSA_LICENCA_AMBIENTAL, TipoDocumento.COD_DISPENSA_LICENCA_AMBIENTAL);
		TIPOS_DOCUMENTO.put(LICENCA_PREVIA, TipoDocumento.COD_LICENCA_PREVIA);
		TIPOS_DOCUMENTO.put(LICENCA_INSTALACAO, TipoDocumento.COD_LICENCA_INSTALACAO);
		TIPOS_DOCUMENTO.put(LICENCA_OPERACAO, TipoDocumento.COD_LICENCA_OPERACAO);
		TIPOS_DOCUMENTO.put(LICENCA_AMBIENTAL_UNICA, TipoDocumento.COD_LICENCA_AMBIENTAL_UNICA);
		TIPOS_DOCUMENTO.put(RENOVACAO_LICENCA_DE_INSTALACAO,TipoDocumento.COD_RENOVACAO_LICENCA_DE_INSTALACAO);
		TIPOS_DOCUMENTO.put(RENOVACAO_LICENCA_DE_OPERACAO,TipoDocumento.COD_RENOVACAO_LICENCA_DE_OPERACAO);
		TIPOS_DOCUMENTO.put(ATUALIZACAO_DE_LICENCA_PREVIA, TipoDocumento.COD_ATUALIZACAO_DE_LICENCA_PREVIA);
		TIPOS_DOCUMENTO.put(RENOVACAO_LICENCA_AMBIENTAL_UNICA, TipoDocumento.COD_RENOVACAO_LICENCA_AMBIENTAL_UNICA);
		TIPOS_DOCUMENTO.put(CADASTRO_AQUICULTURA, TipoDocumento.COD_CADASTRO_AQUICULTURA);
	}

	public enum Finalidades {

		DISPENSA("DISPENSA"),
		SOLICITACAO("SOLICITACAO"),
		RENOVACAO("RENOVACAO"),
		ATUALIZACAO("ATUALIZACAO"),
		CADASTRO("CADASTRO");

		public String codigo;

		Finalidades(String codigo) {
			this.codigo = codigo;
		}

	}

	@Id
	public Long id;

	@Column(name = "validade_em_anos")
	public Integer validadeEmAnos;

	@Column(name = "nome")
	public String nome;

	@Column(name = "sigla")
	public String sigla;

	@Column(name = "finalidade")
	public String finalidade;

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(schema = "licenciamento", name = "rel_tipo_licenca_permitida",
			joinColumns = @JoinColumn(name = "id_tipo_licenca_pai"),
			inverseJoinColumns = @JoinColumn(name = "id_tipo_licenca_filho"))
	public List<TipoLicenca> tiposLicencasPermitidas;

	@Transient
	public List<TipoLicencaVO> licencasFilhas;

	public List<TipoLicencaVO> getLicencasFilhas() {
		return licencasFilhas;
	}

	public void setLicencasFilhas(List<TipoLicencaVO> licencasFilhas) {
		this.licencasFilhas = licencasFilhas;
	}

	@Transient
	public boolean selecionado = false;

	@Transient
	public Double valorDae;

	@Transient
	public boolean isento;

	public TipoDocumento findTipoDocumento() {

		if (this.id == null || !TIPOS_DOCUMENTO.containsKey(this.id))
			return null;

		return TipoDocumento.find("byCodigo", TIPOS_DOCUMENTO.get(this.id) ).first();
	}

	public Boolean siglaLicenca() {

		return !this.sigla.equals("DDLA");

	}

	public Boolean naoSimplificado(){
		return this.id != TipoLicenca.DISPENSA_LICENCA_AMBIENTAL;
	}

	public Map<String, TipoLicencaVO> hashFilhas() {
		return this.tiposLicencasPermitidas.stream().collect(Collectors.toMap(
				tpFilha -> tpFilha.finalidade.toLowerCase(), TipoLicencaVO::new));
	}

	public Boolean isSolicitacao() {
		return this.finalidade.equals(Finalidades.SOLICITACAO.codigo);
	}

	public Boolean isRenovacao() {
		return this.finalidade.equals(Finalidades.RENOVACAO.codigo);
	}

	public Boolean isAtualizacao() {
		return this.finalidade.equals(Finalidades.ATUALIZACAO.codigo);
	}

	public Boolean isDispensa() {
		return this.finalidade.equals(Finalidades.DISPENSA.codigo);
	}

	public Boolean isCadastro() {
		return this.finalidade.equals(Finalidades.CADASTRO.codigo);
	}

}
