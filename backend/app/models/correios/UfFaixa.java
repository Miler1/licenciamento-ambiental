package models.correios;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import models.Estado;
import play.db.jpa.Model;

@Entity
@Table(name = "uf_faixa", schema = "correios")
public class UfFaixa extends Model{

	@ManyToOne
	@JoinColumn(name="cod_estado", referencedColumnName="cod_estado")
	public Estado estado;
	
	@Column(name="cep_inicio")
	public Integer cepInicio;
	
	@Column(name="cep_fim")
	public Integer cepFim;
	
}
