package models;

import models.caracterizacao.Atividade;
import models.caracterizacao.ParametroAtividade;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "licenciamento", name = "atividade_parametro_limites")
public class AtividadeParametroLimites extends GenericModel {

	@Id
	@Column(name = "id")
	public Long id;

	@OneToOne
	@JoinColumn(name="id_atividade")
	public Atividade atividade;

	@ManyToOne
	@JoinColumn(name="id_parametro_atividade")
	public ParametroAtividade parametroAtividade;

	@Column(name="limite_inferior")
	public Double limiteInferior;
	
	@Column(name="limite_superior")
	public Double limiteSuperior;

}
