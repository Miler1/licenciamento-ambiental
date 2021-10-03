package models.caracterizacao;

import play.db.jpa.GenericModel;
import utils.Identificavel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "atividade_cnae")
public class AtividadeCnae extends GenericModel implements Identificavel {

	@Id
	public Long id;

	public String nome;

	public String codigo;

	@Transient
	public List<Atividade> atividades;

	@Transient
	public boolean dispensaLicenciamento;

	@Transient
	public boolean licenciamentoSimplificado;

	@Transient
	public boolean licenciamentoDeclaratorio;

	@Override
	public Long getId() {

		return this.id;
	}

	public static List<AtividadeCnae> findAllWithIds(List<Long> ids) {

		return AtividadeCnae.find("id in (:ids)").setParameter("ids", ids).fetch();
	}

	public static List<AtividadeCnae> findAllWithCodigos(List<String> codigosCnaes) {

		return AtividadeCnae.find("codigo in (:codigos)").setParameter("codigos", codigosCnaes).fetch();
	}
}
