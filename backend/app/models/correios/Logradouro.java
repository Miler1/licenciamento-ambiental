package models.correios;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "logradouro", schema = "correios")
public class Logradouro extends Model {
	
	public String nome;
	
	public Integer cep;
	
	@ManyToOne
	@JoinColumn(name="id_bairro", referencedColumnName="id")
	public Bairro bairro;

	@ManyToOne
	@JoinColumn(name="id_localidade", referencedColumnName="ID")
	public Localidade localidade;
	
}
