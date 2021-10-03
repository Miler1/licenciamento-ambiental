package models.correios;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import models.Municipio;
import play.db.jpa.Model;

@Entity
@Table(name = "localidade", schema = "correios")
public class Localidade extends Model {
	
	@Column(name="tipo_localidade")
	public String tipoLocalidade;
	 
	public Integer cep;
	
	@ManyToOne
	@JoinColumn(name="cod_ibge", referencedColumnName="id_municipio")
	public Municipio municipio;
	
	@ManyToOne
	@JoinColumn(name="id_pai", referencedColumnName="id")
	public Localidade localidadePai;
	
	public String nome;
	
}
