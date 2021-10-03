package models.caracterizacao;

import play.db.jpa.GenericModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "licenciamento", name = "tipo_sobreposicao")
public class TipoSobreposicao extends GenericModel {

	public static final long TERRA_INDIGENA = 1L;
	public static final long UC_FEDERAL = 2L;
	public static final long UC_ESTADUAL = 3L;
	public static final long UC_MUNICIPAL = 4L;
	public static final long TERRA_INDIGENA_ZA = 5L;
	public static final long TERRA_INDIGENA_ESTUDO = 6L;
	public static final long UC_FEDERAL_APA_DENTRO = 7L;
	public static final long UC_FEDERAL_APA_FORA = 8L;
	public static final long UC_FEDERAL_ZA = 9L;
	public static final long UC_ESTADUAL_PI_DENTRO = 10L;
	public static final long UC_ESTADUAL_PI_FORA = 11L;
	public static final long UC_ESTADUAL_ZA = 12L;
	public static final long TOMBAMENTO_ENCONTRO_AGUAS = 13L;
	public static final long TOMBAMENTO_ENCONTRO_AGUAS_ZA = 14L;
	public static final long AREAS_EMBARGADAS_IBAMA = 15L;
	public static final long AUTO_DE_INFRACAO_IBAMA = 16L;
	public static final long SAUIM_DE_COLEIRA = 17L;
	public static final long SITIOS_ARQUEOLOGICOS = 18L;
	public static final long UC_ESTADUAL_ZA_PI_FORA = 19L;
	public static final long BENS_ACAUTELADOS_IPHAN_PT = 20L;
	public static final long BENS_ACAUTELADOS_IPHAN_POL = 21L;

	@Id
	public Long id;

	@Column
	public String codigo;

	@Column
	public String nome;

	@Column(name = "coluna_nome")
	public String colunaNome;

	@Column(name = "coluna_data")
	public String colunaData;

	@Column(name = "coluna_cpf_cnpj")
	public String colunaCpfCnpj;

}