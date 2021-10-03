package models.correios;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "bairro", schema = "correios")
public class Bairro extends Model {
	
	public String nome;
	
	@ManyToOne
	@JoinColumn(name="id_localidade", referencedColumnName="id")
	public Localidade localidade;
	
}
