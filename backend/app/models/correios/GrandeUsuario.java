package models.correios;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "grande_usuario", schema = "correios")
public class GrandeUsuario extends Model {
	
	public Integer cep;
	
	@ManyToOne
	@JoinColumn(name="id_logradouro", referencedColumnName="id")
	public Logradouro logradouro;
	
}
