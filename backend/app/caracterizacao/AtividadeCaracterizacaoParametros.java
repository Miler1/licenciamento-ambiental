package models.caracterizacao;

import exceptions.AppException;
import exceptions.ValidacaoException;
import models.Municipio;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import play.db.jpa.GenericModel;
import utils.Configuracoes;
import utils.GeoCalc;
import utils.Mensagem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "atividade_caracterizacao_parametros")
public class AtividadeCaracterizacaoParametros extends GenericModel {

	private static final String SEQ = "licenciamento.atividade_caracterizacao_parametros_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@ManyToOne
	@JoinColumn(name="id_atividade_caracterizacao", nullable = false)
	public AtividadeCaracterizacao atividadeCaracterizacao;

	@ManyToOne
	@JoinColumn(name="id_parametro_atividade", nullable = false)
	public ParametroAtividade parametroAtividade;

	@Column(name = "valor_parametro", nullable = false)
	public Double valorParametro;

}
